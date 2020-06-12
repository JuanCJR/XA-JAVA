"use strict";

var app = angular.module('app');

/**
 * HTML directive to display visual indicator on program UI for indicating the
 * progress of an asynchronous operation.
 *
 * @author Robin Rizvi
 * @since (2014-10-06.18:54:23)
 */
app.directive('asyncOperationIndicator', function() {
    return {
        restrict: 'E',
        link: function(scope, element) {
            scope.$watchCollection('currentOperation', function(currentOperation) {
                if ((currentOperation != null) && !currentOperation.$resolved) {
                    element.parent().append('<div class="overlay"></div><div class="loading-img"></div>');
                }
                else {
                    element.parent().find('.overlay, .loading-img').remove();
                }
            }, true);
        }
    };
});