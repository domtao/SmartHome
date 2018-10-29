package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.model.Device;

public class TvTimer {
    private volatile static TvTimer install;

    private Device device;
    int powerState;

    private OnTvIndexSureListener onTvIndexSureListener;
    public static TvTimer getInstance() {
        if (null == install) {
            install = new TvTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnTvIndexSureListener onTvIndexSureListener){
        if(_device!=null) {
            this.onTvIndexSureListener = onTvIndexSureListener;
            this.device = _device;
            powerState=device.getState();
             handler.sendEmptyMessage(0);

        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onTvIndexSureListener!=null){
                        onTvIndexSureListener.onState(powerState);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnTvIndexSureListener {
        public void onState(int powerState);
    }

}
