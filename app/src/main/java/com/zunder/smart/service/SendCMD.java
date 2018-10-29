package com.zunder.smart.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.tv.ChannelAddActivity;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;

import com.zunder.smart.model.BindProject;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.History;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.HanziToPinyin;
import com.zunder.smart.tools.HanziToPinyin.Token;
import com.zunder.smart.tools.JSONHelper;

import android.util.Log;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class SendCMD {
    private static int type_window = 12;
    public static Map<String, String> map;
    //static StringBuffer sb = new StringBuffer();
    private static SendCMD install;
    //private static int wakeTime = 0;
    private static int delayPrctime[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static String SubControlWords;
    private static int delaySubControl=0;
    private static boolean setTimeRun = true;
    private static int loopIndex = 0;
    private static boolean oneSecond = false;
    private static int nowSec = 0;
    private static int nowMin = 0;
    private static int showErrorT = 0;
    private int randomKey = 0;
    private String modecode = "00";
    private String modetime = "00";
    private int ActionFlag;
    private String spaceNameStr="";
    //private String mode = "0";
    private String deviceProductCode = "00";
    private String modeIo = "00";
    private String modeSthh = "00";
    private String modeStmm = "00";
    private String modeEndhh = "00";
    private String deviceIO = "0";
    private String modeEndmm = "00";
    private String modeMonth = "00";
    private String allControl="00";
    //private String deviceName;
    private String devieID = "";// 设备ID
    //public static int FreeBackPro = 0;
    private String words_ls = "";
    private int deviceValue;
    private int cStartPos;
    private String devieMid1 = "";
    private String devieMid2 = "";
    //public static String PlayFreeBack = "";
    public static History history;
    private String ModeGateMac="";
    private int ModeGateType=0;
    public static synchronized SendCMD getInstance() {
        if (install == null) {
            install = new SendCMD();
            map = new HashMap<String, String>();
            history=new History();
            install.startTime();
        }
        return install;
    }
    public SendCMD() {
    }

    private void startTime() {

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yy-MM-dd HH:mm:ss");
                        String ReturnStr = dateFormat.format(now);
                        int mGetVal = Integer.parseInt(ReturnStr.substring(15,
                                17));
                        if (nowSec != mGetVal) {
                            nowSec = mGetVal;
                            if (setTimeRun) {
                                if (!oneSecond) {
                                    oneSecond = true;
                                }
                            }
                            mGetVal = Integer.parseInt(ReturnStr.substring(12,
                                    14));
                            if (nowMin != mGetVal) {
                                nowMin = mGetVal;
                            }
                            if (showErrorT > 0) {
                                showErrorT--;
                            }
							/*
							String tempValue = "1";
							if (tempValue.equals("1") || tempValue == "1") {
								if (wakeTime > 0) {
									if (wakeTime > 1)
										wakeTime--;
									if (wakeTime == 1) {
										wakeTime = 0;
										HandlerThread handlerThread = new HandlerThread(
												"handler_thread");
										handlerThread.start();
									} else {
									}
								}
							}
							*/
                        }
                        if (setTimeRun && oneSecond) {
                            if (delayPrctime[loopIndex] > 0) {
                                delayPrctime[loopIndex]--;
                                if (delayPrctime[loopIndex] == 0) {
                                    // sendCmd(delayPrcStr[loopIndex], true);
                                    sendCMD(0, setWords[loopIndex], null);
                                    setWords[loopIndex] = "";
                                    Log.e("ys", AppTools.getCurrentTime()+"error");
                                }
                            }
                            if(delaySubControl>0){
                                delaySubControl--;
                                if(delaySubControl==0){
                                    sendCMD(0,SubControlWords , null);
                                }
                            }

                            loopIndex++;
                            if (loopIndex >= 25) {
                                loopIndex = 0;
                                oneSecond = false;
                            }
                        }

                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String ReturnAct = "";
    //private int sendNumber = 0;
    private String deviceCmd = "";
    private boolean isSendMode;
    private int MultiFlag;

    public String sendCMD(int source, String netCmd, Device device) {
        if(history==null){
            history=new History();
        }
        history.setHistoryName(netCmd);
        type_window = 12;
        //sendNumber = 0;

        if ((source > 0) && (source < 128)) {
            MultiFlag=0;
            String modeSthhTamp = "00";
            String modeStmmTamp = "00";
            String modeEndhhTamp = "00";
            String modeEndmmTamp = "00";
            if(device!=null) {
                ModeGateMac = device.getDeviceBackCode();
                ModeGateType=device.getCmdDecodeType();
            }
            List<Mode> modes = ModeFactory.getInstance().getAll();
            for (int index = 0; index < modes.size(); index++) {
                Mode mode = modes.get(index);
                if (netCmd.contains(mode.getModeName())) {// 有识别词
                    modecode = toHex(mode.getModeCode());
                    modeIo = toHex(mode.getModeLoop());
                    if (source == 1) {
                        // 获取空间集合
                        List<ModeList> modeList = ModeListFactory.getInstance()
                                .getModeDevice(mode.getId());

                        if (modeList.size() > 0) {
                            String[] startTime = mode.getStartTime().split(":");
                            String[] endTime = mode.getEndTime().split(":");
                            if (startTime.length > 1) {
                                modeSthhTamp = startTime[0];
                                modeStmmTamp = startTime[1];
                            }
                            if (endTime.length > 1) {
                                modeEndhhTamp = endTime[0];
                                modeEndmmTamp = endTime[1];
                            }
                            deviceCmd = "*M80" + modecode + "00" + modeIo
                                    + "FFFF0000000000000000";
                            sendToCurrtneGaway(ModeGateType,ModeGateMac,deviceCmd);

                            for (int j = 0; j < modeList.size(); j++) {
                                ModeList deviceMode = modeList.get(j);
                                String period = deviceMode.getModePeriod();
                                int timepos1 = period.indexOf("--");
                                String[] periodArr = new String[2];
                                modeSthh=modeSthhTamp;
                                modeStmm=modeStmmTamp;
                                modeEndhh=modeEndhhTamp;
                                modeEndmm=modeEndmmTamp;
                                if (timepos1 > 0) {
                                    periodArr = period.split("--");
                                    String[] stareArr = periodArr[0].split(":");
                                    String[] endArr = periodArr[1].split(":");
                                    if (stareArr.length > 1) {
                                        if(Integer.parseInt(stareArr[0])!=Integer.parseInt(stareArr[1])){
                                            modeSthh=stareArr[0];
                                            modeStmm=stareArr[1];
                                        }
                                    }
                                    if (endArr.length > 1) {
                                        if(Integer.parseInt(endArr[0])!=Integer	.parseInt(endArr[1])){
                                            modeEndhh=endArr[0];
                                            modeEndmm=endArr[1];
                                        }
                                    }
                                }
                                modetime = "00";
                                String delayed =AppTools.getNumbers(deviceMode.getModeDelayed().replace("秒",""));

                                if (Integer.parseInt(delayed) > 0) {
                                    modetime = toHex(Integer
                                            .parseInt(delayed));
                                }
                                //String moth=deviceMode.getCreationTime();
                                int startM=0;
                                int endM=0;
                                if(deviceMode.getBeginMonth().length()>0){
                                    startM=Integer.valueOf(deviceMode.getBeginMonth().substring(0, 1), 16);
                                }
                                if(deviceMode.getEndMonth().length()>0){
                                    endM=Integer.valueOf(deviceMode.getEndMonth().substring(0, 1), 16);
                                }
                                modeMonth=toHex((startM*16)+endM);

                                isSendMode = false;
                                if (deviceMode.getDeviceTypeKey()==20) {
                                    boolean logicCmd=false;
                                    String[]  Logic={"设置在家安防","*C0019FA07FF0000EB010000","设置离家安防","*C0019FA07FF0000EC010000","设置睡眠安防","*C0019FA07FF0000ED010000","假如在家安防","*C0019FA07FF0000EB000000", "假如离家安防","*C0019FA07FF0000EC000000","假如睡眠安防","*C0019FA07FF0000ED000000","条件不成立","*C0019FA07FF0000EF000000","逻辑结束","*C0019FA07FF0000E0000000"
                                    };
                                    for (int i = 0; i < Logic.length; i+=2) {
                                        if (deviceMode.getModeFunction().contains(Logic[i])) {// 有识别词) {//有识别词
                                            sendMssageAF(source, Logic[i + 1]);
                                            logicCmd=true;
                                        }
                                    }
                                    if(logicCmd==false) {
                                        String AreStr = "";
                                        if (deviceMode.getModeFunction().equals("灯光全开") || deviceMode.getModeFunction().equals("灯光全关")
                                                || deviceMode.getModeFunction().equals("设备全关") || deviceMode.getModeFunction().equals("设备全开")) {
                                            AreStr = "主页";
                                        }
                                        sendCmd(source,
                                                AreStr + deviceMode.getModeFunction(),
                                                null);
                                    }
                                } else {
                                    String cmdStr=deviceMode.getRoomName()+deviceMode.getDeviceName()+deviceMode.getModeAction()+deviceMode.getModeFunction()+deviceMode.getModeTime();
                                    sendCmd(source, cmdStr, null);
                                }
                            }
                        }
                        // deviceCmd="*M04";
                        deviceCmd = "*M04" + modecode + "00" + modeIo
                                + "FFFF0000000000000000";
                        sendToCurrtneGaway(ModeGateType,ModeGateMac,deviceCmd);
                    }
                    if (source == 2) {
                        deviceCmd = "*M00" + modecode + "00" + modeIo
                                + "FFFF0000000000000000";
                        sendToCurrtneGaway(ModeGateType,ModeGateMac,deviceCmd);
                    } else if (source == 3) {
                        // deviceCmd="*M83";
                        deviceCmd = "*M83" + modecode + "00" + modeIo
                                + "FFFF0000000000000000";
                        sendToCurrtneGaway(ModeGateType,ModeGateMac,deviceCmd);
                    }
                    deviceCmd = "*M85" + modecode + "00" + modeIo
                            + "FFFF0000000000000000";
                    sendToCurrtneGaway(ModeGateType,ModeGateMac,deviceCmd);
                    break;
                }
            }
        } else {

            sendCmd(source, netCmd, device);
        }
        return ReturnAct;
    }

    private Device deviceModelParam;
    //private String CampStr = "";
    private String[] setWords = new String[25];

    public void sendCmd(int modelflag, String netCmd, Device device) {
        String CampStr;
        netCmd = format(netCmd);
        String str_zl = netCmd;
        String string_zhiLing;
        //String ss;
        int bSearchFlag = 0;
        ReturnAct = "";
        allControl="00";
        if (modelflag > 127) {
            if (device != null) {
                deviceModelParam=device;
                deviceProductCode = device.getProductsCode();
                Log.e("erroe", deviceProductCode);
                deviceValue = Integer.parseInt(deviceProductCode, 16);
                // [self HexToTen:deviceProductCode];
                devieID = device.getDeviceID();
                devieMid1 = "00";
                devieMid2 = "00";
                if (devieID.length() == 6) {
                    devieID = device.getDeviceID().substring(0, 2);
                    devieMid1 = device.getDeviceID().substring(2, 4);
                    devieMid2 = device.getDeviceID().substring(4, 6);
                }
                deviceIO = "0";
                if (device.getDeviceIO().length() > 0) {
                    deviceIO = device.getDeviceIO();
                }
                if(device!=null) {
                    ModeGateMac = device.getDeviceBackCode();
                    ModeGateType = device.getCmdDecodeType();
                }
            }
            String cmd = "";
            switch (modelflag) {
             case 256: {
                    String arrayStr[] = netCmd.split(":");
                    if (arrayStr[0].equals("00")) {
                        TcpSender.sendMssageAF("*N00"+arrayStr[1]);
                    } else if (arrayStr[0].equals("01")) {
                        TcpSender.sendMssageAF("*N01" + arrayStr[1]);
                    } else if (arrayStr[0].equals("02")) {
                        TcpSender.sendMssageAF("*N020000000000000000");
                    }
                }
                break;
                case 255:// 发送获取设备状态指令
                    GetDeviceState(netCmd);
                    break;
                case 254:// 设置內存數據 //joe 2018/0626修改完成
                    //arrayStr[0]命令  ,arrayStr[2]內存位置, arrayStr[3]位元 ,arrayStr[1]動作
                {
                    String arrayStr[] = netCmd.split(":");
                    cmd = "*C00" + arrayStr[0] + "FA" + deviceProductCode + devieID + devieMid1 + devieMid2 +
                            arrayStr[2] + arrayStr[3] + arrayStr[1] + "00";
                    TcpSender.sendMssageAF(cmd);
                }
                break;
                case 253:// 获取內存數據
                {
                    //arrayStr1[0]內存位置
                    String array[] = netCmd.split(":");
                    cmd = "*C0009FA" + deviceProductCode + devieID + devieMid1 + devieMid2 + "01"
                            + array[0] + array[1] + "00";
                    TcpSender.sendMssageAF(cmd);
                }
                break;
                case 252:// 设置互控
                    break;
                case 250:
                    ActionFlag = 0;
                    words_ls=device.getRoomName()+device.getDeviceName();
                    checkPeriodForDeviceModel(device, 0, netCmd);
                    break;
                case 249:// 定時程序
                    // *T83000000 00 01 0B 25 000000000000
                    String para;
                    if (deviceValue == 255) {
                        deviceIO = "F" + deviceIO;
                        para = toHex(device.getSceneId());
                    } else {
                        deviceIO = "0" + deviceIO;
                        para = "00";
                    }
                    cmd = "*T83000000" + deviceProductCode + devieID + devieMid1 + devieMid2
                            + deviceIO + para + "00000000";
                    TcpSender.sendMssageAF(cmd);
                    break;
                case 248:// 定時程序
                    String[] array = netCmd.split(":");
                    String Action ;
                    String Para ;
                    String event = array[3];
                    if(AppTools.isNumeric(array[array.length - 1])) {
                        int timeType = Integer.parseInt(array[array.length - 1]);
                        if (timeType < 3) {
                            int timeVal = timeComprartion(0, 0, event);
                            Para = toHex(timeVal);
                            if (event.contains("打开")) {
                                if (timeVal > 0) {
                                    Action = "5" + deviceIO;
                                } else {
                                    Action = "2" + deviceIO;
                                }
                            } else {
                                if (timeVal > 0) {
                                    Action = "6" + deviceIO;
                                } else {
                                    Action = "1" + deviceIO;
                                }
                            }
                        } else {
                            Action = "F" + deviceIO;
                            Para = toHex(device.getSceneId());
                        }
                        if ((timeType == 1) || (timeType == 3)) {
                            cmd = "*T81" + array[2] + array[0] + array[1] + deviceProductCode + devieID + devieMid1
                                    + devieMid2 + Action + Para + "00000000";
                        } else {
                            cmd = "*T00" + array[2] + array[0] + array[1]  + deviceProductCode + devieID + devieMid1
                                    + devieMid2+ Action + Para + "000000FF";
                        }
                        TcpSender.sendMssageAF(cmd);
                    }
                    break;
                case 247:// 中繼管理
                    cmd = "";
                    switch (Integer.parseInt(netCmd, 16)) {
                        case 0:
                            cmd = "*Z00" + toHex(device.getId() >> 8)
                                    + toHex(device.getId() & 0x00FF)
                                    + deviceProductCode + devieID + devieMid1
                                    + devieMid2;
                            break;
                        case 1:
                            cmd = "*Z01" + toHex(device.getId() >> 8)
                                    + toHex(device.getId() & 0x00FF)
                                    + deviceProductCode + devieID + devieMid1
                                    + devieMid2;
                            break;
                        case 2:
                            cmd = "*Z02000000000000";
                            break;
                        case 3:
                            cmd = "*Z03000000000000";
                            break;
                        case 255:
                            cmd = "*ZFF000000000000";
                            break;
                        default:
                            break;
                    }
                    //TcpSender.sendMssageAF(cmd);
                    sendToCurrtneGaway(ModeGateType,ModeGateMac,cmd);
                    break;
                case 246: // 获取中继路由表
                    String[] array1 = netCmd.split("_"); // [netCmd
                    cmd = "";
                    String Serial = toHex(Integer.parseInt(array1[1]));
                    switch (Integer.parseInt(array1[0], 16)) {
                        case 0: // delete one
                            cmd = "*C001B" + Serial + deviceProductCode + devieID + devieMid1
                                    + devieMid2  + "0000" + toHex(device.getId() >> 8)
                                    + toHex(device.getId() & 0x00FF);
                            break;
                        case 1: // Add
                            cmd = "*C001B" + Serial + deviceProductCode + devieID + devieMid1
                                    + devieMid2 + "0100" + toHex(device.getId() >> 8)
                                    + toHex(device.getId() & 0x00FF);
                            break;
                        case 2: // info
                            cmd = "*C001BFA" + deviceProductCode + devieID + devieMid1 + devieMid2
                                    + "02" + Serial + "0000" ;
                            break;
                        case 3:
                            cmd = "*C001B" + Serial + deviceProductCode + devieID + devieMid1 + devieMid2
                                    + "0300" + toHex(device.getId() >> 8)
                                    + toHex(device.getId() & 0x00FF);
                            break;
                        case 255:// All delete
                            cmd = "*C001B" + Serial + deviceProductCode + devieID + devieMid1 + devieMid2
                                    + "FF000000" ;
                            break;
                    }
                    //TcpSender.sendMssageAF(cmd);
                    sendToCurrtneGaway(ModeGateType,ModeGateMac,cmd);
                    break;
                case 245: // 电流侦测
                    //Joe
                    String commandID=devieID + devieMid1 + devieMid2;
                    switch (Integer.parseInt(netCmd)) {
                        case 0:
                            cmd = "*C0009FA" + deviceProductCode + commandID + "05000000" ;
                            break;
                        case 1:
                            cmd = "*C0009FA" + deviceProductCode + commandID + "05010000" ;
                            break;
                        case 2:
                            cmd = "*C0009FA" + deviceProductCode + commandID + "05020000" ;
                            break;
                        case 3:
                            if (deviceValue == 5) { // IR 紅外線
                                cmd = "*C000AFA05" + commandID + "31010000" ;
                            } else {
                                cmd = "*C000AFA05" + commandID + "31"+ deviceProductCode +"0000";
                            }
                            break;
                        case 4:
                            if (deviceValue == 5) { // IR 紅外線
                                cmd = "*C0019FA05" + commandID + "A0010000" ;
                            } else if (deviceValue > 47 && deviceValue < 80) { // Air
                                cmd = "*C0019FA" + deviceProductCode + commandID + "00000000" ;
                            } else {
                                cmd = "*C0019FA" + deviceProductCode + commandID + "A00A0000" ;
                            }
                            break;
                        case 5:
                            cmd = "*C0009FA05" + commandID + "01310000" ;
                            break;
                    }
                    TcpSender.sendMssageAF(cmd);
                    break;
                case 244:
                    //搜索設備   //是否增加搜索條件?
                    if(netCmd.equals("FF")){
                        cmd = "*C0009FA" + netCmd + "FF000002000000";
                    }
                    else {
                        cmd = "*C0009" + netCmd+deviceProductCode + "FF000002000000";
                    }
                    TcpSender.sendMssageAF(cmd);
                    break;
                case 243: // 云之声搜索发送闪光声音命令

                    break;
                case 242: // 安防配对学习命令
                    //传递16进制
                    switch (Integer.valueOf(netCmd, 16)) {
                        case 0:// 获取所有安防回路命令
                            cmd = "*C0015FA1CFF000002FF0000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 1:// 配对命令
                            cmd = "*C0015FA1C" + device.getDeviceID() + "010A0000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 2:// 开启命令
                            cmd = "*C0019FA" + device.getProductsCode() + device.getDeviceID() + "20000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 3:// 关闭命令
                            cmd = "*C0019FA" + device.getProductsCode() + device.getDeviceID() + "10000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 4:// 报警解除命令
                            cmd = "*C000AFA1CFF000003000000";
                            TcpSender.sendMssageAF(cmd);
                            cmd = "*C0015FA1CFF000005000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 5://报警设置命令
                            cmd = "*C000AFA1CFF000003010000";
                            TcpSender.sendMssageAF(cmd);
                            cmd = "*C0015FA1CFF000005000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 6:// 删除
                            //檢討網關設置內的安房配對全部清除 2018/0701
                            cmd = "*C0015FA1C" + device.getDeviceID() + "00000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 7://直接定址
                            //傳進來的是選中的設備並帶安防迴路(利用設備無用的參數)
                            cmd = "*C0015" + device.getProductsCode() + "1C" + device.getDeviceID()
                                    + "03"+ device.getDeviceDigtal() + AppTools.toHex(Integer.parseInt(deviceIO)+1) + "00";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 9:// 獲取單一迴路狀態命令
                            cmd = "*C0015FA1C" + device.getDeviceID() + "05000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 10:// 獲取狀態命令
                            cmd = "*C0015FA1CFF000005000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 11://在家安防
                            cmd = "*C0015FA1CFF000005020000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 12://外出安防
                            cmd = "*C0015FA1CFF000005030000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        case 13://睡眠安防
                            cmd = "*C0015FA1CFF000005040000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                        default:
                            cmd = "*C0019FA07" + device.getDeviceID() + netCmd + "000000";
                            TcpSender.sendMssageAF(cmd);
                            break;
                    }
                    break;
                case 241: // 无源控制器第一層指令
                {
                    String[] strs = netCmd.split(":");
                    int cmdType = Integer.parseInt(strs[0]);// 命令类型
                    String random = strs[1];// 随机数
                    String keyValue = strs[2];// 键值
                    switch (cmdType) {
                        case 0://
                            cmd = "*C0051000000000000000000";
                            break;
                        case 1:
                            cmd = "*C005101" + random + "00" + keyValue + "0000000000";
                            break;
                        case 2://
                            cmd = "*C00510200" + random + keyValue + "0000000000";
                            break;
                        case 3://
                            cmd = "*C0051030000000000000000";
                            break;
                        case 4://
                            cmd = "*C0051040000000000000000";
                            break;
                        case 5:
                            cmd="*C00510500"+random+keyValue+"0000000000";
                            break;
                        case 6:
                            cmd="*C0051060000000000000000";
                            break;
                        default:
                            break;
                    }

                    //joe
                    if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
                        String result = ISocketCode.setForward(cmd,DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
                        MainActivity.getInstance().sendCode(result);
                    }else {
                        TcpSender.sendMssageAF(cmd);
                    }
                }
                break;
                case 240:// 无源控制器第二層指令
                {
                    String[] strs = netCmd.split(":");
                    int cmdType = Integer.parseInt(strs[0]);// 命令类型
                    // NSString *random = array[1]; //[self ToHex:self.randomKey];
                    String keyValue = strs[2]; // 键值
                    switch (cmdType) {
                        case 1://
                        case 2: {
                            modeSthh = "00";
                            modeStmm = "00";
                            modeEndhh = "00";
                            modeEndmm = "00";
                            String[] stareArr;
                            String[] endArr ;
                            String period = device.getModePeriod();
                            int timepos1 = period.indexOf("--");
                            String[] periodArr;
                            if (timepos1 > 0) {
                                periodArr = period.split("--");
                                stareArr = periodArr[0].split(":");
                                endArr = periodArr[1].split(":");
                                if (Integer.valueOf(stareArr[0])!=Integer.valueOf(stareArr[1])){
                                    modeSthh = stareArr[0];
                                    modeStmm = stareArr[1];
                                }
                                if (Integer.valueOf(endArr[0])!=Integer.valueOf(endArr[1])){
                                    modeEndhh = endArr[0];
                                    modeEndmm = endArr[1];
                                }
							}
                            modetime = "00";
                            String delay =AppTools.getNumbers(device.getModeDelayed().replace("秒",""));
                            if (delay.equals("")||delay==null) {
                                delay = "0";
                            }
                            if (Integer.parseInt(delay) > 0) {
                                modetime = toHex(Integer.parseInt(delay));
                            }
                            String moth=device.getCreationTime();
                            if(AppTools.isNumeric(moth)){
                                modeMonth=toHex(Integer.parseInt(moth));
                            }else{
                                modeMonth="00";
                            }
                            // modecode=keyValue;
                            modeIo = keyValue;
                            ActionFlag = 0;
                            checkPeriodForDeviceModel(device, cmdType + 1,
                                    device.getDeviceName() + device.getModeEvent());
                        }
                        break;
                        case 0:// 单笔删除传设备key
                        case 3:// 查询所有的设备 传上层的key
                        case 4:// 删除全部， 传上层的key
                            cmd = "*I00" + toHex(randomKey) + "FF" + keyValue + "248"
                                    + cmdType + "0000000000000000000000";
                            if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
                                String result = ISocketCode.setForward(cmd,DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
                                MainActivity.getInstance().sendCode(result);
                            }else {
                                TcpSender.sendMssageAF(cmd);
                            }
                            randomKey++;
                            if (randomKey >= 256) {
                                randomKey = 0;
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
                case 239://
                    // [self checkPeriodForDeviceModel:devicemodel andModelflag:4
                    // andstring:netCmd];
                    words_ls=device.getRoomName()+device.getDeviceName();
                    checkPeriodForDeviceModel(device, 4, netCmd);
                    break;
                case 238:
                    //通用協議設置多自節指令如門禁資料等
                    String[] strs = netCmd.split(":");
                    String key = strs[0];
                    String strCode;
                    boolean isHexCode=true;
                    if(strs[1].startsWith("H")){
                        strCode=strs[1].substring(1);
                    }else {
                        strCode = AppTools.convertStringToUTF8(strs[1]);
                        isHexCode=false;
                    }
                    int strCodeLen=strCode.length();
                    int i=0;
                    if(strCodeLen>0){
                        //if(deviceProductCode.equals("04")) { //
                        //wake up
                        //Joe 檢查硬件對應key值
                        cmd = "*C0009FA" + deviceProductCode + devieID + devieMid1 + devieMid2
                                + "0F"+ key + "0000" ;
                        TcpSender.sendMssageAF(cmd);
                        //}
                        while (true) {
                            if (strCode.length() > 26) {
                                cmd = "*I00" + key + toHex(i) + "0D"
                                        + strCode.substring(0, 26);
                                TcpSender.sendMssageAF(cmd);
                                strCode = strCode.substring(26);
                            } else {
                                Integer Len = strCode.length() / 2;
                                String IsZero = "00000000000000000000000000";
                                cmd = "*I00" + key + toHex(i) + toHex(Len) + strCode
                                        + IsZero.substring(Len * 2);
                                TcpSender.sendMssageAF(cmd);
                                i++;
                                break;
                            }
                            i++;
                        }
                    }
                    cmd = "*I00" + key + "FF00"+ deviceProductCode + devieID + devieMid1 + devieMid2
                            + toHex(i) + toHex(strCodeLen/2)+(isHexCode?"00":"01")+"000000000000";
                    TcpSender.sendMssageAF(cmd);
                    break;
                case 200:
                    TcpSender.sendMssageAF(netCmd);
                    break;
                case 129: // 云之声添加设备
                {
                }
                break;
            }

        } else {
            if(device!=null){
                deviceModelParam=device;
            }
            // String pinying = getSelling(netCmd);
            String pinying = getPinYin(netCmd); // getSelling(netCmd);
            // step 1
            List<Room> spaceName = RoomFactory.getInstance().getAll();
            int len_g = spaceName.size();
            // 当前区域
            int ArceID= ChannelAddActivity.roomId;
            for (int i = 0; i < len_g; i++) {
                Room room = spaceName.get(i);
                if (str_zl.contains(room.getRoomName())) {
                    ArceID=room.getId();
                    break;
                }
            }
            List<Device> words;
            if(ArceID>0){
                // 当前区域设备
                words = DeviceFactory.getInstance()
                        .getDevices(ArceID,-1);
            }else{
                words = DeviceFactory.getInstance().getAll();
            }
            if (words.size() > 0) {
                String[] zhiLing = MainActivity.getInstance().getResources()
                        .getStringArray(R.array.lightSate);
                for (int j = 0; j < zhiLing.length; j++) {
                    if (str_zl.contains(zhiLing[j])) {
                        // 此处需注意多语言
                        if (str_zl.contains("主页")) {
                            str_zl = str_zl.replace("主页", "");
                        } else {
                            allControl="FF";
                            String replase = "";
                            // 当前区域设备
                            List<Device> spWords = DeviceFactory.getInstance()
                                    .getDevices(ArceID,-1);

                            for (Device deviceModel : spWords) {
                                deviceProductCode = deviceModel.getProductsCode();
                                if (j < 4) {
                                    boolean IsDeviceFlag = false;
                                    if ((deviceModel.getDeviceTypeKey()==1)
                                            || "1E".equals(deviceProductCode)) {
                                        IsDeviceFlag = true;
                                    }
                                    if (IsDeviceFlag) {
                                        if (replase.length() > 0) {
                                            replase = replase + ",";
                                        }
                                        String ArceDeviceName=deviceModel.getRoomName()+deviceModel.getDeviceName();
                                        replase = replase
                                                + ArceDeviceName;
                                        if ((j & 1) == 0) {
                                            // "打开"
                                            replase = replase +	MainActivity.getInstance().getString(R.string.open_1) ;
                                        } else {
                                            // "关闭"
                                            replase = replase + MainActivity.getInstance().getString(R.string.open_1);
                                        }
                                        MultiFlag++;
                                    }
                                } else {
                                    int productId = Integer.parseInt(
                                            deviceProductCode, 16);
                                    if ((productId != 5) && (productId != 7)
                                            && (productId != 255)
                                            && (productId != 101)) {
                                        if (replase.length() > 0) {
                                            replase = replase + ",";
                                        }
                                        String ArceDeviceName=deviceModel.getRoomName()+deviceModel.getDeviceName();
                                        if(j<6) {
                                            replase = replase + ArceDeviceName + MainActivity.getInstance().getString(R.string.close_1);
                                        }else{
                                            replase = replase + ArceDeviceName + MainActivity.getInstance().getString(R.string.close_1);
                                        }
                                        MultiFlag++;
                                    }
                                }
                            }
                            if (replase.length() > 0) {
                                str_zl = str_zl.replace(zhiLing[j], replase);
                                pinying = "";
                            } else {
                                str_zl = str_zl.replace(zhiLing[j], "");
                            }
                        }
                        break;
                    }
                }
                MultiFlag = 0;
                for (int d = 0; d < words.size(); d++) {
                    deviceModelParam = words.get(d);
                    Device cmdWords = words.get(d);
                    string_zhiLing = cmdWords.getDeviceName() + ",";
                    String set = cmdWords.getDeviceNickName();
                    string_zhiLing = string_zhiLing + set;
                    string_zhiLing = string_zhiLing.replace(";", ",");
                    string_zhiLing = string_zhiLing.replace("，", ",");
                    String[] AbsName = string_zhiLing.split(",");
                    for (int s = 0; s < AbsName.length; s++) {
                        if (!"".equals(AbsName[s])) {
                            CampStr = AbsName[s];
                            boolean pinyingFlag = false;
                            if (str_zl.contains(CampStr)) {
                                pinyingFlag = true;
                            } else {
                                if(AppTools.isCharNum(CampStr)){
                                    if ((netCmd.length() > 0)
                                            && (CampStr.length() > 0)) {
                                        if (netCmd.contains(CampStr)) {
                                            pinyingFlag = true;
                                        }
                                    }
                                }
                                else {
                                    String pinyingDev = getPinYin(CampStr);
                                    if ((pinying.length() > 0)
                                            && (pinyingDev.length() > 0)) {
                                        if (pinying.contains(pinyingDev)) {
                                            pinyingFlag = true;
                                        }
                                    }
                                }
                            }
                            if (pinyingFlag) {// 有识别词
                                // if (str_zl.indexOf(AbsName[s]) != -1) {//
                                // 有识别词
                                //ss = AbsName[s];
                                if (("FF".equals(cmdWords.getProductsCode()))
                                        || ("C9".equals(cmdWords
                                        .getProductsCode()))) {
                                    Mode mode = ModeFactory.getInstance()
                                            .getModeById(cmdWords.getSceneId());
                                    if (mode != null) {
                                        str_zl = str_zl.replace(CampStr,
                                                mode.getModeName());
                                        // pinying = getPinYin(str_zl);
                                    }
                                    pinying = "";
                                } else {
                                    str_zl = str_zl.replace(CampStr, ( (MultiFlag > 0)?";":"")
                                            + cmdWords.getDeviceName());
                                    MultiFlag++;
                                }
                                break;
                            }
                        }
                    }
                }
                if ((str_zl.length() > 0) && str_zl.startsWith(";")) {
                    str_zl = str_zl.substring(1);
                }
                int allFlag=0;
                String[] allON_OFF=(String[])MainActivity.getInstance().getResources().getStringArray(R.array.all_on_off);
                int allonofflen = allON_OFF.length;
                for (int all = 0; all < allonofflen; all++) {
                    if (str_zl.contains(allON_OFF[all])) {// 有识别词
                        ReturnAct  += allON_OFF[all];
                        bSearchFlag++;
                        switch (all) {
                            case 0:// "灯光全开"
                            case 2:// "照明全开"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA01FF0000230000FF");
                                sendMssageAF(modelflag, "*C0019FA1EFF0000240000FF");
                                allFlag=1;
                                break;
                            case 1:// "灯光全关"
                            case 3:// "照明全关"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA01FF0000130000FF");
                                sendMssageAF(modelflag, "*C0019FAEFF0000140000FF");
                                allFlag=1;
                                break;
                            case 4:// "窗帘全开"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA" + toHex(type_window)
                                        + "FF0000200000FF");
                                allFlag=1;
                                break;
                            case 5:// "窗帘全关"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA" + toHex(type_window)
                                        + "FF0000100000FF");
                                allFlag=1;
                                break;
                            case 6:// "调光全开"
                            case 7:// "调关全开"
                            case 8:// "调光圈开"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FAEFF0000240000FF");
                                allFlag=1;
                                break;
                            case 9:// "调光全关"
                            case 10:// "调光圈关"
                            case 11:// "调关全关"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA1EFF0000140000FF");
                                allFlag=1;
                                break;
                            case 12:// "彩光全开"
                            case 13:// "彩光圈开"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA1EFF0000240000FF");
                                allFlag=1;
                                break;
                            case 14:// "彩光全关"
                            case 15:// "彩光圈关"
                                devieMid1 = "00";
                                devieMid2 = "00";
                                sendMssageAF(modelflag, "*C0019FA1EFF0000140000FF");
                                allFlag=1;
                                break;
                            case 16:// @"撤防"
                                break;
                            case 17:// @"保全解除"
                                break;
                            case 18:// @"保全设定"
                                break;
                            case 19:// @"布防"
                                break;
                        }
                    }
                }
                // ///
                if(allFlag==0) {
                    for (Device cmdWords : words) {
                        ReturnAct = "";
                        deviceModelParam = cmdWords;
                        deviceProductCode = cmdWords.getProductsCode();
                        string_zhiLing = (cmdWords.getDeviceName() + "," + cmdWords
                                .getDeviceNickName()).replace(";", ",");
                        String[] AbsName = string_zhiLing.split(",");
                        for (String cmdSubs : AbsName) {
                            if (!"".equals(cmdSubs) && cmdSubs != null) {
                                CampStr = cmdSubs;
                                boolean pinyingFlag = false;
                                if (str_zl.contains(cmdSubs)) {
                                    pinyingFlag = true;
                                } else {
                                    if(AppTools.isCharNum(CampStr)){
                                        if ((netCmd.length() > 0)
                                                && (CampStr.length() > 0)) {
                                            if (netCmd.contains(CampStr)) {
                                                pinyingFlag = true;
                                            }
                                        }
                                    }else {
                                        String pinyingDev = getPinYin(CampStr); // getSelling(CampStr);

                                        if ((pinying.length() > 0)
                                                && (pinyingDev.length() > 0)) {
                                            if (pinying.contains(pinyingDev)) {
                                                pinyingFlag = true;
                                            }
                                        }
                                    }
                                }
                                if (pinyingFlag) {// 有识别词
                                    bSearchFlag++;
                                    // 增加時間管控 & 人員權限管控 joe
                                    cStartPos = str_zl
                                            .indexOf(cmdWords.getDeviceName());
                                    words_ls = "";
                                    devieID = cmdWords.getDeviceID();
                                    devieMid1 = "00";
                                    devieMid2 = "00";
                                    ActionFlag = 0;
                                    if (devieID.length() == 6) {
                                        devieID = cmdWords.getDeviceID()
                                                .substring(0, 2);
                                        devieMid1 = cmdWords.getDeviceID().substring(2,
                                                4);
                                        devieMid2 = cmdWords.getDeviceID().substring(4,
                                                6);
                                    }
                                    deviceIO = "0";
                                    if (cmdWords.getDeviceIO().length() > 0) {
                                        deviceIO = cmdWords.getDeviceIO();
                                    }
                                    checkPeriodForDeviceModel(cmdWords, modelflag,
                                            str_zl);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (bSearchFlag == 0) {
                    cStartPos = 0;
                    // 获取所有模式
                    List<Mode> sceneArray = ModeFactory.getInstance().getAll();
                    for (int index = 0; index < sceneArray.size(); index++) {
                        Mode scene = sceneArray.get(index);
                        String nickName = scene.getModeNickName();
                        String upString;
                        int startPos = nickName.indexOf("[");
                        int endPos = nickName.indexOf("]");
                        if ((startPos != -1) && (endPos != -1)&&(endPos>startPos)) {
                            upString = nickName.substring(startPos, endPos);
                            nickName = nickName.replace(upString, "");
                        }
                        string_zhiLing = (scene.getModeName() + "," + nickName)
                                .replace(";", ",");
                        string_zhiLing = string_zhiLing.replace("，", ",");
                        String[] AbsName = string_zhiLing.split(",");
                        for (String cmdSubs : AbsName) {
                            if (!"".equals(cmdSubs) && cmdSubs != null) {
                                boolean pinyingFlag = false;
                                if (str_zl.contains(cmdSubs)) {
                                    pinyingFlag = true;
                                } else {
                                    if(AppTools.isCharNum(cmdSubs)){
                                        if ((netCmd.length() > 0) && (cmdSubs.length() > 0)) {
                                            if (netCmd.contains(cmdSubs)) {
                                                pinyingFlag = true;
                                            }
                                        }
                                    }else {
                                        String pinyingDev = getPinYin(cmdSubs);
                                        // getSelling(scene.getModeName());
                                        if ((pinying.length() > 0) && (pinyingDev.length() > 0)) {
                                            if (pinying.contains(pinyingDev)) {
                                                pinyingFlag = true;
                                            }
                                        }
                                    }
                                }
                                if (pinyingFlag) {// 有识别词
                                    cStartPos = 1;
                                    if (checkPeriod(scene.getStartTime(),
                                            scene.getEndTime())) {
                                        if ("FF".equals(scene.getModeType())) {
                                            int modelCode = scene.getModeCode();// 场景代码
                                            String hexString = toHex(modelCode);
                                            hexString = "*C0032FAFF00FF" + hexString + "0"
                                                    + scene.getModeLoop() + "000000";
                                            sendMssageAF(modelflag, hexString);
                                        } else {
                                            // 获取空间集合modelist

                                            List<ModeList> deviceArray = ModeListFactory.getInstance()
                                                    .getModeDevice(scene.getId());

                                            int len = deviceArray.size();
                                            if (len > 0) {
                                                allControl="FF";
                                                for (int m = 0; m < 25; m++) {
                                                    delayPrctime[m] = 0;
                                                }
                                                //ss = "";
                                                int maxtime = 0;
                                                for (int z = 0; z < len; z++) {
                                                    ModeList _device = deviceArray.get(z);
                                                    String period = _device
                                                            .getModePeriod();
                                                    int timepos1 = period.indexOf("--");
                                                    String[] periodArr = new String[2];
                                                    if (timepos1 > 0) {
                                                        periodArr = _device
                                                                .getModePeriod().split(
                                                                        "--");
                                                    } else {
                                                        periodArr[0] = "00:00";
                                                        periodArr[1] = "00:00";
                                                    }
                                                    if (checkPeriod(periodArr[0],
                                                            periodArr[1])) {
                                                        //int month=Integer.parseInt(AppTools.isNumer( _device.getCreationTime()));
                                                        int startM=0;
                                                        int endM=0;
                                                        if(_device.getBeginMonth().length()>0){
                                                            startM=Integer.valueOf(_device.getBeginMonth().substring(0, 1), 16);
                                                        }
                                                        if(_device.getEndMonth().length()>0){
                                                            endM=Integer.valueOf(_device.getEndMonth().substring(0, 1), 16);
                                                        }
                                                        boolean monthEnable=true;
                                                        if((startM>0)&&(endM>0)&&(startM!=endM)){
                                                            int NowMonth=AppTools.getCurrentMonth();
                                                            if(startM>endM){
                                                                monthEnable=!((NowMonth>endM&&NowMonth<startM));
                                                            }else{
                                                                monthEnable=((NowMonth>=startM&&NowMonth<=endM));
                                                            }
                                                        }
                                                        if(monthEnable){
                                                            String value = "1";
                                                            String cmdWord;
                                                            if (_device.getDeviceTypeKey()==20) {
                                                                String AreStr = "";
                                                                if (_device.getModeFunction().equals("灯光全开")|| _device.getModeFunction().equals("灯光全关")
                                                                        || _device.getModeFunction().equals("设备全关")|| _device.getModeFunction().equals("设备全开") ) {
                                                                    AreStr = "主页";
                                                                }
                                                                cmdWord = AreStr+_device.getModeFunction();
                                                            } else {
                                                                cmdWord=_device.getRoomName()+_device.getDeviceName()+_device.getModeAction()+_device.getModeFunction()+_device.getModeTime();
                                                            }
                                                            if ((Integer.parseInt(AppTools.getNumbers(_device
                                                                    .getModeDelayed().replace("秒","")))) > 0) {
                                                                value =AppTools.getNumbers( _device
                                                                        .getModeDelayed().replace("秒",""));
                                                            }
                                                            if (maxtime < 25) {
                                                                delayPrctime[maxtime] = Integer
                                                                        .parseInt(value);
                                                                setWords[maxtime] = cmdWord;
                                                                maxtime++;
                                                                Log.e("ys", AppTools.getCurrentTime());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }}
                                    //else {
                                    // 显示时间管控无法执行
                                    //}
                                    break;
                                }
                            }
                        }
                        if(cStartPos>0){
                            break;
                        }
                    }
                    if ((cStartPos == 0) && (modelflag == 0)
                            && words_ls.length() > 0) {
                        // 选择临时记忆数据
                        sendCmd(0, words_ls + str_zl, device);
                    }
                    if (ActionFlag == 0) {
                        ////1804
                        ReturnAct = "没有找到识别的设备";
                    }
                }
            }
        }
    }

    private void sendTVCmd(String tv_set, int modelflag) {
        //joe (int roomId, int deviceTypeKey,int pastControl,int isShow)
        List<Device> list= DeviceFactory.getInstance().getDeviceByAction(deviceModelParam.getRoomId(),5,0,1);
        if(list.size()>0) {
            Device tvModel=list.get(0);
            String deviceID=deviceModelParam.getDeviceID();
            if(deviceID!=null&&!deviceID.equals("000000")) {
                int len=Integer.parseInt(deviceID.substring(0,1));
                if(len<4) {
                    String cmd;
                    String Hex_pram = "";
                    String channelID = deviceID.substring(1, (1 + len));
                    if(modelflag > 0){
                        Hex_pram = "000" + channelID;
                        Hex_pram = "C" + Hex_pram.substring(Hex_pram.length() - 3);
                        if (modelflag == 1) {
                            cmd = "*M81" + modecode + modetime + modeIo + tvModel.getProductsCode() + tvModel.getDeviceID() + Hex_pram + modeSthh + modeStmm + modeEndhh + modeEndmm + modeMonth;
                            isSendMode = true;
                            if(ModeGateMac.length()>0) {
                                sendToCurrtneGaway
                                        (ModeGateType,ModeGateMac,cmd);
                                return;
                            }
                        } else {
                            cmd = "*I00" + toHex(randomKey) + "FF" + modeIo + "248" + (modelflag - 1)
                                    + tvModel.getProductsCode() + tvModel.getDeviceID() + Hex_pram + modeSthh + modeStmm + modeEndhh
                                    + modeEndmm + modetime;
                            randomKey++;
                            if (randomKey >= 256) {
                                randomKey = 0;
                            }
                        }
                    }else {
                        Hex_pram="";
                        for (int i = 0; i < len; i++) {
                            Hex_pram = Hex_pram + "0" + channelID.substring(i, i + 1);
                        }
                        Hex_pram+="000000";
                        cmd = "*C0019FA" + tvModel.getProductsCode() + tvModel.getDeviceID() + "C" + len + Hex_pram.substring(0,6);
                        TcpSender.sendMssageAF(cmd);
                    }
                }
            }
        }
    }

    private void initDevie(String networds, int modelflag) {
        int iPos = 0;
        int iPosAct = 255;
        int iVqonPos;
        int timeVal = 0;
        boolean MainOption = true;
        boolean SubOption = true;
        //1023
        String CheckAct = networds.replace(words_ls, "").replace(spaceNameStr,"");
        //	iVqonPos=networds.indexOf(words_ls);
        //	CheckAct=networds.substring(iVqonPos+words_ls.length());
        spaceNameStr="";
        String string_sw_prams = "00";
        iVqonPos = networds.length();
        deviceValue = Integer.parseInt(deviceProductCode, 16);
        String action = "";
        //1804
//		String[] Setup = { "设置互控", "单火配对", "单火删除"};
        String[] Setup=(String[])MainActivity.getInstance().getResources().getStringArray(R.array.step);
        for (int i = 0; i < Setup.length; i++) {
            if (networds.contains(Setup[i])) {
                // 有识别词
                iPos = networds.indexOf(Setup[i]);
                if (iPos < iVqonPos) {
                    String CommandID=deviceProductCode + devieID + devieMid1 + devieMid2;
                    switch(i){
                        case 0:
                            //Joe 檢討
                            deviceCmd = "*C001AFA" + CommandID + "0" +deviceIO+ "000000" ;
                            break;
                        case 1:
                            deviceCmd = "*C0019AF" + CommandID + "A0010000" ;
                            break;
                        case 2:
                            deviceCmd = "*C0019FA" + CommandID + "A0000000" ;
                            break;
                        case 3:
                            deviceCmd = "*C0019FA" + CommandID + "C0000000" ;
                            break;
                    }
                    sendMssageAF(modelflag, deviceCmd);
                    MainOption = false;
                }
            }
        }
        String[]  Logic={"假如输出开","*SE1","假如输出关","*SE2","假如输入开","*SE3","假如输入关","*SE4","假如大于","*SE5","假如小于","*SE6", "假如等于","*SE7","假如是优","*SE8","假如一般","*SE9","假如是差","*SEA","或者输出开","*SD1","或者输出关","*SD2","或者输入开","*SD3","或者输入关","*SD4","或者大于","*SD5","或者小于","*SD6","或者等于","*SD7","或者是优","*SD8","或者一般","*SD9","或者是差","*SDA","而且输出开","*SB1","而且输出关","*SB2","而且输入开","*SB3","而且输入关","*SB4","而且大于","*SB5", "而且小于","*SB6","而且等于","*SB7","而且是优","*SB8", "而且一般","*SB9","而且是差","*SBA","条件不成立","*SEF","逻辑结束","*SE0"
};
        for (int i = 0; i < Logic.length; i+=2) {
            if (networds.contains(Logic[i])) {// 有识别词) {//有识别词
                iPos=networds.indexOf(Logic[i]);
                if(iPos<iVqonPos){
                    action=Logic[i+1];
                    int cmdType=Integer.valueOf(action.substring(3,4));
                    if(cmdType<4){
                        string_sw_prams ="0" +deviceIO ;
                    }else{
                        timeVal = timeComprartion(1, 0, CheckAct);
                        string_sw_prams=toHex(timeVal);
                    }
                    deviceCmd=action + deviceProductCode + "00" + devieID + string_sw_prams;
                    sendMssageAF(modelflag, deviceCmd);
                    MainOption = false;
                }
            }
        }
        if(deviceValue==14){
            String[] sccen ={"场景1", "场景2", "场景3", "场景4"};
            for (int i = 0; i < sccen.length; i++) {
                if (networds.contains(sccen[i])) {// 有识别词) {//有识别词
                    iPos=networds.indexOf(sccen[i]);
                    if(iPos<iVqonPos){
                        deviceCmd ="*C0032FAFF00FF"+devieID+AppTools.toHex(i)+"000000";
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        MainOption=false;
                        break;
                    }
                }
            }
        }

        if ((deviceValue >= 80 && deviceValue <= 100) || (deviceValue == 33)) { // 电视&背景音乐
            if (MainOption) {
                String[] mute = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.closeMutes);
                for (int i = 0; i < mute.length; i++) {
                    if (networds.contains(mute[i])) {// 有识别词
                        iPos = networds.indexOf(mute[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + "已" + mute[i];
                            if (deviceValue >= 80 && deviceValue <= 100) {
                                // 电视
                                deviceCmd = "*SA0" + deviceProductCode + "00"
                                        + devieID + "0F";
                            } else if (deviceValue == 33) {
                                deviceCmd = "*S21" + deviceProductCode + "00"
                                        + devieID + "00";
                            }
                            //else {
                            //}
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            MainOption = false;
                            break;
                        }
                    }
                }
            }
            if (MainOption) {

                String[] mute = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.Openmutes);
                for (int i = 0; i < mute.length; i++) {
                    if (networds.contains(mute[i])) {// 有识别词
                        iPos = networds.indexOf(mute[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + "已" + mute[i];
                            if (deviceValue >= 80 && deviceValue <= 100) { // 电视
                                deviceCmd = "*SA0" + deviceProductCode + "00"
                                        + devieID + "0F";
                            } else if (deviceValue == 33) { // 背景音乐
                                if(mute[i].equals("静音")){
                                    deviceCmd = "*SA0" + deviceProductCode + "00"
                                            + devieID + "0F";
                                }else {
                                    deviceCmd = "*S11" + deviceProductCode + "00"
                                            + devieID + "00";
                                }
                            }
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            MainOption = false;
                            break;
                        }
                    }
                }
            }
        }
        if (((deviceValue == 33)) && (MainOption)) {
            // 背景音乐
            String[] play = (String[]) MainActivity.getInstance().getResources()
                    .getStringArray(R.array.playSet);
            for (int i = 0; i < play.length; i++) {
                if (networds.contains(play[i])) {
                    // 有识别词
                    iPos = networds.indexOf(play[i]);
                    if (iPos < iVqonPos) {
                        ReturnAct = ReturnAct + "已" + play[i];
                        String actionPlay = "*S13";
                        if (i < 7) {
                            actionPlay = "*S22";
                        } else if (i < 9) {
                            actionPlay = "*S12";
                        }
                        deviceCmd = actionPlay + deviceProductCode + "00"
                                + devieID + "00";
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        MainOption = false;
                        break;
                    }
                }
            }
        }
        if (((deviceValue == type_window)) && (MainOption)) {
            // Windows
            //1804
//			String[] puase = { "停止", "暂停", "停", "STOP", "stop", "好了", "够了",
//					"可以了" };
            String[] puase=(String[])MainActivity.getInstance().getResources().getStringArray(R.array.puase);
            for (int i = 0; i < puase.length; i++) {
                if (networds.contains(puase[i])) {// 有识别词
                    ReturnAct = ReturnAct + "已" + puase[i];
                    deviceCmd = "*S7" + deviceIO + deviceProductCode + "00" + devieID
                            + "00";
                    sendMssageAF(modelflag, deviceCmd);
                    ActionFlag = 1;
                    MainOption = false;
                    break;
                }
            }
        }
        if (deviceValue == 30 && (MainOption)) { // 調光

            if (!deviceProductCode.equals("03")) {
                //1804
//				String[] LedColor = { "蓝", "蓝色", "蓝色光", "蓝光", "红色", "红色光",
//						"红光", "绿", "绿色", "绿光", "绿色光", "白", "白色", "白光", "白色光",
//						"紫", "紫色", "紫色光", "紫光", "橙", "橙色光", "橙色", "橙光", "黄色光",
//						"黄色", "黄", "黄光", "粉色光", "粉色", "粉红", "粉红色", "暖光", "暖色",
//						"中性光", "青色", "彩光", "模式1", "模式2", "模式3", "模式4", "模式5",
//						"模式6", "模式7", "模式8", "模式9", "场景", "场景模式", "情景", "情景模式",
//						"自动", "自动变色", "自动变" };

                String[] LedColor=(String[])MainActivity.getInstance().getResources().getStringArray(R.array.LedColor);
                for (int i = 0; i < LedColor.length; i++) {
                    if (networds.contains(LedColor[i])) {// 有识别词
                        int cIndex = i;
                        switch (i) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "10";
                                cIndex = 1;
                                break;
                            case 4:
                            case 5:
                            case 6:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "B0";
                                cIndex = 4;
                                break;
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "60";
                                cIndex = 10;
                                break;
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                                deviceCmd = "*SAA" + deviceProductCode + "00"
                                        + devieID + "0" + deviceIO;
                                cIndex = 14;
                                break;
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "F0";
                                cIndex = 18;
                                break;
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "98";
                                cIndex = 22;
                                break;
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "80";
                                cIndex = 26;
                                break;
                            case 27:
                            case 28:
                            case 29:
                            case 30:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "C8";
                                cIndex = 30;
                                break;
                            case 31:
                            case 32:// 暖色，暖光
                                deviceCmd = "*SAA" + deviceProductCode + "00"
                                        + devieID + "1" + deviceIO;
                                cIndex = 32;
                                break;
                            case 33:
                                deviceCmd = "*SAA" + deviceProductCode + "00"
                                        + devieID + "2" + deviceIO;
                                cIndex = 33;
                                break;
                            case 34:
                                deviceCmd = "*SA" + deviceIO + deviceProductCode
                                        + "00" + devieID + "40";
                                cIndex = 34;
                                break;
                            case 35:
                                deviceCmd = "*SAA" + deviceProductCode + "00"
                                        + devieID + "4" + deviceIO;
                                cIndex = 35;
                                break;
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                                cIndex = (i - 35);
                                deviceCmd = "*SAE" + deviceProductCode + "00"
                                        + devieID + cIndex + deviceIO;
                                cIndex = i;
                                break;
                            default:
                                deviceCmd = "*SAE" + deviceProductCode + "00"
                                        + devieID + "0" + deviceIO;
                                break;
                        }
                        ReturnAct = ReturnAct + LedColor[cIndex];

                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        timeVal = timeComprartion(0, 0, CheckAct);
                        if (timeVal > 0) {
                            string_sw_prams = toHex(timeVal);
                            deviceCmd = "*S5" + deviceIO + deviceProductCode
                                    + "00" + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                        }
                        MainOption = false;
                        break;
                    }
                }
                // String[] LedAlarm = { "报警", "警报", "闪烁" };
                String[] LedAlarm = (String[]) MainActivity.getInstance()
                        .getResources().getStringArray(R.array.LedAlarm);
                for (int i = 0; i < LedAlarm.length; i++) {
                    if (networds.contains(LedAlarm[i])) {// 有识别词
                        ReturnAct = ReturnAct + "已" + LedAlarm[i];
                        deviceCmd = "*SA9" + deviceProductCode + "00" + devieID
                                + "0" + deviceIO;
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        MainOption = false;
                        break;
                    }
                }
            }
            String[] LedSpeed = (String[]) MainActivity.getInstance()
                    .getResources().getStringArray(R.array.speed);
            for (int i = 0; i < LedSpeed.length; i++) {
                if (networds.contains(LedSpeed[i])) {// 有识别词
                    ReturnAct = ReturnAct + "已" + LedSpeed[i];
                    deviceCmd = "*SAD" + deviceProductCode + "00" + devieID
                            + ((i < 3) ? "FA" : "FB");
                    sendMssageAF(modelflag, deviceCmd);
                    ActionFlag = 1;
                    SubOption = false;
                    break;
                }
            }
            String[] DimmerSpeed = (String[]) MainActivity.getInstance()
                    .getResources().getStringArray(R.array.ledSpeed);
            for (int i = 0; i < DimmerSpeed.length; i++) {
                if (networds.contains(DimmerSpeed[i])) {// 有识别词
                    ReturnAct = ReturnAct + "已" + DimmerSpeed[i];
                    switch(i){
                        case 0:string_sw_prams = toHex(80);
                            break;
                        case 1:string_sw_prams = toHex(0);
                            break;
                        case 2:string_sw_prams = toHex(50);
                            break;
                        case 3:string_sw_prams = toHex(100);
                            break;
                    }
                    deviceCmd = "*SAD" + deviceProductCode + "00" + devieID
                            + string_sw_prams;
                    sendMssageAF(modelflag, deviceCmd);
                    ActionFlag = 1;
                    SubOption = false;
                    break;
                }
            }
            if(SubOption){
                //1804
                if (networds.contains(MainActivity.getInstance().getString(R.string.speed_1))) {// 有识别词
                    timeVal = timeComprartion(1, 0, CheckAct);
                    if ((timeVal >= 0) && (timeVal <= 100)) {
                        string_sw_prams = toHex(timeVal);
                        deviceCmd = "*SAD" + deviceProductCode + "00" + devieID
                                + string_sw_prams;
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                    }
                }
            }
            String[] dimer = (String[]) MainActivity.getInstance().getResources()
                    .getStringArray(R.array.dimmer);
            for (int i = 0; i < dimer.length; i++) {
                //1804
                if (networds.contains(dimer[i])) {// 有识别词
                    ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + dimer[i];
                    if (i < 11) {
                        deviceCmd = "*S8" + deviceIO + deviceProductCode + "00"
                                + devieID + "00";
                    } else if (i < 23) {
                        deviceCmd = "*S7" + deviceIO + deviceProductCode + "00"
                                + devieID + "00";
                    } else if (i < 28) {
                        deviceCmd = "*S7" + deviceIO + deviceProductCode + "00"
                                + devieID + "64";
                    } else {
                        deviceCmd = "*S7" + deviceIO + deviceProductCode + "00"
                                + devieID + "0F";
                    }
                    sendMssageAF(modelflag, deviceCmd);
                    ActionFlag = 1;
                    SubOption=false;
                    break;
                    // }
                }
            }
        }
        if (deviceValue == 8||deviceValue == 6) { // 时序开关
            if (MainOption) {
                //1804
//				String[] sw_open_no = { "打开时序1路", "*SA1", "打开时序2路", "*SA2",
//						"打开时序3路", "*SA3", "打开时序4路", "*SA4", "打开时序5路", "*SA5",
//						"打开时序6路", "*SA6", "打开时序7路", "*SA7", "打开时序8路", "*SA8" };
                String[] sw_open_no=(String[])MainActivity.getInstance().getResources().getStringArray(R.array.sw_open_on);
                for (int i = 0; i < sw_open_no.length; i += 2) {
                    if (networds.contains(sw_open_no[i])) {// 有识别词
                        deviceCmd = sw_open_no[i + 1] + deviceProductCode
                                + "00" + devieID + "01";
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        MainOption = false;
                        break;
                    }
                }
            }
            if (MainOption) {
                //1804
//				String[] sw_open_no = { "关闭时序1路", "*SA1", "关闭时序2路", "*SA2",
//						"关闭时序3路", "*SA3", "关闭时序4路", "*SA4", "关闭时序5路", "*SA5",
//						"关闭时序6路", "*SA6", "关闭时序7路", "*SA7", "关闭时序8路", "*SA8" };
                String[] sw_open_no = (String[]) MainActivity.getInstance()
                        .getResources().getStringArray(R.array.sw_open_off);
                for (int i = 0; i < sw_open_no.length; i += 2) {
                    if (networds.contains(sw_open_no[i])) {// 有识别词
                        deviceCmd = sw_open_no[i + 1] + deviceProductCode
                                + "00" + devieID + "00";
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        MainOption = false;
                        break;
                    }
                }
            }
        }

        if((deviceValue>101)&&(deviceValue<112)) {
            if (MainOption) {
                String[] singal = {"信号源1","信号源2","信号源3","信号源4"};
                for (int i = 0; i < singal.length;i++){
                    if (networds.contains(singal[i])) {// 有识别词
                        deviceCmd =  "*SA4"+ deviceProductCode+ "00"+devieID+"0"+(i+1);
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                        MainOption = false;
                    }
                }
            }

        }
        if (deviceValue == type_window) { // 窗簾

            String[] openAngle = (String[]) MainActivity.getInstance()
                    .getResources().getStringArray(R.array.openAngle);
            int angle;
            for (angle = 0; angle < openAngle.length; angle++) {
                if (networds.contains(openAngle[angle])) {// 有识别词
                    ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy)+ openAngle[angle];
                    break;
                }
            }
            //joe test
            string_sw_prams = "FF";
            if (angle < openAngle.length) {
                if (angle > 5) {
                    angle = angle - 5;
                }
                if (angle == 5) {
                    angle = 2;
                }
                string_sw_prams = toHex(angle);
                action = "*SA0";
                deviceCmd = action + deviceProductCode + "00" + devieID
                        + string_sw_prams;
                sendMssageAF(modelflag, deviceCmd);
                ActionFlag = 1;
                MainOption=false;
            }
        }
//dom
        Boolean IsOnFlag=false;
        Boolean IsOffFlag=false;
        Boolean IsTimeFlag=false;
        if(modelflag<4) {
            if (MultiFlag > 1) {
                if(MainOption){
                    // 從數據庫獲取
                    //1804
//				String[] sw_open_no = { "打开", "开启", "开起", "开灯", "开去", "启动", "开",
//						"ON", "on", "OPEN", "open", "START", "start", "关闭", "关", "观", "CLOSE", "close", "OFF",
//						"off", "关窗", "关机"  };
                    String[] sw_open_no = (String[]) MainActivity.getInstance()
                            .getResources().getStringArray(R.array.open_off);
                    iVqonPos=networds.length();
                    for (int i = (cStartPos + words_ls.length()); i < iVqonPos; i++) {
                        for (int j = 0; j < sw_open_no.length; j++) {
                            iPos = 1;
                            for (int k = 0; k < sw_open_no[j].length(); k++) {
                                if ((i + k) >= networds.length()) {
                                    break;
                                }
                                String getOneStr1 = networds.substring(i + k, i + k
                                        + 1);
                                String getOneStr2 = sw_open_no[j].substring(k,
                                        k + 1);
                                if (!getOneStr1.equals(getOneStr2)) {
                                    iPos = 0;
                                    break;
                                }
                            }
                            if (iPos == 1) {
                                iPosAct = j;
                                if(j<13){
                                    IsOnFlag = true;
                                }else{
                                    IsOffFlag=true;
                                }
                                MainOption=false;
                                action=sw_open_no[iPosAct];
                                iPos = cStartPos + words_ls.length(); // s.length());
                                if (iPos == 0) {
                                    iPos = networds.indexOf(action);
                                }
                                timeVal = timeComprartion(0, 0, CheckAct);
                                break;
                            }
                        }
                        if (iPosAct < 255) {
                            break;
                        }
                    }
                }
            }else{
                if (MainOption) {
                    //On
                    String[] sw_open_no = (String[]) MainActivity.getInstance()
                            .getResources().getStringArray(R.array.sw_open);
                    //String CheckAct = networds.replace(words_ls, "");
                    for (int i = 0; i < sw_open_no.length; i++) {
                        boolean pinyingFlag = false;
                        if (CheckAct.contains(sw_open_no[i])) {
                            pinyingFlag = true;
                        } else {
                            String pinying= getPinYin(CheckAct);
                            String pinyingDev = getPinYin(sw_open_no[i]);
                            if ((pinying.length() > 0)
                                    && (pinyingDev.length() > 0)) {
                                if (pinying.equals(pinyingDev)) {
                                    pinyingFlag = true;
                                }
                            }
                        }
                        //if (CheckAct.contains(sw_open_no[i])) {// 有识别词
                        if (pinyingFlag){
                            iPosAct = i;
                            IsOnFlag = true;
                            MainOption=false;
                            action=sw_open_no[iPosAct];
                            //IsTimeFlag=yes;
                            timeVal = timeComprartion(0, 0, CheckAct);
                            if(timeVal>0){
                                if(getCharCount(CheckAct,"后")==1){
                                    IsTimeFlag=true;
                                }
                            }
                            break;
                        }
                    }
                }
                if (MainOption) {
                    //Off
                    String[] sw_open_no = (String[]) MainActivity.getInstance()
                            .getResources().getStringArray(R.array.sw_close);
                    //String CheckAct = networds.replace(words_ls, "");
                    for (int i = 0; i < sw_open_no.length; i++) {
                        boolean pinyingFlag = false;
                        if (CheckAct.contains(sw_open_no[i])) {
                            pinyingFlag = true;
                        } else {
                            String pinying= getPinYin(CheckAct);
                            String pinyingDev = getPinYin(sw_open_no[i]);
                            if ((pinying.length() > 0)
                                    && (pinyingDev.length() > 0)) {
                                if (pinying.equals(pinyingDev)) {
                                    pinyingFlag = true;
                                }
                            }
                        }
                        //if (CheckAct.contains(sw_open_no[i])) {// 有识别词
                        if (pinyingFlag){
                            iPosAct = i;
                            IsOffFlag=true;
                            MainOption=false;
                            action=sw_open_no[iPosAct];
                            //IsTimeFlag=yes;
                            timeVal = timeComprartion(0, 0, CheckAct);
                            if(timeVal>0){
                                if(getCharCount(CheckAct,"后")==1){
                                    IsTimeFlag=true;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if(IsOnFlag){
            // 獲取回碼指令
            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + action;
            // 獲取時間參數
            if (timeVal > 0) {
                string_sw_prams = toHex(timeVal);
                if(IsTimeFlag){
                    action = "*S4" + deviceIO;
                }else{
                    action = "*S5" + deviceIO;
                }
            } else {
                action = "*S2" + deviceIO;
                string_sw_prams = "00";
                if (deviceValue == type_window) { // 窗簾
                    timeVal = timeComprartion(1, 0, CheckAct);
                    if((timeVal>0)&&(timeVal<=100)){
                        action = "*SA1";
                        string_sw_prams = toHex(timeVal);
                    }
                }
            }

            deviceCmd = action + deviceProductCode + "00" + devieID
                    + string_sw_prams;
            sendMssageAF(modelflag, deviceCmd);
            /*
            if (deviceValue>=102 && deviceValue<=112) {//投影机
                //1804
                CheckTouYingjiDevice(MainActivity.getInstance().getString(R.string.open_1), modelflag);
            }
            */
            ActionFlag = 1;
            MainOption = false;
        }
        if(IsOffFlag){
            // 獲取回碼指令
            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + action;
            if (timeVal > 0) {
                string_sw_prams = toHex(timeVal);
                if(IsTimeFlag){
                    action = "*S3" + deviceIO;
                }else{
                    action = "*S6" + deviceIO;
                }
            } else {
                action = "*S1" + deviceIO;
                string_sw_prams = "00";
            }
            deviceCmd = action + deviceProductCode + "00" + devieID
                    + string_sw_prams;
            sendMssageAF(modelflag, deviceCmd);
            /*
            if (deviceValue>=102 && deviceValue<=112) {//投影机
                //1804
                CheckTouYingjiDevice(MainActivity.getInstance().getString(R.string.close_1), modelflag);
            }
            */
            ActionFlag = 1;
            MainOption = false;
        }
        //dom
        if (deviceValue >= 48 && deviceValue < 80) {// 空调
            // mode
            if (SubOption) {
                //1804
//				String[] air = { "模式切换", "00", "自冷", "02", "制冷", "02", "制冷模式",
//						"02", "冷气", "02", "智能模式", "02", "智能", "02", "加热", "04",
//						"制热", "04", "制热模式", "04", "炙热", "04", "送风", "05",
//						"送风模式", "05", "出风", "05", "除湿", "03", "抽湿", "03", "厨师",
//						"03", "自动", "01", "自动模式", "01", "制动", "01" };

                String[] air = (String[]) MainActivity.getInstance()
                        .getResources().getStringArray(R.array.air);
                for (int i = 0; i < air.length; i += 2) {
                    if (networds.contains(air[i])) {// 有识别词
                        iPos = networds.indexOf(air[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + air[i];
                            string_sw_prams = air[i + 1];
                            deviceCmd = "*SA4" + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            SubOption = true;
            // fan
            if (SubOption) {
                //1804
//				String[] airfan = { "风量切换", "00", "风量高速", "04", "高速", "04",
//						"风量中速", "03", "中速", "03", "风量低速", "02", "低速", "02" };
                String[] airfan = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.airfan);

                for (int i = 0; i < airfan.length; i += 2) {
                    if (networds.contains(airfan[i])) {// 有识别词
                        iPos = networds.indexOf(airfan[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + "已" + airfan[i];
                            string_sw_prams = airfan[i + 1];
                            deviceCmd = "*SA5" + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            SubOption = true;
            // Set Temp
            if (SubOption) {
                //1804
//				String[] airTemp = { "太冷", "0A", "太冷了", "0A", "太低", "0A",
//						"太低了", "0A", "温度太低", "0A", "温度太低了", "0A", "温度太冷", "0A",
//						"温度太冷了", "0A", "太热", "04", "太热了", "04", "太高", "04",
//						"温度太高", "04", "太高了", "04", "温度太高了", "04", "温度太热", "04",
//						"温度太热了", "04", "温度最高", "0D", "温度最大", "0D", "最大温度",
//						"0D", "最高温度", "0D", "最大", "0D", "最高", "0D", "最小温度",
//						"01", "最低温度", "01", "最小", "01", "最低", "01", "温度最低",
//						"01", "温度最小", "01" };
                String[] airTemp = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.airTemp);
                for (int i = 0; i < airTemp.length; i += 2) {
                    if (networds.contains(airTemp[i])) {// 有识别词
                        iPos = networds.indexOf(airTemp[i]);
                        if (iPos < iVqonPos) {
                            //ReturnAct = ReturnAct + "已" + airTemp[i];
                            if(i<14){
                                ReturnAct=ReturnAct +MainActivity.getInstance().getString(R.string.allset) +"27"+MainActivity.getInstance().getString(R.string.degree)  ;
                            }else if(i<30){
                                ReturnAct=ReturnAct + MainActivity.getInstance().getString(R.string.allset) +"21"+MainActivity.getInstance().getString(R.string.degree);
                            }else if(i<42){
                                ReturnAct=ReturnAct + MainActivity.getInstance().getString(R.string.allset) +"30"+MainActivity.getInstance().getString(R.string.degree) ;
                            }else{
                                ReturnAct=ReturnAct + MainActivity.getInstance().getString(R.string.allset) +"18"+MainActivity.getInstance().getString(R.string.degree);
                            }
                            string_sw_prams = airTemp[i + 1];
                            deviceCmd = "*SA2" + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }

            if (networds.contains(MainActivity.getInstance().getString(R.string.funcontrol))) {
                // 有识别词
                ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + MainActivity.getInstance().getString(R.string.funcontrol) ;
                deviceCmd = "*SA6" + deviceProductCode + "00" + devieID + "00";
                sendMssageAF(modelflag, deviceCmd);
                ActionFlag = 1;
                SubOption = false;
            }
            // airOne
            if (SubOption) {
                //1804
//				String[] airOne = { "温度加一", "*SA2", "温度高一点", "*SA2", "温度高点",
//						"*SA2", "高一点", "*SA2", "升一点", "*SA2", "调高一点", "*SA2",
//						"温度上升", "*SA2", "上升", "*SA2", "再高一点", "*SA2", "温度减一",
//						"*SA3", "温度低一点", "*SA3", "温度低点", "*SA3", "低一点", "*SA3",
//						"降一点", "*SA3", "第一点", "*SA3", "调低一点", "*SA3", "温度下降",
//						"*SA3", "下降", "*SA3", "再低一点", "*SA3" };
                String[] airOne = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.airOne);
                for (int i = 0; i < airOne.length; i += 2) {
                    if (networds.contains(airOne[i])) {// 有识别词
                        iPos = networds.indexOf(airOne[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy)+ airOne[i];
                            deviceCmd = airOne[i + 1] + deviceProductCode
                                    + "00" + devieID + "00";
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            if (SubOption && (timeVal == 0)) { // 温度
                timeVal = timeComprartion(1, 0, CheckAct);
                if (timeVal > 0) {
                    if (timeVal < 6) {
                        if (SubOption) {
                            //1804
//							String[] AddSub = { "加", "*SA9", "升", "*SA9", "生",
//									"*SA9", "昇", "*SA9", "减", "*SAA", "降",
//									"*SAA", "将", "*SAA" };

                            String[] AddSub = (String[]) MainActivity.getInstance().getResources()
                                    .getStringArray(R.array.AddSub);
                            for (int i = 0; i < AddSub.length; i += 2) {
                                if (networds.contains(AddSub[i])) {// 有识别词
                                    string_sw_prams = toHex(timeVal);
                                    deviceCmd = AddSub[i + 1]
                                            + deviceProductCode + "00"
                                            + devieID + string_sw_prams;
                                    sendMssageAF(modelflag, deviceCmd);
                                    ActionFlag = 1;
                                    SubOption = false;
                                    break;
                                }
                            }
                        }
                    } else if ((timeVal > 17) && (timeVal < 31)) {// Set temp
                        timeVal = (timeVal - 17);
                        string_sw_prams = toHex(timeVal);
                        deviceCmd = "*SA2" + deviceProductCode + "00" + devieID
                                + string_sw_prams;
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                    }
                }
            }
        }
        // else if((deviceValue>=80 &&
        // deviceValue<=100)||(deviceValue==33)||VolumFlag){ //电视&背景音乐
        else if ((deviceValue >= 80 && deviceValue <= 100)
                || (deviceValue == 33)) { // 电视&背景音乐
            // 音量
            if (SubOption) {
                //1804
//				String[] vol = { "音量加", "声音大", "音量大", "声音大一点", "音量大一点",
//						"声音太小了", "高一点", "音量加一点", "再加一点", "再大一点", "大一点", "太小了" };
                String[] vol = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.vol_up);
                for (int i = 0; i < vol.length; i++) {
                    if (networds.contains(vol[i])) {// 有识别词
                        iPos = networds.indexOf(vol[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy)+ vol[i];
                            if (deviceValue >= 80 && deviceValue <= 100) {
                                deviceCmd = "*SA0" + deviceProductCode + "00"
                                        + devieID + "0B";
                                if ((timeVal == 0) && (modelflag == 0)) {
                                    timeVal = timeComprartion(1, 0, CheckAct);
                                    if (timeVal > 1 && timeVal < 10) {
                                        for (int j = 1; j < timeVal; j++) {
                                            sendMssageAF(0, deviceCmd);
                                        }
                                    }
                                }
                            } else if (deviceValue == 33) {
                                deviceCmd = "*SA2" + deviceProductCode + "00"
                                        + devieID + "00";
                            }
                            //else {
                            //
                            //}
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            if (SubOption) {
                //1804
//				String[] vol = { "音量小", "声音小", "声音小一点", "音量小一点", "声音太大了",
//						"低一点", "音量减", "再小一点", "小一点", "音量太大了", "减一点", "太大了",
//						"音量在大了", "音量键" };

                String[] vol = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.vol_down);
                for (int i = 0; i < vol.length; i++) {
                    if (networds.contains(vol[i])) {// 有识别词
                        iPos = networds.indexOf(vol[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + vol[i];
                            if (deviceValue >= 80 && deviceValue <= 100) {
                                deviceCmd = "*SA0" + deviceProductCode + "00"
                                        + devieID + "0C";
                                if ((timeVal == 0) && (modelflag == 0)) {
                                    //1027
                                    //	timeVal = timeComprartion(1, iPos, networds);
                                    timeVal = timeComprartion(1, 0, CheckAct);
                                    if (timeVal > 1 && timeVal < 10) {
                                        for (int j = 1; j < timeVal; j++) {
                                            sendMssageAF(0, deviceCmd);
                                            ActionFlag = 1;
                                        }
                                    }
                                }
                            } else if (deviceValue == 33) {
                                deviceCmd = "*SA3" + deviceProductCode + "00"
                                        + devieID + "00";
                            }
                            //else {
                            //
                            //}
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            if (deviceValue >= 80 && deviceValue <= 100) {
                // 頻道
                if (SubOption) {
                    //1804
//					String[] ch = { "频道加", "0D", "电视频道加", "0D", "贫道加", "0D",
//							"平道加", "0D", "平到家", "0D", "下一台", "0D", "加一台", "0D",
//							"频道减", "0E", "电视频道减", "0E", "电视频道假", "0E", "频道假",
//							"0E", "频道几", "0E", "贫道戒", "0E", "电视频道几", "0E",
//							"上一台", "0E", "平道减", "0E", "平到减", "0E", "减一台", "0E" };

                    String[] ch = (String[]) MainActivity.getInstance().getResources()
                            .getStringArray(R.array.ch);
                    for (int i = 0; i < ch.length; i += 2) {
                        if (networds.contains(ch[i])) {// 有识别词
                            iPos = networds.indexOf(ch[i]);
                            if (iPos < iVqonPos) {
                                ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + ch[i];
                                deviceCmd = "*SA0" + deviceProductCode + "00"
                                        + devieID + ch[i + 1];
                                sendMssageAF(modelflag, deviceCmd);
                                ActionFlag = 1;
                                SubOption = false;
                                break;
                            }
                        }
                    }
                }
                if (SubOption) {
                    //1804
//					String[] Key = { "按键0", "00", "按键1", "01", "按键2", "02",
//							"按键3", "03", "按键4", "04", "按键5", "05", "按键6", "06",
//							"按键7", "07", "按键8", "08", "按键9", "09", "设备电源",
//							"0A", "信号源", "10", "返回", "11", "按键上", "12", "按键下",
//							"13", "按键左", "14", "按键右", "15", "ENTER", "16",
//							"扩充00", "17", "扩充01", "18", "扩充02", "19", "扩充03",
//							"1A", "扩充04", "1B", "扩充05", "1C", "扩充06", "1D",
//							"扩充07", "1E", "扩充08", "1F", "扩充09", "20", "扩充10",
//							"21", "扩充11", "22", "扩充12", "23", "扩充13", "24" };
                    String[] Key = (String[]) MainActivity.getInstance().getResources()
                            .getStringArray(R.array.Key);
                    for (int i = 0; i < Key.length; i += 2) {
                        if (networds.contains(Key[i])) {
                            iPos = networds.indexOf(Key[i]);
                            if (iPos < iVqonPos) {
                                ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy)+ Key[i];
                                if (modelflag == 0) {
                                    int IrCode = Integer.parseInt(Key[i + 1],
                                            16);
                                    if (IrCode < 23) {
                                        deviceCmd = "*SA0" + deviceProductCode
                                                + "00" + devieID + Key[i + 1];
                                    } else {
                                        deviceProductCode = "05";
                                        deviceCmd = "*SA00500" + devieID
                                                + toHex(IrCode + 1);
                                    }
                                } else {
                                    deviceCmd = "*SA0" + deviceProductCode
                                            + "00" + devieID + Key[i + 1];
                                }
                                sendMssageAF(modelflag, deviceCmd);
                                ActionFlag = 1;
                                SubOption = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (deviceValue == 33) { // 背景音樂
                // 選曲
                SubOption = true;
                if (SubOption) {
                    //1804
//					String[] music = { "上一首", "*SA8", "上一曲", "*SA8", "上一取",
//							"*SA8", "上一娶", "*SA8", "上一手", "*SA8", "上一守",
//							"*SA8", "下一首", "*SA9", "下一曲", "*SA9", "下一取",
//							"*SA9", "下一娶", "*SA9", "下一手", "*SA9", "下一守", "*SA9" };
                    String[] music = (String[]) MainActivity.getInstance().getResources()
                            .getStringArray(R.array.music);
                    for (int i = 0; i < music.length; i += 2) {
                        if (networds.contains(music[i])) {
                            // 有识别词
                            iPos = networds.indexOf(music[i]);
                            if (iPos < iVqonPos) {
                                ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy) + music[i];
                                deviceCmd = music[i + 1] + deviceProductCode
                                        + "00" + devieID + "00";
                                sendMssageAF(modelflag, deviceCmd);
                                SubOption = false;
                                ActionFlag = 1;
                                break;
                            }
                        }
                    }
                }
                if (SubOption && (timeVal == 0)) {
                    timeVal = timeComprartion(1, 0, CheckAct);

                    if (timeVal > 0 && timeVal <= 100) {
                        //1804
                        string_sw_prams = toHex(timeVal);
//						String[] song = { "首", "曲", "取", "娶", "手" };
                        String[] song = (String[]) MainActivity.getInstance().getResources()
                                .getStringArray(R.array.song);
                        for (int s = 0; s < song.length; s++) {
                            iPos = networds.indexOf(song[s]);
                            if (iPos < iVqonPos && iPos != -1) {
                                ActionFlag = 1;
                                break;
                            }
                        }
                        if (ActionFlag == 1) {
                            deviceCmd = "*SA8" + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                        } else {
                            deviceCmd = "*SA2" + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                        }
                    }
                }
            }
//		} else if ((deviceValue == 3) || (deviceValue == 163)) { // 調光
        } else if (deviceValue == 30) { // 調光
            if (MainOption && (timeVal == 0)) {
                timeVal = timeComprartion(1, 0, CheckAct);
                if (timeVal <= 100) {
                    string_sw_prams = toHex(timeVal);
                    //1804
                    if (networds.contains(MainActivity.getInstance().getString(R.string.saturation))) {//有识别词
                        deviceCmd = "*S9" + deviceIO + deviceProductCode + "00"
                                + devieID + string_sw_prams;
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                    }else if (networds.contains(MainActivity.getInstance().getString(R.string.temp))) {//有识别词
                        deviceCmd = "*SC" + deviceIO + deviceProductCode + "00"
                                + devieID + string_sw_prams;
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                    }else{
                        if ((timeVal>0)&&SubOption) {
                            deviceCmd = "*S7" + deviceIO + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                        }
                    }
                }
            }
        }else if(deviceValue==18){
            SubOption = true;
            if (SubOption) {
                //1804
//				String[] airfan = { "风量切换", "00", "风量高速", "04", "高速", "04",
//						"风量中速", "03", "中速", "03", "风量低速", "02", "低速", "02" };
                String[] airfan = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.airfan);
                for (int i = 0; i < airfan.length; i += 2) {
                    if (networds.contains(airfan[i])) {// 有识别词
                        iPos = networds.indexOf(airfan[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + "已" + airfan[i];
                            string_sw_prams = airfan[i + 1];
                            deviceCmd = "*SA5" + deviceProductCode + "00"
                                    + devieID + string_sw_prams;
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            // airOne
            if (SubOption) {
                //1804
//				String[] airOne = { "温度加一", "*SA2", "温度高一点", "*SA2", "温度高点",
//						"*SA2", "高一点", "*SA2", "升一点", "*SA2", "调高一点", "*SA2",
//						"温度上升", "*SA2", "上升", "*SA2", "再高一点", "*SA2", "温度减一",
//						"*SA3", "温度低一点", "*SA3", "温度低点", "*SA3", "低一点", "*SA3",
//						"降一点", "*SA3", "第一点", "*SA3", "调低一点", "*SA3", "温度下降",
//						"*SA3", "下降", "*SA3", "再低一点", "*SA3" };
                String[] airOne = (String[]) MainActivity.getInstance().getResources()
                        .getStringArray(R.array.airOne);
                for (int i = 0; i < airOne.length; i += 2) {
                    if (networds.contains(airOne[i])) {// 有识别词
                        iPos = networds.indexOf(airOne[i]);
                        if (iPos < iVqonPos) {
                            ReturnAct = ReturnAct + MainActivity.getInstance().getString(R.string.alredy)+ airOne[i];
                            deviceCmd = airOne[i + 1] + deviceProductCode
                                    + "00" + devieID + "00";
                            sendMssageAF(modelflag, deviceCmd);
                            ActionFlag = 1;
                            SubOption = false;
                            break;
                        }
                    }
                }
            }
            if (SubOption && (timeVal == 0)) { // 温度
                timeVal = timeComprartion(1, 0, CheckAct);
                if (timeVal > 0) {
                    if (timeVal < 6) {
                        if (SubOption) {
                            //1804
//							String[] AddSub = { "加", "*SA9", "升", "*SA9", "生",
//									"*SA9", "昇", "*SA9", "减", "*SAA", "降",
//									"*SAA", "将", "*SAA" };

                            String[] AddSub = (String[]) MainActivity.getInstance().getResources()
                                    .getStringArray(R.array.AddSub);
                            for (int i = 0; i < AddSub.length; i += 2) {
                                if (networds.contains(AddSub[i])) {// 有识别词
                                    string_sw_prams = toHex(timeVal);
                                    deviceCmd = AddSub[i + 1]
                                            + deviceProductCode + "00"
                                            + devieID + string_sw_prams;
                                    sendMssageAF(modelflag, deviceCmd);
                                    ActionFlag = 1;
                                    SubOption = false;
                                    break;
                                }
                            }
                        }
                    } else if (timeVal < 100) {// Set temp
                        string_sw_prams = toHex(timeVal);
                        deviceCmd = "*SA2" + deviceProductCode + "00" + devieID
                                + string_sw_prams;
                        sendMssageAF(modelflag, deviceCmd);
                        ActionFlag = 1;
                    }
                }
            }
        }
        if(MainOption){
            //1804
//			String[] SwType = { "副控", "互控"};
            String[] SwType = (String[]) MainActivity.getInstance().getResources()
                    .getStringArray(R.array.SwType);
            for (int i = 0; i < SwType.length; i++) {
                iPos = networds.indexOf(SwType[i]);
                if(iPos<iVqonPos&&iPos!=-1){
                    action="*S0"+deviceIO;
                    string_sw_prams = "00";
                    deviceCmd=action +deviceProductCode+"00"+devieID+string_sw_prams;
                    sendMssageAF(modelflag, deviceCmd);
                    ActionFlag = 1;
                }
            }
        }

        if ((ActionFlag == 1) && (modelflag != 1)) {
            if (ProjectUtils.getRootPath().getPlaySource() == 1) {
                String checkstr = deviceCmd.substring(4, 10) + devieMid1
                        + devieMid2;
                String rect = map.get(checkstr);
                if (rect != null) {
                    ReturnAct=rect + "," + ReturnAct;
                }
                map.put(checkstr, ReturnAct);
            }
        }
    }

    private boolean checkPeriod(String startTime, String endTime) {

        boolean timeOk = true;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        int NowTime = Integer.parseInt(str.replace(":", ""));
        int decStart = Integer.parseInt(startTime.replace(":", ""));
        int decEnd = Integer.parseInt(endTime.replace(":", ""));
        if (decStart != decEnd) {
            if (decStart > decEnd) {
                timeOk = !((NowTime >=decEnd) && (NowTime <decStart));
            }
            else{
                timeOk=((NowTime >= decStart) && (NowTime <= decEnd));
            }
        }
        return timeOk;
    }

    private void checkPeriodForDeviceModel(Device device, int modelflag,
                                           String str_zl) {
        if (checkPeriod(device.getStartTime(), device.getEndTime())
                || ((modelflag >0)&&(modelflag<128))) {
            cStartPos = 0;
            if (str_zl.indexOf(device.getDeviceName()) != -1) {
                cStartPos = str_zl.indexOf(device.getDeviceName());
            }
            words_ls = "";
            if ("65".equals(deviceProductCode)) {
                // 电视集合频道
                // 小郭:設備列表增加記錄類型及節目頻道 TYPE:CH_NO
                sendTVCmd(device.getDeviceIO(), modelflag);
            } else if ("05".equals(deviceProductCode)) {
//				initDevie(str_zl, modelflag);
                if (ActionFlag == 0) {
                    // 獲取紅外線按鍵名稱
                    // if((cStartPos==0)&&(modelflag==0)&&([words_ls
                    String KeyWord = str_zl.substring(cStartPos
                            + device.getDeviceName().length());
                    List<RedInfra> list = RedInfraFactory.getInstance().getInfraById(device
                            .getId());
                    for (RedInfra infrared : list) {
                        boolean pinyingFlag = false;
                        String pinying= getPinYin(KeyWord);
                        if (KeyWord.contains(infrared.getInfraredName())) {
                            pinyingFlag = true;
                        } else {
                            String pinyingDev = getPinYin(infrared.getInfraredName());
                            // getSelling(scene.getModeName());
                            if ((pinying.length() > 0)
                                    && (pinyingDev.length() > 0)) {
                                if (pinying.equals(pinyingDev)) {
                                    pinyingFlag = true;
                                }
                            }
                        }
                        //if (pinyingFlag&&infrared.getInfraredKey()>0) {
                        if(pinyingFlag){
                            ActionFlag = 1;
                            sendModeForInfrared(modelflag, infrared, 0);
                        }
                    }
                }
                if (ActionFlag == 0) {
                    initDevie(str_zl, modelflag);
                }
            } else {
                // 其他设备
                // 其他设备
                words_ls = device.getDeviceName();
                // dataLens=words_ls length);
                ReturnAct = ReturnAct + words_ls;
                initDevie(str_zl, modelflag);
            }
        }
    }
    //joe 紅外線待修改 2018 0812
    public void sendModeForInfrared(int modelflag, RedInfra infrared,
                                    int SendType) {
        String infraredID = infrared.getDeviceId();
        // if (infrared.getInfrared_sendMode() == 1) {
        if (SendType == 0) {
            String key = toHex(infrared.getInfraredKey() + 1);
            String cmd = "*C0019FA05" + infraredID + "A0" + key +"0000";
            sendMssageAF(modelflag, cmd);
        } else {
            String key = toHex(infrared.getInfraredKey() + 128);
            String cmd = "";
            String infraCode = infrared.getInfraredCode();
            if(infraCode.length()>0){
                int i=0;
                while(true) {
                    if (infraCode.length() > 26) {
                        cmd = "*I00" + key + "0" + i + "0D"
                                + infraCode.substring(0, 26);
                        TcpSender.sendMssageAF(cmd);
                        infraCode = infraCode.substring(26);
                    } else {
                        Integer Len = infraCode.length() / 2;
                        String IsZero = "00000000000000000000000000";
                        cmd = "*I00" + key + "0" + i + toHex(Len) + infraCode
                                + IsZero.substring(Len * 2);
                        TcpSender.sendMssageAF(cmd);
                        break;
                    }
                    i++;
                }
                if (SendType == 1) { // send
                    cmd = "*I00" + key + "FF0005" + infraredID +toHex(i)
                            + "0000000000000000";
                } else { // write
                    cmd = "*I00" + key + "FF0105" + infraredID +toHex(i)
                            + "0000000000000000";
                }
                TcpSender.sendMssageAF(cmd);
            }
        }

    }

    private int timeComprartion(int select, int iPos, String str) {

        //1804
//		String[] time = { "秒", "分", "时", ";" };
        String[] time = (String[]) MainActivity.getInstance().getResources()
                .getStringArray(R.array.time);
        int ContValue = 0;
        int LimitPos = 0;
        String UnitStr = "";
        boolean bFlag = true;
        boolean bFirstOk = false;
        if (select == 0) {// Time
            bFlag = false;
            for (int i = iPos; i < str.length(); i++) {
                for (int n = 0; n < time.length; n++) {
                    if (str.substring(i, i + 1).equals(time[n])) {
                        if (str.substring(i, i + 1).equals(time[n])) {
                            if ((n < 2) || ((n == 2) && (UnitStr.equals("小")))
                                    || (n == 3)) {
                                LimitPos = n;
                                if (n < 3) {
                                    bFlag = true;
                                }
                                break;
                            }
                        }
                    }
                }
                UnitStr = str.substring(i, i + 1);
                if ((bFlag) || (LimitPos == 3)) {
                    break;
                }
            }
        }
        //1804
//		String[] number = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
//				"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "两", "拾",
//				"十", "百", ";" };
        String[] number=MainActivity.getInstance().getString(R.string.number_1).split(",");
        if (bFlag) {
            int MemAdd = 0, k = 0;
            int j = 0;
            bFlag = false;
            for (j = iPos; j < str.length(); j++) {
                for (k = 0; k < number.length; k++) {
                    if (str.substring(j, j + 1).equals(number[k])) {
                        break;
                    }
                }
                if (k < number.length) //
                {
                    if (k == 24) {
                        break;
                    } else {
                        if (bFirstOk) {
                            bFirstOk = false;
                            ContValue = 0;
                        }
                        if ((k == 21) || (k == 22)) // ??
                        {
                            if (ContValue == 0) {
                                ContValue = 1;
                            }
                            MemAdd = 1;
                        } else if (k == 23) // ∞?
                        {
                            ContValue = (ContValue * 10);
                            MemAdd = 1;
                        } else {
                            if (k == 20) {
                                k = 2;
                            } else if (k > 9) {
                                k -= 10;
                            }
                            ContValue = (ContValue * 10) + k;
                            MemAdd = 0;
                        }
                        bFlag = true;
                    }
                } else {
                    if (bFlag) {
                        if (str.substring(j, j + 1).equals(UnitStr)) {
                            break;
                        } else {
                            if (MemAdd == 1) {
                                MemAdd = 0;
                                ContValue = (ContValue * 10);
                            }
                            bFirstOk = true; // break;
                        }
                    }
                }
            }
            if (MemAdd == 1) {
                ContValue = (ContValue * 10);
            }
            if (ContValue > 0) {
                //1804
                UnitStr = ","+MainActivity.getInstance().getString(R.string.allset)+ ContValue;
                if (select == 0) {
                    switch (LimitPos) {
                        case 0: // √?
                            if (ContValue > 63) {
                                ContValue = 63;
                            }
                            UnitStr = UnitStr +MainActivity.getInstance().getString(R.string.Second) ;
                            break;
                        case 1: // ∑÷
                            if (ContValue > 60) {
                                ContValue = 60;
                            }
                            ContValue = ContValue + 64;
                            UnitStr = UnitStr +MainActivity.getInstance().getString(R.string.Munits) ;
                            break;
                        case 2: // ?r
                            if (ContValue > 12) {
                                ContValue = 12;
                            }
                            ContValue = ContValue + 128;
                            UnitStr = UnitStr +MainActivity.getInstance().getString(R.string.Hour) ;
                            break;
                    }
                }
                ReturnAct = ReturnAct + UnitStr;
            }
        }

        return ContValue;
    }

    public static List<String> _GetCmdArr = new ArrayList<String>();

    private void GetDeviceState(String index) {
        String sendCmd;
        deviceValue = Integer.parseInt(deviceProductCode, 16);
        sendCmd = "*C0009FA" + deviceProductCode + devieID+ devieMid1 + devieMid2 + "00000000" ;

        if (index.equals("1")) {
            TcpSender.sendMssageAF(sendCmd);
        } else {
            boolean isUnExit = true;
            if (_GetCmdArr.size() > 0) {
                for (String str : _GetCmdArr) {
                    if (str.equals(sendCmd)) {
                        isUnExit = false;
                        break;
                    }
                }
            }
            if (isUnExit) {
                _GetCmdArr.add(sendCmd);
                TcpSender.sendMssageAF(sendCmd);
            }
        }
    }

    private void sendMssageAF(int modeEdit, String str) {
        String sendCmd;
        String cType;
        String cdid;
        String cAct;
        String cPara;
        if (modeEdit == 1) {
            String device = "";
            if (str.substring(0, 2).equals("*S")) {
                device = str.substring(4, 6) + str.substring(8, 10)+ devieMid1
                        + devieMid2 + str.substring(2, 4) + str.substring(10, 12);
            } else if (str.substring(0, 6).equals("*C0019")) {
                //*C0019FA0100IDAA(act)(para)(mid)(mid)     OLD
                //*C0019FA01(ID)(MID)(MID)(ACT)(PARA)...    NEW
                device = str.substring(8, 12)+ devieMid1 + devieMid2 + str.substring(16, 20);
            } else if (str.substring(0, 6).equals("*C0032")) {
                device = str.substring(8, 10) + str.substring(12, 14) + "F"
                        + str.substring(17, 18) + str.substring(14, 16);
            }
            sendCmd = "*M81" + modecode + modetime + modeIo  + device  + modeSthh + modeStmm + modeEndhh
                    + modeEndmm + modeMonth;
            try {
                Thread.sleep(30);
            }catch(Exception e){
                e.printStackTrace();
            }
            isSendMode = true;
            if(ModeGateMac.length()>0) {
                sendToCurrtneGaway(ModeGateType,ModeGateMac,sendCmd);
                return;
            }
        } else if ((modeEdit == 2) || (modeEdit == 3)) {
            String device = "";
            if (str.substring(0, 2).equals("*S")) {
                device = str.substring(4, 6) + str.substring(8, 10)+ devieMid1
                        + devieMid2 + str.substring(2, 4) + str.substring(10, 12);
            } else if (str.substring(0, 6).equals("*C0019")) {
                device = str.substring(8, 12)+ devieMid1 + devieMid2 + str.substring(16, 20);
            } else if (str.substring(0, 6).equals("*C0032")) {
                device = str.substring(8, 10) + str.substring(12, 14) + "F"
                        + str.substring(17, 18) + str.substring(14, 16);
            }
            /*
            sendCmd = "*I" +modeMonth+ toHex(randomKey) + "FF" + modeIo + "248"
                    + (modeEdit - 1) + modeSthh + modeStmm + modeEndhh
                    + modeEndmm + modetime + device;
            */
            sendCmd = "*I" +modeMonth+ toHex(randomKey) + "FF" + modeIo + "248" + (modeEdit - 1)
                    + device+ modeSthh + modeStmm + modeEndhh + modeEndmm + modetime;
            if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
                String result = ISocketCode.setForward(sendCmd,DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
                MainActivity.getInstance().sendCode(result);
            }else {
                TcpSender.sendMssageAF(sendCmd);
            }
            randomKey++;
            if (randomKey >= 256) {
                randomKey = 0;
            }
            return;
        } else if (modeEdit == 4) {
            cAct = str.substring(2, 4);
            cPara = str.substring(10, 12);
            if (cPara.equals("40")) {
                sendCmd = "*C0019FA" + deviceProductCode + devieID + devieMid1 + devieMid2+ "E0" + cPara + "0000";
            } else {
                int IrCode = Integer.parseInt(cPara, 16);
                cPara = toHex(IrCode + 1);
                sendCmd = "*C0019FA05" + devieID + devieMid1+ devieMid2 + "E0" + cPara +"0000";
            }
        } else {
            sendCmd = str;
            String cCmdType = str.substring(0, 2);
            if (cCmdType.equals("*S")) {
                cAct = str.substring(2, 4);
                cPara = str.substring(10, 12);
                sendCmd = "*C0019FA" + deviceProductCode + devieID + devieMid1 + devieMid2
                        + cAct + cPara + "00" +allControl;
            }
        }
        TcpSender.sendMssageAF(sendCmd);
        List<GateWay> gateWayList = GateWayService.list;
        for (int i = 0; i < gateWayList.size(); i++) {
            GateWay gateway = gateWayList.get(i);
            if (gateway.getIsCurrent() ==1) {
                history.setHistoryName(ReturnAct.replace("已", ""));
                history.setHistoryCode(sendCmd);
                history.setMasterMac(gateway.getGatewayID());
                MainActivity.getInstance().sendHistory();
            }
        }
    }

    public String format(String s) {
        String str = s.replaceAll(
                "[`~!$%^&()+=|{}'',\\[\\).<>~！￥%……& （）——+|{}【】‘”“’。，、？|]",
                "");
        return str;
    }

    public static String toHex(int number) {
        if (number <= 15) {
            return ("0" + Integer.toHexString(number).toUpperCase());
        } else {
            return Integer.toHexString(number).toUpperCase();
        }
    }

    private static String getPinYin(String hanzi) {
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(hanzi);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (Token token : tokens) {
                if (Token.PINYIN == token.type) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toUpperCase();
    }
    private static int getCharCount(String str,String charStr) {
        int count = 0;
        for (int i = 0; i <= str.length() - 1; i++) {
            String getstr = str.substring(i, i + 1);
            if (getstr.equals(charStr)) {
                count++;
            }
        }
        return count;
    }

    //joe 修改投影機連動投影幕布
    public void CheckTouYingjiDevice(String timerOrder,String action ,int modelflag){
        if(modelflag==0){
            try {
                BindProject bindProject = JSONHelper.parseObject(timerOrder, BindProject.class);
                if(bindProject!=null) {
                    SubControlWords = bindProject.getTent() + action;
                    delaySubControl = 1;
                }
            }catch(Exception e){

            }
        }
    }

    public void sendToCurrtneGaway(int GatewayType,String GatewayID,String TampStr) {
        if(GatewayID.equals("")||GatewayID==""){
            TcpSender.sendMssageAF(TampStr);
        }else {
            if(GatewayType>2) {
                byte[] data = new byte[TampStr.length()];
                data = TampStr.getBytes();
                data[TampStr.length() - 1] = 13;
                String cmd = "";
                for (int i = 0; i < TampStr.length(); i++) {
                    cmd = cmd + Integer.toHexString(data[i] / 16).toUpperCase()
                            + Integer.toHexString(data[i] & 15).toUpperCase();
                }
                int userID = Integer.parseInt(GateWayService.mp
                        .get(GatewayID));
                int result = DeviceSDK.setDeviceParam(userID, 0x2725,
                        cmd);
            }else{
                MainActivity.getInstance().sendCode(ISocketCode.setForward(TampStr, GatewayID));
            }
        }
    }
}
