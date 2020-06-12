"use strict";

var app = angular.module('app');

/**
 * Controller for Login view.
 *
 * @author Robin Rizvi
 * @since (2014-06-30.17:27:23)
 */
app.controller('LoginController',
	function($log, $rootScope, $scope, Auth, LoginService, ResourceManager,
		UiUtils) {

	/**
     * Performs pre-initialization operations to setup the controller's
     * state.
     */
    $scope.preInit = function() {
        $rootScope.hideProgressIndicator = true;
        UiUtils.setupProgramUI();
        $rootScope.loginTime = null;
        $rootScope.userDescription = null;
        $rootScope.userAuthenticated = false;
        this.consts = ResourceManager.getResource('constants');
    };

    /**
     * Entry point method invoked from (@link Login.html}.
     */
    $scope.init = function() {
        this.preInit();
        $scope.username = '';
        $scope.password = '';
    };

    /**
     * Performs authentication operation by sending user's credentials to
     * WebService for authentication and processes the response received from
     * the WebService.
     */
    $scope.login = function() {
        this.showErrorMessages = false;
        UiUtils.resetProgramUI();
        var authStatus = LoginService.authStatus.get({
            username: $scope.username,
            password: $scope.password
        });

        this.currentOperation = authStatus;
        var authStatusPromise = authStatus.$promise;
        authStatusPromise.then((function(authStatus) {
            $log.info('Authentication operation completed.');
            this.processStatus(authStatus);
        }).bind($scope), (function() {
            $log.warn("Could not connect to server.");
            this.showErrorMessages = true;
            this.errorMessages = ["Could not connect to server"];
        }).bind($scope));
    };

    /**
     * Processes the authentication status received from WebService.
     *
     * @param {Object} authStatus Authentication Status
     */
    $scope.processStatus = function(authStatus) {
        if (authStatus.authenticated) {
            Auth.setStatus(authStatus);
            $rootScope.loginTime = moment().format('hh:mm:ss a');
            $rootScope.userDescription = authStatus.userDesc;
            $rootScope.userAuthenticated = true;
            $scope.redirectToScreen('ScreenParams');
        }
        else {
            $log.warn('Invalid credentials');
            this.showErrorMessages = true;
            this.errorMessages = ["Invalid Credentials"];
        }
    };
});