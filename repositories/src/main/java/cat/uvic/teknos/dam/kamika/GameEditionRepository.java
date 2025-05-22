package cat.uvic.teknos.dam.kamika;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing GameEdition entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface GameEditionRepository {

    /**
     * Find a game edition by game ID and edition name.
     * Note: Assuming the composite key consists of game ID and edition name.
     *
     * @param gameId the game ID
     * @param editionName the edition name
     * @return an Optional containing the game edition if found, empty otherwise
     */
    Optional<GameEdition> findByGameIdAndEditionName(int gameId, String editionName);

    /**
     * Retrieve all game editions.
     *
     * @return a list of all game editions
     */
    List<GameEdition> findAll();

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
     * @param gameId the game ID
     * @param editionName the edition name
     * @return true if the game edition was deleted, false if not found
     */
    boolean deleteByGameIdAndEditionName(int gameId, String editionName);

    /**
     * Count the total number of game editions.
     *
     * @return the total number of game editions
     */
    long count();

    /**
     * Check if a game edition with the given game ID and edition name exists.
     *
     * @param gameId the game ID
     * @param editionName the edition name
     * @return true if the game edition exists, false otherwise
     */
    boolean existsByGameIdAndEditionName(int gameId, String editionName);

    // Custom methods

    /**
     * Find game editions by game ID.
     *
     * @param gameId the game ID
     * @return a list of editions for the specified game
     */
    List<GameEdition> findByGameId(int gameId);

    /**
     * Find game editions by edition name (case-insensitive, partial match).
     *
     * @param editionName the edition name to search for
     * @return a list of game editions matching the edition name
     */
    List<GameEdition> findByEditionNameContainingIgnoreCase(String editionName);

    /**
     * Find game editions by special content (case-insensitive, partial match).
     *
     * @param specialContent the special content to search for
     * @return a list of game editions containing the specified special content
     */
    List<GameEdition> findBySpecialContentContainingIgnoreCase(String specialContent);

    /**
     * Find game editions with price less than or equal to a specified value.
     *
     * @param price the maximum price
     * @return a list of game editions with price less than or equal to the specified value
     */
    List<GameEdition> findByPriceLessThanEqual(double price);

    /**
     * Find game editions with price greater than or equal to a specified value.
     *
     * @param price the minimum price
     * @return a list of game editions with price greater than or equal to the specified value
     */
    List<GameEdition> findByPriceGreaterThanEqual(double price);

    /**
     * Find game editions with price between two values.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return a list of game editions with price between the specified values
     */
    List<GameEdition> findByPriceBetween(double minPrice, double maxPrice);

    /**
     * Find game editions for a specific game with price less than or equal to a specified value.
     *
     * @param gameId the game ID
     * @param price the maximum price
     * @return a list of game editions for the specified game with price less than or equal to the specified value
     */
    List<GameEdition> findByGameIdAndPriceLessThanEqual(int gameId, double price);

    /**
     * Find game editions sorted by price (ascending).
     *
     * @return a list of game editions sorted by price (lowest first)
     */
    List<GameEdition> findAllByOrderByPriceAsc();

    /**
     * Find game editions sorted by price (descending).
     *
     * @return a list of game editions sorted by price (highest first)
     */
    List<GameEdition> findAllByOrderByPriceDesc();

    /**
     * Find game editions for a specific game sorted by price.
     *
     * @param gameId the game ID
     * @return a list of game editions for the specified game sorted by price
     */
    List<GameEdition> findByGameIdOrderByPriceAsc(int gameId);

    /**
     * Find the cheapest edition for each game.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a list of the cheapest edition for each game
     */
    List<GameEdition> findCheapestEditionsByGame();

    /**
     * Calculate the average price of editions for a specific game.
     *
     * @param gameId the game ID
     * @return the average price of editions for the specified game
     */
    double calculateAveragePriceByGameId(int gameId);
}
