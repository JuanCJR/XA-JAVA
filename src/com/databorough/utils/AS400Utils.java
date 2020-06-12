package com.databorough.utils;

import java.sql.Connection;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import static acdemxaMvcprocess.daoservices.SpringFramework.getBean;

import static com.databorough.utils.DateTimeConverter.formatDate;
import static com.databorough.utils.DateTimeConverter.getDateTimeFormat;
import static com.databorough.utils.DateTimeConverter.isDateFormat;
import static com.databorough.utils.JSFUtils.getSessionParam;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.NumberFormatter.toInt;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.User;

import org.apache.tomcat.jdbc.pool.DataSource;

public final class AS400Utils
{
	/**
	 * The AS400Utils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private AS400Utils()
	{
		super();
	}

	/**
	 * Returns the AS400 connection object from the ProxyConnection.
	 *
	 * @return AS400 connection object
	 * @since (2012-06-20.17:48:21)
	 */
	public static AS400JDBCConnection getAS400Conn()
	{
		AS400JDBCConnection as400Conn =
			getAS400Conn((Connection)getConnection());

		return as400Conn;
	}

	/**
	 * Returns the AS400 connection object from the Connection.
	 *
	 * @param proxyConn Connection object
	 * @return AS400 connection object
	 * @since (2012-03-22.16:18:21)
	 */
	private static AS400JDBCConnection getAS400Conn(Connection proxyConn)
	{
		AS400JDBCConnection as4conn = null;

		try
		{
			Object obj = proxyConn.getMetaData().getConnection();

			if (obj instanceof AS400JDBCConnection)
			{
				as4conn = (AS400JDBCConnection)obj;
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return as4conn;
	}

	public static Connection getConnection()
	{
		Connection conn = null;

		try
		{
			DataSource jpds = (DataSource)getBean("datasource");

			conn = jpds.getConnection();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return conn;
	}

	/**
	 * Returns the Connection object from the Connection pool. If object is not
	 * found, then it creates new object and adds to the existing pool.
	 *
	 * @param datasource DataSource object
	 * @param userId user Id
	 * @param password password
	 * @return connection object from the connection pool
	 * @throws SQLException
	 * @since (2012-03-22.17:15:07)
	 */
	public static Connection getConnectionFromPool(
		javax.sql.DataSource datasource, String userId, String password)
		throws SQLException
	{
		DataSource jpds = (DataSource)datasource;

		String jdbcUrl = jpds.getUrl();
		int indx = jdbcUrl.indexOf("prompt=true");

		if (indx != -1)
		{
			jdbcUrl = StringUtils.replaceString(jdbcUrl, "prompt=true",
					"prompt=false");
			jpds.setUrl(jdbcUrl);
		}

		// Allow and honor the getConnection(userId, password) call. There is a
		// performance impact turning this option on, even when not used due to
		// userId checks
		jpds.setAlternateUsernameAllowed(true);

		Connection proxyConn = jpds.getConnection(userId, password);
		Connection conn = getAS400Conn(proxyConn);

		if (conn == null)
		{
			conn = proxyConn;
		}

		return conn;
	}

	/**
	 * Returns the AS400 Job.
	 *
	 * @param createJob
	 * @return AS400 Job
	 * @since (2012-06-20.17:58:21)
	 */
	private static Job getJob(boolean createJob)
	{
		Job jb = null;

		try
		{
			CommandCall cc = getSessionParam("cmdCall");

			if (cc != null)
			{
				jb = cc.getServerJob();
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if ((jb == null) && createJob)
		{
			AS400JDBCConnection as400Conn = getAS400Conn();

			if (as400Conn == null)
			{
				return null;
			}

			String serverJobIdentifier = as400Conn.getServerJobIdentifier();
			String jobName = serverJobIdentifier.substring(0, 10).trim();
			String jobUser = serverJobIdentifier.substring(10, 20).trim();
			String jobNumber = serverJobIdentifier.substring(20).trim();

			jb = new Job(as400Conn.getSystem(), jobName, jobUser, jobNumber);
		}

		return jb;
	}

	/**
	 * Returns the date when the job was placed on the system.
	 *
	 * @return integer containing job date
	 * @since (2010-09-22.11:01:07)
	 */
	public static Integer getJobDate()
	{
		return toInt(formatDate(getJobDateDt(), "yyMMdd"), 10, 0);
	}

	/**
	 * Returns the date and time when the job was placed on the system.
	 *
	 * @return job date and time
	 * @since (2010-09-22.11:01:07)
	 */
	public static Date getJobDateDt()
	{
		Job jb = getJob(true);

		if (jb == null)
		{
			return null;
		}

		Date jbdt = null;

		try
		{
			jbdt = jb.getDate();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return jbdt;
	}

	/**
	 * Returns job's name of the server job.
	 *
	 * @return String containing server job's name
	 * @since (2010-06-15.06:34:24)
	 */
	public static String getJobName()
	{
		Job jb = getJob(false);

		if (jb != null)
		{
			return jb.getName();
		}

		AS400JDBCConnection as400Conn = getAS400Conn();

		if (as400Conn == null)
		{
			return "";
		}

		String serverJobIdentifier = as400Conn.getServerJobIdentifier();
		String jobName = serverJobIdentifier.substring(0, 10).trim();

		return jobName;
	}

	public static Integer getJobNumber()
	{
		Job jb = getJob(false);

		if (jb != null)
		{
			return toInt(jb.getNumber());
		}

		AS400JDBCConnection as400Conn = getAS400Conn();

		if (as400Conn == null)
		{
			return 0;
		}

		String serverJobIdentifier = as400Conn.getServerJobIdentifier();
		String jobNumber =
			StringUtils.trim(serverJobIdentifier.substring(20), "");

		return toInt(jobNumber);
	}

	/**
	 * Returns the time when the job was placed on the system.
	 *
	 * @return integer containing job time
	 * @since (2012-08-13.14:49:07)
	 */
	public static Integer getJobTime()
	{
		return toInt(formatDate(getJobDateDt(), "HHmmss"), 10, 0);
	}

	/**
	 * Returns the user ID of the server job.
	 *
	 * @return the user ID, or an empty string if not set
	 * @since (2010-06-14.14:33:02)
	 */
	public static String getJobUser()
	{
		String user = getSessionParam("user");

		if (user == null)
		{
			user = IBean.user;
		}

		if (user != null)
		{
			return user.toUpperCase();
		}

		AS400JDBCConnection as400Conn = getAS400Conn();

		if (as400Conn == null)
		{
			// com.mysql.jdbc.JDBC4Connection
			Connection proxyConn = getConnection();

			if (proxyConn == null)
			{
				return "";
			}

			String jobUser = "";

			try
			{
				// admin@localhost
				String str = proxyConn.getMetaData().getUserName();
				int indx = str.indexOf('@');

				if (indx != -1)
				{
					jobUser = str.substring(0, indx).toUpperCase();
				}
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}

			return jobUser;
		}

		String jobUser = as400Conn.getSystem().getUserId();

		return jobUser;
	}

	/**
	 * Returns the Current date/time of the server.
	 *
	 * @return Current date/time of the server
	 * @since (2012-09-17.18:52:08)
	 */
	private static Date getSysDate()
	{
		AS400 system = (AS400)getSessionParam("as400");

		if (system == null)
		{
			AS400JDBCConnection as400Conn = getAS400Conn();

			if (as400Conn == null)
			{
				return null;
			}

			system = as400Conn.getSystem();
		}

		Date dt = null;

		try
		{
			SystemValue sysVal = new SystemValue(system, "QDATETIME");
			Object val = sysVal.getValue();

			if (val == null)
			{
				return null;
			}

			String date = val.toString();
			date = date.substring(0, date.length() - 6);

			// Convert String to Date/Time
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			dt = df.parse(date);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return dt;
	}

	/**
	 * Returns the Current date/time of the server.
	 *
	 * @param rpgFormat format of date/time in RPG
	 * @return integer containing Current date/time of the server
	 * @since (2012-08-13.16:02:08)
	 */
	public static Integer getSysDateTime(String rpgFormat)
	{
		String inFormat = isDateFormat(rpgFormat) ? "yyyyMMdd" : "HHmmss";
		String outFormat =
			"*CYMD".equalsIgnoreCase(rpgFormat) ? "CyyMMdd" :
				getDateTimeFormat(rpgFormat, true);

		String ds = formatDate(getSysDate(), inFormat);
		int jobDateTime = toInt(formatDate(ds, inFormat, outFormat), 10, 0);

		return jobDateTime;
	}

	/**
	 * Returns user's description.
	 *
	 * @param system AS400
	 * @param userId String
	 * @return String containing user's description
	 * @since (2012-03-15.16:55:10)
	 */
	public static String getUserDescription(AS400 system, String userId)
	{
		if (system == null)
		{
			return userId;
		}

		try
		{
			User user = new User(system, userId);
			String userDesc = user.getDescription();

			return (Utils.length(userDesc) != 0) ? userDesc : userId;
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return userId;
	}

	/**
	 * Runs a single command.
	 *
	 * @param commandStr the command you want to run
	 * @since (2011-06-21.16:44:10)
	 */
	public static void runCommand(String commandStr)
	{
		if ((commandStr == null) || "".equals(commandStr))
		{
			return;
		}

		if (commandStr.startsWith("SBMJOB"))
		{
			// Run job using Spring Batch
			JobRunner.runJob(commandStr);

			return;
		}

		if (commandStr.startsWith("DLYJOB"))
		{
			// Delay job
			JobRunner.delayJob(commandStr);

			return;
		}

		DecimalFormat formatter = new DecimalFormat("0000000000.00000");
		String formattedLength = formatter.format((double)commandStr.length());
		String strSQL = "CALL QSYS/QCMDEXC (";
		strSQL += ("'" + commandStr + "'");
		strSQL += (" " + formattedLength + ")");

		try
		{
			CommandCall cc = getSessionParam("cmdCall");

			if (cc == null)
			{
				return;
			}

			boolean success = cc.run(strSQL);

			if (success)
			{
				LoggingAspect.appLogger.info("Execute command: " + commandStr);
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Runs a single command.
	 *
	 * @param commands the command you want to run
	 * @since (2012-04-24.11:27:50)
	 */
	public static void runCommand(String commands[])
	{
		if ((commands == null) || (commands.length == 0))
		{
			return;
		}

		runCommand(StringUtils.join(commands, " "));
	}
}
