package com.databorough.utils;

import static java.lang.Math.pow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.sql.Time;
import java.sql.Timestamp;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import static com.databorough.utils.DateTimeConverter.toCentury;
import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Provides the interface for formatting and parsing numbers.
 *
 * @author Amit Arya
 * @since (2001-02-13.09:49:55)
 */
public final class NumberFormatter
{
	/**
	 * The NumberFormatter class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private NumberFormatter()
	{
		super();
	}

	/**
	 * Formats the fieldData to outFormat.
	 *
	 * @param fieldData data of field to be formatted
	 * @param outFormat site format of Number
	 * @return String after formatting
	 * @since (2012-10-18.11:54:33)
	 */
	public static String formatNumber(Object fieldData, String outFormat)
	{
		DecimalFormat decFmt = new DecimalFormat();
		decFmt.applyLocalizedPattern(outFormat);

		return decFmt.format(fieldData);
	}

	/**
	 * Formats the fieldData in inFormat to outFormat.
	 *
	 * @param fieldData data of field to be formatted
	 * @param inFormat database format of Number
	 * @param outFormat site format of Number
	 * @return String after formatting
	 * @since (2001-02-13.12:54:33)
	 */
	public static String formatNumber(String fieldData, String inFormat,
		String outFormat)
	{
		String retStr;

		// if fieldData is null then simply return the fieldData
		if ((fieldData == null) || fieldData.trim().equals(""))
		{
			return fieldData;
		}

		// If inFormat is null then simply return the fieldData
		if ((inFormat == null) || inFormat.equals(""))
		{
			return fieldData;
		}

		// If outFormat is null then simply return the fieldData
		if ((outFormat == null) || outFormat.equals(""))
		{
			return fieldData;
		}

		try
		{
			// Check if inFormat & outFormat are same, and
			// fieldData is of same length as outFormat.
			if (inFormat.equals(outFormat) &&
					(
						(fieldData.length() == outFormat.length()) &&
						(
							(
								(fieldData.indexOf('.') == -1) &&
								(outFormat.indexOf('.') == -1)
							) ||
							(
								(fieldData.indexOf('.') != -1) &&
								(outFormat.indexOf('.') != -1)
							)
						)
					))
			{
				return fieldData;
			}

			String outFormatPositive;
			int indxSemiColon = outFormat.indexOf(';');
			int indxDecimal = outFormat.indexOf('.');

			if (indxSemiColon != -1)
			{
				if (indxDecimal != -1)
				{
					outFormatPositive = outFormat.substring(0, indxDecimal);
				}
				else
				{
					outFormatPositive = outFormat.substring(0, indxSemiColon);
				}
			}
			else
			{
				if (indxDecimal != -1)
				{
					outFormatPositive = outFormat.substring(0, indxDecimal);
				}
				else
				{
					outFormatPositive = outFormat;
				}
			}

			outFormatPositive =
				StringUtils.deleteString(outFormatPositive, ",");

			// Suppress 0 where decimal field is not present
			if (StringUtils.isAllLetter(outFormatPositive, '#') &&
					(indxDecimal == -1) && "0".equals(fieldData))
			{
				return "";
			}

			DecimalFormat inDf = new DecimalFormat();
			inDf.applyLocalizedPattern(inFormat);

			// Creating a Number object using the string value of the Number
			// field.
			Number number = inDf.parse(fieldData, new ParsePosition(0));

			if (number == null)
			{
				return (outFormatPositive.indexOf('0') != -1) ? "0" : "";
			}

			// A DecimalFormat is formed using outFormat pattern and the Number
			// object will be converted to outFormat format.
			DecimalFormat outDf = new DecimalFormat();
			outDf.applyLocalizedPattern(outFormat);

			retStr = outDf.format(number);
		}
		catch (Exception e)
		{
			logStackTrace(e);
			retStr = null;
		}

		return retStr;
	}

	private static String getValidStr(Object objToConvert)
	{
		String strToConvert = String.valueOf(objToConvert);

		if (objToConvert instanceof Number ||
				NumberUtils.isNumber(strToConvert.trim()))
		{
			return strToConvert.trim();
		}

		// Replace slash, colon, minus, space
		StringBuilder regDtRpl = new StringBuilder("[\\/\\:\\-\\ ]");

		if (objToConvert instanceof DateEx || objToConvert instanceof Date)
		{
			regDtRpl.insert(4, "\\.");
		}

		strToConvert = strToConvert.replaceAll(regDtRpl.toString(), "");

		return strToConvert;
	}

