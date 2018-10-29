package com.bluecam.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraBean;
import com.bluecam.bluecamlib.CameraManager;
import com.bluecam.bluecamlib.listener.CameraEventListener;
import com.zunder.smart.R;

/**
 * Created by Administrator on 2017/7/26.
 */

public class BaseActivity extends Activity implements CameraEventListener
{
    private TextView back_iv;
    private TextView title_txt;
    protected ProgressDialog progressDialog;
    //设备管理器
    protected CameraManager cameraManager ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraManager = CameraManager.getDeviceManager(this.getApplicationContext());
        cameraManager.registerEventListener(this);
    }

    protected void initHeaderBar(){
        back_iv   = (TextView)findViewById(R.id.back_iv);
        title_txt = (TextView)findViewById(R.id.title_txt);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
    protected void back(){
        finish();
    }
    protected void setTitle_txt(String title){
        if(title_txt != null){
            title_txt.setText(title);
        }
    }
    protected void virbate(){
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    protected void autoSetAudioVolumn()
    {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(max*0.6), 0);
    }

    protected void showProgressDialog(String msg)
    {
        progressDialog = null;
        progressDialog = ProgressDialog.show(this, "", msg, true, false);
    }
    protected void hideProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    protected void showToast(String msg)
    {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
    protected void doDialogOK(){

    }
    protected void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                doDialogOK();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, String params) {

    }

    @Override
    public void onSetParamsEvent(long camHandler, long paramKey, int nResult) {

    }

    @Override
    public void onCameraStatusChangeEvent(long camHandler, int status) {

    }

    @Override
    public void onCameraSnapShotEvent(long camHandler, byte[] imgBuf, int len) {

    }

    @Override
    public void onSerchEvent(CameraBean cameraBean) {

    }

    @Override
    public void onCameraAddEvent(BCamera camera) {

    }

    @Override
    public void onAudioDataEvent(long camHandler, byte[] pcm, int size) {

    }

    @Override
    public void onAlarmEvent(long camHandler, int nType) {

    }

    @Override
    public void onRecordFileList(long camHandler, int filecount, String fileName, String strDate, int size) {

    }

    @Override
    public void onRecordPlayPos(long camHandler, int pos) {

    }
}
