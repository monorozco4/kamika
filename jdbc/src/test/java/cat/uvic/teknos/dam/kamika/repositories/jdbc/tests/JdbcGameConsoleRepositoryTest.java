package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.model.GameConsole;
import cat.uvic.teknos.dam.kamika.model.impl.GameConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGameConsoleRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcGameConsoleRepositoryTest {

    private JdbcGameConsoleRepository repository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // Crear tabla si no existe
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GAME_CONSOLE (
                    GAME_ID INT NOT NULL,
                    CONSOLE_ID INT NOT NULL,
                    RELEASE_DATE DATE,
                    IS_EXCLUSIVE BOOLEAN,
                    RESOLUTION VARCHAR(255),
                    PRIMARY KEY (GAME_ID, CONSOLE_ID)
                )
            """);

            // Limpiar tabla antes de cada test
            stmt.execute("DELETE FROM GAME_CONSOLE");

        } catch (Exception e) {
            fail("Error setting up database", e);
        }

        repository = new JdbcGameConsoleRepository(dataSource);
    }

    @Test
    void shouldSaveAndFindGameConsoleByCompositeKey() {
        GameConsole gc = new GameConsoleImpl();
        gc.setGameId(1);
        gc.setConsoleId(100);
        gc.setResolution("1920x1080");
        gc.setExclusive(true);
        gc.setReleaseDate(LocalDate.now());

        repository.save(gc);

        Optional<GameConsole> found = repository.findById(1, 100);
        assertTrue(found.isPresent());
        assertEquals("1920x1080", found.get().getResolution());
        assertTrue(found.get().isExclusive());
    }

    @Test
    void shouldUpdateGameConsoleWhenIdsExist() {
        GameConsole gc = new GameConsoleImpl();
        gc.setGameId(2);
        gc.setConsoleId(200);
        gc.setResolution("1280x720");
        gc.setExclusive(false);
        repository.save(gc);

        GameConsole updated = new GameConsoleImpl();
        updated.setGameId(2);
        updated.setConsoleId(200);
        updated.setResolution("3840x2160");
        updated.setExclusive(true);
        updated.setReleaseDate(LocalDate.of(2022, 12, 31));

        repository.save(updated);

        Optional<GameConsole> found = repository.findById(2, 200);
        assertTrue(found.isPresent());
        assertEquals("3840x2160", found.get().getResolution());
        assertTrue(found.get().isExclusive());
    }

    @Test
    void shouldDeleteGameConsoleUsingEntity() {
        GameConsole gc = new GameConsoleImpl();
        gc.setGameId(3);
        gc.setConsoleId(300);
        repository.save(gc);

        repository.delete(gc);

        assertFalse(repository.existsById(3, 300));
    }

    @Test
    void shouldDeleteGameConsoleByIds() {
        GameConsole gc = new GameConsoleImpl();
        gc.setGameId(4);
        gc.setConsoleId(400);
        repository.save(gc);

        boolean deleted = repository.deleteById(4, 400);
        assertTrue(deleted);
        assertFalse(repository.existsById(4, 400));
    }

    @Test
    void shouldCountRelationsCorrectly() {
        GameConsole gc1 = new GameConsoleImpl();
        gc1.setGameId(5);
        gc1.setConsoleId(500);
        repository.save(gc1);

        GameConsole gc2 = new GameConsoleImpl();
        gc2.setGameId(6);
        gc2.setConsoleId(600);
        repository.save(gc2);

        long count = repository.count();
        assertEquals(2, count);
    }

    @Test
    void shouldReturnTrueIfRelationExists() {
        GameConsole gc = new GameConsoleImpl();
        gc.setGameId(7);
        gc.setConsoleId(700);
        repository.save(gc);

        assertTrue(repository.existsById(7, 700));
    }

    @Test
    void shouldReturnFalseIfRelationDoesNotExist() {
        assertFalse(repository.existsById(999, 999));
    }

    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        GameConsole invalid = new GameConsoleImpl();
        invalid.setGameId(-1); // ID inválido

        assertThrows(CrudException.class, () -> repository.save(invalid));
    }
}