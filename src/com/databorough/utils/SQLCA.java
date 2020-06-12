package com.databorough.utils;

import javax.persistence.Column;
import static com.databorough.utils.NumberFormatter.toInt;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.StringUtils.toSbl;

/**
 * An SQLCA is a collection of variables that is updated at the end of the
 * execution of every SQL statement.
 *
 * @author Vishwajeet Pandey
 * @since (2011-10-31.14:09:38)
 */
public class SQLCA {
	/**
	 * An "eye catcher" for storage dumps containing 'SQLCA'. The sixth byte is
	 * 'L' if line number information is returned from parsing an SQL procedure
	 * body.
	 */
	@Column(name="SQLCAID", length=8)
	public StringBuilder sqlcaid = new StringBuilder();

	@Column(name="SQLAID", length=8)
	public StringBuilder getSqlaid() {
		return toSbl(subString(sqlcaid, 1, 8));
	}

	/**
	 * Contains the length of the SQLCA, 136.
	 */
	@Column(name="SQLCABC", precision=10)
	public Integer sqlcabc = 0;

	@Column(name="SQLABC", precision=9)
	public Integer getSqlabc() {
		return toInt(subString(sqlcabc, 1, 4));
	}

	/**
	 * Contains the SQL return code.
	 *
	 * 0: Successful execution (although one or more SQLWARN indicators may be
	 *        set)
	 * positive: Successful execution, but with a warning condition
	 * negative: Error condition
	 */
	@Column(name="SQLCODE", precision=10)
	public Integer sqlcode = 0;

	@Column(name="SQLCOD", precision=9)
	public Integer getSqlcod() {
		return toInt(subString(sqlcode, 1, 4));
	}

	/**
	 * Length indicator for sqlerrmc, in the range 0 through 70. 0 means that
	 * the value of sqlerrmc is not relevant.
	 */
	@Column(name="SQLERRML", precision=5)
	public Integer sqlerrml = 0;

	@Column(name="SQLERL", precision=4)
	public Short getSqlerl() {
		return Short.valueOf(subString(sqlerrml, 1, 2));
	}

	/**
	 * Contains one or more tokens, separated by X'FF', which are substituted
	 * for variables in the descriptions of error conditions.
	 *
	 * This field is also used when a successful connection is completed.
	 *
	 * When a NOT ATOMIC compound SQL statement is issued, it may contain
	 * information on up to seven errors.
	 *
	 * The last token might be followed by X'FF'. The sqlerrml value will
	 * include any trailing X'FF'. 
	 */
	@Column(name="SQLERRMC", length=70)
	public StringBuilder sqlerrmc = new StringBuilder();

	@Column(name="SQLERM", length=70)
	public StringBuilder getSqlerm() {
		return toSbl(subString(sqlerrmc, 1, 70));
	}

	/**
	 * Begins with a three-letter identifier indicating the product, followed by
	 * five digits indicating the version, release, and modification level of
	 * the product. For example, SQL08010 means DB2 Universal Database Version 8
	 * Release 1 Modification level 0.
	 *
	 * If SQLCODE indicates an error condition, this field identifies the module
	 * that returned the error.
	 *
	 * This field is also used when a successful connection is completed.
	 */
	@Column(name="SQLERRP", length=8)
	public StringBuilder sqlerrp = new StringBuilder();

	@Column(name="SQLERP", length=8)
	public StringBuilder getSqlerp() {
		return toSbl(subString(sqlerrp, 1, 8));
	}

	@Column(name="SQLERR", length=24)
	public StringBuilder sqlerr = new StringBuilder();

	/**
	 * If connection is invoked and successful, contains the maximum expected
	 * difference in length of mixed character data (CHAR data types) when
	 * converted to the database code page from the application code page. A
	 * value of 0 or 1 indicates no expansion; a value greater than 1 indicates
	 * a possible expansion in length; a negative value indicates a possible
	 * contraction.
	 *
	 * On successful return from an SQL procedure, contains the return status
	 * value from the SQL procedure.
	 */
	@Column(name="SQLER1", precision=9)
	public Integer getSqler1() {
		return toInt(subString(sqlerr, 1, 4));
	}

