/**
 * Repository interface for managing Console entities.
 * Follows the repository pattern to abstract the data access operations.
 * <p>
 * Related tables:
 * - GameConsole: Many-to-many relationship between Console and Game.
 * - Developer: Consoles are made by developers/manufacturers (many-to-one).
 * </p>
 */

package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Console;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing Console entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface ConsoleRepository {

    /**
     * Find a console by its ID.
     *
     * @param id the console ID
     * @return an Optional containing the console if found, empty otherwise
     */
    Optional<Console> findById(int id);

    /**
     * Save a new console or update an existing one.
     *
     * @param console the console to save
     * @return the saved console
     */
    Console save(Console console);

    /**
     * Delete a console.
     *
     * @param console the console to delete
     */
    void delete(Console console);

    /**
     * Delete a console by its ID.
     *
     * @param id the ID of the console to delete
     * @return true if the console was deleted, false if not found
     */
    boolean deleteById(int id);

    /**
     * Count the total number of consoles.
     *
     * @return the total number of consoles
     */
    long count();

    /**
     * Check if a console with the given ID exists.
     *
     * @param id the console ID
     * @return true if the console exists, false otherwise
     */
    boolean existsById(int id);

    /**
     * Returns all consoles.
     *
     * @return a set of all consoles
     */
    Set<Console> findAll();
}
