package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.ContractHeader;
import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.OrderStatusDescription;
import acdemxaMvcprocess.data.Purchases;
import acdemxaMvcprocess.data.Salespersons;
import acdemxaMvcprocess.data.Sites;
import acdemxaMvcprocess.logic.data.FiPsDsFld;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.WorkWithOrdersGDO;
import acdemxaMvcprocess.logic.data.WorkWithOrdersPDO;
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
import static com.databorough.utils.NumberFormatter.toInt;
import static com.databorough.utils.StringUtils.all;
import static com.databorough.utils.StringUtils.blanks;
import static com.databorough.utils.StringUtils.equal;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.StringUtils.replaceStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.formatUsingEditCode;
import static com.databorough.utils.Utils.getBoolVal;
import static java.lang.Math.abs;

/**
 * Program logic for Work with Orders (WWCONHDRRA).
 *
 * @author KAMALN
 */
public class WorkWithOrdersExt {

	/**
	 * Initialises the <code>WorkWithOrdersPDO</code>
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
		// M a i n l i n e   C o d e
		// Retrieve customer number data
		purchases.retrieve(stateVariable.getXwbccd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		sites.retrieve(stateVariable.getCusno());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		salespersons.retrieve(stateVariable.getPercus());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
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
		if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
			return;
		}
		// Action positioner request
		// BR00001 Contract <> 0
		if (stateVariable.getLocate() != 0) {
			stateVariable.setXwordn(stateVariable.getLocate());
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
			stateVariable.setXwordn(0);
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
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 4, "0000"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Lines
			else if (nmfkpinds.funKey07()) {
				zlines();
			}
			validt();
			// BR00004 Rep found on Salespersons
			if (! nmfkpinds.pgmInd99()) {
				return;
			}
			else {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
			}
			addrec0();
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				stateVariable.setZzfunmode("A ");
				zzNxtFun = "WWCONHDR04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			stateVariable.setShwrec(1);
			sflchg = "1";
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 4, "0000"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Lines
			else if (nmfkpinds.funKey07()) {
				zlines();
			}
			validt();
			// BR00011 Rep found on Salespersons
			if (! nmfkpinds.pgmInd99()) {
				return;
			}
			else {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
			}
			chgrec0();
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				stateVariable.setZzfunmode("B ");
				zzNxtFun = "WWCONHDR04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 4, "0000"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Lines
			else if (nmfkpinds.funKey07()) {
				zlines();
			}
			if (nmfkpinds.funKey23()) {
				return;
			}
			delrec0();
			if (nmfkpinds.funKey23()) {
				stateVariable.setZzfunmode("C ");
				zzNxtFun = "WWCONHDR04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("D ", stateVariable.getZzfunmode())) {
			nmfkpinds.setPgmInd99(false);
			setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 4, "0000"));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			// Lines
			else if (nmfkpinds.funKey07()) {
				zlines();
			}
			dsprec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
	}

	/**
	 * <p>Adds data into the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * </ul>
	 *
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * </ul>
	 *
	 * <p>Updates data in the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * </ul>
	 *
	 * <p>Deletes data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * </ul>
	 *
	 * @see ContractHeader
	 */
	public void panel4Process() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				contractHeader.write();
				nmfkpinds.setPgmInd99(isLastError());
				if (nmfkpinds.pgmInd99()) {
					msgObjIdx = setMsgObj("Y2U0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
			}
			stateVariable.setShwrec(1);
			sflchg = "1";
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				savedata();
				contractHeader.retrieve(stateVariable.getXwordn());
				nmfkpinds.setPgmInd99(! lastIO.isFound());
				nmfkpinds.setPgmInd66(isLastError());
				// BR00012 Contract found on Contract_Header and NOT ERROR(CONHDR)
				if (! nmfkpinds.pgmInd99() && ! nmfkpinds.pgmInd66()) {
					restoredata();
					contractHeader.update();
					nmfkpinds.setPgmInd99(isLastError());
					if (nmfkpinds.pgmInd99()) {
						msgObjIdx = setMsgObj("Y2U0007", "", msgObjIdx, messages);
						stateVariable.setZmsage(subString(errmsg, 1, 78));
					}
				}
			}
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				contractHeader.retrieve(stateVariable.getXwordn());
				nmfkpinds.setPgmInd99(! lastIO.isFound());
				nmfkpinds.setPgmInd66(isLastError());
				// BR00016 Contract found on Contract_Header and NOT ERROR(CONHDR)
				if (! nmfkpinds.pgmInd99() && ! nmfkpinds.pgmInd66()) {
					contractHeader.delete();
					nmfkpinds.setPgmInd99(isLastError());
				}
			}
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			nmfkpinds.setPgmInd99(isLastError());
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			if (equal("1", sflchg)) {
				stateVariable.setXwordn(0);
				populateGridData();
			}
			mainline0();
			return;
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * </ul>
	 *
	 * @see ContractHeader
	 */
	private void populateGridData() {
		// Delete subfile
		nmfkpinds.setPgmInd74(true);
		nmfkpinds.setPgmInd71(false);
		nmfkpinds.setPgmInd72(false);
		nmfkpinds.setPgmInd31(false);
		nmfkpinds.setPgmInd99(isLastError());
		if (stateVariable.getSflrrn() == 0) {
			conhdrl1.fetch(stateVariable.getXwbccd(), stateVariable.getXwordn());
		}
		conhdrl1.next(stateVariable.getXwbccd());
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
			gridVariable[uwScnIdx1].setXwordn(stateVariable.getXwordn());
			gridVariable[uwScnIdx1].setXwcref(stateVariable.getXwcref());
			gridVariable[uwScnIdx1].setXwdldt(stateVariable.getXwdldt());
			gridVariable[uwScnIdx1].setXwstat(stateVariable.getXwstat());
			gridVariable[uwScnIdx1].setXwtamt(stateVariable.getXwtamt());
			gridVariable[uwScnIdx1].setPerson(stateVariable.getPerson());
			nmfkpinds.setPgmInd99(isLastError());
			// Reading just one grid page
			if (uwScnIdx1 == Integer.valueOf(gridVariable.length - 1)) {
				break;
			}
			conhdrl1.next(stateVariable.getXwbccd());
			nmfkpinds.setPgmInd31(lastIO.isEndOfFile());
		}
		if (stateVariable.getSflrrn() > 0) {
			if (equal("0", sflchg)) {
				stateVariable.setShwrec(1);
			}
		}
		if ((stateVariable.getShwrec() > stateVariable.getSflrrn()) || (stateVariable.getShwrec() < 1)) {
			stateVariable.setShwrec(1);
		}
		if (stateVariable.getSflrrn() > 0) {
			nmfkpinds.setPgmInd71(true);
		}
		else {
			nmfkpinds.setPgmInd71(false);
		}
		nmfkpinds.setPgmInd72(true);
	}

	private void zselec() {
		sflchg = "0";
		// For each selection
		zselec0();
		nmfkpinds.setFunKey12(false);
	}

	private void dspscn() {
		if (nmfkpinds.pgmInd99()) {
			nmfkpinds.setPgmInd99(isLastError());
		}
		nmfkpinds.setPgmInd99(false);
		setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 4, "0000"));
		// - Prompt
		if (nmfkpinds.funKey04()) {
			srprom();
		}
		// Lines
		else if (nmfkpinds.funKey07()) {
			zlines();
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * </ul>
	 *
	 * @see ContractHeader
	 */
	private void zlines() {
		contractHeader.retrieve(stateVariable.getXwordn());
		nmfkpinds.setPgmInd36(! lastIO.isFound());
		// BR00002 Contract found on Contract_Header
		if (! nmfkpinds.pgmInd36()) {
			zzNxtPgm = "WWCONDET";
			throw new NewScreenException("", zzNxtPgm);
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * </ul>
	 *
	 * @see OrderStatusDescription
	 */
	private void addrec() {
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd36(true);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "ADDITION"));
		stateVariable.setXwdldt(toDate(unnamedDS1.zdate(), "*ISO", dateFormat));
		stateVariable.setXwstat("01");
		orderStatusDescription.retrieve(stateVariable.getXwstat());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00003 Status not found on Order_status_description
		if (nmfkpinds.pgmInd99()) {
			stateVariable.setXwsdsc(all("-", 20));
		}
		addrec0();
		if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("A ");
			zzNxtFun = "WWCONHDR04D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * <li><code>Salespersons</code> (SLMEN)
	 * </ul>
	 *
	 * @see ContractHeader
	 * @see OrderStatusDescription
	 * @see Salespersons
	 */
	private void dsprec() {
		nmfkpinds.setPgmInd34(true);
		nmfkpinds.setPgmInd36(false);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, " DISPLAY"));
		contractHeader.retrieve(stateVariable.getXwordn());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// BR00005 Contract not found on Contract_Header
		if (nmfkpinds.pgmInd99()) {
			msgObjIdx = setMsgObj("OES0115", "XWORDN", msgObjIdx, messages);
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
		else {
			if (nmfkpinds.pgmInd66()) {
				if (fileds.filests == 1218) {
					msgObjIdx = setMsgObj("Y3U9999", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
				else {
					msgObjIdx = setMsgObj("OEM0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
			}
			else {
				orderStatusDescription.retrieve(stateVariable.getXwstat());
				nmfkpinds.setPgmInd77(! lastIO.isFound());
				// BR00006 Status not found on Order_status_description
				if (nmfkpinds.pgmInd77()) {
					stateVariable.setXwsdsc(all("-", 20));
				}
				salespersons.retrieve(stateVariable.getPerson());
				nmfkpinds.setPgmInd77(! lastIO.isFound());
				// BR00007 Rep not found on Salespersons
				if (nmfkpinds.pgmInd77()) {
					stateVariable.setPname(all("-", 34));
				}
			}
		}
		dsprec0();
		stateVariable.setShwrec(stateVariable.getSflrrn());
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * <li><code>Salespersons</code> (SLMEN)
	 * </ul>
	 *
	 * @see ContractHeader
	 * @see OrderStatusDescription
	 * @see Salespersons
	 */
	private void chgrec() {
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd36(false);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, " UPDATE "));
		contractHeader.retrieve(stateVariable.getXwordn());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// BR00008 Contract not found on Contract_Header
		if (nmfkpinds.pgmInd99()) {
			msgObjIdx = setMsgObj("OES0115", "XWORDN", msgObjIdx, messages);
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
		else {
			if (nmfkpinds.pgmInd66()) {
				if (fileds.filests == 1218) {
					msgObjIdx = setMsgObj("Y3U9999", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
				else {
					msgObjIdx = setMsgObj("OEM0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
			}
			else {
				orderStatusDescription.retrieve(stateVariable.getXwstat());
				nmfkpinds.setPgmInd77(! lastIO.isFound());
				// BR00009 Status not found on Order_status_description
				if (nmfkpinds.pgmInd77()) {
					stateVariable.setXwsdsc(all("-", 20));
				}
				salespersons.retrieve(stateVariable.getPerson());
				nmfkpinds.setPgmInd77(! lastIO.isFound());
				// BR00010 Rep not found on Salespersons
				if (nmfkpinds.pgmInd77()) {
					stateVariable.setPname(all("-", 34));
				}
			}
		}
		chgrec0();
		if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("B ");
			zzNxtFun = "WWCONHDR04D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
		stateVariable.setShwrec(stateVariable.getSflrrn());
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * <li><code>Salespersons</code> (SLMEN)
	 * </ul>
	 *
	 * @see ContractHeader
	 * @see OrderStatusDescription
	 * @see Salespersons
	 */
	private void delrec() {
		nmfkpinds.setPgmInd34(true);
		nmfkpinds.setPgmInd36(false);
		nmfkpinds.setPgmInd37(true);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, " DELETE "));
		contractHeader.retrieve(stateVariable.getXwordn());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// BR00013 Contract not found on Contract_Header
		if (nmfkpinds.pgmInd99()) {
			msgObjIdx = setMsgObj("OES0115", "XWORDN", msgObjIdx, messages);
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
		else {
			if (nmfkpinds.pgmInd66()) {
				if (fileds.filests == 1218) {
					msgObjIdx = setMsgObj("Y3U9999", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
				else {
					msgObjIdx = setMsgObj("OEM0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
			}
			else {
				orderStatusDescription.retrieve(stateVariable.getXwstat());
				nmfkpinds.setPgmInd77(! lastIO.isFound());
				// BR00014 Status not found on Order_status_description
				if (nmfkpinds.pgmInd77()) {
					stateVariable.setXwsdsc(all("-", 20));
				}
				salespersons.retrieve(stateVariable.getPerson());
				nmfkpinds.setPgmInd77(! lastIO.isFound());
				// BR00015 Rep not found on Salespersons
				if (nmfkpinds.pgmInd77()) {
					stateVariable.setPname(all("-", 34));
				}
			}
		}
		delrec0();
		if (nmfkpinds.funKey23()) {
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWCONHDR04D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
		stateVariable.setShwrec(stateVariable.getSflrrn());
	}

	private void cmdkey() {
		if (nmfkpinds.funKey06()) {
			addrec();
			stateVariable.setShwrec(1);
			sflchg = "1";
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * <li><code>Purchases</code> (CUSTS)
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * <li><code>Salespersons</code> (SLMEN)
	 * </ul>
	 *
	 * @see ContractHeader
	 * @see Purchases
	 * @see OrderStatusDescription
	 * @see Salespersons
	 */
	private void validt() {
		nmfkpinds.setPgmInd99(false);
		setObject(nmfkpinds, replaceStr(objectToString(nmfkpinds), 40, 4, "0000"));
		stateVariable.setZmsage(blanks(78));
		for (int idxCntdo = 1; idxCntdo <= 1; idxCntdo++) {
			// When adding contract must not be zero:
			// BR00017 Contract = 0
			if (stateVariable.getXwordn() == 0) {
				nmfkpinds.setPgmInd99(true);
				nmfkpinds.setPgmInd40(true);
				msgObjIdx = setMsgObj("OEM0010", "XWORDN", msgObjIdx, messages);
				break;
			}
			// When adding contract must not exist:
			// BR00018 Contract not found on Contract_Header
			if (nmfkpinds.pgmInd36()) {
				contractHeader.fetch(stateVariable.getXwordn());
				nmfkpinds.setPgmInd99(lastIO.isEqual());
				// BR00019 Exact match found for Contract on Contract_Header
				if (nmfkpinds.pgmInd99()) {
					nmfkpinds.setPgmInd40(true);
					msgObjIdx = setMsgObj("Y2U0003", "XWORDN", msgObjIdx, messages);
					break;
				}
			}
			// - Customer No. must be valid:
			purchases.retrieve(stateVariable.getXwbccd());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00020 Debtor not found on Purchases
			if (nmfkpinds.pgmInd99()) {
				nmfkpinds.setPgmInd41(true);
				msgObjIdx = setMsgObj("OEM0002", "XWBCCD", msgObjIdx, messages);
				break;
			}
			// - Status Code must be valid:
			orderStatusDescription.retrieve(stateVariable.getXwstat());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00021 Status not found on Order_status_description
			if (nmfkpinds.pgmInd99()) {
				nmfkpinds.setPgmInd42(true);
				msgObjIdx = setMsgObj("OEM0019", "XWSTAT", msgObjIdx, messages);
				break;
			}
			// - Salesman Code must be valid:
			salespersons.retrieve(stateVariable.getPerson());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00022 Rep not found on Salespersons
			if (nmfkpinds.pgmInd99()) {
				nmfkpinds.setPgmInd43(true);
				msgObjIdx = setMsgObj("OEM0023", "PERSON", msgObjIdx, messages);
				break;
			}
		}
	}

	private void inzsr() {
		// Set first key value
		stateVariable.setXwbccd(stateVariable.getSwbccd());
		// Initialise second key value
		stateVariable.setXwordn(0);
		// Initialise message subfile
		nmfkpinds.setPgmInd32(true);
		stateVariable.setZzpgm(replaceStr(stateVariable.getZzpgm(), 1, 8, "WWCONHDR"));
		// - Set date
		stateVariable.setZzdate(getDate().toInt());
		unnamedDS1.setZyr(formatUsingEditCode(abs(toInt(getYear())), "X"));
		unnamedDS1.setFillr1("-");
		unnamedDS1.setZmth(formatUsingEditCode(abs(toInt(getMonth())), "X"));
		unnamedDS1.setFillr2("-");
		unnamedDS1.setZday(formatUsingEditCode(abs(toInt(getDay())), "X"));
	}

	private void srprom() {
		// - Customer
		if (equal("XWBCCD", stateVariable.getSfield())) {
			zzNxtPgm = "CUSTSSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("XWSTAT", stateVariable.getSfield())) {
			zzNxtPgm = "ORDSTSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("PERSON", stateVariable.getSfield())) {
			zzNxtPgm = "SLMENSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
	}

	private void savedata() {
		stateVariable.setZwordn(stateVariable.getXwordn());
		stateVariable.setZwbccd(stateVariable.getXwbccd());
		stateVariable.setZwcref(stateVariable.getXwcref());
		stateVariable.setZwdldt(stateVariable.getXwdldt());
		stateVariable.setZwstat(stateVariable.getXwstat());
		stateVariable.setZperson(stateVariable.getPerson());
		stateVariable.setZwtamt(stateVariable.getXwtamt());
		stateVariable.setZwadd1(stateVariable.getXwadd1());
		stateVariable.setZwadd2(stateVariable.getXwadd2());
		stateVariable.setZwadd3(stateVariable.getXwadd3());
		stateVariable.setZwadd4(stateVariable.getXwadd4());
		stateVariable.setZwpcde(stateVariable.getXwpcde());
		stateVariable.setZwcntr(stateVariable.getXwcntr());
	}

	private void restoredata() {
		stateVariable.setXwordn(stateVariable.getZwordn());
		stateVariable.setXwbccd(stateVariable.getZwbccd());
		stateVariable.setXwcref(stateVariable.getZwcref());
		stateVariable.setXwdldt(stateVariable.getZwdldt());
		stateVariable.setXwstat(stateVariable.getZwstat());
		stateVariable.setPerson(stateVariable.getZperson());
		stateVariable.setXwtamt(stateVariable.getZwtamt());
		stateVariable.setXwadd1(stateVariable.getZwadd1());
		stateVariable.setXwadd2(stateVariable.getZwadd2());
		stateVariable.setXwadd3(stateVariable.getZwadd3());
		stateVariable.setXwadd4(stateVariable.getZwadd4());
		stateVariable.setXwpcde(stateVariable.getZwpcde());
		stateVariable.setXwcntr(stateVariable.getZwcntr());
	}

	private void mainline0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setLocate(0);
			zzNxtFun = "WWCONHDR01D         ";
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
		// Lines
		else if (equal("7", stateVariable.getDssel())) {
			stateVariable.setXwg4tx(blanks(40));
			zlines();
		}
		stateVariable.setDssel(blanks(1));
		nmfkpinds.setPgmInd99(isLastError());
	}

	private void addrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("A ");
			zzNxtFun = "WWCONHDR02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void dsprec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12() && ! nmfkpinds.pgmInd99() && ! nmfkpinds.pgmInd66()) {
			stateVariable.setZzfunmode("D ");
			zzNxtFun = "WWCONHDR02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void chgrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12() && ! nmfkpinds.pgmInd99() && ! nmfkpinds.pgmInd66()) {
			stateVariable.setZzfunmode("B ");
			zzNxtFun = "WWCONHDR02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void delrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12() && ! nmfkpinds.pgmInd99() && ! nmfkpinds.pgmInd66()) {
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWCONHDR02D         ";
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
		dataIndicator[7] = false;
		dataIndicator[37] = false;
		dataIndicator[23] = false;
		dataIndicator[36] = false;
		dataIndicator[40] = false;
		dataIndicator[97] = false;
		dataIndicator[41] = false;
		dataIndicator[42] = false;
		dataIndicator[43] = false;
		dataIndicator[32] = false;
	}

	public boolean dataIndicator[] = new boolean[100];
	public LastIO lastIO = new LastIO();

	private static final String dateFormat = "*ISO";
	private final Integer uwRow1 = 9;
	private Fileds fileds = new Fileds();
	public WorkWithOrdersPDO stateVariable = new WorkWithOrdersPDO();
	public DataCRUD<ContractHeader, Integer> conhdrl1 = new DataCRUD<ContractHeader, Integer>(ContractHeader.class, this, new String[] { "xwbccd", "xwordn" }, new char[] { 'A', 'A' }, new char[] { 'R', 'N' });
	private DataCRUD<ContractHeader, Integer> contractHeader = new DataCRUD<ContractHeader, Integer>(ContractHeader.class, this, new String[] { "xwordn" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<OrderStatusDescription, String> orderStatusDescription = new DataCRUD<OrderStatusDescription, String>(OrderStatusDescription.class, this, new String[] { "xwstat" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Purchases, String> purchases = new DataCRUD<Purchases, String>(Purchases.class, this, new String[] { "xwbccd" }, new char[] { 'A' }, new char[] { 'R' }, new String[][] { { "PERCUS", "PERSON" } });
	private DataCRUD<Salespersons, String> salespersons = new DataCRUD<Salespersons, String>(Salespersons.class, this, new String[] { "person" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Sites, Integer> sites = new DataCRUD<Sites, Integer>(Sites.class, this, new String[] { "cusno" }, new char[] { 'A' }, new char[] { 'N' });
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
		@Column(name="FUN_KEY_07", length=1)
		public Boolean funKey07() {
			return getBoolVal(subString(objectToString(nmfkpinds), 7, 1));
		}
		public void setFunKey07(Boolean funKey07) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 7, 1, bool2Str(funKey07)));
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
		@Column(name="PGM_IND_42", length=1)
		public Boolean pgmInd42() {
			return getBoolVal(subString(objectToString(nmfkpinds), 42, 1));
		}
		public void setPgmInd42(Boolean pgmInd42) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 42, 1, bool2Str(pgmInd42)));
		}
		@Column(name="PGM_IND_43", length=1)
		public Boolean pgmInd43() {
			return getBoolVal(subString(objectToString(nmfkpinds), 43, 1));
		}
		public void setPgmInd43(Boolean pgmInd43) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 43, 1, bool2Str(pgmInd43)));
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
		@Column(name="PGM_IND_77", length=1)
		public Boolean pgmInd77() {
			return getBoolVal(subString(objectToString(nmfkpinds), 77, 1));
		}
		public void setPgmInd77(Boolean pgmInd77) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 77, 1, bool2Str(pgmInd77)));
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
	public WorkWithOrdersGDO gridVariable[] = initArray(WorkWithOrdersGDO.class, uwRow1 + 1);
}