package com.zunder.smart.rak47;

import java.io.File;

import com.zunder.smart.R;
import com.zunder.smart.rak47.apconfig.ApconfigStep1;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity implements OnCheckedChangeListener{
	public static TabHost mTabHost;
	private Intent m1Intent;
	private Intent m2Intent;
//	private Intent m3Intent;
	
	public static RadioButton radio_button0;
	public static RadioButton radio_button1;
//	public static RadioButton radio_button2;
	
	public static String RAK47X="/RAK47X";
    public static String RAK47X_RAK473="/RAK47X/RAK473";
    public static String RAK47X_RAK475="/RAK47X/RAK475";
    public static String RAK47X_RAK476="/RAK47X/RAK476";
    public static String RAK47X_RAK477="/RAK47X/RAK477";
    public static String RAK47X_Path="";
    public static String RAK47X_RAK473_Path="";
    public static String RAK47X_RAK475_Path="";
    public static String RAK47X_RAK476_Path="";
    public static String RAK47X_RAK477_Path="";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        setContentView(R.layout.maintabs);
        
//        SharedPreferences p = getSharedPreferences("module_type", MODE_PRIVATE);
//  	  	ApconfigStep1.type=p.getString("type", "RAK477");

        this.m2Intent = new Intent(this,ApconfigStep1.class);
//        this.m3Intent = new Intent(this,OTAActivity.class);
        
        //ʵ�����ؼ�
        radio_button0=((RadioButton) findViewById(R.id.radio_button0));
        radio_button0.setOnCheckedChangeListener(this);
        radio_button1=((RadioButton) findViewById(R.id.radio_button1));
		radio_button1.setOnCheckedChangeListener(this);
        
        setupIntent();    
        createSDCardDir(RAK47X);
        createSDCardDir(RAK47X_RAK473);
        createSDCardDir(RAK47X_RAK475);
        createSDCardDir(RAK47X_RAK476);
        createSDCardDir(RAK47X_RAK477);
    }
    
    Drawable drawable;
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("SCAN");
				radio_button0.setTextColor(Color.rgb(219, 67, 65));
				radio_button1.setTextColor(Color.rgb(0, 0, 0));
//				radio_button2.setTextColor(Color.rgb(0, 0, 0));
				drawable=this.getResources().getDrawable(R.mipmap.device_true);
				radio_button0.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				drawable=this.getResources().getDrawable(R.mipmap.add_false);
				radio_button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				drawable=this.getResources().getDrawable(R.mipmap.about_false);
//				radio_button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("CONFIG");
				radio_button0.setTextColor(Color.rgb(0, 0, 0));
				radio_button1.setTextColor(Color.rgb(219, 67, 65));
//				radio_button2.setTextColor(Color.rgb(0, 0, 0));
				drawable=this.getResources().getDrawable(R.mipmap.device_false);
				radio_button0.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				drawable=this.getResources().getDrawable(R.mipmap.add_true);
				radio_button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				drawable=this.getResources().getDrawable(R.mipmap.about_false);
//				radio_button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("OTA");
				radio_button0.setTextColor(Color.rgb(0, 0, 0));
				radio_button1.setTextColor(Color.rgb(0, 0, 0));
//				radio_button2.setTextColor(Color.rgb(219, 67, 65));
				drawable=this.getResources().getDrawable(R.mipmap.device_false);
				radio_button0.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				drawable=this.getResources().getDrawable(R.mipmap.add_false);
				radio_button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				drawable=this.getResources().getDrawable(R.mipmap.about_true);
//				radio_button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
				break;
			}
		}
		
	}
	
	private void setupIntent() 
	{
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("SCAN", R.string.main_local_scan,
				R.mipmap.device_true, this.m1Intent));
		
		localTabHost.addTab(buildTabSpec("CONFIG",R.string.main_config, 
				R.mipmap.add_false,this.m2Intent));

//		localTabHost.addTab(buildTabSpec("OTA", R.string.main_ota,
//				R.mipmap.about_false, this.m3Intent));
	}
	
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, final Intent content) 
	{
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	}
	
	@Override
	protected void onDestroy() 
	{
        // TODO Auto-generated method stub
        super.onDestroy();
    }  
	
	 public void createSDCardDir(String path)
	{
		//����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
         File sdcardDir =Environment.getExternalStorageDirectory();
         //�õ�һ��·����������sdcard���ļ���·��������
         String pathcat=sdcardDir.getPath()+path;
         File path1 = new File(pathcat);

         if (!path1.exists())
		 {
			 path1.mkdirs();
             setTitle("path ok,path:"+path);
         }
         RAK47X_Path=sdcardDir.getPath()+RAK47X;
         RAK47X_RAK473_Path=sdcardDir.getPath()+RAK47X_RAK473;
         RAK47X_RAK475_Path=sdcardDir.getPath()+RAK47X_RAK475;
         RAK47X_RAK476_Path=sdcardDir.getPath()+RAK47X_RAK476;
         RAK47X_RAK477_Path=sdcardDir.getPath()+RAK47X_RAK477;
	}
}