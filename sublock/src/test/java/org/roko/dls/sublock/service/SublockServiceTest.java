package org.roko.dls.sublock.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.sublock.dto.SublockRequest;
import org.roko.dls.sublock.model.Sublock;
import org.roko.dls.sublock.repository.SublockRepository;
import org.roko.dls.sublock.service.util.SublockDateUtil;

public class SublockServiceTest {

    private static final String TEST_LOCK_ID = "test-lock-id";

    private static final int TEST_DATEFLAG = 20210101;
    private static final long TEST_TIMESTAMP = System.currentTimeMillis();

    @Captor
    private ArgumentCaptor<Sublock> sublockCaptor;

    @Mock
    private SublockRepository repoMock;

    @Mock
    private SublockDateUtil utilMock;

    private SublockRequest sublockRequest;

    private SublockService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        sublockRequest = new SublockRequest();
        sublockRequest.setId(TEST_LOCK_ID);
        sublockRequest.setTimestamp(TEST_TIMESTAMP);

        when(utilMock.getDateFlag()).thenReturn(TEST_DATEFLAG);

        svc = new SublockService(repoMock, utilMock);
    }

    @Test
    public void lockByRequestSucceeds_whenLockDoesNotExist() {
        // given
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.empty());

        // when
        SublockLockResult lockResult = svc.lock(sublockRequest);

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock createdSublock = sublockCaptor.getValue();

        assertEquals(TEST_LOCK_ID, createdSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(true, createdSublock.isLocked(), "Sublock should be locked");
        assertEquals(TEST_TIMESTAMP, createdSublock.getTimestamp(), "Sublock timestamp should be equal to " + TEST_TIMESTAMP);
        assertEquals(TEST_DATEFLAG, createdSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockLockResult.OK, lockResult, "Lock result should be OK when lock does not exist");
    }

    @Test
    public void lockSucceeds_whenLockDoesNotExist(){
        // given
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.empty());

        // when
        SublockLockResult lockResult = svc.lock(TEST_LOCK_ID);

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock createdSublock = sublockCaptor.getValue();

        assertEquals(TEST_LOCK_ID, createdSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(true, createdSublock.isLocked(), "Sublock should be locked");
        assertNotEquals(0, createdSublock.getTimestamp(), "Sublock timestamp should not be equal to 0");
        assertEquals(TEST_DATEFLAG, createdSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockLockResult.OK, lockResult, "Lock result should be OK when lock does not exist");
    }

    @Test
    public void lockByRequestFails_whenLockAlreadyLocked() {
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId(TEST_LOCK_ID);
        existingSublock.setLocked(true);
        existingSublock.setTimestamp(TEST_TIMESTAMP);
        existingSublock.setDateFlag(TEST_DATEFLAG);
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.of(existingSublock));

        // when
        SublockLockResult lockResult = svc.lock(sublockRequest);

        // then
        verify(repoMock).findById(TEST_LOCK_ID);

        assertEquals(SublockLockResult.ALREADY_LOCKED, lockResult, "Lock result should be ALREADY_LOCKED when lock already exists");
    }

    @Test
    public void lockFails_whenLockAlreadyLocked(){
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId(TEST_LOCK_ID);
        existingSublock.setLocked(true);
        existingSublock.setTimestamp(TEST_TIMESTAMP);
        existingSublock.setDateFlag(TEST_DATEFLAG);
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.of(existingSublock));

        // when
        SublockLockResult lockResult = svc.lock(TEST_LOCK_ID);

        // then
        verify(repoMock).findById(TEST_LOCK_ID);

        assertEquals(SublockLockResult.ALREADY_LOCKED, lockResult, "Lock result should be ALREADY_LOCKED when lock already exists");
    }

    @Test
    public void lockSucceeds_whenLockExistsButIsNotLocked(){
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId(TEST_LOCK_ID);
        existingSublock.setLocked(false);
        existingSublock.setTimestamp(TEST_TIMESTAMP);
        existingSublock.setDateFlag(TEST_DATEFLAG);
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.of(existingSublock));

        // when
        SublockLockResult lockResult = svc.lock(TEST_LOCK_ID);

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock updatedSublock = sublockCaptor.getValue();

        assertEquals(TEST_LOCK_ID, updatedSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(true, updatedSublock.isLocked(), "Sublock should be locked");
        assertNotEquals(0, updatedSublock.getTimestamp(), "Sublock timestamp should not be equal to 0");
        assertEquals(TEST_DATEFLAG, updatedSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockLockResult.OK, lockResult, "Lock result should be OK when lock exists but is not locked");
    }

    @Test
    public void lockByRequestSucceeds_whenLockExistsButIsNotLocked(){
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId(TEST_LOCK_ID);
        existingSublock.setLocked(false);
        existingSublock.setTimestamp(TEST_TIMESTAMP);
        existingSublock.setDateFlag(TEST_DATEFLAG);
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.of(existingSublock));

        // when
        SublockLockResult lockResult = svc.lock(sublockRequest);

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock updatedSublock = sublockCaptor.getValue();

        assertEquals(TEST_LOCK_ID, updatedSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(true, updatedSublock.isLocked(), "Sublock should be locked");
        assertEquals(TEST_TIMESTAMP, updatedSublock.getTimestamp(), "Sublock timestamp should be equal to " + TEST_TIMESTAMP);
        assertEquals(TEST_DATEFLAG, updatedSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockLockResult.OK, lockResult, "Lock result should be OK when lock exists but is not locked");
    }

    @Test
    public void unlockSucceeds_whenLockDoesNotExist() {
        // given
        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.empty());

        // when
        SublockUnlockResult lockResult = svc.unlock(TEST_LOCK_ID);

        // then
        assertEquals(SublockUnlockResult.OK, lockResult, "Unlock result should be OK when lock does not exist");
    }

    @Test
    public void unlockMarksSublockEntityAsUnlocked_whenSublockEntityExistsAndIsLocked() {
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId(TEST_LOCK_ID);
        existingSublock.setLocked(true);
        existingSublock.setTimestamp(TEST_TIMESTAMP);
        existingSublock.setDateFlag(TEST_DATEFLAG);

        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.of(existingSublock));

        // when
        SublockUnlockResult unlockResult = svc.unlock(TEST_LOCK_ID);

        // then
        verify(repoMock).save(sublockCaptor.capture());
        Sublock updatedSublock = sublockCaptor.getValue();

        assertEquals(TEST_LOCK_ID, updatedSublock.getId(), "Sublock id should be equal to 'test-lock-id'");
        assertEquals(false, updatedSublock.isLocked(), "Sublock should be unlocked");
        assertNotEquals(0, updatedSublock.getTimestamp(), "Sublock timestamp should not be equal to 0");
        assertEquals(TEST_DATEFLAG, updatedSublock.getDateFlag(), "Sublock date flag should be equal to " + TEST_DATEFLAG);

        assertEquals(SublockUnlockResult.OK, unlockResult, "Unlock result should be OK when sublock entity exists");
    }

    @Test
    public void unlockDoesNothing_whenSublockEntityExistsAndIsUnlocked(){
        // given
        Sublock existingSublock = new Sublock();
        existingSublock.setId(TEST_LOCK_ID);
        existingSublock.setLocked(false);
        existingSublock.setTimestamp(TEST_TIMESTAMP);
        existingSublock.setDateFlag(TEST_DATEFLAG);

        when(repoMock.findById(TEST_LOCK_ID)).thenReturn(Optional.of(existingSublock));

        // when
        SublockUnlockResult unlockResult = svc.unlock(TEST_LOCK_ID);

        // then
        verify(repoMock).findById(TEST_LOCK_ID);
        verify(repoMock, never()).save(any(Sublock.class));

        assertEquals(SublockUnlockResult.OK, unlockResult, "Unlock result should be OK when sublock entity exists");
    }
}
