package com.zunder.base;

import android.graphics.drawable.Drawable;

public class DrawableData {
	private Drawable mDrawable;
	private String name;
	public Drawable getDrawable() {
		return mDrawable;
	}
	public void setDrawable(Drawable mDrawable) {
		this.mDrawable = mDrawable;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DrawableData(Drawable mDrawable, String name) {
		super();
		this.mDrawable = mDrawable;
		this.name = name;
	}
	
	
}
