package acdemxaMvcprocess.logic;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import acdemxaMvcprocess.data.ContractHeader;
import acdemxaMvcprocess.logic.data.FilterFldData;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.ResponseDataObject;
import acdemxaMvcprocess.logic.data.WorkWithOrdersPDO;
import acdemxaMvcprocess.logic.data.WorkWithOrdersGDO;
import acdemxaMvcprocess.logic.ext.WorkWithOrdersExt;

import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import com.databorough.utils.NewScreenException;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for WorkWithOrders (WWCONHDR).
 *
 * @author KAMALN
 */
@Path("/WorkWithOrders")
public class WorkWithOrders {

	@GET
	@Path("/initialize")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject initialize(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			assignObject(wwconhdrra.stateVariable, stateVariable);
			int numRows = stateVariable.getNumRows();

			if (wwconhdrra.gridVariable.length != (numRows + 1)) {
				wwconhdrra.gridVariable = initArray(WorkWithOrdersGDO.class,
						numRows + 1);
			}

			nextFunction = "WWCONHDR01D";
			wwconhdrra.pgmInitialize();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(messages, wwconhdrra.messages);

		retParms.setStateVariable(wwconhdrra.stateVariable);
		retParms.setGridVariable(wwconhdrra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr01dd
	@GET
	@Path("/entryPanelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelDisplay(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr01dp
	@GET
	@Path("/entryPanelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelProcess(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(wwconhdrra.nmfkpinds, stateVariable.getQin());
			assignObject(wwconhdrra.stateVariable, stateVariable);

			int numRows = stateVariable.getNumRows();

			if (wwconhdrra.gridVariable.length != (numRows + 1)) {
				wwconhdrra.gridVariable = initArray(WorkWithOrdersGDO.class,
						numRows + 1);
			}

			wwconhdrra.conhdrl1.lastReadEntity = new ContractHeader();
			assignObject(wwconhdrra.conhdrl1.lastReadEntity, stateVariable, true);

			wwconhdrra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();

			if ("WWCONHDR01D".equals(nextFunction.trim())) {
				nextFunction = "";
			}
		}

		assignObject(stateVariable, wwconhdrra.stateVariable);
		assignObject(messages, wwconhdrra.messages);

		retParms.setStateVariable(stateVariable);
		retParms.setGridVariable(wwconhdrra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwconhdrra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr01gd
	@GET
	@Path("/gridDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridDisplay(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr01gp
	@GET
	@Path("/gridProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridProcess(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(wwconhdrra.nmfkpinds, stateVariable.getQin());
			assignObject(wwconhdrra.stateVariable, stateVariable);
			wwconhdrra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwconhdrra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("01D")) {
			assignObject(messages, wwconhdrra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwconhdrra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr02dd
	@GET
	@Path("/panelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelDisplay(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr02dp
	@GET
	@Path("/panelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelProcess(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		setObject(wwconhdrra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		try {
			assignObject(wwconhdrra.stateVariable, stateVariable);
			wwconhdrra.panelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwconhdrra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("02D")) {
			assignObject(messages, wwconhdrra.messages);
		}

		if (nextFunction.trim().endsWith("04D")) {
			stateVariable.setZzmode("CONFIRM");
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwconhdrra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr04dd
	@GET
	@Path("/panel4Display")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel4Display(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwconhdr04dp
	@GET
	@Path("/panel4Process")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel4Process(
		@QueryParam("stateVariable") WorkWithOrdersPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		boolean confirmationScreen = false;
		setObject(wwconhdrra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		if ("CONFIRM".equals(stateVariable.getZzmode())) {
			confirmationScreen = true;
			stateVariable.setZzmode("");
		}

		try {
			assignObject(wwconhdrra.stateVariable, stateVariable);
			wwconhdrra.panel4Process();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		if (confirmationScreen) {
			nextFunction = "WWCONHDR01D";
			nextProgram = "";
		}
		else {
			assignObject(messages, wwconhdrra.messages);

			if (nextFunction.startsWith("WWCONHDR")) {
				if (messages[0].getMessageStatus() == 0) {
					stateVariable.setZzmode("CONFIRM");
				}

				nextFunction = "WWCONHDR04D";
				nextProgram = "";
			}
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwconhdrra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	private void setState(String qin) {
		for (int indIdx = 1; indIdx <= 99; indIdx++) {
			wwconhdrra.dataIndicator[indIdx] =
				getBoolVal(subString(qin, indIdx, 1));
		}
	}

	private WorkWithOrdersExt wwconhdrra =
		new WorkWithOrdersExt();
	public ArrayList<FilterFldData> filterFlds;
	public Map<String, Boolean> fldsFiltered;
	private ResponseDataObject retParms = new ResponseDataObject();
}