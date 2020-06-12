"use strict";

var app = angular.module('app');

/**
 * Allows to intercept and manipulate every HTTP request and response.
 *
 * @author Robin Rizvi
 * @version (2014-06-17.18:43:23)
 */
app.factory('HttpInterceptor', function() {
    var interceptor = {
        request: function(config) {
            return config;
        },
        requestError: function(rejection) {
            return rejection;
        },
        response: function(response) {
            return response;
        },
        responseError: function(rejection) {
            return rejection;
        }
    };

    return interceptor;
});