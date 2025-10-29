/**
 * Repository interface for managing Publisher entities.
 * Follows the repository pattern to abstract the data access operations.
 * <p>
 * Related tables:
 * - Game: A publisher can publish multiple games (one-to-many).
 * - Developer: Publishers may also be developers in some cases (self-publishing).
 * </p>
 */

package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Publisher;

import java.util.Optional;
import java.util.Set;

public interface PublisherRepository {

    /**
     * Find a publisher by its ID.
     *
     * @param id the publisher ID
     * @return an Optional containing the publisher if found, empty otherwise
     */
    Optional<Publisher> findById(int id);

    /**
     * Save a new publisher or update an existing one.
     *
     * @param publisher the publisher to save
     * @return the saved publisher
     */
    Publisher save(Publisher publisher);

    /**
     * Delete a publisher.
     *
     * @param publisher the publisher to delete
     */
    void delete(Publisher publisher);

    /**
     * Delete a publisher by its ID.
     *
     * @param id the ID of the publisher to delete
     * @return true if the publisher was deleted, false if not found
     */
    boolean deleteById(int id);

    /**
     * Count the total number of publishers.
     *
     * @return the total number of publishers
     */
    long count();

    /**
     * Check if a publisher with the given ID exists.
     *
     * @param id the publisher ID
     * @return true if the publisher exists, false otherwise
     */
    boolean existsById(int id);

    long countByCountryIgnoreCase(String country);

    Optional<Publisher> findByName(String name);

    Set<Publisher> findAll();
}