package cat.uvic.teknos.dam.kamika.impl;

import cat.uvic.teknos.dam.kamika.Developer;

import java.util.Objects;

/**
 * Implementación concreta de la interfaz Developer.
 * Representa a un desarrollador de videojuegos con sus principales atributos.
 */
public class DeveloperImpl implements Developer {

    private int id;
    private String name;
    private String country;
    private Integer foundationYear;

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

    /**
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer)) return false;
        Developer that = (Developer) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getFoundationYear(), that.getFoundationYear());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountry(), getFoundationYear());
    }

    /**
     * Representación textual del objeto, útil para debugging.
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