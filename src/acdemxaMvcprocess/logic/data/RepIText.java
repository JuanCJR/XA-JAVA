package acdemxaMvcprocess.logic.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.lang.reflect.Field;

import java.math.BigDecimal;

import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import acdemxaMvcprocess.data.DataCRUD;
import static com.databorough.utils.DateTimeConverter.formatDate;
import com.databorough.utils.IOUtils;
import static com.databorough.utils.AS400Utils.getJobUser;
import static com.databorough.utils.JSFUtils.getSessionParam;
import static com.databorough.utils.JSFUtils.setSessionParam;
import static com.databorough.utils.LoggingAspect.logMessage;
import static com.databorough.utils.LoggingAspect.logStackTrace;
import static com.databorough.utils.NumberFormatter.formatNumber;
import static com.databorough.utils.ReflectionUtils.getColumnAnnoFldMap;
import static com.databorough.utils.ReflectionUtils.loadEDSRefs;
import static com.databorough.utils.ReflectionUtils.setCompatibleField;
import static com.databorough.utils.ServletContextProvider.getRealPath;
import static com.databorough.utils.ServletContextProvider.setAttribute;
import static com.databorough.utils.StringUtils.padStringWithValue;
import static com.databorough.utils.Utils.length;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

/**
 * @author vishwajeetp
 */
public class RepIText {
	public static final String REP_STREAM = "REP_STREAM";
	public static final String LST_REP_STREAM = "LST_REP_STREAM";

	private static final String FILENAME = "file_name";
	private static final String ODP = "odp";
	private static final String OF_FILE_NAME = "ofFileName";
	private static final String LIBRARY_NAME = "libraryName";
	private static final String SPOOL_FILE_NUM = "spoolFileNum";
	private static final String OF_OVER_FLOW_LINE = "overFlowLine";
	private static final String OF_ROW_PG_HGHT = "rowsPageHeight";
	//private static final String DF_PAGE_LENGTH = "pageLength";
	private static final String PRT_DSP_COLS = "prtDspCols";
	private static final String DF_CUR_LINE = "currentLine";
	private static final String DF_PAGE_NUMBER = "pageNumber";

	public static final int DEF_BORDER_WIDTH = 1;
	public static final int DEF_CELL_PADDING = 3;

	private static final Map<String, Integer> kwdsMap =
		new HashMap<String, Integer>();

	static {
		kwdsMap.put("HIGHLIGHT", Font.BOLD);
		kwdsMap.put("ITALIC", Font.ITALIC);
		kwdsMap.put("UNDERLINE", Font.UNDERLINE);
	}

	public ByteArrayOutputStream outputStream;
	protected Date repDt = new Date();//getJobDateDt();
	private Document document;

	private LinkedHashMap<String, Field> infdsFlds =
		new LinkedHashMap<String, Field>();
	private LinkedList<DataCRUD<?, ?>> lstCruds;
	protected List<Map<String, Field>> fldsMapsObjs =
		new LinkedList<Map<String, Field>>();
	protected List<Object> fldsObjs = new LinkedList<Object>();
	private List<Field> lstClassFlds;
	private List<Field> uqEDSs = new LinkedList<Field>();

	private Object infDs;
	protected Object pgmMbr;

	private Paragraph p;
	private PdfWriter pdfWriter;

	private String pdfFile;
	protected String prtFileName;
	protected String repDate = formatDate(repDt, "yyyy-MM-dd");
	protected String repDesc = "";
	protected String repTime = formatDate(repDt, "HH.mm.ss");

	protected boolean dataIndicator[] = new boolean[100];
	private boolean entityFldsRefsLoaded;
	public boolean fldVal;
	private boolean started;

	private char decSep;
	private char grpSep;

	protected float charsPerInch = 10.0f;

	// from XPRTCTL
	protected float colsPageWidth = 132f;

	// dimensions of Courier new font of size 10
	private float courier_Height; //= 13.08f;
	private float courier_Width; //= 6.45f; //6.049f
	protected float linesPerInch = 6.0f;
	//private float permFontHeight;
	private float permFontWidth;
	protected float rowsPageHeight = 66f; // number of columns
	private float userUnitsPerCol;

	private int elementNumber = -1;
	private int fontSize = 10;
	protected int lastRowNum = 0;
	protected int lineNo = 1;

