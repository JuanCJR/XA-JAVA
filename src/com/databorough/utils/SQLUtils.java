package com.databorough.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.genericdao.search.Filter;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Utility class for SQL.
 * <p>
 * The SQLUtils class is not to be instantiated, only use its public static
 * members outside.
 *
 * @author Amit Arya
 * @since (2003-01-28.09:50:38)
 */
public final class SQLUtils
{
	/**
	 * Default constructor.
	 */
	private SQLUtils()
	{
		super();
	}

	/**
	 * Closes the ResultSet & Statement.
	 *
	 * @param rs a <code>ResultSet</code> object representing a database result
	 *        set
	 * @param stmt a <code>Statement</code> object for executing a static SQL
	 *        statement and returning the results it produces
	 * @since (2004-12-21.10:30:47)
	 */
	public static void close(ResultSet rs, Statement stmt)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
				rs = null;
			}
			catch (Exception sqe)
			{
			}
		}

		if (stmt != null)
		{
			try
			{
				setMaxRows(stmt, 0);
				stmt.close();
				stmt = null;
			}
			catch (Exception sqe)
			{
			}
		}
	}

	/**
	 * Escapes raw text for placement in a SQL expression.
	 *
	 * @param strToEscape String to escape
	 * @return string where raw text has been escaped
	 * @since (2003-04-03.11:51:44)
	 */
	public static String escape(String strToEscape)
	{
		int len = Utils.length(strToEscape);

		if (len == 0)
		{
			return "";
		}

		StringBuffer strToReturn = new StringBuffer();

		for (int i = 0; i < len; i++)
		{
			char ch = strToEscape.charAt(i);

			switch (ch)
			{
			case '\'':
				strToReturn.append('\'');
				strToReturn.append('\'');

				break;

			default:
				strToReturn.append(ch);

				break;
			} // switch (ch)
		} // i

		return strToReturn.toString();
	}

	public static String getOperator(int oprTyp)
	{
		String ret = "";

		switch (oprTyp)
		{
		case Filter.OP_GREATER_OR_EQUAL:
		{
			ret = " >= ";

			break;
		}

		case Filter.OP_GREATER_THAN:
		{
			ret = " > ";

			break;
		}

		case Filter.OP_LESS_OR_EQUAL:
		{
			ret = " <= ";

			break;
		}

		case Filter.OP_LESS_THAN:
		{
			ret = " < ";

			break;
		}

		default:
			ret = " = ";
		}
		
		return ret;
	}

	/**
	 * Gets Time from Timestamp.
	 *
	 * @param ts Timestamp object
	 * @return value of the designated column in the current row as Time object
	 * @since (2001-08-27.10:20:31)
	 */
	private static Time getTime(Timestamp ts)
	{
		// If ts is null then simply return null
		if (ts == null)
		{
			return null;
		}

		Time dt = new Time(ts.getTime());

		return dt;
	}

	/**
	 * This is a workaround for MS Access database which does not support
	 * rs.getTime().
	 *
	 * @param rs ResultSet containing table columns
	 * @param tableColumnIndex column index
	 * @return value of the designated column in the current row as Time object
	 * @throws SQLException An exception that provides information on a database
	 *            access error or other errors
	 * @since (2001-08-24.17:20:40)
	 */
	private static Time getTime(ResultSet rs, int tableColumnIndex)
		throws SQLException
	{
		Time dt;

		try
		{
			dt = rs.getTime(tableColumnIndex);
		}
		catch (InternalError ie)
		{
			Timestamp ts = rs.getTimestamp(tableColumnIndex);
			dt = getTime(ts);
		}

		return dt;
	}

	public static Object getValidVal(Object val, int oprTyp)
	{
		if (oprTyp == Filter.OP_LIKE)
		{
			if ("?".equals(val))
			{
				return " LIKE %?%";
			}
			else
			{
				return " LIKE '%" + val + "%'";
			}
		}

		String ret = getOperator(oprTyp);

		if ("?".equals(val))
		{
			return ret + "?";
		}

		if (val instanceof CharSequence || val instanceof Date ||
				val instanceof Time || val instanceof Timestamp)
		{
			return ret + "'" + val + "'";
		}

		return ret + val;
	}

	/**
	 * Gets the value of the specified column.
	 *
	 * @param rs ResultSet containing table columns
	 * @param tableColumnIndex column
	 * @param tableColumnType the designated column's SQL type
	 * @return value of the specified column
	 * @throws SQLException An exception that provides information on a database
	 *         access error or other errors
	 * @since (2003-09-30.17:23:18)
	 */
	public static String getValue(ResultSet rs, int tableColumnIndex,
		int tableColumnType) throws SQLException
	{
		if (rs == null)
		{
			return null;
		}

		String retStr = null;

		switch (tableColumnType)
		{
		case Types.DATE:
		{
			Date dt = rs.getDate(tableColumnIndex);

			if (dt != null)
			{
				retStr = dt.toString();
			}

			break;
		}

		case Types.TIME:
		{
			Time dt = getTime(rs, tableColumnIndex);

			if (dt != null)
			{
				retStr = dt.toString();
			}

			break;
		}

		case Types.TIMESTAMP:
		{
			Timestamp dt = rs.getTimestamp(tableColumnIndex);

			if (dt != null)
			{
				retStr = dt.toString();
			}

			break;
		}

		default:
		{
			retStr = rs.getString(tableColumnIndex);

			break;
		}
		} // switch

		return retStr;
	}

	/**
	 * Get if SETLL has placed the pointer on a record
	 * which is exactly matched with given criteria
	 *
	 * @param filterlist, criteria
	 * @param pfObj, record where the pointer has been palced by SETLL
	 * @return boolean, equal record found or not
	 */
	public static boolean isEqualRecordFound(List<FilterData> filterlist,
		Object pfObj)
	{
		EqualsBuilder equalbuilder = new EqualsBuilder();
		Map<String, Object> map = new HashMap<String, Object>();

		if (pfObj != null)
		{
			populatePosKeyCriterias("", pfObj, map);
		}

		int size = filterlist.size();

		for (int i = 0; i < size; i++)
		{
			FilterData filterData = (FilterData)filterlist.get(i);
			String key = filterData.getProperty();
			Object value = filterData.getValue();
			Object keyval = map.get(key);
			equalbuilder.append(keyval, value);
		}

		return equalbuilder.isEquals();
	}

	protected static void populatePosKeyCriterias(String prefix, Object obj,
		Map<String, Object> map)
	{
		Class<?> cls = obj.getClass();
		Field sourceFlds[] = cls.getDeclaredFields();

		try
		{
			int count = (sourceFlds == null) ? 0 : sourceFlds.length;

			for (int indx = 0; indx < count; indx++)
			{
				Field srcField = sourceFlds[indx];
				srcField.setAccessible(true);

				int modifiers = srcField.getModifiers();

				if (Modifier.isFinal(modifiers))
				{
					continue;
				}

				Object data = srcField.get(obj);

				if ((data != null) &&
						IXRedoModel.class.isAssignableFrom(data.getClass()))
				{
					populatePosKeyCriterias(prefix + srcField.getName() + ".",
						data, map);
				}
				else
				{
					map.put(prefix + srcField.getName(), data);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Max rows set the limit for the maximum number of rows that any ResultSet
	 * object can contain to the given number. If the limit is exceeded, the
	 * excess rows are silently dropped.
	 * <p>
	 * Fetch size gives the JDBC driver a hint as to the number of rows that
	 * should be fetched from the database when more rows are needed. If the
	 * value specified is zero, then the hint is ignored.
	 * <p>
	 * maxRows - the new max rows limit; zero means there is no limit maxRows -
	 * the number of rows to fetch
	 *
	 * @param stmt statement on which maximum fetched data is to be fixed
	 * @param maxRows the number of rows to fetch
	 * @throws SQLException if a database access error occurs
	 * @since (2004-11-18.18:34:18)
	 */
	public static void setMaxRows(Statement stmt, int maxRows)
		throws SQLException
	{
		if (stmt == null)
		{
			return;
		}

		// Use Statement::setMaxRows(int max) for every paging query as it
		// guarantees that you fetch only one page of data for any given
		// request.
		if (maxRows > 0)
		{
			//stmt.setMaxRows(maxRows + 2);
			stmt.setMaxRows(maxRows + 1);
		}
		else
		{
			stmt.setMaxRows(0);
		}

		// For optimum performance, use Statement::setFetchSize(int rows)
		// to set the fetch size to match the page size. This guarantees
		// that the page will be fetched in one low-level IO request
		// between the driver and the DBMS.
		//stmt.setFetchSize(maxRows + 2);
	}
}