package org.roko.dls.api.lockclient;

import org.roko.dls.api.sublockclient.SublockClient;

public class UnlockResult extends BaseResult {

    private final UnlockResultEnum result;
    
    public UnlockResult(SublockClient sublockClient, UnlockResultEnum result) {
        super(sublockClient);
        this.result = result;
    }

    public UnlockResultEnum getResult() {
        return result;
    }
    
}
