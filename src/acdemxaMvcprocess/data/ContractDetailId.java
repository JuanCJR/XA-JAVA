package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.databorough.utils.IXRedoModel;
import com.databorough.utils.TypeUtil;

/**
 * Composite Keys file for Contract Detail (CONDET).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Embeddable
public class ContractDetailId implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	private Integer contract;
	private String product;

	public ContractDetailId() {}

	public ContractDetailId(Integer contract, String product) {
		this.contract = contract;
		this.product = product;
	}

	@Column(name="XWORDN", precision=6)
	public Integer getContract() {
		return contract;
	}

	public void setContract(Integer contract) {
		if (contract == null) {
			contract = (Integer)TypeUtil.getDefaultValue(Integer.class);
		}

		this.contract = contract;
	}

	@Column(name="XWABCD", length=20)
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		if (product == null) {
			product = (String)TypeUtil.getDefaultValue(String.class);
		}

		this.product = product;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		if (!(other instanceof ContractDetailId)) {
			return false;
		}

		ContractDetailId castOther = (ContractDetailId) other;

		boolean v = new EqualsBuilder()
			.append(contract, castOther.contract)
			.append(product, castOther.product)
			.isEquals();

		return v;
	}

	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		int v = new HashCodeBuilder(17, 37)
			.append(contract)
			.append(product)
			.toHashCode();

	 	return v;
	}
}