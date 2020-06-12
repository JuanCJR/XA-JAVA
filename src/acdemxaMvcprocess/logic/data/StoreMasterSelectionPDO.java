package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Panel Data Object for Store Master Selection (STOMASEL).
 *
 * @author KAMALN
 */
public class StoreMasterSelectionPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String xwaacs = "";
	private String xwdesc = "";
	private String parnam = "";
	private String stre = "";
	private String dssel = "";

	public StoreMasterSelectionPDO() {
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

	@Column(name="XWAACS", length=11)
	public String getXwaacs() {
		return xwaacs;
	}

	public void setXwaacs(String xwaacs) {
		this.xwaacs = xwaacs;
	}

	@Column(name="XWDESC", length=20)
	public String getXwdesc() {
		return xwdesc;
	}

	public void setXwdesc(String xwdesc) {
		this.xwdesc = xwdesc;
	}

	@Column(name="PARNAM", length=20)
	public String getParnam() {
		return parnam;
	}

	public void setParnam(String parnam) {
		this.parnam = parnam;
	}

	@Column(name="STRE", length=11)
	public String getStre() {
		return stre;
	}

	public void setStre(String stre) {
		this.stre = stre;
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	public static StoreMasterSelectionPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				StoreMasterSelectionPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}