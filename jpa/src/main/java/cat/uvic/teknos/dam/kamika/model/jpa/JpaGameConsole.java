package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "gameConsoleId")
@ToString
@Entity
@Table(name = "GAME_CONSOLE")
public class JpaGameConsole implements GameConsole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_CONSOLE_ID")
    private int gameConsoleId;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = false)
    private JpaGame game;

    @ManyToOne
    @JoinColumn(name = "CONSOLE_ID", nullable = false)
    private JpaConsole console;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Column(name = "EXCLUSIVE")
    private boolean exclusive;

    @Column(name = "RESOLUTION", length = 20)
    private String resolution;

    @Override
    public int getGameConsoleId() {
        return gameConsoleId;
    }

    @Override
    public void setGameConsoleId(int id) {
        this.gameConsoleId = id;
    }

    @Override
    public int getGameId() {
        return game != null ? game.getId() : 0;
    }

    @Override
    public void setGameId(int id) {
        if (game == null) {
            game = new JpaGame();
        }
        game.setId(id);
    }

    @Override
    public int getConsoleId() {
        return console != null ? console.getId() : 0;
    }

    @Override
    public void setConsoleId(int id) {
        if (console == null) {
            console = new JpaConsole();
        }
        console.setId(id);
    }

    @Override
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean isExclusive() {
        return exclusive;
    }

    @Override
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    @Override
    public String getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = (JpaGame) game;
    }

    @Override
    public Console getConsole() {
        return console;
    }

    @Override
    public void setConsole(Console console) {
        this.console = (JpaConsole) console;
    }
}