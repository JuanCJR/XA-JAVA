package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.ContractDetail;
import acdemxaMvcprocess.data.ContractDetailId;
import acdemxaMvcprocess.data.ContractHeader;
import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.OrderStatusDescription;
import acdemxaMvcprocess.data.ProductMaster;
import acdemxaMvcprocess.data.Purchases;
import acdemxaMvcprocess.data.Salespersons;
import acdemxaMvcprocess.data.StockBalances;
import acdemxaMvcprocess.data.StockBalancesId;
import acdemxaMvcprocess.data.StoreMaster;
import acdemxaMvcprocess.data.TransactionTypeDescription;
import acdemxaMvcprocess.logic.data.FiPsDsFld;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.WorkWithOrderDetailsGDO;
import acdemxaMvcprocess.logic.data.WorkWithOrderDetailsPDO;
import com.databorough.utils.NewScreenException;
import javax.persistence.Column;
import static acdemxaMvcprocess.data.DataCRUD.isLastError;
import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.DateTimeConverter.getDate;
import static com.databorough.utils.MessageUtils.setMsgObj;
import static com.databorough.utils.StringUtils.all;
import static com.databorough.utils.StringUtils.blanks;
import static com.databorough.utils.StringUtils.equal;
import static com.databorough.utils.StringUtils.replaceStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for Work with Order Details (WWCONDETRA).
 *
 * @author KAMALN
 */
public class WorkWithOrderDetailsExt {

	/**
	 * Initialises the <code>WorkWithOrderDetailsPDO</code>
	 * object to receive the data to display.
	 */
	public void pgmInitialize() {
		zzsetdspfind();
		inzsr();
		// D e f i n i t i o n s
		stateVariable.setSflchg("0");
		stateVariable.setSflrrn(0);
		// M a i n l i n e   C o d e
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
		stateVariable.setZmsage(blanks(78));
		// Action command keys
		if (nmfkpinds.pgmInd28()) {
			cmdkey();
		}
		else {
			// Else action selections
			zselec();
		}
		mainline0();
		return;
	}

