package cat.uvic.teknos.dam.kamika.model;

import java.util.List;
import java.util.Optional;

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
     * Retrieve all consoles.
     *
     * @return a list of all consoles
     */
    List<Console> findAll();

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

    // Custom methods

    /**
     * Find consoles by name (case-insensitive, partial match).
     *
     * @param name the name to search for
     * @return a list of consoles matching the name
     */
    List<Console> findByNameContainingIgnoreCase(String name);

    /**
     * Find consoles by manufacturer (case-insensitive).
     *
     * @param manufacturer the manufacturer name
     * @return a list of consoles by the specified manufacturer
     */
    List<Console> findByManufacturerIgnoreCase(String manufacturer);

    /**
     * Find consoles released in a specific year.
     *
     * @param year the release year
     * @return a list of consoles released in the specified year
     */
    List<Console> findByReleaseYear(Integer year);

    /**
     * Find consoles released after a specific year.
     *
     * @param year the release year
     * @return a list of consoles released after the specified year
     */
    List<Console> findByReleaseYearGreaterThan(Integer year);

    /**
     * Find consoles released before a specific year.
     *
     * @param year the release year
     * @return a list of consoles released before the specified year
     */
    List<Console> findByReleaseYearLessThan(Integer year);

    /**
     * Find consoles released between two years.
     *
     * @param startYear the start year
     * @param endYear the end year
     * @return a list of consoles released between the specified years
     */
    List<Console> findByReleaseYearBetween(Integer startYear, Integer endYear);

    /**
     * Find consoles by manufacturer and release year.
     *
     * @param manufacturer the manufacturer name
     * @param year the release year
     * @return a list of consoles by the specified manufacturer and release year
     */
    List<Console> findByManufacturerIgnoreCaseAndReleaseYear(String manufacturer, Integer year);

    /**
     * Find consoles by manufacturer and release year range.
     *
     * @param manufacturer the manufacturer name
     * @param startYear the start year
     * @param endYear the end year
     * @return a list of consoles by the specified manufacturer within the release year range
     */
    List<Console> findByManufacturerIgnoreCaseAndReleaseYearBetween(String manufacturer, Integer startYear, Integer endYear);

    /**
     * Find consoles sorted by release year (ascending).
     *
     * @return a list of consoles sorted by release year
     */
    List<Console> findAllByOrderByReleaseYearAsc();

    /**
     * Find consoles sorted by release year (descending).
     *
     * @return a list of consoles sorted by release year (newest first)
     */
    List<Console> findAllByOrderByReleaseYearDesc();

    /**
     * Find consoles by manufacturer sorted by release year (ascending).
     *
     * @param manufacturer the manufacturer name
     * @return a list of consoles by the specified manufacturer sorted by release year
     */
    List<Console> findByManufacturerIgnoreCaseOrderByReleaseYearAsc(String manufacturer);
}
