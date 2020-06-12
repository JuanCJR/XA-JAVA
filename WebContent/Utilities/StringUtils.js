"use strict";

var app = angular.module('app');

/**
 * Provides methods for dealing with String related operations.
 *
 * @author Robin Rizvi
 * @since (2014-07-02.13:46:23)
 */
app.factory('StringUtils', function() {
	var StringUtils = {
		/**
		 * Checks if the source string ends with the specified string.
		 *
		 * @param {String} source source string
		 * @param {String} searchString string to search
		 * @param {Number} position position to start searching from
		 * @returns {Boolean}
		 */
		endsWith: function(source, searchString, position) {
			position = position || source.length;
			position = position - searchString.length;
			var lastIndex = source.lastIndexOf(searchString);

			return ((lastIndex !== -1) && (lastIndex === position));
		},

		/**
		 * Compares two objects.
		 *
		 * @param {Object} tgt target object
		 * @param {Object} src source object
		 * @returns {Boolean}
		 */
		equal: function(tgt, src) {
			if ((tgt == null) || (src == null)) {
				return false;
			}

			var tgtStr = tgt.valueOf();
			var srcStr = src.valueOf();
			tgtStr = tgtStr.trim().toLowerCase();
			srcStr = srcStr.trim().toLowerCase();

			if (((srcStr == "1")  && (tgtStr == "true")) ||
					((tgtStr == "1") && (srcStr == "true")) ||
					((srcStr == "0") && (tgtStr == "false")) ||
					((tgtStr == "0") && (srcStr == "false"))) {
				return true;
			}

			return (srcStr == tgtStr);
		},

		/**
		 * Checks whether the String contains only upper case characters.
		 *
		 * @param {String} str source string
		 * @returns {Boolean} <tt>true</tt> if the string contains only upper
		 *         case letters
		 */
		isAllUpperCase: function(str) {
			return (str === str.toUpperCase());
		},

		/**
		 * Trims leading white spaces.
		 *
		 * @param {String} strToLTrim string array to trim
		 * @return {String} string with trimmed white spaces
		 */
		ltrim: function(strToLTrim) {
			return strToLTrim.replace(/^\s+/g, '');
		},

		/**
		 * If length of strSource is less than final length parameter passed in,
		 * pad those many string to pad in the beginning / end of strSource.
		 *
		 * @param {String} strSource source string
		 * @param {String} strToPad string to pad
		 * @param {Number} finalLen final length of the string
		 * @param {Boolean} bBegin whether to pad in the beginning or end
		 * @return {String} string with padded string
		 */
		padStringWithValue: function(strSource, strToPad, finalLen, bBegin) {
			// If strSource is null then make it a empty string
			if (strSource == null) {
				strSource = "";
			}

			// If strToPad is null then make it a empty string
			if (strToPad == null) {
				strToPad = "";
			}

			// If length of strSource is less than finalLen, pad those many
			// strToPad's in the beginning / end of strSource depending on
			// bBegin
			strSource = this.rtrim(strSource);

			var strToReturn = strSource;
			var j = finalLen - strSource.length;

			for (var i = 0; i < j; i++) {
				if (bBegin) {
					strToReturn = strToPad + strToReturn;
				}
				else {
					strToReturn += strToPad;
				}
			} // i

			return strToReturn;
		},

		/**
		 * Checks if the source string starts with the specified string.
		 *
		 * @param {String} source source string
		 * @param {String} searchString string to search
		 * @param {Number} position position to start searching from
		 * @returns {Boolean} 
		 */
		startsWith: function(source, searchString, position) {
			position = position || 0;

			return source.indexOf(searchString, position) === position;
		},

		/**
		 * Sets the value of indicator at the specified position in the string.
		 *
		 * @param {String} str source string
		 * @param {Number} index position to set the indicator
		 * @param {String} indicator value to be set
		 * @returns {String} source string with the indicator set
		 */
		setIndicator: function(str, index, indicator) {
			return str.setCharAt(index - 1, indicator);
		},

		/**
		 * Replaces the specified suffix in a given source string if this string
		 * ends with it.
		 *
		 * @param {String} strSource source string
		 * @param {String} strToReplace string to replace
		 * @param {String} strToReplaceWith string to replace with
		 * @returns {String} source string with string replaced with new string
		 */
		replaceEndString: function(strSource, strToReplace, strToReplaceWith) {
			// If the string source is null then simply return the strSource
			var len = strSource.length;

			if (len == 0) {
				return strSource;
			}

			// If the string to replace is null then simply return the strSource
			if (strToReplace == null || strToReplace == "") {
				return strSource;
			}

			// If the string to replace with is null then simply return the
			// strSource Or if strToReplaceWith is same as strToReplace then
			// simply return the strSource
			if (strToReplaceWith == null || strToReplace === strToReplaceWith) {
				return strSource;
			}

			if (!strSource.endsWith(strToReplace)) {
				return strSource;
			}

			return strSource.substring(0, len - strToReplace.length) +
				strToReplaceWith;
		},

		/**
		 * Replaces all instances of string to replace with new string in a
		 * given source string.
		 *
		 * @param {String} strSource source string
		 * @param {String} strToReplace string to replace
		 * @param {String} strToReplaceWith string to replace with
		 * @returns {String} source string with characters replaced with the
		 *         characters in new string
		 */
		replaceString: function(strSource, strToReplace, strToReplaceWith) {
			// If strSource is null then simply return false
			var len = strSource.length;

			if (len == 0) {
				return strSource;
			}

			if ((strToReplace == null) || (strToReplaceWith == null)) {
				return strSource;
			}

			var strToReturn = "";
			var chArrSource = strSource.split('');

			for (var i = 0; i < len; i++) {
				var indx = chArrSource[i];

				if (indx == -1) {
					strToReturn += chArrSource[i];
				}
				else {
					strToReturn += strToReplaceWith;
				}
			} // i

			return strToReturn;
		},

		/**
		 * Trims trailing white spaces.
		 *
		 * @param {String} strToRTrim source string
		 * @returns {String} string with trimmed white spaces
		 */
		rtrim: function(strToRTrim) {
			if (typeof strToRTrim != "string") {
				return strToRTrim;
			}

			return strToRTrim.replace(/\s+$/g, '');
		},

		/**
		 * Converts the string argument to lowercase.
		 *
		 * @param {String} str source string
		 * @returns {String} the String in lowercase
 		 */
		toLowerCaseIfAllUpper: function(str) {
			if ((str == null) || (str == "")) {
				return str;
			}

			if (this.isAllUpperCase(str)) {
				return str.toLowerCase();
			}
			else {
				return this.uncapitalize(str);
			}
		},

		/**
		 * Uncapitalizes the given string.
		 *
		 * @param {String} str
		 * @returns {String}
		 */
		uncapitalize: function(str) {
			if ((str == null) || (str == "")) {
				return str;
			}

			return str.charAt(0).toLowerCase() + str.substr(1);
		},

		/**
		 * Enables extension methods to String.
		 */
		enableExtensionMethods: function() {
			/*
			 * String.contains
			 */
			if (!String.prototype.contains) {
				String.prototype.contains = function() {
					return String.prototype.indexOf.apply(this, arguments) !== -1;
				};
			}

			/*
			 * String.endsWith
			 */
			if (!String.prototype.endsWith) {
				Object.defineProperty(String.prototype, 'endsWith', {
					enumerable: false,
					configurable: false,
					writable: false,
					value: function(searchString, position) {
						position = position || this.length;
						position = position - searchString.length;
						var lastIndex = this.lastIndexOf(searchString);

						return ((lastIndex !== -1) && (lastIndex === position));
					}
				});
			}

			/*
			 * String.equalsIgnoreCase
			 */
			if (!String.prototype.equalsIgnoreCase) {
				Object.defineProperty(String.prototype, 'equalsIgnoreCase', {
					enumerable: false,
					configurable: false,
					writable: false,
					value: function(string) {
						return (this.localeCompare(string, {}, {
							sensitivity: "base"
						}) == 0);
					}
				});
			}

			/*
			 * String.setCharAt
			 */
			if (!String.prototype.setCharAt) {
				String.prototype.setCharAt = function(index, ch) {
					if ((index < 0) || (index >= this.length)) {
						return this;
					}

					return this.substr(0, index) + ch + this.substr(index + 1);
				};
			}

			/*
			 * String.startsWith
			 */
			if (!String.prototype.startsWith) {
				Object.defineProperty(String.prototype, 'startsWith', {
					enumerable: false,
					configurable: false,
					writable: false,
					value: function(searchString, position) {
						position = position || 0;
						return this.indexOf(searchString, position) === position;
					}
				});
			}

			/*
			 * String.trim
			 */
			if (!String.prototype.trim) {
				String.prototype.trim = function() {
					return this.replace(/^\s+|\s+$/g, '');
				};
			}
		}
	};

	return StringUtils;
});