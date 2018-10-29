package com.zunder.smart.remote.model;

/**
 * Created by Administrator on 2018/5/4.
 */

public class UserType {
    private int Id;
    private String TypeName;
    private String typeUrl;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getTypeUrl() {
        return typeUrl;
    }

    public void setTypeUrl(String typeUrl) {
        this.typeUrl = typeUrl;
    }
}
