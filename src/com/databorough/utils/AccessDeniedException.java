package com.databorough.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Sends appropriate HTTP status to the client for unauthorized access.
 * 
 * @author Robin Rizvi
 * @since (2014-05-26.16:08:56)
 */
public class AccessDeniedException extends WebApplicationException
{
	private static final long serialVersionUID = 1L;

	public AccessDeniedException()
	{
		super(Response.Status.UNAUTHORIZED);
	}
}