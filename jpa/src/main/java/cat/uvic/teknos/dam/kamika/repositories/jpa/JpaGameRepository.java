package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaGame;
import cat.uvic.teknos.dam.kamika.repositories.GameRepository;
import cat.uvic.teknos.dam.kamika.exceptions.CrudException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class JpaGameRepository implements GameRepository {

    public JpaGameRepository() {}

    @Override
    public Optional<Game> findById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            JpaGame game = entityManager.find(JpaGame.class, id);
            return Optional.ofNullable(game);
        });
    }

    @Override
    public Game save(Game game) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaGame jpaGame = convertToJpaGame(game);
                JpaGame savedGame = persistOrMerge(entityManager, jpaGame);
                entityManager.getTransaction().commit();
                return savedGame;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new CrudException("Error guardando el juego", e);
            }
        });
    }

    private JpaGame convertToJpaGame(Game game) {
        if (game instanceof JpaGame) {
            return (JpaGame) game;
        }
        JpaGame jpaGame = new JpaGame();
        jpaGame.setId(game.getId());
        jpaGame.setTitle(game.getTitle());
        jpaGame.setReleaseDate(game.getReleaseDate());
        jpaGame.setDeveloper(game.getDeveloper());
        jpaGame.setPublisher(game.getPublisher());
        jpaGame.setPegiRating(game.getPegiRating());
        jpaGame.setMultiplayer(game.isMultiplayer());
        jpaGame.setGenres(game.getGenres());
        jpaGame.setConsoles(game.getConsoles());
        jpaGame.setEdition(game.getEdition());
        return jpaGame;
    }

    private JpaGame persistOrMerge(EntityManager entityManager, JpaGame jpaGame) {
        if (jpaGame.getId() == 0) {
            entityManager.persist(jpaGame);
            return jpaGame;
        } else {
            return entityManager.merge(jpaGame);
        }
    }

    @Override
    public void delete(Game game) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                JpaGame managedGame;
                if (game instanceof JpaGame && entityManager.contains(game)) {
                    managedGame = (JpaGame) game;
                } else {
                    managedGame = entityManager.find(JpaGame.class, game.getId());
                }
                if (managedGame != null) {
                    entityManager.remove(managedGame);
                }
            } catch (Exception e) {
                throw new CrudException("Error eliminando el juego", e);
            }
        });
    }

    @Override
    public boolean deleteById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaGame game = entityManager.find(JpaGame.class, id);
                if (game != null) {
                    entityManager.remove(game);
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
                throw new CrudException("Error eliminando el juego por ID", e);
            }
        });
    }

    @Override
    public long count() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(g) FROM JpaGame g", Long.class);
            return query.getSingleResult();
        });
    }

    @Override
    public boolean existsById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(g) FROM JpaGame g WHERE g.id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.getSingleResult();
            return count > 0;
        });
    }
}