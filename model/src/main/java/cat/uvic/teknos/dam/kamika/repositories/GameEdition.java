package cat.uvic.teknos.dam.kamika.model;

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