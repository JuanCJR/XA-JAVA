package com.databorough.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.StringUtils.replaceString;

@FacesConverter("NumericDateConverter")
public class NumericDateConverter implements Converter
{
	private final String DB_FORMAT = "dbformat";
	private final String ERR_MSG_STYLE = "XWDFTerrorFont";
	private final String PATTERN = "pattern";
	private final String STYLE_CLASS = "styleClass";

	// Could move in configuration constants
	private String dbformat = "CYYMMDD";
	private String pattern = "dd-MM-yy";

	public Object getAsObject(FacesContext facesContext,
		UIComponent uiComponent, String param)
	{
		setUiParams(JSFUtils.getParams(uiComponent));

		if ((param != null) && (param.trim().length() != 0) &&
				(Utils.length(facesContext.getMessageList()) == 0))
		{
			String msg =
				"Invalid Date. Please enter in " + pattern + " format.";
			FacesMessage message =
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);

			SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

			Date date;

			try
			{
				sdf.setLenient(false);
				date = sdf.parse(param);
			}
			catch (ParseException e)
			{
				setErrorStyleClass(uiComponent);
				throw new ConverterException(message);
			}

			if ("CYYMMDD".equals(getDbformat()))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				String month =
					padStringWithValue("" + (calendar.get(Calendar.MONTH) + 1),
						"0", 2, true);
				String day =
					padStringWithValue("" + calendar.get(Calendar.DATE), "0",
						2, true);
				Integer year = calendar.get(Calendar.YEAR);
				String dateString = (year > 2000) ? "1" : "0";
				year = (year > 2000) ? (year - 2000) : (year - 1900);

				if (year < 0)
				{
					setErrorStyleClass(uiComponent);
					throw new ConverterException(message);
				}

				String yearStr = padStringWithValue("" + year, "0", 2, true);
				String digitStr = dateString + yearStr + month + day;

				return Integer.parseInt(digitStr);
			}
			else if ("YYMMDD".equals(getDbformat()))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				String year =
					padStringWithValue("" + calendar.get(Calendar.YEAR), "0",
						2, true);
				String month =
					padStringWithValue("" + (calendar.get(Calendar.MONTH) + 1),
						"0", 2, true);
				String day =
					padStringWithValue("" + calendar.get(Calendar.DATE), "0",
						2, true);

				if (Utils.length(year) > 2)
				{
					year = year.substring(2);
				}

				String digitStr = year + month + day;

