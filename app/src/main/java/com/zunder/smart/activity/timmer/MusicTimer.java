package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.bumptech.glide.Glide;
import com.zunder.smart.model.Device;

public class MusicTimer {
    private volatile static MusicTimer install;

    private Device device;


    int powerState;
    int muteState;
    int volume;
    int playState;

    private OnMusicIndexSureListener onMusicIndexSureListener;
    public static MusicTimer getInstance() {
        if (null == install) {
            install = new MusicTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnMusicIndexSureListener onMusicIndexSureListener){
        if(_device!=null) {
            this.onMusicIndexSureListener = onMusicIndexSureListener;
            this.device = _device;
            powerState=device.getState();
            if (((device.getDeviceAnalogVar3()>>1)&3)==1) {
                playState=1;
            } else if (((device.getDeviceAnalogVar3()>>1)&3)==0){
                playState=0;
            }
            volume=device.getDeviceAnalogVar2();
             muteState = (device.getDeviceAnalogVar3() & 8) > 0 ? 1 : 0;
             handler.sendEmptyMessage(0);

        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onMusicIndexSureListener!=null){
                        onMusicIndexSureListener.onState(powerState,playState,muteState,volume);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnMusicIndexSureListener {
        public void onState(int powerState,int playState,int muteState,int volume);
    }

}
