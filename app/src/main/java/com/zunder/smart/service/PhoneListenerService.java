package com.zunder.smart.service;

import java.lang.reflect.Method;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zunder.smart.dao.impl.factory.ContactFactory;
import com.zunder.smart.dao.impl.factory.ContactFactory;

public class PhoneListenerService extends Service {
    private MediaRecorder recorder;
    private boolean recording = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.v("TAG", "service onCreate()");
        super.onCreate();
        //电话服务管理
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    private PhoneStateListener listener = new PhoneStateListener() {
        /*
         * @see TelephonyManager#CALL_STATE_IDLE 值为0
         *
         * @see TelephonyManager#CALL_STATE_RINGING 值为1
         *
         * @see TelephonyManager#CALL_STATE_OFFHOOK 值为2
        */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //打印电话状态改变信息
            Log.v("TAG", "onCallStateChanged state=" + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 没有来电 或者 挂断
//                    stopRecord();
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 响铃时
                    stop(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 接起电话
//                    recordCalling();
                    break;
                default:
                    break;
            }
        }
    };
    //停止录音
    private void stopRecord() {
        Log.v("TAG", "stopRecord");
        if (recording) {
            recorder.stop();
            recorder.release();
            recording=false;
        }
    }
    //电话拦截
    public void stop(String s) {
        try {
           String name= ContactFactory.getInstance().getContactName(s);
            SendCMD sendCMD = new SendCMD().getInstance();
            sendCMD.sendCMD(0, name, null);
            recording=false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //进行录音
    private void recordCalling() {
        try {
            Log.v("TAG", "recordCalling");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 读麦克风的声音
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 输出格式.3gp
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 编码方式
            recorder.setOutputFile(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/"
                    + System.currentTimeMillis()
                    + ".3gp");// 存放的位置是放在sdcard目录下
            recorder.prepare();
            recorder.start();
            recording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

