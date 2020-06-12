package com.databorough.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;

import acdemxaMvcprocess.data.Recfmt;

import acdemxaMvcprocess.logic.data.ExtFld;
import acdemxaMvcprocess.logic.data.UnqualifiedEDS;

import com.databorough.utils.DateEx;
import static com.databorough.utils.DateTimeConverter.getRpgDateFormat;
import static com.databorough.utils.DateTimeConverter.timeStampFormat;
import static com.databorough.utils.DateTimeConverter.toDateTime;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.StringUtils.compareTo;
import static com.databorough.utils.StringUtils.equal;
import static com.databorough.utils.StringUtils.rtrim;
import static com.databorough.utils.StringUtils.toLowerCase;
import static com.databorough.utils.Utils.lookupStrInStrArr;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Utility class for reflection mechanism of the Java Core Reflection API.
 * <p>
 * Used for initializing a complete class, array or a resource bundle.
 *
 * @author Amit Arya
 * @since (2002-08-24.14:39:03)
 */
public final class ReflectionUtils
{
	private static String equivalentTypes[][] =
		{
			{ "String", "StringBuilder", "Date", "Time", "Timestamp", "DateEx" }
		};
	private static String varTypes[][] =
		{
			{
				"Double", "Float", "Long", "Integer", "Short", "Byte",
				"Character"
			},
			{ "double", "float", "long", "int", "short", "byte", "char" }
		};
	private static final HashMap<Class<?>, Object> class2ResetVal =
		new HashMap<Class<?>, Object>();

	static
	{
		class2ResetVal.put(java.lang.Boolean.TYPE, false);
		class2ResetVal.put(java.lang.Byte.TYPE, 0);
		class2ResetVal.put(java.lang.Character.TYPE, '\u0000');
		class2ResetVal.put(java.lang.Double.TYPE, 0.0);
		class2ResetVal.put(java.lang.Float.TYPE, 0.0);
		class2ResetVal.put(java.lang.Integer.TYPE, 0);
		class2ResetVal.put(java.lang.Long.TYPE, 0L);
		class2ResetVal.put(java.lang.Short.TYPE, 0);
		class2ResetVal.put(java.lang.Void.TYPE, null);

		class2ResetVal.put(java.lang.Boolean.class, false);
		class2ResetVal.put(java.lang.Byte.class, 0);
		class2ResetVal.put(java.lang.Character.class, '\u0000');
		class2ResetVal.put(java.lang.Double.class, 0.0);
		class2ResetVal.put(java.lang.Float.class, 0.0);
		class2ResetVal.put(java.lang.Integer.class, 0);
		class2ResetVal.put(java.lang.Long.class, 0L);
		class2ResetVal.put(java.lang.Short.class, 0);

		class2ResetVal.put(java.lang.String.class, "");
		class2ResetVal.put(java.lang.StringBuilder.class, new StringBuilder());
		class2ResetVal.put(com.databorough.utils.DateEx.class, new DateEx());
		class2ResetVal.put(java.util.Date.class, new Date());
		class2ResetVal.put(java.sql.Date.class, new java.sql.Date(0));
		class2ResetVal.put(java.sql.Time.class, new Time(0));
		class2ResetVal.put(java.sql.Timestamp.class, new Timestamp(0));

		class2ResetVal.put(java.math.BigDecimal.class, BigDecimal.ZERO);
	}

	public static final HashMap<Class<?>, String> class2Name =
		new HashMap<Class<?>, String>();

	static
	{
		class2Name.put(java.lang.Boolean.TYPE, "boolean");
		class2Name.put(java.lang.Byte.TYPE, "byte");
		class2Name.put(java.lang.Character.TYPE, "char");
		class2Name.put(java.lang.Double.TYPE, "double");
		class2Name.put(java.lang.Float.TYPE, "float");
		class2Name.put(java.lang.Integer.TYPE, "int");
		class2Name.put(java.lang.Long.TYPE, "long");
		class2Name.put(java.lang.Short.TYPE, "short");
		class2Name.put(java.lang.Void.TYPE, "void");

		class2Name.put(java.lang.Boolean.class, "java.lang.Boolean");
		class2Name.put(java.lang.Byte.class, "java.lang.Byte");
		class2Name.put(java.lang.Character.class, "java.lang.Character");
		class2Name.put(java.lang.Double.class, "java.lang.Double");
		class2Name.put(java.lang.Float.class, "java.lang.Float");
		class2Name.put(java.lang.Integer.class, "java.lang.Integer");
		class2Name.put(java.lang.Long.class, "java.lang.Long");
		class2Name.put(java.lang.Short.class, "java.lang.Short");
		class2Name.put(java.lang.String.class, "java.lang.String");
		class2Name.put(java.lang.StringBuilder.class,
			"java.lang.StringBuilder");
		class2Name.put(java.util.Date.class, "java.util.Date");
		class2Name.put(java.sql.Date.class, "java.sql.Date");
		class2Name.put(java.sql.Time.class, "java.sql.Time");
		class2Name.put(java.sql.Timestamp.class, "java.sql.Timestamp");

		class2Name.put(null, "void");

		class2Name.put(java.math.BigDecimal.class, "java.math.BigDecimal");
	}

	/**
	 * Default constructor.
	 * <p>
	 * The ReflectionUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private ReflectionUtils()
	{
		super();
	}

	/**
	 * Returns the value of field, which is a public member field of the class
	 * or interface.
	 *
	 * @param fieldName name of the field whose value is to be extracted
	 * @param instance object from which the represented field's value is to be
	 *        extracted
	 * @return the value of the represented field in object obj; primitive
	 *         values are wrapped in an appropriate object before being returned
	 * @throws NoSuchFieldException occurs if the class doesn't have a field
	 *            of a specified name
	 * @throws IllegalAccessException thrown when an application tries to
	 *            reflectively create an instance (other than an array), set or
	 *            get a field, or invoke a method, but the currently executing
	 *            method does not have access to the definition of the specified
	 *            class, field, method or constructor
	 * @since (2005-11-17.11:45:26)
	 */
	public static Object accessPublicField(String fieldName, Object instance)
		throws NoSuchFieldException, IllegalAccessException
	{
		if ((fieldName == null) || (instance == null))
		{
			return null;
		}

		try
		{
			Class<?> cls =
				(instance instanceof Class) ? (Class<?>)instance
											: instance.getClass();
			Field field = cls.getField(fieldName);

			return field.get(instance);
		}
		catch (NoSuchFieldException e)
		{
			throw e;
		}
		catch (IllegalAccessException e)
		{
			logStackTrace(e);
			throw e;
		}
	}

	/**
	 * Returns the method object with the given arguments, in the specified
	 * instance of the class.
	 *
	 * @param methodName name of the method whose object is to be extracted
	 * @param instance Object of the class from which the method object is to be
	 *        extracted
	 * @param argTypes array containing argument types of the method
	 * @param args array containing arguments on which the method is invoked
	 * @return the result of dispatching the method represented by this object
	 *         on object with parameters arguments
	 * @throws NoSuchMethodException occurs when a particular method cannot
	 *            be found.
	 * @throws IllegalAccessException thrown when an application tries to
	 *            reflectively create an instance (other than an array), set or
	 *            get a field, or invoke a method, but the currently executing
	 *            method does not have access to the definition of the specified
	 *            class, field, method or constructor
	 * @throws InvocationTargetException is a checked exception that wraps an
	 *            exception thrown by an invoked method or constructor
	 * @since (2003-10-13.11:07:22)
	 */
	public static Object callMethod(String methodName, Object instance,
		Class<?> argTypes[], Object args[])
		throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException
	{
		if ((methodName == null) || (instance == null))
		{
			return null;
		}

		try
		{
			Class<?> cls =
				(instance instanceof Class) ? (Class<?>)instance
											: instance.getClass();
			Method method = cls.getMethod(methodName, argTypes);

			return method.invoke(instance, args);
		}
		catch (NoSuchMethodException e)
		{
			throw e;
		}
		catch (IllegalAccessException e)
		{
			logStackTrace(e);
			throw e;
		}
		catch (InvocationTargetException e)
		{
			logStackTrace(e);
			throw e;
		}
	}

