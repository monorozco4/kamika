package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Console;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "CONSOLE")
public class JpaConsole implements Console {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSOLE_ID")
    private int id;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "MANUFACTURER", length = 100)
    private String manufacturer;

    @Column(name = "RELEASE_YEAR")
    private Integer releaseYear;

    @OneToMany(mappedBy = "console", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaGameConsole> gameConsoles = new HashSet<>();

    @Override
    public int getReleaseYear() {
        return releaseYear != null ? releaseYear : 0;
    }

    @Override
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}