package com.zunder.smart.socket.info;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ResponseInfo  extends BaseBeanInfo{
    String MasterID ;
    String ToID ;
    String MsgType ;
    public int MsgState ;
    String Content ;
    String CreateTime;
    int MasterType;
    public int getMasterType() {
		return MasterType;
	}

	public void setMasterType(int masterType) {
		MasterType = masterType;
	}

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

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public int getMsgState() {
        return MsgState;
    }

    public void setMsgState(int msgState) {
        MsgState = msgState;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }


}
