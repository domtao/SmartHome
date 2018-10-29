package com.zunder.smart.model;

public class MasterRak {
	private String Mn;
	private String Pw;
	private String Or;
	private String Op;

	public String getMn() {
		if (Mn == null || Mn == "null") {
			return "";
		}
		return Mn;
	}

	public void setMn(String mn) {
		Mn = mn;
	}

	public String getPw() {
		if (Pw == null || Pw == "null") {
			return "";
		}
		return Pw;
	}

	public void setPw(String pw) {
		Pw = pw;
	}

	public String getOr() {
		if (Or == null || Or == "null") {
			return "";
		}
		return Or;
	}

	public void setOr(String or) {
		Or = or;
	}

	public String getOp() {
		if (Op == null || Op == "null") {
			return "";
		}
		return Op;
	}

	public void setOp(String op) {
		Op = op;
	}

}
