package com.databorough.utils;

import java.sql.Time;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import static com.databorough.utils.AS400Utils.getJobDateDt;
import static com.databorough.utils.DateTimeConverter.getCurrentFormat;
import static com.databorough.utils.DateTimeConverter.rpg2JavaDateFormat;
import static com.databorough.utils.DateTimeConverter.rpg2JavaTimeFormat;
import static com.databorough.utils.DateTimeConverter.timeStampFormat;
import static com.databorough.utils.DateTimeConverter.toDate;
import static com.databorough.utils.DateTimeConverter.toDateTime;
import static com.databorough.utils.DateTimeConverter.toTime;
import static com.databorough.utils.DateTimeConverter.toTimestamp;

/**
 * A Class for direct Date expression operations. Can be used for TimeStamps too
 * where output format needs to be stored.
 *
 * @author vishwajeetp
 */
public class DateEx
{
	public static final int DAY = Calendar.DAY_OF_MONTH;
	public static final int MSECOND = Calendar.MILLISECOND;
	private Calendar cal = Calendar.getInstance();
	private String outputFmtStr;
	private String pgmDateFormat;
	private char typeCode = 'D';

	public DateEx()
	{
		this('D');
	}

	public DateEx(boolean isTime)
	{
		this(isTime ? 'T' : 'D');
	}

	public DateEx(char typeCode)
	{
		super();

		if (typeCode == 'T')
		{
			//setTimeFormat(getDefaultFormat(true));
			String dfmt = getCurrentFormat(true);

			if (Utils.length(dfmt) == 0)
			{
				dfmt = "*ISO";
			}

			setTimeFormat(dfmt);
		}
		else if (typeCode == 'Z')
		{
			setJavaFormat(timeStampFormat);
		}
		else
		{
			//setDateFormat(getDefaultFormat(false));
			String dfmt = getCurrentFormat(false);

			if (Utils.length(dfmt) == 0)
			{
				dfmt = "*ISO";
			}

			setDateFormat(dfmt);
		}

		this.typeCode = typeCode;
	}

	public DateEx(char typeCode, String rpgFormat)
	{
		super();

		if (typeCode == 'T')
		{
			setTimeFormat(rpgFormat);
		}
		else if (typeCode == 'Z')
		{
			setJavaFormat(timeStampFormat);
		}
		else
		{
			setDateFormat(rpgFormat);
		}

		this.typeCode = typeCode;
	}

	public DateEx(Date dt)
	{
		this(dt, 'D');
	}

	public DateEx(Date dt, char typeCode)
	{
		this(typeCode);
		cal.setTime(dt);
	}

	public DateEx(String initVal, char typeCode)
	{
		this(typeCode);

		Date dt = null;

		if (initVal.equalsIgnoreCase("*SYS"))
		{
			dt = new Date();
		}
		else if (initVal.equalsIgnoreCase("*JOB"))
		{
			dt = getJobDateDt();
		}
		else
		{
			dt = toDateTime(initVal, "", false, typeCode);
		}

		if ((dt != null) && (dt.getTime() != 0))
		{
			cal.setTime(dt);
		}
	}

	public DateEx(String initVal, char typeCode, String rpgFormat)
	{
		this(typeCode, rpgFormat);

		Date dt = toDateTime(initVal, rpgFormat, false, typeCode);

		if ((dt != null) && (dt.getTime() != 0))
		{
			cal.setTime(dt);
		}
	}

	public void add(int fld, Number hours)
	{
		cal.add(fld, (Integer)hours);
	}

	public void addDate(DateEx dt)
	{
		addDate(dt.getDateTime());
	}

	public void addDate(Date dt)
	{
		Calendar calSrc = Calendar.getInstance();
		calSrc.setTime(dt);
		cal.add(Calendar.DAY_OF_MONTH, calSrc.get(Calendar.DAY_OF_MONTH));
		cal.add(Calendar.MONTH, calSrc.get(Calendar.MONTH));
		cal.add(Calendar.YEAR, calSrc.get(Calendar.YEAR));
	}

	public boolean after(Date dt)
	{
		return cal.getTime().after(dt);
	}

	public boolean after(DateEx dtx)
	{
		return cal.getTime().after(dtx.getDateTime());
	}

	public boolean before(Date dt)
	{
		return cal.getTime().before(dt);
	}

	public boolean before(DateEx dtx)
	{
		return cal.getTime().before(dtx.getDateTime());
	}

	public boolean beforeOrSame(Date dt)
	{
		return this.before(dt) || this.equals(dt);
	}

	public boolean beforeOrSame(DateEx dt)
	{
		return this.before(dt) || this.equals(dt);
	}

	public double doubleValue()
	{
		return getDateTime().getTime();
	}

	public boolean equals(DateEx dt)
	{
		if (dt != null)
		{
			return this.getDateTime().equals(dt.getDateTime());
		}

		return false;
	}

	public boolean equals(Date dt)
	{
		if (dt != null)
		{
			return this.getDateTime().equals(dt);
		}

		return false;
	}

	public float floatValue()
	{
		return getDateTime().getTime();
	}

	public DateEx getAdded(int fld, Object hours)
	{
		DateEx dtx = new DateEx(typeCode);
		Calendar calTmp = Calendar.getInstance();
		calTmp.setTime(cal.getTime());
		calTmp.add(fld, NumberFormatter.toInt(hours));
		dtx.setDateTime(calTmp.getTime());
		dtx.setJavaFormat(outputFmtStr);

		return dtx;
	}

	public DateEx getAddedDate(DateEx dt)
	{
		return getAddedDate('+', dt);
	}

	public DateEx getAddedDate(char opr, DateEx dt)
	{
		return getAddedDate(opr, dt.getDateTime());
	}

