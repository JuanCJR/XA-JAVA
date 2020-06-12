package com.databorough.utils;

import java.util.LinkedList;
import java.util.List;

import javax.faces.model.ListDataModel;

/**
 * Handles the JSF dataModel used to show grid table data. Also handles the Next
 * page and Previous page data flow.
 *
 * @author Shantanu Rautela
 * @version (2013-02-04.01:01:22)
 */
public class PageHandler<T extends GridDataObject>
{
	public List<T> heap = new LinkedList<T>();
	private T gridData[];
	public boolean eof;
	private int endRow;
	public int heapLen;
	private int page;
	private int pageSize;
	private int startingRow;
	private int totalCount;

	public PageHandler(int pageSize)
	{
		this.pageSize = pageSize;
	}

	/**
	 * Manages the heap data for caching.
	 *
	 * @param gridTableData array of grid data model.
	 */
	public void appendHeap(T gridTableData[])
	{
		int len = eof ? heapLen : gridTableData.length;

		if (gridTableData != null)
		{
			for (int i = 0; i < len; i++)
			{
				heap.add(gridTableData[i]);
			}

			totalCount += len;
		}
	}

	/**
	 * Clears all heap data to start afresh.
	 */
	public void clearHeap()
	{
		heap.clear();
		heapLen = 0;
		totalCount = 0;
		startingRow = 0;
		endRow = pageSize;
		eof = false;
		page = 0;
	}

	/**
	 * Creates the JSF dataModel, used to populate grid table.
	 *
	 * @param filterRowData filter row information
	 * @return <tt>ListDataModel</tt> used by JSF for rendering table.
	 */
	public ListDataModel<T> createPageDataModel(T filterRowData)
	{
		List<T> gridDataList = new LinkedList<T>();
		int count = 0;

		for (int i = startingRow; i < endRow; i++)
		{
			if (heap.size() > i)
			{
				gridDataList.add(heap.get(i));

				if (gridData != null)
				{
					gridData[count++] = heap.get(i);
				}
			}
			else
			{
				break;
			}
		}

		if (filterRowData != null)
		{
			gridDataList.add(0, filterRowData);
		}

		return new ListDataModel<T>(gridDataList);
	}

	/**
	 * Creates the JSF dataModel, used to populate grid table.
	 *
	 * @return <tt>ListDataModel</tt> used by JSF for rendering table.
	 */
	public ListDataModel<T> createPageDataModel()
	{
		List<T> list = new LinkedList<T>();
		int count = 0;
		
		for (int i = startingRow; i < endRow; i++)
		{
			if (heap.size() > i)
			{
				list.add(heap.get(i));
				
				if (gridData != null)
				{
					gridData[count++] = heap.get(i);
				}
			}
			else
			{
				break;
			}
		}
		
		return new ListDataModel<T>(list);
	}

	/**
	 * Checks whether the heap has next page information or end of file 
	 * reached.
	 *
	 * @return <tt>true</tt> when heap has next page information.
	 */
	public boolean fetchNext()
	{
		if (endRow < totalCount)
		{
			page = page + 1;
			startingRow += pageSize;
			endRow += pageSize;

			// load from current heap
			return true;
		}
		else if (!eof)
		{
			page = page + 1;
			// fetch from database
			startingRow += pageSize;
			endRow += pageSize;
		}

		return false;
	}

	/**
	 * Checks whether the heap has previous page information.
	 *
	 * @return <tt>true</tt> when heap has previous page information.
	 */
	public boolean fetchPrevious()
	{
		if ((startingRow > 0) && (startingRow <= totalCount))
		{
			page = page - 1;
			startingRow -= pageSize;
			endRow -= pageSize;

			return true;
		}

		return false;
	}

	/**
	 * Gets the grid row data model array.
	 *
	 * @return <tt>GridDataObject</tt> array.
	 */
	public T[] getGridData()
	{
		return gridData;
	}

	/**
	 * Gets the total number of records shown.
	 *
	 * @return total number of records shown.
	 */
	public int getPageFirstItem()
	{
		int startRecord = page * pageSize;
		startRecord = (page == 0) ? 0 : startRecord;

		return startRecord;
	}

	/**
	 * Checks where next page data available.
	 *
	 * @return <tt>true</tt> when next page data is available.
	 */
	public boolean hasNext()
	{
		return !eof || (endRow < totalCount);
	}

	/**
	 * Checks where previous page data available.
	 *
	 * @return <tt>true</tt> when previous page data is available.
	 */
	public boolean hasPrevious()
	{
		return startingRow > 0;
	}

	/**
	 * Registers the grid row data model array.
	 *
	 * @param gridArray <tt>GridDataObject</tt> array.
	 */
	public void setGridData(T gridArray[])
	{
		this.gridData = gridArray;
	}

	/**
	 * Resets the page size.
	 *
	 * @param pageSize new page size.
	 */
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
}