package com.databorough.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.databorough.utils.LoggingAspect.logStackTrace;

/* ------------------------------------------------------------ */
/** TYPE Utilities.
 * Provides various static utiltiy methods for manipulating types and their
 * string representations.
 *
 * @since Jetty 4.1
 * @author Greg Wilkins (gregw)
 */
public final class TypeUtil
{
	private static final HashMap<String, Class<?>> name2Class =
		new HashMap<String, Class<?>>();

	static
	{
		name2Class.put("boolean", java.lang.Boolean.TYPE);
		name2Class.put("byte", java.lang.Byte.TYPE);
		name2Class.put("char", java.lang.Character.TYPE);
		name2Class.put("double", java.lang.Double.TYPE);
		name2Class.put("float", java.lang.Float.TYPE);
		name2Class.put("int", java.lang.Integer.TYPE);
		name2Class.put("long", java.lang.Long.TYPE);
		name2Class.put("short", java.lang.Short.TYPE);
		name2Class.put("void", java.lang.Void.TYPE);

		name2Class.put("java.lang.Boolean.TYPE", java.lang.Boolean.TYPE);
		name2Class.put("java.lang.Byte.TYPE", java.lang.Byte.TYPE);
		name2Class.put("java.lang.Character.TYPE", java.lang.Character.TYPE);
		name2Class.put("java.lang.Double.TYPE", java.lang.Double.TYPE);
		name2Class.put("java.lang.Float.TYPE", java.lang.Float.TYPE);
		name2Class.put("java.lang.Integer.TYPE", java.lang.Integer.TYPE);
		name2Class.put("java.lang.Long.TYPE", java.lang.Long.TYPE);
		name2Class.put("java.lang.Short.TYPE", java.lang.Short.TYPE);
		name2Class.put("java.lang.Void.TYPE", java.lang.Void.TYPE);

		name2Class.put("java.lang.Boolean", java.lang.Boolean.class);
		name2Class.put("java.lang.Byte", java.lang.Byte.class);
		name2Class.put("java.lang.Character", java.lang.Character.class);
		name2Class.put("java.lang.Double", java.lang.Double.class);
		name2Class.put("java.lang.Float", java.lang.Float.class);
		name2Class.put("java.lang.Integer", java.lang.Integer.class);
		name2Class.put("java.lang.Long", java.lang.Long.class);
		name2Class.put("java.lang.Short", java.lang.Short.class);

		name2Class.put("Boolean", java.lang.Boolean.class);
		name2Class.put("Byte", java.lang.Byte.class);
		name2Class.put("Character", java.lang.Character.class);
		name2Class.put("Double", java.lang.Double.class);
		name2Class.put("Float", java.lang.Float.class);
		name2Class.put("Integer", java.lang.Integer.class);
		name2Class.put("Long", java.lang.Long.class);
		name2Class.put("Short", java.lang.Short.class);

		name2Class.put(null, java.lang.Void.TYPE);
		name2Class.put("string", java.lang.String.class);
		name2Class.put("String", java.lang.String.class);
		name2Class.put("java.lang.String", java.lang.String.class);
	}

	/* ------------------------------------------------------------ */
	private static final HashMap<Class<?>, String> class2Name =
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

