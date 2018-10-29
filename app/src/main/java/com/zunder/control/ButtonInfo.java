package com.zunder.control;

import com.zunder.smart.gateway.bean.ContantsModel;

public class ButtonInfo {
    private int Id;
    private String BtnTitle;
    private String CMD;
    private int Color;//0红色 1蓝色
    private int SelectColor;

    public ButtonInfo(int Id,String BtnTitle,String CMD,int Color,int SelectColor){
        this.Id=Id;
        this.BtnTitle=BtnTitle;
        this.CMD= CMD;
        this.Color=Color;
        this.SelectColor=SelectColor;
    }
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getBtnTitle() {
        return BtnTitle;
    }
    public void setBtnTitle(String btnTitle) {
        BtnTitle = btnTitle;
    }
    public String getCMD() {
        return CMD;
    }
    public void setCMD(String CMD) {
        this.CMD = CMD;
    }
    public int getColor() {
        return Color;
    }
    public void setColor(int Color) {
        this.Color = Color;
    }
    public int getSelectColor() {
        return SelectColor;
    }
    public void setSelectColor(int SelectColor) {
        this.SelectColor = SelectColor;
    }

}
