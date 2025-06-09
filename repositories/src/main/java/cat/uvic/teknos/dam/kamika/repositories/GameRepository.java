package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Game;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing Game entities.
 * Follows the repository pattern to abstract the data access operations.
 * <p>
 * Related tables:
 * - Genre: Connected through GameGenre (many-to-many).
 * - Developer: A game belongs to one developer (many-to-one).
 * - Publisher: A game is published by one publisher (many-to-one).
 * - GameEdition: A game has multiple editions (one-to-many).
 * </p>
 */
public interface GameRepository {

    /**
     * Find a game by its ID.
     */
    Optional<Game> findById(int id);

    /**
     * Save a new game or update an existing one.
     */
    Game save(Game game) throws SQLException;

    /**
     * Delete a game.
     */
    void delete(Game game);

    /**
     * Delete a game by its ID.
     */
    boolean deleteById(int id);

    /**
     * Count the total number of games.
     */
    long count();

    /**
     * Check if a game with the given ID exists.
     */
    boolean existsById(int id);

    /**
     * Find all games.
     *
     * @return a set containing all games
     */
    Set<Game> findAll();
}