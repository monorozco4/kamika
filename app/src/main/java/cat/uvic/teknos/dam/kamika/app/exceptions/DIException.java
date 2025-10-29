package cat.uvic.teknos.dam.kamika.app.exceptions;

/**
 * Exception thrown when a Dependency Injection (DI) error occurs in the application.
 * <p>
 * This is a custom unchecked exception that extends {@link RuntimeException}.
 */
public class DIException extends RuntimeException {

    /**
     * Constructs a new {@code DIException} with {@code null} as its detail message.
     */
    public DIException() {
    }

    /**
     * Constructs a new {@code DIException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public DIException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code DIException} with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception (which is saved for later retrieval).
     */
    public DIException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code DIException} with the specified cause.
     *
     * @param cause the cause of the exception (which is saved for later retrieval).
     */
    public DIException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code DIException} with full control over suppression and stack trace writability.
     *
     * @param message            the detail message.
     * @param cause              the cause of the exception.
     * @param enableSuppression  whether suppression is enabled or disabled.
     * @param writableStackTrace whether the stack trace should be writable.
     */
    public DIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
