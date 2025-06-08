package cat.uvic.teknos.dam.kamika.repositories.jpa;

import cat.uvic.teknos.dam.kamika.model.Genre;
import cat.uvic.teknos.dam.kamika.repositories.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Map;
import java.util.Optional;

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

}