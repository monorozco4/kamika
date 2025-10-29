package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "GAME")
public class JpaGame implements Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_ID")
    private int id;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "DEVELOPER_ID")
    private JpaDeveloper developer;

    @ManyToOne
    @JoinColumn(name = "PUBLISHER_ID")
    private JpaPublisher publisher;

    @Column(name = "PEGI_RATING", length = 10)
    private String pegiRating;

    @Column(name = "MULTIPLAYER")
    private boolean multiplayer;

    @ManyToMany
    @JoinTable(
            name = "GAME_GENRE",
            joinColumns = @JoinColumn(name = "GAME_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
    )
    private Set<JpaGenre> genres;

    @ManyToMany
    @JoinTable(
            name = "GAME_CONSOLE",
            joinColumns = @JoinColumn(name = "GAME_ID"),
            inverseJoinColumns = @JoinColumn(name = "CONSOLE_ID")
    )
    private Set<JpaConsole> consoles;

    @Override
    public int getId() { return id; }
    @Override
    public void setId(int id) { this.id = id; }

    @Override
    public String getTitle() { return title; }
    @Override
    public void setTitle(String title) { this.title = title; }

    @Override
    public LocalDate getReleaseDate() { return releaseDate; }
    @Override
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    @Override
    public Developer getDeveloper() { return developer; }
    @Override
    public void setDeveloper(Developer developer) { this.developer = (JpaDeveloper) developer; }

    @Override
    public Publisher getPublisher() { return publisher; }
    @Override
    public void setPublisher(Publisher publisher) { this.publisher = (JpaPublisher) publisher; }

    @Override
    public String getPegiRating() { return pegiRating; }
    @Override
    public void setPegiRating(String pegiRating) { this.pegiRating = pegiRating; }

    @Override
    public boolean isMultiplayer() { return multiplayer; }
    @Override
    public void setMultiplayer(boolean multiplayer) { this.multiplayer = multiplayer; }

    @Override
    public Set<Genre> getGenres() {
        return genres == null ? null : genres.stream().map(g -> (Genre) g).collect(Collectors.toSet());
    }
    @Override
    public void setGenres(Set<Genre> genres) {
        this.genres = genres == null ? null : genres.stream().map(g -> (JpaGenre) g).collect(Collectors.toSet());
    }

    @Override
    public Set<Console> getConsoles() {
        return consoles == null ? null : consoles.stream().map(c -> (Console) c).collect(Collectors.toSet());
    }
    @Override
    public void setConsoles(Set<Console> consoles) {
        this.consoles = consoles == null ? null : consoles.stream().map(c -> (JpaConsole) c).collect(Collectors.toSet());
    }

    // Implementación vacía para cumplir la interfaz Game
    @Override
    public GameEdition getEdition() {
        return null;
    }

    @Override
    public void setEdition(GameEdition edition) {
        // No hacer nada
    }
}