package com.zunder.smart.activity.backup;

import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.RoomServiceUtils;
import com.zunder.smart.webservice.DeviceServiceUtils;
import com.zunder.smart.webservice.GateWayServiceUtils;
import com.zunder.smart.webservice.ModeListServiceUtils;
import com.zunder.smart.webservice.ModeServiceUtils;
import com.zunder.smart.webservice.RedFraServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DataDownActivity extends Dialog implements OnClickListener {
	TextView gateWayName, modeName, modeListName, arceName, deviceName,
			redFraName;
	TextView gateWayValue, modeValue, modeListValue, arceValue, deviceValue,
			redFraValue;
	TextView title;
	ProgressBar gateWayPro, modePro, modeListPro, arcePro, devicePro,
			redFraPro;

	String projectKey = "";

	private Activity context;

	public DataDownActivity(Activity context, String projectKey) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_data);

		this.projectKey = projectKey;
		title = (TextView) findViewById(R.id.title);

		title.setText(context.getString(R.string.datadown));

		// group
		gateWayName = (TextView) findViewById(R.id.groupName);
		gateWayValue = (TextView) findViewById(R.id.groupValue);
		gateWayPro = (ProgressBar) findViewById(R.id.groupPro);

		// 网关
		modeName = (TextView) findViewById(R.id.cameraName);
		modeValue = (TextView) findViewById(R.id.cameraValue);
		modePro = (ProgressBar) findViewById(R.id.cameraPro);

		// 网关参数
		modeListName = (TextView) findViewById(R.id.cameraParamName);
		modeListValue = (TextView) findViewById(R.id.cameraParamValue);
		modeListPro = (ProgressBar) findViewById(R.id.cameraParamPro);

		// 预设�?
		arceName = (TextView) findViewById(R.id.cameraPointName);
		arceValue = (TextView) findViewById(R.id.cameraPointValue);
		arcePro = (ProgressBar) findViewById(R.id.cameraPointPro);

		// 设备
		deviceName = (TextView) findViewById(R.id.deviceName);
		deviceValue = (TextView) findViewById(R.id.deviceValue);
		devicePro = (ProgressBar) findViewById(R.id.devicePro);

		redFraValue = (TextView) findViewById(R.id.redFraValue);
		redFraPro = (ProgressBar) findViewById(R.id.redFraPro);

		new DataTask().execute();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:

				dismiss();
				sureListener.onCancle();
				break;
			case 1:
				if (exite()) {

					MainActivity.getInstance().updateFresh();

					dismiss();

					sureListener.onSure();
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(R.mipmap.info_systemset);
			builder.setTitle(context.getString(R.string.downing));
			builder.setPositiveButton(context.getString(R.string.sure),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dismiss();
						}
					});
			builder.setNegativeButton(context.getString(R.string.cancle), null);
			builder.show();

		}
		return false;
	}

	class DataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			hideProgressDialog();

			new GateWayDataTask().execute();

			new ArceDataTask().execute();

			new DeviceDataTask().execute();

			new ModeDataTask().execute();

			new ModeListDataTask().execute();

			new RedInfraDataTask().execute();
		}
	}

	class GateWayDataTask extends AsyncTask<String, Integer, List<GateWay>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<GateWay> doInBackground(String... params) {
			List<GateWay> result = null;
			try {

				String resultParam = GateWayServiceUtils
						.getGateWays(projectKey);
				result = (List<GateWay>) JSONHelper.parseCollection(
						resultParam, List.class, GateWay.class);
				gateWayPro.setMax(result.size() == 0 ? 1 : result.size());
			} catch (Exception e) {
				gateWayPro.setMax(1);
				System.out.println(e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<GateWay> result) {
			Sqlite().deleteSyncData("t_gateway");
			if (result == null || result.size() == 0) {
				publishProgress(1);
			} else {
				for (int i = 0; i < result.size(); i++) {
					Sqlite().insertGateWay(result.get(i));
					publishProgress(i + 1);
				}

			}

			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			gateWayPro.setProgress(values[0]);
			gateWayValue.setText((values[0] * 100 / gateWayPro.getMax()) + "%");
		}
	}

	class ModeDataTask extends AsyncTask<String, Integer, List<Mode>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<Mode> doInBackground(String... params) {
			List<Mode> result = null;
			try {

				String resultParam = ModeServiceUtils.getModes(projectKey);
				result = (List<Mode>) JSONHelper.parseCollection(resultParam,
						List.class, Mode.class);
				modePro.setMax(result.size() == 0 ? 1 : result.size());
			} catch (Exception e) {
				modePro.setMax(1);
				System.out.println(e.toString());
			}

			return result;
		}

		@Override
		protected void onPostExecute(List<Mode> result) {
			Sqlite().deleteSyncData("t_mode");
			if (result == null || result.size() == 0) {
				publishProgress(1);
			} else {
				for (int i = 0; i < result.size(); i++) {
					Sqlite().insertMode(result.get(i));
					publishProgress(i + 1);
				}

			}

			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			modePro.setProgress(values[0]);
			modeValue.setText((values[0] * 100 / modePro.getMax()) + "%");
		}
	}

	class ModeListDataTask extends AsyncTask<String, Integer, List<ModeList>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<ModeList> doInBackground(String... params) {
			List<ModeList> result = null;
			try {

				String resultParam = ModeListServiceUtils
						.getModeLists(projectKey);
				result = (List<ModeList>) JSONHelper.parseCollection(
						resultParam, List.class, ModeList.class);
				modeListPro.setMax(result.size() == 0 ? 1 : result.size());
			} catch (Exception e) {
				modeListPro.setMax(1);
				System.out.println(e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<ModeList> result) {
			Sqlite().deleteSyncData("t_mode_list");
			if (result == null || result.size() == 0) {
				publishProgress(1);
			} else {
				for (int i = 0; i < result.size(); i++) {
					Sqlite().insertModeList(result.get(i));
					publishProgress(i + 1);
				}

			}
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			modeListPro.setProgress(values[0]);
			modeListValue.setText((values[0] * 100 / modeListPro.getMax())
					+ "%");
		}
	}

	class ArceDataTask extends AsyncTask<String, Integer, List<Room>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<Room> doInBackground(String... params) {
			List<Room> result = null;
			try {

				String resultParam = RoomServiceUtils.getRooms(projectKey);
				result = (List<Room>) JSONHelper.parseCollection(resultParam,
						List.class, Room.class);
				arcePro.setMax(result.size() == 0 ? 1 : result.size());
			} catch (Exception e) {
				arcePro.setMax(1);
				System.out.println(e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Room> result) {
			Sqlite().deleteSyncData("t_room");
			if (result == null || result.size() == 0) {
				publishProgress(1);
			} else {
				for (int i = 0; i < result.size(); i++) {
					Sqlite().insertRoom(result.get(i));
					publishProgress(i + 1);
				}

			}
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			arcePro.setProgress(values[0]);
			arceValue.setText((values[0] * 100 / arcePro.getMax()) + "%");
		}
	}

	class DeviceDataTask extends AsyncTask<String, Integer, List<Device>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<Device> doInBackground(String... params) {
			List<Device> result = null;
			try {

				String resultParam = DeviceServiceUtils.getDevices(projectKey);
				result = (List<Device>) JSONHelper.parseCollection(resultParam,
						List.class, Device.class);
				devicePro.setMax(result.size() == 0 ? 1 : result.size());
			} catch (Exception e) {
				devicePro.setMax(1);
				System.out.println(e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Device> result) {
			Sqlite().deleteSyncData("t_device");
			if (result == null || result.size() == 0) {
				publishProgress(1);
			} else {
				for (int i = 0; i < result.size(); i++) {
					Sqlite().insertDevice(result.get(i));
					publishProgress(i + 1);
				}

			}
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			devicePro.setProgress(values[0]);
			deviceValue.setText((values[0] * 100 / devicePro.getMax()) + "%");
		}
	}

	class RedInfraDataTask extends AsyncTask<String, Integer, List<RedInfra>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<RedInfra> doInBackground(String... params) {
			List<RedInfra> result = null;
			try {

				String resultParam = RedFraServiceUtils.getRedFras(projectKey);
				result = (List<RedInfra>) JSONHelper.parseCollection(
						resultParam, List.class, RedInfra.class);
				redFraPro.setMax(result.size() == 0 ? 1 : result.size());
			} catch (Exception e) {
				redFraPro.setMax(1);
				System.out.println(e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<RedInfra> result) {
			Sqlite().deleteSyncData("t_infrared");
			if (result == null || result.size() == 0) {
				publishProgress(1);
			} else {
				for (int i = 0; i < result.size(); i++) {
					Sqlite().insertInfrad(result.get(i));
					publishProgress(i + 1);
				}

			}
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			redFraPro.setProgress(values[0]);
			redFraValue.setText((values[0] * 100 / redFraPro.getMax()) + "%");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}

	private OnSureListener sureListener;

	public void setSureListener(OnSureListener sureListener) {
		this.sureListener = sureListener;
	}

	IWidgetDAO Sqlite() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	public void showProgressDialog() {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(context,
					context.getString(R.string.tip),
					context.getString(R.string.loading), true, false);
		}

		progressDialog.show();

	}

	ProgressDialog progressDialog = null;

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public boolean exite() {

		if (gateWayPro.getProgress() != gateWayPro.getMax()) {
			return false;
		} else if (modePro.getProgress() != modePro.getMax()) {
			return false;
		} else if (modeListPro.getProgress() != modeListPro.getMax()) {
			return false;
		} else if (arcePro.getProgress() != arcePro.getMax()) {
			return false;
		} else if (devicePro.getProgress() != devicePro.getMax()) {
			return false;
		} else if (redFraPro.getProgress() != redFraPro.getMax()) {
			return false;
		}
		return true;
	}
}