package org.roko.dls.api.svc;

import java.util.List;

import org.roko.dls.api.lockclient.DistributedLockClient;
import org.roko.dls.api.lockclient.LockResultEnum;
import org.roko.dls.api.lockclient.UnlockResult;
import org.roko.dls.api.lockclient.util.LockResultPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LockService {

    private DistributedLockClient lockClient;
    private LockResultPolicy lockResultPolicy;

    @Autowired
    public LockService(DistributedLockClient lockClient, LockResultPolicy lockResultPolicy) {
        this.lockClient = lockClient;
        this.lockResultPolicy = lockResultPolicy;
	}

	public LockResultEnum lock(String id){
        List<LockResultEnum> lockResults = lockClient.lock(id);

        return lockResultPolicy.inspectLockResults(lockResults);
    }

    public UnlockResult unlock(String id){
        List<UnlockResult> unlockResults = lockClient.unlock(id);

        return lockResultPolicy.inspectUnlockResults(unlockResults);
    }
}
