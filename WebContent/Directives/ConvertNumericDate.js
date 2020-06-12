"use strict";

var app = angular.module('app');

/**
 * Provides the directive for formatting and parsing numeric dates.
 *
 * @author Amit Arya
 * @since (2014-08-12.12:57:23)
 */
app.directive('convertNumericDate', function(StringUtils) {
	return {
		restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {            
            /**
             * Converts the $viewValue into the $modelValue.
             */
    		ngModelCtrl.$parsers.push(function(viewValue) {
				if (typeof viewValue != "number") {
					return viewValue;
				}

	    		var modelFormat = attrs.modelformat;
				var fieldData = viewValue;

	            if ("CYYMMDD" == modelFormat) {
					var calendar = new Date();
					calendar.setTime(fieldData);

					var month =
						StringUtils.padStringWithValue(
							parseInt(calendar.getMonth()) + 1, "0", 2, true);
					var day =
						StringUtils.padStringWithValue(calendar.getDate(), "0",
							2, true);
					var year = calendar.getFullYear();
					var dateString = (year > 2000) ? "1" : "0";
					year = (year > 2000) ? (year - 2000) : (year - 1900);

					var yearStr =
						StringUtils.padStringWithValue(year, "0", 2, true);
					var digitStr = dateString + yearStr + month + day;

					return parseInt(digitStr);
	            }
	            else if ("YYMMDD" == modelFormat) {
					var calendar = new Date();
					calendar.setTime(fieldData);
					var month =
						StringUtils.padStringWithValue(
							parseInt(calendar.getMonth()) + 1, "0", 2, true);
					var day =
						StringUtils.padStringWithValue(calendar.getDate(), "0",
							2, true);
					var year =
						StringUtils.padStringWithValue(calendar.getYear(), "0",
							2, true);

					if (year.length > 2) {
						year = year.substring(2);
					}

					var digitStr = year + month + day;

					return parseInt(digitStr);
	            }
	            else if ("DDMMYY" == modelFormat) {
					var calendar = new Date();
					calendar.setTime(fieldData);

					var month =
						StringUtils.padStringWithValue(
							parseInt(calendar.getMonth()) + 1, "0", 2, true);
					var day =
						StringUtils.padStringWithValue(calendar.getDate(), "0",
							2, true);
					var year =
						StringUtils.padStringWithValue(calendar.getYear(), "0",
							2, true);

					if (year.length > 2) {
						year = year.substring(2);
					}

					var digitStr = day + month + year;

					return parseInt(digitStr);
	            }
	            else if ("YYYYMMDD" == modelFormat) {
					var calendar = new Date();
					calendar.setTime(fieldData);

					var yearStr = calendar.getFullYear();
					var month =
						StringUtils.padStringWithValue(
							parseInt(calendar.getMonth()) + 1, "0", 2, true);
					var day =
						StringUtils.padStringWithValue(calendar.getDate(), "0",
							2, true);

					var digitStr = yearStr + month + day;

					return parseInt(digitStr);
				}
	            else if ("DDMMYYYY" == modelFormat) {
					var calendar = new Date();
					calendar.setTime(fieldData);

					var yearStr = calendar.getFullYear();
					var month =
						StringUtils.padStringWithValue(
							parseInt(calendar.getMonth()) + 1, "0", 2, true);
					var day =
						StringUtils.padStringWithValue(calendar.getDate(), "0",
							2, true);
					var digitStr = day + month + yearStr;

					return parseInt(digitStr);
				}
    		});

            /**
             * Converts the $modelValue into the $viewValue.
             */
			ngModelCtrl.$formatters.push(function(modelValue) {
				if (modelValue == null) {
					return modelValue;
				}

				var modelFormat = attrs.modelformat;
				var viewFormat = attrs.viewformat.toUpperCase();
				var fieldData = modelValue.toString();

				if ("CYYMMDD" == modelFormat) {
					var digitStr =
						StringUtils.padStringWithValue(fieldData, "0", 7, true);

					var century = digitStr.substring(0, 1);
					var year = digitStr.substring(1, 3);
					var yearToAdd = ((century < 1) ? 1900 : 2000);
					year = parseInt(year) + parseInt(yearToAdd);

					var month = digitStr.substring(3, 5) - 1;
					var day = digitStr.substring(5, 7);

					var date = new Date();
					date.setDate(day);
					date.setMonth(month);
					date.setFullYear(year);

					var dt = moment(date).format(viewFormat);

					return dt;
				}
				else if ("YYMMDD" == modelFormat) {
					var digitStr =
						StringUtils.padStringWithValue(fieldData, "0", 6, true);

	                 var year = digitStr.substring(0, 2);
	                 var month = digitStr.substring(2, 4) - 1;
	                 var day = digitStr.substring(4).trim();

	                 var date = new Date();
	                 date.setDate(day);
	                 date.setMonth(month);
	                 date.setYear(year);

	                 var dt = moment(date).format(viewFormat);

	                 return dt;
				}
				else if ("DDMMYY" == modelFormat) {
					var digitStr =
						StringUtils.padStringWithValue(fieldData, "0", 6, true);

					var day = digitStr.substring(0, 2);
					var month = digitStr.substring(2, 4) - 1;
					var year = digitStr.substring(4).trim();

					var date = new Date();
					date.setDate(day);
					date.setMonth(month);
					date.setYear(year);

					var dt = moment(date).format(viewFormat);

					return dt;
				}
				else if ("MMDDYY" == modelFormat)
				{
					var digitStr =
						StringUtils.padStringWithValue(fieldData, "0", 6, true);

					var month = digitStr.substring(0, 2) - 1;
					var day = digitStr.substring(2, 4);
					var year = digitStr.substring(4).trim();

					var date = new Date();
					date.setDate(day);
					date.setMonth(month);
					date.setYear(year);

					var dt = moment(date).format(viewFormat);

					return dt;
				}
				else if ("YYYYMMDD" == modelFormat)
				{
					var digitStr =
						StringUtils.padStringWithValue(fieldData, "0", 8, true);

					var year = digitStr.substring(0, 4);
					var month = digitStr.substring(4, 6);
					var day = digitStr.substring(6).trim();

					var date = new Date();
					date.setDate(day);
					date.setMonth(month);
					date.setYear(year);

					var dt = moment(date).format(viewFormat);

					return dt;
				}
				else if ("DDMMYYYY" == modelFormat)
				{
					var digitStr =
						StringUtils.padStringWithValue(fieldData, "0", 8, true);

					var day = digitStr.substring(0, 2);
					var month = digitStr.substring(2, 4);
					var year = digitStr.substring(4).trim();

					var date = new Date();
					date.setDate(day);
					date.setMonth(month);
					date.setYear(year);

					var dt = moment(date).format(viewFormat);

					return dt;
				}
			});
        }
	};
});