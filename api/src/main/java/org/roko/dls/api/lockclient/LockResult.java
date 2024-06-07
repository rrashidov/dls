package org.roko.dls.api.lockclient;

import org.roko.dls.api.sublockclient.SublockClient;

public class LockResult {

    private final SublockClient client;
    private final LockResultEnum result;

    public LockResult(SublockClient client, LockResultEnum result) {
        this.client = client;
        this.result = result;
    }

    public SublockClient getClient() {
        return client;
    }
    
    public LockResultEnum getResult() {
        return result;
    }

}
