package org.roko.dls.api.sublockclient.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.roko.dls.api.lockclient.LockResult;
import org.roko.dls.api.lockclient.LockResultEnum;
import org.roko.dls.api.lockclient.UnlockResultEnum;
import org.roko.dls.api.lockclient.util.LockResultPolicy;
import org.roko.dls.api.sublockclient.SublockClient;

public class LockResultPolicyTest {

    @Mock
    private SublockClient sublockClientMock;

    private LockResultPolicy policy;

    @BeforeEach
    public void setup(){
        policy = new LockResultPolicy();
    }

    @Test
    public void inspecReturnsOK_whenQuorumOfResultsAreOK(){
        // given
        LockResult lockResult = new LockResult(sublockClientMock, LockResultEnum.OK);
        LockResult lockresult2 = new LockResult(sublockClientMock, LockResultEnum.OK);
        LockResult lockresult3 = new LockResult(sublockClientMock, LockResultEnum.LOCK_FAILED);

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.OK, result);
    }

    @Test
    public void inspectReturnsAlreadyLocked_whenQuorumOfResultsAreAlreadyLocked(){
        // given
        LockResult lockResult = new LockResult(sublockClientMock, LockResultEnum.OK);
        LockResult lockresult2 = new LockResult(sublockClientMock, LockResultEnum.ALREADY_LOCKED);
        LockResult lockresult3 = new LockResult(sublockClientMock, LockResultEnum.ALREADY_LOCKED);

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.ALREADY_LOCKED, result);
    }

    @Test
    public void inspectReturnsLockFailed_whenQuorumOfResultsAreLockFailed(){
        // given
        LockResult lockResult = new LockResult(sublockClientMock, LockResultEnum.LOCK_FAILED);
        LockResult lockresult2 = new LockResult(sublockClientMock, LockResultEnum.LOCK_FAILED);
        LockResult lockresult3 = new LockResult(sublockClientMock, LockResultEnum.OK);

        // when
        LockResultEnum result = policy.inspectLockResults(Arrays.asList(lockResult, lockresult2, lockresult3));

        // then
        assertEquals(LockResultEnum.LOCK_FAILED, result);
    }

    @Test
    public void inspectReturnsLockFailed_whenQuorumOfResultsAreNotOK(){
        // given
        LockResult lockResult = new LockResult(sublockClientMock, LockResultEnum.LOCK_FAILED);
        LockResult lockresult2 = new LockResult(sublockClientMock, LockResultEnum.ALREADY_LOCKED);
        LockResult lockresult3 = new LockResult(sublockClientMock, LockResultEnum.OK);

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
