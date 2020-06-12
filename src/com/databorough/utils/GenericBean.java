package com.databorough.utils;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import acdemxaMvcprocess.logic.data.MessageObject;
import acdemxaMvcprocess.logic.data.ProgramControlData;
import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.JSFUtils.getRequestStringParameter;
import static com.databorough.utils.JSFUtils.getSessionParam;
import static com.databorough.utils.JSFUtils.removeSessionParam;
import static com.databorough.utils.JSFUtils.setSessionParam;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.NumberUtils.compareTo;
import static com.databorough.utils.NumberUtils.isNumber;
import static com.databorough.utils.ReflectionUtils.getColumnAnnoFldMap;
import static com.databorough.utils.StringUtils.replaceEndString;
import static com.databorough.utils.StringUtils.rtrim;
import static com.databorough.utils.StringUtils.split;
import static com.databorough.utils.StringUtils.substring;
import static com.databorough.utils.StringUtils.trim;
import static com.databorough.utils.StringUtils.uncapitalize;
import static com.databorough.utils.URLUtils.encode;

import com.ibm.as400.access.AS400;

public abstract class GenericBean<T extends GridDataObject> extends IBean
	implements IConstants, Serializable
{
	private static final long serialVersionUID = 1L;
	private static List<Field> srcFlds = null;
	private static ProgramControlData controlData = new ProgramControlData();
	private static int rowsToAdd = getNumAddRows();
	private static int rowsToDisplay = getNumRows();
	private static final int OTHER_ERR_MSG_STATUS = 0;
	private static final int CALLING_PGM_ERR_STATUS = 1;
	private static final int CALLED_PGM_ERR_STATUS = 2;
	private static final int CALLED_PGM_INFO_STATUS = 3;

	protected DataModel<T> dataModel;

	private HttpServletRequest req;
	private HttpServletResponse res;

	private LinkedHashMap<String, Field> fldShortToLongNmMap =
		new LinkedHashMap<String, Field>();
	private List<Object> valueList;
	private Map<String, String> nxtFunMap = new HashMap<String, String>();
	private Map<String, Object> pStateVarMap = new HashMap<String, Object>();	
	protected ResourceBundle bundlePrefNames;

	private String action = "";
	private String closeWindow;
	private String confirmScn;
	private String dateFormat;
	private String formName = "updt";
	private String gridErrorFields = "";

	/**
	 * Stores job's name, user, and number of the server job in which the
	 * program will be run.
	 */
	private String jobDetails;

	// Session id
	private String jsessionid;
	private String mode = ADD_MODE;
	private String nextFunction = "";
	private String nextProgram = "";

	// Parent Screen
	private String parentScn;
	private String previousFunction = "";
	private String previousProgram = "";
	private String screen = "";

	//protected MessageObject messages[] = new MessageObject[20];
	protected MessageObject messages[] = initArray(MessageObject.class, 20);

	// use for editable grid to check update/add mode
	private boolean add = false;
	private boolean doItOnce = true;
	private boolean errorFound;
	private boolean filterApplied;

	// use to check that filter condition is applied or not
	private boolean notFilterMode;
	private boolean pgmCalled;
	private boolean postProcessingCycle;
	private boolean readonly;

	// use to disable submit button in certain condition
	private boolean submitButtonDisabled = false;

	/**
	 * Presents the errors and highlights the fields in error on the screen.
	 *
	 * @param errorFields error field names
	 * @param errorMessage message to show
	 * @param isFlat whether a screen is a flat or grid
	 */
	public void addError(String errorFields[], String errorMessage,
		boolean isFlat)
	{
		formName = isFlat ? "updt" : "updtGrid";
		removeStyleClass(formName);
		gridErrorFields = "";

		FacesContext facesCtx = FacesContext.getCurrentInstance();
		UIComponent form = facesCtx.getViewRoot().findComponent(formName);
		String messagedFieldStyle = THEME + FONT_ERROR;
		facesCtx.addMessage(formName, new FacesMessage(errorMessage));

		int errorFldCount = (errorFields == null) ? 0 : errorFields.length;

		if (errorFldCount == 0)
		{
			return;
		}

		for (String errorField : errorFields)
		{
			if ((errorField == null) || "".equals(errorField.trim()))
			{
				continue;
			}

			// Apply Style class to error field
			UIComponent component = null;

			try
			{
				if (isFlat)
				{
					component = form.findComponent(errorField);
				}
				else
				{
					component = form.findComponent("grid");
					component = component.findComponent(errorField);
				}
			}
			catch (Exception e)
			{
			}

			if (component == null)
			{
				continue;
			}

			if (isFlat)
			{
				String styleClass =
					(String)component.getAttributes().get(STYLE_CLASS);
				String stylesFound[] = split(styleClass);

				if ((stylesFound != null) &&
						(
							Utils.lookupStrInStrArr(messagedFieldStyle,
								stylesFound, 0) == -1
						))
				{
					styleClass += (" " + messagedFieldStyle);
				}

				component.getAttributes().put(STYLE_CLASS, styleClass);
			}
			else
			{
				// New code - 02-03-15
				String cmpid = component.getClientId();

				if (!gridErrorFields.contains(cmpid))
				{
					gridErrorFields += cmpid + ",";
				}
			}
		} // errorField
	}

	/**
	 * Adds the program name to the call stack to help in screen navigation.
	 *
	 * @param screen program name to be added to the call stack
	 * @param pStateVar current  status of the program
	 */
	protected void addIntoCallStack(String screen, Object pStateVar)
	{
		this.screen = screen;

		if (screen.endsWith("Grid"))
		{
			screen = substring(screen, 0, screen.length() - 4) + "EntryPanel";
		}

		//if (bundlePrefNames == null)
		if ((bundlePrefNames == null) && (screen.length() == 10))
		{
			screen = screen.toLowerCase();
		}

		LinkedList<String> callStack = getSessionParam("callStack");

		if (callStack == null)
		{
			callStack = new LinkedList<String>();
			setSessionParam("callStack", callStack);
		}

		if ((callStack != null) && callStack.contains(screen))
		{
			int index = callStack.indexOf(screen);
			int size = callStack.size();

			for (int i = index + 1; i < size; i++)
			{
				callStack.remove(i);
				i--;
				size--;
			}
		}
		else
		{
			callStack.add(screen);

			if (pStateVar != null)
			{
				pStateVarMap.put(screen, pStateVar);
			}

			nxtFunMap.put(screen, nextFunction);
		}

		setSessionParam("callStack", callStack);
	}

	/**
	 * Manages the Cancel functionality and returns to the previous screen.
	 *
	 * @param pStateVar current status of the program
	 * @param pgmNm current program name
	 * @param popup <tt>true</tt> if screen is a popup screen
	 * @return screen name to go to
	 */
	protected String cancel(Object pStateVar, String pgmNm, boolean popup)
	{
		String screen;
		LinkedList<String> callStack = getSessionParam("callStack");

		if ((callStack != null) && !callStack.isEmpty())
		{
			if (popup)
			{
				screen = callStack.get(callStack.size() - 1);
			}
			else
			{
				callStack.remove(callStack.size() - 1);
				screen = (!callStack.isEmpty())
					? callStack.get(callStack.size() - 1) : "application";
			}
		}
		else
		{
			screen = "application";
		}

		/* If screen to go to is related with same bean then its StateVar value
		 * needs to be set according to the screen to go.
		 */
		if (screen.toUpperCase().startsWith(pgmNm.toUpperCase()) &&
				(pStateVar != null))
		{
			nextFunction = nxtFunMap.get(screen);

			Object obj = pStateVarMap.get(screen);

			if (obj != null)
			{
				//pStateVar = obj;
				assignObject(pStateVar, obj);
			}
		}
		else
		{
			// If screen to go to is application then no need to set cancelPress
			// to true
			if (!isBatchPgm(pgmNm))
			{
				setCancelPress(!"application".equals(screen));
			}

			doItOnce = true;
		}

		errorFound = false;

		return screen;
	}

	/**
	 * Checks for the error returned by the called program.
	 */
	protected void checkCalledPgmError()
	{
		Object rtnMsgObj = getSessionParam("rtnmsgobj");
		resetMessageObject();
		assignObject(messages, rtnMsgObj);

		String errFlds[] = new String[messages.length];
		String errMsgs = "";
		int num = 0;

		for (MessageObject msgObj : messages)
		{
			if ((
					(msgObj.getMessageStatus() == CALLED_PGM_ERR_STATUS) ||
					(msgObj.getMessageStatus() == CALLED_PGM_INFO_STATUS) ||
					(msgObj.getMessageStatus() == CALLING_PGM_ERR_STATUS)
				) && (trim(msgObj.getMessageText()).length() != 0))
			{
				errFlds[num] = msgObj.getMessageField().toString();
				errMsgs += (msgObj.getMessageText().toString().trim() + " | ");

				if (msgObj.getMessageStatus() != CALLED_PGM_INFO_STATUS)
				{
					errorFound = true;
				}

				num++;
			}
		}

		if (num > 0)
		{
			//errorFound = true;
			errMsgs = replaceEndString(errMsgs, " | ", " ");
			addError(errFlds, errMsgs, true);
		}

		if (errorFound)
		{
			setSessionParam("rtnmsgobj", null);
		}
	}

	/**
	 * Controls the rendering of actions (buttons) on the screen at runtime.
	 *
	 * @param btnId button id
	 * @param isGridBtn <tt>true</tt> if button is on grid
	 * @return <tt>true</tt> if button is to be shown at runtime
	 */
	public String controlButtonRendering(String btnId, boolean isGridBtn)
	{
		String screenToUse = getScreenToUse(isGridBtn);
		Map<Integer, String> ref = ref(screenToUse.toLowerCase() + btnId);

		if (ref == null)
		{
			return "true";
		}

		boolean btnToRender = true;
		Set<Integer> keySet = ref.keySet();

		for (int index : keySet)
		{
			String str = ref.get(index);
			String attribute = str.substring(1);

			if ("ALLOWED".equals(attribute))
			{
				int indicatorValue = Integer.parseInt(str.substring(0, 1));
				btnToRender = isIndicatorOn(index, indicatorValue);
			}
		}

		return new Boolean(btnToRender).toString();
	}

	/**
	 * Checks if a method execute only once.
	 *
	 * @return <tt>true</tt> if a method execute only once
	 */
	public boolean doItOnce()
	{
		return doItOnce;
	}

	/**
	 * Forwards to the specific screen available within the server from where
	 * the call is made. This transfer of control is done by the container
	 * internally and browser is not involved.
	 *
	 * @param screen screen name
	 */
	protected void forwardToScreen(String screen)
	{
		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			setServletContext(facesCtx);

			String nextActionHref = req.getContextPath() + "/#/" + screen;
			req.getRequestDispatcher(nextActionHref).forward(req, res);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Generates a list of passed parameters.
	 *
	 * @param param passed parameters vararg
	 * @return <tt>List</tt> of passed parameters
	 */
	protected List<Object> genParamList(Object... param)
	{
		List<Object> valuesList = new ArrayList<Object>();

		if (param == null)
		{
			return valuesList;
		}

		Collections.addAll(valuesList, param);

		return valuesList;
	}

	/**
	 * Gets the action.
	 *
	 * @return action
	 */
	public String getAction()
	{
		return action;
	}

	protected AS400 getAS400()
	{
		return getSessionParam("as400");
	}

	/**
	 * @return the closeWindow
	 */
	public String getCloseWindow()
	{
		return closeWindow;
	}

	public String getConfirmScn()
	{
		return confirmScn;
	}

	public DataModel<T> getDataModel()
	{
		return dataModel;
	}

	/**
	 * Gets the date format.
	 *
	 * @return the date format
	 */
	public String getDateFormat()
	{
		if (Utils.length(dateFormat) != 0)
		{
			return dateFormat;
		}

		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ResourceBundle bundle =
				facesCtx.getApplication().getResourceBundle(facesCtx, "config");
			dateFormat = bundle.getString("dateFormat");
		}
		catch (Exception e)
		{
			dateFormat = "dd/mm/yyyy";
		}

		return dateFormat;
	}

	/**
	 * Prevents Descriptor image that is down.gif from being shown in Display/
	 * Delete mode.
	 *
	 * @return String containing CSS style applied to Descriptor image if screen
	 *         is read only; otherwise null
	 * @since (2008-01-22.17:25:01)
	 */
	public String getDescriptorImageStyle()
	{
		if (readonly)
		{
			// For hiding the Submit button when Display/Delete mode
			return "display:none";
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the error fields on grid.
	 *
	 * @return error fields on grid
	 */
	public String getGridErrorFields()
	{
		return gridErrorFields;
	}

	/**
	 * Returns job's name, user, and number of the server job in which the
	 * program will be run.
	 * <p>
	 * Typical uses include:
	 * <ul>
	 * <li> before run(), to identify the job before calling the program;<BR>
	 * <li> after run(), to see what job the program ran under (to identify the
	 * job log, for example).
	 * </ul>
	 *
	 * @return String containing server job's name, user, and number
	 * @since (2004-10-14.17:26:02)
	 */
	public String getJobDetails()
	{
		return jobDetails;
	}

	/**
	 * Gets key from prefNames.property file.
	 *
	 * @param value
	 * @return key name
	 */
	public String getKeyByValue(String value)
	{
		try
		{
			for (Enumeration<String> e = bundlePrefNames.getKeys();
					e.hasMoreElements();)
			{
				String elementKey = e.nextElement();
				String bundleVal = bundlePrefNames.getString(elementKey);

				if (bundleVal.equalsIgnoreCase(value))
				{
					return elementKey;
				}
			}
		}
		catch (Exception e)
		{
			return value;
		}

		return value;
	}

	public String getMode()
	{
		return mode;
	}

	/**
	 * Gets the next function name.
	 *
	 * @return next function name
	 */
	public String getNextFunction()
	{
		return nextFunction;
	}

	/**
	 * Gets the next program name.
	 *
	 * @return next program name
	 */
	public String getNextProgram()
	{
		return nextProgram;
	}

	/**
	 * Loads the number of Add rows from the configuration.properties.
	 *
	 * @return number of Add rows
	 */
	private static int getNumAddRows()
	{
		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ResourceBundle bundle =
				facesCtx.getApplication().getResourceBundle(facesCtx, "config");
			rowsToAdd = Integer.parseInt(bundle.getString("numAddRows"));
		}
		catch (Exception e)
		{
			rowsToAdd = -1;
		}

		return rowsToAdd;
	}

	/**
	 * Loads the number of rows from the configuration.properties.
	 *
	 * @return number of rows
	 */
	private static int getNumRows()
	{
		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ResourceBundle bundle =
				facesCtx.getApplication().getResourceBundle(facesCtx, "config");
			rowsToDisplay = Integer.parseInt(bundle.getString("numRows"));
		}
		catch (Exception e)
		{
			rowsToDisplay = -1;
		}

		return rowsToDisplay;
	}

	/**
	 * Gets the parent screen name of this screen.
	 *
	 * @return parent screen name of this screen
	 */
	public String getParentScn()
	{
		return parentScn;
	}

	/**
	 * Returns the well-defined name of the screen.
	 *
	 * @param shortName screen short name
	 * @return equivalent long name
	 */
	public String getPrefLongName(String shortName)
	{
		try
		{
			if (bundlePrefNames == null)
			{
				FacesContext facesCtx = FacesContext.getCurrentInstance();
				bundlePrefNames =
					facesCtx.getApplication().getResourceBundle(facesCtx,
							"prefNames");
			}

			return bundlePrefNames.getString(shortName.trim());
		}
		catch (Exception e)
		{
			return shortName;
		}
	}

	/**
	 * Gets the previous function name.
	 *
	 * @return previous function name
	 */
	public String getPreviousFunction()
	{
		return previousFunction;
	}

	/**
	 * Gets the previous program name.
	 *
	 * @return previous program name
	 */
	public String getPreviousProgram()
	{
		return previousProgram;
	}

	/**
	 * Gets the map of pStateVar.
	 *
	 * @return pStateVarMap
	 */
	public Map<String, Object> getpStateVarMap()
	{
		return pStateVarMap;
	}

	/**
	 * Collects the passed parameters from the calling to the called program.
	 *
	 * @return <tt>true</tt> if passed request parameter found
	 */
	protected boolean getRequestParam()
	{
		if (getRequestStringParameter(PARENT_SCREEN) != null)
		{
			parentScn = getRequestStringParameter(PARENT_SCREEN);
		}

		if (getURLRequestParameters())
		{
			return true;
		}

		List<Object> params = getSessionParam(PARAM_VALUES);

		if (params != null)
		{
			valueList.addAll(params);
			removeSessionParam(PARAM_VALUES);

			return (valueList.size() != 0);
		}

		params = JSFUtils.getRequestParam(PARAM_VALUES);

		if (params != null)
		{
			valueList.addAll(params);

			return (valueList.size() != 0);
		}

		return false;
	}

	/**
	 * Collects the return value list set by the called program; to be used in
	 * calling program's post process logic.
	 *
	 * @return <tt>List</tt> Return value list
	 */
	protected List<Object> getReturnValList()
	{
		List<Object> rtnValList = new ArrayList<Object>();

		for (int indx = 0;; indx++)
		{
			String parm = getRequestStringParameter("rtn" + indx);

			if (parm == null)
			{
				break;
			}

			rtnValList.add(parm);
		}

		if (rtnValList.size() != 0)
		{
			return rtnValList;
		}

		rtnValList = getSessionParam("rtnValList");

		if (rtnValList != null)
		{
			removeSessionParam("rtnValList");

			return rtnValList;
		}

		rtnValList = JSFUtils.getRequestParam("rtnValList");

		if (rtnValList != null)
		{
			JSFUtils.setRequestParam("rtnValList", null);
		}

		return rtnValList;
	}

	/**
	 * Gets the rowsToAdd.
	 *
	 * @return the rowsToAdd
	 */
	protected int getRowsToAdd()
	{
		if (rowsToAdd == -1)
		{
			rowsToAdd = getNumAddRows();
		}

		return rowsToAdd;
	}

	/**
	 * Gets the rowsToDisplay.
	 *
	 * @return the rowsToDisplay
	 */
	protected int getRowsToDisplay()
	{
		if (rowsToDisplay == -1)
		{
			rowsToDisplay = getNumRows();
		}

		return rowsToDisplay;
	}

	/**
	 * Gets the screen.
	 *
	 * @return screen
	 */
	public String getScreen()
	{
		return screen;
	}

	/**
	 * Decides which screen to go to.
	 *
	 * @param pStateVar current status of the program
	 * @param programName current program name
	 * @param isPopup <tt>true</tt> if screen is a popup screen
	 * @return screen to go to
	 */
	protected String getScreenToNavigateTo(Object pStateVar, String programName,
		boolean isPopup)
	{
		String screenNm = cancel(pStateVar, programName, isPopup);

		while (screenNm.toUpperCase().startsWith(programName.toUpperCase()))
		{
			screenNm = cancel(pStateVar, programName, isPopup);
		}

		return screenNm;
	}

	/**
	 * Gets the screen to use for ProgramControlData.
	 *
	 * @param isGridComp whether a grid button/field/column/checkbox
	 * @return screen
	 */
	private String getScreenToUse(boolean isGridComp)
	{
		String screenToUse;

		if (bundlePrefNames == null)
		{
			if (isGridComp)
			{
				screenToUse = substring(screen, 0, screen.length() - 1) + "g";
			}
			else
			{
				screenToUse = screen;
			}
		}
		else
		{
			String orgScreen = getKeyByValue(screen);

			if (isGridComp)
			{
				screenToUse =
					substring(orgScreen, 0, orgScreen.length() - 1) + "g";
			}
			else
			{
				screenToUse = orgScreen;
			}
		}

		return screenToUse;
	}

	/**
	 * Collects the information of the selected rows.
	 *
	 * @return <tt>List</tt> of selected rows
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getSelectedList()
	{
		List<T> selected = new ArrayList<T>();
		List<T> items = (List<T>)dataModel.getWrappedData();

		for (T item : items)
		{
			if (item.isChecked())
			{
				selected.add(item);
			}
		}

		return selected;
	}

	/**
	 * Returns a string containing the unique identifier assigned to this
	 * session.
	 *
	 * @return String that uniquely identifies this session
	 */
	public String getSessionId()
	{
		return jsessionid;
	}

	public String getStyle()
	{
		String retValue = null;

		if (isReadonly())
		{
			retValue = "background-color: #F1F1F1";
		}

		return retValue;
	}

	public String getStyleAddMode()
	{
		if (isReadonlyAddMode())
		{
			return "background-color: #F1F1F1";
		}
		else
		{
			return "background-color: #FFFFFF";
		}
	}

	public String getTimeZone()
	{
		return TimeZone.getDefault().getID();
	}

	/**
	 * Collects the passed parameters from the calling to the called program.
	 *
	 * @return <tt>true</tt> if passed request parameter found
	 */
	private boolean getURLRequestParameters()
	{
		valueList = new ArrayList<Object>();

		for (int indx = 0;; indx++)
		{
			String parm = getRequestStringParameter(URL_PARAM + indx);

			if (parm == null)
			{
				break;
			}

			valueList.add(parm);
		}

		if (valueList.size() != 0)
		{
			return true;
		}

		for (int indx = 0;; indx++)
		{
			Object parm = JSFUtils.getRequestParam(URL_PARAM + indx);

			if (parm == null)
			{
				break;
			}

			valueList.add(parm);
		}

		return (valueList.size() != 0);
	}

	/**
	 * Returns value list.
	 *
	 * @return valueList
	 */
	public List<Object> getValueList()
	{
		return valueList;
	}

	/**
	 * Flags the row as dirty when a gridData cell is changed.
	 * The dirty row(s) are processed by the logic.
	 *
	 * <p>Invoked through JSF's valueChangeListner
	 *
	 * @param change current component <code>ValueChangeEvent</code>
	 */
	public void gridValueChanged(ValueChangeEvent change)
	{
		if ((change.getNewValue() == null) || (change.getOldValue() == null))
		{
			return;
		}

		String newValue = change.getNewValue().toString().trim();
		String oldValue = change.getOldValue().toString().trim();

		if (newValue.equals(oldValue) ||
				(
					isAdd() && (Utils.length(newValue) == 0) &&
					isNumber(oldValue)
				))
		{
			return;
		}

		if (isNumber(newValue) && isNumber(oldValue))
		{
			int retVal;

			try
			{
				retVal = compareTo(newValue, oldValue, null);
			}
			catch (NumberFormatException nfe)
			{
				retVal = -1;
			}

			if (retVal == 0)
			{
				return;
			}
		}

		UIComponent parent1 = (UIComponent)change.getSource();
		UIComponent parent = parent1.getParent().getParent();

		if (parent instanceof HtmlDataTable)
		{
			HtmlDataTable table = (HtmlDataTable)parent;
			GridDataObject rowData = (GridDataObject)table.getRowData();
			rowData.setDirty(true);
		}
	}

	/**
	 * Checks if the current screen is in add mode.
	 *
	 * @return screen mode
	 */
	public boolean isAdd()
	{
		return add;
	}

	/**
	 * Checks if the program is a Batch program.
	 *
	 * @param pgm program name
	 * @return <tt>true</tt> if this is a Batch program
	 */
	protected boolean isBatchPgm(String pgm)
	{
		String orgPgm = getKeyByValue(pgm);

		boolean isIntRtn =
			(orgPgm.length() >= 8) && orgPgm.startsWith("A") &&
			isNumber(orgPgm.substring(1, 8));

		return (orgPgm.endsWith("XFR") || orgPgm.endsWith("UPC") ||
				orgPgm.endsWith("UPR") || isIntRtn);
	}

	/**
	 * CSS style(s) to be applied when drop down button is rendered.
	 *
	 * @return <tt>true</tt> if screen is read only; otherwise false
	 * @since (2008-01-21.18:40:01)
	 */
	public boolean isButtonToRender()
	{
		return !isReadonly();
	}

	/**
	 * Checks if Cancel button is pressed on the called program screen.
	 *
	 * @return <tt>true</tt> if cancel is opted for
	 */
	protected boolean isCancelPress()
	{
		Boolean cancelPress = false;
		Object obj = getSessionParam("cancelPress");

		if (obj != null)
		{
			cancelPress = (Boolean)obj;
		}

		return cancelPress;
	}

	/**
	 * Checks if Confirmation button is required on the screen at runtime.
	 *
	 * @return <tt>true</tt> if confirmation button is required
	 */
	public boolean isConfirmationButtonRequired()
	{
		return CONFIRMATION_MODE.equals(mode);
	}

	/**
	 * Checks if error found on the screen.
	 *
	 * @return <tt>true</tt> if error found
	 */
	protected boolean isErrorFound()
	{
		return errorFound;
	}

	/**
	 * Checks if filtering is opted by the user on the screen.
	 *
	 * @return <tt>true</tt> if filtering is opted for
	 */
	public boolean isFilterApplied()
	{
		return filterApplied;
	}

	/**
	 * Checks whether the grid has no data. If so, the action buttons are
	 * disabled.
	 *
	 * @return <code>true</code> if grid has no data
	 * @since (2008-02-13.12:25:49)
	 */
	@SuppressWarnings("unchecked")
	public boolean isGridEmpty()
	{
		List<T> items = (List<T>)dataModel.getWrappedData();
		int len = items.size();

		if ((len > 0) && items.get(0).isPositionTo())
		{
			return ((dataModel == null) || (dataModel.getRowCount() == 0) ||
			(len == 1));
		}
		else
		{
			return ((dataModel == null) || (dataModel.getRowCount() == 0));
		}
	}

	/**
	 * Checks for the indicator setting at a specific index for the current
	 * <code>pStateVar.dataInd[indSetting]</code>.
	 *
	 * @param indicator 1/0 (ON/OFF) value to be verified
	 * @param indSetting index of dataInd[] to look at
	 * @return <tt>true</tt> if indicator found
	 */
	protected boolean isIndicatorOn(int indicator, int indSetting)
	{
		return false;
	}

	/**
	 * Checks for the indicator setting at a specific index for the
	 * current <code>gridDataModel[gridRowIndex].dataInd[indSetting]</code>.
	 *
	 * @param indicator 1/0 (ON/OFF) value to be verified
	 * @param indSetting index of dataInd[] to look at
	 * @param gridRowIndex index of gridDataModel[] to look at
	 * @return <tt>true</tt> if indicator found
	 */
	protected boolean isIndicatorOn(int indicator, int indSetting,
		int gridRowIndex)
	{
		return false;
	}

	/**
	 * Checks if notFilterMode is required.
	 *
	 * @return <tt>true</tt> if notFilterMode is required
	 */
	public boolean isNotFilterMode()
	{
		return notFilterMode;
	}

	/**
	 * Checks for pgmCalled.
	 *
	 * @return <tt>true</tt> if pgmCalled
	 */
	public boolean isPgmCalled()
	{
		return pgmCalled;
	}

	/**
	 * Checks if postProcessingCycle is required.
	 *
	 * @return <tt>true</tt> if postProcessingCycle is required
	 */
	public boolean isPostProcessingCycle()
	{
		return postProcessingCycle;
	}

	/**
	 * Checks if the calling program requires post processing after the called
	 * program process ends. The method is used by the called program.
	 *
	 * @return <tt>true</tt> if post processing required by calling program
	 */
	protected boolean isPostProcessRequired()
	{
		Boolean b = false;
		Object obj = getSessionParam("QTYPFUN");

		if (obj != null)
		{
			b = (Boolean)obj;
			removeSessionParam("QTYPFUN");

			return b;
		}

		obj = JSFUtils.getRequestParam("QTYPFUN");

		if (obj != null)
		{
			b = (Boolean)obj;
		}

		return b;
	}

	/**
	 * Checks if the screen is to be in readonly mode.
	 *
	 * @return <tt>true</tt> if screen is readonly
	 */
	public boolean isReadonly()
	{
		return (DELETE_MODE.equals(mode) || DISPLAY_MODE.equals(mode) ||
				CONFIRMATION_MODE.equals(mode));
	}

	public boolean isReadonlyAddMode()
	{
		return !ADD_MODE.equals(mode);
	}

	/**
	 * Checks if the Submit button is to be rendered.
	 *
	 * @return String CSS style to be applied on the Submit button
	 */
	public boolean isSubmitBtnToRender()
	{
		return (!DISPLAY_MODE.equals(mode) && !CONFIRMATION_MODE.equals(mode));
	}

	/**
	 * Checks if the Submit button is disabled.
	 *
	 * @return <tt>true</tt> if Submit button is disabled
	 */
	public boolean isSubmitButtonDisabled()
	{
		return submitButtonDisabled;
	}

	public abstract void onPreRender();

	/**
	 * Controls the screen fields readOnly attribute on the basis of the
	 * indicator settings set by the program logic.
	 *
	 * @param screenField screen field name
	 * @param isGridField whether a grid field or a flat field
	 * @param isGridFlatField whether a grid's flat field or not
	 * @return readOnly attribute value
	 */
	public String pgmLogicCtrlReadOnly(String screenField, boolean isGridField,
		boolean isGridFlatField)
	{
		int currRowIndex = -1;

		if (ADD_MODE.equals(getMode()))
		{
			return "";
		}

		if (isGridField)
		{
			UIViewRoot viewRoot =
				FacesContext.getCurrentInstance().getViewRoot();
			UIComponent uiComponent =
				viewRoot.findComponent("updtGrid:grid:" +
					uncapitalize(screenField));

			if (uiComponent != null)
			{
				UIComponent parent2 = uiComponent.getParent();
				UIComponent parent = parent2.getParent();

				if (parent instanceof HtmlDataTable)
				{
					HtmlDataTable table = (HtmlDataTable)parent;
					GridDataObject rowData = (GridDataObject)table.getRowData();

					if (rowData != null)
					{
						currRowIndex = table.getRowIndex();

						if (rowData.isPositionTo())
						{
							return isGridFlatField ? "false" : "true";
						}
					}
				}
			}
			else
			{
				uiComponent = viewRoot.findComponent("updtGrid:" +
						uncapitalize(screenField));
			}
		} // if (isGridField)

		if (isReadonly())
		{
			return "true";
		}

		String screenToUse = getScreenToUse(isGridField);
		Map<Integer, String> ref = ref(screenToUse.toLowerCase() + screenField);

		if (ref == null)
		{
			return "false";
		}

		Set<Integer> keySet = ref.keySet();

		for (int index : keySet)
		{
			String str = ref.get(index);
			String attribute = str.substring(1);

			if (!"PROTECTED".equals(attribute))
			{
				continue;
			}

			int indicatorValue = Integer.parseInt(str.substring(0, 1));
			boolean indicatorOn;

			if (isGridField && (currRowIndex != -1))
			{
				indicatorOn =
					isIndicatorOn(index, indicatorValue, currRowIndex);
			}
			else
			{
				indicatorOn = isIndicatorOn(index, indicatorValue);
			}

			if (indicatorOn)
			{
				return "true";
			}
		}

		return "false";
	}

	/**
	 * Control the screen fields style attribute by the indicator provided by
	 * the logic process.
	 *
	 * @param screenField screen field
	 * @param isGridField whether a grid field or a flat field
	 * @param isGridFltFld whether a grid filter field or not
	 * @return style attribute value
	 */
	public String pgmLogicCtrlStyle(String screenField, boolean isGridField,
		boolean isGridFltFld)
	{
		int currRowIndex = -1;

		if (ADD_MODE.equals(getMode()))
		{
			return "";
		}

		if (isGridField)
		{
			UIViewRoot viewRoot =
				FacesContext.getCurrentInstance().getViewRoot();
			UIComponent uiComponent =
				viewRoot.findComponent("updtGrid:grid:" +
					uncapitalize(screenField));

			if (uiComponent != null)
			{
				UIComponent parent2 = uiComponent.getParent();
				UIComponent parent = parent2.getParent();

				if (parent instanceof HtmlDataTable)
				{
					HtmlDataTable table = (HtmlDataTable)parent;
					GridDataObject rowData = (GridDataObject)table.getRowData();

					if (rowData != null)
					{
						currRowIndex = table.getRowIndex();

						if (rowData.isPositionTo())
						{
							return isGridFltFld ? "Editable" : "ReadOnly";
						}
					}
				}
			}
			else
			{
				uiComponent = viewRoot.findComponent("updtGrid:" +
						uncapitalize(screenField));
			}
		} // if (isGridField)

		String screenToUse = getScreenToUse(isGridField);
		Map<Integer, String> ref = ref(screenToUse.toLowerCase() + screenField);

		if (ref == null)
		{
			return "";
		}

		String styleClass = "";
		Set<Integer> keySet = ref.keySet();

		for (int index : keySet)
		{
			String str = ref.get(index);
			String attribute = str.substring(1).toUpperCase();
			int indicatorValue = Integer.parseInt(str.substring(0, 1));

			boolean indicatorOn;

			if (isGridField && (currRowIndex != -1))
			{
				indicatorOn = isIndicatorOn(index, indicatorValue,
					currRowIndex);
			}
			else
			{
				indicatorOn = isIndicatorOn(index, indicatorValue);
			}

			if (isGridField && (currRowIndex != -1))
			{
				if ("PROTECTED".equals(attribute))
				{
					styleClass = indicatorOn ? "ReadOnly" : "Editable";
				}
			}
			else
			{
				if (indicatorOn)
				{
					if ("HIDDEN".equals(attribute))
					{
						return "hidden";
					}
					else if ("PROTECTED".equals(attribute))
					{
						styleClass = "ReadOnly";
					}
				}
			}
		}

		return styleClass;
	}

	/**
	 * Throws <code>BusinessException</code> whenever an error message is found.
	 * This is handled by the respective calling bean to display the error
	 * message on the screen.
	 * <p>Collects the error messages and their respective error fields from
	 * <code>MessageObject</code> array and generates the
	 * <code>BusinessException</code>.
	 *
	 * @param messageObjects array of error messages and fields
	 * @param pStateVar to get long name
	 * @throws BusinessException
	 */
	protected void processErrorMessage(MessageObject messageObjects[],
		Object pStateVar) throws BusinessException
	{
		if (messageObjects == null)
		{
			return;
		}

		if (fldShortToLongNmMap.size() == 0)
		{
			getColumnAnnoFldMap(pStateVar, fldShortToLongNmMap);
		}

		String errFlds[] = new String[messageObjects.length];
		String errMsgs = "";
		int num = 0;

		for (MessageObject msgObj : messageObjects)
		{
			// 1 for sendErrorMessage(), 0 for sendOtherMessage()
			if ((
					(msgObj.getMessageStatus() == CALLING_PGM_ERR_STATUS) ||
					(msgObj.getMessageStatus() == OTHER_ERR_MSG_STATUS)
				) && (trim(msgObj.getMessageText()).length() != 0))
			{
				String msgFld =
					msgObj.getMessageField().toString().toUpperCase();
				Field fld = (Field)fldShortToLongNmMap.get(msgFld);

				if (fld != null)
				{
					msgObj.setMessageField(fld.getName());
				}

				errFlds[num] = msgObj.getMessageField().toString();
				errMsgs += (msgObj.getMessageText().toString().trim() + " | ");
				num++;
			}
		}

		if (num > 0)
		{
			nextFunction = previousFunction;
			errMsgs = replaceEndString(errMsgs, " | ", " ");
			throw new BusinessException(errFlds, errMsgs);
		}
	}

	/**
	 * Redirects to the specific screen. This transfer of control task is
	 * delegated to the browser by the container.
	 *
	 * @param screen screen name
	 */
	protected void redirectToScreen(String screen)
	{
		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			setServletContext(facesCtx);

			String nextActionHref = req.getContextPath() + "/#/" + screen;
			res.sendRedirect(nextActionHref);
			facesCtx.responseComplete();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Redirects to the specific screen. This transfer of control task is
	 * delegated to the browser by the container.
	 *
	 * @param screen screen name
	 * @param paramsList parameter list for the screen
	 * @param rtnValList return parameter list for the screen
	 */
	protected void redirectToScreen(String screen, List<Object> paramsList,
		List<Object> rtnValList)
	{
		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			setServletContext(facesCtx);

			String nextActionHref = req.getContextPath() + "/#/" + screen;
			nextActionHref += "?SCREEN_MODE=" + mode;

			int numParams = 0;

			if ((paramsList != null) && (paramsList.size() != 0))
			{
				numParams = paramsList.size();

				for (int i = 0; i < numParams; i++)
				{
					nextActionHref += (
						"&parm" + i + "=" +
						encode(rtrim(paramsList.get(i))).replace("+", "%20")
					);
				}
			}

			if ((rtnValList != null) && (rtnValList.size() != 0))
			{
				int numRetParams = rtnValList.size();

				for (int i = 0; i < numRetParams; i++)
				{
					nextActionHref += (
						"&rtn" + i + "=" +
						encode(rtrim(rtnValList.get(i))).replace("+", "%20")
					);
				}
			}

			res.sendRedirect(nextActionHref);
			facesCtx.responseComplete();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Collects the specific field indicator settings from
	 * <code>ProgramControlData</code> class.
	 *
	 * @param fld field name to lookup
	 * @return <tt>Map</tt> of indicator settings
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, String> ref(String fld)
	{
		try
		{
			if (srcFlds == null)
			{
				srcFlds = ReflectionUtils.getAllFields(controlData, "");
			}

			for (Field sfld : srcFlds)
			{
				sfld.setAccessible(true);

				if (sfld.getName().equalsIgnoreCase(fld))
				{
					Object obj = sfld.get(controlData);

					return (Map<Integer, String>)obj;
				}
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return null;
	}

	/**
	 * Removes the last screen from the call stack.
	 *
	 * @return screen name to go to
	 */
	protected String removeFromCallStack()
	{
		String screen;
		LinkedList<String> callStack = getSessionParam("callStack");

		if ((callStack != null) && !callStack.isEmpty())
		{
			callStack.remove(callStack.size() - 1);
			screen = (!callStack.isEmpty())
				? callStack.get(callStack.size() - 1) : "application";
		}
		else
		{
			screen = "application";
		}

		return screen;
	}

	@SuppressWarnings("unchecked")
	protected void removeSelection()
	{
		List<T> items = (List<T>)dataModel.getWrappedData();

		for (T item : items)
		{
			if (item.isChecked())
			{
				item.setChecked(false);
			}
		}
	}

	/**
	 * Removes the error message style, if applied, to the screen field(s).
	 * This method is called before showing error messages on the screen.
	 *
	 * @param formName name of the form
	 */
	public void removeStyleClass(String formName)
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		UIComponent form = facesCtx.getViewRoot().findComponent(formName);
		String messagedFieldStyle = THEME + FONT_ERROR;
		List<UIComponent> list = form.getChildren();
		int childCount = form.getChildCount();

		for (int indx = 0; indx < childCount; indx++)
		{
			UIComponent component = list.get(indx);
			String styleClass =
				(String)component.getAttributes().get(STYLE_CLASS);
			String stylesFound[] = split(styleClass);

			if ((stylesFound != null) &&
					(
						Utils.lookupStrInStrArr(messagedFieldStyle,
							stylesFound, 0) > -1
					))
			{
				styleClass = trim(styleClass.substring(0,
							styleClass.indexOf(messagedFieldStyle)), "");
				component.getAttributes().put(STYLE_CLASS, styleClass);
			}
		}
	}

	/**
	 * Checks if the grid column is to be rendered at runtime.
	 *
	 * @param columnName column name
	 * @return <tt>true</tt> if column is to be rendered
	 */
	public String renderColumn(String columnName)
	{
		String gridScreen = getScreenToUse(true);
		Map<Integer, String> ref = ref(gridScreen.toLowerCase() + columnName);

		if (ref == null)
		{
			return "true";
		}

		Set<Integer> keySet = ref.keySet();

		for (int index : keySet)
		{
			String str = ref.get(index);
			String attribute = str.substring(1).toUpperCase();

			if (!"HIDDEN".equals(attribute))
			{
				continue;
			}

			int indicatorValue = Integer.parseInt(str.substring(0, 1));

			if (isIndicatorOn(index, indicatorValue))
			{
				return "false";
			}
		}

		return "true";
	}

	/**
	 * Resets the message object array.
	 */
	protected void resetMessageObject()
	{
		for (int i = 0; i < messages.length; i++)
		{
			messages[i] = new MessageObject();
		}
	}

	/**
	 * Resets the bean variables.
	 */
	protected void resetVars()
	{
		confirmScn = null;
		closeWindow = null;
		setCancelPress(false);
		action = "";
		nextFunction = "";
		nextProgram = "";
	}

	/**
	 * Sets the action.
	 *
	 * @param action
	 */
	public void setAction(String action)
	{
		this.action = action;
	}

	/**
	 * Sets the screen mode as Add.
	 *
	 * @param add
	 */
	public void setAdd(boolean add)
	{
		this.add = add;
	}

	/**
	 * Sets a boolean flag as session variable to notify the calling program
	 * that the called program has opted for cancel.
	 *
	 * @param cancelPress as true/false
	 */
	protected void setCancelPress(boolean cancelPress)
	{
		setSessionParam("cancelPress", cancelPress);
	}

	/**
	 * Sets the name of the Window to close.
	 *
	 * @param closeWindow name of the window
	 */
	public void setCloseWindow(String closeWindow)
	{
		this.closeWindow = closeWindow;
	}

	/**
	 * Sets the popup screen name.
	 *
	 * @param confirmScreen popup screen name.
	 */
	public void setConfirmScn(String confirmScn)
	{
		this.confirmScn = trim(confirmScn, null);
	}

	/**
	 * Sets the date format.
	 *
	 * @param dateFormat date format
	 */
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	/**
	 * Sets a flag to indicate a method execute only once.
	 *
	 * @param doItOnce true if a method execute only once
	 */
	public void setDoItOnce(boolean doItOnce)
	{
		this.doItOnce = doItOnce;
	}

	/**
	 * Sets a flag to indicate if error found.
	 *
	 * @param errorFound true if error found for else false
	 */
	protected void setErrorFound(boolean errorFound)
	{
		this.errorFound = errorFound;
	}

	/**
	 * Sets a flag to indicate if filtering is opted for.
	 *
	 * @param filterApplied true if filtering opted for else false
	 */
	public void setFilterApplied(boolean filterApplied)
	{
		this.filterApplied = filterApplied;
	}

	/**
	 * Sets the function key indicator value true at the index provided.
	 *
	 * @param funKeyIndArray function key indicator array
	 * @param index int value of the index to be set
	 */
	protected void setFunKeyIndicator(boolean funKeyIndArray[], int index)
	{
		DSUtils.clearObject(funKeyIndArray);
		funKeyIndArray[index] = true;
	}

	/**
	 * This method should not be called directly by the user. It is called by
	 * the JavaServer Faces container.
	 *
	 * @param jobDetails containing server job's name, user, and number
	 * @since (2005-01-06.18:34:32)
	 */
	public void setJobDetails(String jobDetails)
	{
		this.jobDetails = jobDetails;
	}

	/**
	 * Sets the mode (ADD/CHG) of the current screen.
	 *
	 * @param mode to set
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	/**
	 * Sets the next function.
	 *
	 * @param nextFunction
	 */
	public void setNextFunction(String nextFunction)
	{
		this.nextFunction = nextFunction;
	}

	/**
	 * Sets the next program.
	 *
	 * @param nextProgram
	 */
	public void setNextProgram(String nextProgram)
	{
		this.nextProgram = nextProgram;
	}

	/**
	 * Sets a flag to indicate if notFilterMode is required.
	 *
	 * @param notFilterMode true if notFilterMode is required
	 */
	public void setNotFilterMode(boolean notFilterMode)
	{
		this.notFilterMode = notFilterMode;
	}

	/**
	 * Sets the parent of this screen.
	 *
	 * @param parentScn parent screen
	 */
	public void setParentScn(String parentScn)
	{
		this.parentScn = trim(parentScn, null);
	}

	/**
	 * Sets the pgmCalled.
	 *
	 * @param pgmCalled
	 */
	public void setPgmCalled(boolean pgmCalled)
	{
		this.pgmCalled = pgmCalled;
	}

	/**
	 * Sets the postProcessingCycle.
	 *
	 * @param postProcessingCycle
	 */
	public void setPostProcessingCycle(boolean postProcessingCycle)
	{
		this.postProcessingCycle = postProcessingCycle;
	}

	/**
	 * Sets the previous function.
	 *
	 * @param previousFunction
	 */
	public void setPreviousFunction(String previousFunction)
	{
		this.previousFunction = previousFunction;
	}

	/**
	 * Sets the previous program.
	 *
	 * @param previousProgram
	 */
	public void setPreviousProgram(String previousProgram)
	{
		this.previousProgram = previousProgram;
	}

	/**
	 * Sets the map of pStateVar.
	 *
	 * @param pStateVarMap
	 */
	public void setpStateVarMap(Map<String, Object> pStateVarMap)
	{
		this.pStateVarMap = pStateVarMap;
	}

	/**
	 * Sets the readonly attribute.
	 *
	 * @param readonly
	 */
	public void setReadonly(boolean readonly)
	{
		this.readonly = readonly;
	}

	/**
	 * Sets the screen name.
	 *
	 * @param screen
	 */
	public void setScreen(String screen)
	{
		this.screen = screen;
	}

	/**
	 * Sets the servlet context.
	 *
	 * @param facesCtx javax.faces.context.FacesContext
	 * @see #setServletContext(ServletContext)
	 * @since (2005-01-19.12:42:34)
	 */
	protected void setServletContext(FacesContext facesCtx)
	{
		if (facesCtx == null)
		{
			return;
		}

		// Get the state information independent of JavaServer Faces request
		ExternalContext externalCtx = facesCtx.getExternalContext();

		if (externalCtx == null)
		{
			return;
		}

		ServletContext servletCtx = (ServletContext)externalCtx.getContext();

		if (servletCtx == null)
		{
			return;
		}

		this.req = (HttpServletRequest)externalCtx.getRequest();
		this.res = (HttpServletResponse)externalCtx.getResponse();
	}

	/**
	 * This method should not be called directly by the user. It is called by
	 * the JavaServer Faces container.
	 *
	 * @param jsessionid current user's session id which is unique for each user
	 */
	public void setSessionId(String jsessionid)
	{
		this.jsessionid = jsessionid;
	}

	protected void setStr(StringBuilder builder, String value)
	{
		builder.setLength(0);
		builder.append(value);
	}

	/**
	 * Sets a flag to indicate if the submit button is disabled.
	 *
	 * @param submitBtnDisabled <tt>true</tt> if submit button is disabled else
	 *        false
	 */
	public void setSubmitButtonDisabled(boolean submitButtonDisabled)
	{
		this.submitButtonDisabled = submitButtonDisabled;
	}

	/**
	 * Sets value list.
	 *
	 * @param valueList
	 */
	public void setValueList(List<Object> valueList)
	{
		this.valueList = valueList;
	}

	public String suppressCheckbox(String indicators)
	{
		String gridScreen = getScreenToUse(true);
		Map<Integer, String> ref = ref(gridScreen.toLowerCase() + "Checked");

		if (ref == null)
		{
			return "";
		}

		Set<Integer> keySet = ref.keySet();

		for (int index : keySet)
		{
			char lastChar = indicators.charAt(index - 1);

			String str = ref.get(index);
			int indicatorValue = Integer.parseInt(str.substring(0, 1));
			String attribute = str.substring(1);

			if ((Character.digit(lastChar, 10) == indicatorValue) &&
					(
						"PROTECTED".equals(attribute) ||
						"HIDDEN".equals(attribute)
					))
			{
				return "display:none";
			}
		}

		return "";
	}
}