	public static Double hiDFloat(int size)
	{
		return hiLoDFloat(size, false);
	}

	public static Double hiDouble(int len, int dec)
	{
		return hiLoDouble(len, dec, false);
	}

	public static Double hiDouble(int len)
	{
		return hiDouble(len, 0);
	}

	public static Float hiFloat(int size)
	{
		return hiLoFloat(size, false);
	}

	public static Integer hiInt(int len)
	{
		return hiLoInt(len, false);
	}

	public static Double hiLoDFloat(int size, boolean isLoval)
	{
		Double hidfl = 0D;
		int exp = 38;

		if (size <= 4)
		{
			size = 8;
		}
		else
		{
			size = 16;
			exp = 308;
		}

		hidfl = (Double)(hiDouble(size, size - 1) * pow(10, exp));
		hidfl *= (isLoval ? (-1) : 1);

		return hidfl;
	}

	public static Double hiLoDouble(int len, int dec, boolean isLoval)
	{
		Double hidb = 0D;

		if (len > 308)
		{
			hidb = Double.MAX_VALUE;
		}
		else
		{
			for (int i = 0; i < (len - dec); i++)
			{
				hidb = (10 * hidb) + 9;
			}

			for (int i = 1; i <= dec; i++)
			{
				hidb = hidb + (9 * pow(10, -i));
			}
		}

		hidb *= (isLoval ? (-1) : 1);

		return hidb;
	}

	public static Float hiLoFloat(int size, boolean isLoval)
	{
		Float hifl = 0F;
		int exp = 38;

		if (size <= 4)
		{
			size = 8;
		}
		else
		{
			size = 16;
			exp = 308;
		}

		hifl = (float)(hiDouble(size, size - 1) * pow(10, exp));
		hifl *= (isLoval ? (-1) : 1);

		return hifl;
	}

	public static Integer hiLoInt(int len, boolean isLoval)
	{
		Integer hiIn = 0;

		if (len >= 10)
		{
			hiIn = Integer.MAX_VALUE;
		}
		else
		{
			for (int i = 0; i < len; i++)
			{
				hiIn = (10 * hiIn) + 9;
			}
		}

		hiIn *= (isLoval ? (-1) : 1);

		return hiIn;
	}

	public static Long hiLoLong(int len, boolean isLoval)
	{
		Long hiLo = 0L;

		if (len >= 19)
		{
			hiLo = Long.MAX_VALUE;
		}
		else
		{
			for (int i = 0; i < len; i++)
			{
				hiLo = (10 * hiLo) + 9;
			}
		}

		hiLo *= (isLoval ? (-1) : 1);

		return hiLo;
	}

	public static Long hiLong(int len)
	{
		return hiLoLong(len, false);
	}

	public static Double loDFloat(int size)
	{
		return hiLoDFloat(size, true);
	}

	public static Double loDouble(int len, int dec)
	{
		return hiLoDouble(len, dec, true);
	}

	public static Double loDouble(int len)
	{
		return loDouble(len, 0);
	}

	public static Float loFloat(int size)
	{
		return hiLoFloat(size, true);
	}

	public static Integer loInt(int len)
	{
		return hiLoInt(len, true);
	}

	public static Long loLong(int len)
	{
		return hiLoLong(len, true);
	}

	/**
	 * Converts Object to BigDecimal.
	 *
	 * @param objToConvert Object to convert
	 * @return BigDecimal value after converting
	 */
	public static BigDecimal toBigDecimal(Object objToConvert)
	{
		if (objToConvert == null)
		{
			return null;
		}

		BigDecimal bd = null;

		if (objToConvert instanceof Number)
		{
			if (objToConvert instanceof Double)
			{
				bd = BigDecimal.valueOf(((Double)objToConvert).doubleValue());
			}
			else if (objToConvert instanceof Float)
			{
				bd = BigDecimal.valueOf(((Float)objToConvert).floatValue());
			}
			else if (objToConvert instanceof Long)
			{
				bd = BigDecimal.valueOf(((Long)objToConvert).longValue());
			}
			else if (objToConvert instanceof Integer)
			{
				bd = BigDecimal.valueOf(((Integer)objToConvert).intValue());
			}
		}
		else
		{
			String str = String.valueOf(objToConvert).trim();
			// 115 3 -> 11503
			str = str.replaceAll("[ ]", "0");

			if (str.length() > 0)
			{
				bd = new BigDecimal(str);
			}
		}

		return bd;
	}

