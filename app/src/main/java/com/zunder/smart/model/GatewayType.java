package com.zunder.smart.model;

public class GatewayType {
	private int Id;
	private String GatewayTypeName;
	private String GatewayTypeImage;
	private String CreationTime;
	private int Seqencing;
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
	 * @return the gatewayTypeName
	 */
	public String getGatewayTypeName() {
		return GatewayTypeName;
	}
	/**
	 * @param gatewayTypeName the gatewayTypeName to set
	 */
	public void setGatewayTypeName(String gatewayTypeName) {
		GatewayTypeName = gatewayTypeName;
	}
	/**
	 * @return the gatewayTypeImage
	 */
	public String getGatewayTypeImage() {
		return GatewayTypeImage;
	}
	/**
	 * @param gatewayTypeImage the gatewayTypeImage to set
	 */
	public void setGatewayTypeImage(String gatewayTypeImage) {
		GatewayTypeImage = gatewayTypeImage;
	}
	/**
	 * @return the creationTime
	 */
	public String getCreationTime() {
		return CreationTime;
	}
	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}
	/**
	 * @return the seqencing
	 */
	public int getSeqencing() {
		return Seqencing;
	}
	/**
	 * @param seqencing the seqencing to set
	 */
	public void setSeqencing(int seqencing) {
		Seqencing = seqencing;
	}

	public int getTypeId() {
		return TypeId;
	}

	public void setTypeId(int typeId) {
		TypeId = typeId;
	}
}
