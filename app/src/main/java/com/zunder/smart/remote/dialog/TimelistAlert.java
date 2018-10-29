package com.zunder.smart.remote.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.adapter.ItemDialogAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.RadioCheckAlert;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.adapter.FileTypeAdapter;
import com.zunder.smart.remote.adapter.FileTypeDialogAdapter;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.remote.model.FileTimeList;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.remote.webservice.FileTimeListServiceUtils;
import com.zunder.smart.remote.webservice.FileTypeServiceUtils;
import com.zunder.smart.remote.webservice.IFileInfoServiceUtils;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ProgressDialogUtils;
import com.iflytek.cloud.thirdparty.V;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.RadioCheckAlert;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.model.FileTimeList;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;

import org.w3c.dom.Text;

import java.util.List;
import java.util.ResourceBundle;

//ȷ����ʾ��
public class TimelistAlert extends Dialog implements OnClickListener, TimmingListener{

	private Activity activity;
	private Button cancleBtn, sureBtn;
	Spinner actionSpinner;
	EditText startTime;
	ImageView startImg;
	int hourOfDay,minute;
	LinearLayout typeLayout;
	int FileIndex=0;
	int ControlIndex=0;
	int TimeId=0;
	int CycleIndex=0;
	int TypeId=0;
	String ControlTime="";
	String StartTime="";
	String EndTime="";
	String AssignDate="";
	Spinner typeSpinner;
	Spinner gateSpinner;
	LinearLayout gateLayout;
	FileTimeList fileTimeList=null;
	String ControlHex="";
	String ControlDevice="";
	String ControlMasterID="";
	int deviceId=0;
	TextView msg,typeMsg;
	int comeIndex=0;
	public TimelistAlert(Activity _context,FileTimeList _fileTimeList) {
		super(_context, R.style.MyDialog);
		this.activity = _context;
		this.fileTimeList=_fileTimeList;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TcpSender.setTimmingListener(TimelistAlert.this);
		setContentView(R.layout.alert_file_time_list_verify);
		actionSpinner=(Spinner) findViewById(R.id.actionSpinner);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		startTime=(EditText)findViewById(R.id.startTime);
		startImg=(ImageView)findViewById(R.id.startImage);
		typeLayout=(LinearLayout)findViewById(R.id.typeLayout);
		startImg.setOnClickListener(this);
		 gateSpinner=(Spinner) findViewById(R.id.gateSpinner);
		 gateLayout=(LinearLayout)findViewById(R.id.gateLayout);
		msg=(TextView)findViewById(R.id.msg);
		typeMsg=(TextView)findViewById(R.id.typeMsg);
		actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {

				FileType fileType= ((FileType)parent.getItemAtPosition(pos));
				FileIndex=0;
				ControlIndex=fileType.getId();

				if(ControlIndex==4){
					typeLayout.setVisibility(View.VISIBLE);
					gateLayout.setVisibility(View.GONE);

					new FileTypeTask().execute();
				}else if(ControlIndex==5){
					typeLayout.setVisibility(View.GONE);
					gateLayout.setVisibility(View.VISIBLE);

					FileTypeDialogAdapter fileTypeAdapter=new FileTypeDialogAdapter(activity, GateWayFactory.getInstance().getAllByName());
					gateSpinner.setAdapter(fileTypeAdapter);
					if(comeIndex==0){
						comeIndex++;
						return;
					}

					final RadioCheckAlert radioCheckAlert=new RadioCheckAlert(activity,"智能设备");

					radioCheckAlert.setRadioItems(DeviceFactory.getInstance().getDeviceByName(), null, -1, new RadioCheckAlert.OnRadioClickListener() {
						@Override
						public void onClick(ItemName itemName, int postion, boolean isChecked) {
							deviceId=itemName.getId();
							ControlDevice=itemName.getItemName();
						}
					});
					radioCheckAlert.setOnSureListener(new RadioCheckAlert.OnSureListener() {
						@Override
						public void onCancle() {
						}
						@Override
						public void onSure() {
							radioCheckAlert.dismiss();
							ControlHex="";
							if(deviceId==-1){
								SendCMD sendCMD = new SendCMD().getInstance();
								sendCMD.sendCMD(0, ControlDevice, null);
							}else {
								final TimeActionPopupWindow timeActionPopupWindow = new TimeActionPopupWindow(activity, DeviceFactory.getInstance().getDevicesById(deviceId));
								timeActionPopupWindow.setOnSureListener(new TimeActionPopupWindow.OnSureListener() {

									@Override
									public void onCancle() {
									}

									@Override
									public void onSure(String cmd) {
										msg.setText(cmd);
										ControlDevice = cmd;
										timeActionPopupWindow.dismiss();
									}
								});
								timeActionPopupWindow.show();
							}
						}
					});

					radioCheckAlert.show();
				}else{
					TypeId=0;
					comeIndex++;
					typeLayout.setVisibility(View.GONE);
					gateLayout.setVisibility(View.GONE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
		gateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				FileType fileType= ((FileType)parent.getItemAtPosition(pos));
				ControlMasterID=fileType.getTypeText();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
		typeSpinner=(Spinner) findViewById(R.id.typeSpinner);
		typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				TypeId=((FileType)parent.getItemAtPosition(pos)).getId();
				if(comeIndex==0){
					comeIndex++;
					return;
				}
				new DataAsyncTask().execute();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});

		new ActionTypeTask().execute();
	}
	public void setInit(int TimeId,int CycleIndex,String StartTime,String EndTime,String AssignDate){
		this.TimeId = TimeId;
		this.CycleIndex = CycleIndex;
		this.StartTime = StartTime;
		this.EndTime = EndTime;
		this.AssignDate = AssignDate;
		hourOfDay = Integer.parseInt(StartTime.split(":")[0]);
		minute = Integer.parseInt(StartTime.split(":")[1]);
		if(fileTimeList!=null){
			startTime.setText(fileTimeList.getControlTime());
			 ControlHex=fileTimeList.getControlHex();
			 ControlDevice=fileTimeList.getControlDevice();
			 ControlMasterID=fileTimeList.getControlMasterID();
			typeMsg.setText(fileTimeList.getFileName());
			msg.setText(fileTimeList.getControlDevice());

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.startImage:
				int startHour= Integer.parseInt(StartTime.split(":")[0])*24;
				int endHour= Integer.parseInt(EndTime.split(":")[0]);
				DateTimeDialog startDateChooseDialog = new DateTimeDialog(activity, 2,startHour+endHour, new DateTimeDialog.DateChooseInterface() {
					@Override
					public void getDateTime(String time, boolean longTimeChecked) {
						startTime.setText(time);
					}
				});
				startDateChooseDialog.setDateDialogTitle("时间");
				startDateChooseDialog.showDateChooseDialog();	break;
			case R.id.cancle_bt:
				dismiss();
				break;
			case R.id.sure_bt:
				ControlTime=startTime.getText().toString();
				if(AppTools.timeConvert(ControlTime)<AppTools.timeConvert(StartTime)||AppTools.timeConvert(ControlTime)>AppTools.timeConvert(EndTime)){
					ToastUtils.ShowError(activity,"超出时间范围,重新选择时间",Toast.LENGTH_SHORT,true);
					return;
				}
				TcpSender.setTimmingListener(null);
				new TimeListAddTask().execute();
				break;
		default:
			break;
		}
	}

	@Override
	public void setOnDismissListener(@Nullable OnDismissListener listener) {
		super.setOnDismissListener(listener);
		TcpSender.setTimmingListener(null);
	}

	@Override
	public void timerCode(String cmd) {
		if(ControlHex.length()==0){
			ControlHex=cmd;
		}else{
			ControlHex+="|"+cmd;
		}
	}

	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}


