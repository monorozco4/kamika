package cat.uvic.teknos.dam.kamika.server.exceptions;

/**
 * Exception thrown when an unexpected error occurs on the server.
 * This typically corresponds to HTTP 500 status code.
 *
 * @author Montse
 * @version 2.0.0
 */
public class InternalServerErrorException extends RuntimeException {

    /**
     * Constructs a new InternalServerErrorException with the specified detail message.
     *
     * @param message the detail message explaining the internal error
     */
    public InternalServerErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a new InternalServerErrorException with the specified detail message and cause.
     *
     * @param message the detail message explaining the internal error
     * @param cause the underlying cause of the error
     */
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}