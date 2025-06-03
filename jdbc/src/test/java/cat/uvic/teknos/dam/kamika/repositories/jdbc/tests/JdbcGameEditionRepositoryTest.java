package cat.uvic.teknos.dam.kamika.repositories.jdbc.tests;

import cat.uvic.teknos.dam.kamika.repositories.Game;
import cat.uvic.teknos.dam.kamika.repositories.GameEdition;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameEditionImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcGameEditionRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.jupiter.LoadDatabaseExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcGameEditionRepositoryTest {

    private JdbcGameEditionRepository repository;

    @Test
    @Order(1)
    void shouldSaveGameEdition() {
        Game game = new GameImpl();
        ((GameImpl) game).setId(1); // Simulamos un juego ya existente

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Special Edition");
        edition.setSpecialContent("Artbook and Soundtrack");
        edition.setPrice(69.99);

        repository.save(edition);

        assertTrue(repository.existsByGameIdAndEditionName(1, "Special Edition"));
    }

    @Test
    @Order(2)
    void shouldFindGameEditionById() {
        Game game = new GameImpl();
        ((GameImpl) game).setId(2);

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Limited Edition");
        edition.setSpecialContent("Figurine and DLC");
        edition.setPrice(79.99);

        repository.save(edition);

        Optional<GameEdition> found = repository.findByGameIdAndEditionName(2, "Limited Edition");

        assertTrue(found.isPresent());
        assertEquals("Figurine and DLC", found.get().getSpecialContent());
        assertEquals(79.99, found.get().getPrice(), 0.01);
    }

    @Test
    @Order(3)
    void shouldUpdateGameEdition() {
        Game game = new GameImpl();
        ((GameImpl) game).setId(3);

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Collector's Edition");
        edition.setSpecialContent("Original Soundtrack");
        edition.setPrice(49.99);

        repository.save(edition);

        edition.setSpecialContent("Soundtrack + Poster");
        edition.setPrice(59.99);
        repository.save(edition);

        Optional<GameEdition> updated = repository.findByGameIdAndEditionName(3, "Collector's Edition");

        assertTrue(updated.isPresent());
        assertEquals("Soundtrack + Poster", updated.get().getSpecialContent());
        assertEquals(59.99, updated.get().getPrice(), 0.01);
    }

    @Test
    @Order(4)
    void shouldDeleteGameEdition() {
        Game game = new GameImpl();
        ((GameImpl) game).setId(4);

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Remake Edition");
        edition.setSpecialContent("Steelbook Case");
        edition.setPrice(59.99);

        repository.save(edition);

        repository.delete(edition);

        assertFalse(repository.existsByGameIdAndEditionName(4, "Remake Edition"));
    }

    @Test
    @Order(5)
    void shouldCountGameEditions() {
        Game game1 = new GameImpl();
        ((GameImpl) game1).setId(5);

        GameEdition edition1 = new GameEditionImpl();
        edition1.setGame(game1);
        edition1.setEditionName("Standard Edition");
        edition1.setPrice(39.99);

        GameEdition edition2 = new GameEditionImpl();
        edition2.setGame(game1);
        edition2.setEditionName("Deluxe Edition");
        edition2.setPrice(59.99);

        repository.save(edition1);
        repository.save(edition2);

        long count = repository.count();

        assertEquals(2, count);
    }

    @Test
    @Order(6)
    void shouldDeleteByGameIdAndEditionName() {
        Game game = new GameImpl();
        ((GameImpl) game).setId(6);

        GameEdition edition = new GameEditionImpl();
        edition.setGame(game);
        edition.setEditionName("Retro Edition");
        edition.setPrice(19.99);

        repository.save(edition);

        boolean deleted = repository.deleteByGameIdAndEditionName(6, "Retro Edition");

        assertTrue(deleted);
        assertFalse(repository.existsByGameIdAndEditionName(6, "Retro Edition"));
    }
}