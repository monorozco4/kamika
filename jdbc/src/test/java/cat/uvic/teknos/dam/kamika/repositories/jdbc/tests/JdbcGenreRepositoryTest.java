package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.model.Genre;
import cat.uvic.teknos.dam.kamika.model.impl.GenreImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGenreRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;
import java.util.Map;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcGenreRepositoryTest {

    private JdbcGenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DELETE FROM GAME_EDITION");
            stmt.execute("DELETE FROM GAME_CONSOLE");
            stmt.execute("DELETE FROM GAME");
            stmt.execute("DELETE FROM GENRE");

            stmt.execute("""
        CREATE TABLE IF NOT EXISTS GENRE (
            GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
            NAME VARCHAR(50) NOT NULL,
            DESCRIPTION VARCHAR(100)
        )
    """);

            stmt.execute("""
        CREATE TABLE IF NOT EXISTS GAME (
            GAME_ID INT PRIMARY KEY AUTO_INCREMENT,
            TITLE VARCHAR(100) NOT NULL,
            RELEASE_DATE DATE,
            DEVELOPER_ID INT NOT NULL,
            PUBLISHER_ID INT NOT NULL,
            GENRE_ID INT,
            PEGI_RATING VARCHAR(10),
            IS_MULTIPLAYER TINYINT
        )
    """);

        } catch (Exception e) {
            fail("Error cleaning up database before test", e);
        }

        genreRepository = new JdbcGenreRepository(dataSource);
    }

    @Test
    void shouldSaveAndFindGenreById() {
        Genre genre = new GenreImpl();
        genre.setName("Action");
        genre.setDescription("Fast-paced games");

        Genre saved = genreRepository.save(genre);
        int id = saved.getId();

        Optional<Genre> found = genreRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Action", found.get().getName());
        assertEquals("Fast-paced games", found.get().getDescription());
    }

    @Test
    void shouldUpdateGenreWhenIdExists() {
        Genre genre = new GenreImpl();
        genre.setName("RPG");
        genre.setDescription("Role-playing game");

        Genre saved = genreRepository.save(genre);
        int id = saved.getId();

        Genre toUpdate = new GenreImpl();
        toUpdate.setId(id);
        toUpdate.setName("JRPG");
        toUpdate.setDescription("Japanese role-playing game");

        Genre updated = genreRepository.save(toUpdate);

        Optional<Genre> found = genreRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("JRPG", found.get().getName());
        assertEquals("Japanese role-playing game", found.get().getDescription());
    }

    @Test
    void shouldDeleteGenre() {
        Genre genre = new GenreImpl();
        genre.setName("Puzzle");
        genre.setDescription("Brain-teasing games");

        Genre saved = genreRepository.save(genre);
        int id = saved.getId();

        genreRepository.delete(saved);

        Optional<Genre> found = genreRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldDeleteGenreById() {
        Genre genre = new GenreImpl();
        genre.setName("Sports");
        genre.setDescription("Games based on sports");

        Genre saved = genreRepository.save(genre);
        int id = saved.getId();

        boolean deleted = genreRepository.deleteById(id);
        assertTrue(deleted);

        Optional<Genre> found = genreRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCountGenres() {
        Genre genre1 = new GenreImpl();
        genre1.setName("Platformer");
        genre1.setDescription("Jumping between platforms");
        genreRepository.save(genre1);

        Genre genre2 = new GenreImpl();
        genre2.setName("FPS");
        genre2.setDescription("First-person shooter");
        genreRepository.save(genre2);

        long count = genreRepository.count();
        assertEquals(2, count);
    }

    @Test
    void shouldReturnTrueWhenGenreExists() {
        Genre genre = new GenreImpl();
        genre.setName("Fighting");
        genre.setDescription("Combat-based gameplay");
        Genre saved = genreRepository.save(genre);

        assertTrue(genreRepository.existsById(saved.getId()));
    }

    @Test
    void shouldReturnFalseWhenGenreDoesNotExist() {
        assertFalse(genreRepository.existsById(999));
    }

    @Test
    void shouldFindGenreByNameIgnoreCase() {
        Genre genre = new GenreImpl();
        genre.setName("Strategy");
        genre.setDescription("Tactical decision-making");
        genreRepository.save(genre);

        Optional<Genre> found = genreRepository.findByNameIgnoreCase("stratEGY");
        assertTrue(found.isPresent());
        assertEquals("Strategy", found.get().getName());
    }

    @Test
    void shouldCountGamesPerGenre() {
        Map<Integer, Long> counts = genreRepository.countGamesPerGenre();
        assertNotNull(counts);
    }

    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> genreRepository.findById(-1));
    }
}