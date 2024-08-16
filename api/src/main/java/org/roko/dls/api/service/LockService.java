package org.roko.dls.api.service;

import java.util.List;

import org.roko.dls.api.service.util.DistributedSublockClient;
import org.roko.dls.api.service.util.SublockResultPolicy;
import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.UnlockResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockService {

    private DistributedSublockClient distributedSublockClient;
    private SublockResultPolicy policy;

    @Autowired
    public LockService(DistributedSublockClient distributedSublockClient, SublockResultPolicy policy) {
        this.distributedSublockClient = distributedSublockClient;
        this.policy = policy;
    }

    public LockResult lock(String id){
        List<LockResult> lockResults = distributedSublockClient.lock(id);

        LockResult lockResult = policy.verifyLockResults(lockResults);

        return lockResult;
    }

    public UnlockResult unlock(String id){
        List<UnlockResult> unlockResults = distributedSublockClient.unlock(id);

        UnlockResult unlockResult = policy.verifyUnlockResults(unlockResults);

        return unlockResult;
    }
}
