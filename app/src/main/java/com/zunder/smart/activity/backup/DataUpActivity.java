package com.zunder.smart.activity.backup;

import java.util.List;

import org.json.JSONObject;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.webservice.RoomServiceUtils;
import com.zunder.smart.webservice.DeviceServiceUtils;
import com.zunder.smart.webservice.GateWayServiceUtils;
import com.zunder.smart.webservice.ModeListServiceUtils;
import com.zunder.smart.webservice.ModeServiceUtils;
import com.zunder.smart.webservice.RedFraServiceUtils;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;

import android.annotation.SuppressLint;
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

@SuppressLint("Instantiatable")
public class DataUpActivity extends Dialog implements OnClickListener {
	TextView gateWayName, modeName, modeListName, arceName, deviceName,
			redFraName;
	TextView gateWayValue, modeValue, modeListValue, arceValue, deviceValue,
			redFraValue;
	TextView title;
	ProgressBar gateWayPro, modePro, modeListPro, arcePro, devicePro,
			redFraPro;

	String projectKey = "";

	private Activity context;
	public DataUpActivity(Activity context, String projectKey) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_data);

		this.projectKey = projectKey;
		title = (TextView) findViewById(R.id.title);
		title.setText(context.getString(R.string.data_up));
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
					dismiss();
					sureListener.onSure();
				}
				break;

			default:
				break;
			}
		};
	};
	private OnSureListener sureListener;

	public void setSureListener(OnSureListener sureListener) {
		this.sureListener = sureListener;
	}

	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(R.mipmap.info_systemset);
			builder.setTitle(context.getString(R.string.uping));
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
			int gateWaySize = GateWayFactory.getInstance().getAll().size();
			gateWayPro.setMax(gateWaySize == 0 ? 1 : gateWaySize);
			new GateWayDataTask().execute();

			int arceSize = RoomFactory.getInstance().getAll().size();
			arcePro.setMax(arceSize == 0 ? 1 : arceSize);
			new RoomDataTask().execute();

			int deviceSize = DeviceFactory.getInstance().getAll().size();
			devicePro.setMax(deviceSize == 0 ? 1 : deviceSize);
			new DeviceDataTask().execute();

			int modeSize = ModeFactory.getInstance().getAll().size();
			modePro.setMax(modeSize == 0 ? 1 : modeSize);
			new ModeDataTask().execute();
			//
			int mpdeListSize = ModeListFactory.getInstance().getAll().size();
			modeListPro.setMax(mpdeListSize == 0 ? 1 : mpdeListSize);
			new ModeListDataTask().execute();

			int redFraSize = RedInfraFactory.getInstance().getAll().size();
			redFraPro.setMax(redFraSize == 0 ? 1 : redFraSize);
			new RedInfraDataTask().execute();
		}
	}

	class GateWayDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			List<GateWay> list = GateWayFactory.getInstance().getAll();

			try {
				if (list.size() == 0) {
					publishProgress(1);
					return result;
				}
				for (int i = 0; i < list.size(); i++) {
					GateWay gateway = list.get(i);
					int id = gateway.getId();
					String gatewayName = gateway.getGatewayName();
					String gatewayID = gateway.getGatewayID();
					String userName = gateway.getUserName();
					String userPassWprd = gateway.getUserPassWord();
					int gatewayTypeID = gateway.getTypeId();
					String state = gateway.getGateWayPoint();
					int isCurrent = gateway.getIsCurrent();
					int seqencing = gateway.getSeqencing();
					result = GateWayServiceUtils.insertGateway(id,gatewayName,gatewayID,userName,userPassWprd,gatewayTypeID,state,isCurrent,seqencing,projectKey);
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						publishProgress(i + 1);
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				handler.sendEmptyMessage(-1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
		}
		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			gateWayPro.setProgress(values[0]);
			gateWayValue.setText((values[0] * 100 / gateWayPro.getMax()) + "%");
		}
	}

	class ModeDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			List<Mode> list = ModeFactory.getInstance().getAll();

			try {
				if (list.size() == 0) {
					publishProgress(1);
					return result;
				}
				for (int i = 0; i < list.size(); i++) {
					Mode mode = list.get(i);
					int id = mode.getId();
					String modeImage =  mode.getModeImage();
					String modeName = mode.getModeName();
					int modeCode = mode.getModeCode();
					String modeType = mode.getModeType();
					int modeLoop = mode.getModeLoop();
					int seqencing = mode.getSeqencing();
					int isShow=mode.getIsShow();
					String modeNickName=mode.getModeNickName();
					String startTime = mode.getStartTime();
					String endTime = mode.getEndTime();
					result = ModeServiceUtils.insertMode(id,modeName,modeImage,modeCode,modeType,modeLoop,seqencing,startTime,endTime,modeNickName,isShow,projectKey);
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						publishProgress(i + 1);
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				handler.sendEmptyMessage(-1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			modePro.setProgress(values[0]);
			modeValue.setText((values[0] * 100 / modePro.getMax()) + "%");
		}
	}

	class ModeListDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			List<ModeList> list = ModeListFactory.getInstance().getAll();
			try {
				if (list.size() == 0) {
					publishProgress(1);
					return result;
				}
				for (int i = 0; i < list.size(); i++) {
					ModeList modeList = list.get(i);
					int id = modeList.getId();
					int deviceId = modeList.getDeviceId();
					int modeId = modeList.getModeId();
					int seqencing = modeList.getSeqencing();
					String mode_action = modeList.getModeAction();
					String mode_function = modeList.getModeFunction();
					String mode_time = modeList.getModeTime();
					String mode_delayed = modeList.getModeDelayed();
					String mode_period = modeList.getModePeriod();
					String begin = modeList.getBeginMonth();
					String end=modeList.getEndMonth();
					result = ModeListServiceUtils.insertModeList(id,deviceId,modeId,mode_action,mode_function,mode_time,mode_delayed,mode_period,begin,end,seqencing,projectKey);
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						publishProgress(i + 1);
					}
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(-1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
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

	class RoomDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			List<Room> list = RoomFactory.getInstance().getAll();
			try {
				if (list.size() == 0) {
					publishProgress(1);
					return result;
				}
				for (int i = 0; i < list.size(); i++) {
					Room room = list.get(i);
					int id = room.getId();
					String arceName = room.getRoomName();
					String arceImage = room.getRoomImage();
					int isShow = room.getIsShow();
					int seqencing = room.getSeqencing();

					result = RoomServiceUtils.insertRoom(id, arceName,
							arceImage, isShow,seqencing, projectKey);
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						publishProgress(i + 1);
					}

				}
			} catch (Exception e) {
				handler.sendEmptyMessage(-1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			arcePro.setProgress(values[0]);
			arceValue.setText((values[0] * 100 / arcePro.getMax()) + "%");
		}
	}

	class DeviceDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			List<Device> list = DeviceFactory.getInstance().getAll();
			try {
				if (list.size() == 0) {
					publishProgress(1);
					return result;
				}
				for (int i = 0; i < list.size(); i++) {
					Device device = list.get(i);
					int id = device.getId();
					String deviceImage =device.getDeviceImage();
					String deviceName = device.getDeviceName();
					String deviceID = device.getDeviceID();
					String deviceNickName = device.getDeviceNickName();
					String creationTime = device.getCreationTime();
					int seqencing = device.getSeqencing();
					int device_onLine = device.getDeviceOnLine();
					String deviceTimer = device.getDeviceTimer();
					String deviceIO = device.getDeviceIO();
					String deviceOrdered = device.getDeviceOrdered();
					String startTime = device.getStartTime();
					String endTime = device.getEndTime();
					int deviceTypeKey = device.getDeviceTypeKey();
					int deviceProductKey = device.getProductsKey();
					int roomId = device.getRoomId();
					String data1=device.getData1();
					result = DeviceServiceUtils.insertDevice(id,deviceName,deviceImage,deviceID,deviceNickName,seqencing,device_onLine,deviceTimer,deviceIO,deviceOrdered,startTime,endTime,deviceTypeKey,deviceProductKey,roomId,data1,projectKey);
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						publishProgress(i + 1);
					} else {
						i--;
					}
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(-1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			handler.sendEmptyMessage(1);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			devicePro.setProgress(values[0]);
			deviceValue.setText((values[0] * 100 / devicePro.getMax()) + "%");
		}
	}

	class RedInfraDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			List<RedInfra> list = RedInfraFactory.getInstance().getAll();
			try {
				if (list.size() == 0) {
					publishProgress(1);
					return result;
				}
				for (int i = 0; i < list.size(); i++) {
					RedInfra redFra = list.get(i);
					int id = redFra.getId();
					String infrared_image = "";
					String infrared_ID = redFra.getDeviceId();
					int fatherId=redFra.getFatherId();
					int infrared_index = redFra.getInfraredIndex();
					int infrared_brandId=redFra.getInfraredBrandId();
					String infrared_brand = redFra.getInfraredBrandName();
					int infrared_versionId = redFra.getInfraredVersionId();
					String infrared_version = redFra.getInfraredVersionName();
					int infrared_key = redFra.getInfraredKey();
					String infrared_name = redFra.getInfraredName();
					String infrared_code = redFra.getInfraredCode();
					int buttonId=redFra.getInfraredButtonId();
					int infrared_studyType = redFra.getInfraredStudyType();
					int seqencing = redFra.getSeqencing();
					result = RedFraServiceUtils.insertRedFra(id,infrared_name,infrared_image,infrared_ID,fatherId,infrared_index,infrared_brandId,infrared_brand,infrared_versionId,infrared_version,infrared_key,infrared_code,buttonId,infrared_studyType,seqencing,projectKey);
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						publishProgress(i + 1);
					} else {
						i--;
					}
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(-1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
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

	public IWidgetDAO Sqlite() {
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