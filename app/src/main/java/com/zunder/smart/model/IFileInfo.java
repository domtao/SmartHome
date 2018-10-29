package com.zunder.smart.model;

/**
 * Created by Administrator on 2018/4/10.
 */

public class IFileInfo {
    private int Id ;
    private String FileName;
    private String FileUrl ;
    private int TypeId;
    private String FileText ;
    private String Extension ;
    private String MasterID;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public String getFileText() {
        return FileText;
    }

    public void setFileText(String fileText) {
        FileText = fileText;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getMasterID() {
        return MasterID;
    }

    public void setMasterID(String masterID) {
        MasterID = masterID;
    }
}
