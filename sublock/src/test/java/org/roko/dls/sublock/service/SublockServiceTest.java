package org.roko.dls.sublock.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.sublock.model.Sublock;
import org.roko.dls.sublock.repository.SublockRepository;
import org.roko.dls.sublock.service.util.SublockDateUtil;

public class SublockServiceTest {

    private static final int TEST_DATEFLAG = 20210101;

    @Captor
    private ArgumentCaptor<Sublock> sublockCaptor;

    @Mock
    private SublockRepository repoMock;

    @Mock
    private SublockDateUtil utilMock;

    private SublockService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(utilMock.getDateFlag()).thenReturn(TEST_DATEFLAG);

        svc = new SublockService(repoMock, utilMock);
    }

    @Test
    public void lockSucceeds_whenLockDoesNotExist(){
        // given
        when(repoMock.findById("test-lock-id")).thenReturn(Optional.empty());

        // when
        SublockLockResult lockResult = svc.lock("test-lock-id");

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock createdSublock = sublockCaptor.getValue();

        assertEquals("test-lock-id", createdSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(true, createdSublock.isLocked(), "Sublock should be locked");
        assertNotEquals(0, createdSublock.getTimestamp(), "Sublock timestamp should not be equal to 0");
        assertEquals(TEST_DATEFLAG, createdSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockLockResult.OK, lockResult, "Lock result should be OK when lock does not exist");
    }

    @Test
    public void lockFails_whenLockAlreadyLocked(){
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId("test-lock-id");
        existingSublock.setLocked(true);
        existingSublock.setTimestamp(System.currentTimeMillis());
        existingSublock.setDateFlag(TEST_DATEFLAG);
        when(repoMock.findById("test-lock-id")).thenReturn(Optional.of(existingSublock));

        // when
        SublockLockResult lockResult = svc.lock("test-lock-id");

        // then
        verify(repoMock).findById("test-lock-id");

        assertEquals(SublockLockResult.ALREADY_LOCKED, lockResult, "Lock result should be ALREADY_LOCKED when lock already exists");
    }

    @Test
    public void lockSucceeds_whenLockExistsButIsNotLocked(){
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId("test-lock-id");
        existingSublock.setLocked(false);
        existingSublock.setTimestamp(System.currentTimeMillis());
        existingSublock.setDateFlag(TEST_DATEFLAG);
        when(repoMock.findById("test-lock-id")).thenReturn(Optional.of(existingSublock));

        // when
        SublockLockResult lockResult = svc.lock("test-lock-id");

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock updatedSublock = sublockCaptor.getValue();

        assertEquals("test-lock-id", updatedSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(true, updatedSublock.isLocked(), "Sublock should be locked");
        assertNotEquals(0, updatedSublock.getTimestamp(), "Sublock timestamp should not be equal to 0");
        assertEquals(TEST_DATEFLAG, updatedSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockLockResult.OK, lockResult, "Lock result should be OK when lock exists but is not locked");
    }
    
}
