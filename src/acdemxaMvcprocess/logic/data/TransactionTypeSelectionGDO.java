package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Transaction type Selection (TRNTPSEL).
 *
 * @author KAMALN
 */
public class TransactionTypeSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwricd = "";
	private String xwtdsc = "";

	public TransactionTypeSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
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

	public static TransactionTypeSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				TransactionTypeSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}