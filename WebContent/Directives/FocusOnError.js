"use strict";

var app = angular.module('app');

/**
 * @author Robin Rizvi
 * @since 5/2/14 2:27 PM
 */
app.directive('focusOnError', function() {
    return {
        restrict: 'A',
        link: function(scope, element) {
            scope.$watch('flatPanel.$error.businesslogic', function(businessLogicError) {
                if (businessLogicError != undefined && businessLogicError != null)
                {
                    var field = businessLogicError[0];
                    if (field != undefined)
                    {
                        scope.$evalAsync(function(){
                            element.find('#' + field.$name).focus();
                        });
                    }
                }
            }, true);
        }
    };
});