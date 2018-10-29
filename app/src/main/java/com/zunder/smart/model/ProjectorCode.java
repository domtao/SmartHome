package com.zunder.smart.model;

/**
 * Created by joe on 2017/12/28.
 */

public class ProjectorCode {
    private int Id;
    private String BaudRate;
    private String CheckBit ;
    private String HexCode ;
    private String SendCode ;
    private int VersionId ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBaudRate() {
        return BaudRate;
    }

    public void setBaudRate(String baudRate) {
        BaudRate = baudRate;
    }

    public String getCheckBit() {
        return CheckBit;
    }

    public void setCheckBit(String checkBit) {
        CheckBit = checkBit;
    }

    public String getHexCode() {
        return HexCode;
    }

    public void setHexCode(String hexCode) {
        HexCode = hexCode;
    }

    public String getSendCode() {
        return SendCode;
    }

    public void setSendCode(String sendCode) {
        SendCode = sendCode;
    }

    public int getVersionId() {
        return VersionId;
    }

    public void setVersionId(int versionId) {
        VersionId = versionId;
    }
}
