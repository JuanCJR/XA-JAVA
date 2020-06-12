package com.databorough.utils;

public class NewScreenException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String nxtPgm;
	private String nxtScn;

	public NewScreenException(String nxtScn, String nxtPgm) {
		this.nxtScn = nxtScn;
		this.nxtPgm = nxtPgm;
	}

	public String getNextProgram() {
		return nxtPgm;
	}

	public String getNextScreen() {
		return nxtScn;
	}

	public void setNextProgram(String nxtPgm) {
		this.nxtPgm = nxtPgm;
	}

	public void setNextScreen(String nxtScn) {
		this.nxtScn = nxtScn;
	}
}