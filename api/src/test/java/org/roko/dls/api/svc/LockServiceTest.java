package org.roko.dls.api.svc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.dls.api.lockclient.DistributedLockClient;
import org.roko.dls.api.lockclient.LockResult;
import org.roko.dls.api.lockclient.UnlockResultEnum;
import org.roko.dls.api.lockclient.util.LockResultPolicy;

public class LockServiceTest {

    private List<LockResult> lockResults = new ArrayList<>();
    private List<UnlockResultEnum> unlockResults = new ArrayList<>();

    @Mock
    private DistributedLockClient lockClientMock;

    @Mock
    private LockResultPolicy lockResultPolicyMock;

    private LockService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(lockClientMock.lock("test-id")).thenReturn(lockResults);
        when(lockClientMock.unlock("test-id")).thenReturn(unlockResults);

        svc = new LockService(lockClientMock, lockResultPolicyMock);
    }

    @Test
    public void lockCallsDependencies(){
        // when
        svc.lock("test-id");

        // then
        verify(lockClientMock).lock("test-id");
        verify(lockResultPolicyMock).inspectLockResults(lockResults);
    }

    @Test
    public void unlockCallsDependencies(){
        // when
        svc.unlock("test-id");

        // then
        verify(lockClientMock).unlock("test-id");
        verify(lockResultPolicyMock).inspectUnlockResults(unlockResults);
    }
}
