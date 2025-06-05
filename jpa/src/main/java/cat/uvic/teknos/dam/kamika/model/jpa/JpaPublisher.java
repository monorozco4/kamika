package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.Publisher;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * JPA entity representing a Publisher in the system.
 * Implements the {@link Publisher} interface.
 * Mapped to the "PUBLISHER" table in the database.
 */
@Entity
@Table(name = "PUBLISHER")
public class JpaPublisher implements Publisher {

    /**
     * Unique identifier for the publisher. Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLISHER_ID")
    private int id;

    /**
     * Name of the publisher. Cannot be null.
     */
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    /**
     * Country of origin of the publisher.
     */
    @Column(name = "COUNTRY", length = 50)
    private String country;

    /**
     * One-to-one bidirectional relationship with {@link JpaDeveloper}.
     */
    @OneToOne
    @JoinColumn(name = "DEVELOPER_ID", unique = true)
    private JpaDeveloper developer;

    /**
     * One-to-many relationship with {@link JpaGame}.
     * Contains all games published by this publisher.
     */
    @Getter
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaGame> publishedGames = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Developer getDeveloper() {
        return developer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDeveloper(Developer developer) {
        this.developer = (JpaDeveloper) developer;
    }

    /**
     * Checks equality based on ID, name, and country.
     *
     * @param o the object to compare
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCountry(), that.getCountry());
    }

    /**
     * Generates hash code based on ID, name, and country.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountry());
    }

    /**
     * Returns a string representation of the publisher.
     *
     * @return string with publisher details
     */
    @Override
    public String toString() {
        return "JpaPublisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
