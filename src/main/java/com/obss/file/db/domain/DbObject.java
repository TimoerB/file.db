package com.obss.file.db.domain;

import java.time.Instant;

public class DbObject {

    private String id;
    private Long modified;
    private String value;

    public DbObject() {}

    public DbObject(String id, Long modified, String value) {
        this.id = id;
        this.modified = modified == null ? Instant.now().toEpochMilli() : modified;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
