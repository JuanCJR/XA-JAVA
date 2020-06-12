package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Contract Detail (CONDET).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="CONDET")
@IdClass(ContractDetailId.class)
public class ContractDetail implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	public ContractDetailId contractDetailId;
	private Integer contract = 0;
	private String product = "";
	private String store = "";
	private String refNo = "";
	private String trnHstTrnType = "";
	private Double contractQty = 0.0;
	private String um = "";
	private Double price = 0.0;

	public ContractDetail() {
		contractDetailId = new ContractDetailId();
	}

	public ContractDetail(ContractDetailId contractDetailId) {
		this.contractDetailId = contractDetailId;
		this.contract = contractDetailId.getContract(); 
		this.product = contractDetailId.getProduct(); 
	}

	/*public ContractDetailId getContractDetailId() {
		return contractDetailId;
	}

	public void setContractDetailId(ContractDetailId contractDetailId) {
		if (contractDetailId == null) {
			contractDetailId = new ContractDetailId();
		}

		this.contractDetailId = contractDetailId;
	}*/

	@Id
	public Integer getContract() {
		return contract;
	}

	public void setContract(Integer contract) {
		this.contract = contract;
	}

	@Id
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}


	@Column(name="XWAACS", length=11)
	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		if (store == null) {
			store = "";
		}

		this.store = store;
	}

	@Column(name="XWT8TX", length=15)
	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		if (refNo == null) {
			refNo = "";
		}

		this.refNo = refNo;
	}

	@Column(name="XWRICD", length=3)
	public String getTrnHstTrnType() {
		return trnHstTrnType;
	}

	public void setTrnHstTrnType(String trnHstTrnType) {
		if (trnHstTrnType == null) {
			trnHstTrnType = "";
		}

		this.trnHstTrnType = trnHstTrnType;
	}

	@Column(name="XWA5QT", precision=9, scale=2)
	public Double getContractQty() {
		return contractQty;
	}

	public void setContractQty(Double contractQty) {
		if (contractQty == null) {
			contractQty = 0.0;
		}

		this.contractQty = contractQty;
	}

	@Column(name="XWA2CD", length=3)
	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		if (um == null) {
			um = "";
		}

		this.um = um;
	}

	@Column(name="XWPRIC", precision=6, scale=2)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		if (price == null) {
			price = 0.0;
		}

		this.price = price;
	}

}