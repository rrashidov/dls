package org.roko.dls.api.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.SublockClient;
import org.roko.dls.api.sublock.client.UnlockResult;

public class DistributedSublockClientTest {

    private static final String TEST_LOCK_ID = "test-lock-id";

    private static final LockResult TEST_LOCK_RESULT = LockResult.OK;
    private static final UnlockResult TEST_UNLOCK_RESULT = UnlockResult.OK;

    @Mock
    private SublockClient sublockClientMock;

    private DistributedSublockClient client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(sublockClientMock.lock(TEST_LOCK_ID)).thenReturn(TEST_LOCK_RESULT);
        when(sublockClientMock.unlock(TEST_LOCK_ID)).thenReturn(TEST_UNLOCK_RESULT);

        client = new DistributedSublockClient(Arrays.asList(sublockClientMock));
    }

    @Test
    public void lockDelegatesToSublockClients() {
        List<LockResult> lockResults = client.lock(TEST_LOCK_ID);

        verify(sublockClientMock).lock(TEST_LOCK_ID);

        assertEquals(1, lockResults.size(), "Lock results count should equal sublock clients count");
        assertEquals(TEST_LOCK_RESULT, lockResults.get(0), "Lock result should be the same as sublock client lock result");
    }

    @Test
    public void unlockDelegatesToSublockClients() {
        List<UnlockResult> unlockResults = client.unlock(TEST_LOCK_ID);

        verify(sublockClientMock).unlock(TEST_LOCK_ID);

        assertEquals(1, unlockResults.size(), "Unlock results count should equal sublock clients count");
        assertEquals(TEST_UNLOCK_RESULT, unlockResults.get(0), "Unlock result should be the same as sublock client unlock result");
    }
}
