"use strict";

var app = angular.module('app');

/**
 * Controller for HTML (Non-Synon Program): CustomerDetailMaintenance (CUSTMNT1).
 *
 * @author KAMALN
 */
app.controller('CustomerDetailMaintenanceController',
	function($log, $modal, $q, $rootScope, $route, $scope,
		CustomerDetailMaintenanceService, Auth, BusinessException, CallStack,
		Constants, Filter, Pager, Params, ResourceManager, StringUtils, Utils,
		UiUtils) {

	/**
	 * Performs pre-initialization operations to setup the controller's state.
	 */
	$scope.preInit = function() {
		if (!this.programName) {
			this.initialize();
			this.setReturnValList();
			this.programName = 'CustomerDetailMaintenance';
			this.stateVariable.qin = new Array(100).join('0');
			this.setRequestIndicator();
			CallStack.restoreState(this);
		}
	};

	/**
	 * Entry point function invoked from (@link CustomerDetailMaintenanceEntryPanel.html)
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

		if (this.getPassedParams()) {
			this.entryParamterList = this.valueList;
		}

		this.setScreenEntryParameters(this.entryParamterList);

		// Calls the Controller logic initialize method to load the screen data
		var initializeOperation = CustomerDetailMaintenanceService.initialize.get({
			stateVariable: this.stateVariable,
			action: this.action,
			nextFunction: this.nextFunction,
			nextProgram: this.nextProgram
		});

		this.setRequestIndicator(initializeOperation, Constants.LOADING + ' ' + this.programName + '...');
		var initializeOperationPromise = initializeOperation.$promise;
		initializeOperationPromise.then((function(response) {
			$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic initialize executed.");
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
					else if ("CustomerDetailMaintenanceEntryPanel" !== this.nextFunction) {
						this.redirectToScreen(this.nextFunction);
					}

					this.manageCallStack(this.nextFunction);
				}).bind($scope));
			}
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

		if (!this.pgmCalled || (this.stateVariable.zreload === "Y")) {
			if ("CustomerDetailMaintenanceEntryPanel" === method) {
				// Calls the Controller logic entryPanelDisplay method to load the
				// screen information
				this.currentOperation = CustomerDetailMaintenanceService.entryPanelDisplay.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic entryPanelDisplay executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("CustomerDetailMaintenancePanel" === method) {
				// Calls the Controller logic panelDisplay method to load the
				// screen information
				this.currentOperation = CustomerDetailMaintenanceService.panelDisplay.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic panelDisplay executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("CustomerDetailMaintenancePanel3" === method) {
				// Calls the Controller logic panel3Display method to load the
				// screen information
				this.currentOperation = CustomerDetailMaintenanceService.panel3Display.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				this.setRequestIndicator(this.currentOperation, Constants.LOADING + ' ' + this.programName + '...');
				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic panel3Display executed.");
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

			if ("CustomerDetailMaintenanceEntryPanel".equalsIgnoreCase(method)) {
				// Calls the Controller logic entryPanelProcess method to
				// process the screen information
				this.currentOperation = CustomerDetailMaintenanceService.entryPanelProcess.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic entryPanelProcess executed.");
					this.assignPgmResponse(response);
				}).bind($scope), this.handleError.bind($scope));
			}
			else if ("CustomerDetailMaintenancePanel".equalsIgnoreCase(method)) {
				// Calls the Controller logic panelProcess method to
				// process the screen information
				this.currentOperation = CustomerDetailMaintenanceService.panelProcess.get({
					stateVariable: this.stateVariable,
					action: this.action,
					nextFunction: this.nextFunction,
					nextProgram: this.nextProgram
				});

				currentOperationPromise = this.currentOperation.$promise;
				currentOperationPromise.then((function(response) {
					$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic panelProcess executed.");
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

		if ("CustomerDetailMaintenanceEntryPanel" === currentScreen) {
			this.currentOperation = CustomerDetailMaintenanceService.entryPanelProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic entryPanelProcess executed.");
				this.assignPgmResponse(response);
			}).bind($scope), this.handleError.bind($scope));
		}
		else if ("CustomerDetailMaintenancePanel" === currentScreen) {
			this.currentOperation = CustomerDetailMaintenanceService.panelProcess.get({
				stateVariable: this.stateVariable,
				action: this.action,
				nextFunction: this.nextFunction,
				nextProgram: this.nextProgram
			});

			currentOperationPromise = this.currentOperation.$promise;
			currentOperationPromise.then((function(response) {
				$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic panelProcess executed.");
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
		var method = ResourceManager.getKey(this.nextFunction, "prefNames");
		method = Utils.getLongMethodName(method + "p", "CUSTMNT1");

		this.currentOperation = CustomerDetailMaintenanceService[method].get({
			stateVariable: this.stateVariable,
			action: this.action,
			nextFunction: this.nextFunction,
			nextProgram: this.nextProgram
		});

		var currentOperationPromise = this.currentOperation.$promise;
		currentOperationPromise.then((function(response) {
			$log.info("CustomerDetailMaintenance (CUSTMNT1) - Logic executed.");
			this.assignPgmResponse(response);
			Params["errorFound"] = true;
			this.nextFunction = this.previousFunction;
			this.setNextProgram("");

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
		if (screen.startsWith("CustomerDetailMaintenance")) {
			CallStack.add(screen, this);
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
			while (screenToGo.startsWith("CustomerDetailMaintenance") ||
					("*EXIT" === screenToGo)) {
				screenToGo = this.cancel(this.stateVariable, "CustomerDetailMaintenance", false);
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
			this.stateVariable.xwbccd
		];

		Params["rtnValList"] = rtnValList;
	};

	$scope.cusnoValidate = function() {
		var value = this.stateVariable.cusno;

		if ((value > 99999) || (value < 0)) {
			throw new BusinessException([ "cusno" ],
				ResourceManager.get("RANGEERROR", "messages", [ 0, 99999 ]));
		}
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
			this.stateVariable.xwbccd = obj.toString();
		}
	};

	/**
	 * Navigates to the previous screen on Cancel.
	 */
	$scope.cancelPress = function() {
		var screen = this.getScreenToNavigateTo(this.stateVariable, "CustomerDetailMaintenance", false);
		this.redirectToScreen(screen);
	};
});