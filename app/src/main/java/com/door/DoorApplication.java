package com.door;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.door.Utils.Contants;
import com.door.Utils.ToastUtils;
import com.door.sdk.P2PListener;
import com.door.sdk.SettingListener;
import com.door.service.MainService;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.tools.SystemInfo;
import com.libhttp.entity.LoginResult;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16/016.
 */

public class DoorApplication {
    public static DoorApplication instance=null;
    private Context context;
    private boolean isConnect=false;
    public static DoorApplication getInstance(){
        if(instance==null){
            instance=new DoorApplication(MyApplication.getInstance());
        }
        return instance;
    }
    public DoorApplication(Context context){
        this.context=context;
    }
    public boolean isConnect() {
        return isConnect;
    }

    private void registReg() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Contants.P2P_CONNECT);
        context.registerReceiver(receiver, filter);
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connect = intent.getBooleanExtra("connect", false);
            //p2p连接失败  相应处理，用户可以根据具体情况自定义
            if (connect) {
                ToastUtils.ShowSuccess( MyApplication.getInstance(),context.getString(R.string.doorsuccess), Toast.LENGTH_LONG,true);
                isConnect=true;
                List<GateWay> list = GateWayService.list;
                if(list.size()>0) {
                    for (int i = 0; i < list.size(); i++) {
                        GateWay gateWay = list.get(i);
                        if (gateWay.getTypeId() == 4 && !gateWay.getState().equals(context.getString(R.string.line))) {
                            String userName = gateWay.getGatewayID();
                            String passWord = gateWay.getUserPassWord();
                            String pwd = P2PHandler.getInstance().EntryPassword(passWord);
                            //经过转换后的设备密码
                            P2PHandler.getInstance().checkPassword(userName, pwd);
                            break;
                        }
                    }
                }
            }
        }
    };

    public void attemptLogin() {
        registReg();
        MyApplication.getInstance().initP2p();
        SubscriberListener<LoginResult> subscriberListener = new SubscriberListener<LoginResult>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onNext(LoginResult loginResult) {

                //error code 全部改为了新版,如果没有老版对应 的反馈码则可忽略此错误
                //如果不可以忽略,则反馈给技术支持即可
                switch (loginResult.getError_code()) {
                    case HttpErrorCode.ERROR_0:
                        saveAuthor(loginResult);
                        P2PHandler.getInstance().p2pInit(context, new P2PListener(), new SettingListener());
                        Intent mainIntent = new Intent(context, MainService.class);
                        context.startService(mainIntent);
                        break;
                    case HttpErrorCode.ERROR_10902011:
                        ToastUtils.ShowError(MyApplication.getInstance(),"用户不存在", Toast.LENGTH_LONG,true);
                        break;
                    case HttpErrorCode.ERROR_10902003:
                        break;
                    default:
                        //其它错误码需要用户自己实现
                        String msg = String.format("登录失败测试版(%s)", loginResult.getError_code());
                        break;
                }
            }

            @Override
            public void onError(String error_code, Throwable throwable) {

            }
        };
        try {
//			if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            HttpSend.getInstance().ThirdLogin("3", SystemInfo.getMasterID(context), "", "", "0", "3", subscriberListener);
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        //*************************技威代码插入**********************************
    }
    private void saveAuthor(LoginResult loginResult){
        int code1 = Integer.parseInt(loginResult.getP2PVerifyCode1());
        int code2 = Integer.parseInt(loginResult.getP2PVerifyCode2());
        String sessionId =loginResult.getSessionID();
        String sessionId2 =loginResult.getSessionID2();
        String userId =  loginResult.getUserID();
        SharedPreferences sp=context.getSharedPreferences("door_info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putInt("code1",code1);
        editor.putInt("code2",code2);
        editor.putString("sessionId",sessionId);
        editor.putString("sessionId2",sessionId2);
        editor.putString("userId",userId);
        editor.commit();
    }

}
