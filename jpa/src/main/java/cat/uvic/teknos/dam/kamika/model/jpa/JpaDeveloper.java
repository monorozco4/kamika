package cat.uvic.teknos.dam.kamika.model.jpa;

import cat.uvic.teknos.dam.kamika.model.Developer;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "DEVELOPER")
public class JpaDeveloper implements Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVELOPER_ID")
    private int id;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "COUNTRY", length = 100)
    private String country;

    @Column(name = "FOUNDATION_YEAR")
    private Integer foundationYear;

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
    public int getFoundationYear() {
        return foundationYear != null ? foundationYear : 0;
    }

    @Override
    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }
}