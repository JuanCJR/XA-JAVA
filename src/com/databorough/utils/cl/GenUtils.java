package com.databorough.utils.cl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.databorough.utils.JSFUtils;
import static com.databorough.utils.LoggingAspect.appLogger;
import static com.databorough.utils.LoggingAspect.logDebug;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.LoggingAspect.logTrace;
import com.databorough.utils.ReflectionUtils;
import com.databorough.utils.StringUtils;
import com.databorough.utils.Utils;

/**
 * Provides conversion of General CL commands.
 *
 * @author Amit Arya
 * @since (2012-09-05.11:34:12)
 */
public final class GenUtils
{
	/**
	 * The GenUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private GenUtils()
	{
		super();
	}

	private static Map<String, String> clToJavaDateFormat =
		new HashMap<String, String>();

	static
	{
		clToJavaDateFormat.put("*JOB", "dd-MM-yy"); // default format
		clToJavaDateFormat.put("*MDY", "MM/dd/yy");
		clToJavaDateFormat.put("*DMY", "dd/MM/yy");
		clToJavaDateFormat.put("*YMD", "yy/MM/dd");
		clToJavaDateFormat.put("*ISO", "yyyy-MM-dd");
		clToJavaDateFormat.put("*USA", "MM/dd/yyyy");
		clToJavaDateFormat.put("*EUR", "dd.MM.yyyy");
		clToJavaDateFormat.put("*JIS", "dd.MM.yyyy");
		clToJavaDateFormat.put("*JUL", "yy/ddd");
		clToJavaDateFormat.put("*YYMD", "yyyy/MM/dd");
		clToJavaDateFormat.put("*MDYY", "MM/dd/yyyy");
		clToJavaDateFormat.put("*DMYY", "dd/MM/yyyy");
	}

	/**
	 * Copies the data in a PC document to a system database file.
	 *
	 * @param params CPYFRMPCD command parameters
	 */
	public static void cpyfrmpcd(String params)
	{
		String fromFolder = CLUtils.getVarName(params, "FROMFLR");
		String toFile = CLUtils.getVarName(params, "TOFILE");
		String fromDoc = CLUtils.getVarName(params, "FROMDOC");

		File source = new File(fromFolder, fromDoc);
		File dest = new File(fromFolder, toFile);

		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		FileChannel in = null;
		FileChannel out = null;

		try
		{
			fIn = new FileInputStream(source);
			in = fIn.getChannel();
			fOut = new FileOutputStream(dest);
			out = fOut.getChannel();

			long size = in.size();
			MappedByteBuffer buf =
				in.map(FileChannel.MapMode.READ_ONLY, 0, size);

			out.write(buf);
		}
		catch (IOException ioe)
		{
			logStackTrace(ioe);
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
					in = null;
				}
				catch (IOException ioe)
				{
				}
			}

			if (fIn != null)
			{
				try
				{
					fIn.close();
					fIn = null;
				}
				catch (IOException ioe)
				{
				}
			}

			if (out != null)
			{
				try
				{
					out.close();
					out = null;
				}
				catch (IOException ioe)
				{
				}
			}

			if (fOut != null)
			{
				try
				{
					fOut.close();
					fOut = null;
				}
				catch (IOException ioe)
				{
				}
			}
		}
	}

	// Create Bound RPG Program
	/*public static void crtbndrpg(String params)
	{
	}*/

	// Create Class
	/*public static void crtcls(String params)
	{
	}*/

	/**
	 * Converts the format of a date value from one format to another.
	 *
	 * @param dateStr Date to be converted
	 * @param toVar CL var for converted date 	
	 * @param fromFmt From date format
	 * @param toFmt To date format
	 * @param toSep To date separator
	 * @param pgm CL program
	 */
	public static void cvtdat(String dateStr, String toVar, String fromFmt,
		String toFmt, String toSep, Object pgm)
	{
		String outDate = "";
		Date dt = null;

		// set default format
		if (Utils.length(fromFmt) == 0)
		{
			fromFmt = "*JOB";
		}

		// convert String to Date as format specified in fromFmt
		SimpleDateFormat sdf =
			new SimpleDateFormat(clToJavaDateFormat.get(fromFmt));

		try
		{
			dt = sdf.parse(dateStr);
		}
		catch (Exception e)
		{
		}

		// convert the date as format specified in toFmt 
		if ("*CYMD".equalsIgnoreCase(toFmt))
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);

			String year = cal.get(Calendar.YEAR) + "";
			String month =
				StringUtils.padStringWithValue("" +
					(cal.get(Calendar.MONTH) + 1), "0", 2, true);
			String day =
				StringUtils.padStringWithValue("" + (cal.get(Calendar.DATE)),
					"0", 2, true);

			if ((year.length() == 4) && year.startsWith("19"))
			{
				year = "0" + year.substring(2);
			}
			else if ((year.length() == 2) && (Integer.parseInt(year) < 40))
			{
				year = "1" + year;
			}

			outDate = year + month + day;
		}
		else
		{
			sdf = new SimpleDateFormat(clToJavaDateFormat.get(toFmt));
			outDate = sdf.format(dt);
		}

		// Apply separator as specified in toSep
		if (!"*NONE".equalsIgnoreCase(toSep) && (Utils.length(toSep) != 0))
		{
			if (outDate.contains("/"))
			{
				outDate = outDate.replace("/", toSep);
			}
			else if (outDate.contains("-"))
			{
				outDate = outDate.replace("-", toSep);
			}
			else if (outDate.contains("."))
			{
				outDate = outDate.replace(".", toSep);
			}
		}

		CLUtils.setFieldVal(pgm, toVar, outDate, true);
	}

	/**
	 * Dumps variables and all messages in the CL program.
	 *
	 * @param params DMPCLPGM command parameters
	 * @param pgm CL program
	 */
	public static void dmpclpgm(String params, Object pgm)
	{
		Class<?> cls = pgm.getClass().getSuperclass();

		try
		{
			Field flds[] = cls.getDeclaredFields();

			if (appLogger.isTraceEnabled())
			{
				logTrace("Start method: dmpclpgm(Object object)", false, pgm);
			}

			if (appLogger.isDebugEnabled())
			{
				StringBuffer log = new StringBuffer("Declared fields are :");

				for (Field field : flds)
				{
					if (!ReflectionUtils.isUserDefOrArr(field.getType()))
					{
						field.setAccessible(true);
						log.append(field.getName() + ":<" + field.get(pgm) +
							">,");
						field.setAccessible(false);
					}
				}

				logDebug(log.toString(), false, pgm);
			}
		}
		catch (Exception e)
		{
		}
	}

	// End Pass-Through
	/*public static void endpasthr(String params)
	{
	}*/

	/**
	 * Goes to Menu i.e. redirect to JSF Menu page.
	 *
	 * @param params GO command parameters
	 */
	public static void go(String params)
	{
		// GO MENU(PERSMENU)
		String menu = CLUtils.getVarName(params, "MENU");

		JSFUtils.redirectToScreen(menu);
	}

	// Remove Link
	/*public static void rmvlnk(String params)
	{
	}*/

	// Run Java Program
	/*public static void runjva(String params)
	{
	}*/

	/**
	 * Starts Cleanup.
	 *
	 * @param params STRCLNUP command parameters
	 */
	public static void strclnup(String params)
	{
		System.gc();
	}

	// Wait
	/*public static void wait_(String params)
	{
	}*/
}