package cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaLoaderTest {
    @Test
    void loadSchema() {
        SchemaLoader.loadScript("/test.sql");
    }
}