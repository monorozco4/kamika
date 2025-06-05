package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaConsole;
import cat.uvic.teknos.dam.kamika.repositories.ConsoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

/**
 * JPA implementation of the ConsoleRepository interface.
 * Provides concrete data access operations for Console entities using JPA EntityManager.
 * <p>
 * This implementation handles the persistence operations for Console entities,
 * including CRUD operations and query methods.
 * </p>
 */
public class JpaConsoleRepository implements ConsoleRepository {

    /**
     * Default constructor.
     * Uses JPAUtil to manage EntityManager instances.
     */
    public JpaConsoleRepository() {
        // EntityManager instances are created per operation using JPAUtil
    }

    @Override
    public Optional<Console> findById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            JpaConsole console = entityManager.find(JpaConsole.class, id);
            return Optional.ofNullable(console);
        });
    }

    @Override
    public Console save(Console console) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();

            try {
                JpaConsole jpaConsole = convertToJpaConsole(console);
                JpaConsole savedConsole = persistOrMerge(entityManager, jpaConsole);

                entityManager.getTransaction().commit();
                return savedConsole;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new RuntimeException("Error saving console", e);
            }
        });
    }

    /**
     * Converts a Console interface instance to a JpaConsole entity.
     * If the console is already a JpaConsole, returns it as-is.
     * Otherwise, creates a new JpaConsole with the same data.
     *
     * @param console the console to convert
     * @return a JpaConsole instance
     */
    private JpaConsole convertToJpaConsole(Console console) {
        if (console instanceof JpaConsole) {
            return (JpaConsole) console;
        }

        JpaConsole jpaConsole = new JpaConsole();
        jpaConsole.setId(console.getId());
        jpaConsole.setName(console.getName());
        jpaConsole.setManufacturer(console.getManufacturer());
        jpaConsole.setReleaseYear(console.getReleaseYear());
        return jpaConsole;
    }

    /**
     * Persists a new entity or merges an existing one based on the entity's ID.
     * If the ID is 0, the entity is considered new and will be persisted.
     * Otherwise, it will be merged with the existing entity.
     *
     * @param entityManager the EntityManager to use for the operation
     * @param jpaConsole the console entity to save
     * @return the saved console entity
     */
    private JpaConsole persistOrMerge(EntityManager entityManager, JpaConsole jpaConsole) {
        if (jpaConsole.getId() == 0) {
            // New entity - persist
            entityManager.persist(jpaConsole);
            return jpaConsole;
        } else {
            // Existing entity - merge
            return entityManager.merge(jpaConsole);
        }
    }

    @Override
    public void delete(Console console) {
        JPAUtil.executeInTransaction(entityManager -> {
            JpaConsole managedConsole;
            if (console instanceof JpaConsole && entityManager.contains(console)) {
                managedConsole = (JpaConsole) console;
            } else {
                // Find the managed entity
                managedConsole = entityManager.find(JpaConsole.class, console.getId());
            }

            if (managedConsole != null) {
                entityManager.remove(managedConsole);
            }
        });
    }

    @Override
    public boolean deleteById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();

            try {
                JpaConsole console = entityManager.find(JpaConsole.class, id);
                if (console != null) {
                    entityManager.remove(console);
                    entityManager.getTransaction().commit();
                    return true;
                } else {
                    entityManager.getTransaction().rollback();
                    return false;
                }
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new RuntimeException("Error deleting console by ID", e);
            }
        });
    }

    @Override
    public long count() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(c) FROM JpaConsole c", Long.class);
            return query.getSingleResult();
        });
    }

    @Override
    public boolean existsById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(c) FROM JpaConsole c WHERE c.id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.getSingleResult();
            return count > 0;
        });
    }
}