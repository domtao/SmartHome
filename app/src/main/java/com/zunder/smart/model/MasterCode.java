package com.zunder.smart.model;

public class MasterCode {
	private int Id;
	private String MasterMac;
	private String MasterCod;

	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return the masterMac
	 */
	public String getMasterMac() {
		return MasterMac;
	}

	/**
	 * @param masterMac
	 *            the masterMac to set
	 */
	public void setMasterMac(String masterMac) {
		MasterMac = masterMac;
	}

	/**
	 * @return the masterCode
	 */
	public String getMasterCod() {
		return MasterCod;
	}

	/**
	 * @param masterCode
	 *            the masterCode to set
	 */
	public void setMasterCod(String masterCode) {
		MasterCod = masterCode;
	}

}