	/**
	 * If connection is invoked and successful, contains the maximum expected
	 * difference in length of mixed character data (CHAR data types) when
	 * converted to the application code page from the database code page. A
	 * value of 0 or 1 indicates no expansion; a value greater than 1 indicates
	 * a possible expansion in length; a negative value indicates a possible
	 * contraction. If the SQLCA results from a NOT ATOMIC compound SQL
	 * statement that encountered one or more errors, the value is set to the
	 * number of statements that failed.
	 */
	@Column(name="SQLER2", precision=9)
	public Integer getSqler2() {
		return toInt(subString(sqlerr, 5, 4));
	}

	/**
	 * If PREPARE is invoked and successful, contains an estimate of the
	 * number of rows that will be returned. After INSERT, UPDATE, DELETE,
	 * or MERGE, contains the actual number of rows that qualified for the
	 * operation. If compound SQL is invoked, contains an accumulation of all
	 * sub-statement rows. If CONNECT is invoked, contains 1 if the database
	 * can be updated, or 2 if the database is read only.
	 *
	 * If the OPEN statement is invoked, and the cursor contains SQL data
	 * change statements, this field contains the sum of the number of rows
	 * that qualified for the embedded insert, update, delete, or merge
	 * operations.
	 *
	 * If CREATE PROCEDURE for an SQL procedure is invoked, and an 2 error is
	 * encountered when parsing the SQL procedure body, contains 2 the line
	 * number where the error was encountered. 2 The sixth byte of sqlcaid must
	 * be 'L' for this to be a valid line 2 number.
	 */
	@Column(name="SQLER3", precision=9)
	public Integer getSqler3() {
		return toInt(subString(sqlerr, 9, 4));
	}

	/**
	 * If PREPARE is invoked and successful , contains a relative cost estimate
	 * of the resources required to process the statement. If compound SQL is
	 * invoked, contains a count of the number of successful sub-statements.
	 *
	 * If CONNECT is invoked, contains 0 for a one-phase commit from a
	 * down-level client; 1 for a one-phase commit; 2 for a one-phase, read-only
	 * commit; and 3 for a two-phase commit.
	 */
	@Column(name="SQLER4", precision=9)
	public Integer getSqler4() {
		return toInt(subString(sqlerr, 13, 4));
	}

	/**
	 * Contains the total number of rows deleted, inserted, or updated as a
	 * result of both:
	 *
	 * The enforcement of constraints after a successful delete operation
	 * The processing of triggered SQL statements from activated triggers
	 *
	 * If compound SQL is invoked, contains an accumulation of the number of
	 * such rows for all sub-statements. In some cases, when an error is
	 * encountered, this field contains a negative value that is an internal
	 * error pointer. If CONNECT is invoked, contains an authentication type
	 * value of 0 for server authentication; 1 for client authentication; 2 for
	 * authentication using DB2 Connect; 4 for SERVER_ENCRYPT authentication; 5
	 * for authentication using DB2 Connect with encryption; 7 for KERBEROS
	 * authentication; 8 for KRB_SERVER_ENCRYPT authentication; 9 for GSSPLUGIN
	 * authentication; 10 for GSS_SERVER_ENCRYPT authentication; and 255 for
	 * unspecified authentication.
	 */
	@Column(name="SQLER5", precision=9)
	public Integer getSqler5() {
		return toInt(subString(sqlerr, 17, 4));
	}

	/**
	 * For a partitioned database, contains the partition number of the
	 * partition that encountered the error or warning. If no errors or warnings
	 * were encountered, this field contains the partition number of the
	 * coordinator node. The number in this field is the same as that specified
	 * for the partition in the db2nodes.cfg file.
	 */
	@Column(name="SQLER6", precision=9)
	public Integer getSqler6() {
		return toInt(subString(sqlerr, 21, 4));
	}

	@Column(name="SQLERRD", precision=10)
	public Integer[] getSqlerrd() {
		Integer sqlerrd[] = new Integer[6];

		for (int i = 0; i < 6; i++) {
			sqlerrd[i] = toInt(subString(sqlerr, (i * 4) + 1, 4));
		}

		return sqlerrd;
	}

	@Column(name="SQLWRN", length=11)
	public StringBuilder sqlwrn = new StringBuilder();

	/**
	 * Blank if all other indicators are blank; contains W if at least one other
	 * indicator is not blank.
	 */
	@Column(name="SQLWN0", length=1)
	public StringBuilder getSqlwn0() {
		return toSbl(subString(sqlwrn, 1, 1));
	}

