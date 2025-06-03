package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

import cat.uvic.teknos.dam.kamika.repositories.Game;

import java.util.Set;

public interface JdbcGenre {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    Set<Game> getGames();
    void setGames(Set<Game> games);
}