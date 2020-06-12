package com.databorough.utils;

import java.text.ParseException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import javax.swing.text.MaskFormatter;
import static com.databorough.utils.LoggingAspect.logMessage;

/**
 * Conversion of string from one format to the other.
 *
 * @author Mohd. Ahmad
 * @since (2010-12-23.15:36:12)
 */
@FacesConverter("MaskConverter")
public class MaskConverter implements Converter
{
	private static final char digits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	final String MASK = "mask";
	final String TYPE = "type";
	private String mask = "";
	private String type = "";

	/**
	 * <p>
	 * Valid pattern chars are as follows :
	 * </p>
	 * <table border=1 summary="Valid characters and their descriptions">
	 * <tr>
	 *    <th>Character&nbsp;</th>
	 *    <th><p align="left">Description</p></th>
	 * </tr>
	 * <tr>
	 *    <td>#</td>
	 *    <td>Any valid number, uses <code>Character.isDigit</code>.</td>
	 * </tr>
	 * <tr>
	 *    <td>'</td>
	 *    <td>Escape character, used to escape any of the
	 *       special formatting characters.</td>
	 * </tr>
	 * <tr>
	 *    <td>U</td><td>Any character (<code>Character.isLetter</code>). All
	 *        lowercase letters are mapped to upper case.</td>
	 * </tr>
	 * <tr><td>L</td><td>Any character (<code>Character.isLetter</code>). All
	 *        upper case letters are mapped to lower case.</td>
	 * </tr>
	 * <tr><td>A</td><td>Any character or number (<code>Character.isLetter</code>
	 *       or <code>Character.isDigit</code>)</td>
	 * </tr>
	 * <tr><td>?</td><td>Any character
	 *        (<code>Character.isLetter</code>).</td>
	 * </tr>
	 * <tr><td>*</td><td>Anything.</td></tr>
	 * <tr><td>H</td><td>Any hex character (0-9, a-f or A-F).</td></tr>
	 * </table>
	 *
	 * <p>
	 * Presently we have dealt with only "#" , "A" pattern characters.
	 * </p>
	 **/
	final String VALID_PATTERN_CHARS[] = new String[] { "#", "A" };

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
	 * @since (2010-12-23.15:53:53)
	 */
	public Object getAsObject(FacesContext facesContext,
		UIComponent uiComponent, String param)
	{
		setUiParams(JSFUtils.getParams(uiComponent));
		param = StringUtils.rtrim(param);

		int paramLen = Utils.length(param);

		if (paramLen == 0)
		{
			if ("P".equals(type) || "S".equals(type))
			{
				return 0;
			}

			return param;
		}

		// New code - 02-03-15
		if ("P".equals(type) && NumberUtils.isNumber(param) &&
				(param.indexOf('.') != -1))
		{
			// Integer value
			return param.replace(".", "");
		}

		try
		{
			if ((paramLen < mask.length()) &&
					("P".equals(type) || "S".equals(type)))
			{
				mask = StringUtils.replaceString(param, digits, "#");
			}

			MaskFormatter formatter = new MaskFormatter(getMask());
			formatter.setValueContainsLiteralCharacters(false);

			if (paramLen < mask.length())
			{
				mask = mask.substring(0, paramLen);
				formatter.setMask(mask);
			}

			Object val = formatter.stringToValue(param);

			return val;
		}
		catch (ParseException e)
		{
			throw new ConverterException(e.getMessage());
		}

		//return param;
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
	 * @since (2010-12-23.15:55:23)
	 */
	public String getAsString(FacesContext facesContext,
		UIComponent uiComponent, Object obj)
	{
		String val = obj.toString().trim();
		setUiParams(JSFUtils.getParams(uiComponent));

		if (Utils.length(val) == 0)
		{
			return val;
		}

		//if (StringUtils.isAllDigit(val) && !"A".equals(type))
		if (!"A".equals(type) && NumberUtils.isNumber(val))
		{
			// If Date field is an Integer
			if ("0".equals(val))
			{
				return "";
			}

			// New code - 02-03-15
			if (val.indexOf('.') != -1)
			{
				// Integer value
				return val;
			}

			int maskLen = getMaskLength();

			if (val.length() < maskLen)
			{
				val = StringUtils.padStringWithValue(val, "0", maskLen, true);
			}
		}
		else if ("A".equals(type))
		{
			val = StringUtils.replaceString(val, " ", "");
		}

		try
		{
			MaskFormatter mf = new MaskFormatter(mask);
			mf.setValueContainsLiteralCharacters(false);

			//return mf.valueToString(val);
			String str = mf.valueToString(val);

			if ("P".equals(type) && !mask.startsWith("0"))
			{
				// Suppress 0
				str = str.replaceFirst("^0*", "");
			}

			return str;
		}
		catch (ParseException e)
		{
			logMessage(e.getLocalizedMessage());
		}

		return val;
	}

	/**
	 * Gets the mask string.
	 *
	 * @return mask
	 * @since (2010-12-23.15:47:40)
	 */
	public String getMask()
	{
		return mask;
	}

	/**
	 * Gets the mask length excluding literals.
	 *
	 * @return mask length
	 * @since (2010-12-23.15:49:41)
	 */
	private int getMaskLength()
	{
		String maskStr = getMask();

		int nLiterals = 0;

		for (String str : VALID_PATTERN_CHARS)
		{
			nLiterals += StringUtils.countMatches(maskStr, str);
		}

		return (maskStr == null) ? 0 : nLiterals;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the mask.
	 *
	 * @param mask mask to set
	 * @since (2010-12-23.15:48:30)
	 */
	public void setMask(String mask)
	{
		this.mask = mask;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
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

		for (int i = 0; i < count; i++)
		{
			UIParameter parameter = uiParams[i];

			if (MASK.equals(parameter.getName()))
			{
				setMask((String)parameter.getValue());
			}
			else if (TYPE.equals(parameter.getName()))
			{
				setType((String)parameter.getValue());
			}
		}
	}
}