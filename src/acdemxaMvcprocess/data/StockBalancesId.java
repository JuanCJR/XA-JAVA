package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.databorough.utils.IXRedoModel;
import com.databorough.utils.TypeUtil;

/**
 * Composite Keys file for Stock Balances (STKBAL).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Embeddable
public class StockBalancesId implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	private String product;
	private String store;

	public StockBalancesId() {}

	public StockBalancesId(String product, String store) {
		this.product = product;
		this.store = store;
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

	@Column(name="XWAACS", length=11)
	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		if (store == null) {
			store = (String)TypeUtil.getDefaultValue(String.class);
		}

		this.store = store;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		if (!(other instanceof StockBalancesId)) {
			return false;
		}

		StockBalancesId castOther = (StockBalancesId) other;

		boolean v = new EqualsBuilder()
			.append(product, castOther.product)
			.append(store, castOther.store)
			.isEquals();

		return v;
	}

	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		int v = new HashCodeBuilder(17, 37)
			.append(product)
			.append(store)
			.toHashCode();

	 	return v;
	}
}