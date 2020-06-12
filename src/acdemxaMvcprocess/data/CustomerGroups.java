package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Customer Groups (CUSGRP).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="CUSGRP")
public class CustomerGroups implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String cusGrp = "";
	private String description = "";

	@Id
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

	@Column(name="XWKHTX", length=40)
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