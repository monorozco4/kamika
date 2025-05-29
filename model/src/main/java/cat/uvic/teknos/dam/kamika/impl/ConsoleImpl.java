package cat.uvic.teknos.dam.kamika.impl;

import cat.uvic.teknos.dam.kamika.Console;

import java.util.Objects;

/**
 * Implementación concreta de la interfaz Console.
 * Representa una consola de videojuegos con sus principales atributos.
 */
public class ConsoleImpl implements Console {

    private int id;
    private String name;
    private String manufacturer;
    private Integer releaseYear;

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
    public Integer getReleaseYear() {
        return releaseYear;
    }

    @Override
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Console)) return false;
        Console that = (Console) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getManufacturer(), that.getManufacturer()) &&
                Objects.equals(getReleaseYear(), that.getReleaseYear());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getManufacturer(), getReleaseYear());
    }

    /**
     * Representación textual del objeto, útil para debugging.
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