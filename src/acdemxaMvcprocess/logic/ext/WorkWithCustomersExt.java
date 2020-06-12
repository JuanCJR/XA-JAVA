package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.CustomerGroups;
import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.Distributors;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.Purchases;
import acdemxaMvcprocess.data.Salespersons;
import acdemxaMvcprocess.logic.data.FiPsDsFld;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.WorkWithCustomersGDO;
import acdemxaMvcprocess.logic.data.WorkWithCustomersPDO;
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
import static com.databorough.utils.StringUtils.loChar;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.StringUtils.replaceStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.formatUsingEditCode;
import static com.databorough.utils.Utils.getBoolVal;
import static com.databorough.utils.Utils.isBlanks;
import static java.lang.Math.abs;

/**
 * Program logic for Work with Customers (WWCUSTSRA).
 *
 * @author KAMALN
 */
public class WorkWithCustomersExt {

	/**
	 * Initialises the <code>WorkWithCustomersPDO</code>
	 * object to receive the data to display.
	 */
	public void pgmInitialize() {
		zzsetdspfind();
		inzsr();
		// M a i n l i n e   C o d e
		// Build subfile
		populateGridData();
		mainline0();
		return;
	}

	/**
	 * Processes the key data to fetch the complete record to be shown on the
	 * subsequent screen.
	 */
	public void entryPanelProcess() {
		if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
			return;
		}
		// Action positioner request
		// BR00001 Debtor <> blank
		if (!isBlanks(stateVariable.getLocate())) {
			stateVariable.setSflrrn(0);
			stateVariable.setXwbccd(stateVariable.getLocate());
			populateGridData();
			mainline0();
		}
		// Action command keys
		if (nmfkpinds.pgmInd28()) {
			cmdkey();
		}
		else {
			// Else action selections
			zselec();
		}
		if (equal("1", sflchg)) {
			stateVariable.setXwbccd(loChar(11));
			populateGridData();
		}
		mainline0();
		return;
	}

	/**
	 * Processes the data after validating it.
	 */
	public void panelProcess() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			mvscdb();
			offind();
			// Process Command Keys
			// Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Orders
			else if (nmfkpinds.funKey08()) {
				zorder();
			}
			// Transaction history
			else if (nmfkpinds.funKey09()) {
				zhisto();
			}
			if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
				return;
			}
			if (nmfkpinds.funKey04() || nmfkpinds.funKey08() || nmfkpinds.funKey09()) {
				addrec0();
			}
			valid1();
			// If errors found
			// BR00008 Distributor not found on Distributors
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
				mvdbsc();
				addrec0();
			}
			// Request confirmation
			stateVariable.setZzfunmode("A ");
			zzNxtFun = "WWCUSTS04D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			mvscdb();
			offind();
			// Process Command Keys
			// Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Orders
			else if (nmfkpinds.funKey08()) {
				zorder();
			}
			// Transaction history
			else if (nmfkpinds.funKey09()) {
				zhisto();
			}
			if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
				return;
			}
			if (nmfkpinds.funKey04() || nmfkpinds.funKey08() || nmfkpinds.funKey09()) {
				chgrec0();
			}
			valid1();
			// If errors found
			// BR00010 Distributor not found on Distributors
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
				mvdbsc();
				chgrec0();
			}
			// Request confirmation
			stateVariable.setZzfunmode("B ");
			zzNxtFun = "WWCUSTS04D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			mvscdb();
			offind();
			// Process Command Keys
			// Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Orders
			else if (nmfkpinds.funKey08()) {
				zorder();
			}
			// Transaction history
			else if (nmfkpinds.funKey09()) {
				zhisto();
			}
			if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
				return;
			}
			if (nmfkpinds.funKey04() || nmfkpinds.funKey08() || nmfkpinds.funKey09()) {
				delrec0();
			}
			// Request confirmation
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWCUSTS04D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
		if (equal("D ", stateVariable.getZzfunmode())) {
			mvscdb();
			offind();
			// Process Command Keys
			// Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Orders
			else if (nmfkpinds.funKey08()) {
				zorder();
			}
			// Transaction history
			else if (nmfkpinds.funKey09()) {
				zhisto();
			}
			if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
				return;
			}
			if (nmfkpinds.funKey04() || nmfkpinds.funKey08() || nmfkpinds.funKey09()) {
				dsprec0();
			}
			dsprec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwbccd(loChar(11));
				populateGridData();
			}
			mainline0();
			return;
		}
	}

	/**
	 * <p>Adds data into the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * </ul>
	 *
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
	 * <p>Deletes data from the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * </ul>
	 *
	 * @see Purchases
	 */
	public void panel4Process() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				// If confirmation received then write record
				purchases.write();
				nmfkpinds.setPgmInd99(isLastError());
				if (nmfkpinds.pgmInd99()) {
					msgObjIdx = setMsgObj("Y2U0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
				else {
					return;
				}
			}
			addrec0();
			stateVariable.setShwrec(1);
			sflchg = "1";
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwbccd(loChar(11));
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				// If confirmation received then update record
				purchases.retrieve(stateVariable.getXwbccd());
				// BR00011 Debtor found on Purchases
				if (purchases.isFound()) {
					mvscdb();
					purchases.update();
					nmfkpinds.setPgmInd99(isLastError());
					if (nmfkpinds.pgmInd99()) {
						msgObjIdx = setMsgObj("Y2U0007", "", msgObjIdx, messages);
						stateVariable.setZmsage(subString(errmsg, 1, 78));
					}
				}
			}
			chgrec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwbccd(loChar(11));
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				// If confirmation received then delete record
				purchases.retrieve(stateVariable.getXwbccd());
				// BR00012 Debtor found on Purchases
				if (purchases.isFound()) {
					purchases.delete();
				}
				return;
			}
			// End: If not F3/F12
			delrec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwbccd(loChar(11));
				populateGridData();
			}
			mainline0();
			return;
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
	private void populateGridData() {
		// Delete subfile
		if (stateVariable.getSflrrn() == 0) {
			nmfkpinds.setPgmInd74(true);
			nmfkpinds.setPgmInd71(false);
			nmfkpinds.setPgmInd72(false);
			nmfkpinds.setPgmInd81(false);
			nmfkpinds.setPgmInd99(isLastError());
			nmfkpinds.setPgmInd74(false);
			// Write subfile
			stateVariable.setSflrrn(0);
			purchases.fetch(stateVariable.getXwbccd());
		}
		stateVariable.setSflctr(0);
		uwScnIdx1 = 0;
		while (! nmfkpinds.pgmInd81() && (stateVariable.getSflctr() < 12)) {
			purchases.next();
			nmfkpinds.setPgmInd81(lastIO.isEndOfFile());
			if (! nmfkpinds.pgmInd81()) {
				stateVariable.setSflrrn(Integer.valueOf(stateVariable.getSflrrn() + 1));
				stateVariable.setSflctr(Integer.valueOf(stateVariable.getSflctr() + 1));
				if (stateVariable.getSflctr() == 1) {
					stateVariable.setShwrec(stateVariable.getSflrrn());
				}
				stateVariable.setDssel(blanks(1));
				if (uwScnIdx1 < Integer.valueOf(gridVariable.length - 1)) {
					uwScnIdx1 = Integer.valueOf(uwScnIdx1 + 1);
				}
				gridVariable[uwScnIdx1].setDssel("X");
				gridVariable[uwScnIdx1].setXwg4tx(stateVariable.getXwg4tx());
				gridVariable[uwScnIdx1].setXwbccd(stateVariable.getXwbccd());
				nmfkpinds.setPgmInd99(isLastError());
			}
			// Reading just one grid page
			if (uwScnIdx1 == Integer.valueOf(gridVariable.length - 1)) {
				break;
			}
		}
		if (stateVariable.getSflrrn() > 0) {
			nmfkpinds.setPgmInd71(true);
		}
		nmfkpinds.setPgmInd72(true);
	}

	private void cmdkey() {
		if (nmfkpinds.rolKey26()) {
			populateGridData();
		}
		else if (nmfkpinds.funKey06()) {
			addrec();
			stateVariable.setShwrec(1);
			sflchg = "1";
		}
		nmfkpinds.setFunKey12(false);
	}

	private void zselec() {
		sflchg = "0";
		// For each selection
		zselec0();
		nmfkpinds.setFunKey12(false);
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * <li><code>CustomerGroups</code> (CUSGRP)
	 * <li><code>Salespersons</code> (SLMEN)
	 * <li><code>Distributors</code> (DISTS)
	 * </ul>
	 *
	 * @see Purchases
	 * @see CustomerGroups
	 * @see Salespersons
	 * @see Distributors
	 */
	private void valid1() {
		for (int idxCntdo = 1; idxCntdo <= 1; idxCntdo++) {
			// When adding customer must not be zero:
			// BR00002 Debtor = blank
			if (isBlanks(stateVariable.getXwbccd())) {
				nmfkpinds.setPgmInd99(true);
				nmfkpinds.setPgmInd31(true);
				msgObjIdx = setMsgObj("OEM0002", "XWBCCD", msgObjIdx, messages);
				break;
			}
			// When adding customer must not exist:
			if (equal("ADDITION", stateVariable.getActdsp())) {
				purchases.fetch(stateVariable.getXwbccd());
				nmfkpinds.setPgmInd99(lastIO.isEqual());
				// BR00003 Exact match found for Debtor on Purchases
				if (nmfkpinds.pgmInd99()) {
					nmfkpinds.setPgmInd31(true);
					msgObjIdx = setMsgObj("Y2U0003", "XWBCCD", msgObjIdx, messages);
					break;
				}
			}
			// Customer name must not be blank:
			// BR00004 Name = blank
			if (isBlanks(stateVariable.getXwg4tx())) {
				nmfkpinds.setPgmInd32(true);
				nmfkpinds.setPgmInd99(true);
				msgObjIdx = setMsgObj("OEM0012", "XWG4TX", msgObjIdx, messages);
				break;
			}
			// - Customer Group must be valid:
			customerGroups.retrieve(stateVariable.getXwbncd());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00005 CusGrp not found on Customer_Groups
			if (nmfkpinds.pgmInd99()) {
				nmfkpinds.setPgmInd33(true);
				msgObjIdx = setMsgObj("OES0374", "XWBNCD", msgObjIdx, messages);
				break;
			}
			// - Salesman Code must be valid:
			salespersons.retrieve(stateVariable.getPerson());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00006 Rep not found on Salespersons
			if (nmfkpinds.pgmInd99()) {
				nmfkpinds.setPgmInd34(true);
				msgObjIdx = setMsgObj("OEM0023", "PERSON", msgObjIdx, messages);
				break;
			}
			// - Distributor Code must be valid:
			distributors.retrieve(stateVariable.getDsdcde());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00007 Distributor not found on Distributors
			if (nmfkpinds.pgmInd99()) {
				msgObjIdx = setMsgObj("OEM0018", "DSDCDE", msgObjIdx, messages);
				break;
			}
		}
	}

	private void addrec() {
		nmfkpinds.setPgmInd94(false);
		nmfkpinds.setPgmInd96(true);
		nmfkpinds.setPgmInd97(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "ADDITION"));
		offind();
		stateVariable.setPname(blanks(34));
		stateVariable.setDname(blanks(34));
		stateVariable.setXwkhtx(blanks(40));
		mvdbsc();
		addrec0();
	}

	private void chgrec() {
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "  UPDATE"));
		nmfkpinds.setPgmInd94(false);
		nmfkpinds.setPgmInd96(false);
		nmfkpinds.setPgmInd97(false);
		offind();
		getrec();
		// If no record or file error
		// BR00009 Debtor found on Purchases and NOT ERROR(CUSTS)
		if (! nmfkpinds.pgmInd99() && ! nmfkpinds.pgmInd66()) {
			chgrec0();
			// End: If no record or file error
		}
		stateVariable.setShwrec(stateVariable.getSflrrn());
	}

	private void delrec() {
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "  DELETE"));
		nmfkpinds.setPgmInd94(true);
		nmfkpinds.setPgmInd96(false);
		nmfkpinds.setPgmInd97(true);
		offind();
		getrec();
		delrec0();
		stateVariable.setShwrec(stateVariable.getSflrrn());
	}

	private void dsprec() {
		nmfkpinds.setPgmInd94(true);
		nmfkpinds.setPgmInd96(false);
		nmfkpinds.setPgmInd97(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, " DISPLAY"));
		offind();
		getrec();
		dsprec0();
		stateVariable.setShwrec(stateVariable.getSflrrn());
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * <li><code>Salespersons</code> (SLMEN)
	 * <li><code>Distributors</code> (DISTS)
	 * <li><code>CustomerGroups</code> (CUSGRP)
	 * </ul>
	 *
	 * @see Purchases
	 * @see Salespersons
	 * @see Distributors
	 * @see CustomerGroups
	 */
	private void getrec() {
		purchases.retrieve(stateVariable.getXwbccd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// If record error
		// BR00013 Debtor not found on Purchases
		if (nmfkpinds.pgmInd99()) {
			msgObjIdx = setMsgObj("OES0115", "XWBCCD", msgObjIdx, messages);
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
		// If file error
		else if (nmfkpinds.pgmInd66()) {
			if (fileds.filests == 1218) {
				msgObjIdx = setMsgObj("Y3U9999", "", msgObjIdx, messages);
				stateVariable.setZmsage(subString(errmsg, 1, 78));
			}
			else {
				msgObjIdx = setMsgObj("OEM0004", "", msgObjIdx, messages);
				stateVariable.setZmsage(subString(errmsg, 1, 78));
			}
		}
		// If no errors
		else {
			// Retrieve Salesman's name
			salespersons.retrieve(stateVariable.getPerson());
			// Retrieve Distributor's name
			distributors.retrieve(stateVariable.getDsdcde());
			// Retrieve Customer Group's name
			customerGroups.retrieve(stateVariable.getXwbncd());
			mvdbsc();
		}
	}

	private void inzsr() {
		// Initialise message subfile
		nmfkpinds.setPgmInd82(true);
		stateVariable.setZzpgm(replaceStr(stateVariable.getZzpgm(), 1, 8, "WWCUSTS "));
		// - Set date
		stateVariable.setZzdate(getDate().toInt());
		unnamedDS1.setZyr(formatUsingEditCode(abs(toInt(getYear())), "X"));
		unnamedDS1.setFillr1("-");
		unnamedDS1.setZmth(formatUsingEditCode(abs(toInt(getMonth())), "X"));
		unnamedDS1.setFillr2("-");
		unnamedDS1.setZday(formatUsingEditCode(abs(toInt(getDay())), "X"));
	}

	private void srprom() {
		if (equal("ZPERSON", stateVariable.getSfield())) {
			zzNxtPgm = "SLMENSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("ZWBNCD", stateVariable.getSfield())) {
			zzNxtPgm = "CUSGRSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("ZDSDCDE", stateVariable.getSfield())) {
			zzNxtPgm = "DISTSSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
	}

	private void offind() {
		nmfkpinds.setPgmInd99(false);
		nmfkpinds.setPgmInd31(false);
		nmfkpinds.setPgmInd32(false);
		nmfkpinds.setPgmInd33(false);
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd36(false);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setZmsage(blanks(78));
	}

	private void zorder() {
		zzNxtPgm = "WWCONHDR";
		throw new NewScreenException("", zzNxtPgm);
	}

	private void zhisto() {
		zzNxtPgm = "WWTRNHST";
		throw new NewScreenException("", zzNxtPgm);
	}

	private void mvdbsc() {
		stateVariable.setZwbccd(stateVariable.getXwbccd());
		stateVariable.setZwg4tx(stateVariable.getXwg4tx());
		stateVariable.setZwb2cd(stateVariable.getXwb2cd());
		stateVariable.setZwb3cd(stateVariable.getXwb3cd());
		stateVariable.setZwhitx(stateVariable.getXwhitx());
		stateVariable.setZwe0nb(stateVariable.getXwe0nb());
		stateVariable.setZwjun0(stateVariable.getXwjun0());
		stateVariable.setZwdvcd(stateVariable.getXwdvcd());
		stateVariable.setZwbncd(stateVariable.getXwbncd());
		stateVariable.setZperson(stateVariable.getPerson());
		stateVariable.setZdsdcde(stateVariable.getDsdcde());
		stateVariable.setZwbtcd(stateVariable.getXwbtcd());
		stateVariable.setZwgiva(stateVariable.getXwgiva());
		stateVariable.setZwaocd(stateVariable.getXwaocd());
		stateVariable.setZwbbcd(stateVariable.getXwbbcd());
		stateVariable.setZwg4t0(stateVariable.getXwg4t0());
		stateVariable.setZwc7st(stateVariable.getXwc7st());
		stateVariable.setZwdast(stateVariable.getXwdast());
		stateVariable.setZwbpd0(stateVariable.getXwbpd0());
		stateVariable.setZwbad0(stateVariable.getXwbad0());
		stateVariable.setZwkhtx(stateVariable.getXwkhtx());
	}

	private void mvscdb() {
		stateVariable.setXwbccd(stateVariable.getZwbccd());
		stateVariable.setXwg4tx(stateVariable.getZwg4tx());
		stateVariable.setXwb2cd(stateVariable.getZwb2cd());
		stateVariable.setXwb3cd(stateVariable.getZwb3cd());
		stateVariable.setXwhitx(stateVariable.getZwhitx());
		stateVariable.setXwe0nb(stateVariable.getZwe0nb());
		stateVariable.setXwjun0(stateVariable.getZwjun0());
		stateVariable.setXwdvcd(stateVariable.getZwdvcd());
		stateVariable.setXwbncd(stateVariable.getZwbncd());
		stateVariable.setPerson(stateVariable.getZperson());
		stateVariable.setDsdcde(stateVariable.getZdsdcde());
		stateVariable.setXwbtcd(stateVariable.getZwbtcd());
		stateVariable.setXwgiva(stateVariable.getZwgiva());
		stateVariable.setXwaocd(stateVariable.getZwaocd());
		stateVariable.setXwbbcd(stateVariable.getZwbbcd());
		stateVariable.setXwg4t0(stateVariable.getZwg4t0());
		stateVariable.setXwc7st(stateVariable.getZwc7st());
		stateVariable.setXwdast(stateVariable.getZwdast());
		stateVariable.setXwbpd0(stateVariable.getZwbpd0());
		stateVariable.setXwbad0(stateVariable.getZwbad0());
		stateVariable.setXwkhtx(stateVariable.getZwkhtx());
	}

	private void mainline0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setLocate(blanks(11));
			zzNxtFun = "WWCUSTS01D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void zselec0() {
		sflchg = "1";
		// Change
		if (equal("2", stateVariable.getDssel())) {
			chgrec();
		}
		// Delete
		else if (equal("4", stateVariable.getDssel())) {
			delrec();
		}
		else if (equal("5", stateVariable.getDssel())) {
			dsprec();
		}
		// Customer Detail Maintenance
		else if (equal("6", stateVariable.getDssel())) {
			zzNxtPgm = "CUSTMNT1";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("8", stateVariable.getDssel())) {
			zorder();
		}
		// Work with Transaction history
		else if (equal("9", stateVariable.getDssel())) {
			zhisto();
		}
		stateVariable.setDssel(blanks(1));
		nmfkpinds.setPgmInd99(isLastError());
	}

	private void addrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("A ");
			zzNxtFun = "WWCUSTS02D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void chgrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("B ");
			zzNxtFun = "WWCUSTS02D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void delrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWCUSTS02D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void dsprec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("D ");
			zzNxtFun = "WWCUSTS02D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void zzsetdspfind() {
		dataIndicator[3] = false;
		dataIndicator[12] = false;
		dataIndicator[28] = false;
		dataIndicator[6] = false;
		dataIndicator[81] = false;
		dataIndicator[26] = false;
		dataIndicator[71] = false;
		dataIndicator[72] = false;
		dataIndicator[74] = false;
		dataIndicator[94] = false;
		dataIndicator[4] = false;
		dataIndicator[8] = false;
		dataIndicator[9] = false;
		dataIndicator[31] = false;
		dataIndicator[96] = false;
		dataIndicator[32] = false;
		dataIndicator[33] = false;
		dataIndicator[34] = false;
		dataIndicator[35] = false;
		dataIndicator[82] = false;
	}

	public boolean dataIndicator[] = new boolean[100];
	public LastIO lastIO = new LastIO();

	private final Integer uwRow1 = 12;
	private Fileds fileds = new Fileds();
	public LocalFields fileAVariable = new LocalFields();
	private class LocalFields {
		@Column(name="DSDCDE", length=2)
		public String dsdcde = "";
	}
	public WorkWithCustomersPDO stateVariable = new WorkWithCustomersPDO();
	private DataCRUD<CustomerGroups, String> customerGroups = new DataCRUD<CustomerGroups, String>(CustomerGroups.class, this, new String[] { "xwbncd" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Distributors, String> distributors = new DataCRUD<Distributors, String>(Distributors.class, this, new String[] { "dsdcde" }, new char[] { 'A' }, new char[] { 'N' });
	public DataCRUD<Purchases, String> purchases = new DataCRUD<Purchases, String>(Purchases.class, this, new String[] { "xwbccd" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Salespersons, String> salespersons = new DataCRUD<Salespersons, String>(Salespersons.class, this, new String[] { "person" }, new char[] { 'A' }, new char[] { 'N' });
	private Integer msgObjIdx = 0;
	private Integer uwScnIdx1 = 0;
	private String errmsg = "";
	private String sflchg = "0";
	private String zzNxtFun = "";
	private String zzNxtPgm = "";
	private class Fileds {
		@FiPsDsFld(name="FILESTS", fromPos=11, precision=5)
		public Integer filests = 0;
	}
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
		@Column(name="FUN_KEY_06", length=1)
		public Boolean funKey06() {
			return getBoolVal(subString(objectToString(nmfkpinds), 6, 1));
		}
		public void setFunKey06(Boolean funKey06) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 6, 1, bool2Str(funKey06)));
		}
		@Column(name="FUN_KEY_08", length=1)
		public Boolean funKey08() {
			return getBoolVal(subString(objectToString(nmfkpinds), 8, 1));
		}
		public void setFunKey08(Boolean funKey08) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 8, 1, bool2Str(funKey08)));
		}
		@Column(name="FUN_KEY_09", length=1)
		public Boolean funKey09() {
			return getBoolVal(subString(objectToString(nmfkpinds), 9, 1));
		}
		public void setFunKey09(Boolean funKey09) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 9, 1, bool2Str(funKey09)));
		}
		@Column(name="FUN_KEY_12", length=1)
		public Boolean funKey12() {
			return getBoolVal(subString(objectToString(nmfkpinds), 12, 1));
		}
		public void setFunKey12(Boolean funKey12) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 12, 1, bool2Str(funKey12)));
		}
		@Column(name="PGM_IND_28", length=1)
		public Boolean pgmInd28() {
			return getBoolVal(subString(objectToString(nmfkpinds), 28, 1));
		}
		public void setPgmInd28(Boolean pgmInd28) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 28, 1, bool2Str(pgmInd28)));
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
		@Column(name="PGM_IND_71", length=1)
		public Boolean pgmInd71() {
			return getBoolVal(subString(objectToString(nmfkpinds), 71, 1));
		}
		public void setPgmInd71(Boolean pgmInd71) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 71, 1, bool2Str(pgmInd71)));
		}
		@Column(name="PGM_IND_72", length=1)
		public Boolean pgmInd72() {
			return getBoolVal(subString(objectToString(nmfkpinds), 72, 1));
		}
		public void setPgmInd72(Boolean pgmInd72) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 72, 1, bool2Str(pgmInd72)));
		}
		@Column(name="PGM_IND_74", length=1)
		public Boolean pgmInd74() {
			return getBoolVal(subString(objectToString(nmfkpinds), 74, 1));
		}
		public void setPgmInd74(Boolean pgmInd74) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 74, 1, bool2Str(pgmInd74)));
		}
		@Column(name="PGM_IND_81", length=1)
		public Boolean pgmInd81() {
			return getBoolVal(subString(objectToString(nmfkpinds), 81, 1));
		}
		public void setPgmInd81(Boolean pgmInd81) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 81, 1, bool2Str(pgmInd81)));
		}
		@Column(name="PGM_IND_82", length=1)
		public Boolean pgmInd82() {
			return getBoolVal(subString(objectToString(nmfkpinds), 82, 1));
		}
		public void setPgmInd82(Boolean pgmInd82) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 82, 1, bool2Str(pgmInd82)));
		}
		@Column(name="PGM_IND_94", length=1)
		public Boolean pgmInd94() {
			return getBoolVal(subString(objectToString(nmfkpinds), 94, 1));
		}
		public void setPgmInd94(Boolean pgmInd94) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 94, 1, bool2Str(pgmInd94)));
		}
		@Column(name="PGM_IND_96", length=1)
		public Boolean pgmInd96() {
			return getBoolVal(subString(objectToString(nmfkpinds), 96, 1));
		}
		public void setPgmInd96(Boolean pgmInd96) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 96, 1, bool2Str(pgmInd96)));
		}
		@Column(name="PGM_IND_97", length=1)
		public Boolean pgmInd97() {
			return getBoolVal(subString(objectToString(nmfkpinds), 97, 1));
		}
		public void setPgmInd97(Boolean pgmInd97) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 97, 1, bool2Str(pgmInd97)));
		}
		@Column(name="PGM_IND_99", length=1)
		public Boolean pgmInd99() {
			return getBoolVal(subString(objectToString(nmfkpinds), 99, 1));
		}
		public void setPgmInd99(Boolean pgmInd99) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 99, 1, bool2Str(pgmInd99)));
		}
		@Column(name="ROL_KEY_26", length=1)
		public Boolean rolKey26() {
			return getBoolVal(subString(objectToString(nmfkpinds), 26, 1));
		}
		public void setRolKey26(Boolean rolKey26) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 26, 1, bool2Str(rolKey26)));
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
	public WorkWithCustomersGDO gridVariable[] = initArray(WorkWithCustomersGDO.class, uwRow1 + 1);
}