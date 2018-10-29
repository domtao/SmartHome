package com.zunder.smart.onekey;

import com.bluecam.api.MainActivity;
import com.bluecam.api.VoiceConfigActivity;
import com.door.Utils.ToastUtils;
import com.door.activity.SendWifiActivity;
import com.zunder.smart.R;
import com.zunder.smart.rak47.MainTabActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zunder.smart.rak47.MainTabActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import rx.functions.Action1;

public class ConfigActivity extends Activity {
	private LinearLayout cameraLayout;
	private LinearLayout cloudLayout;
	private LinearLayout gateWayLayout;
	private LinearLayout doorLayout;
	private LinearLayout bualLayout;
	TextView backView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_device);

		backView = (TextView) findViewById(R.id.backTxt);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cameraLayout = (LinearLayout) findViewById(R.id.device_camera);
		cameraLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent();
				// intent.setClass(ConfigActivity.this,
				// SimpleConfigActivity.class);
				// startActivity(intent);
			}
		});

		cloudLayout = (LinearLayout) findViewById(R.id.device_cloud);
		cloudLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ConfigActivity.this, SettingWifiActivity.class);
				startActivity(intent);
				finish();
			}
		});
		gateWayLayout = (LinearLayout) findViewById(R.id.device_gateway);
		gateWayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ConfigActivity.this, MainTabActivity.class);
				startActivity(intent);
				finish();
			}
		});
		doorLayout = (LinearLayout) findViewById(R.id.device_door);
		doorLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ConfigActivity.this, SendWifiActivity.class);
				startActivity(intent);
				finish();
			}
		});
		bualLayout = (LinearLayout) findViewById(R.id.device_bual);
		bualLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConfigActivity.this,VoiceConfigActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
