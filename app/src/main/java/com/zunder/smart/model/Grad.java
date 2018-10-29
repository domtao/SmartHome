package com.zunder.smart.model;

public class Grad {
	private int Id;
	private String Grad_Name;
	private byte[] Grad_Image;
	private int Grad_ID;
	private String CreationTime;
	private int Seqencing;

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
	 * @return the grad_Name
	 */
	public String getGrad_Name() {
		return Grad_Name;
	}

	/**
	 * @param grad_Name
	 *            the grad_Name to set
	 */
	public void setGrad_Name(String grad_Name) {
		Grad_Name = grad_Name;
	}

	/**
	 * @return the grad_Image
	 */
	public byte[] getGrad_Image() {
		return Grad_Image;
	}

	/**
	 * @param grad_Image
	 *            the grad_Image to set
	 */
	public void setGrad_Image(byte[] grad_Image) {
		Grad_Image = grad_Image;
	}

	/**
	 * @return the grad_ID
	 */
	public int getGrad_ID() {
		return Grad_ID;
	}

	/**
	 * @param grad_ID
	 *            the grad_ID to set
	 */
	public void setGrad_ID(int grad_ID) {
		Grad_ID = grad_ID;
	}

	/**
	 * @return the creationTime
	 */
	public String getCreationTime() {
		return CreationTime;
	}

	/**
	 * @param creationTime
	 *            the creationTime to set
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
	 * @param seqencing
	 *            the seqencing to set
	 */
	public void setSeqencing(int seqencing) {
		Seqencing = seqencing;
	}

}
