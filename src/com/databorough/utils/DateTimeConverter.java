package com.databorough.utils;

import java.sql.Time;
import java.sql.Timestamp;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static acdemxaMvcprocess.data.DataCRUD.setLastGlobalError;
import acdemxaMvcprocess.data.LastIO;

import com.databorough.utils.DateEx;

import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Provides methods for conversion of date and time string from one format to
 * the other, adding and removing delimiters and checking for valid date and
 * time formats.
 *
 * @author Amit Arya
 * @since (2001-04-01.12:24:12)
 */
public final class DateTimeConverter
{
	public static final String timeStampFormat = "yyyy-MM-dd-HH.mm.ss.SSS000";
	private static final String timePattern =
		"([0-1][0-9]|[2][0-3])[:,-\\\\\\/]" +
		"([0-5][0-9])[:,-\\\\\\/]?([0-5][0-9])?";

	private static String dateFormat;
	private static String timeFormat;
	private static boolean numericDate;
	public static Map<String, String> rpg2JavaDateFormat =
		new HashMap<String, String>();

	static
	{
		rpg2JavaDateFormat.put("*ISO", "yyyy-MM-dd");
		rpg2JavaDateFormat.put("*USA", "MM/dd/yyyy");
		rpg2JavaDateFormat.put("*EUR", "dd.MM.yyyy");
		rpg2JavaDateFormat.put("*JIS", "dd.MM.yyyy");
		rpg2JavaDateFormat.put("*JUL", "yy/ddd");
		rpg2JavaDateFormat.put("*YMD", "yy/MM/dd");
		rpg2JavaDateFormat.put("*YYMD", "yyyy/MM/dd");
		rpg2JavaDateFormat.put("*MDY", "MM/dd/yy");
		rpg2JavaDateFormat.put("*MDYY", "MM/dd/yyyy");
		rpg2JavaDateFormat.put("*DMY", "dd/MM/yy");
		rpg2JavaDateFormat.put("*DMYY", "dd/MM/yyyy");
		// new format created for separator -
		rpg2JavaDateFormat.put("*MDY1", "MM-dd-yy");
		rpg2JavaDateFormat.put("*MDYY1", "MM-dd-yyyy");
		rpg2JavaDateFormat.put("*DMY1", "dd-MM-yy");
		rpg2JavaDateFormat.put("*DMYY1", "dd-MM-yyyy");
	}

	public static Map<String, String> rpg2JavaTimeFormat =
		new HashMap<String, String>();

	static
	{
		rpg2JavaTimeFormat.put("*ISO", "HH.mm.ss");
		rpg2JavaTimeFormat.put("*USA", "HH.mm a");
		rpg2JavaTimeFormat.put("*EUR", "HH.mm.ss");
		rpg2JavaTimeFormat.put("*JIS", "HH:mm:ss");
		rpg2JavaTimeFormat.put("*HMS", "HH:mm:ss");
	}

	private static SimpleDateFormat df;

	public static DateEx convDate(Object dtx, char typeCode)
	{
		if (dtx == null)
		{
			return null;
		}

		if (dtx instanceof java.sql.Date)
		{
			return DateEx.valueOf((java.sql.Date)dtx);
		}
		else if (dtx instanceof Time)
		{
			return DateEx.valueOf((Time)dtx);
		}
		else if (dtx instanceof Timestamp)
		{
			return DateEx.valueOf((Timestamp)dtx);
		}
		else if (dtx instanceof CharSequence)
		{
			return DateEx.valueOf(dtx, typeCode);
		}

		return null;
	}

	public static long difference(DateEx dt1, DateEx dt2, String part)
	{
		return difference(dt1.getDateTime(), dt2.getDateTime(), part);
	}

	public static long difference(Date dt1, Date dt2, String part)
	{
		Calendar cal1 = Calendar.getInstance();

		if (dt1.after(dt2))
		{
			Date dtTmp = dt1;
			dt1 = dt2;
			dt2 = dtTmp;
		}

		cal1.setTime(dt1);
		cal1 = toGMT(cal1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dt2);
		cal2 = toGMT(cal2);

		long diffms = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		long diff = 0;
		int secsDiff = cal2.get(Calendar.SECOND) - cal1.get(Calendar.SECOND);
		int minsDiff = cal2.get(Calendar.MINUTE) - cal1.get(Calendar.MINUTE);
		int hoursDiff =
			cal2.get(Calendar.HOUR_OF_DAY) - cal1.get(Calendar.HOUR_OF_DAY);
		int daysDiff =
			cal2.get(Calendar.DAY_OF_MONTH) - cal1.get(Calendar.DAY_OF_MONTH);
		int monthsDiff = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
		int yearsDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);

