package com.databorough.utils.cl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.databorough.utils.AS400Utils.getConnection;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.SQLUtils.close;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.UserSpaceUtils.getUserSpaceFilePath;
import static com.databorough.utils.Utils.getLongObjectName;
import static com.databorough.utils.Utils.getPrefLongName;
import static com.databorough.utils.Utils.length;

/**
 * Provides conversion of Object commands.
 *
 * @author Zia Shahid
 * @since (2012-08-07.13:42:12)
 */
public final class ObjectUtils
{
	/**
	 * The ObjectUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private ObjectUtils()
	{
		super();
	}

	/**
	 * Is used in a job or thread to reserve an object or list of objects for
	 * use later in the job or thread.
	 *
	 * @param params ALCOBJ command parameters
	 */
	public static void alcobj(String params)
	{
	}

	public static void chgobjd(String params)
	{
	}

	public static void chgobjown(String params)
	{
	}

	public static void chkin(String params)
	{
	}

	/**
	 * Checks object existence.
	 *
	 * @param file object name
	 * @return <code>true</code> if file exists
	 */
	public static boolean chkobj(String file)
	{
		String ext = "";
		String dir = CLUtils.getRealPath() + "WEB-INF\\classes";

		int pos = file.lastIndexOf(".");

		if (pos != -1)
		{
			ext = file.substring(pos + 1);
		}

		if (length(ext) == 0)
		{
			file = file + ".class";
		}

		return (searchFile(dir, file) != null);
	}

	public static void chkout(String params)
	{
	}

	public static void crtdspf(String params)
	{
	}

	/**
	 * Copies a single object or a group of objects.
	 *
	 * @param params CRTDUPOBJ command parameters
	 * @return the row count for INSERT statement
	 */
	public static int crtdupobj(String params)
	{
		// CRTDUPOBJ OBJ(FILEA) FROMLIB(LIB1) OBJTYPE(*FILE) TOLIB(LIB2) DATA(*YES)
		String obj = CLUtils.getVarName(params, "OBJ");
		String fromLib = CLUtils.getVarName(params, "FROMLIB");

		if ("*LIBL".equalsIgnoreCase(fromLib) ||
				"*CURLIB".equalsIgnoreCase(fromLib))
		{
			fromLib = "";
		}
		else if (!"".equals(fromLib))
		{
			fromLib += "/";
		}

		String objTyp = CLUtils.getVarName(params, "OBJTYPE");

		String toLib = CLUtils.getVarName(params, "TOLIB");

		if ("*FROMLIB".equalsIgnoreCase(toLib) ||
				"*SAME".equalsIgnoreCase(toLib) ||
				"*CURLIB".equalsIgnoreCase(toLib))
		{
			toLib = "";
		}
		else if (!"".equals(fromLib))
		{
			toLib += "/";
		}

		String newObj = CLUtils.getVarName(params, "NEWOBJ");

		if ("".equals(newObj) || "*OBJ".equalsIgnoreCase(newObj) ||
				"*SAME".equalsIgnoreCase(newObj))
		{
			newObj = obj;
		}

		String data = CLUtils.getVarName(params, "DATA");

		if (!"*FILE".equalsIgnoreCase(objTyp) ||
				!"*YES".equalsIgnoreCase(data))
		{
			return 0;
		}

		Statement stmt = null;
		int rowCount = 0;

		try
		{
			Connection conn = getConnection();
			stmt = conn.createStatement();
			rowCount = stmt.executeUpdate("INSERT INTO " + toLib + newObj +
					" (SELECT * FROM " + fromLib + obj + ")");
		}
		catch (SQLException sqe)
		{
			logStackTrace(sqe);
		}
		finally
		{
			close(null, stmt);
		}

		return rowCount;
	}

	public static void crtmsgf(String params)
	{
	}

	public static void crtprtf(String params)
	{
	}

	public static void crtsavf(String params)
	{
	}

	public static void crtsrcpf(String params)
	{
	}

	/**
	 * Releases the allocations of the specified objects. The objects, allocated
	 * earlier by one or more Allocate Object (ALCOBJ) commands, are freed for
	 * use by other jobs, or threads.
	 *
	 * @param params DLCOBJ command parameters
	 */
	public static void dlcobj(String params)
	{
	}

	public static void dltmsgf(String params)
	{
	}

