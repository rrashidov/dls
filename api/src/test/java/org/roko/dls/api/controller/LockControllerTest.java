package org.roko.dls.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.service.LockService;
import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.UnlockResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LockControllerTest {

    private static final String TEST_LOCK_ID = "test-lock-id";

    private static final LockResult LOCK_RESULT_OK = LockResult.OK;
    private static final LockResult LOCK_RESULT_ALREADY_LOCKED = LockResult.ALREADY_LOCKED;

    @Mock
    private LockService svcMock;

    private LockController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        controller = new LockController(svcMock);
    }

    @Test
    public void lockReturns200OK_whenLockSucceeds(){
        when(svcMock.lock(TEST_LOCK_ID)).thenReturn(LOCK_RESULT_OK);

        ResponseEntity<String> response = controller.lock(TEST_LOCK_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void lockReturns400BadRequest_whenAlreadyLocked(){
        when(svcMock.lock(TEST_LOCK_ID)).thenReturn(LOCK_RESULT_ALREADY_LOCKED);

        ResponseEntity<String> response = controller.lock(TEST_LOCK_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void lockReturns500InternalServerError_whenLockFails(){
        when(svcMock.lock(TEST_LOCK_ID)).thenReturn(LockResult.LOCK_FAILED);

        ResponseEntity<String> response = controller.lock(TEST_LOCK_ID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void unlockReturns200OK_whenUnlockOK(){
        when(svcMock.unlock(TEST_LOCK_ID)).thenReturn(UnlockResult.OK);

        ResponseEntity<String> response = controller.unlock(TEST_LOCK_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void unlockReturns500InternalServerError_whenUnlockFails(){
        when(svcMock.unlock(TEST_LOCK_ID)).thenReturn(UnlockResult.UNLOCK_FAILED);

        ResponseEntity<String> response = controller.unlock(TEST_LOCK_ID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
