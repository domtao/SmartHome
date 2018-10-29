package com.zunder.smart.socket.info;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ForWardInfo  extends BaseBeanInfo{


    public String MasterID ;
    public String ToID ;
    public int MasterType ;
    public String Content ;
    public String MsgType ;
    public String CreateTime;
    public String getMasterID() {
        return MasterID;
    }

    public void setMasterID(String masterID) {
        MasterID = masterID;
    }

    public String getToID() {
        return ToID;
    }

    public void setToID(String toID) {
        ToID = toID;
    }

    public int getMasterType() {
        return MasterType;
    }

    public void setMasterType(int masterType) {
        MasterType = masterType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }


}
