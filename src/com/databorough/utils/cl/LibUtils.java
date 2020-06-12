package com.databorough.utils.cl;

import java.io.IOException;
import java.util.Properties;

/**
 * Provides conversion of Library commands.
 *
 * @author Amit Arya
 * @since (2012-09-05.19:31:12)
 */
public final class LibUtils
{
	/**
	 * The LibUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private LibUtils()
	{
		super();
	}

	/**
	 * Adds a library name to the user portion of the library list for the
	 * current thread. The user portion is the last portion of the library list.
	 *
	 * @param params ADDLIBLE command parameters
	 * @param object the runtime class of an object
	 * @return datasource properties
	 */
	public static Properties addlible(String params, Object obj)
	{
		// ADDLIBLE LIB(TESTLIB) POSITION(*LAST)
		String lib = CLUtils.getVarName(params, "LIB");

		if (params.indexOf("LIB(&") != -1)
		{
			// ADDLIBLE LIB(&TSTLIB)
			lib = CLUtils.getFieldVal(obj, lib, false);
		}

		String propsFile = "datasource.properties";

		Properties props = new Properties();

		// Read properties file
		try
		{
			ClassLoader loader = LibUtils.class.getClassLoader();
			props.load(loader.getResourceAsStream(propsFile));
		}
		catch (IOException ioe)
		{
		}

		String url = (String)props.get("url");
		int indx = url.indexOf("libraries=");

		if (indx != -1)
		{
			int indxSemicolon = url.indexOf(";", indx + 10);
			String libs = url.substring(indx + 10, indxSemicolon);
			url = url.replace(libs, libs + " " + lib);
			props.put("url", url);
		}

		String libraries = (String)props.get("libraries");
		props.put("libraries", libraries + " " + lib);

		/* Cannot save props to the stream since a file contained in a deployed
		 * web app should not be changed. Control has been handed over to the
		 * container at that point and the file may be overwritten, or it may
		 * not even be writable. It also puts a burden on the application
		 * deployer, because now they cannot simply blow away the exploded WAR
		 * folder (if one exists) and redeploy the archive.
		 */

		return props;
	}

	/**
	 * Replaces the current library entry in the library list for the current
	 * thread.
	 *
	 * @param params CHGCURLIB command parameters
	 */
	public static void chgcurlib(String params)
	{
	}

	/**
	 * Changes the user portion of the library list for the current thread with
	 * the list of libraries specified by the user.
	 *
	 * @param params CHGLIBL command parameters
	 */
	public static void chglibl(String params)
	{
	}

	/**
	 * Deletes all of the objects that you have the authority to delete from the
	 * specified library.
	 *
	 * @param params CLRLIB command parameters
	 */
	public static void clrlib(String params)
	{
	}

	/**
	 * Creates a library.
	 *
	 * @param params CRTLIB command parameters
	 */
	public static void crtlib(String params)
	{
	}

	/**
	 * Deletes a specified library from the system after all objects in the
	 * library have been deleted. If a library that is deleted contains objects,
	 * this command first deletes all of the objects and then deletes the
	 * library.
	 *
	 * @param params DLTLIB command parameters
	 */
	public static void dltlib(String params)
	{
	}

	/**
	 * Removes a library from the user portion of the library list for the
	 * current thread. The user portion is the last portion of the library list.
	 *
	 * @param params RMVLIBLE command parameters
	 * @param object the runtime class of an object
	 * @return datasource properties
	 */
	public static Properties rmvlible(String params, Object obj)
	{
		// RMVLIBLE LIB(TESTLIB)
		String lib = CLUtils.getVarName(params, "LIB");

		if (params.indexOf("LIB(&") != -1)
		{
			// RMVLIBLE LIB(&TSTLIB)
			lib = CLUtils.getFieldVal(obj, lib, false);
		}

		String propsFile = "datasource.properties";

		Properties props = new Properties();

		// Read properties file
		try
		{
			ClassLoader loader = LibUtils.class.getClassLoader();
			props.load(loader.getResourceAsStream(propsFile));
		}
		catch (IOException ioe)
		{
		}

		String url = (String)props.get("url");
		int indx = url.indexOf("libraries=");

		if (indx != -1)
		{
			int indxSemicolon = url.indexOf(";", indx + 10);
			String libs = url.substring(indx + 10, indxSemicolon);
			url = url.replace(libs, libs.replace(lib, ""));
			props.put("url", url);
		}

		String libraries = (String)props.get("libraries");
		props.put("libraries", libraries.replace(lib, ""));

		/* Cannot save props to the stream since a file contained in a deployed
		 * web app should not be changed. Control has been handed over to the
		 * container at that point and the file may be overwritten, or it may
		 * not even be writable. It also puts a burden on the application
		 * deployer, because now they cannot simply blow away the exploded WAR
		 * folder (if one exists) and redeploy the archive.
		 */

		return props;
	}

	/**
	 * Restores to the system one library or a group of libraries that was saved
	 * by the Save Library (SAVLIB) command.
	 *
	 * @param params RSTLIB command parameters
	 */
	public static void rstlib(String params)
	{
	}
}