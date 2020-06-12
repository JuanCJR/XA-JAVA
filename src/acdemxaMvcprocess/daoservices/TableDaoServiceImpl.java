package acdemxaMvcprocess.daoservices;

import acdemxaMvcprocess.dao.TableDAO;

import java.io.Serializable;

import java.util.List;

import com.databorough.utils.ErrorObject;
import com.databorough.utils.FilterData;
import com.databorough.utils.PaginationList;
import static com.databorough.utils.ReflectionUtils.initialize;
import com.databorough.utils.RowNumberSpecification;
import com.databorough.utils.SortData;
import com.databorough.utils.Utils;

import org.springframework.transaction.annotation.Transactional;

/**
 * Offers CRUD operations for a generic type.
 *
 * @param <ET> the type of the domain object for which this instance is to be
 *        used
 * @param <EtId> the type of the id of the domain object for which this instance
 *        is to be used
 * @author TESTER
 * @since (2011-06-06.18:35:51)
 */
@Transactional
public class TableDaoServiceImpl<ET, EtId extends Serializable>
	implements TableDaoService<ET, EtId> {
	//Method idGetter = null;
	TableDAO<ET, EtId> dao;

	public TableDaoServiceImpl(Class<ET> entityCls, Class<EtId> entityIdCls,
		TableDAO<ET, EtId> dao) {
		this.dao = dao;//new TableDAOImpl(entityCls, entityId);
		//Class<ET> entityCls = dao.getPersistentClass();
		//idGetter = getIdGetter(entityCls);
	}

	public Integer addRow(ET originalRowImage, ErrorObject errorObject) {
		try {
			/**
			 * Check the integrity of the Add operation to make sure that the
			 * record does not already exist.
			 */
			/*ET dbEntity = null;

			try {
				dbEntity = readById((EtId) idGetter.invoke(originalRowImage));
			}
			catch(Exception e) {
				logStackTrace();
			}

			if (dbEntity != null) {
				throw new Exception("The record already exists");
			}*/
			ET localEntity = dao.save(originalRowImage);

			if (localEntity != null) {
				initialize(localEntity, originalRowImage, "");
			}

			return 1;
		}
		catch (Exception e) {
			if (errorObject == null) {
				errorObject = new ErrorObject();
			}

			errorObject.setErrorText(e.getMessage());
		}

		return -1;
	}

	public Integer deleteRow(ET originalRowImage, FilterData selectionCriteria,
		ErrorObject errorObject) {
		try {
			//ensureConsistency(originalRowImage);
			if (originalRowImage != null) {
				boolean flag = dao.deleteRow(originalRowImage);

				if (!flag) {
					throw new Exception("Record couldn't be deleted!");
				}
			}

			return 1;
		}
		catch (Exception e) {
			if (errorObject == null) {
				errorObject = new ErrorObject();
			}

			errorObject.setErrorText(e.getMessage());
		}

		return -1;
	}

	/*private void ensureConsistency(ET entity) {
		ET dbEntity = null;

		try {
			dbEntity = readById((EtId)idGetter.invoke(entity));
		}
		catch (Exception e) {
			logStackTrace();
		}

		if (dbEntity == null) {
			throw new RuntimeException("The record does not exist");
		}

		if (!equals(entity, dbEntity)) {
			throw new RuntimeException("The record has already been updated " +
				"by someone");
		}
	}*/

	/**
	 * Executes the SQL.
	 */
	public List<ET> execSQL(String sql) {
		return dao.execSQL(sql);
	}

	/**
	 * Executes the SQL containing aggregate function.
	 */
	public Number execSQLFun(String sql) {
		return dao.execSQLFun(sql);
	}

	/**
	 * Calls the DAO method for fetching a batch of data using customized
	 * criteria parameters.
	 */
	@Transactional(readOnly=true)
	public List<ET> fetch(int startRow, int endRow,
		List<FilterData> selectionCriteria, List<SortData> sortList,
		boolean previous, Object pgmMbr) {
		return dao.fetch(startRow, endRow, selectionCriteria, sortList,
			previous, pgmMbr);
	}

	public Integer getTableRowCount() {
		return dao.getTableRowCount();
	}

	public Integer readByCriteria(List<FilterData> filterDataList,
		ET returnedRow, ErrorObject errorObject) {
		try {
			ET localEntity = dao.findFirst(filterDataList);

			if (localEntity != null) {
				initialize(localEntity, returnedRow, "");

				return 1;
			}
		}
		catch (Exception e) {
			if (errorObject == null) {
				errorObject = new ErrorObject();
			}

			errorObject.setErrorText(e.getMessage());
		}

		return -1;
	}

	public ET readById(EtId id) {
		return dao.find(id);
	}

	/**
	 * Uses a mixed clause in setting the selection criteria.
	 */
	public void readRows(RowNumberSpecification rowNumberSpecifications,
		List<FilterData> selectionCriteria, List<SortData> orderByCriteria,
		List<ET> returnedRows, ErrorObject errorObject, String filter,
		Object pgmMbr) {
		PaginationList<ET> list =
			dao.findAllByTable(rowNumberSpecifications.getPositionToRelativePosition(),
				rowNumberSpecifications.getNumberOfRowsRequested(),
				selectionCriteria, orderByCriteria, filter, pgmMbr);
		rowNumberSpecifications.setNumberOfRowsReturned(Utils.length(list));

		if (list != null) {
			rowNumberSpecifications.setTotalCount(list.getTotalCount());
		}

		if (returnedRows != null) {
			returnedRows.clear();
		}

		if (list != null) {
			returnedRows.addAll(list);
		}
	}

	public Integer updateRow(ET originalRowImage, ET updateRowImage,
		FilterData selectionCriteria, ErrorObject errorObject, String recfmt) {
		try {
			/**
			 * Check the integrity of the Update operation to make sure that no
			 * other user modified the record concurrently.
			 */
			//ensureConsistency(originalRowImage);
			ET updatedRowImage = dao.save(updateRowImage);
			initialize(updatedRowImage, updateRowImage, recfmt);

			return 1;
		}
		catch (Exception e) {
			if (errorObject == null) {
				errorObject = new ErrorObject();
			}

			errorObject.setErrorText(e.getMessage());
		}

		return -1;
	}
}
