package cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface defining a source of database connections.
 */
public interface DataSource {

    /**
     * Returns a database connection.
     * @return an active database connection
     */
    Connection getConnection();

    /**
     * Closes any resources held by this data source, such as open connections or pools.
     * By default, does nothing. Can be overridden by implementations.
     *
     * @throws Exception if an error occurs during closing
     */
    default void close() throws Exception {
        // No-op by default
    }
}