package cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SchemaLoaderTest {
    @Test
    void verificaDataSqlEnClasspath() {
        assertNotNull(SchemaLoader.class.getResourceAsStream("/data.sql"), "data.sql no está en el classpath");
    }
}