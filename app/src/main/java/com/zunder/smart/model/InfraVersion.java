package com.zunder.smart.model;

public class InfraVersion {
	private int Id;
	private String versionName;
	private int infraID;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getInfraID() {
		return infraID;
	}

	public void setInfraID(int infraID) {
		this.infraID = infraID;
	}
}
