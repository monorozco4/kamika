package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.repositories.DeveloperRepository;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.util.*;

/**
 * JDBC implementation of the Developer repository.
 * Follows best practices for connection handling and exception management.
 */
public class JdbcDeveloperRepository implements DeveloperRepository {

    private final DataSource dataSource;

    public JdbcDeveloperRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Developer> findById(int id) {
        if (id <= 0) {
            throw new CrudException("Invalid developer ID: " + id);
        }

        String sql = "SELECT * FROM DEVELOPER WHERE DEVELOPER_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding developer by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Developer save(Developer developer) {
        Objects.requireNonNull(developer, "Developer cannot be null");

        if (developer.getName() == null || developer.getName().trim().isEmpty()) {
            throw new CrudException("Developer name cannot be null or empty");
        }

        if (developer.getId() == 0) {
            return insert(developer);
        } else {
            return update(developer);
        }
    }

    private Developer insert(Developer developer) {
        String sql = "INSERT INTO DEVELOPER (NAME, COUNTRY, FOUNDATION_YEAR) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, developer.getName());
            stmt.setString(2, developer.getCountry());
            stmt.setObject(3, developer.getFoundationYear());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Failed to insert developer, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    developer.setId(generatedKeys.getInt(1));
                }
            }

            return developer;

        } catch (SQLException e) {
            throw new CrudException("Error inserting developer", e);
        }
    }

    private Developer update(Developer developer) {
        String sql = "UPDATE DEVELOPER SET NAME = ?, COUNTRY = ?, FOUNDATION_YEAR = ? WHERE DEVELOPER_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, developer.getName());
            stmt.setString(2, developer.getCountry());
            stmt.setObject(3, developer.getFoundationYear());
            stmt.setInt(4, developer.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error updating developer: no rows affected");
            }

            return developer;

        } catch (SQLException e) {
            throw new CrudException("Error updating developer", e);
        }
    }

    @Override
    public void delete(Developer developer) {
        String sql = "DELETE FROM DEVELOPER WHERE DEVELOPER_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, developer.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new CrudException("Error deleting developer", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM DEVELOPER WHERE DEVELOPER_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting developer by ID", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM DEVELOPER";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting developers", e);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM DEVELOPER WHERE DEVELOPER_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking if developer exists", e);
        }
    }

    @Override
    public long countByCountryIgnoreCase(String country) {
        String sql = "SELECT COUNT(*) FROM DEVELOPER WHERE LOWER(COUNTRY) = LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, country);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting developers by country", e);
        }
        return 0;
    }

    @Override
    public Set<Developer> findAll() {
        Set<Developer> developers = new HashSet<>();
        String sql = "SELECT * FROM DEVELOPER";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                developers.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving all developers", e);
        }
        return developers;
    }

    /**
     * Maps a ResultSet row to a Developer entity.
     *
     * @param rs the result set to map
     * @return the mapped Developer object
     * @throws SQLException if a database access error occurs
     */
    private Developer mapToEntity(ResultSet rs) throws SQLException {
        Developer developer = new DeveloperImpl();
        developer.setId(rs.getInt("DEVELOPER_ID"));
        developer.setName(rs.getString("NAME"));
        developer.setCountry(rs.getString("COUNTRY"));
        developer.setFoundationYear(rs.getObject("FOUNDATION_YEAR", Integer.class));
        return developer;
    }
}