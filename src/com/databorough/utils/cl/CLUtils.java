package com.databorough.utils.cl;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Column;

import com.databorough.utils.AS400Utils;
import com.databorough.utils.DataAreaUtils;
import com.databorough.utils.JMSUtils;
import com.databorough.utils.ServletContextProvider;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logTrace;
import com.databorough.utils.MessageUtils;
import static com.databorough.utils.NumberFormatter.toInt;
import com.databorough.utils.ReflectionUtils;
import static com.databorough.utils.ReflectionUtils.getGetter;
import static com.databorough.utils.ReflectionUtils.getSetter;
import com.databorough.utils.StringUtils;
import static com.databorough.utils.Utils.getLongObjectName;

import com.ibm.as400.access.AS400JDBCConnection;

/**
 * Provide conversion of CL commands.
 *
 * @author Zia
 * @since (2012-07-23.12:24:12)
 */
public final class CLUtils
{
	private static List<String> lstClCommand = new ArrayList<String>();
	private static List<String> lstAS400Command = new ArrayList<String>();

	static
	{
		// Data Area commands
		lstClCommand.add("CRTDTAARA");
		lstClCommand.add("RTVDTAARA");
		lstClCommand.add("DLTDTAARA");
		lstClCommand.add("DSPDTAARA");
		lstClCommand.add("CHGDTAARA");

		// Data Queue commands
		lstClCommand.add("CRTDTAQ");
		lstClCommand.add("DLTDTAQ");
		lstClCommand.add("SNDBRKMSG");
		lstClCommand.add("SNDUSRMSG");
		lstClCommand.add("SNDMSG");

		// Call Message Queue commands
		lstClCommand.add("SNDPGMMSG");
		lstClCommand.add("RCVMSG");
		lstClCommand.add("RMVMSG");

		// Session commands
		lstClCommand.add("RTVUSRPRF");
		lstClCommand.add("SIGNOFF");

		// TCP/FTP Connection commands
		lstClCommand.add("STRTCPFTP");
		lstClCommand.add("ENDTCPCNN");
		lstClCommand.add("RPCBIND");
		lstClCommand.add("ENDRPCBINDN");
		lstClCommand.add("PING");

		// AS400 Management commands
		lstClCommand.add("RTVSYSVAL");
		lstClCommand.add("REN");
		lstClCommand.add("CPYF");
		lstClCommand.add("DLTF");
		lstClCommand.add("CPYSRCF");
		lstClCommand.add("DLTSPLF");
		lstClCommand.add("DSPRDBDIRE");
		lstClCommand.add("RMVENVVAR");

		// Object commands
		lstClCommand.add("ALCOBJ");
		lstClCommand.add("CHGOBJD");
		lstClCommand.add("CHGOBJOWN");
		lstClCommand.add("CHKIN");
		//lstClCommand.add("CHKOBJ");
		lstClCommand.add("CHKOUT");
		lstClCommand.add("CRTDSPF");
		lstClCommand.add("CRTDUPOBJ");
		lstClCommand.add("CRTMSGF");
		lstClCommand.add("CRTPRTF");
		lstClCommand.add("CRTSRCPF");
		lstClCommand.add("DLCOBJ");
		lstClCommand.add("RTVOBJD");
		lstClCommand.add("DLTMSGF");
		lstClCommand.add("DSPFD");
		lstClCommand.add("DSPOBJD");
		lstClCommand.add("MOVOBJ");
		lstClCommand.add("RNMOBJ");
		lstClCommand.add("RST");
		lstClCommand.add("RSTOBJ");
		lstClCommand.add("RVKOBJAUT");
		lstClCommand.add("SAVOBJ");
		lstClCommand.add("RTVMBRD");

		// Override commands
		lstClCommand.add("OVRDBF");
		lstClCommand.add("DLTOVR");
		lstClCommand.add("OVRDSPF");
		lstClCommand.add("OVRPRTF");
		lstClCommand.add("OVRTAPF");

		// Library commands
		lstClCommand.add("ADDLIBLE");
		lstClCommand.add("RMVLIBLE");

		// SQL utils
		lstClCommand.add("OPNQRYF");
		lstClCommand.add("CLOF");

		// Job commands
		lstClCommand.add("ADDAJE");
		lstClCommand.add("ADDJOBQE");
		lstClCommand.add("CHGJOB");
		lstClCommand.add("CHGJOBD");
		lstClCommand.add("CRTJOBD");
		lstClCommand.add("CRTJOBQ");
		lstClCommand.add("DLYJOB");
		lstClCommand.add("DSPJOB");
		lstClCommand.add("DSPJOBLO");
		lstClCommand.add("ENDJOB");
		lstClCommand.add("RTVJOBA");
		lstClCommand.add("SBMJOB");
		lstClCommand.add("WRKOUTQ");
		lstClCommand.add("WRKSBMJO");

		// General commands
		lstClCommand.add("DMPCLPGM");
		lstClCommand.add("CVTDAT");

		// List of commands which will be executed on AS/400
		lstAS400Command.add("CHGPF");
		lstAS400Command.add("CHGLF");
		lstAS400Command.add("CRTPF");
		lstAS400Command.add("CRTLF");
		lstAS400Command.add("CLRPFM");
		lstAS400Command.add("RMVM");
		lstAS400Command.add("RNMM");
		lstAS400Command.add("ADDPFM");
		lstAS400Command.add("ADDLFM");
		lstAS400Command.add("RGZPFM");
	}

