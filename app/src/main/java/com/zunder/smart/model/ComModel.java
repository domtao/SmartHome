package com.zunder.smart.model;

public class ComModel {
    public ComModel(int Id,int DeviceTypekey,String Name,String Cmd){
        this.Id=Id;
        this.Name=Name;
        this.Cmd=Cmd;
        this.DeviceTypekey=DeviceTypekey;
    }
    private int Id;
    private String Name;
    private String Cmd;
    private int DeviceTypekey;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCmd() {
        return Cmd;
    }

    public void setCmd(String cmd) {
        Cmd = cmd;
    }

    public int getDeviceTypekey() {
        return DeviceTypekey;
    }

    public void setDeviceTypekey(int deviceTypekey) {
        DeviceTypekey = deviceTypekey;
    }
}
