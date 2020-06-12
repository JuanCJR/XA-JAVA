"use strict";

var app = angular.module('app');

/**
 * Provides utility methods.
 *
 * @author Robin Rizvi
 * @since (2014-07-16.19:50:23)
 */
app.factory('Utils', function($http, $log, $window, Constants, StringUtils,
	CollectionUtils) {
    var Utils = {
        /**
         * Holds the program control data received from the WebService.
         */
        programControlData: null,

        /**
         * Activates polyfills and extension methods.
         */
        activatePolyFills: function() {
            // String polyfills and extension methods
            StringUtils.enableExtensionMethods();
            // Array polyfills and extensions methods
            CollectionUtils.enableExtensionMethods();
        },

        /**
         * Copies objS to objD if they're having compatible members.
         *
         * @param {Object} objD Destination Object
         * @param {Object} objS Source Object
         * @returns {Object} assigned object
         */
        assignObject: function(objD, objS) {
            if (Array.isArray(objD) || Array.isArray(objS)) {
                return;
            }

            var objDFields = Object.keys(objD);
            var objSFields = Object.keys(objS);
            $.extend(true, objD, objS);

            for (var i = 0; i < objSFields.length; i++) {
                var key = objSFields[i];

                if (objDFields.indexOf(key) == -1)
                    delete objD[key];
            }

            return objD;
        },

        /**
         * Clears the object.
         *
         * @param {Object} obj
         * @returns {Object} cleared object
         */
        clearObject: function(obj) {
            for (var key in obj) {
                obj[key] = null;
            }

            return obj;
        },

        /**
         * Gets Boolean value for a String value.
         *
         * @param {val} String value
         * @returns {Boolean} Boolean value
         */
        getBoolVal: function(val) {
    		return ((val == '1') || (val == '"1"') ||
    				(val.toLowerCase() == 'true'));
    	},

        /**
         * Returns the browser.
         */
		getBrowser: function() {
			var userAgent = $window.navigator.userAgent;
			var browsers = {
				chrome: /chrome/i,
				safari: /safari/i,
				firefox: /firefox/i,
				ie: /mozilla/i
			};

			for (var key in browsers) {
				if (browsers[key].test(userAgent)) {
					return key;
				}
			};

			return 'unknown';
		},

        /**
         * Return the long name of function suffix.
         *
         * @param {String} funName
         * @returns {String} long name of function
         */
        getLongFunSuffix: function(funName) {
            var prefNames = {
                "01D": "EntryPanel",
                "01G": "Grid",
                "02D": "Panel"
            };

            if ((funName == null) || (funName.length == 0)) {
                return funName;
            }

            var suffix = funName.substring(funName.length - 3);
            var suffixVal;

            if ((suffixVal = prefNames[suffix.toUpperCase()]) == null) {
                var suffix1 = parseInt(suffix.substring(0, 2));
                var suffix2 = suffix.substring(2, 3);

                if (("G" == suffix2) && (suffix1 > 1)) {
                    suffix = "01" + suffix2;
                }
                else if (("D" == suffix2) && (suffix1 > 2)) {
                    suffix = "02" + suffix2;
                }

                suffixVal = prefNames[suffix.toUpperCase()];

                if (suffixVal == null) {
                    return funName;
                }

                suffixVal = suffixVal + suffix1;
            }

            return suffixVal;
        },

        /**
         * Returns the long name of method.
         *
         * @param {String} funName
         * @param {String} pgmName
         * @returns {String} long name of method
         */
        getLongMethodName: function(funName, pgmName) {
            funName = funName.toUpperCase();

            var methodName = funName.toLowerCase();

            if (funName.startsWith(pgmName) &&
                    (
                            funName.endsWith("L") || funName.endsWith("D") ||
                            funName.endsWith("V") || funName.endsWith("P") ||
                            funName.endsWith("G") || funName.endsWith("X")
                    )) {
                // TRFFDC05D_AV, TRFFDC05D_BV in TRFFDC in CNVXA
                var indxU =
                	(pgmName.indexOf('_') == -1) ? funName.indexOf('_') : (-1);
                var indx = (indxU != -1) ? indxU : (funName.length - 1);
                var fun = funName.substring(0, indx);
                var lastChar = funName[funName.length - 1];
                var suffix = this.getLongFunSuffix(fun);

                if (indxU != -1) {
                    // TRFFDC05D_AV, TRFFDC05D_BV in TRFFDC in CNVXA
                    suffix += funName[indxU + 1];
                }

                if (suffix != fun) {
                    var ch = suffix[0];
                    methodName = ch.toLowerCase() + suffix.substring(1);

                    if ((lastChar == 'L') || (lastChar == 'D')) {
                        methodName += "Display";
                    }
                    else if ((lastChar == 'V') || (lastChar == 'P')) {
                        methodName += "Process";
                    }
                    else if (lastChar == 'G') {
                        methodName += "Initialize";
                    }
                    else {
                        // X
                        methodName += "Exit";
                    }
                }
            }

            return methodName;
        },

        /**
         * Returns the program name from screen name.
         *
         * @param {String} screen screen name
         * @returns {String} program name
         */
        getProgramName: function(screen) {
            var pgm = null;

            if (screen.endsWith("Grid")) {
                pgm = screen.substring(0, screen.length - 4);
            }
            else if (screen.endsWith("EntryPanel")) {
                pgm = screen.substring(0, screen.length - 10);
            }
            else if (screen.indexOf("Panel") != -1) {
                pgm = screen.substring(0, screen.lastIndexOf("Panel"));
            }
            else if (screen.length > 0) {
                pgm = screen;
            }

            return pgm;
        },

    	/**
    	 * Checks if String is blank.
    	 *
    	 * @param {String} str String
    	 * @returns {Boolean} true if String is blank
    	 */
        isBlanks: function(str) {
    		return (str == null) || (str.valueOf().trim().length == 0);
    	},

        /**
         * Performs WebService call to load program control data.
         */
        loadProgramControlData: function() {
            var urlPrefix = Constants.UrlPrefix();
            function loadControlData() {
            	var url = urlPrefix + 'application/programControlData';
                return $http.get(url).then(function(res) {
                    Utils.programControlData = res.data;
                    $log.info('Program Control Data loaded.');
                });
            }

            if (this.programControlData == null) {
                if (typeof urlPrefix != "string") {
                    return urlPrefix.then(loadControlData);
                }
                else {
                    return loadControlData();
                }
            }
        },

        /**
         * Emits XREDO mark in the logs.
         */
        markXREDO: function() {
            var XREDOmark = '\n' +
	            'â–ˆâ–ˆâ•—  â–ˆâ–ˆâ•—      â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•— â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•—â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•—  â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•— \n' +
	            'â•šâ–ˆâ–ˆâ•—â–ˆâ–ˆâ•”â•      â–ˆâ–ˆâ•”â•â•â–ˆâ–ˆâ•—â–ˆâ–ˆâ•”â•â•â•â•â•â–ˆâ–ˆâ•”â•â•â–ˆâ–ˆâ•—â–ˆâ–ˆâ•”â•â•â•â–ˆâ–ˆâ•—\n' +
	            ' â•šâ–ˆâ–ˆâ–ˆâ•”â• â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•—â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•”â•â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•—  â–ˆâ–ˆâ•‘  â–ˆâ–ˆâ•‘â–ˆâ–ˆâ•‘   â–ˆâ–ˆâ•‘\n' +
	            ' â–ˆâ–ˆâ•”â–ˆâ–ˆâ•— â•šâ•â•â•â•â•â–ˆâ–ˆâ•”â•â•â–ˆâ–ˆâ•—â–ˆâ–ˆâ•”â•â•â•  â–ˆâ–ˆâ•‘  â–ˆâ–ˆâ•‘â–ˆâ–ˆâ•‘   â–ˆâ–ˆâ•‘\n' +
	            'â–ˆâ–ˆâ•”â• â–ˆâ–ˆâ•—      â–ˆâ–ˆâ•‘  â–ˆâ–ˆâ•‘â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•—â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•”â•â•šâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ•”â•\n' +
	            'â•šâ•â•  â•šâ•â•      â•šâ•â•  â•šâ•â•â•šâ•â•â•â•â•â•â•â•šâ•â•â•â•â•â•  â•šâ•â•â•â•â•â• \n\n\n';
            $log.log(XREDOmark);
        },

        /**
         * Performs WebService call to open PDF report.
         */
		openPdfReport: function() {
            var urlPrefix = Constants.UrlPrefix();
        	var url = urlPrefix + 'application/checkPdfReport';
			$http.get(url).then(function() {
				$window.open(urlPrefix + 'application/pdfReport', '_blank', '');
			});
		},

        /**
         * Trims value of objects.
         *
         * @param {Object} obj
         * @returns {Object} trimmed object
         */
        trimAll: function(obj) {
            if ((obj == null) || (obj == undefined)) {
                return obj;
            }

            try {
                for (var objfld in obj) {
                    if (typeof(obj[objfld]) == "string") {
                        obj[objfld] = obj[objfld].trim();
                    }
                }
            }
            catch (e) {
                $log.error(e);
            }

            return obj;
        }
    };

    return Utils;
});