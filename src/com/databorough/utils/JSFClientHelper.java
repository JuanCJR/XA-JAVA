package com.databorough.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class JSFClientHelper
{
	private static final ApplicationContext appCtx =
		new ClassPathXmlApplicationContext("ejb3-spring.xml");

	/**
	 * The JSFClientHelper class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private JSFClientHelper()
	{
		super();
	}

	public static Object getBean(String beanName)
	{
		return appCtx.getBean(beanName);
	}
}