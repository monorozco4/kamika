package cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions;

/**
 * Represents an error related to data source configuration or connection issues.
 * Used to wrap low-level exceptions such as IOException or SQLException.
 */
public class DataSourceException extends RuntimeException {

    public DataSourceException() {
        super();
    }

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }

    public DataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}