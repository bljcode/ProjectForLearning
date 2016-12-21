package fan.ThreadPool;

public class ThreadPoolCreateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ThreadPoolCreateException() {
		super();
	}

	public ThreadPoolCreateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ThreadPoolCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ThreadPoolCreateException(String message) {
		super(message);
	}

	public ThreadPoolCreateException(Throwable cause) {
		super(cause);
	}
	
	

}
