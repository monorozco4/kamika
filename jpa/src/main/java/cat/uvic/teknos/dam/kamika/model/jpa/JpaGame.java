package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * JPA Entity representing a video game.
 * <p>
 * Maps to the "GAME" table in the database.
 * Follows professor's style: uses Lombok, uppercase column names.
 * </p>
 * <p>
 * Relationships will be mapped as follows (to be added later):
 * - Many-to-many with Genre
 * - Many-to-many with Console
 * - One-to-many with GameEdition.
 * - Many-to-one with Developer.
 * - Many-to-one with Publisher.
 * </p>
 */
@Entity
@Table(name = "GAME")
@Data
public class JpaGame implements Game {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Column(name = "PEGI_RATING")
    private String pegiRating;

    @Column(name = "MULTIPLAYER")
    private boolean multiplayer;

    @Override
    public Developer getDeveloper() {
        return null;
    }

    @Override
    public void setDeveloper(Developer developer) {

    }

    @Override
    public Publisher getPublisher() {
        return null;
    }

    @Override
    public void setPublisher(Publisher publisher) {

    }

    @Override
    public Set<Genre> getGenres() {
        return Set.of();
    }

    @Override
    public void setGenres(Set<Genre> genres) {

    }

    @Override
    public Set<Console> getConsoles() {
        return Set.of();
    }

    @Override
    public void setConsoles(Set<Console> consoles) {

    }

    @Override
    public GameEdition getEdition() {
        return null;
    }

    @Override
    public void setEdition(GameEdition edition) {

    }
}