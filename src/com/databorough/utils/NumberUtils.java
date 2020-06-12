package com.databorough.utils;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.databorough.utils.NumberFormatter.toDecimal;
import static com.databorough.utils.NumberFormatter.toDouble;
import static com.databorough.utils.NumberFormatter.toLong;
import static com.databorough.utils.StringUtils.rtrim;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.StringUtils.toChar;
import static com.databorough.utils.Utils.length;
import static com.databorough.utils.Utils.search;

/**
 * The NumberUtils class contains various methods for manipulating numbers.
 *
 * @author Amit Arya
 * @since (2003-02-03.07:26:06)
 */
public final class NumberUtils
{
	/**
	 * sign array containing addition, subtraction operators.
	 */
	public static final char SIGN[] = { '+', '-' };

	/**
	 * Represents a set of DecimalFormat symbols to format numbers.
	 */
	public static final DecimalFormatSymbols DEF_FORMAT_SYMBOLS =
		new DecimalFormatSymbols(Locale.US);

	/**
	 * The NumberUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private NumberUtils()
	{
		super();
	}

	/**
	 * Processes ADD Op-code.
	 * <p>
	 * Analyzes the result field and factor 2 length and if it finds any
	 * possibility of overflow (e.g. the result field is of length 2 and value
	 * is being supplied from a variable of length 3).
	 *
	 * @param upfld1 Factor 1
	 * @param upfld2 Factor 2
	 * @param upresfld result field
	 * @return added value
	 * @since (2014-01-22.11:24:30)
	 */
	public static BigDecimal addValue(BigDecimal upfld1, BigDecimal upfld2,
		String upresfld)
	{
		return BigDecimal.valueOf(addValue(upfld1.doubleValue(),
				upfld2.doubleValue(), upresfld));
	}

