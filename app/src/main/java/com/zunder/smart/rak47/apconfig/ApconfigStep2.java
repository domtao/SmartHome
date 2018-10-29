package com.zunder.smart.rak47.apconfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


import com.zunder.smart.R;
import com.zunder.smart.rak47.simpleconfig_wizard.WLANAPI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ApconfigStep2 extends Activity 
{
	public static ApconfigStep2 apconfignote2;
	private TextView Ap_statu;
	private TextView Goto_easyconfig;
	private EditText User_name;
	private EditText User_Psk;
	private EditText Ap_ssid;
	private EditText Ap_psk;
	private ImageButton Ap_scan;
	private ImageButton Ap_eye;
	private Button Ap_config;
	private SimpleAdapter AP_RecvlistAdapter;
	private ArrayList<HashMap<String, Object>> AP_RecvlistItem;
	
	private Handler UI_Refresh_Handler=null;
	private String dest_ip="192.168.7.1";
	private int dest_port=80;
	private String mac="";
	private WLANAPI mWifiAdmin;//wifi API
	TextView backView;
	//for decode ssid
	private String keyString="selectedSSIDChange(";
	private String keyFlag="'";
	private ArrayList<String> ssids = new ArrayList<String>();
	private String get_mac="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apconfig_step2);
		apconfignote2=this;
		mWifiAdmin=new WLANAPI(this);
				
		Ap_statu=(TextView)findViewById(R.id.ap_status);
		Goto_easyconfig=(TextView)findViewById(R.id.gotoeasyconfig);
		Goto_easyconfig.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		User_name=(EditText)findViewById(R.id.user_name);
		User_Psk=(EditText)findViewById(R.id.user_psk);
		Ap_ssid=(EditText)findViewById(R.id.ap_ssid);
		Ap_psk=(EditText)findViewById(R.id.ap_psk);
		Ap_eye=(ImageButton)findViewById(R.id.ap_eye);
		Ap_eye.setOnClickListener(Ap_eye_click);
		Ap_scan=(ImageButton)findViewById(R.id.ap_scan);
		Ap_scan.setOnClickListener(Ap_scan_click);
		Ap_config=(Button)findViewById(R.id.ap_config);
		Ap_config.setOnClickListener(Ap_config_click);
		
		backView = (TextView) findViewById(R.id.backTxt);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		AP_RecvlistItem = new ArrayList<HashMap<String, Object>>();
		AP_RecvlistAdapter = new SimpleAdapter(this,AP_RecvlistItem, R.layout.ap_list_item,
				          new String[]{ "ssid","rssi"}, 
			              new int[]{ R.id.ap_ssid, R.id.ap_rssi});

		UI_Refresh();// Refresh UI
		
		Intent intent=getIntent();
		get_mac=intent.getStringExtra("mac");
	}
	
	/*
 	 * Add List
 	 */
	public void AddListItem(int logo,String ssid,String rssi)
	{		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ssid", ssid);
		map.put("rssi", rssi);
		
		AP_RecvlistItem.add(map);
		AP_RecvlistAdapter.notifyDataSetChanged();		
	}
	
	/*
	 * Refresh UI
	 */
	int SHOW_NETWORK_LIST=0;	
	int CONFIG_SUCCESS=1;
	int SSID_IS_EMPTY=2;
	int GET_WIFILIST=3;
	int CONNECT_WIFI=4;
	int DISMISS=5;
	int CONNECT_DEVICE_FAILED=6;
	int RESET_SUCCESS=7;
	int ERROR_CODE=8;
	String Error="";
	boolean timeout=false;
	int TIMEOUT=20;
	private Dialog pd;
	
	void UI_Refresh() 
	{	
		UI_Refresh_Handler = new Handler() 
		{  
            @Override  
            public void handleMessage(Message msg) 
            {  
                if(msg.what == SHOW_NETWORK_LIST) 
                {  
                	//Show ssid list have scanned
					if(ssids.size()>0)
					{	
//						pd.dismiss();
//		                pd= new Dialog(ApconfigStep2.this,R.style.MyDialog);
//						LayoutInflater warnDialog_inflater =getLayoutInflater();
//						View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//						TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//						warn_title.setText("Choose a network");
//						TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//						warn_note.setVisibility(View.GONE);
//						TextView warn_ok_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//						TextView warn_cancel_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//						warn_cancel_btn.setVisibility(View.GONE);
//						pd.setCanceledOnTouchOutside(true);
//						ListView warn_list=(ListView)warnDialog_admin.findViewById(R.id.dialog_list);
//						warn_list.setVisibility(View.VISIBLE);
//						warn_list.setAdapter(AP_RecvlistAdapter);
//						for(int n=0;n<ssids.size();n++)
//		                {
//		                	AddListItem(0,ssids.get(n).toString(),"");
//		                }
//						warn_list.setOnItemClickListener(new OnItemClickListener() {
//							@Override
//							public void onItemClick(AdapterView<?> parent,
//									View view, int position, long id) {
//								// TODO Auto-generated method stub
//								Ap_ssid.setText(AP_RecvlistItem.get(position).get("ssid").toString());
//		                    	//get psk of the ssid have saved
//		                		SharedPreferences p = getSharedPreferences(Ap_ssid.getText().toString(), MODE_PRIVATE);
//		                  	  	String psk=p.getString("psk", "");
//		                		Ap_psk.setText(psk);
//		                		pd.dismiss();
//							}
//						});
//						pd.setContentView(warnDialog_admin);
//						pd.show();
		                ssids.clear();
					} 
					else
					{
						pd.dismiss();
						DisplayToast("Get network list failed");
					}
                } 
                else if(msg.what == CONFIG_SUCCESS) 
                {
                	DisplayToast("Config success,reset device...");
				}
                else if(msg.what == SSID_IS_EMPTY) 
                {
                	DisplayToast("SSID can't be empty");
				}
                else if(msg.what == GET_WIFILIST) 
                {
//					pd= new Dialog(ApconfigStep2.this,R.style.MyDialog);
//					LayoutInflater warnDialog_inflater =getLayoutInflater();
//					View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//					TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//					warn_title.setText("Get Wifilist");
//					TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//					warn_note.setVisibility(View.GONE);
//					TextView warn_ok_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//					warn_ok_btn.setText("Getting Wifilist...");
//					TextView warn_cancel_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//					warn_cancel_btn.setVisibility(View.GONE);
//					pd.setCanceledOnTouchOutside(true);
//					pd.setContentView(warnDialog_admin);
//					pd.show();
				}
                else if(msg.what == CONNECT_WIFI) 
                {
//					pd= new Dialog(ApconfigStep2.this,R.style.MyDialog);
//					LayoutInflater warnDialog_inflater =getLayoutInflater();
//					View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//					TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//					warn_title.setText("Connect network");
//					TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//					warn_note.setVisibility(View.GONE);
//					TextView warn_ok_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//					warn_ok_btn.setText("Connecting network...");
//					TextView warn_cancel_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//					warn_cancel_btn.setVisibility(View.GONE);
//					pd.setCanceledOnTouchOutside(true);
//					pd.setContentView(warnDialog_admin);
//					pd.show();
				}
                else if(msg.what == DISMISS) 
                {
                	pd.dismiss();
				}
                else if(msg.what == CONNECT_DEVICE_FAILED) 
                {
                	DisplayToast("Mobile phone didn't connect to RAK device");
				}
                else if(msg.what == RESET_SUCCESS) 
                {	
                	Intent intent=new Intent();
                	intent.putExtra("ssid", Ap_ssid.getText().toString());
					intent.putExtra("mac", get_mac);
                	intent.setClass(ApconfigStep2.this, ApconfigStep3.class);
                	startActivity(intent);
				}
                else if(msg.what == ERROR_CODE) 
                {
                	DisplayToast(Error);
				}
                
                super.handleMessage(msg);  
            }  
        };  
	}
	
	/*
	 * check if connected to RAK module
	 */
	boolean Is_connect_rak=false;
	@Override
	protected void onResume() 
	{		
		super.onResume();
		if(mWifiAdmin.IsopenWifi())
        {   
			mWifiAdmin = new WLANAPI(this);
			Is_connect_rak=false;			
			String get_mac=mWifiAdmin.getBSSID().toUpperCase();
			if(mWifiAdmin.getSSID().toString().contains(ApconfigStep1.type))
			{				
				mac=get_mac;
				Ap_statu.setTextColor(Color.WHITE);
				Ap_statu.setText("Have been connected to RAK device:"+mac);
				Is_connect_rak=true;
			}
			else
			{
				Ap_statu.setTextColor(Color.RED);
				Ap_statu.setText("Haven't been connected to RAK device");
			}
        }
		else
		{			
			Is_connect_rak=false;
			DisplayToast("The network is unavailable, please check the network");
		}	
	}
	
	/*
	 * Goto Simpleconfig
	 */
		
	/*
	 * show or hidden psk
	 */	
	boolean psk_open=false;
	OnClickListener Ap_eye_click=new OnClickListener() 
	{		
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			psk_open=!psk_open;
			if(psk_open)
			{
				Ap_psk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				Ap_eye.setImageResource(R.mipmap.eyeopen);
			}
			else 
			{
				Ap_psk.setTransformationMethod(PasswordTransformationMethod.getInstance());
				Ap_eye.setImageResource(R.mipmap.eye);
			}
		}
	};
	
	
	/*
	 * Scan network
	 */
	boolean Is_scan_network=false;
	OnClickListener Ap_scan_click=new OnClickListener() 
	{		
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			Is_scan_network=true;
			new Thread(Config_device).start();
		}
	};
	
	/*
	 * AP Config
	 */
	boolean Is_config=false;
	OnClickListener Ap_config_click=new OnClickListener() 
	{		
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			//save psk of the ssid
			Editor editor = getSharedPreferences(Ap_ssid.getText().toString(), MODE_PRIVATE).edit();
		    editor.putString("psk", Ap_psk.getText().toString());
		    editor.commit();
		    Is_config=true;
		    new Thread(Config_device).start();   
		}
	};
	
	/*
	 * Scan and Configure
	 */
	Runnable Config_device=new Runnable() 
	{		
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			if(Is_connect_rak)
			{	
				//Authentication information	
				String admin_info= User_name.getText().toString()+":"+User_Psk.getText().toString();
				admin_info=" Basic "+ Base64.encodeToString(admin_info.getBytes(), Base64.NO_WRAP);
	
				if(Is_scan_network)// Scan network
				{					
					Message msg = new Message();  
	                msg.what = GET_WIFILIST;  
	                UI_Refresh_Handler.sendMessage(msg); 
					
					ssids.clear();	                
				    //Start Scan
					String Recv_Str=GetHttps("http://"+dest_ip+":80/scanlist.cgi","",admin_info,"GET");					
					if(Recv_Str.startsWith("ERROR:")==false)
					{
						ssids.clear();
						Is_scan_network=false;
						int position=0;
						keyString="\"ssid\":";
						keyFlag="\"";
						
						while(true)
						{
							int index=Recv_Str.indexOf(keyString,position);
							if(index!=-1)
							{
								position=index+keyString.length();
								int index1=Recv_Str.indexOf(keyFlag,position);
								if(index1!=-1)
								{
									position=index1+keyFlag.length();
									int index2=Recv_Str.indexOf(keyFlag,position);
									if(index2!=-1)
									{
										ssids.add(Recv_Str.substring(position,index2));
										position=index2+keyFlag.length();
									}
									else
									{
										break;
									}
								}
								else
								{
									break;
								}									
							}
							else
							{
								break;
							}
						}
						
						
						
						msg = new Message();  
		                msg.what = SHOW_NETWORK_LIST;  
		                UI_Refresh_Handler.sendMessage(msg); 							
					}
					else
					{
						Error=Recv_Str;
						msg = new Message();  
		                msg.what = ERROR_CODE;  
		                UI_Refresh_Handler.sendMessage(msg); 
					}
					Is_scan_network=false;
				}
				
				if(Is_config)//AP configure
				{	
					String Ap_config_data="";
					if(Ap_ssid.getText().toString().equals(""))							
					{
						Message msg = new Message();  
		                msg.what = SSID_IS_EMPTY;  
		                UI_Refresh_Handler.sendMessage(msg);
					}
					else
					{
						Editor editor = getSharedPreferences(Ap_ssid.getText().toString(), MODE_PRIVATE).edit();
					    editor.putString("psk", Ap_psk.getText().toString());
					    editor.commit();
						Message msg = new Message();  
		                msg.what = CONNECT_WIFI;  
		                UI_Refresh_Handler.sendMessage(msg); 
						if(Ap_psk.getText().toString().equals(""))
						{
							if((ApconfigStep1.type.equals("RAK475"))||(ApconfigStep1.type.equals("RAK477")))
								Ap_config_data="wlan_mode=0&sta_ssid="+Ap_ssid.getText().toString()+"&sta_sec_mode=0&sta_dhcp=1";
							else if((ApconfigStep1.type.equals("RAK473"))||(ApconfigStep1.type.equals("RAK476")))
								Ap_config_data="net_type=STA&ssid="+Ap_ssid.getText().toString()+"&sec_mode=0&dhcp_mode=1";
						}
						else
						{
							if((ApconfigStep1.type.equals("RAK475"))||(ApconfigStep1.type.equals("RAK477")))
								Ap_config_data="wlan_mode=0&sta_ssid="+Ap_ssid.getText().toString()+"&sta_sec_mode=1&sta_psk="+Ap_psk.getText().toString()+"&sta_dhcp=1";	
							else if((ApconfigStep1.type.equals("RAK473"))||(ApconfigStep1.type.equals("RAK476")))
								Ap_config_data="net_type=STA&ssid="+Ap_ssid.getText().toString()+"&sec_mode=1&psk="+Ap_psk.getText().toString()+"&dhcp_mode=1";
						}

						//Start configure
						String Recv_Str="";
						Recv_Str=GetHttps("http://"+dest_ip+":80/parameter.cgi",Ap_config_data,admin_info,"POST");
						if(Recv_Str.startsWith("ERROR:")==false)
						{
							msg = new Message();  
			                msg.what = CONFIG_SUCCESS;  
			                UI_Refresh_Handler.sendMessage(msg); 
			                //reset module after configured
			                Recv_Str=GetHttps("http://"+dest_ip+":80/manage.cgi","module_cmd=reset",admin_info,"POST");			                
			                if(Recv_Str.startsWith("ERROR:")==false)
							{
								msg = new Message();  
				                msg.what = RESET_SUCCESS;  
				                UI_Refresh_Handler.sendMessage(msg); 
							}
							else
							{
								Error=Recv_Str;
								msg = new Message();  
				                msg.what = ERROR_CODE;  
				                UI_Refresh_Handler.sendMessage(msg); 
							}
						}
						else {
							Error=Recv_Str;
							msg = new Message();  
			                msg.what = ERROR_CODE;  
			                UI_Refresh_Handler.sendMessage(msg); 
						}
					}
					Message msg = new Message();  
		            msg.what = DISMISS;  
		            UI_Refresh_Handler.sendMessage(msg); 
					Is_config=false;
				}		
			}
			else
			{
				Message msg = new Message();  
	            msg.what = CONNECT_DEVICE_FAILED;  
	            UI_Refresh_Handler.sendMessage(msg); 
			}
		}
	};

	
	/*
	 * Display message
	 */
	public void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	/*
	 * Exit
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return false;
	}

	/*
	 * Http send
	 */	
	String response="";
	boolean is_excute=false;
	String GetHttps(final String http,final String body,final String basic,final String way)
	{  
		response="";
		is_excute=true;
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) 
			{	
				InputStream inputs=null;
		        byte[] entity=body.getBytes();
		        try
		        {  
		            HttpURLConnection conn = (HttpURLConnection)new URL(http).openConnection();                                          
		            conn.setDoInput(true); 
		            conn.setRequestProperty("Accept", "*/*");  
		            conn.setRequestProperty("connection", "close");  
					conn.setRequestProperty("Authorization", basic);
		            if(way.equals("POST"))
		            {
		            	conn.setDoOutput(true);
		            	conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
		            }
		            else
		            {
		            	conn.setDoOutput(false);
					}
		            conn.setRequestMethod(way); 
		            
		            //set timeout
		            conn.setConnectTimeout(15000);//15S
		            conn.setReadTimeout(30000);//30S
		            conn.connect();  
		            //http send
		            if(way.equals("POST"))
		            {
			            OutputStream outStream = conn.getOutputStream();
			            outStream.write(entity);
			            outStream.flush();
			            outStream.close();	            
		            }
		            //recieve data
		            inputs=conn.getInputStream();           	
		        	BufferedReader br = new BufferedReader(new InputStreamReader(inputs));
		        	String lines="";
		        	response="";
		        	while((lines = br.readLine()) != null)
		        	{
		        		response+=lines;
		        	}        		 
		        	Log.i("Response=>", response);
		        }
		        catch(Exception e)
		        {  
		        	response="ERROR:"+e.toString();
		        	Log.e("",e.toString());  
		        }
		        is_excute=false;
				return null;
			}
			@Override
			protected void onPostExecute(Void result) 
			{	
				is_excute=false;
			}
		}.execute();
		
		while(is_excute){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        return response;
     } 
	
	/*
	 * Close Activity
	 */	
	void CloseActivity(){
		if(ApconfigStep1.apconfignote1!=null)
			ApconfigStep1.apconfignote1.finish();
		if(ApconfigStep2.apconfignote2!=null)
			ApconfigStep2.apconfignote2.finish();
	}
}
