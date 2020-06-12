"use strict";

var app = angular.module('app');

/**
 * Provides the directive for formatting and parsing numeric time.
 *
 * @author Amit Arya
 * @since (2014-08-12.13:29:23)
 */
app.directive('convertNumericTime', function(StringUtils, DateTimeConverter) {
	return {
		restrict: 'A',
        require: 'ngModel',
		link: function(scope, element, attrs, ngModelCtrl) {
            /**
             * Converts the $viewValue into the $modelValue.
             */
			ngModelCtrl.$parsers.push(function(viewValue) {
                var param = viewValue;

				if ((param == null) || (param.trim().length == 0)) {
					return null;
				}

				param = param.trim();

				var hr_mi_se = param.split(":");
				var str = "";

				for (var i = 0; i < hr_mi_se.length; i++) {
					str +=
						StringUtils.padStringWithValue(hr_mi_se[i].trim(), "0",
							2, true);
				}

				return parseInt(str);
			});

            /**
             * Converts the $modelValue into the $viewValue.
             */
			ngModelCtrl.$formatters.push(function(modelValue) {
				var fieldData = modelValue.toString();
                var maxlength = attrs.maxlength;
            	var length = (parseInt(maxlength) == 5) ? 4 : 6;

				return DateTimeConverter.formatTime(fieldData, length);
			});
		}
    };
});