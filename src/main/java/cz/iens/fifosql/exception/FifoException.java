package cz.iens.fifosql.exception;

public class FifoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FifoException(Throwable cause) {
		super(cause);
		cause.printStackTrace();
	}
}
