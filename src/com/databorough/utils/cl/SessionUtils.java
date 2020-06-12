package com.databorough.utils.cl;

import static acdemxaMvcprocess.daoservices.SpringFramework.getBean;

import com.databorough.utils.JSFUtils;
//import com.databorough.utils.Login;

import org.apache.tomcat.jdbc.pool.DataSource;

public final class SessionUtils
{
	/**
	 * The SessionUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private SessionUtils()
	{
		super();
	}

	/**
	 * Retrieves one or more of the values that are stored and associated with a
	 * user.
	 *
	 * @param params RTVUSRPRF command parameters
	 * @param pgm CL program
	 */
	public static void rtvusrprf(String params, Object pgm)
	{
		// User profile
		String varName = CLUtils.getVarName(params, "RTNUSRPRF");
		CLUtils.setFieldVal(pgm, varName,
			JSFUtils.getSessionParam("user").toString(), true);

		// Initial library
		varName = CLUtils.getVarName(params, "INLPGMLIB");

		DataSource jpds = (DataSource)getBean("datasource");
		String url = jpds.getUrl();
		int indx = url.indexOf("libraries=");

		if (indx != -1)
		{
			int indxSpace = url.indexOf(" ", indx + 1);
			String library = url.substring(indx + 10, indxSpace);
			CLUtils.setFieldVal(pgm, varName, library, true);
		}
	}

	/**
	 * Ends an interactive job or causes all jobs in a group to end.
	 */
	public static void signoff()
	{
		//Login login = (Login)getBean("login");
		//login.logoff();
	}
}
