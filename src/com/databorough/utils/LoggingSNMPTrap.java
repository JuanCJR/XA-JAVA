package com.databorough.utils;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import static com.databorough.utils.LoggingAspect.logStackTrace;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;

import org.snmp4j.mp.SnmpConstants;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;

import org.snmp4j.transport.DefaultUdpTransportMapping;

import org.snmp4j.util.DefaultPDUFactory;

/**
 * Asynchronous notification from agent to manager.
 * Destination addressing for traps is determined in an application-specific
 * manner typically through trap configuration variables in the MIB.
 *
 * @author Zia
 * @since (2012-07-03.15:55:12)
 */
public final class LoggingSNMPTrap
{
	private static String community;

	// The hostname or IP address of your NMS i.e. the trap's destination
	private static String ipAddress;
	private static String trapVersion;
	private static String oid;

	// Ideally Port 162 should be used to send & receive Trap, but any other
	// available Port can also be used
	private static int port;
	private static int retries;
	private static long timeout;

	static
	{
		loadProperties();
	}

	/**
	 * LoggingSNMPTrap object would never be constructed. To restrict the
	 * creation of object of this class the method has been made private.
	 */
	private LoggingSNMPTrap()
	{
		super();
	}

	private static void loadProperties()
	{
		try
		{
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ResourceBundle bundle =
				facesCtx.getApplication().getResourceBundle(facesCtx, "config");

			community = bundle.getString("community");
			ipAddress = bundle.getString("ipAddress");
			trapVersion = bundle.getString("trapVersion");
			oid = bundle.getString("oid");

			port = Integer.parseInt(bundle.getString("port"));
			retries = Integer.parseInt(bundle.getString("retries"));
			timeout = Integer.parseInt(bundle.getString("timeout"));
		}
		catch (Exception e)
		{
			community = "public";
			ipAddress = "127.0.0.1";
			trapVersion = "V2";
			oid = ".1.3.6.1.4.1.5000.3003";

			port = 162;
			retries = 2;
			timeout = 50000;
		}
	}

	private static void sendPDUv1(String msgLst[])
	{
		try
		{
			TransportMapping<?> transport = new DefaultUdpTransportMapping();
			//transport.listen();

			// Create Target
			CommunityTarget cTarget = new CommunityTarget();
			cTarget.setCommunity(new OctetString(community));
			cTarget.setVersion(SnmpConstants.version1);
			cTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
			cTarget.setRetries(2);
			cTarget.setTimeout(50000);

			Snmp snmp = new Snmp(transport);

			for (String str : msgLst)
			{
				// Create PDU of type version1
				PDUv1 pdu =
					(PDUv1)DefaultPDUFactory.createPDU(SnmpConstants.version1);
				pdu.add(new VariableBinding(new OID(oid),
						new OctetString(str)));
				pdu.setType(PDU.V1TRAP);

				// Send the PDU
				snmp.trap(pdu, cTarget);
			}

			transport.close();
			snmp.close();
		}
		catch (Exception e)
		{
		}
	}

	public static void sendSNMPTrap(String msg)
	{
		if ("V1".equalsIgnoreCase(trapVersion))
		{
			sendTrapV1(msg);
		}
		else
		{
			sendTrapV2(msg);
		}
	}

	private static void sendTrapV1(String msg)
	{
		try
		{
			if (msg.length() == 0)
			{
				return;
			}

			msg = msg.replaceAll("\r\n", " ");

			String msgLst[];

			if (msg.length() > 400)
			{
				msgLst = StringUtils.split(msg, 400);
			}
			else
			{
				msgLst = new String[1];
				msgLst[0] = msg;
			}

			// Send PDU version 1
			sendPDUv1(msgLst);
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}

	private static void sendTrapV2(String msg)
	{
		try
		{
			msg = msg.replaceAll("\r\n", " ");

			// Create Transport Mapping
			TransportMapping<?> transport = new DefaultUdpTransportMapping();
			//transport.listen();

			// Create Target
			CommunityTarget cTarget = new CommunityTarget();
			cTarget.setCommunity(new OctetString(community));
			cTarget.setVersion(SnmpConstants.version2c);
			cTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
			cTarget.setRetries(retries);
			cTarget.setTimeout(timeout);

			// Create PDU for V2
			PDU pdu = (PDU)DefaultPDUFactory.createPDU(SnmpConstants.version2c);

			pdu.add(new VariableBinding(new OID(oid),
					new OctetString(msg)));
			pdu.setType(PDU.NOTIFICATION);

			// Send the PDU
			Snmp snmp = new Snmp(transport);
			snmp.send(pdu, cTarget);
			snmp.close();
		}
		catch (Exception e)
		{
			logStackTrace(e);
		}
	}
}