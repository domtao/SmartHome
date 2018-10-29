package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zunder.smart.model.Device;

public class CurTimer {
    private volatile static CurTimer install;

    private Device device;
    private int TimeSet;
    private int ImageIndex;
    private int cycleIndex;
    private int isSuccess=0;
    private int processState=0;
    private OnCurIndexSureListener onCurIndexSureListener;
    public static CurTimer getInstance() {
        if (null == install) {
            install = new CurTimer();
        }
        return install;
    }


    //joe ffrompage back code窗帘
    public void setBackeCode(Device _device,OnCurIndexSureListener onCurIndexSureListener){
        this.onCurIndexSureListener=onCurIndexSureListener;
        this.device=_device;
        TimeSet=device.getSetTime();
        ImageIndex=device.getLoadImageIndex();
        handler.sendEmptyMessage(0);
    }

    //joe   code窗帘
    public void setCmdCode(Device deviceString,String cmd,OnCurIndexSureListener onCurIndexSureListener){
        this.onCurIndexSureListener=onCurIndexSureListener;
        if (cmd.length()>=24) {
            String dataType = cmd.substring(4, 6);
            String nowDeviceId= cmd.substring(10, 16);
            if(dataType.equals("12")){
                dataType = cmd.substring(8, 10);
                if (dataType.equals(device.getProductsCode())&&nowDeviceId.equals(device.getDeviceID())) {
                    if ( cmd.substring(18, 20).equals("05")){
                        //memory
                        cycleIndex =Integer.valueOf(cmd.substring(22,24),16);
                        processState=cmd.substring(6,8).equals("FA")?1:0;
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if (onCurIndexSureListener != null) {
                        onCurIndexSureListener.onState(ImageIndex);
                    }
                }else  if (msg.what == 1) {
                    if (onCurIndexSureListener != null) {
                        onCurIndexSureListener.onCycle(cycleIndex,isSuccess,processState);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnCurIndexSureListener {
        public void onState(int index);
        public void onCycle(int cycleIndex,int isSuccess,int processState);
    }

}
