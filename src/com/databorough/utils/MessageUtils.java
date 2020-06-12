package com.databorough.utils;

import java.io.IOException;
import java.io.InputStream;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import acdemxaMvcprocess.logic.data.MessageObject;

import static com.databorough.utils.DSUtils.clearObject;
import static com.databorough.utils.StringUtils.blanks;
import static com.databorough.utils.Utils.isBlanks;

public class MessageUtils
{
	private static ClassLoader getCurrentClassLoader(Object defaultObject)
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null)
		{
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	public static String getMessage(String key)
	{
		return getMessage(key, null);
	}

	public static String getMessage(String key, Object params[])
	{
		String text;
		Locale locale;

		FacesContext context = FacesContext.getCurrentInstance();

		if (context != null)
		{
			locale = context.getViewRoot().getLocale();

			String messageBundle = context.getApplication().getMessageBundle();
			ResourceBundle bundle =
				ResourceBundle.getBundle(messageBundle, locale,
					getCurrentClassLoader(params));

			try
			{
				text = bundle.getString(key);
			}
			catch (MissingResourceException e)
			{
				text = key + " not found in messages file";
			}
		}
		else
		{
			locale = Locale.getDefault();

			// messages_en
			String propsName = "messages_" + locale.getLanguage();
			ClassLoader loader = MessageUtils.class.getClassLoader();
			InputStream inputStream =
				loader.getResourceAsStream(propsName + ".properties");

			if ((inputStream == null) && !"en".equals(locale.getLanguage()))
			{
				inputStream =
					loader.getResourceAsStream("messages_en.properties");
			}

			Properties props = new Properties();

			try
			{
				props.load(inputStream);
				text = props.getProperty(key);
			}
			catch (IOException e)
			{
				text = key + " not found in messages file";
			}
		}

		if (params != null)
		{
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}

	/**
	 * Moves messages from a call message queue in the call stack to the call
	 * message queue of an earlier call stack entry.
	 *
	 * @param obj QMHMOVPM command args
	 * @since (2012-09-11.18:56:36)
	 */
	public static void movePgmMessages(Object... obj)
	{
	}

	/**
	 * Receives a message from a call message queue or external message queue
	 * and returns information describing the message.
	 *
	 * @param obj QMHRCVPM/RCVMSG command args
	 * @since (2012-09-11.18:54:36)
	 */
	public static void receivePgmMessage(Object... obj)
	{
	}

	/**
	 * Removes messages from call message queues and the external message queue.
	 *
	 * @param obj QMHRMVPM/RMVMSG command args
	 * @since (2012-09-11.18:59:36)
	 */
	public static void removePgmMessages(Object... obj)
	{
	}

	/**
	 * Resends an escape message from the current call stack entry to the
	 * previous call stack entry in the call stack or to any call stack entry
	 * that is earlier if the optional parameters are used.
	 *
	 * @param obj QMHRSNEM command args
	 * @since (2012-09-11.19:23:36)
	 */
	public static void resendEscapeMessage(Object... obj)
	{
	}

	/**
	 * Retrieves the messages in the zmessages array from cmessages array.
	 *
	 * @param cmessages source messages array
	 * @param zmessages destination messages array 
	 * @param zmsgidx message index to set
	 * @return message index
	 * @since (2014-02-18.14:44:56)
	 */
	public static Integer retrieveErrorMessage(MessageObject cmessages[],
		MessageObject zmessages[], Integer zmsgidx)
	{
		if ((cmessages == null) || (cmessages[1] == null) ||
				(zmessages == null) || (zmessages[1] == null))
		{
			return zmsgidx;
		}

		for (Integer cmsgidx = 1; cmsgidx <= 20; cmsgidx++)
		{
			if (!isBlanks(cmessages[cmsgidx].getMessageId()) ||
					!isBlanks(cmessages[cmsgidx].getMessageText()))
			{
				zmsgidx = Integer.valueOf(zmsgidx + 1);

				if (zmsgidx <= 20)
				{
					zmessages[zmsgidx].setMessageId(
							cmessages[cmsgidx].getMessageId());
					zmessages[zmsgidx].setMessageText(
							cmessages[cmsgidx].getMessageText());
					zmessages[zmsgidx].setMessageStatus(
							cmessages[cmsgidx].getMessageStatus());
					zmessages[zmsgidx].setMessageField(
							cmessages[cmsgidx].getMessageField());
				}
			}
		}

		clearObject(cmessages);

		return zmsgidx;
	}

	/**
	 * Retrieves the message description from message file.
	 *
	 * @param obj QMHRTVM command args
	 * @return message description for message Id
	 * @since (2012-09-04.17:35:36)
	 */
	public static String retrieveMessage(Object... obj)
	{
		String msgId = obj[3].toString();
		String msgDta = obj[5].toString();

		return getMessage(msgId, new String[] { msgDta });
	}

	/**
	 * Loads message from messages_en.properties file using message Id based on
	 * XRTVSUBMSG API.
	 *
	 * @param messageId message Id to retrieve text
	 * @param messageText message text
	 * @param messageSubs message substitute to set
	 * @return message text
	 */
	public static String retrieveMessage(String messageId, String messageText,
		String messageSubs[])
	{
		if (messageSubs == null)
		{
			return messageText;
		}

		Object obj[] = new Object[messageSubs.length];
		System.arraycopy(messageSubs, 1, obj, 0, messageSubs.length - 1);
		NumberUtils.validateNumerics(obj);

		String msgRead = getMessage(messageId);

		if (Utils.length(msgRead) != 0)
		{
			messageText = String.format(msgRead, obj);
		}
		else
		{
			messageText = messageId;
		}

		return messageText;
	}

	/**
	 * Writes the messages to the <code>MessageObject</code> array to be
	 * presented appropriately on the UI.
	 *
	 * @param messages messages set by the logic
	 * @param messageIdx message index to set
	 * @param messageId error message Id to set
	 * @param messageSubs error message substitute to set
	 * @param messageField error field to set
	 * @return message index
	 */
	public static Integer sendErrorMessage(MessageObject messages[],
		Integer messageIdx, String messageId, String messageSubs[],
		String messageField)
	{
		String messageText = "";

		try
		{
			// Retrieve formatted message
			messageText = retrieveMessage(messageId, messageText, messageSubs);
		}
		catch (Exception e)
		{
			messageId = blanks(7);
			messageText = "ERROR (Message unavailable)";
		}

		messageIdx = writeErrorMessage(messages, messageIdx, messageId,
				messageText, 1, messageField);

		return messageIdx;
	}

	/**
	 * Writes the messages to the <code>MessageObject</code> array to be
	 * presented appropriately on the UI.
	 *
	 * @param messages messages set by the logic
	 * @param messageIdx message index to set
	 * @param messageId error message Id to set
	 * @param messageSubs error message substitute to set
	 * @param messageField error field to set
	 * @return message index
	 */
	public static Integer sendInfoMessage(MessageObject messages[],
		Integer messageIdx, String messageId, String messageSubs[],
		String messageField)
	{
		String messageText = "";

		try
		{
			// Retrieve formatted message
			messageText = retrieveMessage(messageId, messageText, messageSubs);
		}
		catch (Exception e)
		{
			messageId = blanks(7);
			messageText = "ERROR (Message unavailable)";
		}

		messageIdx = writeErrorMessage(messages, messageIdx, messageId,
				messageText, 3, messageField);

		return messageIdx;
	}

	/**
	 * Sends a message to a Data Queue so your program can communicate with
	 * another job or user.
	 *
	 * @param obj QMHSNDM command args
	 * @since (2012-09-04.18:35:36)
	 */
	public static void sendNonPgmMessage(Object... obj)
	{
		String msgId = obj[0].toString();
		String msgDta = obj[2].toString();
		String msgQ = obj[5].toString();

		String txtMsg = getMessage(msgId, new String[] { msgDta });
		JMSUtils.writeDataQueue(msgQ, "", "", txtMsg, "", msgId);
	}

	/**
	 * Writes the messages to the <code>MessageObject</code> array to be
	 * presented appropriately on the UI.
	 *
	 * @param messages messages set by the logic
	 * @param messageIdx message index to set
	 * @param messageId error message Id to set
	 * @param messageSubs error message substitute to set
	 * @param messageField error field to set
	 * @return message index
	 */
	public static Integer sendOtherMessage(MessageObject messages[],
		Integer messageIdx, String messageId, String messageSubs[],
		String messageField)
	{
		String messageText = "";

		try
		{
			// Retrieve formatted message
			messageText = retrieveMessage(messageId, messageText, messageSubs);
		}
		catch (Exception e)
		{
			messageId = blanks(7);
			messageText = "ERROR (Message unavailable)";
		}

		messageIdx = writeErrorMessage(messages, messageIdx, messageId,
				messageText, 0, messageField);

		return messageIdx;
	}

	/**
	 * Sends a message to a call message queue or the external message queue.
	 *
	 * @param obj QMHSNDPM/SNDPGMMSG command args
	 * @since (2012-09-11.18:50:36)
	 */
	public static void sendPgmMessage(Object... obj)
	{
	}

	public static Integer setMsgObj(StringBuilder msgId, StringBuilder msgFld,
		Integer msgObjIdx, MessageObject msgObjs[])
	{
		return setMsgObj(String.valueOf(msgId), String.valueOf(msgFld),
			msgObjIdx, msgObjs);
	}

	public static Integer setMsgObj(String msgId, String msgFld,
		Integer msgObjIdx, MessageObject msgObjs[])
	{
		if (msgObjIdx < msgObjs.length)
		{
			msgObjIdx += 1;
			msgObjs[msgObjIdx].setMessageStatus(1);
			msgObjs[msgObjIdx].setMessageId(msgId);
			msgObjs[msgObjIdx].setMessageField(msgFld);
			msgObjs[msgObjIdx].setMessageText(getMessage(msgId));
		}

		return msgObjIdx;
	}

	/**
	 * Writes the messages to the <code>MessageObject</code> array to be
	 * presented appropriately on the UI.
	 *
	 * @param messages messages set by the logic
	 * @param messageIdx message index to set
	 * @param messageId error message Id to set
	 * @param messageText error message text to set
	 * @param messageStatus error message status to set
	 * @param messageField error field to set
	 * @return message index
	 */
	public static Integer writeErrorMessage(MessageObject messages[],
		Integer messageIdx, String messageId, String messageText,
		Integer messageStatus, String messageField)
	{
		messageIdx = Integer.valueOf(messageIdx + 1);

		if (messageIdx <= 20)
		{
			messages[messageIdx].setMessageId(messageId);
			messages[messageIdx].setMessageText(messageText);
			messages[messageIdx].setMessageStatus(messageStatus);
			messages[messageIdx].setMessageField(messageField);
		}

		return messageIdx;
	}
}
