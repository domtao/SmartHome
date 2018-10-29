package com.zunder.smart.remote.model;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FileUser {
    private int Id;
    private String UserName;
    private int UserType;
    private String UserTel;
    private String CreationTime;
    private String MasterID;
    private String UserNick;
    private String TypeName;
    private String TypeUrl;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public String getUserTel() {
        return UserTel;
    }

    public void setUserTel(String userTel) {
        UserTel = userTel;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getMasterID() {
        return MasterID;
    }

    public void setMasterID(String masterID) {
        MasterID = masterID;
    }

    public String getUserNick() {
        return UserNick;
    }

    public void setUserNick(String userNick) {
        UserNick = userNick;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getTypeUrl() {
        return TypeUrl;
    }

    public void setTypeUrl(String typeUrl) {
        TypeUrl = typeUrl;
    }
}
