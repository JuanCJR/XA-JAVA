package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Product Master (STKMAS).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="STKMAS")
public class ProductMaster implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String product = "";
	private String desc = "";
	private String description = "";
	private String grp1 = "";
	private String grp2 = "";
	private String grp3 = "";
	private Double retailPrice = 0.0;

	@Id
	@Column(name="XWABCD", length=20)
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		if (product == null) {
			product = "";
		}

		this.product = product;
	}

	@Column(name="XWHLTX", length=10)
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		if (desc == null) {
			desc = "";
		}

		this.desc = desc;
	}

	@Column(name="XWG7TX", length=40)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null) {
			description = "";
		}

		this.description = description;
	}

	@Column(name="XWAGCD", length=2)
	public String getGrp1() {
		return grp1;
	}

	public void setGrp1(String grp1) {
		if (grp1 == null) {
			grp1 = "";
		}

		this.grp1 = grp1;
	}

	@Column(name="XWAHCD", length=2)
	public String getGrp2() {
		return grp2;
	}

	public void setGrp2(String grp2) {
		if (grp2 == null) {
			grp2 = "";
		}

		this.grp2 = grp2;
	}

	@Column(name="XWAICD", length=2)
	public String getGrp3() {
		return grp3;
	}

	public void setGrp3(String grp3) {
		if (grp3 == null) {
			grp3 = "";
		}

		this.grp3 = grp3;
	}

	@Column(name="XWANPR", precision=11, scale=2)
	public Double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		if (retailPrice == null) {
			retailPrice = 0.0;
		}

		this.retailPrice = retailPrice;
	}

}