package com.databorough.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.databorough.utils.DSUtils.objectToString;
import static com.databorough.utils.DSUtils.setObject;
import static com.databorough.utils.LoggingAspect.logStackTrace;

/**
 * Provides methods for reading, writing, clearing and creating Data Queue and
 * makes map object persist by using Serialization technique so that object
 * state could be maintained.
 *
 * @author Shree Ram
 * @since (2012-05-03.10:24:12)
 */
public final class DQUtils
{
	private static Map<String, Object> dqMap = new HashMap<String, Object>();
	private static final String DQ = "dq.ser";

	/**
	 * The DQUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private DQUtils()
	{
		super();
	}

	static class DataQueue implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private List<String> messageList = new ArrayList<String>();

		public List<String> getMessageList()
		{
			return messageList;
		}

		public void setMessageList(String msg)
		{
			messageList.add(msg);
		}
	}

	/**
	 * Clears the Data Queue i.e. clears the list of messages according to that
	 * Data Queue.
	 *
	 * @param obj QCLRDTAQ command arguments
	 */
	@SuppressWarnings("unchecked")
	public static void clearDataQueue(Object... obj)
	{
		Map<String, Object> dMap = null;

		try
		{
			ObjectInputStream in =
				new ObjectInputStream(new FileInputStream(DQ));
			dMap = (Map<String, Object>)in.readObject();
			in.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if (dMap == null)
		{
			return;
		}

		if (dMap.containsKey(obj[0]))
		{
			DataQueue dq = (DataQueue)dMap.get(obj[0]);

			if (dq.getMessageList() != null)
			{
				dq.getMessageList().clear();
			}
		}

		try
		{
			ObjectOutputStream out =
				new ObjectOutputStream(new FileOutputStream(DQ));
			out.writeObject(dMap);
			out.close();
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
		File f = new File(DQ);

		if (dqMap.isEmpty() && f.exists())
		{
			f.delete();
		}

		if (!dqMap.containsKey(obj[0]))
		{
			dqMap.put((String)obj[0], new DataQueue());
		}
	}

	/**
	 * Deletes the specified Data Queue from the system (file).
	 *
	 * @param qName Data Queue name
	 */
	@SuppressWarnings("unchecked")
	public static void deleteDataQueue(String qName)
	{
		Map<String, Object> dMap = null;

		try
		{
			ObjectInputStream in =
				new ObjectInputStream(new FileInputStream(DQ));
			dMap = (Map<String, Object>)in.readObject();
			in.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if (dMap == null)
		{
			return;
		}

		if (!dMap.containsKey(qName))
		{
			return;
		}

		dMap.remove(qName);

		try
		{
			ObjectOutputStream out =
				new ObjectOutputStream(new FileOutputStream(DQ));
			out.writeObject(dMap);
			out.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	/**
	 * Reads the Data Queue messages from the list and clears the list.
	 *
	 * @param obj QRCVDTAQ command arguments
	 * @return message
	 */
	@SuppressWarnings("unchecked")
	public static String readDataQueue(Object... obj)
	{
		Map<String, Object> dMap = null;

		try
		{
			ObjectInputStream in =
				new ObjectInputStream(new FileInputStream(DQ));
			dMap = (Map<String, Object>)in.readObject();
			in.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if ((dMap == null) || !dMap.containsKey(obj[0]))
		{
			return "";
		}

		DataQueue dq = (DataQueue)dMap.get(obj[0]);

		if (dq.getMessageList() == null)
		{
			return "";
		}

		String str = dq.getMessageList().get(0);

		if (!(obj[3] instanceof String))
		{
			setObject(obj[3], str);
			dq.getMessageList().remove(0);

			return "";
		}
		else
		{
			obj[3] = str;
			dq.getMessageList().remove(0);

			return obj[3].toString();
		}
	}

	/**
	 * Retrieves the number of entries currently on the Data Queue.
	 *
	 * @param obj QMHQRDQD command arguments
	 * @return the number of entries
	 */
	@SuppressWarnings("unchecked")
	public static int retrieveDataQueueDescription(Object... obj)
	{
		Map<String, Object> dMap = null;

		try
		{
			ObjectInputStream in =
				new ObjectInputStream(new FileInputStream(DQ));
			dMap = (Map<String, Object>)in.readObject();
			in.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if ((dMap == null) || !dMap.containsKey(obj[0]))
		{
			return 0;
		}

		DataQueue dq = (DataQueue)dMap.get(obj[0]);

		if (dq.getMessageList() == null)
		{
			return 0;
		}

		return dq.getMessageList().size();
	}

	/**
	 * Retrieves the Data Queue messages from the list without clearing the
	 * list.
	 *
	 * @param obj QMHRDQM command arguments
	 * @return message
	 */
	@SuppressWarnings("unchecked")
	public static String retrieveDataQueueMessage(Object... obj)
	{
		Map<String, Object> dMap = null;

		try
		{
			ObjectInputStream in =
				new ObjectInputStream(new FileInputStream(DQ));
			dMap = (Map<String, Object>)in.readObject();
			in.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}

		if ((dMap == null) || !dMap.containsKey(obj[3]))
		{
			return "";
		}

		DataQueue dq = (DataQueue)dMap.get(obj[3]);

		if (dq.getMessageList() == null)
		{
			return "";
		}

		String str = dq.getMessageList().get(0);

		if (!(obj[3] instanceof String))
		{
			setObject(obj[3], str);

			return "";
		}
		else
		{
			obj[3] = str;

			return obj[3].toString();
		}
	}

	/**
	 * Writes the messages into the Data Queue. If Data Queue does not exists
	 * then it creates a new Data Queue and writes the messages into that list.
	 *
	 * @param obj QSNDDTAQ command arguments
	 */
	public static void writeDataQueue(Object... obj)
	{
		File f = new File(DQ);

		if (dqMap.isEmpty() && f.exists())
		{
			f.delete();
		}

		if (!dqMap.containsKey(obj[0]))
		{
			dqMap.put((String)obj[0], new DataQueue());
		}

		DataQueue dq = (DataQueue)dqMap.get(obj[0]);

		if (!(obj[3] instanceof String))
		{
			dq.setMessageList(objectToString(obj[3]));
		}
		else
		{
			dq.setMessageList(obj[3].toString());
		}

		try
		{
			ObjectOutputStream out =
				new ObjectOutputStream(new FileOutputStream(DQ));
			out.writeObject(dqMap);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}
}