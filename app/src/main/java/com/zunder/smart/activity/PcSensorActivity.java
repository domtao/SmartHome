/**
 * 
 */
package com.zunder.smart.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.PcsensorFactory;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.DownListener;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.PcSensorInfo;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.utils.ProgressDialogUtils;
import com.zunder.smart.webservice.WebServiceUtils;

import java.util.List;

/**
 * @author Administrator
 * 
 */
public class PcSensorActivity extends Activity implements DownListener,
		OnClickListener {
	PcSensorActivity activity;
	TextView titleTxt;
	private int arceIndex=7;
	TextView backTxt,editeTxt,msgTxt;
	private String primaryKey = "";
	TextView tempText,humText,pmText,jqText,lightText;
	RadioButton outdoor,indoor,sittingRoom,restaurantRoom,bedroomRoom,masterRoom,parentRoom,childRoom;
	RadioGroup groupOne,groupTwo;
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, PcSensorActivity.class);
		activity.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_pcsensor);
		activity = this;
		getDeviceID();
		initView();
		primaryKey=getDeviceID();
//		new DataWeatherTask("深圳").execute();
		if(primaryKey.equals("")){
			ToastUtils.ShowError(activity,"你当前没有设置云知声为主网关",Toast.LENGTH_SHORT,true);
		}else {
			ReceiverBroadcast.setDownListener(this);
			String result = ISocketCode.setGetAnHong("PcSensor",
					primaryKey);
			MainActivity.getInstance().sendCode(result);
			showDialog();
		}
	}
	public String getDeviceID(){
		List<GateWay> list= GateWayService.list;
		for (int i=0;i<list.size();i++){
			if(list.get(i).getIsCurrent()==1&&list.get(i).getTypeId()==2){
				primaryKey=list.get(i).getGatewayID();
				break;
			}
		}
		return primaryKey;
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	private void initView() {
		msgTxt=(TextView)findViewById(R.id.msgTxt);
		editeTxt=(TextView)findViewById(R.id.editeTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		tempText=(TextView)findViewById(R.id.tempText);
		humText=(TextView)findViewById(R.id.humText);
		pmText=(TextView)findViewById(R.id.pmText);
		jqText=(TextView)findViewById(R.id.jqText);
		lightText=(TextView)findViewById(R.id.lightText);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		groupOne=(RadioGroup)findViewById(R.id.groupOne);
		groupTwo=(RadioGroup)findViewById(R.id.groupTwo);
		outdoor=(RadioButton)findViewById(R.id.outdoor);
		indoor=(RadioButton)findViewById(R.id.indoor);
		sittingRoom=(RadioButton)findViewById(R.id.sittingRoom);
		restaurantRoom=(RadioButton)findViewById(R.id.restaurantRoom);
		bedroomRoom=(RadioButton)findViewById(R.id.bedroomRoom);
		masterRoom=(RadioButton)findViewById(R.id.masterRoom);
		parentRoom=(RadioButton)findViewById(R.id.parentRoom);
		childRoom=(RadioButton)findViewById(R.id.childRoom);
		outdoor.setOnClickListener(this);
		indoor.setOnClickListener(this);
		sittingRoom.setOnClickListener(this);
		restaurantRoom.setOnClickListener(this);
		bedroomRoom.setOnClickListener(this);
		masterRoom.setOnClickListener(this);
		parentRoom.setOnClickListener(this);
		childRoom.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
			case R.id.editeTxt: {
				ProgressDialogUtils.showProgressDialog(this, "获取省份");
				new DataProvinceTask().execute();
				break;
			}
			case R.id.indoor:{
				arceIndex=0;
				PcSensorInfo pcSensorInfo= PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("室内传感信息");
				groupTwo.clearCheck();
				break;
			}
			case R.id.sittingRoom: {
				arceIndex = 1;
				PcSensorInfo pcSensorInfo = PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("客厅传感信息");
				groupTwo.clearCheck();
				break;
			}
			case R.id.restaurantRoom:{
				arceIndex=2;
				PcSensorInfo pcSensorInfo=PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("餐厅传感信息");
				groupTwo.clearCheck();
				break;
			}
			case R.id.bedroomRoom: {
				arceIndex = 3;
				PcSensorInfo pcSensorInfo=PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("卧室传感信息");
				groupOne.clearCheck();
				break;
			}
			case R.id.parentRoom: {
				arceIndex = 4;
				PcSensorInfo pcSensorInfo=PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("父母房传感信息");
				groupOne.clearCheck();
				break;
			}
			case R.id.masterRoom: {
				arceIndex = 5;
				PcSensorInfo pcSensorInfo=PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("主人房传感信息");
				groupOne.clearCheck();
				break;
			}
			case R.id.childRoom: {
				arceIndex = 6;
				PcSensorInfo pcSensorInfo=PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				titleTxt.setText("儿童房传感信息");
				groupOne.clearCheck();
				break;
			}
			case R.id.outdoor: {
				arceIndex = 7;
				PcSensorInfo pcSensorInfo = PcsensorFactory.getInstance().getById(arceIndex);
				setText(pcSensorInfo);
				groupTwo.clearCheck();
				titleTxt.setText("室外传感信息");
				break;
			}
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}
	WarnDialog warnDialog = null;
	public void showDialog(){

		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity,getString(R.string.tip));
			warnDialog.setMessage("正在获取传感数据 5");
			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					searchflag = false;
					warnDialog.dismiss();
				}
			});
		}

		warnDialog.show();
		startTime();
	}
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
				case -2:
					int index=Integer.parseInt(msg.obj.toString());
					warnDialog.setMessage("正在获取传感数据 "+(5-index));
					break;
				case 1:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					String result=msg.obj.toString();
