package com.zunder.smart.model;

public class DeviceFunction {
	private int Id;
	private String FunctionName;
	private String FunctionLanguage;
	private int TypeId;

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
	 * @return the functionName
	 */
	public String getFunctionName() {
		return FunctionName;
	}

	/**
	 * @param functionName
	 *            the functionName to set
	 */
	public void setFunctionName(String functionName) {
		FunctionName = functionName;
	}

	/**
	 * @return the functionLanguage
	 */
	public String getFunctionLanguage() {
		return FunctionLanguage;
	}

	/**
	 * @param functionLanguage
	 *            the functionLanguage to set
	 */
	public void setFunctionLanguage(String functionLanguage) {
		FunctionLanguage = functionLanguage;
	}

	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return TypeId;
	}

	/**
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(int typeId) {
		TypeId = typeId;
	}

}
