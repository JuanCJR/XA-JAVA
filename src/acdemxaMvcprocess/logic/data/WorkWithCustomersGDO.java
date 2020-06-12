package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Work with Customers (WWCUSTS).
 *
 * @author KAMALN
 */
public class WorkWithCustomersGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwbccd = "";
	private String xwg4tx = "";

	public WorkWithCustomersGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="XWBCCD", length=11)
	public String getXwbccd() {
		return xwbccd;
	}

	public void setXwbccd(String xwbccd) {
		this.xwbccd = xwbccd;
	}

	@Column(name="XWG4TX", length=40)
	public String getXwg4tx() {
		return xwg4tx;
	}

	public void setXwg4tx(String xwg4tx) {
		this.xwg4tx = xwg4tx;
	}

	public static WorkWithCustomersGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithCustomersGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}