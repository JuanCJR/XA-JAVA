package com.databorough.utils;

import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.BooleanConverter;
import javax.faces.convert.ByteConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.DoubleConverter;
import javax.faces.convert.FloatConverter;
import javax.faces.convert.IntegerConverter;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;

public class JSFConverterFactory
{
	/**
	 * The JSFConverterFactory class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private JSFConverterFactory()
	{
		super();
	}

	public static Converter getConverter(Class<?> clazz)
	{
		if (Boolean.class == clazz)
		{
			return new BooleanConverter();
		}

		if (BigDecimalConverter.class == clazz)
		{
			return new BigDecimalConverter();
		}

		if (Integer.class == clazz)
		{
			return new IntegerConverter();
		}

		if (Long.class == clazz)
		{
			return new LongConverter();
		}

		if (Short.class == clazz)
		{
			return new ShortConverter();
		}

		if (Double.class == clazz)
		{
			return new DoubleConverter();
		}

		if (Float.class == clazz)
		{
			return new FloatConverter();
		}

		if (Byte.class == clazz)
		{
			return new ByteConverter();
		}

		return null;
	}
}