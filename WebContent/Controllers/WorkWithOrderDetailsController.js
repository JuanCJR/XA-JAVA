"use strict";

var app = angular.module('app');

/**
 * Controller for HTML (Non-Synon Program): WorkWithOrderDetails (WWCONDET).
 *
 * @author KAMALN
 */
app.controller('WorkWithOrderDetailsController',
	function($log, $modal, $q, $rootScope, $route, $scope,
		WorkWithOrderDetailsService, Auth, BusinessException, CallStack,
		Constants, Filter, Pager, Params, ResourceManager, StringUtils, Utils,
		UiUtils) {

	/**
	 * Performs pre-initialization operations to setup the controller's state.
	 */
	$scope.preInit = function() {
		if (!this.programName) {
			this.initialize();
			this.setReturnValList();
			this.programName = 'WorkWithOrderDetails';
			// 9 is the number of rows to display on the original application
			this.numberOfRows = parseInt((this.getRowsToDisplay() == 0) ? 9 : this.getRowsToDisplay());
			this.populateFilterFieldData();
			this.pageHandler = new Pager(this.numberOfRows);
			this.pageHandler.reset();
			this.setRequestIndicator();
			CallStack.restoreState(this);
		}
	};

	/**
	 * Collects the filter field information.
	 */
	$scope.populateFilterFieldData = function() {
		this.filterFlds.push({
			fltFld: "Xwabcd",
			fltName: "Xwabcd",
			opType: Filter.OP_GREATER_OR_EQUAL
		});
	};

	/**
	 * Entry point function invoked from (@link WorkWithOrderDetailsEntryPanel.html)
	 * Executes Web Service call to perform logic {@link initialize} operation
	 * which loads the data and gives the information about which screen to
	 * navigate to.
	 */
	$scope.init = function() {
		this.preInit();

		if (this.isCancelPress()) {
			this.confirmScn = null;
		}

		if (CallStack.existsState(this.programName)) {
			this.resetVars();
			this.stateVariable = {};
			CallStack.remove(this.programName);
		}

		this.stateVariable.numRows = this.numberOfRows;

		if (this.getPassedParams()) {
			this.entryParamterList = this.valueList;
		}

		this.setScreenEntryParameters(this.entryParamterList);

		// Calls the Controller logic initialize method to load the screen data
		var initializeOperation = WorkWithOrderDetailsService.initialize.get({
			stateVariable: this.stateVariable,
			action: this.action,
			nextFunction: this.nextFunction,
			nextProgram: this.nextProgram
		});

		this.setRequestIndicator(initializeOperation, Constants.LOADING + ' ' + this.programName + '...');
		var initializeOperationPromise = initializeOperation.$promise;
		initializeOperationPromise.then((function(response) {
			$log.info("WorkWithOrderDetails (WWCONDET) - Logic initialize executed.");
			this.assignPgmResponse(response);

			if (this.nextProgram.length != 0) {
				var method = this.nextProgram + "EntryPanel";
				this.redirectToScreen(method);
			}
			else if (this.nextFunction.length != 0) {
				var loadOperationPromise = null;

				if (this.nextFunction.endsWith("EntryPanel")) {
					loadOperationPromise = this.loadFlatData();
				}

				$q.when(loadOperationPromise).then((function() {
					if (this.nextProgram.length != 0) {
						this.redirectToScreen(this.nextProgram + "EntryPanel");
					}
					else if ("WorkWithOrderDetailsEntryPanel" !== this.nextFunction) {
						this.redirectToScreen(this.nextFunction);
					}

					this.manageCallStack(this.nextFunction);
				}).bind($scope));
			}

			this.updateGridViewModel('dssel');
			Utils.openPdfReport();
		}).bind($scope), this.handleError.bind($scope));
	};

	/**
	 * Loads the flat screen data by making a Web Service call to perform logic
	 * {@link panelDisplay} operation or {@link entryPanelDisplay} operation for
	 * panel and entryPanel screens respectively.
	 *
	 * @returns {@var;currentOperationPromise}
	 */
	$scope.loadFlatData = function() {
		this.preInit();

		if (Params["rtnValList"] != null) {
			CallStack.removeAfter(this.programName);
		}

		if (this.errorFound) {
			var error = Params["ERROR"];

			if (error != null) {
				this.showBusinessMessages = true;
				this.businessException = new BusinessException(null, error);
				Params["ERROR"] = null;
			}

			$rootScope.requestInProgress.$resolved = true;

			return null;
		}

		// Checks if the calling program requires post processing after the
		// called program process ends
		this.postProcessingCycle = this.isPostProcessRequired();

		var method = this.nextFunction;
		var currentOperationPromise = null;

		if (!this.pgmCalled || (this.stateVariable.zreload === "Y") || multiSelProcess) {
			if ("WorkWithOrderDetailsEntryPanel" === method) {
				// Calls the Controller logic entryPanelDisplay method to load the
				// screen information
				this.currentOperation = WorkWithOrderDetailsService.entryPanelDisplay.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("WorkWithOrderDetails (WWCONDET) - Logic entryPanelDisplay executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("WorkWithOrderDetailsPanel" === method) {
				// Calls the Controller logic panelDisplay method to load the
				// screen information
				this.currentOperation = WorkWithOrderDetailsService.panelDisplay.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("WorkWithOrderDetails (WWCONDET) - Logic panelDisplay executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("WorkWithOrderDetailsPanel3" === method) {
				// Calls the Controller logic panel3Display method to load the
				// screen information
				this.currentOperation = WorkWithOrderDetailsService.panel3Display.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("WorkWithOrderDetails (WWCONDET) - Logic panel3Display executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("WorkWithOrderDetailsPanel4" === method) {
				// Calls the Controller logic panel4Display method to load the
				// screen information
				this.currentOperation = WorkWithOrderDetailsService.panel4Display.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("WorkWithOrderDetails (WWCONDET) - Logic panel4Display executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}

			if (currentOperationPromise === null) {
				return null;
			}

			currentOperationPromise.then((function() {
				this.stateVariable.zreload = "N";
				this.setCancelPress(false);
				this.updateFlatViewModel();
			}).bind($scope));
		}
		else {
			this.action = "NOUPDATE";

			if ("WorkWithOrderDetailsPanel".equalsIgnoreCase(method)) {
				// Calls the Controller logic panelProcess method to
				// process the screen information
				this.currentOperation = WorkWithOrderDetailsService.panelProcess.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("WorkWithOrderDetails (WWCONDET) - Logic panelProcess executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("WorkWithOrderDetailsPanel3".equalsIgnoreCase(method)) {
				// Calls the Controller logic panel3Process method to
				// process the screen information
				this.currentOperation = WorkWithOrderDetailsService.panel3Process.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("WorkWithOrderDetails (WWCONDET) - Logic panel3Process executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}

			if (currentOperationPromise === null) {
				return null;
			}

			currentOperationPromise.then((function() {
				this.action = "";
				this.setNextProgram("");

				if ((this.nextFunction.length == 0) ||
						("*EXIT" == this.nextFunction)) {
					this.nextFunction = method;
				}

				this.updateFlatViewModel();
			}).bind($scope));
		}

		this.setCancelPress(false);

		return currentOperationPromise;
	};

	/**
	 * Performs submit record operation when submit button is clicked.
	 */
	$scope.submitOnFlat = function() {
		this.submitRecord();
	};

	/**
	 * Validates and processes the action taken on the flat screen (other than
	 * Cancel). Calls {@link entryPanelProcess} or {@link panelProcess} method
	 * to process the action.
	 *
	 * @returns {@var;currentOperationPromise}
	 */
	$scope.submitRecord = function() {
		this.validateFormStatus(null);
		this.showBusinessMessages = this.errorFound = false;
		this.stateVariable.zreload = "N";
		var currentScreen = this.nextFunction;
		// Save previousFunction so that nextFunction can be reset on exception
		this.previousFunction = currentScreen;
		this.resetVars();
		var currentOperationPromise = null;

		if ("WorkWithOrderDetailsPanel" === currentScreen) {
			this.currentOperation = WorkWithOrderDetailsService.panelProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("WorkWithOrderDetails (WWCONDET) - Logic panelProcess executed.");
				this.assignPgmResponse(response);
			}).bind($scope), this.handleError.bind($scope));
		}
		else if ("WorkWithOrderDetailsPanel3" === currentScreen) {
			this.currentOperation = WorkWithOrderDetailsService.panel3Process.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("WorkWithOrderDetails (WWCONDET) - Logic panel3Process executed.");
				this.assignPgmResponse(response);
			}).bind($scope), this.handleError.bind($scope));
		}

		if (currentOperationPromise === null) {
			return null;
		}

		currentOperationPromise.then((function() {
			var nextScreen = "";

			try {
				var screenTo = "";
				this.nextFunction = (("" === this.nextFunction) ? this.previousFunction : this.nextFunction);

				// Process the error message. If found, then throw
				// <code>BusinessException</code>
				this.processErrorMessage(this.messages);

				if (this.nextProgram.length != 0) {
					this.pgmCalled = true;
					screenTo = this.nextProgram.trim() + "EntryPanel";
					this.setParamToPass(screenTo);
				}
				else {
					this.pgmCalled = false;
					screenTo = this.getNavigateTo(this.nextFunction, this.isIndicatorOn(29, 0));
				}

				nextScreen = screenTo;
			}
			catch (e) {
				if (e instanceof BusinessException) {
					this.showBusinessMessages = this.errorFound = true;
					this.validateFormStatus(e);
					nextScreen = "";
				}
			}
			finally {
				if ("*EXIT" === this.nextFunction) {
					this.nextFunction = this.previousFunction;
				}

				this.manageCallStack(screenTo);
			}

			if ((nextScreen != null) && (nextScreen != "")) {
				this.setParamToPass(nextScreen);
				this.redirectToScreen(nextScreen);
			}
			else {
				Utils.openPdfReport();
			}
		}).bind($scope));

		return currentOperationPromise;
	};

	/**
	 * Validates and processes the action taken on the grid screen (other than
	 * Cancel). Executes Web Service call to perform logic
	 * {@link entryPanelProcess} or {@link gridProcess} method to process the
	 * action.
	 *
	 * All function actions, pagination, position to (filtering) are processed
	 * through {@link entryPanelProcess} and the remaining actions are processed
	 * through {@link gridProcess}.
	 *
	 * Catches {@link BusinessException} exception if validation fails and
	 * displays the error message on the screen.
	 *
	 * @param {string} dssel to distinguish which action needs to be 
	 *        processed by logic {@link gridProcess} operation
	 * @param {boolean} funKeyMode to indicate if function actions 
	 *        (Function Key equivalent e.g. F6/F10/F11), pagination, position 
	 *        to (filtering) is opted
	 * @returns {@var;currentOperationPromise}
	 */
	$scope.process = function(dssel, funKeyMode) {
		// Save previous function so that next function can reset on exception
		this.previousFunction = this.nextFunction;
		this.resetVars();
		this.stateVariable.zreload = "N";
		this.showBusinessMessages = this.errorFound = false;
		this.stateVariable.numRows = this.numberOfRows;
		this.stateVariable.fldsFiltered = this.fldsFiltered;
		this.stateVariable.filterFlds = this.filterFlds;
		var currentOperationPromise;

		if (funKeyMode) {
			this.cachedPage = false;
			// Calls the Controller logic entryPanelProcess method to process
			// the Function actions (e.g.: F6/F10/F11 etc), pagination and
			// filtering
			this.currentOperation = WorkWithOrderDetailsService.entryPanelProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("WorkWithOrderDetails (WWCONDET) - Logic entryPanelProcess executed.");
				this.assignPgmResponse(response);
			}).bind($scope), this.handleError.bind($scope));
		}
		else {
			this.stateVariable.dssel = dssel;
			// Calls the Controller logic gridProcess method to process Grid
			// screen actions
			this.currentOperation = WorkWithOrderDetailsService.gridProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("WorkWithOrderDetails (WWCONDET) - Logic gridProcess executed.");
				this.assignPgmResponse(response);
			}).bind($scope), this.handleError.bind($scope));
		}

		currentOperationPromise.then((function() {
			var nextScreen = "";
			var screenToGo = "";
			this.stateVariable.dssel = "";

			try {
				// Process the error message, if found then throw 
				// <code>BusinessException</code>
				this.processErrorMessage(this.messages);

				if (this.nextProgram.length != 0) {
					this.pgmCalled = true;
					screenToGo = this.nextProgram + "EntryPanel";
					this.nextFunction = this.previousFunction;
				}
				else if (this.nextFunction.length != 0) {
					this.pgmCalled = false;
					screenToGo = this.nextFunction;
				}
				else {
					this.nextFunction = this.previousFunction;
				}
			}
			catch (e) {
				if (e instanceof BusinessException) {
					this.showBusinessMessages = this.errorFound = true;
					this.validateFormStatus(e);

					nextScreen = "";
				}
			}

			if (nextScreen == "") {
				screenToGo = this.getNavigateTo(screenToGo, false);
				this.manageCallStack(screenToGo);
				nextScreen = screenToGo;
			}

			Params[Constants.SCREEN_MODE] = this.mode;
			Params["add"] = this.add;
			this.setParamToPass(nextScreen);

			if ((nextScreen != null) && (nextScreen != "")) {
				this.redirectToScreen(nextScreen);
			}

			this.updateGridViewModel('dssel');
		}).bind($scope));

		return currentOperationPromise;
	};

	/**
	 * Opens the specified Prompt screen.
	 *
	 * @param {String} screen
	 * @param {Array} fields
	 */
	$scope.prompt = function(screen, fields) {
		var modalInstance = this.openWindow(screen);
		modalInstance.result.then((function(result) {
			fields = fields.split(',');

			if (fields == null) {
				return;
			}

			var selRow = result[0];
			var selFields = result[1];

			if (fields.length > selFields.length) {
				fields = fields.slice(0, selFields.length);
			}

			var i = 0;

			fields.forEach(function(field) {
				field = field.trim();
				this.stateVariable[field] = selRow[selFields[i]];
				i++;
			}, this);

			this.processQuestionMark();
		}).bind($scope));
	};

	/**
	 * Performs the action after Prompt processing.
	 */
	$scope.processQuestionMark = function() {
		this.action = "NOUPDATE";
		this.previousFunction = this.nextFunction;

 		if (this.mode == Constants.ADD_MODE) {
			this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 6, '1');
		}

		var method = ResourceManager.getKey(this.nextFunction, "prefNames");
		method = Utils.getLongMethodName(method + "p", "WWCONDET");

		this.currentOperation = WorkWithOrderDetailsService[method].get({
			stateVariable: this.stateVariable,
			action: this.action,
			nextFunction: this.nextFunction,
			nextProgram: this.nextProgram
		});

		var currentOperationPromise = this.currentOperation.$promise;
		currentOperationPromise.then((function(response) {
			$log.info("WorkWithOrderDetails (WWCONDET) - Logic executed.");
			this.assignPgmResponse(response);
			Params["errorFound"] = true;
			this.nextFunction = this.previousFunction;
			this.setNextProgram("");

			if (this.mode == Constants.ADD_MODE) {
				this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 6, '0');
			}

			try {
				// Process the error message, if found then throw <code>BusinessException</code>
				this.processErrorMessage(this.messages);
			}
			catch (e) {
				if (e instanceof BusinessException) {
					Params["ERROR"] = e.message;
				}
			}

			var screenTo = this.nextFunction;
			this.redirectToScreen(screenTo);
		}).bind($scope), this.handleError.bind($scope));
	};

	/**
	 * Manages the call stack for screen navigation.
	 *
	 * @param {String} screen screen name to goto
	 */
	$scope.manageCallStack = function(screen) {
		if (screen.startsWith("WorkWithOrderDetails")) {
			CallStack.add(screen, this);
		}
	};

	/**
	 * Add on grid.
	 */
	$scope.addRecordWWCONDET01G = function() {
		/*Integer xwordn = stateVariable.getXwordn();
		String xwbccd = stateVariable.getXwbccd();
		String xwg4tx = stateVariable.getXwg4tx();
		clearObject(stateVariable);
		stateVariable.setXwordn(xwordn);
		stateVariable.setXwbccd(xwbccd);
		stateVariable.setXwg4tx(xwg4tx);*/
		this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 6, '1');
		this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 28, '1');
		this.mode = Constants.ADD_MODE;
		this.add = true;
		this.process("6", true).then((function() {
			this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 6, '0');
			this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 28, '0');
		}).bind($scope));
	};

	/**
	 * Change on selected grid record.
	 */
	$scope.changeRecordWWCONDET01G = function() {
		var wwcondetg = null;
		var selectedRowList = this.getSelectedRows();

		if (!this.isRecordSelected(selectedRowList)) {
			return;
		}

		if (selectedRowList.length == 1) {
			wwcondetg = selectedRowList[0];
		}
		else {
			wwcondetg = this.multipleRowProcess(wwcondetg, selectedRowList, "changeRecordWWCONDET01G");
		}

		Utils.assignObject(this.stateVariable, wwcondetg);
		this.mode = Constants.CHANGE_MODE;
		this.process("2", false);
	};

	/**
	 * Delete on selected grid record.
	 */
	$scope.deleteRecordWWCONDET01G = function() {
		var wwcondetg = null;
		var selectedRowList = this.getSelectedRows();

		if (!this.confirmDelete(selectedRowList)) {
			return;
		}

		if (selectedRowList.length == 1) {
			wwcondetg = selectedRowList[0];
		}
		else {
			wwcondetg = this.multipleRowProcess(wwcondetg, selectedRowList, "deleteRecordWWCONDET01G");
		}

		Utils.assignObject(this.stateVariable, wwcondetg);
		this.mode = Constants.DELETE_MODE;
		this.process("4", false).then((function() {
			if (!this.errorFound) {
				Params[Constants.SCREEN_MODE] = Constants.CONFIRMATION_MODE;
			}
		}).bind($scope));
	};

	/**
	 * Display on selected grid record.
	 */
	$scope.displayWWCONDET01G = function() {
		var wwcondetg = null;
		var selectedRowList = this.getSelectedRows();

		if (!this.isRecordSelected(selectedRowList)) {
			return;
		}

		if (selectedRowList.length == 1) {
			wwcondetg = selectedRowList[0];
		}
		else {
			wwcondetg = this.multipleRowProcess(wwcondetg, selectedRowList, "displayWWCONDET01G");
		}

		Utils.assignObject(this.stateVariable, wwcondetg);
		this.mode = Constants.DISPLAY_MODE;
		this.process("5", false);
	};

	/**
	 * Confirm on flat record.
	 */
	$scope.confirmWWCONDET02D = function() {
		this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 23, '1');
		this.submitRecord().then((function() {
			this.stateVariable.qin = StringUtils.setIndicator(this.stateVariable.qin, 23, '0');
		}).bind($scope));
	};

	/**
	 * Sets the action parameters to pass depending on the action taken.
	 *
	 * @param screen called screen name
	 */
	$scope.setParamToPass = function(screen) {
	};

	/**
	 * Navigates to the appropriate screen.
	 *
	 * @param {String} screenToGo screen name
	 * @param {Boolean} callFrmSubmit
	 * @returns {String} screen to navigate to
	 */
	$scope.getNavigateTo = function(screenToGo, callFrmSubmit) {
		if (callFrmSubmit && this.isConfirmationScreen()) {
			Params[Constants.SCREEN_MODE] = Constants.CONFIRMATION_MODE;
		}
		else if ((callFrmSubmit && (this.previousFunction === this.nextFunction)) ||
				("*EXIT" === screenToGo)) {
			while (screenToGo.startsWith("WorkWithOrderDetails") ||
					("*EXIT" === screenToGo)) {
				screenToGo = this.cancel(this.stateVariable, "WorkWithOrderDetails", false);
			}

			this.collectReturnValues();
		}

		return screenToGo;
	};

	/**
	 * Registers the return parameter value list to be used by the calling
	 * program for the post process.
	 */
	$scope.collectReturnValues = function() {
		if (this.postProcessingCycle) {
			this.setCancelPress(false);
		}

		var rtnValList = [
			this.stateVariable.xwordn, this.stateVariable.xwbccd,
			this.stateVariable.xwg4tx
		];

		Params["rtnValList"] = rtnValList;
	};

	/**
	 * Fetches records on the basis of filter data.
	 */
	$scope.applyFilter = function() {
		if (this.filterData == null) {
			this.filterData = {
				xwabcd: null
			};
		}

		var wwcondetg = this.filterData;

		this.wwcondetgXwabcd = wwcondetg.xwabcd;
		this.stateVariable.xwabcd = wwcondetg.xwabcd;
		this.fldsFiltered["Xwabcd"] = (this.stateVariable.xwabcd != null);

		this.filterApplied = (((this.wwcondetgXwabcd != null) && (this.wwcondetgXwabcd.toString().trim() != "")));
		this.stateVariable.sflrrn = 0;

		this.pageHandler.reset();
		this.endOfFile = false;

		// Process the filtering operation
		this.process("", true);
		this.errorFound = false;
	};

	/**
	 * Registers the entry parameters before the initialize process.
	 *
	 * @param {Array} entryParamList List of passed parameter values
	 */
	$scope.setScreenEntryParameters = function(entryParamList) {
		if (entryParamList == null) {
			return;
		}

		var obj = null;
		obj = (entryParamList.length > 0) ? entryParamList[0] : null;

		if (obj != null) {
			this.stateVariable.xwordn = parseInt(obj, 10);
		}

		obj = (entryParamList.length > 1) ? entryParamList[1] : null;

		if (obj != null) {
			this.stateVariable.xwbccd = obj.toString();
		}

		obj = (entryParamList.length > 2) ? entryParamList[2] : null;

		if (obj != null) {
			this.stateVariable.xwg4tx = obj.toString();
		}
	};

	/**
	 * Processes multiple record selection on the grid.
	 *
	 * @param wwcondetg grid data model
	 * @param selectedRowList list of selected rows
	 * @param callingFunName name of action to be processed
	 * @returns {Object} grid data model
	 */
	$scope.multipleRowProcess = function(wwcondetg, selectedRowList, callingFunName) {
		var pendingList = Params["WorkWithOrderDetailsController_pendingRowList"];

		if (pendingList != null) {
			selectedRowList = pendingList;
		}

		if (selectedRowList.length != 0) {
			// Remove the first entry from the list and return that entry
			wwcondetg = selectedRowList.shift();
		}

		if (selectedRowList.length != 0) {
			Params["WorkWithOrderDetailsController_pendingScreensFunction"] = callingFunName;
			pendingList = $.extend(true, [], selectedRowList);
			Params["WorkWithOrderDetailsController_pendingRowList"] = pendingList;
		}
		else {
			Params["WorkWithOrderDetailsController_pendingScreensFunction"] = null;
			Params["WorkWithOrderDetailsController_pendingRowList"] = null;
		}

		return wwcondetg;
	};

	/**
	 * Navigates to the previous screen on Cancel.
	 */
	$scope.cancelPress = function() {
		if (this.isConfirmationScreen()) {
			Params[Constants.SCREEN_MODE] = Constants.CHANGE_MODE;
			this.stateVariable.zzmode = "";
		}

		var screen = this.cancel(this.stateVariable, "WorkWithOrderDetails", false);
		this.redirectToScreen(screen);
	};
});