package cat.uvic.teknos.dam.kamika.repositories;

import java.time.LocalDate;
import java.util.Set;

/**
 * Interface representing a video game.
 * Defines the basic properties and behaviors of a game entity.
 * <p>
 * Related tables:
 * - GENRE: Many-to-many relationship.
 * - CONSOLE: Many-to-many relationship
 * - GAMEEDITION: One-to-many relationship.
 * - DEVELOPER: Many-to-one relationship (game belongs to one developer).
 * - PUBLISHER: Many-to-one relationship (game is published by one publisher).
 * </p>
 */
public interface Game {
    int getId();
    void setId(int id);

    String getTitle();
    void setTitle(String title);

    LocalDate getReleaseDate();
    void setReleaseDate(LocalDate releaseDate);

    Developer getDeveloper();
    void setDeveloper(Developer developer);

    Publisher getPublisher();
    void setPublisher(Publisher publisher);

    String getPegiRating();
    void setPegiRating(String pegiRating);

    boolean isMultiplayer();
    void setMultiplayer(boolean multiplayer);

    Set<Genre> getGenres();
    void setGenres(Set<Genre> genres);

    Set<Console> getConsoles();
    void setConsoles(Set<Console> consoles);

    GameEdition getEdition();
    void setEdition(GameEdition edition);
}