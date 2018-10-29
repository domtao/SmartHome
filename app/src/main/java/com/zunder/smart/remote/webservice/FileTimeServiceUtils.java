package com.zunder.smart.remote.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FileTimeServiceUtils {
    static String endPoint = Constants.HTTPS+"Service/FileTimeService.asmx/";
    public static String getFileTimes(String MasterID) throws Exception {
        String methodName = "getFileTimes";
        HashMap data=new HashMap();
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String insertFileTimes(String TimeName,String StartTime, String EndTime,int Cycle, String AssignDate, String MasterID)
    {
        String methodName = "insertFileTimes";
        HashMap data=new HashMap();
        data.put("TimeName",TimeName);
        data.put("StartTime",StartTime);
        data.put("EndTime",EndTime);
        data.put("Cycle",Cycle);
        data.put("AssignDate",AssignDate);
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String updateFileTimes(int Id,String TimeName,String StartTime, String EndTime,int Cycle, String AssignDate, String MasterID)
    {
        String methodName = "updateFileTimes";
        HashMap data=new HashMap();
        data.put("Id",Id);
        data.put("TimeName",TimeName);
        data.put("StartTime",StartTime);
        data.put("EndTime",EndTime);
        data.put("Cycle",Cycle);
        data.put("AssignDate",AssignDate);
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
    public static String delFileTime(int Id){
        String methodName = "delFileTime";
        HashMap data=new HashMap();
        data.put("Id",Id);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
}
