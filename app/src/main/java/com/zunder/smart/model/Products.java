package com.zunder.smart.model;

import android.graphics.Bitmap;

public class Products {

	private int Id;
	private String ProductsImage;
	private String ProductsName;
	private String ProductsCode;
	private String CreationTime;
	private int ProductsKey;
	private int DeviceTypekey;
	private int ProductsIO;
	private int IsSwitch;
	private int ActionShow;
	private int FunctionShow;
	private int ActionPramShow;
	private int FunctionParamShow;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getProductsImage() {
		return ProductsImage;
	}

	public void setProductsImage(String productsImage) {
		ProductsImage = productsImage;
	}

	public String getProductsName() {
		return ProductsName;
	}

	public void setProductsName(String productsName) {
		ProductsName = productsName;
	}

	public String getProductsCode() {
		if(ProductsCode==null||ProductsCode=="null"||ProductsCode.equals("null")){
			ProductsCode="";
		}
		return ProductsCode;
	}

	public void setProductsCode(String productsCode) {
		ProductsCode = productsCode;
	}

	public String getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}

	public int getProductsKey() {
		return ProductsKey;
	}

	public void setProductsKey(int productsKey) {
		ProductsKey = productsKey;
	}

	public int getProductsIO() {
		return ProductsIO;
	}

	public void setProductsIO(int productsIO) {
		ProductsIO = productsIO;
	}

	public int getIsSwitch() {
		return IsSwitch;
	}

	public void setIsSwitch(int isSwitch) {
		IsSwitch = isSwitch;
	}

	public int getActionShow() {
		return ActionShow;
	}

	public void setActionShow(int actionShow) {
		ActionShow = actionShow;
	}

	public int getFunctionShow() {
		return FunctionShow;
	}

	public void setFunctionShow(int functionShow) {
		FunctionShow = functionShow;
	}

	public int getActionPramShow() {
		return ActionPramShow;
	}

	public void setActionPramShow(int actionPramShow) {
		ActionPramShow = actionPramShow;
	}

	public int getFunctionParamShow() {
		return FunctionParamShow;
	}

	public void setFunctionParamShow(int functionParamShow) {
		FunctionParamShow = functionParamShow;
	}

	public int getDeviceTypekey() {
		return DeviceTypekey;
	}

	public void setDeviceTypekey(int deviceTypekey) {
		DeviceTypekey = deviceTypekey;
	}
}
