package cat.uvic.teknos.dam.kamika;

public interface Publisher {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getCountry();
    void setCountry(String country);

    Developer getDeveloper();
    void setDeveloper(Developer developer);
}