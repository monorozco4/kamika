package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.model.impl.PublisherImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGameRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(LoadDatabaseExtension.class)
class JdbcGameRepositoryTest {

    private JdbcGameRepository repository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // Deshabilitar verificaciones de FK temporalmente
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // Limpiar tablas en orden inverso de dependencias
            stmt.execute("DELETE FROM GAME");
            stmt.execute("DELETE FROM PUBLISHER");
            stmt.execute("DELETE FROM DEVELOPER");

            // Volver a habilitar verificaciones de FK
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            // Crear tablas si no existen
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS DEVELOPER (
                    DEVELOPER_ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(100) NOT NULL,
                    COUNTRY VARCHAR(50),
                    FOUNDATION_YEAR INT
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS PUBLISHER (
                    PUBLISHER_ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(100) NOT NULL,
                    COUNTRY VARCHAR(50),
                    DEVELOPER_ID INT UNIQUE,
                    FOREIGN KEY (DEVELOPER_ID) REFERENCES DEVELOPER(DEVELOPER_ID)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GAME (
                    GAME_ID INT PRIMARY KEY AUTO_INCREMENT,
                    TITLE VARCHAR(100) NOT NULL,
                    RELEASE_DATE DATE,
                    DEVELOPER_ID INT NOT NULL,
                    PUBLISHER_ID INT NOT NULL,
                    PEGI_RATING VARCHAR(10),
                    IS_MULTIPLAYER BOOLEAN,
                    FOREIGN KEY (DEVELOPER_ID) REFERENCES DEVELOPER(DEVELOPER_ID),
                    FOREIGN KEY (PUBLISHER_ID) REFERENCES PUBLISHER(PUBLISHER_ID)
                )
            """);

            // Inserciones conservadas del código fuente
            stmt.execute("""
                INSERT INTO DEVELOPER (DEVELOPER_ID, NAME, COUNTRY, FOUNDATION_YEAR) 
                VALUES 
                    (1, 'Naughty Dog', 'USA', 1984),
                    (2, 'FromSoftware', 'Japan', 1986),
                    (3, 'Nintendo EPD', 'Japan', 1889)
            """);

            stmt.execute("""
                INSERT INTO PUBLISHER (PUBLISHER_ID, NAME, COUNTRY, DEVELOPER_ID) 
                VALUES 
                    (1, 'Sony Interactive Entertainment', 'USA', 1),
                    (2, 'Bandai Namco Entertainment', 'Japan', 2),
                    (3, 'Nintendo', 'Japan', 3)
            """);

            stmt.execute("""
                INSERT INTO GAME (GAME_ID, TITLE, RELEASE_DATE, DEVELOPER_ID, PUBLISHER_ID, PEGI_RATING, IS_MULTIPLAYER) 
                VALUES 
                    (1, 'The Last of Us Part II', '2020-06-19', 1, 1, '18', false),
                    (2, 'Elden Ring', '2022-02-25', 2, 2, 'PEGI 16', true)
            """);

        } catch (SQLException e) {
            fail("Error setting up database", e);
        }

        repository = new JdbcGameRepository(dataSource);
    }

    @Test
    @Order(1)
    void shouldSaveAndGetGameById() {
        Optional<Game> found = repository.findById(1);
        assertTrue(found.isPresent());
        assertEquals("The Last of Us Part II", found.get().getTitle());
        assertEquals(LocalDate.of(2020, 6, 19), found.get().getReleaseDate());
        assertFalse(found.get().isMultiplayer());
    }

    @Test
    @Order(2)
    void shouldUpdateGameWhenExists() {
        Optional<Game> optionalGame = repository.findById(2);
        assertTrue(optionalGame.isPresent());
        Game existingGame = optionalGame.get();

        GameImpl gameToUpdate = new GameImpl();
        gameToUpdate.setId(existingGame.getId());
        gameToUpdate.setTitle("Elden Ring DLC Edition");
        gameToUpdate.setReleaseDate(existingGame.getReleaseDate());

        DeveloperImpl developer = new DeveloperImpl();
        developer.setId(2);
        gameToUpdate.setDeveloper(developer);

        PublisherImpl publisher = new PublisherImpl();
        publisher.setId(2);
        gameToUpdate.setPublisher(publisher);

        gameToUpdate.setPegiRating("PEGI 18+");
        gameToUpdate.setMultiplayer(false);

        Game updatedGame = repository.save(gameToUpdate);

        assertNotNull(updatedGame);
        assertEquals("Elden Ring DLC Edition", updatedGame.getTitle());
        assertEquals("PEGI 18+", updatedGame.getPegiRating());
        assertFalse(updatedGame.isMultiplayer());
    }

    @Test
    @Order(3)
    void shouldFailWhenSavingGameWithInvalidDeveloperOrPublisher() {
        DeveloperImpl invalidDeveloper = new DeveloperImpl();
        invalidDeveloper.setId(999);

        PublisherImpl invalidPublisher = new PublisherImpl();
        invalidPublisher.setId(999);

        Game game = new GameImpl();
        game.setTitle("Fake Game");
        game.setReleaseDate(LocalDate.now());
        game.setDeveloper(invalidDeveloper);
        game.setPublisher(invalidPublisher);
        game.setPegiRating("PEGI 18");
        game.setMultiplayer(false);

        assertThrows(CrudException.class, () -> repository.save(game));
    }

    @Test
    @Order(4)
    void shouldDeleteGameUsingEntity() {
        GameImpl game = new GameImpl();
        game.setTitle("The Legend of Zelda: Breath of the Wild");
        game.setReleaseDate(LocalDate.of(2017, 3, 3));

        DeveloperImpl developer = new DeveloperImpl();
        developer.setId(3);

        PublisherImpl publisher = new PublisherImpl();
        publisher.setId(3);

        game.setDeveloper(developer);
        game.setPublisher(publisher);
        game.setPegiRating("PEGI 12");
        game.setMultiplayer(false);

        repository.save(game);
        repository.delete(game);

        assertFalse(repository.existsById(game.getId()));
    }

    @Test
    @Order(5)
    void shouldCountGamesCorrectly() {
        long initialCount = repository.count();

        GameImpl game = new GameImpl();
        game.setTitle("Uncharted 4");
        game.setReleaseDate(LocalDate.of(2016, 5, 10));

        DeveloperImpl developer = new DeveloperImpl();
        developer.setId(1);

        PublisherImpl publisher = new PublisherImpl();
        publisher.setId(1);

        game.setDeveloper(developer);
        game.setPublisher(publisher);
        game.setPegiRating("PEGI 16");
        game.setMultiplayer(false);

        repository.save(game);

        assertEquals(initialCount + 1, repository.count());
    }

    @Test
    @Order(6)
    void shouldReturnTrueWhenGameExists() {
        assertTrue(repository.existsById(1));
    }

    @Test
    @Order(7)
    void shouldReturnFalseWhenGameDoesNotExist() {
        assertFalse(repository.existsById(999));
    }

    @Test
    @Order(8)
    void shouldThrowIllegalArgumentExceptionOnNullTitle() {
        Game game = new GameImpl();
        game.setTitle(null);
        game.setReleaseDate(LocalDate.now());

        DeveloperImpl developer = new DeveloperImpl();
        developer.setId(1);

        PublisherImpl publisher = new PublisherImpl();
        publisher.setId(1);

        game.setDeveloper(developer);
        game.setPublisher(publisher);

        assertThrows(IllegalArgumentException.class, () -> repository.save(game));
    }

    @Test
    @Order(9)
    void shouldThrowCrudExceptionWhenInvalidOperation() {
        DataSource failingDataSource = new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                throw new SQLException("Simulated database failure");
            }
        };

        JdbcGameRepository failingRepository = new JdbcGameRepository(failingDataSource);
        assertThrows(CrudException.class, () -> failingRepository.findById(1));
    }

    @Test
    @Order(10)
    void shouldFindAllGames() {
        var games = repository.findAll();
        assertNotNull(games);
        assertTrue(games.size() >= 2);
        assertTrue(games.stream().anyMatch(g -> "The Last of Us Part II".equals(g.getTitle())));
        assertTrue(games.stream().anyMatch(g -> "Elden Ring".equals(g.getTitle())));
    }
}