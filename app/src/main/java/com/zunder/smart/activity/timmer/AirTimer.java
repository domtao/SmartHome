package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class AirTimer {
    private volatile static AirTimer install;

    private Device device;
    private  boolean startflag = false;
    int powerState;
    int tempIndex;
    int modeIndex;
    int speedIndex;
    private OnAirIndexSureListener onCurIndexSureListener;
    public static AirTimer getInstance() {
        if (null == install) {
            install = new AirTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnAirIndexSureListener onCurIndexSureListener) {
        if(_device!=null){
            this.onCurIndexSureListener = onCurIndexSureListener;
            this.device = _device;
            this.powerState=device.getState();

            if (powerState > 0) {
                tempIndex= device.getDeviceAnalogVar3()&63;
            } else {
                tempIndex=0;
                modeIndex=0;
                speedIndex=0;
            }
            this.modeIndex=device.getDeviceAnalogVar2()& 15;
            if (device.getDeviceAnalogVar2() / 16 == 0
                    && powerState > 0) {
                if (startflag == false) {
                    startflag = true;
                    startTime();
                }
            } else {
                startflag = false;
                speedIndex=device.getDeviceAnalogVar2() / 16;
                handler.sendEmptyMessage(0);
            }
        }else{
            startflag=false;
        }
//        im_show_by
//                .setBackgroundResource((((deviceParams
//                        .getDeviceAnalogVar3() / 16) > 0) ? R.mipmap.wind_vertical_black
//                        : R.mipmap.img_air_direction_rock));
    }

    private  void startTime() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (startflag) {
                    try {
                        Thread.sleep(400);
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onCurIndexSureListener!=null){
                        onCurIndexSureListener.onState(powerState,tempIndex,modeIndex,speedIndex);
                        }
                }else if(msg.what==1){
                    if(startflag){
                        speedIndex++;
                        if (speedIndex > 5) {
                            speedIndex = 0;
                        }
                        if(onCurIndexSureListener!=null){
                            onCurIndexSureListener.onState(powerState,tempIndex,modeIndex,speedIndex);
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnAirIndexSureListener {
        public void onState(int powerState,int tempIndex,int modeIndex,int speedIndex);
    }

}
