package com.zunder.smart.model;

public class DeviceAction {
	private int Id;
	private String ActionName;
	private String ActionLanguage;
	private int TypeId;
	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return ActionName;
	}
	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		ActionName = actionName;
	}
	/**
	 * @return the actionLanguage
	 */
	public String getActionLanguage() {
		return ActionLanguage;
	}
	/**
	 * @param actionLanguage the actionLanguage to set
	 */
	public void setActionLanguage(String actionLanguage) {
		ActionLanguage = actionLanguage;
	}
	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return TypeId;
	}
	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		TypeId = typeId;
	}

}
