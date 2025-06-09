package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Console;
import java.util.Objects;

/**
 * Concrete implementation of the Console interface.
 * Represents a video game console with its main attributes.
 * <p>
 * Related tables:
 * - Many-to-many relationship with Game.
 * </p>
 */
public class ConsoleImpl implements Console {

    private int id;
    private String name;
    private String manufacturer;
    private int releaseYear;

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
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public int getReleaseYear() {
        return releaseYear;
    }

    @Override
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Compares this object with another to check equality.
     * Two consoles are considered equal if they have the same ID, name,
     * manufacturer, and release year.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Console that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getManufacturer(), that.getManufacturer()) &&
                Objects.equals(getReleaseYear(), that.getReleaseYear());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getManufacturer(), getReleaseYear());
    }

    /**
     * String representation of the object, useful for debugging.
     *
     * @return a string containing all relevant fields
     */
    @Override
    public String toString() {
        return "ConsoleImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}