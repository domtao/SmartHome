package com.zunder.smart.menu.model;

import android.graphics.drawable.Drawable;


public class MenuItem {
	private String name;
	private Drawable drawable;
	
	
	public MenuItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MenuItem(String name, Drawable drawable) {
		super();
		this.name = name;
		this.drawable = drawable;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	
	
}
