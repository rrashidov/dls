package org.roko.dls.api.sublockclient.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.roko.dls.api.lockclient.LockResultEnum;
import org.roko.dls.api.lockclient.UnlockResultEnum;
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
        UnlockResultEnum lockResult = UnlockResultEnum.OK;
        UnlockResultEnum lockresult2 = UnlockResultEnum.OK;
        UnlockResultEnum lockresult3 = UnlockResultEnum.UNLOCK_FAILED;

        // when
        UnlockResultEnum result = policy.inspectUnlockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(UnlockResultEnum.OK, result);
    }

    @Test
    public void inspectUnlockResultsReturnsUnlockFailed_whenQuorumOfResultsAreUnlockFailed(){
        // given
        UnlockResultEnum lockResult = UnlockResultEnum.UNLOCK_FAILED;
        UnlockResultEnum lockresult2 = UnlockResultEnum.UNLOCK_FAILED;
        UnlockResultEnum lockresult3 = UnlockResultEnum.OK;

        // when
        UnlockResultEnum result = policy.inspectUnlockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(UnlockResultEnum.UNLOCK_FAILED, result);
    }
}
