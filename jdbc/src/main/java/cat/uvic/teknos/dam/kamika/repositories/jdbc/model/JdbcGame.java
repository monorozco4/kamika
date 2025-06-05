package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

import cat.uvic.teknos.dam.kamika.model.*;

import java.time.LocalDate;
import java.util.Set;

public interface JdbcGame {
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
