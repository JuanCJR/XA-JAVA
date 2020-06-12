package com.databorough.utils;

import java.lang.reflect.Array;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.StringTokenizer;
import java.util.Vector;

import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DateTimeConverter.toDate;
import static com.databorough.utils.DateTimeConverter.toTime;
import static com.databorough.utils.ReflectionUtils.isUserDefOrArr;

/**
 * Utility class for <code>String</code> related operations.
 *
 * @author Amit Arya
 * @since (2003-01-28.10:54:16)
 */
public final class StringUtils
{
	public static final char ON = '1';
	public static final char OFF = '0';

	/**
	 * The StringUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private StringUtils()
	{
		super();
	}

	public static String all(String toRepeat, int length)
	{
		return repeatString(toRepeat, length);
	}

	public static String blanks(int length)
	{
		return repeatString(" ", length);
	}

	public static int checkChar(Object compStr, Object str, int startPos)
	{
		if (compStr instanceof String || compStr instanceof StringBuilder)
		{
			if ((compStr == null) || (Utils.length(compStr) == 0))
			{
				return 0;
			}

			if ((str == null) || (Utils.length(str) == 0))
			{
				return 0;
			}
		}

		startPos -= 1;

		String param1 = String.valueOf(compStr);
		String param2 = String.valueOf(str);
		char ch[] = param2.toCharArray();
		int len = ch.length;
		int ret = 0;

		for (int i = 0; i < len; i++)
		{
			int indx = param1.indexOf(ch[i], startPos);

			if (indx == -1)
			{
				ret = i + 1;

				break;
			}

			if (i == len)
			{
				ret = 0;
			}
		} // i

		return ret;
	}

	public static int checkChar(Object compStr, Object str)
	{
		return checkChar(compStr, str, 0);
	}

	public static int checkCharReverse(Object compStr, Object str, int startPos)
	{
		if (compStr instanceof String || compStr instanceof StringBuilder)
		{
			if ((compStr == null) || (Utils.length(compStr) == 0))
			{
				return 0;
			}

			if ((str == null) || (Utils.length(str) == 0))
			{
				return 0;
			}

			String param1 = String.valueOf(compStr);
			String param2 = String.valueOf(str);

			return checkCharReverse(param1, param2, startPos);
		}

		return 0;
	}

	public static int checkCharReverse(Object compStr, Object str)
	{
		return checkCharReverse(compStr, str, 0);
	}

	public static int checkCharReverse(String compStr, String str, int startPos)
	{
		int pos = -1;

		if ((compStr == null) || (Utils.length(compStr) == 0))
		{
			return 0;
		}

		if ((str == null) || (Utils.length(str) == 0))
		{
			return 0;
		}

		startPos -= 1;

		char ch[] = compStr.toCharArray();
		int len = ch.length;

		for (int i = len - 1; i >= 0; i--)
		{
			int indx = str.indexOf(ch[i], startPos);

			if (indx == -1)
			{
				pos = i + 1;

				break;
			}
		} // i

		return pos;
	}

	public static int compareTo(Object tgt, Object src)
	{
		if ((tgt == null) || (src == null))
		{
			return Integer.MIN_VALUE;
		}

		return String.valueOf(tgt).trim().compareTo(String.valueOf(src).trim());
	}

	/**
	 * Counts the number of occurrence of substring in the source string.
	 *
	 * @param str source string
	 * @param subStr sub string
	 * @return number of occurrence of substring in the source string
	 * @since (2004-01-16.14:47:58)
	 */
	public static int countMatches(String str, String subStr)
	{
		if ((str == null) || (subStr == null))
		{
			return 0;
		}

		int len = subStr.length();

		int count = 0;
		int idx = 0;

		while ((idx = indexOf(str, subStr, idx, false)) != -1)
		{
			count++;
			idx += len;
		}

		return count;
	}

	/**
	 * Removes all instances of string to remove in a given source string.
	 * <P>
	 * This method became a necessity since StringBuffer supports only
	 * delete(int start, int end) since JDK1.2.
	 *
	 * @param strSource source string
	 * @param strToRemove string to remove
	 * @return string after removing all instances of the string to remove
	 * @since (2000-03-16.17:06:58)
	 */
	public static String deleteString(String strSource, String strToRemove)
	{
		return deleteString(strSource, strToRemove, -2);
	}

	/**
	 * Removes the given instance of the string to remove from the source
	 * string.
	 * <ul>
	 * <li> -2 ->All instance</li>
	 * <li> -1 -> Last instance </li>
	 * <li>0 -> No instance</li>
	 * <li> 1 -> First instance</li>
	 * </ul>
	 * and so on.
	 * <p>
	 * This method became a necessity since StringBuffer supports only
	 * delete(int start, int end) since JDK1.2.
	 *
	 * @param strSource source string
	 * @param strToRemove string to remove
	 * @param instance flag indicating the instance to remove
	 * @return String after removing the given instance
	 * @since (2004-10-27.10:54:27)
	 */
	private static String deleteString(String strSource, String strToRemove,
		int instance)
	{
		// If the string source is null then simply return the strSource
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		// If the string to remove is null then simply return the strSource
		if ((strToRemove == null) || strToRemove.equals(""))
		{
			return strSource;
		}

		// If none of the instances are to be replaced then simply return the
		// strSource
		if (instance == 0)
		{
			return strSource;
		}

		// If none of the instances could be found then simply return 
		// the strSource
		int numInstances = countMatches(strSource, strToRemove);

		if (numInstances == 0)
		{
			return strSource;
		}

		if (instance > numInstances)
		{
			return strSource;
		}

		// Vars to use
		StringBuffer strToReturn = new StringBuffer();
		int fromIndex = 0;
		int indexOfStrToRemove;
		int lenOfStrToRemove = strToRemove.length();

		int instanceCount = 0;

		while ((
					indexOfStrToRemove = indexOf(strSource, strToRemove,
							fromIndex, false)
				) != -1)
		{
			if ((instanceCount == numInstances) || (instanceCount == instance))
			{
				break;
			}

			// Append the normal text i.e. text other than strToRemove to
			// strToReturn
			strToReturn.append(
				strSource.substring(fromIndex, indexOfStrToRemove));

			// Skip/bypass the strToRemove
			fromIndex = indexOfStrToRemove + lenOfStrToRemove;

			if ((instance == -2) ||
					(
						(instance == -1) &&
						(instanceCount == (numInstances - 1))
					) || (instanceCount == (instance - 1)))
			{
			}
			else
			{
				// Append to strToReturn the strToRemove
				strToReturn.append(strToRemove);
			}

			instanceCount++;
		}

		// For any normal text left in the strSource
		if (fromIndex < len)
		{
			strToReturn.append(strSource.substring(fromIndex, len));
		}

		return strToReturn.toString();
	}

