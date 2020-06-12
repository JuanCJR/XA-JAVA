package com.databorough.utils.cl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import com.databorough.utils.AS400Utils;
import com.databorough.utils.IOUtils;
import com.databorough.utils.SQLUtils;
import com.databorough.utils.Utils;
import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Provides conversion of AS/400 management commands.
 *
 * @author Amit Arya
 * @since (2012-09-04.14:24:12)
 */
public final class AS400MgmtUtils
{
	/**
	 * The AS400MgmtUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private AS400MgmtUtils()
	{
		super();
	}

	/**
	 * Adds an environment variable.
	 *
	 * @param params ADDENVVAR command parameters
	 * @return the previous value of the system property, or null if it did not
	 *        have one
	 */
	public static String addenvvar(String params)
	{
		// ADDENVVAR ENVVAR(homedir) VALUE('/home')
		String envvar = CLUtils.getVarName(params, "ENVVAR");
		String value = CLUtils.getVarName(params, "VALUE");

		String prevValue = System.getProperty(envvar);
		System.setProperty(envvar, value);

		return prevValue;
	}

	/**
	 * Copies file.
	 *
	 * @param params CPYF command parameters
	 * @return the row count for INSERT statement
	 */
	public static int cpyf(String params)
	{
		// CPYF FROMFILE(PERSONNEL/PAYROLL) TOFILE(TESTPAY/PAYROLL)
		String fromFile = CLUtils.getVarName(params, "FROMFILE");
		String toFile = CLUtils.getVarName(params, "TOFILE");

		Statement stmt = null;
		int rowCount = 0;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();
			rowCount = stmt.executeUpdate("INSERT INTO " + toFile +
					" (SELECT * FROM " + fromFile + ")");
		}
		catch (SQLException sqe)
		{
			logStackTrace(sqe);
		}
		finally
		{
			SQLUtils.close(null, stmt);
		}

