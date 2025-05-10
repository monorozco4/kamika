package cat.uvic.teknos.dam.kamika;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Publisher entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface PublisherRepository {

    /**
     * Find a publisher by its ID.
     *
     * @param id the publisher ID
     * @return an Optional containing the publisher if found, empty otherwise
     */
    Optional<Publisher> findById(int id);

    /**
     * Retrieve all publishers.
     *
     * @return a list of all publishers
     */
    List<Publisher> findAll();

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

    // Custom methods

    /**
     * Find publishers by name (case-insensitive, partial match).
     *
     * @param name the name to search for
     * @return a list of publishers matching the name
     */
    List<Publisher> findByNameContainingIgnoreCase(String name);

    /**
     * Find publishers by country (case-insensitive).
     *
     * @param country the country name
     * @return a list of publishers from the specified country
     */
    List<Publisher> findByCountryIgnoreCase(String country);

    /**
     * Find publishers by developer ID.
     *
     * @param developerId the developer ID
     * @return a list of publishers associated with the specified developer
     */
    List<Publisher> findByDeveloperId(int developerId);

    /**
     * Find publishers by developer name (case-insensitive, partial match).
     *
     * @param developerName the developer name to search for
     * @return a list of publishers associated with developers matching the name
     */
    List<Publisher> findByDeveloperNameContainingIgnoreCase(String developerName);

    /**
     * Find publishers that are also developers (self-publishing).
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a list of publishers that are also developers
     */
    List<Publisher> findSelfPublishingCompanies();

    /**
     * Find publishers by country and developer country.
     *
     * @param publisherCountry the publisher country
     * @param developerCountry the developer country
     * @return a list of publishers from the specified country associated with developers from the specified country
     */
    List<Publisher> findByCountryIgnoreCaseAndDeveloperCountryIgnoreCase(String publisherCountry, String developerCountry);

    /**
     * Find publishers sorted by name (alphabetically).
     *
     * @return a list of publishers sorted by name
     */
    List<Publisher> findAllByOrderByNameAsc();

    /**
     * Find publishers by country sorted by name.
     *
     * @param country the country name
     * @return a list of publishers from the specified country sorted by name
     */
    List<Publisher> findByCountryIgnoreCaseOrderByNameAsc(String country);

    /**
     * Count publishers by country.
     *
     * @param country the country name
     * @return the number of publishers from the specified country
     */
    long countByCountryIgnoreCase(String country);

    /**
     * Find publishers that have published games.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @return a list of publishers that have published at least one game
     */
    List<Publisher> findPublishersWithGames();

    /**
     * Find publishers with a specified number of published games or more.
     * This might need a custom implementation depending on the ORM/framework used.
     *
     * @param count the minimum number of published games
     * @return a list of publishers with at least the specified number of published games
     */
    List<Publisher> findPublishersWithGameCountGreaterThanEqual(int count);
}
