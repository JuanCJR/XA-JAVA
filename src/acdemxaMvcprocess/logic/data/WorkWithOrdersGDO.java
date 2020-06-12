package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Grid Data Object for Work with Orders (WWCONHDR).
 *
 * @author KAMALN
 */
public class WorkWithOrdersGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String person = "";
	private String xwbccd = "";
	private String xwcref = "";
	private DateEx xwdldt = new DateEx('L');
	private Integer xwordn = 0;
	private String xwstat = "";
	private Double xwtamt = 0.0D;

	public WorkWithOrdersGDO() {
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

	@Column(name="XWBCCD", length=11)
	public String getXwbccd() {
		return xwbccd;
	}

	public void setXwbccd(String xwbccd) {
		this.xwbccd = xwbccd;
	}

	@Column(name="XWCREF", length=20)
	public String getXwcref() {
		return xwcref;
	}

	public void setXwcref(String xwcref) {
		this.xwcref = xwcref;
	}

	@Column(name="XWDLDT", length=10)
	public DateEx getXwdldt() {
		return xwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwdldt(DateEx xwdldt) {
		this.xwdldt = xwdldt;
	}

	@Column(name="XWORDN", length=6)
	public Integer getXwordn() {
		return xwordn;
	}

	public void setXwordn(Integer xwordn) {
		this.xwordn = xwordn;
	}

	@Column(name="XWSTAT", length=2)
	public String getXwstat() {
		return xwstat;
	}

	public void setXwstat(String xwstat) {
		this.xwstat = xwstat;
	}

	@Column(name="XWTAMT", precision=13, scale=2)
	public Double getXwtamt() {
		return xwtamt;
	}

	public void setXwtamt(Double xwtamt) {
		this.xwtamt = xwtamt;
	}

	public static WorkWithOrdersGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithOrdersGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}