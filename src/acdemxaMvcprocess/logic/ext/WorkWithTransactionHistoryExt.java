package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.Purchases;
import acdemxaMvcprocess.data.Salespersons;
import acdemxaMvcprocess.data.Sites;
import acdemxaMvcprocess.data.TransactionHistory;
import acdemxaMvcprocess.data.TransactionHistoryId;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.WorkWithTransactionHistoryGDO;
import acdemxaMvcprocess.logic.data.WorkWithTransactionHistoryPDO;
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
import static com.databorough.utils.DateTimeConverter.toDate;
import static com.databorough.utils.MessageUtils.setMsgObj;
import static com.databorough.utils.NumberFormatter.loInt;
import static com.databorough.utils.NumberFormatter.toInt;
import static com.databorough.utils.StringUtils.all;
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
 * Program logic for Work with transaction history (WWTRNHSTRA).
 *
 * @author KAMALN
 */
public class WorkWithTransactionHistoryExt {

	/**
	 * Initialises the <code>WorkWithTransactionHistoryPDO</code>
	 * object to receive the data to display.
	 *
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Purchases</code> (CUSTS)
	 * <li><code>Sites</code> (CUSF)
	 * <li><code>Salespersons</code> (SLMEN)
	 * </ul>
	 *
	 * @see Purchases
	 * @see Sites
	 * @see Salespersons
	 */
	public void pgmInitialize() {
		zzsetdspfind();
		stateVariable.setSwbccd(stateVariable.getXwbccd());
		inzsr();
		// D e f i n i t i o n s
		stateVariable.setSflchg("0");
		stateVariable.setSflrrn(0);
		// M a i n l i n e   C o d e
		purchases.retrieve(stateVariable.getSwbccd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		sites.retrieve(stateVariable.getCusno());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		salespersons.retrieve(stateVariable.getPercus());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00001 Rep not found on Salespersons
		if (nmfkpinds.pgmInd99()) {
			stateVariable.setPname(all("-", 34));
		}
		stateVariable.setSname(stateVariable.getPname());
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
		stateVariable.setZmsage(blanks(78));
		if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
			return;
		}
		// BR00002 Period <> 0 or LOCAT2 <> blank
		if (stateVariable.getLocate() != 0 || !isBlanks(stateVariable.getLocat2())) {
			stateVariable.setXwe4nb(stateVariable.getLocate());
			stateVariable.setXwricd(stateVariable.getLocat2());
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
		if (equal("1", stateVariable.getSflchg())) {
			stateVariable.setXwe4nb(loInt(6));
			stateVariable.setXwricd(loChar(3));
			populateGridData();
		}
		mainline0();
		return;
	}

	/**
	 * Processes the data after validating it.
	 *
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * <p>Deletes data from the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * @see TransactionHistory
	 */
	public void panelProcess() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 2, "00"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				if (equal("PERSON", stateVariable.getSfield())) {
					zzNxtPgm = "SLMENSEL";
					throw new NewScreenException("", zzNxtPgm);
				}
			}
			validt();
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
				addrec0();
			}
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				stateVariable.setZzfunmode("A ");
				zzNxtFun = "WWTRNHST04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			addrec0();
			nmfkpinds.setFunKey12(false);
			nmfkpinds.setFunKey12(false);
			if (equal("1", stateVariable.getSflchg())) {
				stateVariable.setXwe4nb(loInt(6));
				stateVariable.setXwricd(loChar(3));
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 2, "00"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				if (equal("PERSON", stateVariable.getSfield())) {
					zzNxtPgm = "SLMENSEL";
					throw new NewScreenException("", zzNxtPgm);
				}
			}
			validt();
			// BR00005 Trn_Hst_Seq not found on Transaction_History
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
				chgrec0();
			}
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				stateVariable.setZzfunmode("B ");
				zzNxtFun = "WWTRNHST04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			chgrec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", stateVariable.getSflchg())) {
				stateVariable.setXwe4nb(loInt(6));
				stateVariable.setXwricd(loChar(3));
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 2, "00"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				if (equal("PERSON", stateVariable.getSfield())) {
					zzNxtPgm = "SLMENSEL";
					throw new NewScreenException("", zzNxtPgm);
				}
			}
			if (nmfkpinds.funKey23()) {
				transactionHistory.retrieve(stateVariable.getXwe4nb(), stateVariable.getXwdldt(), stateVariable.getXwc8dt());
				nmfkpinds.setPgmInd99(! lastIO.isFound());
				if (! nmfkpinds.pgmInd99()) {
					nmfkpinds.setPgmInd99(isLastError());
				}
				// BR00008 Trn_Hst_Seq found on Transaction_History
				if (! nmfkpinds.pgmInd99()) {
					transactionHistory.delete();
					nmfkpinds.setPgmInd99(isLastError());
				}
				return;
			}
			delrec0();
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", stateVariable.getSflchg())) {
				stateVariable.setXwe4nb(loInt(6));
				stateVariable.setXwricd(loChar(3));
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("D ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 2, "00"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				if (equal("PERSON", stateVariable.getSfield())) {
					zzNxtPgm = "SLMENSEL";
					throw new NewScreenException("", zzNxtPgm);
				}
			}
			dsprec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", stateVariable.getSflchg())) {
				stateVariable.setXwe4nb(loInt(6));
				stateVariable.setXwricd(loChar(3));
				populateGridData();
			}
			mainline0();
			return;
		}
	}

	/**
	 * <p>Adds data into the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * <p>Updates data in the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * @see TransactionHistory
	 */
	public void panel4Process() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				transactionHistory.write();
				nmfkpinds.setPgmInd99(isLastError());
				if (nmfkpinds.pgmInd99()) {
					msgObjIdx = setMsgObj("Y2U0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
					return;
				}
				return;
			}
			addrec0();
			nmfkpinds.setFunKey12(false);
			nmfkpinds.setFunKey12(false);
			if (equal("1", stateVariable.getSflchg())) {
				stateVariable.setXwe4nb(loInt(6));
				stateVariable.setXwricd(loChar(3));
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				savedata();
				transactionHistory.retrieve(stateVariable.getXwe4nb(), stateVariable.getXwdldt(), stateVariable.getXwc8dt());
				nmfkpinds.setPgmInd99(! lastIO.isFound());
				if (! nmfkpinds.pgmInd99()) {
					nmfkpinds.setPgmInd99(isLastError());
				}
				// BR00006 Trn_Hst_Seq found on Transaction_History
				if (! nmfkpinds.pgmInd99()) {
					restoredata();
					transactionHistory.update();
					nmfkpinds.setPgmInd99(isLastError());
				}
				if (nmfkpinds.pgmInd99()) {
					msgObjIdx = setMsgObj("Y2U0007", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
				else {
					return;
				}
				return;
			}
			chgrec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", stateVariable.getSflchg())) {
				stateVariable.setXwe4nb(loInt(6));
				stateVariable.setXwricd(loChar(3));
				populateGridData();
			}
			mainline0();
			return;
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * @see TransactionHistory
	 */
	private void populateGridData() {
		// Delete subfile
		nmfkpinds.setPgmInd74(true);
		nmfkpinds.setPgmInd71(false);
		nmfkpinds.setPgmInd72(false);
		nmfkpinds.setPgmInd31(false);
		nmfkpinds.setPgmInd99(isLastError());
		if (stateVariable.getSflrrn() == 0) {
			trnhstl3.fetch(stateVariable.getSwbccd(), stateVariable.getXwe4nb(), stateVariable.getXwricd());
		}
		trnhstl3.next(stateVariable.getSwbccd());
		nmfkpinds.setPgmInd31(lastIO.isEndOfFile());
		nmfkpinds.setPgmInd74(false);
		// Write subfile
		uwScnIdx1 = 0;
		while (! nmfkpinds.pgmInd31()) {
			stateVariable.setSflrrn(Integer.valueOf(stateVariable.getSflrrn() + 1));
			stateVariable.setDssel(blanks(1));
			if (uwScnIdx1 < Integer.valueOf(gridVariable.length - 1)) {
				uwScnIdx1 = Integer.valueOf(uwScnIdx1 + 1);
			}
			gridVariable[uwScnIdx1].setDssel("X");
			gridVariable[uwScnIdx1].setXwe4nb(stateVariable.getXwe4nb());
			gridVariable[uwScnIdx1].setXwricd(stateVariable.getXwricd());
			gridVariable[uwScnIdx1].setXwdldt(stateVariable.getXwdldt());
			gridVariable[uwScnIdx1].setXwc8dt(stateVariable.getXwc8dt());
			nmfkpinds.setPgmInd99(isLastError());
			// Reading just one grid page
			if (uwScnIdx1 == Integer.valueOf(gridVariable.length - 1)) {
				break;
			}
			trnhstl3.next(stateVariable.getSwbccd());
			nmfkpinds.setPgmInd31(lastIO.isEndOfFile());
		}
		// -
		if (stateVariable.getSflrrn() > 0) {
			if (equal("0", stateVariable.getSflchg())) {
				stateVariable.setShwrec(1);
			}
			if ((stateVariable.getShwrec() > stateVariable.getSflrrn()) || (stateVariable.getShwrec() < 1)) {
				stateVariable.setShwrec(1);
			}
			nmfkpinds.setPgmInd71(true);
		}
		nmfkpinds.setPgmInd72(true);
	}

	private void zselec() {
		stateVariable.setSflchg("0");
		// For each selection
		zselec0();
		nmfkpinds.setFunKey12(false);
	}

	private void dspscn() {
		stateVariable.setZmsage(blanks(78));
		nmfkpinds.setPgmInd99(false);
		setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 2, "00"));
		// - Prompt
		if (nmfkpinds.funKey04()) {
			if (equal("PERSON", stateVariable.getSfield())) {
				zzNxtPgm = "SLMENSEL";
				throw new NewScreenException("", zzNxtPgm);
			}
		}
	}

	private void dsprec() {
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, " DISPLAY"));
		nmfkpinds.setPgmInd34(true);
		nmfkpinds.setPgmInd37(false);
		getrec();
		dsprec0();
	}

	private void chgrec() {
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "  UPDATE"));
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd37(false);
		getrec();
		chgrec0();
	}

	private void delrec() {
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "  DELETE"));
		nmfkpinds.setPgmInd34(true);
		nmfkpinds.setPgmInd37(true);
		getrec();
		delrec0();
	}

	private void addrec() {
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd36(true);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "ADDITION"));
		stateVariable.setXwdldt(toDate(unnamedDS1.zdate(), "*ISO", dateFormat));
		addrec0();
		nmfkpinds.setFunKey12(false);
	}

	private void cmdkey() {
		if (nmfkpinds.funKey06()) {
			addrec();
		}
		nmfkpinds.setFunKey12(false);
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * <li><code>Salespersons</code> (SLMEN)
	 * </ul>
	 *
	 * @see TransactionHistory
	 * @see Salespersons
	 */
	private void validt() {
		// -
		nmfkpinds.setPgmInd99(false);
		setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 2, "00"));
		stateVariable.setZmsage(blanks(78));
		for (int idxCntdo = 1; idxCntdo <= 1; idxCntdo++) {
			// When adding a transaction, period must not be zero:
			// BR00009 Period = 0
			if (stateVariable.getXwe4nb() == 0) {
				nmfkpinds.setPgmInd99(true);
				nmfkpinds.setPgmInd40(true);
				msgObjIdx = setMsgObj("OEM0010", "XWE4NB", msgObjIdx, messages);
				break;
			}
			// When adding transaction must not exist:
			if (nmfkpinds.pgmInd36()) {
				transactionHistory.fetch(stateVariable.getXwe4nb(), stateVariable.getXwdldt(), stateVariable.getXwc8dt());
				nmfkpinds.setPgmInd99(lastIO.isEqual());
				// BR00010 Exact match found for Trn_Hst_Seq on Transaction_History
				if (nmfkpinds.pgmInd99()) {
					nmfkpinds.setPgmInd40(true);
					msgObjIdx = setMsgObj("Y2U0003", "XWC8DT", msgObjIdx, messages);
					break;
				}
			}
			// - Salesman Code must be valid:
			salespersons.retrieve(stateVariable.getPerson());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00011 Rep not found on Salespersons
			if (nmfkpinds.pgmInd99()) {
				nmfkpinds.setPgmInd41(true);
				msgObjIdx = setMsgObj("OEM0023", "PERSON", msgObjIdx, messages);
				stateVariable.setPname(all("-", 34));
				break;
			}
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>TransactionHistory</code> (TRNHST)
	 * </ul>
	 *
	 * @see TransactionHistory
	 */
	private void getrec() {
		transactionHistory.retrieve(stateVariable.getXwe4nb(), stateVariable.getXwdldt(), stateVariable.getXwc8dt());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		if (! nmfkpinds.pgmInd99()) {
			nmfkpinds.setPgmInd99(isLastError());
		}
		// BR00012 Trn_Hst_Seq not found on Transaction_History
		if (nmfkpinds.pgmInd99()) {
			msgObjIdx = setMsgObj("Y3U9999", "XWC8DT", msgObjIdx, messages);
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
		validt();
	}

	private void inzsr() {
		// Initialise message subfile
		nmfkpinds.setPgmInd32(true);
		stateVariable.setZzpgm(replaceStr(stateVariable.getZzpgm(), 1, 8, "WWTRNHST"));
		// - Set date
		stateVariable.setZzdate(getDate().toInt());
		unnamedDS1.setZyr(formatUsingEditCode(abs(toInt(getYear())), "X"));
		unnamedDS1.setFillr1("-");
		unnamedDS1.setZmth(formatUsingEditCode(abs(toInt(getMonth())), "X"));
		unnamedDS1.setFillr2("-");
		unnamedDS1.setZday(formatUsingEditCode(abs(toInt(getDay())), "X"));
	}

	private void savedata() {
		stateVariable.setZwe4nb(stateVariable.getXwe4nb());
		stateVariable.setZwdldt(stateVariable.getXwdldt());
		stateVariable.setZwc8dt(stateVariable.getXwc8dt());
		stateVariable.setZwbccd(stateVariable.getXwbccd());
		stateVariable.setZwbncd(stateVariable.getXwbncd());
		stateVariable.setZwbdcd(stateVariable.getXwbdcd());
		stateVariable.setZperson(stateVariable.getPerson());
		stateVariable.setZwaacs(stateVariable.getXwaacs());
		stateVariable.setZwabcd(stateVariable.getXwabcd());
		stateVariable.setZwagcd(stateVariable.getXwagcd());
		stateVariable.setZwahcd(stateVariable.getXwahcd());
		stateVariable.setZwaicd(stateVariable.getXwaicd());
		stateVariable.setZwt8tx(stateVariable.getXwt8tx());
		stateVariable.setZwordn(stateVariable.getXwordn());
		stateVariable.setZwricd(stateVariable.getXwricd());
		stateVariable.setZwa5qt(stateVariable.getXwa5qt());
		stateVariable.setZwvalu(stateVariable.getXwvalu());
	}

	private void restoredata() {
		stateVariable.setXwe4nb(stateVariable.getZwe4nb());
		stateVariable.setXwdldt(stateVariable.getZwdldt());
		stateVariable.setXwc8dt(stateVariable.getZwc8dt());
		stateVariable.setXwbccd(stateVariable.getZwbccd());
		stateVariable.setXwbncd(stateVariable.getZwbncd());
		stateVariable.setXwbdcd(stateVariable.getZwbdcd());
		stateVariable.setPerson(stateVariable.getZperson());
		stateVariable.setXwaacs(stateVariable.getZwaacs());
		stateVariable.setXwabcd(stateVariable.getZwabcd());
		stateVariable.setXwagcd(stateVariable.getZwagcd());
		stateVariable.setXwahcd(stateVariable.getZwahcd());
		stateVariable.setXwaicd(stateVariable.getZwaicd());
		stateVariable.setXwt8tx(stateVariable.getZwt8tx());
		stateVariable.setXwordn(stateVariable.getZwordn());
		stateVariable.setXwricd(stateVariable.getZwricd());
		stateVariable.setXwa5qt(stateVariable.getZwa5qt());
		stateVariable.setXwvalu(stateVariable.getZwvalu());
	}

	private void mainline0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setLocate(0);
			stateVariable.setLocat2(blanks(3));
			zzNxtFun = "WWTRNHST01D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void zselec0() {
		stateVariable.setSflchg("1");
		// Change
		if (equal("2", stateVariable.getDssel())) {
			chgrec();
			stateVariable.setShwrec(stateVariable.getSflrrn());
		}
		// Delete
		else if (equal("4", stateVariable.getDssel())) {
			delrec();
		}
		else if (equal("5", stateVariable.getDssel())) {
			dsprec();
			stateVariable.setShwrec(stateVariable.getSflrrn());
		}
		stateVariable.setDssel(blanks(1));
		nmfkpinds.setPgmInd99(isLastError());
	}

	private void dsprec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("D ");
			zzNxtFun = "WWTRNHST02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void chgrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("B ");
			zzNxtFun = "WWTRNHST02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void delrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWTRNHST02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void addrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("A ");
			zzNxtFun = "WWTRNHST02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void zzsetdspfind() {
		dataIndicator[3] = false;
		dataIndicator[12] = false;
		dataIndicator[28] = false;
		dataIndicator[6] = false;
		dataIndicator[71] = false;
		dataIndicator[72] = false;
		dataIndicator[74] = false;
		dataIndicator[31] = false;
		dataIndicator[34] = false;
		dataIndicator[4] = false;
		dataIndicator[37] = false;
		dataIndicator[23] = false;
		dataIndicator[36] = false;
		dataIndicator[40] = false;
		dataIndicator[41] = false;
		dataIndicator[32] = false;
	}

	public boolean dataIndicator[] = new boolean[100];
	public LastIO lastIO = new LastIO();

	public static final String dateFormat = "*ISO";
	private final Integer uwRow1 = 9;
	public WorkWithTransactionHistoryPDO stateVariable = new WorkWithTransactionHistoryPDO();
	private DataCRUD<Purchases, String> purchases = new DataCRUD<Purchases, String>(Purchases.class, this, new String[] { "xwbccd" }, new char[] { 'A' }, new char[] { 'R' }, new String[][] { { "PERCUS", "PERSON" } });
	private DataCRUD<Salespersons, String> salespersons = new DataCRUD<Salespersons, String>(Salespersons.class, this, new String[] { "person" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Sites, Integer> sites = new DataCRUD<Sites, Integer>(Sites.class, this, new String[] { "cusno" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<TransactionHistory, TransactionHistoryId> transactionHistory = new DataCRUD<TransactionHistory, TransactionHistoryId>(TransactionHistory.class, this, new String[] { "xwe4nb", "xwdldt", "xwc8dt" }, new char[] { 'A', 'A', 'A' }, new char[] { 'N', 'N', 'N' });
	public DataCRUD<TransactionHistory, TransactionHistoryId> trnhstl3 = new DataCRUD<TransactionHistory, TransactionHistoryId>(TransactionHistory.class, this, new String[] { "xwbccd", "xwe4nb", "xwricd" }, new char[] { 'A', 'A', 'A' }, new char[] { 'R', 'N', 'N' });
	private Integer msgObjIdx = 0;
	private Integer uwScnIdx1 = 0;
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
		@Column(name="FUN_KEY_06", length=1)
		public Boolean funKey06() {
			return getBoolVal(subString(objectToString(nmfkpinds), 6, 1));
		}
		public void setFunKey06(Boolean funKey06) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 6, 1, bool2Str(funKey06)));
		}
		@Column(name="FUN_KEY_12", length=1)
		public Boolean funKey12() {
			return getBoolVal(subString(objectToString(nmfkpinds), 12, 1));
		}
		public void setFunKey12(Boolean funKey12) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 12, 1, bool2Str(funKey12)));
		}
		@Column(name="FUN_KEY_23", length=1)
		public Boolean funKey23() {
			return getBoolVal(subString(objectToString(nmfkpinds), 23, 1));
		}
		public void setFunKey23(Boolean funKey23) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 23, 1, bool2Str(funKey23)));
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
		@Column(name="PGM_IND_40", length=1)
		public Boolean pgmInd40() {
			return getBoolVal(subString(objectToString(nmfkpinds), 40, 1));
		}
		public void setPgmInd40(Boolean pgmInd40) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 40, 1, bool2Str(pgmInd40)));
		}
		@Column(name="PGM_IND_41", length=1)
		public Boolean pgmInd41() {
			return getBoolVal(subString(objectToString(nmfkpinds), 41, 1));
		}
		public void setPgmInd41(Boolean pgmInd41) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 41, 1, bool2Str(pgmInd41)));
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
	public WorkWithTransactionHistoryGDO gridVariable[] = initArray(WorkWithTransactionHistoryGDO.class, uwRow1 + 1);
}