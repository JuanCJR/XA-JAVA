package com.databorough.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import acdemxaMvcprocess.logic.data.ExtFld;
import acdemxaMvcprocess.logic.data.UnqualifiedEDS;
import acdemxaMvcprocess.logic.data.rpgFloat;

import static com.databorough.utils.ArrayUtils.initArray;
import static com.databorough.utils.DateTimeConverter.hiDate;
import static com.databorough.utils.DateTimeConverter.hiTimestamp;
import static com.databorough.utils.DateTimeConverter.loDate;
import static com.databorough.utils.DateTimeConverter.loTimestamp;
import static com.databorough.utils.DateTimeConverter.toDateTime;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.NumberFormatter.hiDFloat;
import static com.databorough.utils.NumberFormatter.hiDouble;
import static com.databorough.utils.NumberFormatter.hiFloat;
import static com.databorough.utils.NumberFormatter.hiInt;
import static com.databorough.utils.NumberFormatter.hiLong;
import static com.databorough.utils.NumberFormatter.loDFloat;
import static com.databorough.utils.NumberFormatter.loDouble;
import static com.databorough.utils.NumberFormatter.loFloat;
import static com.databorough.utils.NumberFormatter.loInt;
import static com.databorough.utils.NumberFormatter.loLong;
import static com.databorough.utils.NumberFormatter.toDouble;
import static com.databorough.utils.NumberFormatter.toFloat;
import static com.databorough.utils.NumberFormatter.toInt;
import static com.databorough.utils.NumberFormatter.toLong;
import static com.databorough.utils.ReflectionUtils.getAllFields;
import static com.databorough.utils.ReflectionUtils.getAllFieldsMap;
import static com.databorough.utils.ReflectionUtils.getCompatibleVal;
import static com.databorough.utils.ReflectionUtils.getFld2ColumnAnnoMap;
import static com.databorough.utils.ReflectionUtils.getFldByName;
import static com.databorough.utils.ReflectionUtils.getPrimResetVal;
import static com.databorough.utils.ReflectionUtils.isTypeCompatible;
import static com.databorough.utils.ReflectionUtils.isUserDefOrArr;
import static com.databorough.utils.ReflectionUtils.reset;
import static com.databorough.utils.ReflectionUtils.resetQin;
import static com.databorough.utils.ReflectionUtils.setCompatibleField;
import static com.databorough.utils.ReflectionUtils.updateExtFlds;
import static com.databorough.utils.StringUtils.all;
import static com.databorough.utils.StringUtils.hiChar;
import static com.databorough.utils.StringUtils.loChar;
import static com.databorough.utils.StringUtils.setStr;
import static com.databorough.utils.StringUtils.subString;
import static com.databorough.utils.StringUtils.toSbl;

public final class DSUtils
{
	private static final boolean chkAnnotations = true;

