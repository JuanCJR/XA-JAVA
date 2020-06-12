package com.databorough.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.databorough.utils.IBean;
import static com.databorough.utils.ReflectionUtils.isUserDefOrArr;
import static com.databorough.utils.Utils.length;

public abstract class WorkFlowThread extends Thread
{
	protected Object getRetParam(IBean initiator, int idx)
	{
		Object obj = null;

		if ((initiator != null) && (initiator.retParamsList != null) &&
				(initiator.retParamsList.size() > idx))
		{
			obj = initiator.retParamsList.get(idx);
		}

		return obj;
	}

	protected void processCall(IBean initiator, String nxtPgm, Object... values)
		throws InterruptedException
	{
		addWfParamsList(initiator, values);
		initiator.wfIntNxtPgm = nxtPgm;
		initiator.freshStart = false;
		//initiator.initiatorThread.resume();
		//Thread.currentThread().suspend();
		synchronized (initiator.initiatorThread)
		{
			initiator.initiatorThread.notify();
		}

		synchronized (Thread.currentThread())
		{
			Thread.currentThread().wait();
		}
	}

	@SuppressWarnings("unchecked")
	private void addWfParamsList(IBean initiator, Object... values)
	{
		if (length(values) == 0)
		{
			return;
		}

		List<Object> paramsList = new ArrayList<Object>();

		for (Object prm : values)
		{
			if (prm == null)
			{
				paramsList.add(prm);

				continue;
			}

			Class<?> prmClass = prm.getClass();

			if (prm instanceof List)
			{
				paramsList.addAll((Collection<?extends Object>)prm);
			}
			else if (!prmClass.isArray() && isUserDefOrArr(prmClass))
			{
				// Get Field value from DS
				Field fields[] = prmClass.getFields();

				if (fields.length == 0)
				{
					fields = prmClass.getDeclaredFields();
				}

				for (Field field : fields)
				{
					Object val = null;

					try
					{
						val = field.get(prm);
					}
					catch (Exception e)
					{
						field.setAccessible(true);

						try
						{
							val = field.get(prm);
						}
						catch (Exception e1)
						{
						}

						field.setAccessible(false);
					}

					paramsList.add(val);
				} // field
			}
			else
			{
				paramsList.add(prm);
			}
		} // prm

		initiator.paramsList = paramsList;
	}
}