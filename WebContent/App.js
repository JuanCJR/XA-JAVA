"use strict";

/* App Declaration with routes */
var appModule = angular.module('app', ['ngRoute', 'ngResource', 'ui.bootstrap', 'ui.keypress']);

appModule.config(
	function($controllerProvider, $httpProvider, $routeProvider) {
		// $controllerProvider to lazily load controllers
		appModule.controller = $controllerProvider.register;

		// Prevent the caching of GET requests in IE since when you send
		// requests via $http, IE caches and re-serves the result.

		// Initialize get if not there
		if (!$httpProvider.defaults.headers.get) {
			$httpProvider.defaults.headers.get = {};   
		}

		// Disable IE ajax request caching
		$httpProvider.defaults.headers.get["Cache-Control"] = "no-cache";
		$httpProvider.defaults.headers.get["If-Modified-Since"] = "0";

		$routeProvider.when(
			'/application',
			{
				templateUrl: 'Views/Login.html',
				controller: 'LoginController',
				resolve: {
					assets: function(ResourceManager) {
						return ResourceManager.loadAllResources();
					}
				}
			});

		$routeProvider.when(
			'/ScreenParams',
			{
				templateUrl: 'Views/ScreenParams.html',
				controller: 'ScreenParamsController',
				resolve: {
					assets: function(ResourceManager) {
						return ResourceManager.loadAllResources();
					}
				}
			});

		$routeProvider.when(
			'/:screen',
			{
				templateUrl: function(routeParams) {
					return 'Views/' + routeParams.screen + '.html';
				},
				controller: function($scope, $routeParams, $controller, Utils) {
					var pgm = Utils.getProgramName($routeParams.screen);
					var contr = pgm + 'Controller';

					$controller(contr, {$scope: $scope});
				},
				resolve: {
					// Lazily load controller
					controller: function($q, $route, Utils) {
						var deferred = $q.defer();

						// Use $route.current.params to access the new route's
						// parameters since $routeParams are only updated after
						// a route change completes successfully
						var pgm =
							Utils.getProgramName($route.current.params.screen);
						var contr = pgm + 'Controller';
						var contrFile = "Controllers/" + contr + ".js";

						head.load(contrFile, function() {
							// Resolve the promise when controller has been
							// lazily loaded
							deferred.resolve();
						});

						return deferred.promise;
					},

					// If /application route is not invoked e.g. for the xhtml
					// to html Workflow case
					assets: function(ResourceManager, Utils) {
						return ResourceManager.loadAllResources()
								.then(function() {
							return Utils.loadProgramControlData();
						});
					}
				},
				reloadOnSearch: false
			});

		$routeProvider.otherwise(
			{
				redirectTo: '/application'
			});

		//$httpProvider.interceptors.push('HttpInterceptor');
		//var interceptor = ['$q', function($q) {
		var interceptor = ['$q', '$injector', 'Params', 'StringUtils',
			function($q , $injector, Params, StringUtils) {
			function success(response) {
				return response;
			}

			function error(response) {
				var status = response.status;

				if (status === 404) {
					//window.location = "#/404";
					var url = response.config.url;
					var screen = url.substring(6, url.length);
					screen = screen.replace(/.html/g, '');

					if (sessionStorage) {
						var rootScope = $injector.get('$rootScope');

						if (rootScope.loginTime != null) {
							sessionStorage.setItem("loginTime",
								rootScope.loginTime);
							sessionStorage.setItem("userDesc",
								rootScope.userDescription);
							sessionStorage.setItem("userAuth",
								rootScope.userAuthenticated);
						}

						// Get CallStack via $injector because of circular
						// dependency problem
						var cs = $injector.get('CallStack');

						if (cs.callStack.length == 0) {
							// xhtml to xhtml Workflow
							cs =
								JSON.parse(sessionStorage.getItem("CallStack"));
						}

						if (cs.callStack.indexOf(screen) != -1) {
							cs.callStack.pop();
						}
						else {
							cs.callStack.push(screen);
						}

						if (cs.callStack.length != 0) {
							sessionStorage.setItem("CallStack",
								JSON.stringify(cs));
						}
					}

					var href =
						window.location.pathname + "faces/" + screen + ".xhtml";
					href += "?SCREEN_MODE=";

					if (Params["SCREEN_MODE"] != null) {
						href += Params["SCREEN_MODE"];
					}

					if (Params["parent"] != null) {
						href += "&parent=" + Params["parent"];
					}

					var valList = Params["PARAM_VALUES"];

					if ((valList != null) && (valList.length != 0)) {
						var numParams = valList.length;

						for (var i = 0; i < numParams; i++) {
							href += "&parm" + i + "=" +
								StringUtils.rtrim(valList[i]);
						}
					}

					var rtnValList = Params["rtnValList"];

					if ((rtnValList != null) && (rtnValList.length != 0)) {
						var numParams = rtnValList.length;

						for (var i = 0; i < numParams; i++) {
							href += "&rtn" + i + "=" +
								StringUtils.rtrim(rtnValList[i]);
						}
					}

					window.location = href;
				}
				else {
					return $q.reject(response);
				}
			}

			return function (promise) {
				return promise.then(success, error);
			};
		}];

		$httpProvider.responseInterceptors.push(interceptor);
	}
);

//sessionStorage API can also be used here for storing session level values
appModule.value("Params", {});

appModule.run(
	function(Utils, UiUtils) {
		Utils.activatePolyFills();
		Utils.markXREDO();
		UiUtils.setupUI();
	}
);