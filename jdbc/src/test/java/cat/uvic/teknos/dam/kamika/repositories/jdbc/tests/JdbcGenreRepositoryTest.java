package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.repositories.Genre;
import cat.uvic.teknos.dam.kamika.repositories.impl.GenreImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGenreRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcGenreRepositoryTest {

    private static DataSource dataSource;
    private static JdbcGenreRepository repository;

    private static final String TEST_NAME = "Fantasy";
    private static final String UPDATED_NAME = "Adventure";
    private static final String TEST_DESCRIPTION = "Epic fantasy games.";
    private static final String UPDATED_DESCRIPTION = "Exploration-based gameplay.";

    private static int genreId;

    @Test
    @Order(1)
    void shouldSaveNewGenre() {
        Genre genre = new GenreImpl();
        genre.setName(TEST_NAME);
        genre.setDescription(TEST_DESCRIPTION);

        Genre saved = repository.save(genre);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(TEST_NAME, saved.getName());
        assertEquals(TEST_DESCRIPTION, saved.getDescription());

        genreId = saved.getId();
    }

    @Test
    @Order(2)
    void shouldFindGenreById() {
        Optional<Genre> found = repository.findById(genreId);
        assertTrue(found.isPresent(), "Genre should be present");

        Genre genre = found.get();
        assertEquals(genreId, genre.getId());
        assertEquals(TEST_NAME, genre.getName());
        assertEquals(TEST_DESCRIPTION, genre.getDescription());
    }

    @Test
    @Order(3)
    void shouldUpdateGenre() {
        Genre genreToUpdate = new GenreImpl();
        genreToUpdate.setId(genreId);
        genreToUpdate.setName(UPDATED_NAME);
        genreToUpdate.setDescription(UPDATED_DESCRIPTION);

        repository.save(genreToUpdate);

        Optional<Genre> updated = repository.findById(genreId);
        assertTrue(updated.isPresent(), "Updated genre should be present");

        Genre genre = updated.get();
        assertEquals(genreId, genre.getId());
        assertEquals(UPDATED_NAME, genre.getName());
        assertEquals(UPDATED_DESCRIPTION, genre.getDescription());
    }

    @Test
    @Order(4)
    void shouldDeleteGenre() {
        Genre genreToDelete = new GenreImpl();
        genreToDelete.setName("RPG");
        genreToDelete.setDescription("Role-playing games.");
        Genre saved = repository.save(genreToDelete);

        repository.delete(saved);

        Optional<Genre> deleted = repository.findById(saved.getId());
        assertFalse(deleted.isPresent(), "Genre should not be found after deletion");
    }

    @Test
    @Order(5)
    void shouldDeleteGenreById() {
        Genre genreToDelete = new GenreImpl();
        genreToDelete.setName("Horror");
        genreToDelete.setDescription("Scary games.");
        Genre saved = repository.save(genreToDelete);

        boolean result = repository.deleteById(saved.getId());

        assertTrue(result, "Deletion should return true");
        Optional<Genre> deleted = repository.findById(saved.getId());
        assertFalse(deleted.isPresent(), "Genre should not be found after deletion by ID");
    }

    @Test
    @Order(6)
    void shouldCountGenres() {
        long countBefore = repository.count();

        Genre genre1 = new GenreImpl();
        genre1.setName("Action");
        genre1.setDescription("Fast-paced action games.");
        repository.save(genre1);

        Genre genre2 = new GenreImpl();
        genre2.setName("Puzzle");
        genre2.setDescription("Brain-teasing games.");
        repository.save(genre2);

        long countAfter = repository.count();
        assertEquals(countBefore + 2, countAfter, "Count should have increased by 2");
    }

    @Test
    @Order(7)
    void shouldCheckIfExistsById() {
        Genre genre = new GenreImpl();
        genre.setName("Sports");
        genre.setDescription("Real-world sports simulation.");
        Genre saved = repository.save(genre);

        assertTrue(repository.existsById(saved.getId()), "existsById should return true for existing genre");
    }

    @Test
    @Order(8)
    void shouldReturnFalseWhenGenreDoesNotExist() {
        assertFalse(repository.existsById(999), "existsById should return false for non-existing genre");
    }

    @Test
    @Order(9)
    void shouldFindGenreByNameIgnoreCase() {
        Genre genre = new GenreImpl();
        genre.setName("Shooter");
        genre.setDescription("Action shooting gameplay.");
        repository.save(genre);

        Optional<Genre> found = repository.findByNameIgnoreCase("SHOOTER");
        assertTrue(found.isPresent(), "Genre with name 'SHOOTER' should be found");
        assertEquals("Shooter", found.get().getName(), "Name should match exactly");
    }

    @Test
    @Order(10)
    void shouldNotFindGenreWithInvalidName() {
        Optional<Genre> found = repository.findByNameIgnoreCase("NonExistentGenre");
        assertFalse(found.isPresent(), "No genre should be found with invalid name");
    }

    @Test
    @Order(11)
    void shouldCountGamesPerGenre() {
        // Este test asume que existe la tabla BELONGSTO y tiene datos
        Map<Integer, Long> gameCounts = repository.countGamesPerGenre();
        assertNotNull(gameCounts, "Game counts map should not be null");
    }

    @Test
    @Order(12)
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> repository.findById(-1), "Invalid ID should throw CrudException");
    }
}