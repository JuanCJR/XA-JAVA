package acdemxaMvcprocess.logic;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import acdemxaMvcprocess.data.Purchases;
import acdemxaMvcprocess.logic.data.FilterFldData;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.ResponseDataObject;
import acdemxaMvcprocess.logic.data.CustomerSelectionPDO;
import acdemxaMvcprocess.logic.data.CustomerSelectionGDO;
import acdemxaMvcprocess.logic.ext.CustomerSelectionExt;

import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import com.databorough.utils.NewScreenException;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for CustomerSelection (CUSTSSEL).
 *
 * @author KAMALN
 */
@Path("/CustomerSelection")
public class CustomerSelection {

	@GET
	@Path("/initialize")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject initialize(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			assignObject(custsselra.stateVariable, stateVariable);
			int numRows = stateVariable.getNumRows();

			if (custsselra.gridVariable.length != (numRows + 1)) {
				custsselra.gridVariable = initArray(CustomerSelectionGDO.class,
						numRows + 1);
			}

			nextFunction = "CUSTSSEL01D";
			custsselra.pgmInitialize();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(messages, custsselra.messages);

		retParms.setStateVariable(custsselra.stateVariable);
		retParms.setGridVariable(custsselra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// custssel01dd
	@GET
	@Path("/entryPanelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelDisplay(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// custssel01dp
	@GET
	@Path("/entryPanelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelProcess(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(custsselra.nmfkpinds, stateVariable.getQin());
			assignObject(custsselra.stateVariable, stateVariable);

			int numRows = stateVariable.getNumRows();

			if (custsselra.gridVariable.length != (numRows + 1)) {
				custsselra.gridVariable = initArray(CustomerSelectionGDO.class,
						numRows + 1);
			}

			custsselra.purchases.lastReadEntity = new Purchases();
			assignObject(custsselra.purchases.lastReadEntity, stateVariable, true);

			custsselra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();

			if ("CUSTSSEL01D".equals(nextFunction.trim())) {
				nextFunction = "";
			}
		}

		assignObject(stateVariable, custsselra.stateVariable);
		assignObject(messages, custsselra.messages);

		retParms.setStateVariable(stateVariable);
		retParms.setGridVariable(custsselra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(custsselra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// custssel01gd
	@GET
	@Path("/gridDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridDisplay(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// custssel01gp
	@GET
	@Path("/gridProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridProcess(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(custsselra.nmfkpinds, stateVariable.getQin());
			assignObject(custsselra.stateVariable, stateVariable);
			custsselra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, custsselra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("01D")) {
			assignObject(messages, custsselra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(custsselra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// custssel02dd
	@GET
	@Path("/panelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelDisplay(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// custssel02dp
	@GET
	@Path("/panelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelProcess(
		@QueryParam("stateVariable") CustomerSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		setObject(custsselra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		try {
			assignObject(custsselra.stateVariable, stateVariable);
			//custsselra.panelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, custsselra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("02D")) {
			assignObject(messages, custsselra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(custsselra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	private void setState(String qin) {
		for (int indIdx = 1; indIdx <= 99; indIdx++) {
			custsselra.dataIndicator[indIdx] =
				getBoolVal(subString(qin, indIdx, 1));
		}
	}

	private CustomerSelectionExt custsselra =
		new CustomerSelectionExt();
	public ArrayList<FilterFldData> filterFlds;
	public Map<String, Boolean> fldsFiltered;
	private ResponseDataObject retParms = new ResponseDataObject();
}