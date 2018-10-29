package com.zunder.smart.model;

public class Passive {
	private int id;
	private String cmdString;
	private String memIndex;
	private int type;
	private String name;
	private int image;
	private String subCommand;
	private String onStart;
	public String getCmdString() {
		return cmdString;
	}
	public void setCmdString(String cmdString) {
		this.cmdString = cmdString;
	}
	public String getMemIndex() {
		return memIndex;
	}
	public void setMemIndex(String memIndex) {
		this.memIndex = memIndex;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getSubCommand() {
		return subCommand;
	}
	public void setSubCommand(String subCommand) {
		this.subCommand = subCommand;
	}
	public String getOnStart() {
		return onStart;
	}
	public void setOnStart(String onStart) {
		this.onStart = onStart;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
