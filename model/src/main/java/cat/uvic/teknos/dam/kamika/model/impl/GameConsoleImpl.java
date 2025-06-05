package cat.uvic.teknos.dam.kamika.repositories.impl;

import cat.uvic.teknos.dam.kamika.repositories.Game;
import cat.uvic.teknos.dam.kamika.repositories.Console;
import cat.uvic.teknos.dam.kamika.repositories.GameConsole;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Concrete implementation of the GameConsole interface.
 * Represents the many-to-many relationship between Game and Console with additional attributes.
 * <p>
 * Related tables:
 * - GAME: One side of the relationship.
 * - CONSOLE: Other side of the relationship.
 * </p>
 */
public class GameConsoleImpl implements GameConsole {

    private int gameId;
    private int consoleId;
    private LocalDate releaseDate;
    private boolean isExclusive;
    private String resolution;

    private Game game;
    private Console console;

    // Getters and setters for primary keys

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(int id) {
        this.gameId = id;
    }

    @Override
    public int getConsoleId() {
        return consoleId;
    }

    @Override
    public void setConsoleId(int id) {
        this.consoleId = id;
    }

    // Getters and setters for additional attributes

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
        return isExclusive;
    }

    @Override
    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }

    @Override
    public String getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    // Getters and setters for full entity references

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Console getConsole() {
        return console;
    }

    @Override
    public void setConsole(Console console) {
        this.console = console;
    }

    /**
     * Compares this object with another to check equality.
     * Two GameConsole instances are considered equal if they have the same game ID, console ID,
     * release date, exclusivity flag, and resolution.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameConsole that)) return false;
        return getGameId() == that.getGameId() &&
                getConsoleId() == that.getConsoleId() &&
                isExclusive() == that.isExclusive() &&
                Objects.equals(getReleaseDate(), that.getReleaseDate()) &&
                Objects.equals(getResolution(), that.getResolution());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getConsoleId(), getReleaseDate(), isExclusive(), getResolution());
    }

    /**
     * String representation of the object, useful for debugging.
     *
     * @return a string containing all relevant fields
     */
    @Override
    public String toString() {
        return "GameConsoleImpl{" +
                "gameId=" + gameId +
                ", consoleId=" + consoleId +
                ", releaseDate=" + releaseDate +
                ", isExclusive=" + isExclusive +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}