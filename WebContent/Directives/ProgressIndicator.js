"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since 3/3/14 1:07 PM
 */
app.directive('progressIndicator', function() {
    return {
        restrict: 'E',
        template: '<div ng-cloak ng-show="!(requestInProgress.$resolved) && !(hideProgressIndicator)"><div class="loadIndicator"></div><div class="ajaxloadergif"><div style="text-align:center"><img src="Assets/Images/ajax-loader-12.gif" /></div><div style="text-align:center">{{ progressMessage }}</div></div></div>',
        replace: true
    };
});