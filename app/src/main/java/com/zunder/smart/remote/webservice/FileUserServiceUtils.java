package com.zunder.smart.remote.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FileUserServiceUtils {
    static String endPoint = Constants.HTTPS+"Service/FileUserService.asmx/";
    public static String getFileUsers(String MasterID) throws Exception {
        String methodName = "getFileUsers";
        HashMap data=new HashMap();
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String getUserType(int Id)
    {
        String methodName = "getUserType";
        HashMap data=new HashMap();
        data.put("Id",Id);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String insertFileUsers(String UserName,int UserType,String UserTel, String UserNick,String MasterID) {
        String methodName = "insertFileUsers";
        HashMap data=new HashMap();
        data.put("UserName",UserName);
        data.put("UserType",UserType);
        data.put("UserTel",UserTel);
        data.put("UserNick",UserNick);
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String updateFileUsers(String UserName,int UserType,String UserTel, String UserNick,String MasterID,int Id) {
        String methodName = "updateFileUsers";
        HashMap data=new HashMap();
        data.put("Id",Id);
        data.put("UserName",UserName);
        data.put("UserType",UserType);
        data.put("UserTel",UserTel);
        data.put("UserNick",UserNick);
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
    public static String delFileUsers(int Id)
    {
        String methodName = "delFileUsers";
        HashMap data=new HashMap();
        data.put("Id",Id);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
}
