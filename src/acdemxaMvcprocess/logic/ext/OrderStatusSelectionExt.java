package acdemxaMvcprocess.logic.ext;

import acdemxaMvcprocess.data.DataCRUD;
import acdemxaMvcprocess.data.LastIO;
import acdemxaMvcprocess.data.OrderStatusDescription;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.OrderStatusSelectionGDO;
import acdemxaMvcprocess.logic.data.OrderStatusSelectionPDO;
import com.databorough.utils.NewScreenException;
import javax.persistence.Column;
import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.StringUtils.replaceStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.POSITION.LOVAL;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for Order status Selection (ORDSTSELRA).
 *
 * @author KAMALN
 */
public class OrderStatusSelectionExt {

	/**
	 * Initialises the <code>OrderStatusSelectionPDO</code>
	 * object to receive the data to display.
	 */
	public void pgmInitialize() {
		zzsetdspfind();
		stateVariable.setPcode(stateVariable.getXwstat());
		stateVariable.setParnam(stateVariable.getXwsdsc());
		//  M a i n l i n e   C o d e
		//  Build subfile
		// Work field Definitions
		//  True / False Constants
		//  Indicator Definitions
		//  Prototype Declaration.
		// Main Procedure
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
			return;
		}
		stateVariable.setPcode(stateVariable.getXwstat());
		stateVariable.setParnam(stateVariable.getXwsdsc());
		return;
	}

	/**
	 * <p>Reads data from the following files:
	 * <ul>
	 * <li><code>OrderStatusDescription</code> (ORDSTS)
	 * </ul>
	 *
	 * @see OrderStatusDescription
	 */
	private void zbuild() {
		if (stateVariable.getSflrrn() == 0) {
			orderStatusDescription.fetch(LOVAL);
		}
		orderStatusDescription.next();
		nmfkpinds.setPgmInd31(lastIO.isEndOfFile());
		uwScnIdx1 = 0;
		while (! nmfkpinds.pgmInd31()) {
			stateVariable.setSflrrn(Integer.valueOf(stateVariable.getSflrrn() + 1));
			if (uwScnIdx1 < Integer.valueOf(gridVariable.length - 1)) {
				uwScnIdx1 = Integer.valueOf(uwScnIdx1 + 1);
			}
			gridVariable[uwScnIdx1].setDssel("X");
			gridVariable[uwScnIdx1].setXwstat(stateVariable.getXwstat());
			gridVariable[uwScnIdx1].setXwsdsc(stateVariable.getXwsdsc());
			// Reading just one grid page
			if (uwScnIdx1 == Integer.valueOf(gridVariable.length - 1)) {
				break;
			}
			orderStatusDescription.next();
			nmfkpinds.setPgmInd31(lastIO.isEndOfFile());
		}
		if (stateVariable.getSflrrn() > 0) {
			nmfkpinds.setPgmInd31(true);
		}
	}

	private void mainline0() {
		while (! nmfkpinds.funKey03() && ! nmfkpinds.funKey12()) {
			zzNxtFun = "ORDSTSEL01D         ";
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
	public OrderStatusSelectionPDO stateVariable = new OrderStatusSelectionPDO();
	public DataCRUD<OrderStatusDescription, String> orderStatusDescription = new DataCRUD<OrderStatusDescription, String>(OrderStatusDescription.class, this, new String[] { "xwstat" }, new char[] { 'A' }, new char[] { 'R' });
	@SuppressWarnings("unused")
	private Integer msgObjIdx = 0;
	private Integer uwScnIdx1 = 0;
	private String zzNxtFun = "";
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
		@Column(name="PGM_IND_30", length=1)
		public Boolean pgmInd30() {
			return getBoolVal(subString(objectToString(nmfkpinds), 30, 1));
		}
		public void setPgmInd30(Boolean pgmInd30) {
			setObject(this, replaceStr(objectToString(nmfkpinds), 30, 1, bool2Str(pgmInd30)));
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
	public OrderStatusSelectionGDO gridVariable[] = initArray(OrderStatusSelectionGDO.class, uwRow1 + 1);
}