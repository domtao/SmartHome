package com.zunder.smart.aiui.info;

public class NetMusic {
	private String musicName;
	private String musicUrl;
	private String musicAuthor;
	private String musicTime;
	private int musicType;

	public int getMusicType() {
		return musicType;
	}

	public void setMusicType(int musicType) {
		this.musicType = musicType;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public String getMusicAuthor() {
		return musicAuthor;
	}

	public void setMusicAuthor(String musicAuthor) {
		this.musicAuthor = musicAuthor;
	}

	public String getMusicTime() {
		return musicTime;
	}

	public void setMusicTime(String musicTime) {
		this.musicTime = musicTime;
	}

	public String getMusicDes() {
		return musicDes;
	}

	public void setMusicDes(String musicDes) {
		this.musicDes = musicDes;
	}

	private String musicDes;
}
