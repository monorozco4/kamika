package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.repositories.Console;
import cat.uvic.teknos.dam.kamika.repositories.impl.ConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcConsoleRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.util.Optional;


import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite for the {@link JdbcConsoleRepository} class.
 *
 * <p>This test class uses an in-memory H2 database to validate that all CRUD operations
 * on the Console entity behave as expected.</p>
 *
 * <p>Before each test:
 * <ul>
 *   <li>A fresh table named "CONSOLE" is created if it does not exist.</li>
 *   <li>The table is cleared to ensure a clean state before each test.</li>
 * </ul>
 * </p>
 */
@ExtendWith(LoadDatabaseExtension.class)
class JdbcConsoleRepositoryTest {

    private JdbcConsoleRepository consoleRepository;

    /**
     * Tests that a newly saved console can be retrieved by its ID.
     *
     * <p>Verifies that:
     * <ul>
     *   <li>The saved console has a valid ID assigned.</li>
     *   <li>The console retrieved using findById() matches the original data.</li>
     * </ul>
     * </p>
     */
    @Test
    void shouldSaveAndFindConsoleById() {
        Console console = new ConsoleImpl();
        console.setName("NES");
        console.setManufacturer("Nintendo");
        console.setReleaseYear(1985);

        Console saved = consoleRepository.save(console);
        int id = saved.getId();

        Optional<Console> found = consoleRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("NES", found.get().getName());
        assertEquals("Nintendo", found.get().getManufacturer());
        assertEquals(1985, found.get().getReleaseYear());
    }

    /**
     * Tests that updating a console with an existing ID modifies its stored data.
     *
     * <p>Verifies that after calling save() on an updated console object,
     * the persisted values reflect the changes.</p>
     */
    @Test
    void shouldUpdateConsoleWhenIdExists() {
        Console console = new ConsoleImpl();
        console.setName("Sega Master System");
        console.setManufacturer("Sega");
        console.setReleaseYear(1987);

        Console saved = consoleRepository.save(console);
        int id = saved.getId();

        Console toUpdate = new ConsoleImpl();
        toUpdate.setId(id);
        toUpdate.setName("Master System II");
        toUpdate.setManufacturer("Sega");
        toUpdate.setReleaseYear(1989);

        Console updated = consoleRepository.save(toUpdate);

        Optional<Console> found = consoleRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Master System II", found.get().getName());
        assertEquals(1989, found.get().getReleaseYear());
    }

    /**
     * Tests that a console can be deleted using the delete() method.
     *
     * <p>Verifies that after deletion, the console is no longer present in the database.</p>
     */
    @Test
    void shouldDeleteConsole() {
        Console console = new ConsoleImpl();
        console.setName("Atari 2600");
        console.setManufacturer("Atari");
        console.setReleaseYear(1977);

        Console saved = consoleRepository.save(console);
        int id = saved.getId();

        consoleRepository.delete(saved);

        Optional<Console> found = consoleRepository.findById(id);
        assertFalse(found.isPresent());
    }

    /**
     * Tests that a console can be deleted using its ID via deleteById().
     *
     * <p>Verifies that the method returns true when a console is successfully deleted.</p>
     */
    @Test
    void shouldDeleteConsoleById() {
        Console console = new ConsoleImpl();
        console.setName("ZX Spectrum");
        console.setManufacturer("Sinclair Research");
        console.setReleaseYear(1982);

        Console saved = consoleRepository.save(console);
        int id = saved.getId();

        boolean deleted = consoleRepository.deleteById(id);
        assertTrue(deleted);

        Optional<Console> found = consoleRepository.findById(id);
        assertFalse(found.isPresent());
    }

    /**
     * Tests that the count() method returns the correct number of consoles in the database.
     */
    @Test
    void shouldCountConsoles() {
        Console console1 = new ConsoleImpl();
        console1.setName("PlayStation");
        console1.setManufacturer("Sony");
        console1.setReleaseYear(1994);
        consoleRepository.save(console1);

        Console console2 = new ConsoleImpl();
        console2.setName("Xbox");
        console2.setManufacturer("Microsoft");
        console2.setReleaseYear(2001);
        consoleRepository.save(console2);

        long count = consoleRepository.count();
        assertEquals(2, count);
    }

    /**
     * Tests that existsById() returns true when a console with the given ID exists.
     */
    @Test
    void shouldReturnTrueWhenConsoleExists() {
        Console console = new ConsoleImpl();
        console.setName("Game Boy");
        console.setManufacturer("Nintendo");
        console.setReleaseYear(1989);
        Console saved = consoleRepository.save(console);

        assertTrue(consoleRepository.existsById(saved.getId()));
    }

    /**
     * Tests that existsById() returns false when a console with the given ID does not exist.
     */
    @Test
    void shouldReturnFalseWhenConsoleDoesNotExist() {
        assertFalse(consoleRepository.existsById(999));
    }

    /**
     * Tests that invalid operations throw a CrudException.
     *
     * <p>In this case, attempting to find a console with a negative ID should result
     * in a CrudException being thrown.</p>
     */
    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> consoleRepository.findById(-1));
    }
}