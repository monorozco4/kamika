package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "GAME_EDITION")
public class JpaGameEdition implements GameEdition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EDITION_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "GAME_ID", nullable = false)
    private JpaGame game;

    @Column(name = "EDITION_NAME", length = 100)
    private String editionName;

    @Column(name = "SPECIAL_CONTENT", length = 255)
    private String specialContent;

    @Column(name = "PRICE")
    private double price;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        if (game instanceof JpaGame) {
            this.game = (JpaGame) game;
        } else {
            throw new IllegalArgumentException("game debe ser instancia de JpaGame");
        }
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

    @Override
    public LocalDate getReleaseDate() {
        return null;
    }

    @Override
    public void setReleaseDate(LocalDate releaseDate) {

    }
}