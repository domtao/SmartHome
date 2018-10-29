package com.zunder.smart.socket.info;

/**
 * Created by Administrator on 2017/6/28.
 */

public class LoginInfo  extends BaseBeanInfo{
    String MasterName;
    String MasterWiFi;
    String MasterID ;
    String ToID ;
    int MasterType ;
    String Content;
    String MsgType ;
    String  PassWord;
    String MasterOperator;
    String  OperatorPwd;
    int MsgState;
    String CreateTime;

    public String getPassWord() {
        return PassWord;
    }
    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
    public String getMasterOperator() {
        return MasterOperator;
    }
    public void setMasterOperator(String masterOperator) {
        MasterOperator = masterOperator;
    }
    public String getOperatorPwd() {
        return OperatorPwd;
    }
    public void setOperatorPwd(String operatorPwd) {
        OperatorPwd = operatorPwd;
    }
    public String getToID() {
        return ToID;
    }
    public void setToID(String toID) {
        ToID = toID;
    }    public void setContent(String content) {
        Content = content;
    }
    public String getMasterName() {
        return MasterName;
    }
    public void setMasterName(String masterName) {
        this.MasterName = masterName;
    }
    public String getMasterWiFi() {
        return MasterWiFi;
    }
    public void setMasterWiFi(String masterWiFi) {
        this.MasterWiFi = masterWiFi;
    }
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
    public String getContent() {
        return Content;
    }
    public String getMsgType() { return MsgType;    }
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }
    public int getMsgState() {   return MsgState;   }
    public void setMsgState(int msgState) { MsgState = msgState;  }
    public String getCreateTime() {  return CreateTime;    }

    public void setCreateTime(String createTime) {    CreateTime = createTime;    }
}
