package org.roko.dls.api.lockclient;

import org.roko.dls.api.sublockclient.SublockClient;

public class BaseResult {

    protected final SublockClient sublockClient;

    public BaseResult(SublockClient sublockClient) {
        this.sublockClient = sublockClient;
    }

    public SublockClient getSublockClient() {
        return sublockClient;
    }

}
