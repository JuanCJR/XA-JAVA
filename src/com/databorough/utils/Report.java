package com.databorough.utils;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import static acdemxaMvcprocess.logic.data.RepIText.LST_REP_STREAM;

import static com.databorough.utils.JSFUtils.getSessionParam;
import static com.databorough.utils.JSFUtils.setSessionParam;
import static com.databorough.utils.JSFUtils.streamPDF;

/**
 * Java Bean Class for report.xhtml.
 *
 * @author vishwajeetp
 */
@ManagedBean(name = "report")
@SessionScoped
public class Report
{
	private static LinkedList<byte[]> getRepByteArrayLst()
	{
		LinkedList<byte[]> lstRepByteArray = getSessionParam(LST_REP_STREAM);

		return lstRepByteArray;
	}

	/*public String getScriptIfReq()
	{
		String jscr = "";
		List<byte[]> repArrayLst = getRepByteArrayLst();

		if ((repArrayLst != null) && (repArrayLst.size() != 0))
		{
			jscr += "\t\t$(document).ready(function() {\n";

			for (int i = 0; i < repArrayLst.size(); i++)
			{
				jscr +=
					"\t\t\twindow.open(\'report.xhtml\', \'_blank\', '');\n";
			}

			jscr += "\t\t});";
		}

		return jscr;
	}*/

	public boolean getScriptIfReq()
	{
		List<byte[]> repArrayLst = getRepByteArrayLst();

		return ((repArrayLst != null) && (repArrayLst.size() != 0));
	}

	public synchronized String getStreamedRep()
	{
		byte pdfOutputByteArray[] = null;

		LinkedList<byte[]> lstRepByteArray = getRepByteArrayLst();

		if ((lstRepByteArray != null) && (lstRepByteArray.size() != 0))
		{
			pdfOutputByteArray = lstRepByteArray.get(0);
			lstRepByteArray.remove(0);
			setSessionParam(LST_REP_STREAM, lstRepByteArray);
		}

		if (pdfOutputByteArray != null)
		{
			streamPDF(pdfOutputByteArray);
		}

		return "";
	}
}
