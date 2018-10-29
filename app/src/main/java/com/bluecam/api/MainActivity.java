package com.bluecam.api;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bluecam.api.adapter.CameraListAdapter;
import com.bluecam.api.utils.ImageLoader;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements CameraListAdapter.OnItemClickListener {
    private ImageView add_iv;
    private RecyclerView device_list_view;
    private List<BCamera> listData = new ArrayList<BCamera>();
    private CameraListAdapter deviceListAdapter;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsl_main);
        CameraPlayActivity.start=1;
        initView();
        imageLoader = ImageLoader.getInstance();

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                cameraManager.initialize();
                return null;
            }
        }.execute();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cameraManager != null){
            cameraManager.unRegisterEventListener(this);
        }
    }
    private void initView(){
        add_iv = (ImageView)findViewById(R.id.add_iv);
        device_list_view = (RecyclerView)findViewById(R.id.device_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        device_list_view.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        deviceListAdapter = new CameraListAdapter(this,listData,this);
        device_list_view.setAdapter(deviceListAdapter);

        add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddView();
            }
        });
    }

    /**
     * 局域网搜索和无线配置选项
     */
    private void showAddView(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.camera_add_fun_window, null);

        final PopupWindow editWindow = new PopupWindow(view,450, LinearLayout.LayoutParams.WRAP_CONTENT);
        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        //editWindow.showAtLocation(add_iv, Gravity.BOTTOM,0, 100);
        editWindow.showAsDropDown(add_iv);
        TextView search_tv       = (TextView)view.findViewById(R.id.search_tv);
        TextView smart_config_tv = (TextView)view.findViewById(R.id.smart_config_tv);
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        smart_config_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
                Intent intent = new Intent(MainActivity.this,VoiceConfigActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCameraStatusChangeEvent(final long camHandler, final int status) {
        //设备状态更新UI
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(BCamera camera:listData)
                {
                    if(camera.getCamHandler() == camHandler)
                    {
                        camera.setStatus(status);
                        int position = listData.indexOf(camera);
                        deviceListAdapter.notifyItemChanged(position);
                        //deviceListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

    }

    @Override
    public void onCameraSnapShotEvent(long camHandler, byte[] imgBuf, int len) {
        //更新抓拍图像
        BCamera camera = cameraManager.getCamera(camHandler);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBuf, 0, len);
        imageLoader.putImage(camera.getCameraBean().getDevID(),bitmap);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCameraAddEvent(final BCamera camera) {
        //添加了设备 通知更新UI
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listData.add(camera);
                deviceListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(BCamera camera) {
        //点击查看视频
        if(camera.getStatus() == CameraContants.DeviceStatus.DEVICE_STATUS_ON_LINE){
            Intent intent = new Intent(this,CameraPlayActivity.class);
            intent.putExtra("camID",camera.getCameraBean().getDevID());
            startActivity(intent);
        }
        else{
            camera.connectCamera();
        }

    }

    @Override
    public void onRecordClick(BCamera camera) {
        //点击查看录像
        if(camera.getStatus() == CameraContants.DeviceStatus.DEVICE_STATUS_ON_LINE){
            Intent intent = new Intent(this,RecordListActivity.class);
            intent.putExtra("camID",camera.getCameraBean().getDevID());
            startActivity(intent);
        }
        else{
            camera.connectCamera();
        }
    }

    @Override
    public void onSettingClick(BCamera camera) {
        //点击进入设置页面
        if(camera.getStatus() == CameraContants.DeviceStatus.DEVICE_STATUS_ON_LINE){
            Intent intent = new Intent(this,SettingActivity.class);
            intent.putExtra("camID",camera.getCameraBean().getDevID());
            startActivity(intent);
        }
        else{
            camera.connectCamera();
        }

    }
}
