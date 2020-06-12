package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Salespersons Selection (SLMENSEL).
 *
 * @author KAMALN
 */
public class SalespersonsSelectionGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String person = "";
	private String pname = "";

	public SalespersonsSelectionGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="PERSON", length=3)
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	@Column(name="PNAME", length=34)
	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public static SalespersonsSelectionGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				SalespersonsSelectionGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}