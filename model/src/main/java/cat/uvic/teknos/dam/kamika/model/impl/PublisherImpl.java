package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.model.Developer;

import java.util.Objects;

/**
 * Concrete implementation of the Publisher interface.
 * Represents a company that publishes video games.
 * <p>
 * This class avoids circular references in toString() to prevent infinite loops
 * when printing related entities like Developer or Game.
 * </p>
 */
public class PublisherImpl implements Publisher {

    private int id;
    private String name;
    private String country;
    private Developer developer;

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
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Developer getDeveloper() {
        return developer;
    }

    @Override
    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    /**
     * Compares this object with another to check equality.
     * Two publishers are considered equal if they have the same ID, name,
     * country, and linked developer.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher publisher)) return false;

        return getId() == publisher.getId() &&
                Objects.equals(getName(), publisher.getName()) &&
                Objects.equals(getCountry(), publisher.getCountry()) &&
                Objects.equals(getDeveloper(), publisher.getDeveloper());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountry(), getDeveloper());
    }

    /**
     * String representation of the object, useful for debugging.
     * Does not include full related entity details to avoid infinite recursion.
     *
     * @return a string containing key fields only
     */
    @Override
    public String toString() {
        return "PublisherImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", developer=" + (developer != null ? developer.getName() : "null") +
                '}';
    }
}