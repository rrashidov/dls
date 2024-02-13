package org.roko.dls.api.sublockclient;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.sublockclient.exc.AlreadyLockedException;
import org.roko.dls.api.sublockclient.exc.LockFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class SublockClientTest {

    @Mock
    private RestTemplate restTemplateMock;

    private SublockClient sublockClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        sublockClient = new SublockClient("test-id", restTemplateMock);
    }

    @Test
    public void lockSendsProperRequest() throws AlreadyLockedException, LockFailedException {
        sublockClient.lock("test-id");

        verify(restTemplateMock).postForObject("/api/v1/sublock/test-id", null, String.class);
    }

    @Test
    public void lockThrowsAlreadyLockedException_whenBackendReturns4xxResponse(){
        // given
        when(restTemplateMock.postForObject("/api/v1/sublock/test-id", null, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // when - then
        assertThrows(AlreadyLockedException.class, () -> sublockClient.lock("test-id"));
    }

    @Test
    public void lockThrowsLockFailedException_whenBackendReturns5xxResponse(){
        // given
        when(restTemplateMock.postForObject("/api/v1/sublock/test-id", null, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // when - then
        assertThrows(LockFailedException.class, () -> sublockClient.lock("test-id"));
    }

    @Test
    public void lockThrowsLockFailedException_whenBackendThrowsRestException(){
        // given
        when(restTemplateMock.postForObject("/api/v1/sublock/test-id", null, String.class))
            .thenThrow(new RestClientException(""));

        // when - then
        assertThrows(LockFailedException.class, () -> sublockClient.lock("test-id"));
    }

    @Test
    public void unlockSendsProperRequest() throws LockFailedException {
        sublockClient.unlock("test-id");

        verify(restTemplateMock).delete("/api/v1/sublock/test-id");
    }

    @Test
    public void unlockFails_whenBackendReturns5xxResponse(){
        // given
        doThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(restTemplateMock)
                .delete("/api/v1/sublock/test-id");

        // when - then
        assertThrows(LockFailedException.class, () -> sublockClient.unlock("test-id"));
    }
}
