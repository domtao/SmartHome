package com.zunder.smart.service;

import android.util.Log;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.util.List;
import java.util.Random;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.Passive;
import com.zunder.smart.model.Products;
import com.zunder.smart.remote.dialog.TimmingListener;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.listener.*;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.SystemInfo;

public class TcpSender {

    private static int iSerial = 0;
    private static int iSRandom = 0;
    private static RelayListener RelayListener = null;
    private static TimerListener timerListener = null;
    private static RelayListener RelayListListener = null;
    private static DeviceAddListener deviceAddListener = null;
    private static SafeListener safeListener=null;
    private static ElectricListeener electricListeener = null;
    private static RedFralistener redFralistener = null;
    private static PassiveListener passiveListener = null;
    private static PassiveListListener passiveListListener = null;
    private static RouteListener routeListener = null;

    private static ModeMsgListener modeMsgListener = null;
    private static    AlertViewListener alertViewListener=null;
    private static AirSwitchListener airSwitchListener;
    private static DeviceStateListener deviceStateListener;
    private static TimmingListener timmingListener;
    public static String preStatStr="";
    public static void setDeviceStateListener(
            DeviceStateListener deviceStateListener) {
        TcpSender.deviceStateListener = deviceStateListener;
    }

    public static void setModeMsgListener(ModeMsgListener modeMsgListener) {
        TcpSender.modeMsgListener = modeMsgListener;
    }

    public static void setPassiveListListener(
            PassiveListListener passiveListListener) {
        TcpSender.passiveListListener = passiveListListener;
    }

    public static void setPassiveListener(PassiveListener passiveListener) {
        TcpSender.passiveListener = passiveListener;
    }

    public static void setRedFralistener(RedFralistener redFralistener) {
        TcpSender.redFralistener = redFralistener;
    }

    public static void setAlertViewListener(AlertViewListener alertViewListener) {
        TcpSender.alertViewListener = alertViewListener;
    }
    /**
     * @param electricListeener
     *            the electricListeener to set
     */
    public static void setElectricListeener(ElectricListeener electricListeener) {
        TcpSender.electricListeener = electricListeener;
    }

    /**
     * @param pageStateListeener
     *            the pageStateListeener to set
     */

    /**
     * @param deviceAddListener
     *            the deviceAddListener to set
     */
    public static void setDeviceAddListener(DeviceAddListener deviceAddListener) {
        TcpSender.deviceAddListener = deviceAddListener;
    }

    /**
     * @param relayListListener
     *            the relayListListener to set
     */
    public static void setRelayListListener(RelayListener relayListListener) {
        RelayListListener = relayListListener;
    }
    // public static boolean isCloud = false;

    public static boolean isGateWay = false;
    private static DeviceListener deviceListener;

    /**
     * @param deviceListener
     *            the deviceListener to set
     */
    public static void setDeviceListener(DeviceListener deviceListener) {
        TcpSender.deviceListener = deviceListener;
    }
    /**
     * @param timerListener
     *            the timerListener to set
     */
    public static void setTimerListener(TimerListener timerListener) {
        TcpSender.timerListener = timerListener;
    }

    public static void setRelayListener(RelayListener relayListener) {
        RelayListener = relayListener;
    }

