package com.zunder.smart.aiui.info;

public class CusDevice {
	public int id;
	public String deviceName;
	public String deviceDes;
	public String deviceType;
	public String deviceDid;
	public String deviceIo;
	public int deviceTypeId;
	private int deviceProductId;

	/**
	 * @return the deviceTypeId
	 */
	public int getDeviceTypeKey() {
		return deviceTypeId;
	}

	/**
	 * @param deviceTypeId
	 *            the deviceTypeId to set
	 */
	public void setDeviceTypeId(int deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	/**
	 * @return the deviceProductId
	 */
	public int getDeviceProductId() {
		return deviceProductId;
	}

	/**
	 * @param deviceProductId
	 *            the deviceProductId to set
	 */
	public void setDeviceProductId(int deviceProductId) {
		this.deviceProductId = deviceProductId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the deviceDes
	 */
	public String getDeviceDes() {
		return deviceDes;
	}

	/**
	 * @param deviceDes
	 *            the deviceDes to set
	 */
	public void setDeviceDes(String deviceDes) {
		this.deviceDes = deviceDes;
	}

	/**
	 * @return the deviceType
	 */
	public String getProductsCode() {
		return deviceType;
	}

	/**
	 * @param deviceType
	 *            the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the deviceDid
	 */
	public String getDeviceDid() {
		return deviceDid;
	}

	/**
	 * @param deviceDid
	 *            the deviceDid to set
	 */
	public void setDeviceDid(String deviceDid) {
		this.deviceDid = deviceDid;
	}

	/**
	 * @return the deviceIo
	 */
	public String getDeviceIo() {
		return deviceIo;
	}

	/**
	 * @param deviceIo
	 *            the deviceIo to set
	 */
	public void setDeviceIo(String deviceIo) {
		this.deviceIo = deviceIo;
	}
}
