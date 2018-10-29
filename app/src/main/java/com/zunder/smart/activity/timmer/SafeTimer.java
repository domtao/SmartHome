package com.zunder.smart.activity.timmer;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class SafeTimer {
    private volatile static SafeTimer install;
    long recordTime=0;
    long recordIOTime=0;
    int powerState;//总状态
    int passIndex;//旁路
    int eleIndex;//低电量
    int disIndex;//防拆
    int alramIndex;//安防类型
    int openModeIndex;
    int closeModeIndex;
    int nocIndex;
    int openDoorIndex;
    int closeDoorIndex;

    private OnSafeIndexSureListener onSafeIndexSureListener;
    public static SafeTimer getInstance() {
        if (null == install) {
            install = new SafeTimer();
        }
        return install;
    }
    public void setBackCode(String cmd,OnSafeIndexSureListener onSafeIndexSureListener) {
        this.onSafeIndexSureListener = onSafeIndexSureListener;
        String productTypeId=cmd.substring(4,10);
        if(productTypeId.equals("0700FF")){
            long devicBackTime=0;
            boolean isFresh=false;
            if(cmd.length()>22){
                devicBackTime=Long.valueOf(cmd.substring(21));
                if(devicBackTime>=recordTime){
                    recordTime=devicBackTime;
                    isFresh=true;
                }
            }
            if(isFresh){
                int setupState=Integer.valueOf(cmd.substring(15,17),16);
                if(setupState>0){
                    powerState=1;
                }else{
                    powerState=0;
                }
                if(setupState==2){
                    alramIndex=1;
                }else if(setupState==3){
                    alramIndex=2;
                }else if(setupState==4){
                    alramIndex=3;
                }else {
                    alramIndex=0;
                }
                handler.sendEmptyMessage(0);
            }
        }

    }
    public void setIOBackeCode(String cmd,OnSafeIndexSureListener onSafeIndexSureListener) {
        this.onSafeIndexSureListener = onSafeIndexSureListener;
            long devicBackTime=0;
            boolean isFresh=true;
            if(cmd.length()>22){
                isFresh=false;
                devicBackTime=Long.valueOf(cmd.substring(21));
                if(devicBackTime>=recordIOTime){
                    recordIOTime=devicBackTime;
                    isFresh=true;
                }
            }
            if(isFresh) {
                int state_1 = Integer.valueOf(cmd.substring(15, 17), 16);
                int state_2 = Integer.valueOf(cmd.substring(18, 20), 16);
                if ((state_2 & 0x04) > 0) { // bypass on
                    passIndex = 1;

                } else {// off
                    passIndex = 0;
                }

                if ((state_2 & 0x08) > 0) { // Setup on
                    powerState = 1;
                    //
                } else {
                    // off
                    powerState = 0;
                }
                if ((state_1 & 0x10) > 0) {
                    openModeIndex = 1;
                } else {
                    openModeIndex = 0;
                }

                if ((state_1 & 0x20) > 0) {
                    closeModeIndex = 1;
                } else {
                    closeModeIndex = 0;
                }

                if ((state_1 & 0x02) > 0) { // Setup on
                    nocIndex = 1;//NO
                } else { // off
                    nocIndex = 0;//NC
                }

                if ((state_1 & 0x04) > 0) {
                    openDoorIndex = 1;
                } else {
                    openDoorIndex = 0;
                }
                if ((state_1 & 0x08) > 0) {
                    closeDoorIndex = 1;

                } else {
                    openDoorIndex = 0;
                }

                if ((state_1 & 0x40) > 0) {//低电量
                    eleIndex = 1;
                } else {
                    eleIndex = 0;
                }
                if ((state_1 & 0x80) > 0) {//防拆
                    disIndex = 1;
                } else {
                    disIndex = 0;
                }
                if ((state_2 & 0x02) > 0) {
                    if (((state_2 & 0x03) == 0x03) && ((state_2 & 0xf0) < 0x40)) {
                        int deviceValue = 0;
                        int musicid = R.raw.panicalarm;
                        if (deviceValue < 2) {
                            //火警
                            alramIndex = 2;
                        } else if (deviceValue < 4) {
                            // 瓦斯
                            alramIndex = 3;
                        } else if (deviceValue < 8) {
                            //求救
                            alramIndex = 4;
                        } else if (deviceValue < 29) {
                            //入侵
                            alramIndex = 5;
                        } else {
                            //其它
                            alramIndex = 6;
                        }
                    }
                } else {
                    if ((state_2 & 0x01) > 0) { // sensor on״̬
                        //chang.setImageResource(R.mipmap.widget_pic_on);开
                        alramIndex = 1;
                    } else { // off
                        // chang.setImageResource(R.mipmap.widget_pic_off);关
                        alramIndex = 0;
                    }
                }
                handler.sendEmptyMessage(1);
            }
    }



    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onSafeIndexSureListener!=null){
                        onSafeIndexSureListener.onState(powerState,alramIndex);
                    }
                }
              else  if (msg.what == 1) {
                    if(onSafeIndexSureListener!=null){
                        onSafeIndexSureListener.onIOState(powerState,alramIndex,passIndex,eleIndex,disIndex,openModeIndex,closeModeIndex,nocIndex,openDoorIndex,closeDoorIndex);
                        ;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnSafeIndexSureListener {
        public void onIOState(int powerState,int alramIndex,int passIndex,int eleIndex,int disIndex,int openModeIndex,int closeModeIndex,int nocIndex,int openDoorIndex, int closeDoorIndex);
        public  void onState(int powerState,int alramIndex);
    }

}
