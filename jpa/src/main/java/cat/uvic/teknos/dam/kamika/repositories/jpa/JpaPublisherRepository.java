package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaPublisher;
import cat.uvic.teknos.dam.kamika.repositories.PublisherRepository;
import cat.uvic.teknos.dam.kamika.exceptions.CrudException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class JpaPublisherRepository implements PublisherRepository {

    public JpaPublisherRepository() {}

    @Override
    public Optional<Publisher> findById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            JpaPublisher publisher = entityManager.find(JpaPublisher.class, id);
            return Optional.ofNullable(publisher);
        });
    }

    @Override
    public Publisher save(Publisher publisher) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaPublisher jpaPublisher = convertToJpaPublisher(publisher);
                JpaPublisher savedPublisher = persistOrMerge(entityManager, jpaPublisher);
                entityManager.getTransaction().commit();
                return savedPublisher;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new CrudException("Error guardando el publisher", e);
            }
        });
    }

    private JpaPublisher convertToJpaPublisher(Publisher publisher) {
        if (publisher instanceof JpaPublisher) {
            return (JpaPublisher) publisher;
        }
        JpaPublisher jpaPublisher = new JpaPublisher();
        jpaPublisher.setId(publisher.getId());
        jpaPublisher.setName(publisher.getName());
        jpaPublisher.setCountry(publisher.getCountry());
        jpaPublisher.setDeveloper(publisher.getDeveloper());
        return jpaPublisher;
    }

    private JpaPublisher persistOrMerge(EntityManager entityManager, JpaPublisher jpaPublisher) {
        if (jpaPublisher.getId() == 0) {
            entityManager.persist(jpaPublisher);
            return jpaPublisher;
        } else {
            return entityManager.merge(jpaPublisher);
        }
    }

    @Override
    public void delete(Publisher publisher) {
        JPAUtil.executeInTransaction(entityManager -> {
            try {
                JpaPublisher managedPublisher;
                if (publisher instanceof JpaPublisher && entityManager.contains(publisher)) {
                    managedPublisher = (JpaPublisher) publisher;
                } else {
                    managedPublisher = entityManager.find(JpaPublisher.class, publisher.getId());
                }
                if (managedPublisher != null) {
                    entityManager.remove(managedPublisher);
                }
            } catch (Exception e) {
                throw new CrudException("Error eliminando el publisher", e);
            }
        });
    }

    @Override
    public boolean deleteById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            entityManager.getTransaction().begin();
            try {
                JpaPublisher publisher = entityManager.find(JpaPublisher.class, id);
                if (publisher != null) {
                    entityManager.remove(publisher);
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
                throw new CrudException("Error eliminando el publisher por ID", e);
            }
        });
    }

    @Override
    public long count() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(p) FROM JpaPublisher p", Long.class);
            return query.getSingleResult();
        });
    }

    @Override
    public boolean existsById(int id) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(p) FROM JpaPublisher p WHERE p.id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.getSingleResult();
            return count > 0;
        });
    }

    @Override
    public long countByCountryIgnoreCase(String country) {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(p) FROM JpaPublisher p WHERE LOWER(p.country) = LOWER(:country)", Long.class);
            query.setParameter("country", country);
            return query.getSingleResult();
        });
    }
}