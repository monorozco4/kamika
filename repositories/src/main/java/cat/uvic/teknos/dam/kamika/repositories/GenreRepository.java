package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Genre;

import java.util.Optional;
import java.util.Set;

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

    /**
     * Find genres by name (exact match, case-insensitive).
     *
     * @param name the genre name
     * @return an Optional containing the genre if found, empty otherwise
     */
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Count the number of games associated with each genre.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a map with genre IDs as keys and the count of associated games as values
     */
    java.util.Map<Integer, Long> countGamesPerGenre();

    Optional<Genre> findByName(String name);

    Set<Genre> findAll();
}