package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Panel Data Object for Work with Customers (WWCUSTS).
 *
 * @author KAMALN
 */
public class WorkWithCustomersPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String actdsp = "";
	private String dname = "";
	private String pname = "";
	private String recnam = "";
	private String sfield = "";
	private String xwbccd = "";
	private String xwg4tx = "";
	private String zdsdcde = "";
	private String zperson = "";
	private String zwaocd = "";
	private DateEx zwardt = new DateEx('L');
	private DateEx zwbad0 = new DateEx('L');
	private String zwbbcd = "";
	private String zwbccd = "";
	private String zwbncd = "";
	private DateEx zwbpd0 = new DateEx('L');
	private String zwbtcd = "";
	private String zwb2cd = "";
	private String zwb3cd = "";
	private String zwc7st = "";
	private String zwdast = "";
	private String zwdvcd = "";
	private Integer zwe0nb = 0;
	private Double zwgiva = 0.0D;
	private String zwg4tx = "";
	private String zwg4t0 = "";
	private String zwhitx = "";
	private Long zwjun0 = 0L;
	private String zwkhtx = "";
	private Integer zzdate = 0;
	private String xsdcde = "";
	private String xwkhtx = "";
	private String dsdcde = "";
	private String person = "";
	private String xwaocd = "";
	private DateEx xwbad0 = new DateEx('L');
	private String xwbbcd = "";
	private String xwbncd = "";
	private DateEx xwbpd0 = new DateEx('L');
	private String xwbtcd = "";
	private String xwb2cd = "";
	private String xwb3cd = "";
	private String xwc7st = "";
	private String xwdast = "";
	private String xwdvcd = "";
	private Integer xwe0nb = 0;
	private Double xwgiva = 0.0D;
	private String xwg4t0 = "";
	private String xwhitx = "";
	private Long xwjun0 = 0L;
	private String dssel = "";
	private String locate = "";
	private Integer shwrec = 0;
	private String zmsage = "";
	private String zzpgm = "";
	private String msgid = "";
	private Integer sflctr = 0;
	private String zzfunmode = "";

	public WorkWithCustomersPDO() {
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

	@Column(name="DNAME", length=34)
	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
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

	@Column(name="XWBCCD", length=11)
	public String getXwbccd() {
		return xwbccd;
	}

	public void setXwbccd(String xwbccd) {
		this.xwbccd = xwbccd;
	}

	@Column(name="XWG4TX", length=40)
	public String getXwg4tx() {
		return xwg4tx;
	}

	public void setXwg4tx(String xwg4tx) {
		this.xwg4tx = xwg4tx;
	}

	@Column(name="ZDSDCDE", length=2)
	public String getZdsdcde() {
		return zdsdcde;
	}

	public void setZdsdcde(String zdsdcde) {
		this.zdsdcde = zdsdcde;
	}

	@Column(name="ZPERSON", length=3)
	public String getZperson() {
		return zperson;
	}

	public void setZperson(String zperson) {
		this.zperson = zperson;
	}

	@Column(name="ZWAOCD", length=3)
	public String getZwaocd() {
		return zwaocd;
	}

	public void setZwaocd(String zwaocd) {
		this.zwaocd = zwaocd;
	}

	@Column(name="ZWARDT", length=10)
	public DateEx getZwardt() {
		return zwardt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setZwardt(DateEx zwardt) {
		this.zwardt = zwardt;
	}

	@Column(name="ZWBAD0", length=10)
	public DateEx getZwbad0() {
		return zwbad0;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setZwbad0(DateEx zwbad0) {
		this.zwbad0 = zwbad0;
	}

	@Column(name="ZWBBCD", length=3)
	public String getZwbbcd() {
		return zwbbcd;
	}

	public void setZwbbcd(String zwbbcd) {
		this.zwbbcd = zwbbcd;
	}

	@Column(name="ZWBCCD", length=11)
	public String getZwbccd() {
		return zwbccd;
	}

	public void setZwbccd(String zwbccd) {
		this.zwbccd = zwbccd;
	}

	@Column(name="ZWBNCD", length=2)
	public String getZwbncd() {
		return zwbncd;
	}

	public void setZwbncd(String zwbncd) {
		this.zwbncd = zwbncd;
	}

	@Column(name="ZWBPD0", length=10)
	public DateEx getZwbpd0() {
		return zwbpd0;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setZwbpd0(DateEx zwbpd0) {
		this.zwbpd0 = zwbpd0;
	}

	@Column(name="ZWBTCD", length=3)
	public String getZwbtcd() {
		return zwbtcd;
	}

	public void setZwbtcd(String zwbtcd) {
		this.zwbtcd = zwbtcd;
	}

	@Column(name="ZWB2CD", length=11)
	public String getZwb2cd() {
		return zwb2cd;
	}

	public void setZwb2cd(String zwb2cd) {
		this.zwb2cd = zwb2cd;
	}

	@Column(name="ZWB3CD", length=11)
	public String getZwb3cd() {
		return zwb3cd;
	}

	public void setZwb3cd(String zwb3cd) {
		this.zwb3cd = zwb3cd;
	}

	@Column(name="ZWC7ST", length=1)
	public String getZwc7st() {
		return zwc7st;
	}

	public void setZwc7st(String zwc7st) {
		this.zwc7st = zwc7st;
	}

	@Column(name="ZWDAST", length=1)
	public String getZwdast() {
		return zwdast;
	}

	public void setZwdast(String zwdast) {
		this.zwdast = zwdast;
	}

	@Column(name="ZWDVCD", length=3)
	public String getZwdvcd() {
		return zwdvcd;
	}

	public void setZwdvcd(String zwdvcd) {
		this.zwdvcd = zwdvcd;
	}

	@Column(name="ZWE0NB", length=9)
	public Integer getZwe0nb() {
		return zwe0nb;
	}

	public void setZwe0nb(Integer zwe0nb) {
		this.zwe0nb = zwe0nb;
	}

	@Column(name="ZWGIVA", precision=15, scale=2)
	public Double getZwgiva() {
		return zwgiva;
	}

	public void setZwgiva(Double zwgiva) {
		this.zwgiva = zwgiva;
	}

	@Column(name="ZWG4TX", length=40)
	public String getZwg4tx() {
		return zwg4tx;
	}

	public void setZwg4tx(String zwg4tx) {
		this.zwg4tx = zwg4tx;
	}

	@Column(name="ZWG4T0", length=10)
	public String getZwg4t0() {
		return zwg4t0;
	}

	public void setZwg4t0(String zwg4t0) {
		this.zwg4t0 = zwg4t0;
	}

	@Column(name="ZWHITX", length=15)
	public String getZwhitx() {
		return zwhitx;
	}

	public void setZwhitx(String zwhitx) {
		this.zwhitx = zwhitx;
	}

	@Column(name="ZWJUN0", length=15)
	public Long getZwjun0() {
		return zwjun0;
	}

	public void setZwjun0(Long zwjun0) {
		this.zwjun0 = zwjun0;
	}

	@Column(name="ZWKHTX", length=40)
	public String getZwkhtx() {
		return zwkhtx;
	}

	public void setZwkhtx(String zwkhtx) {
		this.zwkhtx = zwkhtx;
	}

	@Column(name="ZZDATE", length=8)
	public Integer getZzdate() {
		return zzdate;
	}

	public void setZzdate(Integer zzdate) {
		this.zzdate = zzdate;
	}

	@Column(name="XSDCDE", length=2)
	public String getXsdcde() {
		return xsdcde;
	}

	public void setXsdcde(String xsdcde) {
		this.xsdcde = xsdcde;
	}

	@Column(name="XWKHTX", length=40)
	public String getXwkhtx() {
		return xwkhtx;
	}

	public void setXwkhtx(String xwkhtx) {
		this.xwkhtx = xwkhtx;
	}

	@Column(name="DSDCDE", length=2)
	public String getDsdcde() {
		return dsdcde;
	}

	public void setDsdcde(String dsdcde) {
		this.dsdcde = dsdcde;
	}

	@Column(name="PERSON", length=3)
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	@Column(name="XWAOCD", length=3)
	public String getXwaocd() {
		return xwaocd;
	}

	public void setXwaocd(String xwaocd) {
		this.xwaocd = xwaocd;
	}

	@Column(name="XWBAD0", length=10)
	public DateEx getXwbad0() {
		return xwbad0;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwbad0(DateEx xwbad0) {
		this.xwbad0 = xwbad0;
	}

	@Column(name="XWBBCD", length=3)
	public String getXwbbcd() {
		return xwbbcd;
	}

	public void setXwbbcd(String xwbbcd) {
		this.xwbbcd = xwbbcd;
	}

	@Column(name="XWBNCD", length=2)
	public String getXwbncd() {
		return xwbncd;
	}

	public void setXwbncd(String xwbncd) {
		this.xwbncd = xwbncd;
	}

	@Column(name="XWBPD0", length=10)
	public DateEx getXwbpd0() {
		return xwbpd0;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwbpd0(DateEx xwbpd0) {
		this.xwbpd0 = xwbpd0;
	}

	@Column(name="XWBTCD", length=3)
	public String getXwbtcd() {
		return xwbtcd;
	}

	public void setXwbtcd(String xwbtcd) {
		this.xwbtcd = xwbtcd;
	}

	@Column(name="XWB2CD", length=11)
	public String getXwb2cd() {
		return xwb2cd;
	}

	public void setXwb2cd(String xwb2cd) {
		this.xwb2cd = xwb2cd;
	}

	@Column(name="XWB3CD", length=11)
	public String getXwb3cd() {
		return xwb3cd;
	}

	public void setXwb3cd(String xwb3cd) {
		this.xwb3cd = xwb3cd;
	}

	@Column(name="XWC7ST", length=1)
	public String getXwc7st() {
		return xwc7st;
	}

	public void setXwc7st(String xwc7st) {
		this.xwc7st = xwc7st;
	}

	@Column(name="XWDAST", length=1)
	public String getXwdast() {
		return xwdast;
	}

	public void setXwdast(String xwdast) {
		this.xwdast = xwdast;
	}

	@Column(name="XWDVCD", length=3)
	public String getXwdvcd() {
		return xwdvcd;
	}

	public void setXwdvcd(String xwdvcd) {
		this.xwdvcd = xwdvcd;
	}

	@Column(name="XWE0NB", length=9)
	public Integer getXwe0nb() {
		return xwe0nb;
	}

	public void setXwe0nb(Integer xwe0nb) {
		this.xwe0nb = xwe0nb;
	}

	@Column(name="XWGIVA", precision=15, scale=2)
	public Double getXwgiva() {
		return xwgiva;
	}

	public void setXwgiva(Double xwgiva) {
		this.xwgiva = xwgiva;
	}

	@Column(name="XWG4T0", length=10)
	public String getXwg4t0() {
		return xwg4t0;
	}

	public void setXwg4t0(String xwg4t0) {
		this.xwg4t0 = xwg4t0;
	}

	@Column(name="XWHITX", length=15)
	public String getXwhitx() {
		return xwhitx;
	}

	public void setXwhitx(String xwhitx) {
		this.xwhitx = xwhitx;
	}

	@Column(name="XWJUN0", length=15)
	public Long getXwjun0() {
		return xwjun0;
	}

	public void setXwjun0(Long xwjun0) {
		this.xwjun0 = xwjun0;
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="LOCATE", length=11)
	public String getLocate() {
		return locate;
	}

	public void setLocate(String locate) {
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

	@Column(name="MSGID", length=7)
	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	@Column(name="SFLCTR", length=3)
	public Integer getSflctr() {
		return sflctr;
	}

	public void setSflctr(Integer sflctr) {
		this.sflctr = sflctr;
	}

	@Column(name="ZZFUNMODE", length=2)
	public String getZzfunmode() {
		return zzfunmode;
	}

	public void setZzfunmode(String zzfunmode) {
		this.zzfunmode = zzfunmode;
	}

	public static WorkWithCustomersPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithCustomersPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}