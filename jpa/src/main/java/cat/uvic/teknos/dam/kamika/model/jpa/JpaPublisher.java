package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "PUBLISHER")
public class JpaPublisher implements Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLISHER_ID")
    private int id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "COUNTRY", length = 100)
    private String country;

    @ManyToOne
    @JoinColumn(name = "DEVELOPER_ID")
    private JpaDeveloper developer;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Developer getDeveloper() {
        return developer;
    }

    @Override
    public void setDeveloper(Developer developer) {
        this.developer = (JpaDeveloper) developer;
    }
}