	public static int compCompatibleVal(Object dest, Object src)
	{
		if ((dest == null) && (src != null))
		{
			return 1;
		}

		if ((dest != null) && (src == null))
		{
			return -1;
		}

		if ((dest == null) && (src == null))
		{
			return 0;
		}

		dest = getSimplifiedVal(dest);
		src = getSimplifiedVal(src);

		if (src instanceof Number && dest instanceof Number)
		{
			Double diff =
				((Number)src).doubleValue() - ((Number)dest).doubleValue();

			return (diff == 0) ? 0 : ((diff > 0) ? 1 : (-1));
		}
		else if (src instanceof Date && dest instanceof Date)
		{
			Date sd = (Date)src;
			Date dd = (Date)dest;

			return (sd.getTime() == dd.getTime()) ? 0 : (
				sd.after(dd) ? 1 : (-1)
			);
		}
		else
		{
			return compareTo(src, dest);
		}
	}

	public static int compKeysArrays(Object srcKeyFldVals[],
		Object destKeyFldVals[], char sortOrder[])
	{
		if ((destKeyFldVals == null) && (srcKeyFldVals != null))
		{
			return 1;
		}

		if ((destKeyFldVals != null) && (srcKeyFldVals == null))
		{
			return -1;
		}

		if ((destKeyFldVals == null) && (srcKeyFldVals == null))
		{
			return 0;
		}

		int retFlag = 1;
		int shorterLen = Math.min(srcKeyFldVals.length, destKeyFldVals.length);

		for (int i = 0; i < shorterLen; i++)
		{
			retFlag = compCompatibleVal(destKeyFldVals[i], srcKeyFldVals[i]);

			if (retFlag != 0)
			{
				if (sortOrder[i] == 'D')
				{
					retFlag = ((retFlag == -1) ? 1 : (-1));
				}

				break;
			}
		}

		if (retFlag == 0)
		{
			int diff = srcKeyFldVals.length - destKeyFldVals.length;

			if (diff != 0)
			{
				retFlag = (
						(diff > 0) ? 0 : (
							(sortOrder[shorterLen] == 'D') ? 1 : (-1)
						)
					);
			}
		}

		return retFlag;
	}

	/**
	 * <p>This method uses reflection to determine if the two
	 * <code>Object</code>s containing member variables are equal.
	 * </p>
	 *
	 * @param obj1 <code>this</code> object
	 * @param obj2 the other object
	 * @return <code>true</code> if the two Objects have tested equals.
	 */
	public static boolean equals(Object obj1, Object obj2)
	{
		if (obj1 == obj2)
		{
			return true;
		}

		if (obj1 == null)
		{
			return false;
		}

		if (obj2 == null)
		{
			return false;
		}

		Class<?> obj1Class = obj1.getClass();
		Class<?> obj2Class = obj2.getClass();

		if (obj1Class != obj2Class)
		{
			return false;
		}

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		HashCodeBuilder obj1HashCodeBuilder = new HashCodeBuilder(17, 37);
		HashCodeBuilder obj2HashCodeBuilder = new HashCodeBuilder(17, 37);

		try
		{
			Field obj1Flds[] = obj1Class.getDeclaredFields();

			for (Field obj1Fld : obj1Flds)
			{
				obj1Fld.setAccessible(true);

				Field obj2Fld = obj2Class.getDeclaredField(obj1Fld.getName());
				obj2Fld.setAccessible(true);

				if (Modifier.isStatic(obj1Fld.getModifiers()))
				{
					continue;
				}

				Object data1 = obj1Fld.get(obj1);
				Object data2 = obj2Fld.get(obj2);

				data1 = ((data1 != null) && data1 instanceof String)
					? ((String)data1).trim() : data1;
				data2 = ((data2 != null) && data2 instanceof String)
					? ((String)data2).trim() : data2;

				equalsBuilder.append(data1, data2);
				logMessage("Equal " + equalsBuilder.isEquals() + " upto " +
					obj1Fld.getName());
				obj1HashCodeBuilder.append(data1);
				obj2HashCodeBuilder.append(data2);
			}
		}
		catch (Exception e)
		{
		}

		return (equalsBuilder.isEquals() &&
		(obj1HashCodeBuilder.toHashCode() == obj2HashCodeBuilder.toHashCode()));
	}

