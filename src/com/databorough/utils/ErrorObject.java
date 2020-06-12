package com.databorough.utils;

public class ErrorObject {
	private String errorText = "";
	private String sqlCode = "";
	private String sqlState = "";

	public ErrorObject() {
	}

	public String getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(String sqlCode) {
		this.sqlCode = sqlCode;
	}

	public String getSqlState() {
		return sqlState;
	}

	public void setSqlState(String sqlState) {
		this.sqlState = sqlState;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
}