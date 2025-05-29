package cat.uvic.teknos.dam.kamika;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing Game entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface GameRepository {

    /**
     * Find a game by its ID.
     *
     * @param id the game ID
     * @return an Optional containing the game if found, empty otherwise
     */
    Optional<Game> findById(int id);



    /**
     * Save a new game or update an existing one.
     *
     * @param game the game to save
     * @return the saved game
     */
    Game save(Game game);

    /**
     * Delete a game.
     *
     * @param game the game to delete
     */
    void delete(Game game);

    /**
     * Delete a game by its ID.
     *
     * @param id the ID of the game to delete
     * @return true if the game was deleted, false if not found
     */
    boolean deleteById(int id);

    /**
     * Count the total number of games.
     *
     * @return the total number of games
     */
    long count();

    /**
     * Check if a game with the given ID exists.
     *
     * @param id the game ID
     * @return true if the game exists, false otherwise
     */
    boolean existsById(int id);
}