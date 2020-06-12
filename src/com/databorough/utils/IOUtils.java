package com.databorough.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Utility class for Input/Output.
 * <p>
 * The IOUtils class is not to be instantiated, only use its public static
 * members outside
 *
 * @author Amit Arya
 * @since (2002-10-15.07:25:06)
 */
public final class IOUtils
{
	/**
	 * The IOUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private IOUtils()
	{
		super();
	}

	/**
	 * File separator e.g. "/".
	 */
	public static final String FILE_SEPARATOR =
		System.getProperty("file.separator");

	/**
	 * Creates a new File instance from a parent pathname string and a child
	 * pathname string.
	 *
	 * @param parent pathname of the parent
	 * @param child pathname of the child
	 * @return File
	 * @since (2004-05-25.10:38:16)
	 */
	private static File constructFile(String parent, String child)
	{
		if (parent == null)
		{
			return null;
		}

		// Remove quote
		parent = StringUtils.deleteString(parent, "\"");
		child = StringUtils.deleteString(child, "\"");

		File dir;

		if ((parent != null) && (parent.length() > 0))
		{
			if ((child != null) && (child.length() > 0))
			{
				dir = new File(parent, child);
			}
			else
			{
				dir = new File(parent);
			}
		}
		else
		{
			if ((child != null) && (child.length() > 0))
			{
				dir = new File(child);
			}
			else
			{
				dir = null;
			}
		}

		return dir;
	}

	/**
	 * Checks whether the file denoted by this abstract pathname exists.
	 *
	 * @param file File name
	 * @return true if the file exists, otherwise false
	 * @since (2004-08-04.06:58:16)
	 */
	public static boolean exists(File file)
	{
		if (file == null)
		{
			return false;
		}

		return file.exists();
	}

	/**
	 * Checks whether the file denoted by this parent abstract pathname and a
	 * child pathname string exists.
	 *
	 * @param parent parent directory
	 * @param child child directory
	 * @return true if the file exists, otherwise false
	 * @since (2004-08-04.06:55:13)
	 */
	public static boolean exists(String parent, String child)
	{
		File file = constructFile(parent, child);

		return exists(file);
	}

	/**
	 * Returns directory with file.
	 *
	 * @param dir directory name
	 * @param file file name
	 * @return directory with file name
	 * @since (2003-07-29.12:11:02)
	 */
	public static String getDirWithFile(String dir, String file)
	{
		if ((dir == null) || (dir.length() == 0))
		{
			return file;
		}

		dir = pathToDir(dir);

		if ((file == null) || (file.length() == 0))
		{
			return dir;
		}

		String dirFile = dir;

		if (!dir.endsWith(FILE_SEPARATOR))
		{
			dirFile += FILE_SEPARATOR;
		}

		dirFile += file;

		return dirFile;
	}

	/**
	 * Converts path to directory.
	 *
	 * @param path path name
	 * @return directory created from path name
	 * @since (2005-01-12.07:29:01)
	 */
	private static String pathToDir(String path)
	{
		if ((path == null) || (path.length() == 0))
		{
			return path;
		}

		return StringUtils.replaceString(path, "/", FILE_SEPARATOR);
	}

	/**
	 * Reads file and returns the contents as string.
	 *
	 * @param inputFile file whose contents are to be read
	 * @param appendStr string to be appended in the content
	 * @return contents of the file
	 * @throws IOException if some I/O operation is failed or interrupted.
	 * @since (2003-05-16.18:21:21)
	 */
	public static String readFile(File inputFile, String appendStr)
		throws IOException
	{
		if (inputFile == null)
		{
			return null;
		}

		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();

		try
		{
			fr = new FileReader(inputFile);
			br = new BufferedReader(fr);

			if (appendStr == null)
			{
				appendStr = "";
			}

			String line = null;

			while ((line = br.readLine()) != null)
			{
				sb.append(line).append(appendStr);
			}
		}
		catch (IOException ioe)
		{
			logStackTrace(ioe);
			throw ioe;
		}
		finally
		{
			if (fr != null)
			{
				try
				{
					fr.close();
				}
				catch (Exception e)
				{
				}
			}

			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (Exception e)
				{
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Reads file and returns the contents as string.
	 *
	 * @param dir directory name
	 * @param file file name
	 * @param appendStr string to be appended in the content
	 * @return contents of the file
	 * @throws IOException if some I/O operation is failed or interrupted.
	 * @since (2003-05-16.18:05:09)
	 */
	public static String readFile(String dir, String file, String appendStr)
		throws IOException
	{
		if ((dir == null) || (file == null))
		{
			return null;
		}

		File inputFile = new File(dir, file);

		return readFile(inputFile, appendStr);
	}

	/**
	 * Validates the existing directory name and creates the directory if it
	 * does not exist or if it is not a directory.
	 *
	 * @param dirName directory to be validated
	 * @return a directory
	 * @since (2002-04-30.15:56:10)
	 */
	public static File validateDirName(File dirName)
	{
		if (dirName == null)
		{
			return null;
		}

		if (!dirName.exists())
		{
			dirName.mkdirs();
		}
		else if (!dirName.isDirectory())
		{
			dirName.delete();
			dirName.mkdirs();
		}

		return dirName;
	}

	/**
	 * Validates the existing directory name and creates the directory if it
	 * does not exist or is not a directory.
	 *
	 * @param dirName directory name
	 * @return a directory
	 * @since (2003-05-02.06:31:57)
	 */
	public static File validateDirName(String dirName)
	{
		return validateDirName(dirName, null);
	}

	/**
	 * Validates the existing directory name and creates the directory if it
	 * does not exist or is not a directory.
	 *
	 * @param parent parent directory
	 * @param child child directory
	 * @return a directory
	 * @since (2003-07-31.13:22:00)
	 */
	public static File validateDirName(String parent, String child)
	{
		parent = StringUtils.trim(parent, null);

		File dir = constructFile(parent, child);

		return validateDirName(dir);
	}
}