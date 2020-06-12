"use strict";

var app = angular.module('app');

/**
 * HTML directive for datepicker widget.
 *
 * @author Robin Rizvi
 * @since (2014-10-07.14:30:23)
 */
app.directive('datetimePicker', function($log) {
	return {
		restrict : 'A',
		link : function(scope, elem, attrs) {
			// Watch for changes to model or its attribute. Then, call function
			// specified in the second argument whenever the expression
			// evaluation returns a different value
			scope.$watch(function() {
				return scope.$eval(attrs.ngModel) + '|' +
					scope.$eval(attrs.ngReadonly);
			}, function() {
				if (elem.data("DateTimePicker")) {
					elem.data("DateTimePicker").destroy();
				}

				if (!scope.$eval(attrs.ngReadonly)) {
					elem.datetimepicker({
						format : attrs.dateFormat.toUpperCase(),
						pickTime: false
					});

					elem.on("dp.error",function (e) {
						$log.error('DatePicker Error:');
						$log.info(e);
					});
				}
			});
		}
	};
});