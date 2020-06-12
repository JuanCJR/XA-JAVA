package acdemxaMvcprocess.logic.batch;

/**
 * Program logic for Return message text (RTNMSGTEXT).
 * FIXME: Program out of Application area.
 *
 * @author KAMALN
 */
public class ReturnMessageText {
	public RetParms rtnmsgtext(String msgid, String errmsg) {
		retParms.msgid = msgid;
		retParms.errmsg = errmsg;
		return retParms;
	}
	public RetParms retParms = new RetParms();
	public class RetParms {
		private String msgid = "";
		private String errmsg = "";

		public String getMsgid() {
			return msgid;
		}

		public void setMsgid(String msgid) {
			this.msgid = msgid;
		}

		public String getErrmsg() {
			return errmsg;
		}

		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}
	}
}