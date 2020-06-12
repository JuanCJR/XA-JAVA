package acdemxaMvcprocess.dao;

import java.sql.Date;
import java.sql.Time;

import java.util.ArrayList;
import java.util.List;

import com.databorough.utils.DateTimeConverter;
import com.databorough.utils.FilterData;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import com.databorough.utils.SortData;
import com.databorough.utils.Utils;

import com.googlecode.genericdao.search.Filter;

public class CriteriaBuilder {
	List<Filter> customFilter = new ArrayList<Filter>();
	List<FilterData> selectionCriteria;
	List<SortData> sortList;
	Object pgmMbr;
	String tableName = "";
	boolean withFilters;

	public CriteriaBuilder(List<FilterData> selectionCriteria,
		List<SortData> sortList, boolean withFilters, Object pgmMbr) {
		this.selectionCriteria = selectionCriteria;
		this.sortList = sortList;
		this.withFilters = withFilters;
		this.pgmMbr = pgmMbr;
	}

	private Object getConvValue(Object value, String dataType) {
		if (value instanceof Date || value instanceof Time) {
			return value;
		}

		if ("Date".equalsIgnoreCase(dataType)) {
			String dateFormat = "";

			try {
				dateFormat = (String)pgmMbr.getClass().getField("dateFormat")
										   .get(value);
			}
			catch (Exception e) {
				logStackTrace(e);
			}

			if ("*CYMD".equalsIgnoreCase(dateFormat)) {
				dateFormat = "*YYMD";
			}

			String format =
				DateTimeConverter.rpg2JavaDateFormat.get(dateFormat);
			java.util.Date date =
				DateTimeConverter.getDate(value.toString(), format);
			value = new Date(date.getTime());
		}
		else if ("Time".equalsIgnoreCase(dataType)) {
			value = Time.valueOf(value.toString());
		}

		return value;
	}

	public Filter getCriteria() {
		List<FilterData> rstFldLst = new ArrayList<FilterData>();
		List<FilterData> keyFldLst = new ArrayList<FilterData>();
		List<FilterData> fltrFldLst = new ArrayList<FilterData>();

		for (FilterData fltr : selectionCriteria) {
			if (fltr.getFldType() == FilterData.RSTFLD) {
				rstFldLst.add(fltr);
			}
			else if (fltr.getFldType() == FilterData.KEYFLD) {
				keyFldLst.add(fltr);
			}
			else if (fltr.getFldType() == FilterData.FLTRFLD) {
				fltrFldLst.add(fltr);
			}
		}

		Filter filter = new Filter("AND", null, Filter.OP_AND);

		if (rstFldLst.size() != 0) {
			filter.add(getRestrictorCriteria(rstFldLst));
		}

		if (keyFldLst.size() != 0) {
			filter.add(getPositionerCriteria(keyFldLst));
		}

		if (fltrFldLst.size() != 0) {
			filter.add(getPositionerCriteria(fltrFldLst));
		}

		return filter;
	}

	public List<Filter> getCustomFilter() {
		return customFilter;
	}

	/**
	 * Presents the filters in the following pattern
	 * (F1>=K1) OR (F1 = K1 AND F2 >= K2) OR ...
	 */
	public Filter getORFilter(List<FilterData> filterList) {
		Filter filter = new Filter("OR", null, Filter.OP_OR);
		FilterData f = null;
		int i = 0;
		int j = 0;

		while (i < filterList.size()) {
			j = 0;

			Filter innerFilter = new Filter("AND", null, Filter.OP_AND);

			while ((i > 0) && (j < i)) {
				f = filterList.get(j);
				innerFilter.add(new Filter(f.getProperty(), f.getValue(),
						FilterData.OP_EQUAL));
				j++;
			}

			f = filterList.get(j);
			innerFilter.add(new Filter(f.getProperty(), f.getValue(),
					f.getOperator()));

			if (innerFilter.getValue() instanceof List) {
				filter.add(innerFilter);
			}

			i++;
		}

		if (filter.getValue() instanceof List) {
			return filter;
		}
		else {
			return null;
		}
	}

	/**
	 * Gets positioner criteria.
	 *
	 * @param fltrLst
	 * @param sortList
	 * @param withFilters
	 * @return positioner criteria
	 * @since (2012-11-22.15:24:35)
	 */
	private Filter getPositionerCriteria(List<FilterData> fltrLst) {
		Filter filter = new Filter("OR", null, Filter.OP_OR);

		int i = 0;
		int keyCount = Utils.length(fltrLst);
		boolean hival = false;
		boolean loval = false;

		while (i < keyCount) {
			List<FilterData> criteria = fltrLst.subList(0, i + 1);
			Filter filter1 = new Filter("AND", null, Filter.OP_AND);

			for (int j = 0; j < criteria.size(); j++) {
				if (j == keyCount) {
					break;
				}

				FilterData fltr = criteria.get(j);
				String property = fltr.getProperty();
				int oper = fltr.getOperator();
				// Check for values if data type is Date
				Object val =
					getConvValue(fltr.getValue(), fltr.getFldDataType());

				if ((j == 0) && (fltr.getFldType() == FilterData.FLTRFLD)) {
					char sortOrder = getSortOrder(property);

					if (sortOrder == 'A') {
						fltr.setOperator(Filter.OP_GREATER_OR_EQUAL);
					}
					else {
						fltr.setOperator(Filter.OP_LESS_OR_EQUAL);
					}
				}

				int op =
					(j < (criteria.size() - 1)) ? Filter.OP_EQUAL
												: fltr.getOperator();

				if (withFilters) {
					if (val instanceof String && (oper == Filter.OP_LIKE)) {
						// Case insensitive query
						property = "UCASE(" + property + ")";
						val = val.toString().toUpperCase();
					}
				}

				hival = String.valueOf(val)
							  .startsWith(String.valueOf(Character.MAX_VALUE));
				loval = String.valueOf(val)
							  .startsWith(String.valueOf(Character.MIN_VALUE));

				if (hival || loval) {
					if (hival) {
						val = "(Select MAX(" + property + ")" + " from " +
							tableName + ")";
					}
					else if (loval) {
						val = "(Select MIN(" + property + ")" + " from " +
							tableName + ")";
					}

					customFilter.add(new Filter(property, val, op));

					continue;
				}

				filter1.add(new Filter(property, val, op));
			}

			filter.add(filter1);
			i++;
		}

		return filter;
	}

	/**
	 * Gets restrictor criteria.
	 *
	 * @param rstFldLst
	 * @return restrictor criteria
	 * @since (2012-11-22.15:29:35)
	 */
	private Filter getRestrictorCriteria(List<FilterData> rstFldLst) {
		Filter filter = new Filter("AND", null, Filter.OP_AND);

		for (FilterData f : rstFldLst) {
			// Check for values if data type is Date
			Object val = getConvValue(f.getValue(), f.getFldDataType());
			filter.add(new Filter(f.getProperty(), val, f.getOperator()));
		}

		return filter;
	}

	private char getSortOrder(String fldName) {
		int nSort = (sortList != null) ? sortList.size() : 0;

		if (nSort == 0) {
			return 'A';
		}

		char keySortOrder = 'A';

		for (int i = 0; i < nSort; i++) {
			SortData sortData = sortList.get(i);

			if (sortList.get(i).getProperty().equalsIgnoreCase(fldName)) {
				keySortOrder = (sortData.isDesc() ? 'D' : 'A');

				break;
			}
		}

		return keySortOrder;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
