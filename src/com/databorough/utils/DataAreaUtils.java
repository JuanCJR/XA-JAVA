package com.databorough.utils;

import acdemxaMvcprocess.data.DataAreaInfo;
import acdemxaMvcprocess.data.DataCRUD;

import com.databorough.utils.DSUtils;
import com.databorough.utils.NumberFormatter;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.Utils.setArg;

/**
 * Provides conversion of Data Area commands.
 *
 * @author Zia Shahid
 * @since (2012-08-08.13:07:12)
 */
public final class DataAreaUtils
{
	/**
	 * The DataAreaUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private DataAreaUtils()
	{
		super();
	}

	/**
	 * Changes the value of the specified data area.
	 *
	 * @param dtaName Data area
	 * @param dtaVal New value
	 * @param pgmMbr Object of standard module
	 * @return <code>true</code> if successful, otherwise <code>false</code>
	 */
	public static boolean changeDataArea(String dtaName, String dtaVal,
		Object pgmMbr)
	{
		// CHGDTAARA DTAARA(MYLIB/MYDATA) VALUE(GOODNIGHT)
		DataCRUD<DataAreaInfo, String> dtaAreaCrud =
			new DataCRUD<DataAreaInfo, String>(DataAreaInfo.class, pgmMbr,
				new String[] { "DTANAM" });

		DataAreaInfo ob = dtaAreaCrud.retrieve(dtaName);

		if (ob != null)
		{
			// Update the value
			ob.setValue(dtaVal);
			dtaAreaCrud.update(ob);
		}

		return true;
	}

	/**
	 * Creates a data area and stores it in a specified library. It also
	 * specifies the attributes of the data. The data area can be optionally
	 * initialized to a specific value.
	 *
	 * @param dtaName Data area
	 * @param dtaVal Value
	 * @param dtaTyp Type
	 * @param dtaLen Length
	 * @param pgmMbr Object of standard module
	 * @return <code>true</code> if successful, otherwise <code>false</code>
	 */
	public static boolean createDataArea(String dtaName, String dtaVal,
		String dtaTyp, String dtaLen, Object pgmMbr)
	{
		// CRTDTAARA DTAARA(DA1) TYPE(*CHAR) LEN(3) VALUE(ABC)
		DataCRUD<DataAreaInfo, String> dtaAreaCrud =
			new DataCRUD<DataAreaInfo, String>(DataAreaInfo.class, pgmMbr,
				new String[] { "DTANAM" });

		DataAreaInfo ob = new DataAreaInfo();
		ob.setDataAreaName(dtaName);
		ob.setValue(dtaVal);
		ob.setType(dtaTyp);
		ob.setLength(NumberFormatter.toInt(dtaLen, 10, 0));
		dtaAreaCrud.write(ob);

		return true;
	}

	/**
	 * Deletes the specified data area from a library.
	 *
	 * @param dtaName Data area
	 * @param pgmMbr Object of standard module
	 * @return <code>true</code> if successful, otherwise <code>false</code>
	 */
	public static boolean deleteDataArea(String dtaName, Object pgmMbr)
	{
		// DLTDTAARA DTAARA(MYLIB/MYDATA)
		DataCRUD<DataAreaInfo, String> dtaAreaCrud =
			new DataCRUD<DataAreaInfo, String>(DataAreaInfo.class, pgmMbr,
				new String[] { "DTANAM" });
		dtaAreaCrud.delete(dtaName);

		return true;
	}

	/**
	 * Shows the attributes and value of the specified data area.
	 *
	 * @param dtaName Data area
	 * @param pgmMbr Object of standard module
	 * @return data area value
	 */
	public static String displayDataArea(String dtaName, Object pgmMbr)
	{
		return retrieveDataArea(dtaName, pgmMbr);
	}

	/**
	 * Retrieves the contents of a data area.
	 *
	 * @param obj QWCRDTAA command parameters
	 */
	public static void retrieveDataArea(Object... obj)
	{
		// Qualified data area
		Object qualDtaName = obj[2];

		String dtaName = DSUtils.objectToString(qualDtaName).substring(0, 10);
		String value = retrieveDataArea(dtaName, obj[obj.length - 1]);

		// Receiver variable
		setArg(obj, 0, value);
	}

	/**
	 * Retrieves all or part of a specified data area and copies it into a
	 * variable. RTVDTAARA does not retrieve any other attributes of the data
	 * area.
	 *
	 * @param dtaName Data area
	 * @param pgmMbr Object of standard module
	 * @return data area value
	 */
	public static String retrieveDataArea(String dtaName, Object pgmMbr)
	{
		// RTVDTAARA DTAARA(DA1) RTNVAR(&CLVAR1)
		String value = "";
		DataCRUD<DataAreaInfo, String> dtaAreaCrud =
			new DataCRUD<DataAreaInfo, String>(DataAreaInfo.class, pgmMbr,
				new String[] { "DTANAM" });

		DataAreaInfo ob = dtaAreaCrud.retrieve(dtaName);

		if (ob != null)
		{
			value = ob.getValue();
			int len = ob.getLength();

			if ((len != 0) && (value.length() > len))
			{
				value = value.substring(0, len);
			}
		}

		return value;
	}

	/**
	 * Retrieves all or part of a specified data area and copies it into a
	 * variable. RTVDTAARA does not retrieve any other attributes of the data
	 * area.
	 *
	 * @param dtaName Data area
	 * @param pos Substring starting position
	 * @param len Substring length
	 * @param pgmMbr Object of standard module
	 * @return data area value
	 */
	public static String retrieveDataArea(String dtaName, int pos, int len,
		Object pgmMbr)
	{
		// RTVDTAARA DTAARA(DA1 (2 1)) RTNVAR(&CLVAR1)
		String value = "";
		DataCRUD<DataAreaInfo, String> dtaAreaCrud =
			new DataCRUD<DataAreaInfo, String>(DataAreaInfo.class, pgmMbr,
				new String[] { "DTANAM" });

		DataAreaInfo ob = dtaAreaCrud.retrieve(dtaName);

		if (ob != null)
		{
			value = ob.getValue();
			value = subString(value, pos, len);
		}

		return value;
	}
}
