package com.databorough.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.ReflectionUtils.getPrimResetVal;
import static com.databorough.utils.Utils.bool2Str;
import static com.databorough.utils.Utils.getBoolVal;

/**
 * The ArrayUtils class contains various methods for manipulating arrays.
 *
 * @author Amit Arya
 * @since (2003-01-28.10:15:51)
 */
public final class ArrayUtils
{
	/**
	 * The ArrayUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private ArrayUtils()
	{
		super();
	}

	public static String[] arrayUpperCase(String arr[])
	{
		if (arr == null)
		{
			return arr;
		}

		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = arr[i].toUpperCase();
		}

		return arr;
	}

	/**
	 * Converts Object array to boolean array.
	 *
	 * @param array Object array
	 * @return boolean array
	 * @since (2014-10-18.14:08:15)
	 */
	private static boolean[] boolArray(Object array)
	{
		if (array == null)
		{
			return null;
		}

		int len = Utils.length(array);
		boolean newArray[] = new boolean[len];

		for (int i = 0; i < len; i++)
		{
			newArray[i] = getBoolVal(Array.get(array, i));
		}

		return newArray;
	}

	/**
	 * A Utility method to copy arrays. System.arraycopy wasn't so plain enough
	 * to be accepted in the generated code. This is good especially if whole
	 * length of array has to be copied. It copies only if their lengths are
	 * same.
	 *
	 * @param arrayDest the destination array where source has to be copied
	 * @param arraySrc the source array to be copied
	 * @since (2011-02-04.15:41:26)
	 */
	public static void copyArray(Object arrayDest, Object arraySrc)
	{
		if ((arraySrc == null) || (arrayDest == null))
		{
			return;
		}

		if (!arraySrc.getClass().isArray() || !arrayDest.getClass().isArray())
		{
			return;
		}

		int lenSrc = Utils.length(arraySrc);
		int lenDest = Utils.length(arrayDest);

		if ((lenSrc == 0) || (lenDest == 0))
		{
			return;
		}

		if (arraySrc instanceof String[] && arrayDest instanceof boolean[])
		{
			// Convert to boolean array
			arraySrc = boolArray(arraySrc);
		}
		else if (arraySrc instanceof boolean[] && arrayDest instanceof String[])
		{
			// Convert to String array
			arraySrc = strArray((boolean[])arraySrc);
		}

		if (lenSrc > lenDest)
		{
			System.arraycopy(arraySrc, 1, arrayDest, 0, lenDest);
		}
		else if (lenSrc < lenDest)
		{
			System.arraycopy(arraySrc, 0, arrayDest, 1, lenSrc);
		}
		else
		{
			System.arraycopy(arraySrc, 0, arrayDest, 0, lenSrc);
		}
	}

	/**
	 * A Utility method to copy arrays. System.arraycopy wasn't so plain enough
	 * to be accepted in the generated code. This is good especially if whole
	 * length of array has to be copied. It copies only if their lengths are
	 * same.
	 *
	 * @param arrayDest the destination array where source has to be copied
	 * @param arraySrc the source array to be copied
	 * @param startindex
	 * @param noOfElem
	 * @return the destination array
	 * @since (2013-09-05.15:41:26)
	 */
	public static Object copyArray(Object arrayDest, Object arraySrc,
		int startindex, int noOfElem)
	{
		if ((arraySrc == null) || (arrayDest == null))
		{
			return arrayDest;
		}

		if (!arraySrc.getClass().isArray() || !arrayDest.getClass().isArray())
		{
			return arrayDest;
		}

		int lenSrc = Utils.length(arraySrc);

		if ((lenSrc == 0) || (noOfElem == 0))
		{
			return arrayDest;
		}

		if (lenSrc > noOfElem)
		{
			System.arraycopy(arraySrc, 1, arrayDest, startindex, noOfElem);
		}
		else if (lenSrc < noOfElem)
		{
			System.arraycopy(arraySrc, 0, arrayDest, startindex, noOfElem);
		}
		else
		{
			System.arraycopy(arraySrc, 0, arrayDest, 0, lenSrc);
		}

		return arrayDest;
	}

	/**
	 * Divides the array elements by the divisor.
	 *
	 * @param array Double array to divide
	 * @param divisor denominator
	 * @return division of the elements of an array
	 * @since (2013-10-08.16:51:56)
	 */
	public static Double[] divideArray(Double array[], int divisor)
	{
		if ((array == null) || (divisor == 0))
		{
			return array;
		}

		int len = array.length;
		Double newArray[] = new Double[len];

		for (int i = 0; i < len; i++)
		{
			newArray[i] = array[i] / divisor;
		}

		return newArray;
	}

