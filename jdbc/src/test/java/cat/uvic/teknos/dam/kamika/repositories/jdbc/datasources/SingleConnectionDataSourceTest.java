package cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.DataSourceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SingleConnectionDataSourceTest {

    @Test
    void getDriver() {
        var dataSource = new SingleConnectionDataSource();

        assertEquals("mysql", dataSource.getDriver());
    }

    @Test
    void getServer() {
    }

    @Test
    void getDatabase() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getPassword() {
    }

    @Test
    void getConnectionOk() {
        var dataSource = new SingleConnectionDataSource();

        var connection = dataSource.getConnection();

        assertNotNull(connection);
    }

    @Test
    void getConnectionKo() {
        var dataSource = new SingleConnectionDataSource(
                "mysql",
                "localhost",
                "kamika",
                "root",
                "rootpassword");

        assertThrows(DataSourceException.class, dataSource::getConnection);
    }
}