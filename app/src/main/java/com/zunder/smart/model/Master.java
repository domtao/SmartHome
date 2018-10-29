package com.zunder.smart.model;

public class Master {
	private String Mn;
	private String Mc;
	private String Wf;
	private int Ty;
	private int St;
	private String Un;
	private String Pw;
	private String Or;
	private String Op;

	public String getMn() {
		return Mn;
	}

	public void setMn(String mn) {
		Mn = mn;
	}

	public String getMc() {
		if (Mc == null) {
			return "000";
		}
		return Mc;
	}

	public void setMc(String mc) {
		Mc = mc;
	}

	public String getWf() {
		return Wf;
	}

	public void setWf(String wf) {
		Wf = wf;
	}

	public int getTy() {
		return Ty;
	}

	public void setTy(int ty) {
		Ty = ty;
	}

	public int getSt() {
		return St;
	}

	public void setSt(int st) {
		St = st;
	}

	public String getUn() {
		return Un;
	}

	public void setUn(String un) {
		Un = un;
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