		if (secsDiff < 0)
		{
			secsDiff += 60;
			minsDiff--;
		}

		if (minsDiff < 0)
		{
			minsDiff += 60;
			hoursDiff--;
		}

		if (hoursDiff < 0)
		{
			hoursDiff += 24;
			daysDiff--;
		}

		if (daysDiff < 0)
		{
			daysDiff += numDaysInMonth(cal2);
			monthsDiff--;
		}

		if (monthsDiff < 0)
		{
			monthsDiff += 12;
			yearsDiff--;
		}

		if (part.equalsIgnoreCase("*YEARS") || part.equalsIgnoreCase("*Y"))
		{
			diff = yearsDiff;
		}
		else if (part.equalsIgnoreCase("*MONTHS") ||
				part.equalsIgnoreCase("*M"))
		{
			diff = (yearsDiff * 12) + monthsDiff;
		}
		else if (part.equalsIgnoreCase("*DAYS") || part.equalsIgnoreCase("*D"))
		{
			diff = diffms / 86400000;
			/*double diffD = (diffms / 86400000);

			if (diffD > 0)
			{
				diff = ((long)diffD) + 1;
			}*/
		}
		else if (part.equalsIgnoreCase("*HOURS") ||
				part.equalsIgnoreCase("*H"))
		{
			diff = diffms / 3600000;
		}
		else if (part.equalsIgnoreCase("*MINUTES") ||
				part.equalsIgnoreCase("*MN"))
		{
			diff = diffms / 60000;
		}
		else if (part.equalsIgnoreCase("*SECONDS") ||
				part.equalsIgnoreCase("*S"))
		{
			diff = diffms / 1000;
		}
		else if (part.equalsIgnoreCase("*MSECONDS") ||
				part.equalsIgnoreCase("*MS"))
		{
			diff = diffms;
		}

