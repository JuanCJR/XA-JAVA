package com.databorough.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import acdemxaMvcprocess.daoservices.SpringFramework;

import static com.databorough.utils.DateTimeConverter.getTimeParts;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.ReflectionUtils.getFieldVal;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.StringUtils.replace;
import static com.databorough.utils.Utils.getFunEndIndex;
import static com.databorough.utils.Utils.length;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Runs jobs using Spring Batch which is a lightweight, comprehensive batch
 * framework designed to enable the development of batch applications.
 *
 * @author Shree Ram
 * @since (2012-06-18.11:19:56)
 */
public class JobRunner extends Thread
{
	private static Object obj;
	private static String pgm;
	private static Object paramVals[];
	private static Map<String, String> pgmParams =
		new HashMap<String, String>();
	private static ThreadPoolTaskExecutor taskExecutor;
	private static JobParametersBuilder jobParamsBldr =
		new JobParametersBuilder();
	private static final ThreadLocal<String> threadLocal =
		new ThreadLocal<String>()
		{
			@Override
			protected String initialValue()
			{
				return "XXXXXXXX";
			}
		};

	/**
	 * Changes the batch job attribute according to Switch indicator setting.
	 *
	 * @param changeStr CHGJOB command
	 */
	public static void changeJob(String changeStr)
	{
		// CHGJOB JOB(PAYROLL) JOBPTY(4) OUTPTY(3) SWS(10XXXX00)
		int swsPos = changeStr.indexOf("SWS(");

		if (swsPos == -1)
		{
			return;
		}

		String switchInd = changeStr.substring(swsPos + 4, swsPos + 12);
		threadLocal.set(switchInd);
	}

