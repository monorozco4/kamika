package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JpaPublisherTest {

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
    @DisplayName("Persistencia y recuperación de JpaPublisher")
    void testPersistAndFind() {
        // Crear y persistir developer
        JpaDeveloper developer = new JpaDeveloper();
        developer.setName("Nintendo");
        developer.setCountry("Japón");
        developer.setFoundationYear(1889);

        em.getTransaction().begin();
        em.persist(developer);
        em.getTransaction().commit();

        // Crear y persistir publisher
        JpaPublisher publisher = new JpaPublisher();
        publisher.setName("Nintendo Publisher");
        publisher.setCountry("Japón");
        publisher.setDeveloper(developer);

        em.getTransaction().begin();
        em.persist(publisher);
        em.getTransaction().commit();

        JpaPublisher found = em.find(JpaPublisher.class, publisher.getId());
        assertNotNull(found);
        assertEquals("Nintendo Publisher", found.getName());
        assertEquals("Japón", found.getCountry());
        assertNotNull(found.getDeveloper());
        assertEquals(developer.getId(), found.getDeveloper().getId());
    }
}