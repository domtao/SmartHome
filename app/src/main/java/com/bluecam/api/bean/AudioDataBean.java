package com.bluecam.api.bean;

public class AudioDataBean {
	private int length;
	private int startcode;
	private byte[] data;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getStartcode() {
		return startcode;
	}

	public void setStartcode(int startcode) {
		this.startcode = startcode;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
