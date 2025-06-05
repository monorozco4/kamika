package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Console;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

/**
 * JPA implementation of the Console interface.
 * Represents a video game console entity with JPA annotations for database mapping.
 * <p>
 * This entity has a many-to-many relationship with Game through the game_console table.
 * </p>
 */
@Entity
@Table(name = "CONSOLE")
public class JpaConsole implements Console {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSOLE_ID")
    private int id;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "MANUFACTURER", length = 100)
    private String manufacturer;

    @Column(name = "RELEASE_YEAR")
    private Integer releaseYear;

    /**
     * Many-to-many relationship with Game entity.
     * The relationship is mapped through the game_console join table.
     * -- SETTER --
     *  Sets the set of games available on this console.
     * <p>
     *
     * -- GETTER --
     *  Gets the set of games available on this console.
     * <p>
     /* @param games the set of games to associate with this console
     /* @return a set of games

     */
    @Getter
    @Setter
    @ManyToMany(mappedBy = "consoles", fetch = FetchType.LAZY)
    private Set<JpaGame> games = new HashSet<>();

    /**
     * Default constructor required by JPA.
     */
    public JpaConsole() {
    }

    /**
     * Constructor with parameters for creating a console with basic information.
     *
     * @param name the name of the console
     * @param manufacturer the manufacturer of the console
     * @param releaseYear the year the console was released
     */
    public JpaConsole(String name, String manufacturer, Integer releaseYear) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.releaseYear = releaseYear;
    }

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
     * Adds a game to this console's collection.
     *
     * @param game the game to add
     */
    public void addGame(JpaGame game) {
        this.games.add(game);
        game.getConsoles().add(this);
    }

    /**
     * Removes a game from this console's collection.
     *
     * @param game the game to remove
     */
    public void removeGame(JpaGame game) {
        this.games.remove(game);
        game.getConsoles().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaConsole that = (JpaConsole) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "JpaConsole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}