package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Panel Data Object for Customer Detail Maintenance (CUSTMNT1).
 *
 * @author KAMALN
 */
public class CustomerDetailMaintenancePDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private Integer cusno = 0;
	private String dname = "";
	private String pname = "";
	private String recnam = "";
	private String sdsdcde = "";
	private String sfield = "";
	private String sperson = "";
	private String swaocd = "";
	private DateEx swardt = new DateEx('L');
	private DateEx swasdt = new DateEx('L');
	private DateEx swbad0 = new DateEx('L');
	private String swbbcd = "";
	private String swbccd = "";
	private String swbncd = "";
	private DateEx swbpd0 = new DateEx('L');
	private DateEx swbqdt = new DateEx('L');
	private String swbtcd = "";
	private String swb2cd = "";
	private String swb3cd = "";
	private String swc7st = "";
	private String swdast = "";
	private String swdvcd = "";
	private Integer swe0nb = 0;
	private Double swf0va = 0.0D;
	private Double swf0v0 = 0.0D;
	private Double swf1va = 0.0D;
	private Double swf1v0 = 0.0D;
	private Double swf2va = 0.0D;
	private Double swf3va = 0.0D;
	private Double swf4va = 0.0D;
	private Double swf6va = 0.0D;
	private Double swgava = 0.0D;
	private Double swgbva = 0.0D;
	private Double swgcva = 0.0D;
	private Double swgdva = 0.0D;
	private Double swgeva = 0.0D;
	private Double swgfva = 0.0D;
	private Double swggva = 0.0D;
	private Double swgiva = 0.0D;
	private String swg4tx = "";
	private String swg4t0 = "";
	private String swhitx = "";
	private Double swidv0 = 0.0D;
	private Long swjun0 = 0L;
	private String swkhtx = "";
	private Double swtota = 0.0D;
	private String xwbccd = "";
	private Integer zzdate = 0;
	private String xsdcde = "";
	private String xwkhtx = "";
	private String dsdcde = "";
	private String person = "";
	private String xwaocd = "";
	private DateEx xwardt = new DateEx('L');
	private DateEx xwasdt = new DateEx('L');
	private DateEx xwbad0 = new DateEx('L');
	private String xwbbcd = "";
	private String xwbncd = "";
	private DateEx xwbpd0 = new DateEx('L');
	private DateEx xwbqdt = new DateEx('L');
	private String xwbtcd = "";
	private String xwb2cd = "";
	private String xwb3cd = "";
	private String xwc7st = "";
	private String xwdast = "";
	private String xwdvcd = "";
	private Integer xwe0nb = 0;
	private Double xwf0va = 0.0D;
	private Double xwf0v0 = 0.0D;
	private Double xwf1va = 0.0D;
	private Double xwf1v0 = 0.0D;
	private Double xwf2va = 0.0D;
	private Double xwf3va = 0.0D;
	private Double xwf4va = 0.0D;
	private Double xwf6va = 0.0D;
	private Double xwgava = 0.0D;
	private Double xwgbva = 0.0D;
	private Double xwgcva = 0.0D;
	private Double xwgdva = 0.0D;
	private Double xwgeva = 0.0D;
	private Double xwgfva = 0.0D;
	private Double xwggva = 0.0D;
	private Double xwgiva = 0.0D;
	private String xwg4tx = "";
	private String xwg4t0 = "";
	private String xwhitx = "";
	private Double xwidv0 = 0.0D;
	private Long xwjun0 = 0L;
	private String zmsage = "";
	private String zzpgm = "";
	private String kwbccd = "";
	private Integer cusprm = 0;
	private String msgid = "";
	private Double sdiff = 0.0D;

	public CustomerDetailMaintenancePDO() {
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

	@Column(name="CUSNO", length=5)
	public Integer getCusno() {
		return cusno;
	}

	public void setCusno(Integer cusno) {
		this.cusno = cusno;
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

	@Column(name="SDSDCDE", length=2)
	public String getSdsdcde() {
		return sdsdcde;
	}

	public void setSdsdcde(String sdsdcde) {
		this.sdsdcde = sdsdcde;
	}

	@Column(name="SFIELD", length=10)
	public String getSfield() {
		return sfield;
	}

	public void setSfield(String sfield) {
		this.sfield = sfield;
	}

	@Column(name="SPERSON", length=3)
	public String getSperson() {
		return sperson;
	}

	public void setSperson(String sperson) {
		this.sperson = sperson;
	}

	@Column(name="SWAOCD", length=3)
	public String getSwaocd() {
		return swaocd;
	}

	public void setSwaocd(String swaocd) {
		this.swaocd = swaocd;
	}

	@Column(name="SWARDT", length=10)
	public DateEx getSwardt() {
		return swardt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setSwardt(DateEx swardt) {
		this.swardt = swardt;
	}

	@Column(name="SWASDT", length=10)
	public DateEx getSwasdt() {
		return swasdt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setSwasdt(DateEx swasdt) {
		this.swasdt = swasdt;
	}

	@Column(name="SWBAD0", length=10)
	public DateEx getSwbad0() {
		return swbad0;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setSwbad0(DateEx swbad0) {
		this.swbad0 = swbad0;
	}

	@Column(name="SWBBCD", length=3)
	public String getSwbbcd() {
		return swbbcd;
	}

	public void setSwbbcd(String swbbcd) {
		this.swbbcd = swbbcd;
	}

	@Column(name="SWBCCD", length=11)
	public String getSwbccd() {
		return swbccd;
	}

	public void setSwbccd(String swbccd) {
		this.swbccd = swbccd;
	}

	@Column(name="SWBNCD", length=2)
	public String getSwbncd() {
		return swbncd;
	}

	public void setSwbncd(String swbncd) {
		this.swbncd = swbncd;
	}

	@Column(name="SWBPD0", length=10)
	public DateEx getSwbpd0() {
		return swbpd0;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setSwbpd0(DateEx swbpd0) {
		this.swbpd0 = swbpd0;
	}

	@Column(name="SWBQDT", length=10)
	public DateEx getSwbqdt() {
		return swbqdt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setSwbqdt(DateEx swbqdt) {
		this.swbqdt = swbqdt;
	}

	@Column(name="SWBTCD", length=3)
	public String getSwbtcd() {
		return swbtcd;
	}

	public void setSwbtcd(String swbtcd) {
		this.swbtcd = swbtcd;
	}

	@Column(name="SWB2CD", length=11)
	public String getSwb2cd() {
		return swb2cd;
	}

	public void setSwb2cd(String swb2cd) {
		this.swb2cd = swb2cd;
	}

	@Column(name="SWB3CD", length=11)
	public String getSwb3cd() {
		return swb3cd;
	}

	public void setSwb3cd(String swb3cd) {
		this.swb3cd = swb3cd;
	}

	@Column(name="SWC7ST", length=1)
	public String getSwc7st() {
		return swc7st;
	}

	public void setSwc7st(String swc7st) {
		this.swc7st = swc7st;
	}

	@Column(name="SWDAST", length=1)
	public String getSwdast() {
		return swdast;
	}

	public void setSwdast(String swdast) {
		this.swdast = swdast;
	}

	@Column(name="SWDVCD", length=3)
	public String getSwdvcd() {
		return swdvcd;
	}

	public void setSwdvcd(String swdvcd) {
		this.swdvcd = swdvcd;
	}

	@Column(name="SWE0NB", length=9)
	public Integer getSwe0nb() {
		return swe0nb;
	}

	public void setSwe0nb(Integer swe0nb) {
		this.swe0nb = swe0nb;
	}

	@Column(name="SWF0VA", precision=15, scale=2)
	public Double getSwf0va() {
		return swf0va;
	}

	public void setSwf0va(Double swf0va) {
		this.swf0va = swf0va;
	}

	@Column(name="SWF0V0", precision=15, scale=2)
	public Double getSwf0v0() {
		return swf0v0;
	}

	public void setSwf0v0(Double swf0v0) {
		this.swf0v0 = swf0v0;
	}

	@Column(name="SWF1VA", precision=15, scale=2)
	public Double getSwf1va() {
		return swf1va;
	}

	public void setSwf1va(Double swf1va) {
		this.swf1va = swf1va;
	}

	@Column(name="SWF1V0", precision=15, scale=2)
	public Double getSwf1v0() {
		return swf1v0;
	}

	public void setSwf1v0(Double swf1v0) {
		this.swf1v0 = swf1v0;
	}

	@Column(name="SWF2VA", precision=15, scale=2)
	public Double getSwf2va() {
		return swf2va;
	}

	public void setSwf2va(Double swf2va) {
		this.swf2va = swf2va;
	}

	@Column(name="SWF3VA", precision=15, scale=2)
	public Double getSwf3va() {
		return swf3va;
	}

	public void setSwf3va(Double swf3va) {
		this.swf3va = swf3va;
	}

	@Column(name="SWF4VA", precision=15, scale=2)
	public Double getSwf4va() {
		return swf4va;
	}

	public void setSwf4va(Double swf4va) {
		this.swf4va = swf4va;
	}

	@Column(name="SWF6VA", precision=15, scale=2)
	public Double getSwf6va() {
		return swf6va;
	}

	public void setSwf6va(Double swf6va) {
		this.swf6va = swf6va;
	}

	@Column(name="SWGAVA", precision=15, scale=2)
	public Double getSwgava() {
		return swgava;
	}

	public void setSwgava(Double swgava) {
		this.swgava = swgava;
	}

	@Column(name="SWGBVA", precision=15, scale=2)
	public Double getSwgbva() {
		return swgbva;
	}

	public void setSwgbva(Double swgbva) {
		this.swgbva = swgbva;
	}

	@Column(name="SWGCVA", precision=15, scale=2)
	public Double getSwgcva() {
		return swgcva;
	}

	public void setSwgcva(Double swgcva) {
		this.swgcva = swgcva;
	}

	@Column(name="SWGDVA", precision=15, scale=2)
	public Double getSwgdva() {
		return swgdva;
	}

	public void setSwgdva(Double swgdva) {
		this.swgdva = swgdva;
	}

	@Column(name="SWGEVA", precision=15, scale=2)
	public Double getSwgeva() {
		return swgeva;
	}

	public void setSwgeva(Double swgeva) {
		this.swgeva = swgeva;
	}

	@Column(name="SWGFVA", precision=15, scale=2)
	public Double getSwgfva() {
		return swgfva;
	}

	public void setSwgfva(Double swgfva) {
		this.swgfva = swgfva;
	}

	@Column(name="SWGGVA", precision=15, scale=2)
	public Double getSwggva() {
		return swggva;
	}

	public void setSwggva(Double swggva) {
		this.swggva = swggva;
	}

	@Column(name="SWGIVA", precision=15, scale=2)
	public Double getSwgiva() {
		return swgiva;
	}

	public void setSwgiva(Double swgiva) {
		this.swgiva = swgiva;
	}

	@Column(name="SWG4TX", length=40)
	public String getSwg4tx() {
		return swg4tx;
	}

	public void setSwg4tx(String swg4tx) {
		this.swg4tx = swg4tx;
	}

	@Column(name="SWG4T0", length=10)
	public String getSwg4t0() {
		return swg4t0;
	}

	public void setSwg4t0(String swg4t0) {
		this.swg4t0 = swg4t0;
	}

	@Column(name="SWHITX", length=15)
	public String getSwhitx() {
		return swhitx;
	}

	public void setSwhitx(String swhitx) {
		this.swhitx = swhitx;
	}

	@Column(name="SWIDV0", precision=15, scale=2)
	public Double getSwidv0() {
		return swidv0;
	}

	public void setSwidv0(Double swidv0) {
		this.swidv0 = swidv0;
	}

	@Column(name="SWJUN0", length=15)
	public Long getSwjun0() {
		return swjun0;
	}

	public void setSwjun0(Long swjun0) {
		this.swjun0 = swjun0;
	}

	@Column(name="SWKHTX", length=40)
	public String getSwkhtx() {
		return swkhtx;
	}

	public void setSwkhtx(String swkhtx) {
		this.swkhtx = swkhtx;
	}

	@Column(name="SWTOTA", precision=15, scale=2)
	public Double getSwtota() {
		return swtota;
	}

	public void setSwtota(Double swtota) {
		this.swtota = swtota;
	}

	@Column(name="XWBCCD", length=11)
	public String getXwbccd() {
		return xwbccd;
	}

	public void setXwbccd(String xwbccd) {
		this.xwbccd = xwbccd;
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

	@Column(name="XWARDT", length=10)
	public DateEx getXwardt() {
		return xwardt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwardt(DateEx xwardt) {
		this.xwardt = xwardt;
	}

	@Column(name="XWASDT", length=10)
	public DateEx getXwasdt() {
		return xwasdt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwasdt(DateEx xwasdt) {
		this.xwasdt = xwasdt;
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

	@Column(name="XWBQDT", length=10)
	public DateEx getXwbqdt() {
		return xwbqdt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwbqdt(DateEx xwbqdt) {
		this.xwbqdt = xwbqdt;
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

	@Column(name="XWF0VA", precision=15, scale=2)
	public Double getXwf0va() {
		return xwf0va;
	}

	public void setXwf0va(Double xwf0va) {
		this.xwf0va = xwf0va;
	}

	@Column(name="XWF0V0", precision=15, scale=2)
	public Double getXwf0v0() {
		return xwf0v0;
	}

	public void setXwf0v0(Double xwf0v0) {
		this.xwf0v0 = xwf0v0;
	}

	@Column(name="XWF1VA", precision=15, scale=2)
	public Double getXwf1va() {
		return xwf1va;
	}

	public void setXwf1va(Double xwf1va) {
		this.xwf1va = xwf1va;
	}

	@Column(name="XWF1V0", precision=15, scale=2)
	public Double getXwf1v0() {
		return xwf1v0;
	}

	public void setXwf1v0(Double xwf1v0) {
		this.xwf1v0 = xwf1v0;
	}

	@Column(name="XWF2VA", precision=15, scale=2)
	public Double getXwf2va() {
		return xwf2va;
	}

	public void setXwf2va(Double xwf2va) {
		this.xwf2va = xwf2va;
	}

	@Column(name="XWF3VA", precision=15, scale=2)
	public Double getXwf3va() {
		return xwf3va;
	}

	public void setXwf3va(Double xwf3va) {
		this.xwf3va = xwf3va;
	}

	@Column(name="XWF4VA", precision=15, scale=2)
	public Double getXwf4va() {
		return xwf4va;
	}

	public void setXwf4va(Double xwf4va) {
		this.xwf4va = xwf4va;
	}

	@Column(name="XWF6VA", precision=15, scale=2)
	public Double getXwf6va() {
		return xwf6va;
	}

	public void setXwf6va(Double xwf6va) {
		this.xwf6va = xwf6va;
	}

	@Column(name="XWGAVA", precision=15, scale=2)
	public Double getXwgava() {
		return xwgava;
	}

	public void setXwgava(Double xwgava) {
		this.xwgava = xwgava;
	}

	@Column(name="XWGBVA", precision=15, scale=2)
	public Double getXwgbva() {
		return xwgbva;
	}

	public void setXwgbva(Double xwgbva) {
		this.xwgbva = xwgbva;
	}

	@Column(name="XWGCVA", precision=15, scale=2)
	public Double getXwgcva() {
		return xwgcva;
	}

	public void setXwgcva(Double xwgcva) {
		this.xwgcva = xwgcva;
	}

	@Column(name="XWGDVA", precision=15, scale=2)
	public Double getXwgdva() {
		return xwgdva;
	}

	public void setXwgdva(Double xwgdva) {
		this.xwgdva = xwgdva;
	}

	@Column(name="XWGEVA", precision=15, scale=2)
	public Double getXwgeva() {
		return xwgeva;
	}

	public void setXwgeva(Double xwgeva) {
		this.xwgeva = xwgeva;
	}

	@Column(name="XWGFVA", precision=15, scale=2)
	public Double getXwgfva() {
		return xwgfva;
	}

	public void setXwgfva(Double xwgfva) {
		this.xwgfva = xwgfva;
	}

	@Column(name="XWGGVA", precision=15, scale=2)
	public Double getXwggva() {
		return xwggva;
	}

	public void setXwggva(Double xwggva) {
		this.xwggva = xwggva;
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

	@Column(name="XWIDV0", precision=15, scale=2)
	public Double getXwidv0() {
		return xwidv0;
	}

	public void setXwidv0(Double xwidv0) {
		this.xwidv0 = xwidv0;
	}

	@Column(name="XWJUN0", length=15)
	public Long getXwjun0() {
		return xwjun0;
	}

	public void setXwjun0(Long xwjun0) {
		this.xwjun0 = xwjun0;
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

	@Column(name="KWBCCD", length=11)
	public String getKwbccd() {
		return kwbccd;
	}

	public void setKwbccd(String kwbccd) {
		this.kwbccd = kwbccd;
	}

	@Column(name="CUSPRM", length=5)
	public Integer getCusprm() {
		return cusprm;
	}

	public void setCusprm(Integer cusprm) {
		this.cusprm = cusprm;
	}

	@Column(name="MSGID", length=7)
	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	@Column(name="SDIFF", precision=15, scale=2)
	public Double getSdiff() {
		return sdiff;
	}

	public void setSdiff(Double sdiff) {
		this.sdiff = sdiff;
	}

	public static CustomerDetailMaintenancePDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				CustomerDetailMaintenancePDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}