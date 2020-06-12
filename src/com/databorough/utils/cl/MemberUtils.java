package com.databorough.utils.cl;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.databorough.utils.AS400Utils;
import com.databorough.utils.SQLUtils;
import com.databorough.utils.Utils;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.cl.ObjectUtils.getFileCrtDate;
import static com.databorough.utils.cl.ObjectUtils.getFileLastWrtDate;
import static com.databorough.utils.cl.ObjectUtils.searchFile;

/**
 * Provides conversion of Member commands.
 *
 * @author Zia Shahid
 * @since (2012-08-06.15:22:12)
 */
public final class MemberUtils
{
	/**
	 * The MemberUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private MemberUtils()
	{
		super();
	}

	/*public static void callsubr(String params, Object pgm)
	{
	}*/

	/*public static void chgpgm(String params, Object pgm)
	{
	}*/

	/*public static void endsubr(String params, Object pgm)
	{
	}*/

	/**
	 * Retrieves specific information about a single database file member and
	 * returns the information to the calling program in a receiver variable.
	 * You can only use the QUSRMBRD API with database file types *PF, *LF, and
	 * *DDMF.
	 *
	 * @param obj QUSRMBRD command parameters
	 */
	public static void retrieveMemberDesc(Object... obj)
	{
	}

	/**
	 * Retrieves (returns) the member-level information (in CL variables) from a
	 * database file.
	 *
	 * @param params RTVMBRD command parameters
	 * @param pgm CL program
	 */
	public static void rtvmbrd(String params, Object pgm)
	{
		// RTVMBRD FILE(&LIB/MYFILE) MBR(&MBR \NEXT) RTNMBR(&MBR)
		// CRTDATE(&CRTDATE) TEXT(&TEXT) CHGDATE(&CHGDATE) NBRCURRCD(&NBRRCD)
		// DTASPCSIZ(&SIZE)
		String file = CLUtils.getVarName(params, "FILE");

		if (file.contains("/"))
		{
			file = file.substring(file.indexOf('/') + 1);
		}

		String crtdate = CLUtils.getVarName(params, "CRTDATE");
		String text = CLUtils.getVarName(params, "TEXT");
		String chgdate = CLUtils.getVarName(params, "CHGDATE");

		String longName = Utils.getPrefLongName(file);

		String ext = "";
		String dir = CLUtils.getRealPath() + "WEB-INF\\classes";

		int pos = longName.lastIndexOf(".");

		if (pos != -1)
		{
			ext = longName.substring(pos + 1);
		}

		if (Utils.length(ext) == 0)
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

				String changeDate = getFileLastWrtDate(absPath, null);
				CLUtils.setFieldVal(pgm, chgdate, changeDate, false);
			}
		}

		String nbrcurrcd = CLUtils.getVarName(params, "NBRCURRCD");

		if (nbrcurrcd.length() != 0)
		{
			Statement stmt = null;
			ResultSet rs = null;
			int rowCount = 0;

			try
			{
				Connection conn = AS400Utils.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT COUNT(*) FROM " + file);

				if (rs.next())
				{
					rowCount = rs.getInt(1);
				}
			}
			catch (SQLException sqe)
			{
				logStackTrace(sqe);
			}
			finally
			{
				SQLUtils.close(rs, stmt);
			}

			CLUtils.setFieldVal(pgm, nbrcurrcd, "" + rowCount, false);
		}
	}

	/*public static void subr(String params, Object pgm)
	{
	}*/
}