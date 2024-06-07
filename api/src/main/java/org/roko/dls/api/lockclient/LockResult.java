package org.roko.dls.api.lockclient;

import org.roko.dls.api.sublockclient.SublockClient;

public class LockResult extends BaseResult {

    private final LockResultEnum result;

    public LockResult(SublockClient client, LockResultEnum result) {
        super(client);
        this.result = result;
    }

    public LockResultEnum getResult() {
        return result;
    }

}
