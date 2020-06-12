"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since (2014-04-04.12:46:23)
 */
app.factory('Auth', function($rootScope, $log, $location) {
    var Auth = {
        token: null,
        userDesc: null,

        checkStatus: function() {
            if (this.token == null) {
                $rootScope.hideProgressIndicator = true;
                $log.warn("Authentication failed.");
                this.reset();
                $location.path("/application");

                return false;
            }

            return true;
        },

        reset: function() {
            this.token = null;
            this.userDesc = null;
        },

        setStatus: function(authStatus) {
            if (authStatus.authenticated) {
                this.token = authStatus.authToken;
                this.userDesc = authStatus.userDesc;
            }
            else {
                this.reset();
            }
        }
    };

    return Auth;
});