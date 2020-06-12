package com.databorough.utils;

import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Redirects navigation to login page if user is not logged in.
 *
 * @author Abhishek
 * @since (2012-02-03.14:23:56)
 */
public class LoggedInCheck implements PhaseListener
{
	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent event)
	{
		FacesContext fc = event.getFacesContext();
		UIViewRoot viewRoot = fc.getViewRoot();

		if (viewRoot == null)
		{
			return;
		}

		boolean loginPage = true;

		// Check to see if this is the login page
		String viewId = viewRoot.getViewId();

		if (viewId.indexOf(".xhtml") != -1)
		{
			loginPage = (viewId.indexOf("login.xhtml") != -1) ||
				(viewId.indexOf("sessionInfo.xhtml") != -1);
		}

		if (!loginPage && !loggedIn())
		{
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "login");
		}
	}

	public void beforePhase(PhaseEvent event)
	{
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RESTORE_VIEW;
	}

	/*
	 * Checks whether any user logged in or not.
	 */
	private boolean loggedIn()
	{
		String user = JSFUtils.getSessionParam("user");

		return ((user != null) && !"".equals(user.trim()));
	}
}