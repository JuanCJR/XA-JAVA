package com.databorough.utils;

import java.io.Serializable;

import javax.persistence.Transient;
import static com.databorough.utils.StringUtils.all;

public class GridDataObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private String checkboxStyle;
	private String indicators = all("0", 99);
	private String sel = "";
	private boolean checked;
	private boolean dirty;
	private boolean grdQuMarkFlag;
	private boolean positionTo;

	@Transient
	public String getCheckboxStyle() {
		return checkboxStyle;
	}

	@Transient
	public String getIndicators() {
		return indicators;
	}

	@Transient
	public String getSel() {
		return sel;
	}

	@Transient
	public String getStyle() {
		if (isReadonly()) {
			return "ReadOnly";
		}

		return "Editable";
	}

	@Transient
	public String getStyleEditable() {
		if (isReadonly()) {
			return "Editable";
		}

		return "ReadOnly";
	}

	@Transient
	public String getStyleSelector() {
		if (isReadonly()) {
			return "styleSelector";
		}

		return "ReadOnly";
	}

	@Transient
	public boolean isChecked() {
		return checked;
	}

	@Transient
	public boolean isDirty() {
		return dirty;
	}

	@Transient
	public boolean isGrdQuMarkFlag() {
		return grdQuMarkFlag;
	}

	@Transient
	public boolean isPositionTo() {
		return positionTo;
	}

	@Transient
	public boolean isReadonly() {
		return !positionTo;
	}

	@Transient
	public boolean isReadonlyEditable() {
		return positionTo;
	}

	public void setCheckboxStyle(String checkboxStyle) {
		this.checkboxStyle = checkboxStyle;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void setGrdQuMarkFlag(boolean grdQuMarkProcessing) {
		this.grdQuMarkFlag = grdQuMarkProcessing;
	}

	public void setIndicators(String indicators) {
		this.indicators = indicators;
	}

	public void setPositionTo(boolean positionTo) {
		this.positionTo = positionTo;
	}

	public void setSel(String sel) {
		this.sel = sel;
	}
}