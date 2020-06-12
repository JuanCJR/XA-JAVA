"use strict";

var app = angular.module('app');

/**
 * Provides the filter for formatting date which is of non-Date javascript type
 * on Grid.
 *
 * @author Amit Arya
 * @since (2014-11-04.17:43:23)
 */
app.filter('formatDate', function($filter, DateTimeConverter) {
	return function(input, modelFormat, viewFormat) {
		return $filter('date')(DateTimeConverter.formatDate(input, modelFormat,
			viewFormat));
    };
});