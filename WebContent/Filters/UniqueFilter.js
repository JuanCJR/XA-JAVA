"use strict";

var app = angular.module('app');

/**
 * Returns unique/distinct items from an array. Used to filter our duplicate
 * error messages having same text.
 *
 * @author Robin Rizvi
 * @since (2014-05-06.12:48:23)
 */
app.filter('unique', function() {
    return function(collection) {
    	var output;
        output = [];

        if ((collection != null) && (collection.length != 0)) {
            collection.forEach(function(item) {
                if (output.indexOf(item) === -1) {
                    output.push(item);
                }
            });
        }

        return output;
    };
});