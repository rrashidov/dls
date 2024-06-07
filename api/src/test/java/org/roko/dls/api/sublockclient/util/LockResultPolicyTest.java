package org.roko.dls.api.sublockclient.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.roko.dls.api.lockclient.LockResultEnum;
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
        LockResultEnum lockResult = LockResultEnum.OK;
        LockResultEnum lockresult2 = LockResultEnum.OK;
        LockResultEnum lockresult3 = LockResultEnum.LOCK_FAILED;

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.OK, result);
    }

    @Test
    public void inspectReturnsAlreadyLocked_whenQuorumOfResultsAreAlreadyLocked(){
        // given
        LockResultEnum lockResult = LockResultEnum.OK;
        LockResultEnum lockresult2 = LockResultEnum.ALREADY_LOCKED;
        LockResultEnum lockresult3 = LockResultEnum.ALREADY_LOCKED;

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.ALREADY_LOCKED, result);
    }

    @Test
    public void inspectReturnsLockFailed_whenQuorumOfResultsAreLockFailed(){
        // given
        LockResultEnum lockResult = LockResultEnum.LOCK_FAILED;
        LockResultEnum lockresult2 = LockResultEnum.LOCK_FAILED;
        LockResultEnum lockresult3 = LockResultEnum.OK;

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.LOCK_FAILED, result);
    }

    @Test
    public void inspectReturnsLockFailed_whenQuorumOfResultsAreNotOK(){
        // given
        LockResultEnum lockResult = LockResultEnum.LOCK_FAILED;
        LockResultEnum lockresult2 = LockResultEnum.ALREADY_LOCKED;
        LockResultEnum lockresult3 = LockResultEnum.OK;

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.LOCK_FAILED, result);
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
