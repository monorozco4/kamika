package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.model.impl.PublisherImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcPublisherRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcPublisherRepositoryTest {

    private static DataSource dataSource;
    private static JdbcPublisherRepository repository;

    private static final String TEST_NAME = "Nintendo";
    private static final String UPDATED_NAME = "Square Enix";
    private static final String TEST_COUNTRY = "Japan";
    private static final String UPDATED_COUNTRY = "United Kingdom";

    private static int publisherId;

    @BeforeAll
    static void setUp() {
        dataSource = new SingleConnectionDataSource();
        repository = new JdbcPublisherRepository(dataSource);
        clearTable();
    }

    @AfterAll
    static void tearDown() {
        clearTable();
    }

    private static void clearTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM PUBLISHER");
            statement.executeUpdate("ALTER TABLE PUBLISHER AUTO_INCREMENT = 1");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean up table before test", e);
        }
    }

    @Test
    @Order(1)
    void shouldSaveNewPublisher() {
        Publisher publisher = new PublisherImpl();
        publisher.setName(TEST_NAME);
        publisher.setCountry(TEST_COUNTRY);

        Publisher saved = repository.save(publisher);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(TEST_NAME, saved.getName());
        assertEquals(TEST_COUNTRY, saved.getCountry());

        publisherId = saved.getId();
    }

    @Test
    @Order(2)
    void shouldFindPublisherById() {
        Optional<Publisher> found = repository.findById(publisherId);
        assertTrue(found.isPresent(), "Publisher should be present");

        Publisher p = found.get();
        assertEquals(publisherId, p.getId());
        assertEquals(TEST_NAME, p.getName());
        assertEquals(TEST_COUNTRY, p.getCountry());
    }

    @Test
    @Order(3)
    void shouldUpdatePublisher() {
        Publisher toUpdate = new PublisherImpl();
        toUpdate.setId(publisherId);
        toUpdate.setName(UPDATED_NAME);
        toUpdate.setCountry(UPDATED_COUNTRY);

        repository.save(toUpdate);

        Optional<Publisher> updated = repository.findById(publisherId);
        assertTrue(updated.isPresent(), "Updated publisher should be present");

        Publisher p = updated.get();
        assertEquals(publisherId, p.getId());
        assertEquals(UPDATED_NAME, p.getName());
        assertEquals(UPDATED_COUNTRY, p.getCountry());
    }

    @Test
    @Order(4)
    void shouldDeletePublisher() {
        Publisher publisherToDelete = new PublisherImpl();
        publisherToDelete.setName("EA Sports");
        publisherToDelete.setCountry("USA");
        Publisher saved = repository.save(publisherToDelete);

        repository.delete(saved);

        Optional<Publisher> deleted = repository.findById(saved.getId());
        assertFalse(deleted.isPresent(), "Publisher should not exist after delete()");
    }

    @Test
    @Order(5)
    void shouldDeletePublisherById() {
        Publisher publisherToDelete = new PublisherImpl();
        publisherToDelete.setName("Ubisoft");
        publisherToDelete.setCountry("France");
        Publisher saved = repository.save(publisherToDelete);

        boolean result = repository.deleteById(saved.getId());

        assertTrue(result, "Deletion should return true");
        Optional<Publisher> deleted = repository.findById(saved.getId());
        assertFalse(deleted.isPresent(), "Publisher should not exist after deleteById()");
    }

    @Test
    @Order(6)
    void shouldCountPublishers() {
        long countBefore = repository.count();

        Publisher p1 = new PublisherImpl();
        p1.setName("Sega");
        p1.setCountry("Japan");
        repository.save(p1);

        Publisher p2 = new PublisherImpl();
        p2.setName("Bandai Namco");
        p2.setCountry("Japan");
        repository.save(p2);

        long countAfter = repository.count();
        assertEquals(countBefore + 2, countAfter, "Count should have increased by 2");
    }

    @Test
    @Order(7)
    void shouldCheckIfExistsById() {
        Publisher publisher = new PublisherImpl();
        publisher.setName("Atari");
        publisher.setCountry("USA");
        Publisher saved = repository.save(publisher);

        assertTrue(repository.existsById(saved.getId()), "existsById should return true for existing publisher");
    }

    @Test
    @Order(8)
    void shouldReturnFalseWhenPublisherDoesNotExist() {
        assertFalse(repository.existsById(999), "existsById should return false for non-existing publisher");
    }

    @Test
    @Order(9)
    void shouldCountByCountryIgnoreCase() {
        // Aseguramos tener al menos uno con país coincidente
        Publisher p1 = new PublisherImpl();
        p1.setName("FromSoftware");
        p1.setCountry("Japan");
        repository.save(p1);

        long count = repository.countByCountryIgnoreCase("JAPAN");
        assertTrue(count >= 1, "Should find at least one publisher from Japan");
    }

    @Test
    @Order(10)
    void shouldNotFindAnyPublisherByInvalidCountry() {
        long count = repository.countByCountryIgnoreCase("Atlantis");
        assertEquals(0, count, "No publishers should be found in Atlantis");
    }

    @Test
    @Order(11)
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> repository.findById(-1), "Invalid ID should throw CrudException");
    }
}