package com.databorough.utils;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversion of number string from one format to the other.
 *
 * @author puneets
 * @since (2011-13-04.17:36:12)
 */
@FacesConverter(forClass = DateEx.class)
public class DateExConverter implements Converter
{
	private final String DBDATEPATTERN = "dbDatePattern";
	private final String PATTERN = "pattern";
	private String dbDatePattern = "yyyy-MM-dd";
	private String pattern = "dd/MM/yyyy";

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
		setUiParams(uiComponent);
		data = DateTimeConverter.formatDate(data, getPattern(),
				getDbDatePattern());

		return DateEx.valueOf(data);
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
		setUiParams(uiComponent);

		// Skip conversion for position key fields
		UIComponent parent = uiComponent.getParent().getParent();

		if (parent instanceof HtmlDataTable)
		{
			HtmlDataTable table = (HtmlDataTable)parent;
			Object data = table.getRowData();

			if ((data != null) && data instanceof GridDataObject)
			{
				GridDataObject rowData = (GridDataObject)data;

				if (rowData.isPositionTo())
				{
					return "";
				}
			}
		}

		return DateTimeConverter.formatDate(obj.toString(), getDbDatePattern(),
			getPattern());
	}

	/**
	 * @return the dbDatePattern
	 */
	public String getDbDatePattern()
	{
		return dbDatePattern;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern()
	{
		return pattern;
	}

	/**
	 * @param dbDatePattern the dbDatePattern to set
	 */
	public void setDbDatePattern(String dbDatePattern)
	{
		this.dbDatePattern = dbDatePattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	private void setUiParams(UIComponent uiComponent)
	{
		Object obj = uiComponent.getAttributes().get(PATTERN);
		Object obj1 = uiComponent.getAttributes().get(DBDATEPATTERN);
		String pattern = (obj == null) ? "dd/MM/yyyy" : (String)obj;
		pattern = StringUtils.replaceString(pattern, "mm", "MM");

		String dbDatePattern = (obj1 == null) ? "yyyy-MM-dd" : (String)obj1;
		setPattern(pattern);
		setDbDatePattern(dbDatePattern);
	}
}