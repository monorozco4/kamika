package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Genre entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface GenreRepository {

    /**
     * Find a genre by its ID.
     *
     * @param id the genre ID
     * @return an Optional containing the genre if found, empty otherwise
     */
    Optional<Genre> findById(int id);

    /**
     * Retrieve all genres.
     *
     * @return a list of all genres
     */
    List<Genre> findAll();

    /**
     * Save a new genre or update an existing one.
     *
     * @param genre the genre to save
     * @return the saved genre
     */
    Genre save(Genre genre);

    /**
     * Delete a genre.
     *
     * @param genre the genre to delete
     */
    void delete(Genre genre);

    /**
     * Delete a genre by its ID.
     *
     * @param id the ID of the genre to delete
     * @return true if the genre was deleted, false if not found
     */
    boolean deleteById(int id);

    /**
     * Count the total number of genres.
     *
     * @return the total number of genres
     */
    long count();

    /**
     * Check if a genre with the given ID exists.
     *
     * @param id the genre ID
     * @return true if the genre exists, false otherwise
     */
    boolean existsById(int id);

    // Custom methods

    /**
     * Find genres by name (exact match, case-insensitive).
     *
     * @param name the genre name
     * @return an Optional containing the genre if found, empty otherwise
     */
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Find genres by name (partial match, case-insensitive).
     *
     * @param name the name fragment to search for
     * @return a list of genres containing the specified name fragment
     */
    List<Genre> findByNameContainingIgnoreCase(String name);

    /**
     * Find genres by description (partial match, case-insensitive).
     *
     * @param description the description fragment to search for
     * @return a list of genres containing the specified description fragment
     */
    List<Genre> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Find genres sorted by name (alphabetically).
     *
     * @return a list of genres sorted by name
     */
    List<Genre> findAllByOrderByNameAsc();

    /**
     * Find genres that have associated games.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a list of genres that have at least one associated game
     */
    List<Genre> findGenresWithGames();

    /**
     * Find genres that don't have any associated games.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a list of genres that don't have any associated games
     */
    List<Genre> findGenresWithoutGames();

    /**
     * Find genres with a specified number of associated games or more.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @param count the minimum number of associated games
     * @return a list of genres with at least the specified number of associated games
     */
    List<Genre> findGenresWithGameCountGreaterThanEqual(int count);

    /**
     * Count the number of games associated with each genre.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a map with genre IDs as keys and the count of associated games as values
     */
    java.util.Map<Integer, Long> countGamesPerGenre();
}