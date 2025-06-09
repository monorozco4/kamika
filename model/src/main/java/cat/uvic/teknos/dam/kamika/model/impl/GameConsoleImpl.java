package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.GameConsole;

import java.time.LocalDate;

public class GameConsoleImpl implements GameConsole {
    private int gameConsoleId;
    private int gameId;
    private int consoleId;
    private LocalDate releaseDate;
    private boolean exclusive;
    private String resolution;
    private Game game;
    private Console console;

    @Override
    public int getGameConsoleId() {
        return gameConsoleId;
    }

    @Override
    public void setGameConsoleId(int id) {
        this.gameConsoleId = id;
    }

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
}