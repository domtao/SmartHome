package com.zunder.smart.gateway.bean;

public class ContantsModel
{
  public static final int HighResolution = 0;
  public static final int LowResolution = 2;
  public static final int MidResolution = 1;
  public static final String SERVER = "EBGJEBBBKAJIGEJIEPGIFKEFHAMOHANNGHFBBNCEBKJCLKLIDEACCKOOGILNJHLLAMMALEDAODMMAOCCJONDJJ";
  public static final int WvrHighResolution = 0;
  public static final int WvrLowResolution = 2;
  public static final int WvrMidResolution = 1;

  public final class Alarm
  {
    public static final int AUDIO_ALARM = 36;
    public static final int BELLITF_ALARM = 40;
    public static final int DOORBELL_ALARM = 41;
    public static final int DOOR_ALARM = 38;
    public static final int GPIO_ALARM = 33;
    public static final int IR_ALARM = 37;
    public static final int MOTION_ALARM = 32;
    public static final int NET_EVENT_ALARM_BEDROOM1 = 84;
    public static final int NET_EVENT_ALARM_BEDROOM2 = 85;
    public static final int NET_EVENT_ALARM_BEDROOM3 = 86;
    public static final int NET_EVENT_ALARM_BEDROOM4 = 87;
    public static final int NET_EVENT_ALARM_DOORBELL1 = 68;
    public static final int NET_EVENT_ALARM_DOORBELL2 = 69;
    public static final int NET_EVENT_ALARM_DOORBELL3 = 70;
    public static final int NET_EVENT_ALARM_DOORBELL4 = 71;
    public static final int NET_EVENT_ALARM_HALL1 = 72;
    public static final int NET_EVENT_ALARM_HALL2 = 73;
    public static final int NET_EVENT_ALARM_HALL3 = 74;
    public static final int NET_EVENT_ALARM_HALL4 = 75;
    public static final int NET_EVENT_ALARM_OTHER1 = 92;
    public static final int NET_EVENT_ALARM_OTHER2 = 93;
    public static final int NET_EVENT_ALARM_OTHER3 = 94;
    public static final int NET_EVENT_ALARM_OTHER4 = 95;
    public static final int NET_EVENT_ALARM_REMOTE_CONTROL1 = 64;
    public static final int NET_EVENT_ALARM_REMOTE_CONTROL2 = 65;
    public static final int NET_EVENT_ALARM_REMOTE_CONTROL3 = 66;
    public static final int NET_EVENT_ALARM_REMOTE_CONTROL4 = 67;
    public static final int NET_EVENT_ALARM_VERANDA1 = 80;
    public static final int NET_EVENT_ALARM_VERANDA2 = 81;
    public static final int NET_EVENT_ALARM_VERANDA3 = 82;
    public static final int NET_EVENT_ALARM_VERANDA4 = 83;
    public static final int NET_EVENT_ALARM_WINDOW1 = 76;
    public static final int NET_EVENT_ALARM_WINDOW2 = 77;
    public static final int NET_EVENT_ALARM_WINDOW3 = 78;
    public static final int NET_EVENT_ALARM_WINDOW4 = 79;
    public static final int NET_EVENT_ALARM_YARD1 = 88;
    public static final int NET_EVENT_ALARM_YARD2 = 89;
    public static final int NET_EVENT_ALARM_YARD3 = 90;
    public static final int NET_EVENT_ALARM_YARD4 = 91;
    public static final int SMOKE_ALARM = 39;
    public static final int WVR_GPIO_ALARM = 96;

    public Alarm()
    {
    }
  }

