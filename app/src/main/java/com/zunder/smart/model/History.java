package com.zunder.smart.model;

/**
 * Created by Administrator on 2017/8/16.
 */

public class History {
    public int Id ;
    public String HistoryName ;
    public String HistoryCode;
    public String CreateTime ;
    public String MasterMac ;
    public String PrimaryKey ;
    private String UserName;
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHistoryName() {
        return HistoryName;
    }

    public void setHistoryName(String historyName) {
        HistoryName = historyName;
    }

    public String getHistoryCode() {
        return HistoryCode;
    }

    public void setHistoryCode(String historyCode) {
        HistoryCode = historyCode;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMasterMac() {
        return MasterMac;
    }

    public void setMasterMac(String masterMac) {
        MasterMac = masterMac;
    }

    public String getPrimaryKey() {
        return PrimaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        PrimaryKey = primaryKey;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
