package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaGameTest {

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("kamika_test");
    }

    @AfterAll
    static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM JpaGame").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("Persistencia y recuperación de JpaGame")
    void testPersistAndFind() {

        JpaDeveloper developer = new JpaDeveloper();
        developer.setName("Nintendo");
        developer.setCountry("Japón");
        developer.setFoundationYear(1889);

        JpaPublisher publisher = new JpaPublisher();
        publisher.setName("Nintendo");
        publisher.setCountry("Japón");

        em.getTransaction().begin();
        em.persist(developer);
        em.persist(publisher);
        em.getTransaction().commit();

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

        JpaGame found = em.find(JpaGame.class, game.getId());
        assertNotNull(found);
        assertEquals("The Legend of Zelda", found.getTitle());
        assertEquals(LocalDate.of(1986, 2, 21), found.getReleaseDate());
        assertEquals("12", found.getPegiRating());
        assertFalse(found.isMultiplayer());
        assertNotNull(found.getDeveloper());
        assertEquals("Nintendo", found.getDeveloper().getName());
        assertNotNull(found.getPublisher());
        assertEquals("Nintendo", found.getPublisher().getName());
    }
}