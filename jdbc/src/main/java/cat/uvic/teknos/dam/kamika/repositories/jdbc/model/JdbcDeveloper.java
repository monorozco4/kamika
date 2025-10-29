package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

public interface JdbcDeveloper {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getCountry();
    void setCountry(String country);

    Integer getFoundationYear();
    void setFoundationYear(Integer foundationYear);
}
