package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Transaction type description (TRNTYP).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="TRNTYP")
public class TransactionTypeDescription implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String transactionType = "";
	private String description = "";

	@Id
	@Column(name="XWRICD", length=3)
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		if (transactionType == null) {
			transactionType = "";
		}

		this.transactionType = transactionType;
	}

	@Column(name="XWTDSC", length=20)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null) {
			description = "";
		}

		this.description = description;
	}

}