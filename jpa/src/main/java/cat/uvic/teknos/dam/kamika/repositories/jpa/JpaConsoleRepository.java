package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaConsole;
import cat.uvic.teknos.dam.kamika.repositories.ConsoleRepository;
import cat.uvic.teknos.dam.kamika.exceptions.CrudException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JpaConsoleRepository implements ConsoleRepository {

    public JpaConsoleRepository() {}

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
                throw new CrudException("Error guardando la consola", e);
            }
        });
    }

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

    private JpaConsole persistOrMerge(EntityManager entityManager, JpaConsole jpaConsole) {
        if (jpaConsole.getId() == 0) {
            entityManager.persist(jpaConsole);
            return jpaConsole;
        } else {
            return entityManager.merge(jpaConsole);
        }
    }

    @Override
    public void delete(Console console) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                JpaConsole managedConsole;
                if (console instanceof JpaConsole && entityManager.contains(console)) {
                    managedConsole = (JpaConsole) console;
                } else {
                    managedConsole = entityManager.find(JpaConsole.class, console.getId());
                }
                if (managedConsole != null) {
                    entityManager.remove(managedConsole);
                }
            } catch (Exception e) {
                throw new CrudException("Error eliminando la consola", e);
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
                throw new CrudException("Error eliminando la consola por ID", e);
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

    @Override
    public Set<Console> findAll() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaConsole> query = entityManager.createQuery("SELECT c FROM JpaConsole c", JpaConsole.class);
            return new HashSet<>(query.getResultList());
        });
    }
}