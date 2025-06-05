package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.GameEdition;
import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA Entity representing a specific edition of a video game.
 * <p>
 * Maps to the "GAMEEDITION" table in the database.
 * Follows professor's style: uses Lombok, uppercase column names.
 * </p>
 * <p>
 * Relationships:
 * - Many-to-one with Game (each edition belongs to one game).
 * </p>
 */
@Entity
@Table(name = "GAMEEDITION")
@Data
public class JpaGameEdition implements GameEdition {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "EDITION_NAME")
    private String editionName;

    @Column(name = "SPECIAL_CONTENT")
    private String specialContent;

    @Column(name = "PRICE")
    private double price;

    // Relation to Game (will be added later when Game entity is ready)
    private Game game;
}