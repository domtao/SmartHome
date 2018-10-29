package com.zunder.smart.aiui.info;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SubscribeInfo {
    private int Id ;
    private String SubscribeName ;
    private String SubscribeDate;
    private String SubscribeTime;
    private String SubscribeAction;
    private String SubscribeEvent;
    private String UserName ;
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getSubscribeName() {
        return SubscribeName;
    }
    public void setSubscribeName(String subscribeName) {
        SubscribeName = subscribeName;
    }

    public String getSubscribeDate() {
        return SubscribeDate;
    }

    public void setSubscribeDate(String subscribeDate) {
        SubscribeDate = subscribeDate;
    }

    public String getSubscribeTime() {
        return SubscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        SubscribeTime = subscribeTime;
    }

    public String getSubscribeAction() {
        if(SubscribeAction==null||SubscribeAction=="null"||SubscribeAction.equals("null")){
            SubscribeAction="";
        }
        return SubscribeAction;
    }
    public void setSubscribeAction(String subscribeAction) {
        SubscribeAction = subscribeAction;
    }

    public String getSubscribeEvent() {
        if(SubscribeEvent==null||SubscribeEvent=="null"||SubscribeEvent.equals("null")){
            SubscribeEvent="";
        }
        return SubscribeEvent;
    }

    public void setSubscribeEvent(String subscribeEvent) {
        SubscribeEvent = subscribeEvent;
    }

    public String getUserName() {
        if(UserName==null||UserName==""){
            UserName="0";
        }
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String convertTostring() {
        String result = getId() + ";" + getSubscribeName() + ";" + getSubscribeDate()
                + ";" + getSubscribeTime() + ";" + getSubscribeEvent() + ";"
                + getSubscribeAction() + ";" + getUserName();
        return result;
    }
}
