package org.roko.dls.api.service.util;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.SublockClient;
import org.roko.dls.api.sublock.client.UnlockResult;

public class DistributedSublockClient {

    private List<SublockClient> sublockClients;

    public DistributedSublockClient(List<SublockClient> sublockClients) {
        this.sublockClients = sublockClients;
    }

    public List<LockResult> lock(String id){
        return sublockClients.stream()
            .map(x -> x.lock(id))
            .collect(Collectors.toList());
    }

    public List<UnlockResult> unlock(String id){
        return sublockClients.stream()
            .map(x -> x.unlock(id))
            .collect(Collectors.toList());
    }
}
