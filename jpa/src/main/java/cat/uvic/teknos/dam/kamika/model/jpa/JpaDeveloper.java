package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.Publisher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DEVELOPER")
public class JpaDeveloper implements Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVELOPER_ID")
    private int id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "COUNTRY", length = 50)
    private String country;

    @Column(name = "FOUNDATION_YEAR")
    private Integer foundationYear;

    // Métodos específicos de JPA
    @Getter
    @Setter
    @OneToOne(mappedBy = "developer", cascade = CascadeType.ALL)
    private JpaPublisher publisher;

    @Getter
    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaGame> games = new HashSet<>();

    // Implementación de los métodos de la interfaz Developer
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
    public Integer getFoundationYear() {
        return foundationYear;
    }

    @Override
    public void setFoundationYear(Integer foundationYear) {
        this.foundationYear = foundationYear;
    }

    public void addGame(Game game) {
        games.add((JpaGame) game);
        game.setDeveloper(this);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.setDeveloper(null);
    }

    // equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getFoundationYear(), that.getFoundationYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountry(), getFoundationYear());
    }

    @Override
    public String toString() {
        return "JpaDeveloper{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", foundationYear=" + foundationYear +
                '}';
    }
}