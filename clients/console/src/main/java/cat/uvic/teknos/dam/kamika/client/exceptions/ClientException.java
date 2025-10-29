package cat.uvic.teknos.dam.kamika.client.exceptions;

/**
 * A custom exception for errors that occur within the API client.
 * This wraps lower-level exceptions into a specific error type,
 * allowing the UI layer to handle all client-side errors uniformly.
 * Author: Montse
 * Version: 2.0.0
 */
public class ClientException extends RuntimeException {

    /**
     * Constructs a new ClientException with the specified detail message.
     * @param message the detail message.
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Constructs a new ClientException with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the root cause of the exception.
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}