package com.bluecam.api.bean;

/**
 * Created by Administrator on 2017/7/17.
 */

public class LanBean {
    private String lan;
    private boolean isSelected;

    public LanBean(String lan, boolean isSelected) {
        this.lan = lan;
        this.isSelected = isSelected;
    }

    public LanBean(String lan) {
        this.lan = lan;
    }
    public LanBean(){}

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
