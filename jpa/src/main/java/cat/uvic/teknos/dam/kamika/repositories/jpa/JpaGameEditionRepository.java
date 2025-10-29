package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.GameEdition;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaGameEdition;
import cat.uvic.teknos.dam.kamika.repositories.GameEditionRepository;
import cat.uvic.teknos.dam.kamika.exceptions.CrudException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaGameEditionRepository implements GameEditionRepository {

    public JpaGameEditionRepository() {}

    @Override
    public Optional<GameEdition> findById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            JpaGameEdition gameEdition = entityManager.find(JpaGameEdition.class, id);
            return Optional.ofNullable(gameEdition);
        });
    }

    @Override
    public Optional<GameEdition> findByGameIdAndEditionName(int gameId, String editionName) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaGameEdition> query = entityManager.createQuery(
                    "SELECT ge FROM JpaGameEdition ge WHERE ge.game.id = :gameId AND ge.editionName = :editionName",
                    JpaGameEdition.class
            );
            query.setParameter("gameId", gameId);
            query.setParameter("editionName", editionName);
            return query.getResultStream().findFirst().map(ge -> ge);
        });
    }

    @Override
    public GameEdition save(GameEdition gameEdition) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaGameEdition jpaGameEdition = convertToJpaGameEdition(gameEdition);
                JpaGameEdition saved = persistOrMerge(entityManager, jpaGameEdition);
                entityManager.getTransaction().commit();
                return saved;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new CrudException("Error guardando GameEdition", e);
            }
        });
    }

    private JpaGameEdition convertToJpaGameEdition(GameEdition gameEdition) {
        if (gameEdition instanceof JpaGameEdition) {
            return (JpaGameEdition) gameEdition;
        }
        JpaGameEdition jpaGameEdition = new JpaGameEdition();
        jpaGameEdition.setGame(gameEdition.getGame());
        jpaGameEdition.setEditionName(gameEdition.getEditionName());
        return jpaGameEdition;
    }

    private JpaGameEdition persistOrMerge(EntityManager entityManager, JpaGameEdition jpaGameEdition) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(ge) FROM JpaGameEdition ge WHERE ge.game.id = :gameId AND ge.editionName = :editionName",
                Long.class
        );
        query.setParameter("gameId", jpaGameEdition.getGame().getId());
        query.setParameter("editionName", jpaGameEdition.getEditionName());
        Long count = query.getSingleResult();
        if (count == 0) {
            entityManager.persist(jpaGameEdition);
            return jpaGameEdition;
        } else {
            return entityManager.merge(jpaGameEdition);
        }
    }

    @Override
    public void delete(GameEdition gameEdition) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                JpaGameEdition managed;
                if (gameEdition instanceof JpaGameEdition && entityManager.contains(gameEdition)) {
                    managed = (JpaGameEdition) gameEdition;
                } else {
                    TypedQuery<JpaGameEdition> query = entityManager.createQuery(
                            "SELECT ge FROM JpaGameEdition ge WHERE ge.game.id = :gameId AND ge.editionName = :editionName",
                            JpaGameEdition.class
                    );
                    query.setParameter("gameId", gameEdition.getGame().getId());
                    query.setParameter("editionName", gameEdition.getEditionName());
                    managed = query.getResultStream().findFirst().orElse(null);
                }
                if (managed != null) {
                    entityManager.remove(managed);
                }
            } catch (Exception e) {
                throw new CrudException("Error eliminando GameEdition", e);
            }
        });
    }

    @Override
    public void deleteByGameIdAndEditionName(int gameId, String editionName) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                TypedQuery<JpaGameEdition> query = entityManager.createQuery(
                        "SELECT ge FROM JpaGameEdition ge WHERE ge.game.id = :gameId AND ge.editionName = :editionName",
                        JpaGameEdition.class
                );
                query.setParameter("gameId", gameId);
                query.setParameter("editionName", editionName);
                query.getResultStream().findFirst().ifPresent(entityManager::remove);
            } catch (Exception e) {
                throw new CrudException("Error eliminando GameEdition por clave compuesta", e);
            }
        });
    }

    @Override
    public long count() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(ge) FROM JpaGameEdition ge", Long.class
            );
            return query.getSingleResult();
        });
    }

    @Override
    public boolean existsByGameIdAndEditionName(int gameId, String editionName) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(ge) FROM JpaGameEdition ge WHERE ge.game.id = :gameId AND ge.editionName = :editionName",
                    Long.class
            );
            query.setParameter("gameId", gameId);
            query.setParameter("editionName", editionName);
            Long count = query.getSingleResult();
            return count > 0;
        });
    }

    @Override
    public Optional<GameEdition> findByEditionName(String editionName) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaGameEdition> query = entityManager.createQuery(
                    "SELECT ge FROM JpaGameEdition ge WHERE ge.editionName = :editionName",
                    JpaGameEdition.class
            );
            query.setParameter("editionName", editionName);
            return query.getResultStream().findFirst().map(ge -> ge);
        });
    }

    @Override
    public Set<GameEdition> findAll() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaGameEdition> query = entityManager.createQuery(
                    "SELECT ge FROM JpaGameEdition ge", JpaGameEdition.class
            );
            return query.getResultStream()
                    .map(ge -> (GameEdition) ge)
                    .collect(Collectors.toSet());
        });
    }
}