	/**
	 * Deletes a user space from the system.
	 *
	 * @param cmd DLTUSRSPC command
	 * @param obj Object
	 */
	public static void dltusrspc(String cmd, Object obj)
	{
		// DLTUSRSPC USRSPC(MYBEST/USRSPCTEST)
		// DLTUSRSPC USRSPC(&MYBEST/&USRSPCTEST)
		String userSpace = "";
		int pos = cmd.indexOf("USRSPC(");

		if (pos == -1)
		{
			return;
		}

		String userSpaceStr = cmd.substring(pos + 7, cmd.lastIndexOf(")"));

		pos = userSpaceStr.indexOf("/");

		if (pos != -1)
		{
			userSpaceStr = userSpaceStr.substring(pos + 1, userSpaceStr.length())
									   .trim();

			if (userSpaceStr.startsWith("&"))
			{
				userSpace = CLUtils.getFieldVal(obj, userSpaceStr.substring(1),
						true);
			}
			else
			{
				userSpace = userSpaceStr;
			}
		}
		else
		{
			userSpace = userSpaceStr;
		}

		File file = getUserSpaceFilePath(userSpace);

		try
		{
			if (file.exists())
			{
				file.delete();
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Shows one or more types of information retrieved from the file
	 * descriptions of one or more database files.
	 *
	 * @param params DSPFD command parameters
	 */
	public static void dspfd(String params)
	{
		// DSPFD FILE(LIBRARY1/FILE1)
		String file = CLUtils.getVarName(params, "FILE");
		int indx = file.indexOf('/');
		String lib = file.substring(0, indx);
		String obj = file.substring(indx + 1);

		ResultSet rs = null;

		try
		{
			Connection conn = getConnection();

			// Get the metadata
			DatabaseMetaData dbmd = conn.getMetaData();
			rs = dbmd.getColumns(null, lib, obj, "%");

			// Print the column attributes
			logMessage("Name       Type       Size       Decimal");

			while (rs.next())
			{
				String name = rs.getString(4);
				String type = rs.getString(6);
				int size = rs.getInt(7);
				int decimal = rs.getInt(9);

				logMessage(padStringWithValue(name, " ", 11, false));
				logMessage(padStringWithValue(type, " ", 11, false));
				logMessage(padStringWithValue("" + size, " ", 11, false));
				logMessage("" + decimal);
			}

			rs.close();

			// Primary Keys for Table
			/*rs = dbmd.getPrimaryKeys(null, lib, obj);

			while (rs.next())
			{
			    String name = rs.getString(4);
			}*/
		}
		catch (SQLException sqe)
		{
			logStackTrace(sqe);
		}
		finally
		{
			close(rs, null);
		}
	}

	public static void dspobjd(String params)
	{
	}

	// Creation date
	public static String getFileCrtDate(String filePath, String fileName)
	{
		String absPath = filePath;

		if (fileName != null)
		{
			absPath += ("\\" + fileName);
		}

		String line = "";
		Process process = null;
		Runtime rt = Runtime.getRuntime();

		try
		{
			process = rt.exec("cmd /c dir" + " " + absPath + " " + "/tc");
		}
		catch (Exception e)
		{
		}

		BufferedReader br =
			new BufferedReader(new InputStreamReader(process.getInputStream()));

		for (int i = 0; i < 6; i++)
		{
			try
			{
				line = br.readLine();
			}
			catch (Exception e)
			{
			}
		}

		StringTokenizer st = new StringTokenizer(line);
		String crtdate = st.nextToken();
		String ctrtime = st.nextToken();

		return crtdate + " " + ctrtime;
	}

	// Last Access Date
	public static String getFileLastAccDate(String filePath, String fileName)
	{
		String absPath = filePath;

		if (fileName != null)
		{
			absPath += ("\\" + fileName);
		}

		String line = "";
		Process process = null;
		Runtime rt = Runtime.getRuntime();

		try
		{
			process = rt.exec("cmd /c dir" + " " + absPath + " " + "/ta");
		}
		catch (Exception e)
		{
		}

		BufferedReader br =
			new BufferedReader(new InputStreamReader(process.getInputStream()));

		for (int i = 0; i < 6; i++)
		{
			try
			{
				line = br.readLine();
			}
			catch (Exception e)
			{
			}
		}

		StringTokenizer st = new StringTokenizer(line);
		String lastAccDate = st.nextToken();
		String lastAcctime = st.nextToken();

		return lastAccDate + " " + lastAcctime;
	}

	// Last Written Date or Last change Date
	public static String getFileLastWrtDate(String filePath, String fileName)
	{
		String absPath = filePath;

		if (fileName != null)
		{
			absPath += ("\\" + fileName);
		}

		String line = "";
		Process process = null;
		Runtime rt = Runtime.getRuntime();

		try
		{
			process = rt.exec("cmd /c dir" + " " + absPath + " " + "/tw");
		}
		catch (Exception e)
		{
		}

		BufferedReader br =
			new BufferedReader(new InputStreamReader(process.getInputStream()));

		for (int i = 0; i < 6; i++)
		{
			try
			{
				line = br.readLine();
			}
			catch (Exception e)
			{
			}
		}

		StringTokenizer st = new StringTokenizer(line);
		String lastWrtDate = st.nextToken();
		String lastWrtTime = st.nextToken();

		return lastWrtDate + " " + lastWrtTime;
	}

	/**
	 * Generates a list of lock information about a specific object or database
	 * file member and places the list into the specified user space.
	 *
	 * @param obj QWCLOBJL command parameters
	 */
	public static void listObjectLocks(Object... obj)
	{
	}

	public static void movobj(String params)
	{
	}

	public static void ovrdktf(String params)
	{
	}

	/**
	 * Retrieves object information about a specific object. This information is
	 * similar to the information returned using the RTVOBJD command.
	 *
	 * @param obj QUSROBJD command parameters
	 */
	public static void retrieveObjectDesc(Object... obj)
	{
		Object rcvvar = obj[0];
		String objName = ((String)obj[3]).substring(0, 10).trim();
		String objType = (String)obj[4];
		String objDesc = getLongObjectName(objName, objType);
		setObject(rcvvar, objDesc);
	}

	/**
	 * Changes the name of an object in a library.
	 *
	 * @param params RNMOBJ command parameters
	 */
	public static void rnmobj(String params)
	{
		// RNMOBJ OBJ(PAYROLL/FILEX) OBJTYPE(*FILE) NEWOBJ(MSTR)
		String objTyp = CLUtils.getVarName(params, "OBJTYPE");

		if (!"*FILE".equalsIgnoreCase(objTyp))
		{
			return;
		}

		AS400MgmtUtils.ren(params);
	}

	public static void rst(String params)
	{
	}

	public static void rstobj(String params)
	{
	}

	/**
	 * Retrieves the description of a specific object to a CL program.
	 *
	 * @param params RTVOBJD command parameters
	 * @param pgm CL program
	 */
	public static void rtvobjd(String params, Object pgm)
	{
		// RTVOBJD OBJ(*LIBL/PGMLIB) OBJTYPE(\LIB) TEXT(&TEXT) CRTDATE(&DATE)
		// OWNER(&OWN) STG(&STORE)
		String file = CLUtils.getVarName(params, "OBJ");

		if (file.contains("/"))
		{
			file = file.substring(file.indexOf('/') + 1);
		}

		String crtdate = CLUtils.getVarName(params, "CRTDATE");
		String text = CLUtils.getVarName(params, "TEXT");

		String longName = getPrefLongName(file);

		String ext = "";
		String dir = CLUtils.getRealPath() + "WEB-INF\\classes";

		int pos = longName.lastIndexOf(".");

		if (pos != -1)
		{
			ext = longName.substring(pos + 1);
		}

		if (length(ext) == 0)
		{
			longName = longName + ".class";
		}

		File searchFl = searchFile(dir, longName);

		if (searchFl != null)
		{
			String absPath = searchFl.getAbsolutePath();

			if (absPath.length() != 0)
			{
				CLUtils.setFieldVal(pgm, text, longName, false);

				String creationDate = getFileCrtDate(absPath, null);
				CLUtils.setFieldVal(pgm, crtdate, creationDate, false);
			}
		}
	}

	public static void rvkobjaut(String params)
	{
	}

	public static void savobj(String params)
	{
	}

	public static File searchFile(String dir, String fileToSearch)
	{
		List<File> files = new ArrayList<File>();
		searchFile(dir, fileToSearch, files);

		if (files.size() == 0)
		{
			return null;
		}

		//logMessage("File:" + files.get(0).getAbsolutePath());

		return files.get(0);
	}

	private static void searchFile(String dir, String fileToSearch,
		List<File> files)
	{
		if (length(dir) == 0)
		{
			return;
		}

		File filesList[] = new File(dir).listFiles();

		for (File file : filesList)
		{
			if (file.isFile())
			{
				String fileName = file.getName().toUpperCase().trim();

				if (fileName.equalsIgnoreCase(fileToSearch))
				{
					files.add(file);
					logMessage(fileName);

					break;
				}
			}

			if (file.isDirectory())
			{
				searchFile(file.getAbsolutePath(), fileToSearch, files);
			}
		} // file
	}

	public static void strprtwtr(String params)
	{
	}
}