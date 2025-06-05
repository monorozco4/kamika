package cat.uvic.teknos.dam.kamika.model;

/**
 * Interface representing a video game publisher.
 * Defines the basic properties and behaviors of a publisher entity.
 * <p>
 * Related tables:
 * - GAME: One-to-many relationship (publisher can publish multiple games).
 * - DEVELOPER: Optional reference to a developer (may be self-published by a developer).
 * - PUBLISHING: Many-to-many relationship with Developer.
 * </p>
 */
public interface Publisher {

    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getCountry();
    void setCountry(String country);

    Developer getDeveloper();
    void setDeveloper(Developer developer);
}