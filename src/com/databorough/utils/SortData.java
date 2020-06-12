package com.databorough.utils;

import java.io.Serializable;

public class SortData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String property;
	private boolean desc;
	private boolean ignoreCase;

	public SortData(String property, boolean desc, boolean ignoreCase) {
		this.property = property;
		this.desc = desc;
		this.ignoreCase = ignoreCase;
	}

	public SortData(String property, boolean desc) {
		this.property = property;
		this.desc = desc;
	}

	public String getProperty() {
		return property;
	}

	public boolean isDesc() {
		return desc;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("SortData[" + "property=" + property + "," + "desc=" + desc +
			"," + "ignoreCase=" + ignoreCase + "]");

		return str.toString();
	}
}