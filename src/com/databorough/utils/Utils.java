package com.databorough.utils;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Array;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DateTimeConverter.getRpgDateFormat;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.NumberFormatter.toInt;
import static com.databorough.utils.ReflectionUtils.class2Name;
import static com.databorough.utils.ReflectionUtils.isUserDefOrArr;

/**
 * Utility class for different X-Redo classes.
 * <p>
 * It is not to be instantiated, only use its public static members outside.
 *
 * @author Amit Arya
 * @since (2000-02-16.16:29:02)
 */
public final class Utils
{
	public static final String LINE_SEPARATOR =
		System.getProperty("line.separator");

	public final static boolean FORWARD = false;
	public final static boolean PREVIOUS = true;

	public static enum LASTREAD { READ, READE, READP, READPE, unspecified; }
	public static enum POSITION { END, HIVAL, LOVAL, START; }

	public final static int EQ = 1;
	public final static int GT = 2;
	public final static int GE = 3;
	public final static int LT = 4;
	public final static int LE = 5;

	private static Map<String, String> prefNames =
		new HashMap<String, String>();
	private static ResourceBundle bundleEntities;
	private static ResourceBundle bundlePrefNames;

	/**
	 * Default constructor.
	 */
	private Utils()
	{
		super();
	}

	public static String bool2Str(boolean qind)
	{
		return qind ? "1" : "0";
	}

	public static String formatUsingEditCode(Object fldVal, String editcode)
	{
		String fldstr = String.valueOf(fldVal);
		int di = fldstr.indexOf('.');
		int scale = 0;
		int len = fldstr.length();

		if (di > -1)
		{
			scale = len - di - 1;
			len--;
		}

		return formatUsingEditCode(fldVal, editcode, len, scale);
	}

	public static String formatUsingEditCode(Object fldVal, String editcode,
		int precision)
	{
		return formatUsingEditCode(fldVal, editcode, "", precision);
	}

	public static String formatUsingEditCode(Object fldVal, String editcode,
		String prefix, int precision)
	{
		String fldstr = String.valueOf(fldVal);
		int di = fldstr.indexOf('.');
		int scale = 0;

		if (di > -1)
		{
			scale = precision - di - 1;
			precision--;
		}

		return formatUsingEditCode(fldVal, editcode, prefix, precision, scale);
	}

	public static String formatUsingEditCode(Object fldVal, String editcode,
		int precision, int scale)
	{
		return formatUsingEditCode(fldVal, editcode, "", precision, scale);
	}

