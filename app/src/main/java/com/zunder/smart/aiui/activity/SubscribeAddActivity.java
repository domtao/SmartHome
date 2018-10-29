package com.zunder.smart.aiui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.popwindow.CommandPopupWindow;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.forward.SubscribeServiceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubscribeAddActivity extends Activity implements OnClickListener {

	static SubscribeInfo voiceInfo=null;
	static int edite=0;
	int timerValue = 0;
	public static void startActivity(Activity activity,SubscribeInfo _voiceInfo,int _edite) {
		voiceInfo=_voiceInfo;
		edite=_edite;
		Intent intent = new Intent(activity, SubscribeAddActivity.class);
		activity.startActivityForResult(intent, 100);
	}
    int mYear, mMonth, mDay,hourOfDay,minute;

    final int DATE_DIALOG = 1;
	private ScrollView scrollView;
	private TextView backTxt,titleTxt;
	private TextView editeTxt;

	public static SubscribeAddActivity activity;
	private String io = "0";

	private SharedPreferences spf;
	private EditText subscribeName, subscribeEvent, subscribeAction;
	Button subscribeDate,subscribeTime;
	private Button audioName, audioEvent, audioAction;
	private Button commandBtn,SymbolBtn,deviceBtn,modeBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe_add);
		activity = this;
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		scrollView = (ScrollView) findViewById(R.id.mainScroll);
		subscribeName = (EditText) findViewById(R.id.subscribeName);
		subscribeDate = (Button) findViewById(R.id.subscribeDate);
		subscribeTime = (Button) findViewById(R.id.subscribeTime);
		subscribeEvent = (EditText) findViewById(R.id.subscribeEvent);
		subscribeAction = (EditText) findViewById(R.id.subscribeAction);
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

		audioName = (Button) findViewById(R.id.audioName);
		subscribeDate.setOnClickListener(this);
		subscribeTime.setOnClickListener(this);
		audioEvent = (Button) findViewById(R.id.audioEvent);
		audioAction = (Button) findViewById(R.id.audioAction);
		audioName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subscribeName.requestFocus();
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,subscribeName,subscribeName.getText().toString());
			}
		});
		audioEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subscribeEvent.requestFocus();
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,subscribeEvent,subscribeEvent.getText().toString());
			}
		});

		audioAction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subscribeAction.requestFocus();
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,subscribeAction,subscribeAction.getText().toString());
			}
		});

		if(voiceInfo!=null){
			subscribeName.setText(voiceInfo.getSubscribeName());
			subscribeEvent.setText(voiceInfo.getSubscribeEvent());
			subscribeAction.setText(voiceInfo.getSubscribeAction());

			String date=voiceInfo.getSubscribeDate();
			if(AppTools.isNumeric(date)){
				subscribeDate.setText(AppTools.getWeeks(Integer.parseInt(date)));
				timerValue=Integer.parseInt(date);
			}else {
				subscribeDate.setText(date);
			}
			//subscribeDate.setText(voiceInfo.getSubscribeDate());

			subscribeTime.setText(voiceInfo.getSubscribeTime());
			titleTxt.setText(getString(R.string.editetrip));
		}
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
		hourOfDay = ca.get(Calendar.HOUR_OF_DAY);
		minute = ca.get(Calendar.MINUTE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.subscribeDate:
//				showDialog(DATE_DIALOG);
				AskTimeSch();
				break;

			case R.id.subscribeTime:
				if(TextUtils.isEmpty(subscribeDate.getText())){
					ToastUtils.ShowError(activity,getString(R.string.seletedate),Toast.LENGTH_SHORT,true);
					return;
				}
				new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						String time=(hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute);
						if(subscribeDate.getText().toString().equals(AppTools.getDate())){
							String currenTime=AppTools.getTime();
							if(time.compareTo(currenTime)>0){
								subscribeTime.setText(time);
							}else{
								ToastUtils.ShowError(activity,getString(R.string.timecopare),Toast.LENGTH_SHORT,true);
							}
						}else {
							subscribeTime.setText(time);
						}
					}
				},hourOfDay,minute,true).show();
				break;
		case R.id.backTxt:
			edite=0;
			voiceInfo=null;
			Intent resultIntent = new Intent();
			this.setResult(100, resultIntent);
			finish();
			break;
		case R.id.editeTxt:
			if (TextUtils.isEmpty(subscribeName.getText())) {
				Toast.makeText(activity, getString(R.string.words_null), Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(subscribeDate.getText())||timerValue==0) {
				Toast.makeText(activity, getString(R.string.date_null), Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(subscribeTime.getText())) {
				Toast.makeText(activity, getString(R.string.time_null), Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(subscribeEvent.getText())) {
				Toast.makeText(activity, getString(R.string.trip_null), Toast.LENGTH_SHORT).show();
				return;
			}
			String nameStr=subscribeName.getText().toString();
			String dateStr=timerValue+"";
			if(timerValue==128) {
				dateStr = subscribeDate.getText().toString();
			}
			String timeStr=subscribeTime.getText().toString();
			String eventzStr=subscribeEvent.getText().toString();
			String actionStr=subscribeAction.getText().toString();
			if(voiceInfo==null) {
				new VoiceAsyncTask().execute(nameStr,
						dateStr, timeStr,eventzStr,actionStr);
				voiceInfo=new SubscribeInfo();
				voiceInfo.setId(0);
				voiceInfo.setSubscribeName(nameStr);
				voiceInfo.setSubscribeDate(dateStr);
				voiceInfo.setSubscribeTime(timeStr);
				voiceInfo.setSubscribeEvent(eventzStr);
				voiceInfo.setSubscribeAction(actionStr);
				String result = ISocketCode.setAddSubscribe(
						voiceInfo.convertTostring(),
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(result);
			}else{
				voiceInfo.setSubscribeName(nameStr);
				voiceInfo.setSubscribeDate(dateStr);
				voiceInfo.setSubscribeTime(timeStr);
				voiceInfo.setSubscribeEvent(eventzStr);
				voiceInfo.setSubscribeAction(actionStr);
				if(edite==0){
					String result = ISocketCode.setAddSubscribe(
							voiceInfo.convertTostring(),
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
				}else{
					new VoiceUpAsyncTask().execute(voiceInfo.getId()+"",nameStr,
							dateStr, timeStr,eventzStr,actionStr);
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
				//
				CommandPopupWindow mode=new CommandPopupWindow(activity,getString(R.string.Symbol),2);
				break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		voiceInfo=null;
		activity=null;
		edite=0;
	}

	public class VoiceAsyncTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {

		}
		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo listUser = null;
			try {
				String json = SubscribeServiceUtils.createSubscribe(params[0],
						params[1], params[2],params[3],params[4], spf.getString("userName", ""));
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
			if (result != null) {

				if (result.getResultCode() == 2) {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip), getString(R.string.resubmit));
					verifyAlert.show();
				} else if (result.getResultCode() == 1) {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip), getString(R.string.booksuccess));
					verifyAlert.show();
				} else {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip), getString(R.string.bookfail));
					verifyAlert.show();
				}
			} else {
				TipAlert verifyAlert = new TipAlert(activity, getResources()
						.getString(R.string.tip), getString(R.string.net_fail));
				verifyAlert.show();
			}
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
				String json = SubscribeServiceUtils.updateSubscribe(Integer.parseInt(params[0]),
						params[1], params[2], params[3],params[4],params[5], spf.getString("userName", ""));
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
			if (result != null) {

				if (result.getResultCode() == 2) {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip), getString(R.string.resubmit));

					verifyAlert.show();
				} else if (result.getResultCode() == 1) {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip),result.getMsg());

					verifyAlert.show();
				} else {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip), result.getMsg());

					verifyAlert.show();
				}
			} else {
				TipAlert verifyAlert = new TipAlert(activity, getResources()
						.getString(R.string.tip), getString(R.string.net_fail));
				verifyAlert.show();

			}
		}
	}

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }
    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display( int year, int monthOfYear,
						 int dayOfMonth) {
		try {
			String date = year + "-" + (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
			String current = AppTools.getDate();
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
			long between = (dfs.parse(date).getTime() - dfs.parse(current).getTime()) / 1000;
			long day1 = between / (24 * 3600);
			if(day1<0){
				ToastUtils.ShowError(activity,getString(R.string.datecopare),Toast.LENGTH_SHORT,true);
			}else {
				subscribeDate.setText(date);
			}
		}catch(Exception e){

		}
    }
    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
			display(year, monthOfYear+1,dayOfMonth);
        }
    };

	private void AskTimeSch() {
		final String items[] = (String[]) activity.getResources()
				.getStringArray(R.array.subWeekens);
		final boolean[] selected = new boolean[items.length];

		for (int i = 0; i < selected.length; i++) {
			if((timerValue==0||timerValue==128)&&i==0) {
				selected[i] = false;
			}else if(timerValue==127&&i==1) {
				selected[i] = true;
			}else{
				if ((timerValue & (1 << i-2)) > 0) {
					selected[i] = true;
				}else {
					selected[i] = false;
				}
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.mipmap.img_air_time_meihong);
		builder.setTitle(getString(R.string.trip_cycle));
		builder.setMultiChoiceItems(items, selected,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
										boolean isChecked) {
						// TODO Auto-generated method stub
						selected[which] = isChecked;
					}
				});
		builder.setPositiveButton(activity.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						timerValue=0;
						if (selected[0]) {
							timerValue = 128;
//							subscribeDate.setText(timerValue+items[0]);
							showDialog(DATE_DIALOG);
						}else {
							if(selected[1]){
								timerValue=127;
								subscribeDate.setText(getString(R.string.everyday));
							}else {
								String tempWeek = "";
								for (int i = 2; i < selected.length; i++) {
									if (selected[i]) {
										timerValue += (1 << (i - 2));
										tempWeek += items[i] + "-";
									}
								}
								if (tempWeek == "") {
									return;
								}
								if(timerValue==127){
									subscribeDate.setText(getString(R.string.everyday));
								}else {
									subscribeDate.setText(tempWeek.substring(0,
											tempWeek.lastIndexOf("-")));
								}
							}
						}
					}
				});
		builder.setNegativeButton(activity.getString(R.string.cancle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		builder.create().show();

	}
	public  void  setTxt(String title){
		//最好判斷焦點在哪個控件
		if(subscribeName.hasFocus()){
			int index = subscribeName.getSelectionStart();
			Editable editable = subscribeName.getText();
			editable.insert(index, title);
			if (title.equals("[  ]")) {
				index = subscribeName.getSelectionStart();
				subscribeName.setSelection(index - 1);
			}
		}else if(subscribeAction.hasFocus()) {
			int index = subscribeAction.getSelectionStart();
			Editable editable = subscribeAction.getText();
			editable.insert(index, title);
			if (title.equals("[  ]")) {
				index = subscribeAction.getSelectionStart();
				subscribeAction.setSelection(index - 1);
			}
		}else if(subscribeEvent.hasFocus()){
			int index = subscribeEvent.getSelectionStart();
			Editable editable = subscribeEvent.getText();
			editable.insert(index, title);
			if (title.equals("[  ]")) {
				index = subscribeEvent.getSelectionStart();
				subscribeEvent.setSelection(index - 1);
			}
		}
	}
}