    public static void sendMssageAF(String string) {
        if (string == null||string.equals("")) {
            return;
        }
        Log.e("TCMDS",string);
        try {
            int edition = ProjectUtils.getRootPath().getRootVersion();
            int getwayCount = 0;
            if (edition == 1) {
                List<GateWay> list = GateWayService.list;
                for (GateWay oldGateway : list) {
                    if (oldGateway.getTypeId() < 6
                            && oldGateway.getIsCurrent() > 1) {
                        getwayCount++;
                    }
                }
            }
            Boolean initSerial = true;
            List<GateWay> gateWayList = GateWayService.list;
            if (gateWayList.size() > 0) {
                for (int i = 0; i < gateWayList.size(); i++) {
                    GateWay gateway = gateWayList.get(i);
                    int gatewayTypeID = gateway.getTypeId();
                    //if (gateway.getIsCurrent() > 0) {
                        Boolean IsSendFlag = true;
                        if (gateway.getIsCurrent() > 1) {
                            String s = string.substring(0, 2);
                            IsSendFlag = false;
                            if (string.equals("*D0")||s.equals("*I")) {
                                IsSendFlag = true;
                            } else {
                                if (s.equals("*C")) {
                                    s = string.substring(4, 6);
                                    //String tempType = string.substring(8, 10);
                                    //if (((s.equals("09") || s.equals("19"))
                                    //        && !tempType.equals("07"))
                                    //        || s.equals("32") || s.equals("0A")) {
                                    if (s.equals("09") || s.equals("19") || s.equals("32") || s.equals("0A")) {
                                        IsSendFlag = true;
                                    }
                                }
                            }
                        }
                        if (IsSendFlag) {
                            if (!gateway.getUserName().equals("admin")) {
                                string = string.replace("*", "#");
                            }
                            String TampStr = string;
                            String SmartHomeStr = string;
                            if ((edition == 0) || (getwayCount > 0)) {
                                String s = string.substring(0, 2);
                                if (s.equals("*C")) {
                                    if (getwayCount > 0) {
                                        edition = 1;
                                    }
                                    if (iSRandom == 0) {
                                        iSRandom = (new Random().nextInt(255) % 255) + 1;
                                    }
                                    if (initSerial) {
                                        initSerial = false;
                                        iSerial++;
                                        if (iSerial > 255) {
                                            iSerial = 0;
                                        }
                                    }
                                    String s1 = toHex(iSRandom);
                                    //joe 2018/0628 修改網關發碼
                                    SmartHomeStr = TampStr.substring(0, 2) + s1 + TampStr.substring(4) + toHex(iSerial);
                                    String s2 = toHex(iSerial ^ 0x000f);
                                    TampStr = TampStr.substring(0, 2) + s1
                                            + TampStr.substring(4) + s2;
                                } else {
                                    edition = 1;
                                }
                            }
                            if (gatewayTypeID <3) {
                                if(timmingListener!=null){
                                    timmingListener.timerCode(SmartHomeStr);
                                }else {
                                    if (gateway.getState().equals(MainActivity.getInstance()
                                            .getString(R.string.gateWayLine)) || SystemInfo.getSSID(MainActivity.getInstance()).startsWith("RAK")) {
                                        Thread.sleep(50);
                                        String deviceID = gateway.getGatewayID();
                                        String result = ISocketCode.setForward(SmartHomeStr,
                                                deviceID);
                                        MainActivity.getInstance().sendCode(result);
                                    } else {
                                        if (gateway.getIsCurrent() == 1) {
                                            MainActivity.getInstance()
                                                    .showToast(
                                                            "设备" + gateway.getGatewayName()
                                                                    + "不在线", 3000);
                                            MainActivity.getInstance().showLine();
                                        }
                                    }
                                }
                            }
                        }
                    //}
                }
            } else {
                MainActivity.getInstance()
                        .showToast("请添加网关设备", 3000);
                MainActivity.getInstance().showLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static String toHex(int number) {
        if (number <= 15) {
            return ("0" + Integer.toHexString(number).toUpperCase());
        } else {
            return Integer.toHexString(number).toUpperCase();
        }
    }

    private static String toHex1602(String string) {
        int a = Integer.parseInt(string);
        String ss = Integer.toHexString(a).toUpperCase();
        if (ss.length() < 2) {
            ss = "0" + ss;
        }
        return ss;
    }

    public static void dataProcess(String data) {
        String string = data;
        //*C(00)(cmd)(發送方)(接收方)(ID)(MID1)(MID2)
        String CommandType = string.substring(0, 2);
        Log.e("CMD",data);
        if ("*C".equals(CommandType)) { // 標準指令
            if (deviceListener != null) {
                deviceListener.searchDevice(data);
            }
            CommandType = string.substring(4, 6);
            int Command = Integer.valueOf(string.substring(4, 6), 16);
            if(Command<8){
                getDeviceState(string, Command);
                getLearnDevice(string, 0);
            }else{
                if ("12".equals(CommandType)) { // 查?冊O備回碼
                    String SubCommand = string.substring(16, 18);
                    if ("01".equals(SubCommand)) { // 讀取內存數據
                        if(electricListeener!=null) {
                            electricListeener.setElectric(string);
                        }

                    } else if ("02".equals(SubCommand)) { // 讀取設備?愋?
                        getDeviceState(string, 9);
                        getLearnDevice(string, 2);
                    }
                } else if ("1C".equals(CommandType)) { // 中繼路由表
                    if (RelayListListener != null) {
                        RelayListListener.findAllRelay(string);
                    }
                } else if ("08".equals(CommandType)) {
                    // 紅外設備電流偵測回碼
                    if (electricListeener != null) {
                        electricListeener.setElectric(string);
                    }
                }else if ("32".equals(CommandType)) { // 情景指令
                    //可以用來刷新手機對應情景代碼
                }else if ("52".equals(CommandType)) { // 无源控制器
                    // *C1882000001000281250220
                    Log.e("paasive",string);
                    String SubCommand = string.substring(6, 8);
                    int id= Integer.valueOf(string.substring(10, 12),16);
                    String cmdString = string.substring(10);
                    String memIndex = cmdString.substring(0, 2);
                    int source = Integer.valueOf(string.substring(20, 22), 16);
                    int type = Integer.valueOf(
                            cmdString.substring(cmdString.length() - 2), 16);
                    String name ="脉冲按键" ;
                    int image =DeviceTypeIMG.pImage[0];
                    String OnStart = string.substring(8, 10);
                    if (source < 10) {
                        int index=0;
                        if(source==1){	//if ((type & 0X0f) > 0) {
                            if ((type & 0x0f) == 1) {
                                name = "按键1";
                                //	image = "1";
                                index=1;
                            } else if ((type & 0x0f) == 2) {
                                name = "按键2";
                                //image = "2";
                                index=2;
                            } else if ((type & 0x0f) == 4) {
                                name = "按键3";
                                //	image = "3";
                                index=3;
                            } else if ((type & 0x0f) == 8) {
                                name = "按键4";
                                //image = "4";
                                index=4;
                            }
                            if ((type & 0x20) > 0) {
                                if ((type & 0x10) > 0) {
                                    name = name + "开";
                                    //	image = image + "1";
                                    image=DeviceTypeIMG.pImage[index+1];

                                } else {
                                    name = name + "关";
                                    //	image = image + "0";
                                    image=DeviceTypeIMG.pImage[index+4];
                                }
                            }
                        } else {
                            if ((type & 0x0f) == 0) {
                                index=0;
                            }else if ((type & 0x0f) == 1) {
                                name = "按键1";
                                //	image = "1";
                                index=16;
                            } else if ((type & 0x0f) == 2) {
                                name = "按键2";
                                //image = "2";
                                index=17;
                            } else if ((type & 0x0f) == 4) {
                                name = "按键3";
                                //	image = "3";
                                index=18;
                            }
                            if ((type & 0x20) > 0) {
                                // ON
                                name = name + "按" + (source-2) + "下";
                                //	image = image + "1";
                                image=DeviceTypeIMG.pImage[index];
                            }
                        }
                    } else if (source <= 21) {
                        // 1527
                        name = "433.92 1527编码";
                        //	image = "1527";
                        image=DeviceTypeIMG.pImage[8];
                    } else if (source == 39) {
                        //image = "39";
                        image=DeviceTypeIMG.pImage[9];
                        switch (type) {
                            case 1:
                            case 2:
                            case 3:
                                name = "进出传感"+type;
                                image=DeviceTypeIMG.pImage[8];
                                break;
                            case 4:
                            case 5:
                            case 6:
                                image=DeviceTypeIMG.pImage[8];
                                name = "居家传感"+type;
                                break;
                        }

                    } else if ((source >39) && (source < 46)) {
                        image=DeviceTypeIMG.pImage[10+(source-39)];
                        name = "Airbus 传感器";
                    }
                    //int modeCoide=Integer.parseInt(cmdString.substring(0,2))+150;
                    int modeCoide=Integer.valueOf(
                            cmdString.substring(0,2), 16)+150;
                    Mode mode = ModeFactory.getInstance().getName(modeCoide);
                    if (mode != null) {
                        name = mode.getModeName();
                    }
                    Passive passive = new Passive();
                    passive.setId(id);
                    passive.setCmdString(cmdString);
                    passive.setMemIndex(memIndex);
                    passive.setType(type);
                    passive.setImage(image);
                    passive.setName(name);
                    passive.setSubCommand(SubCommand);
                    passive.setOnStart(OnStart);
                    if (passiveListener != null) {
                        passiveListener.setPassive(passive);
                    }
                }
            }
            /*
            if ("00".equals(CommandType)) { // 數字量回碼
                getLearnDevice(string, 0);
            } else if ("01".equals(CommandType)) { // 數字量位元回碼
                getLearnDevice(string, 0);
            } else if ("02".equals(CommandType)) { // (保留)
                getLearnDevice(string, 0);
            } else if ("03".equals(CommandType)) { // 多筆模擬量回碼
                getLearnDevice(string, 0);
            } else if ("04".equals(CommandType)) { // 單筆模擬量回碼
                // 紅外設備?娏?偵測回碼
                if (electricListeener != null) {
                    electricListeener.setElectric(string);
                }
            } else if ("1F".equals(CommandType)) { //
                getLearnDevice(string, 2);
            } else if ("19".equals(CommandType)) { // 控制指令

            } else if ("1C".equals(CommandType)) { // 中繼路由表
                if (RelayListListener != null) {
                    RelayListListener.findAllRelay(string);
                }
            } else if ("1D".equals(CommandType)) { // 中繼指令

            } else if ("81".equals(CommandType)) { // 門??來?娭噶?

            } else if ("15".equals(CommandType)) { // 讀取智能報警狀態

            } else if ("65".equals(CommandType)) { // 安防報警

            }
            */
            if (deviceStateListener != null) {
                deviceStateListener.receiveDeviceStatus(string);
            }
        } else if ("*N".equals(CommandType)) { //
            // 增加判斷是否本機查?儾奴@取此?嵪?
            if(routeListener!=null){
                routeListener.setRoute(string);
            }
        }else if ("*T".equals(CommandType)) { // 定時指令回碼
            // 增加判斷是否本機查?儾奴@取此?嵪?
        } else if ("*M".equals(CommandType)) { // 情景指令回碼
            // 增加判斷是否本機查?儾奴@取此?嵪?
            modeCode(string);
        //} else if ("*c".equals(CommandType)) { // 精?喼噶疃喙P模擬量回碼:空調,背景音樂....等
        //    // *c003000010000=00,00,00(03)
        //    // 4,5:type 8,9:ID 10,11:mid1 12,13:mid2
        //    //getDeviceState(string, 2);
        } else if ("*d".equals(CommandType)) { // 精?喼噶?數字量回碼:燈光,插座....等
            // *d003000010000=00,00(03)
            // 4,5:type 8,9:ID 10,11:mid1 12,13:mid2
            String type = string.substring(4, 6);
            if (type.equals("07")) {// 安防回码
                if (safeListener != null) {
                    safeListener.setBackCode(string);
                }
            }
            //getDeviceState(string, 0);
        } else if ("*a".equals(CommandType)) { // 精?喼噶?單筆模擬量回碼:調光數值
            // *d000300010000=100(03)
            //getDeviceState(string, 1);
            // [self getLearnDevice:string andIndex:0];
            //在這裡處裡報警程序
            String type = string.substring(4, 6);
            if (type.equals("07")) {// 安防回码

            }
        } else if ("*I".equals(CommandType)) { // 背景音樂或紅外?學?暬卮a
            int index = Integer.valueOf(string.substring(6, 8), 16);
            int type = Integer.valueOf(string.substring(10, 12), 16);
            if (index == 255) {
                if(type == 36) {
                    if (passiveListListener != null) {
                        passiveListListener.setPassiveList(string);
                    }
                }else if(type == 6){
                    int io=Integer.parseInt(string.substring(4,6));
                    String cmdType=string.substring(8,10);
                    if(cmdType.equals("00")){
                        String value1= Integer.parseInt(string.substring(12,16),16)+"";//电压
                        String value2= Integer.parseInt(string.substring(16,20),16)+"";//功率
                        String value3=Float.valueOf(Integer.parseInt(string.substring(20,24),16))/10+"";//温度
                        String value4= Integer.parseInt(string.substring(24,28),16)+"";//电流
                        String value5= Float.valueOf( Integer.parseInt(string.substring(28,32),16))/10+"";
                        String value6= Float.valueOf( Integer.parseInt(string.substring(32,36),16))/100+"";
                        if(alertViewListener!=null){
                            alertViewListener.alertViewParam(value1,value2,value3,value4,value5,value6);
                        }
                    }else if(cmdType.equals("01")){
                        String value1= Integer.parseInt(string.substring(12,16),16)+"";//电压
                        String value2= Integer.parseInt(string.substring(16,20),16)+"";//功率
                        String value3=Float.valueOf(Integer.parseInt(string.substring(20,24),16))/10+"";//温度
                        String value4= Float.valueOf(Integer.parseInt(string.substring(24,28),16))/100+"";//电流
                        String value5= Float.valueOf(Integer.parseInt(string.substring(32,36)+string.substring(28,32),16))/1000+"";
                        if(airSwitchListener!=null){
                            airSwitchListener.setCmd(io+1,value1,value2,value3,value4,value5);
                        }
                    }
                }else{
                    if (redFralistener != null) {
                        redFralistener.setCodeIR(string);
                    }else if (electricListeener != null) {
                        electricListeener.setElectric(string);
                    }
                }
            } else {
                if (redFralistener != null) {
                    redFralistener.setCodeIR(string);
                }else if (electricListeener != null) {
                    electricListeener.setElectric(string);
                }
            }
            // analysisInfrared_code(string);
        } else if ("*m".equals(CommandType)) { //
            // *m00030001=FFh(03)
        } else if ("*Z".equals(CommandType)) { //
            if (RelayListener != null) {
                RelayListener.findAllRelay(string);
            }
        } else if ("*t".equals(CommandType)) {
            if (timerListener != null) {
                timerListener.timerCode(string);
            }
            /*
             * if(workType==1){ //??? *t 00 03 80 11 30 06 42 22 00 20 01 //*t
             * 1E 04 80 12 17 FF FF F0 02 00 00 DSTUserDefaultsModel
             * *userDefaule = [[DSTUserDefaultsModel sharedUserDefaultsManager]
             * getUserDefaultsModelKeyForUserDefaults]; if ([[string
             * substringWithRange:NSMakeRange(12, 6)]
             * isEqualToString:@"FFFFF0"]) {//薄春??
             *
             * }else{ if (_myDelegate.appointmentModel == nil) return ;
             *
             * DSTAppointmentModel *appointmentModel = [DSTAppointmentModel
             * createAppointmentData].firstObject;
             *
             * appointmentModel.timer = [NSString
             * stringWithFormat:@"%@:%@",[string
             * substringWithRange:NSMakeRange(8, 2)],[string
             * substringWithRange:NSMakeRange(10, 2)]]; appointmentModel.number
             * = [string substringWithRange:NSMakeRange(4, 2)];
             *
             *
             * BOOL retAck=YES; NSString *deviceId = [NSString
             * stringWithFormat:@"%@%@",[string
             * substringWithRange:NSMakeRange(14, 2)],[string
             * substringFromIndex:string.length-4]]; if (![deviceId
             * isEqualToString:_myDelegate.appointmentModel.deviceID]) {
             * retAck=NO; } if(![[string substringWithRange:NSMakeRange(12,
             * 2)]isEqualToString:_myDelegate
             * .appointmentModel.deviceProductCode]){ retAck=NO; } if(![[string
             * substringWithRange:NSMakeRange(17, 1)]isEqualToString
             * :_myDelegate.appointmentModel.deviceIO]){ retAck=NO; }
             * if(retAck){ int deviceValue=[self
             * HexToTen:_myDelegate.appointmentModel.deviceProductCode]; NSArray
             * *arr =
             *
             * @[@"㏄ら",@"㏄",@"㏄",@"㏄",@"㏄",@"㏄き",@"㏄せ",@"?Ω"]; int week =
             * strtoul([[string substringWithRange:NSMakeRange(6, 2)]
             * UTF8String],0,16); //NSString *weekStr; NSMutableString *weekStr
             * = [NSMutableString string]; //if((week&0x80)>0){weekStr=[NSString
             * stringWithFormat:@"%@",arr[7]];} if((week&0x80)>0){[weekStr
             * appendFormat:@"%@ ", @"?Ω"];} if((week&0x7f)==0x7f){ [weekStr
             * appendFormat:@"%@ ",
             *
             * @"–ぱ"]; }else{ for(int i=0;i<arr.count-1;i++){ if((week &
             * (1<<i))>0){ [weekStr appendFormat:@"%@ ", arr[i]]; } } } [weekStr
             * deleteCharactersInRange:NSMakeRange([weekStr length]-1, 1)];
             * appointmentModel.cycle = weekStr; // appointmentModel.week =
             * [[DSTSendCMD shareInstance]ToHex:week];
             *
             * int time = strtoul([[string substringWithRange:NSMakeRange(18,
             * 2)] UTF8String],0,16); BOOL timeCheck=YES; NSString
             * *Action=@"ゴ?"; if (deviceValue==5){ //IR 絬 if([[string
             * substringWithRange:NSMakeRange(16, 2)]isEqualToString:@"A2"]){
             * Action=@"??"; } }else if (deviceValue>=48 && deviceValue<=100)
             * {//?&?? //Action =
             *
             * @"A8"; if([[string substringWithRange:NSMakeRange(16,
             * 2)]isEqualToString:@"A1"]&&[[string
             * substringWithRange:NSMakeRange(18, 2)]isEqualToString:@"02"]){
             * Action=@"??"; } }else
             * if(deviceValue==userDefaule.userDefaults_Type_windows){ //windows
             * if([[string substringWithRange:NSMakeRange(16,
             * 2)]isEqualToString:@"52"]){ Action=@"??"; } }else{ if([[string
             * substringWithRange:NSMakeRange(16,
             * 1)]isEqualToString:@"1"]||[[string
             * substringWithRange:NSMakeRange(16, 1)]isEqualToString:@"6"]){
             * Action=@"??"; } } if(timeCheck){ if((time&0xC0)>0){
             * if(time&0x80){ Action=[Action
             * stringByAppendingFormat:@"%d%@",(time&0x3f),@"?"]; }else{ //mm
             * Action=[Action
             * stringByAppendingFormat:@"%d%@",(time&0x3f),@"だ?"]; } }else{
             * //second if ((time&0x3f)>0) { Action=[Action
             * stringByAppendingFormat:@"%d%@",(time&0x3f),@""]; } } }
             * appointmentModel.event = Action; if ([self.delegate
             * respondsToSelector :@selector(backTCPConnetSearchDelegate:)]) {
             * [self.delegate backTCPConnetSearchDelegate:appointmentModel]; } }
             * } }
             */
        } else if ("#E".equals(string)) {

        } else {
            // 是否需特別?幚碇噶?
        }
    }

    public static void getDeviceState(String backString, int index) {
        Log.e("New","Code:"+backString);

        List<Device> list = DeviceFactory.getInstance().getAll();
        // List<Device> list = DeviceAdapter.list;
        if(!preStatStr.equals(backString.substring(4))) {
            preStatStr=backString.substring(4);
            //*C(00)(cmd)(發送方)(接收方)(ID)(MID1)(MID2)(act)(data1)(data2)(data3)
            // 0      4    6       8       10  12    14    16   18     20     22
            String deviceStr=backString.substring(8, 16);
            int state = 0;
            if (list.size() > 0) {
                for (Device device : list) {
                    String deciceData=device.getProductsCode()+device.getDeviceID();
                    if (index == 9) {//3==>9
                        //*C0012AA(TY)(ID)(MID)(MID)
                        if (device.getProductsCode().equals("65")) { // 频道
                            String ID = backString.substring(10, 16);
                            if (backString.substring(8, 10).equals("05")
                                    && device.getDeviceID().equals(ID)) {
                                device.setState(1);
                            }
                        }
                    } else {
                        if (deciceData.equals(deviceStr)) {
                            int Command = Integer.valueOf(backString.substring(4, 6), 16);
                            device.setCmdDecodeType(Command);
                            device.setDeviceOnLine(0);
                            Integer deviceProductID = Integer.valueOf(device.getProductsCode(), 16);
                            Integer dio = Integer.valueOf(device.getDeviceIO(), 16);
                            Integer actionSt = Integer.valueOf(backString.substring(16, 18), 16);
//                            device.setDeviceBackCode(backString);
                            if (Command < 4) {
                                Integer powerSt = Integer.valueOf(backString.substring(18, 20), 16);
                                int value2=Integer.valueOf(backString.substring(20, 22), 16);
                                int value3=Integer.valueOf(backString.substring(22, 24), 16);
                                device.setDeviceAnalogVar2(value2);
                                device.setDeviceAnalogVar3(value3);
                                if (deviceProductID == 12) {
                                    // 窗帘Type_Windows
                                    //比对行程大小，判断开或关// 行程设定
                                    Integer TimeSet = Integer.valueOf(backString.substring(6, 8), 16);
                                    device.setSetTime(TimeSet);
                                    if ((powerSt & (1 << (dio + 4))) > 0) { //stop
                                        state=0;
                                        device.setState(state);
                                        //device.setControlState(2);
//                                      //device.setLoadImageIndex(device.getState());
                                        //傳送給子頁面停止動畫
                                    }else {
                                        if ((actionSt & (1 << dio)) > 0) {
                                            if ((powerSt & (1 << dio)) > 0) { // Open
                                                state = 1;
                                                //device.setLoadImageIndex(1);
                                                //device.setControlState(1);
                                            } else {
                                                state = 2;
                                                //device.setLoadImageIndex(0);
                                                //device.setControlState(0);
                                            }
                                            device.setState(state);
                                        }
                                    }
                                    //
                                    if (dio == 1) {
                                        device.setLoadImageIndex(value3*10/ TimeSet);
                                    } else {
                                        device.setLoadImageIndex(value2*10/ TimeSet);
                                    }

                                } else if (deviceProductID == 7) {
                                    //device.setState(((powerSt & 4) > 0) ? 1 : 0);
                                    state = ((powerSt & 4) > 0) ? 1 : 0;
                                    device.setLoadImageIndex(((powerSt & 1) > 0) ? 1
                                            : 0);
                                    if ((powerSt & 8) > 0) {
                                        device.setLoadImageIndex(device
                                                .getLoadImageIndex() + 2);
                                    }
                                    device.setState(state);
                                } else if ((deviceProductID > 47
                                        && deviceProductID < 80) || deviceProductID == 18) { // 空调  新风系统
                                    device.setLoadImageIndex(0);
                                    //device.setState(powerSt & 15);
                                    state = powerSt & 15;
                                    device.setState(state);
                                    if (device.getState() > 0) {
                                        device.setLoadImageIndex((device
                                                .getDeviceAnalogVar3() & 15) + 1);
                                    }
                                } else if ((deviceProductID > 79) && (deviceProductID < 101)) {
                                    //device.setState(powerSt);
                                    state = powerSt;
                                    device.setLoadImageIndex(powerSt);
                                    device.setState(state);
                                } else if ((deviceProductID > 101) && (deviceProductID < 113)) {
                                    state = powerSt;
                                    device.setLoadImageIndex(powerSt);
                                    device.setState(state);
                                    if(actionSt==1&&device.getDeviceOrdered().length()>0){
                                       SendCMD.getInstance().CheckTouYingjiDevice(device.getDeviceOrdered(),device.getState()>0?"打开":"关闭",0);
                                    }
                                } else if (deviceProductID == 33) { // 背景音乐
                                    device.setDeviceAnalogVar3(powerSt);
                                    //device.setState((powerSt & 4) > 0 ? 1 : 0);
                                    state = (powerSt & 1) > 0 ? 1 : 0;
                                    device.setState(state);
                                    device.setLoadImageIndex(device.getState());
//                                    if (device.getState() > 0) {
//                                        if ((powerSt & 3) == 1) {
//                                            device.setLoadImageIndex(2); // Play
//                                        }
//                                    }
                                } else {
                                    state = (powerSt & (1 << dio)) > 0 ? 1 : 0;
                                    device.setState(state);
                                    device.setDeviceDigtal(backString);
                                    device.setLoadImageIndex(device.getState());
                                }
                            }else{
                                if (deviceProductID !=6 ) {//空开
                                    Integer powerSt = Integer.valueOf(backString.substring(18, 20), 16);
                                    int value2 = Integer.valueOf(backString.substring(20, 22), 16);
                                    int value3 = Integer.valueOf(backString.substring(22, 24), 16);
                                    device.setDeviceAnalogVar2(value2);
                                    device.setDeviceAnalogVar3(value3);
                                }
                                //資料格式特別處裡
                                switch (Command){
                                    case 4:
                                        break;
                                    case 5:
                                        break;
                                    case 6:
                                        break;
                                    case 7:
                                        break;
                                }
                            }
                            device.setDevice_change(1);
                        }
                    }
                }
                /* joe 2018/0628
                if (SendCMD.map != null) {
                    if (!SendCMD.map.isEmpty()) {
                        String cmd = backString.substring(4, 14);
                        String rect = SendCMD.map.get(cmd);
                        if (rect != null) {
                            //讯飞语音修改
                            //	AduioCompound aduioCompound = new AduioCompound(
                            //		MainActivity.getInstance(), rect);
                            TtsService.getInstance().setInit(MainActivity.getInstance(), rect);
                            SendCMD.map.remove(cmd);
                        }
                    }
                }
                */
            }
        }
    }

    static String[] _songCode = new String[8];
    static String _studyCode = "";
    public static void analysisInfrared_code(String string) {

        Integer index = Integer.valueOf(string.substring(6, 8), 16);
        Integer len = Integer.valueOf(string.substring(8, 10), 16);
        Integer number = Integer.valueOf(string.substring(18, 20), 16);
        if (index == 255) {

            int i = 0;
            for (; i < _songCode.length; i++) {
                String sss = _songCode[i];
                if (sss.length() > 0) {
                    _studyCode += sss;
                } else {
                    break;
                }
            }

            if (_studyCode.length() > 3
                    && (((number == i) && (number > 0)) || (number == 0))) {
                // restulBlock(_studyCode);
            }
        } else {
            if (index == 0) {
                _songCode = new String[8];
                _studyCode = "";
            }
            // [_songCode addObject:[string substringWithRange:NSMakeRange(10,
            // 2*len)]];
        }
    }

    public static void modeCode(String blackCode) {

        String mCmdType = blackCode.substring(4, 6);
        // String mCmdType = blackCode.substring(2, 4);
        int Total = (Integer.valueOf(blackCode.substring(10, 12), 16));
        int stateVal = Integer.valueOf(blackCode.substring(8, 10), 16);
        String acttype = MyApplication.getInstance().getString(
                R.string.option_fail);
        if (mCmdType.equals("03")) {// 查询
            acttype = MyApplication.getInstance().getString(
                    R.string.search_success);
            acttype = acttype+Total
                    + MyApplication.getInstance().getString(R.string.all)
                    + 250
                    + MyApplication.getInstance().getString(R.string.box);
        } else if (mCmdType.equals("04")) {// 下载
            acttype = MyApplication.getInstance().getString(R.string.down_end);
            acttype = acttype+Total
                    + MyApplication.getInstance().getString(R.string.all)
                    + 250
                    + MyApplication.getInstance().getString(R.string.box);
        } else if (mCmdType.equals("05")) {// 剩余空间
            acttype = MyApplication.getInstance().getString(
                    R.string.action_space);
            acttype = acttype+Total
                    + MyApplication.getInstance().getString(R.string.all)
                    + 250
                    + MyApplication.getInstance().getString(R.string.box);
            if (modeMsgListener != null) {
                modeMsgListener.modeNumber(Total);
            }
        } else {
            if (blackCode.substring(6, 8).equals("01")) {
                if (mCmdType.equals("00")) { // del
                    acttype = MyApplication.getInstance().getString(
                            R.string.delSuccess);
                    if (Total > 1) {
                        acttype = acttype
                                + ((Total > 1) ? (MyApplication.getInstance()
                                .getString(R.string.all) + Total + MyApplication
                                .getInstance().getString(R.string.box))
                                : "");
                    } else {
                        acttype = MyApplication.getInstance().getString(
                                R.string.sort)
                                + (stateVal + 1)
                                + MyApplication.getInstance().getString(
                                R.string.box) + acttype;// +
                    }
                } else if (mCmdType.equals("01")) { // add
                    acttype = MyApplication.getInstance().getString(
                            R.string.addSuccess);
                    acttype = MyApplication.getInstance().getString(
                            R.string.sort)
                            + (stateVal + 1)
                            + MyApplication.getInstance().getString(
                            R.string.box) + acttype;
                    // }
                } else if (mCmdType.equals("02")) {
                    acttype = MyApplication.getInstance().getString(
                            R.string.updateSuccess);
                    acttype = MyApplication.getInstance().getString(
                            R.string.sort)
                            + stateVal
                            + MyApplication.getInstance().getString(
                            R.string.box) + acttype;
                }
            }
        }
        if (modeMsgListener != null) {
            modeMsgListener.sendMsg(acttype);
        }
        // / MainActivity.getInstance().showToast(acttype, 3000);
    }

    static String _PredeviceID = "";
    public static boolean _isLearnFlag = false;
    static int preIndex = -1, preValue = 0;

    static void getLearnDevice(String backString, int index) { //

        if (_isLearnFlag) {
            // Type ID MID1 MID2
            String NowdeviceID;
            String Type;
            String did;
            int getValue = 0;
            //*C(00)(cmd)(發送方)(接收方)(ID)(MID1)(MID2)
            Type = backString.substring(8, 10);
            int typeId = Integer.valueOf(Type, 16);
            if(index<2) {
                did = backString.substring(10, 16);
                NowdeviceID = Type + did;
                if (index == 0) {
                    getValue = Integer.valueOf(backString.substring(16, 18), 16);
                }
                if (_PredeviceID.equals(NowdeviceID) && (preIndex == index)) {
                    Device device = new Device();
                    boolean isDioFlag = false;
                    int loop = 0;
                    if ((typeId ==2)||(typeId==12)){
                        isDioFlag = true;
                        loop = 2;
                    }
                    else if (typeId ==3)
                    {
                        isDioFlag = true;
                        loop = 3;
                    }
                    if (isDioFlag && (index == 0)) {
                        if (preValue != getValue) {
                            for (int i = 0; i < loop; i++) {
                                if ((getValue & (1 << i)) > 0) {
                                    device.setDeviceIO(i + "");
                                    break;
                                }
                            }
                        } else {
                            preValue = getValue;
                            isDioFlag = false;
                        }
                    } else {
                        isDioFlag = true;
                    }
                    if (isDioFlag) {
                        device.setProductsCode(Type);
                        device.setDeviceID(NowdeviceID.substring(2));
                        device.setRoomId(1);
                        Products p = ProductFactory.getProduct(Type);
                        device.setDeviceTypeKey(p.getDeviceTypekey());
                        device.setProductsKey(p.getProductsKey());
                        device.setDeviceImage(p.getProductsImage());
                        device.setProductsIO(p.getProductsIO());
                        _isLearnFlag = false;
                        preIndex = -1;
                        if (deviceAddListener != null) {
                            deviceAddListener.setLearnDevice(device);
                        }
                    }
                } else {
                    if (index == 0) {
                        preValue = getValue;
                    }
                    preIndex = index;
                    _PredeviceID = NowdeviceID;
                }
            }else{
                //*C001F(type)FF00000000(ID)(mid1)(mid2)
                //*C001FAA(type)(ID)(MID1)(MID2)
                //*C8712010308090302020083
                Device device = new Device();
                device.setProductsCode(Type);
                NowdeviceID=backString.substring(10, 16);
                device.setDeviceID(NowdeviceID);
                device.setDeviceIO("0");
                device.setRoomId(1);
                Products p = ProductFactory.getProduct(Type);
                device.setDeviceTypeKey(p.getDeviceTypekey());
                device.setProductsKey(p.getProductsKey());
                device.setDeviceImage(p.getProductsImage());
                device.setProductsIO(p.getProductsIO());
                _isLearnFlag = false;
                preIndex = -1;
                if (deviceAddListener != null) {
                    deviceAddListener.setLearnDevice(device);
                }
            }
        } else {
            preIndex = -1;
        }
    }

    public static void setAirSwitchListener(AirSwitchListener airSwitchListener) {
        TcpSender.airSwitchListener = airSwitchListener;
    }

    public static void setTimmingListener(TimmingListener timmingListener) {
        TcpSender.timmingListener = timmingListener;
    }

    public static void setRouteListener(RouteListener routeListener) {
        TcpSender.routeListener = routeListener;
    }

    public static void setSafeListener(SafeListener safeListener) {
        TcpSender.safeListener = safeListener;
    }
}
