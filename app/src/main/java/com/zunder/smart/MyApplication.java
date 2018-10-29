package com.zunder.smart;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import cn.jpush.android.api.JPushInterface;

import com.bluecam.bluecamlib.CameraManager;
import com.zunder.base.menu.RMCTabView;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.WidgetDAOProxy;
import com.zunder.smart.langvage.LanguageUtil;
import com.zunder.smart.model.GateWayMsg;
import com.zunder.smart.netty.MockLoginNettyClient;
import com.zunder.smart.tools.PackageNameEntry;
import com.zunder.smart.utils.CrashCollector;
import com.iflytek.cloud.SpeechUtility;

import com.p2p.core.P2PSpecial.P2PSpecial;

import com.zunder.base.RMCBaseView;
import com.zunder.base.RMCMenuView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */

public class MyApplication extends Application {

    private SharedPreferences spf;
    private static MyApplication instance = null;
    public final static String APPID="6ce8e9af6aca0258c8ac23f2bc19cdf4";
    public final static String APPToken="9ed9ef0746dabc4fa89664faddedd8bd4ff57e230bab544f3a7dcd387abf0c61";
    public final static String APPVersion="04.76.00.00";
    public static String AlarmWake="";
    public static boolean SystemStart=false;
    private String WifiName;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        spf = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        CrashCollector collector = CrashCollector.getInstance();
        collector.init(getApplicationContext());
        SpeechUtility
                .createUtility(this, "appid=" + getString(R.string.app_id));
        instance = this;
        JPushInterface.init(this);
        JPushInterface.initCrashHandler(this);
        LanguageUtil.getInstance().onAttach(instance);
        CameraManager.getDeviceManager(MyApplication.getInstance()).initialize();
        initWifiName();
    }
    public void initP2p(){
        P2PSpecial.getInstance().init(getInstance(),APPID,APPToken,APPVersion);
    }
    public void initWifiName(){
        ConnectivityManager connectivityManager = (ConnectivityManager) instance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) instance
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                setWifiName( wifiInfo.getSSID());
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                setWifiName("phone");

            }
        }else{
            setWifiName("none");
        }
    }

    public String getRootPath() {
        return android.os.Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "ZunderHome";
    }

    public String getImagePath() {
        return getRootPath()+File.separator + "SImage";
    }

    public String getDiaryPath() {
        return getRootPath() + File.separator + "diary";
    }

    public static MyApplication getInstance() {
        if(instance==null){
            instance=new MyApplication();
        }
        return instance;
    }
    public Drawable getDrawabled(int resId) {
        return instance.getResources().getDrawable(resId);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        MockLoginNettyClient.getInstans().quite();
    }
    public IWidgetDAO getWidgetDataBase() {
        return WidgetDAOProxy.getInstance();
    }

    public void isExite() {
        String path = getRootPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        path = getRootPath() + "/Root";
        file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        path = getRootPath() + "/SImage";
        file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

    }

    public void initFiles() {
        // TODO Auto-generated method stub
//        SmartHome
        isExite();
        String file = android.os.Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + "ZunderHome"
                + File.separator + "/Root/homedatabases.db";
        if (!new File(file).exists()) {
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                is = this.getResources().openRawResource(R.raw.homedatabases);
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean isActivityForeground(String className) {

        ActivityManager am = (ActivityManager) getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }
    public  String getAPPVersion(){
        String result="";
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            result= info.versionName;

        }catch (Exception e){

            result="0.0.0";
        }
        return result;
    }
    public String UserName(){
        if(spf==null){
            spf = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        }
        return spf.getString("userName", "");
    }

    public String PassWord(){
        if(spf==null){
            spf = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        }
        return spf.getString("passWord", "");
    }
    public String PrimaryKey(){
        if(spf==null){
            spf = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        }
        return spf.getString("PrimaryKey", "");
    }

    public int isLogin(){
        if (spf.getString("userName", "").equals("")
                || spf.getString("passWord", "").equals("")) {
        return 0;
        }
        return 1;
    }

    public PackageNameEntry getApkName(String appName) {
        PackageNameEntry packageNameEntry=null;
        List<PackageInfo> packageInfos = getPackageManager()
                .getInstalledPackages(
                        PackageManager.GET_UNINSTALLED_PACKAGES
                                | PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < packageInfos.size(); i++) {
            try {
                PackageInfo temp = packageInfos.get(i);
                String name=temp.applicationInfo
                        .loadLabel(getPackageManager())+"";
                if(appName.equals(name)) {
                    packageNameEntry = new PackageNameEntry();
                    packageNameEntry.setName(name);
                    packageNameEntry.setPackegeName(temp.packageName);
                    packageNameEntry.setActivity(temp.activities[0].name);
                    break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return packageNameEntry;
    }

    public List<GateWayMsg> getApkName() {
        List<GateWayMsg> list=new ArrayList<GateWayMsg>();
        List<PackageInfo> packageInfos = getPackageManager()
                .getInstalledPackages(
                        PackageManager.GET_UNINSTALLED_PACKAGES
                                | PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < packageInfos.size(); i++) {
            try {
                PackageInfo temp = packageInfos.get(i);
                String name=temp.applicationInfo
                        .loadLabel(getPackageManager())+"";
                GateWayMsg cameraMsg = new GateWayMsg();
                cameraMsg.setDeviceName(name);
                cameraMsg.setDeviceID(temp.activities[0].name);
                cameraMsg.setDeviceType(1);
                cameraMsg.setIP(temp.packageName);
                cameraMsg.setPort(8899);
                cameraMsg.setSmartConnect(1);
                cameraMsg.setMac(temp.packageName);
                list.add(cameraMsg);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return list;
    }
    ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //强制修改应用语言
            if (!LanguageUtil.getInstance().isChineseInApp()) {
                LanguageUtil.getInstance().changeAppLanguage(activity,
                        LanguageUtil.getInstance().getAppLocale(activity));
            }
        }
        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
    public  void sendBroadCast(String resultStr){
        Intent intent = new Intent("com.zunder.smart.receiver");
        Bundle bundle = new Bundle();
        bundle.putString("str", resultStr);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    public String getWifiName() {
        return WifiName;
    }

    public void setWifiName(String wifiName) {
        WifiName = wifiName;
    }
    Uri mImage;
    public void setCaptureImage(Uri uri) {
        mImage = uri;
    }

    public Uri getCaptureImage() {
        return mImage;
    }

}