	private OnSureListener onSureListener;
	/**
	 * @param onSureListener
	 *           the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}

	public class TimeListAddTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo resultInfo = null;
			try {
				if(fileTimeList!=null){
					String result = FileTimeListServiceUtils.updateFileTimeList(fileTimeList.getId(),FileIndex, ControlIndex, TimeId, ControlTime, AssignDate, CycleIndex, TypeId,ControlHex,ControlDevice,ControlMasterID).replace("[", "").replace("]", "");
					resultInfo = (ResultInfo) JSONHelper.parseObject(result,
							ResultInfo.class);
				}
				else {
					String result = FileTimeListServiceUtils.insertFileTimeList(FileIndex, ControlIndex, TimeId, ControlTime, AssignDate, CycleIndex, TypeId,ControlHex,ControlDevice,ControlMasterID).replace("[", "").replace("]", "");
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
					ToastUtils.ShowSuccess(activity, result.getMsg(), Toast.LENGTH_SHORT, true);
					if(onSureListener!=null){
						dismiss();
						onSureListener.onSure();
					}
				} else {
					ToastUtils.ShowSuccess(activity, result.getMsg(), Toast.LENGTH_SHORT, true);
				}
			}
		}
	}
	public class ActionTypeTask extends AsyncTask<String, Void, List<FileType>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<FileType> doInBackground(String... params) {
			List<FileType>  list = null;
			try {
				String json = FileTimeListServiceUtils.getControlCmds(1);
				list = (List<FileType>) JSONHelper.parseCollection(json,
						List.class, FileType.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		@Override
		protected void onPostExecute(List<FileType> result) {

			if (result != null&&result.size()>0) {
				if(result.size()>0) {
					FileTypeDialogAdapter fileTypeAdapter = new FileTypeDialogAdapter(activity, result);
					actionSpinner.setAdapter(fileTypeAdapter);
					if(fileTimeList!=null){
						actionSpinner.setSelection(fileTimeList.getControlIndex()-1);
					}
				}
			}
		}
	}
	public class FileTypeTask extends AsyncTask<String, Void, List<FileType>> {

		@Override
		protected void onPreExecute() {

		}
		@Override
		protected List<FileType> doInBackground(String... params) {
			List<FileType>  list = null;
			try {
				String json =FileTypeServiceUtils.getFileTypes(1,10);
				list = (List<FileType>) JSONHelper.parseCollection(json,
						List.class, FileType.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		@Override
		protected void onPostExecute(List<FileType> result) {
			if (result != null&&result.size()>0) {
				if(result.size()>0) {
					FileTypeDialogAdapter fileTypeAdapter = new FileTypeDialogAdapter(activity, result);
					typeSpinner.setAdapter(fileTypeAdapter);
				}
			}
		}
	}
	public class DataAsyncTask extends AsyncTask<String, Void, List<IFileInfo>> {
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected List<IFileInfo> doInBackground(String... params) {
			List<IFileInfo> result = null;
			try {
				String resultParam = IFileInfoServiceUtils.getIFileInfos(RemoteMainActivity.deviceID,TypeId,1,10);
				result = (List<IFileInfo>) JSONHelper.parseCollection(
						resultParam, List.class, IFileInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}
		@Override
		protected void onPostExecute(List<IFileInfo> result) {

			if (result != null && result.size() > 0) {
				List<ItemName>  itemNames=AppTools.convertItemNames(result);
			final 	RadioCheckAlert radioCheckAlert=new RadioCheckAlert(activity,"节目列表");
				radioCheckAlert.setRadioItems(itemNames, null, -1, new RadioCheckAlert.OnRadioClickListener() {
					@Override
					public void onClick(ItemName itemName, int postion, boolean isChecked) {
					FileIndex=itemName.getId();
						typeMsg.setText(itemName.getItemName());
					}
				});
				radioCheckAlert.setOnSureListener(new RadioCheckAlert.OnSureListener() {
					@Override
					public void onCancle() {

					}
					@Override
					public void onSure() {
						if(FileIndex==0){
							ToastUtils.ShowError(activity,"请选择节目源",Toast.LENGTH_SHORT,true);
						}else{
							radioCheckAlert.dismiss();
						}
					}
				});
				radioCheckAlert.show();
			} else {
				ToastUtils.ShowError(activity,"该类型的数据已添加到用户列表",Toast.LENGTH_SHORT,true);
			}

		}
	}
}
