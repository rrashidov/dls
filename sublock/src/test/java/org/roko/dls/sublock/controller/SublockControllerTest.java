package org.roko.dls.sublock.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.sublock.service.SublockLockResult;
import org.roko.dls.sublock.service.SublockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SublockControllerTest {

    @Mock
    private SublockService svcMock;

    private SublockController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        controller = new SublockController(svcMock);
    }

    @Test
    public void okResponseIsReturned_whenLockSucceeds(){
        // given
        when(svcMock.lock("test-lock-id")).thenReturn(SublockLockResult.OK);

        // when
        ResponseEntity<String> responseEntity = controller.lock("test-lock-id");

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response status code should be equal to 200");
    }

    @Test
    public void badRequestResponseIsReturned_whenLockAlreadyLocked() {
        // given
        when(svcMock.lock("test-lock-id")).thenReturn(SublockLockResult.ALREADY_LOCKED);

        // when
        ResponseEntity<String> responseEntity = controller.lock("test-lock-id");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "Response status code should be equal to 400");
    }

    @Test
    public void internalServerErrorIsReturned_whenLockFails() {
        // given
        when(svcMock.lock("test-lock-id")).thenReturn(SublockLockResult.FAILED);

        // when
        ResponseEntity<String> responseEntity = controller.lock("test-lock-id");

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "Response status code should be equal to 500");
    }
}
