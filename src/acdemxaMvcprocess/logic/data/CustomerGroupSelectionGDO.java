package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Customer group Selection (CUSGRSEL).
 *
 * @author KAMALN
 */
public class CustomerGroupSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwbncd = "";
	private String xwkhtx = "";

	public CustomerGroupSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="XWBNCD", length=2)
	public String getXwbncd() {
		return xwbncd;
	}

	public void setXwbncd(String xwbncd) {
		this.xwbncd = xwbncd;
	}

	@Column(name="XWKHTX", length=40)
	public String getXwkhtx() {
		return xwkhtx;
	}

	public void setXwkhtx(String xwkhtx) {
		this.xwkhtx = xwkhtx;
	}

	public static CustomerGroupSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				CustomerGroupSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}