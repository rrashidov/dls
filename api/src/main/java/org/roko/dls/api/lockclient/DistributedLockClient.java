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

    public List<LockResult> lock(String id) {
        List<LockResult> lockResults = new ArrayList<>();

        for (SublockClient sublockClient : sublockClients) {
            try {
                sublockClient.lock(id);
                lockResults.add(new LockResult(sublockClient, LockResultEnum.OK));
            } catch (AlreadyLockedException e) {
                lockResults.add(new LockResult(sublockClient, LockResultEnum.ALREADY_LOCKED));
            } catch (LockFailedException e) {
                lockResults.add(new LockResult(sublockClient, LockResultEnum.LOCK_FAILED));
            };
        }

        return lockResults;
    }

    public List<UnlockResultEnum> unlock(String id) {
        List<UnlockResultEnum> unlockResults = new ArrayList<>();

        for (SublockClient sublockClient : sublockClients) {
            try {
                sublockClient.unlock(id);
                unlockResults.add(UnlockResultEnum.OK);
            } catch (LockFailedException e) {
                unlockResults.add(UnlockResultEnum.UNLOCK_FAILED);
            };
        }

        return unlockResults;
    }

}
