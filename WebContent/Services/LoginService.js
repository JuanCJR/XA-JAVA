"use strict";

var app = angular.module('app');

/**
 * Declares the authStatus service exposed to the client.
 *
 * @author Robin Rizvi
 * @version (2014-05-06.16:53:56)
 */
app.factory('LoginService', function($resource, Constants) {
	var endPoints = {
		authStatus: $resource(Constants.UrlPrefix() + 'Login/authenticationStatus')
	};

	return endPoints;
});