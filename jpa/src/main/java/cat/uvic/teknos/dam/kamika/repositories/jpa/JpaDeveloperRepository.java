package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaDeveloper;
import cat.uvic.teknos.dam.kamika.repositories.DeveloperRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class JpaDeveloperRepository implements DeveloperRepository {

    @Override
    public Optional<Developer> findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(JpaDeveloper.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Developer save(Developer developer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (developer.getId() <= 0) {
                em.persist(developer);
            } else {
                developer = em.merge(developer);
            }

            em.getTransaction().commit();
            return developer;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error saving developer", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Developer developer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Developer toDelete = em.find(JpaDeveloper.class, developer.getId());
            if (toDelete != null) {
                em.remove(toDelete);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting developer", e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Developer developer = em.find(JpaDeveloper.class, id);
            if (developer != null) {
                em.remove(developer);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting developer by id", e);
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(d) FROM JpaDeveloper d", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsById(int id) {
        return findById(id).isPresent();
    }

    @Override
    public long countByCountryIgnoreCase(String country) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(d) FROM JpaDeveloper d WHERE LOWER(d.country) = LOWER(:country)",
                    Long.class);
            query.setParameter("country", country);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}