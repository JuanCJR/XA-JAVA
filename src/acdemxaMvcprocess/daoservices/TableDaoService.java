package acdemxaMvcprocess.daoservices;

import java.io.Serializable;

import java.util.List;

import com.databorough.utils.ErrorObject;
import com.databorough.utils.FilterData;
import com.databorough.utils.RowNumberSpecification;
import com.databorough.utils.SortData;

/**
 * DAO Service for individual model classes.
 *
 * @param <ET> the type of the domain object for which this instance is to be
 *        used
 * @param <EtId> the type of the id of the domain object for which this instance
 *        is to be used
 * @author TESTER
 * @since (2011-06-06.18:35:51)
 */
public interface TableDaoService<ET, EtId extends Serializable> {
	/**
	 * Adds the data.
	 *
	 * @param newRow An object that holds the data to be inserted.
	 * @param errorObject Contains the errors details if any error occurs in
	 *        database access.
	 * @return <code>rrn</code> value of the record being inserted.
	 */
	Integer addRow(ET newRow, ErrorObject errorObject);

	/**
	 * Deletes the data.
	 *
	 * @param originalRow An object that holds the data to be deleted.
	 * @param selectionCriteria A selection criteria for RRN value.
	 * @param errorObject Contains the errors details if any error occurs in
	 *        database access.
	 * @return <code>rrn</code> value of the record being deleted.
	 * @since (2010-08-05.18:12:57)
	 */
	Integer deleteRow(ET originalRow, FilterData selectionCriteria,
		ErrorObject errorObject);

	/**
	 * Executes the SQL.
	 *
	 * @param sql SQL query to be executed.
	 * @return list of records.
	 * @since (2012-08-29.11:56:57)
	 */
	List<ET> execSQL(String sql);

	/**
	 * Executes the SQL containing aggregate function.
	 *
	 * @param sql SQL query to be executed.
	 * @return a single, summarizing value.
	 * @since (2014-05-22.14:33:07)
	 */
	Number execSQLFun(String sql);

	List<ET> fetch(int startRow, int endRow,
		List<FilterData> selectionCriteria, List<SortData> sortList,
		boolean previous, Object pgmMbr);

	/**
	 * Fetches the total record count from a table.
	 */
	Integer getTableRowCount();

	/**
	 * Reads the data.
	 *
	 * @param filterDataList A list of selection criteria.
	 * @param returnedRow An object that holds the fetched data.
	 * @param errorObject Contains the errors details if any error occurs in
	 *        database access.
	 * @return <code>rrn</code> value of the record being read.
	 */
	Integer readByCriteria(List<FilterData> filterDataList, ET returnedRow,
		ErrorObject errorObject);

	/**
	 * Reads data by Id.
	 *
	 * @param id
	 */
	ET readById(EtId id);

	/**
	 * Reads rows of data.
	 *
	 * @param rowNumberSpecifications
	 * @param selectionCriteria
	 * @param orderByCriteria
	 * @param returnedRows
	 * @param errorObject
	 * @param filter
	 * @param pgmMbr
	 */
	void readRows(RowNumberSpecification rowNumberSpecifications,
		List<FilterData> selectionCriteria, List<SortData> orderByCriteria,
		List<ET> returnedRows, ErrorObject errorObject, String filter,
		Object pgmMbr);

	/**
	 * Updates the data.
	 *
	 * @param originalRowImage An object that holds the most recently data read.
	 * @param updateRowImage An object that holds the data that is to be updated.
	 * @param selectionCriteria A selection criteria for RRN value.
	 * @param errorObject Contains the errors details if any error occurs in
	 *        database access.
	 * @param recfmt
	 * @return <code>rrn</code> value of the record being updated.
	 */
	Integer updateRow(ET originalRowImage, ET updateRowImage,
		FilterData selectionCriteria, ErrorObject errorObject, String recfmt);
}