	/**
	 * Converts Long array to Double array.
	 *
	 * @param array Long array
	 * @return Double array
	 * @since (2013-09-02.15:41:05)
	 */
	public static Double[] doubleArray(Long array[])
	{
		if (array == null)
		{
			return null;
		}

		int len = array.length;
		Double newArray[] = new Double[len];

		for (int i = 0; i < len; i++)
		{
			newArray[i] = array[i].doubleValue();
		}

		return newArray;
	}

	/**
	 * Counts the number of non-default elements in an array.
	 *
	 * @param array in which non-default elements have to be counted
	 * @param fromPos index from which to start the search
	 * @return number of non-default elements in an array
	 * @since (2012-10-22.17:33:25)
	 */
	public static int elemArray(Object array, int fromPos)
	{
		if ((array == null) || !array.getClass().isArray())
		{
			return 0;
		}

		int len = Array.getLength(array);

		if (len == 0)
		{
			return 0;
		}

		int count = 0;

		Class<?> arrType = Array.get(array, 0).getClass();

		Object rval = getPrimResetVal(arrType);

		for (int i = fromPos; i < len; i++)
		{
			if ((rval == null) || !rval.equals(Array.get(array, i)))
			{
				count++;
			}
		}

		return count;
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of Objects are
	 * <i>equal</i> to one another.  The two arrays are considered equal if
	 * both arrays contain the same number of elements, and all corresponding
	 * pairs of elements in the two arrays are equal.  Two objects <tt>e1</tt>
	 * and <tt>e2</tt> are considered <i>equal</i> if <tt>(e1==null ? e2==null
	 * : e1.equals(e2))</tt>.  In other words, the two arrays are equal if
	 * they contain the same elements in the same order.  Also, two array
	 * references are considered equal if both are <tt>null</tt>.<p>
	 *
	 * @param a1 one array to be tested for equality.
	 * @param a2 the other array to be tested for equality.
	 * @return <tt>true</tt> if the two arrays are equal.
	 */
	public static boolean equals(String a1[], String a2[])
	{
		if (a1 == a2)
		{
			return true;
		}

		if ((a1 == null) || (a2 == null))
		{
			return false;
		}

		int length = a1.length;

		if (a2.length != length)
		{
			return false;
		}

		for (int i = 0; i < length; i++)
		{
			String o1 = a1[i];
			String o2 = a2[i];

			if (!((o1 == null) ? (o2 == null) : o1.equals(o2)))
			{
				// Extra check for equality
				// Comparison of e.g. 000104530 & 104530
				if ((o1 == null) || (o2 == null))
				{
					return false;
				}

				int retVal;

				try
				{
					retVal = NumberUtils.compareTo(o1, o2, null);
				}
				catch (NumberFormatException nfe)
				{
					retVal = -1;
				}

				if (retVal != 0)
				{
					return false;
				}
			}
		} // i

		return true;
	}

	/**
	 * Expands an array.
	 *
	 * @param array source array
	 * @param finalLen final length of the array
	 * @return array after expanding
	 * @since (2014-06-09.17:11:31)
	 */
	public static Object expandArray(Object array, int finalLen)
	{
		if (array == null)
		{
			return array;
		}

		Class<?> cl = array.getClass();

		if (!cl.isArray())
		{
			return array;
		}

		int len = Array.getLength(array);

		if (len >= finalLen)
		{
			return array;
		}

		Class<?> componentType = array.getClass().getComponentType();
		Object newArray = Array.newInstance(componentType, finalLen);
		System.arraycopy(array, 0, newArray, 0, len);

		return newArray;
	}

	/**
	 * If length of array is less than finalLen, expand the array. If bBegin is
	 * true, set first 'finalLen-len' elements initialized with value and the
	 * rest same as before. If bBegin is false, set first 'len' elements same as
	 * before and the rest initialized with value.
	 *
	 * @param array source String array
	 * @param finalLen final length of the array
	 * @param value the value with which the extra elements would be initialized
	 * @param bBegin whether the expansion of array is at the beginning
	 * @return String array after expanding
	 * @since (2003-07-29.06:57:31)
	 */
	public static String[] expandArray(String array[], int finalLen,
		String value, boolean bBegin)
	{
		int len = Utils.length(array);

		if (len >= finalLen)
		{
			return array;
		}

		String newArray[] = new String[finalLen];

		if (bBegin)
		{
			for (int i = 0; i < (finalLen - len); i++)
			{
				newArray[i] = value;
			}

			if (len > 0)
			{
				System.arraycopy(array, 0, newArray, finalLen - len, len);
			}
		}
		else
		{
			if (len > 0)
			{
				System.arraycopy(array, 0, newArray, 0, len);
			}

			for (int i = len; i < finalLen; i++)
			{
				newArray[i] = value;
			}
		}

		return newArray;
	}

	/**
	 * Fills a boolean array.
	 *
	 * @param array boolean array to be filled
	 * @param value to fill
	 * @since (2012-05-08.18:24:26)
	 */
	public static void fillArray(boolean array[], boolean value)
	{
		int len = Utils.length(array);

		if (len > 0)
		{
			array[0] = value;
		}

		for (int i = 1; i < len; i += i)
		{
			System.arraycopy(array, 0, array, i,
						((len - i) < i) ? (len - i) : i);
		}
	}

	/**
	 * Fills a boolean array.
	 *
	 * @param array boolean array to be filled
	 * @param value to fill
	 * @since (2013-10-09.13:09:26)
	 */
	public static void fillArray(boolean array[], int value)
	{
		fillArray(array, (value == 1));
	}

	/**
	 * Fills a char array.
	 *
	 * @param array char array to be filled
	 * @param value to fill
	 * @since (2012-11-06.11:41:16)
	 */
	public static void fillArray(char array[], char value)
	{
		int len = Utils.length(array);

		if (len > 0)
		{
			array[0] = value;
		}

		for (int i = 1; i < len; i += i)
		{
			System.arraycopy(array, 0, array, i,
						((len - i) < i) ? (len - i) : i);
		}
	}

	/**
	 * Fills an Object array.
	 *
	 * @param array Object array to be filled
	 * @param value to fill
	 * @since (2001-01-02.13:06:26)
	 */
	public static void fillArray(Object array[], Object value)
	{
		if ((array == null) || (value == null))
		{
			return;
		}

		if (array.getClass().getName().contains("StringBuilder") &&
				value instanceof String)
		{
			value = new StringBuilder(String.valueOf(value));
		}

		int len = Array.getLength(array);

		for (int i = 0; i < len; i++)
		{
			Array.set(array, i, value);
		}
	}

	/**
	 * Gets element at fromCol of a Single Dimensional array.
	 *
	 * @param array Object
	 * @param fromCol column number
	 * @return element at fromCol
	 * @since (2003-02-25.10:50:56)
	 */
	public static Object getElement(Object array, int fromCol)
	{
		// Check if element at fromCol is null
		if (isNull(array, fromCol))
		{
			return null;
		}

		return Array.get(array, fromCol);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] initArray(Class<T> destClass, int size,
		Object declObj)
	{
		if ((destClass == null) || (size == 0))
		{
			return null;
		}

		if (declObj == null)
		{
			return initArray(destClass, size);
		}

		Object ret = Array.newInstance(destClass, size);

		try
		{
			Constructor<T> cons = null;

			try
			{
				cons = destClass.getConstructor();
			}
			catch (Exception e)
			{
				cons =
					destClass.getDeclaredConstructor(
							destClass.getDeclaringClass());
			}

			if (cons != null)
			{
				cons.setAccessible(true);

				for (int i = 0; i < size; i++)
				{
					try
					{
						Array.set(ret, i, cons.newInstance(declObj));
					}
					catch (Exception e)
					{
						logStackTrace(e);
					}
				}

				cons.setAccessible(false);
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return (T[])ret;
	}

	/**
	 * A method to return an array object with each element initialized to
	 * arrType.
	 *
	 * @param arrType - class type
	 * @param size - size of desired array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] initArray(Class<T> arrType, int size)
	{
		if ((arrType == null) || (size == 0))
		{
			return null;
		}

		Object ret = Array.newInstance(arrType, size);

		String name = arrType.getName();
		int indx = name.lastIndexOf('.');

		if (indx != -1)
		{
			name = name.substring(indx + 1);
		}

		// Primitive Wrapper class don't have default constructor
		if ("Boolean".equals(name) || "Byte".equals(name) ||
				"Character".equals(name) || "Double".equals(name) ||
				"Float".equals(name) || "Integer".equals(name) ||
				"Long".equals(name) || "Short".equals(name))
		{
			return (T[])ret;
		}

		try
		{
			Constructor<T> con = arrType.getConstructor();

			for (int i = 0; i < size; i++)
			{
				Array.set(ret, i, con.newInstance());
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return (T[])ret;
	}

	/**
	 * Inserts element at the specified index position of the given array and
	 * shifts the array.
	 *
	 * @param array source array Object
	 * @param element to insert
	 * @param index position
	 * @since (2015-02-06.10:35:45)
	 */
	public static void insertElementAt(Object array, Object element,
		int index)
	{
		// If null array or if not an array, return
		if ((array == null) || !array.getClass().isArray())
		{
			return;
		}

		// If zero length array, return
		int len = Utils.length(array);

		if (len == 0)
		{
			return;
		}

		for (int i = len - 1; i > index; i--)
		{
			Array.set(array, i, Array.get(array, i - 1));
		}

		Array.set(array, index, element);
	}

	/**
	 * Checks if each element of a Single Dimensional array is null.
	 *
	 * @param array which has to be checked for null values
	 * @param breakOnNull break from the array when a null value is found
	 * @return true if each element of a single dimensional is null otherwise
	 *         false
	 * @since (2001-05-05.12:27:26)
	 */
	public static boolean isNull(Object array, boolean breakOnNull)
	{
		// If array is null then simply return true
		if (array == null)
		{
			return true;
		}

		int nonNullElementCount = nonNullElementCount(array, breakOnNull);

		if (nonNullElementCount > 0)
		{
			return false;
		}

		return true;
	}

	/**
	 * Checks if element at fromCol of a Single Dimensional array is null.
	 *
	 * @param array source array in which null has to be checked
	 * @param fromCol column number
	 * @return true if element at fromCol of a single dimensional is null
	 *         otherwise false
	 * @since (2001-05-07.09:44:55)
	 */
	public static boolean isNull(Object array, int fromCol)
	{
		// If array is null then simply return true
		int len = Utils.length(array);

		if ((len == 0) || (fromCol < 0) || (fromCol >= len))
		{
			return true;
		}

		Object obj = Array.get(array, fromCol);

		if (obj == null)
		{
			return true;
		}

		// If array, check
		Class<?> cls = obj.getClass();

		if (cls.isArray())
		{
			if (Array.getLength(obj) <= 0)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if each element of a Double Dimensional array is null.
	 *
	 * @param array which has to be checked for null values
	 * @param breakOnNull break from the array when a null value is found
	 * @return true if each element of a Double Dimensional array is null.
	 * @since (2001-05-01.09:40:55)
	 */
	public static boolean isNull(Object array[], boolean breakOnNull)
	{
		// If array is null then simply return true
		int len = Utils.length(array);

		if (len == 0)
		{
			return true;
		}

		boolean bFlag = false;

		for (int i = 0; i < len; i++)
		{
			bFlag = isNull(array[i], breakOnNull);

			if (!bFlag)
			{
				break;
			}
		} // i

		return bFlag;
	}

	/**
	 * Checks if each element of different Double Dimensional arrays is null.
	 *
	 * @param array which has to be checked for null values
	 * @param breakOnNull break from the array when a null value is found
	 * @return true if the array contains only null otherwise false
	 * @since (2001-05-08.09:57:10)
	 */
	public static boolean isNull(Object array[][], boolean breakOnNull)
	{
		// If array is null then simply return true
		int len = Utils.length(array);

		if (len == 0)
		{
			return true;
		}

		boolean bFlag = false;

		for (int i = 0; i < len; i++)
		{
			bFlag = isNull(array[i], breakOnNull);

			if (!bFlag)
			{
				break;
			}
		} // i

		return bFlag;
	}

	/**
	 * Converts Double array to Long array.
	 *
	 * @param array Double array
	 * @return Long array
	 * @since (2013-09-02.17:35:05)
	 */
	public static Long[] longArray(Double array[])
	{
		if (array == null)
		{
			return null;
		}

		int len = array.length;
		Long newArray[] = new Long[len];

		for (int i = 0; i < len; i++)
		{
			newArray[i] = array[i].longValue();
		}

		return newArray;
	}

	/**
	 * Converts Integer array to Long array.
	 *
	 * @param array Integer array
	 * @return Long array
	 * @since (2013-09-02.16:46:05)
	 */
	public static Long[] longArray(Integer array[])
	{
		if (array == null)
		{
			return null;
		}

		int len = array.length;
		Long newArray[] = new Long[len];

		for (int i = 0; i < len; i++)
		{
			newArray[i] = array[i].longValue();
		}

		return newArray;
	}

	/**
	 * Merges two arrays of the same type.
	 *
	 * @param array1 to merge
	 * @param array2 to merge
	 * @return merged array
	 * @since (2002-06-13.11:21:40)
	 */
	public static Object mergeArray(Object array1, Object array2)
	{
		// If null, return null
		if ((array1 == null) && (array2 == null))
		{
			return null;
		}

		Class<?> componentType1 = null;
		Class<?> componentType2 = null;
		Class<?> componentType = null;
		String clsType1 = null;
		String clsType2 = null;
		int dim1 = 0;
		int dim2 = 0;

		if (array1 != null)
		{
			Class<?> cls1 = array1.getClass();
			componentType1 = ReflectionUtils.getComponentType(cls1);
			clsType1 = ReflectionUtils.getClassType(cls1.toString(), false);
			dim1 = ReflectionUtils.getClassDimension(cls1.toString());
		}

		if (array2 != null)
		{
			Class<?> cls2 = array2.getClass();
			componentType2 = ReflectionUtils.getComponentType(cls2);
			clsType2 = ReflectionUtils.getClassType(cls2.toString(), false);
			dim2 = ReflectionUtils.getClassDimension(cls2.toString());
		}

		// If not of the same type, return
		if ((clsType1 != null) && (clsType2 != null) &&
				!ReflectionUtils.isSameClassType(clsType1, clsType2))
		{
			return (array1 != null) ? array1 : array2;
		}

		// Get the length of the specified array Objects
		int len1;

		// Get the length of the specified array Objects
		int len2;

		if (dim1 == dim2)
		{
			len1 = Utils.length(array1);
			len2 = Utils.length(array2);

			componentType = componentType1;
		}
		else if (dim1 == (dim2 - 1))
		{
			len1 = (array1 != null) ? 1 : 0;
			len2 = Utils.length(array2);

			componentType = componentType2;
		}
		else if (dim1 == (dim2 + 1))
		{
			len1 = Utils.length(array1);
			len2 = (array2 != null) ? 1 : 0;

			componentType = componentType1;
		}
		else
		{
			return (array1 != null) ? array1 : array2;
		}

		if (componentType == null)
		{
			Object newArray;

			if (array1 != null)
			{
				newArray = Array.newInstance(componentType1, len1);
				Array.set(newArray, 0, array1);
			}
			else
			{
				newArray = Array.newInstance(componentType2, len2);
				Array.set(newArray, 0, array2);
			}

			return newArray;
		}

		// Create a new array Object with the specified component type and
		// length
		Object newArray = Array.newInstance(componentType, len1 + len2);

		if (array1 != null)
		{
			if (((dim1 == dim2) && (dim1 != 0) && (dim2 != 0)) ||
					(dim1 == (dim2 + 1)))
			{
				System.arraycopy(array1, 0, newArray, 0, len1);
			}
			else
			{
				Array.set(newArray, 0, array1);
			}
		}

		if (array2 != null)
		{
			if (((dim1 == dim2) && (dim1 != 0) && (dim2 != 0)) ||
					(dim1 == (dim2 - 1)))
			{
				System.arraycopy(array2, 0, newArray, len1, len2);
			}
			else
			{
				Array.set(newArray, len1, array2);
			}
		}

		return newArray;
	}

	/**
	 * Multiplies the elements of two arrays.
	 *
	 * @param array1 Integer array1 to multiply
	 * @param array2 Integer array2 to multiply
	 * @return product of the elements of an array
	 * @since (2012-08-22.11:43:35)
	 */
	public static Integer[] multiplyArray(Integer array1[], Integer array2[])
	{
		if (array1 == null)
		{
			return array2;
		}

		if (array2 == null)
		{
			return array1;
		}

		int len1 = array1.length;
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Integer newArray[] = new Integer[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] * array2[i];
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i];
		}

		return newArray;
	}

	/**
	 * Multiplies the elements of two arrays.
	 *
	 * @param array1 Long array1 to multiply
	 * @param array2 Double array2 to multiply
	 * @return product of the elements of an array
	 * @since (2013-09-02.15:29:53)
	 */
	public static Long[] multiplyArray(Long array1[], Double array2[])
	{
		if (array2 == null)
		{
			return array1;
		}

		int len1 = Utils.length(array1);
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Long newArray[] = new Long[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] * array2[i].longValue();
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i].longValue();
		}

		return newArray;
	}

	/**
	 * Counts the number of non-null elements in a Single Dimensional array.
	 *
	 * @param array in which non-null elements have to be counted
	 * @param breakOnNull break from the array when a null value is found
	 * @return number of non-null elements in a Single Dimensional array
	 * @since (2001-05-05.12:28:25)
	 */
	public static int nonNullElementCount(Object array, boolean breakOnNull)
	{
		int nonNullElementCount = 0;

		// If null array, return 0
		if (array == null)
		{
			return nonNullElementCount;
		}

		// If not an array, return 1
		Class<?> cls = array.getClass();

		if (!cls.isArray())
		{
			return 1;
		}

		// If zero length array, return 0
		int len = Utils.length(array);

		if (len == 0)
		{
			return nonNullElementCount;
		}

		for (int i = 0; i < len; i++)
		{
			boolean isNull = (Array.get(array, i) == null);

			if (isNull && breakOnNull)
			{
				break;
			}

			if (!isNull)
			{
				nonNullElementCount++;
			}
		} // i

		return nonNullElementCount;
	}

	/**
	 * Converts boolean array to String array.
	 *
	 * @param array boolean array
	 * @return String array
	 * @since (2014-10-18.14:28:15)
	 */
	private static String[] strArray(boolean array[])
	{
		if (array == null)
		{
			return null;
		}

		int len = array.length;
		String newArray[] = new String[len];

		for (int i = 0; i < len; i++)
		{
			newArray[i] = bool2Str(array[i]);
		}

		return newArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] subArray(Object arr[], int startindex)
	{
		//return subArray(arr, startindex, arr.length - startindex + 1);
		// the cast tells the Sun compiler what to do
		return (T[])subArray(arr, startindex, arr.length - startindex + 1);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] subArray(Object arr[], int startindex, int noOfElem)
	{
		if ((arr == null) || (arr.length == 0))
		{
			return (T[])arr;
		}

		if (arr.length < ((startindex + noOfElem) - 1))
		{
			noOfElem = arr.length - 1;
		}

		Class<?extends Object> arrType = arr[startindex].getClass();

		if (startindex > 0)
		{
			startindex--;
		}

		Object ret = Array.newInstance(arrType, noOfElem + 1);
		System.arraycopy(arr, startindex, ret, 0, noOfElem + 1);

		return (T[])ret;
	}

	/**
	 * Subtracts the elements of two arrays.
	 *
	 * @param array1 Double array1 to subtract
	 * @param array2 Double array2 to subtract
	 * @return difference of the elements of an array
	 * @since (2013-09-02.15:51:56)
	 */
	public static Double[] subtractArray(Double array1[], Double array2[])
	{
		if (array1 == null)
		{
			return array2;
		}

		if (array2 == null)
		{
			return array1;
		}

		int len1 = array1.length;
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Double newArray[] = new Double[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] - array2[i];
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i];
		}

