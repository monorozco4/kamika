package cat.uvic.teknos.dam.kamika.model;

/**
 * Interface representing a video game console.
 * Defines the basic properties and behaviors of a console entity.
 * <p>
 * Related tables:
 * - Many-to-many relationship with Game.
 * </p>
 */
public interface Console {

    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getManufacturer();
    void setManufacturer(String manufacturer);

    int getReleaseYear();
    void setReleaseYear(int releaseYear);
}