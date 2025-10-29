package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaDeveloper;
import cat.uvic.teknos.dam.kamika.repositories.DeveloperRepository;
import cat.uvic.teknos.dam.kamika.exceptions.CrudException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JpaDeveloperRepository implements DeveloperRepository {

    public JpaDeveloperRepository() {}

    @Override
    public Optional<Developer> findById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            JpaDeveloper developer = entityManager.find(JpaDeveloper.class, id);
            return Optional.ofNullable(developer);
        });
    }

    @Override
    public Developer save(Developer developer) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaDeveloper jpaDeveloper = convertToJpaDeveloper(developer);
                JpaDeveloper savedDeveloper = persistOrMerge(entityManager, jpaDeveloper);
                entityManager.getTransaction().commit();
                return savedDeveloper;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new CrudException("Error guardando el developer", e);
            }
        });
    }

    private JpaDeveloper convertToJpaDeveloper(Developer developer) {
        if (developer instanceof JpaDeveloper) {
            return (JpaDeveloper) developer;
        }
        JpaDeveloper jpaDeveloper = new JpaDeveloper();
        jpaDeveloper.setId(developer.getId());
        jpaDeveloper.setName(developer.getName());
        jpaDeveloper.setCountry(developer.getCountry());
        return jpaDeveloper;
    }

    private JpaDeveloper persistOrMerge(EntityManager entityManager, JpaDeveloper jpaDeveloper) {
        if (jpaDeveloper.getId() == 0) {
            entityManager.persist(jpaDeveloper);
            return jpaDeveloper;
        } else {
            return entityManager.merge(jpaDeveloper);
        }
    }

    @Override
    public void delete(Developer developer) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                JpaDeveloper managedDeveloper;
                if (developer instanceof JpaDeveloper && entityManager.contains(developer)) {
                    managedDeveloper = (JpaDeveloper) developer;
                } else {
                    managedDeveloper = entityManager.find(JpaDeveloper.class, developer.getId());
                }
                if (managedDeveloper != null) {
                    entityManager.remove(managedDeveloper);
                }
            } catch (Exception e) {
                throw new CrudException("Error eliminando el developer", e);
            }
        });
    }

    @Override
    public boolean deleteById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaDeveloper developer = entityManager.find(JpaDeveloper.class, id);
                if (developer != null) {
                    entityManager.remove(developer);
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
                throw new CrudException("Error eliminando el developer por ID", e);
            }
        });
    }

    @Override
    public long count() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(d) FROM JpaDeveloper d", Long.class);
            return query.getSingleResult();
        });
    }

    @Override
    public boolean existsById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(d) FROM JpaDeveloper d WHERE d.id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.getSingleResult();
            return count > 0;
        });
    }

    @Override
    public long countByCountryIgnoreCase(String country) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(d) FROM JpaDeveloper d WHERE LOWER(d.country) = LOWER(:country)",
                    Long.class);
            query.setParameter("country", country);
            return query.getSingleResult();
        });
    }

    @Override
    public Set<Developer> findAll() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaDeveloper> query = entityManager.createQuery("SELECT d FROM JpaDeveloper d", JpaDeveloper.class);
            return new HashSet<>(query.getResultList());
        });
    }
}