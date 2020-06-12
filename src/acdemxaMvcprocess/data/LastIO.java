package acdemxaMvcprocess.data;

public class LastIO {
	private boolean EOF;
	private boolean equal;
	private boolean error;
	private boolean found;

	public boolean isEndOfFile() {
		return EOF;
	}

	public boolean isEqual() {
		return equal;
	}

	public boolean isError() {
		return error;
	}

	public boolean isFound() {
		return found;
	}

	public void setAtEOF(boolean EOF) {
		this.EOF = EOF;
	}

	public void setEqual(boolean equal) {
		this.equal = equal;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public void setFound(boolean found) {
		this.found = found;
	}
}
