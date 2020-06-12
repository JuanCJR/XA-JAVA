package com.databorough.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class IBean {
	public List<Object> paramsList = new ArrayList<Object>();
	public List<Object> retParamsList = new ArrayList<Object>();
	public static String user;
	public String wfIntNxtPgm = "";
	public static String wfIntCurPgm;
	public Thread descThread;
	public Thread initiatorThread;
	public boolean freshStart = true;
}