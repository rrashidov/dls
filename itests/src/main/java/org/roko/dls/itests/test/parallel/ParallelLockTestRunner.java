package org.roko.dls.itests.test.parallel;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.roko.dls.client.DLSClient;
import org.roko.dls.client.LockResult;
import org.roko.dls.client.UnlockResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

//@Component
public class ParallelLockTestRunner implements CommandLineRunner {

    private static final String TEST_LOCK_ID = "parallel-test-lock-id";

    private static final int PARALLEL_EXEC_CNT = 10;

    private static final Object MONITOR = new Object();

    @Value("${lock.service.url}")
    private String lockServiceURL;

    @Override
    public void run(String... args) throws Exception {
        print("Running parallel test");

        cleanup();

        ExecutorService execSvc = Executors.newFixedThreadPool(PARALLEL_EXEC_CNT);

        List<Future<LockResult>> lockFutures = new LinkedList<>();

        CountDownLatch countDownLatch = new CountDownLatch(PARALLEL_EXEC_CNT);

        for (int i = 0; i < PARALLEL_EXEC_CNT; i++) {
            lockFutures.add(execSvc.submit(new LockCallable(lockServiceURL, MONITOR, TEST_LOCK_ID, countDownLatch)));
        }

        countDownLatch.await();

        synchronized (MONITOR) {
            MONITOR.notifyAll();
        }

        List<LockResult> lockResults = lockFutures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return LockResult.LOCK_FAILED;
                    }
                })
                .collect(Collectors.toList());

        long lockOKCount = lockResults.stream()
                .filter(x -> x.equals(LockResult.OK))
                .count();

        long alreadyLockedCount = lockResults.stream()
                .filter(x -> x.equals(LockResult.ALREADY_LOCKED))
                .count();

        try {
            if (lockOKCount != 1) {
                throw new Exception(
                        String.format("Parallel lock test failed! Expected locked: %s, got: %s", 1, lockOKCount));
            }

            if (alreadyLockedCount != (PARALLEL_EXEC_CNT - 1)) {
                throw new Exception(String.format("Parallel lock test failed! Expected already locked: %s, got: %s",
                        (PARALLEL_EXEC_CNT - 1), alreadyLockedCount));
            }
        } finally {
            execSvc.shutdown();
        }

        print("parallel test PASSED");
    }

    private void cleanup() throws Exception {
        DLSClient client = new DLSClient(lockServiceURL);

        UnlockResult unlockResult = client.unlock(TEST_LOCK_ID);

        if (!(unlockResult.equals(UnlockResult.OK))) {
            throw new Exception(String.format("Parallel test cleanup failed! Expected: %s, got: %s", UnlockResult.OK,
                    unlockResult));
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}