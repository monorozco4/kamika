package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JpaGameConsoleTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    void setUpClass() {
        emf = Persistence.createEntityManagerFactory("kamika_test");
    }

    @AfterAll
    void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM JpaGameConsole").executeUpdate();
        em.createQuery("DELETE FROM JpaGame").executeUpdate();
        em.createQuery("DELETE FROM JpaConsole").executeUpdate();
        em.createQuery("DELETE FROM JpaPublisher").executeUpdate();
        em.createQuery("DELETE FROM JpaDeveloper").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("Persistencia y recuperación de JpaGameConsole")
    void testPersistAndFind() {
        // Crear y persistir el developer
        JpaDeveloper developer = new JpaDeveloper();
        developer.setName("Nintendo");
        developer.setCountry("Japón");
        developer.setFoundationYear(1889);

        em.getTransaction().begin();
        em.persist(developer);
        em.getTransaction().commit();

        // Crear y persistir el publisher (asignando developer)
        JpaPublisher publisher = new JpaPublisher();
        publisher.setName("Nintendo");
        publisher.setCountry("Japón");
        publisher.setDeveloper(developer);

        em.getTransaction().begin();
        em.persist(publisher);
        em.getTransaction().commit();

        // Crear y persistir la consola
        JpaConsole console = new JpaConsole();
        console.setName("NES");
        console.setManufacturer("Nintendo");
        console.setReleaseYear(1983);

        em.getTransaction().begin();
        em.persist(console);
        em.getTransaction().commit();

        // Crear y persistir el juego
        JpaGame game = new JpaGame();
        game.setTitle("The Legend of Zelda");
        game.setReleaseDate(LocalDate.of(1986, 2, 21));
        game.setPegiRating("12");
        game.setMultiplayer(false);
        game.setDeveloper(developer);
        game.setPublisher(publisher);

        em.getTransaction().begin();
        em.persist(game);
        em.getTransaction().commit();

        // Crear y persistir la relación GameConsole
        JpaGameConsole gameConsole = new JpaGameConsole();
        gameConsole.setGame(game);
        gameConsole.setConsole(console);
        gameConsole.setReleaseDate(LocalDate.of(1986, 2, 21));
        gameConsole.setExclusive(true);
        gameConsole.setResolution("256x240");

        em.getTransaction().begin();
        em.persist(gameConsole);
        em.getTransaction().commit();

        JpaGameConsole found = em.find(JpaGameConsole.class, gameConsole.getGameConsoleId());
        assertNotNull(found);
        assertEquals(game.getId(), found.getGame().getId());
        assertEquals(console.getId(), found.getConsole().getId());
        assertEquals(LocalDate.of(1986, 2, 21), found.getReleaseDate());
        assertTrue(found.isExclusive());
        assertEquals("256x240", found.getResolution());
    }
}