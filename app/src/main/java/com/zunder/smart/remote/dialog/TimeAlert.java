package com.zunder.smart.remote.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.dialog.MutilCheckAlert;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.remote.model.UserType;
import com.zunder.smart.remote.webservice.FileTimeServiceUtils;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.dialog.MutilCheckAlert;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;

import java.util.Calendar;
import java.util.List;

//ȷ����ʾ��
public class TimeAlert extends Dialog implements OnClickListener {

	private Activity activity;
	private Button cancleBtn, sureBtn;
	Spinner cycleSpinner;
	String[] cycles;
	EditText timeName, startTime,endTime,dateTime,cycleTime;
	ImageView startImg,endImg,dateImg,cycleImg;
	int mYear, mMonth, mDay,hourOfDay,minute;
	LinearLayout dateLayout,cycleLayout;
	int cycle=127;
	List<FileTime> list;
	FileTime fileTime=null;
	public TimeAlert(Activity _context,List<FileTime> list,FileTime _fileTime) {
		super(_context, R.style.MyDialog);
		this.activity = _context;
		this.list=list;
		this.fileTime=_fileTime;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_file_time_verify);
		cycleSpinner=(Spinner) findViewById(R.id.cycleSpinner);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		timeName=(EditText)findViewById(R.id.timeName);
		startTime=(EditText)findViewById(R.id.startTime);
		endTime=(EditText)findViewById(R.id.endTime);
		dateTime=(EditText)findViewById(R.id.dateTime);
		cycleTime=(EditText)findViewById(R.id.cycleTime);
		startImg=(ImageView)findViewById(R.id.startImage);
		endImg=(ImageView)findViewById(R.id.endImage);
		dateImg=(ImageView)findViewById(R.id.dateImage);
		cycleImg=(ImageView)findViewById(R.id.cycleImg);
		dateLayout=(LinearLayout)findViewById(R.id.dateLayout);
		cycleLayout=(LinearLayout)findViewById(R.id.cycleLayout);
		startImg.setOnClickListener(this);
		endImg.setOnClickListener(this);
		dateImg.setOnClickListener(this);
		cycleImg.setOnClickListener(this);
		cycleLayout.setOnClickListener(this);
		cycles = activity.getResources().getStringArray(R.array.cycles);
		ArrayAdapter<String> bausAdapter=new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, cycles);
		bausAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cycleSpinner .setAdapter(bausAdapter);
		cycleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				if(pos==0){
					cycle=127;
					dateLayout.setVisibility(View.GONE);
					cycleLayout.setVisibility(View.GONE);
				}else if(pos==1){
					cycleLayout.setVisibility(View.VISIBLE);
					dateLayout.setVisibility(View.GONE);
				}else if(pos==2){
					cycle=0;
					dateLayout.setVisibility(View.GONE);
					cycleLayout.setVisibility(View.GONE);
				}else if(pos==3){
					cycle=128;
					dateLayout.setVisibility(View.VISIBLE);
					cycleLayout.setVisibility(View.GONE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
		dateTime.setText(AppTools.getDate());
		final Calendar ca = Calendar.getInstance();
		mYear = ca.get(Calendar.YEAR);
		mMonth = ca.get(Calendar.MONTH);
		mDay = ca.get(Calendar.DAY_OF_MONTH);
		hourOfDay = ca.get(Calendar.HOUR_OF_DAY);
		minute = ca.get(Calendar.MINUTE);

		if(fileTime!=null){
			timeName.setText(fileTime.getTimeName());
			startTime.setText(fileTime.getStartTime());
			endTime.setText(fileTime.getEndTime());
			cycle=fileTime.getCycle();
			if(cycle==0){
				cycleSpinner.setSelection(2);
			}
			else if(cycle==127){
				cycleSpinner.setSelection(0);
			}
			else if(cycle==128){
				cycleSpinner.setSelection(3);
			}
			else{
				cycleSpinner.setSelection(1);
			}
			cycleTime.setText(AppTools.getWeeks(fileTime.getCycle()));
			dateTime.setText(fileTime.getAssignDate());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.startImage: {

				DateTimeDialog startDateChooseDialog = new DateTimeDialog(activity, 2,0, new DateTimeDialog.DateChooseInterface() {
					@Override
					public void getDateTime(String time, boolean longTimeChecked) {
						startTime.setText(time);
					}
				});
				startDateChooseDialog.setDateDialogTitle("开始时间");
				startDateChooseDialog.showDateChooseDialog();
			}
				break;
			case R.id.endImage: {
				DateTimeDialog startDateChooseDialog = new DateTimeDialog(activity, 2,0, new DateTimeDialog.DateChooseInterface() {
					@Override
					public void getDateTime(String time, boolean longTimeChecked) {
						endTime.setText(time);
					}
				});
				startDateChooseDialog.setDateDialogTitle("结束时间");
				startDateChooseDialog.showDateChooseDialog();
			}
				break;
			case R.id.dateImage:
				new DatePickerDialog(activity, mdateListener, mYear, mMonth, mDay).show();
				break;
			case R.id.cycleImg:
				AskTimeSch();
				break;
			case R.id.cancle_bt:
				dismiss();
				break;
			case R.id.sure_bt:

				String name=timeName.getText().toString().trim();
				String sart = startTime.getText().toString().trim();
				String end = endTime.getText().toString().trim();

				if (TextUtils.isEmpty(timeName.getText())) {
					ToastUtils.ShowError(activity,"名称不能为空",Toast.LENGTH_SHORT,true);
					return;
				}

				if (AppTools.timeConvert(end) <= AppTools.timeConvert(sart)) {
					ToastUtils.ShowError(activity,activity.getString(R.string.end_start),Toast.LENGTH_SHORT,true);
					return;
				}
				String StartTime=startTime.getText().toString().trim();
				String EndTime=endTime.getText().toString().trim();
				if(fileTime==null) {
					if (checkTime(StartTime, EndTime) > 0) {
						ToastUtils.ShowError(activity, "时间段已经存在,请选择正确的时间", Toast.LENGTH_SHORT, true);
						return;
					}
				}
				String Cycle=cycle+"";
				String AssignDate=dateTime.getText().toString();
				new TimeAddTask().execute(name,StartTime, EndTime, Cycle, AssignDate, RemoteMainActivity.deviceID);
				break;
		default:
			break;
		}
	}
	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}

	private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			String _monthOfYear=(monthOfYear+1)<10?"0"+(monthOfYear+1):(monthOfYear+1)+"";
			String _dayOfMonth=dayOfMonth<10?"0"+dayOfMonth:dayOfMonth+"";
			dateTime.setText(year+"-"+_monthOfYear+"-"+_dayOfMonth);
		}
	};
	private OnSureListener onSureListener;
	/**
	 * @param onSureListener
	 *           the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}
	private void AskTimeSch() {

		final String items[] = (String[]) activity.getResources()
				.getStringArray(R.array.fweeken);
		final boolean[] selected = new boolean[items.length];
		for (int i = 0; i < selected.length; i++) {
			if(cycle==127) {
				selected[i] = true;
			}else{
				if ((cycle & (1 << i)) > 0) {
					selected[i] = true;
				}else {
					selected[i] = false;
				}
			}
		}
		final MutilCheckAlert mutilCheckAlert=new MutilCheckAlert(activity);
		mutilCheckAlert.setMultiChoiceItems(items, selected,
				new MutilCheckAlert.OnMultiChoiceClickListener() {
					@Override
					public void onClick(int which,
										boolean isChecked) {
						// TODO Auto-generated method stub
						selected[which] = isChecked;
					}
				});
		mutilCheckAlert.setOnSureListener(new MutilCheckAlert.OnSureListener() {
			@Override
			public void onCancle() {

			}
			@Override
			public void onSure() {
				cycle=0;
				String tempWeek = "";
				for (int i = 0; i < selected.length; i++) {
					if (selected[i]) {
						cycle += (1 << i);
						tempWeek += items[i] + "-";
					}
				}
				if (tempWeek == "") {
					return;
				}
				if(cycle==127){
					cycleTime.setText(activity.getString(R.string.everyday));
				}else {
					cycleTime.setText(tempWeek.substring(0,
							tempWeek.lastIndexOf("-")));
				}
				mutilCheckAlert.dismiss();
			}
		});
		mutilCheckAlert.show();

	}
	public class TimeAddTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo resultInfo = null;
			try {
				if(fileTime!=null){
					String result = FileTimeServiceUtils.updateFileTimes(fileTime.getId(),params[0], params[1], params[2], Integer.parseInt(params[3]), params[4], params[5]).replace("[", "").replace("]", "");
					resultInfo = (ResultInfo) JSONHelper.parseObject(result,
							ResultInfo.class);
				}else {
					String result = FileTimeServiceUtils.insertFileTimes(params[0], params[1],   params[2],Integer.parseInt(params[3]), params[4], params[5]).replace("[", "").replace("]", "");
					resultInfo = (ResultInfo) JSONHelper.parseObject(result,
							ResultInfo.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return resultInfo;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			String msg;
			if (result != null) {
				if (result.getResultCode() > 0) {
					ToastUtils.ShowSuccess(activity, result.getMsg(), Toast.LENGTH_SHORT, true);
					if(onSureListener!=null){
						onSureListener.onSure();
					}
				} else {
					ToastUtils.ShowSuccess(activity, result.getMsg(), Toast.LENGTH_SHORT, true);
				}
			}
		}
	}
	public int checkTime(String startTimne,String endTime){
		int start= AppTools.timeConvert(startTimne);
		int end=AppTools.timeConvert(endTime);
		int result=0;
		if(list.size()>0){
			for (int i=0;i<list.size();i++){
				FileTime fileTime=list.get(i);
				int startFile= AppTools.timeConvert(fileTime.getStartTime());
				int endFile=AppTools.timeConvert(fileTime.getEndTime());
				if((start>=startFile&&start<=endFile)||(end>=startFile&&end<=endFile)){
					result=1;
					break;
				}else if(end>endFile&&(start>startFile&&start<endFile)){
					result=1;
					break;
				}else if(start<startFile&&(end>startFile&&end<endFile)){
					result=1;
					break;
				}else if(start<startFile&&end>endFile){
					result=1;
					break;
				}
			}
		}
		return result;
	}
}
