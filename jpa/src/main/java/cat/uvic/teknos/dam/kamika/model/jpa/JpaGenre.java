package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Game;
import jakarta.persistence.*;
import cat.uvic.teknos.dam.kamika.model.Genre;

import java.util.Set;

@Entity
@Table(name = "GENRE")
public class JpaGenre implements Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GENRE_ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    // Getters y setters

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {

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

    // Equals & HashCode (opcional pero recomendado para evitar problemas con Hibernate)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaGenre genre)) return false;

        return getName().equals(genre.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}