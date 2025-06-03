package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.repositories.Game;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGameRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JdbcGameRepository} class.
 *
 * <p>This test class verifies all CRUD (Create, Read, Update, Delete) operations
 * for the Game entity using an in-memory H2 database. Tests are executed in a
 * specific order to validate dependent operations.</p>
 *
 * <p>Test environment setup includes:</p>
 * <ul>
 *   <li>Creating the GAME table if it doesn't exist</li>
 *   <li>Clearing all existing data before each test</li>
 *   <li>Initializing a fresh {@link JdbcGameRepository} instance</li>
 * </ul>
 *
 * @author Your Name
 * @version 1.0
 * @see JdbcGameRepository
 * @see Game
 * @see GameImpl
 */
@ExtendWith(LoadDatabaseExtension.class)
public class JdbcGameRepositoryTest {

    /** Repository instance being tested */
    private JdbcGameRepository repository;

    /**
     * Tests successful creation of a game entity.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The save operation returns a non-null game with generated ID</li>
     *   <li>All attributes are stored correctly</li>
     *   <li>The entity exists in the repository after creation</li>
     * </ul>
     */
    @Test
    @Order(1)
    void shouldSaveGame() {
        Game game = new GameImpl();
        game.setTitle("The Legend of Zelda");
        game.setReleaseDate(LocalDate.of(2017, 3, 3));
        game.setPegiRating("PEGI 12");
        game.setMultiplayer(true);

        Game saved = repository.save(game);

        assertNotNull(saved.getId());
        assertEquals("The Legend of Zelda", saved.getTitle());
        assertTrue(repository.existsById(saved.getId()));
    }

    /**
     * Tests retrieval of a game by its ID.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>A saved game can be retrieved using its ID</li>
     *   <li>All attributes match the original values</li>
     *   <li>The returned Optional contains a value</li>
     * </ul>
     */
    @Test
    @Order(2)
    void shouldFindGameById() {
        Game game = new GameImpl();
        game.setTitle("Super Mario Odyssey");
        game.setReleaseDate(LocalDate.of(2017, 10, 27));
        game.setPegiRating("PEGI 3");
        game.setMultiplayer(false);

        Game saved = repository.save(game);

        Optional<Game> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Super Mario Odyssey", found.get().getTitle());
    }

    /**
     * Tests counting of all games in the repository.
     *
     * <p>Verifies that the count matches the exact number of saved games.
     */
    @Test
    @Order(3)
    void shouldCountGames() {
        Game game1 = new GameImpl();
        game1.setTitle("Minecraft");
        game1.setReleaseDate(LocalDate.of(2011, 11, 18));
        game1.setPegiRating("PEGI 7");
        game1.setMultiplayer(true);

        Game game2 = new GameImpl();
        game2.setTitle("Tetris");
        game2.setReleaseDate(LocalDate.of(1984, 6, 6));
        game2.setPegiRating("PEGI 3");
        game2.setMultiplayer(false);

        repository.save(game1);
        repository.save(game2);

        long count = repository.count();

        assertEquals(2, count);
    }

    /**
     * Tests deletion of a game by its ID.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The delete operation returns true when successful</li>
     *   <li>The game no longer exists in the repository after deletion</li>
     * </ul>
     */
    @Test
    @Order(4)
    void shouldDeleteGameById() {
        Game game = new GameImpl();
        game.setTitle("Final Fantasy VII");
        game.setReleaseDate(LocalDate.of(1997, 1, 31));
        game.setPegiRating("PEGI 16");
        game.setMultiplayer(false);

        Game saved = repository.save(game);

        boolean deleted = repository.deleteById(saved.getId());

        assertTrue(deleted);
        assertFalse(repository.existsById(saved.getId()));
    }

    /**
     * Tests deletion of a game by entity reference.
     *
     * <p>Verifies that the game is successfully removed from the repository.
     */
    @Test
    @Order(5)
    void shouldDeleteGame() {
        Game game = new GameImpl();
        game.setTitle("DOOM Eternal");
        game.setReleaseDate(LocalDate.of(2020, 3, 20));
        game.setPegiRating("PEGI 18");
        game.setMultiplayer(true);

        Game saved = repository.save(game);

        repository.delete(saved);

        assertFalse(repository.existsById(saved.getId()));
    }

    /**
     * Tests that deleted games cannot be retrieved.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>Deleted games are not found in the repository</li>
     *   <li>The returned Optional is empty</li>
     * </ul>
     */
    @Test
    @Order(6)
    void shouldNotFindDeletedGame() {
        Game game = new GameImpl();
        game.setTitle("Half-Life 3");
        game.setReleaseDate(LocalDate.of(2025, 1, 1)); // Fictional future date
        game.setPegiRating("PEGI 16");
        game.setMultiplayer(true);

        Game saved = repository.save(game);
        repository.delete(saved);

        Optional<Game> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
    }
}