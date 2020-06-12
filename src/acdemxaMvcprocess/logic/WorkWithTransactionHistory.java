package acdemxaMvcprocess.logic;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import acdemxaMvcprocess.data.TransactionHistory;
import acdemxaMvcprocess.logic.data.FilterFldData;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.ResponseDataObject;
import acdemxaMvcprocess.logic.data.WorkWithTransactionHistoryPDO;
import acdemxaMvcprocess.logic.data.WorkWithTransactionHistoryGDO;
import acdemxaMvcprocess.logic.ext.WorkWithTransactionHistoryExt;

import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import com.databorough.utils.NewScreenException;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for WorkWithTransactionHistory (WWTRNHST).
 *
 * @author KAMALN
 */
@Path("/WorkWithTransactionHistory")
public class WorkWithTransactionHistory {

	@GET
	@Path("/initialize")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject initialize(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			assignObject(wwtrnhstra.stateVariable, stateVariable);
			int numRows = stateVariable.getNumRows();

			if (wwtrnhstra.gridVariable.length != (numRows + 1)) {
				wwtrnhstra.gridVariable = initArray(WorkWithTransactionHistoryGDO.class,
						numRows + 1);
			}

			nextFunction = "WWTRNHST01D";
			wwtrnhstra.pgmInitialize();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(messages, wwtrnhstra.messages);

		retParms.setStateVariable(wwtrnhstra.stateVariable);
		retParms.setGridVariable(wwtrnhstra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst01dd
	@GET
	@Path("/entryPanelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelDisplay(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst01dp
	@GET
	@Path("/entryPanelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelProcess(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(wwtrnhstra.nmfkpinds, stateVariable.getQin());
			assignObject(wwtrnhstra.stateVariable, stateVariable);

			int numRows = stateVariable.getNumRows();

			if (wwtrnhstra.gridVariable.length != (numRows + 1)) {
				wwtrnhstra.gridVariable = initArray(WorkWithTransactionHistoryGDO.class,
						numRows + 1);
			}

			wwtrnhstra.trnhstl3.lastReadEntity = new TransactionHistory();
			assignObject(wwtrnhstra.trnhstl3.lastReadEntity, stateVariable, true);

			wwtrnhstra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();

			if ("WWTRNHST01D".equals(nextFunction.trim())) {
				nextFunction = "";
			}
		}

		assignObject(stateVariable, wwtrnhstra.stateVariable);
		assignObject(messages, wwtrnhstra.messages);

		retParms.setStateVariable(stateVariable);
		retParms.setGridVariable(wwtrnhstra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwtrnhstra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst01gd
	@GET
	@Path("/gridDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridDisplay(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst01gp
	@GET
	@Path("/gridProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridProcess(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(wwtrnhstra.nmfkpinds, stateVariable.getQin());
			assignObject(wwtrnhstra.stateVariable, stateVariable);
			wwtrnhstra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwtrnhstra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("01D")) {
			assignObject(messages, wwtrnhstra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwtrnhstra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst02dd
	@GET
	@Path("/panelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelDisplay(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst02dp
	@GET
	@Path("/panelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelProcess(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		setObject(wwtrnhstra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		try {
			assignObject(wwtrnhstra.stateVariable, stateVariable);
			wwtrnhstra.panelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwtrnhstra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("02D")) {
			assignObject(messages, wwtrnhstra.messages);
		}

		if (nextFunction.trim().endsWith("04D")) {
			stateVariable.setZzmode("CONFIRM");
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwtrnhstra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst04dd
	@GET
	@Path("/panel4Display")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel4Display(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwtrnhst04dp
	@GET
	@Path("/panel4Process")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel4Process(
		@QueryParam("stateVariable") WorkWithTransactionHistoryPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		boolean confirmationScreen = false;
		setObject(wwtrnhstra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		if ("CONFIRM".equals(stateVariable.getZzmode())) {
			confirmationScreen = true;
			stateVariable.setZzmode("");
		}

		try {
			assignObject(wwtrnhstra.stateVariable, stateVariable);
			wwtrnhstra.panel4Process();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		if (confirmationScreen) {
			nextFunction = "WWTRNHST01D";
			nextProgram = "";
		}
		else {
			assignObject(messages, wwtrnhstra.messages);

			if (nextFunction.startsWith("WWTRNHST")) {
				if (messages[0].getMessageStatus() == 0) {
					stateVariable.setZzmode("CONFIRM");
				}

				nextFunction = "WWTRNHST04D";
				nextProgram = "";
			}
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwtrnhstra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	private void setState(String qin) {
		for (int indIdx = 1; indIdx <= 99; indIdx++) {
			wwtrnhstra.dataIndicator[indIdx] =
				getBoolVal(subString(qin, indIdx, 1));
		}
	}

	private WorkWithTransactionHistoryExt wwtrnhstra =
		new WorkWithTransactionHistoryExt();
	public ArrayList<FilterFldData> filterFlds;
	public Map<String, Boolean> fldsFiltered;
	private ResponseDataObject retParms = new ResponseDataObject();
}