	public static String formatUsingEditCode(Object fldVal, String editcode,
		String prefix, int precision, int scale)
	{
		String str = "";
		String fmt = "";
		int length = precision;

		if (length == 0)
		{
			return String.valueOf(fldVal);
		}

		if (fldVal instanceof Float)
		{
			if (precision > 4)
			{
				length = 23;
			}
			else
			{
				length = 14;
			}

			scale = length - 1;
			fmt = "%1$" + length + "." + scale + "e";
		}
		else if (fldVal instanceof Integer || fldVal instanceof Long)
		{
			fmt = "%1$" + length + "d";
		}
		else if (fldVal instanceof Double)
		{
			length++;
			fmt = "%1$" + length + "." + scale + "f";
		}
		else
		{
			fmt = "%1$-" + length + "s";
		}

		switch (editcode.charAt(0))
		{
		case '1':
		case '2':
		case '3':
		case '4':
		{
			if (fldVal != null)
			{
				str = String.format(Locale.US, fmt, fldVal);
			}

			if (length(prefix) > 0)
			{
				str = prefix + str;
			}

			break;
		}

		case 'A':
		case 'B':
		case 'C':
		case 'D':
		{
			if (fldVal != null)
			{
				if (String.valueOf(fldVal).startsWith("-"))
				{
					str = String.format(Locale.US, fmt, fldVal);
				}
			}

			str += "CR";

			if (length(prefix) > 0)
			{
				str = prefix + str;
			}

			return str.substring(1);
		}

		case 'J':
		case 'K':
		case 'L':
		case 'M':
		{
			if (fldVal != null)
			{
				str = String.format(Locale.US, fmt, fldVal);

				if (String.valueOf(fldVal).startsWith("-"))
				{
					str += "-";
				}
			}

			if (length(prefix) > 0)
			{
				str = prefix + str;
			}

			return str.substring(1);
		}

		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		{
			if (fldVal != null)
			{
				str = String.format(Locale.US, fmt, fldVal);
			}

			if (length(prefix) > 0)
			{
				str = prefix + str;
			}

			break;
		}

		case 'X':
		case 'Z':
		{
			if (fldVal instanceof Number)
			{
				length = precision;
			}

			if (fldVal instanceof Float)
			{
				if (precision > 4)
				{
					length = 23;
				}
				else
				{
					length = 14;
				}

				scale = length - 1;
				fmt = "%1$0" + length + "." + scale + "e";
			}
			else if (fldVal instanceof Integer || fldVal instanceof Long)
			{
				fmt = "%1$0" + length + "d";
			}
			else if (fldVal instanceof Double)
			{
				//length++;
				//fmt = "%1$0" + length + "." + scale + "f";
				// No Decimal point
				length++;
				fmt = "%1$0" + length + "d";

				String fldStr = String.valueOf(fldVal);

				if (fldStr.indexOf(".") != -1)
				{
					fldStr = fldStr.replace(".", "");
				}

				fldVal = toInt(fldStr);
			}
			else
			{
				fmt = "%1$-" + length + "s";
			}

			if (fldVal != null)
			{
				str = String.format(Locale.US, fmt, fldVal);
			}

			if (length(prefix) > 0)
			{
				str = prefix + str;
			}

			break;
		}

		case 'Y':
		{
			if (fldVal instanceof Date)
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(((Date)fldVal));

				String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
				String day = String.valueOf(cal.get(Calendar.DATE));
				String year = String.valueOf(cal.get(Calendar.YEAR));
				String dateStr = month + "/" + day + "/" + year;

				return dateStr;
			}

			break;
		}
		}

