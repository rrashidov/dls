package org.roko.dls.api.lockclient.util;

import java.util.List;

import org.roko.dls.api.lockclient.LockResult;
import org.roko.dls.api.lockclient.UnlockResult;

public class LockResultPolicy {

    public LockResult inspectLockResults(List<LockResult> lockResults) {
        int cnt = lockResults.size();
        int quorumCount = cnt / 2 + 1;

        long alreadyLockedCount = lockResults.stream()
            .filter(x -> x == LockResult.ALREADY_LOCKED)
            .count();
        
        if (alreadyLockedCount >= quorumCount) {
            return LockResult.ALREADY_LOCKED;
        }

        long lockFailedCount = lockResults.stream()
            .filter(x -> x == LockResult.LOCK_FAILED)
            .count();

        if ((lockFailedCount + alreadyLockedCount) >= quorumCount) {
            return LockResult.LOCK_FAILED;
        }

        return LockResult.OK;
    }

    public UnlockResult inspectUnlockResults(List<UnlockResult> lockResults) {
        int cnt = lockResults.size();
        int quorumCount = cnt / 2 + 1;

        long lockFailedCount = lockResults.stream()
            .filter(x -> x == UnlockResult.UNLOCK_FAILED)
            .count();

        if ((lockFailedCount) >= quorumCount) {
            return UnlockResult.UNLOCK_FAILED;
        }

        return UnlockResult.OK;
    }
}
