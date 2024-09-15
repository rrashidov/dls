package org.roko.dls.api.service;

import java.util.List;
import java.util.Random;

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
        long millis = System.currentTimeMillis();

        Random r = new Random();
        int random = r.nextInt(100);

        List<LockResult> lockResults = distributedSublockClient.lock(id, millis, random);

        LockResult lockResult = policy.verifyLockResults(lockResults);

        return lockResult;
    }

    public UnlockResult unlock(String id){
        List<UnlockResult> unlockResults = distributedSublockClient.unlock(id);

        UnlockResult unlockResult = policy.verifyUnlockResults(unlockResults);

        return unlockResult;
    }
}
