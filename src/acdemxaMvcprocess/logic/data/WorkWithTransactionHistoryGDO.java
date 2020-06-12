package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import com.databorough.utils.DateEx;
import com.databorough.utils.DateExDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Grid Data Object for Work with transaction history (WWTRNHST).
 *
 * @author KAMALN
 */
public class WorkWithTransactionHistoryGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String swbccd = "";
	private Long xwc8dt = 0L;
	private DateEx xwdldt = new DateEx('L');
	private Integer xwe4nb = 0;
	private String xwricd = "";

	public WorkWithTransactionHistoryGDO() {
		super();
	}

	@Column(name="DSSEL", length=1)
	public String getDssel() {
		return dssel;
	}

	public void setDssel(String dssel) {
		this.dssel = dssel;
	}

	@Column(name="SWBCCD", length=11)
	public String getSwbccd() {
		return swbccd;
	}

	public void setSwbccd(String swbccd) {
		this.swbccd = swbccd;
	}

	@Column(name="XWC8DT", length=11)
	public Long getXwc8dt() {
		return xwc8dt;
	}

	public void setXwc8dt(Long xwc8dt) {
		this.xwc8dt = xwc8dt;
	}

	@Column(name="XWDLDT", length=10)
	public DateEx getXwdldt() {
		return xwdldt;
	}

	@JsonDeserialize(using=DateExDeserializer.class)
	public void setXwdldt(DateEx xwdldt) {
		this.xwdldt = xwdldt;
	}

	@Column(name="XWE4NB", length=6)
	public Integer getXwe4nb() {
		return xwe4nb;
	}

	public void setXwe4nb(Integer xwe4nb) {
		this.xwe4nb = xwe4nb;
	}

	@Column(name="XWRICD", length=3)
	public String getXwricd() {
		return xwricd;
	}

	public void setXwricd(String xwricd) {
		this.xwricd = xwricd;
	}

	public static WorkWithTransactionHistoryGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithTransactionHistoryGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}