package org.roko.dls.api.service.util;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.SublockClient;
import org.roko.dls.api.sublock.client.UnlockResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributedSublockClient {

    private List<SublockClient> sublockClients;

    @Autowired
    public DistributedSublockClient(List<SublockClient> sublockClients) {
        this.sublockClients = sublockClients;
    }

    public List<LockResult> lock(String id){
        return lock(id, 0, 0);
    }

    public List<LockResult> lock(String id, long millis, long extraMillis){
        return sublockClients.stream()
            .map(x -> {
                LockResult lockResult = x.lock(id);
                print("Lock " + millis + ":" + extraMillis + " result [" + x + "]: " + lockResult);
                return lockResult;
            })
            .collect(Collectors.toList());
    }

    public List<UnlockResult> unlock(String id){
        return sublockClients.stream()
            .map(x -> x.unlock(id))
            .collect(Collectors.toList());
    }

    private void print(String msg){
        Thread t = Thread.currentThread();

        long millis = System.currentTimeMillis();

        System.out.println(t + " : " + millis + " : " + msg);
    }
}
