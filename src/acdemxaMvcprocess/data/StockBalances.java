package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Stock Balances (STKBAL).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="STKBAL")
@IdClass(StockBalancesId.class)
public class StockBalances implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	public StockBalancesId stockBalancesId;
	private String product = "";
	private String store = "";
	private String grp1 = "";
	private String grp2 = "";
	private String grp3 = "";
	private String um = "";
	private Double onhandQuantity = 0.0;
	private Double purOrdBalance = 0.0;
	private Double stkBalSalesOrder = 0.0;
	private Double stkBalProduction = 0.0;

	public StockBalances() {
		stockBalancesId = new StockBalancesId();
	}

	public StockBalances(StockBalancesId stockBalancesId) {
		this.stockBalancesId = stockBalancesId;
		this.product = stockBalancesId.getProduct(); 
		this.store = stockBalancesId.getStore(); 
	}

	/*public StockBalancesId getStockBalancesId() {
		return stockBalancesId;
	}

	public void setStockBalancesId(StockBalancesId stockBalancesId) {
		if (stockBalancesId == null) {
			stockBalancesId = new StockBalancesId();
		}

		this.stockBalancesId = stockBalancesId;
	}*/

	@Id
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@Id
	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	@Column(name="XWBHQT", precision=13, scale=4)
	public Double getOnhandQuantity() {
		return onhandQuantity;
	}

	public void setOnhandQuantity(Double onhandQuantity) {
		if (onhandQuantity == null) {
			onhandQuantity = 0.0;
		}

		this.onhandQuantity = onhandQuantity;
	}

	@Column(name="XWBKQT", precision=13, scale=4)
	public Double getPurOrdBalance() {
		return purOrdBalance;
	}

	public void setPurOrdBalance(Double purOrdBalance) {
		if (purOrdBalance == null) {
			purOrdBalance = 0.0;
		}

		this.purOrdBalance = purOrdBalance;
	}

	@Column(name="XWBMQT", precision=13, scale=4)
	public Double getStkBalSalesOrder() {
		return stkBalSalesOrder;
	}

	public void setStkBalSalesOrder(Double stkBalSalesOrder) {
		if (stkBalSalesOrder == null) {
			stkBalSalesOrder = 0.0;
		}

		this.stkBalSalesOrder = stkBalSalesOrder;
	}

	@Column(name="XWFVQT", precision=13, scale=4)
	public Double getStkBalProduction() {
		return stkBalProduction;
	}

	public void setStkBalProduction(Double stkBalProduction) {
		if (stkBalProduction == null) {
			stkBalProduction = 0.0;
		}

		this.stkBalProduction = stkBalProduction;
	}

}