package acdemxaMvcprocess.logic.data;

import java.io.Serializable;

public class FilterFldData implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer opType = 0;
	private String fltFld = "";
	private String fltName = "";

	public FilterFldData() {
	}

	public FilterFldData(String fltName, String fltFld, int opType) {
		this.fltName = fltName;
		this.fltFld = fltFld;
		this.opType = opType;
	}

	public String getFltFld() {
		return fltFld;
	}

	public String getFltName() {
		return fltName;
	}

	public Integer getOpType() {
		return opType;
	}

	public void setFltFld(String fltFld) {
		this.fltFld = fltFld;
	}

	public void setFltName(String fltName) {
		this.fltName = fltName;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("FilterFldData[" + "fltName=" + fltName + "," + "fltFld=" +
			fltFld + "," + "opType=" + opType + "]");

		return str.toString();
	}
}
