"use strict";

var app = angular.module('app');

/**
 * Provides the directive for formatting dates.
 *
 * @author Amit Arya
 * @since (2014-08-06.12:49:23)
 */
app.directive('convertDate', function(StringUtils, DateTimeConverter) {
    return {
        // restrict as attribute
        restrict: 'A',
        require: 'ngModel',

        link: function(scope, element, attrs, ngModelCtrl) {
            /**
             * Converts the $viewValue into the $modelValue.
             */
            ngModelCtrl.$parsers.push(function(viewValue) {
            	var data = viewValue;
				var modelFormat = attrs.modelformat;
				var viewFormat = attrs.viewformat;

				// Numeric dates
				if ((data.indexOf('/') == -1) &&
						(viewFormat.indexOf('/') != -1)) {
					viewFormat = StringUtils.deleteString(viewFormat, "/");
				}
				else if ((data.indexOf('-') == -1) &&
						(viewFormat.indexOf('-') != -1)) {
					viewFormat = StringUtils.deleteString(viewFormat, "-");
				}

                return DateTimeConverter.formatDate(data, viewFormat,
                		modelFormat);
            });

            /**
             * Converts the $modelValue into the $viewValue.
             */
            ngModelCtrl.$formatters.push(function(modelValue) {
				if ((modelValue == null) || (modelValue == "")) {
					return modelValue;
				}

            	var data = modelValue;

				if (typeof modelValue == "object") {
					data = modelValue.date;
				}

            	var modelFormat = attrs.modelformat;
            	var viewFormat = attrs.viewformat;

        		if ((data == "0001-01-01") || (data == "00010101") ||
        				(data == "1010101") || (data == "010101")) {
        			return "";
        		}

                return DateTimeConverter.formatDate(data, modelFormat,
                		viewFormat);
            });
        }
    };
});