package com.databorough.utils.cl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Arrays;

import com.databorough.utils.AS400Utils;
import com.databorough.utils.SQLUtils;
import com.databorough.utils.StringUtils;
import com.databorough.utils.Utils;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Provides conversion of SQL commands.
 *
 * @author Amit Arya
 * @since (2012-09-06.16:00:12)
 */
public final class SqlUtils
{
	private static ResultSet rs;

	/**
	 * The SqlUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private SqlUtils()
	{
		super();
	}

	/**
	 * Closes a database file opened by OPNQRYF command.
	 *
	 * @param params CLOF command parameters
	 */
	public static void clof(String params)
	{
		// CLOF OPNID(APPL)
		SQLUtils.close(rs, null);
	}

	public static void cpyfrmqryf(String params)
	{
	}

	public static void grtobjaut(String params)
	{
	}

	/**
	 * Opens a file that contains a set of database records that satisfies a
	 * database query request.
	 * FILE - SQL FROM statement
	 * QRYSLT - SQL WHERE statement
	 * GRPFLD - SQL GROUP BY statement
	 * GRPSLT - SQL HAVING statement
	 *
	 * @param params OPNQRYF command parameters
	 * @param pgm CL program
	 */
	public static void opnqryf(String params, Object pgm)
	{
		// OPNQRYF FILE(FILEA) QRYSLT('AMT *GT 1000.00')
		String fileName = CLUtils.getVarName(params, "FILE");
		String qrslt = CLUtils.getVarName(params, "QRYSLT");

		int indx = params.indexOf(qrslt);

		if ((indx > 0) && (params.charAt(indx - 1) == '&'))
		{
			qrslt = "&" + qrslt;
		}

		qrslt = StringUtils.replaceString(qrslt, "& ", "AND ");

		qrslt = StringUtils.replaceString(qrslt, "*GT", ">");
		qrslt = StringUtils.replaceString(qrslt, "*GE", ">=");
		qrslt = StringUtils.replaceString(qrslt, "*LT", "<");
		qrslt = StringUtils.replaceString(qrslt, "*LE", "<=");
		qrslt = StringUtils.replaceString(qrslt, "*EQ", "=");
		qrslt = StringUtils.replaceString(qrslt, "*OR", "OR");
		qrslt = StringUtils.replaceString(qrslt, "*AND", "AND");

		String strArr[] = qrslt.split(" ");
		int len = strArr.length;

		for (int i = 0; i < len; i++)
		{
			if (strArr[i].trim().startsWith("&"))
			{
				String var = strArr[i].trim().substring(1);
				String fieldVal = CLUtils.getFieldVal(pgm, var, false);

				if ((len > 1) && !"'".equals(strArr[i - 1].trim()) &&
						(Utils.length(fieldVal) == 0))
				{
					fieldVal = "0";
				}

				strArr[i] = fieldVal;
			}

			if (((i + 2) < len) &&
					(
						"*CAT".equalsIgnoreCase(strArr[i]) &&
						"*CAT".equalsIgnoreCase(strArr[i + 2])
					))
			{
				strArr[i + 1] = CLUtils.getFieldVal(pgm, strArr[i + 1], false);
			}
			else if ("*CAT".equalsIgnoreCase(strArr[i]))
			{
				strArr[i - 1] = CLUtils.getFieldVal(pgm, strArr[i + 1], false);
			}
		} // i

		qrslt = Arrays.toString(strArr);
		qrslt = StringUtils.replaceString(qrslt, ",", "");
		qrslt = qrslt.substring(1, qrslt.length() - 1);

		if (qrslt.startsWith("'\"' *CAT"))
		{
			qrslt = StringUtils.replaceString(qrslt, "'\"' *CAT ", "'");
		}
		else if (qrslt.startsWith("'") && qrslt.endsWith("'"))
		{
			qrslt = qrslt.substring(1, qrslt.length() - 1);
		}

		if (qrslt.indexOf("\"' *CAT") != -1)
		{
			qrslt = StringUtils.replaceString(qrslt, "\"' *CAT ", "'");
		}

		if (qrslt.indexOf(" *CAT '\"") != -1)
		{
			qrslt = StringUtils.replaceString(qrslt, " *CAT '\"", "'");
		}

		if (qrslt.indexOf("*CAT '") != -1)
		{
			qrslt = StringUtils.replaceString(qrslt, "*CAT '", "");
			qrslt = qrslt.substring(0, qrslt.length() - 1);
		}

		qrslt = qrslt.replace("\"", "'");

		// KEYFLD(( &SORT01) (&SORT02) (&SORT03) (&SORT04) (&SORT05))
		String keyFields = "";
		int indxKey = params.indexOf("KEYFLD(");

		if (indxKey != -1)
		{
			params = params.substring(indxKey + 7, params.lastIndexOf(")"));
		}

		params = params.replace("(", "");
		params = params.replace(")", "");

		String keys[] = params.split(" ");
		int numKeys = keys.length;

		for (int i = 0; i < numKeys; i++)
		{
			if (keys[i].startsWith("&"))
			{
				keyFields += (
					CLUtils.getFieldVal(pgm, keys[i].substring(1), false) +
					", "
				);
			}
			else if (keys[i].length() != 0)
			{
				keyFields += (keys[i].trim() + ", ");
			}
		} // i

		StringBuffer qry = new StringBuffer();
		qry.append("SELECT * FROM " + fileName + " WHERE (");
		qry.append(qrslt);
		qry.append(")");

		if (keyFields.length() != 0)
		{
			keyFields = keyFields.substring(0, keyFields.length() - 2);
			qry.append(" ORDER BY " + keyFields);
		}

		Statement stmt = null;

		try
		{
			Connection conn = AS400Utils.getConnection();
			stmt = conn.createStatement();

			// Execute the query
			rs = stmt.executeQuery(qry.toString());
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

	public static void rmvpftrg(String params)
	{
	}

	/**
	 * Runs an existing query or a default query.
	 *
	 * @param params RUNQRY command parameters
	 */
	public static void runqry(String params)
	{
		// RUNQRY QRYFILE((LIBX/FILE2 *FIRST))
		// OUTTYPE(*OUTFILE) OUTFILE(LIB2/OUT1 MBR4 *NEWMBR)
		String qryFile = CLUtils.getVarName(params, "QRYFILE");
		String outType = CLUtils.getVarName(params, "OUTTYPE");
		String outFile = CLUtils.getVarName(params, "OUTFILE");

		if ("*PRINTER".equalsIgnoreCase(outType))
		{
			return;
		}

		int indx = qryFile.indexOf('(');

		if (indx != -1)
		{
			qryFile = qryFile.substring(indx + 1, qryFile.indexOf(')'));
		}

		// fromLib, obj
		int indxSlash = qryFile.indexOf('/');
		int indxSpace = qryFile.indexOf(' ');

		if (indxSpace == -1)
		{
			indxSpace = qryFile.length();
		}

		String fromLib = qryFile.substring(0, indxSlash);
		String obj = qryFile.substring(indxSlash + 1, indxSpace);

		// toLib, newObj
		indxSlash = outFile.indexOf('/');
		indxSpace = outFile.indexOf(' ');

		if (indxSpace == -1)
		{
			indxSpace = outFile.length();
		}

		String toLib = outFile.substring(0, indxSlash);
		String newObj = outFile.substring(indxSlash + 1, indxSpace);

		if ("*OUTFILE".equalsIgnoreCase(outType))
		{
			Statement stmt = null;

			try
			{
				Connection conn = AS400Utils.getConnection();
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO " + toLib + newObj +
					" (SELECT * FROM " + fromLib + obj + ")");
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
		else
		{
			// *DISPLAY
			Statement stmt = null;
			ResultSet rs = null;

			try
			{
				Connection conn = AS400Utils.getConnection();
				stmt = conn.createStatement();

				// Execute the query
				rs = stmt.executeQuery("SELECT * FROM " + fromLib + obj);

				// Get the metadata
				ResultSetMetaData md = rs.getMetaData();
				int numCols = md.getColumnCount();

				// Print the column labels
				for (int i = 1; i <= numCols; i++)
				{
					logMessage(md.getColumnLabel(i) + " ");
				}

				// Loop through the result set
				while (rs.next())
				{
					for (int i = 1; i <= numCols; i++)
					{
						logMessage(rs.getString(i) + " ");
					}
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
		}
	}

	/**
	 * Processes a source file of SQL statements.
	 *
	 * @param params RUNSQLSTM command parameters
	 */
	public static void runsqlstm(String params)
	{
		// RUNSQLSTM SRCFILE(MYLIB/MYFILE) SRCMBR(MYMBR)
	}

	/**
	 * Runs a query.
	 *
	 * @param params STRQMQRY command parameters
	 */
	public static void strqmqry(String params)
	{
	}
}