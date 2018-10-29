package com.zunder.smart.remote.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FileListServiceUtils {
    static String endPoint = Constants.HTTPS+"Service/FileListService.asmx/";
    public static String getFileList(int UserId) throws Exception {
        String methodName = "getFileList";
        HashMap data=new HashMap();
        data.put("UserId",UserId);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String delFileList(int Id)
    {
        String methodName = "delFileList";
        HashMap data=new HashMap();
        data.put("Id",Id);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }

    public static String insertFileList(int userId,int infoId) {
        String methodName = "insertFileList";
        HashMap data=new HashMap();
        data.put("userId",userId);
        data.put("infoId",infoId);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
    public static String getFindFileByType(int TypeId,int UserId,String MasterID) {
        String methodName = "getFindFileByType";
        HashMap data=new HashMap();
        data.put("TypeId",TypeId);
        data.put("UserId",UserId);
        data.put("MasterID",MasterID);
        String url=endPoint+methodName;
        return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
    }
}
