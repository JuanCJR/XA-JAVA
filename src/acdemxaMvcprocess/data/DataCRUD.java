package acdemxaMvcprocess.data;

import java.io.Serializable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import static acdemxaMvcprocess.daoservices.ServiceLocator.getFromApplicationContext;

import acdemxaMvcprocess.daoservices.TableDaoService;

import static com.databorough.utils.ArrayUtils.arrayUpperCase;
import static com.databorough.utils.ArrayUtils.fillArray;
import com.databorough.utils.BusinessException;
import static com.databorough.utils.DSUtils.assignObject;
import com.databorough.utils.ErrorObject;
import com.databorough.utils.FilterData;
import static com.databorough.utils.IConstants.IOACTIONS.*;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.ReflectionUtils.getColumnAnnoFldMap;
import static com.databorough.utils.ReflectionUtils.getFld2ColumnAnnoMap;
import static com.databorough.utils.ReflectionUtils.getKeyVals;
import static com.databorough.utils.ReflectionUtils.initialize;
import static com.databorough.utils.ReflectionUtils.isKeysArrayEqual;
import static com.databorough.utils.ReflectionUtils.setCompatibleField;
import static com.databorough.utils.ReflectionUtils.setKeyVals;
import static com.databorough.utils.ReflectionUtils.updKeyFlds;
import com.databorough.utils.RowNumberSpecification;
import com.databorough.utils.SortData;
import static com.databorough.utils.StringUtils.trim;
import com.databorough.utils.Utils;
import static com.databorough.utils.Utils.FORWARD;
import com.databorough.utils.Utils.LASTREAD;

/**
 * Provides CRUD (Create, Read, Update and Delete) operations on entities with a
 * provision for pagination in both directions.
 *
 * @author TESTER
 * @version (2011-06-06.18:35:51)
 */
