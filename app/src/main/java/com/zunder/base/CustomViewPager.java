package com.zunder.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CustomViewPager extends ViewPager implements OnTouchListener{
	private Boolean isCallScroll;
	public CustomViewPager(Context context) {
		super(context);
		setOnTouchListener(this);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(isCallScroll){
			return super.onInterceptTouchEvent(arg0);
		}else{
			return false;
		}
	}

	public Boolean getIsCallScroll() {
		return isCallScroll;
	}

	public void setIsCallScroll(Boolean isCallScroll) {
		this.isCallScroll = isCallScroll;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return !isCallScroll;
	}

	
}
