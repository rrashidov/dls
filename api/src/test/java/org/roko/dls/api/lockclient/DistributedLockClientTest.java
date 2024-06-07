package org.roko.dls.api.lockclient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.sublockclient.SublockClient;
import org.roko.dls.api.sublockclient.exc.AlreadyLockedException;
import org.roko.dls.api.sublockclient.exc.LockFailedException;

public class DistributedLockClientTest {

    private static final String TEST_ID = "test-id";

    @Mock
    private SublockClient subLockClient1;

    @Mock
    private SublockClient subLockClient2;

    @Mock
    private SublockClient subLockClient3;

    private DistributedLockClient lockClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lockClient = new DistributedLockClient(Arrays.asList(subLockClient1, subLockClient2, subLockClient3));
    }

    @Test
    public void lockReturnsAllOKResults_whenAllSubclientsSucceed(){
        // given
        // none of the sublock clients throw any exceptions

        // when
        List<LockResult> lockResults = lockClient.lock(TEST_ID);

        // then
        assertFalse(lockResultsContains(lockResults, LockResultEnum.LOCK_FAILED));
        assertFalse(lockResultsContains(lockResults, LockResultEnum.ALREADY_LOCKED));
    }

    @Test
    public void lockReturnsAllFailedResults_whenAllSubclientsFail() throws AlreadyLockedException, LockFailedException{
        // given
        doThrow(new LockFailedException()).when(subLockClient1).lock(TEST_ID);
        doThrow(new LockFailedException()).when(subLockClient2).lock(TEST_ID);
        doThrow(new LockFailedException()).when(subLockClient3).lock(TEST_ID);

        // when
        List<LockResult> lockResults = lockClient.lock(TEST_ID);

        // then
        assertFalse(lockResultsContains(lockResults, LockResultEnum.OK));
        assertFalse(lockResultsContains(lockResults, LockResultEnum.ALREADY_LOCKED));
    }

    @Test
    public void lockReturnsAllAlreadyLockedResults_whenAllSubclientsReturnAlreadyLocked() throws AlreadyLockedException, LockFailedException{
        // given
        doThrow(new AlreadyLockedException()).when(subLockClient1).lock(TEST_ID);
        doThrow(new AlreadyLockedException()).when(subLockClient2).lock(TEST_ID);
        doThrow(new AlreadyLockedException()).when(subLockClient3).lock(TEST_ID);

        // when
        List<LockResult> lockResults = lockClient.lock(TEST_ID);

        // then
        assertFalse(lockResultsContains(lockResults, LockResultEnum.OK));
        assertFalse(lockResultsContains(lockResults, LockResultEnum.LOCK_FAILED));
    }

    @Test
    public void unlockReturnsAllOKResults_whenAllSubclientsSucceed(){
        // given
        // none of the sublock clients throw any exceptions

        // when
        List<UnlockResultEnum> unlockResults = lockClient.unlock(TEST_ID);

        // then
        assertFalse(unlockResults.contains(UnlockResultEnum.UNLOCK_FAILED));
    }

    @Test
    public void unlockReturnsAllFailedResults_whenAllSubclientsFail() throws AlreadyLockedException, LockFailedException{
        // given
        doThrow(new LockFailedException()).when(subLockClient1).unlock(TEST_ID);
        doThrow(new LockFailedException()).when(subLockClient2).unlock(TEST_ID);
        doThrow(new LockFailedException()).when(subLockClient3).unlock(TEST_ID);

        // when
        List<UnlockResultEnum> unlockResults = lockClient.unlock(TEST_ID);

        // then
        assertFalse(unlockResults.contains(UnlockResultEnum.OK));
    }

    private boolean lockResultsContains(List<LockResult> lockResults, LockResultEnum lockResult) {
        return lockResults.stream()
            .map(x -> x.getResult())
            .anyMatch(x -> x.equals(lockResult));
    }

}
