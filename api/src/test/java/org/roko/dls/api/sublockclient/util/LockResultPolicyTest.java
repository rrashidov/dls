package org.roko.dls.api.sublockclient.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.roko.dls.api.lockclient.LockResult;
import org.roko.dls.api.lockclient.UnlockResult;
import org.roko.dls.api.lockclient.util.LockResultPolicy;

public class LockResultPolicyTest {

    private LockResultPolicy policy;

    @BeforeEach
    public void setup(){
        policy = new LockResultPolicy();
    }

    @Test
    public void inspecReturnsOK_whenQuorumOfResultsAreOK(){
        // given
        LockResult lockResult = LockResult.OK;
        LockResult lockresult2 = LockResult.OK;
        LockResult lockresult3 = LockResult.LOCK_FAILED;

        // when
        LockResult result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResult.OK, result);
    }

    @Test
    public void inspectReturnsAlreadyLocked_whenQuorumOfResultsAreAlreadyLocked(){
        // given
        LockResult lockResult = LockResult.OK;
        LockResult lockresult2 = LockResult.ALREADY_LOCKED;
        LockResult lockresult3 = LockResult.ALREADY_LOCKED;

        // when
        LockResult result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResult.ALREADY_LOCKED, result);
    }

    @Test
    public void inspectReturnsLockFailed_whenQuorumOfResultsAreLockFailed(){
        // given
        LockResult lockResult = LockResult.LOCK_FAILED;
        LockResult lockresult2 = LockResult.LOCK_FAILED;
        LockResult lockresult3 = LockResult.OK;

        // when
        LockResult result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResult.LOCK_FAILED, result);
    }

    @Test
    public void inspectReturnsLockFailed_whenQuorumOfResultsAreNotOK(){
        // given
        LockResult lockResult = LockResult.LOCK_FAILED;
        LockResult lockresult2 = LockResult.ALREADY_LOCKED;
        LockResult lockresult3 = LockResult.OK;

        // when
        LockResult result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResult.LOCK_FAILED, result);
    }

    @Test
    public void inspectUnlockResultsReturnsOK_whenQuorumOfResultsAreOK(){
        // given
        UnlockResult lockResult = UnlockResult.OK;
        UnlockResult lockresult2 = UnlockResult.OK;
        UnlockResult lockresult3 = UnlockResult.UNLOCK_FAILED;

        // when
        UnlockResult result = policy.inspectUnlockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(UnlockResult.OK, result);
    }

    @Test
    public void inspectUnlockResultsReturnsUnlockFailed_whenQuorumOfResultsAreUnlockFailed(){
        // given
        UnlockResult lockResult = UnlockResult.UNLOCK_FAILED;
        UnlockResult lockresult2 = UnlockResult.UNLOCK_FAILED;
        UnlockResult lockresult3 = UnlockResult.OK;

        // when
        UnlockResult result = policy.inspectUnlockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(UnlockResult.UNLOCK_FAILED, result);
    }
}
