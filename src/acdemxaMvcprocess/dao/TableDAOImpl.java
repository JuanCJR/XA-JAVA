package acdemxaMvcprocess.dao;

import java.io.Serializable;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import acdemxaMvcprocess.dao.CriteriaBuilder;

import com.databorough.utils.FilterData;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import com.databorough.utils.PaginationList;
import static com.databorough.utils.SQLUtils.getOperator;
import com.databorough.utils.SortData;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.Sort;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

/**
 * Implementation of TableDAO using Hibernate.
 * <p>
 * Relies on Spring framework to inject the current <code>EntityManager</code>
 * into our DAOs via the @PersistenceContext annotation. Also, having Spring
 * automatically set our <code>SearchProcessor</code> via an @Autowired
 * annotation.
 *
 * @param <ET> the type of the domain object for which this instance is to be
 *        used
 * @param <EtId> the type of the id of the domain object for which this instance
 *        is to be used
 * @author TESTER
 * @since (2011-06-06.18:35:51)
 */
@Repository
public class TableDAOImpl<ET, EtId extends Serializable>
	extends GenericDAOImpl<ET, EtId> implements TableDAO<ET, EtId> {
	public TableDAOImpl(Class<ET> entityCls, Class<EtId> entityIdCls) {
		this.persistentClass = entityCls;
	}

	public boolean deleteRow(ET entity) {
		Serializable id = getMetadataUtil().getId(entity);

		try {
			em().remove(em().getReference(persistentClass, id));

			return true;
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public List<ET> execSQL(String sql) {
		Query query = em().createNativeQuery(sql, persistentClass);

		return query.getResultList();
	}

	public Number execSQLFun(String sql) {
		Query query = em().createNativeQuery(sql);
		Object obj = query.getSingleResult();

		if (obj == null) {
			return 0;
		}

		return (Number)obj;
	}

	public PaginationList<ET> fetch(int startRow, int size,
		List<FilterData> filterList, List<SortData> sortList,
		boolean withFilters, Object pgmMbr) {
		Search search = new Search();
		CriteriaBuilder criteriaBuilder =
			new CriteriaBuilder(filterList, sortList, false, pgmMbr);
		criteriaBuilder.setTableName(this.persistentClass.getSimpleName());

		if ((filterList != null) && (filterList.size() > 0)) {
			search.addFilter(criteriaBuilder.getCriteria());
		}

		if (criteriaBuilder.getCustomFilter().size() != 0) {
			Filter customFilter = criteriaBuilder.getCustomFilter().get(0);
			search.addFilterCustom(customFilter.getProperty() + " " +
				getOperator(customFilter.getOperator()) + " " +
				customFilter.getValue());
		}

		if ((sortList != null) && (sortList.size() > 0)) {
			search.addSorts(getSorts(sortList));
		}

		search.setFirstResult(startRow);
		search.setMaxResults(size);

		List<ET> searchList = search(search);
		PaginationList<ET> paginationList =
			new PaginationList<ET>(startRow, size);
		paginationList.addAll(searchList);

		return paginationList;
	}

	public PaginationList<ET> findAllByTable(int startRow, int size,
		List<FilterData> filterList, List<SortData> sortList, String filter,
		Object pgmMbr) {
		if (filter.equalsIgnoreCase("OR")) {
			Search search = new Search();
			CriteriaBuilder criteriaBuilder =
				new CriteriaBuilder(filterList, sortList, false, pgmMbr);

			if ((filterList != null) && (filterList.size() > 0)) {
				search.addFilter(criteriaBuilder.getORFilter(filterList));
			}

			if ((sortList != null) && (sortList.size() > 0)) {
				search.addSorts(getSorts(sortList));
			}

			search.setFirstResult(startRow);
			search.setMaxResults(size);

			SearchResult<ET> searchresult = searchAndCount(search);
			PaginationList<ET> paginationList =
				new PaginationList<ET>(startRow, size);
			paginationList.addAll(searchresult.getResult());
			paginationList.setTotalCount(searchresult.getTotalCount());

			return paginationList;
		}
		else {
			return fetch(startRow, size, filterList, sortList, false, pgmMbr);
		}
	}

	public ET findFirst(List<FilterData> filterList) {
		Search search = new Search(persistentClass);

		if ((filterList != null) && (filterList.size() > 0)) {
			search.addFilter(getFilter(filterList));
		}

		List<ET> list = search(search);

		return ((list != null) && (list.size() > 0)) ? list.get(0) : null;
	}

	private Filter getFilter(List<FilterData> filterList) {
		Filter filter = new Filter("AND", null, Filter.OP_AND);

		for (FilterData f : filterList) {
			filter.add(new Filter(f.getProperty(), f.getValue(),
				f.getOperator()));
		}

		return filter;
	}

	private Sort[] getSorts(List<SortData> sortList) {
		int nSort = (sortList != null) ? sortList.size() : 0;

		if (nSort == 0) {
			return null;
		}

		Sort sorts[] = new Sort[nSort];

		for (int i = 0; i < nSort; i++) {
			SortData sortData = sortList.get(i);
			sorts[i] = new Sort(sortData.getProperty(), sortData.isDesc(),
					sortData.isIgnoreCase());
		}

		return sorts;
	}

	public Integer getTableRowCount() {
		return _count(persistentClass);
	}

	@Override
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	@Autowired
	public void setSearchProcessor(JPASearchProcessor searchProcessor) {
		super.setSearchProcessor(searchProcessor);
	}
}