	public static boolean equal(Object tgt, Object src)
	{
		if ((tgt == null) || (src == null))
		{
			return false;
		}

		String tgtStr =
			isUserDefOrArr(tgt.getClass()) ? objectToString(tgt)
										   : String.valueOf(tgt);
		String srcStr =
			isUserDefOrArr(src.getClass()) ? objectToString(src)
										   : String.valueOf(src);
		tgtStr = tgtStr.trim();
		srcStr = srcStr.trim();

		// New code - 17-05-13
		if (("1".equals(srcStr) && "true".equalsIgnoreCase(tgtStr)) ||
				("1".equals(tgtStr) && "true".equalsIgnoreCase(srcStr)) ||
				("0".equals(srcStr) && "false".equalsIgnoreCase(tgtStr)) ||
				("0".equals(tgtStr) && "false".equalsIgnoreCase(srcStr)))
		{
			return true;
		}

		return srcStr.equalsIgnoreCase(tgtStr);
	}

	public static char getIndicator(String str, int index)
	{
		return str.charAt(index);
	}

	public static String getIndicatorStr(String str, int index)
	{
		return "" + getIndicator(str, index);
	}

	public static String hiChar(int len)
	{
		return hiLoChar(len, false);
	}

	private static String hiLoChar(int len, boolean isLoval)
	{
		StringBuilder hiStr = new StringBuilder();
		char repeat = isLoval ? Character.MIN_VALUE : Character.MAX_VALUE;

		for (int i = 0; i < len; i++)
		{
			hiStr.append(repeat);
		}

		return String.valueOf(hiStr);
	}

	/**
	 * Returns the index within this string of the first occurrence of the
	 * specified substring, starting at the specified index, not ignoring case
	 * if bEquals is true.
	 *
	 * @param str source string
	 * @param subStr source sub string
	 * @param fromIndex index to start with
	 * @param bEquals whether to ignore case or not
	 * @return index of the first occurrence of the specified substring
	 * @since (2005-02-04.11:27:14)
	 */
	private static int indexOf(String str, String subStr, int fromIndex,
		boolean bEquals)
	{
		if ((str == null) || (subStr == null))
		{
			return -1;
		}

		if (bEquals)
		{
			return str.indexOf(subStr, fromIndex);
		}
		else
		{
			return str.toLowerCase().indexOf(subStr.toLowerCase(), fromIndex);
		}
	}

	/**
	 * Returns the index within this string of the first occurrence of any
	 * digit.
	 *
	 * @param strSource source string
	 * @return index of the first occurrence of the specified digit
	 * @since (2004-06-23.16:48:03)
	 */
	public static int indexOfFirstDigit(String strSource)
	{
		return indexOfFirstDigit(strSource, 0);
	}

	/**
	 * Returns the index within this string of the first occurrence of any
	 * digit, starting the search at the specified index.
	 *
	 * @param strSource source string
	 * @param fromIndex index to start with
	 * @return index of the first occurrence of the specified digit
	 * @since (2004-06-23.16:47:16)
	 */
	private static int indexOfFirstDigit(String strSource, int fromIndex)
	{
		// If strSource is null then simply return -1
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return -1;
		}

		char chSource[] = strSource.toCharArray();

