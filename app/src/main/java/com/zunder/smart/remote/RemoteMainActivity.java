package com.zunder.smart.remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zunder.smart.R;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.remote.fragment.FileTypeFragment;
import com.zunder.smart.remote.fragment.TimeFragment;
import com.zunder.smart.remote.fragment.UserFragment;

public class RemoteMainActivity extends FragmentActivity implements OnClickListener {
	public static String deviceID = "";
	private FileTypeFragment fragmentFileType;
	private UserFragment fragmentUser;
	private TimeFragment fragmentTime;

	private LinearLayout userFl, fileFl, timeFl, setFl;

	private ImageView userIv, fileIv, timeIv, setIv;

	private Button home_btn;

	private static RemoteMainActivity instance;
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, RemoteMainActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_remote_main);
		init();
	}


	public void init() {
		initView();
		initData();
		initFragMent();
		clickFileBtn();
	}

	private void initView() {
		userFl = (LinearLayout) findViewById(R.id.layout_user);
		fileFl = (LinearLayout) findViewById(R.id.layout_file);
		timeFl = (LinearLayout) findViewById(R.id.layout_time);
		setFl = (LinearLayout) findViewById(R.id.layout_set);

		userIv = (ImageView) findViewById(R.id.image_user);
		fileIv = (ImageView) findViewById(R.id.image_file);
		timeIv = (ImageView) findViewById(R.id.image_time);
		setIv = (ImageView) findViewById(R.id.image_set);

		home_btn = (Button) findViewById(R.id.home_btn);
		home_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogAlert alert = new DialogAlert(instance);
				alert.init(getString(R.string.tip), "是否返回主页");
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						finish();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();

			}
		});
	}
	void initFragMent() {
		fragmentFileType = (FileTypeFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.fileTypeFragment);
		fragmentUser = (UserFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.userFragment);
		fragmentTime= (TimeFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.timeFragment);
		hide();
	}

	void hide() {
		this.getSupportFragmentManager().beginTransaction().hide(fragmentFileType)
				.commit();
		this.getSupportFragmentManager().beginTransaction().hide(fragmentUser)
				.commit();
		this.getSupportFragmentManager().beginTransaction().hide(fragmentTime)
				.commit();
	}

	private void initData() {
		userFl.setOnClickListener(this);
		fileFl.setOnClickListener(this);
		timeFl.setOnClickListener(this);
		setFl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_user:
				clickUserBtn();
				break;
			case R.id.layout_file:
				clickFileBtn();
				break;
			case R.id.layout_time:
				clickTimeBtn();
				break;
			case R.id.layout_set:
				break;
		}
	}

	private void clickUserBtn() {
		if (userIv.isSelected()) {
			return;
		}
		hide();
		this.getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right).show(fragmentUser).commit();
		userFl.setSelected(true);
		userIv.setSelected(true);
		fileFl.setSelected(false);
		fileIv.setSelected(false);
		timeFl.setSelected(false);
		timeIv.setSelected(false);
		setFl.setSelected(false);
		setIv.setSelected(false);
	}

	private void clickFileBtn() {
		if (fileIv.isSelected()) {
			return;
		}
		hide();
		this.getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right).show(fragmentFileType).commit();

		userFl.setSelected(false);
		userIv.setSelected(false);

		fileFl.setSelected(true);
		fileIv.setSelected(true);

		timeFl.setSelected(false);
		timeIv.setSelected(false);

		setFl.setSelected(false);
		setIv.setSelected(false);
	}

	private void clickTimeBtn() {
		if (timeIv.isSelected()) {
			return;
		}
		hide();
		this.getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right).show(fragmentTime).commit();
		userFl.setSelected(false);
		userIv.setSelected(false);

		fileFl.setSelected(false);
		fileIv.setSelected(false);

		timeFl.setSelected(true);
		timeIv.setSelected(true);

		setFl.setSelected(false);
		setIv.setSelected(false);
	}

	private void clickWeatherBtn() {
		if (timeIv.isSelected()) {
			return;
		}
		hide();
		userFl.setSelected(false);
		userIv.setSelected(false);

		fileFl.setSelected(false);
		fileIv.setSelected(false);
		timeFl.setSelected(true);
		timeIv.setSelected(true);
		setFl.setSelected(false);
		setIv.setSelected(false);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == event.KEYCODE_BACK) {
			// hideFragMent();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}