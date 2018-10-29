package com.zunder.smart.activity.sub.set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.smart.R;
import com.zunder.smart.adapter.StudyAdapter;
import com.zunder.smart.dao.impl.factory.StudyFactory;
import com.zunder.smart.dialog.InfraNameDialog;
import com.zunder.smart.dialog.InfraVersionDialog;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.ElectricListeener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.InfraCode;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.InfraServiceUtils;

import java.util.List;

public class StudyActivity extends Activity implements OnClickListener,ElectricListeener{
	StudyAdapter adapter;
	List<ItemName> listItem;
	private TextView backTxt,editeTxt;
	GridView gridView;
	private Activity activity;
	private static Device device;
	private Button sureBtn;
	private int index=0;
	TimeAlert alertTime;
	public int infraID = 0;
	public int versionID = 0;;
	public String sendStr = "00000";
	public static void startActivity(Activity activity, Device _device) {
		device=_device;
		Intent intent = new Intent(activity, StudyActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		activity = this;
		TcpSender.setElectricListeener(this);
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt= (TextView) findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		sureBtn=(Button)findViewById(R.id.sureBtn);
		sureBtn.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.productsGrid);
		if(device.getDeviceTypeKey()==5){
			editeTxt.setVisibility(View.VISIBLE);
		}
		listItem= StudyFactory.getInstance().getAll(device.getDeviceTypeKey());
		adapter=new StudyAdapter(activity,listItem);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				adapter.changeSelected(position);
				if(device!=null) {
					index=position;
					String cmd = "*C0019FA05" + device.getDeviceID() + "E0" + AppTools.toHex(position) + "0000";
					SendThread send = SendThread.getInstance(cmd);
					new Thread(send).start();
				}
				if(alertTime==null){
					alertTime = new TimeAlert(activity);
					alertTime.setSureListener(new TimeAlert.OnSureListener() {

						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
							alertTime.diss();
						}
					});
				}
				alertTime.show(getString(R.string.study_key));
			}
		});
		alertTime = new TimeAlert(activity);
		alertTime.setSureListener(new TimeAlert.OnSureListener() {

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
				alertTime.diss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			finish();
			break;
			case R.id.sureBtn:

				String cmd = "*C0019FA" + device.getProductsCode() + device.getDeviceID() + "A0" + AppTools.toHex(index) +"0000";
				SendThread send = SendThread.getInstance(cmd);
				new Thread(send).start();
				break;
			case R.id.editeTxt:
				InfraNameDialog buDialog = new InfraNameDialog(
						activity, getString(R.string.product1), 47, 80);
				buDialog.setInfraInterFace(new InfraNameDialog.InfraInterFace() {

					@Override
					public void setInfraID(
							String infraName, int _infraID) {

						infraID = _infraID;
						InfraVersionDialog versionDialog = new InfraVersionDialog(
								activity, getString(R.string.type), infraID);
						versionDialog
								.setInfraInterFace(new InfraVersionDialog.InfraVersionInterFace() {

									@Override
									public void setInfraID(
											String infraName,
											int _infraID) {
										new DataTask()
												.execute(_infraID
														+ "");
									}
								});
						versionDialog.show();
					}
				});
				buDialog.show();
				break;
		default:
			break;
		}
	}
	private  Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try { if (msg.what == 1) {
					String cmd = msg.obj.toString();
					//*C0008(ACT)(TYPE)(ID)(MID)(MID)(mem)(LenH)(LenL)(St)
					String state=cmd.substring(22,24);
					String MsgText="红外学习码不完整";
					if(state.equals("00")){ //成功
						String value1= Integer.parseInt(cmd.substring(18,22),16)+"";
						MsgText=activity.getString(R.string.studySuccess)+value1+activity.getString(R.string.rf_byte);
					}
					Toast.makeText(	activity,MsgText,Toast.LENGTH_SHORT).show();
					if (alertTime.isShow()) {
						alertTime.diss();
					}
				} else if(msg.what==2){
				warnDialog.setMessage(msg.obj.toString());
				}else if(msg.what==3) {
					String getCode = (String) msg.obj;
					int index = Integer.valueOf(getCode.substring(6, 8), 16);
					int len = Integer.valueOf(getCode.substring(8, 10), 16);
					int number = Integer.valueOf(getCode.substring(18, 20), 16);

					if (index == 240) { //start
					} else if (index == 241) { //end
						searchflag=false;
						if(warnDialog.isShowing()){
							warnDialog.setMessage(activity.getString(R.string.start_down));
							warnDialog.dismiss();
							Toast.makeText(activity, activity.getString(R.string.downsuccess), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};

	@Override
	public void setElectric(String cmd) {
		// TODO Auto-generated method stub
		String cmdType = cmd.substring(0, 2);
		//*C0008(ACT)(TYPE)(ID)(MID)(MID)
		if (cmdType.equals("*C")) {
			if(cmd.substring(10,16).equals(device.getDeviceID())) {
				String cmdNum=cmd.substring(4,6);
				String act = cmd.substring(6, 8);
				if((cmdNum.equals("08"))&&(act.equals("E0"))) {
					Message message = handler.obtainMessage();
					message.what = 1;
					message.obj = cmd;
					handler.sendMessage(message);
				}
			}
		}else if (cmdType.equals("*I")) {
			Message msg = handler.obtainMessage();
			msg.what = 3;
			msg.obj = cmd;
			handler.sendMessage(msg);
		}
	}
	class DataTask extends AsyncTask<String, Integer, List<InfraCode>> {
		@Override
		protected void onPreExecute() {
			showDialog();
		}

		@Override
		protected List<InfraCode> doInBackground(String... params) {
			List<InfraCode> result = null;
			try {
				result = (List<InfraCode>) JSONHelper.parseCollection(
						InfraServiceUtils.GetInfraCodes(1, 10,
								Integer.parseInt(params[0])), List.class,
						InfraCode.class);
				try {
					if (result != null && result.size() > 0) {
						for (int i=0; i<result.size();i++) {
							InfraCode infraCode=result.get(i);
							sendDownCode(Integer.parseInt(infraCode.getInfraKey()),
									infraCode.getInfraCode());
							Message message = handler.obtainMessage();
							message.what = 2;
							message.obj =activity.getString(R.string.dateDown)+ (i+1)*100/result.size()+"%";
							handler.sendMessage(message);
						}

						String cmd = "*I00" + toHex1602((128) + "") + "F10105"
								+ device.getDeviceID() + "0000000000000000000"; //F1 end  F0 start
						SendThread send = SendThread.getInstance(cmd);
						new Thread(send).start();
						Message message = handler.obtainMessage();
						message.what = 2;
						message.obj =activity.getString(R.string.save_data);
						handler.sendMessage(message);
						startTime();
					} else {
						if(	warnDialog.isShowing()) {
							warnDialog.dismiss();
						}
						Toast.makeText(activity, activity.getString(R.string.no_type_data), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<InfraCode> result) {

		}

	}
	public void sendDownCode(int index, String codeStr) {
		boolean Issend = false;
		int i=0;
		while(true) {
			if (codeStr.length() > 26) {
				Issend = true;
				String cmd = "*I00" + toHex1602((128 + index) + "") + "0" + i
						+ "0D" + codeStr.substring(0, 26);
				SendThread send = SendThread.getInstance(cmd);
				new Thread(send).start();
				codeStr = codeStr.substring(26);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				String Len = (codeStr.length() / 2) + "";
				String IsZero = "00000000000000000000000000";
				String cmd = "*I00" + toHex1602((128 + index) + "") + "0" + i
						+ toHex1602(Len) + codeStr
						+ IsZero.substring(codeStr.length());
				SendThread send = SendThread.getInstance(cmd);
				new Thread(send).start();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			i++;
		}
		if (Issend) {
			String cmd = "*I00" + toHex1602((128 + index) + "") + "FF0105"
					+device.getDeviceID() + "000000000000000000";
			SendThread send = SendThread.getInstance(cmd);
			new Thread(send).start();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static String toHex1602(String string) {
		int a = Integer.parseInt(string);
		String ss = Integer.toHexString(a).toUpperCase();
		if (ss.length() < 2) {
			ss = "0" + ss;
		}
		return ss;
	}

	static WarnDialog warnDialog = null;
	public void showDialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity,getString(R.string.tip));
			warnDialog.setMessage(getString(R.string.start_down_red));
			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					warnDialog.dismiss();
				}
			});
		}
		warnDialog.show();
	}
	private static boolean searchflag = false;
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

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
