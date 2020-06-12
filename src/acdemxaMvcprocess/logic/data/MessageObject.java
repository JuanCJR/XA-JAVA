package acdemxaMvcprocess.logic.data;

public class MessageObject {
	private Integer messageStatus = 0; // xa_status
	private String messageField = ""; // xa_msgfd
	private String messageId = ""; // xa_msgid
	private String messageText = ""; // xa_msgds

	public String getMessageField() {
		return messageField;
	}

	public String getMessageId() {
		return messageId;
	}

	public int getMessageStatus() {
		return messageStatus;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageField(String messageField) {
		this.messageField = messageField;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setMessageStatus(int messageStatus) {
		this.messageStatus = messageStatus;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("messageId:<" + messageId + ">" + "," + "messageText:<" +
			messageText + ">" + "," + "messageStatus:<" + messageStatus + ">" +
			"," + "messageField:<" + messageField + ">");

		return str.toString();
	}
}
