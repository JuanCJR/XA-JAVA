package com.databorough.utils;

import static com.databorough.utils.StringUtils.trim;
import acdemxaMvcprocess.logic.data.MessageObject;

/**
 * Catches exceptions and wraps the error messages into a common
 * BusinessException class and throws it to UI layer or calling clients.
 *
 * @author Amit Arya
 * @since (2012-08-16.15:51:24)
 */
public final class ExceptionUtils
{
	/**
	 * The ExceptionUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private ExceptionUtils()
	{
		super();
	}

	/**
	 * Throws exception whenever a error message is found.
	 *
	 * @param msgObjs array of error messages and fields
	 * @throws BusinessException
	 */
	public static void checkBusinessException(MessageObject msgObjs[])
		throws BusinessException
	{
		if (msgObjs == null)
		{
			return;
		}

		String errFlds[] = new String[msgObjs.length];
		String errMsgs = "";
		int num = 0;

		for (MessageObject msgObj : msgObjs)
		{
			if ((msgObj.getMessageStatus() == 1) &&
					(trim(msgObj.getMessageText()).length() != 0))
			{
				errFlds[num] = msgObj.getMessageField().toString();
				errMsgs += (msgObj.getMessageText().toString().trim() + " | ");
				num++;
			}
		}

		if (num > 0)
		{
			errMsgs = StringUtils.replaceEndString(errMsgs, " | ", " ");
			throw new BusinessException(errFlds, errMsgs);
		}
	}
}
