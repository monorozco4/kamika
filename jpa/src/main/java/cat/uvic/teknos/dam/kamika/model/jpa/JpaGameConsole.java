package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.GameConsole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * JPA implementation of the GameConsole interface.
 * Represents the many-to-many relationship between Game and Console with additional attributes.
 */
@Entity
@Table(name = "GAME_CONSOLE")
public class JpaGameConsole implements GameConsole {

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Column(name = "IS_EXCLUSIVE")
    private boolean exclusive;

    @Column(name = "RESOLUTION", length = 20)
    private String resolution;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "GAME_ID")
    private JpaGame game;

    @ManyToOne
    @MapsId("consoleId")
    @JoinColumn(name = "CONSOLE_ID")
    private JpaConsole console;

    @Override
    public int getGameId() {
        return id.getGameId();
    }

    @Override
    public void setGameId(int id) {
        this.id.setGameId(id);
    }

    @Override
    public int getConsoleId() {
        return id.getConsoleId();
    }

    @Override
    public void setConsoleId(int id) {
        this.id.setConsoleId(id);
    }

    @Override
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean isExclusive() {
        return exclusive;
    }

    @Override
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    @Override
    public String getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = (JpaGame) game;
        setGameId(game.getId());
    }

    @Override
    public Console getConsole() {
        return console;
    }

    @Override
    public void setConsole(Console console) {
        this.console = (JpaConsole) console;
        setConsoleId(console.getId());
    }

    /**
     * Embedded ID class representing the composite key of Game and Console.
     */
    @Embeddable
    @Getter
    @Setter
    public static class Id implements Serializable {
        @Column(name = "GAME_ID")
        private int gameId;

        @Column(name = "CONSOLE_ID")
        private int consoleId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id id)) return false;
            return gameId == id.gameId && consoleId == id.consoleId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(gameId, consoleId);
        }
    }
}
