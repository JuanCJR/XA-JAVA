package acdemxaMvcprocess.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.databorough.utils.IXRedoModel;

@Entity
@Table(name="XINFDTAARA")
public class DataAreaInfo implements Serializable, IXRedoModel {
	private static final long serialVersionUID = 1L;
	private Integer decimalPosition = 0;
	private Integer length = 0;
	private String dataAreaName = "";
	private String lastModified = "";
	private String remoteDataArea = "";
	private String remoteDataAreaLib = "";
	private String remoteLocation = "";
	private String timestamp = "";
	private String type = "";
	private String value = "";

	@Id
	@Column(name="DTANAM", length=10)
	public String getDataAreaName() {
		return dataAreaName;
	}

	@Column(name="DTADEC", length=1, precision=0)
	public Integer getDecimalPosition() {
		return decimalPosition;
	}

	@Column(name="LASTMODIF", length=10)
	public String getLastModified() {
		return lastModified;
	}

	@Column(name="DTALEN", length=4, precision=0)
	public Integer getLength() {
		return length;
	}

	@Column(name="RMTDTAARA", length=10)
	public String getRemoteDataArea() {
		return remoteDataArea;
	}

	@Column(name="RMTDTALIB", length=10)
	public String getRemoteDataAreaLib() {
		return remoteDataAreaLib;
	}

	@Column(name="RMTLOCNAM", length=20)
	public String getRemoteLocation() {
		return remoteLocation;
	}

	@Column(name="TIMESTAMP", length=26)
	public String getTimestamp() {
		return timestamp;
	}

	@Column(name="DTATYP", length=1)
	public String getType() {
		return type;
	}

	@Column(name="DTAVAL", length=2000)
	public String getValue() {
		return value;
	}

	public void setDataAreaName(String dataAreaName) {
		this.dataAreaName = dataAreaName;
	}

	public void setDecimalPosition(Integer decimalPosition) {
		this.decimalPosition = decimalPosition;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public void setRemoteDataArea(String remoteDataArea) {
		this.remoteDataArea = remoteDataArea;
	}

	public void setRemoteDataAreaLib(String remoteDataAreaLib) {
		this.remoteDataAreaLib = remoteDataAreaLib;
	}

	public void setRemoteLocation(String remoteLocation) {
		this.remoteLocation = remoteLocation;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