	public DateEx getAddedDate(Date dt)
	{
		return getAddedDate('+', dt);
	}

	public DateEx getAddedDate(char opr, Date dt)
	{
		DateEx dtx = new DateEx(typeCode);
		Calendar calTmp = Calendar.getInstance();
		calTmp.setTime(cal.getTime());

		Calendar calSrc = Calendar.getInstance();
		calSrc.setTime(dt);

		calTmp.add(Calendar.DAY_OF_MONTH,
			((opr == '-') ? (-1) : 1) * calSrc.get(Calendar.DAY_OF_MONTH));
		calTmp.add(Calendar.MONTH,
			((opr == '-') ? (-1) : 1) * calSrc.get(Calendar.MONTH));
		calTmp.add(Calendar.YEAR,
			((opr == '-') ? (-1) : 1) * calSrc.get(Calendar.YEAR));
		dtx.setDateTime(calTmp.getTime());
		dtx.setJavaFormat(outputFmtStr);

		return dtx;
	}

	public java.sql.Date getDate()
	{
		return new java.sql.Date(cal.getTimeInMillis());
	}

	public Date getDateTime()
	{
		return cal.getTime();
	}

	public String getJavaFormat()
	{
		return outputFmtStr;
	}

	public DateEx getReducedDate(DateEx dt)
	{
		return getAddedDate('-', dt.getDateTime());
	}

	public DateEx getReducedDate(Date dt)
	{
		return getAddedDate('-', dt);
	}

	public Time getTime()
	{
		return new Time(cal.getTimeInMillis());
	}

	public Timestamp getTimestamp()
	{
		return new Timestamp(cal.getTimeInMillis());
	}

	public char getTypeCode()
	{
		return typeCode;
	}

	public int intValue()
	{
		return (int)getDateTime().getTime();
	}

	public long longValue()
	{
		return getDateTime().getTime();
	}

	public boolean sameOrAfter(Date dt)
	{
		return this.after(dt) || this.equals(dt);
	}

	public boolean sameOrAfter(DateEx dt)
	{
		return this.after(dt) || this.equals(dt);
	}

	public void set(DateEx dtx)
	{
		if (dtx != null)
		{
			cal.setTime(dtx.getDateTime());
			outputFmtStr = dtx.getJavaFormat();
			typeCode = dtx.getTypeCode();
		}
	}

	public void setDateFormat(String rpgFormat)
	{
		char sep = '-';

		if ((Utils.length(rpgFormat) > 4) && rpgFormat.endsWith("0"))
		{
			sep = rpgFormat.charAt(4);
			rpgFormat = rpgFormat.substring(0, 4);
		}

		String fmt = rpg2JavaDateFormat.get(rpgFormat);

		if (fmt != null)
		{
			outputFmtStr = fmt;
			setSep(sep);
			typeCode = 'D';

			if (Utils.length(pgmDateFormat) == 0)
			{
				pgmDateFormat = fmt;
			}
		}
	}

	public void setDateTime(Date dt)
	{
		cal.setTime(dt);
	}

	public void setDateTime(Object dateStr)
	{
		set(valueOf(dateStr, typeCode));
	}

	public void setJavaFormat(String javaFormat)
	{
		if (Utils.length(javaFormat) != 0)
		{
			outputFmtStr = javaFormat;

			if (Utils.length(pgmDateFormat) == 0)
			{
				pgmDateFormat = javaFormat;
			}
		}
	}

	public void setSep(char separator)
	{
		if (separator == '0')
		{
			outputFmtStr = outputFmtStr.replaceAll("[\\/\\.\\:\\-]", "");
		}
	}

	public void setTime(long milliSecs)
	{
		cal.setTimeInMillis(milliSecs);
	}

	public void setTimeFormat(String rpgFormat)
	{
		char sep = ':';

		if (Utils.length(rpgFormat) > 4)
		{
			sep = rpgFormat.charAt(4);
			rpgFormat = rpgFormat.substring(0, 4);
		}

		String fmt = rpg2JavaTimeFormat.get(rpgFormat);

		if (fmt != null)
		{
			outputFmtStr = fmt;
			typeCode = 'T';
			setSep(sep);

			if (Utils.length(pgmDateFormat) == 0)
			{
				pgmDateFormat = fmt;
			}
		}
	}

	public void setTypeCode(char typeCode)
	{
		this.typeCode = typeCode;
	}

	/**
	 * Converts this DateEx to an int.
	 *
	 * @return this DateEx converted to an int
	 * @since (2013-07-31.16:50:20)
	 */
	public int toInt()
	{
		String format = outputFmtStr.replaceAll("[\\/\\.\\:\\-]", "");

		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(format);

		return NumberFormatter.toInt(sdf.format(getDateTime()));
	}

	public String toString()
	{
		if (cal.getTimeInMillis() == 0)
		{
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(outputFmtStr);
		outputFmtStr = pgmDateFormat;

		return sdf.format(cal.getTime());
	}

	public static DateEx valueOf(java.sql.Date dt)
	{
		return new DateEx(dt, 'D');
	}

	public static DateEx valueOf(Object dateStr, char typeCode)
	{
		if (typeCode == 'T')
		{
			return toTime(dateStr);
		}
		else if (typeCode == 'Z')
		{
			return toTimestamp(dateStr);
		}
		else
		{
			return toDate(dateStr);
		}
	}

	public static DateEx valueOf(String dateStr)
	{
		if (Utils.length(dateStr) == 0)
		{
			return new DateEx();
		}

		return toDate(dateStr);
	}

	public static DateEx valueOf(Time t)
	{
		return new DateEx(t, 'T');
	}

	public static DateEx valueOf(Timestamp ts)
	{
		return new DateEx(ts, 'Z');
	}
}