  public final class Cmd
  {
    public static final int CMD_PTZ_DOWN = 2;
    public static final int CMD_PTZ_DOWN_STOP = 3;
    public static final int CMD_PTZ_LEFT = 4;
    public static final int CMD_PTZ_LEFT_RIGHT = 28;
    public static final int CMD_PTZ_LEFT_RIGHT_STOP = 29;
    public static final int CMD_PTZ_LEFT_STOP = 5;
    public static final int CMD_PTZ_MINUS = 253;
    public static final int CMD_PTZ_PLUS = 252;
    public static final int CMD_PTZ_QJ_STOP = 254;
    public static final int CMD_PTZ_RIGHT = 6;
    public static final int CMD_PTZ_RIGHT_STOP = 7;
    public static final int CMD_PTZ_UP = 0;
    public static final int CMD_PTZ_UP_DOWN = 26;
    public static final int CMD_PTZ_UP_DOWN_STOP = 27;
    public static final int CMD_PTZ_UP_STOP = 1;

    public Cmd()
    {
    }
  }

  public final class DeviceStatus
  {
    public static final int PPPP_STATUS_CHECK_ACCOUNT = 12;
    public static final int PPPP_STATUS_CONNECTING = 0;
    public static final int PPPP_STATUS_CONNECT_ERRER = 101;
    public static final int PPPP_STATUS_CONNECT_TIMEOUT = 10;
    public static final int PPPP_STATUS_DISCONNECT = 11;
    public static final int PPPP_STATUS_INVALID_ID = 5;
    public static final int PPPP_STATUS_NOT_ON_LINE = 9;
    public static final int PPPP_STATUS_ON_LINE = 100;
    public static final int PPPP_STATUS_UNKNOWN = -1;
    public static final int PPPP_STATUS_USER_ERR = 1;

    public DeviceStatus()
    {
    }
  }

  public final class DeviceType
  {
    public static final int CAMERA = 0;
    public static final int CARDCAMERA = 5;
    public static final int CLOUD_BOX = 1;
    public static final int DOORBELL = 4;
    public static final int QJ = 6;
    public static final int SMART_CAMERA = 3;
    public static final int SWITCH = 2;

    public DeviceType()
    {
    }
  }

  public final class MsgType
  {
    public static final int ALARM = 3;
    public static final int IMAGE = 1;
    public static final int VIDEO = 2;

    public MsgType()
    {
    }
  }

  public final class Param
  {
    public static final int DOOR_BELL_CONTROL = 10006;
    public static final int GET_CAMERA_PARAMS = 8229;
    public static final int GET_DOOR_BELL_VOICE_LANGUAGE = 10012;
    public static final int GET_LOGIN_PARAM = 9989;
    public static final int GET_PARAM_ALARM = 8216;
    public static final int GET_PARAM_ALARM_CAM_LIST = 10003;
    public static final int GET_PARAM_ALARM_PTZ_LIST = 10015;
    public static final int GET_PARAM_APWIFI = 9987;
    public static final int GET_PARAM_DATETIME = 8214;
    public static final int GET_PARAM_DDNS = 8197;
    public static final int GET_PARAM_FTP = 8199;
    public static final int GET_PARAM_MAIL = 8201;
    public static final int GET_PARAM_NETWORK = 8193;
    public static final int GET_PARAM_ONLINE_USER = 9997;
    public static final int GET_PARAM_PTZ = 8209;
    public static final int GET_PARAM_RECORDPATH = 8217;
    public static final int GET_PARAM_RECORDSCH = 8225;
    public static final int GET_PARAM_SDFORMAT = 8227;
    public static final int GET_PARAM_SNAPSHOT = 9998;
    public static final int GET_PARAM_STATUS = 9985;
    public static final int GET_PARAM_USERINFO = 8195;
    public static final int GET_PARAM_WIFI = 8211;
    public static final int GET_PARAM_WIFI_LIST = 8212;
    public static final int REBOOT_EDV = 9984;
    public static final int SEARCH_RECORD_FILE = 9990;
    public static final int SET_CAMERA_PARAMS = 8230;
    public static final int SET_DOOR_BELL_MASTER_BUSY = 10013;
    public static final int SET_DOOR_BELL_VOICE_LANGUAGE = 10011;
    public static final int SET_PARAM_ALARM = 8215;
    public static final int SET_PARAM_ALARM_CAM_CODE = 10005;
    public static final int SET_PARAM_ALARM_CAM_PRESET_CTRL = 10004;
    public static final int SET_PARAM_ALIAS = 9986;
    public static final int SET_PARAM_APWIFI = 9988;
    public static final int SET_PARAM_DATETIME = 8213;
    public static final int SET_PARAM_DDNS = 8196;
    public static final int SET_PARAM_FTP = 8198;
    public static final int SET_PARAM_MAIL = 8200;
    public static final int SET_PARAM_NETWORK = 8192;
    public static final int SET_PARAM_PTZ = 8208;
    public static final int SET_PARAM_REBOOT = 10000;
    public static final int SET_PARAM_RECORDPATH = 8224;
    public static final int SET_PARAM_RECORDSCH = 8226;
    public static final int SET_PARAM_SDFORMAT = 8228;
    public static final int SET_PARAM_USERINFO = 8194;
    public static final int SET_PARAM_WIFI = 8210;

