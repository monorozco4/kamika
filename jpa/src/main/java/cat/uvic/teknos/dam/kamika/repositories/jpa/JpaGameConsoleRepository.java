package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.GameConsole;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaGameConsole;
import cat.uvic.teknos.dam.kamika.repositories.GameConsoleRepository;
import cat.uvic.teknos.dam.kamika.exceptions.CrudException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class JpaGameConsoleRepository implements GameConsoleRepository {

    public JpaGameConsoleRepository() {}

    @Override
    public Optional<GameConsole> findByGameConsoleId(int gameConsoleId) {
        return JPAUtil.executeQuery(entityManager -> {
            JpaGameConsole gameConsole = entityManager.find(JpaGameConsole.class, gameConsoleId);
            return Optional.ofNullable(gameConsole);
        });
    }

    @Override
    public boolean deleteByGameConsoleId(int gameConsoleId) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaGameConsole gameConsole = entityManager.find(JpaGameConsole.class, gameConsoleId);
                if (gameConsole != null) {
                    entityManager.remove(gameConsole);
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
                throw new CrudException("Error eliminando GameConsole por gameConsoleId", e);
            }
        });
    }

    @Override
    public Optional<GameConsole> findById(int gameId, int consoleId) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaGameConsole> query = entityManager.createQuery(
                    "SELECT gc FROM JpaGameConsole gc WHERE gc.game.id = :gameId AND gc.console.id = :consoleId",
                    JpaGameConsole.class
            );
            query.setParameter("gameId", gameId);
            query.setParameter("consoleId", consoleId);
            return query.getResultStream().findFirst().map(gc -> gc);
        });
    }

    @Override
    public GameConsole save(GameConsole gameConsole) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaGameConsole jpaGameConsole = convertToJpaGameConsole(gameConsole);
                JpaGameConsole savedGameConsole = persistOrMerge(entityManager, jpaGameConsole);
                entityManager.getTransaction().commit();
                return savedGameConsole;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new CrudException("Error guardando GameConsole", e);
            }
        });
    }

    private JpaGameConsole convertToJpaGameConsole(GameConsole gameConsole) {
        if (gameConsole instanceof JpaGameConsole) {
            return (JpaGameConsole) gameConsole;
        }
        JpaGameConsole jpaGameConsole = new JpaGameConsole();
        jpaGameConsole.setGameConsoleId(gameConsole.getGameConsoleId());
        jpaGameConsole.setGame(gameConsole.getGame());
        jpaGameConsole.setConsole(gameConsole.getConsole());
        jpaGameConsole.setReleaseDate(gameConsole.getReleaseDate());
        jpaGameConsole.setExclusive(gameConsole.isExclusive());
        jpaGameConsole.setResolution(gameConsole.getResolution());
        return jpaGameConsole;
    }

    private JpaGameConsole persistOrMerge(EntityManager entityManager, JpaGameConsole jpaGameConsole) {
        if (jpaGameConsole.getGameConsoleId() == 0) {
            entityManager.persist(jpaGameConsole);
            return jpaGameConsole;
        } else {
            return entityManager.merge(jpaGameConsole);
        }
    }

    @Override
    public void delete(GameConsole gameConsole) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                JpaGameConsole managedGameConsole;
                if (gameConsole instanceof JpaGameConsole && entityManager.contains(gameConsole)) {
                    managedGameConsole = (JpaGameConsole) gameConsole;
                } else {
                    managedGameConsole = entityManager.find(JpaGameConsole.class, gameConsole.getGameConsoleId());
                }
                if (managedGameConsole != null) {
                    entityManager.remove(managedGameConsole);
                }
            } catch (Exception e) {
                throw new CrudException("Error eliminando GameConsole", e);
            }
        });
    }

    @Override
    public boolean deleteById(int gameId, int consoleId) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                TypedQuery<JpaGameConsole> query = entityManager.createQuery(
                        "SELECT gc FROM JpaGameConsole gc WHERE gc.game.id = :gameId AND gc.console.id = :consoleId",
                        JpaGameConsole.class
                );
                query.setParameter("gameId", gameId);
                query.setParameter("consoleId", consoleId);
                JpaGameConsole gameConsole = query.getResultStream().findFirst().orElse(null);
                if (gameConsole != null) {
                    entityManager.remove(gameConsole);
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
                throw new CrudException("Error eliminando GameConsole por clave compuesta", e);
            }
        });
    }

    @Override
    public long count() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(gc) FROM JpaGameConsole gc", Long.class
            );
            return query.getSingleResult();
        });
    }

    @Override
    public boolean existsById(int gameId, int consoleId) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(gc) FROM JpaGameConsole gc WHERE gc.game.id = :gameId AND gc.console.id = :consoleId",
                    Long.class
            );
            query.setParameter("gameId", gameId);
            query.setParameter("consoleId", consoleId);
            Long count = query.getSingleResult();
            return count > 0;
        });
    }
}