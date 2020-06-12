package com.databorough.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.Utils.getArg;

/**
 * The user space APIs allow you to create and delete user spaces, change and
 * retrieve the contents of user spaces, and change and retrieve information
 * about user spaces.
 *
 * @author Zia Shahid
 * @since (2012-09-07.17:08:12)
 */
public class UserSpaceUtils
{
	/**
	 * Changes the contents of the user space (*USRSPC) object by moving a
	 * specified amount of data to the object.
	 *
	 * @param obj QUSCHGUS command arguments
	 */
	public static void changeUserSpace(Object... obj)
	{
		// Starting position
		int startPos = getArg(obj, 1);

		// Length of data
		int lenData = getArg(obj, 2);

		// Input data
		String inputData = (String)obj[3];

		Object outputData = retrieveUserSpace(obj);
		String chunk = objectToString(outputData);
		StringBuilder sb = new StringBuilder(chunk);

		if (Utils.length(inputData) < lenData)
		{
			String fmt = "%1$-" + lenData + "s";
			inputData = String.format(fmt, inputData);
		}

		sb.replace(startPos, startPos + lenData, inputData);
		setObject(outputData, sb);
		createUserSpace(outputData);
	}

	/**
	 * Changes the attributes of a user space object.
	 *
	 * @param obj QUSCUSAT command arguments
	 */
	public static void changeUserSpaceAttributes(Object... obj)
	{
	}

	/**
	 * Creates a user space in either the user domain or the system domain.
	 *
	 * @param obj QUSCRTUS command arguments
	 */
	public static void createUserSpace(Object... obj)
	{
		// Qualified user space
		Object qualUserSpace = obj[0];
		String userSpace = objectToString(qualUserSpace).substring(0, 10);

		writeObject(userSpace, qualUserSpace);
	}

	/**
	 * Deletes user spaces created with the Create User Space (QUSCRTUS) API.
	 *
	 * @param qualUserSpace qualified user space
	 * @param errorCode error code
	 */
	public static void deleteUserSpace(Object qualUserSpace, String errorCode)
	{
		String userSpace = objectToString(qualUserSpace).substring(0, 10);
		File file = getUserSpaceFilePath(userSpace);

		if (file.exists())
		{
			file.delete();
		}
	}

	public static File getUserSpaceFilePath(String file)
	{
		String dir = JSFUtils.getRealPath() + IOUtils.FILE_SEPARATOR;
		File userDir = IOUtils.validateDirName(dir, "userspace");

		String path =
			userDir.getPath() + IOUtils.FILE_SEPARATOR + file + ".ser";

		return new File(path);
	}

	/**
	 * Lists File Field Information. The list of fields is placed in a specified
	 * user space.
	 *
	 * @param obj QUSLFLD command arguments
	 */
	public static void listFields(Object... obj)
	{
		// Qualified user space
		Object qualUserSpace = obj[0];
		String userSpace = objectToString(qualUserSpace).substring(0, 10);

		// Qualified file
		Object qualFile = obj[2];
		String str = objectToString(qualFile);
		String file = str.substring(0, 10);
		String lib = str.substring(10);

		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;

		try
		{
			Connection conn = AS400Utils.getConnection();

			// Get the metadata
			DatabaseMetaData dbmd = conn.getMetaData();
			rs = dbmd.getColumns(null, lib, file, "%");

			// Print the column attributes
			sb.append("Name       Type       Size       Decimal\r\n");

			while (rs.next())
			{
				String field = rs.getString(4);
				String type = rs.getString(6);
				int size = rs.getInt(7);
				int decimal = rs.getInt(9);

				sb.append(padStringWithValue(field, " ", 11, false));
				sb.append(padStringWithValue(type, " ", 11, false));
				sb.append(padStringWithValue("" + size, " ", 11, false));
				sb.append(decimal + "\r\n");
			}
		}
		catch (SQLException sqe)
		{
			logStackTrace(sqe);
		}
		finally
		{
			SQLUtils.close(rs, null);
		}

		writeObject(userSpace, sb);
	}

	/**
	 * Retrieve the contents of a user space.
	 *
	 * @param obj QUSRTVUS command arguments
	 * @return contents of a user space
	 */
	public static Object retrieveUserSpace(Object... obj)
	{
		// Qualified user space
		Object qualUserSpace = obj[0];

		String userSpace = objectToString(qualUserSpace).substring(0, 10);
		File file = getUserSpaceFilePath(userSpace);
		ObjectInputStream os = null;
		UserSpace us = null;

		try
		{
			FileInputStream fs = new FileInputStream(file);
			os = new ObjectInputStream(fs);
			us = (UserSpace)os.readObject();
		}
		catch (Exception e)
		{
			logMessage(e.getMessage());
		}
		finally
		{
			try
			{
				os.close();
			}
			catch (Exception e)
			{
			}
		}

		return us.getUserObj();
	}

	/**
	 * Retrieves a pointer to the contents of a user-domain user space.
	 *
	 * @param obj QUSPTRUS command arguments
	 */
	public static void retrieveUserSpacePointer(Object... obj)
	{
	}

	/**
	 * Write the specified object to the ObjectOutputStream.
	 *
	 * @param userSpace file name
	 * @param obj the object to be written
	 */
	public static void writeObject(String userSpace, Object obj)
	{
		File file = getUserSpaceFilePath(userSpace);
		ObjectOutputStream os = null;

		try
		{
			FileOutputStream fs = new FileOutputStream(file);
			os = new ObjectOutputStream(fs);
			os.writeObject(obj);
		}
		catch (Exception e)
		{
			logMessage(e.getMessage());
		}
		finally
		{
			try
			{
				os.close();
			}
			catch (Exception e)
			{
			}
		}
	}
}