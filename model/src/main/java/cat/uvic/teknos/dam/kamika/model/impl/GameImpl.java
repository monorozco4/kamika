package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Concrete implementation of the Game interface.
 * Contains the main attributes of a video game, such as title, release date, etc.
 * <p>
 * This class avoids circular references in toString() to prevent infinite loops
 * when printing related entities like Developer, Publisher, Genre, or Console.
 * </p>
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
     * Compares this object with another to check equality.
     * Two games are considered equal if they have the same ID, title,
     * release date, PEgi rating, and multiplayer flag.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game that)) return false;
        return getId() == that.getId() &&
                isMultiplayer() == that.isMultiplayer() &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getReleaseDate(), that.getReleaseDate()) &&
                Objects.equals(getPegiRating(), that.getPegiRating());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getReleaseDate(), getPegiRating(), isMultiplayer());
    }

    /**
     * String representation of the object, useful for debugging.
     * Does not include collections or entity references to avoid infinite recursion.
     *
     * @return a string containing key fields only
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