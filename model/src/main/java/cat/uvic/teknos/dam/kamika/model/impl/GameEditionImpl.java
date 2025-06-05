package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.GameEdition;

import java.util.Objects;

/**
 * Concrete implementation of the GameEdition interface.
 * Represents a specific edition of a game (e.g., standard, deluxe, collector's edition).
 */
public class GameEditionImpl implements GameEdition {

    private Game game;
    private String editionName;
    private String specialContent;
    private double price;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String getEditionName() {
        return editionName;
    }

    @Override
    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    @Override
    public String getSpecialContent() {
        return specialContent;
    }

    @Override
    public void setSpecialContent(String specialContent) {
        this.specialContent = specialContent;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Compares this object with another to check equality.
     * Two editions are considered equal if they have the same name, special content and price.
     *
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEdition that)) return false;

        return Double.compare(that.getPrice(), getPrice()) == 0 &&
                Objects.equals(getEditionName(), that.getEditionName()) &&
                Objects.equals(getSpecialContent(), that.getSpecialContent());
    }

    /**
     * Generates a unique hash code based on the main attributes.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEditionName(), getSpecialContent(), getPrice());
    }

    /**
     * String representation of the object, useful for debugging.
     *
     * @return a string containing all relevant fields
     */
    @Override
    public String toString() {
        return "GameEditionImpl{" +
                "editionName='" + editionName + '\'' +
                ", specialContent='" + specialContent + '\'' +
                ", price=" + price +
                '}';
    }
}