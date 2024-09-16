package org.roko.dls.itests.test.smoke;

import org.roko.dls.client.DLSClient;
import org.roko.dls.client.LockResult;
import org.roko.dls.client.UnlockResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SmokeTestRunner implements CommandLineRunner {

    private static final String TEST_LOCK_ID = "test-lock-id";

    @Value("${lock.service.url}")
    private String lockServiceURL;

    @Override
    public void run(String... args) throws Exception {
        print("Running smoke test");

        DLSClient client = new DLSClient(lockServiceURL);
        LockResult lockResult = client.lock(TEST_LOCK_ID);

        if (!lockResult.equals(LockResult.OK)) {
            throw new Exception(String.format("Lock failed! Expected: %s, got: %s", LockResult.OK, lockResult));
        }

        LockResult secondLockResult = client.lock(lockServiceURL);

        if (!(secondLockResult.equals(LockResult.ALREADY_LOCKED))) {
            throw new Exception(String.format("Second lock failed! Expected: %s, got: %s", LockResult.ALREADY_LOCKED, secondLockResult));
        }

        UnlockResult unlockResult = client.unlock(TEST_LOCK_ID);

        if (!(unlockResult.equals(UnlockResult.OK))) {
            throw new Exception(String.format("Unlock failed! Expected: %s, got: %s", UnlockResult.OK, unlockResult));
        }

        LockResult thirdLockResult = client.lock(TEST_LOCK_ID);

        if (!(thirdLockResult.equals(LockResult.OK))) {
            throw new Exception(String.format("Third lock failed! Expected: %s, got: %s", LockResult.OK, thirdLockResult));
        }

        unlockResult = client.unlock(TEST_LOCK_ID);

        if (!(unlockResult.equals(UnlockResult.OK))) {
            throw new Exception(String.format("Final unlock failed! Expected: %s, got: %s", UnlockResult.OK, unlockResult));
        }

        print("smoke test PASSED");
    }

    private void print(String msg){
        System.out.println(msg);
    }
}
