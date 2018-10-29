package com.zunder.smart.rak47.ota;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.zunder.smart.rak47.MainTabActivity;
import com.zunder.smart.R;
import com.zunder.smart.rak47.apconfig.ApconfigStep1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class OTAActivity2 extends Activity 
{	
	private String _deviceIp="192.168.7.1";
	private int _devicePort=80;
	private TextView _curVersion;
	private TextView _newVersion;
	private TcpSocket _tcpSocket;
	private Button _startButton;
	private String Post_ip = "POST /upgrade.cgi HTTP/1.1\r\nHost: ";
	private String Post_length = "\r\nContent-Type: multipart/form-data; boundary=---------------------------7e04923e029e\r\nConnection: Keep-Alive\r\nContent-Length: ";
	private String Post_admin = "\r\nAuthorization:";
	private String Post_end = "---------------------------7e04923e029e\r\nContent-Disposition: form-data; name=\"fw_file\"; filename=\"ota_all.bin\"\r\nContent-Type: application/octet-stream\r\n\r\n";
	private String admin_info= "admin:admin";
	private Handler _refresHandler=new Handler();
	private Dialog pd;
	private boolean _isCancel=false;
	private TextView pd_progress_num;
	private String path= "";
	private ProgressBar pd_progress;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota2);
        pd= new Dialog(OTAActivity2.this,R.style.MyDialog);
        _curVersion=(TextView)findViewById(R.id.cur_version);
        _newVersion=(TextView)findViewById(R.id.new_version);
        _startButton=(Button)findViewById(R.id.start_button);
        _startButton.setOnClickListener(_startButton_Click);
        
        Intent intent=getIntent();
        _deviceIp=intent.getStringExtra("deviceip");
        admin_info=" Basic "+ Base64.encodeToString(admin_info.getBytes(), Base64.NO_WRAP);
        
        File file=new File(MainTabActivity.RAK47X_Path+"/"+ApconfigStep1.type+"/");
		File[] files = file.listFiles();
		if(files.length>0){
			_newVersion.setText(findString(files[0].getName(), "OTA_V", ".bin"));
			path=MainTabActivity.RAK47X_Path+"/"+ApconfigStep1.type+"/"+files[0].getName();
		}
		if(_deviceIp.equals("192.168.7.1")==false){
			if((ApconfigStep1.type.equals("RAK473"))||(ApconfigStep1.type.equals("RAK476"))){
				_devicePort=1352;
			}
		}
		//Get version
		String Version=GetHttps("http://"+_deviceIp+":"+_devicePort+"/info.cgi","",admin_info,"GET");
        _curVersion.setText(findString(Version, "module_version=", "&"));
	}
	
	
	@Override
    public void onDestroy()
	{
		_isCancel=true;
		super.onDestroy();
	}
	
	Runnable _refresHandlerRunnable=new Runnable() {
		public void run() {
			if(pd_progress_num!=null)
				pd_progress_num.setText(percent+"%");
			if(pd_progress!=null)
				pd_progress.setProgress(percent);
			_refresHandler.postDelayed(_refresHandlerRunnable,100);
		}
	};
	
	/**
	 * Start Upgrade Firmware
	 **/
	boolean isUpgradeOk=false;
	OnClickListener _startButton_Click=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			isUpgradeOk=false;
			_isCancel=false;
			percent=0;
			if(pd_progress_num!=null)
				pd_progress_num.setText(percent+"%");
			if(pd_progress!=null)
				pd_progress.setProgress(percent);
			_refresHandler.post(_refresHandlerRunnable);
