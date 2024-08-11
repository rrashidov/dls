package org.roko.dls.sublock.svc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.sublock.domain.LockResult;
import org.roko.dls.sublock.domain.Sublock;
import org.roko.dls.sublock.domain.UnlockResult;
import org.roko.dls.sublock.repo.SublockRepo;
import org.springframework.dao.QueryTimeoutException;

public class SublockServiceTest {

    private static final String TEST_ID = "test-id";

    @Mock
    private SublockRepo repoMock;

    private SublockService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new SublockService(repoMock);
    }

    @Test
    public void lockSucceeds_whenSublockEntityDoesNotExist() {
        when(repoMock.findById(TEST_ID)).thenReturn(Optional.empty());

        LockResult lockResult = svc.lock(TEST_ID);

        assertEquals(LockResult.OK, lockResult, "Lock should succeed, when entity does not exist");
    }

    @Test
    public void lockReturnsAlreadyLocked_whenSublockEntityExists(){
        when(repoMock.findById(TEST_ID)).thenReturn(Optional.of(new Sublock()));

        LockResult lockResult = svc.lock(TEST_ID);

        assertEquals(LockResult.ALREADY_LOCKED, lockResult, "Lock should return ALREADY_LOCKED, when entity exists");
    }

    @Test
    public void lockReturnsLockFailed_whenIssuesWithDBCommunication(){
        when(repoMock.findById(TEST_ID)).thenThrow(QueryTimeoutException.class);

        LockResult lockResult = svc.lock(TEST_ID);

        assertEquals(LockResult.LOCK_FAILED, lockResult, "Lock should return LOCK_FAILED, when issues with DB communication");
    }

    @Test
    public void unlockSucceeds_whenSublockEntityDoesNotExist(){
        when(repoMock.findById(TEST_ID)).thenReturn(Optional.empty());

        UnlockResult lockResult = svc.unlock(TEST_ID);

        assertEquals(UnlockResult.OK, lockResult, "Unlock should succeed, when entity does not exist");
    }

    @Test
    public void unlockSucceeds_whenSublockEntityExists(){
        Sublock sublock = new Sublock();
        when(repoMock.findById(TEST_ID)).thenReturn(Optional.of(sublock));

        UnlockResult lockResult = svc.unlock(TEST_ID);

        assertEquals(UnlockResult.OK, lockResult, "Unlock should succeed, when entity exists");
        verify(repoMock).delete(sublock);
    }

    @Test
    public void unlockFails_whenIssuesWithDBCommunication(){
        when(repoMock.findById(TEST_ID)).thenThrow(QueryTimeoutException.class);

        UnlockResult lockResult = svc.unlock(TEST_ID);

        assertEquals(UnlockResult.UNLOCK_FAILED, lockResult, "Unlock should return UNLOCK_FAILED, when issues with DB communication");
    }
}
