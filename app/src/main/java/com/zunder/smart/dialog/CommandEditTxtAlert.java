package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.tools.AppTools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
public class CommandEditTxtAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editText;
	private Button cancleBtn, sureBtn;
	ImageView icoImg;
	RadioButton Gradio,Lradio;
	Spinner arceSpinner;
	String tempStr="";
	LinearLayout radioLayout;
	ImageView timeImage;
	String[] arces;
	LinearLayout arceLay;
	private int index=0;
	private int arceIndex=0;
	int mYear, mMonth, mDay,hourOfDay,minute;
	public CommandEditTxtAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_command_verify);
		this.context = context;
		tempStr=context.getString(R.string.Big);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		editText = (EditText) findViewById(R.id.editTxt);
		arceSpinner=(Spinner)findViewById(R.id.arceSpinner);
		arceLay=(LinearLayout)findViewById(R.id.arceLay);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
		Gradio=(RadioButton)findViewById(R.id.Gradio);
		Lradio=(RadioButton)findViewById(R.id.Lradio);
		timeImage=(ImageView)findViewById(R.id.timeImage);
		timeImage.setOnClickListener(this);
		radioLayout=(LinearLayout)findViewById(R.id.radioLayout) ;

		Gradio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(Gradio.isChecked()){
					tempStr=Gradio.getText().toString();
					Lradio.setChecked(false);
				}
			}
		});
		Lradio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(Lradio.isChecked()){
					tempStr=Lradio.getText().toString();
					Gradio.setChecked(false);
				}
			}
		});
	}

	public void setTitle(int imageId, String title,int index,String params1,String params2) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
		this.index=index;
		tempStr=context.getString(R.string.Big);
		if(index>=1&&index<6){
			arceLay.setVisibility(View.VISIBLE);
			arces = context.getResources().getStringArray(R.array.arces);
			ArrayAdapter<String> serialAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, arces);
			serialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			arceSpinner .setAdapter(serialAdapter);
			arceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					arceIndex=pos;
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}else if(index==8){
			tempStr=context.getString(R.string.excellent);
			Gradio.setText(params1);
			Lradio.setText(params2);
			editText.setVisibility(View.GONE);
		}else if(index>9&&index<19){
				final Calendar ca = Calendar.getInstance();
				mYear = ca.get(Calendar.YEAR);
				mMonth = ca.get(Calendar.MONTH);
				mDay = ca.get(Calendar.DAY_OF_MONTH);
				hourOfDay = ca.get(Calendar.HOUR_OF_DAY);
				minute = ca.get(Calendar.MINUTE);
				tempStr="";
				radioLayout.setVisibility(View.GONE);
				timeImage.setVisibility(View.VISIBLE);
				editText.setHintTextColor(Color.GRAY);
			    if(index==10) {
					editText.setHint("如:18年01月01日08时01分");
				}else	if(index==11) {
					editText.setHint("如:18年01月01日");
				}else	if(index==12) {
					editText.setHint("输入范围:01--12");
				}else if(index==13) {
					editText.setHint("输入范围:01--31");
				}else if(index==14) {
					editText.setHint("输入范围:00--23");
				}else if(index==15) {
					editText.setHint("输入范围:00--59");
				}else if(index==17) {
					radioLayout.setVisibility(View.VISIBLE);
					tempStr=context.getString(R.string.Big);
					editText.setHint("输入范围:00--23" );
				}else if(index==18) {
					tempStr=context.getString(R.string.Big);
					radioLayout.setVisibility(View.VISIBLE);
					editText.setHint("输入范围:00-59");
				}else if(index==16){
					radioLayout.setVisibility(View.GONE);
					tempStr=context.getString(R.string.Second);
					editText.setVisibility(View.VISIBLE);
					Gradio.setChecked(false);
					editText.setHint("只能输入数字,默认单位秒");
					timeImage.setVisibility(View.GONE);
				}
			}
	}
	public void setEditTextType(int type){
		editText.setInputType(type);
	}

	public void setHint(String str) {
		editText.setHintTextColor(Color.GRAY);
		editText.setHint(str);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			if(editText.getVisibility()==View.VISIBLE) {
				if (TextUtils.isEmpty(editText.getText())) {
					ToastUtils.ShowError(context, context.getString(R.string.input_num_code), Toast.LENGTH_SHORT, true);
					return;
				}
			}
			if (onSureListener != null) {
				String result=editText.getText().toString();
				if(index==16){
					String resultStr="";
					if(arces!=null){
						resultStr=arces[arceIndex];
					}
					onSureListener.onSure(resultStr+titleTxt.getText()+ (result.length() == 1 ? "0" + result : result)+tempStr);
				}else {
					String resultStr="";
					if(arces!=null){
						resultStr=arces[arceIndex];
					}
					onSureListener.onSure(resultStr+titleTxt.getText() + tempStr + (result.length() == 1 ? "0" + result : result));
				}
				dismiss();
			}
			break;
			case R.id.timeImage:
				dateTime="";
				if(index==10){
					new DatePickerDialog(context, mdateListener, mYear, mMonth, mDay).show();
				}else if(index==11) {
					new DatePickerDialog(context, mdateListener, mYear, mMonth, mDay).show();
				}else if(index==12) {
					new DatePickerDialog(context, mdateListener, mYear, mMonth, mDay).show();
				}else if(index==13) {
					new DatePickerDialog(context, mdateListener, mYear, mMonth, mDay).show();
				}else if(index==14) {
					new TimePickerDialog(context, mtimeListener, hourOfDay, minute, true).show();
			}else if(index==15) {
					new TimePickerDialog(context, mtimeListener, hourOfDay, minute, true).show();
			}else if(index==17) {
					new TimePickerDialog(context, mtimeListener, hourOfDay, minute, true).show();
			}else if(index==18) {
					new TimePickerDialog(context, mtimeListener, hourOfDay, minute, true).show();
			}
				break;
		default:
			break;
		}

	}
	 String dateTime="";
	private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {

			String _year=(year-2000)+"";
			String _monthOfYear=(monthOfYear+1)<10?"0"+(monthOfYear+1):(monthOfYear+1)+"";
			String _dayOfMonth=dayOfMonth<10?"0"+dayOfMonth:dayOfMonth+"";
			if(dateTime.equals("")) {
				if(index==10){
				display(year, monthOfYear + 1, dayOfMonth);
			}else if(index==11) {
					editText.setText(_year+_monthOfYear+_dayOfMonth);
				}else if(index==12) {
					editText.setText(_monthOfYear);
				}else if(index==13) {
					editText.setText(_dayOfMonth);
				}
				editText.setSelection(editText.getText().length());
			}
		}
	};
	private TimePickerDialog.OnTimeSetListener mtimeListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			String _hour = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + "";
			String _minute = (minute < 10 ? "0" + minute : minute) + "";
			if(index==14) {
				editText.setText(_hour );
			}else if(index==15) {
				editText.setText( _minute);
			}else if(index==17) {
				editText.setText(_hour);
			}else if(index==18) {
				editText.setText( _minute);
			}
			editText.setSelection(editText.getText().length());
		}
	};

	public void display( int year, int monthOfYear,
						 int dayOfMonth) {
		try {
			dateTime=(year-2000) + "";
			final String _year=(year-2000) + "";
			final String _monthOfYear=(monthOfYear < 10 ? "0" + monthOfYear : monthOfYear) + "";
			final String _dayOfMonth=(dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)+"";
			showTime(_year,_monthOfYear,_dayOfMonth);
		}catch(Exception e){
		}
	}
	void showTime(final String _year,final String _monthOfYear,final String _dayOfMonth ){
		new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
				String _hour = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + "";
				String _minute = (minute < 10 ? "0" + minute : minute) + "";
				editText.setText(_year + _monthOfYear + _dayOfMonth + _hour + _minute);
				editText.setSelection(editText.getText().length());
			}
		}, hourOfDay, minute, true).show();
	}
	public interface OnSureListener {
		public void onCancle();

		public void onSure(String str);
	}

	private OnSureListener onSureListener;
	/**
	 * @param onSureListener
	 *           the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}
}