		class2Name.put(null, "void");
		name2Class.put("java.lang.String", java.lang.String.class);
	}

	/* ------------------------------------------------------------ */
	private static final HashMap<Class<?>, Method> class2Value =
		new HashMap<Class<?>, Method>();

	static
	{
		try
		{
			Class<?> strClass[] = { java.lang.String.class };

			class2Value.put(java.lang.Boolean.TYPE,
				java.lang.Boolean.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Byte.TYPE,
				java.lang.Byte.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Double.TYPE,
				java.lang.Double.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Float.TYPE,
				java.lang.Float.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Integer.TYPE,
				java.lang.Integer.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Long.TYPE,
				java.lang.Long.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Short.TYPE,
				java.lang.Short.class.getMethod("valueOf", strClass));

			class2Value.put(java.lang.Boolean.class,
				java.lang.Boolean.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Byte.class,
				java.lang.Byte.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Double.class,
				java.lang.Double.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Float.class,
				java.lang.Float.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Integer.class,
				java.lang.Integer.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Long.class,
				java.lang.Long.class.getMethod("valueOf", strClass));
			class2Value.put(java.lang.Short.class,
				java.lang.Short.class.getMethod("valueOf", strClass));
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Holds a map of primitive type to the default value for the type.  Isn't it
	 * odd that there's no way to get this programmatically from the Class objects?
	 */
	protected static final Map<Class<?>, Object> defaults =
		new HashMap<Class<?>, Object>();

	static
	{
		defaults.put(Boolean.class, false);
		defaults.put(Character.class, '\0');
		defaults.put(Byte.class, new Byte("0"));
		defaults.put(Short.class, new Short("0"));
		defaults.put(Integer.class, new Integer(0));
		defaults.put(Long.class, new Long(0L));
		defaults.put(Float.class, new Float(0f));
		defaults.put(Double.class, new Double(0.0));
		defaults.put(BigDecimal.class, new BigDecimal(0));
		defaults.put(Date.class, new Date());
		defaults.put(String.class, "");
	}

	/* ------------------------------------------------------------ */
	private static Class<?> stringArg[] = { java.lang.String.class };

	/* ------------------------------------------------------------ */
	private static int intCacheSize =
		Integer.getInteger("org.mortbay.util.TypeUtil.IntegerCacheSize", 600)
			   .intValue();
	private static Integer integerCache[] = new Integer[intCacheSize];
	private static String integerStrCache[] = new String[intCacheSize];
	private static Integer minusOne = new Integer(-1);

	/**
	 * The TypeUtil class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private TypeUtil()
	{
		super();
	}

	/**
	 * Get all superclasses and interfaces recursively.
	 *
	 * @param clazz The class to start the search with.
	 * @param classes List of classes to which to add all found super classes and interfaces.
	 */
	private static void computeClassHierarchy(Class<?> clazz,
		List<Class<?>> classes)
	{
		for (Class<?> current = clazz; current != null;
				current = current.getSuperclass())
		{
			if (classes.contains(current))
			{
				return;
			}

			classes.add(current);

			for (Class<?> currentInterface : current.getInterfaces())
			{
				computeClassHierarchy(currentInterface, classes);
			}
		}
	}

	/* ------------------------------------------------------------ */
	/**
	 * @param b An ASCII encoded character 0-9 a-f A-F
	 * @return The byte value of the character 0-16.
	 */
	public static byte convertHexDigit(byte b)
	{
		if ((b >= '0') && (b <= '9'))
		{
			return (byte)(b - '0');
		}

		if ((b >= 'a') && (b <= 'f'))
		{
			return (byte)(b - 'a' + 10);
		}

		if ((b >= 'A') && (b <= 'F'))
		{
			return (byte)(b - 'A' + 10);
		}

		return 0;
	}

	public static void dump(Class<?> c)
	{
		System.err.println("Dump: " + c);
		dump(c.getClassLoader());
	}

	public static void dump(ClassLoader cl)
	{
		System.err.println("Dump Loaders:");

		while (cl != null)
		{
			System.err.println("  loader " + cl);
			cl = cl.getParent();
		}
	}

	/**
	 * Utility method used to load a class.  Any time that Stripes needs to load of find a
	 * class by name it uses this method.  As a result any time the classloading strategy
	 * needs to change it can be done in one place!  Currently uses
	 * {@code Thread.currentThread().getContextClassLoader().loadClass(String)}.
	 *
	 * @param name the fully qualified (binary) name of the class to find or load
	 * @return the Class object representing the class
	 * @throws ClassNotFoundException if the class cannot be loaded
	 */

	//@SuppressWarnings("unchecked") // this allows us to assign without casting
	public static Class<?> findClass(String name) throws ClassNotFoundException
	{
		return Thread.currentThread().getContextClassLoader().loadClass(name);
	}

	/* ------------------------------------------------------------ */
	public static byte[] fromHexString(String s)
	{
		if ((s.length() % 2) != 0)
		{
			throw new IllegalArgumentException(s);
		}

		byte array[] = new byte[s.length() / 2];

		for (int i = 0; i < array.length; i++)
		{
			int b = Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16);
			array[i] = (byte)(0xff & b);
		}

		return array;
	}

	/* ------------------------------------------------------------ */
	/** Class from a canonical name for a type.
	 * @param name A class or type name.
	 * @return A class , which may be a primitive TYPE field..
	 */
	public static Class<?> fromName(String name)
	{
		return name2Class.get(name);
	}

	public static Date getDate(String dateValue, String pattern)
		throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		return sdf.parse(dateValue);
	}

	/**
	 * Returns an appropriate default value for the class supplied. Mirrors the defaults used
	 * when the JVM initializes instance variables.
	 *
	 * @param clazz the class for which to find the default value
	 * @return null for non-primitive types and an appropriate wrapper instance for primitives
	 */
	public static Object getDefaultValue(Class<?> clazz)
	{
		return defaults.get(clazz);
	}

	/**
	 * @param type the type to check.
	 *
	 * @return Returns <code>true</code> if <code>type</code> is a iterable type, <code>false</code> otherwise.
	 */
	public static boolean isIterable(Type type)
	{
		if (type instanceof Class && isIterableClass((Class<?>)type))
		{
			return true;
		}

		if (type instanceof ParameterizedType)
		{
			return isIterable(((ParameterizedType)type).getRawType());
		}

		if (type instanceof WildcardType)
		{
			Type upperBounds[] = ((WildcardType)type).getUpperBounds();

			return (upperBounds.length != 0) && isIterable(upperBounds[0]);
		}

		return false;
	}

	/**
	 * Checks whether the specified class parameter is an instance of a
	 * collection class.
	 *
	 * @param clazz <code>Class</code> to check.
	 *
	 * @return <code>true</code> is <code>clazz</code> is instance of a
	 *        collection class, <code>false</code> otherwise.
	 */
	private static boolean isIterableClass(Class<?> clazz)
	{
		List<Class<?>> classes = new ArrayList<Class<?>>();
		computeClassHierarchy(clazz, classes);

		return classes.contains(Iterable.class);
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
			return formalType.equals(actualType);
		}
		else
		{
			return formalType.isAssignableFrom(actualType);
		}
	}

	/* ------------------------------------------------------------ */
	/** Convert int to Integer using cache.
	 */
	public static Integer newInteger(int i)
	{
		if ((i >= 0) && (i < intCacheSize))
		{
			if (integerCache[i] == null)
			{
				integerCache[i] = new Integer(i);
			}

			return integerCache[i];
		}
		else if (i == -1)
		{
			return minusOne;
		}

		return new Integer(i);
	}

	/* ------------------------------------------------------------ */
	public static byte[] parseBytes(String s, int base)
	{
		byte bytes[] = new byte[s.length() / 2];

		for (int i = 0; i < s.length(); i += 2)
		{
			bytes[i / 2] = (byte)parseInt(s, i, 2, base);
		}

		return bytes;
	}

	/* ------------------------------------------------------------ */
	/** Parse an int from a substring.
	 * Negative numbers are not handled.
	 * @param s String
	 * @param offset Offset within string
	 * @param length Length of integer or -1 for remainder of string
	 * @param base base of the integer
	 * @exception NumberFormatException
	 */
	public static int parseInt(String s, int offset, int length, int base)
		throws NumberFormatException
	{
		int value = 0;

		if (length < 0)
		{
			length = s.length() - offset;
		}

		for (int i = 0; i < length; i++)
		{
			char c = s.charAt(offset + i);

			int digit = c - '0';

			if ((digit < 0) || (digit >= base) || (digit >= 10))
			{
				digit = (10 + c) - 'A';

				if ((digit < 10) || (digit >= base))
				{
					digit = (10 + c) - 'a';
				}
			}

			if ((digit < 0) || (digit >= base))
			{
				throw new NumberFormatException(s.substring(offset,
						offset + length));
			}

			value = (value * base) + digit;
		}

		return value;
	}

	/* ------------------------------------------------------------ */
	public static String toHexString(byte b[])
	{
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < b.length; i++)
		{
			int bi = 0xff & b[i];
			int c = '0' + ((bi / 16) % 16);

			if (c > '9')
			{
				c = 'A' + (c - '0' - 10);
			}

			buf.append((char)c);
			c = '0' + (bi % 16);

			if (c > '9')
			{
				c = 'a' + (c - '0' - 10);
			}

			buf.append((char)c);
		}

		return buf.toString();
	}

	/* ------------------------------------------------------------ */
	public static String toHexString(byte b[], int offset, int length)
	{
		StringBuffer buf = new StringBuffer();

		for (int i = offset; i < (offset + length); i++)
		{
			int bi = 0xff & b[i];
			int c = '0' + ((bi / 16) % 16);

			if (c > '9')
			{
				c = 'A' + (c - '0' - 10);
			}

			buf.append((char)c);
			c = '0' + (bi % 16);

			if (c > '9')
			{
				c = 'a' + (c - '0' - 10);
			}

			buf.append((char)c);
		}

		return buf.toString();
	}

	/* ------------------------------------------------------------ */
	/** Canonical name for a type.
	 * @param type A class , which may be a primitive TYPE field.
	 * @return Canonical name.
	 */
	public static String toName(Class<?> type)
	{
		return class2Name.get(type);
	}

	/* ------------------------------------------------------------ */
	/** Convert int to String using cache.
	 */
	public static String toString(int i)
	{
		if ((i >= 0) && (i < intCacheSize))
		{
			if (integerStrCache[i] == null)
			{
				integerStrCache[i] = Integer.toString(i);
			}

			return integerStrCache[i];
		}
		else if (i == -1)
		{
			return "-1";
		}

		return Integer.toString(i);
	}

	/* ------------------------------------------------------------ */
	public static String toString(byte bytes[], int base)
	{
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < bytes.length; i++)
		{
			int bi = 0xff & bytes[i];
			int c = '0' + ((bi / base) % base);

			if (c > '9')
			{
				c = 'a' + (c - '0' - 10);
			}

			buf.append((char)c);
			c = '0' + (bi % base);

			if (c > '9')
			{
				c = 'a' + (c - '0' - 10);
			}

			buf.append((char)c);
		}

		return buf.toString();
	}

	/* ------------------------------------------------------------ */
	/** Convert String value to instance.
	 *
	 * @param type The class of the instance, which may be a primitive TYPE
	 *        field.
	 * @param value The value as a string.
	 * @return The value as an Object.
	 * @throws Exception
	 */
	public static Object valueOf(Class<?> type, String value)
		throws Exception
	{
		try
		{
			if (type.equals(java.lang.String.class))
			{
				return value;
			}

			Method m = (Method)class2Value.get(type);

			if (m != null)
			{
				return m.invoke(null, new Object[] { value });
			}

			if (type.equals(java.lang.Character.TYPE) ||
					type.equals(java.lang.Character.class))
			{
				return new Character(value.charAt(0));
			}

			Constructor<?> c = type.getConstructor(stringArg);

			return c.newInstance(new Object[] { value });
		}

		catch (InvocationTargetException e)
		{
			if (e.getTargetException() instanceof Error)
			{
				throw (Error)(e.getTargetException());
			}
		}

		return null;
	}

	/* ------------------------------------------------------------ */
	/** Convert String value to instance.
	 * @param type classname or type (eg int)
	 * @param value The value as a string.
	 * @return The value as an Object.
	 * @throws Exception
	 */
	public static Object valueOf(String type, String value)
		throws Exception
	{
		return valueOf(fromName(type), value);
	}
}