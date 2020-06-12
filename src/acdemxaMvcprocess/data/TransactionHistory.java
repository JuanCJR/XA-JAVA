package acdemxaMvcprocess.data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Transaction History (TRNHST).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="TRNHST")
@IdClass(TransactionHistoryId.class)
public class TransactionHistory implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	public TransactionHistoryId transactionHistoryId;
	private Integer period = 0;
	private Date trnHstDate = new Date(0);
	private Long trnHstSeq = 0l;
	private String debtor = "";
	private String dGrp = "";
	private String delArea = "";
	private String rep = "";
	private String store = "";
	private String product = "";
	private String stkGrp1 = "";
	private String stkGrp2 = "";
	private String stkGrp3 = "";
	private String docRef = "";
	private Integer contract = 0;
	private String trn = "";
	private Double quantity = 0.0;
	private Double value = 0.0;

	public TransactionHistory() {
		transactionHistoryId = new TransactionHistoryId();
	}

	public TransactionHistory(TransactionHistoryId transactionHistoryId) {
		this.transactionHistoryId = transactionHistoryId;
		this.period = transactionHistoryId.getPeriod(); 
		this.trnHstDate = transactionHistoryId.getTrnHstDate(); 
		this.trnHstSeq = transactionHistoryId.getTrnHstSeq(); 
	}

	/*public TransactionHistoryId getTransactionHistoryId() {
		return transactionHistoryId;
	}

	public void setTransactionHistoryId(TransactionHistoryId transactionHistoryId) {
		if (transactionHistoryId == null) {
			transactionHistoryId = new TransactionHistoryId();
		}

		this.transactionHistoryId = transactionHistoryId;
	}*/

	@Id
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	@Id
	public Date getTrnHstDate() {
		return trnHstDate;
	}

	public void setTrnHstDate(Date trnHstDate) {
		this.trnHstDate = trnHstDate;
	}

	@Id
	public Long getTrnHstSeq() {
		return trnHstSeq;
	}

	public void setTrnHstSeq(Long trnHstSeq) {
		this.trnHstSeq = trnHstSeq;
	}


	@Column(name="XWBCCD", length=11)
	public String getDebtor() {
		return debtor;
	}

	public void setDebtor(String debtor) {
		if (debtor == null) {
			debtor = "";
		}

		this.debtor = debtor;
	}

	@Column(name="XWBNCD", length=2)
	public String getDGrp() {
		return dGrp;
	}

	public void setDGrp(String dGrp) {
		if (dGrp == null) {
			dGrp = "";
		}

		this.dGrp = dGrp;
	}

	@Column(name="XWBDCD", length=3)
	public String getDelArea() {
		return delArea;
	}

	public void setDelArea(String delArea) {
		if (delArea == null) {
			delArea = "";
		}

		this.delArea = delArea;
	}

	@Column(name="PERSON", length=3)
	public String getRep() {
		return rep;
	}

	public void setRep(String rep) {
		if (rep == null) {
			rep = "";
		}

		this.rep = rep;
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

	@Column(name="XWAGCD", length=2)
	public String getStkGrp1() {
		return stkGrp1;
	}

	public void setStkGrp1(String stkGrp1) {
		if (stkGrp1 == null) {
			stkGrp1 = "";
		}

		this.stkGrp1 = stkGrp1;
	}

	@Column(name="XWAHCD", length=2)
	public String getStkGrp2() {
		return stkGrp2;
	}

	public void setStkGrp2(String stkGrp2) {
		if (stkGrp2 == null) {
			stkGrp2 = "";
		}

		this.stkGrp2 = stkGrp2;
	}

	@Column(name="XWAICD", length=2)
	public String getStkGrp3() {
		return stkGrp3;
	}

	public void setStkGrp3(String stkGrp3) {
		if (stkGrp3 == null) {
			stkGrp3 = "";
		}

		this.stkGrp3 = stkGrp3;
	}

	@Column(name="XWT8TX", length=15)
	public String getDocRef() {
		return docRef;
	}

	public void setDocRef(String docRef) {
		if (docRef == null) {
			docRef = "";
		}

		this.docRef = docRef;
	}

	@Column(name="XWORDN", precision=6)
	public Integer getContract() {
		return contract;
	}

	public void setContract(Integer contract) {
		if (contract == null) {
			contract = 0;
		}

		this.contract = contract;
	}

	@Column(name="XWRICD", length=3)
	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		if (trn == null) {
			trn = "";
		}

		this.trn = trn;
	}

	@Column(name="XWA5QT", precision=9, scale=2)
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		if (quantity == null) {
			quantity = 0.0;
		}

		this.quantity = quantity;
	}

	@Column(name="XWVALU", precision=13, scale=2)
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		if (value == null) {
			value = 0.0;
		}

		this.value = value;
	}

}