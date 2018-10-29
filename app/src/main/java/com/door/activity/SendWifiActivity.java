package com.door.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.door.DoorApplication;
import com.zunder.smart.R;
import com.hdl.udpsenderlib.UDPResult;
import com.jwkj.soundwave.ResultCallback;
import com.jwkj.soundwave.SoundWaveManager;
import com.jwkj.soundwave.SoundWaveSender;
import com.jwkj.soundwave.bean.NearbyDevice;

public class SendWifiActivity extends Activity implements View.OnClickListener {
    private boolean isNeedSendWave = true;//是否需要发送声波，没有接到正确数据之前都需要发送哦
    private Button btn_sendWifi;

    private TextView tv_log, backTxt;
    private EditText et_password;
    private EditText et_ssid;
    private String wifiSSID;//wifi名字
    private String wifiPwd;//wifi密码
    byte type = 9;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendwifi);
        boolean isSuccess = SoundWaveManager.init(this);//初始化声波配置
        activity=this;
        tv_log = (TextView) findViewById(R.id.tv_log);
        backTxt = (TextView) findViewById(R.id.backTxt);
        backTxt.setOnClickListener(this);
        btn_sendWifi = (Button) findViewById(R.id.btn_sendWifi);
        et_ssid = (EditText) findViewById(R.id.et_ssid);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_sendWifi.setOnClickListener(this);
        et_ssid.setText(getSSID());
        if(DoorApplication.getInstance().isConnect()){
            DoorApplication.getInstance().attemptLogin();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    /**
     * 开始发送声波
     *
     */
    public void onSend() {
        isNeedSendWave=true;//每次点击发送都要设置为可以继续发送
        wifiSSID=et_ssid.getText().toString().trim();
        wifiPwd = et_password.getText().toString().trim();//记录密码
        tv_log.append(getString(R.string.soundsend)+"\n");
        sendSoundWave();
    }
    /**
     * 开始发送声波
     */
    private void sendSoundWave() {
        SoundWaveSender.getInstance()
                .with(this)
                .setWifiSet(wifiSSID, wifiPwd)
                .send(new ResultCallback() {

                    @Override
                    public void onNext(UDPResult udpResult) {
                        if(isNeedSendWave) {
                            NearbyDevice device = NearbyDevice.getDeviceInfoByByteArray(udpResult.getResultData());
                            device.setIp(udpResult.getIp());
                            tv_log.setText(getString(R.string.net_success)+
                                    ":"+ device.toString());
                            isNeedSendWave = false;
                            String ip = device.getIp();
                            int index =ip.lastIndexOf(".");
                            String ipFlag = ip.substring(index+1);
                            Intent add_device = new Intent(SendWifiActivity.this,
                                    InitPwdActivity.class);
                            add_device.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            add_device.putExtra("ipFlag", ipFlag);
                            add_device.putExtra("deviceID", device.getDeviceId());

                            startActivity(add_device);
                            finish();
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        SoundWaveSender.getInstance().stopSend();//出错了就要停止任务，然后重启发送
                    }

                    /**
                     * 当声波停止的时候
                     */
                    @Override
                    public void onStopSend() {
                        if (isNeedSendWave) {//是否需要继续发送声波
                            tv_log.append(getString(R.string.soundsend1)+"...\n");
                            sendSoundWave();
                        } else {
                            //结束了就需要将发送器关闭
                            SoundWaveSender.getInstance().stopSend();
                            SoundWaveManager.onDestroy(activity);
                        }
                    }
                });
    }
    /**
     * 停止发送声波
     *
     * @param view
     */
    public void onStopSoundWave(View view) {
        SoundWaveSender.getInstance().with(this).stopSend();
    }
    private String getSSID() {
        WifiManager manager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();

        return wifiInfo.getSSID().replace("\"", "");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendWifi:
                onSend();
                break;
            case R.id.backTxt:
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        SoundWaveSender.getInstance().with(this).stopSend();
        SoundWaveManager.onDestroy(this);
        super.onDestroy();
    }

}
