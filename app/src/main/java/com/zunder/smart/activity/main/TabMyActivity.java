package com.zunder.smart.activity.main;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.HistoryActivity;
import com.zunder.smart.activity.LoginActivity;


import com.zunder.smart.activity.WelcomeActivity;
import com.zunder.smart.activity.camera.CameraFragment;
import com.zunder.smart.activity.device.DeviceAddFragment;
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.activity.device.DeviceTypeFragment;
import com.zunder.smart.activity.gateway.GateWayAddFragment;
import com.zunder.smart.activity.gateway.GateWayFragment;
import com.zunder.smart.activity.gateway.GateWaySettingFragment;
import com.zunder.smart.activity.gateway.SettingWifiFragment;
import com.zunder.smart.activity.gateway.SimpleConfigFragment;
import com.zunder.smart.activity.mode.ModeAddFragment;
import com.zunder.smart.activity.passive.PassiveFragment;
import com.zunder.smart.activity.relay.RelayDeviceFragment;
import com.zunder.smart.activity.relay.RelayFragment;
import com.zunder.smart.activity.relay.RelayListFragment;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.SystemInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TabMyActivity extends FragmentActivity implements OnClickListener {

	private Activity activity;
	private SharedPreferences spf;
	private Button loginButton;

	public DeviceFragment deviceFragment;
	public DeviceAddFragment deviceAddFragment;
	DeviceTypeFragment deviceTypeFragment;
	public GateWayFragment gatewayFragment;
	public GateWayAddFragment gatewayAddFragment;
	public GateWaySettingFragment gateWaySettingFragment;
	SimpleConfigFragment simpleConfigFragment;
	SettingWifiFragment settingWifiFragment;
	public RelayFragment relayFragment;
	public RelayListFragment relayListFragment;
	public RelayDeviceFragment relayDeviceFragment;
	public PassiveFragment passiveFragment;

	public ModeAddFragment modeAddFragment;
     private static TabMyActivity instance;
     TextView userTxt,gateWayTxt;
     private TextView versionTxt;

    public static TabMyActivity getInstance() {
        return instance;
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_my);
		activity=this;
		instance=this;
		spf = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		userTxt=(TextView)findViewById(R.id.userTxt);
		gateWayTxt=(TextView)findViewById(R.id.gateWayTxt);
        versionTxt=(TextView)findViewById(R.id.versionTxt);
		loginButton=(Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);

		ImageButton mydeviceLayout = (ImageButton) findViewById(R.id.set_device);
		mydeviceLayout.setOnClickListener(this);

		ImageButton passiveLayout = (ImageButton)findViewById(R.id.sensorBtn);
		passiveLayout.setOnClickListener(this);

		ImageButton set_gateWay = (ImageButton) findViewById(R.id.set_gateWay);
		set_gateWay.setOnClickListener(this);

		ImageButton relayLayout = (ImageButton)findViewById(R.id.relayButton);
		relayLayout.setOnClickListener(this);

		ImageButton optionHis = (ImageButton)findViewById(R.id.optionHis);
		optionHis.setOnClickListener(this);

		ImageButton optionZL = (ImageButton)findViewById(R.id.optionZL);
		optionZL.setOnClickListener(this);


		spf = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		setData();
        intitFragment();

        versionTxt.setText("当前版本"+MyApplication.getInstance().getAPPVersion());
	}

	void intitFragment(){
		deviceFragment= (DeviceFragment) getSupportFragmentManager()
				.findFragmentById(R.id.deviceFragment);
		getSupportFragmentManager().beginTransaction().hide(deviceFragment).commit();

		deviceAddFragment= (DeviceAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.deviceAddFragment);
		getSupportFragmentManager().beginTransaction().hide(deviceAddFragment).commit();

		deviceTypeFragment = (DeviceTypeFragment)getSupportFragmentManager()
				.findFragmentById(R.id.deviceTypeFragment);
        getSupportFragmentManager().beginTransaction().hide(deviceTypeFragment).commit();

        gatewayFragment= (GateWayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gatewayFragment);
        getSupportFragmentManager().beginTransaction().hide(gatewayFragment).commit();

        gatewayAddFragment= (GateWayAddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gatewayAddFragment);
        getSupportFragmentManager().beginTransaction().hide(gatewayAddFragment).commit();

		gateWaySettingFragment= (GateWaySettingFragment) getSupportFragmentManager()
				.findFragmentById(R.id.gateWaySettingFragment);
		getSupportFragmentManager().beginTransaction().hide(gateWaySettingFragment).commit();

		simpleConfigFragment= (SimpleConfigFragment) getSupportFragmentManager()
				.findFragmentById(R.id.configFragment);
		getSupportFragmentManager().beginTransaction().hide(simpleConfigFragment).commit();

		settingWifiFragment= (SettingWifiFragment) getSupportFragmentManager()
				.findFragmentById(R.id.settingWifiFragment);
		getSupportFragmentManager().beginTransaction().hide(settingWifiFragment).commit();

		relayFragment= (RelayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.relayFragment);
		getSupportFragmentManager().beginTransaction().hide(relayFragment).commit();

		relayListFragment= (RelayListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.relayListFragment);
		getSupportFragmentManager().beginTransaction().hide(relayListFragment).commit();

		relayDeviceFragment= (RelayDeviceFragment) getSupportFragmentManager()
				.findFragmentById(R.id.relayDeviceFragment);
		getSupportFragmentManager().beginTransaction().hide(relayDeviceFragment).commit();
		passiveFragment= (PassiveFragment) getSupportFragmentManager()
				.findFragmentById(R.id.passiveFragment);
		getSupportFragmentManager().beginTransaction().hide(passiveFragment).commit();
		modeAddFragment= (ModeAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.modeAddFragment);
		getSupportFragmentManager().beginTransaction().hide(modeAddFragment).commit();
	}

	@Override
	public void onResume() {
    	super.onResume();
   setTip("");
	}
	public String connectGateway() {
    	String resultStr="0";
    	int result=0;
		List<GateWay> gateWayList = GateWayService.list;
		if (gateWayList != null && gateWayList.size() > 0) {
			for (int i = 0; i < gateWayList.size(); i++) {
				final GateWay gateWay = gateWayList.get(i);
				if (gateWay.getState().equals("在线")&&gateWay.getTypeId()<3) {
					resultStr=gateWay.getGatewayName();
					result++;
				}
			}
		}
		if(result==0){
			 resultStr="0";
		}else if(result>1){
			resultStr=result+"";
		}
		return resultStr;
	}
	@SuppressLint("ResourceType")
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.userLogin:
				if (spf.getString("userName", "").equals("")
						|| spf.getString("passWord", "").equals("")) {
					Intent loginIntent = new Intent(activity, LoginActivity.class);
					startActivity(loginIntent);
				}
				break;
			case R.id.set_device:
				if(GateWayFactory.getInstance().getMainGateWay()>=1) {
					showFragMent(0);
					deviceFragment.initAdapter();
				}else{
					showFragMent(2);
					gatewayFragment.init();
					ToastUtils.ShowError(activity,"请先添加网关",Toast.LENGTH_SHORT,true);
				}
				break;
			case R.id.set_gateWay:
                showFragMent(2);
                gatewayFragment.init();
				break;
			case R.id.loginButton:
				if (spf.getString("userName", "").equals("")
						|| spf.getString("passWord", "").equals("")) {
					LoginActivity.startActivity(activity);
				}else{
					TipAlert tipAlert = new TipAlert(activity, getString(R.string.tip), getString(R.string.is_exite));
					tipAlert.setSureListener(new TipAlert.OnSureListener() {
						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							Editor editor = spf.edit();
							editor.putString("passWord", "");
							editor.putString("PrimaryKey", "");
							editor.commit();
							loginButton.setText("用户登录");
							userTxt.setText("未登录");
						}
					});
					tipAlert.show();
				}
				break;
			case R.id.sensorBtn:
				showVideoWindow();
				break;
			case R.id.optionZL:
				WelcomeActivity.startActivity(activity,1);
				break;
			case R.id.relayButton:
				//joe 中继
				if(GateWayFactory.getInstance().getMainGateWay()>1) {
					final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.seletgateway),GateWayFactory.getInstance().getGateWay(), null, 0);
					alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
						@Override
						public void onItem(int pos, String itemName) {
							if (itemName.equals("")) {
								return;
							}
							GateWay gateWay = GateWayFactory.getInstance().getGateWayByName(itemName);
							if (gateWay != null) {
								Device device=new Device();
								device.setDeviceBackCode(gateWay.getGatewayID());
								device.setCmdDecodeType(gateWay.getTypeId());
								DeviceFactory.getInstance().setGateWayDevice(device);
								TabMyActivity.getInstance().showFragMent(7);
								TabMyActivity.getInstance().relayFragment.initAdpapter(device);
							}
							alertViewWindow.dismiss();
						}
					});
					alertViewWindow.show();
				}else{
					if(GateWayFactory.getInstance().getMainGateWay()==1) {
						Device device = new Device();
						device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
						device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
						DeviceFactory.getInstance().setGateWayDevice(device);
						TabMyActivity.getInstance().showFragMent(7);
						TabMyActivity.getInstance().relayFragment.initAdpapter(device);
					}else{
						ToastUtils.ShowError(activity,"请添加网关设备",Toast.LENGTH_SHORT,true);
					}
				}
				break;
			case R.id.optionHis:
				if (spf.getString("userName", "").equals("")
						|| spf.getString("passWord", "").equals("")) {
					ToastUtils.ShowError(activity,"用户未登录",Toast.LENGTH_SHORT,true);
				}else{
					HistoryActivity.startActivity(activity);
				}
				break;
			default:
				break;
		}
	}
	//joe
	public void setData() {
		if (spf.getString("userName", "").equals("")
				|| spf.getString("passWord", "").equals("")) {
		loginButton.setText("用户登录");
			userTxt.setText("未登录");
		}else{
			loginButton.setText("账号注销");
			userTxt.setText(spf.getString("userName", "未登录"));
		}

	}

	public void setTip(String msgTxt) {
		String result=connectGateway();
		if(result.equals("0")){
			gateWayTxt.setText("网关不在线");
		}else{
			if(Integer.parseInt(AppTools.getNumbers(result))>0&&Integer.parseInt(AppTools.getNumbers(result))<10){
				gateWayTxt.setText(result + "个网关在线");
			}else {
				if(msgTxt.equals("")) {
					gateWayTxt.setText(result+"网关在线");
				}else{
					gateWayTxt.setText(msgTxt);
				}
			}
		}
	}

	private void showVideoWindow() {
		if(GateWayFactory.getInstance().getMainGateWay()>1) {
			final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.seletgateway), GateWayFactory.getInstance().getGateWay(), null, 0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					if (itemName.equals("")) {
						return;
					}
					GateWay gateWay =  GateWayFactory.getInstance().getGateWayByName(itemName);
					if (gateWay != null) {
						Device device=new Device();
						device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
						device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
						DeviceFactory.getInstance().setGateWayDevice(device);

						String cmd = "*C0051000100000000000000";
						String result = ISocketCode.setForward(cmd, gateWay.getGatewayID());
						MainActivity.getInstance().sendCode(result);
					}

//					PassiveActivity.startActivity(activity);
					showFragMent(10);
					passiveFragment.init();
					alertViewWindow.dismiss();
				}
			});
			alertViewWindow.show();
		}else{
			if(GateWayFactory.getInstance().getMainGateWay()==1) {
				Device device=new Device();
				device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
				device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
				DeviceFactory.getInstance().setGateWayDevice(device);
				String cmd = "*C0051000100000000000000";
				String result = ISocketCode.setForward(cmd, GateWayFactory.getInstance().list.get(0).getGatewayID());
				MainActivity.getInstance().sendCode(result);
				showFragMent(10);
				passiveFragment.init();
			}else{
				ToastUtils.ShowError(activity,"请添加网关设备",Toast.LENGTH_SHORT,true);
			}
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == event.KEYCODE_BACK) {
			if(deviceAddFragment!=null&&!deviceAddFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(deviceAddFragment).commit();
			} else if(deviceFragment!=null&&!deviceFragment.isHidden()){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,
                                R.anim.slide_out_right)
                        .hide(deviceFragment).commit();
            } else  if(deviceTypeFragment!=null&&!deviceTypeFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(deviceTypeFragment).commit();
			}else  if(gateWaySettingFragment!=null&&!gateWaySettingFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(gateWaySettingFragment).commit();
			}
			else  if(gatewayAddFragment!=null&&!gatewayAddFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(gatewayAddFragment).commit();
			}else  if(simpleConfigFragment!=null&&!simpleConfigFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(simpleConfigFragment).commit();
			}else  if(settingWifiFragment!=null&&!settingWifiFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(settingWifiFragment).commit();
			}else  if(gatewayFragment!=null&&!gatewayFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(gatewayFragment).commit();
			}else  if(relayDeviceFragment!=null&&!relayDeviceFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(relayDeviceFragment).commit();
			}else  if(relayListFragment!=null&&!relayListFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(relayListFragment).commit();
				relayListFragment.onHideCode();
			}else  if(relayFragment!=null&&!relayFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(relayFragment).commit();
			}else  if(modeAddFragment!=null&&!modeAddFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(modeAddFragment).commit();
			}else  if(passiveFragment!=null&&!passiveFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(passiveFragment).commit();
				passiveFragment.onHideCode();
			}
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showFragMent(int index){
        Fragment fragment=null;
	    switch (index){
			case -1:
				fragment=deviceAddFragment;
				break;
			case 0:
				fragment=deviceFragment;
				break;
            case 1:
                fragment=deviceTypeFragment;
                break;
            case 2:
                fragment=gatewayFragment;
                break;
			case 3:
                fragment=simpleConfigFragment;
                break;
			case 4:
				fragment=settingWifiFragment;
				break;
			case 5:
                fragment=gatewayAddFragment;
                break;
			case 6:
				fragment=gateWaySettingFragment;
				break;
			case 7:
				fragment=relayFragment;
				break;
			case 8:
				fragment=relayListFragment;
				break;
			case 9:
				fragment=relayDeviceFragment;
				break;
			case 10:
				fragment=passiveFragment;
				break;
			case 11:
				fragment=modeAddFragment;
				break;

        }
        if(fragment!=null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left,
                            R.anim.slide_out_right)
                    .show(fragment).commit();
        }
    }
    public void hideFragMent(int index){
        Fragment fragment=null;
		switch (index){
			case -1:
				fragment=deviceAddFragment;
				break;
			case 0:
				fragment=deviceFragment;
				break;
			case 1:
				fragment=deviceTypeFragment;
				break;
			case 2:
				fragment=gatewayFragment;
				break;
            case 3:
                fragment=simpleConfigFragment;
                break;
			case 4:
				fragment=settingWifiFragment;
				break;
			case 5:
				fragment=gatewayAddFragment;
				break;
			case 6:
				fragment=gateWaySettingFragment;
				break;
			case 7:
				fragment=relayFragment;
				break;
			case 8:
				fragment=relayListFragment;
				break;
			case 9:
				fragment=relayDeviceFragment;
				break;
			case 10:
				fragment=passiveFragment;
				break;
			case 11:
				fragment=modeAddFragment;
				break;
		}
        if(fragment!=null&&!fragment.isHidden()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left,
                            R.anim.slide_out_right)
                    .hide(fragment).commit();
        }
    }
	public void hiFragment(){
		if(instance!=null){
			for (int i=-1;i<11;i++) {
                if(i==2&&MainActivity.getInstance().mHost.getCurrentTab()!=3){
                    continue;
                }else{
                    hideFragMent(i);
                }
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setData();
	}
	public void setCamera(){
    	if(gatewayFragment!=null&&!gatewayFragment.isHidden()){
			GateWayService.setCameraHandleListener(gatewayFragment);
		}
	}
}
