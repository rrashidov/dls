package org.roko.dls.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

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

public class DLSClientTest {

    private static final String LOCK_API_PATH = "/api/v1/lock/";

    private static final String TEST_LOCK_ID = "test-lock-id";

    @Mock
    private Supplier<RestTemplate> restTemplateSupplierMock;

    @Mock
    private RestTemplate restTemplateMock;

    private DLSClient client;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(restTemplateSupplierMock.get()).thenReturn(restTemplateMock);

        client = new DLSClient(restTemplateSupplierMock);
    }

    @Test
    public void lockReturnsOK_whenDLSReturns200OK() {
        when(restTemplateMock.postForEntity(LOCK_API_PATH + TEST_LOCK_ID, TEST_LOCK_ID, String.class))
                .thenReturn(ResponseEntity.ok().build());

        LockResult lockResult = client.lock(TEST_LOCK_ID);

        assertEquals(LockResult.OK, lockResult);
    }

    @Test
    public void lockReturnsAlreadyLocked_whenDLSReturns400BadRequest() {
        when(restTemplateMock.postForEntity(LOCK_API_PATH + TEST_LOCK_ID, TEST_LOCK_ID, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(400)));

        LockResult lockResult = client.lock(TEST_LOCK_ID);

        assertEquals(LockResult.ALREADY_LOCKED, lockResult);
    }

    @Test
    public void lockReturnsLockFailed_whenDLSReturns500InternalServerError() {
        when(restTemplateMock.postForEntity(LOCK_API_PATH + TEST_LOCK_ID, TEST_LOCK_ID, String.class))
                .thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        LockResult lockResult = client.lock(TEST_LOCK_ID);

        assertEquals(LockResult.LOCK_FAILED, lockResult);
    }

    @Test
    public void unlockReturnsOK_whenDLSReturns200OK(){
        when(restTemplateMock.exchange(LOCK_API_PATH + TEST_LOCK_ID, HttpMethod.DELETE, null, String.class))
                .thenReturn(ResponseEntity.ok().build());

        UnlockResult unlockResult = client.unlock(TEST_LOCK_ID);

        assertEquals(UnlockResult.OK, unlockResult);
    }

    @Test
    public void unlockReturnsUnlockFailed_whenDLSReturns500InternalServerError(){
        when(restTemplateMock.exchange(LOCK_API_PATH + TEST_LOCK_ID, HttpMethod.DELETE, null, String.class))
                .thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        UnlockResult unlockResult = client.unlock(TEST_LOCK_ID);

        assertEquals(UnlockResult.UNLOCK_FAILED, unlockResult);
    }
}
