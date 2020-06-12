package acdemxaMvcprocess.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

/**
 * Entity file for Salespersons (SLMEN).
 * File Attribute: PF.
 *
 * @author KAMALN
 */
@Entity
@Table(name="SLMEN")
public class Salespersons implements java.io.Serializable, IXRedoModel {
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	private String person = "";
	private String fullName = "";

	@Id
	@Column(name="PERSON", length=3)
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		if (person == null) {
			person = "";
		}

		this.person = person;
	}

	@Column(name="PNAME", length=34)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		if (fullName == null) {
			fullName = "";
		}

		this.fullName = fullName;
	}

}