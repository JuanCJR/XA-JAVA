package com.databorough.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import static com.databorough.utils.AS400Utils.getConnection;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.NumberFormatter.toLong;
import static com.databorough.utils.ReflectionUtils.getFld2ColumnAnnoMap;
import static com.databorough.utils.ReflectionUtils.getFldByName;
import static com.databorough.utils.ReflectionUtils.setCompatibleVal;
import static com.databorough.utils.ReflectionUtils.updKeyFlds;
import static com.databorough.utils.StringUtils.countMatches;
import static com.databorough.utils.StringUtils.setStr;

public class EmbedSQL
{
	private Connection conn;
	private PreparedStatement pstmt;

	private ResultSet rs;
	private ResultSetMetaData rsmd;
	public SQLCA sqlca;
	public Object result[];
	private int colsCnt;

	public EmbedSQL(SQLCA sqlca)
	{
		this.sqlca = sqlca;
	}

	public void close()
	{
		try
		{
			if (pstmt != null)
			{
				pstmt.close();
			}

			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			handleSQLException(e);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public void declare(String qryStr, Object... params)
	{
		try
		{
			conn = getConnection();

			if ((qryStr.indexOf("rownumber()") != -1) &&
					(conn.getMetaData().getConnection().toString()
							.indexOf("mysql") != -1))
			{
				// com.mysql.jdbc.JDBC4Connection
				qryStr = getRowNumberQry(qryStr);
			}

			pstmt = conn.prepareStatement(qryStr);
		}
		catch (SQLException e)
		{
			handleSQLException(e);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		int numParams = Utils.length(params);

		if (numParams == 0)
		{
			return;
		}

		for (int i = 1; i <= numParams; i++)
		{
			try
			{
				pstmt.setObject(i, params[i - 1]);
			}
			catch (SQLException sqe)
			{
				if ("07006".equals(sqe.getSQLState()))
				{
					// java.sql.SQLException: Data type mismatch
					try
					{
						String param = (String)params[i - 1];

						if ((countMatches(param, "-") == 2) ||
								(countMatches(param, "/") == 2))
						{
							// 2014-10-06
							pstmt.setDate(i, Date.valueOf(param));
						}
					}
					catch (SQLException e)
					{
						handleSQLException(e);
					}
				}
				else
				{
					handleSQLException(sqe);
				}
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}
	}

	public void execSQL(String qryStr, Object... args)
	{
		declare(qryStr, args);
		open();
		next();
		close();
	}

	/**
	 * Maps JDBC ResultSet to Annotated POJO using annotations. The 'Column'
	 * annotation specifies the SQL column name. The annotation 'Entity' is
	 * required to mark the class capable of hold database values.
	 *
	 * @param entityObj
	 * @since (2014-06-01.09:44:35)
	 */
	public void fetchInto(Object entityObj)
	{
		if ((rs == null) || (result == null))
		{
			return;
		}

		Class<?> entityClass = entityObj.getClass();

		if (entityClass.getSuperclass() != Object.class)
		{
			entityClass = entityClass.getSuperclass();
		}

		// Check if Output Class has 'Entity' annotation
		if (!entityClass.isAnnotationPresent(Entity.class))
		{
			return;
		}

		// Get all the attributes of Output Class
		LinkedHashMap<String, Column> columnFieldMap =
			new LinkedHashMap<String, Column>();
		List<Field> fields =
			getFld2ColumnAnnoMap(entityClass, columnFieldMap, "");

		for (int i = 0; i < colsCnt; i++)
		{
			try
			{
				// Read the SQL column value
				Object columnValue = result[i];

				if (columnValue == null)
				{
					continue;
				}

				// Get the SQL column name
				String columnName = rsmd.getColumnName(i + 1);

				Field entityField =
					getFldByName(columnName, fields, columnFieldMap);

				if (entityField == null)
				{
					continue;
				}

				if (Number.class.isAssignableFrom(entityField.getType()))
				{
					setCompatibleVal(entityField, entityObj,
						(Number)columnValue);
				}
				else
				{
					entityField.setAccessible(true);
					entityField.set(entityObj, columnValue);
					entityField.setAccessible(false);
				}
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		} // i

		updKeyFlds(entityObj, "");
	}

	public int getColsCnt()
	{
		return colsCnt;
	}

	/**
	 * Gets rownumber() query for MySQL database.
	 *
	 * @param qryStr query string
	 * @return modified query string
	 * @since (2014-08-01.10:44:35)
	 */
	private String getRowNumberQry(String qryStr)
	{
		// select * from (select A.*,B.*,rownumber() over(order by
		// A.ADADCD, A.ADAECD) as rownum from TSADREP A left outer join TSACREP B on (
		// B.ACADCD = A.ADADCD) where A.ADADCD = ?) as data where rownum between 1 and
		// 19999999999997

		// select * from (select A.*,rownumber() over(order by A.ACAGST, 
		// A.ACADCD) as rownum , (select coalesce(count(*),0) as Number_of_wards
		// from TSADREP as B where B.ADADCD = A.ACADCD) , (select
		// coalesce(count(*),0) as Number_5 from TSAEREP as C where
		// C.AEADCD = A.ACADCD) from TSACREP A) as data where rownum between 1
		// and 19999999999997
		int indx = qryStr.indexOf(' ', qryStr.indexOf("("));
		String tableName = qryStr.substring(indx + 1, qryStr.indexOf("."));
		String aliasName = "";

		// from TSADREP A left
		int indxFrom = qryStr.lastIndexOf("from");
		int indx1 = qryStr.indexOf(" left", indxFrom);

		if (indx1 == -1)
		{
			// from TSAJREP A where
			int indxWhere = qryStr.indexOf(" where", indxFrom);
			// from TSACREP A)
			int indxBracket = qryStr.indexOf(")", indxFrom);

			if ((indxWhere != -1) && (indxWhere < indxBracket))
			{
				indx1 = indxWhere;
			}
			else
			{
				indx1 = indxBracket;
			}
		}

		String tableWithAlias = qryStr.substring(indxFrom + 5, indx1);
		int indxSpace = tableWithAlias.indexOf(" ");

		if (indxSpace != -1)
		{
			// TSADREP A
			tableName = tableWithAlias.substring(0, indxSpace);
			aliasName = tableWithAlias.substring(indxSpace + 1);
		}

		// left outer join TSACREP B on ( B.ACADCD = A.ADADCD)
		List<String> tables = new ArrayList<String>();
		List<String> aliases = new ArrayList<String>();
		int indxJoin = qryStr.indexOf("join");

		while (indxJoin != -1)
		{
			indx1 = qryStr.indexOf(" on", indxJoin);
			tableWithAlias = qryStr.substring(indxJoin + 5, indx1);
			indxSpace = tableWithAlias.indexOf(" ");

			if (indxSpace != -1)
			{
				// TSACREP B
				tables.add(tableWithAlias.substring(0, indxSpace));
				aliases.add(tableWithAlias.substring(indxSpace + 1));
			}
			else
			{
				tables.add(tableWithAlias);
			}

			indxJoin = qryStr.indexOf("join", indxJoin + 5);
		}

		int ch = 'B';
		int indxAs = qryStr.indexOf(" as " + (char)ch);

		while (indxAs != -1)
		{
			int indx0 = qryStr.lastIndexOf(" ", indxAs - 1);
			indx1 = qryStr.indexOf(" where", indxAs);
			tableWithAlias = qryStr.substring(indx0 + 1, indx1);
			indxSpace = tableWithAlias.indexOf(" ");

			// TSADREP as B
			tables.add(tableWithAlias.substring(0, indxSpace));
			aliases.add(tableWithAlias.substring(indxSpace + 4));

			ch += 1;
			indxAs = qryStr.indexOf(" as " + (char)ch, indxAs + 6);
		}

		// Replace alias by table
		qryStr = qryStr.replace(aliasName + ".", tableName + ".");
		qryStr = qryStr.replace(tableName + " " + aliasName, tableName);

		for (int i = 0; i < aliases.size(); i++)
		{
			String alias = aliases.get(i);
			String table = tables.get(i);

			qryStr = qryStr.replace(alias + ".", table + ".");
			qryStr = qryStr.replace(table + " " + alias, table);
			qryStr = qryStr.replace(table + " as " + alias, table);
		}

		int indxOrderBy = qryStr.indexOf("order by");
		int indxWhere = qryStr.indexOf(tableName + " where");
		String join = "";

		if (indxWhere != -1)
		{
			indxWhere += tableName.length();
		}
		else
		{
			indxWhere = qryStr.indexOf(") where");

			if (indxWhere != -1)
			{
				indxWhere += 1;

				int indxTable = qryStr.lastIndexOf(tableName + " ", indxWhere);
				join =
					qryStr.substring(indxTable + tableName.length(), indxWhere);
			}
		}

		int indxBetween = qryStr.indexOf("between ");

		String limit = qryStr.substring(qryStr.lastIndexOf(" ") + 1);
		String offsetStr =
			qryStr.substring(indxBetween + 8,
				qryStr.indexOf(" ", indxBetween + 8));
		long offset = toLong(offsetStr) - 1;
		String orderBy =
			qryStr.substring(indxOrderBy, qryStr.indexOf(")", indxOrderBy + 9));
		int indxSelect = qryStr.indexOf(", (select");

		// SELECT *,@row:=@row+1 as rownum FROM TSADREP<JOIN>,
		// (SELECT @row:=0) as r
		StringBuilder qry =
			new StringBuilder("select *,@row:=@row+1 as rownum FROM " +
				tableName + join + ", (select @row:=" + offset + ") as r");

		if (indxWhere != -1)
		{
			qry.append(qryStr.substring(indxWhere,
					qryStr.indexOf(") as", indxWhere + 6)));
		}

		qry.append(" " + orderBy);

		if (indxSelect != -1)
		{
			indxFrom = qryStr.lastIndexOf("from");
			String subQry = qryStr.substring(indxSelect, indxFrom - 1);
			qry.append(" " + subQry);
		}

		qry.append(" limit " + limit + " offset " + offset);

		return qry.toString();
	}

	private void handleSQLException(SQLException e)
	{
		sqlca.sqlcode = e.getErrorCode();
		setStr(sqlca.sqlstate, e.getSQLState());

		//setStr(sqlca.sqlwrn, e.getSQLState());

		/*if (e instanceof DB2Diagnosable)
		 {
		 	// DB2 specific handling
		    DB2Diagnosable diagnosable = (DB2Diagnosable)e;
		    DB2Sqlca db2Sqlca = diagnosable.getSqlca();
		    sqlca.sqlcode = db2Sqlca.getSqlCode();
		    sqlca.sqlwarn = db2Sqlca.getSqlWarn();
		    sqlca.setSqlerrd(db2Sqlca.getSqlErrd());
		    setStr(sqlca.sqlerrmc, db2Sqlca.getSqlErrmc());
		    setStr(sqlca.sqlerrp, db2Sqlca.getSqlErrp());
		    setStr(sqlca.sqlwrn, String.valueOf(db2Sqlca.getSqlWarn()));
		}*/
	}

	public boolean next()
	{
		try
		{
			if (rs.next())
			{
				setCurrRecord();

				return true;
			}
		}
		catch (SQLException e)
		{
			handleSQLException(e);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if (sqlca.getSqlcod() == 0)
		{
			sqlca.sqlcode = -1;
		}

		return false;
	}

	public void open()
	{
		try
		{
			resetSQLCA();

			if (pstmt.execute())
			{
				rs = pstmt.getResultSet();
				rsmd = rs.getMetaData();
				colsCnt = rsmd.getColumnCount();
				result = new Object[colsCnt];
			}
		}
		catch (SQLException e)
		{
			handleSQLException(e);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public boolean previous()
	{
		try
		{
			if (rs.previous())
			{
				setCurrRecord();

				return true;
			}
		}
		catch (SQLException e)
		{
			handleSQLException(e);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return false;
	}

	private void resetSQLCA()
	{
		sqlca.sqlcode = 0;
		sqlca.sqlstate.setLength(0);
	}

	private void setCurrRecord()
	{
		for (int i = 0; i < colsCnt; i++)
		{
			try
			{
				result[i] = rs.getObject(i + 1);
			}
			catch (SQLException e)
			{
				handleSQLException(e);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}
	}
}