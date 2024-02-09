package org.roko.dls.api.lockclient;

import java.util.ArrayList;
import java.util.List;

import org.roko.dls.api.lockclient.util.LockResultPolicy;
import org.roko.dls.api.sublockclient.SublockClient;
import org.roko.dls.api.sublockclient.exc.AlreadyLockedException;
import org.roko.dls.api.sublockclient.exc.LockFailedException;

public class DistributedLockClient implements LockClient {

    private List<SublockClient> sublockClients;
    private LockResultPolicy lockResultPolicy;

    public DistributedLockClient(List<SublockClient> sublockClients, LockResultPolicy lockResultPolicy) {
        this.sublockClients = sublockClients;
        this.lockResultPolicy = lockResultPolicy;
    }

    @Override
    public LockResult lock(String id) {
        List<LockResult> lockResults = new ArrayList<>();

        for (SublockClient sublockClient : sublockClients) {
            try {
                sublockClient.lock(id);
                lockResults.add(LockResult.OK);
            } catch (AlreadyLockedException e) {
                lockResults.add(LockResult.ALREADY_LOCKED);
            } catch (LockFailedException e) {
                lockResults.add(LockResult.LOCK_FAILED);
            };
        }

        return lockResultPolicy.inspectLockResults(lockResults);
    }

    @Override
    public UnlockResult unlock(String id) {
        List<UnlockResult> lockResults = new ArrayList<>();

        for (SublockClient sublockClient : sublockClients) {
            try {
                sublockClient.unlock(id);
                lockResults.add(UnlockResult.OK);
            } catch (LockFailedException e) {
                lockResults.add(UnlockResult.UNLOCK_FAILED);
            };
        }

        return lockResultPolicy.inspectUnlockResults(lockResults);
    }

}