	/**
	 * Returns a String whose decimal places is specified value.
	 *
	 * @param intToConvert int to convert
	 * @param precision number after decimal places
	 * @param decimalPlaces decimal places of the strToConvert value to be
	 *         returned
	 * @return
	 */
	public static String toDecimal(Number intToConvert, int precision,
		int decimalPlaces)
	{
		return toDecimal(String.valueOf(intToConvert), precision, decimalPlaces);
	}

	public static String toDecimal(DateEx dtToConvert, String outputFmt)
	{
		if (dtToConvert == null)
		{
			return "";
		}

		if (Utils.length(outputFmt) != 0)
		{
			if (dtToConvert.getTypeCode() == 'T')
			{
				dtToConvert.setTimeFormat(outputFmt);
			}
			else
			{
				dtToConvert.setDateFormat(outputFmt);
			}
		}

		if ("*CYMD".equalsIgnoreCase(outputFmt))
		{
			String orgFormat = dtToConvert.getJavaFormat();
			dtToConvert.setJavaFormat("yyyyMMdd");
			String date = toCentury(dtToConvert.toString(), "CyyMMdd");
			dtToConvert.setJavaFormat(orgFormat);

			return date;
		}

		dtToConvert.setSep('0');

		return dtToConvert.toString();
	}

	public static String toDecimal(DateEx dtToConvert)
	{
		return toDecimal(dtToConvert, "");
	}

	public static String toDecimal(Date dtToConvert, String outputFmt)
	{
		if (dtToConvert == null)
		{
			return "";
		}

		SimpleDateFormat sdf =
			DateTimeConverter.getDateTimeFormat(outputFmt, true,
				((dtToConvert instanceof Time) ? 'T'
											   : (
					(dtToConvert instanceof Timestamp) ? 'Z' : 'D'
				)));

		return sdf.format(dtToConvert);
	}

	public static String toDecimal(Date dtToConvert)
	{
		return toDecimal(dtToConvert, "");
	}

	public static String toDecimal(DateEx intToConvert, int precision,
		int decimalPlaces)
	{
		return toDecimal(getValidStr(intToConvert), precision, decimalPlaces);
	}

	public static String toDecimal(Date intToConvert, int precision,
		int decimalPlaces)
	{
		return toDecimal(getValidStr(intToConvert), precision, decimalPlaces);
	}

	public static String toDecimal(int intToConvert, int precision,
		int decimalPlaces)
	{
		return toDecimal(String.valueOf(intToConvert), precision,
			decimalPlaces);
	}

	/**
	 * Returns a String whose decimal places is specified value.
	 *
	 * @param strToConvert String to convert
	 * @param precision number after decimal places
	 * @param decimalPlaces decimal places of the strToConvert value to be
	 *         returned
	 * @return a String whose decimal places is specified value
	 */
	public static String toDecimal(String strToConvert, int precision,
		int decimalPlaces)
	{
		if (strToConvert == null)
		{
			return strToConvert;
		}

		if (!"".equals(strToConvert) && (precision != 0) &&
				(decimalPlaces == 0) && (strToConvert.length() > precision))
		{
			strToConvert = strToConvert.substring(0, precision);
		}

		strToConvert = getValidStr(strToConvert);

		if (decimalPlaces == 0)
		{
			// New code - 30-10-12
			// 112.69 -> 0112
			strToConvert = String.format(Locale.US, "%1$0" + precision + "d",
				toLong(strToConvert));
		}
		else
		{
			try
			{
				BigDecimal bd =
					new BigDecimal(strToConvert,
						new MathContext(precision, RoundingMode.HALF_UP));
				bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
				strToConvert = bd.toPlainString();
			}
			catch (Exception e)
			{
				strToConvert = String.format(Locale.US,
					"%1$" + (precision + 1) + "." + decimalPlaces + "f",
					toDouble(strToConvert));
			}
		}

		return strToConvert;
	}

	public static String toDecimal(StringBuilder strToConvert, int precision,
		int decimalPlaces)
	{
		return toDecimal(String.valueOf(strToConvert), precision,
			decimalPlaces);
	}

	/**
	 * Converts from any numbering system, such as from binary, hex, in the
	 * radix specified by the second argument to signed double.
	 *
	 * @param strToConvert String to convert
	 * @param inScale Input scale
	 * @param nbrNfe Number to set in case of NumberFormatException
	 * @return double value after converting
	 * @since (2003-01-30.15:49:14)
	 */
	public static Double toDouble(String strToConvert, int inScale,
		double nbrNfe)
	{
		strToConvert = StringUtils.trim(strToConvert, null);

		if ((strToConvert == null) || (strToConvert.length() == 0))
		{
			return nbrNfe;
		}

		strToConvert = getValidStr(strToConvert);

		// New code - 23-10-12
		if (strToConvert.indexOf('@') != -1)
		{
			return nbrNfe;
		}

		BigDecimal bd = new BigDecimal(strToConvert);

		return bd.doubleValue();
	}