	/**
	 * Contains W if the value of a string column was truncated when assigned to
	 * a host variable. Contains N if the null terminator was truncated.
	 * Contains A if the CONNECT or ATTACH is successful, and the authorization
	 * name for the connection is longer than 8 bytes. Contains P if the PREPARE
	 * statement relative cost estimate stored in sqlerrd(4) exceeded the value
	 * that could be stored in an INTEGER or was less than 1, and either the
	 * CURRENT EXPLAIN MODE or the CURRENT EXPLAIN SNAPSHOT special register is
	 * set to a value other than NO.
	 */
	@Column(name="SQLWN1", length=1)
	public StringBuilder getSqlwn1() {
		return toSbl(subString(sqlwrn, 2, 1));
	}

	/**
	 * Contains W if null values were eliminated from the argument of a
	 * function.
	 */
	@Column(name="SQLWN2", length=1)
	public StringBuilder getSqlwn2() {
		return toSbl(subString(sqlwrn, 3, 1));
	}

	/**
	 * Contains W if the number of columns is not equal to the number of host
	 * variables. Contains Z if the number of result set locators specified on
	 * the ASSOCIATE LOCATORS statement is less than the number of result sets
	 * returned by a procedure.
	 */
	@Column(name="SQLWN3", length=1)
	public StringBuilder getSqlwn3() {
		return toSbl(subString(sqlwrn, 4, 1));
	}

	/**
	 * Contains W if a prepared UPDATE or DELETE statement does not include a
	 * WHERE clause.
	 */
	@Column(name="SQLWN4", length=1)
	public StringBuilder getSqlwn4() {
		return toSbl(subString(sqlwrn, 5, 1));
	}

	/**
	 * Reserved for future use.
	 */
	@Column(name="SQLWN5", length=1)
	public StringBuilder getSqlwn5() {
		return toSbl(subString(sqlwrn, 6, 1));
	}

	/**
	 * Contains W if the result of a date calculation was adjusted to avoid an
	 * impossible date.
	 */
	@Column(name="SQLWN6", length=1)
	public StringBuilder getSqlwn6() {
		return toSbl(subString(sqlwrn, 7, 1));
	}

	/**
	 * Reserved for future use.
	 *
	 * If CONNECT is invoked and successful, contains 'E' if the DYN_QUERY_MGMT
	 * database configuration parameter is enabled.
	 */
	@Column(name="SQLWN7", length=1)
	public StringBuilder getSqlwn7() {
		return toSbl(subString(sqlwrn, 8, 1));
	}

	/**
	 * Contains W if a character that could not be converted was replaced with a
	 * substitution character.
	 */
	@Column(name="SQLWN8", length=1)
	public StringBuilder getSqlwn8() {
		return toSbl(subString(sqlwrn, 9, 1));
	}

	/**
	 * Contains W if arithmetic expressions with errors were ignored during
	 * column function processing.
	 */
	@Column(name="SQLWN9", length=1)
	public StringBuilder getSqlwn9() {
		return toSbl(subString(sqlwrn, 10, 1));
	}

	/**
	 * Contains W if there was a conversion error when converting a character
	 * data value in one of the fields in the SQLCA.
	 */
	@Column(name="SQLWNA", length=1)
	public StringBuilder getSqlwna() {
		return toSbl(subString(sqlwrn, 11, 1));
	}

	/**
	 * A set of warning indicators, each containing a blank or W. If compound
	 * SQL is invoked, contains an accumulation of the warning indicators set
	 * for all sub-statements.
	 */
	@Column(name="SQLWARN", length=1)
	public StringBuilder[] getSqlwarn() {
		StringBuilder sqlwarn[] = new StringBuilder[11];

		for (int i = 0; i < 11; i++) {
			sqlwarn[i] = toSbl(subString(sqlwrn, (i * 1) + 1, 1));
		}

		return sqlwarn;
	}

	/**
	 * A return code that indicates the outcome of the most recently executed
	 * SQL statement.
	 */
	@Column(name="SQLSTATE", length=5)
	public StringBuilder sqlstate = new StringBuilder();

	@Column(name="SQLSTT", length=5)
	public StringBuilder getSqlstt() {
		return toSbl(subString(sqlstate, 1, 5));
	}
}