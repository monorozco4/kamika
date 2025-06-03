package cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.DataSourceException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A data source that provides a single shared database connection.
 * Suitable for simple applications or testing environments.
 */
public class SingleConnectionDataSource implements DataSource, AutoCloseable {

    private Connection connection;
    private final String driver;
    private final String server;
    private final String database;
    private final String user;
    private final String password;

    public SingleConnectionDataSource(String driver, String server, String database, String user, String password) {
        this.driver = validateProperty(driver, "driver");
        this.server = validateProperty(server, "server");
        this.database = validateProperty(database, "database");
        this.user = validateProperty(user, "user");
        this.password = password; // Password can be empty (e.g., for H2)
    }

    public SingleConnectionDataSource() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getResourceAsStream("/datasource.properties")) {
            if (inputStream == null) {
                throw new DataSourceException("File datasource.properties not found in resources");
            }
            properties.load(inputStream);

            this.driver = getProperty(properties, "driver");
            this.server = getProperty(properties, "server");
            this.database = getProperty(properties, "database");
            this.user = getProperty(properties, "user");
            this.password = properties.getProperty("password", ""); // Default to empty

        } catch (Exception e) {
            throw new DataSourceException("Failed to load datasource.properties", e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url;
                if ("h2".equalsIgnoreCase(driver)) {
                    // Formato especial para H2 en memoria
                    url = String.format("jdbc:%s:%s", driver, server);
                } else {
                    // Formato genérico para otros motores como MySQL
                    url = String.format("jdbc:%s://%s/%s", driver, server, database);
                }

                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to establish database connection", e);
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DataSourceException("Error closing database connection", e);
        }
    }

    private String validateProperty(String value, String propertyName) {
        if (value == null || value.trim().isEmpty()) {
            throw new DataSourceException("Invalid or missing property: " + propertyName);
        }
        return value;
    }

    private String getProperty(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new DataSourceException("Missing or empty property: " + key);
        }
        return value;
    }

    // Optional getters for testing purposes

    public String getDriver() {
        return driver;
    }

    public String getServer() {
        return server;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}