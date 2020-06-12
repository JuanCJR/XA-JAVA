package acdemxaMvcprocess.logic.data;

import static com.databorough.utils.AS400Utils.getJobDate;
import static com.databorough.utils.AS400Utils.getJobName;
import static com.databorough.utils.AS400Utils.getJobNumber;
import static com.databorough.utils.AS400Utils.getJobUser;
import static com.databorough.utils.StringUtils.padStringWithValue;

/**
 * <code>ProgramStatusDataStructure</code> (PSDS) contains subfields that hold a
 * lot of information about the current program and the job it's running in. It
 * provides the easiest way to get the program name, the job name, user ID
 * information and more.
 *
 * @author Amit Arya
 * @version (2013-03-07.11:30:25)
 */
public class ProgramStatusDataStructure {
	@FiPsDsFld(name="zjobname", fromPos=244, length=10)
	private String zjobname = "";
	@FiPsDsFld(name="zjobnumber", fromPos=264, precision=6)
	private Integer zjobnumber = 0;
	@FiPsDsFld(name="zjobdate", fromPos=270, precision=6)
	private Integer zjobdate = 0;
	// zjobtime for 'A' module
	@FiPsDsFld(name="zjobtime", fromPos=282, precision=6)
	private Integer zjobtime = 0;
	// zjobhour, zjobminute for Batch program
	@FiPsDsFld(name="zjobhour", fromPos=282, precision=2)
	public Integer zjobhour = 0;
	@FiPsDsFld(name="zjobminute", fromPos=283, precision=2)
	public Integer zjobminute = 0;
	@FiPsDsFld(name="qqPGM", fromPos=334, length=10)
	private String qqpgm = "";
	@FiPsDsFld(name="zprogram", fromPos=334, length=10)
	private String zprogram = "";
	@FiPsDsFld(name="zusername", fromPos=358, length=10)
	private String zusername = "";

	public String getZjobname() {
		if ("".equals(zjobname)) {
			zjobname = getJobName();
		}

		return zjobname;
	}

	public Integer getZjobnumber() {
		if (zjobnumber == 0) {
			zjobnumber = getJobNumber();
		}

		return zjobnumber;
	}

	public Integer getZjobdate() {
		if (zjobdate == 0) {
			zjobdate = getJobDate();
		}

		return zjobdate;
	}

	public Integer getZjobtime() {
		return zjobtime;
	}

	public Integer getZjobhour() {
		return zjobhour;
	}

	public Integer getZjobminute() {
		return zjobminute;
	}

	public String getQqpgm() {
		return qqpgm;
	}

	public String getZprogram() {
		return zprogram;
	}

	public String getZusername() {
		if ("".equals(zusername)) {
			zusername = getJobUser();
		}

		//return zusername;
		return padStringWithValue(zusername, " ", 10, false);
	}

	public void setZjobname(String zjobname) {
		this.zjobname = zjobname;
	}

	public void setZjobnumber(Integer zjobnumber) {
		this.zjobnumber = zjobnumber;
	}

	public void setZjobdate(Integer zjobdate) {
		this.zjobdate = zjobdate;
	}

	public void setZjobtime(Integer zjobtime) {
		this.zjobtime = zjobtime;
	}

	public void setZjobhour(Integer zjobhour) {
		this.zjobhour = zjobhour;
	}

	public void setZjobminute(Integer zjobminute) {
		this.zjobminute = zjobminute;
	}

	public void setQqpgm(String qqpgm) {
		this.qqpgm = qqpgm;
	}

	public void setZprogram(String zprogram) {
		this.zprogram = zprogram;
	}

	public void setZusername(String zusername) {
		this.zusername = zusername;
	}
}
