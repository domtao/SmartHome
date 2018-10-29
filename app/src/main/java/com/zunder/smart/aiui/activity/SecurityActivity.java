package com.zunder.smart.aiui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.aiui.adapter.AnHongAdapter;
import com.zunder.smart.aiui.info.AnHong;
import com.zunder.smart.dialog.SecurityAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.SecurityServiceUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class SecurityActivity extends Activity implements OnClickListener
		{
	private ListView listView;
	AnHongAdapter adapter;
	SecurityActivity activity;
	TextView freshDevice;
	TextView addDevice;
	private RightMenu rightButtonMenuAlert;
	List<AnHong> list = new ArrayList<AnHong>();
	static	String deviceID;
	public static void startActivity(Activity activity,String _deviceID) {
		Intent intent = new Intent(activity, SecurityActivity.class);
		deviceID=_deviceID;
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aiui_anhong_list);
		activity = this;
		initview();


//		String result = ISocketCode.setForwardSecurity("get info", deviceID);
//		MainActivity.getInstance().sendCode( result);
		new DataTask().execute();
	}
	WarnDialog warnDialog = null;
	public void showDialog() {

		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.tip));
			LayoutInflater warnDialog_inflater = getLayoutInflater();
			warnDialog.setMessage(getString(R.string.get_anhong_info));

			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {

					warnDialog.dismiss();
				}
			});
		}

		warnDialog.show();
	}


	private void initview() {
		listView = (ListView) findViewById(R.id.anhongList);
		freshDevice = (TextView) findViewById(R.id.backTxt);
		addDevice = (TextView) findViewById(R.id.addDevice);
		freshDevice.setOnClickListener(this);
		addDevice.setOnClickListener(this);
		adapter = new AnHongAdapter(activity, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final SecurityAlert alert = new SecurityAlert(activity);
				alert.setTitle(R.mipmap.info_systemset, getString(R.string.anhonginfo)+"ID:0000"
						+ (list.get(position).getId() + 1));
				alert.setText(list.get(position).getMsgName() + "",
						list.get(position).getMsgInfo());

				alert.setHint(getString(R.string.input_name1), getString(R.string.tip_info));
				alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
				alert.setOnSureListener(new SecurityAlert.OnSureListener() {

					@Override
					public void onSure(String editName, String editValue) {
						String fileName = editName;
						list.get(position).setMsgName(editName);
						list.get(position).setMsgInfo(editValue);
						new UpDataTask().execute(editName,editValue,list.get(position).getId()+"");
						adapter.notifyDataSetChanged();
						alert.dismiss();
					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.addDevice:
			list.clear();

			new DataTask().execute();
			break;
		default:
			break;
		}

	}

	public void back() {

		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}
			class DataTask extends AsyncTask<String, Integer, List<AnHong>> {

				@Override
				protected void onPreExecute() {
					list.clear();
					showDialog();
				}

				@Override
				protected List<AnHong> doInBackground(String... params) {
					List<AnHong> result = null;
					try {
						String resultParam = SecurityServiceUtils.getSecuritys(deviceID);
						result= (List<AnHong>) JSONHelper.parseCollection(resultParam, List.class, AnHong.class);
					} catch (Exception e) {
					}
					return result;
				}

				@Override
				protected void onPostExecute(List<AnHong> result) {
					if((warnDialog != null) && warnDialog.isShowing()) {
						warnDialog.dismiss();
					}
					if (result == null) {
						TipAlert alert = new TipAlert(activity,
								getString(R.string.tip), getString(R.string.getdatefail));
						alert.show();
					} else {
						list.addAll(result);
						adapter = new AnHongAdapter(activity, list);
						listView.setAdapter(adapter);
					}
				}
			}

			class UpDataTask extends AsyncTask<String, Integer, String> {
				@Override
				protected void onPreExecute() {
				}

				@Override
				protected String doInBackground(String... params) {
					String result = "";
					try {
						result = SecurityServiceUtils.updateSecurity(params[0],params[1],Integer.parseInt(params[2]),deviceID);
					} catch (Exception e) {
					}
					return result;
				}
				@Override
				protected void onPostExecute(String result) {

						TipAlert alert = new TipAlert(activity,
								getString(R.string.tip), result);
						alert.show();
				}
			}
}