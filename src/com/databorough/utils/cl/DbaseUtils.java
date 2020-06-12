package com.databorough.utils.cl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.databorough.utils.AS400Utils;
import com.databorough.utils.SQLUtils;
import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Provides conversion of Database commands.
 *
 * @author Amit Arya
 * @since (2012-09-05.14:19:12)
 */
public final class DbaseUtils
{
	/**
	 * The DbaseUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private DbaseUtils()
	{
		super();
	}

	/**
	 * Adds a named file member to a logical file, which must already exist on
	 * the system.
	 *
	 * @param params ADDLFM command parameters
	 * @return the row count for INSERT statement
	 */
	public static int addlfm(String params)
	{
		// ADDLFM FILE(INVENLIB/STOCKTXS) MBR(JANUARY)
		String file = CLUtils.getVarName(params, "FILE");
		String mbr = CLUtils.getVarName(params, "MBR");
		
		Statement stmt = null;
		int rowCount = 0;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();
			rowCount = stmt.executeUpdate("CREATE VIEW " + mbr +
					" AS SELECT * FROM " + file);
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
	 * Adds a named member to a physical file, which must already exist on the
	 * system.
	 *
	 * @param params ADDPFM command parameters
	 * @return the row count for INSERT statement
	 */
	public static int addpfm(String params)
	{
		// ADDPFM FILE(INVENTX) MBR(MONDAYTX)
		return addlfm(params);
	}

	/**
	 * Changes the attributes of a logical file and its members.
	 *
	 * @param params CHGLF command parameters
	 */
	public static void chglf(String params)
	{
	}

	/**
	 * Changes the attributes of a physical file and its members.
	 *
	 * @param params CHGPF command parameters
	 */
	public static void chgpf(String params)
	{
	}

	/**
	 * Changes the attributes of a physical file member.
	 *
	 * @param params CHGPFM command parameters
	 */
	public static void chgpfm(String params)
	{
	}

	/**
	 * Clear all the records from a physical file member.
	 *
	 * @param params CLRPFM command parameters
	 */
	public static void clrpfm(String params)
	{
		// CLRPFM FILE(*CURLIB/INV) MBR(FEB)
		String file = CLUtils.getVarName(params, "FILE");

		Statement stmt = null;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM " + file);
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
	 * Creates Logical File.
	 *
	 * @param params CRTLF command parameters
	 */
	public static void crtlf(String params)
	{
		// CRTLF FILE(INVEN/STOCKCTL) SRCFILE(SRCLIB/STKLFSRC)
	}

	/**
	 * Creates Physical File.
	 *
	 * @param params CRTPF command parameters
	 */
	public static void crtpf(String params)
	{
		// CRTPF FILE(PAYLIB/PAYTXS) SRCFILE(SRCLIB/PAYTXS)
	}

	// Display Database Relations
	/*public static void dspdbr(String params)
	{
	}*/

	// Display File Field Description
	/*public static void dspffd(String params)
	{
		return "";
	}*/

	// Open Data Base File
	/*public static void opndbf(String params)
	{
		return "";
	}*/

	// Position Data Base File
	// Becomes a SETLL, which the Java conversion appropriately handles
	/*public static void posdbf(String params)
	{
		return "";
	}*/

	/**
	 * Reorganizes Physical File Member.
	 *
	 * @param params RGZPFM command parameters
	 */
	public static void rgzpfm(String params)
	{
	}

	/**
	 * Removes one or more members from the specified physical file or logical
	 * file.
	 *
	 * @param params RMVM command parameters
	 */
	public static void rmvm(String params)
	{
		// RMVM FILE(JOBHIST1) MBR(JOBHIST1A)
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
	 * Changes the name of a specified file member.
	 *
	 * @param params RNMM command parameters
	 */
	public static void rnmm(String params)
	{
		// RNMM FILE(ELEMENT) MBR(LEAD) NEWMBR(GOLD)
		String mbr = CLUtils.getVarName(params, "MBR");
		String newMbr = CLUtils.getVarName(params, "NEWMBR");

		Statement stmt = null;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("ALTER TABLE " + mbr + " RENAME TO " + newMbr);
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
}