package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

import cat.uvic.teknos.dam.kamika.repositories.Game;

public interface JdbcGameEdition {
    Game getGame();
    void setGame(Game game);

    String getEditionName();
    void setEditionName(String editionName);

    String getSpecialContent();
    void setSpecialContent(String specialContent);

    double getPrice();
    void setPrice(double price);
}
