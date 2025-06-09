package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JpaGameEditionTest {

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
        em.createQuery("DELETE FROM JpaGameEdition").executeUpdate();
        em.createQuery("DELETE FROM JpaGame").executeUpdate();
        em.createQuery("DELETE FROM JpaDeveloper").executeUpdate();
        em.createQuery("DELETE FROM JpaPublisher").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("Persistencia y recuperación de JpaGameEdition")
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

            // Crear y persistir juego
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

        // Crear y persistir edición
        JpaGameEdition edition = new JpaGameEdition();
        edition.setGame(game);
        edition.setEditionName("Gold Edition");
        edition.setSpecialContent("Mapa dorado y póster");
        edition.setPrice(59.99);

        em.getTransaction().begin();
        em.persist(edition);
        em.getTransaction().commit();

        JpaGameEdition found = em.find(JpaGameEdition.class, edition.getId());
        assertNotNull(found);
        assertEquals("Gold Edition", found.getEditionName());
        assertEquals("Mapa dorado y póster", found.getSpecialContent());
        assertEquals(59.99, found.getPrice());
        assertEquals(game.getId(), found.getGame().getId());
    }
}