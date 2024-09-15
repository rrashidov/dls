package org.roko.dls.itests.test.prolonged;

import java.util.concurrent.Callable;

import org.roko.dls.client.DLSClient;

public class SimpleLockCallable implements Callable<String> {

    private String id;
    private DLSClient client;

    public SimpleLockCallable(String id, DLSClient client) {
        this.id = id;
        this.client = client;
    }

    @Override
    public String call() throws Exception {
        client.lock(id);
        client.unlock(id);
        
        return "OK";
    }

}
