"use strict";

var app = angular.module('app');

/**
 * Declares all the abstract methods or services that are exposed to the client.
 */
app.factory('SalespersonsSelectionService', function($resource, Constants) {
	var prefix = Constants.UrlPrefix() + 'SalespersonsSelection/';
	var endPoints = {
		initialize: $resource(prefix + 'initialize'),
		entryPanelDisplay: $resource(prefix + 'entryPanelDisplay'),
		entryPanelProcess: $resource(prefix + 'entryPanelProcess'),
		gridDisplay: $resource(prefix + 'gridDisplay'),
		gridProcess: $resource(prefix + 'gridProcess')
	};

	return endPoints;
});