"use strict";

var app = angular.module('app');

/**
 * Conversion of string from one format to the other.
 *
 * @author Amit Arya
 * @since (2014-08-13.17:48:12)
 */
app.directive('convertMask', function() {
    //var digits = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
    var mask = "";
    var type = "";

    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            /**
             * Converts the $viewValue into the $modelValue.
             */
            ngModelCtrl.$parsers.push(function(viewValue) {
                var param = StringUtils.rtrim(attrs.value);
                var paramLen = param.length;
                mask = attrs.viewformat;
                type = attrs.modelformat;

                if (paramLen == 0) {
                	if ((type == "P") || (type == "S")) {
                		return 0;
                	}

                	return param;
                }

                if ((paramLen < mask.length) &&
                		((type == "P") || (type == "S"))) {
                	mask = param.replace(/\d/gi, '#');
                }

                if (paramLen < mask.length) {
                	mask = mask.substr(0, paramLen);
                }

                var val = '';

                for (var i = 0; i < mask.length; i++) {
                	if (mask[i] == "#") {
                		val += param[i];
                	}  
                	else {
                		val += mask[i];
                	}
                }

            	val = val.replace(/[^\da-z]/gi, '');

                return val;
            });

            /**
             * Converts the $modelValue into the $viewValue.
             */
            ngModelCtrl.$formatters.push(function(modelValue) {
                var val = modelValue.toString().trim();                
                mask = attrs.viewformat;
                type = attrs.modelformat;

                if (val.length == 0) {
                	return val;
                }

                if (!isNaN(val) && (type != "A")) {
                	if (val == "0") {
                		return "";
                	}
                }

                var maskLen = mask.length;

                if (val.length < maskLen) {
                	val =
                		StringUtils.padStringWithValue(val, "0", maskLen, true);
                }
                else if (type == "A") {
                	val = StringUtils.replaceString(val, "", "");
                }

                var retVal = '';

                for (var i = 0; i < mask.length; i++) {
                	if (mask[i] == "#") {
                		retVal += val[i];
                	}      
                	else {
                		retVal += mask[i];
                	}
                }

            	retVal = retVal.replace(/[^\da-z]/gi, '');

                return retVal.toString();
            });
        }
    };
});