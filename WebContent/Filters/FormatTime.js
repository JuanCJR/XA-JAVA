"use strict";

var app = angular.module('app');

/**
 * Provides the filter for formatting time which is of non-Date javascript type
 * on Grid.
 *
 * @author Amit Arya
 * @since (2014-12-08.15:35:23)
 */
app.filter('formatTime', function($filter, DateTimeConverter) {
	return function(input, length) {
		return $filter('date')(DateTimeConverter.formatTime(input, length));
    };
});