package com.bluecam.api;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bluecam.api.adapter.CameraSearchListAdapter;
import com.bluecam.api.bean.SearchBean;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.Camera;
import com.bluecam.bluecamlib.CameraBean;
import com.bluecam.bluecamlib.CameraManager;
import com.zunder.smart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class SearchActivity extends BaseActivity implements CameraSearchListAdapter.AddClickListener {
    private RecyclerView search_list_view;
    private CameraSearchListAdapter searchListAdapter;
    private List<SearchBean> cameraList = new ArrayList<SearchBean>();
    private CameraManager cameraManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        cameraManager = CameraManager.getDeviceManager(this.getApplicationContext());
        cameraManager.registerEventListener(this);
        cameraManager.initSearchCamera();
        cameraManager.searchCamera();
        //showProgressDialog("正在搜索...");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraManager.exitSearchCamera();
    }

    private void initView(){
        initHeaderBar();
        setTitle_txt("搜索设备");
        search_list_view    = (RecyclerView)findViewById(R.id.search_list_view);
        searchListAdapter  = new CameraSearchListAdapter(this,cameraList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        search_list_view.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        search_list_view.setAdapter(searchListAdapter);
    }

    @Override
    public void onAddClick(BCamera camera) {
        showInputPwdDialog(camera);

    }

    private void showInputPwdDialog(final BCamera camera){

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_wifi_pwd_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        TextView done_item = (TextView)view.findViewById(R.id.done_item);
        final EditText pwd_et = (EditText)view.findViewById(R.id.wifi_pwd_et);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(search_list_view, Gravity.BOTTOM,0, 0);

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
                String pwd = pwd_et.getText().toString();
                camera.getCameraBean().setPassword(pwd);
                cameraManager.addCamera(camera);
                int len = cameraList.size();
                for(int i=0;i<len;i++){
                    SearchBean bean = cameraList.get(i);
                    if(bean.getCamera().getCamHandler() == camera.getCamHandler()){
                        cameraList.remove(i);
                        break;
                    }
                }
                searchListAdapter.notifyDataSetChanged();
                showToast("添加成功");
                if(editWindow.isShowing())
                    editWindow.dismiss();

            }
        });
    }
    //搜索返回结果
    @Override
    public void onSerchEvent(final CameraBean cameraBean) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BCamera camera  = new Camera();
                camera.setCameraBean(cameraBean);
                SearchBean searchBean = new SearchBean();
                searchBean.setCamera(camera);
                for(SearchBean cam:cameraList)
                {
                    if(cam.getCamera().getCameraBean().getDevID().equals(searchBean.getCamera().getCameraBean().getDevID()))
                    {
                        return;
                    }
                }
                cameraList.add(searchBean);
                searchListAdapter.notifyDataSetChanged();
                hideProgressDialog();
            }
        });
    }
}
