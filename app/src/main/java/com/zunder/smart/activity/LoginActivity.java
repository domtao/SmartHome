package com.zunder.smart.activity;

import org.json.JSONException;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.model.User;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.UserServiceUtils;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements
		android.view.View.OnClickListener {
	private static final String TAG = "MainActivity";
	private static final String APP_ID = "1105602574";//官方获取的APPID

	EditText ed_username = null;
	EditText ed_password = null;
	Button loginBtn, registerBtn;
	private SharedPreferences spf;
	TextView forgetPwd;
	private Activity context;


	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, LoginActivity.class);
		activity.startActivityForResult(intent,100);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		context = this;
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);

		ed_username = (EditText) findViewById(R.id.userName);
		ed_password = (EditText) findViewById(R.id.passWord);
		loginBtn = (Button) findViewById(R.id.loginButton);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		forgetPwd = (TextView) findViewById(R.id.forgetPwd);
		forgetPwd.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		ed_username.setText(spf.getString("userName", ""));
		ed_password.setText(spf.getString("passWord", ""));


	}

	class DataTask extends AsyncTask<String, Void, ResultInfo> {
		@Override
		protected void onPreExecute() {

		}
		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo result = null;
			try {
				result = (ResultInfo) JSONHelper.parseObject(
						UserServiceUtils.LoginUser(params[0],params[1]), ResultInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			if (result != null) {
				switch (result.getResultCode()) {
					case 1:
						try {
							User user = (User) JSONHelper.parseObject(result
											.getMsg().replace("[", "").replace("]", ""),
									User.class);
							Editor editor = spf.edit();
							editor.putString("userName", ed_username.getText()
									.toString().trim());
							editor.putString("passWord", ed_password.getText()
									.toString().trim());
							editor.putString("PrimaryKey", user.getPrimaryKey());
							editor.commit();
							Intent intent=new Intent();
							setResult(100, intent);
							finish();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case 0:
						TipAlert alert1 = new TipAlert(context,
								getString(R.string.tip), result.getMsg());
						alert1.show();
						break;
					default:
						break;
				}
			} else {
				TipAlert alert0 = new TipAlert(context,
						getString(R.string.tip), getString(R.string.login_fail));
				alert0.show();
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Intent intent=new Intent();
			this.setResult(100, intent);
			finish();
		}
		return false;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.loginButton:
				if (TextUtils.isEmpty(ed_username.getText())) {
					Toast.makeText(this, getString(R.string.input_user),
							Toast.LENGTH_LONG).show();
				} else if (TextUtils.isEmpty(ed_password.getText())) {
					Toast.makeText(this, getString(R.string.input_pwd),
							Toast.LENGTH_LONG).show();
				} else {
					new DataTask().execute(ed_username.getText()
							.toString().trim(), ed_password.getText()
							.toString().trim());
				}
				break;
			case R.id.registerBtn:
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
//				mIUiListener = new BaseUiListener();
//				mTencent.login(context,"all", mIUiListener);

				break;
			case R.id.forgetPwd:
				Intent intent1 = new Intent(LoginActivity.this,
						ForgetPwdActivity.class);
				startActivity(intent1);
				break;
			default:
				break;
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
