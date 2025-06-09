package cat.uvic.teknos.dam.kamika.model.jpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JpaConsoleTest {

    private void limpiarTabla(EntityManager entityManager) {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM JpaGameConsole").executeUpdate(); // Borra dependencias primero si existen
        entityManager.createQuery("DELETE FROM JpaConsole").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    @DisplayName("Prueba JPQL con JpaConsole")
    void jpqlTest() {
        try (var entityManagerFactory = Persistence.createEntityManagerFactory("kamika_test")) {
            var entityManager = entityManagerFactory.createEntityManager();
            limpiarTabla(entityManager);

            JpaConsole ps = new JpaConsole();
            ps.setName("PlayStation");
            ps.setManufacturer("Sony");
            ps.setReleaseYear(1994);

            JpaConsole xbox = new JpaConsole();
            xbox.setName("Xbox");
            xbox.setManufacturer("Microsoft");
            xbox.setReleaseYear(2001);

            entityManager.getTransaction().begin();
            entityManager.persist(ps);
            entityManager.persist(xbox);
            entityManager.getTransaction().commit();

            var query = entityManager.createQuery("select c from JpaConsole c where c.name = :name", JpaConsole.class);
            query.setParameter("name", "PlayStation");
            var console = query.getSingleResult();

            assertNotNull(console);
            assertEquals("PlayStation", console.getName());
            assertEquals("Sony", console.getManufacturer());
            assertEquals(1994, console.getReleaseYear());

            entityManager.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            fail("Excepción inesperada: " + exception.getMessage());
        }
    }

    @Test
    @DisplayName("Prueba Criteria con JpaConsole")
    void criteriaTest() {
        try (var entityManagerFactory = Persistence.createEntityManagerFactory("kamika_test")) {
            var entityManager = entityManagerFactory.createEntityManager();
            limpiarTabla(entityManager);

            JpaConsole ps = new JpaConsole();
            ps.setName("PlayStation");
            ps.setManufacturer("Sony");
            ps.setReleaseYear(1994);

            JpaConsole xbox = new JpaConsole();
            xbox.setName("Xbox");
            xbox.setManufacturer("Microsoft");
            xbox.setReleaseYear(2001);

            entityManager.getTransaction().begin();
            entityManager.persist(ps);
            entityManager.persist(xbox);
            entityManager.getTransaction().commit();

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<JpaConsole> cq = cb.createQuery(JpaConsole.class);
            Root<JpaConsole> console = cq.from(JpaConsole.class);
            cq.select(console).where(cb.equal(console.get("name"), "PlayStation"));

            var consoleFromCriteria = entityManager.createQuery(cq).getSingleResult();

            assertNotNull(consoleFromCriteria);
            assertEquals("PlayStation", consoleFromCriteria.getName());
            assertEquals("Sony", consoleFromCriteria.getManufacturer());
            assertEquals(1994, consoleFromCriteria.getReleaseYear());

            entityManager.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            fail("Excepción inesperada: " + exception.getMessage());
        }
    }
}