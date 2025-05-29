package cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();
}