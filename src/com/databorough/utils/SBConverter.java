package com.databorough.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversion of number string from one format to the other.
 *
 * @author puneets
 * @since (2011-13-04.17:36:12)
 */
@FacesConverter(forClass=StringBuilder.class)
public class SBConverter implements Converter
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
		return new StringBuilder(data);
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
		return obj.toString();
	}
}