package com.zunder.smart.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.zunder.smart.tools.AppTools;

/**
 * Created by Administrator on 2017/6/15.
 */

public class Room {
    private int Id;
    private String RoomName;
    private String RoomImage;
    private String CreationTime;
    private int Seqencing;
    private int IsShow;
    private int LanguageId;
    private int CompanyId;
    private String Data1="0";
    private String Data2="0";
    private String Primary_Key="000000";
    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int isShow) {
        IsShow = isShow;
    }

    public int getLanguageId() {
        return LanguageId;
    }

    public void setLanguageId(int languageId) {
        LanguageId = languageId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public String getData1() {
        return Data1;
    }

    public void setData1(String data1) {
        Data1 = data1;
    }

    public String getData2() {
        return Data2;
    }

    public void setData2(String data2) {
        Data2 = data2;
    }





    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRoomName() {
        if (RoomName == null || RoomName.equals("null")) {
            return "";
        }
        return RoomName;
    }

    public void setRoomName(String roomName) {

        RoomName = roomName;
    }


    public String getRoomImage() {

        return RoomImage;
    }


    public void setRoomImage(String arceImage) {
        RoomImage = arceImage;
    }


    public String getCreationTime() {
        if (CreationTime == null || CreationTime.equals("null")) {
            return AppTools.getCurrentTime();
        }
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {

        CreationTime = creationTime;
    }

    public int getSeqencing() {
        return
                Seqencing;
    }

    public void setSeqencing(int seqencing) {
        Seqencing = seqencing;
    }


    public String getPrimary_Key() {

        return Primary_Key;
    }

    public void setPrimary_Key(String primary_Key) {
        Primary_Key = primary_Key;
    }
    public String convertTostring() {
        String result = getId() + ";" + getRoomName() + ";" + getIsShow()
                + ";" + getSeqencing();
        Log.e("infoCmd",result);
        return result;
    }
}
