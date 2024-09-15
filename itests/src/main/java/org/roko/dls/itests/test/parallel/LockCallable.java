package org.roko.dls.itests.test.parallel;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.roko.dls.client.DLSClient;
import org.roko.dls.client.LockResult;

public class LockCallable implements Callable<LockResult> {

    private String lockServiceURL;
    private Object monitor;
    private String lockId;
    private CountDownLatch countDownLatch;

    public LockCallable(String lockServiceURL, Object monitor, String lockId, CountDownLatch countDownLatch) {
        this.lockServiceURL = lockServiceURL;
        this.monitor = monitor;
        this.lockId = lockId;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public LockResult call() throws Exception {
        DLSClient client = new DLSClient(lockServiceURL);

        countDownLatch.countDown();
        
        synchronized(monitor){
            monitor.wait();
        }

        return client.lock(lockId);
    }

}
