package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Order status description (ORDSTS).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="ORDSTS")
public class OrderStatusDescription implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String statusCode = "";
	private String description = "";

	@Id
	@Column(name="XWSTAT", length=2)
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		if (statusCode == null) {
			statusCode = "";
		}

		this.statusCode = statusCode;
	}

	@Column(name="XWSDSC", length=20)
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