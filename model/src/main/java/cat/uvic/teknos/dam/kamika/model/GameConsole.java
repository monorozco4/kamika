package cat.uvic.teknos.dam.kamika.model;

import java.time.LocalDate;

/**
 * Manages the many-to-many relationship between Game and Console with additional attributes.
 * <p>
 * This interface represents the full relationship including metadata such as release date,
 * exclusivity status, and resolution support.
 * </p>
 */
public interface GameConsole {

    /**
     * Gets the game ID.
     * @return the game ID
     */
    int getGameId();

    /**
     * Sets the game ID.
     * @param id the game ID to set
     */
    void setGameId(int id);

    /**
     * Gets the console ID.
     * @return the console ID
     */
    int getConsoleId();

    /**
     * Sets the console ID.
     * @param id the console ID to set
     */
    void setConsoleId(int id);

    // Additional attributes

    /**
     * Gets the release date of the game on this console.
     * @return the release date
     */
    LocalDate getReleaseDate();

    /**
     * Sets the release date of the game on this console.
     * @param releaseDate the release date to set
     */
    void setReleaseDate(LocalDate releaseDate);

    /**
     * Checks if the game is exclusive to this console.
     * @return true if exclusive, false otherwise
     */
    boolean isExclusive();

    /**
     * Sets whether the game is exclusive to this console.
     * @param exclusive true if exclusive, false otherwise
     */
    void setExclusive(boolean exclusive);

    /**
     * Gets the supported resolution for the game on this console.
     * @return the resolution string (e.g., "1080p")
     */
    String getResolution();

    /**
     * Sets the supported resolution for the game on this console.
     * @param resolution the resolution string to set
     */
    void setResolution(String resolution);

    // Optional references to full entities

    /**
     * Gets the associated Game entity.
     * @return the Game object
     */
    Game getGame();

    /**
     * Sets the associated Game entity.
     * @param game the Game object to set
     */
    void setGame(Game game);

    /**
     * Gets the associated Console entity.
     * @return the Console object
     */
    Console getConsole();

    /**
     * Sets the associated Console entity.
     * @param console the Console object to set
     */
    void setConsole(Console console);
}