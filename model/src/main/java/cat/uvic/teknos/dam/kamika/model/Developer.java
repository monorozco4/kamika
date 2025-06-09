package cat.uvic.teknos.dam.kamika.model;

/**
 * Interface representing a video game developer.
 * Defines the basic properties and behaviors of a developer entity.
 * <p>
 * Related tables:
 * - GAME: One-to-many relationship (developer creates multiple games).
 * - PUBLISHING: Many-to-many relationship with Publisher.
 * </p>
 */
public interface Developer {

    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getCountry();
    void setCountry(String country);

    int getFoundationYear();
    void setFoundationYear(int foundationYear);

}