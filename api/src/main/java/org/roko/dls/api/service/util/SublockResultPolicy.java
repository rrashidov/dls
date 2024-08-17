package org.roko.dls.api.service.util;

import java.util.List;

import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.UnlockResult;
import org.springframework.stereotype.Component;

@Component
public class SublockResultPolicy {

    public LockResult verifyLockResults(List<LockResult> lockResults){
        int resultCount = lockResults.size();

        int quorumCount = (resultCount / 2) + 1;

        int okCount = (int)lockResults.stream()
            .filter(x -> x.equals(LockResult.OK))
            .count();

        if (okCount >= quorumCount) {
            return LockResult.OK;
        }

        int alreadyLockedCount = (int)lockResults.stream()
            .filter(x -> x.equals(LockResult.ALREADY_LOCKED))
            .count();

        if (alreadyLockedCount >= quorumCount) {
            return LockResult.ALREADY_LOCKED;
        }
    
        return LockResult.LOCK_FAILED;
    }

    public UnlockResult verifyUnlockResults(List<UnlockResult> unlockResults){
        int resultCount = unlockResults.size();

        int quorumCount = (resultCount / 2) + 1;

        int okCount = (int)unlockResults.stream()
            .filter(x -> x.equals(UnlockResult.OK))
            .count();

        if (okCount >= quorumCount) {
            return UnlockResult.OK;
        }

        return UnlockResult.UNLOCK_FAILED;
    }
}
