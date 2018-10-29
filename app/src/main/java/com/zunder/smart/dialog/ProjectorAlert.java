package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.ProjectorServiceUtils;

//ȷ����ʾ��
public class ProjectorAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editText,nameText,versionText;
	private Button cancleBtn, sureBtn;
	RadioButton ascIIBox,hexBox;
	ImageView icoImg;
	Spinner bauText,checkText;
	private int bauIndex=0;
	private int checkIndex=0;
	String[] baus;
	String[] checks;
	boolean isAscii=false;
	boolean isHex=false;
	public ProjectorAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_projector_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		editText = (EditText) findViewById(R.id.editTxt);
		nameText=(EditText)findViewById(R.id.nameText);
		versionText=(EditText)findViewById(R.id.versionText);
		bauText=(Spinner)  findViewById(R.id.bauText);
		checkText=(Spinner)  findViewById(R.id.checkText);
		ascIIBox=(RadioButton)findViewById(R.id.ascIIBox);
		hexBox=(RadioButton)findViewById(R.id.hexBox);
		ascIIBox.setOnClickListener(this);
		hexBox.setOnClickListener(this);
		editText.requestFocus();
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
		//波特率
		 baus = context.getResources().getStringArray(R.array.baus);
		ArrayAdapter<String> bausAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, baus);
		bausAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bauText .setAdapter(bausAdapter);
		bauText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
		ArrayAdapter<String> checkAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, checks);
		checkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		checkText .setAdapter(checkAdapter);
		checkText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
		isAscii=ascIIBox.isChecked();
		isHex=hexBox.isChecked();
	}

	public void setTitle(int imageId, String title) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
	}
	public void setEditTextType(int type){
		editText.setInputType(type);
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
					isAscii=false;
					editText.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEF"));
					editText.setInputType(InputType.TYPE_CLASS_TEXT);
					int len=editText.getText().length();
					if(len>0) {
						editText.setSelection(len);
					}
				}
			}
			break;
			case R.id.hexBox:
			{
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
			String bauStr="0"+bauIndex+"0"+checkIndex;
			String codeData=editText.getText().toString().toUpperCase().trim().replace(" ","");
			if(hexBox.isChecked()){
				if(codeData.length()%2!=0){
					ToastUtils.ShowError(context,"请输入正确的码",Toast.LENGTH_SHORT,true);
					return;
				}
			}

			if (onSureListener != null) {
					onSureListener.onSure(bauStr+codeData);
				}
			if(!TextUtils.isEmpty(nameText.getText())&&!TextUtils.isEmpty(versionText.getText())) {
				String nameStr=nameText.getText().toString();
				String versionStr=versionText.getText().toString().toUpperCase()+(titleTxt.getText().toString().contains("开")?"_ON":"_OFF");
				new NamesAsyncTask().execute(nameStr, versionStr, baus[bauIndex], checks[checkIndex], codeData, bauStr+codeData);
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
				String json = ProjectorServiceUtils.createProjectors(params[0], params[1], params[2], params[3], params[4], params[5],1);
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