	/**
	 * Processes ADD Op-code.
	 * <p>
	 * Analyzes the result field and factor 2 length and if it finds any
	 * possibility of overflow (e.g. the result field is of length 2 and value
	 * is being supplied from a variable of length 3).
	 *
	 * @param upfld1 Factor 1
	 * @param upfld2 Factor 2
	 * @param upresfld result field
	 * @return added value
	 * @since (2014-01-22.11:24:30)
	 */
	public static Double addValue(Double upfld1, Double upfld2, String upresfld)
	{
		Long uwdec = 0L;
		Long uwlen = (long)(length(rtrim(upresfld)) - 1);
		Long uwdecsep = (long)(search(".", upresfld));

		if (uwdecsep == 0L)
		{
			uwdecsep = (long)(search(",", upresfld));
		}

		if (uwdecsep > 0L)
		{
			uwdec = uwlen - uwdecsep + 1;
			uwlen = uwlen - 1;
		}

		Double uwresfld = upfld1 + upfld2;

		if (uwresfld == 0D)
		{
			return 0D;
		}

		String uwresfldch = toChar(uwresfld);

		if (uwresfld > 0D)
		{
			uwresfldch = "+" + uwresfldch;
		}

		uwdecsep = (long)(search(".", uwresfldch));

		if (uwdecsep == 0L)
		{
			uwdecsep = (long)(search(",", uwresfldch));
		}

		if (uwdec == 0L)
		{
			if ((uwdecsep - 2) <= uwlen)
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1,
								(int)(uwdecsep - 1)), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
			else
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch,
								(int)(uwdecsep - uwlen), uwlen.intValue()), 63,
							15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
		}
		else if (uwdec > 0L)
		{
			if ((uwdecsep - 2) <= (uwlen - uwdec))
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1,
								(int)(uwdecsep - 1)) + "." +
							subString(uwresfldch, (int)(uwdecsep + 1),
								uwdec.intValue()), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
			else
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1, 1) +
							subString(uwresfldch,
								(int)(uwdecsep - uwlen + uwdec),
								(int)(uwlen - uwdec)) + "." +
							subString(uwresfldch, (int)(uwdecsep + 1),
								uwdec.intValue()), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
		}

		return 0D;
	}

	/**
	 * Compares two BigDecimal values.
	 *
	 * @param tgt value of tgt to be compare
	 * @param src value of src to be compare
	 * @return -1, 0, or 1 as tgt BigDecimal is numerically less than, equal to,
	 *         or greater than src
	 * @since (2015-01-06.11:24:15)
	 */
	public static int compareTo(BigDecimal tgt, BigDecimal src)
	{
		if ((tgt == null) || (src == null))
		{
			return Integer.MIN_VALUE;
		}

		return tgt.compareTo(src);
	}

	/**
	 * Compares fieldData1 with fieldData2.
	 * <p>
	 * Returns : the value 0 if the argument is a double numerically equal to
	 * this double; a value less than 0 if the argument is a double numerically
	 * greater than this double; and a value greater than 0 if the argument is a
	 * double numerically less than this double.
	 *
	 * @param fieldData1 data of field1 to be compare
	 * @param fieldData2 data of field2 to be compare
	 * @param dcmFrmSyms set of DecimalFormat symbols to format numbers
	 * @return 0 if number numerically equal, less than 0 if the argument
	 *         numerically greater and greater than 0 if the argument
	 *         numerically less.
	 * @throws NumberFormatException if any attempt is made convert a string to
	 *         one of the numeric types, but that the string does not have the
	 *         appropriate format
	 * @since (2001-04-05.10:17:38)
	 */
	public static int compareTo(String fieldData1, String fieldData2,
		DecimalFormatSymbols dcmFrmSyms) throws NumberFormatException
	{
		dcmFrmSyms = getDecimalFormatSymbols(dcmFrmSyms);

		int retVal = -1;

		// if fieldData1 is null then simply return -1
		if ((fieldData1 == null) || "".equals(fieldData1))
		{
			return retVal;
		}

		// if fieldData2 is null then simply throw IllegalArgumentException
		if (fieldData2 == null)
		{
			throw new IllegalArgumentException(
				"Second argument in compareTo() is null");
		}

		try
		{
			fieldData1 = toDoubleString(fieldData1, dcmFrmSyms);

			double d1 = Double.valueOf(fieldData1).doubleValue();
			fieldData2 = toDoubleString(fieldData2, dcmFrmSyms);

			double d2 = Double.valueOf(fieldData2).doubleValue();

			if (d1 < d2)
			{
				retVal = -1;
			}
			else if (d1 > d2)
			{
				retVal = 1;
			}
			else
			{
				long thisBits = Double.doubleToLongBits(d1);
				long anotherBits = Double.doubleToLongBits(d2);
				retVal = (
						(thisBits == anotherBits) ? 0
												  : // Values are equal
						((thisBits < anotherBits) ? (-1) : // (-0.0, 0.0) or
														   // (!NaN, NaN)
						1)
					);
			}
		}
		catch (NumberFormatException nfe)
		{
			throw new NumberFormatException(nfe.getMessage());
		}

		return retVal;
	}

	/**
	 * Processes DIV Op-code.
	 * <p>
	 * Analyzes the result field and factor 2 length and if it finds any
	 * possibility of overflow (e.g. the result field is of length 2 and value
	 * is being supplied from a variable of length 3).
	 *
	 * @param upfld1 Factor 1
	 * @param upfld2 Factor 2
	 * @param upresfld result field
	 * @return divided value
	 * @since (2014-01-22.13:29:30)
	 */
	public static Double divideValue(Double upfld1, Double upfld2,
		String upresfld)
	{
		Long uwdec = 0L;
		Long uwlen = (long)(length(rtrim(upresfld)) - 1);
		Long uwdecsep = (long)(search(".", upresfld));

		if (uwdecsep == 0L)
		{
			uwdecsep = (long)(search(",", upresfld));
		}

		if (uwdecsep > 0L)
		{
			uwdec = uwlen - uwdecsep + 1;
			uwlen = uwlen - 1;
		}

		Double uwresfld = upfld1 / upfld2;

		if (uwresfld == 0D)
		{
			return 0D;
		}

		String uwresfldch = toChar(uwresfld);

		if (uwresfld > 0D)
		{
			uwresfldch = "+" + uwresfldch;
		}

		uwdecsep = (long)(search(".", uwresfldch));

		if (uwdecsep == 0L)
		{
			uwdecsep = (long)(search(",", uwresfldch));
		}

		if (uwdec == 0L)
		{
			if ((uwdecsep - 2) <= uwlen)
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1,
								(int)(uwdecsep - 1)), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
			else
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch,
								(int)(uwdecsep - uwlen), uwlen.intValue()), 63,
							15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
		}
		else if (uwdec > 0L)
		{
			if ((uwdecsep - 2) <= (uwlen - uwdec))
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1,
								(int)(uwdecsep - 1)) + "." +
							subString(uwresfldch, (int)(uwdecsep + 1),
								uwdec.intValue()), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
			else
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1, 1) +
							subString(uwresfldch,
								(int)(uwdecsep - uwlen + uwdec),
								(int)(uwlen - uwdec)) + "." +
							subString(uwresfldch, (int)(uwdecsep + 1),
								uwdec.intValue()), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
		}

		return 0D;
	}

	/**
	 * Returns the Decimal Format Symbols.
	 *
	 * @param dcmFrmSyms set of DecimalFormat symbols to format numbers
	 * @return Decimal Format Symbols
	 * @since (2001-04-07.09:51:03)
	 */
	private static DecimalFormatSymbols getDecimalFormatSymbols(
		DecimalFormatSymbols dcmFrmSyms)
	{
		return ((dcmFrmSyms == null) ? DEF_FORMAT_SYMBOLS : dcmFrmSyms);
	}

	/**
	 * Extracts all the digits in a string with Decimal, 'E' and Sign. The sign
	 * can only be at the very beginning or at the very end.
	 *
	 * @param strSource String from which digits to be extracted
	 * @param dcmFrmSyms set of DecimalFormat symbols to format numbers
	 * @return all extracted digit with decimal
	 * @since (2001-02-15.09:28:06)
	 */
	private static String getDigitsWithDecimal(String strSource,
		DecimalFormatSymbols dcmFrmSyms)
	{
		// If the string source is null then simply return the strSource
		if ((strSource == null) || strSource.equals(""))
		{
			return strSource;
		}

		dcmFrmSyms = getDecimalFormatSymbols(dcmFrmSyms);

		char dcmSym = dcmFrmSyms.getDecimalSeparator();

		// Replace CR with negative sign
		strSource = StringUtils.replaceString(strSource, "CR", "" + '-');

		int indxExponent = strSource.indexOf('E');

		if (indxExponent == -1)
		{
			indxExponent = strSource.indexOf('e');
		}

		StringBuffer strToReturn = new StringBuffer();
		int len = strSource.length();

		for (int i = 0; i < len; i++)
		{
			char ch = strSource.charAt(i);

			if (Character.isDigit(ch) || (ch == dcmSym) || (ch == 'E') ||
					(ch == 'e') ||
					(
						((ch == '-') || (ch == '+')) &&
						(
							((i == 0) || (i == (len - 1))) ||
							(
								(indxExponent != -1) &&
								(i == (indxExponent + 1))
							)
						)
					))
			{
				strToReturn.append(strSource.substring(i, i + 1));
			}
		} // i

		return strToReturn.toString();
	}

	public static boolean isNumber(String os)
	{
		return (os != null) && os.matches("[\\+\\-]?\\d+([\\,\\.]\\d+)?");
	}

	/**
	 * Processes MULT Op-code.
	 * <p>
	 * Analyzes the result field and factor 2 length and if it finds any
	 * possibility of overflow (e.g. the result field is of length 2 and value
	 * is being supplied from a variable of length 3).
	 *
	 * @param upfld1 Factor 1
	 * @param upfld2 Factor 2
	 * @param upresfld result field
	 * @return multiplied value
	 * @since (2014-01-22.13:21:30)
	 */
	public static Double multiplyValue(Double upfld1, Double upfld2,
		String upresfld)
	{
		Long uwdec = 0L;
		Long uwlen = (long)(length(rtrim(upresfld)) - 1);
		Long uwdecsep = (long)(search(".", upresfld));

		if (uwdecsep == 0L)
		{
			uwdecsep = (long)(search(",", upresfld));
		}

		if (uwdecsep > 0L)
		{
			uwdec = uwlen - uwdecsep + 1;
			uwlen = uwlen - 1;
		}

		Double uwresfld = upfld1 * upfld2;

		if (uwresfld == 0D)
		{
			return 0D;
		}

		String uwresfldch = toChar(uwresfld);

		if (uwresfld > 0D)
		{
			uwresfldch = "+" + uwresfldch;
		}

		uwdecsep = (long)(search(".", uwresfldch));

		if (uwdecsep == 0L)
		{
			uwdecsep = (long)(search(",", uwresfldch));
		}

		if (uwdec == 0L)
		{
			if ((uwdecsep - 2) <= uwlen)
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1,
								(int)(uwdecsep - 1)), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
			else
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch,
								(int)(uwdecsep - uwlen), uwlen.intValue()), 63,
							15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
		}
		else if (uwdec > 0L)
		{
			if ((uwdecsep - 2) <= (uwlen - uwdec))
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1,
								(int)(uwdecsep - 1)) + "." +
							subString(uwresfldch, (int)(uwdecsep + 1),
								uwdec.intValue()), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
			else
			{
				try
				{
					return toDouble(toDecimal(subString(uwresfldch, 1, 1) +
							subString(uwresfldch,
								(int)(uwdecsep - uwlen + uwdec),
								(int)(uwlen - uwdec)) + "." +
							subString(uwresfldch, (int)(uwdecsep + 1),
								uwdec.intValue()), 63, 15));
				}
				catch (Exception e)
				{
					return 0D;
				}
			}
		}

		return 0D;
	}

	/**
	 * Returns data in double format
	 *
	 * @param fieldDataRecv received field's data
	 * @param dcmFrmSyms set of DecimalFormat symbols to format numbers
	 * @return data in double format
	 * @throws NumberFormatException if any attempt is made convert a string to
	 *         one of the numeric types, but that the string does not have the
	 *         appropriate format
	 * @since (2001-04-05.10:12:54)
	 */
	private static String toDoubleString(String fieldDataRecv,
		DecimalFormatSymbols dcmFrmSyms) throws NumberFormatException
	{
		String fieldData;

		if ((fieldDataRecv == null) || "".equals(fieldDataRecv))
		{
			return fieldDataRecv;
		}

		try
		{
			// Get only digits, decimal as '.', 'E', & sign in fieldDataRecv
			// since even the Grouping Symbol creates problem with Double.
			fieldData = toNumberString(fieldDataRecv, dcmFrmSyms);

			// Check if fieldData represents number
			Double.valueOf(fieldData).doubleValue();

			// fieldData represents number
		}
		catch (NumberFormatException nfe)
		{
			throw new NumberFormatException(fieldDataRecv);
		}

		return fieldData;
	}

	/**
	 * Extracts all the digits in a given field's data with Decimal as '.', 'E'
	 * and Sign since even the Grouping Symbol creates problem with Double. The
	 * sign can only be at the very beginning or at the very end.
	 *
	 * @param fieldData data of the field
	 * @param dcmFrmSyms set of DecimalFormat symbols to format numbers
	 * @return String after extracting the number
	 * @since (2002-04-04.15:50:32)
	 */
	private static String toNumberString(String fieldData,
		DecimalFormatSymbols dcmFrmSyms)
	{
		// if fieldData is null then throw NumberFormatException
		if (fieldData == null)
		{
			throw new NumberFormatException(fieldData);
		}

		dcmFrmSyms = getDecimalFormatSymbols(dcmFrmSyms);

		char dcmSym = dcmFrmSyms.getDecimalSeparator();

		// Get only digits, decimal, & sign in fieldData since even the Grouping
		// Symbol creates problem with Double.
		fieldData = getDigitsWithDecimal(fieldData, dcmFrmSyms);

		int fieldLength = Utils.length(fieldData);

		if (fieldLength > 0)
		{
			// Place the sign in the beginning if it is at the end
			//char sign[] = NumberValidator.SIGN;
			char sign[] = SIGN;
			int len = Utils.length(sign);

			if (len > 0)
			{
				for (int i = 0; i < len; i++)
				{
					int indx = fieldData.indexOf(sign[i]);

					if (indx != -1)
					{
						if (indx == (fieldLength - 1))
						{
							// 255.20- -> -255.20
							// Removes all instances of sign[i] in fieldData
							fieldData = StringUtils.deleteString(fieldData,
									String.valueOf(sign[i]));

							fieldData = sign[i] + fieldData;
						}
					}
				} // i
			}
		}

		// Replace dcmSym by dot
		fieldData = StringUtils.replaceString(fieldData, "" + dcmSym, ".");

		return fieldData;
	}

	public static void validateNumerics(Object obj[])
	{
		if (Utils.length(obj) == 0)
		{
			return;
		}

		for (int i = 0; i < obj.length; i++)
		{
			String os = String.valueOf(obj[i]);

			if (isNumber(os))
			{
				if (os.contains("."))
				{
					obj[i] = toDouble(os);
				}
				else
				{
					//obj[i] = toInt(os);
					obj[i] = toLong(os);
				}
			}
		}
	}
}