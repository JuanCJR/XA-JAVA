package com.databorough.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirects to the login page if the session is not valid.
 *
 * @author Amit Arya
 * @since (2010-12-29.17:17:42)
 */
public class SessionTimeoutFilter implements Filter
{
	private String timeoutPage = "login.xhtml";

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * taken out of service.
	 */
	public void destroy()
	{
	}

	/**
	 * Called by the web container each time a request/response pair is passed
	 * through the chain due to a client request for a resource at the end of
	 * the chain.
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param filterChain The filter chain we are processing
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain filterChain) throws IOException, ServletException
	{
		// Ajax call do not function properly in IE 9/IE 10
		((HttpServletResponse) response).setHeader("X-UA-Compatible",
			"IE=EmulateIE8");

		if ((request instanceof HttpServletRequest) &&
				(response instanceof HttpServletResponse))
		{
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse res = (HttpServletResponse)response;

			// Is session expire control required for this request ?
			if (isSessionControlRequiredForThisResource(req))
			{
				// Is session invalid ?
				if (isSessionInvalid(req))
				{
					req.getSession().invalidate();
					String timeoutUrl =
						req.getContextPath() + "/faces/" + getTimeoutPage();
					res.sendRedirect(timeoutUrl);

					return;
				}
			}
		}

		/*
		 * Handles the ViewExpiredException which occurs when your session
		 * timed out or an invalidate session occurred.
		 */
		try
		{
			filterChain.doFilter(request, response);
		}
		catch (ServletException se)
		{
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse res = (HttpServletResponse)response;

			String timeoutUrl =
				req.getContextPath() + "/faces/" + getTimeoutPage();
			String msg = se.getMessage();

			if ((msg != null) &&
					(
						msg.endsWith("could not be restored.") ||
						msg.equals("system")
					))
			{
				req.getSession().invalidate();
				res.sendRedirect(timeoutUrl);

				return;
			}

			throw se;
		}
	}

	public String getTimeoutPage()
	{
		return timeoutPage;
	}

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * placed into service.
	 */
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	/**
	 * Session shouldn't be checked for some pages. For example: for timeout
	 * page..
	 * Since we're redirecting to timeout page from this filter, if we don't
	 * disable session control for it, filter will again redirect to it
	 * and this will be result with an infinite loop...
	 */
	private boolean isSessionControlRequiredForThisResource(
		HttpServletRequest req)
	{
		String requestPath = req.getRequestURI();

		String arr[] = StringUtils.split(requestPath, "/");
		boolean controlRequired =
			Utils.lookupStrInStrArr(getTimeoutPage(), arr, 0) == -1;

		return controlRequired;
	}

	private boolean isSessionInvalid(HttpServletRequest req)
	{
		boolean sessionInValid =
			(req.getRequestedSessionId() != null) &&
			!req.isRequestedSessionIdValid();

		return sessionInValid;
	}

	public void setTimeoutPage(String timeoutPage)
	{
		this.timeoutPage = timeoutPage;
	}
}