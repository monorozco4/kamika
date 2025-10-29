package cat.uvic.teknos.dam.kamika.app.exceptions;

/**
 * Exception thrown when an error related to banners occurs in the application.
 * <p>
 * This is a custom unchecked exception that extends {@link RuntimeException}.
 */
public class BannerException extends RuntimeException {

    /**
     * Constructs a new {@code BannerException} with {@code null} as its detail message.
     */
    public BannerException() {
    }

    /**
     * Constructs a new {@code BannerException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public BannerException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code BannerException} with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception (which is saved for later retrieval).
     */
    public BannerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code BannerException} with the specified cause.
     *
     * @param cause the cause of the exception (which is saved for later retrieval).
     */
    public BannerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code BannerException} with full control over suppression and stack trace writability.
     *
     * @param message            the detail message.
     * @param cause              the cause of the exception.
     * @param enableSuppression  whether suppression is enabled or disabled.
     * @param writableStackTrace whether the stack trace should be writable.
     */
    public BannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
