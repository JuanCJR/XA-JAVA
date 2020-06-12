package com.databorough.utils.cl;

import com.databorough.utils.AS400Utils;
import com.databorough.utils.JobLog;
import com.databorough.utils.JobRunner;
import com.databorough.utils.LoggingAspect;

/**
 * Provides conversion of Job commands.
 *
 * @author Amit Arya
 * @since (2012-09-06.11:46:00)
 */
public final class JobUtils
{
	/**
	 * The JobUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private JobUtils()
	{
		super();
	}

	/**
	 * Adds an autostart job entry to the specified subsystem description. The
	 * entry identifies the job name and the job description to be used to
	 * automatically start a job.
	 *
	 * @param params ADDAJE command parameters
	 */
	public static void addaje(String params)
	{
	}

	/**
	 * Adds a job queue entry to the specified subsystem description. A job
	 * queue entry identifies a job queue from which jobs are selected for
	 * running in the subsystem.
	 *
	 * @param params ADDJOBQE command parameters
	 */
	public static void addjobqe(String params)
	{
	}

	/**
	 * Changes the batch job attribute according to Switch indicator setting.
	 *
	 * @param cmd CHGJOB command
	 */
	public static void chgjob(String cmd)
	{
		JobRunner.changeJob(cmd);
	}

	/**
	 * Changes the job-related attributes contained in a job description object.
	 *
	 * @param params CHGJOBD command parameters
	 */
	public static void chgjobd(String params)
	{
	}

	/**
	 * Creates a job description object that contains a specific set of
	 * job-related attributes that can be used by one or more jobs.
	 *
	 * @param params CRTJOBD command parameters
	 */
	public static void crtjobd(String params)
	{
	}

	/**
	 * Creates a new job queue. A job queue contains entries for jobs that are
	 * waiting to be processed by the system.
	 *
	 * @param params CRTJOBQ command parameters
	 */
	public static void crtjobq(String params)
	{
	}

	/**
	 * Delays the batch job execution to wait for a specified time.
	 *
	 * @param cmd DLYJOB command
	 */
	public static void dlyjob(String cmd)
	{
		JobRunner.delayJob(cmd);
	}

	/**
	 * Displays the jobs information.
	 *
	 * @param cmd DSPJOB command
	 * @return jobs information
	 */
	public static String dspjob(String cmd)
	{
		return JobLog.displayJobInfo(cmd);
	}

	/**
	 * Shows commands and related messages for a job that is still active when
	 * its job log has not been written.
	 *
	 * @param cmd DSPJOBLOG command
	 */
	public static void dspjoblog(String cmd)
	{
		// DSPJOBLOG JOB(ANDERSON/PAYROLL) OUTPUT(*PRINT)
		LoggingAspect.logJob(dspjob(cmd));
	}

	/**
	 * Ends the batch job execution in a controlled manner.
	 * <p>
	 * The job could be on a job queue, it could be active, or it could have
	 * already completed running.
	 *
	 * @param args arguments for End Job
	 */
	public static void endjob(String... args)
	{
		JobRunner.endJob(args);
	}

	/**
	 * Retrieves the values of one or more job attributes and place the values
	 * into the specified variable.
	 *
	 * @param params RTVJOBA command parameters
	 * @param pgm CL program
	 */
	public static void rtvjoba(String params, Object pgm)
	{
		// RTVJOBA NBR(&JOBNBR) DATE(&JOBDATE) DFTCCSID(&DFTCSID)
		String nbr = CLUtils.getVarName(params, "NBR");
		String date = CLUtils.getVarName(params, "DATE");

		if (!"".equals(nbr))
		{
			String jobNbr = String.valueOf(AS400Utils.getJobNumber());
			CLUtils.setFieldVal(pgm, nbr, jobNbr, false);
		}

		if (!"".equals(date))
		{
			String jobDate = String.valueOf(AS400Utils.getJobDate());
			CLUtils.setFieldVal(pgm, date, jobDate, false);
		}
	}

	/**
	 * Runs SBMJOB command.
	 *
	 * @param cmd SBMJOB command
	 * @param object the runtime class of an object
	 */
	public static void sbmjob(String cmd, Object object)
	{
		JobRunner.runJob(cmd, object);
	}

	/**
	 * Allows the user to display and work with either the overall status of all
	 * output queues or all output queues that match the qualified generic name
	 * specified and to which the user is authorized, or the detailed status of
	 * a specific output queue.
	 *
	 * @param params WRKOUTQ command parameters
	 */
	public static void wrkoutq(String params)
	{
	}

	/**
	 * Allows you to work with all jobs submitted from your work station, job,
	 * or user profile.
	 *
	 * @param params WRKSBMJOB command parameters
	 */
	public static void wrksbmjob(String params)
	{
	}
}