	/*
	 * Compare two objects, if they are equal then return true else false.
	 *
	 * @param obj1
	 * @param obj2
	 * @return true if objects are equal, otherwise false
	 */
	public static boolean equalsSuperClassFields(Object obj1, Object obj2)
	{
		if (obj1 == obj2)
		{
			return true;
		}

		if (obj1 == null)
		{
			return false;
		}

		if (obj2 == null)
		{
			return false;
		}

		Class<?> obj1Class = obj1.getClass().getSuperclass();
		Class<?> obj2Class = obj2.getClass().getSuperclass();

		if (obj1Class != obj2Class)
		{
			return false;
		}

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		HashCodeBuilder obj1HashCodeBuilder = new HashCodeBuilder(17, 37);
		HashCodeBuilder obj2HashCodeBuilder = new HashCodeBuilder(17, 37);

		try
		{
			Field obj1Flds[] = obj1Class.getDeclaredFields();

			for (Field obj1Fld : obj1Flds)
			{
				obj1Fld.setAccessible(true);

				Field obj2Fld = obj2Class.getDeclaredField(obj1Fld.getName());
				obj2Fld.setAccessible(true);

				if (Modifier.isStatic(obj1Fld.getModifiers()))
				{
					continue;
				}

				Object data1 = obj1Fld.get(obj1);
				Object data2 = obj2Fld.get(obj2);

				data1 = (
						(data1 != null) &&
						(
							data1 instanceof String ||
							data1 instanceof StringBuilder
						)
					) ? data1.toString().trim() : data1;
				data2 = (
						(data2 != null) &&
						(
							data2 instanceof String ||
							data2 instanceof StringBuilder
						)
					) ? data2.toString().trim() : data2;

				equalsBuilder.append(data1, data2);
				//logMessage("Equal " + equalsBuilder.isEquals() + " upto " +
					//obj1Fld.getName());
				obj1HashCodeBuilder.append(data1);
				obj2HashCodeBuilder.append(data2);
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return (equalsBuilder.isEquals() &&
		(obj1HashCodeBuilder.toHashCode() == obj2HashCodeBuilder.toHashCode()));
	}

	/**
	 * Returns list of all field objects.
	 *
	 * @param obj class name whose fields are to be found
	 * @param recfmtName
	 * @return list of all field objects
	 */
	public static List<Field> getAllFields(Object obj, String recfmtName)
	{
		Map<String, Field> mapFlds = getAllFieldsMap(obj, recfmtName);

		return new ArrayList<Field>(mapFlds.values());
	}

	/**
	 * Returns map of all field objects.
	 *
	 * @param obj class name whose fields are to be found
	 * @param recfmtName
	 * @return map of all field objects
	 * @since (2013-10-17.14:40:33)
	 */
	public static Map<String, Field> getAllFieldsMap(Object obj,
		String recfmtName)
	{
		Map<String, Field> mapFlds = new LinkedHashMap<String, Field>();
		Class<?> sup = obj.getClass();

		while (sup != null)
		{
			Field fa[] = sup.getDeclaredFields();

			for (Field fld : fa)
			{
				String fldName = fld.getName();
				Method gttr = getGetter(sup, fldName);
				int modifier = fld.getModifiers();

				if (!(
						Modifier.isFinal(modifier) ||
						Modifier.isStatic(modifier)
					))
				{
					if ((gttr != null) && (recfmtName.length() != 0))
					{
						Recfmt recfmt = gttr.getAnnotation(Recfmt.class);

						if ((recfmt != null) &&
								recfmt.recfname().equalsIgnoreCase(recfmtName))
						{
							mapFlds.put(fldName.toUpperCase(), fld);
						}
					}
					else
					{
						mapFlds.put(fldName.toUpperCase(), fld);
					}
				}
			}

			sup = sup.getSuperclass();
		}

		return mapFlds;
	}

	
	/**
	 * Returns the dimension for the given class.
	 * <p>
	 * For Example:<br>
	 * Arrays of classes are encoded by the VM like
	 * <code>[Ljava.lang.String</code> for Single Dimensional array and
	 * <code>[[Ljava.lang.String</code> for Double Dimensional array
	 *
	 * @param cls class name whose dimension is to be extracted
	 * @return dimension for the given class
	 * @since (2004-02-05.11:33:33)
	 */
	public static int getClassDimension(String cls)
	{
		if (cls == null)
		{
			return 0;
		}

		String clsType;

		if (cls.startsWith("class "))
		{
			clsType = cls.substring(6);
		}
		else
		{
			clsType = cls;
		}

		// Look for arrays which start with '['
		int objDim = 0;

		while (clsType.startsWith("["))
		{
			objDim++;
			clsType = clsType.substring(1);
		}

		return objDim;
	}

	/**
	 * Returns the type of the class, or the type with dimension, if specified
	 * in the parameter.
	 * <p>
	 * For Example:<br>
	 * Arrays of classes are encoded by the VM like
	 * <code>[Ljava.lang.String</code> for Single Dimensional array and
	 * <code>[[Ljava.lang.String</code> for Double Dimensional array
	 *
	 * @param cls class name whose dimension is to be extracted
	 * @param withDimension whether to return class type with dimension or not
	 * @return type of the class or the type with dimension
	 * @since (2004-02-05.11:16:12)
	 */
	public static String getClassType(String cls, boolean withDimension)
	{
		if (cls == null)
		{
			return cls;
		}

		String clsType;

		if (cls.startsWith("class "))
		{
			clsType = cls.substring(6);
		}
		else
		{
			clsType = cls;
		}

		// Look for arrays which start with '['
		String array = "";

		while (clsType.startsWith("["))
		{
			array += "[]";
			clsType = clsType.substring(1);
		}

		// If the object was an array decode the type. 
		switch (clsType.charAt(0))
		{
		case 'B':
			clsType = "byte";

			break;

		case 'C':
			clsType = "char";

			break;

		case 'D':
			clsType = "double";

			break;

		case 'F':
			clsType = "float";

			break;

		case 'I':
			clsType = "int";

			break;

		case 'J':
			clsType = "long";

			break;

		case 'L':
			// Object types begin with 'L' and end with ';'. Strip
			// both off
			clsType = clsType.substring(1, clsType.length() - 1);

			break;

		case 'S':
			clsType = "short";

			break;

		case 'Z':
			clsType = "boolean";

			break;

		default:
			break;
		}

		// Add the array dimensions
		if (withDimension)
		{
			clsType += array;
		}

		return clsType;
	}

	/**
	 * Gets Annotation to Field mapping, in addition to returning List of All
	 * field objects.
	 *
	 * @param obj - obj whose class is to be reflected for the fields
	 * @param colFldMap - map for saving Annotation to Field mapping
	 * @return
	 */
	public static List<Field> getColumnAnnoFldMap(Object obj,
		LinkedHashMap<String, Field> colFldMap)
	{
		return getColumnAnnoFldMap(obj, colFldMap, false, false);
	}

	public static List<Field> getColumnAnnoFldMap(Object obj,
		LinkedHashMap<String, Field> colFldMap, boolean forceMap,
		boolean skipExtFlds)
	{
		if (obj == null)
		{
			return null;
		}

		return getColumnAnnoFldMap(obj.getClass(), colFldMap, forceMap,
			skipExtFlds);
	}

	public static List<Field> getColumnAnnoFldMap(Class<?> cls,
		LinkedHashMap<String, Field> colFldMap, boolean forceMap,
		boolean skipExtFlds)
	{
		List<Field> lstFlds = new ArrayList<Field>();
		Class<?> sup = cls;
		boolean idClassChecked = false;
		List<String> refFlds = null;

		while (sup != null)
		{
			Field fa[] = sup.getDeclaredFields();

			for (Field fld : fa)
			{
				int modifier = fld.getModifiers();

				if (!(
						Modifier.isFinal(modifier) ||
						Modifier.isStatic(modifier)
					))
				{
					Column colAnno = null;

					if (skipExtFlds)
					{
						if (refFlds == null)
						{
							refFlds = new ArrayList<String>();
						}

						if (fld.isAnnotationPresent(ExtFld.class))
						{
							refFlds.add(fld.getAnnotation(ExtFld.class)
										   .refFldName());
						}
					}

					if (fld.isAnnotationPresent(Column.class))
					{
						colAnno = fld.getAnnotation(Column.class);
					}
					else
					{
						Method gttr = getGetter(sup, fld.getName());

						if (gttr != null)
						{
							colAnno = gttr.getAnnotation(Column.class);
						}
					}

					if (colAnno != null)
					{
						if (skipExtFlds && (refFlds != null) &&
								refFlds.contains(colAnno.name()))
						{
							continue;
						}

						if (idClassChecked)
						{
							Field tp = getFldByName(fld.getName(), lstFlds);

							if (tp != null)
							{
								colFldMap.put(colAnno.name().toUpperCase(), tp);
							}
						}
						else
						{
							colFldMap.put(colAnno.name().toUpperCase(), fld);
						}
					}
					else if (forceMap && !idClassChecked)
					{
						colFldMap.put(fld.getName().toUpperCase(), fld);
					}

					if (!idClassChecked)
					{
						lstFlds.add(fld);
					}
				}
			}

			sup = sup.getSuperclass();

			if ((sup == null) && !idClassChecked)
			{
				IdClass idc = cls.getAnnotation(IdClass.class);

				if (idc != null)
				{
					sup = idc.value();
					idClassChecked = true;
				}
			}
		}

		return lstFlds;
	}

	/**
	 * Returns the column name of the field.
	 *
	 * @param field
	 * @param cls
	 * @return column name of the field
	 * @since (2012-01-23.11:14:10)
	 */
	public static String getColumnName(String field, Class<?> cls)
	{
		String fld = null;

		try
		{
			String name = cls.getDeclaredField(field).getName();
			String getter =
				"get" + Character.toUpperCase(name.charAt(0)) +
				name.substring(1);
			Method method = cls.getDeclaredMethod(getter);

			// Get Method Annotation
			Annotation annotation = method.getAnnotation(Column.class);
			Column col = (Column)annotation;
			fld = col.name();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return fld;
	}

	public static Object getCompatibleVal(Object dest, Object src)
	{
		if ((src == null) && (dest != null))
		{
			return getPrimResetVal(dest.getClass());
		}

		if ((src != null) && (dest == null))
		{
			dest = getPrimResetVal(src.getClass());
		}

		if ((src == null) || (dest == null))
		{
			return null;
		}

		String destType = dest.getClass().getSimpleName();
		String srcType = src.getClass().getSimpleName();

		Object retObj = null;

		if (src instanceof DateEx && !(dest instanceof DateEx))
		{
			DateEx sf = (DateEx)src;

			if (sf != null)
			{
				if (dest instanceof java.sql.Date)
				{
					retObj = sf.getDate();
				}
				else if (dest instanceof Time)
				{
					retObj = sf.getTime();
				}
				else if (dest instanceof Timestamp)
				{
					retObj = sf.getTimestamp();
				}
				else
				{
					retObj = sf.getDateTime();
				}
			}
		}
		else if (dest instanceof StringBuilder)
		{
			StringBuilder df = (StringBuilder)dest;

			if (dest != src)
			{
				df.setLength(0);
				df.append(String.valueOf(src));
			}
			else if (!equal(df, src))
			{
				df = new StringBuilder(String.valueOf(src));
			}

			retObj = df;
		}
		else if (dest instanceof String)
		{
			retObj = String.valueOf(src);
		}
		else if (dest instanceof DateEx && !(src instanceof DateEx))
		{
			Date sf = null;
			DateEx df = (DateEx)dest;

			if (src instanceof Date)
			{
				sf = (Date)src;
			}
			else if (src instanceof StringBuilder || src instanceof String)
			{
				String sDtStr = String.valueOf(src);

				// if it's a String then it must be either a Time or TimeStamp
				// here. For Dates will remain of Date type in entity objects.
				if (sDtStr != null)
				{
					sf = toDateTime(sDtStr, "", false,
							(df.getJavaFormat()
								.equalsIgnoreCase(timeStampFormat)
							? 'Z' : 'T'));
				}
			}

			if (sf != null)
			{
				//df.setDateTime(sf);
				String dateFormat = "*" + getRpgDateFormat(df.getJavaFormat());
				df = new DateEx(String.valueOf(sf),
						(src instanceof Time) ? 'T' : 'D', dateFormat);
				retObj = df;
			}
		}
		else if (dest instanceof Time && !(src instanceof Time))
		{
			DateEx dx = new DateEx(String.valueOf(src), 'T', "*ISO");
			dest = dx.getTime();
			retObj = dest;
		}
		else if (dest instanceof Date && !(src instanceof Date))
		{
			DateEx dx = new DateEx(String.valueOf(src), 'D', "*ISO");
			dest = dx.getDate();
			retObj = dest;
		}
		else if (srcType.equalsIgnoreCase(destType) ||
				(
					(src instanceof Integer || src instanceof Float) &&
					(dest instanceof Short || dest instanceof Integer)
				))
		{
			if (src instanceof DateEx)
			{
				DateEx ddtx = (DateEx)dest;
				DateEx sdtx = (DateEx)src;
				ddtx.set(sdtx);
				retObj = ddtx;
			}
			else if (src instanceof Date)
			{
				Date ddt = (Date)dest;
				Date sdt = (Date)src;
				ddt.setTime(sdt.getTime());
				retObj = ddt;
			}
			else if (src instanceof String)
			{
				retObj = new String((String)src);
			}
			else if (src instanceof Number)
			{
				int ti = lookupStrInStrArr(srcType, varTypes[0], 0, true);

				if (ti != -1)
				{
					try
					{
						retObj = src.getClass()
									.getMethod(varTypes[1][ti] + "Value")
									.invoke(src);
					}
					catch (Exception e)
					{
						logStackTrace(e);
					}
				}
			}

			if (retObj == null)
			{
				retObj = src;
			}
		}

		return retObj;
	}

	/**
	 * Returns the Class object representing the component type.
	 *
	 * @param cls class whose component object is to be returned
	 * @return Class object representing the component type
	 * @since (2004-04-11.16:24:49)
	 */
	public static Class<?> getComponentType(Class<?> cls)
	{
		if (cls == null)
		{
			return null;
		}

		// Get the Class representing the component type of an array		
		Class<?> componentType = cls.getComponentType();

		try
		{
			if (componentType == null)
			{
				componentType = Class.forName(cls.getName());
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			// impossible!!
		}

		return componentType;
	}

	public static Object getField(Object object, String field)
	{
		try
		{
			String split[] = StringUtils.split(field, ".");
			String propName = split[0];
			Class<?> srcCls = object.getClass();
			Field srcFld = srcCls.getDeclaredField(propName);
			srcFld.setAccessible(true);

			Object data = srcFld.get(object);

			if (data == null)
			{
				return null;
			}

			if (split.length > 1)
			{
				String newArr[] = new String[split.length - 1];
				System.arraycopy(split, 1, newArr, 0, split.length - 1);

				String nextPropName = StringUtils.join(newArr, ".");
				getField(data, nextPropName);
			}

			return data;
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return null;
	}

	public static Class<?> getFieldType(Object object, String field)
	{
		try
		{
			String split[] = StringUtils.split(field, ".");
			String propName = split[0];
			Class<?> srcCls = object.getClass();
			Field srcFld = srcCls.getDeclaredField(propName);
			srcFld.setAccessible(true);

			Object data = srcFld.get(object);

			if (split.length > 1)
			{
				data = (data == null) ? srcFld.getType().newInstance() : data;

				String newArr[] = new String[split.length - 1];
				System.arraycopy(split, 1, newArr, 0, split.length - 1);

				String nextPropName = StringUtils.join(newArr, ".");
				getFieldType(data, nextPropName);
			}
			else
			{
				return srcFld.getType();
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return null;
	}

	/**
	 * Gets the class fields.
	 *
	 * @param cls the runtime class of an object
	 * @return class fields
	 * @since (2008-06-02.14:41:19)
	 */
	public static String[] getFields(Class<?> cls)
	{
		String fields[] = null;

		try
		{
			Field flds[] = cls.getFields();
			int numFields = Utils.length(flds);

			if (numFields == 0)
			{
				return null;
			}

			ArrayList<String> arrayList = new ArrayList<String>(numFields);

			for (int i = 0; i < numFields; i++)
			{
				Field fld = flds[i];

				int modifier = fld.getModifiers();

				// Ignore final and static fields
				if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier))
				{
					continue;
				}

				arrayList.add(flds[i].getName());
			} // i

			int size = arrayList.size();

			if (size > 0)
			{
				fields = (String[])arrayList.toArray(new String[size]);
			}
		}
		catch (Exception e)
		{
			logMessage(e.getMessage());
		}

		return fields;
	}

	/**
	 * Gets the field value of the class field.
	 *
	 * @param object the runtime class of an object
	 * @param fieldName field name
	 * @param searchInChild whether to search in children
	 * @return field value
	 * @since (2012-09-10.14:41:19)
	 */
	public static String getFieldVal(Object object, String fieldName,
		boolean searchInChild)
	{
		Class<?> cls;

		if (searchInChild)
		{
			cls = object.getClass().getSuperclass();
		}
		else
		{
			cls = object.getClass();
		}

		try
		{
			Field flds[] = cls.getDeclaredFields();
			Field newFlds[] = new Field[flds.length];

			for (int i = 0, j = 0; i < flds.length; i++)
			{
				if (!ReflectionUtils.isUserDefOrArr(flds[i].getType()))
				{
					if (flds[i].getName().equalsIgnoreCase(fieldName))
					{
						flds[i].setAccessible(true);

						return flds[i].get(object).toString();
					}
				}
				else if (searchInChild)
				{
					newFlds[j] = flds[i];
					j++;
				}
			}

			LinkedHashMap<String, Field> colFldMap =
				new LinkedHashMap<String, Field>();

			try
			{
				ReflectionUtils.getColumnAnnoFldMap(object, colFldMap);

				for (String str : colFldMap.keySet())
				{
					if (str.equals(fieldName))
					{
						return getMethodVal(cls, str, object);
					}
				}
			}
			catch (Exception e)
			{
			}

			if (searchInChild)
			{
				for (int i = 0; i < newFlds.length; i++)
				{
					if (newFlds[i] == null)
					{
						continue;
					}

					Object object1 = newFlds[i].get(object);

					String fldValue = getFieldVal(object1, fieldName, false);

					if (fldValue.length() != 0)
					{
						return fldValue;
					}
				}
			}
		}
		catch (Exception e)
		{
			logMessage(e.getMessage());
		}

		return "";
	}

	/**
	 * Gets Field-name to Annotation mapping in addition to returning List of
	 * all field objects.
	 *
	 * @param objClass the class whose fields are concerned
	 * @param colFldMap
	 * @param recfmtName
	 * @return
	 */
	public static List<Field> getFld2ColumnAnnoMap(Class<?> objClass,
		LinkedHashMap<String, Column> colFldMap, String recfmtName)
	{
		List<Field> lstFlds = new ArrayList<Field>();
		Class<?> cls = objClass;
		boolean idClassChecked = false;

		while (cls != null)
		{
			Field fa[] = cls.getDeclaredFields();

			for (Field fld : fa)
			{
				int modifier = fld.getModifiers();

				if (!(
						Modifier.isFinal(modifier) ||
						Modifier.isStatic(modifier)
					))
				{
					Column colAnno = null;

					if (fld.isAnnotationPresent(Column.class))
					{
						colAnno = fld.getAnnotation(Column.class);
					}

					if (colAnno == null)
					{
						if (recfmtName.length() != 0)
						{
							Method gttr = getGetter(cls, fld.getName());

							if (gttr != null)
							{
								Recfmt rfmt = gttr.getAnnotation(Recfmt.class);

								if ((rfmt != null) &&
										rfmt.recfname()
												.equalsIgnoreCase(recfmtName))
								{
									colAnno = gttr.getAnnotation(Column.class);
								}
								else
								{
									continue;
								}
							}
						}
						else
						{
							// Get the getter method and its annotation for the
							// short/actual name of the field
							Method gttr = getGetter(cls, fld.getName());

							if (gttr != null)
							{
								colAnno = gttr.getAnnotation(Column.class);
							}
						}
					}

					if (colAnno != null)
					{
						if (idClassChecked)
						{
							Field tp = getFldByName(fld.getName(), lstFlds);

							if (tp != null)
							{
								colFldMap.put(tp.getName().toUpperCase(),
									colAnno);
							}
						}
						else
						{
							colFldMap.put(fld.getName().toUpperCase(), colAnno);
						}
					}

					if (!idClassChecked)
					{
						lstFlds.add(fld);
					}
				}
			} // for

			cls = cls.getSuperclass();

			//if ((cls == null) && !idClassChecked)
			if (((cls == null) || (cls != objClass.getSuperclass())) &&
					!idClassChecked)
			{
				IdClass idc;

				if (cls == null)
				{
					// If objClass is Entity
					idc = objClass.getAnnotation(IdClass.class);
				}
				else
				{
					// If objClass is Unqualified EDS whose super is Entity
					idc =
						objClass.getSuperclass().getAnnotation(IdClass.class);
				}

				if (idc != null)
				{
					cls = idc.value();
					idClassChecked = true;
				}
			}
		} // while

		return lstFlds;
	}

	public static Field getFldByName(String fldName, List<Field> lstFlds)
	{
		if ((Utils.length(fldName) == 0) || (lstFlds == null))
		{
			return null;
		}

		for (Field fld : lstFlds)
		{
			if (fld.getName().equalsIgnoreCase(fldName))
			{
				return fld;
			}
		}

		return null;
	}

	/**
	 * Iterates over Class attributes to check if any attribute has 'Column'
	 * annotation with matching 'name' value.
	 *
	 * @param fldName
	 * @param lstFlds
	 * @param colFldMap
	 * @return Entity field
	 * @since (2014-06-01.10:44:35)
	 */
	public static Field getFldByName(String fldName, List<Field> lstFlds,
		Map<String, Column> colFldMap)
	{
		if ((Utils.length(fldName) == 0) || (lstFlds == null))
		{
			return null;
		}

		for (Field fld : lstFlds)
		{
			Column column;

			if (fld.isAnnotationPresent(Column.class))
			{
				column = fld.getAnnotation(Column.class);
			}
			else
			{
				column = colFldMap.get(fld.getName().toUpperCase());
			}

			if (column == null)
			{
				continue;
			}

			if (column.name().equalsIgnoreCase(fldName))
			{
				return fld;
			}
		}

		return null;
	}

	public static Method getGetter(Class<?> clazz, String field)
	{
		try
		{
			char firstChar = field.charAt(0);
			String getter =
				"get" + Character.toTitleCase(firstChar) + field.substring(1);
			Method method = clazz.getDeclaredMethod(getter);

			return method;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Method getIdGetter(Class<?> clazz)
	{
		Method idn = null;

		if (clazz.isAnnotationPresent(IdClass.class))
		{
			IdClass idc = clazz.getAnnotation(IdClass.class);
			idn = getGetter(clazz, idc.value().getSimpleName().toLowerCase());
		}
		else
		{
			Method allMethods[] = clazz.getDeclaredMethods();

			for (Method m : allMethods)
			{
				if (m.isAnnotationPresent(Id.class))
				{
					idn = m;
				}
			}
		}

		return idn;
	}

	public static Object[] getKeyVals(Object entity, String keyFlds[],
		int numKeys)
	{
		if ((entity == null) || (keyFlds == null))
		{
			return null;
		}

		Object keyVals[] = new Object[numKeys];

		LinkedHashMap<String, Field> colFldMap =
			new LinkedHashMap<String, Field>();
		getColumnAnnoFldMap(entity, colFldMap);

		for (int i = 0; i < numKeys; i++)
		{
			Field kfld = colFldMap.get(keyFlds[i].toUpperCase());

			if (kfld == null)
			{
				kfld = colFldMap.get(keyFlds[i]);
			}

			if (kfld == null)
			{
				continue;
			}

			kfld.setAccessible(true);

			try
			{
				keyVals[i] = kfld.get(entity);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}

			if (keyVals[i] == null)
			{
				keyVals[i] = getPrimResetVal(kfld.getType());
			}

			kfld.setAccessible(false);
		}

		return keyVals;
	}

	private static String getMethodVal(Class<?> childClass, String fieldName,
		Object object) throws Exception
	{
		Method method = null;
		Method methods[] = childClass.getDeclaredMethods();

		for (Method m : methods)
		{
			int modifier = m.getModifiers();

			if (!(Modifier.isFinal(modifier) || Modifier.isStatic(modifier)))
			{
				if (m.isAnnotationPresent(Column.class))
				{
					Column column = m.getAnnotation(Column.class);
					String fldName = column.name();

					if (fieldName.equalsIgnoreCase(fldName.trim()))
					{
						method = m;

						break;
					}
				}
			}
		} // m

		if (method == null)
		{
			return "";
		}

		String name = method.getName();

		if (name.startsWith("get"))
		{
			name = name.substring(3, name.length()).toLowerCase();
		}

		Method getter = getGetter(childClass, name);

		return getter.invoke(object).toString();
	}

	public static Object[] getKeyVals(Object entity, String keyFlds[])
	{
		return getKeyVals(entity, keyFlds, Utils.length(keyFlds));
	}

	public static Object getPrimResetVal(Class<?> type)
	{
		if (type == null)
		{
			return null;
		}

		Object obj;

		if (type.isAssignableFrom(Number.class))
		{
			obj = 0;
		}
		else if (type.isAssignableFrom(StringBuilder.class))
		{
			obj = new StringBuilder();
		}
		else if (type.isAssignableFrom(String.class))
		{
			obj = new String();
		}
		else if (type.isAssignableFrom(DateEx.class))
		{
			obj = new DateEx();
		}
		else if (type.isAssignableFrom(java.sql.Date.class))
		{
			obj = new java.sql.Date(0);
		}
		else if (type.isAssignableFrom(Time.class))
		{
			obj = new Time(0);
		}
		else if (type.isAssignableFrom(Timestamp.class))
		{
			obj = new Timestamp(0);
		}
		else if (type.isAssignableFrom(Date.class))
		{
			obj = new Date();
		}
		else if (!isUserDefOrArr(type, true))
		{
			obj = class2ResetVal.get(type);
		}
		else
		{
			return null;
		}

		return obj;
	}

	public static Method getSetter(Class<?> clazz, String field)
	{
		try
		{
			//field = Character.toTitleCase(field.charAt(0)) +
				//field.substring(1).toLowerCase();
			field = Character.toTitleCase(field.charAt(0)) + field.substring(1);

			String setter = "set" + field;
			Method methods[] = clazz.getDeclaredMethods();

			for (Method method : methods)
			{
				method.setAccessible(true);

				if (method.getName().equals(setter))
				{
					return method;
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}

		return null;
	}

	public static Object getSimplifiedVal(Object src)
	{
		if (src == null)
		{
			return null;
		}

		Object retObj = src;

		if (src instanceof DateEx)
		{
			DateEx sf = (DateEx)src;

			if (sf != null)
			{
				if (sf.getTypeCode() == 'T')
				{
					retObj = sf.getTime();
				}
				else if (sf.getTypeCode() == 'Z')
				{
					retObj = sf.getDateTime();
				}
				else
				{
					retObj = sf.getDate();
				}
			}
		}
		else if (src instanceof StringBuilder)
		{
			retObj = String.valueOf(src);
		}

		return retObj;
	}

	public static void initialize(Object source)
	{
		Class<?> srcCls = source.getClass();
		Field sourceFlds[] = srcCls.getDeclaredFields();

		try
		{
			int count = (sourceFlds == null) ? 0 : sourceFlds.length;

			for (int indx = 0; indx < count; indx++)
			{
				Field srcField = sourceFlds[indx];
				srcField.setAccessible(true);

				int modifiers = srcField.getModifiers();

				if (Modifier.isFinal(modifiers))
				{
					continue;
				}

				Object data = srcField.get(source);

				if (data == null)
				{
					Method setter = getSetter(srcCls, srcField.getName());
					setter.invoke(source, data);
				}

				if (IXRedoModel.class.isAssignableFrom(srcField.getType()))
				{
					initialize(data);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	public static void initialize(Object source, Object target, String str)
	{
		Class<?> srcCls = source.getClass();
		Class<?> targetCls = target.getClass();
		Field sourceFlds[] = srcCls.getDeclaredFields();

		try
		{
			int count = (sourceFlds == null) ? 0 : sourceFlds.length;

			for (int indx = 0; indx < count; indx++)
			{
				Field srcField = sourceFlds[indx];
				srcField.setAccessible(true);

				int modifiers = srcField.getModifiers();

				if (Modifier.isFinal(modifiers))
				{
					continue;
				}

				Field targetFld =
					targetCls.getDeclaredField(srcField.getName());
				targetFld.setAccessible(true);

				Object data = srcField.get(source);

				if ((data != null) &&
						IXRedoModel.class.isAssignableFrom(data.getClass()))
				{
					initialize(data, targetFld.get(target), str);
				}
				else
				{
					Method getter = getGetter(targetCls, targetFld.getName());
					Recfmt recfrmt = null;

					if (getter != null)
					{
						recfrmt = getter.getAnnotation(Recfmt.class);
					}

					if (str.length() > 0)
					{
						if ((recfrmt != null) &&
								recfrmt.recfname().equalsIgnoreCase(str))
						{
							Method setter =
								getSetter(targetCls, targetFld.getName());
							setter.invoke(target, data);
						}
					}
					else
					{
						Method setter =
							getSetter(targetCls, targetFld.getName());
						setter.invoke(target, data);
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Initializes the fields of the target class which corresponds to the
	 * fields of the SQL stored procedure.
	 *
	 * @param targetClass class whose fields have to be initialized
	 * @param cStmt a <code>CallableStatement</code> object to execute SQL
	 *        stored procedure
	 * @since (2009-02-13.12:14:24)
	 */
	public static void initializeClass(Object targetClass,
		CallableStatement cStmt) throws SQLException
	{
		Object classObj = targetClass;
		Class<?> cls = targetClass.getClass();

		Field flds[] = cls.getFields();
		int numFields = Utils.length(flds);

		if (numFields == 0)
		{
			return;
		}

		for (int i = 0; i < numFields; i++)
		{
			try
			{
				String fldName = flds[i].getName();
				Object data = cStmt.getString(i + 1);

				if (data == null)
				{
					continue;
				}

				Field field = cls.getField(fldName);
				field.set(classObj, data);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		} // i
	}

	/**
	 * Initializes the fields of the target class with the fields of the source
	 * class.
	 *
	 * @param targetObj object whose fields have to be initialized
	 * @param targetCls class whose fields have to be initialized
	 * @param sourceObj object from where the value of the fields have to be
	 *        retrieved
	 * @param sourceCls class from where the value of the fields have to be
	 *        retrieved
	 * @param allFields if <code>true</code> all fields of target class would be
	 *        initialized, else only non null fields of target class would be
	 *        initialized
	 * @since (2014-05-29.12:44:35)
	 */
	private static void initializeClass(Object targetObj, Class<?> targetCls,
		Object sourceObj, Class<?> sourceCls, boolean allFields)
	{
		if ((targetCls == null) || (sourceCls == null))
		{
			return;
		}

		Field sourceFlds[] = sourceCls.getDeclaredFields();
		int numFlds = Utils.length(sourceFlds);

		if (numFlds == 0)
		{
			return;
		}

		for (int i = 0; i < numFlds; i++)
		{
			try
			{
				String fldName = sourceFlds[i].getName();

				Field sourceFld = sourceCls.getDeclaredField(fldName);
				sourceFld.setAccessible(true);
				Object data = sourceFld.get(sourceObj);

				if (allFields || (sourceFld.get(targetObj) != null))
				{
					Field targetFld = targetCls.getDeclaredField(fldName);
					int modifier = targetFld.getModifiers();

					// Ignore final and static fields
					if (Modifier.isFinal(modifier) ||
							Modifier.isStatic(modifier))
					{
						continue;
					}

					targetFld.setAccessible(true);
					targetFld.set(targetObj, data);
					targetFld.setAccessible(false);
				}

				sourceFld.setAccessible(false);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		} // i
	}

	/**
	 * Initializes the fields of the target class with the fields of the source
	 * class.
	 *
	 * @param targetObj object whose fields have to be initialized
	 * @param sourceObj object from where the value of the fields have to be
	 *        retrieved
	 * @param allFields if <code>true</code> all fields of target class would be
	 *        initialized, else only non null fields of target class would be
	 *        initialized
	 * @since (2008-09-26.18:08:35)
	 */
	public static void initializeClass(Object targetObj, Object sourceObj,
		boolean allFields)
	{
		if ((targetObj == null) || (sourceObj == null))
		{
			return;
		}

		Class<?> sourceCls = sourceObj.getClass();
		Class<?> targetCls = targetObj.getClass();

		initializeClass(targetObj, targetCls, sourceObj, sourceCls, allFields);

		if (sourceCls.getSuperclass() != Object.class)
		{
			initializeClass(targetObj, targetCls.getSuperclass(), sourceObj,
				sourceCls.getSuperclass(), allFields);
		}
	}

	public static void initializeField(Object object, String field,
		Object fieldValue)
	{
		try
		{
			String split[] = StringUtils.split(field, ".");
			String propName = split[0];
			Class<?> srcCls = object.getClass();
			Field srcFld = srcCls.getDeclaredField(propName);
			srcFld.setAccessible(true);

			Object data = srcFld.get(object);

			if (split.length > 1)
			{
				data = (data == null) ? srcFld.getType().newInstance() : data;

				String newArr[] = new String[split.length - 1];
				System.arraycopy(split, 1, newArr, 0, split.length - 1);

				String nextPropName = StringUtils.join(newArr, ".");
				initializeField(data, nextPropName, fieldValue);
			}
			else
			{
				Method setter = getSetter(srcCls, srcFld.getName());
				setter.invoke(object, fieldValue);
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public static boolean isCompatiblyEqual(Object obj1, Object obj2)
	{
		if ((obj1 == null) || (obj2 == null))
		{
			return false;
		}

		if (!isTypeCompatible(obj1.getClass(), obj2.getClass(), true))
		{
			return false;
		}

		Date dt1 = null;
		Date dt2 = null;

		if (obj1 instanceof DateEx)
		{
			dt1 = ((DateEx)obj1).getDateTime();
		}
		else if (obj1 instanceof Date)
		{
			dt1 = (Date)obj1;
		}

		if (obj2 instanceof DateEx)
		{
			dt2 = ((DateEx)obj2).getDateTime();
		}
		else if (obj2 instanceof Date)
		{
			dt2 = (Date)obj2;
		}

		if ((dt1 != null) && (dt2 != null))
		{
			return dt1.equals(dt2);
		}
		else if (obj1 instanceof CharSequence || obj2 instanceof CharSequence)
		{
			return equal(obj2, obj1);
		}
		else
		{
			return obj1.equals(obj2);
		}
	}

	public static boolean isKeysArrayEqual(Object destKeyFldVals[],
		Object srcKeyFldVals[])
	{
		if ((destKeyFldVals == null) ^ (srcKeyFldVals == null))
		{
			return false;
		}

		if ((destKeyFldVals == null) && (srcKeyFldVals == null))
		{
			return true;
		}

		if (destKeyFldVals.length != srcKeyFldVals.length)
		{
			return false;
		}

		for (int i = 0; i < srcKeyFldVals.length; i++)
		{
			if (!isCompatiblyEqual(destKeyFldVals[i], srcKeyFldVals[i]))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if the class type of the two given classes are same.
	 *
	 * @param clsType1 class name of first class
	 * @param clsType2 class name of second class
	 * @return true if the classes have same type, otherwise false
	 * @since (2004-04-13.11:44:31)
	 */
	public static boolean isSameClassType(String clsType1, String clsType2)
	{
		if ((clsType1 == null) || (clsType2 == null))
		{
			return false;
		}

		if (("byte".equals(clsType1) && "java.lang.Byte".equals(clsType2)) ||
				("java.lang.Byte".equals(clsType1) && "byte".equals(clsType2)) ||
				(
					"char".equals(clsType1) &&
					"java.lang.Character".equals(clsType2)
				) ||
				(
					"java.lang.Character".equals(clsType1) &&
					"char".equals(clsType2)
				) ||
				(
					"double".equals(clsType1) &&
					"java.lang.Double".equals(clsType2)
				) ||
				(
					"java.lang.Double".equals(clsType1) &&
					"double".equals(clsType2)
				) ||
				(
					"float".equals(clsType1) &&
					"java.lang.Float".equals(clsType2)
				) ||
				(
					"java.lang.Float".equals(clsType1) &&
					"float".equals(clsType2)
				) ||
				(
					"int".equals(clsType1) &&
					"java.lang.Integer".equals(clsType2)
				) ||
				(
					"java.lang.Integer".equals(clsType1) &&
					"int".equals(clsType2)
				) ||
				(
					"long".equals(clsType1) &&
					"java.lang.Long".equals(clsType2)
				) ||
				(
					"java.lang.Long".equals(clsType1) &&
					"long".equals(clsType2)
				) ||
				(
					"short".equals(clsType1) &&
					"java.lang.Short".equals(clsType2)
				) ||
				(
					"java.lang.Short".equals(clsType1) &&
					"short".equals(clsType2)
				) ||
				(
					"boolean".equals(clsType1) &&
					"java.lang.Boolean".equals(clsType2)
				) ||
				(
					"java.lang.Boolean".equals(clsType1) &&
					"boolean".equals(clsType2)
				))
		{
			return true;
		}

		return clsType1.equals(clsType2);
	}

	public static boolean isTypeCompatible(Class<?> formalType,
		Class<?> actualType, boolean strict)
	{
		if ((formalType == null) && (actualType != null))
		{
			return false;
		}

		if ((formalType != null) && (actualType == null))
		{
			return false;
		}

		if ((formalType == null) && (actualType == null))
		{
			return true;
		}

		if (strict)
		{
			String formalIfStr = formalType.getSimpleName();
			String actualIfStr = actualType.getSimpleName();

			if ((formalIfStr != null) && formalIfStr.equals(actualIfStr))
			{
				return true;
			}

			for (String eqArr[] : equivalentTypes)
			{
				if ((lookupStrInStrArr(formalIfStr, eqArr, 0) > -1) &&
						(lookupStrInStrArr(actualIfStr, eqArr, 0) > -1))
				{
					return true;
				}
			}

			return formalType.equals(actualType);
		}
		else
		{
			return formalType.isAssignableFrom(actualType);
		}
	}

	public static boolean isUserDefOrArr(Class<?> typ)
	{
		return isUserDefOrArr(typ, false);
	}

	/**
	 * Returns true if a type is User defined
	 * @param objTyp
	 * @param considerSbDt - considers StringBuilder, Date & DateEx too as User
	 *        defined if true
	 * @return
	 */
	public static boolean isUserDefOrArr(Class<?> objTyp, boolean considerSbDt)
	{
		if (objTyp == null)
		{
			return false;
		}

		String typeName = objTyp.getSimpleName().toLowerCase();

		return (((class2ResetVal.get(objTyp) == null) ||
		(
			considerSbDt &&
			(
				typeName.contains("stringbuilder") ||
				typeName.contains("date") || typeName.contains("time")
			)
		)));
	}

	public static void loadEDSRefs(Object mbr, List<Field> uqEDSs)
	{
		if ((mbr == null) || (mbr == null) || !isUserDefOrArr(mbr.getClass()) ||
				!isUserDefOrArr(mbr.getClass()))
		{
			return;
		}

		Class<?> mbrClass = mbr.getClass();

		Field dSs[] = mbrClass.getDeclaredFields();

		for (Field dsf : dSs)
		{
			if (dsf.isAnnotationPresent(UnqualifiedEDS.class))
			{
				uqEDSs.add(dsf);
			}
		}
	}

	public static void reset(Object obj)
	{
		if ((obj == null) || !isUserDefOrArr(obj.getClass(), true))
		{
			return;
		}

		if (obj instanceof StringBuilder)
		{
			((StringBuilder)obj).setLength(0);
		}
		else if (obj instanceof DateEx)
		{
			((DateEx)obj).setTime(0);
		}
		else if (obj instanceof Time)
		{
			((Time)obj).setTime(0);
		}
		else if (obj instanceof Timestamp)
		{
			((Timestamp)obj).setTime(0);
		}
		else if (obj instanceof java.sql.Date)
		{
			((java.sql.Date)obj).setTime(0);
		}
		else if (obj instanceof Date)
		{
			((Date)obj).setTime(0);
		}
	}

	public static void resetQin(Object obj)
	{
		if (obj instanceof StringBuilder)
		{
			StringBuilder qin = (StringBuilder)obj;
			int len = qin.length();

			for (int i = 0; i < len; i++)
			{
				qin.setCharAt(i, '0');
			}
		}
	}

	public static void setCompatibleField(Field dfld, Object objD, Field sfld,
		Object objS)
	{
		if ((dfld == null) || (objD == null) || (objS == null))
		{
			return;
		}

		try
		{
			dfld.setAccessible(true);

			Object src = objS;

			if (sfld != null)
			{
				sfld.setAccessible(true);
				src = sfld.get(objS);
			}

			Object dest = dfld.get(objD);
			src = getCompatibleVal(dest, src);

			if (src != null)
			{
				if (src instanceof Integer && dest instanceof Short)
				{
					dfld.set(objD, ((Integer)src).shortValue());
				}
				else if (src instanceof Float && dest instanceof Short)
				{
					dfld.set(objD, ((Float)src).shortValue());
				}
				else if (src instanceof Float && dest instanceof Integer)
				{
					dfld.set(objD, ((Float)src).intValue());
				}
				else
				{
					dfld.set(objD, src);
				}
			}

			dfld.setAccessible(false);

			if (sfld != null)
			{
				sfld.setAccessible(false);
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public static void setCompatibleVal(Field dfld, Object objD, Number numS)
	{
		if ((dfld == null) || (objD == null) || (numS == null))
		{
			return;
		}

		try
		{
			dfld.setAccessible(true);
			Object val = dfld.get(objD);

			if (val instanceof Double)
			{
				dfld.set(objD, numS.doubleValue());
			}
			else if (val instanceof Float)
			{
				dfld.set(objD, numS.floatValue());
			}
			else if (val instanceof Integer)
			{
				dfld.set(objD, numS.intValue());
			}
			else if (val instanceof Long)
			{
				dfld.set(objD, numS.longValue());
			}
			else if (val instanceof Short)
			{
				dfld.set(objD, numS.shortValue());
			}
			else
			{
				dfld.set(objD, numS);
			}

			dfld.setAccessible(false);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public static void setKeyVals(Object entity, String keyFlds[],
		Object values[])
	{
		setKeyVals(entity, keyFlds, Utils.length(keyFlds), values);
	}

	public static void setKeyVals(Object entity, String keyFlds[], int numKeys,
		Object values[])
	{
		if ((entity == null) || (keyFlds == null))
		{
			return;
		}

		LinkedHashMap<String, Field> colFldMap =
			new LinkedHashMap<String, Field>();
		getColumnAnnoFldMap(entity, colFldMap);

		for (int i = 0; i < numKeys; i++)
		{
			Field kfld = colFldMap.get(keyFlds[i].toUpperCase());

			if (kfld == null)
			{
				continue;
			}

			try
			{
				if (values[i] == null)
				{
					values[i] = getPrimResetVal(kfld.getType());
				}

				kfld.set(entity, values[i]);
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}
	}

	public static void simplifyTypes(Object values[])
	{
		if (values == null)
		{
			return;
		}

		int len = values.length;

		for (int i = 0; i < len; i++)
		{
			values[i] = getSimplifiedVal(values[i]);
		}
	}

	/*
	 * Trims value of objects.
	 *
	 * @param obj
	 * @return trimmed object
	 */
	public static Object trimAll(Object obj)
	{
		if (obj == null)
		{
			return obj;
		}

		try
		{
			List<Field> objFlds = getAllFields(obj, "");

			for (Field objFld : objFlds)
			{
				objFld.setAccessible(true);

				if (Modifier.isStatic(objFld.getModifiers()))
				{
					continue;
				}

				Object data = objFld.get(obj);

				if (data == null)
				{
					continue;
				}

				if (data instanceof CharSequence)
				{
					String dataStr = rtrim(data.toString());

					if (data instanceof StringBuilder)
					{
						data = new StringBuilder(dataStr);
					}
					else
					{
						data = dataStr;
					}

					objFld.set(obj, data);
				}
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return obj;
	}

	public static void updKeyFlds(Object obj, String recfmtName)
	{
		if (obj == null)
		{
			return;
		}

		Class<?> cls = obj.getClass();

		if (cls.getSuperclass() != Object.class)
		{
			cls = cls.getSuperclass();
		}

		if (!cls.isAnnotationPresent(IdClass.class))
		{
			return;
		}

		IdClass idc = cls.getAnnotation(IdClass.class);

		try
		{
			String simpleName = idc.value().getSimpleName();
			String name = simpleName.toLowerCase();
			Field ckFld = null;

			try
			{
				ckFld = cls.getDeclaredField(name);
			}
			catch (NoSuchFieldException e)
			{
			}

			if (ckFld == null)
			{
				//name = Character.toLowerCase(simpleName.charAt(0)) +
					//simpleName.substring(1);
				name = toLowerCase(simpleName);
				ckFld = cls.getDeclaredField(name);
			}

			Object compositeKey = ckFld.get(obj);
			List<Field> allKeys = getAllFields(compositeKey, recfmtName);
			List<Field> allFlds = getAllFields(obj, recfmtName);

			for (Field kf : allKeys)
			{
				kf.setAccessible(true);
				Field matchingFld = getFldByName(kf.getName(), allFlds);

				if (matchingFld != null)
				{
					matchingFld.setAccessible(true);
					kf.set(compositeKey, matchingFld.get(obj));
					matchingFld.setAccessible(false);
				}

				kf.setAccessible(false);
			}

			ckFld.set(obj, compositeKey);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public static void updateExtFlds(Object obj, boolean updLclFlds)
	{
		if ((obj == null) || !isUserDefOrArr(obj.getClass()))
		{
			return;
		}

		List<Field> flds = getAllFields(obj, "");

		for (Field fld : flds)
		{
			if (fld.isAnnotationPresent(ExtFld.class))
			{
				ExtFld extAnno = fld.getAnnotation(ExtFld.class);
				String refFldName = extAnno.refFldName();

				for (Field srcfld : flds)
				{
					if (srcfld.getName().equalsIgnoreCase(refFldName) &&
							isTypeCompatible(fld.getType(), srcfld.getType(),
								true))
					{
						if (updLclFlds)
						{
							setCompatibleField(fld, obj, srcfld, obj);
						}
						else
						{
							setCompatibleField(srcfld, obj, fld, obj);
						}

						break;
					}
				}
			}
		}
	}
}
