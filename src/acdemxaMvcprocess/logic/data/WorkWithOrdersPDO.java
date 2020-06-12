package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Panel Data Object for Work with Orders (WWCONHDR).
 *
 * @author KAMALN
 */
public class WorkWithOrdersPDO extends PanelDataObject {
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
	private String telno = "";
	private String xwadd1 = "";
	private String xwadd2 = "";
	private String xwadd3 = "";
	private String xwadd4 = "";
	private String xwbccd = "";
	private String xwcntr = "";
	private String xwcref = "";
	private DateEx xwdldt = new DateEx('L');
	private Double xwgiva = 0.0D;
	private String xwg4tx = "";
	private Double xwidv0 = 0.0D;
	private Integer xwordn = 0;
	private String xwpcde = "";
	private String xwsdsc = "";
	private String xwstat = "";
	private Double xwtamt = 0.0D;
	private Integer zzdate = 0;
	private String dssel = "";
	private Integer locate = 0;
	private Integer shwrec = 0;
	private String zmsage = "";
	private String zzpgm = "";
	private String swbccd = "";
	private String msgid = "";
	private String zperson = "";
	private String zwadd1 = "";
	private String zwadd2 = "";
	private String zwadd3 = "";
	private String zwadd4 = "";
	private String zwbccd = "";
	private String zwcntr = "";
	private String zwcref = "";
	private DateEx zwdldt = new DateEx('D');
	private Integer zwordn = 0;
	private String zwpcde = "";
	private String zwstat = "";
	private Double zwtamt = 0.0D;
	private String zzfunmode = "";

	public WorkWithOrdersPDO() {
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

	@Column(name="TELNO", length=17)
	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
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

	@Column(name="XWDLDT", length=10)
	public DateEx getXwdldt() {
		return xwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwdldt(DateEx xwdldt) {
		this.xwdldt = xwdldt;
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

	@Column(name="XWPCDE", length=10)
	public String getXwpcde() {
		return xwpcde;
	}

	public void setXwpcde(String xwpcde) {
		this.xwpcde = xwpcde;
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

	@Column(name="ZZDATE", length=8)
	public Integer getZzdate() {
		return zzdate;
	}

	public void setZzdate(Integer zzdate) {
		this.zzdate = zzdate;
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

	@Column(name="ZZPGM", length=10)
	public String getZzpgm() {
		return zzpgm;
	}

	public void setZzpgm(String zzpgm) {
		this.zzpgm = zzpgm;
	}

	@Column(name="SWBCCD", length=11)
	public String getSwbccd() {
		return swbccd;
	}

	public void setSwbccd(String swbccd) {
		this.swbccd = swbccd;
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

	@Column(name="ZWADD1", length=40)
	public String getZwadd1() {
		return zwadd1;
	}

	public void setZwadd1(String zwadd1) {
		this.zwadd1 = zwadd1;
	}

	@Column(name="ZWADD2", length=40)
	public String getZwadd2() {
		return zwadd2;
	}

	public void setZwadd2(String zwadd2) {
		this.zwadd2 = zwadd2;
	}

	@Column(name="ZWADD3", length=40)
	public String getZwadd3() {
		return zwadd3;
	}

	public void setZwadd3(String zwadd3) {
		this.zwadd3 = zwadd3;
	}

	@Column(name="ZWADD4", length=40)
	public String getZwadd4() {
		return zwadd4;
	}

	public void setZwadd4(String zwadd4) {
		this.zwadd4 = zwadd4;
	}

	@Column(name="ZWBCCD", length=11)
	public String getZwbccd() {
		return zwbccd;
	}

	public void setZwbccd(String zwbccd) {
		this.zwbccd = zwbccd;
	}

	@Column(name="ZWCNTR", length=3)
	public String getZwcntr() {
		return zwcntr;
	}

	public void setZwcntr(String zwcntr) {
		this.zwcntr = zwcntr;
	}

	@Column(name="ZWCREF", length=20)
	public String getZwcref() {
		return zwcref;
	}

	public void setZwcref(String zwcref) {
		this.zwcref = zwcref;
	}

	@Column(name="ZWDLDT", length=0)
	public DateEx getZwdldt() {
		return zwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setZwdldt(DateEx zwdldt) {
		this.zwdldt = zwdldt;
	}

	@Column(name="ZWORDN", length=6)
	public Integer getZwordn() {
		return zwordn;
	}

	public void setZwordn(Integer zwordn) {
		this.zwordn = zwordn;
	}

	@Column(name="ZWPCDE", length=10)
	public String getZwpcde() {
		return zwpcde;
	}

	public void setZwpcde(String zwpcde) {
		this.zwpcde = zwpcde;
	}

	@Column(name="ZWSTAT", length=2)
	public String getZwstat() {
		return zwstat;
	}

	public void setZwstat(String zwstat) {
		this.zwstat = zwstat;
	}

	@Column(name="ZWTAMT", precision=13, scale=2)
	public Double getZwtamt() {
		return zwtamt;
	}

	public void setZwtamt(Double zwtamt) {
		this.zwtamt = zwtamt;
	}

	@Column(name="ZZFUNMODE", precision=2, scale=2)
	public String getZzfunmode() {
		return zzfunmode;
	}

	public void setZzfunmode(String zzfunmode) {
		this.zzfunmode = zzfunmode;
	}

	public static WorkWithOrdersPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithOrdersPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}