package acdemxaMvcprocess.logic;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import acdemxaMvcprocess.data.Salespersons;
import acdemxaMvcprocess.logic.data.FilterFldData;
import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.ResponseDataObject;
import acdemxaMvcprocess.logic.data.SalespersonsSelectionPDO;
import acdemxaMvcprocess.logic.data.SalespersonsSelectionGDO;
import acdemxaMvcprocess.logic.ext.SalespersonsSelectionExt;

import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import com.databorough.utils.NewScreenException;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * Program logic for SalespersonsSelection (SLMENSEL).
 *
 * @author KAMALN
 */
@Path("/SalespersonsSelection")
public class SalespersonsSelection {

	@GET
	@Path("/initialize")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject initialize(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			assignObject(slmenselra.stateVariable, stateVariable);
			int numRows = stateVariable.getNumRows();

			if (slmenselra.gridVariable.length != (numRows + 1)) {
				slmenselra.gridVariable = initArray(SalespersonsSelectionGDO.class,
						numRows + 1);
			}

			nextFunction = "SLMENSEL01D";
			slmenselra.pgmInitialize();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(messages, slmenselra.messages);

		retParms.setStateVariable(slmenselra.stateVariable);
		retParms.setGridVariable(slmenselra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// slmensel01dd
	@GET
	@Path("/entryPanelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelDisplay(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// slmensel01dp
	@GET
	@Path("/entryPanelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject entryPanelProcess(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(slmenselra.nmfkpinds, stateVariable.getQin());
			assignObject(slmenselra.stateVariable, stateVariable);

			int numRows = stateVariable.getNumRows();

			if (slmenselra.gridVariable.length != (numRows + 1)) {
				slmenselra.gridVariable = initArray(SalespersonsSelectionGDO.class,
						numRows + 1);
			}

			slmenselra.salespersons.lastReadEntity = new Salespersons();
			assignObject(slmenselra.salespersons.lastReadEntity, stateVariable, true);

			slmenselra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();

			if ("SLMENSEL01D".equals(nextFunction.trim())) {
				nextFunction = "";
			}
		}

		assignObject(stateVariable, slmenselra.stateVariable);
		assignObject(messages, slmenselra.messages);

		retParms.setStateVariable(stateVariable);
		retParms.setGridVariable(slmenselra.gridVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(slmenselra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// slmensel01gd
	@GET
	@Path("/gridDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridDisplay(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// slmensel01gp
	@GET
	@Path("/gridProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject gridProcess(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		MessageObject messages[] = initArray(MessageObject.class, 20);

		try {
			setState(stateVariable.getQin());
			setObject(slmenselra.nmfkpinds, stateVariable.getQin());
			assignObject(slmenselra.stateVariable, stateVariable);
			slmenselra.entryPanelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, slmenselra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("01D")) {
			assignObject(messages, slmenselra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(slmenselra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// slmensel02dd
	@GET
	@Path("/panelDisplay")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelDisplay(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	// slmensel02dp
	@GET
	@Path("/panelProcess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataObject panelProcess(
		@QueryParam("stateVariable") SalespersonsSelectionPDO stateVariable,
		@QueryParam("action") String action,
		@QueryParam("nextFunction") String nextFunction,
		@QueryParam("nextProgram") String nextProgram) {
		setObject(slmenselra.nmfkpinds, stateVariable.getQin());
		MessageObject messages[] = initArray(MessageObject.class, 20);
		action = "";

		try {
			assignObject(slmenselra.stateVariable, stateVariable);
			//slmenselra.panelProcess();
		}
		catch (NewScreenException nse) {
			nextFunction = nse.getNextScreen();
			nextProgram = nse.getNextProgram();
		}

		assignObject(stateVariable, slmenselra.stateVariable);

		if ("".equals(nextFunction) || nextFunction.trim().endsWith("02D")) {
			assignObject(messages, slmenselra.messages);
		}

		retParms.setStateVariable(stateVariable);
		retParms.setMessages(messages);

		if (messages[0].getMessageStatus() == 1) {
			return retParms;
		}

		stateVariable.setQin(objectToString(slmenselra.nmfkpinds));

		retParms.setAction(action);
		retParms.setNextFunction(nextFunction);
		retParms.setNextProgram(nextProgram);

		return retParms;
	}

	private void setState(String qin) {
		for (int indIdx = 1; indIdx <= 99; indIdx++) {
			slmenselra.dataIndicator[indIdx] =
				getBoolVal(subString(qin, indIdx, 1));
		}
	}

	private SalespersonsSelectionExt slmenselra =
		new SalespersonsSelectionExt();
	public ArrayList<FilterFldData> filterFlds;
	public Map<String, Boolean> fldsFiltered;
	private ResponseDataObject retParms = new ResponseDataObject();
}