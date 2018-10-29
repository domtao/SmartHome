package com.bluecam.api;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bluecam.api.adapter.CameraWifiListAdapter;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_WiFiActivity extends BaseActivity implements CameraWifiListAdapter.OnWifiItemClickListener{
    private final int NO = 0;
    private final int WEP = 1;
    private final int WPA_PSK_AES = 2;
    private final int WPA_PSK_TKIP = 3;
    private final int WPA2_PSK_AES = 4;
    private final int WPA2_PSK_TKIP = 5;

    private TextView cam_wifi_name_txt;
    private TextView cam_wifi_safe_txt;
    private TextView cam_wifi_channel_txt;

    private RecyclerView setting_wifi_list_view;
    private CameraWifiListAdapter wifiListAdapter;
    private List<JSONObject> wifiList = new ArrayList<JSONObject>();
    private BCamera camera;
    private JSONObject paramObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_wifi);
        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_WIFI_PARAM_KEY);
        camera.getCameraParam(CameraContants.ParamKey.GET_WIFI_LIST_PARAM_KEY);

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
        setTitle_txt("WIFI设置");
        cam_wifi_name_txt    = (TextView)findViewById(R.id.cam_wifi_name_txt);
        cam_wifi_safe_txt    = (TextView)findViewById(R.id.cam_wifi_safe_txt);
        cam_wifi_channel_txt = (TextView)findViewById(R.id.cam_wifi_channel_txt);
        setting_wifi_list_view = (RecyclerView)findViewById(R.id.setting_wifi_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        setting_wifi_list_view.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        wifiListAdapter = new CameraWifiListAdapter(this,wifiList,this);
        setting_wifi_list_view.setAdapter(wifiListAdapter);
    }



    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_WIFI_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            paramObj = new JSONObject(params);

                            cam_wifi_name_txt.setText(paramObj.getString("ssid"));
                            cam_wifi_channel_txt.setText(paramObj.getInt("channel")+"");
                            int authtype =  paramObj.getInt("authtype");
                            switch (authtype)
                            {
                                case NO:
                                    cam_wifi_safe_txt.setText("无");
                                    break;
                                case WEP:
                                    cam_wifi_safe_txt.setText("WEP");
                                    break;
                                case WPA_PSK_AES:
                                    cam_wifi_safe_txt.setText("WPA_PSK(AES)");
                                    break;
                                case WPA_PSK_TKIP:
                                    cam_wifi_safe_txt.setText("WPA_PSK(TKIP)");
                                    break;
                                case WPA2_PSK_AES:
                                    cam_wifi_safe_txt.setText("WPA2_PSK(AES)");
                                    break;
                                case WPA2_PSK_TKIP:
                                    cam_wifi_safe_txt.setText("WPA2_PSK(TKIP)");
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(paramKey == CameraContants.ParamKey.GET_WIFI_LIST_PARAM_KEY)
            {
                //onShowWifiList(param);
                try
                {
                    JSONArray arr = new JSONArray(params);
                    for (int i = 0; i < arr.length(); i++)
                    {
                        wifiList.add(arr.getJSONObject(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wifiListAdapter.notifyDataSetChanged();
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
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.SET_USERPWD_PARAM_KEY){
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
    public void onItemClick(JSONObject obj) {
        showInputPwdDialog(obj);
    }
    private void showInputPwdDialog(final JSONObject obj){

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_wifi_pwd_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        TextView done_item = (TextView)view.findViewById(R.id.done_item);
        final EditText wifi_pwd_et = (EditText)view.findViewById(R.id.wifi_pwd_et);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(setting_wifi_list_view, Gravity.BOTTOM,0, 0);

        cancel_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(editWindow.isShowing())
                    editWindow.dismiss();
            }
        });


        done_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String wifiPwd = wifi_pwd_et.getText().toString();
                try
                {
                    int security = obj.getInt("security");
                    int channel = obj.getInt("channel");
                    if(security == NO)
                    {
                        paramObj.put("key1", "");
                        paramObj.put("wpa_psk", "");
                    }
                    else
                    {
                        if(TextUtils.isEmpty(wifiPwd))
                        {
                            showToast("密码不能为空");
                            return;
                        }
                        else
                        {
                            try {
                                wifiPwd = URLEncoder.encode(wifiPwd, "UTF-8");
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                            if (security == WEP) {
                                paramObj.put("key1", wifiPwd);
                            } else {
                                paramObj.put("wpa_psk", wifiPwd);
                            }
                        }
                    }
                    paramObj.put("ssid",obj.getString("ssid"));
                    paramObj.put("channel", channel);
                    paramObj.put("authtype", security);
                    camera.setCameraParam(CameraContants.ParamKey.SET_WIFI_PARAM_KEY,paramObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(editWindow.isShowing())
                    editWindow.dismiss();

            }
        });
    }
}