		return str;
	}

	/**
	 * Returns a String result representing the numeric value edited according
	 * to the edit word.
	 *
	 * @param numVal numeric value
	 * @param editWord edit word
	 * @return edited numeric value
	 * @since (2012-05-18.16:38:53)
	 */
	public static String formatUsingEditWord(Object numVal, String editWord)
	{
		if (numVal == null)
		{
			return "";
		}

		char numValArr[] = numVal.toString().toCharArray();
		int numPos = numValArr.length - 1;
		char editWordArr[] = editWord.toCharArray();
		int len = editWordArr.length;

		int pos = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = len - 1; i >= 0; i--)
		{
			String edtwStr = Character.toString(editWordArr[i]);

			if (Character.isSpaceChar(editWordArr[i]))
			{
				if (numPos != -1)
				{
					sb.insert(pos++, numValArr[numPos--]);
				}
			}
			else
			{
				if ("&".equals(edtwStr))
				{
					sb.insert(pos++, ' ');
				}
				else
				{
					sb.insert(pos++, editWordArr[i]);
				}
			}
		} // i

		return sb.reverse().toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getArg(Object values[], int index)
	{
		if ((values == null) || (index >= values.length))
		{
			return null;
		}

		Object obj = null;

		String type = values.getClass().getName();
		int numVals = values.length;

		for (int i = 0; i < numVals; i++)
		{
			if (type.startsWith("[") && !"[Ljava.lang.Object;".equals(type))
			{
				obj = values;

				break;
			}
			else
			{
				if (i == index)
				{
					obj = values[i];

					break;
				}
			}
		} // i

		return (T)obj;
	}

	public static boolean getBoolVal(Object var2)
	{
		return getBoolVal(String.valueOf(var2));
	}

	public static boolean getBoolVal(String var2)
	{
		return (var2.equals("1") || var2.equals("\"1\"") ||
		var2.equalsIgnoreCase("true"));
	}

	public static String getDate()
	{
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = formatter.format(currentDate.getTime());

		return dateNow;
	}

	/**
	 * Retrieves system value QDATFMT for RTVDTFMT.
	 *
	 * @return Date format
	 * @since (2013-04-15.12:36:51)
	 */
	public static String getDateFormat()
	{
		String dateFormat;

		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ResourceBundle bundle =
				facesCtx.getApplication().getResourceBundle(facesCtx, "config");
			dateFormat = bundle.getString("dateFormat");
		}
		catch (Exception e)
		{
			return "ISO";
		}

		return getRpgDateFormat(dateFormat);
	}

	public static String getDay()
	{
		return "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the entity file description.
	 *
	 * @param shortName file name
	 * @return entity file description
	 */
	public static String getEntityLongName(String shortName)
	{
		try
		{
			if (bundleEntities == null)
			{
				FacesContext facesCtx = FacesContext.getCurrentInstance();
				bundleEntities = facesCtx.getApplication()
										  .getResourceBundle(facesCtx,
						"entities");
			}

			return bundleEntities.getString(shortName);
		}
		catch (Exception e)
		{
			return shortName;
		}
	}

	/**
	 * Gets index of line where fun call ends.
	 *
	 * @param line
	 * @param bfn
	 * @since (2012-04-27.08:10:51)
	 */
	public static int getFunEndIndex(String line, String bfn)
	{
		int indx = line.toLowerCase().indexOf(bfn);

		if (indx == -1)
		{
			return -1;
		}

		char linec[] = line.toCharArray();
		int len = linec.length;
		int numOpenBkts = 0;
		int numCloseBkts = 0;

		for (int i = indx + bfn.length(); i < len; i++)
		{
			if (linec[i] == '(')
			{
				numOpenBkts++;
			}
			else if (linec[i] == ')')
			{
				numCloseBkts++;

				if (numOpenBkts == numCloseBkts)
				{
					return i;
				}
			}
		} // i

		return -1;
	}

	/**
	 * Return the long name of function suffix.
	 *
	 * @param funName
	 * @return long name of function
	 * @since (2012-07-22.08:33:36)
	 */
	private static String getLongFunSuffix(String funName)
	{
		prefNames.put("01D", "EntryPanel");
		prefNames.put("01G", "Grid");
		prefNames.put("02D", "Panel");

		if ((funName == null) || (funName.length() == 0))
		{
			return funName;
		}

		String suffix = funName.substring(funName.length() - 3);
		String suffixVal;

		try
		{
			suffixVal = prefNames.get(suffix.toUpperCase());
		}
		catch (MissingResourceException mre)
		{
			int suffix1 = toInt(suffix.substring(0, 2), 10, 0);
			String suffix2 = suffix.substring(2, 3);

			if ("G".equals(suffix2) && (suffix1 > 1))
			{
				suffix = "01" + suffix2;
			}
			else if ("D".equals(suffix2) && (suffix1 > 2))
			{
				suffix = "02" + suffix2;
			}

			try
			{
				suffixVal = prefNames.get(suffix.toUpperCase());
				suffixVal = suffixVal + suffix1;
			}
			catch (Exception e)
			{
				return funName;
			}
		}

		return suffixVal;
	}

	/**
	 * Returns the long name of method.
	 *
	 * @param funName
	 * @param pgmName
	 * @return long name of method
	 * @since (2012-07-22.11:32:46)
	 */
	public static String getLongMethodName(String funName, String pgmName)
	{
		funName = funName.toUpperCase();

		String methodName = funName.toLowerCase();

		if (funName.startsWith(pgmName) &&
				(
					funName.endsWith("L") || funName.endsWith("D") ||
					funName.endsWith("V") || funName.endsWith("P") ||
					funName.endsWith("G") || funName.endsWith("X")
				))
		{
			// TRFFDC05D_AV, TRFFDC05D_BV in TRFFDC in CNVXA
			int indxU =
				(pgmName.indexOf('_') == -1) ? funName.indexOf('_') : (-1);
			int indx = (indxU != -1) ? indxU : (funName.length() - 1);
			String fun = funName.substring(0, indx);
			char lastChar = funName.charAt(funName.length() - 1);
			String suffix = getLongFunSuffix(fun);

			if (indxU != -1)
			{
				// TRFFDC05D_AV, TRFFDC05D_BV in TRFFDC in CNVXA
				suffix += funName.charAt(indxU + 1);
			}

			if (!suffix.equals(fun))
			{
				char ch = suffix.charAt(0);
				methodName = Character.toLowerCase(ch) + suffix.substring(1);

				if ((lastChar == 'L') || (lastChar == 'D'))
				{
					methodName += "Display";
				}
				else if ((lastChar == 'V') || (lastChar == 'P'))
				{
					methodName += "Process";
				}
				else if (lastChar == 'G')
				{
					methodName += "Initialize";
				}
				else
				{
					// X
					methodName += "Exit";
				}
			}
		}

		return methodName;
	}

	/**
	 * Returns the long name for the Program or Entity.
	 *
	 * @param shortName Program/Entity name
	 * @param type object type
	 * @return long name or null if key not found or if no object for the given
	 *        key can be found
	 * @since (2013-04-12.15:56:46)
	 */
	public static String getLongObjectName(String shortName, String type)
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String propsName =
			"*PGM".equalsIgnoreCase(type) ? "prefNames" : "entities";

		if (facesCtx != null)
		{
			if ("prefNames".equals(propsName))
			{
				return getPrefLongName(shortName);
			}
			else
			{
				return getEntityLongName(shortName);
			}
		}
		else
		{
			ClassLoader loader = Utils.class.getClassLoader();
			InputStream inputStream =
				loader.getResourceAsStream(propsName + ".properties");

			Properties props = new Properties();

			try
			{
				props.load(inputStream);
			}
			catch (IOException e)
			{
				return shortName;
			}

			return props.getProperty(shortName);
		}
	}

	/**
	 * Loads the localized dynamic message from the Messages_en_GB.properties
	 * for the specified key.
	 *
	 * @param key key to search the message
	 * @return message corresponding to the key
	 * @since (2008-07-30.16:29:23)
	 */
	public static String getMessage(String key)
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		// Look up the requested message text
		String text = null;

		try
		{
			ResourceBundle bundle =
				ResourceBundle.getBundle("messages",
					facesCtx.getViewRoot().getLocale());
			text = bundle.getString(key);
		}
		catch (Exception e)
		{
			logMessage(e.toString());
		}

		return text;
	}

	public static String getMonth()
	{
		return "" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
	}

	public static String getPrefLongName(String shortName)
	{
		try
		{
			if (bundlePrefNames == null)
			{
				FacesContext facesCtx = FacesContext.getCurrentInstance();
				bundlePrefNames = facesCtx.getApplication()
										  .getResourceBundle(facesCtx,
						"prefNames");
			}

			return bundlePrefNames.getString(shortName);
		}
		catch (Exception e)
		{
			return shortName;
		}
	}

	public static String getYear()
	{
		return "" + Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * Converts data from Hex (EBCDIC) to String (ASCII).
	 *
	 * @param hex Hex data in 8-bit character encoding
	 * @return String equivalent data in 7-bit character encoding
	 * @since (2012-06-13.15:28:16)
	 */
	public static String hex2Str(String hex)
	{
		if ((hex == null) || (hex.length() == 0))
		{
			return hex;
		}

		if (hex.startsWith("X'"))
		{
			// X'7D'
			hex = hex.substring(2, hex.length() - 1);
		}

		StringBuffer ascii = new StringBuffer();
		int len = hex.length();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < len; i += 2)
		{
			// Grab the hex in pairs
			String output = hex.substring(i, (i + 2));

			// Convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			ascii.append(decimal);
		}

		return ascii.toString();
	}

	/**
	 * Checks if String is blank.
	 *
	 * @param str String
	 * @return true if String is blank
	 * @since (2008-12-23.17:59:41)
	 */
	public static boolean isBlanks(Object str)
	{
		return (str == null) || (String.valueOf(str).trim().length() == 0);
	}

	/**
	 * Checks if BigDecimal is zero.
	 *
	 * @param bd BigDecimal
	 * @return true if BigDecimal is zero
	 * @since (2015-01-06.15:30:11)
	 */
	public static boolean isZeros(BigDecimal bd)
	{
		return (bd != null) && (bd.compareTo(BigDecimal.ZERO) == 0);
	}

	/**
	 * Checks if String is zero.
	 *
	 * @param str String
	 * @return true if String is zero
	 * @since (2009-01-23.13:28:41)
	 */
	public static boolean isZeros(Object str)
	{
		return (toInt(str, 10, -1) == 0);
	}

	/**
	 * Returns the length of an ArrayList.
	 *
	 * @param alSource ArrayList whose length is to be known
	 * @return length of the ArrayList
	 * @since (2003-05-11.12:07:01)
	 */
	public static int length(List<?> alSource)
	{
		return (alSource == null) ? 0 : alSource.size();
	}

	/**
	 * Returns the length of an Object.
	 *
	 * @param obj Object whose length is to be known
	 * @return length of an Object
	 * @since (2002-08-09.10:46:45)
	 */
	public static int length(Object obj)
	{
		if (obj == null)
		{
			return 0;
		}

		if (isUserDefOrArr(obj.getClass()))
		{
			Class<?> cls = obj.getClass();

			if (cls.isArray())
			{
				return Array.getLength(obj);
			}
			else
			{
				return obj.toString().length();
			}
		}

		int len = 0;
		String type = (String)class2Name.get(obj.getClass());

		if (type.equalsIgnoreCase("boolean") ||
				type.equalsIgnoreCase("java.lang.Boolean"))
		{
			len = 1;
		}
		else if (type.equalsIgnoreCase("float") ||
				type.equalsIgnoreCase("java.lang.Float"))
		{
			len = 4;
		}
		else if (type.equalsIgnoreCase("double") ||
				type.equalsIgnoreCase("java.lang.Double"))
		{
			len = 8;
		}
		else
		{
			len = String.valueOf(obj).length();
		}

		return len;
	}

	/**
	 * Returns the length of a double dimension array.
	 *
	 * @param array array whose length is to be known
	 * @return length of the array
	 * @since (2002-08-09.10:50:53)
	 */
	public static int length(Object array[])
	{
		return (array == null) ? 0 : array.length;
	}

	/**
	 * Returns the length of a String.
	 *
	 * @param strSource string whose length is to be known
	 * @return length of the String
	 * @since (2002-08-13.10:20:29)
	 */
	public static int length(String strSource)
	{
		return (strSource == null) ? 0 : strSource.length();
	}

	/**
	 * Returns the position of the value passed in the parameter, in the array
	 * given in the parameter, starting the search from the given position.
	 *
	 * @param lookUpVal value whose position is to be searched
	 * @param lookUpArr array in which the position of the value is to be
	 *        searched
	 * @param fromPos index from which to start the search
	 * @return index of the value in the given source array.
	 */
	public static int lookUp(char lookUpVal, char lookUpArr[], int fromPos)
	{
		int returnPos = -1;
		int len = (lookUpArr == null) ? 0 : lookUpArr.length;

		for (int i = fromPos; i < len; i++)
		{
			if (lookUpVal == lookUpArr[i])
			{
				returnPos = i;

				break;
			}
		}

		return returnPos;
	}

	public static int lookUp(int opCode, int lookUpVal, int lookUpArr[],
		int fromPos, int len)
	{
		return lookUpVal(opCode, lookUpVal, lookUpArr, fromPos,
			(fromPos + len) - 1);
	}

	public static int lookUp(int lookUpVal, int lookUpArr[], int fromPos,
		int len)
	{
		return lookUp(EQ, lookUpVal, lookUpArr, fromPos, len);
	}

	public static int lookUp(int opCode, int lookUpVal, int lookUpArr[],
		int fromPos)
	{
		if (lookUpArr == null)
		{
			return 0;
		}

		return lookUpVal(opCode, lookUpVal, lookUpArr, fromPos,
			lookUpArr.length);
	}

	public static int lookUp(int opCode, int lookUpVal, int lookUpArr[])
	{
		return lookUp(opCode, lookUpVal, lookUpArr, 1);
	}

	public static int lookUp(int opCode, Object lookupStr,
		Object lookupStrArr[], int fromPos, int len)
	{
		if ((lookupStr == null) || (lookupStrArr == null))
		{
			return 0;
		}

		return lookupStrInStrArr(opCode, lookupStr, lookupStrArr, fromPos,
			fromPos + len);
	}

	public static int lookUp(int opCode, Object lookupStr,
		Object lookupStrArr[], int fromPos)
	{
		if (lookupStrArr == null)
		{
			return 0;
		}

		return lookUp(opCode, lookupStr, lookupStrArr, fromPos,
			lookupStrArr.length);
	}

	public static int lookUp(int opCode, Object lookupStr,
		Object lookupStrArr[])
	{
		return lookUp(opCode, lookupStr, lookupStrArr, 1);
	}

	public static int lookUp(Object lookupStr, Object lookupStrArr[],
		int fromPos, int len)
	{
		return lookUp(EQ, lookupStr, lookupStrArr, fromPos, len);
	}

	public static int lookUp(Object lookupStr, Object lookupStrArr[],
		int fromPos)
	{
		if (lookupStrArr == null)
		{
			return 0;
		}

		return lookUp(EQ, lookupStr, lookupStrArr, fromPos);
	}

	public static int lookUp(Object lookupStr, Object lookupStrArr[])
	{
		return lookUp(EQ, lookupStr, lookupStrArr, 1);
	}

	/**
	 * Returns the position of the value passed in the parameter, in the array
	 * given in the parameter, starting the search from the given position.
	 *
	 * @param opCode Operator code
	 * @param lookUpVal value whose position is to be searched
	 * @param lookUpArr array in which the position of the value is to be
	 *        searched
	 * @param fromPos index from which to start the search
	 * @param toPos position (exclusive) on which to end the search
	 * @return index of the value in the given source array.
	 */
	public static int lookUpVal(int opCode, int lookUpVal, int lookUpArr[],
		int fromPos, int toPos)
	{
		int returnPos = -1;

		toPos = (lookUpArr == null) ? 0
									: (
				(toPos > lookUpArr.length) ? lookUpArr.length : toPos
			);

		int diffFrmClosest = Integer.MAX_VALUE;

		for (int i = fromPos; i < toPos; i++)
		{
			boolean equals = false;

			switch (opCode)
			{
			case EQ:
			{
				equals = lookUpArr[i] == lookUpVal;

				break;
			}

			case GT:
			{
				equals = lookUpArr[i] > lookUpVal;

				break;
			}

			case GE:
			{
				equals = lookUpArr[i] >= lookUpVal;

				break;
			}

			case LT:
			{
				equals = lookUpArr[i] < lookUpVal;

				break;
			}

			case LE:
			{
				equals = lookUpArr[i] <= lookUpVal;

				break;
			}
			}

			if (equals)
			{
				int d = Math.abs(lookUpVal - lookUpArr[i]);

				if (diffFrmClosest > d)
				{
					diffFrmClosest = d;
					returnPos = i;
				}

				if (opCode == EQ)
				{
					break;
				}
			}
		} // i

		//return returnPos + 1;
		return returnPos;
	}

	/**
	 * Returns the position of the source string in the array specified in the
	 * parameter, starting from the source position to the destination position.
	 *
	 * @param opCode Operator code
	 * @param lookupStr source string
	 * @param lookupStrArr array in which to look up the given string
	 * @param fromPos position from which to start the search
	 * @param toPos position (exclusive) on which to end the search
	 * @return position of the source string in the given array
	 * @since (2006-12-02.14:21:43)
	 */
	public static int lookupStrInStrArr(int opCode, Object lookupStr,
		Object lookupStrArr[], int fromPos, int toPos)
	{
		// for the lookupStrArr can be String array as well as StringBuilder
		// array
		int returnPos = -1;

		if ((lookupStr == null) || (lookupStrArr == null))
		{
			return returnPos;
		}

		int len = Math.min(toPos, lookupStrArr.length);

		String strTolookUp;

		if (lookupStr instanceof CharSequence || lookupStr instanceof Number)
		{
			strTolookUp = String.valueOf(lookupStr).trim();
		}
		else
		{
			strTolookUp = objectToString(lookupStr).trim();
		}

		int diffFrmClosest = Integer.MAX_VALUE;

		for (int i = fromPos; i < len; i++)
		{
			boolean equals = false;

			switch (opCode)
			{
			case EQ:
			{
				equals = String.valueOf(lookupStrArr[i]).trim()
							   .equalsIgnoreCase(strTolookUp);

				break;
			}

			case GT:
			{
				equals = String.valueOf(lookupStrArr[i]).trim()
							   .compareToIgnoreCase(strTolookUp) > 0;

				break;
			}

			case GE:
			{
				equals = String.valueOf(lookupStrArr[i]).trim()
							   .compareToIgnoreCase(strTolookUp) >= 0;

				break;
			}

			case LT:
			{
				equals = String.valueOf(lookupStrArr[i]).trim()
							   .compareToIgnoreCase(strTolookUp) < 0;

				break;
			}

			case LE:
			{
				equals = String.valueOf(lookupStrArr[i]).trim()
							   .compareToIgnoreCase(strTolookUp) <= 0;

				break;
			}
			}

			if (equals)
			{
				int d =
					Math.abs(String.valueOf(lookupStr)
								   .compareToIgnoreCase(String.valueOf(
								lookupStrArr[i]).trim()));

				if (diffFrmClosest > d)
				{
					diffFrmClosest = d;
					returnPos = i;
				}

				if (opCode == EQ)
				{
					break;
				}
			}
		} // i

		return returnPos;
	}

	/**
	 * Returns the position of the source string in array given in the
	 * parameter, starting the lookup from the position specified.
	 *
	 * @param lookupStr source string
	 * @param lookupStrArr array in which to lookup the source string.
	 * @param fromPos position from which to start the look up.
	 * @return position of the source string in given array
	 * @since (2000-04-24.14:21:43)
	 */
	public static int lookupStrInStrArr(String lookupStr,
		String lookupStrArr[], int fromPos)
	{
		int returnPos = -1;

		if ((lookupStr == null) || (lookupStrArr == null))
		{
			return returnPos;
		}

		int len = lookupStrArr.length;

		for (int i = fromPos; i < len; i++)
		{
			if (lookupStr.equalsIgnoreCase(lookupStrArr[i]))
			{
				returnPos = i;

				break;
			}
		}

		return returnPos;
	}

	/**
	 * Returns the position of the given source string in the array passed in
	 * the parameter, starting from the source position, considering the case if
	 * specified in the parameter.
	 *
	 * @param lookupStr source string
	 * @param lookupStrArr array in which to look up the given string
	 * @param fromPos position from which to start the search
	 * @param bEquals whether to consider the case or not.
	 * @return position of the source string in given array
	 * @since (2001-09-05.10:31:29)
	 */
	public static int lookupStrInStrArr(String lookupStr,
		String lookupStrArr[], int fromPos, boolean bEquals)
	{
		int returnPos = -1;

		if ((lookupStr == null) || (lookupStrArr == null))
		{
			return returnPos;
		}

		int len = lookupStrArr.length;

		for (int i = fromPos; i < len; i++)
		{
			boolean bResult =
				(bEquals) ? lookupStr.equals(lookupStrArr[i])
						  : lookupStr.equalsIgnoreCase(lookupStrArr[i]);

			if (bResult)
			{
				returnPos = i;

				break;
			}
		} // i

		return returnPos;
	}

	public static void saveRetVals(Object retVals[], Object... values)
	{
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				retVals[i] = values[i];
			}
		}
	}

	public static int search(Object srch, Object src, int start)
	{
		if ((src == null) || (srch == null) || (start < 1))
		{
			return 0;
		}

		return src.toString().indexOf(srch.toString(), start - 1) + 1;
	}

	public static int search(Object srch, Object src)
	{
		return search(srch, src, 1);
	}

	/**
	 * Forms a string containing all <code>val</code>.
	 *
	 * @param val value to be appended in a string
	 * @param len number of elements
	 * @return string containing all <code>strVal</code> value
	 * @since (2009-05-13.12:51:53)
	 */
	public static String setAll(String val, int len)
	{
		if ((val == null) || (val.length() == 0))
		{
			return val;
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < len; i++)
		{
			sb.append(val);
		}

		return sb.toString();
	}

	/**
	 * Sets argument value.
	 *
	 * @param values variable arguments
	 * @param index index of array
	 * @param obj value
	 * @since (2012-04-26.15:55:40)
	 */
	public static void setArg(Object values[], int index, Object obj)
	{
		if ((values != null) && (index < values.length))
		{
			values[index] = obj;
		}
	}

	/**
	 * Converts data from String (ASCII) to Hex (EBCDIC).
	 *
	 * @param str String data in 7-bit character encoding
	 * @return Hex data in 8-bit character encoding
	 * @since (2012-06-13.19:18:16)
	 */
	public static String str2Hex(String str)
	{
		if ((str == null) || (str.length() == 0))
		{
			return str;
		}

		return Integer.toHexString(Integer.parseInt(str));
	}

	/**
	 * Converts String to signed double.
	 *
	 * @param strToConvert String to convert
	 * @return double value after converting
	 * @since (2008-11-11.12:50:33)
	 */
	public static double toDouble(String strToConvert)
	{
		return NumberFormatter.toDouble(strToConvert, 10, 0);
	}
}