package com.databorough.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.List;
import static com.databorough.utils.LoggingSNMPTrap.sendSNMPTrap;

import org.aspectj.lang.ProceedingJoinPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements logging aspect using Spring AOP and AspectJ.
 *
 * @author Shantanu
 * @since (2011-02-28.12:18:55)
 */
public class LoggingAspect
{
	public static Logger appLogger =
		LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	private static final String VALUE_SEPARATOR = ":";
	private static final String FIELD_SEPARATOR = ";";

	private static void log(String debugMsg, String infoMsg, String traceMsg,
		boolean isBatch, Object clsObj)
	{
		Logger log = LoggerFactory.getLogger(clsObj.getClass());

		if (debugMsg != null)
		{
			log.debug(debugMsg);

			if (isBatch)
			{
				sendSNMPTrap(debugMsg);
			}
		}

		if (infoMsg != null)
		{
			log.info(infoMsg);

			if (isBatch)
			{
				sendSNMPTrap(infoMsg);
			}
		}

		if (traceMsg != null)
		{
			log.trace(traceMsg);
		}
	}

	public Object log(ProceedingJoinPoint call) throws Throwable
	{
		long time = System.currentTimeMillis();

		String params = "";
		Object args[] = call.getArgs();
		int numArgs = (args != null) ? args.length : 0;

		for (int i = 0; i < numArgs; i++)
		{
			if (i != 0)
			{
				params += ", ";
			}

			params += args[i];
		}

		String className = call.getTarget().getClass().getSimpleName();
		String methodName = call.getSignature().getName();

		if (appLogger.isDebugEnabled())
		{
			// for trace/debug levels
			appLogger.info("");
			appLogger.info("Entered in " + className + "::" + methodName +
				" with params: " + params);
		}

		Object point = null;

		try
		{
			point = call.proceed();
		}
		catch (Exception e)
		{
			appLogger.info("## Exception ##" + e.toString());
		}

		if (!appLogger.isDebugEnabled())
		{
			// if info level
			return point;
		}

		appLogger.info("Left " + className + "::" + methodName +
			" with return as: " + point);

		//if (appLogger.isDebugEnabled() && point instanceof List)
		if (point instanceof List)
		{
			// for trace/debug levels
			logEntities((List<?>)point);
		}

		time = (System.currentTimeMillis() - time);
		appLogger.info("Total time taken: " + time + " milliseconds");

		return point;
	}

	public static void logDebug(String debugMsg, boolean isBatch, Object clsObj)
	{
		log(debugMsg, null, null, isBatch, clsObj);
	}

	/**
	 * Logs the values of the records retrieved from database for the Hibernate
	 * data entities.
	 *
	 * @param entities list of data entities
	 */
	private static void logEntities(List<?> entities)
	{
		if ((entities == null) || (entities.size() == 0))
		{
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("Current Entity: " + entities.get(0).getClass().getName() +
			"\r\n");

		for (Object entity : entities)
		{
			Method methods[] = entity.getClass().getDeclaredMethods();

			for (Method method : methods)
			{
				if (method.getName().startsWith("get") &&
						Modifier.isPublic(method.getModifiers()))
				{
					try
					{
						sb.append(method.getName().substring(3))
						  .append(VALUE_SEPARATOR);
						sb.append(method.invoke(entity).toString())
						  .append(FIELD_SEPARATOR);
						sb.append(" ");
					}
					catch (Exception e)
					{
					}
				}
			} // method

			sb.setLength(sb.length() - 2);

			sb.append("\r\n");
		} // entity

		sb.setLength(sb.length() - 2);

		appLogger.info(sb.toString());
	}

	public static void logInfo(String infoMsg, boolean isBatch, Object clsObj)
	{
		log(null, infoMsg, null, isBatch, clsObj);
	}

	/**
	 * Shows messages for a job that is still active.
	 *
	 * @param logMsg DSPJOBLOG message
	 */
	public static void logJob(String logMsg)
	{
		logMessage(logMsg);
	}

	/**
	 * Shows provided messages into log.
	 *
	 * @param logMsg
	 */
	public static void logMessage(String logMsg)
	{
		appLogger.info(logMsg);
	}

	/**
	 * Prints this throwable and its backtrace to the standard error stream.
	 *
	 * @param e instance of Throwable
	 * @since (2013-04-11.15:11:17)
	 */
	public static void logStackTrace(Throwable e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		pw.flush();
		sw.flush();

		appLogger.info(sw.toString());
	}

	public static void logTrace(String traceMsg, boolean isBatch, Object clsObj)
	{
		log(null, null, traceMsg, isBatch, clsObj);
	}
}