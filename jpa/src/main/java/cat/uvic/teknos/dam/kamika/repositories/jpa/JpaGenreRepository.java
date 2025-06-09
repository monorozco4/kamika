package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Genre;
import cat.uvic.teknos.dam.kamika.model.jpa.JpaGenre;
import cat.uvic.teknos.dam.kamika.repositories.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JpaGenreRepository implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Genre> findById(int id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        if (!existsById(genre.getId())) {
            entityManager.persist(genre);
            return genre;
        } else {
            return entityManager.merge(genre);
        }
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        Genre managed = entityManager.contains(genre) ? genre : entityManager.merge(genre);
        entityManager.remove(managed);
    }

    @Override
    @Transactional
    public boolean deleteById(int id) {
        Optional<Genre> genre = findById(id);
        genre.ifPresent(this::delete);
        return genre.isPresent();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(g) FROM JpaGenre g", Long.class).getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<Genre> findByNameIgnoreCase(String name) {
        return Optional.empty();
    }

    @Override
    public Map<Integer, Long> countGamesPerGenre() {
        return Map.of();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        var query = entityManager.createQuery(
                "SELECT g FROM JpaGenre g WHERE LOWER(g.name) = LOWER(:name)", Genre.class);
        query.setParameter("name", name);
        var resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultList.getFirst());
    }

    @Override
    public Set<Genre> findAll() {
        return JPAUtil.executeQuery(entityManager -> {
            TypedQuery<JpaGenre> query = entityManager.createQuery("SELECT g FROM JpaGenre g", JpaGenre.class);
            return new HashSet<>(query.getResultList());
        });
    }
}