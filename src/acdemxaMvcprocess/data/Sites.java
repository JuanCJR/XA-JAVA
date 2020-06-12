package acdemxaMvcprocess.data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.databorough.utils.DateTimeConverter;
import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Sites (CUSF).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="CUSF")
public class Sites implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String company = "";
	private String distributor = "";
	private String sts = "";
	private String phone = "";
	private String extn = "";
	private Integer lastCntDate = 0;
	private Integer nextCntDate = 0;
	private String contact = "";
	private String salutation = "";
	private String jobTitle = "";
	private String faxNo = "";
	private String email = "";
	private String website = "";
	private String address1 = "";
	private String address2 = "";
	private String address3 = "";
	private String address4 = "";
	private String country = "";
	private String postCode = "";
	private Integer cusNo = 0;
	private String commentLine1 = "";
	private String commentLine2 = "";
	private String commentLine3 = "";
	private String docSent = "";
	private String docSent1 = "";
	private String docSent2 = "";
	private String docSent3 = "";
	private String docSent4 = "";
	private String docSent5 = "";
	private String mch = "";
	private String productCode = "";
	private String origList = "";
	private String docPrf = "";
	private Integer lastLet = 0;
	private String specReq = "";
	private String ciscRiscBoth = "";
	private String altTelNo1 = "";
	private String altTelNo2 = "";
	private String salesperson = "";
	private String verified = "";
	private String verifiedBy = "";
	private Integer verificationDate = 0;
	private String createdBy = "";
	private Integer creationDate = 0;
	private String updatedBy = "";
	private Integer updateDate = 0;
	private Integer organisation = 0;

	@Column(name="CNAME", length=34)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		if (company == null) {
			company = "";
		}

		this.company = company;
	}

	@Column(name="DSDCDE", length=2)
	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		if (distributor == null) {
			distributor = "";
		}

		this.distributor = distributor;
	}

	@Column(name="STATUS", length=1)
	public String getSts() {
		return sts;
	}

	public void setSts(String sts) {
		if (sts == null) {
			sts = "";
		}

		this.sts = sts;
	}

	@Column(name="TELNO", length=17)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if (phone == null) {
			phone = "";
		}

		this.phone = phone;
	}

	@Column(name="EXTN", length=6)
	public String getExtn() {
		return extn;
	}

	public void setExtn(String extn) {
		if (extn == null) {
			extn = "";
		}

		this.extn = extn;
	}

	@Column(name="LCTDAT", precision=6)
	public Integer getLastCntDate() {
		return lastCntDate;
	}

	public void setLastCntDate(Integer lastCntDate) {
		if (lastCntDate == null) {
			lastCntDate = DateTimeConverter.getDateDigits((new Date(0)), "YYMMDD");
		}

		this.lastCntDate = lastCntDate;
	}

	@Transient
	public java.util.Date getLastCntDate_date() {
		if ((lastCntDate == null) ||
				(lastCntDate == 0)) {
			return null;
		}

		return (Date)DateTimeConverter.getDate(lastCntDate, "YYMMDD");
	}

	public void setLastCntDate_date(Date date) {
		if (date == null) {
			this.lastCntDate = null;
		}
		else {
			this.lastCntDate = DateTimeConverter.getDateDigits(date, "YYMMDD");
		}
	}

	@Column(name="APDATE", precision=6)
	public Integer getNextCntDate() {
		return nextCntDate;
	}

	public void setNextCntDate(Integer nextCntDate) {
		if (nextCntDate == null) {
			nextCntDate = DateTimeConverter.getDateDigits((new Date(0)), "YYMMDD");
		}

		this.nextCntDate = nextCntDate;
	}

	@Transient
	public java.util.Date getNextCntDate_date() {
		if ((nextCntDate == null) ||
				(nextCntDate == 0)) {
			return null;
		}

		return (Date)DateTimeConverter.getDate(nextCntDate, "YYMMDD");
	}

	public void setNextCntDate_date(Date date) {
		if (date == null) {
			this.nextCntDate = null;
		}
		else {
			this.nextCntDate = DateTimeConverter.getDateDigits(date, "YYMMDD");
		}
	}

	@Column(name="USERNM", length=34)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		if (contact == null) {
			contact = "";
		}

		this.contact = contact;
	}

	@Column(name="SALUT", length=34)
	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		if (salutation == null) {
			salutation = "";
		}

		this.salutation = salutation;
	}

	@Column(name="JTITLE", length=34)
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		if (jobTitle == null) {
			jobTitle = "";
		}

		this.jobTitle = jobTitle;
	}

	@Column(name="FAXNO", length=15)
	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		if (faxNo == null) {
			faxNo = "";
		}

		this.faxNo = faxNo;
	}

	@Column(name="EMAIL", length=40)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null) {
			email = "";
		}

		this.email = email;
	}

	@Column(name="WEBSIT", length=40)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		if (website == null) {
			website = "";
		}

		this.website = website;
	}

	@Column(name="ADD1", length=34)
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		if (address1 == null) {
			address1 = "";
		}

		this.address1 = address1;
	}

	@Column(name="ADD2", length=34)
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		if (address2 == null) {
			address2 = "";
		}

		this.address2 = address2;
	}

	@Column(name="ADD3", length=34)
	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		if (address3 == null) {
			address3 = "";
		}

		this.address3 = address3;
	}

	@Column(name="ADD4", length=34)
	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		if (address4 == null) {
			address4 = "";
		}

		this.address4 = address4;
	}

	@Column(name="CNTRY", length=25)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if (country == null) {
			country = "";
		}

		this.country = country;
	}

	@Column(name="PSCD", length=8)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		if (postCode == null) {
			postCode = "";
		}

		this.postCode = postCode;
	}

	@Id
	@Column(name="CUSNO", precision=5)
	public Integer getCusNo() {
		return cusNo;
	}

	public void setCusNo(Integer cusNo) {
		if (cusNo == null) {
			cusNo = 0;
		}

		this.cusNo = cusNo;
	}

	@Column(name="CMMNT1", length=40)
	public String getCommentLine1() {
		return commentLine1;
	}

	public void setCommentLine1(String commentLine1) {
		if (commentLine1 == null) {
			commentLine1 = "";
		}

		this.commentLine1 = commentLine1;
	}

	@Column(name="CMMNT2", length=40)
	public String getCommentLine2() {
		return commentLine2;
	}

	public void setCommentLine2(String commentLine2) {
		if (commentLine2 == null) {
			commentLine2 = "";
		}

		this.commentLine2 = commentLine2;
	}

	@Column(name="CMMNT3", length=40)
	public String getCommentLine3() {
		return commentLine3;
	}

	public void setCommentLine3(String commentLine3) {
		if (commentLine3 == null) {
			commentLine3 = "";
		}

		this.commentLine3 = commentLine3;
	}

	@Column(name="DOCRCD", length=3)
	public String getDocSent() {
		return docSent;
	}

	public void setDocSent(String docSent) {
		if (docSent == null) {
			docSent = "";
		}

		this.docSent = docSent;
	}

	@Column(name="DOC2", length=3)
	public String getDocSent1() {
		return docSent1;
	}

	public void setDocSent1(String docSent1) {
		if (docSent1 == null) {
			docSent1 = "";
		}

		this.docSent1 = docSent1;
	}

	@Column(name="DOC3", length=3)
	public String getDocSent2() {
		return docSent2;
	}

	public void setDocSent2(String docSent2) {
		if (docSent2 == null) {
			docSent2 = "";
		}

		this.docSent2 = docSent2;
	}

	@Column(name="DOC4", length=3)
	public String getDocSent3() {
		return docSent3;
	}

	public void setDocSent3(String docSent3) {
		if (docSent3 == null) {
			docSent3 = "";
		}

		this.docSent3 = docSent3;
	}

	@Column(name="DOC5", length=3)
	public String getDocSent4() {
		return docSent4;
	}

	public void setDocSent4(String docSent4) {
		if (docSent4 == null) {
			docSent4 = "";
		}

		this.docSent4 = docSent4;
	}

	@Column(name="DOC6", length=3)
	public String getDocSent5() {
		return docSent5;
	}

	public void setDocSent5(String docSent5) {
		if (docSent5 == null) {
			docSent5 = "";
		}

		this.docSent5 = docSent5;
	}

	@Column(name="OLDMCH", length=2)
	public String getMch() {
		return mch;
	}

	public void setMch(String mch) {
		if (mch == null) {
			mch = "";
		}

		this.mch = mch;
	}

	@Column(name="PRPCDE", length=2)
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		if (productCode == null) {
			productCode = "";
		}

		this.productCode = productCode;
	}

	@Column(name="LSLCDE", length=2)
	public String getOrigList() {
		return origList;
	}

	public void setOrigList(String origList) {
		if (origList == null) {
			origList = "";
		}

		this.origList = origList;
	}

	@Column(name="PREFIX", length=5)
	public String getDocPrf() {
		return docPrf;
	}

	public void setDocPrf(String docPrf) {
		if (docPrf == null) {
			docPrf = "";
		}

		this.docPrf = docPrf;
	}

	@Column(name="LLETSQ", precision=3)
	public Integer getLastLet() {
		return lastLet;
	}

	public void setLastLet(Integer lastLet) {
		if (lastLet == null) {
			lastLet = 0;
		}

		this.lastLet = lastLet;
	}

	@Column(name="SPREQ", length=40)
	public String getSpecReq() {
		return specReq;
	}

	public void setSpecReq(String specReq) {
		if (specReq == null) {
			specReq = "";
		}

		this.specReq = specReq;
	}

	@Column(name="RISC", length=1)
	public String getCiscRiscBoth() {
		return ciscRiscBoth;
	}

	public void setCiscRiscBoth(String ciscRiscBoth) {
		if (ciscRiscBoth == null) {
			ciscRiscBoth = "";
		}

		this.ciscRiscBoth = ciscRiscBoth;
	}

	@Column(name="ALTTEL", length=17)
	public String getAltTelNo1() {
		return altTelNo1;
	}

	public void setAltTelNo1(String altTelNo1) {
		if (altTelNo1 == null) {
			altTelNo1 = "";
		}

		this.altTelNo1 = altTelNo1;
	}

	@Column(name="ALTTL2", length=17)
	public String getAltTelNo2() {
		return altTelNo2;
	}

	public void setAltTelNo2(String altTelNo2) {
		if (altTelNo2 == null) {
			altTelNo2 = "";
		}

		this.altTelNo2 = altTelNo2;
	}

	@Column(name="SINIT", length=3)
	public String getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(String salesperson) {
		if (salesperson == null) {
			salesperson = "";
		}

		this.salesperson = salesperson;
	}

	@Column(name="VERYFD", length=1)
	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		if (verified == null) {
			verified = "";
		}

		this.verified = verified;
	}

	@Column(name="VINIT", length=3)
	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		if (verifiedBy == null) {
			verifiedBy = "";
		}

		this.verifiedBy = verifiedBy;
	}

	@Column(name="VFDATE", precision=6)
	public Integer getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(Integer verificationDate) {
		if (verificationDate == null) {
			verificationDate = DateTimeConverter.getDateDigits((new Date(0)), "YYMMDD");
		}

		this.verificationDate = verificationDate;
	}

	@Transient
	public java.util.Date getVerificationDate_date() {
		if ((verificationDate == null) ||
				(verificationDate == 0)) {
			return null;
		}

		return (Date)DateTimeConverter.getDate(verificationDate, "YYMMDD");
	}

	public void setVerificationDate_date(Date date) {
		if (date == null) {
			this.verificationDate = null;
		}
		else {
			this.verificationDate = DateTimeConverter.getDateDigits(date, "YYMMDD");
		}
	}

	@Column(name="CRTNAM", length=10)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		if (createdBy == null) {
			createdBy = "";
		}

		this.createdBy = createdBy;
	}

	@Column(name="CRTDAT", precision=6)
	public Integer getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Integer creationDate) {
		if (creationDate == null) {
			creationDate = DateTimeConverter.getDateDigits((new Date(0)), "YYMMDD");
		}

		this.creationDate = creationDate;
	}

	@Transient
	public java.util.Date getCreationDate_date() {
		if ((creationDate == null) ||
				(creationDate == 0)) {
			return null;
		}

		return (Date)DateTimeConverter.getDate(creationDate, "YYMMDD");
	}

	public void setCreationDate_date(Date date) {
		if (date == null) {
			this.creationDate = null;
		}
		else {
			this.creationDate = DateTimeConverter.getDateDigits(date, "YYMMDD");
		}
	}

	@Column(name="UPDNAM", length=10)
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		if (updatedBy == null) {
			updatedBy = "";
		}

		this.updatedBy = updatedBy;
	}

	@Column(name="UPDDAT", precision=6)
	public Integer getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Integer updateDate) {
		if (updateDate == null) {
			updateDate = DateTimeConverter.getDateDigits((new Date(0)), "YYMMDD");
		}

		this.updateDate = updateDate;
	}

	@Transient
	public java.util.Date getUpdateDate_date() {
		if ((updateDate == null) ||
				(updateDate == 0)) {
			return null;
		}

		return (Date)DateTimeConverter.getDate(updateDate, "YYMMDD");
	}

	public void setUpdateDate_date(Date date) {
		if (date == null) {
			this.updateDate = null;
		}
		else {
			this.updateDate = DateTimeConverter.getDateDigits(date, "YYMMDD");
		}
	}

	@Column(name="ORG", precision=5)
	public Integer getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Integer organisation) {
		if (organisation == null) {
			organisation = 0;
		}

		this.organisation = organisation;
	}

}