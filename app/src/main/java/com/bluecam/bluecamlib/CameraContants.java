package com.bluecam.bluecamlib;


/**
 * Created by Administrator on 2017/6/15.
 */

public final class CameraContants
{
    public final class Resolution
    {
        public static final int HIGH = 0; //高清
        public static final int MID = 1;  //标清
        public static final int LOW = 2;  //普通
    }

    public final class AlarmType
    {
        public static final int NET_EVENT_ALARM_MOTION_START = 0x20;		// 发生移动侦测
        public static final int NET_EVENT_ALARM_GPIO_START	 = 0x21;		// 发生GPIO警报
        public static final int NET_EVENT_ALARM_AUDIO_START	 = 0x24;		// 发生声音警报
        public static final int NET_EVENT_ALARM_IR			 = 0x25;		// 红外警报信息
    }
    public final class DeviceStatus
    {
        public static final int DEVICE_STATUS_UNKNOWN = 0xffffffff;
        public static final int DEVICE_STATUS_CONNECTING = 0;//连接中
        public static final int DEVICE_STATUS_USER_ERR = 1;//用户名或者密码错误
        public static final int DEVICE_STATUS_INVALID_ID = 5;//无效ID
        public static final int DEVICE_STATUS_NOT_ON_LINE = 9;//不在线
        public static final int DEVICE_STATUS_CONNECT_TIMEOUT = 10;//连接超时
        public static final int DEVICE_STATUS_DISCONNECT = 11;//断开
        public static final int DEVICE_STATUS_CHECK_ACCOUNT = 12;//检测账号
        public static final int DEVICE_STATUS_ON_LINE = 100; //在线
        public static final int DEVICE_STATUS_CONNECT_ERRER = 101;//连接错误

        public static  final int DEVICE_STATUS_CREATE_ERR = 20; //创建失败
        public static  final int DEVICE_STATUS_OPEN_ERR = 21; //创建失败

    }

    public final class Control
    {
        //上下循环
        public static final int CMD_PTZ_UP_DOWN = 26;
        //上下循环结束
        public static final int CMD_PTZ_UP_DOWN_STOP = 27;
        //左右循环
        public static final int CMD_PTZ_LEFT_RIGHT = 28;
        //左右循环结束
        public static final int CMD_PTZ_LEFT_RIGHT_STOP = 29;

        public static final int CMD_PTZ_UP = 0;  //上
        public static final int CMD_PTZ_UP_STOP = 1;//上结束
        public static final int CMD_PTZ_DOWN = 2; // 下
        public static final int CMD_PTZ_DOWN_STOP = 3;//下结束
        public static final int CMD_PTZ_LEFT = 4; //左
        public static final int CMD_PTZ_LEFT_STOP = 5; //左结束
        public static final int CMD_PTZ_RIGHT = 6; // 右
        public static final int CMD_PTZ_RIGHT_STOP = 7; //右结束

    }

    public final class ParamKey
    {
        public static final int SET_NETWORK_PARAM_KEY         = 0x2000;       //设置网络参数
        public static final int GET_NETWORK_PARAM_KEY         = 0x2001;       // 获取网络参数
        public static final int GET_CAMERA_PARAM_KEY          = 0x2025;       //获取摄像机参数
        public static final int SET_CAMERA_PARAM_KEY          = 0x2026;       //设置摄像机参数

        public static final int GET_SNAPSHOT_PARAM_KEY        = 0x270E;       //抓拍

        public static final int GET_STATUS_PARAM_KEY          = 0x2701;      //获取摄像机状态
        public static final int SET_CAMERA_NAME_PARAM_KEY     = 0x2702;      //设置摄像机名称

        public static final int GET_USERPWD_PARAM_KEY         = 0x2003;      //获取用户名密码
        public static final int SET_USERPWD_PARAM_KEY         = 0x2002;      //设置用户名密码

        public static final int SET_WIFI_PARAM_KEY            = 0x2012;      //设置WIFI状态
        public static final int GET_WIFI_PARAM_KEY            = 0x2013;      //获取WIFI状态
        public static final int GET_WIFI_LIST_PARAM_KEY       = 0x2014;      //获取WIFI列表

        public static final int SET_ALARM_PARAM_KEY           = 0x2017;      //设置报警参数
        public static final int GET_ALARM_PARAM_KEY           = 0x2018;      //获取报警参数

        public static final int GET_RECORDSCH_PARAM_KEY       = 0x2021;      //获取sd卡状态
        public static final int SET_RECORDSCH_PARAM_KEY       = 0x2022;      //设置sd卡状态

        public static final int SET_SDFORMAT_PARAM_KEY        = 0x2024;      // 格式化sd卡

        public static final int SET_DATETIME_PARAM_KEY        = 0x2015;     //设置设备时钟参数
        public static final int GET_DATETIME_PARAM_KEY        = 0x2016;     //获取设备时钟参数

        public static final int SET_MAIL_PARAM_KEY            = 0x2008;     //设置邮箱参数
        public static final int GET_MAIL_PARAM_KEY            = 0x2009;     //获取邮箱参数

        public static final int SET_ALARM_TIME_PARAM_KEY      =	0x2740;		// 设置获取报警时间段
        public static final int GET_ALARM_TIME_PARAM_KEY      = 0x2741;     // 获取报警时间段

        public static final int GET_IPC_RUN_TIME_PARAM_KEY    = 0x8000;     //获取设备运行时间状态

        public static final int GET_RECORD_TIME_PARAM_KEY     = 0x8001;     //获取录像定时时间段
        public static final int SET_RECORD_TIME_PARAM_KEY     = 0x8002;     //设置录像定时时间段

        public static final int SET_VOICE_LANGUAGE_PARAM_KEY = 0x271B;      //设置设备语音提示参数
        public static final int GET_VOICE_LANGUAGE_PARAM_KEY = 0x271c;      //获取设备语音提示参数
    }


}
