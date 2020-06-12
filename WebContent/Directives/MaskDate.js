"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since 5/2/14 6:02 PM
 */
app.directive('maskDate', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            ngModelCtrl.$parsers.push(function(viewValue) {
                viewValue = viewValue.replace(/[^\d|^/|^-]/g, '');

                if (ngModelCtrl.$viewValue != viewValue) {
                    ngModelCtrl.$setViewValue(viewValue);
                    ngModelCtrl.$render();
                }

                return viewValue;
            });
        }
    };
});