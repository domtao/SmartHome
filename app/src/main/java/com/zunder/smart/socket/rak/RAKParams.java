package com.zunder.smart.socket.rak;

import com.zunder.smart.MyApplication;
import com.zunder.smart.socket.info.ResponseInfo;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.utils.Base64;
import com.zunder.smart.MyApplication;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;

/**
 * Created by Administrator on 2017/11/27.
 */

public class RAKParams {

    public static String getParams(String type,String toID,int state){
        // {"MasterID":"342E31DF34EA0F23","ToID":"00000000","MsgType":"Login","MsgState":1,"Content":"登录成功","CreateTime":"2017-07-16 10:34:21"}

        ResponseInfo responseInfo=new ResponseInfo();
        responseInfo.setMasterID(SystemInfo.getMasterID(MyApplication.getInstance()));
        responseInfo.setToID(toID);
        responseInfo.setMsgType(type);
        responseInfo.setMsgState(state);
        responseInfo.setContent("连接到RAK小网关");
        responseInfo.setCreateTime(AppTools.getCurrentTime());
        // return Base64.encode(JSONHelper.toJSON(responseInfo).getBytes());
        return JSONHelper.toJSON(responseInfo);
    }
}
