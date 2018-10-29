package com.zunder.smart.aiui.info;

/**
 * Created by Administrator on 2018/1/23/023.
 */

public class BlueDevice {
    private int Id;
    private String address;
    private String deviceName;
    private int boundState;
    private int connectState;
    private int deviceType;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceName() {
        if(deviceName==null||deviceName.equals("null")||deviceName=="null"){
            deviceName="未知设备 ";
        }
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getBoundState() {
        return boundState;
    }

    public void setBoundState(int boundState) {
        this.boundState = boundState;
    }

    public int getConnectState() {
        return connectState;
    }

    public void setConnectState(int connectState) {
        this.connectState = connectState;
    }

    public int getProductsCode() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
