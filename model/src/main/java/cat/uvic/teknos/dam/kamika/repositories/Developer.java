package cat.uvic.teknos.dam.kamika.model;

public interface Developer {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getCountry();
    void setCountry(String country);

    Integer getFoundationYear();
    void setFoundationYear(Integer foundationYear);
}