package com.bluecam.api.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/7.
 */

public class RecordBean implements Serializable {
    private String camID;
    private String fileName;
    private String strDate;
    private int size;

    public RecordBean(String camID, String fileName, String strDate, int size) {
        this.camID = camID;
        this.fileName = fileName;
        this.strDate = strDate;
        this.size = size;
    }

    public String getCamID() {
        return camID;
    }

    public void setCamID(String camID) {
        this.camID = camID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