		return diff;
	}

	/**
	 * Formats the date to outFormat.
	 *
	 * @param date date object
	 * @param outFormat output format pattern
	 * @return formated field date
	 * @since (2001-01-10.14:37:12)
	 */
	public static String formatDate(Date date, String outFormat)
	{
		// If date is null then simply return null
		if (date == null)
		{
			return null;
		}

		// If outFormatRecv is null then simply return null
		if ((outFormat == null) || outFormat.equals(""))
		{
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat();

		try
		{
			sdf.applyPattern(outFormat);

			return sdf.format(date);
		}
		catch (IllegalArgumentException iae)
		{
			return null;
		}
	}

	/**
	 * Formats the fieldData in inFormat to output format.
	 *
	 * @param fieldData data of field
	 * @param inFormat input format pattern
	 * @param outFormat output format pattern
	 * @return formatted field's data
	 * @since (2003-01-24.18:17:51)
	 */
	public static String formatDate(String fieldData, String inFormat,
		String outFormat)
	{
		String retStr = null;

		// If fieldData is null then simply return the fieldData
		if ((fieldData == null) || fieldData.equals(""))
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
			// Check if inFormat & outFormat are same
			if (inFormat.equals(outFormat))
			{
				return fieldData;
			}

			if ("0".equals(fieldData) &&
					("yyyyMMdd".equals(inFormat) || "yyMMdd".equals(inFormat)))
			{
				return "";
			}

			int inFormatLen = inFormat.length();

			if (inFormat.substring(0, 1).equalsIgnoreCase("C"))
			{
				inFormatLen += 1;
			}

			// If length of fieldData is less than input format, pad those many
			// zeros in the beginning of fieldData
			fieldData = StringUtils.padStringWithValue(fieldData, "0",
					inFormatLen, true);

			// CASE I :
			// C used in inFormat
			// Convert the data containing Century to Year
			fieldData = fromCentury(fieldData, inFormat);

			if (inFormat.substring(0, 1).equalsIgnoreCase("C"))
			{
				inFormat = "yy" + inFormat.substring(1);
			}

			String outFormatRecv = outFormat;

			if (outFormat.substring(0, 1).equalsIgnoreCase("C"))
			{
				outFormat = "yy" + outFormat.substring(1);
			}

			SimpleDateFormat sdf = new SimpleDateFormat();

			// A SimpleDateFormat is formed using the pattern same as the
			// inFormat pattern.
			sdf.applyPattern(inFormat);

			// Creating a date object using the string value of the date field.
			Date date = sdf.parse(fieldData, new ParsePosition(0));

			if (date == null)
			{
				return fieldData;
			}

			// A SimpleDateFormat is formed using outFormat pattern and the date
			// object will be converted to outFormat format.
			sdf.applyPattern(outFormat);

			retStr = sdf.format(date);

			retStr = validateYear(fieldData, retStr, inFormat, outFormat);

			// CASE II :
			// C used in outFormat
			// Convert the data containing Year to Century
			retStr = toCentury(retStr, outFormatRecv);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return retStr;
	}

	/**
	 * Converts the data containing Century to Year.
	 *
	 * @param fieldData data of field to be converted
	 * @param inFormat the pattern received for describing the in format
	 * @return converted field data
	 * @since (2001-01-10.11:17:06)
	 */
	public static String fromCentury(String fieldData, String inFormat)
	{
		// if fieldData is null then simply return the fieldData
		if ((fieldData == null) || fieldData.equals(""))
		{
			return fieldData;
		}

		// If inFormat is null then simply return the fieldData
		if ((inFormat == null) || inFormat.equals(""))
		{
			return fieldData;
		}

		// CASE I :
		// C used in inFormat

		// Assumption :	C is always the first character

		// Make proper fieldData based on inFormat since
		// SimpleDateFormat does not support C

		// The below order is important
		if (!inFormat.substring(0, 2).equalsIgnoreCase("CC") &&
				inFormat.substring(0, 1).equalsIgnoreCase("C"))
		{
			// CYYMMDD - Can be anything after C
			String tempStr = fieldData.substring(0, 2);

			fieldData = (Integer.parseInt(tempStr) + 19) +
				fieldData.substring(2);
		}

		return fieldData;
	}

	/**
	 * Returns the current date or time format.
	 *
	 * @param getTime
	 * @return current date or time format
	 * @since (2013-06-18.17:06:52)
	 */
	public static String getCurrentFormat(boolean getTime)
	{
		return getTime ? timeFormat : dateFormat;
	}

	public static DateEx getDate()
	{
		DateEx dx = new DateEx('D');
		dx.setDateFormat(getDefaultFormat(false));

		return dx;
	}

	public static Date getDate(String ds, String format)
	{
		if ((ds == null) || (ds.trim().length() == 0))
		{
			return null;
		}

		// If outFormatRecv is null then simply return null
		if ((format == null) || (format.trim().length() == 0))
		{
			return null;
		}

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			return sdf.parse(ds);
		}
		catch (Exception e)
		{
		}

		return null;
	}

	public static Date getDate(Integer digits, String dateFormat)
	{
		if ("CYYMMDD".equals(dateFormat))
		{
			String digitStr =
				StringUtils.padStringWithValue("" + digits, "0", 7, true);
			Integer century = Integer.valueOf(digitStr.substring(0, 1));
			Integer year = Integer.valueOf(digitStr.substring(1, 3));
			year += ((century < 1) ? 1900 : 2000);

			Integer month = Integer.valueOf(digitStr.substring(3, 5)) - 1;
			Integer date = Integer.valueOf(digitStr.substring(5).trim());
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, date);

			return calendar.getTime();
		}
		else if ("YYMMDD".equals(dateFormat))
		{
			String digitStr =
				StringUtils.padStringWithValue("" + digits, "0", 6, true);
			Integer year = Integer.valueOf(digitStr.substring(0, 2));
			Integer month = Integer.valueOf(digitStr.substring(2, 4)) - 1;
			Integer date = Integer.valueOf(digitStr.substring(4).trim());
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, date);

			return calendar.getTime();
		}
		else if ("MMYYDD".equals(dateFormat))
		{
			String digitStr =
				StringUtils.padStringWithValue("" + digits, "0", 6, true);
			Integer month = Integer.valueOf(digitStr.substring(0, 2)) - 1;
			Integer year = Integer.valueOf(digitStr.substring(2, 4));
			Integer date = Integer.valueOf(digitStr.substring(4).trim());
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, date);

			return calendar.getTime();
		}
		else if ("YYYYMMDD".equals(dateFormat))
		{
			String digitStr =
				StringUtils.padStringWithValue("" + digits, "0", 8, true);
			Integer year = Integer.valueOf(digitStr.substring(0, 4));
			Integer month = Integer.valueOf(digitStr.substring(4, 6));
			Integer date = Integer.valueOf(digitStr.substring(6).trim());
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, date);

			return calendar.getTime();
		}

		return new Date();
	}

	public static Integer getDateDigits(Date date, String dateFormat)
	{
		if ("CYYMMDD".equals(dateFormat))
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			String month =
				StringUtils.padStringWithValue("" +
					(calendar.get(Calendar.MONTH) + 1), "0", 2, true);
			String day =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.DATE), "0", 2, true);
			Integer year = calendar.get(Calendar.YEAR);
			String dateString = (year > 2000) ? "1" : "0";
			year = (year > 2000) ? (year - 2000) : (year - 1900);

			String yearStr =
				StringUtils.padStringWithValue("" + year, "0", 2, true);
			String digitStr = dateString + yearStr + month + day;

			return Integer.parseInt(digitStr);
		}
		else if ("YYMMDD".equals(dateFormat))
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			String year =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.YEAR), "0", 2, true);
			String month =
				StringUtils.padStringWithValue("" +
					(calendar.get(Calendar.MONTH) + 1), "0", 2, true);
			String day =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.DATE), "0", 2, true);
			String digitStr = year + month + day;

			return Integer.parseInt(digitStr);
		}
		else if ("MMYYDD".equals(dateFormat))
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			String month =
				StringUtils.padStringWithValue("" +
					(calendar.get(Calendar.MONTH) + 1), "0", 2, true);
			String day =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.DATE), "0", 2, true);
			String yearStr =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.YEAR), "0", 2, true);
			String digitStr = month + yearStr + day;

			return Integer.parseInt(digitStr);
		}
		else if ("YYYYMMDD".equals(dateFormat))
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			String yearStr =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.YEAR), "0", 4, true);
			String month =
				StringUtils.padStringWithValue("" +
					(calendar.get(Calendar.MONTH) + 1), "0", 2, true);
			String day =
				StringUtils.padStringWithValue("" +
					calendar.get(Calendar.DATE), "0", 2, true);
			String digitStr = yearStr + month + day;

			return Integer.parseInt(digitStr);
		}

		return 0;
	}

	/**
	 * Gets the date format.
	 *
	 * @param rpgFormat format of date in RPG
	 * @param remSep to remove the separator before applying pattern or not
	 * @return date format
	 * @since (2012-08-13.16:15:51)
	 */
	public static String getDateTimeFormat(String rpgFormat, boolean remSep)
	{
		if (Utils.length(rpgFormat) == 0)
		{
			return null;
		}

		if ("*CYMD".equalsIgnoreCase(rpgFormat))
		{
			rpgFormat = "*YYMD";
		}

		String format = rpg2JavaDateFormat.get(rpgFormat.toUpperCase());

		if (format == null)
		{
			format = rpg2JavaTimeFormat.get(rpgFormat.toUpperCase());
		}

		if ((format != null) && remSep)
		{
			format = format.replaceAll("[\\/\\.\\:\\-]", "");
		}

		return format;
	}

	/**
	 * Gets the date format.
	 *
	 * @param format the pattern describing the date format
	 * @param remSep to remove the separator before applying pattern or not
	 * @param typeCode
	 * @return date format
	 * @since (2004-06-06.11:37:13)
	 */
	public static SimpleDateFormat getDateTimeFormat(String format,
		boolean remSep, char typeCode)
	{
		if (typeCode == 'Z')
		{
			format = timeStampFormat;
		}
		else
		{
			if (Utils.length(format) == 0)
			{
				format = getDefaultFormat(typeCode == 'T');
			}

			Map<String, String> formatMap =
				(typeCode == 'T') ? rpg2JavaTimeFormat : rpg2JavaDateFormat;
			format = formatMap.get(format.toUpperCase());
		}

		if (format != null)
		{
			if (remSep)
			{
				if (numericDate)
				{
					format = format.replaceAll("[\\/\\.\\:\\-]", "");
					numericDate = false;
				}
			}

			if (df == null)
			{
				df = new SimpleDateFormat(format);

				Calendar cal = Calendar.getInstance();
				cal.set(1940, 0, 1, 0, 0, 0);
				df.set2DigitYearStart(cal.getTime());
			}
			else
			{
				df.applyPattern(format);
			}
		}

		return df;
	}

	public static String getDay()
	{
		Calendar cal1 = Calendar.getInstance();

		return "" + cal1.get(Calendar.DAY_OF_MONTH);
	}

	public static String getDefaultFormat(boolean getTime)
	{
		String dfmt = getCurrentFormat(getTime);

		if (Utils.length(dfmt) == 0)
		{
			dfmt = "*ISO";

			/*if (!getTime)
			{
				String dateFormat = Utils.getDateFormat();

				if (Utils.length(dateFormat) != 0)
				{
					dfmt = "*" + dateFormat;
				}
			}*/
		}

		return dfmt;
	}

	public static String getMonth()
	{
		Calendar cal = Calendar.getInstance();

		return "" + (cal.get(Calendar.MONTH) + 1);
	}

	/**
	 * Returns the RPG date format equivalent to the passed Java format.
	 *
	 * @param dateFormat
	 * @return String equivalent RPG date format
	 * @since (2013-06-27.18:00:00)
	 */
	public static String getRpgDateFormat(String dateFormat)
	{
		String returnFormat = null;

		Iterator<Map.Entry<String, String>> iter =
			rpg2JavaDateFormat.entrySet().iterator();

		while (iter.hasNext())
		{
			Map.Entry<String, String> entry = iter.next();

			if (entry.getValue().equalsIgnoreCase(dateFormat))
			{
				returnFormat = entry.getKey().substring(1);

				break;
			}
		}

		if (returnFormat == null)
		{
			returnFormat = "ISO";
		}

		return returnFormat;
	}

	public static DateEx getTime()
	{
		DateEx dx = new DateEx('T');
		dx.setTimeFormat(getDefaultFormat(true));

		return dx;
	}

	/**
	 * Splits the time pattern string to its components.
	 *
	 * @param time time pattern string
	 * @return time array containing Hours, Minutes, Seconds
	 * @since (2012-07-31.13:28:40)
	 */
	public static String[] getTimeParts(String time)
	{
		if (time == null)
		{
			return null;
		}

		String timeArr[] = new String[3];

		Pattern pattern = Pattern.compile(timePattern);
		Matcher matcher = pattern.matcher(time);

		if (matcher.matches())
		{
			int groupCount = matcher.groupCount();

			for (int i = 1; i < (groupCount + 1); i++)
			{
				String group = matcher.group(i);

				if (group == null)
				{
					continue;
				}

				timeArr[i - 1] = group;
			}
		}

		return timeArr;
	}

	public static DateEx getTimestamp()
	{
		DateEx dx = new DateEx('Z');

		return dx;
	}

	public static DateEx getUDate(String dateEdit)
	{
		DateEx dx = new DateEx('D');
		dx.setDateFormat(dateEdit);

		return dx;
	}

	public static String getYear()
	{
		Calendar cal = Calendar.getInstance();

		return "" + cal.get(Calendar.YEAR);
	}

	public static Date hiDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(9999, Calendar.DECEMBER, 31, 23, 59, 59);

		return cal.getTime();
	}

	public static Timestamp hiTimestamp()
	{
		Timestamp ts = new Timestamp(hiDate().getTime());
		ts.setNanos(999999999);

		return ts;
	}

	/**
	 * Finds whether the format is a Date format or a Time format.
	 *
	 * @param rpgFormat format of date in RPG
	 * @return <code>true</code> if date format, otherwise false
	 * @since (2012-08-14.17:09:45)
	 */
	public static boolean isDateFormat(String rpgFormat)
	{
		if (Utils.length(rpgFormat) == 0)
		{
			return true;
		}

		if ("*CYMD".equalsIgnoreCase(rpgFormat))
		{
			rpgFormat = "*YYMD";
		}

		String format = rpg2JavaTimeFormat.get(rpgFormat.toUpperCase());

		if (format != null)
		{
			return false;
		}

		return true;
	}

	public static Date loDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(1, Calendar.JANUARY, 1, 0, 0, 0);

		return cal.getTime();
	}

	public static Timestamp loTimestamp()
	{
		Timestamp ts = new Timestamp(loDate().getTime());
		ts.setNanos(0);

		return ts;
	}

	private static int numDaysInMonth(Calendar cal)
	{
		int month = cal.get(Calendar.MONTH) + 1;
		int days = 30;

		if (month == 2)
		{
			days = 28;

			if ((cal.get(Calendar.YEAR) % 4) == 0)
			{
				days++;
			}
		}
		else
		{
			if (month > 7)
			{
				month--;
			}

			if ((month % 2) != 0)
			{
				days++;
			}
		}

		return days;
	}

	/**
	 * Converts a given string to Sql Date.
	 *
	 * @param ds date as String
	 * @return string converted as Sql Date
	 * @since (2013-10-07.17:14:37)
	 */
	public static java.sql.Date sqlDate(String ds)
	{
		//return java.sql.Date.valueOf(ds);
		return new DateEx(ds, 'D').getDate();
	}

	/**
	 * Converts a given string to Time.
	 *
	 * @param ds time as String
	 * @return string converted as Time
	 * @since (2013-10-07.17:19:37)
	 */
	public static Time sqlTime(String ds)
	{
		//return Time.valueOf(ds);
		return new DateEx(ds, 'T').getTime();
	}

	/**
	 * Converts a given string to Timestamp.
	 *
	 * @param ds timestamp as String
	 * @return string converted as Timestamp
	 * @since (2013-10-07.17:24:37)
	 */
	public static Timestamp sqlTimestamp(String ds)
	{
		//return Timestamp.valueOf(ds);
		return new DateEx(ds, 'Z').getTimestamp();
	}

	public static int subDate(DateEx dt, String part)
	{
		if (dt == null)
		{
			return 0;
		}

		return subDate(dt.getDateTime(), part);
	}

	public static int subDate(Date dt, String part)
	{
		int sub = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		if (part.equalsIgnoreCase("*YEARS") || part.equalsIgnoreCase("*Y"))
		{
			sub = cal.get(Calendar.YEAR);
		}
		else if (part.equalsIgnoreCase("*MONTHS") ||
				part.equalsIgnoreCase("*M"))
		{
			sub = cal.get(Calendar.MONTH) + 1;
		}
		else if (part.equalsIgnoreCase("*DAYS") || part.equalsIgnoreCase("*D"))
		{
			sub = cal.get(Calendar.DAY_OF_MONTH);
		}
		else if (part.equalsIgnoreCase("*HOURS") ||
				part.equalsIgnoreCase("*H"))
		{
			sub = cal.get(Calendar.HOUR);
		}
		else if (part.equalsIgnoreCase("*MINUTES") ||
				part.equalsIgnoreCase("*MN"))
		{
			sub = cal.get(Calendar.MINUTE);
		}
		else if (part.equalsIgnoreCase("*SECONDS") ||
				part.equalsIgnoreCase("*S"))
		{
			sub = cal.get(Calendar.SECOND);
		}
		else if (part.equalsIgnoreCase("*MSECONDS") ||
				part.equalsIgnoreCase("*MS"))
		{
			sub = cal.get(Calendar.MILLISECOND);
		}

		return sub;
	}

	public static void test(LastIO lio, char typeCode, String format,
		Object dtzVar)
	{
		test(lio, typeCode, format, String.valueOf(dtzVar));
	}

	public static void test(LastIO lio, char typeCode, String format,
		String dtzVar)
	{
		if (Utils.length(format) == 0)
		{
			if (typeCode != 'Z')
			{
				format = getDefaultFormat(typeCode == 'T');
			}
		}

		boolean error =
			toDateTime(dtzVar, format, format.endsWith("0"), typeCode) == null;
		lio.setError(error);
		setLastGlobalError(lio.isError());
	}

	public static void test(LastIO lio, Object dtzVar)
	{
		if (dtzVar == null)
		{
			return;
		}

		if (dtzVar instanceof DateEx)
		{
			lio.setError(false);
		}
		else if (dtzVar instanceof Date)
		{
			lio.setError(false);
		}
		else if (dtzVar instanceof String)
		{
			test(lio, 'T', "", dtzVar);

			return;
		}

		setLastGlobalError(lio.isError());
	}

	/**
	 * Converts the data containing Year to Century.
	 *
	 * @param fieldData data of field
	 * @param outFormatRecv the pattern received for describing the out format
	 * @return string after converting it to Century
	 * @since (2001-01-10.11:31:40)
	 */
	public static String toCentury(String fieldData, String outFormatRecv)
	{
		// If fieldData is null then simply return the fieldData
		if ((fieldData == null) || fieldData.equals(""))
		{
			return fieldData;
		}

		// If outFormatRecv is null then simply return the fieldData
		if ((outFormatRecv == null) || outFormatRecv.equals(""))
		{
			return fieldData;
		}

		// CASE II :
		// C used in outFormat

		// Assumption :	C is always the first character

		// Make proper fieldData based on outFormat since
		// SimpleDateFormat does not support C

		// The below order is important
		if (!outFormatRecv.substring(0, 2).equalsIgnoreCase("CC") &&
				outFormatRecv.substring(0, 1).equalsIgnoreCase("C"))
		{
			// CYYMMDD - Can be anything after C
			String tempStr = fieldData.substring(0, 2);

			fieldData = (Integer.parseInt(tempStr) - 19) +
				fieldData.substring(2);
		}

		return fieldData;
	}

	public static DateEx toDate(Object ds)
	{
		return toDate(ds, "");
	}

	/**
	 * Converts a given string to Date.
	 *
	 * @param ds date as String
	 * @param outFormat the pattern for describing the format
	 * @return DateEx converted as Date
	 * @since (2001-01-09.09:43:55)
	 */
	public static DateEx toDate(Object ds, String outFormat)
	{
		dateFormat = outFormat;

		String inFormat = getDefaultFormat(false);

		if (Utils.length(outFormat) == 0)
		{
			outFormat = inFormat;
		}

		return toDate(ds, inFormat, outFormat);
	}

	/**
	 * Converts a given string to Date.
	 *
	 * @param ds date as String
	 * @param inFormat input format pattern
	 * @param outFormat output format pattern
	 * @return DateEx converted as Date
	 * @since (2012-03-12.09:43:55)
	 */
	public static DateEx toDate(Object ds, String inFormat, String outFormat)
	{
		dateFormat = outFormat;

		if (ds == null)
		{
			return null;
		}

		if (ds instanceof DateEx)
		{
			DateEx dx = (DateEx)ds;
			dx.setDateFormat(outFormat);

			return dx;
		}
		else if (ds instanceof Date)
		{
			DateEx dx = new DateEx('D');
			dx.setDateTime((Date)ds);
			dx.setDateFormat(outFormat);

			return dx;
		}

		if (ds instanceof Integer)
		{
			numericDate = true;
		}

		DateEx dx = null;
		Date dt = toDateTime(String.valueOf(ds), inFormat, true, 'D');

		if (dt != null)
		{
			dx = new DateEx(dt, 'D');
		}

		return dx;
	}

	/**
	 * Converts String to Date/Time.
	 *
	 * @param ds date as a string
	 * @param format date format of the field
	 * @param remSep to remove the separator or not before applying the pattern
	 * @param typeCode
	 * @return converted string as Date
	 * @since (2004-05-31.13:01:41)
	 */
	public static Date toDateTime(String ds, String format, boolean remSep,
		char typeCode)
	{
		if ((ds == null) || (ds.trim().length() == 0))
		{
			return new Date(0);
		}

		if (Utils.length(format) > 4)
		{
			if ("*CYMD".equalsIgnoreCase(format))
			{
				String dsRemSep = ds.replaceAll("[\\/\\.\\:\\-]", "");

				if (Utils.length(dsRemSep) == 7)
				{
					String yearPrefix = "20";

					if (dsRemSep.charAt(0) == '0')
					{
						yearPrefix = "19";
					}

					ds = yearPrefix + ds.substring(1);
				}

				format = "*YYMD";
			}

			if (format.endsWith("0"))
			{
				format = format.substring(0, format.length() - 1);
			}
		}

		Date d;
		SimpleDateFormat df = getDateTimeFormat(format, remSep, typeCode);

		if (typeCode == 'T')
		{
			String fmt = df.toPattern();

			ds = StringUtils.padStringWithValue(ds, "0", fmt.length(), true);

			if ((ds.indexOf(':') != -1) && (fmt.indexOf('.') != -1))
			{
				ds = ds.replace(':', '.');
			}
		}

		try
		{
			d = df.parse(ds);
		}
		catch (ParseException e)
		{
			d = null;
		}

		return d;
	}

	/**
	 * Converts to GMT Time Zone.
	 *
	 * @param cal Calendar instance
	 * @return Calendar in GMT Time Zone
	 * @since (2012-09-25.18:50:51)
	 */
	private static Calendar toGMT(Calendar cal)
	{
		Date date = cal.getTime();
		TimeZone tz = cal.getTimeZone();
		long timeInMilliseconds = date.getTime();
		int offsetFromUTC = tz.getOffset(timeInMilliseconds);

		Calendar gmtCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		gmtCal.setTime(date);
		gmtCal.add(Calendar.MILLISECOND, offsetFromUTC);

		return gmtCal;
	}

	public static DateEx toTime(Object ds)
	{
		return toTime(ds, "");
	}

	/**
	 * Converts a given string to Time.
	 *
	 * @param ds date as String
	 * @param outFormat the pattern for describing the format
	 * @return DateEx converted as Time
	 * @since (2001-01-09.09:43:55)
	 */
	public static DateEx toTime(Object ds, String outFormat)
	{
		timeFormat = outFormat;

		String inFormat = getDefaultFormat(true);

		if (Utils.length(outFormat) == 0)
		{
			outFormat = inFormat;
		}

		return toTime(ds, inFormat, outFormat);
	}

	/**
	 * Converts a given string to Time.
	 *
	 * @param ds date as String
	 * @param inFormat input format pattern
	 * @param outFormat output format pattern
	 * @return DateEx converted as Time
	 * @since (2012-03-12.10:43:55)
	 */
	public static DateEx toTime(Object ds, String inFormat, String outFormat)
	{
		timeFormat = outFormat;

		if (ds == null)
		{
			return null;
		}

		if (ds instanceof DateEx)
		{
			DateEx dx = (DateEx)ds;
			dx.setTimeFormat(outFormat);

			return dx;
		}
		else if (ds instanceof Date)
		{
			DateEx dx = new DateEx('T');
			dx.setDateTime((Date)ds);
			dx.setTimeFormat(outFormat);

			return dx;
		}

		if (ds instanceof Integer)
		{
			numericDate = true;
		}

		DateEx dx = null;
		Date dt = toDateTime(String.valueOf(ds), inFormat, true, 'T');

		if (dt != null)
		{
			dx = new DateEx(dt, 'T');
		}

		return dx;
	}

	public static DateEx toTimestamp(Object ds)
	{
		return toTimestamp(String.valueOf(ds));
	}

	public static DateEx toTimestamp(String ds)
	{
		Date dt = toDateTime(ds, "", false, 'Z');

		if (dt == null)
		{
			return null;
		}

		return new DateEx(dt, 'Z');
	}

	/**
	 * Validates the year.
	 *
	 * @param inFieldData input data of the field
	 * @param outFieldData output data of the field
	 * @param inFormat input format pattern
	 * @param outFormat output format pattern
	 * @return formatted field data
	 * @since (2008-04-01.13:31:45)
	 */
	private static String validateYear(String inFieldData, String outFieldData,
		String inFormat, String outFormat)
	{
		String retStr = outFieldData;

		// if inFieldData is null then simply return the outFieldData
		if ((inFieldData == null) || inFieldData.equals(""))
		{
			return outFieldData;
		}

		// if outFieldData is null then simply return the outFieldData
		if ((outFieldData == null) || outFieldData.equals(""))
		{
			return outFieldData;
		}

		if (StringUtils.isAllLetter(inFieldData, '0'))
		{
			// 30-11-02 in dd-MM-yy
			retStr = StringUtils.replaceString(outFieldData, "30", "00");
			retStr = StringUtils.replaceString(retStr, "11", "00");

			if ("yyMMddyy".equals(inFormat))
			{
				// 30-11-99 in dd-MM-yy
				retStr = StringUtils.replaceString(retStr, "99", "00");
			}
			else
			{
				retStr = StringUtils.replaceString(retStr, "02", "00");
			}

			return retStr;
		}

		boolean zeroYear = false;

		int inYearIndx = inFormat.indexOf("yyyy");

		if (inYearIndx != -1)
		{
			String year = inFieldData.substring(inYearIndx, inYearIndx + 4);

			zeroYear = ("0000".equals(year));
		}
		else
		{
			inYearIndx = inFormat.indexOf("yy");

			if (inYearIndx != -1)
			{
				String year = inFieldData.substring(inYearIndx, inYearIndx + 2);

				zeroYear = ("00".equals(year));
			}
		}

		if (!zeroYear)
		{
			return retStr;
		}

		int outYearIndx = outFormat.indexOf("yyyy");

		if (outYearIndx != -1)
		{
			retStr = StringUtils.replaceString(outFieldData, outYearIndx,
					outYearIndx + 4, "0000");
		}
		else
		{
			outYearIndx = outFormat.indexOf("yy");

			if (outYearIndx != -1)
			{
				retStr = StringUtils.replaceString(outFieldData, outYearIndx,
						outYearIndx + 2, "00");
			}
		}

		return retStr;
	}
}
