package com.zunder.smart.model;

public class VoiceInfo {

	private int Id;
	private String VoiceName;
	private String VoiceAnswer;
	private String VoiceAction;
	private String UserName;

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
	 * @return the vocieName
	 */
	public String getVoiceName() {
		return VoiceName;
	}


	public void setVoiceName(String voiceName) {
		VoiceName = voiceName;
	}

	/**
	 * @return the voiceAnswer
	 */
	public String getVoiceAnswer() {
		if(VoiceAnswer==null||VoiceAnswer=="null"){
			VoiceAnswer="";
		}
		return VoiceAnswer;
	}

	/**
	 * @param voiceAnswer
	 *            the voiceAnswer to set
	 */
	public void setVoiceAnswer(String voiceAnswer) {
		VoiceAnswer = voiceAnswer;
	}
	/**
	 * @return the voiceAction
	 */
	public String getVoiceAction() {
		if(VoiceAction==null||VoiceAction=="null"){
			VoiceAction="";
		}
		return VoiceAction;
	}

	/**
	 * @param voiceAction
	 *            the voiceAction to set
	 */
	public void setVoiceAction(String voiceAction) {
		VoiceAction = voiceAction;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		if(UserName==null||UserName==""){
			UserName="0";
		}
		return UserName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}

	public String convertTostring() {

		return "\"Id\":\"" + getId() + "\",\"VoiceName\":\"" + getVoiceName()
				+ "\",\"VoiceAnswer\":\"" + getVoiceAnswer()
				+ "\",\"VoiceAction\":\"" + getVoiceAction()
				+ "\",\"UserName\":\"" + getUserName() + "\"";
	}
}