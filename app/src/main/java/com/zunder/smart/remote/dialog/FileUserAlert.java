package com.zunder.smart.remote.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
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
import com.zunder.smart.dialog.RedFraCloundAlert;
import com.zunder.smart.model.ProjectorCode;
import com.zunder.smart.model.ProjectorName;
import com.zunder.smart.model.ProjectorVersion;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.adapter.UserTypeAdapter;
import com.zunder.smart.remote.model.FileUser;
import com.zunder.smart.remote.model.UserType;
import com.zunder.smart.remote.webservice.FileUserServiceUtils;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.ProjectorServiceUtils;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.model.FileUser;
import com.zunder.smart.remote.model.UserType;
import com.zunder.smart.tools.JSONHelper;

import java.util.List;

//ȷ����ʾ��
public class FileUserAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText userName,userTel,userNick;
	private Button cancleBtn, sureBtn;
	Spinner userTypeSpinner;
	private int userType=1;
	FileUser user;
	public FileUserAlert(Activity _context,	FileUser _user) {
		super(_context, R.style.MyDialog);
		this.context = _context;
		this.user=_user;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_file_user_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		userName = (EditText) findViewById(R.id.userName);
		userTel = (EditText) findViewById(R.id.tel);
		userNick = (EditText) findViewById(R.id.nickName);
		userTypeSpinner=(Spinner) findViewById(R.id.userType);

		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				//new RedFraCloundAlert.CodeTask(((ProjectorVersion)parent.getItemAtPosition(pos)).getId()).execute();
				userType=((UserType)parent.getItemAtPosition(pos)).getId();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
		new UserTypeTask().execute();
		if(user!=null){
			userName.setText(user.getUserName());
			userNick.setText(user.getUserNick());
			userTel.setText(user.getUserTel());
		}
	}

	private OnSureListener onSureListener;

	/**
	 * @param onSureListener
	 *            the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			String userNickStr=userNick.getText().toString();
			String userTelStr=userTel.getText().toString();
			if(TextUtils.isEmpty(userName.getText())) {
				ToastUtils.ShowError(context,context.getString(R.string.noUserNull),Toast.LENGTH_SHORT,true);
				return;
			}
			if(TextUtils.isEmpty(userNick.getText())) {
				userNickStr=userTypeSpinner.getSelectedItem().toString();
			}
			if(TextUtils.isEmpty(userTel.getText())) {
				userTelStr="00000000";

			}
			String  userNameStr=userName.getText().toString();


			new UserAddTask().execute(userNameStr, userType+"",userTelStr, userNickStr, RemoteMainActivity.deviceID);
			break;
		default:
			break;
		}
	}



	public class UserTypeTask extends AsyncTask<String, Void, List<UserType>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<UserType> doInBackground(String... params) {
			List<UserType>  list = null;
			try {
				String json = FileUserServiceUtils.getUserType(0);
				list = (List<UserType>) JSONHelper.parseCollection(json,
						List.class, UserType.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		@Override
		protected void onPostExecute(List<UserType> result) {

			if (result != null&&result.size()>0) {
				if(result.size()>0) {
					UserTypeAdapter userTypeAdapter = new UserTypeAdapter(context, result);
					userTypeSpinner.setAdapter(userTypeAdapter);

					if(user!=null){
						userTypeSpinner.setSelection(user.getUserType()-1);
					}
				}
			}
		}
	}
	public class UserAddTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo resultInfo = null;
			try {
			if(user!=null){
				String result = FileUserServiceUtils.updateFileUsers(params[0],Integer.parseInt(params[1]),params[2],params[3],params[4],user.getId()).replace("[", "").replace("]", "");
				resultInfo = (ResultInfo) JSONHelper.parseObject(result,
						ResultInfo.class);
			}else{
				String result = FileUserServiceUtils.insertFileUsers(params[0],Integer.parseInt(params[1]),params[2],params[3],params[4]).replace("[", "").replace("]", "");
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
					ToastUtils.ShowSuccess(context, result.getMsg(), Toast.LENGTH_SHORT, true);
					if(onSureListener!=null){
						onSureListener.onSure();
					}
				} else {
					ToastUtils.ShowSuccess(context, result.getMsg(), Toast.LENGTH_SHORT, true);
				}
			}
		}
	}
	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}
}
