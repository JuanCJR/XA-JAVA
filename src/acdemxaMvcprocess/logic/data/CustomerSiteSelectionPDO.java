package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Panel Data Object for Customer Site Selection (CUSFSEL).
 *
 * @author KAMALN
 */
public class CustomerSiteSelectionPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private Integer cusno = 0;
	private String dscname = "";
	private Integer dscusno = 0;
	private String cname = "";
	private Integer customer = 0;
	private Integer zsflrrn = 0;
	private String dssel = "";

	public CustomerSiteSelectionPDO() {
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

	@Column(name="DSCNAME", length=20)
	public String getDscname() {
		return dscname;
	}

	public void setDscname(String dscname) {
		this.dscname = dscname;
	}

	@Column(name="DSCUSNO", length=5)
	public Integer getDscusno() {
		return dscusno;
	}

	public void setDscusno(Integer dscusno) {
		this.dscusno = dscusno;
	}

	@Column(name="CNAME", length=34)
	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Column(name="CUSTOMER", length=5)
	public Integer getCustomer() {
		return customer;
	}

	public void setCustomer(Integer customer) {
		this.customer = customer;
	}

	@Column(name="ZSFLRRN", length=5)
	public Integer getZsflrrn() {
		return zsflrrn;
	}

	public void setZsflrrn(Integer zsflrrn) {
		this.zsflrrn = zsflrrn;
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	public static CustomerSiteSelectionPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				CustomerSiteSelectionPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}