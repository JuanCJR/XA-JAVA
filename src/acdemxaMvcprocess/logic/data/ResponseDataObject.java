package acdemxaMvcprocess.logic.data;

import javax.xml.bind.annotation.XmlRootElement;
import com.databorough.utils.GridDataObject;
import com.databorough.utils.PanelDataObject;

/**
 * A generic response class for Web Services.
 *
 * @author Amit Arya
 * @version (2014-05-29.18:57:32)
 */
@XmlRootElement
public class ResponseDataObject {
	private PanelDataObject stateVariable;
	private String action;
	private String nextFunction;
	private String nextProgram;
	private GridDataObject gridVariable[];
	private MessageObject messages[];

	public String getAction() {
		return action;
	}

	public GridDataObject[] getGridVariable() {
		return gridVariable;
	}

	public MessageObject[] getMessages() {
		return messages;
	}

	public String getNextFunction() {
		return nextFunction;
	}

	public String getNextProgram() {
		return nextProgram;
	}

	public PanelDataObject getStateVariable() {
		return stateVariable;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setGridVariable(GridDataObject gridVariable[]) {
		this.gridVariable = gridVariable;
	}

	public void setMessages(MessageObject messages[]) {
		this.messages = messages;
	}

	public void setNextFunction(String nextFunction) {
		this.nextFunction = nextFunction;
	}

	public void setNextProgram(String nextProgram) {
		this.nextProgram = nextProgram;
	}

	public void setStateVariable(PanelDataObject stateVariable) {
		this.stateVariable = stateVariable;
	}
}
