package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Panel Data Object for Distributor Selection (DISTSSEL).
 *
 * @author KAMALN
 */
public class DistributorSelectionPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String dname = "";
	private String dsdcde = "";
	private String parnam = "";
	private String pcode = "";
	private String dssel = "";

	public DistributorSelectionPDO() {
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

	@Column(name="DNAME", length=34)
	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	@Column(name="DSDCDE", length=2)
	public String getDsdcde() {
		return dsdcde;
	}

	public void setDsdcde(String dsdcde) {
		this.dsdcde = dsdcde;
	}

	@Column(name="PARNAM", length=34)
	public String getParnam() {
		return parnam;
	}

	public void setParnam(String parnam) {
		this.parnam = parnam;
	}

	@Column(name="PCODE", length=2)
	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	public static DistributorSelectionPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				DistributorSelectionPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}