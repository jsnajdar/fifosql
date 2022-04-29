package cz.iens.fifosql.exception;

public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
		cause.printStackTrace();
	}
	
	
	
}
