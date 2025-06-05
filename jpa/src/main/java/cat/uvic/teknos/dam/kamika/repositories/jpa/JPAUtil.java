package cat.uvic.teknos.dam.kamika.repositories.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA EntityManager and EntityManagerFactory.
 * This class provides methods to create and manage JPA entities and transactions.
 * <p>
 * It follows the singleton pattern for the EntityManagerFactory to ensure
 * efficient resource usage across the application.
 * </p>
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "kamika-persistence-unit";
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with static methods only.
     */
    private JPAUtil() {
        // Utility class
    }

    /**
     * Gets the EntityManagerFactory instance.
     * Creates it if it doesn't exist (lazy initialization).
     *
     * @return the EntityManagerFactory instance
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create EntityManagerFactory", e);
            }
        }
        return entityManagerFactory;
    }

    /**
     * Creates a new EntityManager instance.
     * The caller is responsible for closing the EntityManager.
     *
     * @return a new EntityManager instance
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Closes the EntityManagerFactory.
     * This should be called when the application is shutting down
     * to properly release resources.
     */
    public static synchronized void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    /**
     * Executes a transaction with the provided EntityManager operation.
     * Automatically handles transaction begin, commit, and rollback.
     *
     * @param operation the operation to execute within the transaction
     * @throws RuntimeException if the transaction fails
     */
    public static void executeInTransaction(EntityManagerOperation operation) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            operation.execute(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Transaction failed", e);
        } finally {
            em.close();
        }
    }

    /**
     * Executes a query operation with the provided EntityManager.
     * Automatically handles EntityManager lifecycle for read operations.
     *
     * @param <T> the return type of the operation
     * @param operation the query operation to execute
     * @return the result of the operation
     */
    public static <T> T executeQuery(EntityManagerQuery<T> operation) {
        EntityManager em = getEntityManager();
        try {
            return operation.execute(em);
        } finally {
            em.close();
        }
    }

    /**
     * Functional interface for operations that need an EntityManager
     * and don't return a value (typically write operations).
     */
    @FunctionalInterface
    public interface EntityManagerOperation {
        void execute(EntityManager entityManager);
    }

    /**
     * Functional interface for operations that need an EntityManager
     * and return a value (typically read operations).
     *
     * @param <T> the return type
     */
    @FunctionalInterface
    public interface EntityManagerQuery<T> {
        T execute(EntityManager entityManager);
    }
}