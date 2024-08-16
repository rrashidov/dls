package org.roko.dls.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.service.util.DistributedSublockClient;
import org.roko.dls.api.service.util.SublockResultPolicy;
import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.UnlockResult;

public class LockServiceTest {

    private static final UnlockResult TEST_UNLOCK_RESULT = UnlockResult.OK;

    private static final LockResult TEST_LOCK_RESULT = LockResult.OK;

    private static final String TEST_ID = "test-id";

    @Mock
    private DistributedSublockClient sublockClientMock;

    @Mock
    private SublockResultPolicy policyMock;

    private LockService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        svc = new LockService(sublockClientMock, policyMock);
    }

    @Test
    public void lockReturnsProperResult(){
        List<LockResult> sublockResults = new ArrayList<>();        
        when(sublockClientMock.lock(TEST_ID)).thenReturn(sublockResults);

        when(policyMock.verifyLockResults(sublockResults)).thenReturn(TEST_LOCK_RESULT);

        LockResult lockResult = svc.lock(TEST_ID);

        assertEquals(TEST_LOCK_RESULT, lockResult);
    }

    @Test
    public void unlockReturnsProperResults(){
        List<UnlockResult> unlockResults = new ArrayList<>();
        when(sublockClientMock.unlock(TEST_ID)).thenReturn(unlockResults);

        when(policyMock.verifyUnlockResults(unlockResults)).thenReturn(TEST_UNLOCK_RESULT);

        UnlockResult unlockResult = svc.unlock(TEST_ID);

        assertEquals(TEST_UNLOCK_RESULT, unlockResult);
    }
}
