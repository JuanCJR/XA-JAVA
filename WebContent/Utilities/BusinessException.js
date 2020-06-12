"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since (2014-03-13.14:40:23)
 */
app.factory('BusinessException', function() {
    var BusinessException = function(errorField, errorMessage) {
        this.name = "BusinessException";
        this.message = errorMessage;
        this.errorField = errorField;
        this.errorMessage = errorMessage;
    };

    BusinessException.prototype = new Error();
    BusinessException.prototype.constructor = BusinessException;

    return BusinessException;
});