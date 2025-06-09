package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.model.impl.PublisherImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcPublisherRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcPublisherRepositoryTest {

    private JdbcPublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DELETE FROM GAME_CONSOLE");
            stmt.execute("DELETE FROM GAME_EDITION");
            stmt.execute("DELETE FROM GAME");
            stmt.execute("DELETE FROM PUBLISHER");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS PUBLISHER (
                    PUBLISHER_ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(100) NOT NULL,
                    COUNTRY VARCHAR(50)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS GAME (
                    GAME_ID INT PRIMARY KEY AUTO_INCREMENT,
                    TITLE VARCHAR(100) NOT NULL,
                    RELEASE_DATE DATE,
                    DEVELOPER_ID INT NOT NULL,
                    PUBLISHER_ID INT NOT NULL,
                    GENRE_ID INT,
                    PEGI_RATING VARCHAR(10),
                    IS_MULTIPLAYER TINYINT
                )
            """);

        } catch (Exception e) {
            fail("Error cleaning up database before test", e);
        }

        publisherRepository = new JdbcPublisherRepository(dataSource);
    }

    @Test
    void shouldSaveAndFindPublisherById() {
        Publisher publisher = new PublisherImpl();
        publisher.setName("Nintendo");
        publisher.setCountry("Japan");

        Publisher saved = publisherRepository.save(publisher);
        int id = saved.getId();

        Optional<Publisher> found = publisherRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Nintendo", found.get().getName());
        assertEquals("Japan", found.get().getCountry());
    }

    @Test
    void shouldUpdatePublisherWhenIdExists() {
        Publisher publisher = new PublisherImpl();
        publisher.setName("Sony");
        publisher.setCountry("Japan");

        Publisher saved = publisherRepository.save(publisher);
        int id = saved.getId();

        Publisher toUpdate = new PublisherImpl();
        toUpdate.setId(id);
        toUpdate.setName("Sony Interactive");
        toUpdate.setCountry("Japan");

        Publisher updated = publisherRepository.save(toUpdate);

        Optional<Publisher> found = publisherRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Sony Interactive", found.get().getName());
    }

    @Test
    void shouldDeletePublisher() {
        Publisher publisher = new PublisherImpl();
        publisher.setName("Ubisoft");
        publisher.setCountry("France");

        Publisher saved = publisherRepository.save(publisher);
        int id = saved.getId();

        publisherRepository.delete(saved);

        Optional<Publisher> found = publisherRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldDeletePublisherById() {
        Publisher publisher = new PublisherImpl();
        publisher.setName("EA");
        publisher.setCountry("USA");

        Publisher saved = publisherRepository.save(publisher);
        int id = saved.getId();

        boolean deleted = publisherRepository.deleteById(id);
        assertTrue(deleted);

        Optional<Publisher> found = publisherRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCountPublishers() {
        Publisher publisher1 = new PublisherImpl();
        publisher1.setName("Square Enix");
        publisher1.setCountry("Japan");
        publisherRepository.save(publisher1);

        Publisher publisher2 = new PublisherImpl();
        publisher2.setName("Activision");
        publisher2.setCountry("USA");
        publisherRepository.save(publisher2);

        long count = publisherRepository.count();
        assertEquals(2, count);
    }

    @Test
    void shouldReturnTrueWhenPublisherExists() {
        Publisher publisher = new PublisherImpl();
        publisher.setName("Capcom");
        publisher.setCountry("Japan");
        Publisher saved = publisherRepository.save(publisher);

        assertTrue(publisherRepository.existsById(saved.getId()));
    }

    @Test
    void shouldReturnFalseWhenPublisherDoesNotExist() {
        assertFalse(publisherRepository.existsById(999));
    }

    @Test
    void shouldThrowCrudExceptionOnInvalidOperation() {
        assertThrows(CrudException.class, () -> publisherRepository.findById(-1));
    }
}