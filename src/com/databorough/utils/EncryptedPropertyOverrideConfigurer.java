package com.databorough.utils;

import java.util.Enumeration;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;

import org.springframework.util.ObjectUtils;

import static com.databorough.utils.LoggingAspect.logStackTrace;

public class EncryptedPropertyOverrideConfigurer
	extends PropertyOverrideConfigurer
{
	private byte key[];

	@Override
	protected void convertProperties(Properties properties)
	{
		key = hexToBytes((String)properties.get("key"));
		properties.remove("key");

		Enumeration<?> propertyNames = properties.propertyNames();

		while (propertyNames.hasMoreElements())
		{
			String propertyName = (String)propertyNames.nextElement();
			String propertyValue = properties.getProperty(propertyName);
			String convertedValue = convertPropertyValue(propertyValue);

			if (!ObjectUtils.nullSafeEquals(propertyValue, convertedValue))
			{
				properties.setProperty(propertyName, convertedValue);
			}
		}
	}

	protected String convertPropertyValue(String originalValue)
	{
		try
		{
			byte value[] = hexToBytes(originalValue);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

			// Instantiate the cipher
			Cipher cipher = Cipher.getInstance(skeySpec.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			byte original[] = cipher.doFinal(value);

			return new String(original);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return originalValue;
	}

	public static byte[] hexToBytes(String hex)
	{
		char hexArray[] = hex.toCharArray();
		int length = hexArray.length / 2;
		byte raw[] = new byte[length];

		for (int i = 0; i < length; i++)
		{
			int high = Character.digit(hexArray[i * 2], 16);

			int low = Character.digit(hexArray[(i * 2) + 1], 16);
			int value = (high << 4) | low;

			if (value > 127)
			{
				value -= 256;
			}

			raw[i] = (byte)value;
		}

		return raw;
	}
}