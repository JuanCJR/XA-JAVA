package com.databorough.utils;

public interface IConstants {
	// Control String constants
	String SELECTED = "SELECTED";
	String SUCCESS = "success";
	String ERROR = "error";

	String DB_DATE_FORMAT = "dd-MM-yy";
	String PARAM_VALUES = "PARAM_VALUES";
	String URL_PARAM = "parm";
	String RESET_PAGER = "resetPager";

	String IS_DEP_GRID = ".IS_DEP_GRID";

	String EQUAL_KEY_VALS = "_EQUAL_KEY_VALS";

	// Flat Screen Modes
	String SCREEN_MODE = "SCREEN_MODE";
	String DISPLAY_MODE = "DISPLAY";
	String ADD_MODE = "WRITE";
	String CHANGE_MODE = "UPDATE";
	String DELETE_MODE = "DELETE";
	String CONFIRMATION_MODE = "CONFIRMATION";
	String NXT_PGM = "nxtPgm";

	// UI look and feel conatants
	String THEME = "XWDFT";
	String FONT_ERROR = "errorFont";
	String FONT_WARNING = "warningFont";
	String FONT_INFORMATION = "infoFont";
	String FONT_FIELD = "fieldFont";
	String STYLE_CLASS = "styleClass";

	String PARENT_SCREEN = "parent";
	String START_SCN = "startScn";
	String START_PARENT_SCN = "startParentScn";
	String BIZRLZ_BEAN = "BIZRLZ_BEAN";
	String PARAMS_LIST = "paramsList";
	String RE_INITIALIZE_BIZ_RLZ = "RE_INITIALIZE_BIZ_RLZ";

	// Function Key Indicators
	int KA = 1;
	int KB = 2;
	int KC = 3;
	int KD = 4;
	int KE = 5;
	int KF = 6;
	int KG = 7;
	int KH = 8;
	int KI = 9;
	int KJ = 10;
	int KK = 11;
	int KL = 12;
	int KM = 13;
	int KN = 14;
	int KP = 15;
	int KQ = 16;
	int KR = 17;
	int KS = 18;
	int KT = 19;
	int KU = 20;
	int KV = 21;
	int KW = 22;
	int KX = 23;
	int KY = 24;

	// Overflow Indicators
	int OA = 1;
	int OB = 2;
	int OC = 3;
	int OD = 4;
	int OE = 5;
	int OF = 6;
	int OG = 7;
	int OV = 8;

	// DataCrud Enum Constants
	enum IOACTIONS { LIOERROR, LIOFOUND, LIOEQUAL, LIOEOF };
}