package com.zunder.smart.gateway.bean;

public class RootPath {
	private String rootPath;
	private String rootName;
	private String rootImage;
	private int rootWay;
	private String rootID;
	private int rootVersion;
	private int connectState;
	private int messageBack;
	private int language;
	private int playSource;
	private int controlState;
	private String AlarmId;

	public int getControlState() {
		return controlState;
	}

	public void setControlState(int controlState) {
		this.controlState = controlState;
	}
	public int getPlaySource() {
		return playSource;
	}

	public void setPlaySource(int playSource) {
		this.playSource = playSource;
	}

	/**
	 * @return the language
	 */
	public int getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(int language) {
		this.language = language;
	}

	/**
	 * @return the messageBack
	 */
	public int getMessageBack() {
		return messageBack;
	}

	/**
	 * @param messageBack
	 *            the messageBack to set
	 */
	public void setMessageBack(int messageBack) {
		this.messageBack = messageBack;
	}

	/**
	 * @return the connectState
	 */
	public int getConnectState() {
		return connectState;
	}

	/**
	 * @param connectState
	 *            the connectState to set
	 */
	public void setConnectState(int connectState) {
		this.connectState = connectState;
	}

	/**
	 * @return the rootPath
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * @param rootPath
	 *            the rootPath to set
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	/**
	 * @return the rootName
	 */
	public String getRootName() {
		return rootName;
	}

	/**
	 * @param rootName
	 *            the rootName to set
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	/**
	 * @return the rootWay
	 */
	public int getRootWay() {
		return rootWay;
	}

	/**
	 * @param rootWay
	 *            the rootWay to set
	 */
	public void setRootWay(int rootWay) {
		this.rootWay = rootWay;
	}

	/**
	 * @return the rootID
	 */
	public String getRootID() {
		return rootID;
	}

	/**
	 * @param rootID
	 *            the rootID to set
	 */
	public void setRootID(String rootID) {
		this.rootID = rootID;
	}

	/**
	 * @return the rootVersion
	 */
	public int getRootVersion() {
		return rootVersion;
	}

	/**
	 * @param rootVersion
	 *            the rootVersion to set
	 */
	public void setRootVersion(int rootVersion) {
		this.rootVersion = rootVersion;
	}

	//AlarmId
	/**
	 * @return the AlarmId
	 */
	public String getAlarmId() {
		return AlarmId;
	}

	/**
	 * @param AlarmId
	 *            the AlarmId to set
	 */
	public void setAlarmId(String AlarmId) {
		this.AlarmId = AlarmId;
	}

	public String getRootImage() {
		return rootImage;
	}

	public void setRootImage(String rootImage) {
		this.rootImage = rootImage;
	}
}
