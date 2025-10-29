package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Developer;
import java.util.Objects;

/**
 * Concrete implementation of the Developer interface.
 * Represents a video game developer with its main attributes.
 * <p>
 * Related tables:
 * - GAME: One-to-many relationship (developer creates multiple games).
 * - PUBLISHING: Many-to-many relationship with Publisher.
 * </p>
 */
public class DeveloperImpl implements Developer {

    private int id;
    private String name;
    private String country;
    private int foundationYear;

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
    public int getFoundationYear() {
        return foundationYear;
    }

    @Override
    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    /**
     * Compares this object with another to check equality.
     * Two developers are considered equal if they have the same ID, name,
     * country, and foundation year.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getFoundationYear(), that.getFoundationYear());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountry(), getFoundationYear());
    }

    /**
     * String representation of the object, useful for debugging.
     *
     * @return a string containing all relevant fields
     */
    @Override
    public String toString() {
        return "DeveloperImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", foundationYear=" + foundationYear +
                '}';
    }
}