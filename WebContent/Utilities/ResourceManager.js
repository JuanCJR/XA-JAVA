"use strict";

var app = angular.module('app');

/**
 * Manages the application resources (configuration and settings). 
 * Provides operations for loading and retrieval of values from resources.
 *
 * @author Robin Rizvi
 * @since (2014-07-03.17:18:23)
 */
app.factory('ResourceManager', function($http, $log, $q) {
	var ResourceManager = {
		/**
		 * Holds the resources that have been loaded into the application.
		 */
		resources: {},

		/**
		 * Checks the existence of a key in the resource.
		 *
		 * @param {String} key
		 * @param {String} resource
		 * @returns {Boolean}
		 */
		exists: function(key, resource) {
			if ((key != null) && (resource != null)) {
				if (this.resources[resource] != null) {
					return (this.resources[resource]).hasOwnProperty(key);
				}
				else {
					return this.loadResource(resource);
				}
			}
			else if (key != null) {
				var numResources = Object.keys(this.resources).length;

				if (numResources == 0) {
					return this.loadAllResources();
				}

				for (var resource in this.resources) {
					if ((this.resources[resource])[key] != null) {
						return true;
					}
				}
			}

			return false;
		},

		/**
		 * Gets the value of a particular key in a resource.
		 *
		 * @param {String} key
		 * @param {String} resource
		 * @param {Array} [params]
		 * @returns {String} Value
		 */
		get: function(key, resource, params) {
			if ((key != null) && (resource != null)) {
				if (this.resources[resource] != null &&
						(this.resources[resource]).hasOwnProperty(key)) {
					var res = (this.resources[resource])[key];

					if ((params != null) && Array.isArray(params)) {
						for (var i in params) {
							res = res.replace("{" + i + "}", params[i]);
						}
					}

					return res;
				}
				else {
					return this.loadResource(resource);
				}
			}
			else if (key != null) {
				var numResources = Object.keys(this.resources).length;

				if (numResources == 0) {
					return this.loadAllResources();
				}

				for (var resource in this.resources) {
					if ((this.resources[resource])[key] != null) {
						var res = (this.resources[resource])[key];

						if ((params != null) && Array.isArray(params)) {
							for (var i in params) {
								res = res.replace("{" + i + "}", params[i]);
							}
						}

						return res;
					}
				}
			}

			return null;
		},

		/**
		 * Get all keys present in a resource.
		 *
		 * @param {String} resource
		 * @returns {Array} Collection of keys in the resource
		 */
		getAllKeys: function(resource) {
			var keys = [];

			if (resource != null) {
				if (this.resources[resource] != null) {
					keys = Object.keys(this.resources[resource]);
				}
			}
			else {
				for (var resource in this.resources) {
					var resourceKeys = Object.keys(this.resources[resource]);

					for (var i = 0; i < resourceKeys.length; i++) {
						keys.push(resourceKeys[i]);
					}
				}
			}

			return keys;
		},

		/**
		 * Searches for a particular key in a resource file that has the 
		 * specified value.
		 *
		 * @param {String} value
		 * @param {String} resource
		 * @returns {String} The key property having the specified value
		 */
		getKey: function(value, resource) {
			var keys = this.getAllKeys(resource);

			for (var i = 0; i < keys.length; i++) {
				var key = keys[i];

				if (this.get(key, resource) === value) {
					return key;
				}
			}

			return value;
		},

		/**
		 * Gets the object representing the resource.
		 *
		 * @param {String} resource
		 * @returns {Object} Object containing resource keys and values
		 */
		getResource: function(resource) {
			if (resource != null) {
				return this.resources[resource];
			}
		},

		/**
		 * Performs aysnc requests to load all the resource files.
		 *
		 * @returns {Object} Aggregated promise for all requests
		 */
		loadAllResources: function() {
			var promises = [];

			if (this.resources["prefNames"] == null) {
				promises.push(this.loadResource('prefNames'));
			}

			if (this.resources["configuration"] == null) {
				promises.push(this.loadResource('configuration'));
			}

			if (this.resources["constants"] == null) {
				promises.push(this.loadResource('constants_en'));
			}

			if (this.resources["messages"] == null) {
				promises.push(this.loadResource('messages_en'));
			}

			return $q.all(promises);
		},

		/**
		 * Performs an async request to load the resource file.
		 *
		 * @param {String} resource
		 * @returns {Object} Promise of the request
		 */
		loadResource: function(resource) {
			return $http.get('Assets/Resources/' + resource + '.json').then((function(res) {
				var key = resource;

				if (resource.charAt(resource.length - 3) === '_') {
					key = resource.substring(0, resource.length - 3);
				}

				this.resources[key] = res.data;
				$log.info(resource + '.json loaded');
			}).bind(this));
		}
	};

	return ResourceManager;
});