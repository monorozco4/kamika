package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JpaDeveloperTest {

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
        em.createQuery("DELETE FROM JpaGameEdition").executeUpdate();
        em.createQuery("DELETE FROM JpaGame").executeUpdate();
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
    @DisplayName("Persistencia y recuperación de JpaDeveloper")
    void testPersistAndFind() {
        JpaDeveloper dev = new JpaDeveloper();
        dev.setName("Nintendo");
        dev.setCountry("Japón");
        dev.setFoundationYear(1889);

        em.getTransaction().begin();
        em.persist(dev);
        em.getTransaction().commit();

        JpaDeveloper found = em.find(JpaDeveloper.class, dev.getId());
        assertNotNull(found);
        assertEquals("Nintendo", found.getName());
        assertEquals("Japón", found.getCountry());
        assertEquals(1889, found.getFoundationYear());
    }
}