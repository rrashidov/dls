package org.roko.dls.api.sublock.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class RESTSublockClientTest {

    private static final String SUBLOCK_URL = "/api/v1/sublock/";

    private static final String TEST_ID = "test-id";

    @Mock
    private RestTemplate restTemplateMock;

    private SublockClient client;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        client = new RESTSublockClient(restTemplateMock);
    }

    @Test
    public void lockReturnsOK_whenServerReturns200() {
        when(restTemplateMock.postForEntity(SUBLOCK_URL + TEST_ID, TEST_ID, String.class))
                .thenReturn(ResponseEntity.ok().build());

        LockResult lockResult = client.lock(TEST_ID);

        assertEquals(LockResult.OK, lockResult);
    }

    @Test
    public void lockReturnsAlreadyLocked_whenServerReturns4xx() {
        when(restTemplateMock.postForEntity(SUBLOCK_URL + TEST_ID, TEST_ID, String.class))
                .thenReturn(ResponseEntity.badRequest().build());

        LockResult lockResult = client.lock(TEST_ID);

        assertEquals(LockResult.ALREADY_LOCKED, lockResult);
    }

    @Test
    public void lockReturnsLockFailed_whenServerReturns5xx() {
        when(restTemplateMock.postForEntity(SUBLOCK_URL + TEST_ID, TEST_ID, String.class))
                .thenReturn(ResponseEntity.status(500).build());

        LockResult lockResult = client.lock(TEST_ID);

        assertEquals(LockResult.LOCK_FAILED, lockResult);
    }

    @Test
    public void lockReturnsAlreadyLocked_whenClientThrowsHttpClientErrorException() {
        when(restTemplateMock.postForEntity(SUBLOCK_URL + TEST_ID, TEST_ID, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(400)));

        LockResult lockResult = client.lock(TEST_ID);

        assertEquals(LockResult.ALREADY_LOCKED, lockResult);
    }

    @Test
    public void lockReturnsLockFailed_whenClientThrowsHttpServerErrorException(){
        when(restTemplateMock.postForEntity(SUBLOCK_URL + TEST_ID, TEST_ID, String.class))
                .thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        LockResult lockResult = client.lock(TEST_ID);

        assertEquals(LockResult.LOCK_FAILED, lockResult);
    }

    @Test
    public void unlockReturnsOK_whenServerReturns200() {
        when(restTemplateMock.exchange(SUBLOCK_URL + TEST_ID, HttpMethod.DELETE, null, String.class))
                .thenReturn(ResponseEntity.ok().build());

        UnlockResult unlockResult = client.unlock(TEST_ID);

        assertEquals(UnlockResult.OK, unlockResult);
    }

    @Test
    public void unlockReturnsUnlockFailed_whenServerReturns5xx() {
        when(restTemplateMock.exchange(SUBLOCK_URL + TEST_ID, HttpMethod.DELETE, null, String.class))
                .thenReturn(ResponseEntity.status(500).build());

        UnlockResult unlockResult = client.unlock(TEST_ID);

        assertEquals(UnlockResult.UNLOCK_FAILED, unlockResult);
    }

    @Test
    public void unlockReturnsUnlockFailed_whenClientThrowsHttpServerErrorException(){
        when(restTemplateMock.exchange(SUBLOCK_URL + TEST_ID, HttpMethod.DELETE, null, String.class))
                .thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        UnlockResult unlockResult = client.unlock(TEST_ID);

        assertEquals(UnlockResult.UNLOCK_FAILED, unlockResult);
    }
}
