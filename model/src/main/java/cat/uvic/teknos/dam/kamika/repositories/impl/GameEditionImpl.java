package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.GameEdition;

import java.util.Objects;

/**
 * Implementación concreta de la interfaz GameEdition.
 * Representa una edición específica de un juego (por ejemplo, edición estándar, deluxe, coleccionista...).
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
     * Compara este objeto con otro para ver si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEdition)) return false;

        GameEdition that = (GameEdition) o;

        return Double.compare(that.getPrice(), getPrice()) == 0 &&
                Objects.equals(getEditionName(), that.getEditionName()) &&
                Objects.equals(getSpecialContent(), that.getSpecialContent());
    }

    /**
     * Genera un código hash único basado en los atributos principales.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEditionName(), getSpecialContent(), getPrice());
    }

    /**
     * Representación textual del objeto, útil para debugging.
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