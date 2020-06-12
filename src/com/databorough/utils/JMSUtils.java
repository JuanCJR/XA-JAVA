package com.databorough.utils;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.LoggingAspect.logStackTrace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.jms.core.JmsTemplate;

/**
 * Provides methods for reading, writing, clearing and creating Data Queue using
 * Java Message Service (JMS). JMS API is a Java Message Oriented Middleware
 * (MOM) API for sending messages between two or more clients.
 * <p>
 * Apache ActiveMQ is an open sourced implementation of JMS 1.1 as part of the
 * J2EE 1.4 specification. ActiveMQ provides Spring Support so that it can be
 * easily embedded into Spring applications and configured using Spring's XML
 * configuration mechanism.
 *
 * @author Shree Ram
 * @since (2012-06-19.15:11:12)
 */
public class JMSUtils
{
	private static Log appLogger = LogFactory.getLog(LoggingAspect.class);
	private static JmsTemplate jmsTemplate;
	private static Connection connection;
	private static Session session;

	/**
	 * Clears the Data Queue i.e. clears the list of messages according to that
	 * Data Queue.
	 *
	 * @param obj QCLRDTAQ command arguments
	 */
	public static void clearDataQueue(Object... obj)
	{
		try
		{
			while (jmsTemplate.receive(obj[0].toString()) != null)
			{
				continue;
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Creates the new Data Queue if it does not exists.
	 *
	 * @param obj CRTDTAQ command arguments
	 */
	public static void createDataQueue(Object... obj)
	{
		jmsTemplate.convertAndSend(obj[0].toString(), "");
	}

	/**
	 * Deletes the specified Data Queue from the system (file).
	 *
	 * @param qName Data Queue name
	 */
	public static void deleteDataQueue(String qName)
	{
		clearDataQueue(qName);
	}

	/**
	 * Creates client's active connection to its JMS provider.
	 *
	 * @return client's active connection to its JMS provider
	 */
	private static Connection getConnection()
	{
		try
		{
			connection = jmsTemplate.getConnectionFactory().createConnection();
		}
		catch (Exception e)
		{
			appLogger.info("Message queue server not started.");
		}

		return connection;
	}

	/**
	 * Returns a helper class that simplifies synchronous JMS access code.
	 *
	 * @return a helper class that simplifies synchronous JMS access code
	 */
	public JmsTemplate getJmsTemplate()
	{
		return jmsTemplate;
	}

	/**
	 * Creates a single-threaded context for producing and consuming messages.
	 *
	 * @return a single-threaded context for producing and consuming messages
	 */
	private static Session getSession()
	{
		Connection conn = getConnection();

		if (conn == null)
		{
			return null;
		}

		try
		{
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return session;
	}

	/**
	 * Returns an object that is used to send a message containing a String. It
	 * inherits from the Message interface and adds a text message body.
	 *
	 * @return an object that is used to send a message
	 */
	private static TextMessage getTextMessage()
	{
		Session session = getSession();

		if (session == null)
		{
			return null;
		}

		TextMessage textMessage = null;

		try
		{
			textMessage = session.createTextMessage();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		return textMessage;
	}

	/**
	 * Reads the Data Queue messages from the server.
	 * The key data parameter is set to the actual key of the received data when
	 * the API returns.
	 *
	 * @param obj QRCVDTAQ command arguments
	 * @return Data Queue messages
	 */
	public static String readDataQueue(Object... obj)
	{
		String qName = obj[0].toString();
		jmsTemplate.setReceiveTimeout(3);

		Message message = jmsTemplate.receive(qName);

		if (message == null)
		{
			return "";
		}

		String msg = "";

		if (obj.length >= 8)
		{
			String keyData = obj[7].toString();
			String keyOrder = obj[5].toString();
			msg = readKeyedDataQueue(message, qName, keyData, keyOrder);
		}
		else
		{
			TextMessage textMessage = (TextMessage)message;

			try
			{
				msg = textMessage.getStringProperty("String");
			}
			catch (JMSException e)
			{
				appLogger.info("Message queue server not started.");
			}
		}

		if (obj[3] instanceof String)
		{
			obj[3] = msg;
		}
		else
		{
			setObject(obj[3], msg);
		}

		return msg;
	}

	/**
	 * Reads the Keyed Data Queue messages from the server according to the key
	 * order.
	 *
	 * @param message message object
	 * @param qName Data Queue name
	 * @param keyData key data
	 * @param keyOrder key order
	 * @return key value according to the key order
	 */
	@SuppressWarnings("unchecked")
	private static String readKeyedDataQueue(Message message, String qName,
		String keyData, String keyOrder)
	{
		if (message == null)
		{
			return "";
		}

		String msg = "";

		List<String> keyList = new ArrayList<String>();

		/**
		 * ObjectMessage is used the send the data in object form. Data will be
		 * in the form of key value pair.
		 */
		try
		{
			ObjectMessage objMsg = (ObjectMessage)message;
			Serializable object = objMsg.getObject();
			TreeMap<String, String> keyedMap = (TreeMap<String, String>)object;

			if (Utils.length(keyedMap) != 0)
			{
				keyList.addAll(keyedMap.keySet());
			}

			/**
			 * Default body of ObjectMessage is readOnly. Change to read and
			 * write format by using clearBody() so that message could be
			 * rewritten.
			 */
			objMsg.clearBody();

			if (keyOrder.length() == 0)
			{
				msg = keyedMap.get(keyData);
				resend(qName, keyData, keyedMap, objMsg);

				return msg;
			}

			int size = keyList.size();

			for (int i = 0; i < size; i++)
			{
				int cvalue = keyList.get(i).compareTo(keyData);
				boolean found = false;

				if ("GE".equalsIgnoreCase(keyOrder))
				{
					found = (cvalue >= 0);
				}
				else if ("GT".equalsIgnoreCase(keyOrder))
				{
					found = (cvalue > 0);
				}
				else if ("LT".equalsIgnoreCase(keyOrder))
				{
					found = (cvalue < 0);
				}
				else if ("LE".equalsIgnoreCase(keyOrder))
				{
					found = (cvalue <= 0);
				}
				else
				{
					resend(qName, "", keyedMap, objMsg);

					break;
				}

				if (found)
				{
					String key = keyList.get(i);
					msg = keyedMap.get(key);
					resend(qName, key, keyedMap, objMsg);

					break;
				}
			} // i
		}
		catch (Exception e)
		{
			appLogger.info("Message queue server not started.");
		}

		return msg;
	}

	/**
	 * Resends the message to the server.
	 *
	 * @param qName Data Queue name
	 * @param key key value
	 * @param keyedMap key map
	 * @param objMsg object to send messages
	 */
	private static void resend(String qName, String key,
		TreeMap<String, String> keyedMap, ObjectMessage objMsg)
	{
		if (key.length() != 0)
		{
			keyedMap.remove(key);
		}

		try
		{
			objMsg.setObject(keyedMap);
		}
		catch (JMSException e)
		{
			logStackTrace(e);
		}

		jmsTemplate.convertAndSend(qName, objMsg);
	}

	/**
	 * Retrieves the text description of the Data Queue.
	 *
	 * @param obj QMHQRDQD command arguments
	 * @return the text description
	 */
	public static String retrieveDataQueueDescription(Object... obj)
	{
		jmsTemplate.setReceiveTimeout(3);

		String qName;

		if (obj[3] instanceof Object)
		{
			qName = objectToString(obj[3]).substring(0, 9);
		}
		else
		{
			qName = obj[3].toString();
		}

		return readDataQueue(qName, "", "", obj[0]);
	}

	/**
	 * Retrieves the Data Queue messages from the list without clearing the
	 * list.
	 *
	 * @param obj QMHRDQM command arguments
	 * @return message
	 */
	public static String retrieveDataQueueMessage(Object... obj)
	{
		jmsTemplate.setReceiveTimeout(3);

		String qName;

		if (obj[3] instanceof Object)
		{
			qName = objectToString(obj[3]).substring(0, 9);
		}
		else
		{
			qName = obj[3].toString();
		}

		String msg = readDataQueue(qName, "", "", obj[0]);
		jmsTemplate.convertAndSend(qName, msg);

		return msg;
	}

	/**
	 * The Send Break Message (SNDBRKMSG) command is used to send an immediate
	 * message to one or more work station message queues.
	 *
	 * @param qName Data Queue name
	 * @param msg message text
	 */
	public static void sendBreakMessage(String qName, String msg)
	{
		if ((qName == null) || (qName.length() == 0))
		{
			qName = "SNDBRKMSGQ";
		}

		if (msg.length() == 0)
		{
			return;
		}

		try
		{
			TextMessage textMessage = getTextMessage();

			if (textMessage == null)
			{
				return;
			}

			textMessage.setStringProperty("String", msg);
			jmsTemplate.convertAndSend(qName, textMessage);
		}
		catch (JMSException e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Sets a helper class that is used for message production and synchronous
	 * message reception.
	 * Called from applicationContext.xml.
	 *
	 * @param jmsTemplate a helper class that simplifies synchronous JMS access
	 *        code
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		JMSUtils.jmsTemplate = jmsTemplate;
	}

	/**
	 * Writes the messages into the Data Queue. If Data Queue does not exists
	 * then it creates a new Data Queue and writes the messages into queue.
	 *
	 * @param obj QSNDDTAQ command arguments
	 * @return reply
	 */
	@SuppressWarnings("unchecked")
	public static String writeDataQueue(Object... obj)
	{
		String reply = "1";

		try
		{
			String qName = obj[0].toString();

			if ((obj.length >= 6) && (obj[5].toString().length() != 0))
			{
				String key = obj[5].toString();
				Map<String, String> rcvdmap = new TreeMap<String, String>();
				TreeMap<String, String> map = new TreeMap<String, String>();
				ObjectMessage objMsg = getSession().createObjectMessage();

				jmsTemplate.setReceiveTimeout(3);

				Message msg = jmsTemplate.receive(qName);

				if (msg != null)
				{
					objMsg = (ObjectMessage)msg;

					Serializable object = objMsg.getObject();
					rcvdmap.putAll((TreeMap<String, String>)object);
					objMsg.clearBody();
					clearDataQueue(qName);
				}

				if (rcvdmap.size() != 0)
				{
					map.clear();
					map.putAll(rcvdmap);
				}

				if (obj[3] instanceof String)
				{
					map.put(key, obj[3].toString());
				}
				else
				{
					map.put(key, objectToString(obj[3]));
				}

				objMsg.setObject(map);
				jmsTemplate.convertAndSend(qName, objMsg);
			}
			else
			{
				TextMessage textMessage = getTextMessage();

				if (textMessage == null)
				{
					return "0";
				}

				if (obj[3] instanceof String)
				{
					textMessage.setStringProperty("String", obj[3].toString());
				}
				else
				{
					textMessage.setStringProperty("String",
						objectToString(obj[3]));
				}

				jmsTemplate.convertAndSend(qName, textMessage);
			}
		}
		catch (Exception e)
		{
			logStackTrace(e);
			reply = "0";
		}

		return reply;
	}
}