package com.databorough.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Column;

import acdemxaMvcprocess.logic.data.FilterFldData;
import static com.databorough.utils.StringUtils.all;

/**
 * Common fields of StateVar.
 *
 * @author Amit Arya
 * @since (2012-11-02.11:52:12)
 */
public class PanelDataObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<FilterFldData> filterFlds;// Session var
	private Integer growstart = 0;
	private Integer sflrrn = 0;
	private Integer znxtpgrrn = 0;
	private Integer zscanlim = 0;
	private Integer ztotrcread = 0;
	private Map<String, Boolean> fldsFiltered;// Session var
	private String f4field = "";
	private String fncrecfnd = "";
	private String grdcsropn = "";
	private String mode1 = "C";// Session var
	private String qin = all("0", 99);
	private String sel = "";
	private String sflchg = "";
	private String vcvval = "";
	private String vlltxt = "";
	private String wrkZexereturn = "";
	private String zcrtnvar = "";
	private String zdefer = "";
	private String zexereturn = "";
	private String zioerror = "";
	private String zKeyFlag = "Y";// Session var
	private String zlastpgm = "";
	private String zlastproc = "";
	private String zmrtnvar = "";
	private String znxtpgcid = "";
	private String zrecsel = "";
	private String zreload = "";
	private String zrtncode = "";
	private String zzmode = "";
	private int numRows;// Session var

	@Column(name="F4FIELD", length=10)
	public String getF4field() {
		return f4field;
	}

	@Column(name="FNCRECFND", length=1)
	public String getFncrecfnd() {
		return fncrecfnd;
	}

	@Column(name="GRDCSROPN", length=1)
	public String getGrdcsropn() {
		return grdcsropn;
	}

	public ArrayList<FilterFldData> getFilterFlds() {
		return filterFlds;
	}

	public Map<String, Boolean> getFldsFiltered() {
		return fldsFiltered;
	}

	public String getMode1() {
		return mode1;
	}

	public int getNumRows() {
		return numRows;
	}

	@Column(name="QIN", length=99)
	public String getQin() {
		return qin;
	}

	@Column(name="SEL", length=1)
	public String getSel() {
		return sel;
	}

	@Column(name="SFLCHG", length=1)
	public String getSflchg() {
		return sflchg;
	}

	@Column(name="VCVVAL", length=30)
	public String getVcvval() {
		return vcvval;
	}

	@Column(name="VLLTXT", length=50)
	public String getVlltxt() {
		return vlltxt;
	}

	@Column(name="GROWSTART", length=3)
	public Integer getGrowstart() {
		return growstart;
	}

	@Column(name="SFLRRN", length=5)
	public Integer getSflrrn() {
		return sflrrn;
	}

	@Column(name="SEL", length=1)
	public String getWrkZexereturn() {
		return wrkZexereturn;
	}

	@Column(name="ZCRTNVAR", length=7)
	public String getZcrtnvar() {
		return zcrtnvar;
	}

	@Column(name="ZDEFER", length=1)
	public String getZdefer() {
		return zdefer;
	}

	@Column(name="ZEXERETURN", length=1)
	public String getZexereturn() {
		return zexereturn;
	}

	@Column(name="ZIOERROR", length=1)
	public String getZioerror() {
		return zioerror;
	}

	public String getZKeyFlag() {
		return zKeyFlag;
	}

	@Column(name="ZLASTPGM", length=10)
	public String getZlastpgm() {
		return zlastpgm;
	}

	@Column(name="ZLASTPROC", length=20)
	public String getZlastproc() {
		return zlastproc;
	}

	@Column(name="ZMRTNVAR", length=7)
	public String getZmrtnvar() {
		return zmrtnvar;
	}

	@Column(name="ZNXTPGRRN", length=5)
	public Integer getZnxtpgrrn() {
		return znxtpgrrn;
	}

	@Column(name="ZNXTPGCID", length=50)
	public String getZnxtpgcid() {
		return znxtpgcid;
	}

	@Column(name="ZRECSEL", length=1)
	public String getZrecsel() {
		return zrecsel;
	}

	@Column(name="ZRELOAD", length=1)
	public String getZreload() {
		return zreload;
	}

	@Column(name="ZRTNCODE", length=7)
	public String getZrtncode() {
		return zrtncode;
	}

	@Column(name="ZSCANLIM", length=7)
	public Integer getZscanlim() {
		return zscanlim;
	}

	@Column(name="ZTOTRCREAD", length=7)
	public Integer getZtotrcread() {
		return ztotrcread;
	}

	@Column(name="ZZMODE", length=10)
	public String getZzmode() {
		return zzmode;
	}

	public void setF4field(String f4field) {
		this.f4field = f4field;
	}

	public void setFncrecfnd(String fncrecfnd) {
		this.fncrecfnd = fncrecfnd;
	}

	public void setFilterFlds(ArrayList<FilterFldData> filterFlds) {
		this.filterFlds = filterFlds;
	}

	public void setFldsFiltered(Map<String, Boolean> fldsFiltered) {
		this.fldsFiltered = fldsFiltered;
	}

	public void setGrdcsropn(String grdcsropn) {
		this.grdcsropn = grdcsropn;
	}

	public void setGrowstart(Integer growstart) {
		this.growstart = growstart;
	}

	public void setMode1(String mode1) {
		this.mode1 = mode1;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public void setQin(String qin) {
		this.qin = qin;
	}

	public void setSel(String sel) {
		this.sel = sel;
	}

	public void setSflchg(String sflchg) {
		this.sflchg = sflchg;
	}

	public void setVcvval(String vcvval) {
		this.vcvval = vcvval;
	}

	public void setVlltxt(String vlltxt) {
		this.vlltxt = vlltxt;
	}

	public void setSflrrn(Integer sflrrn) {
		this.sflrrn = sflrrn;
	}

	public void setWrkZexereturn(String wrkZexereturn) {
		this.wrkZexereturn = wrkZexereturn;
	}

	public void setZcrtnvar(String zcrtnvar) {
		this.zcrtnvar = zcrtnvar;
	}

	public void setZdefer(String zdefer) {
		this.zdefer = zdefer;
	}

	public void setZexereturn(String zexereturn) {
		this.zexereturn = zexereturn;
	}

	public void setZioerror(String zioerror) {
		this.zioerror = zioerror;
	}

	public void setZKeyFlag(String zKeyFlag) {
		this.zKeyFlag = zKeyFlag;
	}

	public void setZlastpgm(String zlastpgm) {
		this.zlastpgm = zlastpgm;
	}

	public void setZlastproc(String zlastproc) {
		this.zlastproc = zlastproc;
	}

	public void setZmrtnvar(String zmrtnvar) {
		this.zmrtnvar = zmrtnvar;
	}

	public void setZnxtpgrrn(Integer znxtpgrrn) {
		this.znxtpgrrn = znxtpgrrn;
	}

	public void setZnxtpgcid(String znxtpgcid) {
		this.znxtpgcid = znxtpgcid;
	}

	public void setZrecsel(String zrecsel) {
		this.zrecsel = zrecsel;
	}

	public void setZreload(String zreload) {
		this.zreload = zreload;
	}

	public void setZrtncode(String zrtncode) {
		this.zrtncode = zrtncode;
	}

	public void setZscanlim(Integer zscanlim) {
		this.zscanlim = zscanlim;
	}

	public void setZtotrcread(Integer ztotrcread) {
		this.ztotrcread = ztotrcread;
	}

	public void setZzmode(String zzmode) {
		this.zzmode = zzmode;
	}
}
