package com.zunder.smart.aiui.info;

public class ScheduleInfo {
	private String scheName;
	private String scheContent;
	private String scheStartTime;
	private String scheEndTime;
	private int scheIntervalTime;
	private String scheRelation;
	private String scheDate;
	private int scheState;

	/**
	 * @return the scheState
	 */
	public int getScheState() {
		return scheState;
	}

	/**
	 * @param scheState
	 *            the scheState to set
	 */
	public void setScheState(int scheState) {
		this.scheState = scheState;
	}

	/**
	 * @return the scheDate
	 */
	 public String getScheDate() {
	 return scheDate;
	 }

	/**
	 * @param scheDate
	 *            the scheDate to set
	 */
	 public void setScheDate(String scheDate) {
	 this.scheDate = scheDate;
	 }

	public String getScheName() {
		return scheName;
	}

	public void setScheName(String scheName) {
		this.scheName = scheName;
	}

	public String getScheContent() {
		return scheContent;
	}

	public void setScheContent(String scheContent) {
		this.scheContent = scheContent;
	}

	public String getScheStartTime() {
		return scheStartTime;
	}

	public void setScheStartTime(String scheStartTime) {
		this.scheStartTime = scheStartTime;
	}

	public String getScheEndTime() {
		return scheEndTime;
	}

	public void setScheEndTime(String scheEndTime) {
		this.scheEndTime = scheEndTime;
	}

	public int getScheIntervalTime() {
		return scheIntervalTime;
	}

	public void setScheIntervalTime(int scheIntervalTime) {
		this.scheIntervalTime = scheIntervalTime;
	}

	public String getScheRelation() {
		return scheRelation;
	}

	public void setScheRelation(String scheRelation) {
		this.scheRelation = scheRelation;
	}
}
