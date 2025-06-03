package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.Genre;
import cat.uvic.teknos.dam.kamika.model.GameEdition;
import cat.uvic.teknos.dam.kamika.model.Publisher;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Implementación concreta de la interfaz Game.
 * Contiene los atributos principales del juego, como título, fecha de lanzamiento, etc.
 */
public class GameImpl implements Game {

    private int id;
    private String title;
    private LocalDate releaseDate;
    private Developer developer;
    private Publisher publisher;
    private String pegiRating;
    private boolean multiplayer;
    private Set<Genre> genres = new HashSet<>();
    private Set<Console> consoles = new HashSet<>();
    private GameEdition edition;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public Developer getDeveloper() {
        return developer;
    }

    @Override
    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String getPegiRating() {
        return pegiRating;
    }

    @Override
    public void setPegiRating(String pegiRating) {
        this.pegiRating = pegiRating;
    }

    @Override
    public boolean isMultiplayer() {
        return multiplayer;
    }

    @Override
    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    @Override
    public Set<Genre> getGenres() {
        return genres;
    }

    @Override
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public Set<Console> getConsoles() {
        return consoles;
    }

    @Override
    public void setConsoles(Set<Console> consoles) {
        this.consoles = consoles;
    }

    @Override
    public GameEdition getEdition() {
        return edition;
    }

    @Override
    public void setEdition(GameEdition edition) {
        this.edition = edition;
    }

    /**
     * Compara este objeto con otro para ver si son iguales.
     * Se usa el ID y otros campos relevantes para determinar la igualdad.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game that = (Game) o;
        return getId() == that.getId() &&
                isMultiplayer() == that.isMultiplayer() &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getReleaseDate(), that.getReleaseDate()) &&
                Objects.equals(getPegiRating(), that.getPegiRating());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getReleaseDate(), getPegiRating(), isMultiplayer());
    }

    /**
     * Representación textual del objeto, útil para debugging.
     */
    @Override
    public String toString() {
        return "GameImpl{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", pegiRating='" + pegiRating + '\'' +
                ", multiplayer=" + multiplayer +
                '}';
    }
}