	// number of Lines excluding margins
	protected int overFlowLine = 70; // 90
	protected int pageNo = 1;
	protected int pagesPerSide = 1;

	public RepIText(Object pgmMbr, boolean dataIndicator[]) {
		this(pgmMbr, dataIndicator, null);
	}

	public RepIText(Object pgmMbr, boolean dataIndicator[], Object infds) {
		if (pgmMbr == null) {
			return;
		}

		if (length(dataIndicator) == 0) {
			return;
		}

		this.pgmMbr = pgmMbr;
		this.dataIndicator = dataIndicator;
		this.infDs = infds;

		Class<?> pgmCls = pgmMbr.getClass();

		// first adding classFlds in our list of resolvers
		fldsObjs.add(pgmMbr);

		LinkedHashMap<String, Field> classFlds =
			new LinkedHashMap<String, Field>();
		lstClassFlds = getColumnAnnoFldMap(pgmMbr, classFlds, true, false);
		fldsMapsObjs.add(classFlds);

		// then stateVar if it exists
		Object stvr = null;

		try {
			stvr = pgmCls.getDeclaredField("stateVariable").get(pgmMbr);
		}
		catch (Exception e) {
		}

		if (stvr != null) {
			fldsObjs.add(stvr);

			LinkedHashMap<String, Field> stvrFlds =
				new LinkedHashMap<String, Field>();
			getColumnAnnoFldMap(stvr, stvrFlds);
			fldsMapsObjs.add(stvrFlds);
		}

		// then infds if it exists
		if (infDs != null) {
			addInfDsFldsRefs();
			fldsObjs.add(infDs);
			fldsMapsObjs.add(infdsFlds);
		}

		// then loadUnqualifiedEDS refs
		loadEDsFlds();

		// then LocalFileFields if it exists
		Object lclf = null;

		try {
			lclf = pgmCls.getDeclaredField("fileVariable").get(pgmMbr);
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		if (lclf != null) {
			fldsObjs.add(lclf);

			LinkedHashMap<String, Field> lclFlds =
				new LinkedHashMap<String, Field>();
			getColumnAnnoFldMap(lclf, lclFlds);
			fldsMapsObjs.add(lclFlds);
		}

		// then PrtfFields if it exists
		Object lclp = null;

		try {
			lclp = pgmCls.getDeclaredField("prtfVariable").get(pgmMbr);
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		if (lclp != null) {
			fldsObjs.add(lclp);

			LinkedHashMap<String, Field> prtfFlds =
				new LinkedHashMap<String, Field>();
			getColumnAnnoFldMap(lclp, prtfFlds);
			fldsMapsObjs.add(prtfFlds);
		}

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		decSep = symbols.getDecimalSeparator();
		grpSep = symbols.getGroupingSeparator();
	}

	/**
	 * Specifies that the printer device is to skip to a specific line before it
	 * prints the next line.
	 *
	 * @param numLines Line number to skip
	 * @param indicator indicator flag
	 */
	public void SKIP(int numLines, boolean indicator) {
		if (!indicator) {
			return;
		}

		int numLine = Math.abs(numLines);

		if (numLine >= lineNo) {
			int diffLine = numLine - lineNo;

			while (diffLine-- > 0) {
				lineNo++;
				p.add(Chunk.NEWLINE);

				if (lineNo >= overFlowLine) {
					p.add(Chunk.NEXTPAGE);
					pageNo++;
					lineNo = numLine;

					while (numLine-- > 0) {
						// go to specified Line no in Page
						p.add(Chunk.NEWLINE);
					}

					break;
				}
			}
		}
		else {
			p.add(Chunk.NEXTPAGE);
			pageNo++;
			lineNo = numLine;
			numLine = numLine - 1;

			while (numLine-- > 0) {
				// go to specified Line no in Page
				p.add(Chunk.NEWLINE);
			}
		}
	}

	/**
	 * Specifies that the printer device is to space lines before it prints one
	 * or more lines.
	 *
	 * @param num Number of lines to space
	 * @param indicator indicator flag
	 */
	public void SPACE(int num, boolean indicator) {
		if (fldVal || !indicator) {
			return;
		}

		while (num > 0) {
			num--;
			addToDoc(Chunk.NEWLINE);
			lineNo++;

			if (lineNo >= overFlowLine) {
				p.add(Chunk.NEXTPAGE);
				lineNo = 1;
				pageNo++;
			}
		}
	}

	// synch InfDS field references which have to be supported
	protected void addInfDsFldsRefs() {
		if ((pgmMbr == null) || (infDs == null)) {
			return;
		}

		try {
			Class<?> infdsClass = infDs.getClass();
			Field infdsFields[] = infdsClass.getDeclaredFields();

			for (Field infFld : infdsFields) {
				FiPsDsFld fiDsAnno = infFld.getAnnotation(FiPsDsFld.class);

				if (fiDsAnno == null) {
					continue;
				}

				int fromPos = fiDsAnno.fromPos();
				int len = fiDsAnno.length();

				if (Number.class.isAssignableFrom(infFld.getType())) {
					len = fiDsAnno.precision();

					if (fiDsAnno.scale() > 0) {
						len++;
					}
				}

				int toPos = (fromPos + len) - 1;
				int length = toPos - fromPos + 1;

				if (fromPos <= 80) {
					// File feedback information
					if ((fromPos == 1) && (toPos == 8)) {
						infdsFlds.put(FILENAME, infFld);
					}
				}
				else if ((fromPos > 80) && (toPos <= 240)) {
					// Open Feedback Information
					int offset = fromPos - 81;

					if ((offset == 0) && (length == 2)) {
						infdsFlds.put(ODP, infFld);
					}
					else if ((offset == 2) && (length == 10)) {
						infdsFlds.put(OF_FILE_NAME, infFld);
					}
					else if ((offset == 12) && (length == 10)) {
						infdsFlds.put(LIBRARY_NAME, infFld);
					}
					else if ((offset == 42) && (length == 2)) {
						infdsFlds.put(SPOOL_FILE_NUM, infFld);
					}
					else if ((offset == 107) && (length == 2)) {
						infdsFlds.put(OF_OVER_FLOW_LINE, infFld);
					}
					else if ((offset == 71) && (length == 2)) {
						infdsFlds.put(OF_ROW_PG_HGHT, infFld);
						//infdsFlds.put(DF_PAGE_LENGTH, infFld);
					}
					else if ((offset == 73) && (length == 34)) {
						infdsFlds.put(PRT_DSP_COLS, infFld);
					}
				}
				else if ((fromPos > 240) && (toPos <= 366)) {
					// Input/Output Feedback Information or Get Attributes
					// Feedback Information
				}
				else if (fromPos > 366) {
					// Device Specific or Get Attributes Feedback Information
					int offset = fromPos - 367;

					if ((offset == 0) && (length == 2)) {
						infdsFlds.put(DF_CUR_LINE, infFld);
					}
					else if ((offset == 2) && (length == 4)) {
						infdsFlds.put(DF_PAGE_NUMBER, infFld);
					}
				}
			}
		}
		catch (Exception e) {
			logStackTrace(e);
		}
	}

	private void addToDoc(Chunk elem) {
		if (elementNumber == -1) {
			p.add(elem);
		}
		else {
			p.add(elementNumber, elem);
			elementNumber++;
		}
	}

	protected void calHeightWidthOfFont(int fontSize) {
		// Get font Height and width
		BaseFont bf_courier = null;

		try {
			bf_courier = BaseFont.createFont(BaseFont.COURIER, "", true);
		}
		catch (Exception e) {
		}

		// Get 1 char width of size 10
		permFontWidth = bf_courier.getWidthPoint("M",
				Float.valueOf(String.valueOf(fontSize)));

		// Get 1 char height of size 12
		/*float heightAscent =
			bf_courier.getAscentPoint("M",
				Float.valueOf(String.valueOf(fontSize)));
		float heighDesent =
			bf_courier.getDescentPoint("M",
				Float.valueOf(String.valueOf(fontSize)));
		// Get 1 char height of size 10
		permFontHeight = heightAscent - heighDesent;*/

		courier_Height = 1.2f * fontSize; // 1.5f * permFontHeight;
		courier_Width = permFontWidth;
		userUnitsPerCol = permFontWidth;
	}

	public void disposePdfDoc() {
		document = null;
		p = null;
	}

	private Chunk format(Object textobj, String type, int fldWidth,
		String editcode, String editword) {
		textobj = formatText(textobj, type, fldWidth);

		if (!textobj.toString().contains("/") &&
				!textobj.toString().contains("\\")) {
			if (type.equalsIgnoreCase("Integer")) {
				textobj = new Integer(textobj.toString());
			}
	        else if (type.equalsIgnoreCase("Long")) {
	        	textobj = new Long(textobj.toString());
			}
	        else if (type.equalsIgnoreCase("Float")) {
	        	textobj = new Float(textobj.toString());
			}
	        else if (type.equalsIgnoreCase("Double")) {
	        	textobj = new Double(textobj.toString());
			}
	        else if (type.equalsIgnoreCase("BigDecimal")) {
	        	textobj = new BigDecimal(textobj.toString().trim());
			}
		}

		if (length(editcode) > 0) {
			textobj = formatUsingEditCode(textobj, type, fldWidth, editcode);
		}
		else if (length(editword) > 0) {
			textobj = formatUsingEditWord(textobj, type, fldWidth, editword);
		}

		return new Chunk(textobj.toString());
	}

	private Object formatText(Object textObj, String type, int fldWidth) {
		String fmt = null;

		if (type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("Long")) {
			fmt = "%1$0" + fldWidth + "d";
		}
		else if (type.equalsIgnoreCase("Float") ||
				type.equalsIgnoreCase("Double") ||
				type.equalsIgnoreCase("BigDecimal")) {
			fmt = "%1$" + fldWidth + ".2f";
		}
		else if (type.equalsIgnoreCase("Date")) {
			fmt = "%1$tY-%1$tm-%1$td";
		}
		else if (type.equalsIgnoreCase("Time")) {
			fmt = "%tH:%tM:%tS" + fldWidth + ".2f";
		}
		else {
			if (length(textObj.toString().trim()) > 0) {
				fmt = "%1$-" + fldWidth + "s";
			}
		}

		if (fmt == null) {
			return textObj;
		}

		return String.format(fmt, textObj);
	}

	private String formatUsingEditCode(Object textObj, String type,
		int fldWidth, String editCode) {
		String str = null;

		switch (editCode.toUpperCase().charAt(0)) {
		case '1':
		case '2':
		case 'J':
		case 'K':
		case 'A':
		case 'B':
		case 'N':
		case 'O':

			if (type.equalsIgnoreCase("Integer") ||
					type.equalsIgnoreCase("Long")) {
				String format = "##" + grpSep + "###";
				str = formatNumber(textObj, format);

				try {
					str = padStringWithValue(str, " ", fldWidth, true);
				}
				catch (Exception e) {
					logStackTrace(e);
				}
			}
			else if (type.equalsIgnoreCase("Float") ||
					type.equalsIgnoreCase("Double") ||
					type.equalsIgnoreCase("BigDecimal")) {
				String format =
					"###" + grpSep + "###" + grpSep + "###" + decSep + "00";
				str = formatNumber(textObj, format);

				try {
					str = padStringWithValue(str, " ", fldWidth, true);
				}
				catch (Exception e) {
					logStackTrace(e);
				}
			}

			if ((length(str.trim()) == 0) &&
					(
						editCode.equalsIgnoreCase("1") ||
						editCode.equalsIgnoreCase("A") ||
						editCode.equalsIgnoreCase("J") ||
						editCode.equalsIgnoreCase("N")
					)) {
				if (type.equalsIgnoreCase("Integer") ||
						type.equalsIgnoreCase("Long")) {
					str = "0";
				}
				else {
					str = decSep + "00";
				}
			}

			break;

		case '3':
		case '4':
		case 'L':
		case 'M':
		case 'C':
		case 'D':
		case 'P':
		case 'Q':

			try {
				str =
					padStringWithValue(textObj.toString(), " ", fldWidth, true);
			}
			catch (Exception e) {
				logStackTrace(e);
			}

			if ((length(str.trim()) == 0) &&
					(
						editCode.equalsIgnoreCase("3") ||
						editCode.equalsIgnoreCase("C") ||
						editCode.equalsIgnoreCase("L") ||
						editCode.equalsIgnoreCase("P")
					)) {
				if (type.equalsIgnoreCase("Integer") ||
						type.equalsIgnoreCase("Long")) {
					str = "0";
				}
				else {
					str = decSep + "00";
				}
			}

			break;

		case 'Y':
			str = textObj.toString();

			String str1;
			String str2;
			String str3;

			if ((str.length() == 3) || (str.length() == 4)) {
				str1 = str.substring(0, 2);
				str2 = str.substring(2, str.length());

				if (str1.startsWith("0")) {
					str1 = str1.substring(1);
				}

				str = str1 + "/" + str2;
			}
			else if (str.length() >= 5) {
				if ((str.length() ==  5) && !str.startsWith("0")) {
			     	str = "0" + str;
				}

				str1 = str.substring(0, 2);
				str2 = str.substring(2, 4);
				str3 = str.substring(4, str.length());

				if (str1.startsWith("0")) {
					str1 = str1.substring(1);
				}

				str = str1 + "/" + str2 + "/" + str3;
			}

			break;

		case 'Z':

			try {
				str =
					padStringWithValue(textObj.toString(), " ", fldWidth, true);
			}
			catch (Exception e) {
				logStackTrace(e);
			}

			StringBuilder sb = new StringBuilder(str);

			if (str.contains("-")) {
				sb.delete(sb.indexOf("-"), sb.indexOf("-") + 1);
			}

			if (str.contains(",")) {
				sb.delete(sb.indexOf(","), sb.indexOf(",") + 1);
			}

			if (str.contains(".")) {
				sb.delete(sb.indexOf("."), sb.indexOf(".") + 1);
			}

			str = sb.toString();

			break;
		}

		if (str.contains("-")) // negative number 
		 {
			StringBuilder sb = new StringBuilder(str);
			int index = sb.indexOf("-");

			if (editCode.equalsIgnoreCase("1") ||
					editCode.equalsIgnoreCase("2")) {
				sb.delete(index, index + 1);
				str = sb.toString();
			}
			else if (editCode.equalsIgnoreCase("J") ||
					editCode.equalsIgnoreCase("K") ||
					editCode.equalsIgnoreCase("L") ||
					editCode.equalsIgnoreCase("M")) {
				sb.delete(index, index + 1);
				str = sb.toString() + "-";
			}
			else if (editCode.equalsIgnoreCase("A") ||
					editCode.equalsIgnoreCase("B") ||
					editCode.equalsIgnoreCase("C") ||
					editCode.equalsIgnoreCase("D")) {
				sb.delete(index, index + 1);
				str = sb.toString() + "CR";
			}
		}

		return str;
	}

	private String formatUsingEditWord(Object textObj, String type,
		int fldWidth, String editword) {
		String txtObj = textObj.toString().trim();

		// For edit words which will be used to format date and time from
		// integer values
		if ((editword.indexOf(':') != -1) || (editword.indexOf('/') != -1) ||
				(editword.indexOf('-') != -1) ||
				(editword.indexOf('\\') != -1)) {
			if ((fldWidth > 0) && (length(txtObj) != fldWidth)) {
				txtObj = formatText(textObj, type, fldWidth).toString();
			}
		}

		if ((
				"Float".equals(type) || "Double".equals(type) ||
				"BigDecimal".equals(type)
			) && txtObj.contains(".")) {
			// edtwrd('   0')
			txtObj = txtObj.replace(".", "");

			while (txtObj.startsWith("0")) {
				// Remove leading zeroes
				txtObj = txtObj.substring(1);
			}
		}

		char textObject[] = txtObj.toCharArray(); // "123456"
		int j = textObject.length - 1;
		char edtwrd[] = editword.toCharArray(); // "  -  -  Lotus"
		int k = 0;
		StringBuilder sb = new StringBuilder(edtwrd.length);

		for (int i = edtwrd.length - 1; i >= 0; i--) {
			String str1 = Character.toString(edtwrd[i]);

			if (str1.equalsIgnoreCase("0") || str1.equalsIgnoreCase("*")) {
				char ch = edtwrd[i];

				try {
					// edtwrd = "   *.  ", value = "145", output = ***1.45
					while (!Character.isSpaceChar(textObject[j])) {
						sb.insert(k++, textObject[j--]);
						i--;
					}
				}
				catch (Exception e) {
					if (!str1.equalsIgnoreCase("0")) {
						while (i-- >= 0) {
							sb.insert(k++, ch);
						}
					}
				}

				if (!str1.equalsIgnoreCase("0")) {
					while (i-- >= 0) {
						sb.insert(k++, ch);
					}
				}

				break;
			}

			if (Character.isSpaceChar(edtwrd[i])) {
				if (j >= 0) {
					sb.insert(k++, textObject[j--]);
				}
			}
			else {
				if (str1.equalsIgnoreCase("&")) {
					sb.insert(k++, ' ');
				}
				else {
					sb.insert(k++, edtwrd[i]);
				}
			}
		}// end - loop

		if ((
				"Float".equals(type) || "Double".equals(type) ||
				"BigDecimal".equals(type)
			) && textObj.toString().contains(".")) {
			// edtwrd('   0')
			String str =
				padStringWithValue(sb.toString(), " ", fldWidth, false);
			sb.setLength(0);
			sb.append(str);
		}

		return sb.reverse().toString();
	}

	public void formatUsingKwds(Chunk chk, String keywords) {
		Font font = new Font();
		String split[] = keywords.split(" ");
		int a = 0;

		for (String s : split) {
			if (s.equalsIgnoreCase("UNDERLINE")) {
				chk.setUnderline(0.1f, -2f);
			}
			else {
				a += kwdsMap.get(s.toUpperCase());
			}
		}

		font.setStyle(a);
		chk.setFont(font);
	}

	@SuppressWarnings("unchecked")
	public void generatePDF() {
		if (!started) {
			return;
		}

		try {
			document.add(p);
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		document.close();
		this.started = false;

		if (outputStream != null) {
			Object lstRepStrm = getSessionParam(LST_REP_STREAM);
			LinkedList<byte[]> strmLst;

			if (lstRepStrm == null) {
				strmLst = new LinkedList<byte[]>();
			}
			else {
				strmLst = (LinkedList<byte[]>)lstRepStrm;
			}

			strmLst.add(outputStream.toByteArray());
			setSessionParam(LST_REP_STREAM, strmLst);

			// Store PDF file name in ServletContext if no Session
			lstRepStrm = getSessionParam(LST_REP_STREAM);

			if (lstRepStrm == null) {
				setAttribute("report" + getJobUser(), pdfFile);
			}

			try {
				outputStream.close();
			}
			catch (Exception e) {
				logStackTrace(e);
			}
		}

		p = null;

		return;
	}

	private String getActualPath() {
		File reportDir = IOUtils.validateDirName(getRealPath(""), "report");

		return getFileNameWithLoc(reportDir.getPath(), prtFileName, false);
	}

	private String getFileNameWithLoc(String filePath, String title,
		boolean autoIncrement) {
		String fileExtension = ".pdf";

		Date dt = new Date();
		String sdf =
			new SimpleDateFormat("yyyy-MM-dd" + "_" + "HH-mm-ss").format(dt);
		title = title.replaceAll("\\*", "");
		title = title.replaceAll("/", "-");

		int indx = title.lastIndexOf(":");

		if (indx > 2) {
			title = title.replace(": ", "-");
		}

		String fileName =
			filePath + IOUtils.FILE_SEPARATOR + title + "_" + sdf +
			fileExtension;

		return fileName;
	}

	public boolean isStarted() {
		return started;
	}

	private void loadCrudsEntityFldsRefs(Object pgmMbr,
		List<Field> lstClassFlds) {
		if ((pgmMbr != null) && (lstClassFlds != null)) {
			lstCruds = new LinkedList<DataCRUD<?, ?>>();

			for (Field clsFld : lstClassFlds) {
				if (DataCRUD.class.isAssignableFrom(clsFld.getType())) {
					try {
						clsFld.setAccessible(true);

						DataCRUD<?, ?> crud =
							(DataCRUD<?, ?>)clsFld.get(pgmMbr);
						clsFld.setAccessible(false);
						lstCruds.add(crud);

						LinkedHashMap<String, Field> entityFlds =
							new LinkedHashMap<String, Field>();
						getColumnAnnoFldMap(crud.entityCls, entityFlds, false,
							false);
						fldsMapsObjs.add(entityFlds);
					}
					catch (Exception e) {
						logStackTrace(e);
					}
				}
			}

			entityFldsRefsLoaded = true;
		}
	}

	private void loadEDsFlds() {
		loadEDSRefs(pgmMbr, uqEDSs);

		for (Field dsFld : uqEDSs) {
			try {
				LinkedHashMap<String, Field> dsFldsMap =
					new LinkedHashMap<String, Field>();
				getColumnAnnoFldMap(dsFld.getType(), dsFldsMap, true, true);
				// for EDSs we better add their FieldRefs themselves for they
				// may take time to actually initialize
				fldsObjs.add(dsFld);
				fldsMapsObjs.add(dsFldsMap);
			}
			catch (Exception e) {
				logStackTrace(e);
			}
		}
	}

	public void setFieldText(int col, int row, String objFld, int fldLen,
		String type, String keywords, String editword, boolean indicator) {
		setFieldText(col, row, objFld, fldLen, type, "", keywords, editword,
			indicator);
	}

	@SuppressWarnings("unchecked")
	public void setFieldText(int col, int row, String objFld, int fldLen,
		String type, String editCode, String keywords, String editword,
		boolean indicator) {
		int i = 0;
		Field f = null;
		objFld = objFld.toUpperCase();

		Object refObj = null;

		for (; i < fldsObjs.size(); i++) {
			Object mbrFld = fldsObjs.get(i);

			if (mbrFld instanceof Field) {
				Field dsFld = (Field)mbrFld;

				try {
					mbrFld = dsFld.get(pgmMbr);
				}
				catch (Exception e) {
					logStackTrace(e);
				}

				if (mbrFld != null) {
					// Replace Unqualified EDs's field ref with actual Object
					// value (which would've initialized by now)
					fldsObjs.remove(i);
					fldsObjs.add(i, mbrFld);
				}
			}

			Map<String, Field> mapFlds = fldsMapsObjs.get(i);
			f = mapFlds.get(objFld);

			if (f != null) {
				refObj = mbrFld;

				break;
			}
		}

		if (f == null) {
			if (!entityFldsRefsLoaded) {
				loadCrudsEntityFldsRefs(pgmMbr, lstClassFlds);
			}

			String prtfFld = objFld;

			for (DataCRUD<?, ?> dc : lstCruds) {
				Map<String, Field> mapFlds = fldsMapsObjs.get(i);

				if ((dc.prefix.length() > 0) &&
						prtfFld.startsWith(dc.prefix.toUpperCase())) {
					prtfFld = prtfFld.substring(dc.prefix.length());
				}

				f = mapFlds.get(prtfFld);

				if (f != null) {
					refObj = dc.lastReadEntity;

					if (refObj != null) {
						break;
					}
					else {
						f = null;
					}
				}

				i++;
			}
		}

		if (f == null) {
			return;
		}

		try {
			f.setAccessible(true);
			Object testObj = f.get(refObj);
			f.setAccessible(false);
			ArrayList<Chunk> al = p.getChunks();

			if ((length(testObj) == 0) && (length(al) != 0)) {
				if (al.size() != 0) {
					int z = al.size() - 1;
					Chunk chk = al.get(z);

					if (chk.getContent()
							   .equalsIgnoreCase(Chunk.NEWLINE.getContent())) {
						fldVal = true;

						return;
					}
					else {
						fldVal = false;
					}
				}
			}
			else {
				fldVal = false;
			}

			setText(col, row, testObj, fldLen, type, editCode, keywords,
				editword, indicator);
		}
		catch (Exception e) {
			logStackTrace(e);
		}

		return;
	}

	private void setInfDsFld(String fldName, Object value) {
		Field fld = infdsFlds.get(fldName);

		if ((fld != null) && (value != null)) {
			setCompatibleField(fld, infDs, null, value);
		}
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void setText(int col, int row, Object text, String keywords,
		boolean indicator) {
		if (text != null) {
			setText(col, row, text, text.toString().trim().length(), "String",
				"", keywords, "", indicator);
		}
	}

	public void setText(int col, int row, Object textObj, int fldWidth,
		String type, String editcode, String keywords, String editword,
		boolean indicator) {
		if (!isStarted()) {
			startPdfDoc();
		}

		if (fldVal) {
			fldVal = false;
		}

		if (!indicator) {
			return;
		}

		if (lastRowNum != row) {
			SPACE(1, true);
		}

		lastRowNum = row;

		if (type.equalsIgnoreCase("String")) {
			textObj = textObj.toString().trim();

			String strTextObj = textObj.toString();
			int len = length(strTextObj);

			if (len > fldWidth) {
				int diff = len - fldWidth;
				strTextObj = strTextObj.substring(0, len - diff);
				textObj = strTextObj;
			}
		}

		addToDoc(new Chunk(new VerticalPositionMark(), col * userUnitsPerCol,
				false));

		if (textObj == null) {
			return;
		}

		if (!textObj.getClass().getSimpleName().equalsIgnoreCase(type)) {
			if (type.equalsIgnoreCase("Date") ||
					type.equalsIgnoreCase("Time")) {
				// 2003-08-14 00:00:00.0
				SimpleDateFormat format =
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'.0'");

				try {
					textObj = (Date)format.parse(textObj.toString());
				}
				catch (Exception e) {
					textObj = textObj.toString();
					type = "String";
				}
			}
		}

		Chunk chk = format(textObj, type, fldWidth, editcode, editword);

		if (length(keywords) >= 1) {
			formatUsingKwds(chk, keywords);
		}

		addToDoc(chk);
	}

	/**
	 * Responsible for the pdf document initialization in memory.
	 *
	 * @return boolean notifying the success or failure
	 */
	public boolean startPdfDoc() {
		// For Courier new font at font size 10, width: 6.049 user-units,
		// height: 13.08 user-units
		// A4 page height (width in landscape mode): 841.68f user-units
		// which means 72 user-units per inch

		//float marginLeft = (841.68f - maxCols * courier_Width)/2;
		float marginSide = 10f; //(1400f - maxCols * courier_Width)/2;
		float marginTop = 10f; //(739.44f - maxRows * courier_Height) * 0.31f;
		float marginBtm = 10f; //(739.44f - maxRows * courier_Height) * 0.68f;

		p = new Paragraph("");

		Font font = p.getFont();
		font.setSize(fontSize);
		font.setFamily(BaseFont.COURIER);

		calHeightWidthOfFont(fontSize);

		p.setLeading(courier_Height); // permFontHeight * 1.5

		float uuPageHeight =
			(overFlowLine * courier_Height) + (marginTop + marginBtm);
		float uuPageWidth = (colsPageWidth * courier_Width) + (marginSide * 2);

		//userUnitsPerCol = permFontWidth; //(colsPageWidth * courier_Width)/87;
		Document document =
			new Document(new Rectangle(uuPageWidth, uuPageHeight), marginSide,
				marginSide, marginTop, marginBtm);

		return startPdfDoc(document);
	}

	public boolean startPdfDoc(Document doc) {
		this.setStarted(true);

		if (p == null) {
			p = new Paragraph();
		}

		document = doc;

		try {
			outputStream = new ByteArrayOutputStream();
			pdfWriter = PdfWriter.getInstance(document, outputStream);
			pdfWriter.setStrictImageSequence(true);

			// Save pdf
			pdfFile = getActualPath();
			PdfWriter savePdf =
				PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			savePdf.setStrictImageSequence(true);

			// Open pdf
			document.open();

			lastRowNum = 0;
			elementNumber = -1;

			return true;
		}
		catch (Exception e) {
			logStackTrace(e);
			logMessage("Error in creating stream/opening DOC!");
		}

		return false;
	}

	protected void syncInfDs() {
		if ((infDs == null) || (infdsFlds.size() == 0)) {
			addInfDsFldsRefs();
		}

		if (infDs != null) {
			//setInfDsFld(FILENAME, fileName);
			//setInfDsFld(OF_FILE_NAME, fileName);
			setInfDsFld(LIBRARY_NAME, "");
			setInfDsFld(SPOOL_FILE_NUM, 0);
			setInfDsFld(OF_OVER_FLOW_LINE, overFlowLine); //revisit
			setInfDsFld(OF_ROW_PG_HGHT, rowsPageHeight);
			setInfDsFld(PRT_DSP_COLS, 0);
			setInfDsFld(DF_CUR_LINE, lineNo);
			setInfDsFld(DF_PAGE_NUMBER, pageNo);
		}
	}
}
