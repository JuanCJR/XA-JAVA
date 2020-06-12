package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Product Master Selection (STKMASEL).
 *
 * @author KAMALN
 */
public class ProductMasterSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwabcd = "";
	private String xwhltx = "";

	public ProductMasterSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
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

	public static ProductMasterSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				ProductMasterSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}