package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.jpush.TestActivity;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.ProjectorServiceUtils;

public class RedFraAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editText,nameText,versionText;
	private Button cancleBtn, sureBtn;
	ImageView icoImg;
	Spinner serialSpinner, bauSpinner,checkSpinner;
	CheckBox spliceBox;
	RadioButton ascIIBox,hexBox;
	private int serialIndex=0;
	private int bauIndex=0;
	private int checkIndex=0;
	TextView checkText;

    private String serialStr="FF";
    private String spliceStr="00";
	String[] serials;
	String[] baus;
	String[] checks;
	boolean isAscii=false;
	boolean isHex=false;
	public RedFraAlert(Activity _context) {
		super(_context, R.style.MyDialog);
		this.context = _context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_redfra_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		editText = (EditText) findViewById(R.id.editTxt);
		serialSpinner=(Spinner)findViewById(R.id.serialSpinner);
		bauSpinner=(Spinner)  findViewById(R.id.bauSpinner);
		checkSpinner=(Spinner)  findViewById(R.id.checkSpinner);
		spliceBox=(CheckBox)findViewById(R.id.spliceBox);
		checkText=(TextView)findViewById(R.id.checkText) ;
		nameText=(EditText)findViewById(R.id.nameText);
		versionText=(EditText)findViewById(R.id.versionText);
		spliceBox.setOnClickListener(this);
		ascIIBox=(RadioButton)findViewById(R.id.ascIIBox);
		hexBox=(RadioButton)findViewById(R.id.hexBox);
		ascIIBox.setOnClickListener(this);
		hexBox.setOnClickListener(this);
		editText.requestFocus();
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
		//串口
		serials = context.getResources().getStringArray(R.array.serials);
		ArrayAdapter<String> serialAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, serials);
		serialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		serialSpinner .setAdapter(serialAdapter);
		serialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				serialIndex=pos;
				if(serialIndex==0){
					checkText.setText(context.getString(R.string.memory));
					bauSpinner.setEnabled(false);
					bauSpinner.setSelection(0);
					checks = context.getResources().getStringArray(R.array.check);
					ArrayAdapter<String> checkAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, checks);
					checkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					checkSpinner .setAdapter(checkAdapter);
					checkIndex=0;
					bauIndex=0;
				}else{
					checkText.setText(context.getString(R.string.checkText));
					bauSpinner.setEnabled(true);
					checks = context.getResources().getStringArray(R.array.checks);
					ArrayAdapter<String> checkAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, checks);
					checkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					checkSpinner .setAdapter(checkAdapter);
					checkIndex=0;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});

		//波特率
		 baus = context.getResources().getStringArray(R.array.baus);
		ArrayAdapter<String> bausAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, baus);
		bausAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bauSpinner .setAdapter(bausAdapter);
		bauSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				bauIndex=pos;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}

		});

		//校验
		checks = context.getResources().getStringArray(R.array.checks);
		final ArrayAdapter<String> checkAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, checks);
		checkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		checkSpinner .setAdapter(checkAdapter);
		checkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				checkIndex=pos;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
//		textChange();
		 isAscii=ascIIBox.isChecked();
		 isHex=hexBox.isChecked();
	}

	public void setTitle(int imageId, String title) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
	}
	public void setHint(String str) {
		editText.setHint(str);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.ascIIBox: {
				if (!isAscii) {
					setHint("请输入字符串");
					String codeData = editText.getText().toString();
					if (codeData.length() > 0) {
						codeData = AppTools.hexStr2Str(codeData);
						editText.setText(codeData);
					}
					isAscii=ascIIBox.isChecked();
					isHex=false;
					editText.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEF"));
					editText.setInputType(InputType.TYPE_CLASS_TEXT);
					int len=editText.getText().length();
					if(len>0) {
						editText.setSelection(len);
					}
				}
			}
				break;
			case R.id.hexBox: {
				if (!isHex) {
					setHint("请输入16进制");
					String codeData = editText.getText().toString();
					if (codeData.length() > 0) {
						codeData = AppTools.toStringHex(codeData);
						editText.setText(codeData);
					}
					isHex=hexBox.isChecked();
					isAscii=false;
					editText.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEF"));
					int len=editText.getText().length();
					if(len>0) {
						editText.setSelection(len);
					}
				}
			}
				break;
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			if(TextUtils.isEmpty(editText.getText())) {
				ToastUtils.ShowError(context,context.getString(R.string.input_hexadecimal),Toast.LENGTH_SHORT,true);
				return;
			}
			if(TextUtils.isEmpty(nameText.getText())) {
				ToastUtils.ShowError(context,"请输入名称",Toast.LENGTH_SHORT,true);
				return;
			}
			if(TextUtils.isEmpty(versionText.getText())) {
				ToastUtils.ShowError(context,"请输入型号",Toast.LENGTH_SHORT,true);
				return;
			}
			if(serialIndex==0){
				serialStr="FF";
			}else{
				serialStr= AppTools.toHex(bauIndex*16+(serialIndex-1));
			}

			if(spliceBox.isChecked()){
				spliceStr="F0";
			}else{
				spliceStr="0"+checkIndex;
			}
			String codeData=editText.getText().toString().toUpperCase().trim().replace(" ","");
			if(hexBox.isChecked()){
				if(codeData.length()%2!=0){
					ToastUtils.ShowError(context,"请输入正确的码",Toast.LENGTH_SHORT,true);
					return;
				}
			}

			if (onSureListener != null) {
					onSureListener.onSure(serialStr+spliceStr+codeData);
				}
			if(!TextUtils.isEmpty(nameText.getText())&&!TextUtils.isEmpty(versionText.getText())) {
				String nameStr=nameText.getText().toString();
				String versionStr=versionText.getText().toString().toUpperCase();
				new NamesAsyncTask().execute(nameStr, versionStr, baus[bauIndex], checks[checkIndex], codeData, serialStr+spliceStr+codeData);
			}

			break;
			case R.id.spliceBox:
				if(spliceBox.isChecked()){
					spliceBox.setChecked(true);
					checkSpinner.setEnabled(false);
				}else{
					spliceBox.setChecked(false);
					checkSpinner.setEnabled(true);
				}
				break;
		default:
			break;
		}
	}

	public interface OnSureListener {
		public void onCancle();

		public void onSure(String str);
	}

	private OnSureListener onSureListener;

	/**
	 * @param onSureListener
	 *            the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}
	public class NamesAsyncTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo listUser = null;
			try {
				String json = ProjectorServiceUtils.createProjectors(params[0], params[1], params[2], params[3], params[4], params[5],2);
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
				ToastUtils.ShowSuccess(context, result.getMsg(), Toast.LENGTH_SHORT, true);
			}
		}
	}
}
