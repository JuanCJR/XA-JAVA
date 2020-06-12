package acdemxaMvcprocess.data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.databorough.utils.IXRedoModel;
import com.databorough.utils.TypeUtil;

/**
 * Composite Keys file for Transaction History (TRNHST).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Embeddable
public class TransactionHistoryId implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	private Integer period;
	private Date trnHstDate;
	private Long trnHstSeq;

	public TransactionHistoryId() {}

	public TransactionHistoryId(Integer period, Date trnHstDate, Long trnHstSeq) {
		this.period = period;
		this.trnHstDate = trnHstDate;
		this.trnHstSeq = trnHstSeq;
	}

	@Column(name="XWE4NB", precision=6)
	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		if (period == null) {
			period = (Integer)TypeUtil.getDefaultValue(Integer.class);
		}

		this.period = period;
	}

	@Column(name="XWDLDT", length=10)
	public Date getTrnHstDate() {
		return trnHstDate;
	}

	public void setTrnHstDate(Date trnHstDate) {
		if (trnHstDate == null) {
			trnHstDate = (Date)TypeUtil.getDefaultValue(Date.class);
		}

		this.trnHstDate = trnHstDate;
	}

	@Column(name="XWC8DT", precision=11)
	public Long getTrnHstSeq() {
		return trnHstSeq;
	}

	public void setTrnHstSeq(Long trnHstSeq) {
		if (trnHstSeq == null) {
			trnHstSeq = (Long)TypeUtil.getDefaultValue(Long.class);
		}

		this.trnHstSeq = trnHstSeq;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		if (!(other instanceof TransactionHistoryId)) {
			return false;
		}

		TransactionHistoryId castOther = (TransactionHistoryId) other;

		boolean v = new EqualsBuilder()
			.append(period, castOther.period)
			.append(trnHstDate, castOther.trnHstDate)
			.append(trnHstSeq, castOther.trnHstSeq)
			.isEquals();

		return v;
	}

	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		int v = new HashCodeBuilder(17, 37)
			.append(period)
			.append(trnHstDate)
			.append(trnHstSeq)
			.toHashCode();

	 	return v;
	}
}