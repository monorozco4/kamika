package cat.uvic.teknos.dam.kamika.repositories;

import java.util.Set;

/**
 * Interface representing a video game genre.
 * Defines the basic properties and behaviors of a genre entity.
 * <p>
 * Related tables:
 * - Many-to-many relationship with Game.
 * </p>
 */
public interface Genre {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);


    Set<Game> getGames();
    void setGames(Set<Game> games);
}