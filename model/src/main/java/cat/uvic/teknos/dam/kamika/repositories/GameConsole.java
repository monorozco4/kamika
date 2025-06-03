package cat.uvic.teknos.dam.kamika.model;

import java.time.LocalDate;

public interface GameConsole {

    // Getters y Setters para las claves primarias

    int getGameId();
    void setGameId(int id);

    int getConsoleId();
    void setConsoleId(int id);

    // Getters y Setters para otros atributos

    LocalDate getReleaseDate();
    void setReleaseDate(LocalDate releaseDate);

    boolean isExclusive();
    void setExclusive(boolean exclusive);

    String getResolution();
    void setResolution(String resolution);

    // Métodos para acceder a las entidades completas (opcional)

    Game getGame();
    void setGame(Game game);

    Console getConsole();
    void setConsole(Console console);
}