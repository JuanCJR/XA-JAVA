"use strict";

var app = angular.module('app');

/**
 * AngularJS browser autofill workaround by using a directive with relay method
 * calls.
 * When submitting a form in AngularJS and use the browser remember password
 * functionality, and in a subsequent login attempt you let the browser fill in
 * the login form with the username and password, the $scope model won't be
 * changed based on the autofill.
 *
 * @author Robin Rizvi
 * @since (2014-05-02.14:27:23)
 */
app.directive('autoFill', function() {
    return {
        restrict: 'A',
        link: function(scope, element) {
            scope.submit = function() {
                scope.username = element.find("#userid").val();
                scope.password = element.find("#password").val();
                scope.login();
            };
        }
    };
});