package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.adapter.ProjectorNameAdapter;
import com.zunder.smart.adapter.ProjectorVersionAdapter;
import com.zunder.smart.model.ProjectorCode;
import com.zunder.smart.model.ProjectorName;
import com.zunder.smart.model.ProjectorVersion;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.ProjectorServiceUtils;
import com.zunder.smart.model.ProjectorCode;
import com.zunder.smart.model.ProjectorName;
import com.zunder.smart.model.ProjectorVersion;
import com.zunder.smart.tools.JSONHelper;

import java.util.ArrayList;
import java.util.List;

//ȷ����ʾ��
public class ProjectorCloundAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editText;
	private Button cancleBtn, sureBtn;
	ImageView icoImg;
	Spinner bauSpinner,checkSpinner,nameSpinner,versionSpinner;
	private int bauIndex=0;
	private int checkIndex=0;
	String[] baus;
	String[] checks;
	private String nameStr="";
	public ProjectorCloundAlert(Activity _context,List<ProjectorName> _list) {
		super(_context, R.style.MyDialog);
		this.context = _context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_projector_clound_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		editText = (EditText) findViewById(R.id.editTxt);
		nameSpinner=(Spinner) findViewById(R.id.nameSpinner);
		versionSpinner=(Spinner) findViewById(R.id.versionSpinner);
		bauSpinner=(Spinner)  findViewById(R.id.bauSpinner);
		checkSpinner=(Spinner)  findViewById(R.id.checkSpinner);
		editText.clearFocus();
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
		//品牌
		final List<ProjectorName> list=_list;
		ProjectorNameAdapter nameAdapter=new ProjectorNameAdapter(context, list);
		nameSpinner .setAdapter(nameAdapter);
		nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				new VersionTask(list.get(pos).getId()).execute();

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});

		versionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				ProjectorVersion projectorVersion=((ProjectorVersion)parent.getItemAtPosition(pos));
				nameStr=projectorVersion.getVersionName();
				new CodeTask(projectorVersion.getId()).execute();
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
		ArrayAdapter<String> checkAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, checks);
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
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			if(TextUtils.isEmpty(editText.getText())) {
				ToastUtils.ShowError(context,context.getString(R.string.input_hexadecimal),Toast.LENGTH_SHORT,true);
				return;
			}
			String bauStr="0"+bauIndex+"0"+checkIndex;
			String codeData=editText.getText().toString().toUpperCase().trim().replace(" ","");
			if (onSureListener != null) {
					onSureListener.onSure(nameStr,bauStr+codeData);
				}

			break;
		default:
			break;
		}
	}

	public interface OnSureListener {
		public void onCancle();

		public void onSure(String name,String code);
	}

	private OnSureListener onSureListener;

	/**
	 * @param onSureListener
	 *            the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}

	public class VersionTask extends AsyncTask<String, Void, List<ProjectorVersion>> {
		private int index=0;
		public VersionTask(int _index){
			this.index=_index;
		}
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<ProjectorVersion> doInBackground(String... params) {
			List<ProjectorVersion>  list = null;
			try {
				String json = ProjectorServiceUtils.getProjectorVersions(index);
				list = (List<ProjectorVersion>) JSONHelper.parseCollection(json,
						List.class, ProjectorVersion.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		@Override
		protected void onPostExecute(List<ProjectorVersion> result) {

			if (result != null) {
				if(result.size()>0) {
					ProjectorVersionAdapter projectorVersionAdapter = new ProjectorVersionAdapter(context, result);
					versionSpinner.setAdapter(projectorVersionAdapter);
				}else{
					ToastUtils.ShowError(context,context.getString(R.string.no_version),Toast.LENGTH_SHORT,true);
					versionSpinner.setAdapter(null);
					editText.setText("");
				}
			}else{
				ToastUtils.ShowError(context,context.getString(R.string.no_version),Toast.LENGTH_SHORT,true);
				versionSpinner.setAdapter(null);
				editText.setText("");
            }
		}
	}

	public class CodeTask extends AsyncTask<String, Void, List<ProjectorCode>> {
		private int index=0;
		public CodeTask(int _index){
			this.index=_index;
		}
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<ProjectorCode> doInBackground(String... params) {
			List<ProjectorCode>  list = null;
			try {
				String json = ProjectorServiceUtils.getProjectorCode(index);
				list = (List<ProjectorCode>) JSONHelper.parseCollection(json,
						List.class, ProjectorCode.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		@Override
		protected void onPostExecute(List<ProjectorCode> result) {
			String msg;
			if (result != null) {
				if(result.size()>0) {
					ProjectorCode projectorCode = result.get(0);
					String code = projectorCode.getSendCode();
					String hex = projectorCode.getHexCode();
					bauIndex = Integer.parseInt(code.substring(0, 2));
					checkIndex = Integer.parseInt(code.substring(2, 4));
					editText.setText(projectorCode.getHexCode());
					bauSpinner.setSelection(bauIndex);
					checkSpinner.setSelection(checkIndex);
				}else{
					ToastUtils.ShowSuccess(context,context.getString(R.string.no_data),Toast.LENGTH_SHORT,true);
					editText.setText("");
				}
			}else{
				ToastUtils.ShowSuccess(context,context.getString(R.string.no_data),Toast.LENGTH_SHORT,true);
				editText.setText("");
			}
		}
	}
}
