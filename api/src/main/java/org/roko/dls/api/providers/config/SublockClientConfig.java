package org.roko.dls.api.providers.config;

public class SublockClientConfig {

    private String rootUri;
    private int connectTimeout;
    private int readTimeout;

    public String getRootUri() {
        return rootUri;
    }

    public void setRootUri(String rootUri) {
        this.rootUri = rootUri;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
