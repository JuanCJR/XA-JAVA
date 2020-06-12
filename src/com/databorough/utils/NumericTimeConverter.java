package com.databorough.utils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.databorough.utils.Utils;
import static com.databorough.utils.StringUtils.join;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.StringUtils.trim;

@FacesConverter("NumericTimeConverter")
public class NumericTimeConverter implements Converter
{
	private final String DISPLAY_LEN = "displayLen";
	private int displayLen = 0;

	public Object getAsObject(FacesContext facesContext,
		UIComponent uiComponent, String param)
	{
		setUiParams(JSFUtils.getParams(uiComponent));

		if ((param == null) || (param.trim().length() == 0))
		{
			return null;
		}

		param = trim(param);

		String hr_mi_se[] = param.split(":");
		String str = "";

		for (int i = 0; i < Utils.length(hr_mi_se); i++)
		{
			str += padStringWithValue(hr_mi_se[i].trim(), "0", 2, true);
		}

		return Integer.parseInt(str);
	}

	public String getAsString(FacesContext facesContext,
		UIComponent uiComponent, Object obj)
	{
		String data = obj.toString();

		// Skipping for position to row
		UIComponent parent2 = uiComponent.getParent();

		if ((parent2 != null) && (parent2.getParent() instanceof HtmlDataTable))
		{
			UIComponent parent = parent2.getParent();
			HtmlDataTable table = (HtmlDataTable)parent;
			GridDataObject rowData = (GridDataObject)table.getRowData();

			if (rowData.isPositionTo() && "0".equals(data))
			{
				return "";
			}
		}

		setUiParams(JSFUtils.getParams(uiComponent));

		Integer digits = (Integer)obj;
		String digitStr =
			padStringWithValue("" + digits, "0", getDisplayLen(), true);

		int len = getDisplayLen() / 2;
		String hr_mi_se[] = new String[len];
		int start_pos = 0;
		int end_pos = 0;

		for (int i = 0; i < len; i++)
		{
			start_pos = end_pos;
			end_pos = start_pos + 2;

			if (end_pos > getDisplayLen())
			{
				end_pos = getDisplayLen();
			}

			hr_mi_se[i] = digitStr.substring(start_pos, end_pos);
		}

		return join(hr_mi_se, ":");
	}

	public int getDisplayLen()
	{
		return displayLen;
	}

	public void setDisplayLen(int displayLen)
	{
		this.displayLen = displayLen;
	}

	/**
	 * Sets the parameters from UIComponents.
	 *
	 * @param uiParams the array of UIParameters
	 * @since (2010-12-23.15:51:13)
	 */
	private void setUiParams(UIParameter uiParams[])
	{
		int count = (uiParams != null) ? uiParams.length : 0;

		for (int indx = 0; indx < count; indx++)
		{
			UIParameter parameter = uiParams[indx];

			if (DISPLAY_LEN.equals(parameter.getName()))
			{
				setDisplayLen(Integer.valueOf(parameter.getValue().toString()));
			}
		}
	}
}