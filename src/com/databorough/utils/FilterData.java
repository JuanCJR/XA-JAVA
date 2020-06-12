package com.databorough.utils;

import java.io.Serializable;

public class FilterData implements Serializable {
	private static final long serialVersionUID = 1L;

	// com.trg.search.Filter constants
	public static final int OP_EQUAL = 0;
	public static final int OP_LESS_THAN = 2;
	public static final int OP_GREATER_THAN = 3;
	public static final int OP_LESS_OR_EQUAL = 4;
	public static final int OP_GREATER_OR_EQUAL = 5;
	public static final int OP_LIKE = 6;
	public static final int OP_ILIKE = 7;

	public static final int KEYFLD = 11;
	public static final int RSTFLD = 12;
	public static final int FLTRFLD = 13;

	private Object value;
	private String fldDataType;
	private String property;
	private int fldType;
	private int fldWidth;
	private int operator;

	public FilterData(String property, Object value, int operator,
		int fldWidth, int fldType, String fldDataType) {
		this.property = property;
		this.value = value;
		this.operator = operator;
		this.fldWidth = fldWidth;
		this.fldType = fldType;
		this.fldDataType = fldDataType;
	}

	public FilterData(String property, Object value, int operator, int fldType,
		String fldDataType) {
		this.property = property;
		this.value = value;
		this.operator = operator;
		this.fldType = fldType;
		this.fldDataType = fldDataType;
	}

	public FilterData(String property, Object value, int operator) {
		this.property = property;
		this.value = value;
		this.operator = operator;
		this.fldType = KEYFLD;
	}

	public FilterData(String property, Object value) {
		this.property = property;
		this.value = value;
		this.operator = OP_EQUAL;
		this.fldType = KEYFLD;
	}

	public static FilterData equal(String property, Object value) {
		return new FilterData(property, value, OP_EQUAL);
	}

	private String getComp() {
		if (operator == OP_EQUAL) {
			return "=";
		}

		if (operator == OP_LESS_THAN) {
			return "<";
		}

		if (operator == OP_GREATER_THAN) {
			return ">";
		}

		if (operator == OP_LESS_OR_EQUAL) {
			return "<=";
		}

		if (operator == OP_GREATER_OR_EQUAL) {
			return ">=";
		}

		if (operator == OP_LIKE) {
			return "LIKE";
		}

		if (operator == OP_ILIKE) {
			return "ILIKE";
		}

		return "";
	}

	public String getFldDataType() {
		return fldDataType;
	}

	public int getFldType() {
		return fldType;
	}

	public int getFldWidth() {
		return fldWidth;
	}

	public int getOperator() {
		return operator;
	}

	public String getProperty() {
		return property;
	}

	public Object getValue() {
		return value;
	}

	public static FilterData greaterOrEqual(String property, Object value) {
		return new FilterData(property, value, OP_GREATER_OR_EQUAL);
	}

	public static FilterData greaterThan(String property, Object value) {
		return new FilterData(property, value, OP_GREATER_THAN);
	}

	public static FilterData ilike(String property, String value) {
		return new FilterData(property, value, OP_ILIKE);
	}

	public static FilterData lessOrEqual(String property, Object value) {
		return new FilterData(property, value, OP_LESS_OR_EQUAL);
	}

	public static FilterData lessThan(String property, Object value) {
		return new FilterData(property, value, OP_LESS_THAN);
	}

	public static FilterData like(String property, String value) {
		return new FilterData(property, value, OP_LIKE);
	}

	public void setFldDataType(String fldDataType) {
		this.fldDataType = fldDataType;
	}

	public void setFldWidth(int fldWidth) {
		this.fldWidth = fldWidth;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("FilterData[" + property + " " + getComp() + " ");

		if (value instanceof String) {
			str.append(((String)value).trim());
		}
		else {
			str.append(value);
		}

		str.append("]");

		return str.toString();
	}
}