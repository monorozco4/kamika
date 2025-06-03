package cat.uvic.teknos.dam.kamika.model;

import java.util.List;
import java.util.Optional;

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
     * Retrieve all developers.
     *
     * @return a list of all developers
     */
    List<Developer> findAll();

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
     * Find developers by name (case-insensitive, partial match).
     *
     * @param name the name to search for
     * @return a list of developers matching the name
     */
    List<Developer> findByNameContainingIgnoreCase(String name);

    /**
     * Find developers by country (case-insensitive).
     *
     * @param country the country name
     * @return a list of developers from the specified country
     */
    List<Developer> findByCountryIgnoreCase(String country);

    /**
     * Find developers founded in a specific year.
     *
     * @param year the foundation year
     * @return a list of developers founded in the specified year
     */
    List<Developer> findByFoundationYear(Integer year);

    /**
     * Find developers founded after a specific year.
     *
     * @param year the foundation year
     * @return a list of developers founded after the specified year
     */
    List<Developer> findByFoundationYearGreaterThan(Integer year);

    /**
     * Find developers founded before a specific year.
     *
     * @param year the foundation year
     * @return a list of developers founded before the specified year
     */
    List<Developer> findByFoundationYearLessThan(Integer year);

    /**
     * Find developers founded between two years.
     *
     * @param startYear the start year
     * @param endYear the end year
     * @return a list of developers founded between the specified years
     */
    List<Developer> findByFoundationYearBetween(Integer startYear, Integer endYear);

    /**
     * Find developers by country and foundation year.
     *
     * @param country the country name
     * @param year the foundation year
     * @return a list of developers from the specified country founded in the specified year
     */
    List<Developer> findByCountryIgnoreCaseAndFoundationYear(String country, Integer year);

    /**
     * Find developers by country and foundation year range.
     *
     * @param country the country name
     * @param startYear the start year
     * @param endYear the end year
     * @return a list of developers from the specified country founded within the year range
     */
    List<Developer> findByCountryIgnoreCaseAndFoundationYearBetween(String country, Integer startYear, Integer endYear);

    /**
     * Find developers sorted by foundation year (ascending).
     *
     * @return a list of developers sorted by foundation year (oldest first)
     */
    List<Developer> findAllByOrderByFoundationYearAsc();

    /**
     * Find developers sorted by foundation year (descending).
     *
     * @return a list of developers sorted by foundation year (newest first)
     */
    List<Developer> findAllByOrderByFoundationYearDesc();

    /**
     * Find developers sorted by name (alphabetically).
     *
     * @return a list of developers sorted by name
     */
    List<Developer> findAllByOrderByNameAsc();

    /**
     * Find developers by country sorted by foundation year.
     *
     * @param country the country name
     * @return a list of developers from the specified country sorted by foundation year
     */
    List<Developer> findByCountryIgnoreCaseOrderByFoundationYearAsc(String country);

    /**
     * Count developers by country.
     *
     * @param country the country name
     * @return the number of developers from the specified country
     */
    long countByCountryIgnoreCase(String country);
}