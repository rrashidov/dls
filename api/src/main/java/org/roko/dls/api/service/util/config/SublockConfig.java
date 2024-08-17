package org.roko.dls.api.service.util.config;

import java.util.List;

public class SublockConfig {

    private List<SublockClientConfig> sublockClients;

    public List<SublockClientConfig> getSublockClients() {
        return sublockClients;
    }

    public void setSublockClients(List<SublockClientConfig> sublockClients) {
        this.sublockClients = sublockClients;
    }

}
