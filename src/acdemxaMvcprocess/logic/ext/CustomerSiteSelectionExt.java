package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.Sites;
import acdemxaMvcprocess.logic.data.CustomerSiteSelectionGDO;
import acdemxaMvcprocess.logic.data.CustomerSiteSelectionPDO;
import acdemxaMvcprocess.logic.data.MessageObject;
import com.databorough.utils.NewScreenException;
import javax.persistence.Column;
import static acdemxaMvcprocess.data.DataCRUD.isLastError;
import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.StringUtils.replaceStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.POSITION.LOVAL;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for Customer Site Selection (CUSFSELRA).
 *
 * @author KAMALN
 */
public class CustomerSiteSelectionExt {

	/**
	 * Initialises the <code>CustomerSiteSelectionPDO</code>
	 * object to receive the data to display.
	 */
	public void pgmInitialize() {
		zzsetdspfind();
		stateVariable.setCustomer(stateVariable.getCusno());
		//  Build subfile
		// Work field Definitions
		//  True / False Constants
		//  Indicator Definitions
		//  Prototype Declaration.
		// Main Procedure
		// M a i n l i n e   C o d e
		// Build subfile
		zbuild();
		// Until Exit/Cancel
		mainline0();
		// Terminate program
		return;
	}

	/**
	 * Processes the key data to fetch the complete record to be shown on the
	 * subsequent screen.
	 */
	public void entryPanelProcess() {
		// If Exit/Cancel
		if (nmfkpinds.funKey03() || nmfkpinds.funKey12()) {
			stateVariable.setCustomer(0);
			return;
		}
		stateVariable.setCustomer(stateVariable.getDscusno());
		return;
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>Sites</code> (CUSF)
	 * </ul>
	 *
	 * @see Sites
	 */
	private void zbuild() {
		if (stateVariable.getZsflrrn() == 0) {
			sites.fetch(LOVAL);
		}
		sites.next();
		uwScnIdx1 = 0;
		while (! sites.isEndOfFile() && ! isLastError()) {
			stateVariable.setZsflrrn(Integer.valueOf(stateVariable.getZsflrrn() + 1));
			stateVariable.setDscusno(stateVariable.getCusno());
			stateVariable.setDscname(stateVariable.getCname());
			if (uwScnIdx1 < Integer.valueOf(gridVariable.length - 1)) {
				uwScnIdx1 = Integer.valueOf(uwScnIdx1 + 1);
			}
			gridVariable[uwScnIdx1].setDssel("X");
			gridVariable[uwScnIdx1].setDscusno(stateVariable.getDscusno());
			gridVariable[uwScnIdx1].setDscname(stateVariable.getDscname());
			// Reading just one grid page
			if (uwScnIdx1 == Integer.valueOf(gridVariable.length - 1)) {
				break;
			}
			sites.next();
		}
		if (stateVariable.getZsflrrn() > 0) {
			nmfkpinds.setPgmInd31(true);
		}
	}

	private void mainline0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			zzNxtFun = "CUSFSEL01D          ";
			throw new NewScreenException(zzNxtFun, "");
		}
	}

	private void zzsetdspfind() {
		dataIndicator[3] = false;
		dataIndicator[12] = false;
		dataIndicator[31] = false;
		dataIndicator[30] = false;
	}

	public boolean dataIndicator[] = new boolean[100];
	public LastIO lastIO = new LastIO();

	private final Integer uwRow1 = 10;
	public CustomerSiteSelectionPDO stateVariable = new CustomerSiteSelectionPDO();
	public DataCRUD<Sites, Integer> sites = new DataCRUD<Sites, Integer>(Sites.class, this, new String[] { "cusno" }, new char[] { 'A' }, new char[] { 'R' });
	@SuppressWarnings("unused")
	private Integer msgObjIdx = 0;
	private Integer uwScnIdx1 = 0;
	private String zzNxtFun = "";
	public CustomerSiteSelectionGDO gridVariable[] = initArray(CustomerSiteSelectionGDO.class, uwRow1 + 1);
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
		@Column(name="remNmfkpinds", length=99)
		public String remNmfkpinds = "";
	}
}