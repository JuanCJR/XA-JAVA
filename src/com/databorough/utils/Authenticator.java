package com.databorough.utils;

import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import acdemxaMvcprocess.daoservices.SpringFramework;
import static com.databorough.utils.AS400Utils.getConnectionFromPool;
import static com.databorough.utils.AS400Utils.getUserDescription;
import static com.databorough.utils.LoggingAspect.logStackTrace;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;

import org.apache.commons.codec.binary.Base64;

/**
 * Performs authentication operation for RESTful interactions.
 *
 * @author Robin Rizvi
 * @since (2014-05-26.16:08:56)
 */
@Path("/Login")
public class Authenticator
{
	private static final String secretKey = "cw_S3Q#D%SHVn1T5";
	private static final String sep = "__$$__";

	public static AuthenticationResponse authenticate(String authToken)
	{
		return authenticate(null, null, authToken);
	}

	@GET
	@Path("/authenticationStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public static AuthenticationResponse authenticate(
		@QueryParam("username") String userId,
		@QueryParam("password") String password,
		@QueryParam("token") String authToken)
	{
		try
		{
			String arr[] = getUserIdPassword(authToken);

			if (arr != null)
			{
				userId = arr[0];
				password = arr[1];
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		Connection connection = null;
		AuthenticationResponse auth = new AuthenticationResponse();

		try
		{
			if ((userId == null) || (password == null) || userId.isEmpty() ||
					password.isEmpty())
			{
				throw new Exception();
			}

			DataSource datasource =
				(DataSource)SpringFramework.getBean("datasource");
			connection = getConnectionFromPool(datasource, userId, password);

			if (connection != null)
			{
				auth.setAuthenticated(true);
				IBean.user = userId;
			}

			if (connection instanceof AS400JDBCConnection)
			{
				AS400JDBCConnection as400Conn = (AS400JDBCConnection)connection;
				AS400 system = as400Conn.getSystem();
				String userDesc = getUserDescription(system, userId);
				userId = userDesc;
			}

			auth.setUserDesc(userId);

			if (auth.getAuthenticated())
			{
				if (authToken != null)
				{
					auth.setAuthToken(authToken);
				}
				else
				{
					auth.setAuthToken(getAuthToken(userId, password));
				}
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
		finally
		{
			try
			{
				if (connection != null)
				{
					connection.close();
				}
			}
			catch (SQLException sqe)
			{
				logStackTrace(sqe);
			}
		}

		return auth;
	}

	private static String getAuthToken(String userId, String password)
		throws Exception
	{
		Key key = new SecretKeySpec(secretKey.getBytes("UTF8"), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		String str = userId + sep + password;
		byte token[] = cipher.doFinal(str.getBytes("UTF8"));

		return Base64.encodeBase64URLSafeString(token);
	}

	private static String[] getUserIdPassword(String authToken)
		throws Exception
	{
		if (authToken == null)
		{
			return null;
		}

		Key key = new SecretKeySpec(secretKey.getBytes("UTF8"), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte decodedValue[] = Base64.decodeBase64(authToken);
		byte decryptedValue[] = cipher.doFinal(decodedValue);
		String str = new String(decryptedValue);

		int indx = str.indexOf(sep);
		String userId = str.substring(0, indx);
		String password = str.substring(indx + sep.length(), str.length());

		return new String[] { userId, password };
	}
}
