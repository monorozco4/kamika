package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaPublisher;
import cat.uvic.teknos.dam.kamika.repositories.PublisherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class JpaPublisherRepository implements PublisherRepository {

    @Override
    public Optional<Publisher> findById(int id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return Optional.ofNullable(em.find(JpaPublisher.class, id));
        }
    }

    @Override
    public Publisher save(Publisher publisher) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            JpaPublisher jpaPublisher = (JpaPublisher) publisher;
            if (jpaPublisher.getId() <= 0) {
                em.persist(jpaPublisher);
            } else {
                jpaPublisher = em.merge(jpaPublisher);
            }

            em.getTransaction().commit();
            return jpaPublisher;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error saving publisher", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Publisher publisher) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            JpaPublisher toDelete = em.find(JpaPublisher.class, publisher.getId());
            if (toDelete != null) {
                em.remove(toDelete);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting publisher", e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            JpaPublisher publisher = em.find(JpaPublisher.class, id);
            if (publisher != null) {
                em.remove(publisher);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting publisher by id", e);
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM JpaPublisher p", Long.class);
            return query.getSingleResult();
        }
    }

    @Override
    public boolean existsById(int id) {
        return findById(id).isPresent();
    }

    @Override
    public long countByCountryIgnoreCase(String country) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM JpaPublisher p WHERE LOWER(p.country) = LOWER(:country)",
                    Long.class);
            query.setParameter("country", country);
            return query.getSingleResult();
        }
    }
}