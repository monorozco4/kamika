package cat.uvic.teknos.dam.kamika.model;

/**
 * Interface representing a specific edition of a game.
 * Defines the basic properties and behaviors of a game edition entity.
 * <p>
 * Related tables:
 * - GAME: Many-to-one relationship (each edition belongs to one game).
 * </p>
 */

public interface GameEdition {

    Game getGame();
    void setGame(Game game);

    String getEditionName();
    void setEditionName(String editionName);

    String getSpecialContent();
    void setSpecialContent(String specialContent);

    double getPrice();
    void setPrice(double price);
}