package org.roko.dls.api.lockclient.util;

import java.util.List;

import org.roko.dls.api.lockclient.LockResult;
import org.roko.dls.api.lockclient.LockResultEnum;
import org.roko.dls.api.lockclient.UnlockResult;
import org.roko.dls.api.lockclient.UnlockResultEnum;
import org.springframework.stereotype.Component;

@Component
public class LockResultPolicy {

    public LockResultEnum inspectLockResults(List<LockResult> lockResults) {
        int cnt = lockResults.size();
        int quorumCount = cnt / 2 + 1;

        long alreadyLockedCount = lockResults.stream()
            .filter(x -> x.getResult() == LockResultEnum.ALREADY_LOCKED)
            .count();
        
        if (alreadyLockedCount >= quorumCount) {
            return LockResultEnum.ALREADY_LOCKED;
        }

        long lockFailedCount = lockResults.stream()
            .filter(x -> x.getResult() == LockResultEnum.LOCK_FAILED)
            .count();

        if ((lockFailedCount + alreadyLockedCount) >= quorumCount) {
            return LockResultEnum.LOCK_FAILED;
        }

        return LockResultEnum.OK;
    }

    public UnlockResultEnum inspectUnlockResults(List<UnlockResult> lockResults) {
        int cnt = lockResults.size();
        int quorumCount = cnt / 2 + 1;

        long lockFailedCount = lockResults.stream()
            .filter(x -> x.getResult() == UnlockResultEnum.UNLOCK_FAILED)
            .count();

        if ((lockFailedCount) >= quorumCount) {
            return UnlockResultEnum.UNLOCK_FAILED;
        }

        return UnlockResultEnum.OK;
    }
}
