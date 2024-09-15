package org.roko.dls.itests.test.prolonged;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.roko.dls.client.DLSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProlongedTestRunner implements CommandLineRunner {

    private static final int ITERATION_COUNT = 10_000;

    @Value("${lock.service.url}")
    private String lockServiceURL;

    @Override
    public void run(String... args) throws Exception {
        print("Running prolonged integration test");

        DLSClient client = new DLSClient(lockServiceURL);

        ExecutorService es = Executors.newFixedThreadPool(10);

        List<Future<String>> results = new ArrayList<>();

        print("Start scheduling lock tasks");

        for (long i = 0; i < ITERATION_COUNT; i++) {
            results.add(es.submit(new SimpleLockCallable(String.valueOf(System.currentTimeMillis() + i), client)));
        }

        print("Finished scheduling lock tasks. Now wait until all are finished");

        long failedLockCount = results.stream()
            .map(x -> {
                try {
                    x.get();
                    return "OK";
                } catch (InterruptedException | ExecutionException e) {
                    return "FAILED";
                }
            })
            .filter(x -> x.equals("FAILED"))
            .count();

        if (failedLockCount > 0) {
            throw new Exception("Prolonged lock scenario failed. Failed number of locks: " + failedLockCount);
        }

        es.shutdown();

        print("All lock results received");

        print("Prolonged integration test PASSED");
    }

    private void print(String s){
        System.out.println(s);
    }
}
