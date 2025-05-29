package cat.uvic.teknos.dam.kamika.impl;

import cat.uvic.teknos.dam.kamika.Genre;

import java.util.Objects;

/**
 * Implementación concreta de la interfaz Genre.
 * Representa un género de videojuego (por ejemplo: acción, aventura, rol...).
 */
public class GenreImpl implements Genre {

    private int id;
    private String name;
    private String description;

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

    /**
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;

        Genre genre = (Genre) o;

        return getId() == genre.getId() &&
                Objects.equals(getName(), genre.getName()) &&
                Objects.equals(getDescription(), genre.getDescription());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    /**
     * Representación textual del objeto, útil para debugging.
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