    public Param()
    {
    }
  }

  public final class WvrParam
  {
    public static final int GET_PARAM_WIFI_LIST = 8463;
    public static final int GET_SMART_PLUG_RELAY = 8469;
    public static final int GET_SMART_PLUG_TASK_SCHUDULE = 8470;
    public static final int NAS_GET_PARAM_ALARM = 8457;
    public static final int NAS_GET_PARAM_ALARM_DEVICE_LIST = 8478;
    public static final int NAS_GET_PARAM_ALARM_STUDY = 8474;
    public static final int NAS_GET_PARAM_APWIFI = 8461;
    public static final int NAS_GET_PARAM_CAMERA_LIST = 8467;
    public static final int NAS_GET_PARAM_CHANNEL_SCENE = 8479;
    public static final int NAS_GET_PARAM_DATETIME = 8452;
    public static final int NAS_GET_PARAM_MAIL = 8458;
    public static final int NAS_GET_PARAM_MOUNT_CAMERA = 8450;
    public static final int NAS_GET_PARAM_PLUG_MODE = 8473;
    public static final int NAS_GET_PARAM_RECORDSCH = 8455;
    public static final int NAS_GET_PARAM_STATUS = 8449;
    public static final int NAS_GET_PARAM_USERINFO = 8466;
    public static final int NAS_GET_PARAM_WIFI = 8462;
    public static final int NAS_SEARCH_ALARM_DEVICE = 8477;
    public static final int NAS_SET_FORMAT_DISK = 8206;
    public static final int NAS_SET_PARAM_ALARM = 8205;
    public static final int NAS_SET_PARAM_ALARM_DEVICE_CONTEXT = 8220;
    public static final int NAS_SET_PARAM_ALARM_MODULE_CONTROL = 8217;
    public static final int NAS_SET_PARAM_ALARM_STUDY = 8215;
    public static final int NAS_SET_PARAM_APWIFI = 8201;
    public static final int NAS_SET_PARAM_CAMERA_CONTROL = 8216;
    public static final int NAS_SET_PARAM_CHANNEL_SCENE = 8221;
    public static final int NAS_SET_PARAM_DATETIME = 8198;
    public static final int NAS_SET_PARAM_MAIL = 8196;
    public static final int NAS_SET_PARAM_MOUNT_CAMERA = 8202;
    public static final int NAS_SET_PARAM_RECORDSCH = 8199;
    public static final int NAS_SET_PARAM_USERINFO = 8193;
    public static final int SET_PARAM_WIFI = 8197;
    public static final int SET_SMART_PLUG_RELAY = 8209;
    public static final int SET_SMART_PLUG_TASK = 8210;

    public WvrParam()
    {
    }
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.ContantsModel
 * JD-Core Version:    0.6.2
 */