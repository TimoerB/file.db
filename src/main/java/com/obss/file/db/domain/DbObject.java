package com.obss.file.db.domain;

import java.util.Date;

public class DbObject {

    private String id;
    private String modified;
    private String value;

    public DbObject() {}

    public DbObject(String id, String modified, String value) {
        this.id = id;
        this.modified = modified == null ? new Date().toGMTString() : modified;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
