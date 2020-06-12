"use strict";

var app = angular.module('app');

/**
 * Provides the directive for formatting and parsing numbers.
 *
 * @author Amit Arya
 * @since (2014-07-09.12:59:23)
 */
app.directive('convertNumber', function() {
    return {
        // restrict as attribute
        restrict: 'A',
        require: 'ngModel',
        /**
         * The link method is mainly used for attaching event listeners to DOM
         * elements, watching model properties for changes, and updating the
         * DOM.
         *
         * @param {$rootScope.Scope} scope the scope that is tied to our HTML
         *        template
         * @param {Object} element the actual HTML DOM element
         * @param {Object} attrs the attributes of the original directive HTML
         * @param {ngModel.NgModelController} ngModelCtrl an instance of the
         *        required NgModelController
         */
        link: function(scope, element, attrs, ngModelCtrl) {

            /**
             * Converts the $viewValue into the $modelValue. Function added to
             * $parsers array is called as soon as the value in the form input
             * is modified by the user.
             */
            ngModelCtrl.$parsers.push(function(viewValue) {
                var modelFormat = attrs.modelformat;

            	if ((viewValue == null) || (modelFormat == null)) {
                    return viewValue;
                }

				viewValue = viewValue.trim();

                if (viewValue == "") {
                    return "0";
                }

				if (viewValue.endsWith("-")) {
					viewValue = "-" + viewValue.replace("-", "");
				}

				// Replace slash
				if (viewValue.indexOf('/') != -1) {
					viewValue = viewValue.replace("/", "");
				}

				if (isNaN(viewValue)) {
					viewValue = viewValue.replace(/[^\d]/g, '');
				}

                var parsedVal =
            		new DecimalFormat(modelFormat).formatBack(viewValue);

				if (ngModelCtrl.$viewValue != parsedVal) {
					ngModelCtrl.$setViewValue(parsedVal);
					ngModelCtrl.$render();
				}

                return parsedVal;
            });

            /**
             * Converts the $modelValue into the $viewValue.
             */
            ngModelCtrl.$formatters.push(function(modelValue) {
            	var viewFormat = attrs.viewformat;

            	if ((modelValue == null) || (viewFormat == null)) {
            		return "";
            	}

				modelValue = modelValue.toString().trim();

				// Blank out zero values on Position To row
                if (attrs.ngModel.startsWith("filterData") &&
                		(
                			(modelValue == "0") || (modelValue == "0.0") ||
                			(modelValue == "0.00")
                		)) {
                    return "";
                }

            	var formattedVal =
            		new DecimalFormat(viewFormat).format(modelValue);

                return formattedVal;
            });
        }
    };
});