	/**
	 * The CLUtils class is not to be instantiated, only use its public
	 * static members outside, so I'm keeping the constructor private.
	 */
	private CLUtils()
	{
		super();
	}

	private static boolean chgDtaAra(String param, Object obj)
	{
		String dtaName = removeLib(getVarName(param, "DTAARA"));
		String dtaVal = getVarName(param, "VALUE");
		int indx1 = dtaName.indexOf('(');

		if (indx1 != -1)
		{
			dtaName = dtaName.substring(0, indx1).trim();
		}

		return DataAreaUtils.changeDataArea(dtaName, dtaVal, obj);
	}

	public static void chkobj(String commandStr, Object obj)
		throws FileNotFoundException
	{
		int index = commandStr.indexOf(' ');

		String param = "";

		if (index != -1)
		{
			param = commandStr.substring(index, commandStr.length()).trim()
							  .toUpperCase();
		}

		int indx1 = param.toUpperCase().indexOf("OBJ");

		if (indx1 == -1)
		{
			return;
		}

		indx1 = param.indexOf('(', indx1 + 1);

		int indx2 = param.indexOf(')', indx1 + 1);

		String objName = param.substring(indx1 + 1, indx2);
		int pos = objName.indexOf('/');

		if (pos != -1)
		{
			objName = objName.substring(pos + 1);
		}

		String objType = getVarName(commandStr, "OBJTYPE");

		if (objType.length() != 0)
		{
			if (objName.startsWith("&"))
			{
				objName = objName.substring(1);
				objName = getFieldVal(obj, objName, false);
			}

			if ("*DTAQ".equalsIgnoreCase(objType))
			{
				throw new FileNotFoundException();
			}
			else if ("*PGM".equalsIgnoreCase(objType))
			{
				String objDesc = getLongObjectName(objName, objType);

				if ((objDesc == null) || objDesc.equals(objName))
				{
					throw new FileNotFoundException("Object " + objName +
						" not found.");
				}
			}
			else if ("*FILE".equalsIgnoreCase(objType))
			{
				String objDesc = getLongObjectName(objName, objType);

				if ((objDesc == null) || objDesc.equals(objName))
				{
					throw new FileNotFoundException("Object " + objName +
						" not found.");
				}
			}
		}
	}

	private static boolean crtDtaAra(String param, Object obj)
	{
		String dtaName = removeLib(getVarName(param, "DTAARA"));
		String dtaVal = getVarName(param, "VALUE");
		String dtaTyp = getDataAreaType(getVarName(param, "TYPE"));
		String dtaLen = getVarName(param, "LEN");

		return DataAreaUtils.createDataArea(dtaName, dtaVal, dtaTyp, dtaLen,
			obj);
	}

	private static void cvtdat(String param, Object obj)
	{
		String date = getVarName(param, "DATE");
		String toVar = getVarName(param, "TOVAR");
		String fromFmt = getVarName(param, "FROMFMT");
		String toFmt = getVarName(param, "TOFMT");
		String toSep = getVarName(param, "TOSEP");
		GenUtils.cvtdat(date, toVar, fromFmt, toFmt, toSep, obj);
	}

	private static boolean dltDtaAra(String param, Object obj)
	{
		String dtaName = removeLib(getVarName(param, "DTAARA"));

		return DataAreaUtils.deleteDataArea(dtaName, obj);
	}

	private static String dspDtaAra(String param, Object obj)
	{
		return DataAreaUtils.displayDataArea(param, obj);
	}