		for (int i = fromIndex; i < len; i++)
		{
			if (Character.isDigit(chSource[i]))
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns the index within this string of the first occurrence of any
	 * letter.
	 *
	 * @param strSource source string
	 * @return index of the first occurrence of the specified letter
	 * @since (2004-06-16.10:24:53)
	 */
	public static int indexOfFirstLetter(String strSource)
	{
		return indexOfFirstLetter(strSource, 0);
	}

	/**
	 * Returns the index within this string of the first occurrence of any
	 * letter, starting the search at the specified index.
	 *
	 * @param strSource source string
	 * @param fromIndex index to start with
	 * @return index of the first occurrence of the specified letter
	 * @since (2004-06-16.10:29:29)
	 */
	private static int indexOfFirstLetter(String strSource, int fromIndex)
	{
		// If strSource is null then simply return -1
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return -1;
		}

		char chSource[] = strSource.toCharArray();

		for (int i = fromIndex; i < len; i++)
		{
			if (Character.isLetter(chSource[i]))
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns the index within this string of the first occurrence of any non
	 * letter and non digit.
	 *
	 * @param strSource source string
	 * @return index of the first occurrence of any non letter and non digit
	 * @since (2006-03-06.16:34:42)
	 */
	public static int indexOfFirstNonLetterAndDigit(String strSource)
	{
		return indexOfFirstNonLetterAndDigit(strSource, 0);
	}

	/**
	 * Returns the index within this string of the first occurrence of any non
	 * letter and non digit, starting the search at the specified index.
	 *
	 * @param strSource source string
	 * @param fromIndex index to start with
	 * @return index of the first occurrence of any non letter and non digit
	 * @since (2006-03-06.16:34:11)
	 */
	private static int indexOfFirstNonLetterAndDigit(String strSource,
		int fromIndex)
	{
		// If strSource is null then simply return -1
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return -1;
		}

		char chSource[] = strSource.toCharArray();

		for (int i = fromIndex; i < len; i++)
		{
			if (!Character.isLetterOrDigit(chSource[i]))
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Checks whether the String contains only digit characters.
	 *
	 * @param strSource source string
	 * @return true if string contains only digits otherwise false
	 * @since (2003-04-17.16:49:57)
	 */
	public static boolean isAllDigit(String strSource)
	{
		// If strSource is null then simply return false
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return false;
		}

		char chSource[] = strSource.toCharArray();

		for (int i = 0; i < len; i++)
		{
			if (!Character.isDigit(chSource[i]))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks whether the String contains only this letter.
	 *
	 * @param strSource source string
	 * @param letter source letter
	 * @return true if string contains specified letter otherwise false
	 * @since (2003-01-29.15:27:44)
	 */
	public static boolean isAllLetter(String strSource, char letter)
	{
		// If the string source is null then simply return false
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return false;
		}

		char chSource[] = strSource.toCharArray();

		for (int i = 0; i < len; i++)
		{
			if (chSource[i] != letter)
			{
				return false;
			}
		} // i

		return true;
	}

	/**
	 * Joins array via a specified pattern.
	 *
	 * @param array array containing objects to join
	 * @param delimiters delimiters to separate the objects.
	 * @return string representing objects joined via delimiters
	 * @since (2001-04-06.11:27:18)
	 */
	public static String join(Object array, String delimiters)
	{
		return join(array, delimiters, 0);
	}

	/**
	 * Joins two string arrays via a specified pattern.
	 * <p>
	 * The join() method is the antithesis of the split() method. Rather than
	 * splitting a string into an array of smaller strings, join() assembles an
	 * array of smaller strings into one big string. Each substring is joined to
	 * its partners via a specified pattern. For example, if we had the strings
	 * "one," "two," and "three" in an array, and we specified that pattern as
	 * "precedes," then the output of the call to join() would be
	 * "oneprecedestwoprecedesthree"
	 *
	 * @param array array containing objects to join
	 * @param delimiters delimiters to separate the objects.
	 * @param numGroups number of groups
	 * @return string representing objects joined via delimiters
	 * @since (2004-06-18.15:42:37)
	 */
	private static String join(Object array, String delimiters, int numGroups)
	{
		StringBuffer strToReturn = null;

		// If array is null then simply return null
		int len = Utils.length(array);

		if (len == 0)
		{
			return null;
		}

		// If delimiters is null then simply set it to ","
		if (delimiters == null)
		{
			delimiters = ",";
		}

		boolean atleastOne = false;
		int j = 0;

		for (int i = 0; i < len; i++)
		{
			Object obj = Array.get(array, i);

			if (obj == null)
			{
				continue;
			}

			if (atleastOne)
			{
				strToReturn.append(delimiters);

				if ((numGroups > 0) && ((j % numGroups) == 0))
				{
					// End of group
					strToReturn.append(Utils.LINE_SEPARATOR);
				}
			}
			else
			{
				strToReturn = new StringBuffer();
			}

			strToReturn.append(obj);

			atleastOne = true;
			j++;
		} // i

		return (strToReturn == null) ? null : strToReturn.toString();
	}

	public static String loChar(int len)
	{
		return hiLoChar(len, true);
	}

	public static String ltrim(StringBuilder strToLTrim)
	{
		return ltrim(String.valueOf(strToLTrim));
	}

	/**
	 * Trims leading white spaces.
	 *
	 * @param strToLTrim string array to trim
	 * @return array containing strings with trimmed white spaces
	 * @since (2000-12-18.09:51:04)
	 */
	public static String ltrim(String strToLTrim)
	{
		// If strToLTrim is null then simply return strToLTrim
		int len = Utils.length(strToLTrim);

		if (len == 0)
		{
			return strToLTrim;
		}

		char chArrToLTrim[] = strToLTrim.toCharArray();

		int i;

		for (i = 0; i < len; i++)
		{
			char ch = chArrToLTrim[i];
			int chAscii = ch;

			if (!(Character.isSpaceChar(ch) || (chAscii == 0)))
			{
				break;
			}
		}

		if (i == 0)
		{
			return strToLTrim;
		}

		return strToLTrim.substring(i, len);
	}

	/**
	 * If length of strSource is less than final length parameter passed in, pad
	 * those many string to pad in the beginning / end of strSource.
	 *
	 * @param strSource source string
	 * @param strToPad string to pad
	 * @param finalLen final length of the string
	 * @param bBegin whether to pad in the beginning or end
	 * @return string with padded string
	 * @since (2000-12-18.10:11:55)
	 */
	public static String padStringWithValue(String strSource, String strToPad,
		int finalLen, boolean bBegin)
	{
		// If strSource is null then make it a empty string
		if (strSource == null)
		{
			strSource = "";
		}

		// If strToPad is null then make it a empty string
		if (strToPad == null)
		{
			strToPad = "";
		}

		// If length of strSource is less than finalLen
		// pad those many strToPad's in the beginning / end of strSource
		// depending on bBegin
		strSource = StringUtils.rtrim(strSource);

		StringBuffer strToReturn = new StringBuffer(strSource);

		int j = finalLen - strSource.length();

		for (int i = 0; i < j; i++)
		{
			if (bBegin)
			{
				strToReturn.insert(0, strToPad);
			}
			else
			{
				strToReturn.append(strToPad);
			}
		} // i

		return strToReturn.toString();
	}

	public static String repeatString(String strSource, int numTimes)
	{
		// If strSource is null then simply return strSource
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		if (numTimes == 0)
		{
			return "";
		}

		if (numTimes == 1)
		{
			return strSource;
		}

		StringBuffer strToReturn = new StringBuffer(numTimes * len);

		for (int i = 0; i < numTimes; ++i)
		{
			strToReturn.append(strSource);
		}

		return strToReturn.toString();
	}

	public static String replace(String strSrc, int start, int length,
		String strToReplaceWith)
	{
		if (strSrc.length() < start)
		{
			start = strSrc.length() + 1;
		}

		return replaceStr(strSrc, start, length, strToReplaceWith, true);
	}

	public static String replace(String strSrc, int start,
		String strToReplaceWith)
	{
		return replace(strSrc, start, Utils.length(strToReplaceWith),
			strToReplaceWith);
	}

	public static String replace(String strSrc, String strToReplaceWith)
	{
		return replaceString(strSrc, 1, Utils.length(strToReplaceWith),
			strToReplaceWith);
	}

	public static String replace(Object strSrc, int start, int length,
		Object strToReplaceWith)
	{
		return replaceString(String.valueOf(strSrc), start, length,
			String.valueOf(strToReplaceWith));
	}

	public static String replace(Object strSrc, int start,
		Object strToReplaceWith)
	{
		return replace(String.valueOf(strSrc), start,
			String.valueOf(strToReplaceWith));
	}

	public static String replace(Object strSrc, Object strToReplaceWith)
	{
		return replace(String.valueOf(strSrc), 1,
			String.valueOf(strToReplaceWith));
	}

	/**
	 * Replaces the specified suffix in a given source string if this string
	 * ends with it.
	 *
	 * @param strSource source string
	 * @param strToReplace string to replace
	 * @param strToReplaceWith string to replace with
	 * @return source string with string replaced with new string
	 * @since (2004-10-27.12:19:48)
	 */
	public static String replaceEndString(String strSource,
		String strToReplace, String strToReplaceWith)
	{
		// If the string source is null then simply return the strSource
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		// If the string to replace is null then simply return the strSource
		if ((strToReplace == null) || strToReplace.equals(""))
		{
			return strSource;
		}

		// If the string to replace with is null then simply return the strSource
		// Or if strToReplaceWith is same as strToReplace then simply return the strSource
		if ((strToReplaceWith == null) ||
				strToReplace.equals(strToReplaceWith))
		{
			return strSource;
		}

		if (!strSource.endsWith(strToReplace))
		{
			return strSource;
		}

		return strSource.substring(0, len - strToReplace.length()) +
		strToReplaceWith;
	}

	public static String replaceStr(String strSrc, int start, int length,
		String strToReplaceWith, boolean bForceReplace)
	{
		int srcLen = Utils.length(strSrc);

		if (strSrc == null)
		{
			return "";
		}

		if (start <= 0 /*|| start > srcLen*/)
		{
			return strSrc;
		}

		start--;

		if (start > srcLen)
		{
			strSrc += blanks(start - srcLen);
			srcLen = start;
		}

		int dest = length + start;

		if ((dest > srcLen) && !bForceReplace)
		{
			dest = srcLen;
		}

		if ((strToReplaceWith == null) ||
				strToReplaceWith.equalsIgnoreCase("null"))
		{
			return strSrc;
		}

		if ((Utils.length(strToReplaceWith) > (dest - start)) &&
				(dest > start) && !bForceReplace)
		{
			strToReplaceWith = strToReplaceWith.substring(0, dest - start);
		}

		StringBuffer strToReturn = new StringBuffer();

		// Append the text before start
		if (start > 0)
		{
			strToReturn.append(strSrc.substring(0, start));
		}

		// Append the strToReplaceWith
		if (dest > start)
		{
			if (strToReplaceWith.length() > (dest - start))
			{
				strToReturn.append(strToReplaceWith.substring(0, dest - start));
			}
			else
			{
				strToReturn.append(strToReplaceWith);

				if (bForceReplace)
				{
					strToReturn.append(blanks(dest - start -
							strToReplaceWith.length()));
				}
			}
		}

		// Append the text after dest
		if (dest < srcLen)
		{
			strToReturn.append(strSrc.substring(dest, srcLen));
		}

		return strToReturn.toString();
	}

	public static String replaceStr(String strSrc, int start, int length,
		String strToReplaceWith)
	{
		return replaceStr(strSrc, start, length, strToReplaceWith, true);
	}

	public static String replaceStr(String strSrc, int start,
		String strToReplaceWith)
	{
		return replaceStr(strSrc, start, Utils.length(strToReplaceWith),
			strToReplaceWith);
	}

	public static String replaceStr(String strSrc, long start, int length,
		String strToReplaceWith)
	{
		return replaceStr(strSrc, (int)start, length, strToReplaceWith, true);
	}

	public static String replaceStr(Object strSrc, int start, int length,
		Object strToReplaceWith)
	{
		return replaceStr(String.valueOf(strSrc), start, length,
			String.valueOf(strToReplaceWith));
	}

	public static String replaceStr(Object strSrc, int start,
		Object strToReplaceWith)
	{
		return replaceStr(String.valueOf(strSrc), start,
			String.valueOf(strToReplaceWith));
	}

	/**
	 * Replaces all instances of character array to replace parameter with
	 * character array to replace with parameter in a given source string.
	 *
	 * @param strSource source string
	 * @param chArrToReplace character array to replace
	 * @param strToReplaceWith string to replace with
	 * @return source string with characters replaced with the characters in new
	 *         string
	 * @since (2012-08-24.11:45:44)
	 */
	public static String replaceString(String strSource, char chArrToReplace[],
		String strToReplaceWith)
	{
		// If strSource is null then simply return false
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		if ((chArrToReplace == null) || (strToReplaceWith == null))
		{
			return strSource;
		}

		StringBuffer strToReturn = new StringBuffer();

		char chArrSource[] = strSource.toCharArray();

		for (int i = 0; i < len; i++)
		{
			int indx = Utils.lookUp(chArrSource[i], chArrToReplace, 0);

			if (indx == -1)
			{
				strToReturn.append(chArrSource[i]);
			}
			else
			{
				strToReturn.append(strToReplaceWith);
			}
		} // i

		return strToReturn.toString();
	}

	/**
	 * Replaces all instances of character array to replace parameter with
	 * character array to replace with parameter in a given source string.
	 *
	 * @param strSource source string
	 * @param chArrToReplace character array to replace
	 * @param chArrToReplaceWith character array to replace with
	 * @return source string with characters replaced with the characters in new
	 *         array
	 * @since (2003-12-11.17:45:44)
	 */
	public static String replaceString(String strSource, char chArrToReplace[],
		char chArrToReplaceWith[])
	{
		// If strSource is null then simply return false
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		if ((chArrToReplace == null) || (chArrToReplaceWith == null))
		{
			return strSource;
		}

		StringBuffer strToReturn = new StringBuffer();

		char chArrSource[] = strSource.toCharArray();

		for (int i = 0; i < len; i++)
		{
			int indx = Utils.lookUp(chArrSource[i], chArrToReplace, 0);

			if (indx == -1)
			{
				strToReturn.append(chArrSource[i]);
			}
			else
			{
				strToReturn.append(chArrToReplaceWith[indx]);
			}
		} // i

		return strToReturn.toString();
	}

	/**
	 * Replaces all instances of character array to replace parameter with
	 * character array to replace with parameter in a given source string.
	 *
	 * @param strSource source string
	 * @param chArrToReplace character array to replace
	 * @param chArrToReplaceWith character array to replace with
	 * @param start the beginning index, inclusive
	 * @return source string with characters replaced with the characters in new
	 *         array
	 * @since (2003-12-11.17:45:44)
	 */
	public static String replaceString(String strSource, char chArrToReplace[],
		char chArrToReplaceWith[], int start)
	{
		// If strSource is null then simply return false
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		if ((chArrToReplace == null) || (chArrToReplaceWith == null))
		{
			return strSource;
		}

		StringBuffer strToReturn = new StringBuffer();

		char chArrSource[] = strSource.toCharArray();

		// Append the text before start
		if (start > 0)
		{
			strToReturn.append(strSource.substring(0, start));
		}

		for (int i = start; i < len; i++)
		{
			int indx = Utils.lookUp(chArrSource[i], chArrToReplace, 0);

			if (indx == -1)
			{
				strToReturn.append(chArrSource[i]);
			}
			else
			{
				strToReturn.append(chArrToReplaceWith[indx]);
			}
		} // i

		return strToReturn.toString();
	}

	/**
	 * Replaces the characters starting from source to destination with new
	 * string in a given source string.
	 * <p>
	 * This method became a necessity since StringBuffer supports replace(int
	 * start, int dest, String str) only since JDK1.2.
	 *
	 * @param strSource source string
	 * @param start int The beginning index, inclusive.
	 * @param dest int The ending index, exclusive.
	 * @param strToReplaceWith String that will replace previous contents.
	 * @return source string with characters from source to destination replaced
	 *         with new string
	 * @throws IndexOutOfBoundsException Thrown to indicate that an index of
	 *         some sort (such as to an array, to a string, or to a vector) is
	 *         out of range
	 * @since (2000-12-15.14:34:58)
	 */
	public static String replaceString(String strSource, int start, int dest,
		String strToReplaceWith)
	{
		// If the string source is null then simply return the strSource
		int len = Utils.length(strSource);

		if ((len == 0) || strSource.equalsIgnoreCase("null"))
		{
			return strSource;
		}

		// If the string to replace with is null then simply return the 
		// strSource
		if ((strToReplaceWith == null) ||
				strToReplaceWith.equalsIgnoreCase("null"))
		{
			return strSource;
		}

		// Check index bounds
		/*Utils.checkIndexBounds(start, dest, len);*/
		StringBuffer strToReturn = new StringBuffer();

		// Append the text before start
		if (start > 0)
		{
			strToReturn.append(strSource.substring(0, start));
		}

		// Append the strToReplaceWith
		strToReturn.append(strToReplaceWith);

		// Append the text after dest
		if (dest < len)
		{
			strToReturn.append(strSource.substring(dest, len));
		}

		return strToReturn.toString();
	}

	/**
	 * Replaces all instances of string to replace with new string in a given
	 * source string.
	 * <p>
	 * This method became a necessity since StringBuffer supports only
	 * replace(int start, int end, String str) since JDK1.2.
	 *
	 * @param strSource source string
	 * @param strToReplace string to replace
	 * @param strToReplaceWith string to replace with
	 * @return source string with string replaced with new string
	 * @since (2000-03-16.17:06:58)
	 */
	public static String replaceString(String strSource, String strToReplace,
		String strToReplaceWith)
	{
		return replaceString(strSource, strToReplace, strToReplaceWith, -2);
	}

	/**
	 * Replaces the given instance of string to replace with new string in a
	 * given source string. <ui>
	 * <li>-2 -> All instance</li>
	 * <li>-1 -> Last instance</li>
	 * <li> 0 -> No instance</li>
	 * <li> 1 -> First instance</li>
	 * </ui> and so on.
	 * <p>
	 * This method became a necessity since StringBuffer supports only
	 * replace(int start, int end, String str) since JDK1.2.
	 *
	 * @param strSource source string
	 * @param strToReplace string to replace
	 * @param strToReplaceWith string to replace with
	 * @param instance instance number
	 * @return source string with string replaced with new string
	 * @since (2004-09-20.10:51:22)
	 */
	private static String replaceString(String strSource, String strToReplace,
		String strToReplaceWith, int instance)
	{
		// If the string source is null then simply return the strSource
		int len = Utils.length(strSource);

		if (len == 0)
		{
			return strSource;
		}

		// If the string to replace is null then simply return the strSource
		if ((strToReplace == null) || strToReplace.equals(""))
		{
			return strSource;
		}

		// If the string to replace with is null then simply return the 
		// strSource
		if (strToReplaceWith == null)
		{
			return strSource;
		}

		// If strToReplaceWith is same as strToReplace then simply return the
		// strSource
		if (strToReplace.equals(strToReplaceWith))
		{
			return strSource;
		}

		// If none of the instances are to be replaced then simply return the
		// strSource
		if (instance == 0)
		{
			return strSource;
		}

		// If none of the instances could be found then simply return the 
		// strSource
		int numInstances = countMatches(strSource, strToReplace);

		if (numInstances == 0)
		{
			return strSource;
		}

		if (instance > numInstances)
		{
			return strSource;
		}

		// Vars to use
		StringBuffer strToReturn = new StringBuffer();
		int fromIndex = 0;
		int indexOfStrToReplace;
		int lenOfStrToReplace = strToReplace.length();

		int instanceCount = 0;

		while ((
					indexOfStrToReplace = indexOf(strSource, strToReplace,
							fromIndex, false)
				) != -1)
		{
			if ((instanceCount == numInstances) || (instanceCount == instance))
			{
				break;
			}

			// Append the normal text i.e. text other than strToReplace to
			// strToReturn
			strToReturn.append(strSource.substring(fromIndex,
					indexOfStrToReplace));

			// Skip/bypass the strToReplace
			fromIndex = indexOfStrToReplace + lenOfStrToReplace;

			if ((instance == -2) ||
					(
						(instance == -1) &&
						(instanceCount == (numInstances - 1))
					) || (instanceCount == (instance - 1)))
			{
				// Append to strToReturn the strToReplaceWith
				strToReturn.append(strToReplaceWith);
			}
			else
			{
				// Append to strToReturn the strToReplace
				strToReturn.append(strToReplace);
			}

			instanceCount++;
		}

		// For any normal text left in the strSource
		if (fromIndex < len)
		{
			strToReturn.append(strSource.substring(fromIndex, len));
		}

		return strToReturn.toString();
	}

	public static String rtrim(Object strToRTrim)
	{
		return rtrim(String.valueOf(strToRTrim));
	}

	/**
	 * Trims trailing white spaces.
	 *
	 * @param strToRTrim source string
	 * @return string with trimmed white spaces
	 * @since (2000-12-18.09:51:04)
	 */
	public static String rtrim(String strToRTrim)
	{
		// If strToRTrim is null then simply return strToRTrim
		int len = Utils.length(strToRTrim);

		if (len == 0)
		{
			return strToRTrim;
		}

		char chArrToRTrim[] = strToRTrim.toCharArray();

		int i;

		for (i = len - 1; i >= 0; i--)
		{
			char ch = chArrToRTrim[i];
			int chAscii = ch;

			if (!(Character.isSpaceChar(ch) || (chAscii == 0)))
			{
				break;
			}
		}

		if (i == (len - 1))
		{
			return strToRTrim;
		}

		return strToRTrim.substring(0, i + 1);
	}

	/**
	 * Trims trailing white spaces in a given source string. Also, sets the
	 * string to default value if the string is null or empty.
	 *
	 * @param strToRTrim source string
	 * @param strDef default string
	 * @return string with trimmed white spaces or default value
	 * @since (2003-09-10.16:35:20)
	 */
	public static String rtrim(String strToRTrim, String strDef)
	{
		String strToReturn = rtrim(strToRTrim);

		if (Utils.length(strToReturn) == 0)
		{
			strToReturn = strDef;
		}

		return strToReturn;
	}

	/**
	 * Trims trailing white spaces in a given source string array.
	 *
	 * @param strToRTrim source string array
	 * @return an array containing strings with trimmed white spaces
	 * @since (2001-09-03.09:46:24)
	 */
	public static String[] rtrim(String strToRTrim[])
	{
		// If strToRTrim is null then simply return strToRTrim
		int len = Utils.length(strToRTrim);

		if (len == 0)
		{
			return strToRTrim;
		}

		String strArrToReturn[] = new String[len];

		for (int i = 0; i < len; i++)
		{
			strArrToReturn[i] = rtrim(strToRTrim[i]);
		} // i

		return strArrToReturn;
	}

	/**
	 * Trims trailing white spaces in a given source string array. Also, sets
	 * the string to default value if the string is null or empty.
	 *
	 * @param strArrToRTrim source string array
	 * @param strDef default string array
	 * @return an array containing strings with trimmed white spaces or default
	 *         value
	 */
	public static String[] rtrim(String strArrToRTrim[], String strDef)
	{
		// If strArrToTrim is null then simply return strArrToRTrim
		int len = Utils.length(strArrToRTrim);

		if (len == 0)
		{
			return strArrToRTrim;
		}

		String strArrToReturn[] = new String[len];

		for (int i = 0; i < len; i++)
		{
			strArrToReturn[i] = trim(strArrToRTrim[i], strDef);
		} // i

		return strArrToReturn;
	}

	public static String setIndicator(String str, int index, char indicator)
	{
		StringBuilder strBuilder = new StringBuilder(str);
		strBuilder.setCharAt(index - 1, indicator);

		return strBuilder.toString();
	}

	public static void setStr(StringBuilder builder, String value)
	{
		if (builder != null)
		{
			builder.setLength(0);

			if (value != null)
			{
				builder.append(value);
			}
		}
	}

	public static void setStr(StringBuilder builder, Object value)
	{
		if ((builder != null) && (builder != value))
		{
			builder.setLength(0);

			if (value != null)
			{
				builder.append(toChar(value));
			}
		}
	}

	/**
	 * It then splits the string up into smaller strings based on the occurrence
	 * of comma, space, new line, tab, backward slash, and forward slash.
	 * <p>
	 * For example, if the string is "Five Finnish Fiddlers Finally Finished
	 * Fiddling?, then the results of the split would be Five, Finnish, and so
	 * on.
	 *
	 * @param strSource source string
	 * @return array containing split strings
	 * @since (2001-08-29.11:28:18)
	 */
	public static String[] split(String strSource)
	{
		// If the string source is null then simply return null
		if ((strSource == null) || strSource.equals(""))
		{
			return null;
		}

		StringTokenizer st = new StringTokenizer(strSource, ", \n\t\\/");
		int len = st.countTokens();

		String strArrToReturn[] = new String[len];

		for (int i = 0; i < len; i++)
		{
			strArrToReturn[i] = st.nextToken();
		} // i

		return strArrToReturn;
	}

	/**
	 * It then splits the string up into smaller strings based on the occurrence
	 * of the specified pattern.
	 * <p>
	 * For example, if the string is "Five Finnish Fiddlers Finally Finished
	 * Fiddling?" and the pattern is "Fi", then the results of the split would
	 * be several new strings containing "Fi".
	 *
	 * @param input source string
	 * @param delimiters pattern to split the string
	 * @return array containing split strings
	 * @since (2001-04-06.11:20:11)
	 */
	public static String[] split(String input, String delimiters)
	{
		return split(input, delimiters, true);
	}

	/**
	 * It then splits the string up into smaller strings based on the occurrence
	 * of the specified pattern.
	 * <p>
	 * For example, if the string is "Five Finnish Fiddlers Finally Finished
	 * Fiddling?" and the pattern is "Fi", then the results of the split would
	 * be several new strings containing "Fi".
	 *
	 * @param input source string
	 * @param delimiters pattern to split the string
	 * @param delimiterAsGroup whether the delimiter is regarded as single group
	 * @return array containing split strings
	 * @since (2004-08-14.07:58:43)
	 */
	private static String[] split(String input, String delimiters,
		boolean delimiterAsGroup)
	{
		return tokenize(input, delimiters, delimiterAsGroup);
	}

	/**
	 * Splits string in equal portions.
	 *
	 * @param strSource source string
	 * @param size size of each portion
	 * @return array containing split strings
	 * @since (2012-07-05.15:32:43)
	 */
	public static String[] split(String strSource, int size)
	{
		// If the string source is null then simply return null
		if ((strSource == null) || strSource.equals(""))
		{
			return null;
		}

		int strlen = strSource.length();
		Vector<String> v = new Vector<String>();

		for (int i = 0; i < strlen; i += size)
		{
			v.add(strSource.substring(i, Math.min(strlen, i + size)));
		}

		String toks[] = null;
		int cnt = v.size();

		if (cnt > 0)
		{
			toks = new String[cnt];
			v.copyInto(toks);
		}

		return toks;
	}

	/**
	 * Gets n characters from the middle of a string. If n characters are not
	 * available, the remainder of the string will be returned without an
	 * exception. If the string is null, null will be returned.
	 *
	 * @param strSource source string
	 * @param pos index to start with
	 * @param len length of the string to be extracted
	 * @return n characters from the middle of a string or remainder of the
	 *         string or null
	 * @since (2004-01-18.08:43:11)
	 */
	public static String subString(String strSource, int pos, int len)
	{
		pos--;

		if ((strSource == null) || (strSource.length() == 0) || (pos < 0) ||
				(len < 0) || (pos > strSource.length()))
		{
			return "";
		}

		// Check start index bounds
		if (strSource.length() <= (pos + len))
		{
			return strSource.substring(pos);
		}
		else
		{
			return strSource.substring(pos, pos + len);
		}
	}

	public static String subString(String strSource, int pos)
	{
		if (strSource == null)
		{
			return "";
		}

		return subString(strSource, pos, Utils.length(strSource));
	}

	public static String subString(StringBuilder strSource, int pos, int len)
	{
		if (strSource == null)
		{
			return "";
		}

		return subString(String.valueOf(strSource), pos, len);
	}

	public static String subString(StringBuilder strSource, int pos)
	{
		if (strSource == null)
		{
			return "";
		}

		return subString(String.valueOf(strSource), pos);
	}

	public static String subString(Object strSource, int pos, int len)
	{
		if (strSource == null)
		{
			return "";
		}

		return subString(String.valueOf(strSource), pos, len);
	}

	public static String subString(Object strSource, int pos)
	{
		if (strSource == null)
		{
			return "";
		}

		return subString(strSource, pos, Utils.length(strSource));
	}

	/**
	 * Returns a new string that is a substring of this string. The substring
	 * begins at the specified <code>beginIndex</code> and extends to the
	 * character at index <code>endIndex - 1</code>. Thus the length of the
	 * substring is <code>endIndex-beginIndex</code>.
	 * <p>
	 * Examples: <blockquote>
	 * <pre>
	 *     &quot;hamburger&quot;.substring(4, 8) returns &quot;urge&quot;
	 *     &quot;smiles&quot;.substring(1, 5) returns &quot;mile&quot;
	 * </pre>
	 * </blockquote>
	 *
	 * @param strSource source string
	 * @param start The beginning index, inclusive.
	 * @param dest The ending index, exclusive.
	 * @return The specified substring.
	 * @since (2004-02-14.13:36:18)
	 */
	public static String substring(String strSource, int start, int dest)
	{
		return subString(strSource, start + 1, dest - start);
	}

	public static String toChar(DateEx dt, String rpgFmt)
	{
		if (dt != null)
		{
			if (dt.getTypeCode() == 'T')
			{
				return String.valueOf(toTime(dt, rpgFmt));
			}
			else if (dt.getTypeCode() == 'Z')
			{
				return String.valueOf(toTime(dt, rpgFmt));
			}
			else
			{
				return String.valueOf(toDate(dt, rpgFmt));
			}
		}

		return String.valueOf(dt);
	}

	public static String toChar(java.sql.Date dt, String rpgFmt)
	{
		return String.valueOf(toDate(dt, rpgFmt));
	}

	public static String toChar(Object obj)
	{
		if (obj == null)
		{
			return "";
		}

		if (isUserDefOrArr(obj.getClass()))
		{
			return objectToString(obj);
		}

		String str = String.valueOf(obj);

		if (str.startsWith("0") && obj instanceof Integer)
		{
			// %Char converts leading zeros in the numeric field to blanks (the
			// numeric field is zero suppressed)
			return str.replaceFirst("^0*", "");
		}

		return str;
	}

	public static String toChar(Time dt, String rpgFmt)
	{
		return String.valueOf(toTime(dt, rpgFmt));
	}

	public static String toChar(Timestamp dt, String rpgFmt)
	{
		return String.valueOf(dt);
	}

	/**
	 * Uncapitalizes a String changing the required letters to title case.
	 *
	 * @param strSource the String to uncapitalize, may be null
	 * @return the uncapitalized String, <code>null</code> if null String
	 *         input
	 * @since (2012-06-30.17:10:36)
	 */
	public static String toLowerCase(String strSource)
	{
		if ((strSource == null) || strSource.equals(""))
		{
			return strSource;
		}

		char chArr[] = strSource.toCharArray();
		int j = 0;
		int len = chArr.length;

		for (int i = 0; i < len; i++)
		{
			if (Character.isLowerCase(chArr[i]))
			{
				j = i;

				break;
			}
		}

		if (j < 2)
		{
			return Character.toLowerCase(chArr[0]) + strSource.substring(1);
		}
		else
		{
			// PPVCampaign -> ppvCampaign
			return strSource.substring(0, j - 1).toLowerCase() +
			strSource.substring(j - 1);
		}
	}

	public static StringBuilder toSbl(Object str)
	{
		return new StringBuilder(String.valueOf(str));
	}

	/**
	 * Tokenizes the given string.
	 * <p>
	 * This one accepts a Boolean flag to signify whether the String delimiter
	 * should be regarded as a single group. After all, a String commonly
	 * represents a related grouping of characters.
	 *
	 * @param input source string
	 * @param delimiters delimiters
	 * @param delimiterAsGroup whether the String delimiter should be regarded
	 *        as a single group
	 * @return array containing tokenized strings
	 * @since (2001-04-03.12:27:27)
	 */
	private static String[] tokenize(String input, String delimiters,
		boolean delimiterAsGroup)
	{
		String toks[] = null;

		// If the string to tokenized is null then simply return the input
		if (input == null)
		{
			return null;
		}

		if (input.equals(""))
		{
			return new String[] { input };
		}

		// If the delimiters are null then simply return the input
		if (delimiters == null)
		{
			return null;
		}

		if (delimiters.equals(""))
		{
			return new String[] { input };
		}

		Vector<String> v = new Vector<String>();

		if (!delimiterAsGroup)
		{
			StringTokenizer t = new StringTokenizer(input, delimiters);

			while (t.hasMoreTokens())
			{
				v.addElement(t.nextToken());
			}
		}
		else
		{
			int start = 0;
			int end = input.length();

			int delimitersLen = delimiters.length();

			while (start <= end)
			{
				int delimIdx = input.indexOf(delimiters, start);

				if (delimIdx < 0)
				{
					String tok = input.substring(start);
					v.addElement(tok);
					start = end + 1;
				}
				else
				{
					String tok = input.substring(start, delimIdx);
					v.addElement(tok);
					start = delimIdx + delimitersLen;
				}
			}
		}

		int cnt = v.size();

		if (cnt > 0)
		{
			toks = new String[cnt];
			v.copyInto(toks);
		}

		return toks;
	}

	public static String translate(Object from, Object to, Object str)
	{
		return translate(from, to, str, 1);
	}

	/**
	 * Searches the input string for any instances of the individual characters
	 * contained in the <code>from</code> string and replaces them with the
	 * corresponding character in the <code>to</code> string.
	 *
	 * @param from list of characters that should be replaced
	 * @param to contains their replacements
	 * @param str the string to be translated
	 * @param startPos the starting position for translation
	 * @return the translated string
	 * @since (2010-12-14.19:24:25)
	 */
	public static String translate(Object from, Object to, Object str,
		int startPos)
	{
		if ((str == null) || (String.valueOf(str).length() == 0))
		{
			return String.valueOf(str);
		}

		if ((from == null) || (String.valueOf(from).length() == 0) ||
				(to == null) || (String.valueOf(to).length() < startPos))
		{
			return String.valueOf(str);
		}

		startPos -= 1;

		return String.valueOf(str).substring(0, startPos) +
		replaceString(String.valueOf(str).substring(startPos),
			String.valueOf(from).toCharArray(),
			String.valueOf(to).toCharArray());
	}

	/**
	 * Trims leading & trailing white spaces in a given source string.
	 *
	 * @param strToTrim source string
	 * @return string with trimmed white spaces
	 * @since (2002-03-18.10:05:28)
	 */
	public static String trim(String strToTrim)
	{
		// If the string to trim is null then simply return the strToTrim
		if (strToTrim == null)
		{
			return strToTrim;
		}

		return ltrim(rtrim(strToTrim));
	}

	public static String trim(Object strToTrim)
	{
		// If the string to trim is null then simply return the strToTrim
		if (strToTrim == null)
		{
			return null;
		}

		return ltrim(rtrim(strToTrim));
	}

	/**
	 * Trims leading & trailing white spaces in a given source string and also,
	 * sets the string to default value if the string is null or empty.
	 *
	 * @param strToTrim source string
	 * @param strDef default string
	 * @return string with trimmed white spaces or default value
	 * @since (2003-09-10.16:07:35)
	 */
	public static String trim(String strToTrim, String strDef)
	{
		String strToReturn = trim(strToTrim);

		if (Utils.length(strToReturn) == 0)
		{
			strToReturn = strDef;
		}

		return strToReturn;
	}

	/**
	 * Trims leading & trailing white spaces in a given source string array and
	 * also sets the string to default value if the string is null or empty.
	 *
	 * @param strArrToTrim array containing source strings
	 * @param strArrDef array containing default values
	 * @return string with trimmed white spaces or default value
	 * @since (2003-09-10.16:21:52)
	 */
	public static String[] trim(String strArrToTrim[], String strArrDef[])
	{
		// If strArrToTrim is null then simply return strArrToTrim
		int len = Utils.length(strArrToTrim);

		if (len == 0)
		{
			return strArrToTrim;
		}

		String strArrToReturn[] = new String[len];

		for (int i = 0; i < len; i++)
		{
			strArrToReturn[i] = trim(strArrToTrim[i],
					(String)ArrayUtils.getElement(strArrDef, i));
		} // i

		return strArrToReturn;
	}

	public static String uncapitalize(String strSource)
	{
		// If the string to uncapitalize is null then simply return the 
		// strSource
		if ((strSource == null) || strSource.equals(""))
		{
			return strSource;
		}

		return Character.toLowerCase(strSource.charAt(0)) +
				strSource.substring(1);
	}

	public static String zeros(int length)
	{
		return repeatString("0", length);
	}
}