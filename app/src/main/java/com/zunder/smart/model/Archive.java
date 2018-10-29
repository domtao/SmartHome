package com.zunder.smart.model;

import android.graphics.Bitmap;

public class Archive {
	private int Id;
	private String ProjectName;
	private String ProjectTime;
	private String ProjectPwd;
	private int ProjectNum;
	private String ProjectKey;
	private String primaryKey;
	private Bitmap ProjectImage;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getProjectTime() {
		return ProjectTime;
	}

	public void setProjectTime(String projectTime) {
		ProjectTime = projectTime;
	}

	public String getProjectPwd() {
		return ProjectPwd;
	}

	public void setProjectPwd(String projectPwd) {
		ProjectPwd = projectPwd;
	}

	public int getProjectNum() {
		return ProjectNum;
	}

	public void setProjectNum(int projectNum) {
		ProjectNum = projectNum;
	}

	public String getProjectKey() {
		return ProjectKey;
	}

	public void setProjectKey(String projectKey) {
		ProjectKey = projectKey;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Bitmap getProjectImage() {
		return ProjectImage;
	}

	public void setProjectImage(Bitmap projectImage) {
		ProjectImage = projectImage;
	}
}
