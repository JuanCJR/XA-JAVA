package com.databorough.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Conversion of number string from one format to the other.
 * To be used only for components in data table.
 *
 * @author shantanur
 * @since (2010-11-02.15:36:12)
 */
@FacesConverter("com.databorough.utils.NumberConverter")
public class NumberConverter implements Converter
{
	/**
	 * Converts the specified string value, which is associated with the
	 * specified UIComponent, into a model data object that is appropriate for
	 * being stored during the Apply Request Values phase of the request
	 * processing life cycle.
	 *
	 * @param facesContext FacesContext for the request being processed
	 * @param uiComponent UIComponent with which this model object value is
	 *        associated
	 * @param data String value to be converted (may be null)
	 * @return null if the value to convert is null, otherwise the result of the
	 *        conversion
	 * @since (2010-11-02.13:42:54)
	 */
	public Object getAsObject(FacesContext facesContext,
		UIComponent uiComponent, String data)
	{
		UIParameter params[] = JSFUtils.getParams(uiComponent);
		String dbFmt = (String)params[0].getValue();
		String siteFmt = (String)params[1].getValue();

		if ((data == null) || (dbFmt == null) || (siteFmt == null) ||
				(Utils.length(facesContext.getMessageList()) > 0))
		{
			return data;
		}

		// Skipping for position to row
		UIComponent parent2 = uiComponent.getParent();

		if ((parent2 != null) &&
				(parent2.getParent() instanceof HtmlDataTable))
		{
			UIComponent parent = parent2.getParent();
			HtmlDataTable table = (HtmlDataTable)parent;
			GridDataObject rowData = (GridDataObject)table.getRowData();

			if (rowData.isPositionTo() && "".equals(data.trim()))
			{
				return null;
			}
		}

		if (data.trim().equals(""))
		{
			return "0";
		}

		//return NumberFormatter.formatNumber(data, siteFmt, dbFmt);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		String decimalSeparator = symbols.getDecimalSeparator() + "";
		String groupingSeparator = symbols.getGroupingSeparator() + "";
		String retStr = data.replace(groupingSeparator, "");
		retStr = retStr.replace(decimalSeparator, ".");

		if (retStr.endsWith("-"))
		{
			retStr = "-" + retStr.replace("-", "");
		}

		// Replace slash
		if (retStr.indexOf('/') != -1)
		{
			retStr = retStr.replace("/", "");
		}

		if (!NumberUtils.isNumber(retStr))
		{
			String msg = "Please enter a valid numeric value.";
			FacesMessage message =
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			throw new ConverterException(message);
		}
		else if (retStr.contains(".") && !siteFmt.contains("."))
		{
			String msg =
				"Invalid value. Please enter in " +
				siteFmt.replaceAll("0", "#") + " format.";
			FacesMessage message =
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			throw new ConverterException(message);
		}

		return retStr;
	}

	/**
	 * Converts the specified model object value, which is associated with the
	 * specified UIComponent, into a String that is suitable for being included
	 * in the response generated during the Render Response phase of the request
	 * processing life cycle.
	 *
	 * @param facesContext FacesContext for the request being processed
	 * @param uiComponent UIComponent with which this model object value is
	 *        associated
	 * @param obj Model object value to be converted (may be null)
	 * @return a zero-length String if value is null, otherwise the result of
	 *        the conversion
	 * @since (2010-11-02.13:46:23)
	 */
	public String getAsString(FacesContext facesContext,
		UIComponent uiComponent, Object obj)
	{
		if (obj == null)
		{
			return null;
		}

		UIParameter params[] = JSFUtils.getParams(uiComponent);
		String dbFmt = (String)params[0].getValue();
		String siteFmt = (String)params[1].getValue();
		String data = obj.toString();

		if ((data == null) || (dbFmt == null) || (siteFmt == null))
		{
			return data;
		}

		// Skipping for position to row
		UIComponent parent2 = uiComponent.getParent();

		if ((parent2 != null) &&
				(parent2.getParent() instanceof HtmlDataTable))
		{
			UIComponent parent = parent2.getParent();
			HtmlDataTable table = (HtmlDataTable)parent;
			GridDataObject rowData = (GridDataObject)table.getRowData();

			if (rowData.isPositionTo() &&
					(
						"0".equals(data) || "0.0".equals(data) ||
						"0.00".equals(data)
					))
			{
				return "";
			}
		}

		/*String retStr = NumberFormatter.formatNumber(data, dbFmt, siteFmt);

		if (retStr == null)
		{
			return retStr;
		}

		if (retStr.startsWith("0") && (retStr.length() > 1) && (dbFmt != null))
		{
			if ((dbFmt.startsWith("0") || dbFmt.startsWith("#")) &&
					(dbFmt.indexOf(".") == -1))
			{
				// Remove leading zeroes to correct 'unexpected token:' error in
				// case of Hibernate numeric fields of type int
				int nonZeroIndx = -1;

				int len = retStr.length();

				for (int i = 0; i < len; i++)
				{
					if (retStr.charAt(i) != '0')
					{
						nonZeroIndx = i;

						break;
					}
				}

				if (nonZeroIndx != -1)
				{
					retStr = retStr.substring(nonZeroIndx);
				}
			}
		}*/

		// Suppress 0 where decimal field is not present
		if ("0".equals(data) && (siteFmt.indexOf('.') == -1) &&
				StringUtils.isAllLetter(siteFmt, '#'))
		{
			return "";
		}

		NumberFormat NumbFormat = NumberFormat.getInstance();
		DecimalFormat decFmt = (DecimalFormat)NumbFormat;
		decFmt.applyPattern(siteFmt);

		String retStr = decFmt.format(Double.parseDouble(data));

		return retStr;
	}
}