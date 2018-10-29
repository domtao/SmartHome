package com.zunder.smart.model;

public class SocketInfo {
	private String Service;
	private String ToID;;
	private int State;
	private String SendID;
	private String Message;
	/**
	 * @return the service
	 */
	public String getService() {
		return Service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		Service = service;
	}
	/**
	 * @return the toID
	 */
	public String getToID() {
		return ToID;
	}
	/**
	 * @param toID the toID to set
	 */
	public void setToID(String toID) {
		ToID = toID;
	}
	/**
	 * @return the state
	 */
	public int getState() {
		return State;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		State = state;
	}
	/**
	 * @return the sendID
	 */
	public String getSendID() {
		return SendID;
	}
	/**
	 * @param sendID the sendID to set
	 */
	public void setSendID(String sendID) {
		SendID = sendID;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}
	

}