				return Integer.parseInt(digitStr);
			}
			else if ("DDMMYY".equals(getDbformat()))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				String year =
					padStringWithValue("" + calendar.get(Calendar.YEAR), "0",
						2, true);
				String month =
					padStringWithValue("" + (calendar.get(Calendar.MONTH) + 1),
						"0", 2, true);
				String day =
					padStringWithValue("" + calendar.get(Calendar.DATE), "0",
						2, true);

				if (Utils.length(year) > 2)
				{
					year = year.substring(2);
				}

				String digitStr = day + month + year;

				return Integer.parseInt(digitStr);
			}
			else if ("YYYYMMDD".equals(getDbformat()))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				String yearStr =
					padStringWithValue("" + calendar.get(Calendar.YEAR), "0",
						4, true);
				String month =
					padStringWithValue("" + (calendar.get(Calendar.MONTH) + 1),
						"0", 2, true);
				String day =
					padStringWithValue("" + calendar.get(Calendar.DATE), "0",
						2, true);
				String digitStr = yearStr + month + day;

				return Integer.parseInt(digitStr);
			}
			else if ("DDMMYYYY".equals(getDbformat()))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				String yearStr =
					padStringWithValue("" + calendar.get(Calendar.YEAR), "0",
						4, true);
				String month =
					padStringWithValue("" + (calendar.get(Calendar.MONTH) + 1),
						"0", 2, true);
				String day =
					padStringWithValue("" + calendar.get(Calendar.DATE), "0",
						2, true);
				String digitStr = day + month + yearStr;

				return Integer.parseInt(digitStr);
			}
 			else if ("CYYMM".equals(getDbformat()))
			{
				// 08.00 -> 10008
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				String month =
					padStringWithValue("" + (calendar.get(Calendar.MONTH) + 1),
						"0", 2, true);
				Integer year = calendar.get(Calendar.YEAR);
				String dateString = (year > 2000) ? "1" : "0";
				year = (year > 2000) ? (year - 2000) : (year - 1900);

				if (year < 0)
				{
					setErrorStyleClass(uiComponent);
					throw new ConverterException(message);
				}

				String yearStr = padStringWithValue("" + year, "0", 2, true);
				String digitStr = dateString + yearStr + month;

				return Integer.parseInt(digitStr);
			}
		}

		//return TypeUtil.getDefaultValue(Integer.class);
		// Return null from Position To when empty for Filter to work
		return null;
	}

	public String getAsString(FacesContext facesContext,
		UIComponent uiComponent, Object obj)
	{
		Integer digits = (Integer)obj;
		Integer defaultVal = (Integer)TypeUtil.getDefaultValue(Integer.class);

		if ((digits != null) && !digits.equals(defaultVal))
		{
			setUiParams(JSFUtils.getParams(uiComponent));

			String num = "" + digits;

			if ("CYYMMDD".equalsIgnoreCase(getDbformat()))
			{
				String digitStr = padStringWithValue(num, "0", 7, true);
				Integer century = Integer.valueOf(digitStr.substring(0, 1));
				Integer year = Integer.valueOf(digitStr.substring(1, 3));
				year += ((century < 1) ? 1900 : 2000);

				Integer month = Integer.valueOf(digitStr.substring(3, 5)) - 1;
				Integer day = Integer.valueOf(digitStr.substring(5).trim());
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

				return sdf.format(date);
			}
			else if ("YYMMDD".equals(getDbformat()))
			{
				String digitStr = padStringWithValue(num, "0", 6, true);
				Integer year = Integer.valueOf(digitStr.substring(0, 2));
				Integer month = Integer.valueOf(digitStr.substring(2, 4)) - 1;
				Integer day = Integer.valueOf(digitStr.substring(4).trim());
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

				return sdf.format(date);
			}
			else if ("DDMMYY".equals(getDbformat()))
			{
				String digitStr = padStringWithValue(num, "0", 6, true);
				Integer day = Integer.valueOf(digitStr.substring(0, 2));
				Integer month = Integer.valueOf(digitStr.substring(2, 4)) - 1;
				Integer year = Integer.valueOf(digitStr.substring(4).trim());
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

				return sdf.format(date);
			}
			else if ("MMDDYY".equals(getDbformat()))
			{
				String digitStr = padStringWithValue(num, "0", 6, true);
				Integer month = Integer.valueOf(digitStr.substring(0, 2)) - 1;
				Integer day = Integer.valueOf(digitStr.substring(2, 4));
				Integer year = Integer.valueOf(digitStr.substring(4).trim());
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

				return sdf.format(date);
			}
			else if ("YYYYMMDD".equals(getDbformat()))
			{
				String digitStr = padStringWithValue(num, "0", 8, true);
				Integer year = Integer.valueOf(digitStr.substring(0, 4));
				Integer month = Integer.valueOf(digitStr.substring(4, 6));
				Integer day = Integer.valueOf(digitStr.substring(6).trim());
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

				return sdf.format(date);
			}
			else if ("DDMMYYYY".equals(getDbformat()))
			{
				String digitStr = padStringWithValue(num, "0", 8, true);
				Integer day = Integer.valueOf(digitStr.substring(0, 2));
				Integer month = Integer.valueOf(digitStr.substring(2, 4));
				Integer year = Integer.valueOf(digitStr.substring(4).trim());
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);

				Date date = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(getPattern());

				return sdf.format(date);
			}
 			else if ("CYYMM".equals(getDbformat()))
			{
				// Convert to MM.JJ
				// 10008 -> 08.00
				String digitStr = padStringWithValue(num, "0", 5, true);
				String year = digitStr.substring(1, 3);
				String month = digitStr.substring(3);
				char sep = (getPattern().indexOf('.') != -1) ? '.' : '/';

				return month + sep + year;
			}
		}

		return null;
	}

	public String getDbformat()
	{
		return dbformat;
	}

	public String getPattern()
	{
		pattern = replaceString(pattern, "DD", "dd");
		pattern = replaceString(pattern, "mm", "MM");

		if ("YYMMDD".equals(getDbformat()) || "DDMMYY".equals(getDbformat()))
		{
			pattern = replaceString(pattern, "yyyy", "yy");
		}
		else
		{
			pattern = replaceString(pattern, "YY", "yy");
		}

		return pattern;
	}

	public void setDbformat(String dbformat)
	{
		this.dbformat = dbformat;
	}

	private void setErrorStyleClass(UIComponent uiComponent)
	{
		if (uiComponent.getClientId().startsWith("updtGrid"))
		{
			return;
		}

		String styleClass =
			(String)uiComponent.getAttributes().get(STYLE_CLASS);
		String stylesFound[] = StringUtils.split(styleClass);

		if (stylesFound != null)
		{
			if (Utils.lookupStrInStrArr(ERR_MSG_STYLE, stylesFound, 0) > -1)
			{
				styleClass = StringUtils.trim(styleClass.substring(0,
							styleClass.indexOf(ERR_MSG_STYLE)), "");
				uiComponent.getAttributes().put(STYLE_CLASS, styleClass);
			}

			styleClass += (" " + ERR_MSG_STYLE);
		}
		else
		{
			styleClass = ERR_MSG_STYLE;
		}

		uiComponent.getAttributes().put(STYLE_CLASS, styleClass);
	}

	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	/**
	 * Sets the parameters from UIComponents.
	 *
	 * @param uiParams the array of UIParameters
	 * @since (2010-11-10.12:24:26)
	 */
	private void setUiParams(UIParameter uiParams[])
	{
		int count = (uiParams == null) ? 0 : uiParams.length;

		for (int indx = 0; indx < count; indx++)
		{
			UIParameter parameter = uiParams[indx];

			if (DB_FORMAT.equals(parameter.getName()))
			{
				setDbformat((String)parameter.getValue());
			}
			else if (PATTERN.equals(parameter.getName()))
			{
				setPattern((String)parameter.getValue());
			}
		}
	}
}