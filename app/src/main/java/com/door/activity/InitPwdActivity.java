package com.door.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.gateway.GateWayAddFragment;
import com.p2p.core.P2PHandler;

public class InitPwdActivity extends Activity implements View.OnClickListener {
	private Button btn_save;
	private EditText et_pwd;
	private TextView tv_log, backTxt;
	private String ipFlag;
	private int deviceID=0;
	private String input_create_pwd;
	public static String ACK_RET_SET_INIT_PASSWORD = "ACK_RET_SET_INIT_PASSWORD";
	public static String RET_SET_INIT_PASSWORD = "RET_SET_INIT_PASSWORD";
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_door_password);
		activity=this;
		tv_log = (TextView) findViewById(R.id.tv_log);
		backTxt = (TextView) findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		tv_log.setText(getString(R.string.line_pwd)+"\n");
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		btn_save = (Button) findViewById(R.id.btn_save);

		btn_save.setOnClickListener(this);
		ipFlag = getIntent().getExtras().getString("ipFlag");
		deviceID = getIntent().getExtras().getInt("deviceID");

		registerFilter();
	}

	private void registerFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACK_RET_SET_INIT_PASSWORD);
		filter.addAction(RET_SET_INIT_PASSWORD);
		registerReceiver(mReceiver, filter);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACK_RET_SET_INIT_PASSWORD)) {
				tv_log.append(getString(R.string.ack)+"\n");
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					tv_log.append(getString(R.string.success)+"\n");
				} else {
					tv_log.append(getString(R.string.othererr)+"\n");
				}
			} else if (intent.getAction().equals(RET_SET_INIT_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					tv_log.append(getString(R.string.initpwd)+"\n");
					Intent intent1 = new Intent(activity, GateWayAddFragment.class);
					intent1.putExtra("edit", "Edite1");
					intent1.putExtra("Id", 0);
					intent1.putExtra("gateWayName", "门铃");
					intent1.putExtra("gatewayID", deviceID+"");
					intent1.putExtra("userName", "admin");
					intent1.putExtra("userPassWprd",et_pwd.getText().toString());
					intent1.putExtra("gatewayTypeID", 3);
					intent1.putExtra("seqencing", 0);
					intent1.putExtra("IsCurrent", 0);
					activity.startActivity(intent1);
					finish();
				} else if (result == 43) {
					tv_log.append(getString(R.string.pwdexise)+"\n");
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			if(TextUtils.isEmpty(et_pwd.getText())){
				ToastUtils.ShowError(activity,getString(R.string.input_pwd),Toast.LENGTH_SHORT,true);
				return;
			}
			input_create_pwd = et_pwd.getText().toString();
			String input_create_pwd_new = P2PHandler.getInstance()
					.EntryPassword(input_create_pwd);
			P2PHandler.getInstance().setInitPassword(ipFlag,
					input_create_pwd_new, input_create_pwd, input_create_pwd);
			break;
		case R.id.backTxt:
			finish();
		default:
			break;
		}
	}

}
