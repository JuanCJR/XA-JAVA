package acdemxaMvcprocess.data;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import acdemxaMvcprocess.daoservices.TableDaoService;

import acdemxaMvcprocess.logic.data.FilterFldData;
import static com.databorough.utils.DSUtils.assignObject;
import static com.databorough.utils.DSUtils.assignObjectFields;
import static com.databorough.utils.DSUtils.clearObject;
import com.databorough.utils.FilterData;
import static com.databorough.utils.FilterData.FLTRFLD;
import static com.databorough.utils.FilterData.KEYFLD;
import static com.databorough.utils.FilterData.OP_EQUAL;
import static com.databorough.utils.FilterData.OP_GREATER_OR_EQUAL;
import static com.databorough.utils.FilterData.OP_GREATER_THAN;
import static com.databorough.utils.FilterData.OP_LESS_THAN;
import static com.databorough.utils.FilterData.RSTFLD;
import com.databorough.utils.IConstants.IOACTIONS;
import static com.databorough.utils.IConstants.IOACTIONS.LIOEOF;
import static com.databorough.utils.IConstants.IOACTIONS.LIOEQUAL;
import static com.databorough.utils.IConstants.IOACTIONS.LIOFOUND;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.ReflectionUtils.compKeysArrays;
import static com.databorough.utils.ReflectionUtils.getColumnAnnoFldMap;
import static com.databorough.utils.ReflectionUtils.getField;
import static com.databorough.utils.ReflectionUtils.getKeyVals;
import static com.databorough.utils.ReflectionUtils.isKeysArrayEqual;
import static com.databorough.utils.ReflectionUtils.loadEDSRefs;
import static com.databorough.utils.ReflectionUtils.simplifyTypes;
import static com.databorough.utils.ReflectionUtils.updKeyFlds;
import com.databorough.utils.SortData;
import com.databorough.utils.Utils;
import static com.databorough.utils.Utils.FORWARD;
import com.databorough.utils.Utils.LASTREAD;
import com.databorough.utils.Utils.POSITION;

/**
 * Super class of DataCRUD for performing CRUD (Create, Read, Update and Delete)
 * operations on entities with a provision for pagination in both directions.
 *
 * @author TESTER
 * @version (2013-03-30.12:06:51)
 */
public class AbstractDataCrud<ET, EtId extends Serializable> {
	protected static boolean globalError;
	public static int BATCHSIZE = 1000;// 25, 200
	protected static enum RTVOPCODE { SETLL, SETGT, CHAIN, NONE };

	protected ArrayList<FilterFldData> filterFlds =
		new ArrayList<FilterFldData>();
	public Class<ET> entityCls;
	public ET lastReadEntity;

	// Refers to LocalFileFields for A Member
	protected Field fileavar;
	// Refers to LocalFileFields
	protected Field filevar;
	// Reference of lastIO field which stores result status (success/failure) of
	// last I/O operation at Program level
	protected Field lastIO;
	// Refers to PRTFs' fields for A Member
	protected Field prtfavar;
	// Refers to PRTFs' fields
	protected Field prtfvar;
	protected Field statevar;

	protected LASTREAD lastRead;

	protected LinkedHashMap<String, Field> entityAnnoFieldsMap =
		new LinkedHashMap<String, Field>();
	protected LinkedHashMap<String, Column> entityFieldsAnnoMap =
		new LinkedHashMap<String, Column>();
	protected LinkedHashMap<String, Integer> fieldNames =
		new LinkedHashMap<String, Integer>();

	protected List<ET> recList = new ArrayList<ET>();
	// For storing references of all Unqualified EDSs which also now need to be
	// updated/read by CRUD
	public List<Field> uqEDSs = new ArrayList<Field>();
	public Map<String, Boolean> fldsFiltered;
	protected Map<String, String> ispMap = new HashMap<String, String>();
	protected Object pgmMbr;
	protected RTVOPCODE rtvOpCode;
	protected String aggrFunction;

	// For handling file-field prefixes at runtime
	public String prefix = "";
	protected String recfmtName = "";
	protected TableDaoService<ET, EtId> service;
	protected Object equalKeyVals[] = new Object[] {  };
	protected String keyFlds[] = {  };
	protected char keyFldsSlt[] = {  };
	protected char keyFldsSortOrder[] = {  };
	protected Object lastEqKeyVals[] = new Object[] {  };
	protected Object lastPosKeyVals[] = new Object[] {  };
	protected Object positionKeyVals[] = new Object[] {  };

	// Represents both the BOF & EOF conditions
	protected boolean EOF;
	protected boolean applyFilters;
	protected boolean canBeEOF;
	protected boolean chkAnnotations = true;
	protected boolean equal;
	protected boolean error;
	protected boolean filterFldsLoaded;
	protected boolean found;
	protected boolean lastCondEqual;
	protected boolean positionedInPreviousMode;
	protected boolean previous = false;
	protected boolean reQuery = true;
	private boolean scrollableFetch;
	protected boolean synon = false;
	protected int currentBatchIndex;
	protected int lastRowPosition;
	protected int startRowPosition;

