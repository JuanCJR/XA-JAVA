"use strict";

var app = angular.module('app');

/**
 * Acts as application level controller and base controller for program
 * controllers.
 *
 * @author Robin Rizvi
 * @since (2014-05-12.19:26:23)
 */
app.controller('BaseController',
    function($location, $log, $modal, $rootScope, $route, $scope, Auth,
    	BusinessException, CallStack, Constants, Params, ResourceManager,
    	StringUtils, Utils, UiUtils) {

	/**
	 * Holds the collection of skins/themes to display as options for the user.
	 */
	$scope.skins = UiUtils.getSkins();

	/**
	 * Sets the response received from the WebService to controller fields.
	 *
	 * @param {Object} response
	 */
	$scope.assignPgmResponse = function(response) {
		if ((typeof response.action !== 'undefined') &&
        		(response.action != null)) {
			this.action = response.action;
		}

		if ((typeof response.nextFunction !== 'undefined') &&
        		(response.nextFunction != null)) {
			this.nextFunction = this.getPrefLongName(response.nextFunction);
		}

		if ((typeof response.nextProgram !== 'undefined') &&
        		(response.nextProgram != null)) {
			this.nextProgram = this.getPrefLongName(response.nextProgram);
			CallStack.setNextProgram(this.programName, this.nextProgram);
		}

		if ((typeof response.messages !== 'undefined') &&
        		(response.messages != null)) {
			this.messages = response.messages;
		}

		if ((typeof response.stateVariable !== 'undefined') &&
        		(response.stateVariable != null)) {
			//this.stateVariable = response.stateVariable;
			$.extend(true, this.stateVariable, response.stateVariable);
		}

		if ((typeof (response.gridVariable) !== 'undefined') &&
        		(response.gridVariable != null)) {
			this.gridVariable = response.gridVariable.slice(1);
		}
	};

	/**
	 * Determines whether drop down button is rendered.
	 *
	 * @returns {Boolean} <tt>true</tt> if button is to be rendered
	 */
	$scope.buttonToRender = function() {
		return !this.readonly();
	};

	/**
	 * Manages the Cancel functionality and returns to the previous screen.
	 *
	 * @param {Object} pStateVar current status of the program
	 * @param {String} pgmNm current program name
	 * @param {Boolean} popup <tt>true</tt> if screen is a popup screen
	 * @returns {String} screen name to go to
	 */
	$scope.cancel = function(pStateVar, pgmNm, popup) {
		var screen;
		var callStack = CallStack.callStack;

		if ((callStack != null) && (callStack.length != 0)) {
			if (popup) {
				screen = callStack[callStack.length - 1];
			}
			else {
				callStack.pop();
				screen = (callStack.length != 0)
					? callStack[callStack.length - 1] : "application";
			}
		}
		else {
			screen = "application";
		}

		// If screen to go to is related with same program then its StateVar 
		// value needs to be set according to the screen to go
		if (screen.toUpperCase().startsWith(pgmNm.toUpperCase()) &&
        		(pStateVar != null)) {
			CallStack.restoreState(this);
		}
		else {
			CallStack.deleteState(this);

			// If screen to go to is application then no need to set cancelPress
			// to true
			if (!this.isBatchPgm(pgmNm)) {
				this.setCancelPress(screen != "application");
			}
		}

		this.errorFound = false;
		Params[Constants.PARENT_SCREEN] = null;

		return screen;
	};

	/**
	 * Checks if Confirmation button is required on the screen at runtime.
	 *
	 * @returns {Boolean} <tt>true</tt> if confirmation button is required
	 */
	$scope.confirmationButtonRequired = function() {
		return (this.mode === Constants.CONFIRMATION_MODE);
	};

	/**
	 * Confirms before proceeding to delete.
	 *
	 * @param {Array} Array of selected rows
	 * @returns {Boolean} <tt>true</tt> if record is to be deleted
	 */
 	$scope.confirmDelete = function(selectedRowList) {
 		//clearErrorMsg();

		if (!this.isRecordSelected(selectedRowList)) {
			return false;
		}

		if (this.pendingRows()) {
			return true;
		}

	    return confirm("Confirm to delete ?");
	};

	/**
	 * Changes the application UI theme when change theme action is taken.
	 *
	 * @param {String} theme theme name
	 */
	$scope.changeTheme = function(theme) {
		var skin = "skin-" + theme;
		$('body').removeClass().addClass(skin);
	};

	/**
	 * Collects the specific field indicator settings from 
	 * {@link ProgramControlData} class instance received from WebService.
	 *
	 * @param {String} fld field name to lookup
	 * @returns {Object} indicator settings
	 */
	$scope.fieldRef = function(fld) {
		if (this.controlData == null) {
			return null;
		}

		return this.controlData[fld];
	};

	/**
	 * Gets the date format.
	 *
	 * @returns {String} date format
	 */
	$scope.getDateFormat = function() {
		var dateFormat;

		try {
			dateFormat = ResourceManager.get("dateFormat", "configuration");
			dateFormat = dateFormat.replace(/D/g, 'd');
			dateFormat = dateFormat.replace(/m/g, 'M');
			dateFormat = dateFormat.replace(/Y/g, 'y');
		}
		catch (e) {
			dateFormat = "dd/MM/yyyy";
		}

		return dateFormat;
	};

	/**
	 * Collects the passed parameters from the calling to the called program.
	 *
	 * @returns {Boolean} <tt>true</tt> if parameters have been passed
	 */
	$scope.getPassedParams = function() {
		if ((Params[Constants.PARAM_VALUES] != null) &&
				(Params[Constants.PARAM_VALUES] != undefined)) {
			this.valueList = Params[Constants.PARAM_VALUES];
			Params[Constants.PARAM_VALUES] = null;

			return this.valueList.length > 0;
		}

		var params = $location.search();

		if (params == null) {
			return false;
		}

		this.valueList = [];

		for (var key in params) {
			if (!key.startsWith("parm")) {
				continue;
			}

			this.valueList.push(params[key]);
			$location.search(key, null);
		}

		return this.valueList.length > 0;
	};

	/**
	 * Gets the long name of the screen.
	 *
	 * @param {String} shortName screen short name
	 * @returns {String} equivalent long name
	 */
	$scope.getPrefLongName = function(shortName) {
		if ((shortName = shortName.trim()) != '') {
			if (ResourceManager.exists(shortName.trim())) {
				return ResourceManager.get(shortName.trim());
			}
			else {
				return shortName;
			}
		}
		else {
			return shortName;
		}
	};

	/**
	 * Collects the return value list set by the called program, to be used in
	 * calling program's post process logic.
	 *
	 * @returns {Array} Return value list
	 */
	$scope.getReturnValList = function() {
		var rtnValList = [];

		if (Params["rtnValList"] != null) {
			rtnValList = Params["rtnValList"];
			Params["rtnValList"] = null;
		}
		else if ($location.search()['rtnValList'] != null) {
			rtnValList = $location.search()['rtnValList'];
			$location.search('rtnValList', null);
		}

		return rtnValList;
	};

	/**
	 * Gets the rows to display on the grid.
	 *
	 * @returns {Number} number of rows
	 */
	$scope.getRowsToDisplay = function() {
		var rowsToDisplay;

		try {
			rowsToDisplay = ResourceManager.get("numRows", "configuration");
		}
		catch (e) {
			rowsToDisplay = 0;
		}

		return rowsToDisplay;
	};

	/**
	 * Gets the screen name to navigate to.
	 *
	 * @param {Object} pStateVar current status of the program
	 * @param {String} programName current program name
	 * @param {Boolean} isPopup <tt>true</tt> if screen is a popup screen
	 * @returns {String} screen name to navigate to
	 */
	$scope.getScreenToNavigateTo = function(pStateVar, programName, isPopup) {
		var screenNm = this.cancel(pStateVar, programName, isPopup);

		while (screenNm.toUpperCase().startsWith(programName.toUpperCase())) {
			screenNm = this.cancel(pStateVar, programName, isPopup);
		}

		return screenNm;
	};

	/**
	 * Gets the screen to use for ProgramControlData.
	 *
	 * @param {Boolean} isGridComp whether a grid button/field/column/checkbox
	 * @returns {String} screen
	 */
	$scope.getScreenToUse = function(isGridComp) {
		var lastScreen = CallStack.getLastScreen();

		if (lastScreen == null) {
			return "";
		}

		var screenToUse;
		var orgScreen = ResourceManager.getKey(lastScreen);

		if (isGridComp) {
			screenToUse = orgScreen.substr(0, orgScreen.length - 1) + "g";
		}
		else {
			screenToUse = orgScreen;
		}

		return screenToUse;
	};

	/**
	 * Collects the information of the selected rows.
	 *
	 * @returns {Array} Array of selected rows
	 */
	$scope.getSelectedRows = function() {
		var selected = [];

		for (var i = 0; i < this.gridVariable.length; i++) {
			var row = this.gridVariable[i];
			row.checked = (row.checked === "true") || (row.checked === true);

			if (row.checked) {
				selected.push(row);
			}
		}

		return selected;
	};

	/**
	 * Checks whether the grid has no data. If so, the action buttons are
	 * disabled.
	 *
	 * @returns {Boolean} <tt>true</tt> if grid has no rows
	 */
	$scope.gridEmpty = function() {
		return ((this.gridVariable == null) || (this.gridVariable.length == 0));
	};

	/**
	 * Handles the HTTP errors resulting from asynchronous REST call to the
	 * web service.
	 *
	 * @param {Object} error
	 */
	$scope.handleError = function(error) {
		// HTTP 401
		if (error.status == 401) {
			$log.warn("Authentication failed.");
			Auth.reset();
			this.redirectToScreen("application");
		}
	};

	/**
	 * Checks the availability of next page of the grid.
	 *
	 * @returns <tt>true</tt> if next page is available.
	 */
	$scope.hasNextPage = function() {
		return this.pageHandler.hasNext();
	};

	/**
	 * Checks the availability of previous page of the grid.
	 *
	 * @returns <tt>true</tt> if previous page available.
	 */
	$scope.hasPreviousPage = function() {
		return this.pageHandler.hasPrevious();
	};

	/**
	 * Initializes common variables.
	 */
	$scope.initialize = function() {
		UiUtils.setupProgramUI();
		//Auth.checkStatus();
		this.consts = ResourceManager.getResource('constants');
		this.dateFormat = this.getDateFormat();
		this.currentOperation = null;
		this.action = "";
		this.nextFunction = "";
		this.nextProgram = "";
		this.stateVariable = {};
		this.qTypeFun = false;
		this.postProcessingCycle = false;
		this.pgmCalled = false;
		this.submitButtonDisabled = false;

		this.fldsFiltered = {};
		this.filterFlds = [];
		this.endOfFile = false;
		this.cachedPage = false;

		this.mode = Constants.ADD_MODE;

		if (Params[Constants.SCREEN_MODE] != null) {
			this.mode = Params[Constants.SCREEN_MODE];
			Params[Constants.SCREEN_MODE] = null;
		}

		this.add = false;

		if (Params["add"] != null) {
			this.add = Params["add"];
			Params["add"] = null;
		}

		this.controlData = Utils.programControlData;

		var params = $location.search();

		if (params && (params[Constants.SCREEN_MODE] != null)) {
			this.mode = params[Constants.SCREEN_MODE];
			$location.search(Constants.SCREEN_MODE, null);
		}

		if (Params["errorFound"] != null) {
			this.errorFound = Params["errorFound"];
			Params["errorFound"] = null;
		}

		if (sessionStorage) {
			var loginTime = sessionStorage.getItem("loginTime");

			if (loginTime != null) {
				$rootScope.loginTime = loginTime;
				$rootScope.userDescription = sessionStorage.getItem("userDesc");
				$rootScope.userAuthenticated =
					sessionStorage.getItem("userAuth");

				sessionStorage.removeItem("loginTime");
				sessionStorage.removeItem("userDesc");
				sessionStorage.removeItem("userAuth");
			}

			var cs = sessionStorage.getItem("CallStack");

			if (cs != null) {
				Utils.assignObject(CallStack, JSON.parse(cs));
				sessionStorage.removeItem("CallStack");
			}
		}
	};

	/**
	 * Checks if the program is a Batch program.
	 *
	 * @param pgm program name
	 * @returns {Boolean} <tt>true</tt> if this is a Batch program
	 */
	$scope.isBatchPgm = function(pgm) {
		var orgPgm = ResourceManager.getKey(pgm);

		var isIntRtn =
			(orgPgm.length >= 8) && orgPgm.startsWith("A") &&
			!isNaN(orgPgm.substring(1, 8));

		return (orgPgm.endsWith("XFR") || orgPgm.endsWith("UPC") ||
				orgPgm.endsWith("UPR") || isIntRtn);
	};

	/**
	 * Checks if Cancel button is pressed on the called program screen.
	 *
	 * @returns {Boolean} <tt>true</tt> if cancel is opted for
	 */
	$scope.isCancelPress = function() {
		var cancelPress = false;

		if (Params["cancelPress"] != null) {
			cancelPress = Params["cancelPress"];
		}

		return cancelPress;
	};

	/**
	 * Checks if the current screen is a Confirmation screen.
	 *
	 * @returns {Boolean} <tt>true</tt> if current screen is a confirmation
	 *        screen
	 */
	$scope.isConfirmationScreen = function() {
		return (this.stateVariable.zzmode === "CONFIRM");
	};

	/**
	 * Checks for the indicator setting at a specific index for the current
	 * stateVariable {@link stateVariable.qin} or 
	 * gridVariable {@link gridVariable[gridRowIndex].indicators}.
	 *
	 * @param {Number} index index to look at
	 * @param {String} indicatorValue 1/0 (ON/OFF) value to be verified
	 * @param {Number} gridRowIndex index of gridVariable[] to look at
	 * @returns {Boolean} <tt>true</tt> if indicator found
	 */
	$scope.isIndicatorOn = function(index, indicatorValue, gridRowIndex) {
		if (gridRowIndex != null) {
			if ((index >= 0) && (this.gridVariable.length > 0)) {
				if (gridRowIndex >= 0) {
					var value =
						this.gridVariable[gridRowIndex].indicators[index - 1]
							.toString();

					return (value == indicatorValue);
				}
			}
		}
		else {
			if ((index > 0) && (this.stateVariable != null) &&
					(this.stateVariable.qin != null)) {
				var value = this.stateVariable.qin[index - 1];

				return (value == indicatorValue);
			}
		}

		return false;
	};

	/**
	 * Checks if the calling program requires post processing after the called
	 * program process ends. The method is used by the called program.
	 *
	 * @return {Boolean} <tt>true</tt> if post processing required by calling
	 *        program
	 */
	$scope.isPostProcessRequired = function() {
		var b = false;

		if (Params["QTYPFUN"] != null) {
			b = Params["QTYPFUN"];
			Params["QTYPFUN"] = null;
		}

		return b;
	};

	/**
	 * Simply check for grid record selection.
	 *
	 * @param {Array} Array of selected rows
	 * @returns {Boolean} <tt>true</tt> if at least one record is selected
	 */
	$scope.isRecordSelected = function(selectedRowList) {
		if ((selectedRowList.length == 0) && !this.pendingRows()) {
			alert('Please select atleast one record!');

			return false;
		}

		//clearErrorMsg();

		return true;
	};

	/**
	 * Performs log out operation when log out action is taken from the UI.
	 */
	$scope.logout = function() {
		$rootScope.loginTime = null;
		$rootScope.userDescription = null;
		$rootScope.userAuthenticated = false;
		Auth.reset();
		CallStack.reset();

		if ($location.path() === "/application") {
			$route.reload();
		}
		else {
			$location.path("/application");
		}
	};

	/**
	 * Fetches the next page data for the grid.
	 *
	 * @param {Integer} [rollup] Optional Roll-Up key
	 */
	$scope.nextPage = function(rollup) {
		this.errorFound = false;

		if (this.pageHandler.fetchNext()) {// if already loaded
			this.cachedPage = true;
			this.updateGridViewModel();
			this.removeSelection();
		}
		else if (!this.endOfFile) {
			if (rollup == null) {
				rollup = 27;
			}

			this.stateVariable.qin =
				StringUtils.setIndicator(this.stateVariable.qin, 28, '1');

			if (rollup != 0) {
				this.stateVariable.qin =
					StringUtils.setIndicator(this.stateVariable.qin, rollup,
						'1');
			}

			this.stateVariable.sflrrn =
				parseInt(this.stateVariable.sflrrn) + this.numberOfRows;

			// Process the pagination
			var dataPromise = this.process("", true);

			dataPromise.then((function() {
				this.stateVariable.qin =
					StringUtils.setIndicator(this.stateVariable.qin, 28, '0');

				if (rollup != 0) {
					this.stateVariable.qin =
						StringUtils.setIndicator(this.stateVariable.qin, rollup,
							'0');
				}
			}).bind(this), this.handleError.bind(this));
		}
	};

 	/**
	 * Opens the specified Pop-up window.
	 *
	 * @param {String} screen
	 * @returns {Object} a modal instance, an object with the following 
	 *        properties: close(result), dismiss(reason), result, opened
	 */
	$scope.openWindow = function(screen) {
		var view = this.getPrefLongName(screen);
		var screenUrl = 'Views/' + view + '.html';
		var contr = Utils.getProgramName(view) + 'Controller';
		var modalScope = $scope.$new();

		var modalInstance = $modal.open({
			templateUrl: screenUrl,
			scope: modalScope,
			controller: contr,
			resolve: {
				// Lazily load controller
				controller: function($q) {
					var deferred = $q.defer();
					var contrFile = "Controllers/" + contr + ".js";

					head.load(contrFile, function() {
						// Resolve the promise when controller has been lazily
						// loaded
						deferred.resolve();
					});

					return deferred.promise;
				}
			}
		});

		// Passing caller's scope to the modal controller so that the same
		// controller can be used for modal and non-modal view
		modalScope.modalInstance = modalInstance;

		return modalInstance;
	};

	/**
	 * Checks if there are pending rows on the grid.
	 *
	 * @returns {Boolean} <tt>true</tt> if there are pending rows
	 */
 	$scope.pendingRows = function() {
		var pendingList =
			Params[this.programName + "Controller_pendingRowList"];

		return (pendingList != null);
 	};

	/**
	 * Controls the screen fields readOnly attribute on the basis of the 
	 * indicator settings set by the program logic.
	 *
	 * @param {String} screenField screen field name
	 * @param {Boolean} isGridField whether a grid field or a flat field
	 * @param {Boolean} isGridFlatField whether a grid's flat field or not
	 * @param {Number} currRowIndex grid's row index
	 * @returns {Boolean} readOnly attribute value
	 */
	$scope.pgmLogicCtrlReadOnly = function(screenField, isGridField,
		isGridFlatField, currRowIndex) {
		if (this.readonly()) {
			return true;
		}

		var screenToUse = this.getScreenToUse(isGridField);
		var fldref = this.fieldRef(screenToUse.toLowerCase() + screenField);

		if (fldref == null) {
			return false;
		}

		var key = Object.keys(fldref)[0];
		var str = fldref[key];
		var attribute = str.substring(1);

		if ("PROTECTED" !== attribute) {
			return false;
		}

		var indicatorValue = str.substring(0, 1);
		var indicatorOn;

		if (isGridField && (currRowIndex != -1)) {
			indicatorOn = this.isIndicatorOn(key, indicatorValue, currRowIndex);
		}
		else {
			indicatorOn = this.isIndicatorOn(key, indicatorValue);
		}

		return indicatorOn;
	};

	/**
	 * Control the screen fields style attribute by the indicator provided by 
	 * the logic process.
	 *
	 * @param {String} screenField screen field name
	 * @param {Boolean} isGridField whether a grid field or a flat field
	 * @param {Boolean} isGridFlatField whether a grid's flat field or not
	 * @param {Number} currRowIndex grid's row index
	 * @returns style attribute value
	 */
	$scope.pgmLogicCtrlStyle = function(screenField, isGridField,
		isGridFlatField, currRowIndex) {
		var screenToUse = this.getScreenToUse(isGridField);
		var fldref = this.fieldRef(screenToUse.toLowerCase() + screenField);

		if (fldref == null) {
			return "";
		}

		var styleClass = "";
		var key = Object.keys(fldref)[0];
		var str = fldref[key];
		var attribute = str.substring(1);

		var indicatorValue = str.substring(0, 1);
		var indicatorOn;

		if (isGridField && (currRowIndex != -1)) {
			indicatorOn = this.isIndicatorOn(key, indicatorValue, currRowIndex);
		}
		else {
			indicatorOn = this.isIndicatorOn(key, indicatorValue);
		}

		if (indicatorOn) {
			if ("HIDDEN" == attribute) {
				styleClass = "hidden";
			}
			else if ("PROTECTED" == attribute) {
				styleClass = "readonly";
			}
		}

		return styleClass;
	};

    /**
	 * Fetches the previous page data for the grid.
	 */
	$scope.previousPage = function() {
		this.cachedPage = true;
		this.pageHandler.fetchPrevious();
		this.errorFound = false;
		this.updateGridViewModel();
	};

	/**
	 * Throws {@link BusinessException} whenever an error message is found.
	 * This is handled by the respective calling controller to display the
	 * error message on the screen.
	 * <p>Collects the error messages and their respective error fields
	 * from {@link messages} array and generates the {@link BusinessException}.
	 *
	 * @param {Array} messages array of error messages and fields
	 */
	$scope.processErrorMessage = function(messages) {
		if ((messages == null) || (messages.length == 0)) {
			return;
		}

		var num = 0;
		var errMsgs = [];
		var errFlds = [];

		for (var i = 0; i < messages.length; i++) {
			var msgObj = messages[i];
			// 1 for sendErrorMessage(), 0 for sendOtherMessage()
			if ((
					(
						msgObj.messageStatus == Constants.CALLING_PGM_ERR_STATUS
					) ||
					(msgObj.messageStatus == Constants.OTHER_ERR_MSG_STATUS)
				) && (msgObj.messageText.trim().length != 0)) {
				errMsgs.push(msgObj.messageText.toString().trim());
				var errFld = msgObj.messageField.toString();
				errFlds.push(StringUtils.toLowerCaseIfAllUpper(errFld.trim()));
				num++;
			}
		}

		if (num > 0) {
			this.nextFunction = this.previousFunction;
			throw new BusinessException(errFlds, errMsgs);
		}
	};

	/**
	 * Checks if the screen is to be in readonly mode.
	 *
	 * @returns {Boolean} <tt>true</tt> if screen is readonly
	 */
	$scope.readonly = function() {
		return ((this.mode === Constants.DELETE_MODE) ||
				(this.mode === Constants.DISPLAY_MODE) ||
				(this.mode === Constants.CONFIRMATION_MODE));
	};

	/**
	 * Checks if the screen is to be in readonly mode in Add mode.
	 *
	 * @returns {Boolean}
	 */
	$scope.readonlyAddMode = function() {
		return (this.mode !== Constants.ADD_MODE);
	};

	/**
	 * Performs redirection to the specified screen.
	 *
	 * @param {String} nextScreen screen to redirect
	 */
	$scope.redirectToScreen = function(nextScreen) {
		if ((nextScreen == null) || (nextScreen == "")) {
			return;
		}

		if (nextScreen == $route.current.pathParams.screen) {
			$route.reload();
		}

		if ($rootScope.requestInProgress != undefined) {
			$rootScope.requestInProgress.$resolved = false;
		}

		this.showBusinessMessages = false;
		this.businessException = null;
		$location.path("/" + nextScreen);
	};

	/**
	 * Invalidates the row selection on the grid.
	 */
	$scope.removeSelection = function() {
		var items = this.gridVariable;

		for (var i = 0; i < items.length; i++) {
			var item = items[i];

			if (item.checked) {
				item.checked = false;
			}
		}
	};

	/**
	 * Resets the scope fields.
	 */
	$scope.resetVars = function() {
		this.confirmScn = null;
		this.setCancelPress(false);
		this.action = "";
		this.nextFunction = "";
		this.nextProgram = "";
	};

	/**
	 * Sets a boolean flag as session variable to notify the calling program 
	 * that the called program has opted for cancel.
	 *
	 * @param cancelPress as true/false
	 */
	$scope.setCancelPress = function(cancelPress) {
		Params["cancelPress"] = cancelPress;
	};

	/**
	 * Sets next program.
	 *
	 * @param {String} nextProgram next program name
	 */
	$scope.setNextProgram = function(nextProgram) {
		this.nextProgram = nextProgram;
		CallStack.setNextProgram(this.programName, nextProgram);
	};

	/**
	 * Sets qTypeFun.
	 *
	 * @param {String} qTypeFun
	 */
	$scope.setQTypeFun = function(qTypeFun) {
		this.qTypeFun = qTypeFun;
		CallStack.setQTypeFun(this.programName, qTypeFun);
	};

	/**
	 * Displays global application level async progress indicator.
	 *
	 * @param {$resource} operation angular $resource class object
	 * @param {String} message message string to display
	 */
	$scope.setRequestIndicator = function(operation, message) {
		if (operation == null) {
			if ($rootScope.requestInProgress != undefined) {
				$rootScope.requestInProgress.$resolved = false;
			}

			$rootScope.hideProgressIndicator = false;
		}
		else {
			$rootScope.requestInProgress = operation;
			$rootScope.progressMessage = message;
		}
	};

	/**
	 * Sets the return value list of the called program, to be used in calling
	 * program's post process logic.
	 */
	$scope.setReturnValList = function() {
		var params = $location.search();

		if (params == null) {
			return;
		}

		var valueList = [];

		for (var key in params) {
			if (!key.startsWith("rtn")) {
				continue;
			}

			valueList.push(params[key]);
			$location.search(key, null);
		}

		if (valueList.length != 0) {
			Params["rtnValList"] = valueList;
		}
	};

	/**
	 * Gets style for the field on the screen in readonly mode.
	 */
	$scope.style = function() {
		var retValue = null;

		if (this.readonly()) {
			retValue = {'background-color':'#F1F1F1'};
		}

		return retValue;
	};

	/**
	 * Gets style for the field on the screen in readonly Add mode.
	 */
	$scope.styleAddMode = function() {
		if (this.readonlyAddMode()) {
			return {'background-color':'#F1F1F1'};
		}
		else {
			return {'background-color':'#FFFFFF'};
		}
	};

	/**
	 * Checks if the Submit button is to be rendered.
	 *
	 * @returns {Boolean} <tt>true</tt> if Submit button is to be rendered
	 */
	$scope.submitBtnToRender = function() {
		return ((this.mode !== Constants.DISPLAY_MODE) &&
				(this.mode !== Constants.CONFIRMATION_MODE));
	};

	/**
	 * Suppresses the Checkbox.
	 *
	 * @param {String} indicators
	 * @returns style attribute value
	 */
	$scope.suppressCheckbox = function(indicators) {
		var gridScreen = this.getScreenToUse(true);
		var ref = this.fieldRef(gridScreen.toLowerCase() + "Checked");

		if (ref == null) {
			return "";
		}

		var key = Object.keys(ref)[0];

		var str = ref[key];
		var indicatorValue = str.substring(0, 1);
		var attribute = str.substring(1);

		if ((indicators.charAt(key - 1) == indicatorValue) &&
				(
					(attribute == "HIDDEN") || (attribute == "PROTECTED")
				)) {
			//return "display:none";
			return {'display':'none'};
		}

		return "";
	};

	/**
	 * Prepares the view model for flat screens.
	 */
	$scope.updateFlatViewModel = function() {
		this.stateVariable = Utils.trimAll(this.stateVariable);
	};

	/**
	 * Prepares the view model for grid screens.
	 *
	 * @param {String} [sel] Optional Selection field
	 */
	$scope.updateGridViewModel = function(sel) {
		if (this.errorFound) {
			return;
		}

		var gridVariable = this.gridVariable;

		if (this.add) {
			for (var i = 0; i < gridVariable.length; i++) {
				gridVariable[i] = Utils.trimAll(gridVariable[i]);
				//this.resetAddModeGridFlds(gridVariable[i]);
				this.pageHandler.addRow(gridVariable[i]);
			}
		}
		else if (!(this.endOfFile || this.cachedPage)) {
			// Neither the end of file reached nor the cached page data
			// requested then load up the fetched data
			for (var i = 0; i < gridVariable.length; i++) {
				gridVariable[i] = Utils.trimAll(gridVariable[i]);

				/*if (this.add) {
					this.resetGridDropDownFlds(gridVariable[i]);
				}*/

				if ((gridVariable[i].sel == 'X') ||
						((sel != null) && (gridVariable[i][sel] == 'X'))) {
					this.pageHandler.addRow(gridVariable[i]);
				}
				else {
					this.pageHandler.eof = (this.endOfFile = true);

					break;
				}

				// Suppress grid row selection on the basis of indicator
				gridVariable[i].checkboxStyle =
					this.suppressCheckbox(gridVariable[i].indicators);
			}

			if ((((sel == null) && gridVariable[0].sel == 0)) ||
					((sel != null) && (gridVariable[0][sel] == 0))) {
				// Since no data was found revert the indexes
				this.pageHandler.fetchPrevious();
			}
		}

		this.gridVariable = this.pageHandler.createViewModel();
	};

	/**
	 * Processes the business exception and displays field validity indicators
	 * on the UI.
	 *
	 * @param {BusinessException} businessException
	 */
	$scope.validateFormStatus = function(businessException) {
		UiUtils.resetProgramUI();
		this.businessException = businessException;

		if (this.flatPanel == null) {
			return;
		}

		this.flatPanel.$setValidity("businesslogic", true);

		// Removes the previously indicated invalid fields that were invalid as
		// indicated by the back end business logic
		if ((this.flatPanel.$error.businesslogic != undefined) &&
				(this.flatPanel.$error.businesslogic != null)) {
			for (var i = 0; i < this.flatPanel.$error.businesslogic.length;
				i++) {
				var field = this.flatPanel.$error.businesslogic[i];
				field.$setValidity("businesslogic", true);
			}
		}

		// Adding validity status to fields of the form that are indicated as
		// invalid by the back end business logic
		if ((businessException != undefined) &&
				(businessException != null)) {
			var fields = businessException.errorField;

			if ((fields != undefined) && (fields != null)) {
				for (var i = 0; i < fields.length; i++) {
					var field = fields[i];

					if (field == "") {
						continue;
					}

					if (this.flatPanel[field] == null) {
						var fld = document.getElementById(field);

						if (fld != null) {
							field = fld.name;
						}
					}

					this.flatPanel[field].$setValidity("businesslogic", false);
					this.flatPanel.$setValidity("businesslogic", false);
				}
			}
		}
		else {
			this.showBusinessMessages = this.errorFound = false;
		}
	};
});