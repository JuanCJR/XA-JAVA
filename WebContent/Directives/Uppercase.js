"use strict";

var app = angular.module('app');

/**
 * Directive to force uppercase in textbox.
 *
 * @author Rajat Nigam
 * @since 5/29/14 1:07 PM
 */
app.directive('uppercase', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ngModel) {
            function parse(viewValue) {
                viewValue = (viewValue || '').toUpperCase();

                if (ngModel.$viewValue != viewValue) {
                    ngModel.$setViewValue(viewValue);
                    ngModel.$render();
                }

                return viewValue;
            }

            ngModel.$parsers.push(parse);
        }
    };
});