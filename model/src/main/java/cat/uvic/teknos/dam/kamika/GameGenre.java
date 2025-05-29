package cat.uvic.teknos.dam.kamika;

public interface GameGenre {

    // Getters y Setters para las claves primarias

    int getGameId();
    void setGameId(int id);

    int getGenreId();
    void setGenreId(int id);

    // Métodos para acceder a las entidades completas (opcional)

    Game getGame();
    void setGame(Game game);

    Genre getGenre();
    void setGenre(Genre genre);
}