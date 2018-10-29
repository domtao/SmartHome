package com.zunder.smart.activity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.sms.CharacterParserUtil;
import com.zunder.smart.sms.CountryActivity;
import com.zunder.smart.sms.CountrySortModel;
import com.zunder.smart.sms.GetCountryNameSort;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.UserServiceUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity implements
		View.OnClickListener {

	private SharedPreferences spf;
	private Button  nextBtn, readBtn, subMit;
	private Activity context;
	private EditText userName;
	private EditText passWord;
	private EditText phone;
	private EditText mac;
	private EditText editText_countryNum;
	private TextView tv_countryName;
	ViewFlipper viewFliper;
	private RelativeLayout relative_choseCountry;
	private GetCountryNameSort countryChangeUtil;
	private CharacterParserUtil characterParserUtil;
	private List<CountrySortModel> mAllCountryList;
	TextView backTxt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.activity_register);
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		relative_choseCountry = (RelativeLayout) findViewById(R.id.rala_chose_country);
		tv_countryName = (TextView) findViewById(R.id.tv_chosed_country);
		userName = (EditText) findViewById(R.id.reName);
		phone = (EditText) findViewById(R.id.rePhone);
		passWord = (EditText) findViewById(R.id.rePassword);
		mac = (EditText) findViewById(R.id.reMac);
		editText_countryNum=(EditText)findViewById(R.id.edt_chosed_country_num);
		viewFliper = (ViewFlipper) findViewById(R.id.viewFliper);
		backTxt = (TextView) findViewById(R.id.backTxt);
		subMit = (Button) findViewById(R.id.reSubButton);
		readBtn = (Button) findViewById(R.id.readBtn);
		nextBtn = (Button) findViewById(R.id.nextBtn);
		backTxt.setOnClickListener(this);
		subMit.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		readBtn.setOnClickListener(this);
		relative_choseCountry.setOnClickListener(this);
		SMSSDK.initSDK(this, "1cbb3a1a7d0fa",
				"e3f8d2efadaa29e5b2e91cebda1c5280");
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				super.afterEvent(event, result, data);
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandle.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eventHandler);
		init();initCountryList();setListener();
	}
	public void init(){
		mAllCountryList = new ArrayList<CountrySortModel>();
		countryChangeUtil = new GetCountryNameSort();
		characterParserUtil = new CharacterParserUtil();
	}
	private void initCountryList()
	{
		String[] countryList = getResources().getStringArray(R.array.country_code_list_ch);

		for (int i = 0, length = countryList.length; i < length; i++)
		{
			String[] country = countryList[i].split("\\*");

			String countryName = country[0];
			String countryNumber = country[1];
			String countrySortKey = characterParserUtil.getSelling(countryName);
			CountrySortModel countrySortModel = new CountrySortModel(countryName, countryNumber,
					countrySortKey);
			String sortLetter = countryChangeUtil.getSortLetterBySortKey(countrySortKey);
			if (sortLetter == null)
			{
				sortLetter = countryChangeUtil.getSortLetterBySortKey(countryName);
			}

			countrySortModel.sortLetters = sortLetter;
			mAllCountryList.add(countrySortModel);
		}

	}
	String beforText = null;
	private void setListener()
	{

		editText_countryNum.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				beforText = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				String contentString = editText_countryNum.getText().toString();

				CharSequence contentSeq = editText_countryNum.getText();

//				Log.i(TAG, "contentString :" + contentString.length());

				if (contentString.length() > 1)
				{
					// 按照输入内容进行匹配
					List<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
							.search(contentString, mAllCountryList);

					if (fileterList.size() == 1)
					{
						tv_countryName.setText(fileterList.get(0).countryName);
					}
					else
					{
						tv_countryName.setText(getString(R.string.contryNo));
					}

				}
				else
				{
					if (contentString.length() == 0)
					{
						editText_countryNum.setText(beforText);
						tv_countryName.setText(getString(R.string.listselect));
					}
					else if (contentString.length() == 1 && contentString.equals("+"))
					{
						tv_countryName.setText(getString(R.string.listselect));
					}

				}

				if (contentSeq instanceof Spannable)
				{
					Spannable spannable = (Spannable) contentSeq;
					Selection.setSelection(spannable, contentSeq.length());
				}
			}
		});

	}
	Handler mHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if (result == SMSSDK.RESULT_COMPLETE) {
			}
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
				try {
					JSONObject object = new JSONObject(data.toString());
					String str = object.getString("phone");

					if (str.equals(phone.getText().toString())) {
						viewFliper.setDisplayedChild(1);
					} else {
						// {"phone":13760342685,"country":86}
						Toast.makeText(context, getString(R.string.check_err), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, getString(R.string.check_err), Toast.LENGTH_SHORT)
							.show();
				}

			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
				if (result == SMSSDK.RESULT_COMPLETE) {
					boolean smart = (Boolean) data;
					if (smart) {
						// 通过智能验证
					} else {
						// 依然走短信验证
					}
				}
				Toast.makeText(context, getString(R.string.check_send), Toast.LENGTH_SHORT).show();

			} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

			} else {
				int status = 0;
				try {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;

					JSONObject object = new JSONObject(throwable.getMessage());
					String des = object.optString("detail");
					status = object.optInt("status");
					if (!TextUtils.isEmpty(des)) {
						Toast.makeText(context, des, Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (Exception e) {
					SMSLog.getInstance().w(e);
				}
			}
		}

	};

	class DataTask extends AsyncTask<String, Void, ResultInfo> {
		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo result = null;
			try {
				result = (ResultInfo) JSONHelper.parseObject(UserServiceUtils
						.RegisterUser(phone.getText().toString(), passWord
								.getText().toString(), 1, 1, userName.getText()
								.toString()), ResultInfo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			hideProgressDialog();
			TipAlert alert;
			if (result != null) {
				switch (result.getResultCode()) {
				case 0:
					alert = new TipAlert(context, getString(R.string.tip),
							result.getMsg());
					alert.show();
					break;
				case 1:
					Editor editor = spf.edit();
					editor.putString("userName", phone.getText().toString());
					editor.putString("passWord", passWord.getText().toString());
					editor.putString("PrimaryKey", result.getMsg());
					editor.commit();
					Intent intent = new Intent(RegisterActivity.this,
							MainActivity.class);
					startActivity(intent);
					context.finish();
					break;
				default:
					break;
				}
			} else {
				alert = new TipAlert(context, getString(R.string.tip), getString(R.string.check_net));
				alert.show();
			}
		}
	}

	class CheckDataTask extends AsyncTask<String, Void, ResultInfo> {
		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo result = null;
			try {

				result = (ResultInfo) JSONHelper.parseObject(
						UserServiceUtils.isUserInfo(phone.getText().toString()
								.trim()), ResultInfo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			hideProgressDialog();
			TipAlert alert;
			if (result != null) {
				switch (result.getResultCode()) {
				case 0:
					SMSSDK.getVerificationCode(editText_countryNum.getText().toString().trim().replace("+",""), phone.getText().toString());
					break;
				case 1:
					alert = new TipAlert(context, getString(R.string.tip),getString(R.string.countyes));
					alert.show();
					break;

				default:
					break;
				}
			} else {
				alert = new TipAlert(context, getString(R.string.tip),getString(R.string.get_check_no));
				alert.show();
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			context.finish();
			break;
		case R.id.reSubButton:
			if (TextUtils.isEmpty(passWord.getText())) {
				Toast.makeText(this, getString(R.string.input_pwd),
						Toast.LENGTH_LONG).show();
			} else if (TextUtils.isEmpty(userName.getText())) {
				Toast.makeText(this, getString(R.string.input_name),
						Toast.LENGTH_LONG).show();
			} else {
				new DataTask().execute();
			}
			break;
		case R.id.nextBtn:
			if (TextUtils.isEmpty(mac.getText())) {
				Toast.makeText(context, getString(R.string.input_check), Toast.LENGTH_SHORT).show();
				return;
			}
			SMSSDK.submitVerificationCode(editText_countryNum.getText().toString().trim().replace("+",""), phone.getText().toString(), mac
					.getText().toString());

			break;
		case R.id.readBtn:

			if (TextUtils.isEmpty(phone.getText())) {
				ToastUtils.ShowError(context,getString(R.string.input_user),Toast.LENGTH_SHORT,true);
				return;
			}
				new CheckDataTask().execute();
			break;
			case R.id.rala_chose_country:
				Intent intent = new Intent();
				intent.setClass(context, CountryActivity.class);
				startActivityForResult(intent, 12);
				break;
		default:
			break;
		}
	}

	private ProgressDialog progressDialog;

	public void showProgressDialog() {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(context,
					getString(R.string.tip), getString(R.string.loading), true,
					false);
		}

		progressDialog.show();

	}

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		switch (requestCode)
		{
			case 12:
				if (resultCode == RESULT_OK)
				{
					Bundle bundle = data.getExtras();
					String countryName = bundle.getString("countryName");
					String countryNumber = bundle.getString("countryNumber");

					editText_countryNum.setText(countryNumber);
					tv_countryName.setText(countryName);

				}
				break;

			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
