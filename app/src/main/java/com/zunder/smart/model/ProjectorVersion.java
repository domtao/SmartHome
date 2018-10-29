package com.zunder.smart.model;

/**
 * Created by joe on 2017/12/28.
 */

public class ProjectorVersion {
    private int Id;
    private String VersionName;
    private int NameId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public int getNameId() {
        return NameId;
    }

    public void setNameId(int nameId) {
        NameId = nameId;
    }
}