	/**
	 * The DSUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private DSUtils()
	{
		super();
	}

	/**
	 * Copies only the fields prefixed with <tt>prefix</tt> argument. The rest
	 * of the source field name must match with the destination field name.
	 * @see evalCorr
	 *
	 * @param objD Destination Object
	 * @param objS Source Object
	 * @param prefix prefix that source field must have
	 */
	public static void assignObject(Object objD, Object objS, String prefix)
	{
		if ((objD == null) || (objS == null) ||
				!isUserDefOrArr(objS.getClass()) ||
				!isUserDefOrArr(objD.getClass()))
		{
			return;
		}

		if ((objS.getClass().isArray() && objD.getClass().isArray()))
		{
			if ((Array.getLength(objS) > 0) &&
					(Array.getLength(objS) == Array.getLength(objD)))
			{
				if (isUserDefOrArr(Array.get(objS, 0).getClass()))
				{
					int len = Array.getLength(objS);

					for (int i = 0; i < len; i++)
					{
						assignObject(Array.get(objD, i), Array.get(objS, i),
							prefix);
					}
				}
				else
				{
					// if these are primitive arrays
					if (isTypeCompatible(Array.get(objD, 0).getClass(),
								Array.get(objS, 0).getClass(), false))
					{
						int len = Array.getLength(objS);

						for (int i = 0; i < len; i++)
						{
							Array.set(objD, i, Array.get(objS, i));
						}
					}
				}
			}

			return;
		}

		List<Field> srcFlds = getAllFields(objS, "");
		List<Field> destFlds = getAllFields(objD, "");

		for (Field sfld : srcFlds)
		{
			try
			{
				sfld.setAccessible(true);

				for (Field dfld : destFlds)
				{
					if (dfld.getName()
							.equalsIgnoreCase(sfld.getName()
								.substring(prefix.length())))
					{
						if (isUserDefOrArr(sfld.getType()))
						{
							dfld.setAccessible(true);

							if (isUserDefOrArr(dfld.getType()))
							{
								assignObject(dfld.get(objD), sfld.get(objS),
									prefix);
							}

							dfld.setAccessible(false);

							break;
						}
						else if (isTypeCompatible(dfld.getType(),
									sfld.getType(), true))
						{
							dfld.setAccessible(true);
							dfld.set(objD, sfld.get(objS));
							dfld.setAccessible(false);

							break;
						}
					}
				} // dfld

				sfld.setAccessible(false);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		} // sfld

		return;
	}

	public static void assignObject(Object objD, Object objS)
	{
		assignObject(objD, "", false, objS, "", false, false, "");
	}

	public static void assignObject(Object objD, Object objS,
		boolean chkAnnotations)
	{
		assignObject(objD, "", false, objS, "", false, chkAnnotations, "");
	}

	public static void assignObject(Object objD, boolean destGeneratedPrefix,
		Object objS, boolean srcGeneratedPrefix, boolean chkAnnotations)
	{
		assignObject(objD, "", destGeneratedPrefix, objS, "",
			srcGeneratedPrefix, chkAnnotations, "");
	}

	public static void assignObject(Object objD, String destPrefix,
		Object objS, String srcPrefix)
	{
		//assignObject(objD, destPrefix, false, objS, srcPrefix, false, true, "");
		boolean anno = chkAnnotations && !"pi".equals(srcPrefix);
		assignObject(objD, destPrefix, false, objS, srcPrefix, false, anno, "");
	}

	public static void assignObject(Object objD, String destPrefix,
		boolean destGeneratedPrefix, Object objS, String srcPrefix,
		boolean srcGeneratedPrefix, boolean chkAnnotations, String recFmt)
	{
		assignObject(objD, destPrefix, destGeneratedPrefix, objS, srcPrefix,
			srcGeneratedPrefix, chkAnnotations, null, recFmt);
	}

	/**
	 * Copies objS to objD if they're having (strictly, for now) compatible
	 * members.
	 * Also copies (strictly) compatible Arrays & Object Arrays.
	 * Doesn't copy primitives.
	 *
	 * @param objD - Destination object, objS - Source object
	 */
	public static void assignObject(Object objD, String destPrefix,
		boolean destGeneratedPrefix, Object objS, String srcPrefix,
		boolean srcGeneratedPrefix, boolean chkAnnotations,
		Map<String, String> renames, String recFmt)
	{
		getAssignedObject(objD, destPrefix, destGeneratedPrefix, objS,
			srcPrefix, srcGeneratedPrefix, chkAnnotations, renames, recFmt);
	}

	public static void assignObjectFields(Object dest, String destPrefix,
		List<Field> uqEDSs, Object pgmMbr, Map<String, String> ispecMap)
	{
		if ((dest == null) || (uqEDSs == null))
		{
			return;
		}

		for (Field ds : uqEDSs)
		{
			try
			{
				ds.setAccessible(true);

				Object dso = ds.get(pgmMbr);
				ds.setAccessible(false);

				String srcPrefix = "";

				if (ds.isAnnotationPresent(UnqualifiedEDS.class))
				{
					UnqualifiedEDS anno =
						ds.getAnnotation(UnqualifiedEDS.class);
					srcPrefix = anno.prefix();
				}

				assignObject(dest, destPrefix, false, dso, srcPrefix, false,
					chkAnnotations, ispecMap, "");
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}
	}

	public static void assignObjectFields(List<Field> uqEDSs, Object src,
		String srcPrefix, Object pgmMbr, Map<String, String> ispecMap)
	{
		if ((src == null) || (uqEDSs == null))
		{
			return;
		}

		for (Field ds : uqEDSs)
		{
			try
			{
				ds.setAccessible(true);

				Object dso = ds.get(pgmMbr);
				ds.setAccessible(false);

				String destPrefix = "";

				if (ds.isAnnotationPresent(UnqualifiedEDS.class))
				{
					UnqualifiedEDS anno =
						ds.getAnnotation(UnqualifiedEDS.class);
					destPrefix = anno.prefix();
				}

				assignObject(dso, destPrefix, false, src, srcPrefix, false,
					chkAnnotations, ispecMap, "");

				if (ds.getType().getSuperclass().getSimpleName()
						  .equalsIgnoreCase(src.getClass().getSimpleName()))
				{
					updateExtFlds(ds, true);
				}
				else
				{
					updateExtFlds(ds, false);
				}

				ds.set(pgmMbr, dso);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}
	}

	/**
	 * Assigns value in destination class object.
	 *
	 * @param objD destination class object
	 * @param dfld destination field
	 * @param objS source class object
	 * @param sfld source field
	 * @param chkAnnotations
	 * @return flag
	 * @since (2013-10-17.15:15:23)
	 */
	private static boolean assignValue(Object objD, Field dfld, Object objS,
		Field sfld, boolean chkAnnotations)
	{
		Class<?> dfldType = dfld.getType();
		Class<?> sfldType = sfld.getType();

		if (isUserDefOrArr(sfldType))
		{
			if (isUserDefOrArr(dfldType))
			{
				dfld.setAccessible(true);

				try
				{
					if (List.class.isAssignableFrom(sfldType) ||
							Map.class.isAssignableFrom(sfldType))
					{
						dfld.set(objD, sfld.get(objS));
					}
					else
					{
						assignObject(dfld.get(objD), sfld.get(objS),
							chkAnnotations);
					}
				}
				catch (Exception e)
				{
					logStackTrace(e);
				}

				dfld.setAccessible(false);
			}

			return true;
		}
		else if (isTypeCompatible(dfldType, sfldType, true))
		{
			setCompatibleField(dfld, objD, sfld, objS);

			return true;
		}

		return false;
	}

	/**
	 * Clears the object if it is user defined or an array or an object array.
	 * Doesn't handle primitives.
	 *
	 * @param obj
	 */
	public static void clearObject(Object obj)
	{
		// doesn't set primitive objects
		getClearedObj(obj);
	}

	public static int compareObject(Object obj1, Object obj2)
	{
		if ((obj1 == null) || (obj2 == null))
		{
			return -Integer.MIN_VALUE;
		}

		String str1 = String.valueOf(obj1);

		if (isUserDefOrArr(obj1.getClass()))
		{
			str1 = objectToString(obj1);
		}

		String str2 = String.valueOf(obj2);

		if (isUserDefOrArr(obj2.getClass()))
		{
			str2 = objectToString(obj2);
		}

		return StringUtils.compareTo(str1, str2);
	}

	public static void convertToObject(Object ds, Object ds2)
	{
		setObject(ds, objectToString(ds2), null);
	}

	/**
	 * Simply a method to copy one object to another. Only for non-primitive
	 * objects.
	 *
	 * @param tgt
	 * @param src
	 */
	public static void copyObject(Object tgt, Object src)
	{
		if ((tgt == null) || (src == null))
		{
			return;
		}

		if (!isUserDefOrArr(tgt.getClass()) && src instanceof DateEx)
		{
			if (tgt instanceof java.sql.Date)
			{
				((java.sql.Date)tgt).setTime(((DateEx)src).getTime().getTime());
			}
			else if (tgt instanceof Time)
			{
				((Time)tgt).setTime(((DateEx)src).getTime().getTime());
			}
			else if (tgt instanceof Timestamp)
			{
				((Timestamp)tgt).setTime(((DateEx)src).getTime().getTime());
			}
			else if (tgt instanceof StringBuilder)
			{
				setStr(((StringBuilder)tgt), src);
			}
		}
		else if (isUserDefOrArr(tgt.getClass()) && !tgt.getClass().isArray() &&
				!isUserDefOrArr(src.getClass()))
		{
			// if src is a primitive then set the first field of tgt object
			List<Field> tgtFlds = getAllFields(tgt, "");

			for (Field tFld : tgtFlds)
			{
				if (isTypeCompatible(tFld.getType(), src.getClass(), true))
				{
					setCompatibleField(tFld, tgt, null, src);
				}
			}
		}
		else
		{
			assignObject(tgt, src);
		}
	}

	public static boolean equalObject(Object obj1, Object obj2)
	{
		if ((obj1 != null) && (obj2 != null))
		{
			String str1 = String.valueOf(obj1);

			if (isUserDefOrArr(obj1.getClass()))
			{
				str1 = objectToString(obj1);
			}

			String str2 = String.valueOf(obj2);

			if (isUserDefOrArr(obj2.getClass()))
			{
				str2 = objectToString(obj2);
			}

			return StringUtils.equal(str1, str2);
		}

		return false;
	}

	public static Object getAssignedObject(Object objD, Object objS)
	{
		return getAssignedObject(objD, "", false, objS, "", false, false, null,
			"");
	}

	private static Object getAssignedObject(Object objD, String destPrefix,
		boolean destGeneratedPrefix, Object objS, String srcPrefix,
		boolean srcGeneratedPrefix, boolean chkAnnotations,
		Map<String, String> renames, String recFmt)
	{
		if ((objD == null) || (objS == null) ||
				!isUserDefOrArr(objS.getClass()) ||
				!isUserDefOrArr(objD.getClass()))
		{
			return objD;
		}

		Class<?> srcClass = objS.getClass();
		Class<?> destClass = objD.getClass();

		if ((srcClass.isArray() && destClass.isArray()))
		{
			int lenS = Array.getLength(objS);
			int lenD = Array.getLength(objD);
			int startSrc = 0;
			int startDest = 0;

			if (Math.abs(lenS - lenD) > 1)
			{
				return objD;
			}

			if (lenS > lenD)
			{
				startSrc = 1;
				lenS--;
			}
			else if (lenS < lenD)
			{
				startDest = 1;
				lenD--;
			}

			Class<?> srcType = srcClass.getComponentType();
			Class<?> destType = destClass.getComponentType();

			if ((lenS > 0) && isTypeCompatible(destType, srcType, true))
			{
				if (isUserDefOrArr(srcType))
				{
					// if these are object arrays
					for (int i = 0; i < lenS; i++)
					{
						assignObject(Array.get(objD, i + startDest),
							Array.get(objS, i + startSrc), chkAnnotations);
					}
				}
				else if (!isUserDefOrArr(srcType, true) &&
						!isUserDefOrArr(destType, true))
				{
					// if these are pure-primitive arrays
					for (int i = startSrc; i < lenS; i++)
					{
						Array.set(objD, i + startDest,
							Array.get(objS, i + startSrc));
					}
				}
				else
				{
					for (int i = startSrc; i < lenS; i++)
					{
						Object src =
							getCompatibleVal(Array.get(objD, i),
								Array.get(objS, i + startSrc));

						if (src != null)
						{
							Array.set(objD, i + startDest, src);
						}
					}
				}
			}

			return objD;
		} // if ((srcClass.isArray() && destClass.isArray()))

		// Looking for prefix annotation/__prefix field in the src & dest
		// classes & if found, overwriting their prefixValues passed in params
		UnqualifiedEDS edsAnno = srcClass.getAnnotation(UnqualifiedEDS.class);

		if (edsAnno != null)
		{
			String annoPrefix = edsAnno.prefix();

			if (annoPrefix.length() > 0)
			{
				srcPrefix = annoPrefix;
			}
		}

		edsAnno = destClass.getAnnotation(UnqualifiedEDS.class);

		if (edsAnno != null)
		{
			String annoPrefix = edsAnno.prefix();

			if (annoPrefix.length() > 0)
			{
				destPrefix = annoPrefix;
			}
		}

		LinkedHashMap<String, Column> srcColFldMap =
			new LinkedHashMap<String, Column>();
		LinkedHashMap<String, Column> destColFldMap =
			new LinkedHashMap<String, Column>();

		Map<String, Field> srcFldMap = getAllFieldsMap(objS, "");
		Map<String, Field> destFldMap = getAllFieldsMap(objD, "");

		List<Field> srcFlds;
		List<Field> destFlds;

		if (chkAnnotations)
		{
			srcFlds = getFld2ColumnAnnoMap(srcClass, srcColFldMap, "");
			destFlds = getFld2ColumnAnnoMap(destClass, destColFldMap, recFmt);
		}
		else
		{
			srcFlds = new ArrayList<Field>(srcFldMap.values());
			destFlds = new ArrayList<Field>(destFldMap.values());
		}

		boolean readColAnno = chkAnnotations && (srcColFldMap.size() != 0);
		boolean writeColAnno = chkAnnotations && (destColFldMap.size() != 0);

		boolean srcIsEntity =
			(renames != null) && srcClass.isAnnotationPresent(Entity.class);
		boolean destIsEntity =
			(renames != null) && destClass.isAnnotationPresent(Entity.class);

		if (!srcIsEntity)
		{
			for (int i = 0; i < srcFlds.size(); i++)
			{
				Field sfld = srcFlds.get(i);

				if (sfld.isAnnotationPresent(ExtFld.class))
				{
					ExtFld extf = sfld.getAnnotation(ExtFld.class);
					Field refFld = getFldByName(extf.refFldName(), srcFlds);

					if (refFld != null)
					{
						srcFlds.remove(refFld);
					}
				}
			}
		}

		for (Field sfld : srcFlds)
		{
			sfld.setAccessible(true);

			String srcFldName = sfld.getName().toUpperCase();

			if (readColAnno)
			{
				if (!sfld.isAnnotationPresent(ExtFld.class))
				{
					Column fn = srcColFldMap.get(srcFldName);

					if (fn != null)
					{
						srcFldName = fn.name();
					}
				}

				if (srcIsEntity)
				{
					String newName = renames.get(srcFldName);

					if (newName != null)
					{
						srcFldName = newName;
					}
				}
			}

			if (!srcGeneratedPrefix)
			{
				srcFldName = srcPrefix + srcFldName;
			}

			for (Field dfld : destFlds)
			{
				String destFldName = dfld.getName().toUpperCase();

				if (writeColAnno)
				{
					if (!dfld.isAnnotationPresent(ExtFld.class))
					{
						Column fn = destColFldMap.get(destFldName);

						if (fn != null)
						{
							destFldName = fn.name();
						}
					}

					if (destIsEntity)
					{
						String newName = renames.get(destFldName);

						if (newName != null)
						{
							destFldName = newName;
						}
					}
				}

				if (!destGeneratedPrefix)
				{
					destFldName = destPrefix + destFldName;
				}

				if (srcFldName.equalsIgnoreCase(destFldName))
				{
					if (assignValue(objD, dfld, objS, sfld, chkAnnotations))
					{
						break;
					}
				}
			} // dfld

			sfld.setAccessible(false);
		} // sfld

		return objD;
	}

	private static Object getClearedObj(Object obj)
	{
		if ((obj == null) || !isUserDefOrArr(obj.getClass()))
		{
			return obj;
		}

		if (obj.getClass().isArray())
		{
			int len = Array.getLength(obj);

			if (len == 0)
			{
				return obj;
			}

			Object arr = Array.get(obj, 0);

			if (arr == null)
			{
				return obj;
			}

			Class<?> arrType = arr.getClass();

			// if it's an object array
			if (isUserDefOrArr(arrType))
			{
				for (int i = 0; i < len; i++)
				{
					Object element = Array.get(obj, i);

					if (element != null)
					{
						clearObject(element);
					}
					else
					{
						try
						{
							element = arrType.getConstructor().newInstance();
						}
						catch (Exception e)
						{
							logStackTrace(e);
						}

						Array.set(obj, i, element);
					}
				}
			}
			else if (!isUserDefOrArr(arrType, true))
			{
				// if it's a pure primitive array
				Object rval = getPrimResetVal(arrType);

				for (int i = 0; i < len; i++)
				{
					Array.set(obj, i, rval);
				}
			}
			else
			{
				for (int i = 0; i < len; i++)
				{
					Object element = Array.get(obj, i);

					if (element != null)
					{
						reset(Array.get(obj, i));
					}
					else
					{
						element = getPrimResetVal(arrType);
						Array.set(obj, i, element);
					}
				}
			}

			return obj;
		} // if (obj.getClass().isArray())

		// if it's a complete instance of a class
		List<Field> lstFlds = getAllFields(obj, "");

		for (Field fld : lstFlds)
		{
			try
			{
				fld.setAccessible(true);

				Class<?> fldType = fld.getType();
				Object fldVal = fld.get(obj);

				if (isUserDefOrArr(fldType))
				{
					clearObject(fldVal);
				}
				else if (!isUserDefOrArr(fldType, true))
				{
					if ((
							fldVal instanceof String &&
							"QIN".equalsIgnoreCase(fld.getName())
						) || "INDICATORS".equalsIgnoreCase(fld.getName()))
					{
						String qin = (String)fldVal;
						fld.set(obj, all("0", qin.length()));
					}
					else
					{
						fld.set(obj, getPrimResetVal(fldType));
					}
				}
				else if ("QIN".equalsIgnoreCase(fld.getName()) ||
						"INDICATORS".equalsIgnoreCase(fld.getName()))
				{
					resetQin(fldVal);
				}
				else
				{
					reset(fldVal);
				}

				fld.setAccessible(false);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}

		return obj;
	}

	private static String getField2Str(Field fld, int length, int precision,
		int scale, Object src)
	{
		if (src == null)
		{
			return "";
		}

		Object fldVal = null;
		Class<?> fldType = src.getClass();

		if (fld != null)
		{
			fldType = fld.getType();
		}

		if (fld != null)
		{
			fld.setAccessible(true);

			try
			{
				fldVal = fld.get(src);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}

			fld.setAccessible(false);
		}
		else
		{
			fldVal = src;
		}

		if (isUserDefOrArr(fldType))
		{
			if (fldType.isArray())
			{
				int len = Array.getLength(fldVal);
				StringBuilder flatStr = new StringBuilder();

				if (len == 0)
				{
					return flatStr.toString();
				}

				Class<?> arrType = Array.get(fldVal, 0).getClass();

				// if it's an object array
				if (isUserDefOrArr(arrType))
				{
					for (int i = 1; i < len; i++)
					{
						Object element = Array.get(fldVal, i);

						if (element != null)
						{
							flatStr.append(objectToString(element));
						}
					}
				}
				else
				{
					for (int i = 1; i < len; i++)
					{
						Object element = Array.get(fldVal, i);

						if (element != null)
						{
							flatStr.append(getField2Str(null, length,
									precision, scale, element));
						}
					}
				}

				return flatStr.toString();
			}
			else
			{
				return objectToString(fldVal);
			}
		} // if (isUserDefOrArr(fldType))

		String fldStr = String.valueOf(fldVal);
		String fmt = "";

		//if (fldVal instanceof Number)
		if (fldVal instanceof Number && (precision != 0))
		{
			length = precision;
		}

		boolean remDot = false;

		if (fldVal instanceof Float ||
				((fld != null) && fld.isAnnotationPresent(rpgFloat.class)))
		{
			if (precision > 4)
			{
				length = 23;
			}
			else
			{
				length = 14;
			}

			scale = length - 1;
			fmt = "%1$" + length + "." + scale + "e";
			remDot = true;
		}
		else if (fldVal instanceof Integer || fldVal instanceof Long)
		{
			fmt = "%1$" + length + "d";
		}
		else if (fldVal instanceof Double)
		{
			length++;
			fmt = "%1$" + length + "." + scale + "f";
			remDot = true;
		}
		else
		{
			fmt = "%1$-" + length + "s";
		}

		if (fldVal != null)
		{
			fldStr = String.format(Locale.US, fmt, fldVal);

			if (remDot)
			{
				fldStr = fldStr.replace(".", "");
				length--;
			}
		}
		else
		{
			fldStr = all(" ", length);
		}

		if (fldStr.length() > length)
		{
			int xtraLen = fldStr.length() - length;

			if (fldVal instanceof Number)
			{
				fldStr = fldStr.substring(xtraLen);
			}
			else
			{
				fldStr = fldStr.substring(0, fldStr.length() - xtraLen);
			}
		}

		return fldStr;
	}

	public static Object getObjectHivaled(Object src)
	{
		return getObjectHivaled(src, false);
	}

	private static Object getObjectHivaled(Object src, boolean isLoval)
	{
		if (src == null)
		{
			return src;
		}

		updateExtFlds(src, true);

		LinkedHashMap<String, Column> srcColFldMap =
			new LinkedHashMap<String, Column>();
		List<Field> srcFlds =
			getFld2ColumnAnnoMap(src.getClass(), srcColFldMap, "");

		for (int i = 0; i < srcFlds.size(); i++)
		{
			Field fld = srcFlds.get(i);
			Column col;

			if (fld.isAnnotationPresent(Column.class))
			{
				col = fld.getAnnotation(Column.class);
			}
			else
			{
				col = srcColFldMap.get(fld.getName().toUpperCase());
			}

			if (col == null)
			{
				continue;
			}

			int length = col.length();
			int precision = col.precision();
			int scale = col.scale();

			try
			{
				Object fldVal = fld.get(src);

				if (fldVal instanceof Number)
				{
					length = precision;
				}

				if (fldVal instanceof Float)
				{
					fldVal = isLoval ? loFloat(length) : hiFloat(length);
				}
				else if (fld.isAnnotationPresent(rpgFloat.class))
				{
					fldVal = isLoval ? loDFloat(length) : hiDFloat(length);
				}
				else if (fldVal instanceof Double)
				{
					fldVal = isLoval ? loDouble(length, scale)
									 : hiDouble(length, scale);
				}
				else if (fldVal instanceof Long)
				{
					fldVal = isLoval ? loLong(length) : hiLong(length);
				}
				else if (fldVal instanceof Integer)
				{
					fldVal = isLoval ? loInt(length) : hiInt(length);
				}
				else if (fldVal instanceof DateEx)
				{
					DateEx dtx = (DateEx)fldVal;
					dtx.setDateTime(isLoval ? loDate() : hiDate());
					fldVal = dtx;
				}
				else if (fldVal instanceof Timestamp)
				{
					fldVal = isLoval ? loTimestamp() : hiTimestamp();
				}
				else if (fldVal instanceof Date)
				{
					fldVal = isLoval ? loDate() : hiDate();
				}
				else
				{
					fldVal = isLoval ? loChar(length) : hiChar(length);
				}

				fld.set(src, fldVal);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}

		updateExtFlds(src, false);

		return src;
	}

	public static Object getObjectLovaled(Object src)
	{
		return getObjectHivaled(src, true);
	}

	private static Object getStr2Fld(Object fldVal, Class<?> fldType,
		String fldStr, Object declaringMbr, int length, int scale)
	{
		if ((fldType == null) && (fldVal == null))
		{
			return null;
		}

		if (fldStr == null)
		{
			return fldVal;
		}

		if ((fldVal == null) && (fldType != null))
		{
			if (fldType.isArray())
			{
				fldVal = isUserDefOrArr(fldType)
					? initArray(fldType, length, declaringMbr)
					: initArray(fldType, length);
			}
			else
			{
				Class<?> consParams[] = {  };
				Object consDeclParams[] = {  };

				if (isUserDefOrArr(fldType))
				{
					consParams = new Class[] { fldType.getSuperclass() };

					try
					{
						if (declaringMbr == null)
						{
							Constructor<?> declCons =
								fldType.getDeclaredConstructor();
							declCons.setAccessible(true);
							consDeclParams = new Object[]
								{
									declCons.newInstance()
								};
							declCons.setAccessible(false);
						}
						else
						{
							consDeclParams = new Object[] { declaringMbr };
						}
					}
					catch (Exception e)
					{
						logStackTrace(e);
					}
				}

				try
				{
					Constructor<?> fldCons =
						fldType.getDeclaredConstructor(consParams);
					fldCons.setAccessible(true);
					fldVal = fldCons.newInstance(consDeclParams);
					fldCons.setAccessible(false);
				}
				catch (Exception e)
				{
					logStackTrace(e);
				}
			}
		}

		if ((fldType == null) && (fldVal != null))
		{
			fldType = fldVal.getClass();
		}

		if (isUserDefOrArr(fldType))
		{
			if (fldType.isArray())
			{
				int len = Array.getLength(fldVal);
				Class<?> elementType = fldType.getComponentType();
				boolean prim = !isUserDefOrArr(elementType);
				int elmLen = length / len;

				for (int i = 1; i < len; i++)
				{
					Object srcElem = Array.get(fldVal, i);
					String elemStr =
						subString(fldStr, ((i - 1) * elmLen) + 1, elmLen);

					if (prim)
					{
						// primitive array
						Array.set(fldVal, i,
							getStr2Fld(srcElem, elementType, elemStr,
								declaringMbr, elmLen, scale));
					}
					else
					{
						// Object array
						Array.set(fldVal, i,
							toObject(elementType, elemStr, declaringMbr));
					}
				}
			}
			else
			{
				// Object class
				fldVal = toObject(fldType, fldStr, declaringMbr);
			}

			return fldVal;
		}

		if (scale > 0)
		{
			fldStr = subString(fldStr, 1, length - scale) + '.' +
				subString(fldStr, (length - scale) + 1);
		}

		if (fldType.isAssignableFrom(Float.class))
		{
			// Object class
			fldVal = toFloat(fldStr);
		}
		else if (fldType.isAssignableFrom(Double.class))
		{
			fldVal = toDouble(fldStr);
		}
		else if (fldType.isAssignableFrom(Long.class))
		{
			fldVal = toLong(fldStr);
		}
		else if (fldType.isAssignableFrom(Integer.class))
		{
			fldVal = toInt(fldStr);
		}
		else if (fldType.isAssignableFrom(String.class))
		{
			fldVal = fldStr;
		}
		else if (fldType.isAssignableFrom(StringBuilder.class))
		{
			fldVal = toSbl(fldStr);
		}
		else if (fldType.isAssignableFrom(Timestamp.class))
		{
			Date dt = toDateTime(fldStr, "", false, 'Z');
			Timestamp ts = (Timestamp)fldVal;

			if ((ts != null) && (dt != null))
			{
				ts.setTime(dt.getTime());
			}

			fldVal = ts;
		}
		else if (fldType.isAssignableFrom(Time.class))
		{
			Date dt = toDateTime(fldStr, "", false, 'T');
			Time t = (Time)fldVal;

			if ((t != null) && (dt != null))
			{
				t.setTime(dt.getTime());
			}

			fldVal = t;
		}
		else if (fldType.isAssignableFrom(Date.class))
		{
			fldVal = toDateTime(fldStr, "", false, 'D');
		}

		if (fldType.isAssignableFrom(DateEx.class))
		{
			DateEx fldDat = (DateEx)fldVal;
			char tc = fldDat.getTypeCode();
			fldDat.setDateTime(toDateTime(fldStr, "", false, tc));
			fldVal = fldDat;
		}

		return fldVal;
	}

	/**
	 * Returns false if any of the object field value is other than default
	 * value. This function is specially designed to deal with those UI beans
	 * which don't have any selector field in grid screen.
	 *
	 * @param obj
	 * @return <code>true</code> if object is empty
	 * @since (2012-07-31.16:04:36)
	 */
	public static boolean isObjectEmpty(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		List<Field> lstFlds = getAllFields(obj, "");

		for (Field fld : lstFlds)
		{
			try
			{
				fld.setAccessible(true);

				Class<?> fldType = fld.getType();
				Object fldVal = fld.get(obj);

				if ((fldVal == null) || "QIN".equalsIgnoreCase(fld.getName()) ||
						"INDICATORS".equalsIgnoreCase(fld.getName()) ||
						isUserDefOrArr(fldType, true))
				{
					continue;
				}

				if (!ReflectionUtils.equals(fldVal, getPrimResetVal(fldType)))
				{
					return false;
				}

				fld.setAccessible(false);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		} // fld

		return true;
	}

	public static String objectToString(Object src)
	{
		if (src == null)
		{
			return "";
		}

		if (src instanceof CharSequence || !isUserDefOrArr(src.getClass()))
		{
			return String.valueOf(src);
		}

		StringBuilder chunkStr = new StringBuilder();
		updateExtFlds(src, false);

		LinkedHashMap<String, Column> srcColFldMap =
			new LinkedHashMap<String, Column>();
		List<Field> srcFlds =
			getFld2ColumnAnnoMap(src.getClass(), srcColFldMap, "");
		List<String> refFlds = new ArrayList<String>();

		for (int i = 0; i < srcFlds.size(); i++)
		{
			Field fld = srcFlds.get(i);
			Column col;
			int length;
			int precision;
			int scale;

			if (fld.isAnnotationPresent(ExtFld.class))
			{
				ExtFld ext = fld.getAnnotation(ExtFld.class);
				length = ext.length();
				precision = ext.precision();
				scale = ext.scale();
				refFlds.add(ext.refFldName().toUpperCase());
			}
			else
			{
				if (fld.isAnnotationPresent(Column.class))
				{
					col = fld.getAnnotation(Column.class);
				}
				else
				{
					col = srcColFldMap.get(fld.getName().toUpperCase());
				}

				if ((col == null) || refFlds.contains(col.name().toUpperCase()))
				{
					continue;
				}

				length = col.length();
				precision = col.precision();
				scale = col.scale();
			}

			chunkStr.append(getField2Str(fld, length, precision, scale, src));
		}

		return chunkStr.toString();
	}

	public static void setObject(Object ds, Object chunk)
	{
		setObject(ds, String.valueOf(chunk));
	}

	public static void setObject(Object ds, String chunkStr)
	{
		setObject(ds, chunkStr, null);
	}

	private static void setObject(Object ds, String chunkStr,
		Object declaringMbr)
	{
		if (ds == null)
		{
			return;
		}

		LinkedHashMap<String, Column> srcColFldMap =
			new LinkedHashMap<String, Column>();
		Class<?> destClass = ds.getClass();
		List<Field> destFlds =
			getFld2ColumnAnnoMap(destClass, srcColFldMap, "");
		List<String> refFlds = new ArrayList<String>();
		int fstart = 0;

		for (int i = 0; i < destFlds.size(); i++)
		{
			Field fld = destFlds.get(i);
			Column col;
			int length;
			int precision = 0;
			int scale;

			if (fld.isAnnotationPresent(ExtFld.class))
			{
				ExtFld ext = fld.getAnnotation(ExtFld.class);
				length = ext.length();
				precision = ext.precision();
				scale = ext.scale();
				refFlds.add(ext.refFldName().toUpperCase());
			}
			else
			{
				if (fld.isAnnotationPresent(Column.class))
				{
					col = fld.getAnnotation(Column.class);
				}
				else
				{
					col = srcColFldMap.get(fld.getName().toUpperCase());
				}

				if ((col == null) || refFlds.contains(col.name().toUpperCase()))
				{
					continue;
				}

				length = col.length();
				precision = col.precision();
				scale = col.scale();
			}

			try
			{
				fld.setAccessible(true);

				Class<?> fldType = fld.getType();
				Object fldVal = fld.get(ds);

				if (Number.class.isAssignableFrom(fldType))
				{
					length = precision;
				}

				// for '.' operator
				if (fldType.isAssignableFrom(Float.class) ||
						fld.isAnnotationPresent(rpgFloat.class))
				{
					if (precision > 4)
					{
						length = 23;
					}
					else
					{
						length = 14;
					}
				}

				if (fldType.isArray())
				{
					length *= Array.getLength(fldVal);
				}

				boolean exitAfterAssign = false;

				if ((fstart + length) > chunkStr.length())
				{
					exitAfterAssign = true;
				}

				String fldStr = subString(chunkStr, fstart + 1, length);
				fstart += length;
				fldVal = getStr2Fld(fldVal, fldType, fldStr, declaringMbr,
						length, scale);
				fld.set(ds, fldVal);
				fld.setAccessible(false);

				if (exitAfterAssign)
				{
					break;
				}
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}

			updateExtFlds(ds, true);
		}
	}

	public static void setObjectHival(Object src)
	{
		getObjectHivaled(src, false);
	}

	public static void setObjectLoval(Object src)
	{
		getObjectHivaled(src, true);
	}

	public static <T> T toObject(Class<T> destClass, Object chunkStr)
	{
		return toObject(destClass, String.valueOf(chunkStr));
	}

	public static <T> T toObject(Class<T> destClass, String chunkStr)
	{
		return toObject(destClass, chunkStr, null);
	}

	public static <T> T toObject(Class<T> destClass, Object chunkStr,
		Object declaringMbr)
	{
		return toObject(destClass, String.valueOf(chunkStr), declaringMbr);
	}

	public static <T> T toObject(Class<T> destClass, String chunkStr,
		Object declaringMbr)
	{
		if ((destClass == null) || (chunkStr == null))
		{
			return null;
		}

		T ds = null;

		try
		{
			Constructor<T> cons = null;

			try
			{
				cons = destClass.getDeclaredConstructor();
				cons.setAccessible(true);
				ds = cons.newInstance();
			}
			catch (Exception e)
			{
				Class<?> declCls  = destClass.getDeclaringClass();

				if (declCls == null)
				{
					return ds;
				}

				cons = destClass.getDeclaredConstructor(declCls);
				cons.setAccessible(true);

				if (declaringMbr == null)
				{
					Constructor<?> declCons = declCls.getDeclaredConstructor();
					declCons.setAccessible(true);
					declaringMbr = declCons.newInstance();
					declCons.setAccessible(false);
				}

				ds = cons.newInstance(declaringMbr);
			}
			finally
			{
				if (cons != null)
				{
					cons.setAccessible(false);
				}
			}

			setObject(ds, chunkStr, declaringMbr);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return ds;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toObject(Object src[], Class<T> destClass)
	{
		if (src == null)
		{
			return null;
		}

		T dest[] = (T[])Array.newInstance(destClass, src.length);

		for (int i = 0; i < src.length; i++)
		{
			dest[i] = toObject(src[i], destClass);
		}

		return dest;
	}

	public static <T> T toObject(Object src, Class<T> destClass)
	{
		return toObject(destClass, objectToString(src));
	}

	public static String toString(Object obj)
	{
		if (obj == null)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();

		Field fld[] = obj.getClass().getFields();
		int numFields = fld.length;

		for (int i = 0; i < numFields; i++)
		{
			if (i != 0)
			{
				sb.append(",");
			}

			try
			{
				fld[i].setAccessible(true);
				sb.append(fld[i].getName()).append(":");
				sb.append("<").append(fld[i].get(obj)).append(">");
			}
			catch (Exception e)
			{
			}
		}

		return sb.toString();
	}

	public static void updLastIO(Object currMbr, Object extPgmMbr)
	{
		if ((currMbr == null) || (extPgmMbr == null))
		{
			return;
		}

		Field lioCurr = null;
		Field lioExtPgm = null;

		try
		{
			lioCurr = currMbr.getClass().getDeclaredField("lastIO");
			lioExtPgm = extPgmMbr.getClass().getDeclaredField("lastIO");
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if ((lioCurr == null) || (lioExtPgm == null))
		{
			return;
		}

		try
		{
			assignObject(lioCurr.get(currMbr), lioExtPgm.get(extPgmMbr));
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}
}