	/**
	 * Converts Object to signed double.
	 *
	 * @param objToConvert Object to convert
	 * @return double value after converting
	 * @since (2008-11-11.12:50:33)
	 */
	public static Double toDouble(Object objToConvert)
	{
		if (objToConvert == null)
		{
			return 0.0D;
		}

		if (objToConvert instanceof BigDecimal)
		{
			return ((BigDecimal)objToConvert).doubleValue();
		}

		return toDouble(String.valueOf(objToConvert), 10, 0);
	}

	/**
	 * Converts from any numbering system, such as from binary, hex, in the
	 * radix specified by the second argument to signed float.
	 *
	 * @param strToConvert String to convert
	 * @param inScale Input scale
	 * @param nbrNfe Number to set in case of NumberFormatException
	 * @return float value after converting
	 * @since (2011-05-03.12:41:48)
	 */
	public static Float toFloat(String strToConvert, int inScale, float nbrNfe)
	{
		strToConvert = StringUtils.trim(strToConvert, null);

		if ((strToConvert == null) || (strToConvert.length() == 0))
		{
			return nbrNfe;
		}

		BigDecimal bd = new BigDecimal(strToConvert);

		return bd.floatValue();
	}

	/**
	 * Converts Object to float.
	 *
	 * @param objToConvert Object to convert
	 * @return float value after converting
	 */
	public static Float toFloat(Object objToConvert)
	{
		return toFloat(String.valueOf(objToConvert), 10, 0);
	}

	/**
	 * Converts from any numbering system, such as from binary, hex, in the
	 * radix specified by the second argument to signed integer.
	 *
	 * @param objToConvert Object to convert
	 * @param inScale Input scale
	 * @param nbrNfe Number to set in case of NumberFormatException
	 * @return an integer after converting
	 * @since (2002-02-25.10:01:48)
	 */
	public static Integer toInt(Object objToConvert, int inScale, int nbrNfe)
	{
		String strToConvert = getValidStr(objToConvert);

		if ((strToConvert == null) || (strToConvert.length() == 0))
		{
			return nbrNfe;
		}

		// New code - 23-10-12
		if (!NumberUtils.isNumber(strToConvert))
		{
			return nbrNfe;
		}

		BigDecimal bd = new BigDecimal(strToConvert);

		return bd.intValue();
	}

	/**
	 * Converts from any numbering system, such as from binary, hex, in the
	 * radix specified by the second argument to signed integer.
	 *
	 * @param objToConvert Object to convert
	 * @param inScale Input scale
	 * @return an integer array after converting
	 */
	public static Integer toInt(Object objToConvert, int inScale)
	{
		int nbrToReturn = 0;

		String strToConvert = getValidStr(objToConvert);

		if ((strToConvert == null) || (strToConvert.length() == 0))
		{
			return nbrToReturn;
		}

		strToConvert = strToConvert.trim();

		try
		{
			nbrToReturn = Integer.parseInt(strToConvert, inScale);
		}
		catch (NumberFormatException nfe1)
		{
			try
			{
				nbrToReturn = Double.valueOf(strToConvert).intValue();
			}
			catch (NumberFormatException nfe2)
			{
			}
		}

		return nbrToReturn;
	}

	public static Integer toInt(Object objToConvert)
	{
		return toInt(objToConvert, 10, 0);
	}

	public static Long toLong(Object objToConvert)
	{
		return toLong(objToConvert, 10, 0);
	}

	/**
	 * Converts from any numbering system, such as from binary, hex, in the
	 * radix specified by the second argument to signed long.
	 *
	 * @param objToConvert Object to convert
	 * @param inScale Input scale
	 * @param nbrNfe Number to set in case of NumberFormatException
	 * @return a long value after converting
	 * @since (2011-03-22.17:30:00)
	 */
	public static Long toLong(Object objToConvert, int inScale, long nbrNfe)
	{
		Long nbrToReturn = nbrNfe;

		if (Utils.length(objToConvert) == 0)
		{
			return nbrToReturn;
		}

		BigDecimal bd = toBigDecimal(objToConvert);

		if (bd != null)
		{
			nbrToReturn = bd.longValue();
		}

		return nbrToReturn;
	}
}