package com.bluecam.api;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bluecam.api.adapter.SettingCommonAdapter;
import com.bluecam.api.utils.Contants;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_TimeActivity extends BaseActivity implements View.OnClickListener{


    private TextView cur_time_tv;
    private TextView time_ntp_tv;
    private TextView time_zone_tv;
    private View timezone_view;
    private View ntp_view;
    private CheckBox autu_cb;
    private Button local_btn;

    private Button done_btn;

    private int[] timeZ = {39600,36000,32400,28800,25200,21600,18000,14400,12600,10800,7200,3600,0,
            -3600,-7200,-10800,-12600,-14400,-16200,-18000,-19800,-21600,-25200,-28800,
            -32400,34200,-36000,-39600,-43200};

    private String[] zoneStr;
    private String[] ntpStr;
    private String camID;

    private BCamera camera;
    private JSONObject paramObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time);
        zoneStr = this.getResources().getStringArray(R.array.zone_lable);
        ntpStr = this.getResources().getStringArray(R.array.ntp_lable);

        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_DATETIME_PARAM_KEY);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraManager != null){
            cameraManager.unRegisterEventListener(this);
        }
    }

    private void initView() {
        initHeaderBar();
        setTitle_txt("时间设置");


        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
        cur_time_tv = (TextView)findViewById(R.id.cur_time_tv);
        time_ntp_tv = (TextView)findViewById(R.id.time_ntp_tv);
        time_zone_tv = (TextView)findViewById(R.id.time_zone_tv);

        timezone_view = findViewById(R.id.timezone_view);
        ntp_view      = findViewById(R.id.ntp_view);

        autu_cb = (CheckBox)findViewById(R.id.autu_cb);

        local_btn = (Button)findViewById(R.id.local_btn);

        timezone_view.setOnClickListener(this);
        ntp_view.setOnClickListener(this);
        autu_cb.setOnClickListener(this);
        local_btn.setOnClickListener(this);
    }

    private void done() {
        try {
            setTime();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_DATETIME_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            paramObj       = new JSONObject(params);
                            int now        = paramObj.getInt("now");
                            int timezone   = paramObj.getInt("timezone");
                            int ntp_enable = paramObj.getInt("ntp_enable");
                            String ntp_svr = paramObj.getString("ntp_svr");
                            time_ntp_tv.setText(ntp_svr);
                            if (ntp_enable == 1) {
                                autu_cb.setChecked(true);
                                ntp_view.setVisibility(View.VISIBLE);
                            } else {
                                autu_cb.setChecked(false);
                                ntp_view.setVisibility(View.GONE);
                            }
                            setTimeZone(now,timezone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    @Override
    public void onSetParamsEvent(long camHandler, long paramKey, final int nResult) {
        if(camera == null){
            return;
        }
        System.out.println("nResult == "+nResult);
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.SET_DATETIME_PARAM_KEY){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nResult == 1){
                            showToast("设置成功");
                        }
                        else{
                            showToast("设置失败");
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //时区选择
        if(id == R.id.timezone_view){
            showTimeZoneDialog();
        }
        //NTP服务器
        else if(id == R.id.ntp_view){
            showNtpDialog();
        }
        //自动校时
        else if(id == R.id.autu_cb){
            int ntp_enable = 0;
            if(autu_cb.isChecked()){
                ntp_view.setVisibility(View.VISIBLE);
                ntp_enable = 1;
            }
            else{
                ntp_view.setVisibility(View.GONE);
                ntp_enable = 0;
            }
            if(paramObj != null){
                try {
                    paramObj.put("ntp_enable",ntp_enable);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //本地时间校准
        else if(id == R.id.local_btn){
            try {
                localTimeCheck();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setTime() throws JSONException {
        if(paramObj == null){
            return;
        }
        paramObj.put("now", 0);
        camera.setCameraParam(CameraContants.ParamKey.SET_DATETIME_PARAM_KEY,paramObj.toString());
    }

    private void localTimeCheck() throws JSONException {
        if(paramObj == null){
            return;
        }
        TimeZone timeZone = TimeZone.getDefault();
        int tz = -timeZone.getRawOffset() / 1000;
        Calendar calendar = Calendar.getInstance();
        int now = (int) (calendar.getTimeInMillis() / 1000);
        paramObj.put("now",now);
        paramObj.put("timezone",tz);
        camera.setCameraParam(CameraContants.ParamKey.SET_DATETIME_PARAM_KEY,paramObj.toString());
    }
    private void showTimeZoneDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,zoneStr);
        param_list_view.setAdapter(adapter);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(done_btn, Gravity.BOTTOM,0, 0);
        cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
            }
        });
        param_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int motion_sensitivity = position+1;
                if(paramObj != null)
                {
                    try {
                        paramObj.put("timezone",timeZ[position]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                time_zone_tv.setText((String)parent.getItemAtPosition(position));

                editWindow.dismiss();
            }
        });
    }
    private void showNtpDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,ntpStr);
        param_list_view.setAdapter(adapter);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(done_btn, Gravity.BOTTOM,0, 0);
        cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
            }
        });
        param_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ntp = (String)parent.getItemAtPosition(position);
                if(paramObj != null)
                {
                    try {
                        paramObj.put("ntp_svr",ntp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                time_ntp_tv.setText(ntp);

                editWindow.dismiss();
            }
        });

    }

    private void setTimeZone(int now,int timezone){
        Long lon = new Long(now);
        String time_cur = "";
        switch (timezone) {

            case 39600:
                time_cur=setDeviceTime(lon * 1000, "GMT-11:00");
                time_zone_tv.setText(R.string.date_middle_island);
                break;
            case 36000:
                time_cur=setDeviceTime(lon * 1000, "GMT-10:00");
                time_zone_tv.setText(R.string.date_hawaii);
                break;
            case 32400:
                time_cur= setDeviceTime(lon * 1000, "GMT-09:00");
                time_zone_tv.setText(R.string.date_alaska);
                break;
            case 28800:
                time_cur=setDeviceTime(lon * 1000, "GMT-08:00");
                time_zone_tv.setText(R.string.date_pacific_time);
                break;
            case 25200:
                time_cur=setDeviceTime(lon * 1000, "GMT-07:00");
                time_zone_tv.setText(R.string.date_mountain_time);
                break;
            case 21600:
                time_cur=setDeviceTime(lon * 1000, "GMT-06:00");
                time_zone_tv.setText(R.string.date_middle_part_time);
                break;
            case 18000:
                time_cur=setDeviceTime(lon * 1000, "GMT-05:00");
                time_zone_tv.setText(R.string.date_eastern_time);
                break;
            case 14400:
                time_cur=setDeviceTime(lon * 1000, "GMT-04:00");
                time_zone_tv.setText(R.string.date_ocean_time);
                break;
            case 12600:
                time_cur=setDeviceTime(lon * 1000, "GMT-03:30");
                time_zone_tv.setText(R.string.date_newfoundland);
                break;
            case 10800:
                time_cur=setDeviceTime(lon * 1000, "GMT-03:00");
                time_zone_tv.setText(R.string.date_brasilia);
                break;
            case 7200:
                time_cur=setDeviceTime(lon * 1000, "GMT-02:00");
                time_zone_tv.setText(R.string.date_center_ocean);
                break;
            case 3600:
                time_cur=setDeviceTime(lon * 1000, "GMT-01:00");
                time_zone_tv.setText(R.string.date_cape_verde_island);
                break;
            case 0:
                time_cur=setDeviceTime(lon * 1000, "GMT");
                time_zone_tv.setText(R.string.date_greenwich);
                break;
            case -3600:
                time_cur=setDeviceTime(lon * 1000, "GMT+01:00");
                time_zone_tv.setText(R.string.date_brussels);
                break;
            case -7200:
                time_cur=setDeviceTime(lon * 1000, "GMT+02:00");
                time_zone_tv.setText(R.string.date_athens);
                break;
            case -10800:
                time_cur=setDeviceTime(lon * 1000, "GMT+03:00");
                time_zone_tv.setText(R.string.date_nairobi);
                break;
            case -12600:
                time_cur=setDeviceTime(lon * 1000, "GMT+03:30");
                time_zone_tv.setText(R.string.date_teheran);
                break;
            case -14400:
                time_cur=setDeviceTime(lon * 1000, "GMT+04:00");
                time_zone_tv.setText(R.string.date_baku);
                break;
            case -16200:
                time_cur=setDeviceTime(lon * 1000, "GMT+04:30");
                time_zone_tv.setText(R.string.date_kebuer);
                break;
            case -18000:
                time_cur=setDeviceTime(lon * 1000, "GMT+05:00");
                time_zone_tv.setText(R.string.date_islamabad);
                break;
            case -19800:
                time_cur=setDeviceTime(lon * 1000, "GMT+05:30");
                time_zone_tv.setText(R.string.date_calcutta);
                break;

            case -21600:
                time_cur=setDeviceTime(lon * 1000, "GMT+06:00");
                time_zone_tv.setText(R.string.date_alamotu);
                break;
            case -25200:
                time_cur=setDeviceTime(lon * 1000, "GMT+07:00");
                time_zone_tv.setText(R.string.date_bangkok);
                break;
            case -28800:
                time_cur=setDeviceTime(lon * 1000, "GMT+08:00");
                time_zone_tv.setText(R.string.date_beijing);
                break;
            case -32400:
                time_cur=setDeviceTime(lon * 1000, "GMT+09:00");
                time_zone_tv.setText(R.string.date_seoul);
                break;
            case -34200:
                time_cur=setDeviceTime(lon * 1000, "GMT+09:30");
                time_zone_tv.setText(R.string.date_darwin);
                break;
            case -36000:
                time_cur=setDeviceTime(lon * 1000, "GMT+10:00");
                time_zone_tv.setText(R.string.date_guam);
                break;
            case -39600:
                time_cur=setDeviceTime(lon * 1000, "GMT+11:00");
                time_zone_tv.setText(R.string.date_suolumen);
                break;
            case -43200:
                time_cur=setDeviceTime(lon * 1000, "GMT+12:00");
                time_zone_tv.setText(R.string.date_auckland);
                break;
            default:
                break;
        }
        cur_time_tv.setText(time_cur);
    }
    private String setDeviceTime(long millisutc, String tz){
        TimeZone timeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(timeZone);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year+"-"+ Contants.formatNum(month)+"-"+Contants.formatNum(day)+" "+Contants.formatNum(hour)
                +":"+Contants.formatNum(minute)+":"+Contants.formatNum(second);
    }
}