	public static boolean generate(String fileContent, String path)
	{
		FileWriter writer = null;

		try
		{
			writer = new FileWriter(path);
			writer.write(fileContent);
		}
		catch (IOException e)
		{
			logTrace(e.getMessage(), false, null);

			return false;
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
				}
			}
		}

		return true;
	}

	private static String getDataAreaType(String dtaTyp)
	{
		if ("*CHAR".equalsIgnoreCase(dtaTyp))
		{
			dtaTyp = "C";
		}
		else if ("*DEC".equalsIgnoreCase(dtaTyp))
		{
			dtaTyp = "N";
		}
		else if ("*LGL".equalsIgnoreCase(dtaTyp))
		{
			dtaTyp = "L";
		}

		return dtaTyp;
	}

	/**
	 * Gets the field value of the class field.
	 *
	 * @param object the runtime class of an object
	 * @param fieldName field name
	 * @param searchInChild whether to search in children
	 * @return field value
	 * @since (2012-09-10.14:41:19)
	 */
	public static String getFieldVal(Object object, String fieldName,
		boolean searchInChild)
	{
		Class<?> cls;

		if (searchInChild)
		{
			cls = object.getClass().getSuperclass();
		}
		else
		{
			cls = object.getClass();
		}

		try
		{
			Field flds[] = cls.getDeclaredFields();
			Field newFlds[] = new Field[flds.length];

			for (int i = 0, j = 0; i < flds.length; i++)
			{
				if (!ReflectionUtils.isUserDefOrArr(flds[i].getType()))
				{
					if (flds[i].getName().equalsIgnoreCase(fieldName))
					{
						flds[i].setAccessible(true);

						return flds[i].get(object).toString();
					}
				}
				else if (searchInChild)
				{
					newFlds[j] = flds[i];
					j++;
				}
			}

			LinkedHashMap<String, Field> colFldMap =
				new LinkedHashMap<String, Field>();

			try
			{
				ReflectionUtils.getColumnAnnoFldMap(object, colFldMap);

				for (String str : colFldMap.keySet())
				{
					if (str.equals(fieldName))
					{
						return getMethodVal(cls, str, object);
					}
				}
			}
			catch (Exception e)
			{
			}

			if (searchInChild)
			{
				for (int i = 0; i < newFlds.length; i++)
				{
					if (newFlds[i] == null)
					{
						continue;
					}

					Object object1 = newFlds[i].get(object);

					String fldValue = getFieldVal(object1, fieldName, false);

					if (fldValue.length() != 0)
					{
						return fldValue;
					}
				}
			}
		}
		catch (Exception e)
		{
			logMessage(e.getMessage());
		}

		return "";
	}

	private static String getMethodVal(Class<?> childClass, String fieldName,
		Object object) throws Exception
	{
		Method method = null;
		Method methods[] = childClass.getDeclaredMethods();

		for (Method m : methods)
		{
			int modifier = m.getModifiers();

			if (!(Modifier.isFinal(modifier) || Modifier.isStatic(modifier)))
			{
				if (m.isAnnotationPresent(Column.class))
				{
					Column column = m.getAnnotation(Column.class);
					String fldName = column.name();

					if (fieldName.equalsIgnoreCase(fldName.trim()))
					{
						method = m;

						break;
					}
				}
			}
		} // m

		if (method == null)
		{
			return "";
		}

		String name = method.getName();

		if (name.startsWith("get"))
		{
			name = name.substring(3, name.length()).toLowerCase();
		}

		Method getter = getGetter(childClass, name);

		return getter.invoke(object).toString();
	}

	public static String getRealPath()
	{
		return ServletContextProvider.getRealPath("/");
	}

	public static String getVarName(String paramStr, String paramToSrch)
	{
		String varName = "";
		int indx1 = paramStr.indexOf(paramToSrch);

		if (indx1 != -1)
		{
			indx1 = paramStr.indexOf('(', indx1 + 1);

			int indx2 = paramStr.indexOf(')', indx1 + 1);

			varName = paramStr.substring(indx1 + 1, indx2).trim();

			if (varName.startsWith("&"))
			{
				varName = varName.substring(1);
			}
		}

		int indxBrkt = varName.lastIndexOf("(");

		if (indxBrkt != -1)
		{
			varName = varName.substring(indxBrkt + 1);
		}

		return varName;
	}

	private static boolean isDB400Conn()
	{
		Connection proxyConn = AS400Utils.getConnection();

		try
		{
			Field fld = proxyConn.getClass().getDeclaredField("inner");
			fld.setAccessible(true);

			if (fld.get(proxyConn) instanceof AS400JDBCConnection)
			{
				return true;
			}

			fld.setAccessible(false);
		}
		catch (Exception e)
		{
			return false;
		}

		return false;
	}

	private static boolean ping(String params)
	{
		//String rmtSys = getVarName(params, "RMTSYS");
		String ipadr = getVarName(params, "INTNETADR");
		String size = getVarName(params, "PKTLEN");
		String nbrPckt = getVarName(params, "NBRPKT");
		String wtTime = getVarName(params, "WAITTIME");

		return TcpFtpUtils.ping(ipadr, nbrPckt, size, wtTime);
	}

	private static String removeLib(String dtaName)
	{
		int indx = dtaName.indexOf('/');

		if (indx != -1)
		{
			dtaName = dtaName.substring(indx + 1);
		}

		return dtaName;
	}

	private static boolean rtvDtaAra(String param, Object obj)
	{
		String dtaName = "";

		int indx1 = param.indexOf("DTAARA");

		if (indx1 != -1)
		{
			indx1 = param.indexOf('(', indx1 + 1);

			int indx2 = param.indexOf(") ", indx1 + 1);

			if (indx2 == -1)
			{
				indx2 = param.indexOf(')', indx1 + 1);
			}

			dtaName = param.substring(indx1 + 1, indx2).trim();
		}

		dtaName = removeLib(dtaName);

		String retVal = getVarName(param, "RTNVAR");

		indx1 = dtaName.indexOf('(');

		String pos = null;
		String len = null;

		if (indx1 != -1)
		{
			// RTVDTAARA DTAARA(DA1 (2 1)) RTNVAR(&CLVAR1)
			int indx2 = dtaName.indexOf(')', indx1 + 1);
			int indxSpace = dtaName.indexOf(' ', indx1 + 1);

			if (indxSpace != -1)
			{
				pos = dtaName.substring(indx1 + 1, indxSpace);
				len = dtaName.substring(indxSpace + 1, indx2).trim();
			}

			dtaName = dtaName.substring(0, indx1).trim();
		}

		String value = "";

		if (pos != null)
		{
			value = DataAreaUtils.retrieveDataArea(dtaName, toInt(pos),
					toInt(len), obj);
		}
		else
		{
			value = DataAreaUtils.retrieveDataArea(dtaName, obj);
		}

		return setFieldVal(obj, retVal, value, true);
	}

	public static boolean runCommand(String commandStr)
	{
		return runCommand(commandStr, null);
	}

	public static boolean runCommand(String commandStr, Object obj)
	{
		if ((commandStr == null) || "".equals(commandStr))
		{
			return false;
		}

		int index = commandStr.indexOf(' ');
		String cmd;
		String params;

		if (index != -1)
		{
			cmd = commandStr.substring(0, index).toUpperCase().trim();
			params = commandStr.substring(index, commandStr.length()).trim()
							   .toUpperCase();
		}
		else
		{
			cmd = commandStr.toUpperCase();
			params = "";
		}

		if (lstClCommand.contains(cmd))
		{
			// Data Area utils
			if ("CRTDTAARA".equalsIgnoreCase(cmd))
			{
				crtDtaAra(params, obj);
			}
			else if ("RTVDTAARA".equalsIgnoreCase(cmd))
			{
				rtvDtaAra(params, obj);
			}
			else if ("DLTDTAARA".equalsIgnoreCase(cmd))
			{
				dltDtaAra(params, obj);
			}
			else if ("DSPDTAARA".equalsIgnoreCase(cmd))
			{
				dspDtaAra(params, obj);
			}
			else if ("CHGDTAARA".equalsIgnoreCase(cmd))
			{
				chgDtaAra(params, obj);
			}

			// Data Queue utils
			else if ("CRTDTAQ".equalsIgnoreCase(cmd))
			{
				// CRTDTAQ DTAQ(LIB/DEPTADTA) MAXLEN(100) AUT(*EXCLUDE)
				String queueName = removeLib(getVarName(params, "DTAQ"));

				if (queueName.startsWith("&"))
				{
					// CRTDTAQ DTAQ(&WRKLIB/&QUENAM) MAXLEN(256) AUT(*EXCLUDE) 
					queueName = getFieldVal(obj, queueName.substring(1), false);
				}

				JMSUtils.createDataQueue(queueName);
			}
			else if ("DLTDTAQ".equalsIgnoreCase(cmd))
			{
				// DLTDTAQ DTAQ(LIB/DEPTADTAQ)
				String queueName = removeLib(getVarName(params, "DTAQ"));

				if (queueName.startsWith("&"))
				{
					queueName = getFieldVal(obj, queueName.substring(1), false);
				}

				JMSUtils.deleteDataQueue(queueName);
			}
			else if ("SNDBRKMSG".equalsIgnoreCase(cmd))
			{
				// SNDBRKMSG MSG('Inventory application shuts down at 4:00 PM.')
				// SNDBRKMSG MSG('Your printed output is ready.')
				// TOMSGQ(GEORGEMSGQ)
				String msg = getVarName(params, "MSG");
				String queueName = removeLib(getVarName(params, "TOMSGQ"));
				JMSUtils.sendBreakMessage(queueName, msg);
			}
			else if ("SNDUSRMSG".equalsIgnoreCase(cmd))
			{
				// SNDUSRMSG     MSG(\"Re-run Toll Usage ended in Error 
				// for site \" *CAT &SITE *CAT \"ok\") TOMSGQ(&WSID)
				String msg = getVarName(params, "MSG");
				String queueName = removeLib(getVarName(params, "TOMSGQ"));

				if (params.indexOf("TOMSGQ(&") != -1)
				{
					queueName = getFieldVal(obj, queueName, false);
				}

				if (msg.indexOf("*CAT") == -1)
				{
					return false;
				}

				if (msg.indexOf('"') == -1)
				{
					return false;
				}

				String msgs[] = msg.split("\"");
				int numMsgs = msgs.length;

				for (int i = 0; i < numMsgs; i++)
				{
					if (msgs[i].indexOf("*CAT") == -1)
					{
						continue;
					}

					String catStrArr[] = msgs[i].split(" ");
					int numCats = catStrArr.length;

					for (int j = 0; j < numCats; j++)
					{
						if (catStrArr[j].indexOf("*CAT") != -1)
						{
							catStrArr[j] = "";
						}
						else if (catStrArr[j].indexOf('&') != -1)
						{
							catStrArr[j] = getFieldVal(obj,
									catStrArr[j].substring(1).trim(), false);
						}
					}

					msgs[i] = StringUtils.join(catStrArr, "");
				} // i

				JMSUtils.writeDataQueue(queueName, StringUtils.join(msgs, ""));
			}
			else if ("SNDMSG".equalsIgnoreCase(cmd))
			{
				// SNDMSG     MSG(\"Auto execution NIGHTLY procedure \" \"could not run 
				// MENU03 Option 17.\") TOUSR(*SYSOPR)
				String msg = getVarName(params, "MSG");
				String queueName = removeLib(getVarName(params, "TOMSGQ"));

				if (queueName.length() == 0)
				{
					queueName = removeLib(getVarName(params, "TOUSR"));
				}
				else
				{
					int pos = params.indexOf("TOMSGQ(&");

					if (pos != -1)
					{
						queueName = getFieldVal(obj, queueName, false);
					}
				}

				if (msg.length() != 0)
				{
					// MSG(Interface &SITEA/&IFCD Job Description not found. Interface not  started.)
					String msgStr[] = msg.split(" ");

					for (int j = 0; j < msgStr.length; j++)
					{
						if (msgStr[j].startsWith("&"))
						{
							if (msgStr[j].indexOf('/') != -1)
							{
								String strArr[] = msgStr[j].split("/");
								String str =
									getFieldVal(obj, strArr[0].substring(1),
										false);

								str += "/";

								if (strArr[1].startsWith("&"))
								{
									str += getFieldVal(obj,
										strArr[1].substring(1), false);
								}

								msgStr[j] = str;
							}
							else
							{
								msgStr[j] = getFieldVal(obj,
										msgStr[j].substring(1), false);
							}
						}
					}

					msg = StringUtils.join(msgStr, " ");
				}

				JMSUtils.writeDataQueue(queueName, msg);
			}

			// Call Message Queue commands
			else if ("SNDPGMMSG".equalsIgnoreCase(cmd))
			{
				MessageUtils.sendPgmMessage(params, obj);
			}
			else if ("RCVMSG".equalsIgnoreCase(cmd))
			{
				MessageUtils.receivePgmMessage(params, obj);
			}
			else if ("RMVMSG".equalsIgnoreCase(cmd))
			{
				MessageUtils.removePgmMessages(params, obj);
			}

			// Session utils
			else if ("RTVUSRPRF".equalsIgnoreCase(cmd))
			{
				SessionUtils.rtvusrprf(params, obj);
			}
			else if ("SIGNOFF".equalsIgnoreCase(cmd))
			{
				SessionUtils.signoff();
			}

			// TCP/FTP Connection utils
			else if ("STRTCPFTP".equalsIgnoreCase(cmd))
			{
				TcpFtpUtils.strtcpftp();
			}
			else if ("ENDTCPCNN".equalsIgnoreCase(cmd))
			{
				TcpFtpUtils.endtcpcnn();
			}
			else if ("RPCBIND".equalsIgnoreCase(cmd))
			{
				TcpFtpUtils.rpcbind(params);
			}
			else if ("ENDRPCBINDN".equalsIgnoreCase(cmd))
			{
				TcpFtpUtils.endrpcbind();
			}
			else if ("PING".equalsIgnoreCase(cmd))
			{
				ping(params);
			}

			// AS400 Management utils
			else if ("RTVSYSVAL".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.rtvSysVal(params, obj);
			}
			else if ("REN".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.ren(params);
			}
			else if ("CPYF".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.cpyf(params);
			}
			else if ("DLTF".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.dltf(params);
			}
			else if ("CPYSRCF".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.cpysrcf(params);
			}
			else if ("DLTSPLF".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.dltsplf(params);
			}
			else if ("DSPRDBDIRE".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.dsprdbdire(params);
			}
			else if ("RMVENVVAR".equalsIgnoreCase(cmd))
			{
				AS400MgmtUtils.dsprdbdire(params);
			}

			// Object utils
			else if ("ALCOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.alcobj(params);
			}
			else if ("DLCOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.dlcobj(params);
			}
			else if ("RTVOBJD".equalsIgnoreCase(cmd))
			{
				ObjectUtils.rtvobjd(params, obj);
			}
			else if ("DSPFD".equalsIgnoreCase(cmd))
			{
				ObjectUtils.dspfd(params);
			}
			else if ("RNMOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.rnmobj(params);
			}
			else if ("CRTDUPOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.crtdupobj(params);
			}
			else if ("CHGOBJD".equalsIgnoreCase(cmd))
			{
				ObjectUtils.chgobjd(params);
			}
			else if ("CHGOBJOWN".equalsIgnoreCase(cmd))
			{
				ObjectUtils.chgobjown(params);
			}
			else if ("CHKIN".equalsIgnoreCase(cmd))
			{
				ObjectUtils.chkin(params);
			}
			else if ("CHKOUT".equalsIgnoreCase(cmd))
			{
				ObjectUtils.chkout(params);
			}
			else if ("CRTDSPF".equalsIgnoreCase(cmd))
			{
				ObjectUtils.crtdspf(params);
			}
			else if ("CRTMSGF".equalsIgnoreCase(cmd))
			{
				ObjectUtils.crtmsgf(params);
			}
			else if ("CRTPRTF".equalsIgnoreCase(cmd))
			{
				ObjectUtils.crtprtf(params);
			}
			else if ("CRTSRCPF".equalsIgnoreCase(cmd))
			{
				ObjectUtils.crtsrcpf(params);
			}
			else if ("DLTMSGF".equalsIgnoreCase(cmd))
			{
				ObjectUtils.dltmsgf(params);
			}
			else if ("DSPOBJD".equalsIgnoreCase(cmd))
			{
				ObjectUtils.dspobjd(params);
			}
			else if ("MOVOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.movobj(params);
			}
			else if ("RST".equalsIgnoreCase(cmd))
			{
				ObjectUtils.rst(params);
			}
			else if ("RSTOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.rstobj(params);
			}
			else if ("RVKOBJAUT".equalsIgnoreCase(cmd))
			{
				ObjectUtils.rvkobjaut(params);
			}
			else if ("SAVOBJ".equalsIgnoreCase(cmd))
			{
				ObjectUtils.savobj(params);
			}
			else if ("RTVMBRD".equalsIgnoreCase(cmd))
			{
				MemberUtils.rtvmbrd(params, obj);
			}

			// Override utils
			else if ("OVRDBF".equalsIgnoreCase(cmd))
			{
				OvrrdUtils.ovrdbf(params);
			}
			else if ("DLTOVR".equalsIgnoreCase(cmd))
			{
				OvrrdUtils.dltovr(params);
			}
			else if ("OVRDSPF".equalsIgnoreCase(cmd))
			{
				OvrrdUtils.ovrdspf(params);
			}
			else if ("OVRPRTF".equalsIgnoreCase(cmd))
			{
				OvrrdUtils.ovrprtf(params);
			}
			else if ("OVRTAPF".equalsIgnoreCase(cmd))
			{
				OvrrdUtils.ovrtapf(params);
			}

			// Library utils			
			else if ("ADDLIBLE".equalsIgnoreCase(cmd))
			{
				LibUtils.addlible(params, obj);
			}
			else if ("RMVLIBLE".equalsIgnoreCase(cmd))
			{
				LibUtils.rmvlible(params, obj);
			}

			// SQL utils
			else if ("OPNQRYF".equalsIgnoreCase(cmd))
			{
				SqlUtils.opnqryf(params, obj);
			}
			else if ("CLOF".equalsIgnoreCase(cmd))
			{
				SqlUtils.clof(params);
			}

			// Job utils
			else if ("ADDAJE".equalsIgnoreCase(cmd))
			{
				JobUtils.addaje(params);
			}
			else if ("ADDJOBQE".equalsIgnoreCase(cmd))
			{
				JobUtils.addjobqe(params);
			}
			else if ("CHGJOB".equalsIgnoreCase(cmd))
			{
				JobUtils.chgjob(commandStr);
			}
			else if ("CHGJOBD".equalsIgnoreCase(cmd))
			{
				JobUtils.chgjobd(params);
			}
			else if ("CRTJOBD".equalsIgnoreCase(cmd))
			{
				JobUtils.crtjobd(params);
			}
			else if ("CRTJOBQ".equalsIgnoreCase(cmd))
			{
				JobUtils.crtjobq(params);
			}
			else if ("DLYJOB".equalsIgnoreCase(cmd))
			{
				JobUtils.dlyjob(commandStr);
			}
			else if ("DSPJOB".equalsIgnoreCase(cmd))
			{
				JobUtils.dspjob(commandStr);
			}
			else if ("DSPJOBLOG".equalsIgnoreCase(cmd))
			{
				JobUtils.dspjoblog(commandStr);
			}
			else if ("ENDJOB".equalsIgnoreCase(cmd))
			{
				// ENDJOB JOB(999999/USER/JOBNAME) DELAY(300)
				String strArr[] = params.split(" ");
				String jobName = "";

				if (strArr[0].indexOf('/') != -1)
				{
					jobName = strArr[0].substring(strArr[0].lastIndexOf("/") +
							1, strArr[0].length() - 1);
				}
				else
				{
					jobName = strArr[0].substring(strArr[0].lastIndexOf("JOB(") +
							1, strArr[0].length() - 1);
				}

				for (int i = 0; i < strArr.length; i++)
				{
					int pos = strArr[i].indexOf("DELAY(");

					if (pos != -1)
					{
						String timeStr =
							strArr[i].substring(pos + 6, strArr[i].length() -
								1);
						JobUtils.endjob(jobName, "", timeStr);
					}
				}
			}
			else if ("RTVJOBA".equalsIgnoreCase(cmd))
			{
				JobUtils.rtvjoba(params, obj);
			}
			else if ("SBMJOB".equalsIgnoreCase(cmd))
			{
				JobUtils.sbmjob(params, obj);
			}
			else if ("WRKOUTQ".equalsIgnoreCase(cmd))
			{
				JobUtils.wrkoutq(params);
			}
			else if ("WRKSBMJO".equalsIgnoreCase(cmd))
			{
				JobUtils.wrksbmjob(params);
			}

			// General utils
			else if ("DMPCLPGM".equalsIgnoreCase(cmd))
			{
				GenUtils.dmpclpgm(params, obj);
			}
			else if ("CVTDAT".equalsIgnoreCase(cmd))
			{
				cvtdat(params, obj);
			}
		}
		else if (lstAS400Command.contains(cmd))
		{
			// Database utils
			// If DB is other than DB400, then following db utils will be used
			// Otherwise, these commands will be executed at AS400
			if (!isDB400Conn())
			{
				if ("CHGPF".equalsIgnoreCase(cmd))
				{
					DbaseUtils.chgpf(params);
				}
				else if ("CHGLF".equalsIgnoreCase(cmd))
				{
					DbaseUtils.chglf(params);
				}
				else if ("CRTPF".equalsIgnoreCase(cmd))
				{
					DbaseUtils.crtpf(params);
				}
				else if ("CRTLF".equalsIgnoreCase(cmd))
				{
					DbaseUtils.crtlf(params);
				}
				else if ("CLRPFM".equalsIgnoreCase(cmd))
				{
					DbaseUtils.clrpfm(params);
				}
				else if ("RMVM".equalsIgnoreCase(cmd))
				{
					DbaseUtils.rmvm(params);
				}
				else if ("RNMM".equalsIgnoreCase(cmd))
				{
					DbaseUtils.rnmm(params);
				}
				else if ("ADDPFM".equalsIgnoreCase(cmd))
				{
					DbaseUtils.addpfm(params);
				}
				else if ("ADDLFM".equalsIgnoreCase(cmd))
				{
					DbaseUtils.addlfm(params);
				}
				else if ("RGZPFM".equalsIgnoreCase(cmd))
				{
					DbaseUtils.rgzpfm(params);
				}
			}
			else
			{
				AS400Utils.runCommand(commandStr);
			}
		}
		else
		{
			AS400Utils.runCommand(commandStr);
		}

		return false;
	}

	/**
	* Runs a single command.
	*
	* @param commands the command you want to run
	* @since (2012-07-24.11:27:50)
	*/
	public static boolean runCommand(String commands[])
	{
		if ((commands == null) || (commands.length == 0))
		{
			return false;
		}

		return runCommand(StringUtils.join(commands, " "));
	}

	/**
	* Runs a single command.
	*
	* @param commands the command you want to run
	* @param obj the runtime class of an object
	* @since (2012-10-04.13:06:50)
	*/
	public static boolean runCommand(String commands[], Object obj)
	{
		if ((commands == null) || (commands.length == 0))
		{
			return false;
		}

		return runCommand(StringUtils.join(commands, " "), obj);
	}

	/**
	 * Sets the field value in the class field.
	 *
	 * @param object the runtime class of an object
	 * @param fieldName field name
	 * @param value field value
	 * @param searchInChild whether to search in children
	 * @return class fields
	 * @since (2012-07-26.14:41:19)
	 */
	public static boolean setFieldVal(Object object, String fieldName,
		String value, boolean searchInChild)
	{
		Class<?> cls;

		if (searchInChild)
		{
			cls = object.getClass().getSuperclass();

			if (cls.equals(Object.class))
			{
				cls = object.getClass();
			}
		}
		else
		{
			cls = object.getClass();
		}

		try
		{
			Field flds[] = cls.getDeclaredFields();
			Field newFlds[] = new Field[flds.length];

			for (int i = 0, j = 0; i < flds.length; i++)
			{
				if (!ReflectionUtils.isUserDefOrArr(flds[i].getType()))
				{
					if (flds[i].getName().equalsIgnoreCase(fieldName))
					{
						Object fieldType = flds[i].getType();
						flds[i].setAccessible(true);

						if (fieldType.equals(Double.class))
						{
							flds[i].set(object, Double.valueOf(value));
						}
						else if (fieldType.equals(Float.class))
						{
							flds[i].set(object, Float.valueOf(value));
						}
						else if (fieldType.equals(Long.class))
						{
							flds[i].set(object, Long.valueOf(value));
						}
						else if (fieldType.equals(Integer.class))
						{
							flds[i].set(object, Integer.valueOf(value));
						}
						else
						{
							flds[i].set(object, value);
						}

						flds[i].setAccessible(false);

						return true;
					}
				}
				else if (searchInChild)
				{
					newFlds[j] = flds[i];
					j++;
				}
			} // i

			LinkedHashMap<String, Field> colFldMap =
				new LinkedHashMap<String, Field>();

			try
			{
				ReflectionUtils.getColumnAnnoFldMap(object, colFldMap);

				for (String str : colFldMap.keySet())
				{
					if (str.equals(fieldName))
					{
						setMethodVal(cls, str, value, object);

						return true;
					}
				}
			}
			catch (Exception e)
			{
			}

			if (searchInChild)
			{
				for (int i = 0; i < newFlds.length; i++)
				{
					if (newFlds[i] == null)
					{
						continue;
					}

					Object object1 = newFlds[i].get(object);

					if (setFieldVal(object1, fieldName, value, false))
					{
						return true;
					}
				}
			}
		}
		catch (Exception e)
		{
			logMessage(e.getMessage());
		}

		return false;
	}

	private static boolean setMethodVal(Class<?> childClass, String fieldName,
		Object val, Object object) throws Exception
	{
		Method method = null;
		Method methods[] = childClass.getDeclaredMethods();

		for (Method m : methods)
		{
			int modifier = m.getModifiers();

			if (!(Modifier.isFinal(modifier) || Modifier.isStatic(modifier)))
			{
				if (m.isAnnotationPresent(Column.class))
				{
					Column column = m.getAnnotation(Column.class);
					String fldName = column.name();

					if (fieldName.equalsIgnoreCase(fldName.trim()))
					{
						method = m;

						break;
					}
				}
			}
		} // m

		if (method == null)
		{
			return false;
		}

		String name = method.getName();

		if (name.startsWith("get"))
		{
			name = name.substring(3, name.length()).toLowerCase();
		}

		Method setter = getSetter(childClass, name);
		setter.invoke(object, val);

		return true;
	}
}