	/**
	 * Processes the data after validating it.
	 */
	public void panelProcess() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			validt();
			if (! nmfkpinds.pgmInd99()) {
				stateVariable.setXwpric(stateVariable.getXwanpr());
			}
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
				addrec0();
			}
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				stateVariable.setZzfunmode("A ");
				zzNxtFun = "WWCONDET04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			addrec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			validt();
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setZmsage(subString(errmsg, 1, 78));
				chgrec0();
			}
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				stateVariable.setZzfunmode("B ");
				zzNxtFun = "WWCONDET04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			chgrec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			if (nmfkpinds.funKey23()) {
				return;
			}
			delrec0();
			if (nmfkpinds.funKey23()) {
				stateVariable.setZzfunmode("C ");
				zzNxtFun = "WWCONDET04D         ";
				throw new NewScreenException(zzNxtFun, "");
			}
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
		if (equal("D ", stateVariable.getZzfunmode())) {
			stateVariable.setZmsage(blanks(78));
			// - Prompt
			if (nmfkpinds.funKey04()) {
				srprom();
			}
			dsprec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
	}

	/**
	 * <p>Adds data into the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * </ul>
	 *
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * </ul>
	 *
	 * <p>Deletes data from the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * </ul>
	 *
	 * @see ContractDetail
	 */
	public void panel4Process() {
		if (equal("A ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				contractDetail.write();
				nmfkpinds.setPgmInd99(isLastError());
				if (nmfkpinds.pgmInd99()) {
					msgObjIdx = setMsgObj("Y2U0004", "", msgObjIdx, messages);
					stateVariable.setZmsage(subString(errmsg, 1, 78));
				}
				return;
			}
			addrec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
		if (equal("B ", stateVariable.getZzfunmode())) {
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				updrec();
				return;
			}
			chgrec0();
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
		if (equal("C ", stateVariable.getZzfunmode())) {
			nmfkpinds.setPgmInd99(isLastError());
			if (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
				contractDetail.retrieve(stateVariable.getXwordn(), stateVariable.getXwabcd());
				nmfkpinds.setPgmInd36(! lastIO.isFound());
				nmfkpinds.setPgmInd66(isLastError());
				// BR00002 Product found on Contract_Detail and NOT ERROR(CONDET)
				if (! nmfkpinds.pgmInd36() && ! nmfkpinds.pgmInd66()) {
					contractDetail.delete();
					nmfkpinds.setPgmInd99(isLastError());
				}
			}
			stateVariable.setShwrec(stateVariable.getSflrrn());
			stateVariable.setDssel(blanks(1));
			// End: For each selection
			zselec0();
			nmfkpinds.setFunKey12(false);
			mainline0();
			return;
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * <li><code>ProductMaster</code> (STKMAS)
	 * </ul>
	 *
	 * @see ContractDetail
	 * @see ProductMaster
	 */
	private void populateGridData() {
		// Delete subfile
		nmfkpinds.setPgmInd74(true);
		nmfkpinds.setPgmInd71(false);
		nmfkpinds.setPgmInd72(false);
		nmfkpinds.setPgmInd31(false);
		nmfkpinds.setPgmInd74(false);
		// Write subfile
		if (stateVariable.getSflrrn() == 0) {
			contractDetail.fetch(stateVariable.getXwordn());
		}
		contractDetail.next(stateVariable.getXwordn());
		nmfkpinds.setPgmInd31(lastIO.isEndOfFile());
		uwScnIdx1 = 0;
		while (! nmfkpinds.pgmInd31()) {
			stateVariable.setSflrrn(Integer.valueOf(stateVariable.getSflrrn() + 1));
			stateVariable.setDssel(blanks(1));
			stateVariable.setSwabcd(subString(stateVariable.getXwabcd(), 1, 15));
			stateVariable.setSwt8tx(subString(stateVariable.getXwt8tx(), 1, 9));
			productMaster.retrieve(stateVariable.getXwabcd());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00001 Product not found on Product_Master
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setXwhltx(all("-", 10));
			}
			if (uwScnIdx1 < Integer.valueOf(gridVariable.length - 1)) {
				uwScnIdx1 = Integer.valueOf(uwScnIdx1 + 1);
			}
			gridVariable[uwScnIdx1].setDssel("X");
			gridVariable[uwScnIdx1].setSwabcd(stateVariable.getSwabcd());
			gridVariable[uwScnIdx1].setXwabcd(stateVariable.getXwabcd());
			gridVariable[uwScnIdx1].setXwaacs(stateVariable.getXwaacs());
			gridVariable[uwScnIdx1].setSwt8tx(stateVariable.getSwt8tx());
			gridVariable[uwScnIdx1].setXwt8tx(stateVariable.getXwt8tx());
			gridVariable[uwScnIdx1].setXwricd(stateVariable.getXwricd());
			gridVariable[uwScnIdx1].setXwa5qt(stateVariable.getXwa5qt());
			gridVariable[uwScnIdx1].setXwa2cd(stateVariable.getXwa2cd());
			gridVariable[uwScnIdx1].setXwpric(stateVariable.getXwpric());
			gridVariable[uwScnIdx1].setXwhltx(stateVariable.getXwhltx());
			// Reading just one grid page
			if (uwScnIdx1 == Integer.valueOf(gridVariable.length - 1)) {
				break;
			}
			contractDetail.next(stateVariable.getXwordn());
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
			// Richard T: 27 Sep 2010
			if (stateVariable.getSflrrn() > 0) {
				nmfkpinds.setPgmInd71(true);
			}
			else {
				nmfkpinds.setPgmInd71(false);
			}
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
		// - Prompt
		if (nmfkpinds.funKey04()) {
			srprom();
		}
	}

	private void dsprec() {
		nmfkpinds.setPgmInd34(true);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, " DISPLAY"));
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
		if (nmfkpinds.funKey23()) {
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWCONDET04D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>TransactionTypeDescription</code> (TRNTYP)
	 * </ul>
	 *
	 * @see TransactionTypeDescription
	 */
	private void addrec() {
		nmfkpinds.setPgmInd34(false);
		nmfkpinds.setPgmInd36(true);
		nmfkpinds.setPgmInd37(false);
		stateVariable.setActdsp(replaceStr(stateVariable.getActdsp(), 1, 8, "ADDITION"));
		stateVariable.setXwricd("INV");
		transactionTypeDescription.retrieve(stateVariable.getXwricd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00003 Trn_Hst_Trn_Type not found on Transaction_type_description
		if (nmfkpinds.pgmInd99()) {
			stateVariable.setXwtdsc(all("-", 20));
		}
		stateVariable.setXwa2cd("EAC");
		stateVariable.setUmdes(replaceStr(stateVariable.getUmdes(), 1, 11, um[1]));
		addrec0();
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
	 * <li><code>ProductMaster</code> (STKMAS)
	 * <li><code>StoreMaster</code> (STOMAS)
	 * <li><code>StockBalances</code> (STKBAL)
	 * <li><code>TransactionTypeDescription</code> (TRNTYP)
	 * </ul>
	 *
	 * @see ProductMaster
	 * @see StoreMaster
	 * @see StockBalances
	 * @see TransactionTypeDescription
	 */
	private void validt() {
		// -
		nmfkpinds.setPgmInd99(false);
		stateVariable.setZmsage(blanks(78));
		for (int idxCntdo = 1; idxCntdo <= 1; idxCntdo++) {
			productMaster.retrieve(stateVariable.getXwabcd());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00005 Product not found on Product_Master
			if (nmfkpinds.pgmInd99()) {
				msgObjIdx = setMsgObj("OEM0003", "XWABCD", msgObjIdx, messages);
				break;
			}
			// - If addition, pull the price from file:
			if (nmfkpinds.funKey06()) {
				stateVariable.setXwpric(stateVariable.getXwanpr());
			}
			// -
			storeMaster.retrieve(stateVariable.getXwaacs());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00006 Store not found on Store_Master
			if (nmfkpinds.pgmInd99()) {
				msgObjIdx = setMsgObj("OES0369", "XWAACS", msgObjIdx, messages);
				break;
			}
			stockBalances.retrieve(stateVariable.getXwabcd(), stateVariable.getXwaacs());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00007 Store not found on Stock_Balances or CONDET.Contract_Qty >
			// BR Onhand_Quantity
			if (nmfkpinds.pgmInd99() || (stateVariable.getXwa5qt() > stateVariable.getXwbhqt())) {
				msgObjIdx = setMsgObj("OES0370", "XWAACS", msgObjIdx, messages);
				nmfkpinds.setPgmInd99(true);
				break;
			}
			// - Transaction Type:
			transactionTypeDescription.retrieve(stateVariable.getXwricd());
			nmfkpinds.setPgmInd99(! lastIO.isFound());
			// BR00008 Trn_Hst_Trn_Type not found on Transaction_type_description
			if (nmfkpinds.pgmInd99()) {
				stateVariable.setXwtdsc(all("-", 20));
				msgObjIdx = setMsgObj("OES0371", "XWRICD", msgObjIdx, messages);
				break;
			}
			// - Unit of measure:
			// BR00009 U_M = 'EAC'
			if (equal("EAC", stateVariable.getXwa2cd())) {
				stateVariable.setUmdes(replaceStr(stateVariable.getUmdes(), 1, 11, um[1]));
			}
			else {
				nmfkpinds.setPgmInd99(true);
				msgObjIdx = setMsgObj("OES0372", "XWA2CD", msgObjIdx, messages);
				break;
			}
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * </ul>
	 *
	 * @see ContractDetail
	 */
	private void getrec() {
		contractDetail.retrieve(stateVariable.getXwordn(), stateVariable.getXwabcd());
		nmfkpinds.setPgmInd36(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// BR00010 Product not found on Contract_Detail
		if (nmfkpinds.pgmInd36()) {
			msgObjIdx = setMsgObj("OES0115", "XWABCD", msgObjIdx, messages);
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
				validt();
			}
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * </ul>
	 *
	 * <p>Updates data in the following files:
	 * <ul>
	 * <li><code>ContractDetail</code> (CONDET)
	 * </ul>
	 *
	 * @see ContractDetail
	 */
	private void updrec() {
		savedata();
		contractDetail.retrieve(stateVariable.getXwordn(), stateVariable.getXwabcd());
		nmfkpinds.setPgmInd36(! lastIO.isFound());
		nmfkpinds.setPgmInd66(isLastError());
		// BR00011 Product found on Contract_Detail and NOT ERROR(CONDET)
		if (! nmfkpinds.pgmInd36() && ! nmfkpinds.pgmInd66()) {
			restoredata();
			contractDetail.update();
			nmfkpinds.setPgmInd99(isLastError());
			if (nmfkpinds.pgmInd99()) {
				msgObjIdx = setMsgObj("Y2U0007", "", msgObjIdx, messages);
				stateVariable.setZmsage(subString(errmsg, 1, 78));
			}
		}
		else {
			stateVariable.setZmsage(subString(errmsg, 1, 78));
		}
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>ContractHeader</code> (CONHDR)
	 * <li><code>Purchases</code> (CUSTS)
	 * <li><code>Salespersons</code> (SLMEN)
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * </ul>
	 *
	 * @see ContractHeader
	 * @see Purchases
	 * @see Salespersons
	 * @see OrderStatusDescription
	 */
	private void inzsr() {
		id1Ctdta = 1;
		strCtdta = "Each KGS   ";
		for (int idxCtdta = 1; idxCtdta <= 1; idxCtdta++) {
			um[idxCtdta] = subString(strCtdta, id1Ctdta, 11);
			id1Ctdta = Integer.valueOf(id1Ctdta + 11);
		}
		// Initialise message subfile
		nmfkpinds.setPgmInd32(true);
		stateVariable.setZzpgm(replaceStr(stateVariable.getZzpgm(), 1, 8, "WWCONDET"));
		// - Set date
		stateVariable.setZzdate(getDate().toInt());
		// -
		// - CONTRACT
		contractHeader.retrieve(stateVariable.getXwordn());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// - CUSTOMER
		purchases.retrieve(stateVariable.getXwbccd());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00012 Debtor not found on Purchases
		if (nmfkpinds.pgmInd99()) {
			stateVariable.setXwg4tx(all("-", 40));
		}
		// - REPRESENTATIVE
		salespersons.retrieve(stateVariable.getPerson());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00013 Rep not found on Salespersons
		if (nmfkpinds.pgmInd99()) {
			stateVariable.setPname(all("-", 34));
		}
		// - STATUS
		orderStatusDescription.retrieve(stateVariable.getXwstat());
		nmfkpinds.setPgmInd99(! lastIO.isFound());
		// BR00014 Status not found on Order_status_description
		if (nmfkpinds.pgmInd99()) {
			stateVariable.setXwsdsc(all("-", 20));
		}
	}

	private void srprom() {
		// - PRODUCT
		if (equal("XWABCD", stateVariable.getSfield())) {
			zzNxtPgm = "STKMASEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("XWAACS", stateVariable.getSfield())) {
			zzNxtPgm = "STOMASEL";
			throw new NewScreenException("", zzNxtPgm);
		}
		else if (equal("XWRICD", stateVariable.getSfield())) {
			zzNxtPgm = "TRNTPSEL";
			throw new NewScreenException("", zzNxtPgm);
		}
	}

	private void savedata() {
		stateVariable.setZwordn(stateVariable.getXwordn());
		stateVariable.setZwabcd(stateVariable.getXwabcd());
		stateVariable.setZwaacs(stateVariable.getXwaacs());
		stateVariable.setZwt8tx(stateVariable.getXwt8tx());
		stateVariable.setZwricd(stateVariable.getXwricd());
		stateVariable.setZwa5qt(stateVariable.getXwa5qt());
		stateVariable.setZwa2cd(stateVariable.getXwa2cd());
		stateVariable.setZwpric(stateVariable.getXwpric());
	}

	private void restoredata() {
		stateVariable.setXwordn(stateVariable.getZwordn());
		stateVariable.setXwabcd(stateVariable.getZwabcd());
		stateVariable.setXwaacs(stateVariable.getZwaacs());
		stateVariable.setXwt8tx(stateVariable.getZwt8tx());
		stateVariable.setXwricd(stateVariable.getZwricd());
		stateVariable.setXwa5qt(stateVariable.getZwa5qt());
		stateVariable.setXwa2cd(stateVariable.getZwa2cd());
		stateVariable.setXwpric(stateVariable.getZwpric());
	}

	private void mainline0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			// Build subfile
			populateGridData();
			zzNxtFun = "WWCONDET01D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void zselec0() {
		stateVariable.setSflchg("1");
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
		stateVariable.setShwrec(stateVariable.getSflrrn());
		stateVariable.setDssel(blanks(1));
	}

	private void dsprec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("D ");
			zzNxtFun = "WWCONDET02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void chgrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("B ");
			zzNxtFun = "WWCONDET02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void delrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("C ");
			zzNxtFun = "WWCONDET02D         ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void addrec0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			stateVariable.setZzfunmode("A ");
			zzNxtFun = "WWCONDET02D         ";
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
		dataIndicator[32] = false;
	}

	public boolean dataIndicator[] = new boolean[100];
	public LastIO lastIO = new LastIO();

	private final Integer uwRow1 = 9;
	private Fileds fileds = new Fileds();
	public WorkWithOrderDetailsPDO stateVariable = new WorkWithOrderDetailsPDO();
	public DataCRUD<ContractDetail, ContractDetailId> contractDetail = new DataCRUD<ContractDetail, ContractDetailId>(ContractDetail.class, this, new String[] { "xwordn", "xwabcd" }, new char[] { 'A', 'A' }, new char[] { 'R', 'N' });
	private DataCRUD<ContractHeader, Integer> contractHeader = new DataCRUD<ContractHeader, Integer>(ContractHeader.class, this, new String[] { "xwordn" }, new char[] { 'A' }, new char[] { 'R' });
	private DataCRUD<OrderStatusDescription, String> orderStatusDescription = new DataCRUD<OrderStatusDescription, String>(OrderStatusDescription.class, this, new String[] { "xwstat" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<ProductMaster, String> productMaster = new DataCRUD<ProductMaster, String>(ProductMaster.class, this, new String[] { "xwabcd" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<Purchases, String> purchases = new DataCRUD<Purchases, String>(Purchases.class, this, new String[] { "xwbccd" }, new char[] { 'A' }, new char[] { 'R' }, new String[][] { { "PERCUS", "PERSON" } });
	private DataCRUD<Salespersons, String> salespersons = new DataCRUD<Salespersons, String>(Salespersons.class, this, new String[] { "person" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<StockBalances, StockBalancesId> stockBalances = new DataCRUD<StockBalances, StockBalancesId>(StockBalances.class, this, new String[] { "xwabcd", "xwaacs" }, new char[] { 'A', 'A' }, new char[] { 'N', 'N' }, new String[][] { { "WA2CD", "XWA2CD" } });
	private DataCRUD<StoreMaster, String> storeMaster = new DataCRUD<StoreMaster, String>(StoreMaster.class, this, new String[] { "xwaacs" }, new char[] { 'A' }, new char[] { 'N' });
	private DataCRUD<TransactionTypeDescription, String> transactionTypeDescription = new DataCRUD<TransactionTypeDescription, String>(TransactionTypeDescription.class, this, new String[] { "xwricd" }, new char[] { 'A' }, new char[] { 'N' });
	private Integer id1Ctdta = 0;
	private Integer id1Quote = 0;
	private Integer idxCtdta = 0;
	private Integer msgObjIdx = 0;
	private Integer uwScnIdx1 = 0;
	private String errmsg = "";
	private String strCtdta = "";
	private String um[] = initArray(String.class, 2);
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
	public WorkWithOrderDetailsGDO gridVariable[] = initArray(WorkWithOrderDetailsGDO.class, uwRow1 + 1);
}