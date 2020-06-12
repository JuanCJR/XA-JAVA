package com.databorough.utils;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class for URL.
 * <p>
 * The URLUtils class is not to be instantiated, only use its public static
 * members outside.
 *
 * @author Amit Arya
 * @since (2002-12-22.08:34:57)
 */
public final class URLUtils
{
	/**
	 * Default constructor.
	 */
	private URLUtils()
	{
		super();
	}

	/**
	 * Decodes a given string using a specific encoding scheme.
	 * <p>
	 * As of the JDK 1.4, the preferred way to encode a string is via the
	 * decode(String s, String enc) method using a specific encoding scheme.
	 *
	 * @param strToDecode string to decode
	 * @return decoded string
	 * @since (2008-01-06.12:05:16)
	 */
	public static String decode(String strToDecode)
	{
		String dfltEncName = System.getProperty("file.encoding");

		if (dfltEncName == null)
		{
			dfltEncName = "UTF-8";
		}

		return decode(strToDecode, dfltEncName);
	}

	/**
	 * Decodes a given string using a specific encoding scheme.
	 * <p>
	 * As of the JDK 1.4, the preferred way to encode a string is via the
	 * decode(String s, String enc) method using a specific encoding scheme.
	 *
	 * @param strToDecode string to decode
	 * @param charset charset name
	 * @return decoded string
	 * @since (2005-01-22.16:58:43)
	 */
	public static String decode(String strToDecode, String charset)
	{
		String strToReturn;

		// If strToDecode is null then simply return strToDecode
		if ((strToDecode == null) || strToDecode.equals(""))
		{
			return strToDecode;
		}

		try
		{
			strToReturn = URLDecoder.decode(strToDecode, charset);
		}
		catch (UnsupportedEncodingException uee)
		{
			strToReturn = strToDecode;
		}

		return strToReturn;
	}

	/**
	 * Deletes parameter and its value from the source string.
	 * <p>
	 * strSource :
	 * id=-8198266266938271885&pid0=0&oldQry=id%3D-8198266266938271885
	 * &pgid=ZACUSF&cmpid=B01
	 *
	 * @param strSource source string
	 * @param paramToDelete parameter to delete
	 * @return String after deleting the specified parameter and its value
	 * @since (2000-12-27.09:46:03)
	 */
	public static String deleteParamFromURL(String strSource,
		String paramToDelete)
	{
		// If strSource is null then simply return strSource
		if ((strSource == null) || strSource.equals(""))
		{
			return strSource;
		}

		// If paramToDelete is null then simply return strSource
		if ((paramToDelete == null) || paramToDelete.equals(""))
		{
			return strSource;
		}

		StringBuffer strToReturn = new StringBuffer();

		if (strSource.startsWith(paramToDelete))
		{
			// The first parameter of URL does not have '&'
			int indx1 = strSource.indexOf('&');

			if (indx1 != -1)
			{
				// Remove the first parameter along with '&' so that the next
				// parameter becomes the first parameter
				strToReturn.append(strSource.substring(indx1 + 1));
			}
		}
		else
		{
			int indx1 = strSource.indexOf("&" + paramToDelete + "=");

			if (indx1 != -1)
			{
				int indx2 = strSource.indexOf("&", indx1 + 1);

				if (indx2 != -1)
				{
					strToReturn.append(strSource.substring(0, indx1))
							   .append(strSource.substring(indx2,
							strSource.length()));
				}
				else
				{
					strToReturn.append(strSource.substring(0, indx1));
				}
			}
			else
			{
				indx1 = strSource.indexOf("?" + paramToDelete + "=");

				if (indx1 != -1)
				{
					int indx2 = strSource.indexOf("&", indx1 + 1);

					if (indx2 != -1)
					{
						strToReturn.append(strSource.substring(0, indx1 + 1))
								   .append(strSource.substring(indx2 + 1,
								strSource.length()));
					}
					else
					{
						strToReturn.append(strSource.substring(0, indx1));
					}
				}
				else
				{
					strToReturn.append(strSource);
				}
			}
		}

		return strToReturn.toString();
	}

