package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.model.GameEdition;
import cat.uvic.teknos.dam.kamika.model.impl.GameEditionImpl;
import cat.uvic.teknos.dam.kamika.model.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGameEditionRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(LoadDatabaseExtension.class)
class JdbcGameEditionRepositoryTest {

    private JdbcGameEditionRepository repository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // Crear tabla GAME si no existe
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GAME (
                    GAME_ID INT PRIMARY KEY AUTO_INCREMENT,
                    TITLE VARCHAR(255) NOT NULL,
                    RELEASE_YEAR INT
                )
            """);

            // Crear tabla GAME_EDITION si no existe
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GAME_EDITION (
                    GAME_ID INT NOT NULL,
                    EDITION_NAME VARCHAR(100) NOT NULL,
                    SPECIAL_CONTENT VARCHAR(100),
                    PRICE DECIMAL(6,2),
                    PRIMARY KEY (GAME_ID, EDITION_NAME),
                    FOREIGN KEY (GAME_ID) REFERENCES GAME(GAME_ID)
                )
            """);

            // Crear tabla GAME_CONSOLE si no existe
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GAME_CONSOLE (
                    GAME_ID INT NOT NULL,
                    CONSOLE_ID INT NOT NULL,
                    IS_EXCLUSIVE BOOLEAN,
                    RESOLUTION VARCHAR(255),
                    PRIMARY KEY (GAME_ID, CONSOLE_ID),
                    FOREIGN KEY (GAME_ID) REFERENCES GAME(GAME_ID),
                    FOREIGN KEY (CONSOLE_ID) REFERENCES CONSOLE(CONSOLE_ID)
                )
            """);

            // Limpiar solo lo necesario
            stmt.execute("DELETE FROM GAME_EDITION");
            stmt.execute("DELETE FROM GAME_CONSOLE");

        } catch (SQLException e) {
            fail("Error setting up database", e);
        }

        repository = new JdbcGameEditionRepository(dataSource);
    }

    @Test
    @Order(1)
    void shouldSaveAndGetGameEditionById() {
        // Simulamos un juego ya persistido
        GameImpl game = new GameImpl();
        game.setId(1); // ID fijo como si estuviera en la BD

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Special Edition");
        edition.setSpecialContent("Artbook + Soundtrack");
        edition.setPrice(69.99);

        assertDoesNotThrow(() -> repository.save(edition));

        Optional<GameEdition> found = repository.findByGameIdAndEditionName(1, "Special Edition");
        assertTrue(found.isPresent());
        assertEquals("Artbook + Soundtrack", found.get().getSpecialContent());
        assertEquals(69.99, found.get().getPrice(), 0.01);
    }

    @Test
    @Order(2)
    void shouldUpdateGameEditionWhenExists() {
        GameImpl game = new GameImpl();
        game.setId(2); // Juego ya guardado

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Deluxe Edition");
        edition.setSpecialContent("Soundtrack + DLC");
        edition.setPrice(89.99);

        repository.save(edition);

        edition.setSpecialContent("Updated Soundtrack + New DLC");
        edition.setPrice(99.99);

        repository.save(edition);

        Optional<GameEdition> updated = repository.findByGameIdAndEditionName(2, "Deluxe Edition");
        assertTrue(updated.isPresent());
        assertEquals("Updated Soundtrack + New DLC", updated.get().getSpecialContent());
        assertEquals(99.99, updated.get().getPrice(), 0.01);
    }

    @Test
    @Order(3)
    void shouldFailWhenInsertingEditionWithoutExistingGame() {
        GameImpl game = new GameImpl();
        game.setId(999); // Este juego no existe

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Fake Edition");
        edition.setPrice(59.99);

        assertThrows(CrudException.class, () -> repository.save(edition));
    }

    @Test
    @Order(4)
    void shouldDeleteGameEditionUsingEntity() {
        GameImpl game = new GameImpl();
        game.setId(3); // Juego simulado

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Limited Edition");
        edition.setPrice(129.99);

        repository.save(edition);
        repository.delete(edition);

        assertFalse(repository.existsByGameIdAndEditionName(3, "Limited Edition"));
    }

    @Test
    @Order(5)
    void shouldCountGameEditions() {
        GameImpl game = new GameImpl();
        game.setId(4); // Juego simulado

        GameEdition edition1 = new GameEditionImpl();
        edition1.setGame(game);
        edition1.setEditionName("Standard Edition");
        edition1.setPrice(59.99);

        GameEdition edition2 = new GameEditionImpl();
        edition2.setGame(game);
        edition2.setEditionName("Collector's Edition");
        edition2.setPrice(199.99);

        repository.save(edition1);
        repository.save(edition2);

        long count = repository.count();
        assertEquals(2, count);
    }

    @Test
    @Order(6)
    void shouldReturnTrueWhenGameEditionExists() {
        GameImpl game = new GameImpl();
        game.setId(5); // Juego simulado

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Retro Edition");
        edition.setPrice(49.99);

        repository.save(edition);

        assertTrue(repository.existsByGameIdAndEditionName(5, "Retro Edition"));
    }

    @Test
    @Order(7)
    void shouldReturnFalseWhenGameEditionDoesNotExist() {
        assertFalse(repository.existsByGameIdAndEditionName(999, "Nonexistent Edition"));
    }

    @Test
    @Order(8)
    void shouldThrowIllegalArgumentExceptionOnNullGame() {
        GameEdition invalid = new GameEditionImpl();
        invalid.setEditionName("NoGameEdition");

        assertThrows(IllegalArgumentException.class, () -> repository.save(invalid));
    }

    @Test
    @Order(9)
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> repository.findByGameIdAndEditionName(-1, ""));
    }
}