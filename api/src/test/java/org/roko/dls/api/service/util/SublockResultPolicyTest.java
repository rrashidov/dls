package org.roko.dls.api.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.UnlockResult;

public class SublockResultPolicyTest {

    private SublockResultPolicy policy;

    @BeforeEach
    public void setup(){
        policy = new SublockResultPolicy();
    }

    @Test
    public void lockReturnsOK_whenQuorumResultsAreOK(){
        List<LockResult> lockResults = Arrays.asList(
            LockResult.OK, 
            LockResult.OK, 
            LockResult.ALREADY_LOCKED);

        LockResult lockResult = policy.verifyLockResults(lockResults);

        assertEquals(LockResult.OK, lockResult);
    }

    @Test
    public void lockReturnsAlreadyLocked_whenQuorumResultsAreAlreadyLocked(){
        List<LockResult> lockResults = Arrays.asList(
            LockResult.OK, 
            LockResult.ALREADY_LOCKED, 
            LockResult.ALREADY_LOCKED);

        LockResult lockResult = policy.verifyLockResults(lockResults);

        assertEquals(LockResult.ALREADY_LOCKED, lockResult);
    }

    @Test
    public void lockReturnsLockFailed_whenQuorumResultsAreLockFailed(){
        List<LockResult> lockResults = Arrays.asList(
            LockResult.OK, 
            LockResult.LOCK_FAILED, 
            LockResult.LOCK_FAILED);

        LockResult lockResult = policy.verifyLockResults(lockResults);

        assertEquals(LockResult.LOCK_FAILED, lockResult);
    }

    @Test
    public void lockReturnsLockFailed_whenQuorumResultsAreNotOK(){
        List<LockResult> lockResults = Arrays.asList(
            LockResult.OK, 
            LockResult.ALREADY_LOCKED, 
            LockResult.LOCK_FAILED);

        LockResult lockResult = policy.verifyLockResults(lockResults);

        assertEquals(LockResult.LOCK_FAILED, lockResult);
    }

    @Test
    public void unlockReturnsOK_whenQuorumNumberOfResultsAreOK(){
        List<UnlockResult> unlockResults = Arrays.asList(
            UnlockResult.OK,
            UnlockResult.OK,
            UnlockResult.OK);

        UnlockResult unlockResult = policy.verifyUnlockResults(unlockResults);

        assertEquals(UnlockResult.OK, unlockResult);
    }

    @Test
    public void unlockReturnsUnlockFailed_whenQuorumNumberOfResultsAreFailed(){
        List<UnlockResult> unlockResults = Arrays.asList(
            UnlockResult.OK,
            UnlockResult.UNLOCK_FAILED,
            UnlockResult.UNLOCK_FAILED);

        UnlockResult unlockResult = policy.verifyUnlockResults(unlockResults);

        assertEquals(UnlockResult.UNLOCK_FAILED, unlockResult);
    }
}
