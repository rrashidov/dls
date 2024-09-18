package org.roko.dls.dto;

public class LockRequest {

    private long timestamp;

    public LockRequest(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
