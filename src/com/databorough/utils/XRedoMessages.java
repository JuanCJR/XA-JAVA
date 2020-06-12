package com.databorough.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public final class XRedoMessages {

	/**
	 * The XRedoMessages class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private XRedoMessages()
	{
		super();
	}

	protected static ClassLoader getCurrentClassLoader(Object defaultObject){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(loader == null)
			loader = defaultObject.getClass().getClassLoader();

		return loader;
	}

	public static String getMessage(String key){
		return getMessage(key, null);
	}

	public static String getMessage(String key, Object[] params){
		String text = null;
		FacesContext context = FacesContext.getCurrentInstance();
		String messageBundle = context.getApplication().getMessageBundle();
		Locale locale = context.getViewRoot().getLocale();
		ResourceBundle bundle 
			= ResourceBundle.getBundle(messageBundle, locale, getCurrentClassLoader(params));
		try{
			text = bundle.getString(key);
		} catch(MissingResourceException e){
			text = "?? key ??";
		}

		if(params != null){
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}
}