package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.model.Device;

public class LightTimer {
    private volatile static LightTimer install;

    private int powerState;
    private int ioIndex1;
    private int ioIndex2;
    private int ioIndex3;
    private int ioIndex4;


    private OnLightIndexSureListener onLightIndexSureListener;
    public static LightTimer getInstance() {
        if (null == install) {
            install = new LightTimer();
        }
        return install;
    }
    public void setBackeCode(String cmd,OnLightIndexSureListener onLightIndexSureListener){
        this.onLightIndexSureListener=onLightIndexSureListener;
        powerState=Integer.valueOf(cmd.substring(18, 20), 16);

        ioIndex1=((powerState&1)>0)?1:0;
        ioIndex2=((powerState&2)>0)?1:0;
        ioIndex3=((powerState&4)>0)?1:0;
        ioIndex4=((powerState&8)>0)?1:0;
        handler.sendEmptyMessage(0);
    }

    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onLightIndexSureListener!=null){
                            onLightIndexSureListener.onState(powerState,  ioIndex1,ioIndex2,ioIndex3,ioIndex4);
                        }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnLightIndexSureListener {
        public void onState(int powerState,  int ioIndex1,int ioIndex2,int ioIndex3,int ioIndex4);
    }

}
