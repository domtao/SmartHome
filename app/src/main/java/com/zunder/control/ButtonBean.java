package com.zunder.control;

import java.util.List;

public class ButtonBean {

    private List<ButtonInfo> list;
    private int[] ImageIndexs;
    private int[] State;
    private int DeviceTypeKey;
    private int IsPower;
    private int[] modeImage;
    private int[] speedImage;
    private int[] tempImage;

    public List <ButtonInfo> getList() {
        return list;
    }

    public void setList(List <ButtonInfo> list) {
        this.list = list;
    }
    public int[] getImageIndexs() {
        return ImageIndexs;
    }

    public void setImageIndexs(int[] imageIndexs) {
        ImageIndexs = imageIndexs;
    }

    public int[] getState() {
        return State;
    }

    public void setState(int[] state) {
        State = state;
    }

    public int getDeviceTypeKey() {
        return DeviceTypeKey;
    }

    public void setDeviceTypeKey(int deviceTypeKey) {
        DeviceTypeKey = deviceTypeKey;
    }

    public int getIsPower() {
        return IsPower;
    }

    public void setIsPower(int isPower) {
        IsPower = isPower;
    }

    public int[] getModeImage() {
        return modeImage;
    }

    public void setModeImage(int[] modeImage) {
        this.modeImage = modeImage;
    }

    public int[] getSpeedImage() {
        return speedImage;
    }

    public void setSpeedImage(int[] speedImage) {
        this.speedImage = speedImage;
    }

    public int[] getPicImage() {
        return picImage;
    }

    public void setPicImage(int[] picImage) {
        this.picImage = picImage;
    }

    private int[] picImage;


    public int[] getTempImage() {
        return tempImage;
    }

    public void setTempImage(int[] tempImage) {
        this.tempImage = tempImage;
    }
}
