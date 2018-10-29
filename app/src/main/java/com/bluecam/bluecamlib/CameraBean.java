package com.bluecam.bluecamlib;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/15.
 */

public class CameraBean implements Serializable {
    protected int id;
    //设备mac
    protected String devMac="";
    //设备名称
    protected String devName="";
    //设备ID
    protected String devID="";
    //设备IP
    protected String devIP="";
    //设备端口
    protected int port;
    //设备类型
    protected int deviceType;
    //访问设备的用户名
    protected String username="";
    //访问设备的密码
    protected String password="";
    //声波无线配置标识
    protected  int SmartConnect;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDevMac() {
        return devMac;
    }
    public void setDevMac(String devMac) {
        this.devMac = devMac;
    }
    public String getDevName() {
        return devName;
    }
    public void setDevName(String devName) {
        this.devName = devName;
    }
    public String getDevID() {
        return devID;
    }
    public void setDevID(String devID) {
        this.devID = devID;
    }
    public String getDevIP() {
        return devIP;
    }
    public void setDevIP(String devIP) {
        this.devIP = devIP;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getProductsCode() {
        return deviceType;
    }
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getSmartConnect() {
        return SmartConnect;
    }

    public void setSmartConnect(int smartConnect) {
        SmartConnect = smartConnect;
    }

    @Override
    public String toString() {
        return "DeviceBean{" +
                "id=" + id +
                ", devMac='" + devMac + '\'' +
                ", devName='" + devName + '\'' +
                ", devID='" + devID + '\'' +
                ", devIP='" + devIP + '\'' +
                ", port=" + port +
                ", deviceType=" + deviceType +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
