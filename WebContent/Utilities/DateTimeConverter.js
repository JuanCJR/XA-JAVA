"use strict";

var app = angular.module('app');

/**
 * Provides methods for conversion of date and time string from one format to
 * the other, adding and removing delimiters and checking for valid date and
 * time formats.
 *
 * @author Amit Arya
 * @since (2014-08-12.14:42:23)
 */
app.factory('DateTimeConverter', function(StringUtils) {
	var DateTimeConverter = {
		/**
		 * Formats the fieldData in inFormat to output format.
		 *
		 * @param {String} fieldData data of field
		 * @param {String} inFormat input format pattern
		 * @param {String} outFormat output format pattern
		 * @return {String} formatted field's data
		 */
		formatDate: function(fieldData, inFormat, outFormat) {
			// If fieldData is null then simply return the fieldData
			if ((fieldData == null) || fieldData == "") {
				return "";
			}

			// If inFormat is null then simply return the fieldData
			if ((inFormat == null) || inFormat == "") {
				return fieldData;
			}

			// If outFormat is null then simply return the fieldData
			if ((outFormat == null) || (outFormat == "")) {
				return fieldData;
			}

			// Check if inFormat & outFormat are same
			if (inFormat == outFormat) {
				return fieldData;
			}

			fieldData = fieldData.toString();

			if ((fieldData == "0") &&
					((inFormat == "yyyyMMdd") || (inFormat == "yyMMdd"))) {
				return "";
			}

			var inFormatLen = inFormat.length;

			if (inFormat.substring(0, 1) == "C") {
				inFormatLen += 1;
			}

			// If length of fieldData is less than input format, pad those many
			// zeros in the beginning of fieldData
			fieldData = StringUtils.padStringWithValue(fieldData, "0",
					inFormatLen, true);

			// CASE I :
			// C used in inFormat
			// Convert the data containing Century to Year
			fieldData = this.fromCentury(fieldData, inFormat);

			if (inFormat.substring(0, 1) == "C") {
				inFormat = "yy" + inFormat.substring(1);
			}

			var outFormatRecv = outFormat;

			if (outFormat.substring(0, 1).equalsIgnoreCase("C")) {
				outFormat = "yy" + outFormat.substring(1);
			}

			// Using Momentjs to apply date pattern
			var date = moment(fieldData, inFormat.toUpperCase());

			//if (date == null) {
			if ((date == null) || !date.isValid()) {
				return fieldData;
			}

			var dt = date.format(outFormat.toUpperCase());

			// CASE II :
			// C used in outFormat
			// Convert the data containing Year to Century
			var retStr = this.toCentury(dt.toString(), outFormatRecv);

			return retStr;
		},

		/**
		 * Formats the time.
		 *
		 * @param {String} fieldData data of field
		 * @param {Integer} fieldLength input format length
		 * @return {String} formatted field's data
		 */
		formatTime: function(fieldData, fieldLength) {
			// If fieldData is null then simply return the fieldData
			if ((fieldData == null) || fieldData == "") {
				return "";
			}

			fieldData = fieldData.toString();

			var digitStr =
				StringUtils.padStringWithValue(fieldData, "0", fieldLength,
					true);

			var len = Math.floor(fieldLength / 2);
			var hr_mi_se;
			hr_mi_se = [];
			var start_pos = 0;
			var end_pos = 0;

			for (var i = 0; i < len; i++) {
				start_pos = end_pos;
				end_pos = start_pos + 2;

				if (end_pos > fieldLength) {
					end_pos = fieldLength;
				}

				hr_mi_se[i] = digitStr.substring(start_pos, end_pos);
			}

			return hr_mi_se.join(":");
		},

		/**
		 * Converts the data containing Century to Year.
		 *
		 * @param {String} fieldData data of field to be converted
		 * @param {String} inFormat the pattern received for describing the in
		 *        format
		 * @return {String} converted field data
		 */
		fromCentury: function(fieldData, inFormat) {
			// if fieldData is null then simply return the fieldData
			if ((fieldData == null) || (fieldData == "")) {
				return fieldData;
			}

			// If inFormat is null then simply return the fieldData
			if ((inFormat == null) || (inFormat == "")) {
				return fieldData;
			}

			// CASE I :
			// C used in inFormat

			// Assumption :	C is always the first character

			// Make proper fieldData based on inFormat since
			// SimpleDateFormat does not support C

			// The below order is important
			if (!inFormat.substring(0, 2).equalsIgnoreCase("CC") &&
					inFormat.substring(0, 1).equalsIgnoreCase("C")) {
				// CYYMMDD - Can be anything after C
				var tempStr = fieldData.substring(0, 2);

				fieldData = (parseInt(tempStr) + 19) + fieldData.substring(2);
			}

			return fieldData;
		},

		/**
		 * Converts the data containing Year to Century.
		 *
		 * @param {String} fieldData data of field
		 * @param {String} outFormatRecv the pattern received for describing the
		 *        out format
		 * @return {String} string after converting it to Century
		 */
		toCentury: function(fieldData, outFormatRecv) {
			// If fieldData is null then simply return the fieldData
			if ((fieldData == null) || (fieldData == "")) {
				return fieldData;
			}

			// If outFormatRecv is null then simply return the fieldData
			if ((outFormatRecv == null) || (outFormatRecv == "")) {
				return fieldData;
			}

			// CASE II :
			// C used in outFormat

			// Assumption :	C is always the first character

			// Make proper fieldData based on outFormat since
			// SimpleDateFormat does not support C

			// The below order is important
			if (!outFormatRecv.substring(0, 2).equalsIgnoreCase("CC") &&
					outFormatRecv.substring(0, 1).equalsIgnoreCase("C")) {
				// CYYMMDD - Can be anything after C
				var tempStr = fieldData.substring(0, 2);

				fieldData = (parseInt(tempStr) - 19) + fieldData.substring(2);
			}

			return fieldData;
		}
    };

    return DateTimeConverter;
});