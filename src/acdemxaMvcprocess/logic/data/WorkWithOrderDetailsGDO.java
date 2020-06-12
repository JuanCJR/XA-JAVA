package acdemxaMvcprocess.logic.data;

import javax.persistence.Column;
import com.databorough.utils.GridDataObject;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Grid Data Object for Work with Order Details (WWCONDET).
 *
 * @author KAMALN
 */
public class WorkWithOrderDetailsGDO extends GridDataObject {
	private static final long serialVersionUID = 1L;
	private String dssel = "";
	private String xwaacs = "";
	private String swabcd = "";
	private String xwabcd = "";
	private String xwa2cd = "";
	private Double xwa5qt = 0.0D;
	private String xwhltx = "";
	private Integer xwordn = 0;
	private Double xwpric = 0.0D;
	private String xwricd = "";
	private String swt8tx = "";
	private String xwt8tx = "";

	public WorkWithOrderDetailsGDO() {
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

	@Column(name="SWABCD", length=15)
	public String getSwabcd() {
		return swabcd;
	}

	public void setSwabcd(String swabcd) {
		this.swabcd = swabcd;
	}

	@Column(name="XWABCD", length=20)
	public String getXwabcd() {
		return xwabcd;
	}

	public void setXwabcd(String xwabcd) {
		this.xwabcd = xwabcd;
	}

	@Column(name="XWA2CD", length=3)
	public String getXwa2cd() {
		return xwa2cd;
	}

	public void setXwa2cd(String xwa2cd) {
		this.xwa2cd = xwa2cd;
	}

	@Column(name="XWA5QT", precision=9, scale=2)
	public Double getXwa5qt() {
		return xwa5qt;
	}

	public void setXwa5qt(Double xwa5qt) {
		this.xwa5qt = xwa5qt;
	}

	@Column(name="XWHLTX", length=10)
	public String getXwhltx() {
		return xwhltx;
	}

	public void setXwhltx(String xwhltx) {
		this.xwhltx = xwhltx;
	}

	@Column(name="XWORDN", length=6)
	public Integer getXwordn() {
		return xwordn;
	}

	public void setXwordn(Integer xwordn) {
		this.xwordn = xwordn;
	}

	@Column(name="XWPRIC", precision=6, scale=2)
	public Double getXwpric() {
		return xwpric;
	}

	public void setXwpric(Double xwpric) {
		this.xwpric = xwpric;
	}

	@Column(name="XWRICD", length=3)
	public String getXwricd() {
		return xwricd;
	}

	public void setXwricd(String xwricd) {
		this.xwricd = xwricd;
	}

	@Column(name="SWT8TX", length=9)
	public String getSwt8tx() {
		return swt8tx;
	}

	public void setSwt8tx(String swt8tx) {
		this.swt8tx = swt8tx;
	}

	@Column(name="XWT8TX", length=15)
	public String getXwt8tx() {
		return xwt8tx;
	}

	public void setXwt8tx(String xwt8tx) {
		this.xwt8tx = xwt8tx;
	}

	public static WorkWithOrderDetailsGDO valueOf(String jsonData)
		throws Exception {
		try {
			return new ObjectMapper().readValue(jsonData,
				WorkWithOrderDetailsGDO.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}