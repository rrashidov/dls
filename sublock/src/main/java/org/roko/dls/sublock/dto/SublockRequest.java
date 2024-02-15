package org.roko.dls.sublock.dto;

public class SublockRequest {

    private String id;
    private long timestamp;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
