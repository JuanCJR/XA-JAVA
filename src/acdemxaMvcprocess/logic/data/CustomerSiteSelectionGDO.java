package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Customer Site Selection (CUSFSEL).
 *
 * @author KAMALN
 */
public class CustomerSiteSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private Integer dscusno = 0;
	private String dscname = "";

	public CustomerSiteSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="DSCUSNO", length=5)
	public Integer getDscusno() {
		return dscusno;
	}

	public void setDscusno(Integer dscusno) {
		this.dscusno = dscusno;
	}

	@Column(name="DSCNAME", length=20)
	public String getDscname() {
		return dscname;
	}

	public void setDscname(String dscname) {
		this.dscname = dscname;
	}

	public static CustomerSiteSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				CustomerSiteSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}