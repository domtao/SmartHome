package com.zunder.smart.aiui.activity;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.popwindow.CommandPopupWindow;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.forward.VoiceServiceUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceAddActivity extends Activity implements OnClickListener {

	static	VoiceInfo voiceInfo=null;
	static int edite=0;
	public static void startActivity(Activity activity,VoiceInfo _voiceInfo,int _edite) {
		voiceInfo=_voiceInfo;
		edite=_edite;
		Intent intent = new Intent(activity, VoiceAddActivity.class);
		activity.startActivityForResult(intent, 100);
	}

	private ScrollView scrollView;
	private TextView backTxt,titleTxt;
	private TextView editeTxt;

	public static VoiceAddActivity activity;
	private String io = "0";

	private SharedPreferences spf;
	private EditText voiceName, voiceAnswer, voiceAction;
	private Button audioBtn, audioQuest, audioAnswer;
	private Button commandBtn,SymbolBtn,deviceBtn,modeBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_add);
		activity = this;
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		scrollView = (ScrollView) findViewById(R.id.mainScroll);
		voiceName = (EditText) findViewById(R.id.voiceName);
		voiceAnswer = (EditText) findViewById(R.id.voiceAnswer);
		voiceAction = (EditText) findViewById(R.id.voiceAction);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		commandBtn=(Button)findViewById(R.id.commandBtn);
		SymbolBtn=(Button)findViewById(R.id.SymbolBtn);
		deviceBtn=(Button)findViewById(R.id.deviceBtn);
		modeBtn=(Button)findViewById(R.id.modeBtn);
		deviceBtn.setOnClickListener(this);
		modeBtn.setOnClickListener(this);
		commandBtn.setOnClickListener(this);
		SymbolBtn.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		audioQuest = (Button) findViewById(R.id.audioQuest);
		audioAnswer = (Button) findViewById(R.id.audioAnswer);
		audioBtn = (Button) findViewById(R.id.audioAction);
		audioQuest.setOnClickListener(this);

		audioAnswer.setOnClickListener(this);

		audioBtn.setOnClickListener(this);

		//提示 1:
		//		2: (),(),
		if(voiceInfo!=null){
			voiceName.setText(voiceInfo.getVoiceName());
			voiceAnswer.setText(voiceInfo.getVoiceAnswer());
			voiceAction.setText(voiceInfo.getVoiceAction());
			titleTxt.setText(getString(R.string.editeanswer));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			edite=0;
			voiceInfo=null;
			Intent resultIntent = new Intent();
			this.setResult(100, resultIntent);
			finish();
			break;
		case R.id.editeTxt:
			if (TextUtils.isEmpty(voiceName.getText())) {
				Toast.makeText(activity, getString(R.string.quest_null), Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(voiceAnswer.getText())) {
				Toast.makeText(activity, getString(R.string.answer_null), Toast.LENGTH_SHORT).show();
				return;
			}
			String voiceNameStr=voiceName.getText().toString();
			String voiceAnswerStr=voiceAnswer.getText().toString();
			String voiceActionStr=voiceAction.getText().toString();
			if(voiceInfo==null) {
				new VoiceAsyncTask().execute(voiceNameStr,
						voiceAnswerStr, voiceActionStr);
				voiceInfo=new VoiceInfo();
				voiceInfo.setId(0);
				voiceInfo.setVoiceName(voiceNameStr);
				voiceInfo.setVoiceAnswer(voiceAnswerStr);
				voiceInfo.setVoiceAction(voiceActionStr);
				voiceInfo.setUserName("0");
				String result = ISocketCode.setAddVoice(
						voiceInfo.convertTostring(),
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(result);
			}else{
				voiceInfo.setVoiceName(voiceNameStr);
				voiceInfo.setVoiceAnswer(voiceAnswerStr);
				voiceInfo.setVoiceAction(voiceActionStr);
				voiceInfo.setUserName("0");
				if(edite==0){
					String result = ISocketCode.setAddVoice(
							voiceInfo.convertTostring(),
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
				}else{
					new VoiceUpAsyncTask().execute(voiceInfo.getId()+"",voiceNameStr,
							voiceAnswerStr, voiceActionStr);
				}
			}
			break;
			case R.id.commandBtn:
			    //joe
				CommandPopupWindow cp=new CommandPopupWindow(activity,getString(R.string.help_all),0);
				cp.show();
				break;
			case R.id.SymbolBtn:
				CommandPopupWindow Sym=new CommandPopupWindow(activity,getString(R.string.Symbol),1);
				Sym.show();
				break;
			case R.id.deviceBtn:
			    //joe

				break;
			case R.id.modeBtn:
			    //joe
				CommandPopupWindow mode=new CommandPopupWindow(activity,getString(R.string.Symbol),2);
				mode.show();
				break;
			case R.id.audioQuest: {
				AduioService aduioService = AduioService.getInstance();
				aduioService.setEditDevice(activity, voiceName, voiceName.getText().toString());
			}
				break;
			case R.id.audioAnswer: {
				AduioService aduioService = AduioService.getInstance();
				aduioService.setEditDevice(activity, voiceAnswer, voiceAnswer.getText().toString());
			}
				break;
			case R.id.audioAction:
			{
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,voiceAction,voiceAction.getText().toString());
			}
				break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		voiceInfo=null;
		edite=0;
		activity=null;
	}

	public class VoiceAsyncTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo listUser = null;
			try {
				String json = VoiceServiceUtils.createVoice(params[0],
						params[1], params[2], spf.getString("userName", ""));
				listUser = (ResultInfo) JSONHelper.parseObject(json,
						ResultInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return listUser;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			String msg;
			if (result != null) {
				if (result.getResultCode() == 2) {
					msg=getString(R.string.resubmit);
				} else if (result.getResultCode() == 1) {
					msg=getString(R.string.answersuccess);
				} else {
					msg=getString(R.string.answerfail);
				}
			} else {
				msg=getString(R.string.net_fail);
			}
			TipAlert verifyAlert = new TipAlert(activity, getResources()
					.getString(R.string.tip), msg);
			verifyAlert.show();
		}
	}
	public class VoiceUpAsyncTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo listUser = null;
			try {
				String json = VoiceServiceUtils.updateVoice(Integer.parseInt(params[0]),
						params[1], params[2], params[3], spf.getString("userName", ""));
				listUser = (ResultInfo) JSONHelper.parseObject(json,
						ResultInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return listUser;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			String msg;
			if (result != null) {
				if (result.getResultCode() == 2) {
					msg=getString(R.string.resubmit);
				} else if (result.getResultCode() == 1) {
					msg=result.getMsg();
				} else {
					msg=result.getMsg();
				}
			} else {
				msg=getString(R.string.net_fail);
			}
			TipAlert verifyAlert = new TipAlert(activity, getResources()
					.getString(R.string.tip), msg);
			verifyAlert.show();
		}
	}
	public  void  setTxt(String title){
		//最好判斷焦點在哪個控件
		if(voiceName.hasFocus()){
			int index = voiceName.getSelectionStart();
			Editable editable = voiceName.getText();
			editable.insert(index, title);
			if(title.equals("[  ]")){
				index = voiceName.getSelectionStart();
				voiceName.setSelection(index-1);
			}
		}else if(voiceAction.hasFocus()){
		int index = voiceAction.getSelectionStart();
		Editable editable = voiceAction.getText();
		editable.insert(index, title);
		if(title.equals("[  ]")){
			index = voiceAction.getSelectionStart();
			voiceAction.setSelection(index-1);
		}
		}else if(voiceAnswer.hasFocus()){
			int index = voiceAnswer.getSelectionStart();
			Editable editable = voiceAnswer.getText();
			editable.insert(index, title);
			if(title.equals("[  ]")){
				index = voiceAnswer.getSelectionStart();
				voiceAnswer.setSelection(index-1);
			}
		}
	}
}
