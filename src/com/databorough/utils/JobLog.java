package com.databorough.utils;

import java.util.Map;

import static com.databorough.utils.JobRunner.getPgmParams;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.Utils.setArg;

/**
 * Logs all the active jobs.
 *
 * @author Shree Ram
 * @since (2012-08-03.13:19:56)
 */
public final class JobLog
{
	private static boolean isAlive;

	/**
	 * The JobLog class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private JobLog()
	{
		super();
	}

	/**
	 * Provides the names of all the active jobs.
	 *
	 * @param group a set of threads
	 * @param log buffer
	 */
	private static void activeThreads(ThreadGroup group, StringBuffer log)
	{
		log.append(group.getName());

		int numActiveGroups = group.activeGroupCount();
		ThreadGroup groups[] = new ThreadGroup[numActiveGroups];
		numActiveGroups = group.enumerate(groups, false);

		for (int i = 0; i < numActiveGroups; i++)
		{
			log.append(",");
			activeThreads(groups[i], log);
		}
	}

	/**
	 * Checks whether the job is active.
	 *
	 * @param group a set of threads
	 * @param groupName thread group name
	 * @return whether the job is active
	 */
	private static boolean checkActiveJob(ThreadGroup group, String groupName)
	{
		if (group.getName().equalsIgnoreCase(groupName))
		{
			isAlive = true;
		}

		int numActiveGroups = group.activeGroupCount();
		ThreadGroup groups[] = new ThreadGroup[numActiveGroups];
		numActiveGroups = group.enumerate(groups, false);

		for (int i = 0; i < numActiveGroups; i++)
		{
			checkActiveJob(groups[i], groupName);
		}

		return isAlive;
	}

	/**
	 * Displays all the active jobs.
	 *
	 * @return log of active threads
	 */
	public static String displayActiveJobs()
	{
		ThreadGroup tg = Thread.currentThread().getThreadGroup();

		StringBuffer log = new StringBuffer("ActiveThreads[");
		activeThreads(tg, log);
		logMessage(log.append("]").toString());

		logMessage("Total active threads : " + tg.activeCount());

		return log.toString();
	}

	/**
	 * Displays for the specified job, the following information: 
	 * Job status attributes
	 * Job definition attributes
	 * Job run attributes
	 * Activation group information
	 * Thread information
	 *
	 * @param commandStr DSPJOB command
	 * @return information about the job
	 */
	public static String displayJobInfo(String commandStr)
	{
		if (commandStr.length() == 0)
		{
			return "";
		}

		String groupName = "";
		int jobPos = commandStr.indexOf("JOB(");

		if (jobPos == -1)
		{
			if (commandStr.indexOf("*ALL") != -1)
			{
				// DSPJOB OPTION(*ALL)
				groupName = Thread.currentThread().getThreadGroup().getName();
			}
		}
		else
		{
			// DSPJOB JOB(SMITH/PAYROLL) OUTPUT(*PRINT)
			String str[] = commandStr.split(" ");

			if (str.length >= 2)
			{
				int sPos = str[1].indexOf('/');

				if (sPos != -1)
				{
					groupName = str[1].substring(sPos + 1, str[1].length() - 1);
				}
			}
		}

		StringBuffer sb = new StringBuffer();

		if ("main".equalsIgnoreCase(groupName))
		{
			StackTraceElement stackTrace[] =
				Thread.currentThread().getStackTrace();

			for (StackTraceElement ste : stackTrace)
			{
				if (ste.getClassName().startsWith("java."))
				{
					continue;
				}

				sb.append("Thread isAlive : " + "<" +
					Thread.currentThread().isAlive() + ">\r\n");
				sb.append("Current class : " + "<" + ste.getClassName() +
					".java>\r\n");
				sb.append("Current executing method : " + "<" +
					ste.getMethodName() + "()>");
				logMessage(sb.toString());

				break;
			}
		}
		else
		{
			ThreadGroup tg = Thread.currentThread().getThreadGroup();
			boolean isActive = checkActiveJob(tg, groupName);

			if (isActive)
			{
				Map<String, String> pgmParams = getPgmParams();
				sb.append("Current running job : <" + groupName + ">\r\n");
				sb.append("Job params : <" + pgmParams.get(groupName) + ">");
				logMessage(sb.toString());
			}
		}

		return sb.toString();
	}

	/**
	 * Generates a list of jobs on the system into User Space.
	 *
	 * @param obj QUSLJOB command parameters
	 */
	public static void listJobInfo(Object... obj)
	{
		// Qualified user space
		Object qualUserSpace = obj[0];

		String userSpace =
			DSUtils.objectToString(qualUserSpace).substring(0, 10);

		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		StringBuffer sb = new StringBuffer();
		activeThreads(tg, sb);

		UserSpaceUtils.writeObject(userSpace, sb);
	}

	/**
	 * Lists Subsystem Job Queue Data into User Space.
	 *
	 * @param obj QWDLSJBQ command parameters
	 */
	public static void listJobQueueInfo(Object... obj)
	{
		// Qualified user space
		Object qualUserSpace = obj[0];

		// Qualified subsystem
		Object subSys = obj[2];

		String userSpace =
			DSUtils.objectToString(qualUserSpace).substring(0, 10);
		String jobName = DSUtils.objectToString(subSys).substring(0, 10);

		ThreadGroup tg = Thread.currentThread().getThreadGroup();

		if (checkActiveJob(tg, jobName))
		{
			Map<String, String> pgmParams = JobRunner.getPgmParams();

			StringBuffer sb = new StringBuffer();
			sb.append("Current active job : <" + jobName + ">\r\n");
			sb.append("Job params : <" + pgmParams.get(jobName) + ">");

			UserSpaceUtils.writeObject(userSpace, sb);
		}
	}

	/**
	 * Retrieves job and thread attributes that apply to the thread in which
	 * this API is called. If a thread attribute exists, it is retrieved. If a
	 * thread attribute does not exist, the job attribute for the job in which
	 * this thread is running is retrieved.
	 *
	 * @param params QWCRTVCA command parameters
	 */
	public static void retrieveCurrentAttrs(Object... obj)
	{
	}

	/**
	 * Retrieves information from a job description object and places it into a
	 * single variable in the calling program.
	 *
	 * @param params QWDRJOBD command parameters
	 */
	public static void retrieveJobDescInfo(Object... obj)
	{
	}

	/**
	 * Retrieves specific information about a job.
	 *
	 * @param params QUSRJOBI command parameters
	 */
	public static void retrieveJobInfo(Object... obj)
	{
	}

	/**
	 * Retrieves information associated with a specified job queue.
	 *
	 * @param obj QSPRJOBQ command parameters
	 */
	public static void retrieveJobQueueInfo(Object... obj)
	{
		// Qualified job queue name
		Object qualQName = obj[3];
		String jobQueue = DSUtils.objectToString(qualQName).substring(0, 10);

		StringBuffer sb = new StringBuffer();
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		activeThreads(tg, sb);

		boolean isActive = checkActiveJob(tg, jobQueue);

		if (isActive)
		{
			Map<String, String> pgmParams = JobRunner.getPgmParams();
			sb.append("Current running job : <" + jobQueue + ">\r\n");
			sb.append("Job params : <" + pgmParams.get(jobQueue) + ">");
		}
		else
		{
			sb.append("Job isAlive : <" + isActive + ">\r\n");
		}

		// Receiver variable
		setArg(obj, 0, sb.toString());
	}
}