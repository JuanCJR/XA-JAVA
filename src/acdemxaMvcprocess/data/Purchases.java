package acdemxaMvcprocess.data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Purchases (CUSTS).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="CUSTS")
public class Purchases implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String customer = "";
	private String name = "";
	private String statementAccount = "";
	private String relatedAccount = "";
	private String taxReg = "";
	private Integer bank = 0;
	private Long bankAC = 0l;
	private String forex = "";
	private String cusGrp = "";
	private String rep = "";
	private String distributor = "";
	private String terms = "";
	private Double creditLimit = 0.0;
	private String stlDsc = "";
	private String intX = "";
	private String crGuarantee = "";
	private String bo = "";
	private String lang = "";
	private Date dateLoaded = new Date(0);
	private Date chgDate = new Date(0);
	private Date lastSale = new Date(0);
	private Double amtLastSale = 0.0;
	private Date dateLastPay = new Date(0);
	private Double lastPay = 0.0;
	private Date dateLastStmt = new Date(0);
	private Double currPay = 0.0;
	private Double currClaim = 0.0;
	private Double currSls = 0.0;
	private Double currCost = 0.0;
	private Double ytdSales = 0.0;
	private Double lastYearSls = 0.0;
	private Double balance = 0.0;
	private Double currentAmt = 0.0;
	private Double x030Days = 0.0;
	private Double x060Days = 0.0;
	private Double x090Days = 0.0;
	private Double x120Days = 0.0;
	private Double x150Days = 0.0;
	private Double x180Days = 0.0;
	private Integer prospectNo = 0;

	@Id
	@Column(name="XWBCCD", length=11)
	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		if (customer == null) {
			customer = "";
		}

		this.customer = customer;
	}

	@Column(name="XWG4TX", length=40)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			name = "";
		}

		this.name = name;
	}

	@Column(name="XWB2CD", length=11)
	public String getStatementAccount() {
		return statementAccount;
	}

	public void setStatementAccount(String statementAccount) {
		if (statementAccount == null) {
			statementAccount = "";
		}

		this.statementAccount = statementAccount;
	}

	@Column(name="XWB3CD", length=11)
	public String getRelatedAccount() {
		return relatedAccount;
	}

	public void setRelatedAccount(String relatedAccount) {
		if (relatedAccount == null) {
			relatedAccount = "";
		}

		this.relatedAccount = relatedAccount;
	}

	@Column(name="XWHITX", length=15)
	public String getTaxReg() {
		return taxReg;
	}

	public void setTaxReg(String taxReg) {
		if (taxReg == null) {
			taxReg = "";
		}

		this.taxReg = taxReg;
	}

	@Column(name="XWE0NB", precision=9)
	public Integer getBank() {
		return bank;
	}

	public void setBank(Integer bank) {
		if (bank == null) {
			bank = 0;
		}

		this.bank = bank;
	}

	@Column(name="XWJUN0", precision=15)
	public Long getBankAC() {
		return bankAC;
	}

	public void setBankAC(Long bankAC) {
		if (bankAC == null) {
			bankAC = 0l;
		}

		this.bankAC = bankAC;
	}

	@Column(name="XWDVCD", length=3)
	public String getForex() {
		return forex;
	}

	public void setForex(String forex) {
		if (forex == null) {
			forex = "";
		}

		this.forex = forex;
	}

	@Column(name="XWBNCD", length=2)
	public String getCusGrp() {
		return cusGrp;
	}

	public void setCusGrp(String cusGrp) {
		if (cusGrp == null) {
			cusGrp = "";
		}

		this.cusGrp = cusGrp;
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

	@Column(name="XWBTCD", length=3)
	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		if (terms == null) {
			terms = "";
		}

		this.terms = terms;
	}

	@Column(name="XWGIVA", precision=15, scale=2)
	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		if (creditLimit == null) {
			creditLimit = 0.0;
		}

		this.creditLimit = creditLimit;
	}

	@Column(name="XWAOCD", length=3)
	public String getStlDsc() {
		return stlDsc;
	}

	public void setStlDsc(String stlDsc) {
		if (stlDsc == null) {
			stlDsc = "";
		}

		this.stlDsc = stlDsc;
	}

	@Column(name="XWBBCD", length=3)
	public String getIntX() {
		return intX;
	}

	public void setIntX(String intX) {
		if (intX == null) {
			intX = "";
		}

		this.intX = intX;
	}

	@Column(name="XWG4T0", length=10)
	public String getCrGuarantee() {
		return crGuarantee;
	}

	public void setCrGuarantee(String crGuarantee) {
		if (crGuarantee == null) {
			crGuarantee = "";
		}

		this.crGuarantee = crGuarantee;
	}

	@Column(name="XWC7ST", length=1)
	public String getBo() {
		return bo;
	}

	public void setBo(String bo) {
		if (bo == null) {
			bo = "";
		}

		this.bo = bo;
	}

	@Column(name="XWDAST", length=1)
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		if (lang == null) {
			lang = "";
		}

		this.lang = lang;
	}

	@Column(name="XWBPD0", length=10)
	public Date getDateLoaded() {
		return dateLoaded;
	}

	public void setDateLoaded(Date dateLoaded) {
		if (dateLoaded == null) {
			dateLoaded = new Date(0);
		}

		this.dateLoaded = dateLoaded;
	}

	@Column(name="XWBAD0", length=10)
	public Date getChgDate() {
		return chgDate;
	}

	public void setChgDate(Date chgDate) {
		if (chgDate == null) {
			chgDate = new Date(0);
		}

		this.chgDate = chgDate;
	}

	@Column(name="XWARDT", length=10)
	public Date getLastSale() {
		return lastSale;
	}

	public void setLastSale(Date lastSale) {
		if (lastSale == null) {
			lastSale = new Date(0);
		}

		this.lastSale = lastSale;
	}

	@Column(name="XWF0VA", precision=15, scale=2)
	public Double getAmtLastSale() {
		return amtLastSale;
	}

	public void setAmtLastSale(Double amtLastSale) {
		if (amtLastSale == null) {
			amtLastSale = 0.0;
		}

		this.amtLastSale = amtLastSale;
	}

	@Column(name="XWASDT", length=10)
	public Date getDateLastPay() {
		return dateLastPay;
	}

	public void setDateLastPay(Date dateLastPay) {
		if (dateLastPay == null) {
			dateLastPay = new Date(0);
		}

		this.dateLastPay = dateLastPay;
	}

	@Column(name="XWF0V0", precision=15, scale=2)
	public Double getLastPay() {
		return lastPay;
	}

	public void setLastPay(Double lastPay) {
		if (lastPay == null) {
			lastPay = 0.0;
		}

		this.lastPay = lastPay;
	}

	@Column(name="XWBQDT", length=10)
	public Date getDateLastStmt() {
		return dateLastStmt;
	}

	public void setDateLastStmt(Date dateLastStmt) {
		if (dateLastStmt == null) {
			dateLastStmt = new Date(0);
		}

		this.dateLastStmt = dateLastStmt;
	}

	@Column(name="XWF1VA", precision=15, scale=2)
	public Double getCurrPay() {
		return currPay;
	}

	public void setCurrPay(Double currPay) {
		if (currPay == null) {
			currPay = 0.0;
		}

		this.currPay = currPay;
	}

	@Column(name="XWF1V0", precision=15, scale=2)
	public Double getCurrClaim() {
		return currClaim;
	}

	public void setCurrClaim(Double currClaim) {
		if (currClaim == null) {
			currClaim = 0.0;
		}

		this.currClaim = currClaim;
	}

	@Column(name="XWF2VA", precision=15, scale=2)
	public Double getCurrSls() {
		return currSls;
	}

	public void setCurrSls(Double currSls) {
		if (currSls == null) {
			currSls = 0.0;
		}

		this.currSls = currSls;
	}

	@Column(name="XWF3VA", precision=15, scale=2)
	public Double getCurrCost() {
		return currCost;
	}

	public void setCurrCost(Double currCost) {
		if (currCost == null) {
			currCost = 0.0;
		}

		this.currCost = currCost;
	}

	@Column(name="XWF4VA", precision=15, scale=2)
	public Double getYtdSales() {
		return ytdSales;
	}

	public void setYtdSales(Double ytdSales) {
		if (ytdSales == null) {
			ytdSales = 0.0;
		}

		this.ytdSales = ytdSales;
	}

	@Column(name="XWF6VA", precision=15, scale=2)
	public Double getLastYearSls() {
		return lastYearSls;
	}

	public void setLastYearSls(Double lastYearSls) {
		if (lastYearSls == null) {
			lastYearSls = 0.0;
		}

		this.lastYearSls = lastYearSls;
	}

	@Column(name="XWIDV0", precision=15, scale=2)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		if (balance == null) {
			balance = 0.0;
		}

		this.balance = balance;
	}

	@Column(name="XWGAVA", precision=15, scale=2)
	public Double getCurrentAmt() {
		return currentAmt;
	}

	public void setCurrentAmt(Double currentAmt) {
		if (currentAmt == null) {
			currentAmt = 0.0;
		}

		this.currentAmt = currentAmt;
	}

	@Column(name="XWGBVA", precision=15, scale=2)
	public Double getX030Days() {
		return x030Days;
	}

	public void setX030Days(Double x030Days) {
		if (x030Days == null) {
			x030Days = 0.0;
		}

		this.x030Days = x030Days;
	}

	@Column(name="XWGCVA", precision=15, scale=2)
	public Double getX060Days() {
		return x060Days;
	}

	public void setX060Days(Double x060Days) {
		if (x060Days == null) {
			x060Days = 0.0;
		}

		this.x060Days = x060Days;
	}

	@Column(name="XWGDVA", precision=15, scale=2)
	public Double getX090Days() {
		return x090Days;
	}

	public void setX090Days(Double x090Days) {
		if (x090Days == null) {
			x090Days = 0.0;
		}

		this.x090Days = x090Days;
	}

	@Column(name="XWGEVA", precision=15, scale=2)
	public Double getX120Days() {
		return x120Days;
	}

	public void setX120Days(Double x120Days) {
		if (x120Days == null) {
			x120Days = 0.0;
		}

		this.x120Days = x120Days;
	}

	@Column(name="XWGFVA", precision=15, scale=2)
	public Double getX150Days() {
		return x150Days;
	}

	public void setX150Days(Double x150Days) {
		if (x150Days == null) {
			x150Days = 0.0;
		}

		this.x150Days = x150Days;
	}

	@Column(name="XWGGVA", precision=15, scale=2)
	public Double getX180Days() {
		return x180Days;
	}

	public void setX180Days(Double x180Days) {
		if (x180Days == null) {
			x180Days = 0.0;
		}

		this.x180Days = x180Days;
	}

	@Column(name="CUSNO", precision=5)
	public Integer getProspectNo() {
		return prospectNo;
	}

	public void setProspectNo(Integer prospectNo) {
		if (prospectNo == null) {
			prospectNo = 0;
		}

		this.prospectNo = prospectNo;
	}

}