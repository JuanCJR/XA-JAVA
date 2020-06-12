package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Panel Data Object for Transaction type Selection (TRNTPSEL).
 *
 * @author KAMALN
 */
public class TransactionTypeSelectionPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String xwricd = "";
	private String xwtdsc = "";
	private String parnam = "";
	private String pcode = "";
	private String dssel = "";

	public TransactionTypeSelectionPDO() {
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

	@Column(name="XWRICD", length=3)
	public String getXwricd() {
		return xwricd;
	}

	public void setXwricd(String xwricd) {
		this.xwricd = xwricd;
	}

	@Column(name="XWTDSC", length=20)
	public String getXwtdsc() {
		return xwtdsc;
	}

	public void setXwtdsc(String xwtdsc) {
		this.xwtdsc = xwtdsc;
	}

	@Column(name="PARNAM", length=20)
	public String getParnam() {
		return parnam;
	}

	public void setParnam(String parnam) {
		this.parnam = parnam;
	}

	@Column(name="PCODE", length=3)
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

	public static TransactionTypeSelectionPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				TransactionTypeSelectionPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}