package com.zunder.smart.remote.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;
/**
 * Created by Administrator on 2018/5/4.
 */

public class FileTimeListServiceUtils {
    static String endPoint = Constants.HTTPS+"Service/FileTimeListService.asmx/";
    public static String getControlCmds(int Id) throws Exception {
        String methodName = "getControlCmds";
        HashMap data=new HashMap();
        data.put("Id",Id);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
    public static String insertFileTimeList(int FileIndex,int ControlIndex,int TimeId,String ControlTime,String AssignDate,int CycleIndex,int TypeId,String ControlHex, String ControlDevice, String ControlMasterID)  {
        String methodName = "insertFileTimeList";
        HashMap data=new HashMap();
        data.put("FileIndex",FileIndex);
        data.put("ControlIndex",ControlIndex);
        data.put("TimeId",TimeId);
        data.put("ControlTime",ControlTime);
        data.put("CycleIndex",CycleIndex);
        data.put("TypeId",TypeId);
        data.put("AssignDate",AssignDate);
        data.put("ControlHex",ControlHex);
        data.put("ControlDevice",ControlDevice);
        data.put("ControlMasterID",ControlMasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
    public static String updateFileTimeList(int Id,int FileIndex,int ControlIndex,int TimeId,String ControlTime,String AssignDate,int CycleIndex,int TypeId,String ControlHex, String ControlDevice, String ControlMasterID) {
        String methodName = "updateFileTimeList";
        HashMap data=new HashMap();
        data.put("Id",Id);
        data.put("FileIndex",FileIndex);
        data.put("ControlIndex",ControlIndex);
        data.put("TimeId",TimeId);
        data.put("ControlTime",ControlTime);
        data.put("CycleIndex",CycleIndex);
        data.put("TypeId",TypeId);
        data.put("AssignDate",AssignDate);
        data.put("ControlHex",ControlHex);
        data.put("ControlDevice",ControlDevice);
        data.put("ControlMasterID",ControlMasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }



    public static String getFileTimeLists(int TimeId){
        String methodName = "getFileTimeLists";
        HashMap data=new HashMap();
        data.put("TimeId",TimeId);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
    public static String delFileTimeList(int Id){
        String methodName = "delFileTimeList";
        HashMap data=new HashMap();
        data.put("Id",Id);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
}
