/**
 * Repository interface for managing GameEdition entities.
 * Follows the repository pattern to abstract the data access operations.
 * <p>
 * Related tables:
 * - Game: Each edition belongs to one game (many-to-one).
 * - Publisher: An edition may be published by a publisher (many-to-one).
 * </p>
 */

package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.GameEdition;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing GameEdition entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface GameEditionRepository {

    /**
     * Find a game edition by game ID and edition name.
     *
     * @param gameId      the game ID
     * @param editionName the edition name
     * @return an Optional containing the game edition if found, empty otherwise
     */
    Optional<GameEdition> findByGameIdAndEditionName(int gameId, String editionName);

    /**
     * Save a new game edition or update an existing one.
     *
     * @param gameEdition the game edition to save
     * @return the saved game edition
     */
    GameEdition save(GameEdition gameEdition);

    /**
     * Delete a game edition.
     *
     * @param gameEdition the game edition to delete
     */
    void delete(GameEdition gameEdition);

    /**
     * Delete a game edition by game ID and edition name.
     *
     * @param gameId      the game ID
     * @param editionName the edition name
     */
    void deleteByGameIdAndEditionName(int gameId, String editionName);

    /**
     * Count the total number of game editions.
     *
     * @return the total number of game editions
     */
    long count();

    /**
     * Check if a game edition with the given game ID and edition name exists.
     *
     * @param gameId      the game ID
     * @param editionName the edition name
     * @return true if the game edition exists, false otherwise
     */
    boolean existsByGameIdAndEditionName(int gameId, String editionName);

    Optional<GameEdition> findByEditionName(String editionName);

    /**
     * Returns all game editions.
     *
     * @return a set of all game editions
     */
    Set<GameEdition> findAll();

    /**
     * Find a game edition by its ID.
     *
     * @param id the ID of the game edition
     * @return an Optional containing the game edition if found, empty otherwise
     */
    Optional<GameEdition> findById(int id);
}