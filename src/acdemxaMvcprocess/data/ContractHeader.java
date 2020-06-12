package acdemxaMvcprocess.data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Contract Header (CONHDR).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="CONHDR")
public class ContractHeader implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private Integer contract = 0;
	private String debtor = "";
	private String custRef = "";
	private Date contractDate = new Date(0);
	private String status = "";
	private String rep = "";
	private Double contractValue = 0.0;
	private String address = "";
	private String address2 = "";
	private String address3 = "";
	private String address4 = "";
	private String postCode = "";
	private String country = "";

	@Id
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

	@Column(name="XWCREF", length=20)
	public String getCustRef() {
		return custRef;
	}

	public void setCustRef(String custRef) {
		if (custRef == null) {
			custRef = "";
		}

		this.custRef = custRef;
	}

	@Column(name="XWDLDT", length=10)
	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		if (contractDate == null) {
			contractDate = new Date(0);
		}

		this.contractDate = contractDate;
	}

	@Column(name="XWSTAT", length=2)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status == null) {
			status = "";
		}

		this.status = status;
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

	@Column(name="XWTAMT", precision=13, scale=2)
	public Double getContractValue() {
		return contractValue;
	}

	public void setContractValue(Double contractValue) {
		if (contractValue == null) {
			contractValue = 0.0;
		}

		this.contractValue = contractValue;
	}

	@Column(name="XWADD1", length=40)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if (address == null) {
			address = "";
		}

		this.address = address;
	}

	@Column(name="XWADD2", length=40)
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		if (address2 == null) {
			address2 = "";
		}

		this.address2 = address2;
	}

	@Column(name="XWADD3", length=40)
	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		if (address3 == null) {
			address3 = "";
		}

		this.address3 = address3;
	}

	@Column(name="XWADD4", length=40)
	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		if (address4 == null) {
			address4 = "";
		}

		this.address4 = address4;
	}

	@Column(name="XWPCDE", length=10)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		if (postCode == null) {
			postCode = "";
		}

		this.postCode = postCode;
	}

	@Column(name="XWCNTR", length=3)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if (country == null) {
			country = "";
		}

		this.country = country;
	}

}