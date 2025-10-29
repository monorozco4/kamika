package cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions;

/**
 * Represents an error that occurred during a CRUD operation.
 * Wraps low-level exceptions like SQLException to provide cleaner error handling.
 */
public class CrudException extends RuntimeException {

    public CrudException() {
        super();
    }

    public CrudException(String message) {
        super(message);
    }

    public CrudException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrudException(Throwable cause) {
        super(cause);
    }

    public CrudException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}