//					 msgTxt.append(result+"\n");
					 if(result.startsWith("insideTemp")){
					 	String[] Str=result.replace("insideTemp","").trim().split(",");
						 PcsensorFactory.getInstance().updateInsideTemp(Str);
					 }
					 else if(result.startsWith("Humidity")){
						 String[] Str=result.replace("Humidity","").trim().split(",");
						 PcsensorFactory.getInstance().updateHumidity(Str);
					 }
					 else if(result.startsWith("pm25")){
						 String[] Str=result.replace("pm25","").trim().split(",");
						 PcsensorFactory.getInstance().updatePm25(Str);
					 }
					 else if(result.startsWith("formaldehyde")){
						 String[] Str=result.replace("formaldehyde","").trim().split(",");
						 PcsensorFactory.getInstance().updateFormaldehyde(Str);

					 }
					 else if(result.startsWith("illumination")){
						 String[] Str=result.replace("illumination","").trim().split(",");
						 PcsensorFactory.getInstance().updateIllumination(Str);

					 }
					PcSensorInfo pcSensorInfo=PcsensorFactory.getInstance().getById(arceIndex);
					setText(pcSensorInfo);

					break;
				case 2: {
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount = 0;
						warnDialog.dismiss();
					}
				}
				break;
				default:
					break;
			}
		}
	};
	private boolean searchflag = false;
	private int startCount = 0;
	private void startTime() {
		startCount=0;
		searchflag=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >= 5) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}else{
							Message message = handler.obtainMessage();
							message.what = -2;
							message.obj = startCount;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReceiverBroadcast.setDownListener(null);
	}

	public void back() {
		finish();
	}
	@Override
	public void count(String number) {
		Message message=handler.obtainMessage();
		message.what=1;
		message.obj=number;
		handler.dispatchMessage(message);
	}

	class DataProvinceTask extends AsyncTask<String, Integer, List<String>> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> result = null;
			try {
			result=WebServiceUtils.getSupportProvince();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			ProgressDialogUtils.dismissProgressDialog();
			if (result != null&&result.size()>0) {
				final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,"地区列表",result,null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						ProgressDialogUtils.showProgressDialog(activity,"获取城市列表……");
						new DataCityTask(itemName).execute();
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();

			}else{
				ToastUtils.ShowError(activity,"获取数据失败",Toast.LENGTH_SHORT,true);
			}
		}

	}
	//获取城市
	class DataCityTask extends AsyncTask<String, Integer, List<String>> {
		private String provinceName;
		public DataCityTask(String _provinceName){
			this.provinceName=_provinceName;
		}
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> result = null;
			try {
				result=WebServiceUtils.getSupportCity(provinceName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(List<String> result) {
			ProgressDialogUtils.dismissProgressDialog();
			if (result != null&&result.size()>0) {
				final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,provinceName+"城市列表",result,null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						ProgressDialogUtils.showProgressDialog(activity,"获取天气信息……");
						int start=itemName.indexOf("(");
						new DataWeatherTask(itemName.substring(0,start)).execute();
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}
			else
			{
				ToastUtils.ShowError(activity,"获取数据失败",Toast.LENGTH_SHORT,true);
			}
		}
	}
	//获取天气
	class DataWeatherTask extends AsyncTask<String, Integer, List<String>> {
		private String cityName;
		public DataWeatherTask(String _cityName){
			this.cityName=_cityName;
		}
		@Override
		protected void onPreExecute() {
			ProgressDialogUtils.showProgressDialog(activity,"获取天气信息");
		}

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> result = null;
			try {
				result=WebServiceUtils.getWeatherbyCityName(cityName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			msgTxt.setText("");
			ProgressDialogUtils.dismissProgressDialog();
			if (result != null&&result.size()>0) {
				for (int i=0;i<result.size();i++){
					msgTxt.append(result.get(i)+"\n");
				}
			}else{
				ToastUtils.ShowError(activity,"获取数据失败",Toast.LENGTH_SHORT,true);
			}
		}
	}
	public void setText(PcSensorInfo pcSensorInfo){
		if(pcSensorInfo!=null){
			tempText.setText(pcSensorInfo.getInsideTemp());
			humText.setText(pcSensorInfo.getHumidity());
			pmText.setText(pcSensorInfo.getPm25());
			jqText.setText(pcSensorInfo.getFormaldehyde());
			lightText.setText(pcSensorInfo.getIllumination());
		}
	}
}