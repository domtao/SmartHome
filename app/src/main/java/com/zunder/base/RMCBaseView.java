package com.zunder.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import com.zunder.smart.MyApplication;

@SuppressWarnings("deprecation")
public abstract class RMCBaseView extends LinearLayout{

	protected Context _context;
	private int tabViewTag = -1;

	private String backgroundString="";

	public RMCBaseView(Context context,AttributeSet attrs){
		super(context, attrs);
		_context=context;
	}
	
	public RMCBaseView(Context context) {
		super(context);
		_context=context;
		
	}


	public int getTabViewTag() {
		return tabViewTag;
	}

	public void setTabViewTag(int tabViewTag) {
		this.tabViewTag = tabViewTag;
	}


	private String params;

	public  void setParams(String p) {
		try {
			params = p;
			String ps[] = p.split(",");
			int pi[] = new int[ps.length];
			for (int i = 0; i < ps.length; i++) {
				pi[i] = Integer.parseInt(ps[i]);
			}
			setParams(pi[0],pi[1], pi[2], pi[3]);
		} catch (NumberFormatException e) {
			System.out.println(this.getClass().toString() + ":" + e);
		}
	}
	
	public void setParams(int w,int h,int x,int y){
		this.setLayoutParams(new AbsoluteLayout.LayoutParams(w,h,x,y));
	}


	public String getParams() {
		return params;
	}
	
	public void hide(){
		this.setVisibility(View.GONE);
	}


	public void display() {
		this.setVisibility(View.VISIBLE);
		
	}

	 protected void init(){
		 register();
	 }
	

	
	public String getBackgroundProperties() {
		return backgroundString;
	}

	//�����Զ��屳��
	public void setBackgroundFromProperties(String backgroundString) {
//		if(!TextUtils.isEmpty(backgroundString)){
//			try {
//				this.setBackgroundDrawable(MyApplication.getInstance().getStateListDrawableFromProperties(backgroundString));
//				this.backgroundString = backgroundString;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//			this.backgroundString="";
//		}
	}

	@Override
	public String toString() {
		 return getResources().getString(getTitleResId())+"\r\nID: "+getTag()+"\r\n�ߴ�:"+getWidth()+"x"+getHeight();
	}

	
	abstract protected int getTitleResId();
	
	 public void release(){
		 unregister() ;
	 }
	
	
	private UpdateViewBroadcast mBroadcast;
	// ע��㲥
	private void register() {
		if(!TextUtils.isEmpty(getBroadcastAction())){
			mBroadcast = new UpdateViewBroadcast();
			IntentFilter filter = new IntentFilter(getBroadcastAction());
			_context.registerReceiver(mBroadcast, filter);
		}
	}

	// ע���㲥
	private void unregister() {
		try {
			if(mBroadcast!=null){
				_context.unregisterReceiver(mBroadcast);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(this.getClass().toString()+":::"+e.toString());
		}
	}

    private class UpdateViewBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			try {
				doUpdate(intent.getExtras());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(this.getClass()+":"+e.toString());
			}
		}
		
	}
	
	 public void doUpdate(Bundle bundle) throws Exception{}
	 public String getBroadcastAction(){ return "";}
	 
		private String color;
		public void setTextColor(String color) {this.color=color;}
		public String getTextColor(){return color;}

		private Typeface tf;
		public void setTypeface(Typeface tf) {this.tf=tf;}
		public Typeface getTypeface(){return tf;}
}
