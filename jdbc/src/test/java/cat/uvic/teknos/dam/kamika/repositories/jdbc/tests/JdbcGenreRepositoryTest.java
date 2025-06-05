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

/**
 * Unit test suite for the {@link JdbcGenreRepository} class.
 *
 * <p>This test class uses an in-memory H2 database to validate that all CRUD operations
 * on the Genre entity behave as expected.</p>
 *
 * <p>Before each test:
 * <ul>
 *   <li>A fresh table named "GENRE" is created if it does not exist.</li>
 *   <li>The table is cleared to ensure a clean state before each test.</li>
 * </ul>
 * </p>
 */
@ExtendWith(LoadDatabaseExtension.class)
class JdbcGenreRepositoryTest {

    private JdbcGenreRepository genreRepository;

    /**
     * Sets up the test environment before each test method is executed.
     *
     * <p>Creates or clears the GENRE table and initializes a new repository instance.</p>
     */
    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // Create table if it doesn't exist
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GENRE (
                    GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(50) NOT NULL,
                    DESCRIPTION VARCHAR(100)
                )
            """);

            // Clear table before each test
            stmt.execute("DELETE FROM GENRE");

        } catch (Exception e) {
            fail("Error cleaning up database before test", e);
        }

        genreRepository = new JdbcGenreRepository(dataSource);
    }

    /**
     * Tests that a newly saved genre can be retrieved by its ID.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The saved genre has a valid ID assigned.</li>
     *   <li>The genre retrieved using findById() matches the original data.</li>
     * </ul>
     * </p>
     */
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

    /**
     * Tests that updating a genre with an existing ID modifies its stored data.
     *
     * <p>Verifies that after calling save() on an updated genre object,
     * the persisted values reflect the changes.</p>
     */
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

    /**
     * Tests that a genre can be deleted using the delete() method.
     *
     * <p>Verifies that after deletion, the genre is no longer present in the database.</p>
     */
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

    /**
     * Tests that a genre can be deleted using its ID via deleteById().
     *
     * <p>Verifies that the method returns true when a genre is successfully deleted.</p>
     */
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

    /**
     * Tests that the count() method returns the correct number of genres in the database.
     */
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

    /**
     * Tests that existsById() returns true when a genre with the given ID exists.
     */
    @Test
    void shouldReturnTrueWhenGenreExists() {
        Genre genre = new GenreImpl();
        genre.setName("Fighting");
        genre.setDescription("Combat-based gameplay");
        Genre saved = genreRepository.save(genre);

        assertTrue(genreRepository.existsById(saved.getId()));
    }

    /**
     * Tests that existsById() returns false when a genre with the given ID does not exist.
     */
    @Test
    void shouldReturnFalseWhenGenreDoesNotExist() {
        assertFalse(genreRepository.existsById(999));
    }

    /**
     * Tests that findByNameIgnoreCase retrieves a genre regardless of case sensitivity.
     */
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

    /**
     * Tests that countGamesPerGenre returns a map with genre IDs and their associated game counts.
     */
    @Test
    void shouldCountGamesPerGenre() {
        Map<Integer, Long> counts = genreRepository.countGamesPerGenre();
        assertNotNull(counts);
    }

    /**
     * Tests that invalid operations throw a CrudException.
     *
     * <p>In this case, attempting to find a genre with a negative ID should result
     * in a CrudException being thrown.</p>
     */
    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> genreRepository.findById(-1));
    }
}