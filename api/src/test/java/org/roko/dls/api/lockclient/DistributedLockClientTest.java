package org.roko.dls.api.lockclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.lockclient.util.LockResultPolicy;
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

    private LockClient lockClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lockClient = new DistributedLockClient(Arrays.asList(subLockClient1, subLockClient2, subLockClient3), new LockResultPolicy());
    }

    @Test
    public void lockSucceeds_whenAllSublocksSucceed() throws AlreadyLockedException, LockFailedException {
        // given
        // any sublock client throws any exception
        
        // when
        LockResult lockResult = lockClient.lock(TEST_ID);

        // then
        assertEquals(LockResult.OK, lockResult);
    }

    @Test
    public void lockSucceeds_whenQuorumOfSublocksSucceed() throws AlreadyLockedException, LockFailedException,
            org.roko.dls.api.sublockclient.exc.AlreadyLockedException,
            org.roko.dls.api.sublockclient.exc.LockFailedException {
        // given
        doThrow(new AlreadyLockedException()).when(subLockClient3).lock(TEST_ID);

        // when
        LockResult lockResult = lockClient.lock(TEST_ID);

        // then
        assertEquals(LockResult.OK, lockResult);
    }

    @Test
    public void lockThrowsAlreadyLockedException_whenQuorumOfSublocksThrowAlreadyLockedException() throws AlreadyLockedException, LockFailedException{
        // given
        doThrow(new AlreadyLockedException()).when(subLockClient1).lock(TEST_ID);
        doThrow(new AlreadyLockedException()).when(subLockClient2).lock(TEST_ID);

        // when
        LockResult lockResult = lockClient.lock(TEST_ID);

        // then
        assertEquals(LockResult.ALREADY_LOCKED, lockResult);
    }

    @Test
    public void unlockSucceeds_whenAllSublocksSucceed() {
        // given
        // no sublock client throws any exception
        
        // when
        UnlockResult unlockResult = lockClient.unlock(TEST_ID);

        // then
        assertEquals(UnlockResult.OK, unlockResult);
    }

    @Test
    public void unlockThrowsUnlockFailedException_whenQuorumOfSublocksThrowUnlockFailedException() throws LockFailedException {
        // given
        doThrow(new LockFailedException()).when(subLockClient1).unlock(TEST_ID);
        doThrow(new LockFailedException()).when(subLockClient2).unlock(TEST_ID);

        // when
        UnlockResult unlockResult = lockClient.unlock(TEST_ID);

        // then
        assertEquals(UnlockResult.UNLOCK_FAILED, unlockResult);
    }
}
