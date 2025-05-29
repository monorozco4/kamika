package cat.uvic.teknos.dam.kamika.impl;

import cat.uvic.teknos.dam.kamika.Publisher;
import cat.uvic.teknos.dam.kamika.Developer;

import java.util.Objects;

/**
 * Implementación concreta de la interfaz Publisher.
 * Representa a una empresa que publica videojuegos.
 */
public class PublisherImpl implements Publisher {

    private int id;
    private String name;
    private String country;
    private Developer developer;

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
    public Developer getDeveloper() {
        return developer;
    }

    @Override
    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    /**
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher)) return false;

        Publisher publisher = (Publisher) o;

        return getId() == publisher.getId() &&
                Objects.equals(getName(), publisher.getName()) &&
                Objects.equals(getCountry(), publisher.getCountry()) &&
                Objects.equals(getDeveloper(), publisher.getDeveloper());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountry(), getDeveloper());
    }

    /**
     * Representación textual del objeto, útil para debugging.
     */
    @Override
    public String toString() {
        return "PublisherImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", developer=" + (developer != null ? developer.getName() : "null") +
                '}';
    }
}