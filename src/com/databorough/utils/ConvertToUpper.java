package com.databorough.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversion of string into upper case.
 *
 * @author Abhishek Gaharwar
 * @since (2010-11-02.15:36:12)
 */
@FacesConverter("convertToUpper") 
public class ConvertToUpper implements Converter
{
	/**
	 * Converts the specified string value, into a model data object.
	 * 
	 * @param facesContext FacesContext for the request being processed
	 * @param uiComponent UIComponent with which this model object value is
	 *        associated
	 * @param data String value to be converted 
	 */
	public Object getAsObject(FacesContext facesContext,
		UIComponent uiComponent, String data)
	{
		if (data == null)
		{
			return data;
		}

		return new StringBuilder(data.toUpperCase());  
	}

	/**
	 * Converts the specified model object value, into upper case String.
	 * 
	 * @param facesContext FacesContext for the request being processed
	 * @param uiComponent UIComponent with which this model object value is
	 *        associated
	 * @param obj Model object value to be converted
	 */
	public String getAsString(FacesContext facesContext,
		UIComponent uiComponent, Object obj)
	{
		if (obj == null)
		{
			return null;
		}

		return obj.toString().toUpperCase();
	}
}