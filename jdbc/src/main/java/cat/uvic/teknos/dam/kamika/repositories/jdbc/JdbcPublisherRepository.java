package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.repositories.PublisherRepository;
import cat.uvic.teknos.dam.kamika.model.impl.PublisherImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.util.*;

/**
 * JDBC implementation of the Publisher repository.
 * Follows best practices for connection handling and exception management.
 */
public class JdbcPublisherRepository implements PublisherRepository {

    private final DataSource dataSource;

    public JdbcPublisherRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public Optional<Publisher> findById(int id) {
        String sql = "SELECT * FROM PUBLISHER WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding publisher by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Publisher save(Publisher publisher) {
        if (publisher.getId() == 0) {
            return insert(publisher);
        } else {
            return update(publisher);
        }
    }

    private Publisher insert(Publisher publisher) {
        String sql = "INSERT INTO PUBLISHER (NAME, COUNTRY, DEVELOPERID) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, publisher.getName());
            stmt.setString(2, publisher.getCountry());
            stmt.setObject(3, publisher.getDeveloper() != null ? publisher.getDeveloper().getId() : null);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Failed to insert publisher, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    publisher.setId(generatedKeys.getInt(1));
                }
            }

            return publisher;

        } catch (SQLException e) {
            throw new CrudException("Error inserting publisher", e);
        }
    }

    private Publisher update(Publisher publisher) {
        String sql = "UPDATE PUBLISHER SET NAME = ?, COUNTRY = ?, DEVELOPERID = ? WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, publisher.getName());
            stmt.setString(2, publisher.getCountry());
            stmt.setObject(3, publisher.getDeveloper() != null ? publisher.getDeveloper().getId() : null);
            stmt.setInt(4, publisher.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error updating publisher: no rows affected");
            }

            return publisher;

        } catch (SQLException e) {
            throw new CrudException("Error updating publisher", e);
        }
    }

    @Override
    public void delete(Publisher publisher) {
        String sql = "DELETE FROM PUBLISHER WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, publisher.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new CrudException("Error deleting publisher", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM PUBLISHER WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting publisher by ID", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM PUBLISHER";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting publishers", e);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM PUBLISHER WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new CrudException("Error checking if publisher exists", e);
        }
    }

    @Override
    public long countByCountryIgnoreCase(String country) {
        String sql = "SELECT COUNT(*) FROM PUBLISHER WHERE LOWER(COUNTRY) = LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, country);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting publishers by country", e);
        }
        return 0;
    }

    /**
     * Maps a ResultSet row to a Publisher entity.
     *
     * @param rs the result set to map
     * @return the mapped Publisher object
     * @throws SQLException if a database access error occurs
     */
    private Publisher mapToEntity(ResultSet rs) throws SQLException {
        Publisher publisher = new PublisherImpl();
        publisher.setId(rs.getInt("ID"));
        publisher.setName(rs.getString("NAME"));
        publisher.setCountry(rs.getString("COUNTRY"));

        // Si hay relación directa con Developer (self-publishing)
        Integer developerId = rs.getObject("DEVELOPERID", Integer.class);
        if (developerId != null && developerId > 0) {
            // Usaría JDBCPublisherRepository para cargarlo si es necesario
            // Por ahora lo dejamos como solo ID
        }

        return publisher;
    }
}