public class DataCRUD<ET, EtId extends Serializable>
	extends AbstractDataCrud<ET, EtId> {
	/**
	 * Default constructor.
	 */
	public DataCRUD() {
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 */
	@SuppressWarnings("unchecked")
	public DataCRUD(Class<ET> entityCls) {
		this.entityCls = entityCls;
		this.service = (TableDaoService<ET, EtId>)getFromApplicationContext(
				entityCls.getSimpleName());

		getFld2ColumnAnnoMap(entityCls, entityFieldsAnnoMap, "");
		getColumnAnnoFldMap(entityCls, entityAnnoFieldsMap, false, false);

		for (Column col : entityFieldsAnnoMap.values()) {
			int width = col.length();

			if (width == 0) {
				width = ((col.precision() + col.scale()) > 0) ? 1 : 0;
			}

			if (width == 0) {
				width = 20;
			}

			fieldNames.put(col.name(), width);
		}
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 * @param pgmMbr Object of standard module
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr) {
		this(entityCls);
		this.pgmMbr = pgmMbr;
		collectReferencesInfo(pgmMbr);
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 * @param pgmMbr Object of standard module
	 * @param prefix String for handling file-field prefixes at runtime
	 * @param keyFlds String array of key fields
	 * @param keyFldsSortOrder character array of sort order of key fields
	 * @param keyFldsSlt character array for selecting key fields
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr, String prefix,
		String keyFlds[], char keyFldsSortOrder[], char keyFldsSlt[]) {
		this(entityCls);
		this.prefix = prefix;
		this.pgmMbr = pgmMbr;
		this.keyFlds = arrayUpperCase(keyFlds);
		this.keyFldsSortOrder = keyFldsSortOrder;
		this.keyFldsSlt = keyFldsSlt;
		collectReferencesInfo(pgmMbr);

		if (synon && (keyFlds != null)) {
			// Setting short names for keyFlds in case long names are specified
			// in key fields
			int numKeyFlds = keyFlds.length;

			for (int i = 0; i < numKeyFlds; i++) {
				Column shortNameAnno =
					entityFieldsAnnoMap.get(keyFlds[i].toUpperCase());

				if (shortNameAnno != null) {
					this.keyFlds[i] = shortNameAnno.name();
				}
			}
		}

		if ((keyFldsSortOrder == null) && (keyFlds != null)) {
			this.keyFldsSortOrder = new char[keyFlds.length];
			fillArray(this.keyFldsSortOrder, 'A');
		}

		if ((keyFldsSlt == null) && (keyFlds != null)) {
			this.keyFldsSlt = new char[keyFlds.length];
			fillArray(this.keyFldsSlt, 'N');
		}
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 * @param pgmMbr Object of standard module
	 * @param prefix String for handling file-field prefixes at runtime
	 * @param keyFlds String array of key fields
	 * @param keyFldsSortOrder character array of sort order of key fields
	 * @param keyFldsSlt character array for selecting key fields
	 * @param recfmtName String contains record format name
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr, String prefix,
		String keyFlds[], char keyFldsSortOrder[], char keyFldsSlt[],
		String recfmtName) {
		this(entityCls, pgmMbr, prefix, keyFlds, keyFldsSortOrder, keyFldsSlt);
		this.recfmtName = recfmtName;
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 * @param pgmMbr Object of standard module
	 * @param prefix String for handling file-field prefixes at runtime
	 * @param keyFlds String array of key fields
	 * @param keyFldsSortOrder character array of sort order of key fields
	 * @param keyFldsSlt character array for selecting key fields
	 * @param ispMapping
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr, String prefix,
		String keyFlds[], char keyFldsSortOrder[], char keyFldsSlt[],
		String ispMapping[][]) {
		this(entityCls, pgmMbr, prefix, keyFlds, keyFldsSortOrder, keyFldsSlt);

		if (ispMapping != null) {
			for (String isp[] : ispMapping) {
				ispMap.put(isp[1], isp[0]);
			}
		}
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 * @param pgmMbr Object of standard module
	 * @param keyFlds String array of key fields
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr, String keyFlds[]) {
		this(entityCls, pgmMbr, "", keyFlds, null, null);
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class 
	 * @param pgmMbr Object of standard module
	 * @param keyFlds String array of key fields
	 * @param keyFldsSortOrder character array of sort order of key fields
	 * @param keyFldsSlt character array for selecting key fields
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr, String keyFlds[],
		char keyFldsSortOrder[], char keyFldsSlt[]) {
		this(entityCls, pgmMbr, "", keyFlds, keyFldsSortOrder, keyFldsSlt);
	}

	/**
	 * Constructs <code>DataCRUD</code> object.
	 *
	 * @param entityCls entity class
	 * @param pgmMbr Object of standard module
	 * @param keyFlds String array of key fields
	 * @param keyFldsSortOrder character array of sort order of key fields
	 * @param keyFldsSlt character array for selecting key fields
	 * @param ispMapping
	 */
	public DataCRUD(Class<ET> entityCls, Object pgmMbr, String keyFlds[],
		char keyFldsSortOrder[], char keyFldsSlt[], String ispMapping[][]) {
		this(entityCls, pgmMbr, "", keyFlds, keyFldsSortOrder, keyFldsSlt,
			ispMapping);
	}

	/**
	 * Checks the existence of specific row/record based on the selection
	 * criteria provided.
	 *
	 * @param selectionCriteriaList list of selection criteria
	 * @return <code>rrn</code> value of the record if record is found otherwise
	 *        returns -1
	 * @since (2010-08-05.18:12:57)
	 */
	public Integer checkRow(List<FilterData> selectionCriteriaList) {
		ErrorObject errorObject = new ErrorObject();
		ET newRow = null;

		try {
			newRow = entityCls.getConstructor().newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		Integer rrn =
			service.readByCriteria(selectionCriteriaList, newRow, errorObject);

		return rrn;
	}

	/**
	 * Deletes a record from a file using the key arguments specified.
	 *
	 * @param values variable number of keys
	 * @return deleted row <code>rrn</code>
	 */
	public Integer delete(Object... values) {
		ET originalRow = null;

		try {
			originalRow = (lastReadEntity != null) ? lastReadEntity
												   : entityCls.getConstructor()
															  .newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		if (Utils.length(values) > 0) {
			setKeyVals(originalRow, keyFlds, values.length, values);
		}
		else {
			copyBeforeUpdate(originalRow);
		}

		ErrorObject errorObject = new ErrorObject();
		Integer rrn = service.deleteRow(originalRow, null, errorObject);

		if (Utils.length(trim(errorObject.getErrorText(), "")) > 0) {
			this.error = true;
			throw new BusinessException(null, errorObject.getErrorText());
		}
		else {
			this.error = false;
		}

		globalError = error;

		return rrn;
	}

	/**
	 * Deletes the passed state from the file.
	 *
	 * @param state
	 * @return deleted row <code>rrn</code>
	 */
	public Integer deleteState(Object state) {
		ET originalRow = null;

		try {
			originalRow = (lastReadEntity != null) ? lastReadEntity
												   : entityCls.getConstructor()
															  .newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		assignObject(originalRow, prefix, state, "");
		updKeyFlds(originalRow, recfmtName);

		ErrorObject errorObject = new ErrorObject();
		Integer rrn = service.deleteRow(originalRow, null, errorObject);

		if (Utils.length(trim(errorObject.getErrorText(), "")) > 0) {
			this.error = true;
			throw new BusinessException(null, errorObject.getErrorText());
		}
		else {
			this.error = false;
		}

		globalError = error;

		return rrn;
	}

	/**
	 * Executes the passed SQL.
	 *
	 * @param sql SQL query to be executed
	 * @since (2012-08-29.12:17:17)
	 */
	public void execSQL(String sql) {
		sql = sql.replace("WHERE AND", "WHERE");

		List<ET> tmpRecList = service.execSQL(sql);

		if (tmpRecList != null) {
			recList = tmpRecList;
			found = recList.size() > 0;
		}
		else {
			found = false;
			error = true;
			globalError = error;
		}

		if (found) {
			EOF = false;
			setLio(LIOEOF, EOF);
		}

		setLio(LIOFOUND, found);

		if (found) {
			reQuery = false;
			// canBeEOF will be true only if in READ's case. Not in extended
			// Start's case with eqKeys in which reade itself will determine if
			// it'll be EOF or not.
			positionedInPreviousMode = false;
		}

		if (found || false) {
			canBeEOF = recList.size() < BATCHSIZE;
		}

		currentBatchIndex = 0;
		lastRead = LASTREAD.unspecified;
		lastReadEntity = null;
	}

	/**
	 * Fetches the data from the start with a key that is greater than or equal
	 * to the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 */
	public void fetch(Object... posKeyValues) {
		rtvOpCode = RTVOPCODE.SETLL;
		setMemberVariables(0, BATCHSIZE, posKeyValues, null, FORWARD, !FORWARD);
		fetch();

		if (found) {
			equal = isKeysArrayEqual(posKeyValues,
					getKeyVals(recList.get(0), keyFlds, posKeyValues.length));
		}
		else {
			equal = false;
		}

		setLio(LIOEQUAL, equal);
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 */
	public void fetchAfter(Object... posKeyValues) {
		rtvOpCode = RTVOPCODE.SETGT;
		setMemberVariables(0, BATCHSIZE, posKeyValues, null, FORWARD, FORWARD);
		fetch();
		setLio(LIOEOF, EOF);
	}

	/**
	 * Returns specific row/record based on the selection criteria provided.
	 *
	 * @param selectionCriteriaList list of selection criteria
	 * @return specific row/record based on the selection criteria provided
	 * @since (2012-08-21.12:50:57)
	 */
	public Object getRow(List<FilterData> selectionCriteriaList) {
		ErrorObject errorObject = new ErrorObject();
		ET newRow = null;

		try {
			newRow = entityCls.getConstructor().newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		service.readByCriteria(selectionCriteriaList, newRow, errorObject);

		return newRow;
	}

	/**
	 * Retrieves the next record from the file.
	 *
	 * @return <tt>Entity</tt> class object
	 */
	public ET next() {
		ET nextEntity = getNext();
		copyAfterRead(nextEntity);

		return nextEntity;
	}

	/**
	 * Retrieves the next record from the file.
	 *
	 * @param copy whether to copy the values of entity object into the local
	 *        variables
	 * @return <tt>Entity</tt> class object
	 */
	public ET next(boolean copy) {
		ET nextEntity = getNext();

		if (copy) {
			copyAfterRead(nextEntity);
		}

		return nextEntity;
	}

	/**
	 * Retrieves the next record from the file if the key of the record matches
	 * the key argument.
	 *
	 * @param values variable number of keys
	 * @return <tt>Entity</tt> class object
	 */
	public ET next(Object... values) {
		ET nextEntity = getNext(values);
		copyAfterRead(nextEntity);

		return nextEntity;
	}

	/**
	 * Performs a read operation on a file.
	 *
	 * @param rowNumberSpecifications The number of starting row
	 * @param selectionCriteria Field names under criteria
	 * @param orderByCriteria Specification of the ordering
	 * @param positionToCriteria Positioning clause of the record pointer
	 * @param returnedRows DataStructure which contains the results of a query
	 * @param joinWhereClause Useful for joining of different files
	 */
	public void page(RowNumberSpecification rowNumberSpecifications,
		List<FilterData> selectionCriteria, List<SortData> orderByCriteria,
		List<FilterData> positionToCriteria, List<ET> returnedRows,
		String joinWhereClause) {
		service.readRows(rowNumberSpecifications, selectionCriteria,
			orderByCriteria, returnedRows, new ErrorObject(), joinWhereClause,
			pgmMbr);
	}

	/**
	 * Retrieves the previous record from a file.
	 *
	 * @return <tt>Entity</tt> class object
	 */
	public ET previous() {
		ET prevEntity = getPrevious();
		copyAfterRead(prevEntity);

		return prevEntity;
	}

	/**
	 * Retrieves the next prior record from a file if the key of the record
	 * matches the key argument specified.
	 *
	 * @param values variable number of keys
	 * @return <tt>Entity</tt> class object
	 */
	public ET previous(Object... values) {
		ET prevEntity = getPrevious(values);
		copyAfterRead(prevEntity);

		return prevEntity;
	}

	/**
	 * Populates the grid object and selection field.
	 *
	 * @param selFld Name of selection field
	 * @param grid array of grid objects
	 * @param eqKeys variable number of equal keys
	 */
	public void readIntoGrid(String selFld, Object grid[], Object... eqKeys) {
		if ((grid == null) || (grid.length == 0)) {
			return;
		}

		Field selRef = null;

		try {
			selRef = grid.getClass().getComponentType()
						 .getDeclaredField(selFld);
			selRef.setAccessible(true);
		}
		catch (NoSuchFieldException e) {
			try {
				selRef = grid.getClass().getComponentType().getSuperclass()
				 				.getDeclaredField(selFld);
				selRef.setAccessible(true);
			}
			catch (NoSuchFieldException nsfe) {
				logStackTrace(nsfe);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		for (Object g : grid) {
			readIntoObj(g, eqKeys);

			if (EOF) {
				break;
			}

			setCompatibleField(selRef, g, null, "X");
		}

		if (selRef != null) {
			selRef.setAccessible(false);
		}
	}

	/**
	 * If lastState is set to true, it doesn't move ahead the record pointer,
	 * simply loads up the object with the last read value.
	 *
	 * @param obj
	 * @param lastState
	 */
	public void readIntoObj(Object obj, boolean lastState) {
		if (lastState) {
			assignObject(obj, "", lastReadEntity, prefix);
		}
	}

	/**
	 * If lastState is set to true, it doesn't move ahead the record pointer,
	 * simply loads up the object with the last read value.
	 *
	 * @param obj
	 * @param lastState
	 * @param eqKeys variable number of equal keys
	 */
	private void readIntoObj(Object obj, boolean lastState, Object... eqKeys) {
		if (lastState) {
			assignObject(obj, "", lastReadEntity, prefix);
		}
		else if (!EOF) {
			ET entObj = getNext(eqKeys);
			assignObject(obj, "", entObj, prefix);
		}
	}

	/**
	 * Populates the object with the read value.
	 *
	 * @param obj object which has to be populated
	 * @param eqKeys variable number of equal keys
	 */
	private void readIntoObj(Object obj, Object... eqKeys) {
		readIntoObj(obj, false, eqKeys);
	}

	/**
	 * Retrieves a record from a file for the key arguments specified.
	 *
	 * @param values variable number of keys
	 * @return <tt>Entity</tt> class object
	 */
	public ET retrieve(Object... values) {
		// Retrieve operation
		rtvOpCode = RTVOPCODE.CHAIN;
		setMemberVariables(0, 3, values, null, FORWARD, !FORWARD);
		fetch();

		ET entity = null;

		if (found) {
			// 1st condition for Non-keyed file
			if ((Utils.length(keyFlds) == 0) ||
					isKeysArrayEqual(values,
						getKeyVals(recList.get(0), keyFlds, values.length))) {
				entity = next();
			}
			else {
				found = false;
				setLio(LIOFOUND, found);
			}
		}

		return entity;
	}

	/**
	 * Returns the sum.
	 *
	 * @param field field to be summed
	 * @param eqKeyValues array of equal keys
	 * @return sum
	 * @since (2014-05-22.16:10:17)
	 */
	public Number sum(String field, Object eqKeyValues[]) {
		Column shortNameAnno = entityFieldsAnnoMap.get(field.toUpperCase());

		if (shortNameAnno != null) {
			field = shortNameAnno.name();
		}

		Annotation annotation = entityCls.getAnnotation(Table.class);
		String file = ((Table)annotation).name();

		int numKeys = Math.min(keyFlds.length, eqKeyValues.length);

		String sql = "SELECT SUM(" + field + ") FROM " + file + " WHERE ";

		for (int i = 0; i < numKeys; i++) {
			String keyDataType = getKeyDataType(entityCls, keyFlds[i]);

			if (i != 0) {
				sql += " AND ";
			}

			sql += keyFlds[i] + " = ";

			if ("String".equals(keyDataType)) {
				sql += "'";
			}

			sql += eqKeyValues[i];

			if ("String".equals(keyDataType)) {
				sql += "'";
			}
		} // i

		Number sum = service.execSQLFun(sql);

		return sum;
	}

	/**
	 * Updates the last record retrieved from a file.
	 *
	 * @param values variable number of keys
	 * @return updated row <code>rrn</code>
	 */
	public Integer update(Object... values) {
		// Handled cases are:
		// 1. UPDATE with DS argument - when it updates from the DS's fields
		// 2. UPDATE without any argument - when it updates from work fields
		ET updateRowImage = null;
		ET originalRowImage = null;

		try {
			updateRowImage = (lastReadEntity != null) ? lastReadEntity
					   : entityCls.getConstructor().newInstance();
			originalRowImage = entityCls.getConstructor().newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		if (Utils.length(values) == 1) {
			// when DS argument is specified.
			assignObject(updateRowImage, prefix, values[0], "");
		}
		else if (Utils.length(values) > 1) {
		}
		else {
			// update from Program work fields
			copyBeforeUpdate(updateRowImage);
		}

		ErrorObject errorObject = new ErrorObject();

		Integer rrn =
			service.updateRow(originalRowImage, updateRowImage, null,
				errorObject, recfmtName);

		if (Utils.length(trim(errorObject.getErrorText(), "")) > 0) {
			this.error = true;
			throw new BusinessException(null, errorObject.getErrorText());
		}
		else {
			this.error = false;
		}

		globalError = error;
		initialize(updateRowImage, originalRowImage, recfmtName);

		return rrn;
	}

	/**
	 * Writes a new record using the work fields data.
	 *
	 * @return inserted row <code>rrn</code>
	 */
	public Integer write() {
		ET newRow = null;

		try {
			newRow = (lastReadEntity != null) ? lastReadEntity
											  : entityCls.getConstructor()
														 .newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		copyBeforeUpdate(newRow);

		return write(newRow);
	}

	/**
	 * Writes a new record using the file's entity-object data.
	 *
	 * @param newRow entity-object
	 * @return inserted row <code>rrn</code>
	 */
	public Integer write(ET newRow) {
		ErrorObject errorObject = new ErrorObject();
		Integer rrn = service.addRow(newRow, errorObject);

		if (Utils.length(trim(errorObject.getErrorText(), "")) > 0) {
			this.error = true;
			globalError = error;
			throw new BusinessException(null, errorObject.getErrorText());
		}

		return rrn;
	}

	/**
	 * Writes the passed state to the file.
	 *
	 * @param state Object which has to be persisted on the file
	 * @return inserted row <code>rrn</code>
	 */
	public Integer writeState(Object state) {
		ET newRow = null;

		try {
			newRow = (lastReadEntity != null) ? lastReadEntity
											  : entityCls.getConstructor()
														 .newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		assignObject(newRow, prefix, state, "");

		return write(newRow);
	}
}
