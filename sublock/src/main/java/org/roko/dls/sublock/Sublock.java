package org.roko.dls.sublock;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Sublock {

    @Id
    private String id;
    private boolean locked;
    private long timestamp;
    private long dateFlag;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public long getDateFlag() {
        return dateFlag;
    }
    public void setDateFlag(long dateFlag) {
        this.dateFlag = dateFlag;
    }

}