	/**
	 * Determines if all keys are restrictor keys.
	 *
	 * @return <code>true</code> if all keys are restricted, false otherwise
	 * @since (2012-11-22.18:05:53)
	 */
	protected boolean allKeysRestricted() {
		boolean flag = true;

		for (char sltFld : keyFldsSlt) {
			if (sltFld != 'R') {
				flag = false;

				break;
			}
		}

		return flag;
	}

	/**
	 * Sets elements in an Entity class to its default initialization value.
	 */
	public void clear() {
		Object obj = null;

		try {
			obj = entityCls.getConstructor().newInstance();
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		clear(obj);
	}

	/**
	 * Clears a File Object.
	 *
	 * @param obj Object to be cleared
	 */
	public void clear(Object obj) {
		clearObject(obj);
		copyAfterRead(obj);
	}

	/**
	 * Stores references of workfields to synchronize from pgm to DataCRUD on
	 * I/O operations.
	 *
	 * @param pgmMbr Object of pgm to get the references
	 */
	protected void collectReferencesInfo(Object pgmMbr) {
		try {
			this.statevar = pgmMbr.getClass().getField("stateVariable");
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			this.filevar = pgmMbr.getClass().getField("fileVariable");
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			this.fileavar = pgmMbr.getClass().getField("fileAVariable");
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			this.prtfvar = pgmMbr.getClass().getField("prtfVariable");
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			this.prtfavar = pgmMbr.getClass().getField("prtfAVariable");
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			this.lastIO = pgmMbr.getClass().getField("lastIO");
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		// Loading references of all UnqualifiedEDSs at program level which too
		// form a part of workfields in addition to stateVariable, fileVariable
		// & fileAVariable
		loadEDSRefs(pgmMbr, uqEDSs);
	}

	/**
	 * Copies the values of entity object into the local variables which is used
	 * in the logic after performing the Read operation.
	 *
	 * @param rdxRet Object of the entity class
	 */
	protected void copyAfterRead(Object rdxRet) {
		Object tmpEc = null;

		try {
			if (statevar != null) {
				tmpEc = statevar.get(pgmMbr);
				//assignObject(tmpEc, "", rdxRet, prefix);
				assignObject(tmpEc, "", false, rdxRet, prefix, false,
					chkAnnotations, ispMap, recfmtName);
				assignObject(tmpEc, "o_", true, rdxRet, "o_", false,
					chkAnnotations, ispMap, recfmtName);
				statevar.set(pgmMbr, tmpEc);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		assignObjectFields(uqEDSs, rdxRet, prefix, pgmMbr, ispMap);

		try {
			if (fileavar != null) {
				tmpEc = fileavar.get(pgmMbr);
				assignObject(tmpEc, prefix, true, rdxRet, prefix, false,
					chkAnnotations, ispMap, recfmtName);
				fileavar.set(pgmMbr, tmpEc);
			}

			if (filevar != null) {
				tmpEc = filevar.get(pgmMbr);
				assignObject(tmpEc, prefix, true, rdxRet, prefix, false,
					chkAnnotations, ispMap, recfmtName);
				filevar.set(pgmMbr, tmpEc);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			if (prtfavar != null) {
				tmpEc = prtfavar.get(pgmMbr);
				assignObject(tmpEc, prefix, true, rdxRet, prefix, false,
					chkAnnotations, ispMap, recfmtName);
				prtfavar.set(pgmMbr, tmpEc);
			}

			if (prtfvar != null) {
				tmpEc = prtfvar.get(pgmMbr);
				assignObject(tmpEc, prefix, true, rdxRet, prefix, false,
					chkAnnotations, ispMap, recfmtName);
				prtfvar.set(pgmMbr, tmpEc);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}
	}

	/**
	 * Copies the values of the all local variables in the entity object before
	 * performing the Update.
	 *
	 * @param updateRowImage Object of the entity class
	 */
	protected void copyBeforeUpdate(ET updateRowImage) {
		Object tmpEc = null;

		try {
			if (fileavar != null) {
				tmpEc = fileavar.get(pgmMbr);
				assignObject(updateRowImage, prefix, false, tmpEc, prefix,
					true, chkAnnotations, recfmtName);
			}

			if (filevar != null) {
				tmpEc = filevar.get(pgmMbr);
				assignObject(updateRowImage, prefix, false, tmpEc, prefix,
					true, chkAnnotations, recfmtName);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		try {
			if (prtfavar != null) {
				tmpEc = prtfavar.get(pgmMbr);
				assignObject(updateRowImage, prefix, false, tmpEc, prefix,
					true, chkAnnotations, recfmtName);
			}

			if (prtfvar != null) {
				tmpEc = prtfvar.get(pgmMbr);
				assignObject(updateRowImage, prefix, false, tmpEc, prefix,
					true, chkAnnotations, recfmtName);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		assignObjectFields(updateRowImage, prefix, uqEDSs, pgmMbr, ispMap);

		try {
			if (statevar != null) {
				tmpEc = statevar.get(pgmMbr);
				assignObject(updateRowImage, prefix, tmpEc, "");
				assignObject(updateRowImage, "o_", false, tmpEc, "o_", true,
					chkAnnotations, recfmtName);
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		updKeyFlds(updateRowImage, recfmtName);
	}

	/**
	 * Creates the Filter/Position criteria for fetching the data.
	 *
	 * @return a list of FilterData
	 */
	protected List<FilterData> createCriteria() {
		List<FilterData> selectionCriteria = new ArrayList<FilterData>();

		if (Utils.length(positionKeyVals) > 0) {
			// Compare lastPosKeyVals and posKeyVals to see if reQuery is true
			List<String> chgdKeyFlds = getChangedKeyFlds();

			int numPosKeys = Math.min(positionKeyVals.length, keyFlds.length);
			int numKeyFlds = numPosKeys;
			int numEqKeys = Utils.length(equalKeyVals);
			boolean allRestrictorKeys = allKeysRestricted();

			// New code - 04-03-14
			for (int i = numPosKeys - 1; i >= 0; i--) {
				if (!(positionKeyVals[i] instanceof String) ||
						!isBlanks(positionKeyVals[i])) {
					numKeyFlds = numPosKeys = i + 1;

					break;
				}
			} // i

			for (int i = 0; i < numPosKeys; i++) {
				int compOpr = xor(previous, (keyFldsSortOrder[i] == 'A'))
					? OP_GREATER_THAN : OP_LESS_THAN;

				if (positionKeyVals[i] instanceof String) {
					boolean skipKeyFld = false;

					if (minOrMaxStrVal(positionKeyVals[i])) {
						boolean fldIsLoval =
							String.valueOf(positionKeyVals[i])
								  .startsWith(String.valueOf(
									Character.MIN_VALUE));

						boolean effFldIsLoval =
							!(xor(fldIsLoval, (keyFldsSortOrder[i] == 'A')));

						if (!xor(effFldIsLoval, (compOpr == OP_GREATER_THAN))) {
							skipKeyFld = true;
						}
					}

					if (skipKeyFld || isBlanks(positionKeyVals[i])) {
						if (lastCondEqual && (i == (numKeyFlds - 1)) &&
								(selectionCriteria.size() > 0)) {
							FilterData lastKeyFilter =
								selectionCriteria.get(selectionCriteria.size() -
									1);

							if (lastKeyFilter != null) {
								int oper = lastKeyFilter.getOperator();

								if ((oper == OP_LESS_THAN) ||
										(oper == OP_GREATER_THAN)) {
									lastKeyFilter.setOperator(oper + 2);
								}
							}
						}

						numKeyFlds--;

						continue;
					}
				} // if (positionKeyVals[i] instanceof String)

				if (lastCondEqual && (i == (numKeyFlds - 1))) {
					compOpr += 2;
				}

				String keyDataType = getKeyDataType(entityCls, keyFlds[i]);
				Field fld = entityAnnoFieldsMap.get(keyFlds[i].toUpperCase());

				if (fld == null) {
					fld = entityAnnoFieldsMap.get(keyFlds[i]);
				}

				String keyFldName = fld.getName();

				// If blank key field is a restricted field
				if ((
						isBlanks(positionKeyVals[i]) ||
						(
							reQuery && (i < Utils.length(lastPosKeyVals)) &&
							isBlanks(lastPosKeyVals[i])
						)
					) && (keyFldsSlt[i] == 'R')) {
					keyFldsSlt[i] = 'N';
				}

				if (keyFldsSlt[i] == 'R') {
					// If field is filter field and also a restricted field then
					// mark it as filter field
					if (applyFilters && isFilterField(keyFlds[i])) {
						continue;
					}

					if (chgdKeyFlds.contains(keyFlds[i].toUpperCase())) {
						if (!applyFilters &&
								!(lastCondEqual && (i == (numKeyFlds - 1)))) {
							compOpr = OP_GREATER_THAN;
						}
					}
					else {
						if ((i == (numKeyFlds - 1)) && allRestrictorKeys) {
							// If next batch of data is being fetched
							if (reQuery && !lastCondEqual) {
								compOpr -= 2;
							}
							else {
								if ((i < numEqKeys) ||
										(rtvOpCode == RTVOPCODE.CHAIN)) {
									compOpr = OP_EQUAL;
								}
							}
						}
						else {
							if ((i < numEqKeys) ||
									(rtvOpCode == RTVOPCODE.CHAIN)) {
								compOpr = OP_EQUAL;
							}
						}
					}

					//if ("0".equals(positionKeyVals[i].toString())) {
					if ("0".equals(positionKeyVals[i].toString()) &&
							(rtvOpCode != RTVOPCODE.CHAIN)) {
						compOpr = OP_GREATER_OR_EQUAL;
					}

					selectionCriteria.add(new FilterData(keyFldName,
							positionKeyVals[i], compOpr, RSTFLD, keyDataType));
				} // if (keyFldsSlt[i] == 'R')
				else {
					if (!applyFilters) {
						selectionCriteria.add(new FilterData(keyFldName,
								positionKeyVals[i], compOpr, KEYFLD,
								keyDataType));
					}
					else if (!isFilterField(keyFlds[i])) {
						selectionCriteria.add(new FilterData(keyFldName,
								positionKeyVals[i], compOpr, KEYFLD,
								keyDataType));
					}
				}
			} // i
		} // if (Utils.length(positionKeyVals) > 0)

		if (Utils.length(equalKeyVals) > 0) {
			simplifyTypes(equalKeyVals);

			int comp =
				compKeysArrays(positionKeyVals, equalKeyVals, keyFldsSortOrder);

			if (applyFilters || (comp == 0) || (!previous && (comp > 0)) ||
					(previous && (comp < 0))) {
				int numEqKeys = equalKeyVals.length;

				for (int i = 0; i < numEqKeys; i++) {
					if (applyFilters && (fldsFiltered != null) &&
							!fldsFiltered.get(filterFlds.get(i).getFltName())) {
						continue;
					}

					if (equalKeyVals[i] instanceof String &&
							isBlanks(equalKeyVals[i])) {
						continue;
					}

					if (!applyFilters && (keyFldsSlt[i] == 'R')) {
						continue;
					}

					String keyName =
						applyFilters ? filterFlds.get(i).getFltFld() :
							keyFlds[i];
					Integer width = fieldNames.get(keyName);

					if (width == null) {
						width = 0;
					}

					Field fld = entityAnnoFieldsMap.get(keyName.toUpperCase());

					if (fld == null) {
						fld = entityAnnoFieldsMap.get(keyName);
					}

					String keyFldName = fld.getName();

					String keyDataType = getKeyDataType(entityCls, keyName);

					FilterData filterData =
						new FilterData(keyFldName, equalKeyVals[i],
							(applyFilters ? filterFlds.get(i).getOpType()
										  : OP_EQUAL), width,
							(applyFilters ? FLTRFLD : RSTFLD), keyDataType);
					selectionCriteria.add(filterData);
				} // i
			}
		} // if (Utils.length(equalKeyVals) > 0)

		return selectionCriteria;
	}

	/**
	 * Fetches data on the basis of key values provided.
	 *
	 * @param startRowPosition first rowNum to fetch
	 * @param lastRowToFetch last rowNum to fetch
	 * @param posKeyValues Positioning values in the sequence order of keyFlds
	 * @param eqKeyValues Equal Key values to read next record(s)
	 * @param previous true if it has to fetch previous record(s)
	 * @param lastCondEqual should be specified true forward fetching and false
	 *        for backward fetching
	 */
	protected void fetch(int startRowPosition, int lastRowToFetch,
		Object posKeyValues[], Object eqKeyValues[], boolean previous,
		boolean lastCondEqual) {
		setMemberVariables(startRowPosition, lastRowToFetch, posKeyValues,
			eqKeyValues, previous, lastCondEqual);
		fetch();
	}

	/**
	 * Fetches the data. After completing the data fetching also sets some
	 * variables which used in the logic to show the data.
	 */
	protected void fetch() {
		//lastPosKeyVals = positionKeyVals;

		if (applyFilters) {
			loadFilterFldsIfRqd();
		}
		else {
			lastEqKeyVals = equalKeyVals;
		}

		recList.clear();

		List<FilterData> selectionCriteria = createCriteria();

		if (previous) {
			toggleKeyFldsSortOrder();
		}

		// New code - 15-07-13
		// lastRowPosition replaced with BATCHSIZE
		List<ET> tmpRecList = service.fetch(startRowPosition, BATCHSIZE,
				selectionCriteria, getSortList(), applyFilters, pgmMbr);

		if (previous) {
			toggleKeyFldsSortOrder();
		}

		if (tmpRecList != null) {
			recList = tmpRecList;
			found = recList.size() != 0;
		}
		else {
			found = false;
			error = true;
			globalError = error;
		}

		if (found) {
			EOF = false;
		}

		if (!found &&
				(
					(rtvOpCode == RTVOPCODE.SETGT) ||
					(rtvOpCode == RTVOPCODE.SETLL) ||
					(rtvOpCode == RTVOPCODE.CHAIN)
				)) {
			EOF =  true;
			error = true;
			globalError = error;
			// New code - 15-07-13
			lastRowPosition = 0;
		}

		setLio(LIOEOF, EOF);
		setLio(LIOFOUND, found);

		if (found) {
			reQuery = false;
			positionedInPreviousMode = previous;
		}

		if (found) {
			canBeEOF = recList.size() < BATCHSIZE;

			if ((Utils.length(positionKeyVals) == 0)) {
				found = false;
			}

			// New code - 15-07-13
			if (scrollableFetch) {
				lastRowPosition += recList.size();

				if (canBeEOF) {
					lastRowPosition = 0;
				}
			}
		}

		currentBatchIndex = 0;
		lastRead = LASTREAD.unspecified;
		lastReadEntity = null;
		applyFilters = false;
		lastPosKeyVals = positionKeyVals;
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 * @param readPrev whether to read the prior record from a file
	 */
	public void fetch(Object posKeyValues[], boolean readPrev) {
		setMemberVariables(0, BATCHSIZE, posKeyValues, null, readPrev,
			!readPrev);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 * @param readPrev whether to read the prior record from a file
	 * @param eqKeyValues array of equal keys
	 */
	public void fetch(Object posKeyValues[], boolean readPrev,
		Object eqKeyValues[]) {
		setMemberVariables(0, BATCHSIZE, posKeyValues, eqKeyValues, readPrev,
			!readPrev);
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
	 * than or equal to the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that a Read Previous operation retrieves the
	 * last record in the file, or a Read operation receives an end-of-file
	 * indication.
	 *
	 * @param pos HIVAL/LOVAL conditions
	 * @param readPrev whether to read the prior record from a file
	 * @param eqKeyValues array of equal keys
	 */
	public void fetch(POSITION pos, boolean readPrev, Object eqKeyValues[]) {
		setMemberVariables(0, BATCHSIZE, null, eqKeyValues, readPrev,
			!readPrev);
		fetch();
		equal = found;
		setLio(LIOEQUAL, equal);
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that a Read Previous operation retrieves the
	 * last record in the file, or a Read operation receives an end-of-file
	 * indication.
	 *
	 * @param pos HIVAL/LOVAL conditions
	 * @param eqKeyValues array of equal keys
	 */
	public void fetch(POSITION pos, Object eqKeyValues[]) {
		setMemberVariables(0, BATCHSIZE, null, eqKeyValues, FORWARD, !FORWARD);
		fetch();
		equal = found;
		setLio(LIOEQUAL, equal);
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that a Read Previous operation retrieves the
	 * last record in the file, or a Read operation receives an end-of-file
	 * indication.
	 *
	 * @param pos HIVAL/LOVAL conditions
	 * @param readPrev whether to read the prior record from a file
	 */
	public void fetch(POSITION pos, boolean readPrev) {
		setMemberVariables(0, BATCHSIZE, null, null, readPrev, !readPrev);
		fetch();
		equal = found;
		setLio(LIOEQUAL, equal);
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that a Read Previous operation retrieves the
	 * last record in the file, or a Read operation receives an end-of-file
	 * indication.
	 *
	 * @param pos HIVAL/LOVAL conditions
	 */
	public void fetch(POSITION pos) {
		setMemberVariables(0, BATCHSIZE, null, null, FORWARD, !FORWARD);
		fetch();
		equal = found;
		setLio(LIOEQUAL, equal);
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 * @param eqKeyValues array of equal keys
	 */
	public void fetch(Object posKeyValues[], Object eqKeyValues[]) {
		rtvOpCode = RTVOPCODE.SETLL;
		setMemberVariables(0, BATCHSIZE, posKeyValues, eqKeyValues, FORWARD,
			!FORWARD);
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
	 * @param readPrev whether to read the prior record from a file
	 * @param eqKeyValues array of equal keys
	 */
	public void fetchAfter(Object posKeyValues[], boolean readPrev,
		Object eqKeyValues[]) {
		setMemberVariables(0, BATCHSIZE, posKeyValues, eqKeyValues, readPrev,
			readPrev);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 * @param eqKeyValues array of equal keys
	 */
	public void fetchAfter(Object posKeyValues[], Object eqKeyValues[]) {
		rtvOpCode = RTVOPCODE.SETGT;
		setMemberVariables(0, BATCHSIZE, posKeyValues, eqKeyValues, FORWARD,
			FORWARD);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument specified.
	 *
	 * @param posKeyValues array of position keys
	 * @param readPrev whether to read the prior record from a file
	 */
	public void fetchAfter(Object posKeyValues[], boolean readPrev) {
		setMemberVariables(0, BATCHSIZE, posKeyValues, null, readPrev, readPrev);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that the first Read operation retrieves the
	 * first record in the file (the record with the highest key).
	 *
	 * @param pos HIVAL/LOVAL conditions
	 * @param readPrev whether to read the prior record from a file
	 * @param eqKeyValues array of equal keys
	 */
	public void fetchAfter(POSITION pos, boolean readPrev,
		Object eqKeyValues[]) {
		setMemberVariables(0, BATCHSIZE, null, eqKeyValues, readPrev, readPrev);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that the first Read operation retrieves the
	 * first record in the file (the record with the highest key).
	 *
	 * @param pos HIVAL/LOVAL conditions.
	 * @param eqKeyValues array of equal keys
	 */
	public void fetchAfter(POSITION pos, Object eqKeyValues[]) {
		setMemberVariables(0, BATCHSIZE, null, eqKeyValues, FORWARD, FORWARD);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that the first Read operation retrieves the
	 * first record in the file (the record with the highest key).
	 *
	 * @param pos HIVAL/LOVAL conditions
	 * @param readPrev whether to read the prior record from a file
	 */
	public void fetchAfter(POSITION pos, boolean readPrev) {
		setMemberVariables(0, BATCHSIZE, null, null, readPrev, readPrev);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than the key argument (figurative constant) specified.
	 * <p>
	 * LOVAL positions the file so that the first Read operation retrieves the
	 * record with the lowest key.
	 * <p>
	 * HIVAL positions the file so that the first Read operation retrieves the
	 * first record in the file (the record with the highest key).
	 *
	 * @param pos HIVAL/LOVAL conditions
	 */
	public void fetchAfter(POSITION pos) {
		setMemberVariables(0, BATCHSIZE, null, null, FORWARD, FORWARD);
		fetch();
	}

	/**
	 * Positions the result set at the next record with a key that is greater
	 * than or equal to the key argument specified starting from the last row
	 * position.
	 *
	 * @param posKeyValues array of position keys
	 * @param eqKeyValues array of equal keys
	 * @since (2013-07-15.17:18:17)
	 */
	public void fetchScrollable(Object posKeyValues[], Object eqKeyValues[]) {
		scrollableFetch = true;
		rtvOpCode = RTVOPCODE.SETLL;
		setMemberVariables(lastRowPosition, BATCHSIZE, posKeyValues,
			eqKeyValues, FORWARD, !FORWARD);
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
	 * Gets list of changed key fields.
	 *
	 * @return list of changed key fields
	 * @since (2012-11-22.14:53:35)
	 */
	private List<String> getChangedKeyFlds() {
		List<String> lstFlds = new ArrayList<String>();

		int len = Utils.length(lastPosKeyVals);
		int numKeys = Utils.length(keyFlds);

		for (int i = 0; i < len; i++) {
			if (i <= (numKeys - 1)) {
				Field field = entityAnnoFieldsMap.get(keyFlds[i].toUpperCase());

				if (isValueChanged(field.getName(),
						lastPosKeyVals[i].toString())) {
					lstFlds.add(keyFlds[i]);
				}
			}
		}

		return lstFlds;
	}

	/**
	 * Gets the filter fields value using the reflection.
	 *
	 * @return array of object which has the value of filter fields
	 */
	protected Object[] getFilterVals() {
		if (filterFlds == null) {
			return null;
		}

		List<Object> values = new ArrayList<Object>();
		Object tmpEc = null;

		try {
			if (statevar != null) {
				tmpEc = statevar.get(pgmMbr);

				for (FilterFldData fltFld : filterFlds) {
					try {
						Field sfld =
							statevar.getType()
									.getDeclaredField(fltFld.getFltName());
						sfld.setAccessible(true);
						values.add(sfld.get(tmpEc));
						sfld.setAccessible(false);
					}
					catch (NoSuchFieldException e) {
						values.add(null);
					}
					catch (Exception e) {
						logStackTrace(e);
					}
				}
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		return values.toArray();
	}

	/**
	 * Gets the data type of key field.
	 *
	 * @param cls Entity class
	 * @param keyField
	 * @return the data type of provided keyField
	 */
	protected String getKeyDataType(Class<?> cls, String keyField) {
		LinkedHashMap<String, Field> colFldMap =
			new LinkedHashMap<String, Field>();
		getColumnAnnoFldMap(cls, colFldMap, false, false);

		Field fld = colFldMap.get(keyField.toUpperCase());

		if (fld == null) {
			fld = colFldMap.get(keyField);
		}

		return fld.getType().getSimpleName();
	}

	/**
	 * Retrieves the next record from the file.
	 *
	 * @return <tt>Entity</tt> class object
	 */
	protected ET getNext() {
		// READ operation
		ET nextEntity = readex(false, null);

		return nextEntity;
	}

	/**
	 * Retrieves the next record from the file if the key of the record matches
	 * the key argument.
	 *
	 * @param values variable number of keys
	 * @return <tt>Entity</tt> class object
	 */
	protected ET getNext(Object... values) {
		ET nextEntity = readex(false, values);
		lastEqKeyVals = null;

		return nextEntity;
	}

	/**
	 * Retrieves the previous record from the file.
	 *
	 * @return <tt>Entity</tt> class object
	 */
	protected ET getPrevious() {
		ET prevEntity = readx(true);

		return prevEntity;
	}

	/**
	 * Retrieves the next prior record from a file if the key of the record
	 * matches the key argument specified.
	 *
	 * @param values variable number of keys
	 * @return <tt>Entity</tt> class object
	 */
	protected ET getPrevious(Object... values) {
		ET prevEntity = readex(true, values);
		lastEqKeyVals = null;

		return prevEntity;
	}

	/**
	 * Returns the list of SortData from keyFields which is used in orderby
	 * clause.
	 *
	 * @return list of SortData
	 */
	public List<SortData> getSortList() {
		List<SortData> sortList = new ArrayList<SortData>();
		int numKeys = Utils.length(keyFlds);

		for (int i = 0; i < numKeys; i++) {
			if (keyFldsSortOrder[i] == ' ') {
				continue;
			}

			Field fld = entityAnnoFieldsMap.get(keyFlds[i].toUpperCase());

			if (fld == null) {
				fld = entityAnnoFieldsMap.get(keyFlds[i]);
			}

			if (fld == null) {
				continue;
			}

			String keyFldName = fld.getName();
			sortList.add(new SortData(keyFldName, keyFldsSortOrder[i] == 'D'));
		}

		return sortList;
	}

	/**
	 * Returns true if the object has the blank value.
	 *
	 * @param obj
	 * @return boolean
	 */
	private boolean isBlanks(Object obj) {
		return (obj == null) || (String.valueOf(obj).trim().length() == 0);
	}

	/**
	 * Returns true if the record pointer is at the end of file.
	 *
	 * @return boolean
	 */
	public boolean isEndOfFile() {
		return EOF;
	}

	/**
	 * Returns true if the record is found only on start operation.
	 *
	 * @return boolean
	 */
	public boolean isEqual() {
		return equal;
	}

	/**
	 * Returns true if an I/O operation results in error in a File (File level
	 * error condition).
	 *
	 * @return boolean
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Checks if this is a filter field.
	 *
	 * @param field field name
	 * @return <code>true</code> if filter field, false otherwise
	 * @since (2012-11-22.14:56:35)
	 */
	private boolean isFilterField(String field) {
		for (FilterFldData filterFld : filterFlds) {
			if (filterFld.getFltFld().equalsIgnoreCase(field)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if record is found or not.
	 *
	 * @return boolean
	 */
	public boolean isFound() {
		return found;
	}

	/**
	 * Returns true if last I/O operation in this or among all the programs
	 * resulted in an error (globalError condition).
	 *
	 * @return boolean
	 */
	public static boolean isLastError() {
		return globalError;
	}

	/**
	 * Determines if the entity's field value was changed.
	 *
	 * @param field field name
	 * @param value current value
	 * @return <code>true</code> if value was changed, false otherwise
	 * @since (2012-11-01.13:28:35)
	 */
	public boolean isValueChanged(String field, String value) {
		if (lastReadEntity == null) {
			return false;
		}

		Object origValue = getField(lastReadEntity, field);

		if (origValue == null) {
			return false;
		}

		return !origValue.toString().trim().equals(value.trim());
	}

	/**
	 * Filter fields are loaded first time when corresponding bean is created.
	 * Their references & names are essential to query them for fetch method.
	 */
	@SuppressWarnings("unchecked")
	protected void loadFilterFldsIfRqd() {
		if (!filterFldsLoaded) {
			try {
				filterFlds = (ArrayList<FilterFldData>)pgmMbr.getClass()
				 		 .getField("filterFlds").get(pgmMbr);
			}
			catch (NoSuchFieldException e) {
				logStackTrace(e);
			}
			catch (Exception e) {
				logStackTrace(e);
			}

			if (filterFlds != null) {
				if (synon) {
					for (int i = 0; i < filterFlds.size();) {
						Column fltFldAnno =
							entityFieldsAnnoMap.get(
								filterFlds.get(i).getFltFld().toUpperCase());

						if (fltFldAnno != null) {
							filterFlds.get(i).setFltFld(fltFldAnno.name());
							i++;
						}
						else {
							filterFlds.remove(i);
						}
					}
				}

				filterFldsLoaded = true;
			}
		}

		if (filterFldsLoaded) {
			try {
				fldsFiltered = (Map<String, Boolean>)pgmMbr.getClass()
				   		   .getField("fldsFiltered").get(pgmMbr);
			}
			catch (NoSuchFieldException e) {
				logStackTrace(e);
			}
			catch (Exception e) {
				logStackTrace(e);
			}
		}
	}

	/**
	 * Checks if the passed object has the minimum or maximum string value.
	 *
	 * @param str
	 * @return true/false depending on the value
	 */
	private boolean minOrMaxStrVal(Object str) {
		return String.valueOf(str)
					 .startsWith(String.valueOf(Character.MAX_VALUE)) ||
		String.valueOf(str).startsWith(String.valueOf(Character.MIN_VALUE));
	}

	/**
	 * Reads data and also sets EOF.
	 *
	 * @param readPrev true if reading previous record, false otherwise
	 * @param equalKeyValues equal keys if the operation involves such Values
	 * @return an entity object fetched from recList (batch of records it holds)
	 */
	protected ET readex(boolean readPrev, Object equalKeyValues[]) {
		if (EOF) {
			// New code - 15-07-13
			lastRowPosition = 0;

			return null;
		}

		/*
		 * awayFrmLastPos means away from where rec-pointer was last positioned
		 * whether by Start or by READs.
		 * And false value means the opposite
		 */
		boolean awayFrmLastPos = !(positionedInPreviousMode ^ readPrev);

		if ((currentBatchIndex == 0) && !awayFrmLastPos) {
			positionedInPreviousMode = !positionedInPreviousMode;
			reQuery = true;
			awayFrmLastPos = true;
		}

		if (lastRead == LASTREAD.unspecified) {
			// means a Start/StartAfter has been called before
			if (positionedInPreviousMode != readPrev) {
				reQuery = true;
			}
		}

		if (Utils.length(equalKeyValues) > 0) {
			lastRead = readPrev ? LASTREAD.READPE : LASTREAD.READE;
		}
		else {
			if ((lastRead == LASTREAD.READE) || (lastRead == LASTREAD.READPE)) {
				reQuery = true;
			}

			if ((lastRead == LASTREAD.unspecified) ||
					(Utils.length(lastEqKeyVals) != 0)) {
				equalKeyValues = lastEqKeyVals;
			}

			lastRead = readPrev ? LASTREAD.READP : LASTREAD.READ;
		}

		if (reQuery) {
			if (scrollableFetch) {
				// New code - 15-07-13
				fetch(lastRowPosition, BATCHSIZE,
					getKeyVals(lastReadEntity, keyFlds), equalKeyValues,
					readPrev, true);

				if (Utils.length(recList) < 1) {
					EOF = true;
					lastRowPosition = 0;
				}
			}
			else {
				fetch(0, BATCHSIZE, getKeyVals(lastReadEntity, keyFlds),
					equalKeyValues, readPrev, false);

				if (Utils.length(recList) < 1) {
					EOF = true;
				}
			}
		}

		ET entity = null;

		if (((currentBatchIndex > 0) && !awayFrmLastPos) ||
				(
					awayFrmLastPos &&
					(currentBatchIndex < Utils.length(recList))
				)) {
			entity = recList.get(awayFrmLastPos ? currentBatchIndex++
												: (--currentBatchIndex));
			lastReadEntity = entity;

			if (Utils.length(equalKeyValues) > 0) {
				if (!isKeysArrayEqual(equalKeyValues,
						getKeyVals(entity, keyFlds, equalKeyValues.length))) {
					EOF = true;
					currentBatchIndex += (awayFrmLastPos ? (-1) : 1);
					entity = null;
					// New code - 15-07-13
					lastRowPosition = 0;
				}
			}
		}
		else if (!isEndOfFile()) {
			if (canBeEOF) {
				EOF = true;
				// New code - 15-07-13
				lastRowPosition = 0;
			}
			else {
				reQuery = true; // batch finished means re-query
				entity = readex(readPrev, equalKeyValues);
			}
		}

		setLio(LIOEOF, EOF);

		return entity;
	}

	/**
	 * Reads the data and also sets EOF.
	 *
	 * @param readPrev true if reading previous record, false otherwise
	 * @return an entity object fetched from recList (batch of records it holds)
	 */
	private ET readx(boolean readPrev) {
		return readex(readPrev, null);
	}

	public void setApplyFilters(boolean applyFilters) {
		this.applyFilters = applyFilters;
	}

	public static void setLastGlobalError(boolean lastGlobalError) {
		globalError = lastGlobalError;
	}

	protected void setLio(IOACTIONS ioact, boolean val) {
		if (lastIO == null) {
			return;
		}

		try {
			LastIO lstIO = (LastIO)lastIO.get(pgmMbr);

			switch (ioact) {
			case LIOFOUND: {
				lstIO.setFound(val);

				break;
			}

			case LIOEQUAL: {
				lstIO.setEqual(val);

				break;
			}

			case LIOEOF: {
				lstIO.setAtEOF(val);

				break;
			}

			case LIOERROR: {
				lstIO.setError(val);

				break;
			}
			}

			lastIO.set(pgmMbr, lstIO);
		}
		catch (Exception e) {
			logStackTrace(e);
		}
	}

	protected void setMemberVariables(int startRowPosition,
		int lastRowPosition, Object posKeyValues[], Object eqKeyValues[],
		boolean previous, boolean lastCondEqual) {
		this.startRowPosition = startRowPosition;
		// Commented code - 15-07-13
		//this.lastRowPosition = lastRowPosition;
		this.positionKeyVals = posKeyValues;
		this.equalKeyVals = eqKeyValues;
		this.previous = previous;
		this.lastCondEqual = lastCondEqual;
	}

	/**
	 * A private utility to temporarily toggleKeyFldsSortOrder & reverse it.
	 * Used by fetch method.
	 */
	private void toggleKeyFldsSortOrder() {
		int numKeys = Utils.length(keyFldsSortOrder);

		for (int i = 0; i < numKeys; i++) {
			keyFldsSortOrder[i] = (keyFldsSortOrder[i] == 'A') ? 'D' : 'A';
		}
	}

	/**
	 * Performs the XOR operation.
	 *
	 * @param condition1
	 * @param condition2
	 * @return true/false depending on the conditions
	 */
	private boolean xor(boolean condition1, boolean condition2) {
		if ((condition1 && condition2) || (!condition1 && !condition2)) {
			return false;
		}
		else {
			return true;
		}
	}
}