		return rowCount;
	}

	/**
	 * Copies the data records in the specified spooled file to a user-defined
	 * physical database file.
	 *
	 * @param params CPYSPLF command parameters
	 */
	public static void cpysplf(String params)
	{
	}

	/**
	 * Copies file and converts the character data from the from-file CCSID to
	 * the to-file CCSID.
	 *
	 * @param params CPYSRCF command parameters
	 * @return the row count for INSERT statement
	 */
	public static int cpysrcf(String params)
	{
		return cpyf(params);
	}

	/**
	 * Starts the RPG/400 compiler.
	 *
	 * @param params CRTRPGPGM command parameters
	 */
	public static void crtrpgpgm(String params)
	{
	}

	/**
	 * Deletes file.
	 *
	 * @param params DLTF command parameters
	 */
	public static void dltf(String params)
	{
		// DLTF FILE(RLKAYS/MYFILE)
		String file = CLUtils.getVarName(params, "FILE");

		Statement stmt = null;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE " + file);
		}
		catch (SQLException sqe)
		{
			logStackTrace(sqe);
		}
		finally
		{
			SQLUtils.close(null, stmt);
		}
	}

	/**
	 * Removes the specified spooled file from the output queue.
	 *
	 * @param params DLTSPLF command parameters
	 */
	public static void dltsplf(String params)
	{
		// DLTSPLF FILE(WEEKLY) JOB(000146/SMITH/PAYROLL5)
		String file = CLUtils.getVarName(params, "FILE");
		String path = CLUtils.getRealPath() + IOUtils.FILE_SEPARATOR + "report";

		String fileDesc = Utils.getPrefLongName(file);
		File report = new File(path, fileDesc + ".pdf");

		if (report.exists())
		{
			report.delete();
		}
	}

	/**
	 * Allows you to display one entry, generic entries, or all entries in the
	 * relational database (RDB) directory.
	 *
	 * @param params DSPRDBDIRE command parameters
	 */
	public static void dsprdbdire(String params)
	{
	}

	/**
	 * Changes the name of an object in a directory.
	 *
	 * @param params REN command parameters
	 */
	public static void ren(String params)
	{
		// REN OBJ('DECEMBER-1994-MONTHLY-PAYROLL-FILE')
		// NEWOBJ('JANUARY-1995-MONTHLY-PAYROLL-FILE')
		String obj = CLUtils.getVarName(params, "OBJ");
		String newObj = CLUtils.getVarName(params, "NEWOBJ");

		Statement stmt = null;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("ALTER TABLE " + obj + " RENAME TO " + newObj);
		}
		catch (SQLException sqe)
		{
			logStackTrace(sqe);
		}
		finally
		{
			SQLUtils.close(null, stmt);
		}
	}

	/**
	 * Retrieves information about one or more specific subsystems or all active
	 * subsystems.
	 *
	 * @param obj QWDRSBSD command parameters
	 */
	public static void retrieveSubsystemInfo(Object... obj)
	{
	}

	/**
	 * Releases the specified file on an output queue.
	 *
	 * @param params RLSSPLF command parameters
	 */
	public static void rlssplf(String params)
	{
	}

	/**
	 * Removes an environment variable (or all environment variables) that
	 * exists.
	 *
	 * @param params RMVENVVAR command parameters
	 */
	public static void rmvenvvar(String params)
	{
		// RMVENVVAR ENVVAR(altdir)
		String envvar = CLUtils.getVarName(params, "ENVVAR");

		if ("*ALL".equalsIgnoreCase(envvar))
		{
			// Get all system properties
			Properties props = System.getProperties();

			// Enumerate all system properties
			Enumeration<?> keys = props.propertyNames();

			while (keys.hasMoreElements())
			{
			    // Get property name
			    String propName = (String)keys.nextElement();
			    // Set property value
			    System.clearProperty(propName);
			}
		}
		else
		{
			System.clearProperty(envvar);
		}
	}

	/**
	 * Retrieves the value from the specified system value so that it can be
	 * used in the program.
	 *
	 * @param params RTVSYSVAL command parameters
	 * @param pgm CL program
	 */
	public static void rtvSysVal(String params, Object pgm)
	{
		// RTVSYSVAL SYSVAL(QDATE) RTNVAR(&DATE)
		String sysVal = CLUtils.getVarName(params, "SYSVAL");
		String rtnVar = CLUtils.getVarName(params, "RTNVAR");

		Calendar cal = Calendar.getInstance();
		Date dt = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if ("QDATE".equalsIgnoreCase(sysVal))
		{
			String date = sdf.format(dt);
			CLUtils.setFieldVal(pgm, rtnVar, date, true);
		}
		else if ("QTIME".equalsIgnoreCase(sysVal))
		{
			String time = new Time(cal.getTimeInMillis()).toString();
			CLUtils.setFieldVal(pgm, rtnVar, time, true);
		}
		else if ("QDAYOFWEEK".equalsIgnoreCase(sysVal))
		{
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			CLUtils.setFieldVal(pgm, rtnVar, dayOfWeek + "", true);
		}
		else if ("QDAY".equalsIgnoreCase(sysVal))
		{
			int day = cal.get(Calendar.DATE);
			CLUtils.setFieldVal(pgm, rtnVar, day + "", true);
		}
		else if ("QMONTH".equalsIgnoreCase(sysVal))
		{
			int month = cal.get(Calendar.MONTH);
			CLUtils.setFieldVal(pgm, rtnVar, month + "", true);
		}
		else if ("QYEAR".equalsIgnoreCase(sysVal))
		{
			int year = cal.get(Calendar.YEAR);
			CLUtils.setFieldVal(pgm, rtnVar, year + "", true);
		}
		else if ("QCENTURY".equalsIgnoreCase(sysVal))
		{
			int century = (cal.get(Calendar.YEAR) / 100) + 1;
			CLUtils.setFieldVal(pgm, rtnVar, century + "", true);
		}
		else if ("QHOUR".equalsIgnoreCase(sysVal))
		{
			int hour = cal.get(Calendar.HOUR);
			CLUtils.setFieldVal(pgm, rtnVar, hour + "", true);
		}
		else if ("QMINUTE".equalsIgnoreCase(sysVal))
		{
			int minute = cal.get(Calendar.MINUTE);
			CLUtils.setFieldVal(pgm, rtnVar, minute + "", true);
		}
		else if ("QSECOND".equalsIgnoreCase(sysVal))
		{
			int second = cal.get(Calendar.SECOND);
			CLUtils.setFieldVal(pgm, rtnVar, second + "", true);
		}
	}

	/**
	 * Displays a list of all the spooled files on the system or a selected list
	 * from them.
	 *
	 * @param params WRKSPLF command parameters
	 */
	public static void wrksplf(String params)
	{
	}
}