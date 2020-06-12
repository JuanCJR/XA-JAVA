package acdemxaMvcprocess.dao;

import java.io.Serializable;

import java.util.List;

import com.databorough.utils.FilterData;
import com.databorough.utils.PaginationList;
import com.databorough.utils.SortData;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

/**
 * DAO for individual model classes.
 *
 * @param <ET> the type of the domain object for which this instance is to be
 *        used
 * @param <EtId> the type of the id of the domain object for which this instance
 *        is to be used
 * @author TESTER
 * @since (2011-06-06.18:35:51)
 */
public interface TableDAO<ET, EtId extends Serializable>
	extends GenericDAO<ET, EtId> {
	boolean deleteRow(ET entity);

	List<ET> execSQL(String sql);

	Number execSQLFun(String sql);

	List<ET> fetch(int startRow, int endRow,
		List<FilterData> selectionCriteria, List<SortData> sortList,
		boolean withFilters, Object pgmMbr);

	PaginationList<ET> findAllByTable(int startRow, int size,
		List<FilterData> filterList, List<SortData> sortList, String filter,
		Object pgmMbr);

	ET findFirst(List<FilterData> filterList);

	Integer getTableRowCount();
}
