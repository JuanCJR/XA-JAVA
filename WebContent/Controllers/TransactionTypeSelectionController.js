"use strict";

var app = angular.module('app');

/**
 * Controller for HTML (Non-Synon Program): TransactionTypeSelection (TRNTPSEL).
 *
 * @author KAMALN
 */
app.controller('TransactionTypeSelectionController',
	function($log, $modal, $q, $rootScope, $route, $scope,
		TransactionTypeSelectionService, Auth, BusinessException, CallStack,
		Constants, Filter, Pager, Params, ResourceManager, StringUtils, Utils,
		UiUtils) {

	var $modalInstance = $scope.modalInstance;

	/**
	 * Performs pre-initialization operations to setup the controller's state.
	 */
	$scope.preInit = function() {
		if (!this.programName) {
			$scope = this;
			this.initialize();
			this.setReturnValList();
			this.programName = 'TransactionTypeSelection';
			// 5 is the number of rows to display on the original application
			this.numberOfRows = parseInt((this.getRowsToDisplay() == 0) ? 5 : this.getRowsToDisplay());
			this.populateFilterFieldData();
			this.pageHandler = new Pager(this.numberOfRows);
			this.pageHandler.reset();
		}
	};

	/**
	 * Collects the filter field information.
	 */
	$scope.populateFilterFieldData = function() {
		this.filterFlds.push({
			fltFld: "Xwricd",
			fltName: "Xwricd",
			opType: Filter.OP_GREATER_OR_EQUAL
		});
	};

	/**
	 * Entry point function invoked from (@link TransactionTypeSelectionEntryPanel.html)
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
		var initializeOperation = TransactionTypeSelectionService.initialize.get({
			stateVariable: this.stateVariable,
			action: this.action,
			nextFunction: this.nextFunction,
			nextProgram: this.nextProgram
		});

		this.currentOperation = initializeOperation;
		var initializeOperationPromise = initializeOperation.$promise;
		initializeOperationPromise.then((function(response) {
			$log.info("TransactionTypeSelection (TRNTPSEL) - Logic initialize executed.");
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
					else if ("TransactionTypeSelectionEntryPanel" !== this.nextFunction) {
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
			$rootScope.requestInProgress.$resolved = true;

			return null;
		}

		// Checks if the calling program requires post processing after the
		// called program process ends
		this.postProcessingCycle = this.isPostProcessRequired();

		var method = this.nextFunction;
		var currentOperationPromise = null;

		if (!this.pgmCalled || (this.stateVariable.zreload === "Y")) {
			if ("TransactionTypeSelectionEntryPanel" === method) {
				// Calls the Controller logic entryPanelDisplay method to load the
				// screen information
				this.currentOperation = TransactionTypeSelectionService.entryPanelDisplay.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("TransactionTypeSelection (TRNTPSEL) - Logic entryPanelDisplay executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("TransactionTypeSelectionPanel" === method) {
				// Calls the Controller logic panelDisplay method to load the
				// screen information
				this.currentOperation = TransactionTypeSelectionService.panelDisplay.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("TransactionTypeSelection (TRNTPSEL) - Logic panelDisplay executed.");
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

			if ("TransactionTypeSelectionPanel".equalsIgnoreCase(method)) {
				// Calls the Controller logic panelProcess method to
				// process the screen information
				this.currentOperation = TransactionTypeSelectionService.panelProcess.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("TransactionTypeSelection (TRNTPSEL) - Logic panelProcess executed.");
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
	 * Validates prompt screen through Ajax call.
	 *
	 * @return screen to go to
	 * @throws Exception
	 */
	$scope.validatePromptScreen = function() {
		var nextScreen = this.submitRecord();

		if (this.parentScn == null) {
			return nextScreen;
		}

		return "";
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

		if ("TransactionTypeSelectionPanel" === currentScreen) {
			this.currentOperation = TransactionTypeSelectionService.panelProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("TransactionTypeSelection (TRNTPSEL) - Logic panelProcess executed.");
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
			this.currentOperation = TransactionTypeSelectionService.entryPanelProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("TransactionTypeSelection (TRNTPSEL) - Logic entryPanelProcess executed.");
				this.assignPgmResponse(response);
			}).bind($scope), this.handleError.bind($scope));
		}
		else {
			this.stateVariable.dssel = dssel;
			// Calls the Controller logic gridProcess method to process Grid
			// screen actions
			this.currentOperation = TransactionTypeSelectionService.gridProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("TransactionTypeSelection (TRNTPSEL) - Logic gridProcess executed.");
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

			if ((nextScreen != null) && (nextScreen != "")) {
				this.redirectToScreen(nextScreen);
			}

			this.updateGridViewModel('dssel');
		}).bind($scope));

		return currentOperationPromise;
	};

	/**
	 * Manages the call stack for screen navigation.
	 *
	 * @param {String} screen screen name to goto
	 */
	$scope.manageCallStack = function(screen) {
		if (screen.startsWith("TransactionTypeSelection")) {
			this.screen = screen;
		}
	};

	/**
	 * Select a record from prompt screen.
	 */
	$scope.promptSelect = function() {
		var selectedRows = this.getSelectedRows();

		if (selectedRows.length == 0) {
			alert("Please select a record");
		}
		else {
			$modalInstance.close([selectedRows[0], ['xwricd', 'xwtdsc']]);
		}
	};

	/**
	 * Navigates to the appropriate screen.
	 *
	 * @param {String} screenToGo screen name
	 * @param {Boolean} callFrmSubmit
	 * @returns {String} screen to navigate to
	 */
	$scope.getNavigateTo = function(screenToGo, callFrmSubmit) {
		if ((callFrmSubmit && (this.previousFunction === this.nextFunction)) ||
				("*EXIT" === screenToGo)) {
			while (screenToGo.startsWith("TransactionTypeSelection") ||
					("*EXIT" === screenToGo)) {
				screenToGo = this.cancel(this.stateVariable, "TransactionTypeSelection", true);
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
			this.stateVariable.xwricd, this.stateVariable.xwtdsc
		];

		Params["rtnValList"] = rtnValList;
	};

	/**
	 * Fetches records on the basis of filter data.
	 */
	$scope.applyFilter = function() {
		if (this.filterData == null) {
			this.filterData = {
				xwricd: null
			};
		}

		var trntpselg = this.filterData;

		this.trntpselgXwricd = trntpselg.xwricd;
		this.stateVariable.xwricd = trntpselg.xwricd;
		this.fldsFiltered["Xwricd"] = (this.stateVariable.xwricd != null);

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
			this.stateVariable.xwricd = obj.toString();
		}

		obj = (entryParamList.length > 1) ? entryParamList[1] : null;

		if (obj != null) {
			this.stateVariable.xwtdsc = obj.toString();
		}
	};

	/**
	 * Navigates to the previous screen on Cancel.
	 */
	$scope.cancelPress = function() {
		if ($modalInstance != null) {
			$modalInstance.dismiss('User cancelled prompt selection.');
		}

		var screen = this.cancel(this.stateVariable, "TransactionTypeSelection", true);
		this.redirectToScreen(screen);
	};
});