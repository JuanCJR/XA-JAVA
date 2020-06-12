package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Distributors (DISTS).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="DISTS")
public class Distributors implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String code = "";
	private String description = "";

	@Id
	@Column(name="DSDCDE", length=2)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		if (code == null) {
			code = "";
		}

		this.code = code;
	}

	@Column(name="DNAME", length=34)
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