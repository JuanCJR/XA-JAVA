package com.databorough.utils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversion of date string from one format to the other.
 *
 * @author Prashant Mishra
 * @since (2010-11-02.15:36:12)
 */
@FacesConverter("com.databorough.utils.DateConverter")
public class DateConverter implements Converter
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

		if ((data == null) || (dbFmt == null) || (siteFmt == null))
		{
			return data;
		}

		// Numeric dates
		if ((data.indexOf('/') == -1) && (siteFmt.indexOf('/') != -1))
		{
			siteFmt = StringUtils.deleteString(siteFmt, "/");
		}
		else if ((data.indexOf('-') == -1) && (siteFmt.indexOf('-') != -1))
		{
			siteFmt = StringUtils.deleteString(siteFmt, "-");
		}

		return new StringBuilder(DateTimeConverter.formatDate(data, siteFmt,
			dbFmt));
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
			return "";
		}

		if ("0001-01-01".equals(obj.toString()) ||
				"00010101".equals(obj.toString()) ||
				"1010101".equals(obj.toString()) ||
				"010101".equals(obj.toString()))
		{
			return "";
		}

		UIParameter params[] = JSFUtils.getParams(uiComponent);
		String dbFmt = (String)params[0].getValue();
		String siteFmt = (String)params[1].getValue();

		return DateTimeConverter.formatDate(obj.toString(), dbFmt, siteFmt);
	}
}