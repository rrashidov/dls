package org.roko.dls.api.lockclient;

import java.util.ArrayList;
import java.util.List;

import org.roko.dls.api.sublockclient.SublockClient;
import org.roko.dls.api.sublockclient.exc.AlreadyLockedException;
import org.roko.dls.api.sublockclient.exc.LockFailedException;

public class DistributedLockClient {

    private List<SublockClient> sublockClients;

    public DistributedLockClient(List<SublockClient> sublockClients) {
        this.sublockClients = sublockClients;
    }

    public List<LockResultEnum> lock(String id) {
        List<LockResultEnum> lockResults = new ArrayList<>();

        for (SublockClient sublockClient : sublockClients) {
            try {
                sublockClient.lock(id);
                lockResults.add(LockResultEnum.OK);
            } catch (AlreadyLockedException e) {
                lockResults.add(LockResultEnum.ALREADY_LOCKED);
            } catch (LockFailedException e) {
                lockResults.add(LockResultEnum.LOCK_FAILED);
            };
        }

        return lockResults;
    }

    public List<UnlockResult> unlock(String id) {
        List<UnlockResult> unlockResults = new ArrayList<>();

        for (SublockClient sublockClient : sublockClients) {
            try {
                sublockClient.unlock(id);
                unlockResults.add(UnlockResult.OK);
            } catch (LockFailedException e) {
                unlockResults.add(UnlockResult.UNLOCK_FAILED);
            };
        }

        return unlockResults;
    }

}
