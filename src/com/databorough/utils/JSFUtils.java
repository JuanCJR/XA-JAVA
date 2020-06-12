package com.databorough.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * A utility class for JSF.
 *
 * @author Amit Arya
 * @since (2005-02-07.14:47:56)
 */
public final class JSFUtils
{
	/**
	 * The JSFUtils class is not to be instantiated, only use its public static
	 * members outside, so I'm keeping the constructor private.
	 */
	private JSFUtils()
	{
		super();
	}

	/**
	 * Removes the bean specific variables from the session.
	 *
	 * @since (2012-10-03.12:06:10)
	 */
	public static void clearSession()
	{
		HttpSession sess = (HttpSession)getExternalContext().getSession(false);
		Enumeration<String> e = sess.getAttributeNames();

		while (e.hasMoreElements())
		{
			String key = e.nextElement();

			if ((key.indexOf("pendingScreensFunction") != -1) ||
					(key.indexOf("pendingRowList") != -1) ||
					key.endsWith("Bean"))
			{
				removeSessionParam(key);
			}
		}
	}

	public static void forwardUrl(String url)
	{
		try
		{
			getExternalContext().dispatch(url);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Returns the ExternalContext instance for this FacesContext instance.
	 *
	 * @return ExternalContext
	 * @since (2013-05-29.17:18:27)
	 */
	public static ExternalContext getExternalContext()
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		if (facesCtx == null)
		{
			return null;
		}

		return facesCtx.getExternalContext();
	}

	public static String[] getFilterKeyParameter()
	{
		HttpServletRequest req =
			(HttpServletRequest)getExternalContext().getRequest();

		ArrayList<String> arrayList = new ArrayList<String>();
		int i = 0;

		while (true)
		{
			String paramVal = req.getParameter("filterKey" + i);

			if (paramVal == null)
			{
				break;
			}

			arrayList.add(URLUtils.decode(paramVal));

			i++;
		}

		if (i > 0)
		{
			return (String[])arrayList.toArray(new String[i]);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the parameters for a component added as f:param tag.
	 *
	 * @param component the base class for all user interface components in JSF
	 * @return an array of optionally named configuration parameters for a
	 *         component
	 * @since (2008-01-04.16:45:40)
	 */
	public static UIParameter[] getParams(UIComponent component)
	{
		if (component == null)
		{
			return null;
		}

		ArrayList<Object> arrayList = new ArrayList<Object>();

		List<UIComponent> uiList = (List<UIComponent>)component.getChildren();
		Iterator<UIComponent> iter = uiList.iterator();

		while (iter.hasNext())
		{
			Object obj = iter.next();

			if (!(obj instanceof UIParameter))
			{
				continue;
			}

			arrayList.add(obj);
		} // while

		return (UIParameter[])arrayList.toArray(
			new UIParameter[arrayList.size()]);
	}

	public static String getRealPath()
	{
		ServletContext con = (ServletContext)getExternalContext().getContext();

		return con.getRealPath("/");
	}

	@SuppressWarnings("unchecked")
	public static <T> T getRequestParam(String key)
	{
		return (T)getExternalContext().getRequestMap().get(key);
	}

	public static String getRequestStringParameter(String key)
	{
		return getExternalContext().getRequestParameterMap().get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSessionParam(String key)
	{
		ExternalContext extCtx = getExternalContext();

		if (extCtx == null)
		{
			return null;
		}

		return (T)extCtx.getSessionMap().get(key);
	}

	/**
	 * Redirects to the specific JSF screen.
	 *
	 * @param screen screen name
	 */
	public static void redirectToScreen(String screen)
	{
		try
		{
			String nextActionHref = screen + ".xhtml";
			ExternalContext extCtx = getExternalContext();
			HttpServletResponse res = (HttpServletResponse)extCtx.getResponse();
			res.sendRedirect(nextActionHref);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	public static Object removeSessionParam(String key)
	{
		return getExternalContext().getSessionMap().remove(key);
	}

	public static void setRequestParam(String key, Object value)
	{
		getExternalContext().getRequestMap().put(key, value);
	}

	public static void setSessionParam(String key, Object value)
	{
		ExternalContext extCtx = getExternalContext();

		if (extCtx == null)
		{
			return;
		}

		extCtx.getSessionMap().put(key, value);
	}

	@SuppressWarnings("deprecation")
	public static void streamPDF(byte byteArray[])
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		HttpServletResponse res = (HttpServletResponse)extCtx.getResponse();

		try
		{
			res.reset();
			res.setContentType("application/pdf");
			res.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
			res.addHeader("Content-Disposition",
				"inline; filename=" + "report.pdf");

			ServletOutputStream output = res.getOutputStream();
			output.write(byteArray);
			output.flush();
			output.close();

			facesCtx.getApplication().getStateManager().saveView(facesCtx);
			facesCtx.responseComplete();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}
}