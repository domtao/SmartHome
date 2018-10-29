package com.zunder.smart.activity.timmer;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class LockTimer {
    private volatile static LockTimer install;

    private Device device;
    int powerState;
    int imageIndex;

    private OnLockIndexSureListener onLockIndexSureListener;
    public static LockTimer getInstance() {
        if (null == install) {
            install = new LockTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnLockIndexSureListener onLockIndexSureListener){
        if(_device!=null) {
            this.onLockIndexSureListener = onLockIndexSureListener;
            this.device = _device;
            int state = device.getDeviceAnalogVar2();
            if ((state & 1) > 0) {
                powerState=1;
            } else {
                powerState=0;
            }
            if ((state & 2) > 0) {
                imageIndex=1;
            } else {
                imageIndex=0;
            }
            handler.sendEmptyMessage(0);

        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onLockIndexSureListener!=null){
                        onLockIndexSureListener.onState(powerState,imageIndex);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnLockIndexSureListener {
        public void onState(int powerState,int imageIndex);
    }

}
