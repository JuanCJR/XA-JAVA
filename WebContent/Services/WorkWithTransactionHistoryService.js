"use strict";

var app = angular.module('app');

/**
 * Declares all the abstract methods or services that are exposed to the client.
 */
app.factory('WorkWithTransactionHistoryService', function($resource, Constants) {
	var prefix = Constants.UrlPrefix() + 'WorkWithTransactionHistory/';
	var endPoints = {
		initialize: $resource(prefix + 'initialize'),
		entryPanelDisplay: $resource(prefix + 'entryPanelDisplay'),
		entryPanelProcess: $resource(prefix + 'entryPanelProcess'),
		gridDisplay: $resource(prefix + 'gridDisplay'),
		gridProcess: $resource(prefix + 'gridProcess'),
		panelDisplay: $resource(prefix + 'panelDisplay'),
		panelProcess: $resource(prefix + 'panelProcess'),
		panel4Display: $resource(prefix + 'panel4Display'),
		panel4Process: $resource(prefix + 'panel4Process')
	};

	return endPoints;
});