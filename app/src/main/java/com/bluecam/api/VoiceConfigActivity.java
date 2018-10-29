package com.bluecam.api;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluecam.bluecamlib.CameraManager;
import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.mediatek.elian.ElianNative;

import voice.encoder.DataEncoder;
import voice.encoder.VoicePlayer;
import voice.encoder.VoicePlayerListener;

/**
 * Created by Administrator on 2017/7/31.
 */

public class VoiceConfigActivity extends BaseActivity {
    private EditText wifi_ssid_et;
    private EditText wifi_pwd_et;
    private Button done_btn;
    private WifiManager mWifiManager;
    private String mConnectedSsid;
    private int mLocalIp;
    private VoicePlayer player;
    private boolean isExit;
    private String wifi_name;
    private String wifi_pwd;
    private String key = "0123456789012345" ;
    private TextView backTxt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        initView();
        initNetWorkParam();
        autoSetAudioVolumn();
        //声波初始化
        player  = CameraManager.startVoicePlayer();
        player.setListener(new VoicePlayerListener(){
            @Override
            public void onPlayEnd(VoicePlayer arg0) {

            }
            @Override
            public void onPlayStart(VoicePlayer arg0) {
            }
        });
        //Smart Config 初始化
        int libVersion = ElianNative.GetLibVersion();
        int protoVersion = ElianNative.GetProtoVersion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isExit = true;
        ElianNative.StopSmartConnection();
        if(player != null)
        {
            player.stop();
        }
    }

    private void initView(){
        initHeaderBar();
        setTitle_txt("兴未来一键配置");
        backTxt=(TextView)findViewById(R.id.back_iv) ;
        backTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wifi_ssid_et = (EditText)findViewById(R.id.wifi_ssid_et);
        wifi_pwd_et = (EditText)findViewById(R.id.wifi_pwd_et);
        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("正在配置wifi，请等待30秒");
                timeHandler.sendEmptyMessageDelayed(0,30*1000);
                done();
            }
        });
    }

    private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hideProgressDialog();
            isExit = false;
            ToastUtils.ShowSuccess(VoiceConfigActivity.this,"网络配置成功!", Toast.LENGTH_SHORT,true);
        }
    };
    private void done(){
        wifi_name = wifi_ssid_et.getText().toString();
        wifi_pwd  = wifi_pwd_et.getText().toString();
        startSendVoice();
        startSendElian();
    }
    private void initNetWorkParam()
    {
        mWifiManager = (WifiManager)getApplicationContext().getSystemService (WIFI_SERVICE);
        if(mWifiManager.isWifiEnabled())
        {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();

            mConnectedSsid = wifiInfo.getSSID();
            int iLen = mConnectedSsid.length();

            if (iLen == 0)
            {
                return;
            }
            if (mConnectedSsid.startsWith("\"") && mConnectedSsid.endsWith("\""))
            {
                mConnectedSsid = mConnectedSsid.substring(1, iLen - 1);
            }
            mLocalIp = wifiInfo.getIpAddress();
            wifi_ssid_et.setText(mConnectedSsid);//设置wifi名称
        }
        else
        {
            showDialog("手机没连接wifi，请到手机wifi设置里连接一个WIFI");
        }
    }

    //开始声波配置
    private void startSendVoice()
    {
       new Thread(){
           @Override
           public void run() {
               if(player == null)
               {
                   return;
               }
               int count = 0;
               do
               {
                   if(isExit)
                   {
                       player.stop();
                       return;
                   }
                   player.play(DataEncoder.encodeSSIDWiFi(wifi_name, wifi_pwd), 1, 200);
                   try {
                       Thread.sleep(5000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   count = count+1;
               }while (count<=2);
           }
       }.start();
    }


    //开始无线配置
    private void startSendElian()
    {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int libVersion = ElianNative.GetLibVersion();
                int protoVersion = ElianNative.GetProtoVersion();
                System.out.println("WifiConfigSetup3Activity send Elian ssid=="+wifi_name+" pwd=="+wifi_pwd+" key=="+key);
                ElianNative.InitSmartConnection(null, 0, 1);
                int ret = ElianNative.StartSmartConnection(wifi_name, wifi_pwd, key);
                System.out.println("StartSmartConnection ret == "+ret);
            }
        }.start();
    }
    @Override
    protected  void doDialogOK(){
        finish();
    }
}
