package com.databorough.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Returns a method logger.
 *
 * @author Amit Arya
 * @since (2012-04-13.14:36:12)
 */
public final class MethodLogger
{
	public static Log appLogger = LogFactory.getLog(MethodLogger.class);

	/**
	 * The MethodLogger class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private MethodLogger()
	{
		super();
	}
}