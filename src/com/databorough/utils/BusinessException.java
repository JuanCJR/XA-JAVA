package com.databorough.utils;

//@javax.ejb.ApplicationException(rollback = true)
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String[] errorField;
	private String errorMessage;

	public BusinessException(String[] errorField, String errorMessage) {
		this.errorField = errorField;
		this.errorMessage = errorMessage;
	}

	public String[] getErrorField() {
		return errorField;
	}

	public void setErrorField(String[] errorField) {
		this.errorField = errorField;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}