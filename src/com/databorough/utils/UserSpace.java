package com.databorough.utils;

import java.io.Serializable;

/**
 * User spaces are objects that consist of a collection of bytes used for
 * storing user-defined information.
 *
 * @author Zia Shahid
 * @since (2012-09-07.17:10:12)
 */
public final class UserSpace implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Object userObj;

	public UserSpace(Object userObj)
	{
		this.userObj = userObj;
	}

	public Object getUserObj()
	{
		return userObj;
	}
}