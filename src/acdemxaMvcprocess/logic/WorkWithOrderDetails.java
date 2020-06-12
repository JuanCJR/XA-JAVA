package acdemxaMvcprocess.logic;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import acdemxaMvcprocess.data.ContractDetail;
import acdemxaMvcprocess.logic.data.FilterFldData;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.ResponseDataObject;
import acdemxaMvcprocess.logic.data.WorkWithOrderDetailsPDO;
import acdemxaMvcprocess.logic.data.WorkWithOrderDetailsGDO;
import acdemxaMvcprocess.logic.ext.WorkWithOrderDetailsExt;

import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import com.databorough.utils.NewScreenException;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for WorkWithOrderDetails (WWCONDET).
 *
 * @author KAMALN
 */
@Path("/WorkWithOrderDetails")
public class WorkWithOrderDetails {

	@GET
	@Path("/initialize")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject initialize(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			assignObject(wwcondetra.stateVariable, stateVariable);
			int numRows = stateVariable.getNumRows();

			if (wwcondetra.gridVariable.length != (numRows + 1)) {
				wwcondetra.gridVariable = initArray(WorkWithOrderDetailsGDO.class,
						numRows + 1);
			}

			nextFunction = "WWCONDET01D";
			wwcondetra.pgmInitialize();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(messages, wwcondetra.messages);

		retParms.setStateVariable(wwcondetra.stateVariable);
		retParms.setGridVariable(wwcondetra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet01dd
	@GET
	@Path("/entryPanelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelDisplay(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet01dp
	@GET
	@Path("/entryPanelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelProcess(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(wwcondetra.nmfkpinds, stateVariable.getQin());
			assignObject(wwcondetra.stateVariable, stateVariable);

			int numRows = stateVariable.getNumRows();

			if (wwcondetra.gridVariable.length != (numRows + 1)) {
				wwcondetra.gridVariable = initArray(WorkWithOrderDetailsGDO.class,
						numRows + 1);
			}

			wwcondetra.contractDetail.lastReadEntity = new ContractDetail();
			assignObject(wwcondetra.contractDetail.lastReadEntity, stateVariable, true);

			wwcondetra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();

			if ("WWCONDET01D".equals(nextFunction.trim())) {
				nextFunction = "";
			}
		}

		assignObject(stateVariable, wwcondetra.stateVariable);
		assignObject(messages, wwcondetra.messages);

		retParms.setStateVariable(stateVariable);
		retParms.setGridVariable(wwcondetra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwcondetra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet01gd
	@GET
	@Path("/gridDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridDisplay(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet01gp
	@GET
	@Path("/gridProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridProcess(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(wwcondetra.nmfkpinds, stateVariable.getQin());
			assignObject(wwcondetra.stateVariable, stateVariable);
			wwcondetra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwcondetra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("01D")) {
			assignObject(messages, wwcondetra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwcondetra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet02dd
	@GET
	@Path("/panelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelDisplay(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet02dp
	@GET
	@Path("/panelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelProcess(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		setObject(wwcondetra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		try {
			assignObject(wwcondetra.stateVariable, stateVariable);
			wwcondetra.panelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwcondetra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("02D")) {
			assignObject(messages, wwcondetra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwcondetra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet03dd
	@GET
	@Path("/panel3Display")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel3Display(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet03dp
	@GET
	@Path("/panel3Process")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel3Process(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		setObject(wwcondetra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		try {
			assignObject(wwcondetra.stateVariable, stateVariable);
			//wwcondetra.panel3Process();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, wwcondetra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("03D")) {
			assignObject(messages, wwcondetra.messages);
		}

		if (nextFunction.trim().endsWith("04D")) {
			stateVariable.setZzmode("CONFIRM");
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwcondetra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet04dd
	@GET
	@Path("/panel4Display")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel4Display(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// wwcondet04dp
	@GET
	@Path("/panel4Process")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panel4Process(
		@QueryParam("stateVariable") WorkWithOrderDetailsPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		boolean confirmationScreen = false;
		setObject(wwcondetra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		if ("CONFIRM".equals(stateVariable.getZzmode())) {
			confirmationScreen = true;
			stateVariable.setZzmode("");
		}

		try {
			assignObject(wwcondetra.stateVariable, stateVariable);
			wwcondetra.panel4Process();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		if (confirmationScreen) {
			nextFunction = "WWCONDET01D";
			nextProgram = "";
		}
		else {
			assignObject(messages, wwcondetra.messages);

			if (nextFunction.startsWith("WWCONDET")) {
				if (messages[0].getMessageStatus() == 0) {
					stateVariable.setZzmode("CONFIRM");
				}

				nextFunction = "WWCONDET04D";
				nextProgram = "";
			}
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(wwcondetra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	private void setState(String qin) {
		for (int indIdx = 1; indIdx <= 99; indIdx++) {
			wwcondetra.dataIndicator[indIdx] =
				getBoolVal(subString(qin, indIdx, 1));
		}
	}

	private WorkWithOrderDetailsExt wwcondetra =
		new WorkWithOrderDetailsExt();
	public ArrayList<FilterFldData> filterFlds;
	public Map<String, Boolean> fldsFiltered;
	private ResponseDataObject retParms = new ResponseDataObject();
}