package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JpaGenreTest {

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
        em.createQuery("DELETE FROM JpaGenre").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("Persistencia y recuperación de JpaGenre")
    void testPersistAndFind() {
        JpaGenre genre = new JpaGenre();
        genre.setName("Aventura");
        genre.setDescription("Juegos de exploración y desafíos");

        em.getTransaction().begin();
        em.persist(genre);
        em.getTransaction().commit();

        JpaGenre found = em.find(JpaGenre.class, genre.getId());
        assertNotNull(found);
        assertEquals("Aventura", found.getName());
        assertEquals("Juegos de exploración y desafíos", found.getDescription());
    }
}