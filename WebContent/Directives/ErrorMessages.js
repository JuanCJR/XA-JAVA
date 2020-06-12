"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since (2014-06-30.15:10:23)
 */
app.directive('errorMessages', function(UiUtils) {
    return {
        restrict: 'E',
        templateUrl: 'Views/ErrorMessages.html',
        replace: true,
        link: function() {
            UiUtils.setupProgramUI();
        }
    };
});