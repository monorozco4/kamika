package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

import cat.uvic.teknos.dam.kamika.model.Developer;

public interface JdbcPublisher {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getCountry();
    void setCountry(String country);

    Developer getDeveloper();
    void setDeveloper(Developer developer);
}
