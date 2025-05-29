package cat.uvic.teknos.dam.kamika.impl;

import cat.uvic.teknos.dam.kamika.Game;
import cat.uvic.teknos.dam.kamika.Console;
import cat.uvic.teknos.dam.kamika.GameConsole;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Implementación concreta de la interfaz GameConsole.
 * Representa la relación entre un juego y una consola (lanzamiento, exclusividad, resolución...).
 */
public class GameConsoleImpl implements GameConsole {

    private int gameId;
    private int consoleId;
    private LocalDate releaseDate;
    private boolean isExclusive;
    private String resolution;

    private Game game;
    private Console console;

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
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameConsole)) return false;
        GameConsole that = (GameConsole) o;
        return getGameId() == that.getGameId() &&
                getConsoleId() == that.getConsoleId() &&
                isExclusive() == that.isExclusive() &&
                Objects.equals(getReleaseDate(), that.getReleaseDate()) &&
                Objects.equals(getResolution(), that.getResolution());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getConsoleId(), getReleaseDate(), isExclusive(), getResolution());
    }

    /**
     * Representación textual del objeto, útil para debugging.
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