package org.roko.dls.sublock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Sublock {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
