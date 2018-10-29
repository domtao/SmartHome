package com.zunder.image.model;

public class ImageNet {

	private int Id ;
	private String ImageName ;
	private String ImageAlia;
	private String ImageUrl ;
	private int TypeId;
	private int CompanyId ;
	private String CreateTime;
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getImageName() {
		return ImageName;
	}

	public void setImageName(String imageName) {
		ImageName = imageName;
	}

	public String getImageAlia() {
		return ImageAlia;
	}

	public void setImageAlia(String imageAlia) {
		ImageAlia = imageAlia;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	public int getTypeId() {
		return TypeId;
	}

	public void setTypeId(int typeId) {
		TypeId = typeId;
	}

	public int getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(int companyId) {
		CompanyId = companyId;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}



}
