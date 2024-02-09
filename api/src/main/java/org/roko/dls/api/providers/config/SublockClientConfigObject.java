package org.roko.dls.api.providers.config;

import java.util.List;

public class SublockClientConfigObject {

    private List<SublockClientConfig> sublockClients;

    public List<SublockClientConfig> getSublockClients() {
        return sublockClients;
    }

    public void setSublockClients(List<SublockClientConfig> sublockClients) {
        this.sublockClients = sublockClients;
    }

}
