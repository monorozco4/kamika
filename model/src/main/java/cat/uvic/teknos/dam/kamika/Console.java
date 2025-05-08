package cat.uvic.teknos.dam.kamika;

public interface Console {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    String getManufacturer();
    void setManufacturer(String manufacturer);

    Integer getReleaseYear();
    void setReleaseYear(Integer releaseYear);
}