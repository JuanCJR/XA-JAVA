package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Store Master (STOMAS).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="STOMAS")
public class StoreMaster implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String store = "";
	private String description = "";

	@Id
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

	@Column(name="XWDESC", length=20)
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