package com.databorough.utils.cl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static acdemxaMvcprocess.daoservices.SpringFramework.getBean;

import com.databorough.utils.JSFUtils;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import com.databorough.utils.Utils;

import com.ibm.as400.access.FTP;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * Provides conversion of TCP/FTP commands.
 *
 * @author Zia Shahid
 * @since (2012-08-07.12:47:12)
 */
public final class TcpFtpUtils
{
	private static FTP ftp;

	/**
	 * The TcpFtpUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private TcpFtpUtils()
	{
		super();
	}

	/**
	 * Ends the RPC RPCBind daemon.
	 */
	public static void endrpcbind()
	{
	}

	/**
	 * Ends a TCP/IP connection.
	 */
	public static void endtcpcnn()
	{
		if (ftp != null)
		{
			try
			{
				ftp.disconnect();
			}
			catch (Exception e)
			{
				return;
			}
		}
	}

	/**
	 * Puts file on the server.
	 *
	 * @param sourceFile file to put
	 * @param targetFile file on the server
	 */
	public static void ftp(String sourceFile, String targetFile)
	{
		/*Connection conn = AS400Utils.getConnection();
		AS400JDBCConnection as400Conn = (AS400JDBCConnection)conn;
		AS400 system = as400Conn.getSystem();
		ftp = new AS400FTP(system);*/

		try
		{
			ftp.put(sourceFile, targetFile);
		}
		catch (IOException ioe)
		{
			logStackTrace(ioe);
		}
	}

	/**
	 * Command to ping any server/client node.
	 *
	 * @param ipadr IP Address of target system
	 * @param numpckt Number of pkts
	 * @param size Size
	 * @param wtTime Wait Time
	 * @param pgm Pgm object from where this command has been called.
	 * @return <code>true</code> if connection could be made
	 */
	public static boolean ping(String ipadr, String numpckt, String size,
		String wtTime)
	{
		boolean canConnect = false;
		String line = "";
		Process process = null;
		Runtime rt = Runtime.getRuntime();

		try
		{
			String cmd = "cmd /c ping " + ipadr;

			if (Utils.length(numpckt) > 0)
			{
				cmd += (" -n " + numpckt);
			}

			if (Utils.length(size) > 0)
			{
				cmd += (" -l " + size);
			}

			if (Utils.length(wtTime) > 0)
			{
				cmd += (" -w " + wtTime);
			}

			logMessage("Command:" + cmd);
			process = rt.exec(cmd);
		}
		catch (Exception e)
		{
		}

		BufferedReader br =
			new BufferedReader(new InputStreamReader(process.getInputStream()));

		for (int i = 0; i < 15; i++)
		{
			try
			{
				line = br.readLine();

				if (line.toUpperCase().startsWith("REPLY FROM"))
				{
					canConnect = true;
				}

				logMessage("Line_" + (i + 1) + ":" + line);
			}
			catch (Exception e)
			{
			}
		}

		return canConnect;
	}

	/**
	 * Starts the Remote Procedure Call (RPC) RPCBind daemon.
	 *
	 * @param params RPCBIND command parameters
	 */
	public static void rpcbind(String params)
	{
		// RPCBIND RTVRPCREG(*YES)
	}

	/**
	 * Starts the FTP client application that transfers files between systems
	 * using the TCP/IP. FTP is an application protocol used for transferring
	 * files to and from a remote system. FTP requires a user ID, and in some
	 * cases a password, to gain access to files on a remote system.
	 */
	public static void strtcpftp()
	{
		// STRTCPFTP ??RMTSYS('')
		String site = "";

		DataSource jpds = (DataSource)getBean("datasource");
		String url = jpds.getUrl();
		int indx = url.indexOf("://");

		if (indx != -1)
		{
			int indxColon = url.indexOf(";", indx + 1);
			site = url.substring(indx + 3, indxColon);
		}

		String user = JSFUtils.getSessionParam("user").toString();
		String pass = JSFUtils.getSessionParam("pass").toString();
		ftp = new FTP(site, user, pass);

		try
		{
			ftp.connect();
		}
		catch (Exception e)
		{
			return;
		}
	}
}
