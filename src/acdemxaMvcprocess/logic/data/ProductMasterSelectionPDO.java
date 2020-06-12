package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.PanelDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Panel Data Object for Product Master Selection (STKMASEL).
 *
 * @author KAMALN
 */
public class ProductMasterSelectionPDO extends PanelDataObject {
	private static final long serialVersionUID = 1L;
	private String zzjob = "";
	private String funMode = "";
	private String zzuser = "";
	private String xwabcd = "";
	private String xwhltx = "";
	private String parnam = "";
	private String stkprd = "";
	private String dssel = "";

	public ProductMasterSelectionPDO() {
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

	@Column(name="XWABCD", length=20)
	public String getXwabcd() {
		return xwabcd;
	}

	public void setXwabcd(String xwabcd) {
		this.xwabcd = xwabcd;
	}

	@Column(name="XWHLTX", length=10)
	public String getXwhltx() {
		return xwhltx;
	}

	public void setXwhltx(String xwhltx) {
		this.xwhltx = xwhltx;
	}

	@Column(name="PARNAM", length=10)
	public String getParnam() {
		return parnam;
	}

	public void setParnam(String parnam) {
		this.parnam = parnam;
	}

	@Column(name="STKPRD", length=20)
	public String getStkprd() {
		return stkprd;
	}

	public void setStkprd(String stkprd) {
		this.stkprd = stkprd;
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	public static ProductMasterSelectionPDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				ProductMasterSelectionPDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}