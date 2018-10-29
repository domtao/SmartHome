package com.zunder.smart.model;

import android.graphics.Bitmap;

public class SmartFile {
	private String fileName;
	private String fileTime;
	private Bitmap bitmap;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileTime
	 */
	public String getFileTime() {
		return fileTime;
	}

	/**
	 * @param fileTime
	 *            the fileTime to set
	 */
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}

	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap
	 *            the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
