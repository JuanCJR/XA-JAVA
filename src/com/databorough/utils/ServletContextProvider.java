package com.databorough.utils;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

/**
 * Implements ServletContextAware interface so that it gets notified of the
 * ServletContext (typically determined by the WebApplicationContext) that it
 * runs in.
 * Helps in getting ServletContext from non-spring managed code.
 *
 * @author Amit Arya
 * @since (2014-02-07.13:30:15)
 */
public class ServletContextProvider implements ServletContextAware
{
	private static ServletContext ctx;

	/**
	 * Returns the servlet container attribute with the given name, or null if
	 * there is no attribute by that name.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if no
	 *        attribute exists matching the given name
	 */
	public static Object getAttribute(String name)
	{
		return ctx.getAttribute(name);
	}

	/**
	 * Gets the real path corresponding to the given virtual path.
	 *
	 * @param path the virtual path to be translated to a real path
	 * @return the real path, or null if the translation cannot be performed
	 */
	public static String getRealPath(String path)
	{
		return ctx.getRealPath(path);
	}

	/**
	 * Gets the ServletContext that this object runs in.
	 *
	 * @return ServletContext
	 */
	public static ServletContext getServletContext()
	{
		return ctx;
	}

	/**
	 * Removes the attribute with the given name from the servlet context.
	 *
	 * @param name a String specifying the name of the attribute to be removed
	 */
	public static void removeAttribute(String name)
	{
		ctx.removeAttribute(name);
	}

	/**
	 * Binds an object to a given attribute name in this servlet context.
	 *
	 * @param name a String specifying the name of the attribute
	 * @param object an Object representing the attribute to be bound
	 */
	public static void setAttribute(String name, Object object)
	{
		ctx.setAttribute(name, object);
	}

	/**
	 * Sets the ServletContext that this object runs in.
	 *
	 * @param ctx ServletContext
	 */
	public void setServletContext(ServletContext ctx)
	{
		ServletContextProvider.ctx = ctx;
	}
}