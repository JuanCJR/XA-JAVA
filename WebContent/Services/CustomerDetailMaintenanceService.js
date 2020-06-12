"use strict";

var app = angular.module('app');

/**
 * Declares all the abstract methods or services that are exposed to the client.
 */
app.factory('CustomerDetailMaintenanceService', function($resource, Constants) {
	var prefix = Constants.UrlPrefix() + 'CustomerDetailMaintenance/';
	var endPoints = {
		initialize: $resource(prefix + 'initialize'),
		entryPanelDisplay: $resource(prefix + 'entryPanelDisplay'),
		entryPanelProcess: $resource(prefix + 'entryPanelProcess'),
		panelDisplay: $resource(prefix + 'panelDisplay'),
		panelProcess: $resource(prefix + 'panelProcess')
	};

	return endPoints;
});