package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.CustomerGroups;
import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.Distributors;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.Purchases;
import acdemxaMvcprocess.data.Salespersons;
import acdemxaMvcprocess.data.Sites;
import acdemxaMvcprocess.logic.data.CustomerDetailMaintenancePDO;
import acdemxaMvcprocess.logic.data.MessageObject;
import com.databorough.utils.NewScreenException;
import javax.persistence.Column;
import static acdemxaMvcprocess.data.DataCRUD.isLastError;
import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.DateTimeConverter.getDate;
import static com.databorough.utils.DateTimeConverter.getDay;
import static com.databorough.utils.DateTimeConverter.getMonth;
import static com.databorough.utils.DateTimeConverter.getYear;
import static com.databorough.utils.MessageUtils.setMsgObj;
import static com.databorough.utils.NumberFormatter.toInt;
import static com.databorough.utils.StringUtils.blanks;
import static com.databorough.utils.StringUtils.equal;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.StringUtils.replaceStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.formatUsingEditCode;
import static com.databorough.utils.Utils.getBoolVal;
import static com.databorough.utils.Utils.isBlanks;
import static java.lang.Math.abs;

/**
 * Program logic for Customer Detail Maintenance (CUSTMNT1RA).
 *
 * @author KAMALN
 */
public class CustomerDetailMaintenanceExt {

	/**
	 * Initialises the <code>CustomerDetailMaintenancePDO</code>
	 * object to receive the data to display.
	 */
	public void pgmInitialize() {
		zzsetdspfind();
		stateVariable.setKwbccd(stateVariable.getXwbccd());
		// M a i n l i n e   C o d e
		// Entry parameters
		// Initialise message subfile
		nmfkpinds.setPgmInd82(true);
		stateVariable.setZzpgm(replaceStr(stateVariable.getZzpgm(), 1, 8, "CUSTMNT1"));
		stateVariable.setSwbccd(stateVariable.getKwbccd());
		getrec();
		// - Set date
		stateVariable.setZzdate(getDate().toInt());
		unnamedDS1.setZyr(formatUsingEditCode(abs(toInt(getYear())), "X"));
		unnamedDS1.setFillr1("-");
		unnamedDS1.setZmth(formatUsingEditCode(abs(toInt(getMonth())), "X"));
		unnamedDS1.setFillr2("-");
		unnamedDS1.setZday(formatUsingEditCode(abs(toInt(getDay())), "X"));
		scr1();
		if (! nmfkpinds.funKey03()) {
			scr2();
		}
		return;
	}

	/**
	 * Processes the key data to fetch the complete record to be shown on the
	 * subsequent screen.
	 */
	public void entryPanelProcess() {
		// Prompt
		if (nmfkpinds.funKey04()) {
			srprom();
		}
		valid1();
		scr1();
		if (! nmfkpinds.funKey03()) {
			scr2();
		}
		return;
	}

	/**
	 * Processes the data after validating it.
	 */
	public void panelProcess() {
		// Prompt
		if (nmfkpinds.funKey04()) {
			srprom();
		}
		valid2();
		scr2();
		// If not F3/F12
		if (! nmfkpinds.funKey03()) {
			updrec();
		}
		return;
	}

