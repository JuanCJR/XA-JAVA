"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since 4/24/14 5:06 PM
 */
app.directive('businessMessages', function(UiUtils) {
    return {
        restrict: 'E',
        templateUrl: 'Views/BusinessMessages.html',
        replace: true,
        link: function() {
            UiUtils.setupProgramUI();
        }
    };
});