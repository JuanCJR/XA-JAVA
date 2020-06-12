package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Store Master Selection (STOMASEL).
 *
 * @author KAMALN
 */
public class StoreMasterSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwaacs = "";
	private String xwdesc = "";

	public StoreMasterSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
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

	public static StoreMasterSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				StoreMasterSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}