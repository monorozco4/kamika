package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.repositories.Developer;
import cat.uvic.teknos.dam.kamika.repositories.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcDeveloperRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JdbcDeveloperRepository} class.
 *
 * <p>This test class validates all CRUD (Create, Read, Update, Delete) operations
 * for the Developer entity using an in-memory H2 database. Each test method
 * verifies a specific aspect of the repository's functionality.</p>
 *
 * <p>The test environment is reset before each test method execution to ensure
 * test isolation:</p>
 * <ul>
 *   <li>Creates the DEVELOPER table if it doesn't exist</li>
 *   <li>Clears all existing data from the table</li>
 * </ul>
 *
 * @author Your Name
 * @version 1.0
 * @see JdbcDeveloperRepository
 * @see Developer
 * @see DeveloperImpl
 */
@ExtendWith(LoadDatabaseExtension.class)
class JdbcDeveloperRepositoryTest {

    /** The repository instance being tested */
    private JdbcDeveloperRepository developerRepository;

    /**
     * Tests that a developer can be successfully saved and retrieved by ID.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The save operation returns a non-null Developer with an assigned ID</li>
     *   <li>The findById operation returns the saved developer with correct values</li>
     * </ul>
     */
    @Test
    void shouldSaveAndFindDeveloperById() {
        Developer developer = new DeveloperImpl();
        developer.setName("Nintendo");
        developer.setCountry("Japan");
        developer.setFoundationYear(1889);

        Developer saved = developerRepository.save(developer);
        int id = saved.getId();

        Optional<Developer> found = developerRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Nintendo", found.get().getName());
        assertEquals("Japan", found.get().getCountry());
        assertEquals(1889, found.get().getFoundationYear());
    }

    /**
     * Tests that an existing developer can be updated.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>Changes to an existing developer are persisted</li>
     *   <li>The updated values can be retrieved correctly</li>
     * </ul>
     */
    @Test
    void shouldUpdateDeveloperWhenIdExists() {
        Developer developer = new DeveloperImpl();
        developer.setName("Sega");
        developer.setCountry("Japan");
        developer.setFoundationYear(1960);

        Developer saved = developerRepository.save(developer);
        int id = saved.getId();

        Developer toUpdate = new DeveloperImpl();
        toUpdate.setId(id);
        toUpdate.setName("SEGA Corporation");
        toUpdate.setCountry("Japan");
        toUpdate.setFoundationYear(1983);

        Developer updated = developerRepository.save(toUpdate);

        Optional<Developer> found = developerRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("SEGA Corporation", found.get().getName());
        assertEquals(1983, found.get().getFoundationYear());
    }

    /**
     * Tests that a developer can be deleted by passing the entity object.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The developer is removed from the database</li>
     *   <li>Subsequent find operations for the deleted developer return empty</li>
     * </ul>
     */
    @Test
    void shouldDeleteDeveloper() {
        Developer developer = new DeveloperImpl();
        developer.setName("Atari");
        developer.setCountry("USA");
        developer.setFoundationYear(1972);

        Developer saved = developerRepository.save(developer);
        int id = saved.getId();

        developerRepository.delete(saved);

        Optional<Developer> found = developerRepository.findById(id);
        assertFalse(found.isPresent());
    }

    /**
     * Tests that a developer can be deleted by ID.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The delete operation returns true when successful</li>
     *   <li>The developer is no longer retrievable after deletion</li>
     * </ul>
     */
    @Test
    void shouldDeleteDeveloperById() {
        Developer developer = new DeveloperImpl();
        developer.setName("Ubisoft");
        developer.setCountry("France");
        developer.setFoundationYear(1986);

        Developer saved = developerRepository.save(developer);
        int id = saved.getId();

        boolean deleted = developerRepository.deleteById(id);
        assertTrue(deleted);

        Optional<Developer> found = developerRepository.findById(id);
        assertFalse(found.isPresent());
    }

    /**
     * Tests the counting of all developers in the repository.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The count matches the number of saved developers</li>
     * </ul>
     */
    @Test
    void shouldCountDevelopers() {
        Developer dev1 = new DeveloperImpl();
        dev1.setName("Sony");
        dev1.setCountry("Japan");
        dev1.setFoundationYear(1946);
        developerRepository.save(dev1);

        Developer dev2 = new DeveloperImpl();
        dev2.setName("Microsoft");
        dev2.setCountry("USA");
        dev2.setFoundationYear(1975);
        developerRepository.save(dev2);

        long count = developerRepository.count();
        assertEquals(2, count);
    }

    /**
     * Tests that existsById returns true for an existing developer.
     */
    @Test
    void shouldReturnTrueWhenDeveloperExists() {
        Developer developer = new DeveloperImpl();
        developer.setName("Epic Games");
        developer.setCountry("USA");
        developer.setFoundationYear(1991);
        Developer saved = developerRepository.save(developer);

        assertTrue(developerRepository.existsById(saved.getId()));
    }

    /**
     * Tests that existsById returns false for a non-existent developer.
     */
    @Test
    void shouldReturnFalseWhenDeveloperDoesNotExist() {
        assertFalse(developerRepository.existsById(999));
    }

    /**
     * Tests that invalid operations throw a CrudException.
     *
     * <p>Verifies that attempting to find a developer with an invalid ID (-1)
     * throws the expected exception.</p>
     */
    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> developerRepository.findById(-1));
    }

    /**
     * Tests case-insensitive counting of developers by country.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>Country name matching is case-insensitive</li>
     *   <li>The counts match the expected number of developers per country</li>
     * </ul>
     */
    @Test
    void shouldCountDevelopersByCountryIgnoreCase() {
        Developer dev1 = new DeveloperImpl();
        dev1.setName("Nintendo");
        dev1.setCountry("Japan");
        dev1.setFoundationYear(1889);
        developerRepository.save(dev1);

        Developer dev2 = new DeveloperImpl();
        dev2.setName("Capcom");
        dev2.setCountry("japan");
        dev2.setFoundationYear(1983);
        developerRepository.save(dev2);

        Developer dev3 = new DeveloperImpl();
        dev3.setName("EA");
        dev3.setCountry("USA");
        dev3.setFoundationYear(1982);
        developerRepository.save(dev3);

        long japanCount = developerRepository.countByCountryIgnoreCase("japan");
        long usaCount = developerRepository.countByCountryIgnoreCase("usa");

        assertEquals(2, japanCount);
        assertEquals(1, usaCount);
    }
}