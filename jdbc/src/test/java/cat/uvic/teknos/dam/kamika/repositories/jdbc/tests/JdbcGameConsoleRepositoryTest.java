package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.repositories.GameConsole;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGameConsoleRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JdbcGameConsoleRepository} class.
 *
 * <p>This test class verifies the CRUD operations for game-console release relationships
 * using an in-memory H2 database. The RELEASEDON table represents the many-to-many
 * relationship between games and consoles with additional release information.</p>
 *
 * <p>Test environment setup includes:</p>
 * <ul>
 *   <li>Creating the RELEASEDON table if it doesn't exist</li>
 *   <li>Clearing all existing data before each test</li>
 *   <li>Initializing a fresh {@link JdbcGameConsoleRepository} instance</li>
 * </ul>
 *
 * @author Your Name
 * @version 1.0
 * @see JdbcGameConsoleRepository
 * @see GameConsole
 * @see GameConsoleImpl
 */
@ExtendWith(LoadDatabaseExtension.class)
class JdbcGameConsoleRepositoryTest {

    /** The repository instance under test */
    private JdbcGameConsoleRepository repository;

    /**
     * Tests successful creation and retrieval of a game-console relationship.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>A relationship can be saved to the repository</li>
     *   <li>The relationship can be retrieved using its composite key</li>
     *   <li>All attributes are stored and retrieved correctly</li>
     * </ul>
     */
    @Test
    void shouldSaveAndFindGameConsoleByCompositeKey() {
        GameConsole relation = new GameConsoleImpl();
        relation.setGameId(1);
        relation.setConsoleId(100);
        relation.setReleaseDate(LocalDate.of(1985, 10, 15));
        relation.setExclusive(true);
        relation.setResolution("720x480");

        repository.save(relation);

        Optional<GameConsole> found = repository.findById(1, 100);
        assertTrue(found.isPresent());
        assertEquals(LocalDate.of(1985, 10, 15), found.get().getReleaseDate());
        assertTrue(found.get().isExclusive());
        assertEquals("720x480", found.get().getResolution());
    }

    /**
     * Tests updating an existing game-console relationship.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>Changes to an existing relationship are persisted</li>
     *   <li>All updated attributes are correctly stored</li>
     *   <li>The composite key remains unchanged</li>
     * </ul>
     */
    @Test
    void shouldUpdateGameConsoleWhenExists() {
        GameConsole relation = new GameConsoleImpl();
        relation.setGameId(2);
        relation.setConsoleId(200);
        relation.setReleaseDate(LocalDate.now());
        relation.setExclusive(false);
        relation.setResolution("640x480");

        repository.save(relation);

        GameConsole updated = new GameConsoleImpl();
        updated.setGameId(2);
        updated.setConsoleId(200);
        updated.setReleaseDate(LocalDate.of(2000, 1, 1));
        updated.setExclusive(true);
        updated.setResolution("1080p");

        repository.save(updated);

        Optional<GameConsole> result = repository.findById(2, 200);
        assertTrue(result.isPresent());
        assertEquals(LocalDate.of(2000, 1, 1), result.get().getReleaseDate());
        assertTrue(result.get().isExclusive());
        assertEquals("1080p", result.get().getResolution());
    }

    /**
     * Tests deletion of a game-console relationship by entity object.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The relationship is successfully removed from the repository</li>
     *   <li>Subsequent existence checks return false</li>
     * </ul>
     */
    @Test
    void shouldDeleteGameConsole() {
        GameConsole relation = new GameConsoleImpl();
        relation.setGameId(3);
        relation.setConsoleId(300);
        relation.setReleaseDate(LocalDate.now());
        relation.setExclusive(false);
        relation.setResolution("480i");

        repository.save(relation);

        repository.delete(relation);

        assertFalse(repository.existsById(3, 300));
    }

    /**
     * Tests deletion of a game-console relationship by composite key.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The delete operation returns true when successful</li>
     *   <li>The relationship is no longer retrievable after deletion</li>
     * </ul>
     */
    @Test
    void shouldDeleteGameConsoleById() {
        GameConsole relation = new GameConsoleImpl();
        relation.setGameId(4);
        relation.setConsoleId(400);
        relation.setReleaseDate(LocalDate.now());
        relation.setExclusive(false);
        relation.setResolution("480i");

        repository.save(relation);

        boolean deleted = repository.deleteById(4, 400);
        assertTrue(deleted);

        assertFalse(repository.existsById(4, 400));
    }

    /**
     * Tests counting of all game-console relationships in the repository.
     *
     * <p>Verifies that the count matches the number of saved relationships.
     */
    @Test
    void shouldCountRelations() {
        GameConsole relation1 = new GameConsoleImpl();
        relation1.setGameId(5);
        relation1.setConsoleId(500);
        relation1.setReleaseDate(LocalDate.now());
        relation1.setExclusive(false);
        relation1.setResolution("720p");
        repository.save(relation1);

        GameConsole relation2 = new GameConsoleImpl();
        relation2.setGameId(6);
        relation2.setConsoleId(600);
        relation2.setReleaseDate(LocalDate.now());
        relation2.setExclusive(true);
        relation2.setResolution("1080i");
        repository.save(relation2);

        long count = repository.count();
        assertEquals(2, count);
    }

    /**
     * Tests existence check for a game-console relationship.
     *
     * <p>Verifies that existsById returns true for an existing relationship.
     */
    @Test
    void shouldReturnTrueIfExists() {
        GameConsole relation = new GameConsoleImpl();
        relation.setGameId(7);
        relation.setConsoleId(700);
        relation.setReleaseDate(LocalDate.now());
        relation.setExclusive(false);
        relation.setResolution("480i");

        repository.save(relation);

        assertTrue(repository.existsById(7, 700));
    }

    /**
     * Tests non-existence check for game-console relationships.
     *
     * <p>Verifies that existsById returns false for non-existent relationships.
     */
    @Test
    void shouldReturnFalseIfNotExists() {
        assertFalse(repository.existsById(999, 999));
    }

    /**
     * Tests exception handling for invalid operations.
     *
     * <p>Verifies that attempting to find a relationship with invalid IDs
     * throws a {@link CrudException}.
     */
    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> repository.findById(-1, 0));
    }
}