package com.zunder.smart.rak47.apconfig;



import com.zunder.smart.R;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.rak47.simpleconfig_wizard.WLANAPI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ApconfigStep1 extends Activity
{
	public static ApconfigStep1 apconfignote1;
	private Button Next;
	public static String type="RAK477";
	private TextView _apConfigNote1;
	TextView backView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apconfig_step1);
		apconfignote1=this;
  	  	_apConfigNote1=(TextView)findViewById(R.id.apconfig_note1);
  	  	if(type.equals("RAK473")){
  	  		_apConfigNote1.setText(getApplication().getString(R.string.apconfig_rak473_note));
  	  	}
  	  	else if(type.equals("RAK475")){
  	  		_apConfigNote1.setText(getApplication().getString(R.string.apconfig_rak475_note));
  	  	}
  	  	else if(type.equals("RAK476")){
	  		_apConfigNote1.setText(getApplication().getString(R.string.apconfig_rak476_note));
	  	}
  	  	else if(type.equals("RAK477")){
	  		_apConfigNote1.setText(getApplication().getString(R.string.apconfig_rak477_note));
	  	}
  		
		backView = (TextView) findViewById(R.id.backTxt);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Next=(Button)findViewById(R.id.next);
		Next.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				WLANAPI mWifiAdmin = new WLANAPI(getApplicationContext());
				if(mWifiAdmin.getSSID().contains(type))
				{
					String get_mac=mWifiAdmin.getBSSID().toUpperCase();
					Intent intent=new Intent();
					intent.putExtra("mac", get_mac);
					intent.setClass(ApconfigStep1.this, ApconfigStep2.class);
					startActivity(intent);
				}
				else{
					final WarnDialog warnDialog= new WarnDialog(ApconfigStep1.this,getString(R.string.warning));

					if(type.equals("RAK473")){
						warnDialog.setMessage("Please connect to the module's network, it's named as RAK473_WEB_XXXXXX");
			  	  	}
			  	  	else if(type.equals("RAK475")){
						warnDialog.setMessage("Please connect to the module's network, it's named as RAK475_AP_XXXXXX");
			  	  	}
			  	  	else if(type.equals("RAK476")){
						warnDialog.setMessage("Please connect to the module's network, it's named as RAK476_WEB_XXXXXX");
			  	  	}
			  	  	else if(type.equals("RAK477")){
						warnDialog.setMessage("请链接模块网络, 它的名称是RAK477_AP_XXXXXX");
			  	  	}
					warnDialog.setSureListener(new WarnDialog.OnSureListener() {
						@Override
						public void onCancle() {
							warnDialog.dismiss();
						}
					});
					warnDialog.show();
				}
			}
		});
	}
}
