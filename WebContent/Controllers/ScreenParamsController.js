"use strict";

var app = angular.module('app');

/**
 * Controller for ScreenParams view.
 *
 * @author Robin Rizvi
 * @since (2014-06-30.17:27:23)
 */
app.controller('ScreenParamsController',
    function($rootScope, $scope, CallStack, Constants, Params, UiUtils,
    ResourceManager) {

    /**
     * Navigates to the previous screen on Cancel.
     */
    $scope.cancelPress = function() {
        this.redirectToScreen("application");
    };

    /**
     * Entry point method invoked from (@link ScreenParams.html}.
     */
    $scope.init = function() {
        this.preInit();
        this.manageCallStack("ScreenParams");
    };

    /**
     * Manages the call stack for screen navigation.
     *
     * @param {String} screen screen name to goto
     */
    $scope.manageCallStack = function(screen) {
        CallStack.add(screen, this);
    };

    /**
     * Performs navigation to the entered program when navigate button is
     * clicked on the UI.
     */
    $scope.navigate = function() {
        this.showErrorMessages = false;
        UiUtils.resetProgramUI();

		if (this.screenName === null) {
            this.showErrorMessages = true;
            this.errorMessages = ["Invalid Program Name"];

            return;
		}

        var shortName = this.screenName.toUpperCase() + "01D";
        var longName = $scope.getPrefLongName(shortName);
        var screenToNavigate = longName;

        if (shortName === longName) {
            shortName = this.screenName.toUpperCase() + "D";
            longName = screenToNavigate = $scope.getPrefLongName(shortName);

            if (shortName === longName) {
                screenToNavigate = longName + "EntryPanel";
            }
        }

        if (this.screenParams !== null) {
            Params[Constants.PARAM_VALUES] = this.screenParams.split(",");
            Params[Constants.SCREEN_MODE] = Constants.CHANGE_MODE;
        }

        if (shortName === longName) {
            this.showErrorMessages = true;
            this.errorMessages = ["Invalid Program Name"];

            return;
        }

        $scope.redirectToScreen(screenToNavigate);
    };

    /**
     * Performs pre-initialization operations to setup the controller's state.
     */
    $scope.preInit = function() {
        $rootScope.hideProgressIndicator = true;
        UiUtils.setupProgramUI();
        $scope.screenName = null;
        $scope.screenParams = null;
        this.consts = ResourceManager.getResource('constants');
    };
});