package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Order status Selection (ORDSTSEL).
 *
 * @author KAMALN
 */
public class OrderStatusSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwsdsc = "";
	private String xwstat = "";

	public OrderStatusSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="XWSDSC", length=20)
	public String getXwsdsc() {
		return xwsdsc;
	}

	public void setXwsdsc(String xwsdsc) {
		this.xwsdsc = xwsdsc;
	}

	@Column(name="XWSTAT", length=2)
	public String getXwstat() {
		return xwstat;
	}

	public void setXwstat(String xwstat) {
		this.xwstat = xwstat;
	}

	public static OrderStatusSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				OrderStatusSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}