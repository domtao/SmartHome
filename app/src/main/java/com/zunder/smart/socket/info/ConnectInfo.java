package com.zunder.smart.socket.info;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ConnectInfo extends BaseBeanInfo
{
    public String MasterID ;
    public String ToID ;
    public int MasterType ;
    public String MsgType ;
    public String UserName ;
    public String PassWord ;

    String CreateTime;
    String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getMsgState() {
        return MsgState;
    }

    public void setMsgState(int msgState) {
        MsgState = msgState;
    }

    int MsgState;
    public String getMasterID() {
        return MasterID;
    }

    public void setMasterID(String masterID) {
        MasterID = masterID;
    }

    public int getMasterType() {
        return MasterType;
    }

    public void setMasterType(int masterType) {
        MasterType = masterType;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
    public String getToID() {
        return ToID;
    }

    public void setToID(String toID) {
        ToID = toID;
    }
    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
