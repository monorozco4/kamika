/**
 * Repository interface for managing Developer entities.
 * Follows the repository pattern to abstract the data access operations.
 * <p>
 * Related tables:
 * - Game: A developer can develop multiple games (one-to-many).
 * - Publisher: Developers can also act as publishers in self-publishing scenarios (one-to-zero-or-one).
 * </p>
 */

package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Developer;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing Developer entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface DeveloperRepository {

    /**
     * Find a developer by its ID.
     *
     * @param id the developer ID
     * @return an Optional containing the developer if found, empty otherwise
     */
    Optional<Developer> findById(int id);

    /**
     * Save a new developer or update an existing one.
     *
     * @param developer the developer to save
     * @return the saved developer
     */
    Developer save(Developer developer);

    /**
     * Delete a developer.
     *
     * @param developer the developer to delete
     */
    void delete(Developer developer);

    /**
     * Delete a developer by its ID.
     *
     * @param id the ID of the developer to delete
     * @return true if the developer was deleted, false if not found
     */
    boolean deleteById(int id);

    /**
     * Count the total number of developers.
     *
     * @return the total number of developers
     */
    long count();

    /**
     * Check if a developer with the given ID exists.
     *
     * @param id the developer ID
     * @return true if the developer exists, false otherwise
     */
    boolean existsById(int id);

    // Custom methods

    /**
     * Count developers by country.
     *
     * @param country the country name
     * @return the number of developers from the specified country
     */
    long countByCountryIgnoreCase(String country);

    /**
     * Returns all developers.
     *
     * @return a set of all developers
     */
    Set<Developer> findAll();
}