	private void scr1() {
		do {
			zzNxtFun = "CUSTMNT101D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
		while (!(! nmfkpinds.pgmInd99() || nmfkpinds.funKey03()));
	}

	private void scr2() {
		do {
			zzNxtFun = "CUSTMNT102D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
		while (!(! nmfkpinds.pgmInd99() || nmfkpinds.funKey03()));
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>CustomerGroups</code> (CUSGRP)
	 * <li><code>Salespersons</code> (SLMEN)
	 * <li><code>Distributors</code> (DISTS)
	 * </ul>
	 *
	 * @see CustomerGroups
	 * @see Salespersons
	 * @see Distributors
	 */
	private void valid1() {
		nmfkpinds.setPgmInd99(false);
		nmfkpinds.setPgmInd31(false);
		nmfkpinds.setPgmInd32(false);
		nmfkpinds.setPgmInd33(false);
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd35(false);
		errmsg = blanks(132);
		// CUSTOMER MUST NOT BE BLANK
		// BR00001 Debtor = blank
		if (isBlanks(stateVariable.getSwbccd())) {
			nmfkpinds.setPgmInd99(true);
			nmfkpinds.setPgmInd31(true);
			msgObjIdx = setMsgObj("OEM0002", "XWBCCD", msgObjIdx, messages);
		}
		// Customer name
		// BR00002 Name = blank
		if (isBlanks(stateVariable.getSwg4tx())) {
			nmfkpinds.setPgmInd32(true);
			nmfkpinds.setPgmInd99(true);
			msgObjIdx = setMsgObj("OEM0012", "XWG4TX", msgObjIdx, messages);
		}
		// Customer group
		customerGroups.retrieve(stateVariable.getSwbncd());
		// BR00003 End of file on CUSGRP and CUSGRP.CusGrp <> blank
		if (lastIO.isEndOfFile() && !isBlanks(stateVariable.getSwbncd())) {
			nmfkpinds.setPgmInd33(true);
			msgObjIdx = setMsgObj("OES0374", "XWBNCD", msgObjIdx, messages);
		}
		else {
			stateVariable.setSwkhtx(stateVariable.getXwkhtx());
		}
		// Salesman
		salespersons.retrieve(stateVariable.getSperson());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00004 Rep not found on Salespersons
		if (nmfkpinds.pgmInd99()) {
			nmfkpinds.setPgmInd34(true);
			msgObjIdx = setMsgObj("OEM0023", "PERSON", msgObjIdx, messages);
		}
		// Distributor
		distributors.retrieve(stateVariable.getSdsdcde());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00005 Distributor not found on Distributors
		if (nmfkpinds.pgmInd99()) {
			nmfkpinds.setPgmInd35(true);
			msgObjIdx = setMsgObj("OEM0018", "DSDCDE", msgObjIdx, messages);
		}
		stateVariable.setZmsage(subString(errmsg, 1, 78));
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Sites</code> (CUSF)
	 * </ul>
	 *
	 * @see Sites
	 */
	private void valid2() {
		nmfkpinds.setPgmInd99(false);
		// Balance
		stateVariable.setSdiff(stateVariable.getSwgiva() - stateVariable.getSwidv0());
		// BR00006 Credit_Limit < 0
		if (stateVariable.getSdiff() < 0) {
			nmfkpinds.setPgmInd99(true);
			nmfkpinds.setPgmInd36(true);
			msgObjIdx = setMsgObj("OES0373", "XWGIVA", msgObjIdx, messages);
		}
		// Customer cannot be zero
		// BR00007 Cus_No = 0
		if (stateVariable.getCusno() == 0) {
			nmfkpinds.setPgmInd37(true);
			nmfkpinds.setPgmInd99(true);
			msgObjIdx = setMsgObj("CNP0008", "CUSNO", msgObjIdx, messages);
		}
		// Customer
		sites.retrieve(stateVariable.getCusno());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00008 Cus_No not found on Sites
		if (nmfkpinds.pgmInd99()) {
			nmfkpinds.setPgmInd37(true);
			msgObjIdx = setMsgObj("CNP0002", "CUSNO", msgObjIdx, messages);
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * </ul>
	 *
	 * @see Purchases
	 */
	private void getrec() {
		purchases.retrieve(stateVariable.getSwbccd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// Main data
		stateVariable.setSwbccd(stateVariable.getXwbccd());
		stateVariable.setSwg4tx(stateVariable.getXwg4tx());
		stateVariable.setSwb2cd(stateVariable.getXwb2cd());
		stateVariable.setSwb3cd(stateVariable.getXwb3cd());
		stateVariable.setSwhitx(stateVariable.getXwhitx());
		stateVariable.setSwe0nb(stateVariable.getXwe0nb());
		stateVariable.setSwjun0(stateVariable.getXwjun0());
		stateVariable.setSwdvcd(stateVariable.getXwdvcd());
		stateVariable.setSwbncd(stateVariable.getXwbncd());
		stateVariable.setSperson(stateVariable.getPerson());
		// eval      PNAME =PNAME
		stateVariable.setSdsdcde(stateVariable.getDsdcde());
		stateVariable.setSwbtcd(stateVariable.getXwbtcd());
		stateVariable.setSwgiva(stateVariable.getXwgiva());
		stateVariable.setSwaocd(stateVariable.getXwaocd());
		stateVariable.setSwbbcd(stateVariable.getXwbbcd());
		stateVariable.setSwg4t0(stateVariable.getXwg4t0());
		stateVariable.setSwc7st(stateVariable.getXwc7st());
		stateVariable.setSwdast(stateVariable.getXwdast());
		stateVariable.setSwbpd0(stateVariable.getXwbpd0());
		stateVariable.setSwbad0(stateVariable.getXwbad0());
		stateVariable.setSwardt(stateVariable.getXwardt());
		stateVariable.setZmsage(stateVariable.getZmsage());
		stateVariable.setSwkhtx(stateVariable.getXwkhtx());
		// eval      DNAME =DNAME
		// Sales data
		stateVariable.setSwf0va(stateVariable.getXwf0va());
		stateVariable.setSwasdt(stateVariable.getXwasdt());
		stateVariable.setSwf0v0(stateVariable.getXwf0v0());
		stateVariable.setSwbqdt(stateVariable.getXwbqdt());
		stateVariable.setSwf1va(stateVariable.getXwf1va());
		stateVariable.setSwf1v0(stateVariable.getXwf1v0());
		stateVariable.setSwf2va(stateVariable.getXwf2va());
		stateVariable.setSwf3va(stateVariable.getXwf3va());
		stateVariable.setSwf4va(stateVariable.getXwf4va());
		stateVariable.setSwf6va(stateVariable.getXwf6va());
		stateVariable.setSwidv0(stateVariable.getXwidv0());
		stateVariable.setSwgava(stateVariable.getXwgava());
		stateVariable.setSwgbva(stateVariable.getXwgbva());
		stateVariable.setSwgcva(stateVariable.getXwgcva());
		stateVariable.setSwgdva(stateVariable.getXwgdva());
		stateVariable.setSwgeva(stateVariable.getXwgeva());
		stateVariable.setSwgfva(stateVariable.getXwgfva());
		stateVariable.setSwggva(stateVariable.getXwggva());
		zgetnames();
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * </ul>
	 *
	 * <p>Updates data in the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * </ul>
	 *
	 * @see Purchases
	 */
	private void updrec() {
		purchases.retrieve(stateVariable.getSwbccd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// BR00009 Debtor found on Purchases
		if (purchases.isFound()) {
			stateVariable.setXwbccd(stateVariable.getSwbccd());
			stateVariable.setXwg4tx(stateVariable.getSwg4tx());
			stateVariable.setXwb2cd(stateVariable.getSwb2cd());
			stateVariable.setXwb3cd(stateVariable.getSwb3cd());
			stateVariable.setXwhitx(stateVariable.getSwhitx());
			stateVariable.setXwe0nb(stateVariable.getSwe0nb());
			stateVariable.setXwjun0(stateVariable.getSwjun0());
			stateVariable.setXwdvcd(stateVariable.getSwdvcd());
			stateVariable.setXwbncd(stateVariable.getSwbncd());
			stateVariable.setXwbtcd(stateVariable.getSwbtcd());
			stateVariable.setXwgiva(stateVariable.getSwgiva());
			stateVariable.setXwaocd(stateVariable.getSwaocd());
			stateVariable.setXwbbcd(stateVariable.getSwbbcd());
			stateVariable.setXwg4t0(stateVariable.getSwg4t0());
			stateVariable.setXwc7st(stateVariable.getSwc7st());
			stateVariable.setXwdast(stateVariable.getSwdast());
			stateVariable.setXwbpd0(stateVariable.getSwbpd0());
			stateVariable.setXwbad0(stateVariable.getSwbad0());
			stateVariable.setXwardt(stateVariable.getSwardt());
			stateVariable.setXwkhtx(stateVariable.getSwkhtx());
			stateVariable.setXwf0va(stateVariable.getSwf0va());
			stateVariable.setXwasdt(stateVariable.getSwasdt());
			stateVariable.setXwf0v0(stateVariable.getSwf0v0());
			stateVariable.setXwbqdt(stateVariable.getSwbqdt());
			stateVariable.setXwf1va(stateVariable.getSwf1va());
			stateVariable.setXwf1v0(stateVariable.getSwf1v0());
			stateVariable.setXwf2va(stateVariable.getSwf2va());
			stateVariable.setXwf3va(stateVariable.getSwf3va());
			stateVariable.setXwf4va(stateVariable.getSwf4va());
			stateVariable.setXwf6va(stateVariable.getSwf6va());
			stateVariable.setXwidv0(stateVariable.getSwidv0());
			stateVariable.setXwgava(stateVariable.getSwgava());
			stateVariable.setXwgbva(stateVariable.getSwgbva());
			stateVariable.setXwgcva(stateVariable.getSwgcva());
			stateVariable.setXwgdva(stateVariable.getSwgdva());
			stateVariable.setXwgeva(stateVariable.getSwgeva());
			stateVariable.setXwgfva(stateVariable.getSwgfva());
			stateVariable.setXwggva(stateVariable.getSwggva());
			stateVariable.setPerson(stateVariable.getSperson());
			stateVariable.setDsdcde(stateVariable.getSdsdcde());
			purchases.update();
			nmfkpinds.setPgmInd99(isLastError());
		}
	}

	private void srprom() {
		if (equal("SPERSON", stateVariable.getSfield())) {
			zzNxtPgm = "SLMENSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("CUSNO", stateVariable.getSfield())) {
			stateVariable.setCusprm(0);
			zzNxtPgm = "CUSFSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("SWBNCD", stateVariable.getSfield())) {
			zzNxtPgm = "CUSGRSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("SDSDCDE", stateVariable.getSfield())) {
			zzNxtPgm = "DISTSSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>CustomerGroups</code> (CUSGRP)
	 * <li><code>Salespersons</code> (SLMEN)
	 * <li><code>Distributors</code> (DISTS)
	 * </ul>
	 *
	 * @see CustomerGroups
	 * @see Salespersons
	 * @see Distributors
	 */
	private void zgetnames() {
		customerGroups.retrieve(stateVariable.getSwbncd());
		stateVariable.setSwkhtx(stateVariable.getXwkhtx());
		salespersons.retrieve(stateVariable.getSperson());
		distributors.retrieve(stateVariable.getSdsdcde());
	}

	private void zzsetdspfind() {
		dataIndicator[3] = false;
		dataIndicator[12] = false;
		dataIndicator[28] = false;
		dataIndicator[94] = false;
		dataIndicator[4] = false;
		dataIndicator[31] = false;
		dataIndicator[96] = false;
		dataIndicator[32] = false;
		dataIndicator[33] = false;
		dataIndicator[34] = false;
		dataIndicator[35] = false;
		dataIndicator[36] = false;
		dataIndicator[37] = false;
		dataIndicator[82] = false;
	}

	public boolean dataIndicator[] = new boolean[100];
	public LastIO lastIO = new LastIO();

	public CustomerDetailMaintenancePDO stateVariable = new CustomerDetailMaintenancePDO();
	public LocalFields fileAVariable = new LocalFields();
	private class LocalFields {
		@Column(name="DSDCDE", length=2)
		public String dsdcde = "";
	}
	private DataCRUD<CustomerGroups, String> customerGroups = new DataCRUD<CustomerGroups, String>(CustomerGroups.class, this, new String[] { "xwbncd" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Distributors, String> distributors = new DataCRUD<Distributors, String>(Distributors.class, this, new String[] { "dsdcde" }, new char[] { 'A' }, new char[] { 'N' });
	public DataCRUD<Purchases, String> purchases = new DataCRUD<Purchases, String>(Purchases.class, this, new String[] { "xwbccd" }, new char[] { 'A' }, new char[] { 'R' });
	private DataCRUD<Salespersons, String> salespersons = new DataCRUD<Salespersons, String>(Salespersons.class, this, new String[] { "person" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Sites, Integer> sites = new DataCRUD<Sites, Integer>(Sites.class, this, new String[] { "cusno" }, new char[] { 'A' }, new char[] { 'N' }, new String[][] { { "XSDCDE", "DSDCDE" } });
	private Integer msgObjIdx = 0;
	private String errmsg = "";
	private String zzNxtFun = "";
	private String zzNxtPgm = "";
	public MessageObject messages[] = initArray(MessageObject.class, 21);
	public Nmfkpinds nmfkpinds = new Nmfkpinds();
	public class Nmfkpinds {
		@Column(name="FUN_KEY_03", length=1)
		public Boolean funKey03() {
			return getBoolVal(subString(objectToString(nmfkpinds), 3, 1));
		}
		public void setFunKey03(Boolean funKey03) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 3, 1, bool2Str(funKey03)));
		}
		@Column(name="FUN_KEY_04", length=1)
		public Boolean funKey04() {
			return getBoolVal(subString(objectToString(nmfkpinds), 4, 1));
		}
		public void setFunKey04(Boolean funKey04) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 4, 1, bool2Str(funKey04)));
		}
		@Column(name="PGM_IND_31", length=1)
		public Boolean pgmInd31() {
			return getBoolVal(subString(objectToString(nmfkpinds), 31, 1));
		}
		public void setPgmInd31(Boolean pgmInd31) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 31, 1, bool2Str(pgmInd31)));
		}
		@Column(name="PGM_IND_32", length=1)
		public Boolean pgmInd32() {
			return getBoolVal(subString(objectToString(nmfkpinds), 32, 1));
		}
		public void setPgmInd32(Boolean pgmInd32) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 32, 1, bool2Str(pgmInd32)));
		}
		@Column(name="PGM_IND_33", length=1)
		public Boolean pgmInd33() {
			return getBoolVal(subString(objectToString(nmfkpinds), 33, 1));
		}
		public void setPgmInd33(Boolean pgmInd33) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 33, 1, bool2Str(pgmInd33)));
		}
		@Column(name="PGM_IND_34", length=1)
		public Boolean pgmInd34() {
			return getBoolVal(subString(objectToString(nmfkpinds), 34, 1));
		}
		public void setPgmInd34(Boolean pgmInd34) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 34, 1, bool2Str(pgmInd34)));
		}
		@Column(name="PGM_IND_35", length=1)
		public Boolean pgmInd35() {
			return getBoolVal(subString(objectToString(nmfkpinds), 35, 1));
		}
		public void setPgmInd35(Boolean pgmInd35) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 35, 1, bool2Str(pgmInd35)));
		}
		@Column(name="PGM_IND_36", length=1)
		public Boolean pgmInd36() {
			return getBoolVal(subString(objectToString(nmfkpinds), 36, 1));
		}
		public void setPgmInd36(Boolean pgmInd36) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 36, 1, bool2Str(pgmInd36)));
		}
		@Column(name="PGM_IND_37", length=1)
		public Boolean pgmInd37() {
			return getBoolVal(subString(objectToString(nmfkpinds), 37, 1));
		}
		public void setPgmInd37(Boolean pgmInd37) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 37, 1, bool2Str(pgmInd37)));
		}
		@Column(name="PGM_IND_66", length=1)
		public Boolean pgmInd66() {
			return getBoolVal(subString(objectToString(nmfkpinds), 66, 1));
		}
		public void setPgmInd66(Boolean pgmInd66) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 66, 1, bool2Str(pgmInd66)));
		}
		@Column(name="PGM_IND_82", length=1)
		public Boolean pgmInd82() {
			return getBoolVal(subString(objectToString(nmfkpinds), 82, 1));
		}
		public void setPgmInd82(Boolean pgmInd82) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 82, 1, bool2Str(pgmInd82)));
		}
		@Column(name="PGM_IND_99", length=1)
		public Boolean pgmInd99() {
			return getBoolVal(subString(objectToString(nmfkpinds), 99, 1));
		}
		public void setPgmInd99(Boolean pgmInd99) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 99, 1, bool2Str(pgmInd99)));
		}
		@Column(name="remNmfkpinds", length=99)
		public String remNmfkpinds = "";
	}
	private UnnamedDS1 unnamedDS1 = new UnnamedDS1();
	private class UnnamedDS1 {
		@Column(name="ZDATE", length=10)
		public String zdate() {
			return subString(objectToString(unnamedDS1), 1, 10);
		}
		public void setZdate(String zdate) {
			setObject(this, replaceStr(objectToString(unnamedDS1), 1, 10, padStringWithValue(zdate, " ", 10, false)));
		}
		@Column(name="ZYR", length=4)
		public String zyr() {
			return subString(objectToString(unnamedDS1), 1, 4);
		}
		public void setZyr(String zyr) {
			setObject(this, replaceStr(objectToString(unnamedDS1), 1, 4, padStringWithValue(zyr, " ", 4, false)));
		}
		@Column(name="FILLR1", length=1)
		public String fillr1() {
			return subString(objectToString(unnamedDS1), 5, 1);
		}
		public void setFillr1(String fillr1) {
			setObject(this, replaceStr(objectToString(unnamedDS1), 5, 1, padStringWithValue(fillr1, " ", 1, false)));
		}
		@Column(name="ZMTH", length=2)
		public String zmth() {
			return subString(objectToString(unnamedDS1), 6, 2);
		}
		public void setZmth(String zmth) {
			setObject(this, replaceStr(objectToString(unnamedDS1), 6, 2, padStringWithValue(zmth, " ", 2, false)));
		}
		@Column(name="FILLR2", length=1)
		public String fillr2() {
			return subString(objectToString(unnamedDS1), 8, 1);
		}
		public void setFillr2(String fillr2) {
			setObject(this, replaceStr(objectToString(unnamedDS1), 8, 1, padStringWithValue(fillr2, " ", 1, false)));
		}
		@Column(name="ZDAY", length=2)
		public String zday() {
			return subString(objectToString(unnamedDS1), 9, 2);
		}
		public void setZday(String zday) {
			setObject(this, replaceStr(objectToString(unnamedDS1), 9, 2, padStringWithValue(zday, " ", 2, false)));
		}
		@Column(name="remUnnamedds1", length=10)
		public String remUnnamedds1 = "";
	}
}