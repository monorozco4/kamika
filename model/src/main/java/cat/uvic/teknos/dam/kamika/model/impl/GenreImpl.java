package cat.uvic.teknos.dam.kamika.repositories.impl;

import cat.uvic.teknos.dam.kamika.repositories.Genre;
import cat.uvic.teknos.dam.kamika.repositories.Game;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Concrete implementation of the Genre interface.
 * Represents a video game genre (e.g., action, adventure, RPG...).
 * <p>
 * This class avoids circular references in toString() to prevent infinite loops
 * when printing related entities like Game.
 * </p>
 */
public class GenreImpl implements Genre {

    private int id;
    private String name;
    private String description;
    private Set<Game> games = new HashSet<>();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Set<Game> getGames() {
        return games;
    }

    @Override
    public void setGames(Set<Game> games) {
        this.games = games;
    }

    /**
     * Compares this object with another to check equality.
     * Two genres are considered equal if they have the same ID, name, and description.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;

        return getId() == genre.getId() &&
                Objects.equals(getName(), genre.getName()) &&
                Objects.equals(getDescription(), genre.getDescription());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    /**
     * String representation of the object, useful for debugging.
     * Does not include related collections or entities to avoid infinite recursion.
     *
     * @return a string containing key fields only
     */
    @Override
    public String toString() {
        return "GenreImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}