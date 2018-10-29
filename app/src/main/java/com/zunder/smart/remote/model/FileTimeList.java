package com.zunder.smart.remote.model;

/**
 * Created by Administrator on 2018/5/9.
 */

public class FileTimeList {
   private int Id;
    private int TimeId;
    private String  ControlTime;
    private int ControlIndex;
    private String ControlName;
    private int TypeId;
    private  String FileUrl;
    private String ControlCmd;
    private String FileName;
    private int CycleIndex;
    private String Extension;
    private String AssignDate;
    private String ControlMasterID ;
    private String ControlDevice ;
    private String ControlHex ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTimeId() {
        return TimeId;
    }

    public void setTimeId(int timeId) {
        TimeId = timeId;
    }

    public String getControlTime() {
        return ControlTime;
    }

    public void setControlTime(String controlTime) {
        ControlTime = controlTime;
    }

    public int getControlIndex() {
        return ControlIndex;
    }

    public void setControlIndex(int controlIndex) {
        ControlIndex = controlIndex;
    }

    public String getControlName() {
        return ControlName;
    }

    public void setControlName(String controlName) {
        ControlName = controlName;
    }

    public String getControlCmd() {
        return ControlCmd;
    }

    public void setControlCmd(String controlCmd) {
        ControlCmd = controlCmd;
    }

    public String getFileName() {
        if(FileName==null||FileName=="null"||FileName.equals("null")){
            FileName="控制命令";
        }
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public int getCycleIndex() {
        return CycleIndex;
    }

    public void setCycleIndex(int cycleIndex) {
        CycleIndex = cycleIndex;
    }

    public String getExtension()
    {  if(Extension==null||Extension=="null"||Extension.equals("null")){
        Extension="";
    }
        return Extension.replace(".","");
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getAssignDate() {
        return AssignDate;
    }

    public void setAssignDate(String assignDate) {
        AssignDate = assignDate;
    }

    public String getControlDevice() {

     if(ControlDevice==null||ControlDevice=="null"||ControlDevice.equals("null")){
         ControlDevice="";
        }
        return ControlDevice;
    }

    public void setControlDevice(String controlDevice) {
        ControlDevice = controlDevice;
    }

    public String getControlHex() {if(ControlHex==null||ControlHex=="null"||ControlHex.equals("null")){
            ControlHex="";
        }

        return ControlHex;
    }

    public void setControlHex(String controlHex) {
        ControlHex = controlHex;
    }

    public String getControlMasterID() {
        if(ControlMasterID==null||ControlMasterID=="null"||ControlMasterID.equals("null")){
            ControlMasterID="";
        }
        return ControlMasterID;
    }

    public void setControlMasterID(String controlMasterID) {
        ControlMasterID = controlMasterID;
    }
}
