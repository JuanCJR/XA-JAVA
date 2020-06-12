"use strict";

var app = angular.module('app');

/**
 * HTML directive for Checkbox & Radio buttons styling based on JQuery.iCheck
 * plugin.
 *
 * @author Amit Arya
 * @since (2014-10-13.14:50:23)
 */
app.directive('icheck', function($timeout) {
	return {
        require: 'ngModel',
        link: function($scope, element, $attrs, ngModel) {
            return $timeout(function() {
                var value = $attrs['value'];

                $scope.$watch($attrs['ngModel'], function(newValue) {
                    $(element).icheck('update');
                });

				if (element.is(':visible')) {
					return $(element).icheck({
						checkboxClass: 'icheckbox_minimal',
						radioClass: 'iradio_minimal'
					}).on('ifChanged', function(event) {
						if ($attrs['ngModel']) {
							var elemType = $(element).attr('type');

							if (elemType === 'checkbox') {
								$scope.$apply(function() {
									return ngModel.$setViewValue(event.target.checked);
								});
							}
							else if (elemType === 'radio') {
								return $scope.$apply(function() {
									return ngModel.$setViewValue(value);
								});
							}
						}
					});
				}
				else {
					$(element).icheck('destroy');
				}
			});
		}
	};
});