		return newArray;
	}

	/**
	 * Subtracts the elements of two arrays.
	 *
	 * @param array1 Integer array1 to subtract
	 * @param array2 Integer array2 to subtract
	 * @return difference of the elements of an array
	 * @since (2013-07-03.17:28:35)
	 */
	public static Integer[] subtractArray(Integer array1[], Integer array2[])
	{
		if (array1 == null)
		{
			return array2;
		}

		if (array2 == null)
		{
			return array1;
		}

		int len1 = array1.length;
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Integer newArray[] = new Integer[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] - array2[i];
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i];
		}

		return newArray;
	}

	/**
	 * Subtracts the elements of two arrays.
	 *
	 * @param array1 Integer array1 to subtract
	 * @param array2 Long array2 to subtract
	 * @return difference of the elements of an array
	 * @since (2013-07-03.17:35:35)
	 */
	public static Integer[] subtractArray(Integer array1[], Long array2[])
	{
		if (array2 == null)
		{
			return array1;
		}

		int len1 = Utils.length(array1);
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Integer newArray[] = new Integer[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] - array2[i].intValue();
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i].intValue();
		}

		return newArray;
	}

	/**
	 * Subtracts the elements of two arrays.
	 *
	 * @param array1 Long array1 to subtract
	 * @param array2 Integer array2 to subtract
	 * @return difference of the elements of an array
	 * @since (2013-07-03.17:59:35)
	 */
	public static Long[] subtractArray(Long array1[], Integer array2[])
	{
		if (array2 == null)
		{
			return array1;
		}

		int len1 = Utils.length(array1);
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Long newArray[] = new Long[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] - array2[i];
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i];
		}

		return newArray;
	}

	/**
	 * Subtracts the elements of two arrays.
	 *
	 * @param array1 Long array1 to subtract
	 * @param array2 Long array2 to subtract
	 * @return difference of the elements of an array
	 * @since (2013-07-03.18:11:35)
	 */
	public static Long[] subtractArray(Long array1[], Long array2[])
	{
		if (array1 == null)
		{
			return array2;
		}

		if (array2 == null)
		{
			return array1;
		}

		int len1 = array1.length;
		int len2 = array2.length;

		int min = Math.min(len1, len2);
		int max = Math.max(len1, len2);

		Long newArray[] = new Long[max];

		for (int i = 0; i < min; i++)
		{
			newArray[i] = array1[i] - array2[i];
		}

		for (int i = min; i < max; i++)
		{
			newArray[i] = (len1 > len2) ? array1[i] : array2[i];
		}

		return newArray;
	}

	/**
	 * Sums the elements of an array.
	 *
	 * @param Double array to sum
	 * @return sum of the elements of an array
	 * @since (2013-07-16.10:24:56)
	 */
	public static double sumArray(Double array[])
	{
		if (array == null)
		{
			return 0;
		}

		double sum = 0;

		int len = array.length;

		for (int i = 0; i < len; i++)
		{
			if (array[i] != null)
			{
				sum += array[i];
			}
		}

		return sum;
	}

	/**
	 * Sums the elements of an array.
	 *
	 * @param Integer array to sum
	 * @return sum of the elements of an array
	 * @since (2012-08-20.16:10:40)
	 */
	public static int sumArray(Integer array[])
	{
		if (array == null)
		{
			return 0;
		}

		int sum = 0;

		int len = array.length;

		for (int i = 0; i < len; i++)
		{
			if (array[i] != null)
			{
				sum += array[i];
			}
		}

		return sum;
	}

	/**
	 * Sums the elements of an array.
	 *
	 * @param Long array to sum
	 * @return sum of the elements of an array
	 * @since (2012-08-28.13:35:40)
	 */
	public static long sumArray(Long array[])
	{
		if (array == null)
		{
			return 0;
		}

		long sum = 0;

		int len = array.length;

		for (int i = 0; i < len; i++)
		{
			if (array[i] != null)
			{
				sum += array[i];
			}
		}

		return sum;
	}

	/**
	 * Returns a string representation of the contents of the specified array.
	 * The string representation consists of a list of the array's elements,
	 * enclosed in square brackets ("[]") and grouped using curly braces ("{}").
	 * Adjacent elements are separated by the character "," (a comma).
	 *
	 * @param arr the array whose string representation to return
	 * @return a string representation of array
	 * @since (2012-07-17.18:51:05)
	 */
	public static String toString(Object arr[])
	{
		int len1 = Utils.length(arr);

		if (len1 == 0)
		{
			return "";
		}

		if (!arr.getClass().isArray())
		{
			return "";
		}

		// [LMSGOBJ;
		String name = arr.getClass().getName();
		name = name.substring(2, name.length() - 1);			

		StringBuffer sb = new StringBuffer("[");

		for (int i = 0; i < len1; i++)
		{
			if (i != 0)
			{
				sb.append(",");
			}

			sb.append("{");

			String str = arr[i].toString();

			if (!str.startsWith(name + "@"))
			{
				sb.append(str);
			}
			else
			{
				Object obj = Array.get(arr, i);
				Field fields[] = obj.getClass().getDeclaredFields();
				int len2 = fields.length;

				for (int j = 0; j < len2; j++)
				{
					try
					{
						
						fields[j].setAccessible(true);
						Object val = fields[j].get(obj);

						if (val.toString().trim().length() != 0)
						{
							if (j != 0)
							{
								sb.append(",");
							}

							sb.append(fields[j].getName()).append(":");
							sb.append("<").append(val).append(">");
						}
					}
					catch (Exception e)
					{
					}
				} // j
			}

			sb.append("}");
		} // i

		sb.append("]");

		return sb.toString();
	}
}