	/**
	 * Translates a string into x-www-form-urlencoded format using a specific
	 * encoding scheme.
	 * <p>
	 * As of the JDK 1.4, the preferred way to encode a string is via the
	 * encode(String s, String enc) method using a specific encoding scheme.
	 *
	 * @param strToEncode string to encode
	 * @return encoded string
	 * @since (2008-01-06.12:02:13)
	 */
	public static String encode(String strToEncode)
	{
		String dfltEncName = System.getProperty("file.encoding");

		if (dfltEncName == null)
		{
			dfltEncName = "UTF-8";
		}

		return encode(strToEncode, dfltEncName);
	}

	/**
	 * Translates a string into x-www-form-urlencoded format using a specific
	 * encoding scheme.
	 * <p>
	 * As of the JDK 1.4, the preferred way to encode a string is via the
	 * encode(String s, String enc) method using a specific encoding scheme.
	 *
	 * @param strToEncode string to encode
	 * @param charset charset name
	 * @return encoded string
	 * @since (2004-05-28.07:12:32)
	 */
	public static String encode(String strToEncode, String charset)
	{
		String strToReturn;

		// If strToEncode is null then simply return strToEncode
		if ((strToEncode == null) || strToEncode.equals(""))
		{
			return strToEncode;
		}

		try
		{
			strToReturn = URLEncoder.encode(strToEncode, charset);
		}
		catch (UnsupportedEncodingException uee)
		{
			strToReturn = strToEncode;
		}

		return strToReturn;
	}

	/**
	 * Forms the URL containing the enumerated parameters from the values.
	 *
	 * @param paramVal Array containing parameter value
	 * @param prefixParam prefix for parameter
	 * @param bEncode URL to be encoded if true, otherwise not.
	 * @return URL String
	 * @since (2001-08-31.09:42:51)
	 */
	public static String getEnumeratedURL(String paramVal[],
		String prefixParam, boolean bEncode)
	{
		StringBuffer strToReturn = new StringBuffer();

		// If paramVal is null then simply return strToReturn.toString()
		int len = Utils.length(paramVal);

		if (len == 0)
		{
			return strToReturn.toString();
		}

		// If prefixParam is null then simply return strToReturn.toString()
		if ((prefixParam == null) || prefixParam.equals(""))
		{
			return strToReturn.toString();
		}

		String val;

		for (int i = 0; i < len; i++)
		{
			if (paramVal[i] == null)
			{
				val = "";
			}
			else
			{
				val = (bEncode) ? URLUtils.encode(paramVal[i]) : paramVal[i];
			}

			strToReturn.append("&" + prefixParam + i + "=" + val);
		} // i

		return strToReturn.toString();
	}

	/**
	 * Returns the query string that is contained in the request URL after the
	 * path. This method returns null if the URL does not have a query string.
	 * Same as the value of the CGI variable QUERY_STRING.
	 *
	 * @param req a <code>ServletRequest</code> object that contains the
	 *        client's request
	 * @return a String containing the query string or null if the URL contains
	 *         no query string. The value is not decoded by the container
	 * @since (2008-01-29.07:19:22)
	 */
	public static String getQueryString(HttpServletRequest req)
	{
		String queryString = req.getQueryString();

		if (queryString != null)
		{
			return queryString;
		}

		return (String)req.getAttribute("javax.servlet.forward.query_string");
	}

	/**
	 * Returns an array containing request parameter values.
	 *
	 * @param req reference of HttpServletRequest
	 * @param prefixParam prefix parameter
	 * @param suffixParam suffix parameter
	 * @return array containing request parameter values
	 * @since (2000-12-20.10:22:38)
	 */
	public static String[] paramsFromURL(HttpServletRequest req,
		String prefixParam, String suffixParam)
	{
		// If prefixParam is null then simply return null
		if ((prefixParam == null) || prefixParam.equals(""))
		{
			return null;
		}

		// If suffixParam is null then set suffixParam to ""
		if (suffixParam == null)
		{
			suffixParam = "";
		}

		ArrayList<String> arrayList = new ArrayList<String>();
		int i = 0;

		while (true)
		{
			String paramVal = req.getParameter(prefixParam + i + suffixParam);

			if (paramVal == null)
			{
				break;
			}

			arrayList.add(URLUtils.decode(paramVal));
			i++;
		}

		if (i > 0)
		{
			return (String[])arrayList.toArray(new String[i]);
		}
		else
		{
			return null;
		}
	}
}