package cat.uvic.teknos.dam.kamika.impl;

import cat.uvic.teknos.dam.kamika.Game;
import cat.uvic.teknos.dam.kamika.Genre;
import cat.uvic.teknos.dam.kamika.GameGenre;

import java.util.Objects;

/**
 * Implementación concreta de la interfaz GameGenre.
 * Representa la relación muchos a muchos entre un juego y un género.
 */
public class GameGenreImpl implements GameGenre {

    private int gameId;
    private int genreId;

    private Game game;
    private Genre genre;

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(int id) {
        this.gameId = id;
    }

    @Override
    public int getGenreId() {
        return genreId;
    }

    @Override
    public void setGenreId(int id) {
        this.genreId = id;
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
    public Genre getGenre() {
        return genre;
    }

    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameGenre)) return false;

        GameGenre that = (GameGenre) o;

        return getGameId() == that.getGameId() &&
                getGenreId() == that.getGenreId();
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getGenreId());
    }

    /**
     * Representación textual del objeto, útil para debugging.
     */
    @Override
    public String toString() {
        return "GameGenreImpl{" +
                "gameId=" + gameId +
                ", genreId=" + genreId +
                '}';
    }
}