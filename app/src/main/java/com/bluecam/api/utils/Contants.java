package com.bluecam.api.utils;

import android.content.Context;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import java.util.regex.Pattern;


public class Contants
{
    public static String getOnlineStatusString(int status, Context mContext)
    {
        String onLineMsg = "";
        if(status == CameraContants.DeviceStatus.DEVICE_STATUS_CONNECTING)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_connecting);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_NOT_ON_LINE)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_offline);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_CONNECT_TIMEOUT)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_connect_timeout);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_DISCONNECT)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_disconnect);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_CONNECT_ERRER)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_connect_failed);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_ON_LINE)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_online);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_INVALID_ID)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_invalid_id);
        }
        else if(status == CameraContants.DeviceStatus.DEVICE_STATUS_USER_ERR)
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_user_pwd_err);
        }
        else
        {
            onLineMsg = mContext.getResources().getString(R.string.camera_status_connecting);
        }
        return  onLineMsg;
    }

    public static String getAlarmMsgForType(Context context, int type)
    {
        String msg = "";
        if(type == CameraContants.AlarmType.NET_EVENT_ALARM_MOTION_START){
            msg = context.getResources().getString(R.string.alarm_move_lable);
        }
        else if(type == CameraContants.AlarmType.NET_EVENT_ALARM_GPIO_START){
            msg = context.getResources().getString(R.string.alerm_gpio_lable);
        }
        else if(type == CameraContants.AlarmType.NET_EVENT_ALARM_AUDIO_START){
            msg = context.getResources().getString(R.string.alarm_audio_lable);
        }
        else if(type == CameraContants.AlarmType.NET_EVENT_ALARM_IR){
            msg = context.getResources().getString(R.string.alerm_pir_lable);
        }
        return  msg;
    }

    public static String formatNum(int a){
        if(a>9){
            return String.valueOf(a);
        }
        else {
            return "0"+ String.valueOf(a);
        }
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
