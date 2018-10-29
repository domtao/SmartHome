package com.zunder.smart.activity.sub;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.control.ButtonBean;
import com.zunder.control.ButtonInfo;
import com.zunder.control.SubButtonUtils;
import com.zunder.scrollview.widget.WheelAdapter;
import com.zunder.scrollview.widget.WheelView;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.activity.timmer.CurTimer;
import com.zunder.smart.activity.timmer.EmptyTimer;
import com.zunder.smart.activity.timmer.LightTimer;
import com.zunder.smart.activity.timmer.LockTimer;
import com.zunder.smart.activity.timmer.NewWinTimer;
import com.zunder.smart.activity.timmer.ProjectorTimer;
import com.zunder.smart.activity.timmer.SeriesTimer;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

import java.util.List;

public class LightActivity extends Activity implements OnClickListener,
        DeviceStateListener {
	private Activity activity;

	private static int Id = 0;
	static Device deviceParams;
	TextView titleTxt, backTxt, editeTxt;
	String deviceName = "";

	private WheelView wvTime, wvUtil;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView img_2;
	private ImageView img_3;
	private LinearLayout ioLayout;

	private RightMenu rightButtonMenuAlert;
	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, LightActivity.class);
		activity.startActivityForResult(intent,100);
	}
	private LinearLayout mainLayout;
	private TextView timeTxt;
	private LinearLayout timeLayout;
	private TextView msgTxt;
	private Button timeSure;

	private List<ButtonInfo> list;
	String HourStr="0";
	String HourUnit="小时";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light);
		activity = this;
		TcpSender.setDeviceStateListener(this);
		deviceParams = DeviceFactory.getInstance().getDevicesById(Id);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
		msgTxt = (TextView) findViewById(R.id.msgTxt);
		timeSure = (Button) findViewById(R.id.timeSure);
		wvTime=(WheelView)findViewById(R.id.wvTime) ;
		wvUtil=(WheelView)findViewById(R.id.wvUtil) ;

		img1=(ImageView)findViewById(R.id.imgIO1);
		img2=(ImageView)findViewById(R.id.imgIO2);
		img3=(ImageView)findViewById(R.id.imgIO3);

		img_2=(ImageView)findViewById(R.id.imgIO_2);
		img_3=(ImageView)findViewById(R.id.imgIO_3);
		ioLayout=(LinearLayout)findViewById(R.id.IoLayout);

		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img_2.setOnClickListener(this);
		img_3.setOnClickListener(this);
		List<String> list=ListNumBer.getNumbers();
		WheelAdapter adapter=new WheelAdapter(list);
		wvTime.setAdapter(adapter);
		List<String> listUnits=ListNumBer.getUnits();
		WheelAdapter unitsAdapter=new WheelAdapter(listUnits);
		wvUtil.setAdapter(unitsAdapter);

		timeSure.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		deviceName = deviceParams.getDeviceName();
		titleTxt.setText(deviceParams.getRoomName()+deviceName);
		initRightButtonMenuAlert();
		init();
		wvTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				if (index== 0) {
					timeLayout.setVisibility(View.GONE);
				} else {
					timeLayout.setVisibility(View.VISIBLE);
				}
				HourStr=itemName;
			}
		});
		wvUtil.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				HourUnit=itemName;
			}
		});
	}
	void init(){
		ButtonBean buttonBean = SubButtonUtils.getInstance().getButtonBean(deviceParams.getDeviceTypeKey());
		if (buttonBean != null) {
			list = buttonBean.getList();
			initButtons();
		}
		if(deviceParams.getProductsCode().equals("01")){
			img2.setVisibility(View.GONE);
			img3.setVisibility(View.GONE);
			ioLayout.setVisibility(View.GONE);
		} else if(deviceParams.getProductsCode().equals("02")){
			img3.setVisibility(View.GONE);
			ioLayout.setVisibility(View.GONE);
		} else if(deviceParams.getProductsCode().equals("03")){
			ioLayout.setVisibility(View.GONE);
		} else if(deviceParams.getProductsCode().equals("04")){
			img2.setVisibility(View.GONE);
			ioLayout.setVisibility(View.VISIBLE);
		}

	}
	//joe 控件赋值
	public void initButtons() {
	    //joe 动态添加控件
        int ccount=list.size();
        int group=ccount/3;
        if((ccount%3)!=0){
            group+=1;
        }
		for(int i=0;i<group;i++){
			LinearLayout linearLayout=new LinearLayout(activity);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams layout= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layout.height=150;
            layout.gravity = Gravity.TOP;
            layout.setMargins(10,10,10,10);
			linearLayout.setLayoutParams(layout);
			for(int j=0;j<3;j++){
				Button button=new Button(activity);
				ButtonInfo buttonInfo=list.get(i*3+j);
				button.setTag(buttonInfo.getId()+"");
				button.setText(buttonInfo.getBtnTitle());
				button.setTextColor(getResources().getColor(R.color.font_open_press));
				button.setBackgroundResource(R.drawable.button_boder_shape);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//joe 0823 发码
						ButtonInfo info=SubButtonUtils.getInstance().getButtonInfo(list,Integer.parseInt(((Button)v).getTag().toString()));
						if(info!=null){
							SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceParams.getDeviceName() + info.getCMD(), deviceParams);
							ToastUtils.ShowSuccess(activity,deviceParams.getRoomName()+deviceParams.getDeviceName() + info.getCMD(),Toast.LENGTH_SHORT,true);
						}
						playKeytone();
					}
				});
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				params.weight = 1.0f;
				params.setMargins(20,20,20,20);
				button.setLayoutParams(params);
				linearLayout.addView(button);
			}
			mainLayout.addView(linearLayout);
		}
	}
	private void playKeytone() {
		MediaPlayer.create(activity, R.raw.ping_short).start();
	}
	private void initRightButtonMenuAlert() {
		rightButtonMenuAlert = new RightMenu(activity, R.array.right_light,
				R.drawable.right_airswitch_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						DeviceTimerActivity.startActivity(activity,deviceParams.getId());
						break;
					case 1:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(getString(R.string.tip), getString(R.string.is_set) + deviceParams.getDeviceName()
								+ "为互控开关");
						alert.setSureListener(new OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceParams.getDeviceName() + getString(R.string.set_mutu), deviceParams);
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;
					case 2:
						DialogAlert alert1 = new DialogAlert(activity);
						alert1.init(getString(R.string.tip), getString(R.string.is_match_mutu));
						alert1.setSureListener(new OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceParams.getDeviceName()  + getString(R.string.mutu_match), deviceParams);
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert1.show();
						break;
					case 3:
						DialogAlert alert2 = new DialogAlert(activity);
						alert2.init(getString(R.string.tip), getString(R.string.is_del_mutu));
						alert2.setSureListener(new OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceParams.getDeviceName() + getString(R.string.mutu_del), deviceParams);
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert2.show();
						break;
					case 4:
						MoreActivity.startActivity(activity,deviceParams.getId());
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}
	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.editeTxt:
			rightButtonMenuAlert.show(editeTxt);
			break;

			case R.id.timeSure: {
				//joe 0823 打开命令
				SendCMD.getInstance().sendCMD(0, deviceParams.getRoomName()+deviceName + getString(R.string.open_1)+HourStr+HourUnit, null);
				ToastUtils.ShowSuccess(activity,deviceParams.getRoomName()+deviceName + getString(R.string.open_1)+HourStr+HourUnit,Toast.LENGTH_SHORT,true);
			}
				break;
			case R.id.imgIO1: {
				String cmd = "*C0019FA" + deviceParams.getProductsCode() + deviceParams.getDeviceID() + AppTools.toHex(0) + "000000";
				SendCMD.getInstance().sendCMD(200, cmd, null);
			}
				break;
			case R.id.imgIO2:
			{
				String cmd = "*C0019FA" + deviceParams.getProductsCode() + deviceParams.getDeviceID() + AppTools.toHex(1) + "000000";
				SendCMD.getInstance().sendCMD(200, cmd, null);
			}
				break;
			case R.id.imgIO3:
			{
				String cmd = "*C0019FA" + deviceParams.getProductsCode() + deviceParams.getDeviceID() + AppTools.toHex(3) + "000000";
				SendCMD.getInstance().sendCMD(200, cmd, null);
			}
				break;
			case R.id.imgIO_2:
			{
				String cmd = "*C0019FA" + deviceParams.getProductsCode() + deviceParams.getDeviceID() + AppTools.toHex(1) + "000000";
				SendCMD.getInstance().sendCMD(200, cmd, null);
			}
				break;
			case R.id.imgIO_3:
			{
				String cmd = "*C0019FA" + deviceParams.getProductsCode() + deviceParams.getDeviceID() + AppTools.toHex(2) + "000000";
				SendCMD.getInstance().sendCMD(200, cmd, null);
			}
				break;

		default:
			break;
		}
		playKeytone();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		//joe 子页面回码处理
		if(deviceParams!=null) {
			if (deviceParams.getDeviceTypeKey() == 1) {
				//joe 灯光
				if(cmd.substring(8, 16).equals(deviceParams.getProductsCode()+deviceParams.getDeviceID())) {
					LightTimer.getInstance().setBackeCode(cmd, onLightIndexSureListener);
					}
				}
			}
		}
	//joe 0823 窗帘回码
	private LightTimer.OnLightIndexSureListener onLightIndexSureListener=new LightTimer.OnLightIndexSureListener(){

		@Override
		public void onState(int powerState, int ioIndex1, int ioIndex2, int ioIndex3, int ioIndex4) {
			if(deviceParams.getProductsCode().equals("01")){
				img1.setImageResource(ioIndex1>0?R.mipmap.light_on_1:R.mipmap.light_off_1);

			} else if(deviceParams.getProductsCode().equals("02")){
				img1.setImageResource(ioIndex1>0?R.mipmap.light_on_3:R.mipmap.light_off_3);
				img2.setImageResource(ioIndex2>0?R.mipmap.light_on_3:R.mipmap.light_off_3);

			} else if(deviceParams.getProductsCode().equals("03")){
				img1.setImageResource(ioIndex1>0?R.mipmap.light_on_3:R.mipmap.light_off_3);
				img2.setImageResource(ioIndex2>0?R.mipmap.light_on_3:R.mipmap.light_off_3);
				img3.setImageResource(ioIndex3>0?R.mipmap.light_on_3:R.mipmap.light_off_3);

			} else if(deviceParams.getProductsCode().equals("04")){
				img1.setImageResource(ioIndex1>0?R.mipmap.light_on_3:R.mipmap.light_off_3);
				img_2.setImageResource(ioIndex2>0?R.mipmap.light_on_4:R.mipmap.light_off_4);
				img_3.setImageResource(ioIndex3>0?R.mipmap.light_on_4:R.mipmap.light_off_4);
				img3.setImageResource(ioIndex4>0?R.mipmap.light_on_3:R.mipmap.light_off_3);
			}
		}
	};
}
