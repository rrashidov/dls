package org.roko.dls.sublock.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.sublock.domain.LockResult;
import org.roko.dls.sublock.domain.UnlockResult;
import org.roko.dls.sublock.svc.SublockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SublockControllerTest {

    private static final String TEST_ID = "test-id";

    @Mock
    private SublockService svcMock;

    private SublockController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        controller = new SublockController(svcMock);
    }

    @Test
    public void lockReturnsHttpOK_whenLockSucceeds() {
        when(svcMock.lock(TEST_ID)).thenReturn(LockResult.OK);

        ResponseEntity<String> response = controller.lock(TEST_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK, when lock succeeds");
    }

    @Test
    public void lockReturnsHttpBadRequest_whenAlreadyLocked() {
        when(svcMock.lock(TEST_ID)).thenReturn(LockResult.ALREADY_LOCKED);

        ResponseEntity<String> response = controller.lock(TEST_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response should be BAD_REQUEST, when already locked");
    }

    @Test
    public void lockReturnsHttpInternalServerError_whenLockFails() {
        when(svcMock.lock(TEST_ID)).thenReturn(LockResult.LOCK_FAILED);

        ResponseEntity<String> response = controller.lock(TEST_ID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Response should be INTERNAL_SERVER_ERROR, when lock fails");
    }

    @Test
    public void unlockReturnsHttpOK_whenUnlockSucceeds() {
        when(svcMock.unlock(TEST_ID)).thenReturn(UnlockResult.OK);

        ResponseEntity<String> response = controller.unlock(TEST_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK, when unlock succeeds");
    }

    @Test
    public void unlockReturnsHttpInternalServerError_whenUnlockFails() {
        when(svcMock.unlock(TEST_ID)).thenReturn(UnlockResult.UNLOCK_FAILED);

        ResponseEntity<String> response = controller.unlock(TEST_ID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Response should be INTERNAL_SERVER_ERROR, when unlock fails");
    }
}