	/**
	 * Delays the batch job execution to wait for a specified time.
	 *
	 * @param delayStr used to get the time slot
	 */
	public static void delayJob(String delayStr)
	{
		// DLYJOB DLY(15)
		// DLYJOB RSMTIME('000000')
		int dlyPos = delayStr.indexOf("DLY(");
		long waitTime = 0;

		if (dlyPos == -1)
		{
			int rstPos = delayStr.indexOf("RSMTIME(");

			if (rstPos != -1)
			{
				String timeStr =
					delayStr.substring(rstPos + 9, delayStr.length() - 2);
				String timeParts[] = getTimeParts(timeStr);

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeParts[0]));
				cal.set(Calendar.MINUTE, Integer.valueOf(timeParts[1]));

				if (timeParts[2] != null)
				{
					cal.set(Calendar.SECOND, Integer.valueOf(timeParts[2]));
				}

				waitTime = Math.abs(System.currentTimeMillis() -
						cal.getTimeInMillis());
			}
		}
		else
		{
			waitTime = Integer.valueOf(delayStr.substring(dlyPos + 4,
						delayStr.length() - 1)) * 1000;
		}

		Thread t = Thread.currentThread();

		synchronized (t)
		{
			try
			{
				t.wait(waitTime);
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * Ends the batch job execution in a controlled manner.
	 * <p>
	 * The job could be on a job queue, it could be active, or it could have
	 * already completed running.
	 *
	 * @param args arguments for End Job
	 */
	public static void endJob(String... args)
	{
		if (args.length == 0)
		{
			return;
		}

		ThreadGroup tg = taskExecutor.getThreadGroup();
		String groupName = tg.getName();

		if (!groupName.equalsIgnoreCase(args[0]))
		{
			return;
		}

		if (args.length > 2)
		{
			long second = Integer.valueOf(args[2]);

			try
			{
				synchronized (tg)
				{
					tg.wait(second);
				}
			}
			catch (Exception e)
			{
				logStackTrace(e);
			}
		}

		taskExecutor.destroy();
	}

	public static Object[] getParamValues()
	{
		return paramVals;
	}

	public static Map<String, String> getPgmParams()
	{
		return pgmParams;
	}

	/**
	 * Checks if the switch at specified position has a logical value of true.
	 *
	 * @param pos Switch indicator position
	 * @return Switch indicator flag
	 */
	public static boolean isSwitch(int pos)
	{
		pos--;

		char switchInd[] = threadLocal.get().toCharArray();

		return (switchInd[pos] == '1');
	}

	/**
	 * Compares one or more of eight switches with the eight switch settings
	 * already established for the job and returns a logical value of false or
	 * true.
	 *
	 * @param str Switch indicator
	 * @return Switch indicator flag
	 */
	public static boolean isSwitch(String str)
	{
		if ((str == null) || (str.length() == 0))
		{
			return false;
		}

		char switchInd[] = threadLocal.get().toCharArray();

		if (switchInd == null)
		{
			return false;
		}

		if (str.length() < 8)
		{
			str = padStringWithValue(str, "0", 8, true);
		}

		boolean isSwitch = true;
		char checkArr[] = str.toCharArray();

		for (int i = 0; i < checkArr.length; i++)
		{
			if ((checkArr[i] != 'X') && (checkArr[i] != switchInd[i]))
			{
				isSwitch = false;

				break;
			}
		}

		return isSwitch;
	}

	/**
	 * Launches the job in a separate thread group.
	 **/
	public void run()
	{
		new ThreadGroup(pgm);
		run(pgm);
	}

	/**
	 * Runs job with a supplied program name.
	 *
	 * @param pgm used to find the name of class from batchConfig.xml
	 */
	private void run(String pgm)
	{
		if ((pgm == null) || (pgm.length() == 0))
		{
			return;
		}

		Job job;

		try
		{
			// Get bean configured in batchConfig.xml
			Object obj = SpringFramework.getBean(pgm);
			job = (Job)obj;
		}
		catch (Exception e)
		{
			logMessage("Batch program not found: " + pgm);

			return;
		}

		JobLauncher jobLauncher =
			(JobLauncher)SpringFramework.getBean("jobLauncher");
		taskExecutor = (ThreadPoolTaskExecutor)SpringFramework.getBean(
				"taskExecutor");
		taskExecutor.setThreadGroupName(pgm);

		jobParamsBldr.addLong("currTime", new Long(System.currentTimeMillis()));

		JobParameters jobParams = jobParamsBldr.toJobParameters();

		try
		{
			jobLauncher.run(job, jobParams);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Runs SBMJOB command.
	 *
	 * @param cmd SBMJOB command
	 */
	public static void runJob(String cmd)
	{
		// CMD(BILP1MCL SITE(999) PRCNBR(9999999) THRNBR(999) 
		// PRDSQ1(99999) PRCCDE(XXXXXXXXXX) PRDSQBB(99999) RESTRT(X) 
		// PRDXUM(9999999)) JOB(S999B1TH99) JOBQ(XXXXXXXXXX) OUTQ(XXXXXXXXXX)

		// CMD(CALL PGM(BILP1MCL) PARM(999 9999999 999 99999 XXXXXXXXXX 99999 X 
		// 9999999) JOB(S999B1TH99) JOBQ(XXXXXXXXXX) OUTQ(XXXXXXXXXX)

		// RQSDTA(CALL BILP1MCL PARM(999 9999999 999 99999 XXXXXXXXXX 99999 X 
		// 9999999) JOB(S999B1TH99) JOBQ(XXXXXXXXXX) OUTQ(XXXXXXXXXX)

		// CMD(CALL PGM(MENU0305C) PARM(&BJOBNAME &SITE)) JOB(&JOB16)
		String cmdStr = "";
		int pos = cmd.indexOf("CMD");

		if (pos != -1)
		{
			int cmdlen = cmd.indexOf('(', pos) - pos;
			int indxBkt = getFunEndIndex(cmd, "cmd");
			cmdStr = cmd.substring(pos + cmdlen + 1, indxBkt);
		}
		else
		{
			pos = cmd.indexOf("RQSDTA");

			if (pos != -1)
			{
				int cmdlen = cmd.indexOf('(', pos) - pos;
				int indxBkt = getFunEndIndex(cmd, "rqsdta");
				cmdStr = cmd.substring(pos + cmdlen + 1, indxBkt);
			}
		}

		String params[] = cmdStr.split(" ");
		int numParams = params.length;

		// Set program name
		int indxPgm = cmdStr.indexOf("PGM");

		if (indxPgm != -1)
		{
			int indx = cmdStr.indexOf('(', indxPgm + 1);
			pgm = cmdStr.substring(indx + 1, cmdStr.indexOf(')', indx + 1))
						.trim();
		}
		else if (params[0].indexOf("CALL") != -1)
		{
			pgm = params[1];
		}
		else
		{
			pgm = params[0];
		}

		pgm = pgm.toLowerCase();

		jobParamsBldr = new JobParametersBuilder();

		StringBuilder sb = new StringBuilder();

		int indxParm = cmdStr.indexOf("PARM");

		if (indxParm != -1)
		{
			int indx = cmdStr.indexOf('(', indxParm + 1);
			String parm =
				cmdStr.substring(indx + 1, cmdStr.indexOf(')', indx + 1))
						.trim();

			parm = parm.replaceAll("'", "");
			params = parm.split(" ");
			numParams = params.length;

			for (int i = 0; i < numParams; i++)
			{
				params[i] = params[i].trim();

				if (params[i].length() == 0)
				{
					continue;
				}

				if (params[i].startsWith("&"))
				{
					String paramStr =
						getFieldVal(obj, params[i].substring(1), false);

					jobParamsBldr.addString(params[i].substring(1), paramStr);
					sb.append(paramStr + ", ");
				}
				else
				{
					jobParamsBldr.addString(params[i], params[i]);
					sb.append(params[i] + ", ");
				}
			} // i
		}
		else
		{
			for (int i = 1; i < numParams; i++)
			{
				String val;
				int indx = params[i].indexOf('(');

				if (indx != -1)
				{
					if (params[i].endsWith(")"))
					{
						val = params[i].substring(indx + 1,
								params[i].length() - 1);
					}
					else
					{
						val = " ";
						i += 1;
					}
				}
				else
				{
					val = params[i];
				}

				jobParamsBldr.addString(val + i, val);
				sb.append(val + ", ");
			} // i
		}

		if (sb.toString().endsWith(", "))
		{
			sb.setLength(sb.length() - 2);
		}

		pgmParams.put(pgm, sb.toString());

		// Set program params
		paramVals = sb.toString().split(", ");

		// Set Switch indicator
		// SWS(11000000);
		int swsPos = cmd.indexOf("SWS(");

		if (swsPos != -1)
		{
			String switchInd = cmd.substring(swsPos + 4, swsPos + 12);
			jobParamsBldr.addString("switchInd", switchInd);
		}

		new JobRunner().start();
	}

	/**
	 * Runs SBMJOB command.
	 *
	 * @param cmd SBMJOB command
	 * @param object the runtime class of an object
	 */
	public static void runJob(String cmd, Object object)
	{
		obj = object;
		runJob(cmd);
	}

	/**
	 * Runs SBMJOB command.
	 *
	 * @param pgmName Job to submit
	 * @param params SBMJOB parameters
	 */
	public static void runJob(String pgmName, Object params[])
	{
		pgm = pgmName;
		paramVals = params;

		int numParams = length(params);

		for (int i = 0; i < numParams; i++)
		{
			jobParamsBldr.addString(params[i].toString(), params[i].toString());
		}

		new JobRunner().start();
	}

	/**
	 * Sets Switch indicator in thread local.
	 *
	 * @param switchInd Switch indicator
	 */
	public static void setSwitchInd(Object switchInd)
	{
		if (switchInd != null)
		{
			threadLocal.set(switchInd.toString());
		}
		else
		{
			threadLocal.remove();
		}
	}

	/**
	 * Sets switch indicator value at the given position.
	 *
	 * @param pos Switch indicator position
	 * @param val logical value
	 */
	public static void setSwitchInd(int pos, int val)
	{
		threadLocal.set(replace(threadLocal.get(), pos, 1, val));
	}
}