//			LayoutInflater pd_inflater =getLayoutInflater();
//			View pd_admin=pd_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//			TextView pd_title =(TextView)pd_admin.findViewById(R.id.dialog_title);
//			pd_title.setText("���¹̼�");
//			TextView pd_note =(TextView)pd_admin.findViewById(R.id.dialog_note);
//			pd_note.setText("���ڸ�����...");
//			TextView pd_ok_btn =(TextView)pd_admin.findViewById(R.id.dialog_ok_btn);
//			pd_ok_btn.setVisibility(View.GONE);
//			TextView pd_cancel_btn =(TextView)pd_admin.findViewById(R.id.dialog_cancel_btn);
//			pd_cancel_btn.setText("ȡ ��");
//			pd_cancel_btn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					_isCancel=true;
//					pd.dismiss();
//				}
//			});
//			pd_progress =(ProgressBar)pd_admin.findViewById(R.id.dialog_progress);
//			pd_progress_num =(TextView)pd_admin.findViewById(R.id.dialog_progress_num);
//			pd_progress_num.setText(percent+"%");
//			pd_progress.setVisibility(View.VISIBLE);
//			pd_progress_num.setVisibility(View.VISIBLE);
//			pd.setCanceledOnTouchOutside(false);
//			pd.setContentView(pd_admin);
//			pd.show();
			// TODO Auto-generated method stub
			new AsyncTask<Void, Void, Void>() {
				protected Void doInBackground(Void... params) 
				{	
					Upgrade_Firmware(path);
					return null;
				}
				@Override
				protected void onPostExecute(Void result) 
				{	
					pd.dismiss();
					if(_tcpSocket!=null)
		 			{
		 				_tcpSocket.Close();
		 				_tcpSocket=null;
		 			}
					if(isUpgradeOk==false){
						if(_isCancel)
		 				{
		 					return;
		 				}
						if(warnDialog1!=null)
							warnDialog1.dismiss();
//						final Dialog warnDialog= new Dialog(OTAActivity2.this,R.style.MyDialog);
//						LayoutInflater warnDialog_inflater =getLayoutInflater();
//						View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//						TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//						warn_title.setText("����");
//						warn_title.setVisibility(View.VISIBLE);
//						TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//						warn_note.setText("����ʧ��.");
//						warn_note.setVisibility(View.VISIBLE);
//						TextView warn_retry_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//						warn_retry_btn.setVisibility(View.GONE);
//						TextView warn_ap_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//						warn_ap_btn.setText("ȷ��");
//						warn_ap_btn.setVisibility(View.VISIBLE);
//						warn_ap_btn.setOnClickListener(new OnClickListener()
//						{
//
//							@Override
//							public void onClick(View v)
//							{
//								// TODO Auto-generated method stub
//								warnDialog.dismiss();
//							}
//						});
//						warnDialog.setContentView(warnDialog_admin);
//						warnDialog.show();
					}
				}
			}.execute();
		}
	};
	
	/**
	 * Upgrade Firmware
	 **/
	int percent = 0;
	Dialog warnDialog1;
    public void Upgrade_Firmware(String path) 
    {
    	percent = 0;
    	int send_len=0;
    	try
 		{	
    		int bin_len=0;
			FileInputStream read_binfile=null;
			read_binfile=new FileInputStream(path);
 			bin_len=read_binfile.available();
 			send_len=bin_len+Post_end.length();
 			byte[] buffile=new byte[send_len];
 			read_binfile.read(buffile, Post_end.length(), bin_len);
 			byte[] bufhead=Post_end.getBytes();
 			System.arraycopy(bufhead, 0, buffile, 0, Post_end.length());
 			
 			_tcpSocket=new TcpSocket();
 			if(_tcpSocket.Connect(_deviceIp, _devicePort))
 			{
 				int xx=bin_len+Post_end.length();
 				Log.e("xx==>", xx+"");
	 			String upgrademodule_filename=Post_ip + _deviceIp + Post_length + (bin_len+Post_end.length()) + Post_admin + admin_info+"\r\n\r\n";
	 			_tcpSocket.Send_Str(upgrademodule_filename);
	 			Thread.sleep(100);
	 			
	 			while(percent<100)
	 			{
	 				if(_isCancel)
	 				{
	 					break;
	 				}
		 			try 
					{
						if(send_len>512)
			 			{
			 				_tcpSocket.Send_Byte(buffile,buffile.length-send_len,512);
			 				send_len-=512;
			 				percent=((bin_len-send_len)*100/bin_len);
			 			}
			 			else
			 			{
			 				_tcpSocket.Send_Byte(buffile,buffile.length-send_len,send_len);
			 				percent=100;
			 				read_binfile.close();
				 			read_binfile=null;
				 			runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
//									warnDialog1= new Dialog(OTAActivity2.this,R.style.MyDialog);
//									LayoutInflater warnDialog_inflater =getLayoutInflater();
//									View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//									TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//									warn_title.setText("Load");
//									warn_title.setVisibility(View.VISIBLE);
//									TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//									warn_note.setText("Wait for loding...");
//									warn_note.setVisibility(View.VISIBLE);
//									TextView warn_retry_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//									warn_retry_btn.setVisibility(View.GONE);
//									TextView warn_ap_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//									warn_ap_btn.setText("OK");
//									warn_ap_btn.setVisibility(View.VISIBLE);
//									warn_ap_btn.setOnClickListener(new OnClickListener()
//									{
//
//										@Override
//										public void onClick(View v)
//										{
//											// TODO Auto-generated method stub
//											warnDialog1.dismiss();
//										}
//									});
//									warnDialog1.setContentView(warnDialog_admin);
//									warnDialog1.show();
								}
							});
				 			
				 			int count=0;
				 			while(count<20)
				 			{
				 				byte[] data=_tcpSocket.Read();
				 				if(data!=null)
				 				{
				 					String str=new String(data);
				 					if(str.contains("result=\"0\""))
				 					{
				 						isUpgradeOk=true;
				 						if(_tcpSocket!=null)
							 			{
							 				_tcpSocket.Close();
							 				_tcpSocket=null;
							 			}
				 						
				 						runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// TODO Auto-generated method stub
//												warnDialog1.dismiss();
//												LayoutInflater warnDialog_inflater =getLayoutInflater();
//												View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//												TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//												warn_title.setText("Upgrade");
//												warn_title.setVisibility(View.VISIBLE);
//												TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//												warn_note.setText("Upgrade Success!");
//												warn_note.setVisibility(View.VISIBLE);
//												TextView warn_retry_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//												warn_retry_btn.setVisibility(View.GONE);
//												TextView warn_ap_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//												warn_ap_btn.setText("OK");
//												warn_ap_btn.setVisibility(View.VISIBLE);
//												warn_ap_btn.setOnClickListener(new OnClickListener()
//												{
//
//													@Override
//													public void onClick(View v)
//													{
//														// TODO Auto-generated method stub
//														warnDialog1.dismiss();
//													}
//												});
//												warnDialog1.setContentView(warnDialog_admin);
//												warnDialog1.show();
											}
										});
				 						break;
				 					}
				 				}
				 				Thread.sleep(1000);
				 				count++;
				 			}
				 			
						}
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 			Thread.sleep(50);
	 			}
 			}
 		} 
		catch (Exception e)
 		{
 			// TODO: handle exception
 		}
    }
	
	/**
	 * Find String
	 **/	
	String findString(String srcSting,String keyString,String endKey)
	{
		String value="";
		int index=srcSting.indexOf(keyString, 0);
		 if(index!=-1){
			 int index2=srcSting.indexOf(endKey, index+keyString.length());
			 if(index2!=-1){
				 value=srcSting.substring(index+keyString.length(),index2);
			 }
		 }
		 return value;
	}
	
	 /**
	 * Http send
	 **/	
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
		            conn.setConnectTimeout(15000);//15S
		            conn.setReadTimeout(15000);//15S
		            conn.connect();  
		            if(way.equals("POST"))
		            {
			            OutputStream outStream = conn.getOutputStream();
			            outStream.write(entity);
			            outStream.flush();
			            outStream.close();	            
		            }
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
}
