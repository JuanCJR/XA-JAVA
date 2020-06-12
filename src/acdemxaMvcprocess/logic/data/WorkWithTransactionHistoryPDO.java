package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Panel Data Object for Work with transaction history (WWTRNHST).
 *
 * @author KAMALN
 */
public class WorkWithTransactionHistoryPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String actdsp = "";
	private Integer cusno = 0;
	private String percus = "";
	private String person = "";
	private String pname = "";
	private String recnam = "";
	private String sfield = "";
	private String sname = "";
	private String swbccd = "";
	private String telno = "";
	private String xwaacs = "";
	private String xwabcd = "";
	private String xwagcd = "";
	private String xwahcd = "";
	private String xwaicd = "";
	private Double xwa5qt = 0.0D;
	private String xwbccd = "";
	private String xwbdcd = "";
	private String xwbncd = "";
	private Long xwc8dt = 0L;
	private DateEx xwdldt = new DateEx('L');
	private Integer xwe4nb = 0;
	private Double xwgiva = 0.0D;
	private String xwg4tx = "";
	private Double xwidv0 = 0.0D;
	private Integer xwordn = 0;
	private String xwricd = "";
	private String xwt8tx = "";
	private Double xwvalu = 0.0D;
	private Integer zzdate = 0;
	private String zzpgm = "";
	private String dssel = "";
	private Integer locate = 0;
	private String locat2 = "";
	private Integer shwrec = 0;
	private String zmsage = "";
	private String msgid = "";
	private String zperson = "";
	private String zwaacs = "";
	private String zwabcd = "";
	private String zwagcd = "";
	private String zwahcd = "";
	private String zwaicd = "";
	private Double zwa5qt = 0.0D;
	private String zwbccd = "";
	private String zwbdcd = "";
	private String zwbncd = "";
	private Long zwc8dt = 0L;
	private DateEx zwdldt = new DateEx('D');
	private Integer zwe4nb = 0;
	private Integer zwordn = 0;
	private String zwricd = "";
	private String zwt8tx = "";
	private Double zwvalu = 0.0D;
	private String zzfunmode = "";

	public WorkWithTransactionHistoryPDO() {
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

	@Column(name="CUSNO", length=5)
	public Integer getCusno() {
		return cusno;
	}

	public void setCusno(Integer cusno) {
		this.cusno = cusno;
	}

	@Column(name="PERCUS", length=3)
	public String getPercus() {
		return percus;
	}

	public void setPercus(String percus) {
		this.percus = percus;
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

	@Column(name="SNAME", length=34)
	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	@Column(name="SWBCCD", length=11)
	public String getSwbccd() {
		return swbccd;
	}

	public void setSwbccd(String swbccd) {
		this.swbccd = swbccd;
	}

	@Column(name="TELNO", length=17)
	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
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

	@Column(name="XWAGCD", length=2)
	public String getXwagcd() {
		return xwagcd;
	}

	public void setXwagcd(String xwagcd) {
		this.xwagcd = xwagcd;
	}

	@Column(name="XWAHCD", length=2)
	public String getXwahcd() {
		return xwahcd;
	}

	public void setXwahcd(String xwahcd) {
		this.xwahcd = xwahcd;
	}

	@Column(name="XWAICD", length=2)
	public String getXwaicd() {
		return xwaicd;
	}

	public void setXwaicd(String xwaicd) {
		this.xwaicd = xwaicd;
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

	@Column(name="XWBDCD", length=3)
	public String getXwbdcd() {
		return xwbdcd;
	}

	public void setXwbdcd(String xwbdcd) {
		this.xwbdcd = xwbdcd;
	}

	@Column(name="XWBNCD", length=2)
	public String getXwbncd() {
		return xwbncd;
	}

	public void setXwbncd(String xwbncd) {
		this.xwbncd = xwbncd;
	}

	@Column(name="XWC8DT", length=11)
	public Long getXwc8dt() {
		return xwc8dt;
	}

	public void setXwc8dt(Long xwc8dt) {
		this.xwc8dt = xwc8dt;
	}

	@Column(name="XWDLDT", length=10)
	public DateEx getXwdldt() {
		return xwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwdldt(DateEx xwdldt) {
		this.xwdldt = xwdldt;
	}

	@Column(name="XWE4NB", length=6)
	public Integer getXwe4nb() {
		return xwe4nb;
	}

	public void setXwe4nb(Integer xwe4nb) {
		this.xwe4nb = xwe4nb;
	}

	@Column(name="XWGIVA", precision=15, scale=2)
	public Double getXwgiva() {
		return xwgiva;
	}

	public void setXwgiva(Double xwgiva) {
		this.xwgiva = xwgiva;
	}

	@Column(name="XWG4TX", length=40)
	public String getXwg4tx() {
		return xwg4tx;
	}

	public void setXwg4tx(String xwg4tx) {
		this.xwg4tx = xwg4tx;
	}

	@Column(name="XWIDV0", precision=15, scale=2)
	public Double getXwidv0() {
		return xwidv0;
	}

	public void setXwidv0(Double xwidv0) {
		this.xwidv0 = xwidv0;
	}

	@Column(name="XWORDN", length=6)
	public Integer getXwordn() {
		return xwordn;
	}

	public void setXwordn(Integer xwordn) {
		this.xwordn = xwordn;
	}

	@Column(name="XWRICD", length=3)
	public String getXwricd() {
		return xwricd;
	}

	public void setXwricd(String xwricd) {
		this.xwricd = xwricd;
	}

	@Column(name="XWT8TX", length=15)
	public String getXwt8tx() {
		return xwt8tx;
	}

	public void setXwt8tx(String xwt8tx) {
		this.xwt8tx = xwt8tx;
	}

	@Column(name="XWVALU", precision=13, scale=2)
	public Double getXwvalu() {
		return xwvalu;
	}

	public void setXwvalu(Double xwvalu) {
		this.xwvalu = xwvalu;
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

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="LOCATE", length=6)
	public Integer getLocate() {
		return locate;
	}

	public void setLocate(Integer locate) {
		this.locate = locate;
	}

	@Column(name="LOCAT2", length=3)
	public String getLocat2() {
		return locat2;
	}

	public void setLocat2(String locat2) {
		this.locat2 = locat2;
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

	@Column(name="ZPERSON", length=3)
	public String getZperson() {
		return zperson;
	}

	public void setZperson(String zperson) {
		this.zperson = zperson;
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

	@Column(name="ZWAGCD", length=2)
	public String getZwagcd() {
		return zwagcd;
	}

	public void setZwagcd(String zwagcd) {
		this.zwagcd = zwagcd;
	}

	@Column(name="ZWAHCD", length=2)
	public String getZwahcd() {
		return zwahcd;
	}

	public void setZwahcd(String zwahcd) {
		this.zwahcd = zwahcd;
	}

	@Column(name="ZWAICD", length=2)
	public String getZwaicd() {
		return zwaicd;
	}

	public void setZwaicd(String zwaicd) {
		this.zwaicd = zwaicd;
	}

	@Column(name="ZWA5QT", precision=9, scale=2)
	public Double getZwa5qt() {
		return zwa5qt;
	}

	public void setZwa5qt(Double zwa5qt) {
		this.zwa5qt = zwa5qt;
	}

	@Column(name="ZWBCCD", length=11)
	public String getZwbccd() {
		return zwbccd;
	}

	public void setZwbccd(String zwbccd) {
		this.zwbccd = zwbccd;
	}

	@Column(name="ZWBDCD", length=3)
	public String getZwbdcd() {
		return zwbdcd;
	}

	public void setZwbdcd(String zwbdcd) {
		this.zwbdcd = zwbdcd;
	}

	@Column(name="ZWBNCD", length=2)
	public String getZwbncd() {
		return zwbncd;
	}

	public void setZwbncd(String zwbncd) {
		this.zwbncd = zwbncd;
	}

	@Column(name="ZWC8DT", length=11)
	public Long getZwc8dt() {
		return zwc8dt;
	}

	public void setZwc8dt(Long zwc8dt) {
		this.zwc8dt = zwc8dt;
	}

	@Column(name="ZWDLDT", length=0)
	public DateEx getZwdldt() {
		return zwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setZwdldt(DateEx zwdldt) {
		this.zwdldt = zwdldt;
	}

	@Column(name="ZWE4NB", length=6)
	public Integer getZwe4nb() {
		return zwe4nb;
	}

	public void setZwe4nb(Integer zwe4nb) {
		this.zwe4nb = zwe4nb;
	}

	@Column(name="ZWORDN", length=6)
	public Integer getZwordn() {
		return zwordn;
	}

	public void setZwordn(Integer zwordn) {
		this.zwordn = zwordn;
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

	@Column(name="ZWVALU", precision=13, scale=2)
	public Double getZwvalu() {
		return zwvalu;
	}

	public void setZwvalu(Double zwvalu) {
		this.zwvalu = zwvalu;
	}

	@Column(name="ZZFUNMODE", precision=2, scale=2)
	public String getZzfunmode() {
		return zzfunmode;
	}

	public void setZzfunmode(String zzfunmode) {
		this.zzfunmode = zzfunmode;
	}

	public static WorkWithTransactionHistoryPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithTransactionHistoryPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}