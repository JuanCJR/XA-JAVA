package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Panel Data Object for Work with Order Details (WWCONDET).
 *
 * @author KAMALN
 */
public class WorkWithOrderDetailsPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String actdsp = "";
	private String person = "";
	private String pname = "";
	private String recnam = "";
	private String sfield = "";
	private String swabcd = "";
	private String swt8tx = "";
	private String umdes = "";
	private String xwaacs = "";
	private String xwabcd = "";
	private String xwadd1 = "";
	private String xwadd2 = "";
	private String xwadd3 = "";
	private String xwadd4 = "";
	private String xwa2cd = "";
	private Double xwa5qt = 0.0D;
	private String xwbccd = "";
	private String xwcntr = "";
	private String xwcref = "";
	private String xwdesc = "";
	private DateEx xwdldt = new DateEx('L');
	private String xwg4tx = "";
	private String xwhltx = "";
	private Integer xwordn = 0;
	private String xwpcde = "";
	private Double xwpric = 0.0D;
	private String xwricd = "";
	private String xwsdsc = "";
	private String xwstat = "";
	private Double xwtamt = 0.0D;
	private String xwtdsc = "";
	private String xwt8tx = "";
	private Integer zzdate = 0;
	private String zzpgm = "";
	private String percus = "";
	private String wa2cd = "";
	private Double xwbhqt = 0.0D;
	private Double xwanpr = 0.0D;
	private String dssel = "";
	private Integer shwrec = 0;
	private String zmsage = "";
	private String msgid = "";
	private String zwaacs = "";
	private String zwabcd = "";
	private String zwa2cd = "";
	private Double zwa5qt = 0.0D;
	private Integer zwordn = 0;
	private Double zwpric = 0.0D;
	private String zwricd = "";
	private String zwt8tx = "";
	private String zzfunmode = "";

	public WorkWithOrderDetailsPDO() {
		super();
	}

	@Column(name="ZZJOB", length=10)
	public String getZzjob() {
		return zzjob;
	}

	public void setZzjob(String zzjob) {
		this.zzjob = zzjob;
	}

	@Column(name="FUN_MODE", length=1)
	public String getFunMode() {
		return funMode;
	}

	public void setFunMode(String funMode) {
		this.funMode = funMode;
	}

	@Column(name="ZZUSER", length=10)
	public String getZzuser() {
		return zzuser;
	}

	public void setZzuser(String zzuser) {
		this.zzuser = zzuser;
	}

	@Column(name="ACTDSP", length=10)
	public String getActdsp() {
		return actdsp;
	}

	public void setActdsp(String actdsp) {
		this.actdsp = actdsp;
	}

	@Column(name="PERSON", length=3)
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	@Column(name="PNAME", length=34)
	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	@Column(name="RECNAM", length=10)
	public String getRecnam() {
		return recnam;
	}

	public void setRecnam(String recnam) {
		this.recnam = recnam;
	}

	@Column(name="SFIELD", length=10)
	public String getSfield() {
		return sfield;
	}

	public void setSfield(String sfield) {
		this.sfield = sfield;
	}

	@Column(name="SWABCD", length=15)
	public String getSwabcd() {
		return swabcd;
	}

	public void setSwabcd(String swabcd) {
		this.swabcd = swabcd;
	}

	@Column(name="SWT8TX", length=9)
	public String getSwt8tx() {
		return swt8tx;
	}

	public void setSwt8tx(String swt8tx) {
		this.swt8tx = swt8tx;
	}

	@Column(name="UMDES", length=20)
	public String getUmdes() {
		return umdes;
	}

	public void setUmdes(String umdes) {
		this.umdes = umdes;
	}

	@Column(name="XWAACS", length=11)
	public String getXwaacs() {
		return xwaacs;
	}

	public void setXwaacs(String xwaacs) {
		this.xwaacs = xwaacs;
	}

	@Column(name="XWABCD", length=20)
	public String getXwabcd() {
		return xwabcd;
	}

	public void setXwabcd(String xwabcd) {
		this.xwabcd = xwabcd;
	}

	@Column(name="XWADD1", length=40)
	public String getXwadd1() {
		return xwadd1;
	}

	public void setXwadd1(String xwadd1) {
		this.xwadd1 = xwadd1;
	}

	@Column(name="XWADD2", length=40)
	public String getXwadd2() {
		return xwadd2;
	}

	public void setXwadd2(String xwadd2) {
		this.xwadd2 = xwadd2;
	}

	@Column(name="XWADD3", length=40)
	public String getXwadd3() {
		return xwadd3;
	}

	public void setXwadd3(String xwadd3) {
		this.xwadd3 = xwadd3;
	}

	@Column(name="XWADD4", length=40)
	public String getXwadd4() {
		return xwadd4;
	}

	public void setXwadd4(String xwadd4) {
		this.xwadd4 = xwadd4;
	}

	@Column(name="XWA2CD", length=3)
	public String getXwa2cd() {
		return xwa2cd;
	}

	public void setXwa2cd(String xwa2cd) {
		this.xwa2cd = xwa2cd;
	}

	@Column(name="XWA5QT", precision=9, scale=2)
	public Double getXwa5qt() {
		return xwa5qt;
	}

	public void setXwa5qt(Double xwa5qt) {
		this.xwa5qt = xwa5qt;
	}

	@Column(name="XWBCCD", length=11)
	public String getXwbccd() {
		return xwbccd;
	}

	public void setXwbccd(String xwbccd) {
		this.xwbccd = xwbccd;
	}

	@Column(name="XWCNTR", length=3)
	public String getXwcntr() {
		return xwcntr;
	}

	public void setXwcntr(String xwcntr) {
		this.xwcntr = xwcntr;
	}

	@Column(name="XWCREF", length=20)
	public String getXwcref() {
		return xwcref;
	}

	public void setXwcref(String xwcref) {
		this.xwcref = xwcref;
	}

	@Column(name="XWDESC", length=20)
	public String getXwdesc() {
		return xwdesc;
	}

	public void setXwdesc(String xwdesc) {
		this.xwdesc = xwdesc;
	}

	@Column(name="XWDLDT", length=10)
	public DateEx getXwdldt() {
		return xwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwdldt(DateEx xwdldt) {
		this.xwdldt = xwdldt;
	}

	@Column(name="XWG4TX", length=40)
	public String getXwg4tx() {
		return xwg4tx;
	}

	public void setXwg4tx(String xwg4tx) {
		this.xwg4tx = xwg4tx;
	}

	@Column(name="XWHLTX", length=10)
	public String getXwhltx() {
		return xwhltx;
	}

	public void setXwhltx(String xwhltx) {
		this.xwhltx = xwhltx;
	}

	@Column(name="XWORDN", length=6)
	public Integer getXwordn() {
		return xwordn;
	}

	public void setXwordn(Integer xwordn) {
		this.xwordn = xwordn;
	}

	@Column(name="XWPCDE", length=10)
	public String getXwpcde() {
		return xwpcde;
	}

	public void setXwpcde(String xwpcde) {
		this.xwpcde = xwpcde;
	}

	@Column(name="XWPRIC", precision=6, scale=2)
	public Double getXwpric() {
		return xwpric;
	}

	public void setXwpric(Double xwpric) {
		this.xwpric = xwpric;
	}

	@Column(name="XWRICD", length=3)
	public String getXwricd() {
		return xwricd;
	}

	public void setXwricd(String xwricd) {
		this.xwricd = xwricd;
	}

	@Column(name="XWSDSC", length=20)
	public String getXwsdsc() {
		return xwsdsc;
	}

	public void setXwsdsc(String xwsdsc) {
		this.xwsdsc = xwsdsc;
	}

	@Column(name="XWSTAT", length=2)
	public String getXwstat() {
		return xwstat;
	}

	public void setXwstat(String xwstat) {
		this.xwstat = xwstat;
	}

	@Column(name="XWTAMT", precision=13, scale=2)
	public Double getXwtamt() {
		return xwtamt;
	}

	public void setXwtamt(Double xwtamt) {
		this.xwtamt = xwtamt;
	}

	@Column(name="XWTDSC", length=20)
	public String getXwtdsc() {
		return xwtdsc;
	}

	public void setXwtdsc(String xwtdsc) {
		this.xwtdsc = xwtdsc;
	}

	@Column(name="XWT8TX", length=15)
	public String getXwt8tx() {
		return xwt8tx;
	}

	public void setXwt8tx(String xwt8tx) {
		this.xwt8tx = xwt8tx;
	}

	@Column(name="ZZDATE", length=8)
	public Integer getZzdate() {
		return zzdate;
	}

	public void setZzdate(Integer zzdate) {
		this.zzdate = zzdate;
	}

	@Column(name="ZZPGM", length=10)
	public String getZzpgm() {
		return zzpgm;
	}

	public void setZzpgm(String zzpgm) {
		this.zzpgm = zzpgm;
	}

	@Column(name="PERCUS", length=3)
	public String getPercus() {
		return percus;
	}

	public void setPercus(String percus) {
		this.percus = percus;
	}

	@Column(name="WA2CD", length=3)
	public String getWa2cd() {
		return wa2cd;
	}

	public void setWa2cd(String wa2cd) {
		this.wa2cd = wa2cd;
	}

	@Column(name="XWBHQT", precision=13, scale=4)
	public Double getXwbhqt() {
		return xwbhqt;
	}

	public void setXwbhqt(Double xwbhqt) {
		this.xwbhqt = xwbhqt;
	}

	@Column(name="XWANPR", precision=11, scale=2)
	public Double getXwanpr() {
		return xwanpr;
	}

	public void setXwanpr(Double xwanpr) {
		this.xwanpr = xwanpr;
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="SHWREC", length=4)
	public Integer getShwrec() {
		return shwrec;
	}

	public void setShwrec(Integer shwrec) {
		this.shwrec = shwrec;
	}

	@Column(name="ZMSAGE", length=78)
	public String getZmsage() {
		return zmsage;
	}

	public void setZmsage(String zmsage) {
		this.zmsage = zmsage;
	}

	@Column(name="MSGID", length=7)
	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	@Column(name="ZWAACS", length=11)
	public String getZwaacs() {
		return zwaacs;
	}

	public void setZwaacs(String zwaacs) {
		this.zwaacs = zwaacs;
	}

	@Column(name="ZWABCD", length=20)
	public String getZwabcd() {
		return zwabcd;
	}

	public void setZwabcd(String zwabcd) {
		this.zwabcd = zwabcd;
	}

	@Column(name="ZWA2CD", length=3)
	public String getZwa2cd() {
		return zwa2cd;
	}

	public void setZwa2cd(String zwa2cd) {
		this.zwa2cd = zwa2cd;
	}

	@Column(name="ZWA5QT", precision=9, scale=2)
	public Double getZwa5qt() {
		return zwa5qt;
	}

	public void setZwa5qt(Double zwa5qt) {
		this.zwa5qt = zwa5qt;
	}

	@Column(name="ZWORDN", length=6)
	public Integer getZwordn() {
		return zwordn;
	}

	public void setZwordn(Integer zwordn) {
		this.zwordn = zwordn;
	}

	@Column(name="ZWPRIC", precision=6, scale=2)
	public Double getZwpric() {
		return zwpric;
	}

	public void setZwpric(Double zwpric) {
		this.zwpric = zwpric;
	}

	@Column(name="ZWRICD", length=3)
	public String getZwricd() {
		return zwricd;
	}

	public void setZwricd(String zwricd) {
		this.zwricd = zwricd;
	}

	@Column(name="ZWT8TX", length=15)
	public String getZwt8tx() {
		return zwt8tx;
	}

	public void setZwt8tx(String zwt8tx) {
		this.zwt8tx = zwt8tx;
	}

	@Column(name="ZZFUNMODE", length=2)
	public String getZzfunmode() {
		return zzfunmode;
	}

	public void setZzfunmode(String zzfunmode) {
		this.zzfunmode = zzfunmode;
	}

	public static WorkWithOrderDetailsPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithOrderDetailsPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}