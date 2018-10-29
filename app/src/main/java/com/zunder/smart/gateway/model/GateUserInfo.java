package com.zunder.smart.gateway.model;

public class GateUserInfo {
	private String pwd1;
	private String pwd2;
	private String pwd3;
	private String user1;
	private String user2;
	private String user3;

	public String getPwd1() {
		if (pwd1 == null || pwd1.equals("null")) {
			return "";
		}
		return pwd1;
	}

	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}

	public String getPwd2() {if (pwd2 == null || pwd2.equals("null")) {
		return "";
	}
		return pwd2;
	}

	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}

	public String getPwd3() {
		if (pwd3 == null || pwd3.equals("null")) {
			return "";
		}
		return pwd3;
	}

	public void setPwd3(String pwd3) {
		this.pwd3 = pwd3;
	}

	public String getUser1() {
		if (user1 == null || user1.equals("null")) {
			return "";
		}
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getUser2() {
		if (user2 == null || user2.equals("null")) {
			return "";
		}
		return user2;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}

	public String getUser3() {
		if (user3 == null || user3.equals("null")) {
			return "";
		}
		return user3;
	}

	public void setUser3(String user3) {
		this.user3 = user3;
	}

}
