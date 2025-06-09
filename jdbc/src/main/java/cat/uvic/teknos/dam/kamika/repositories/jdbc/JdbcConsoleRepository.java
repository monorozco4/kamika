package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.repositories.ConsoleRepository;
import cat.uvic.teknos.dam.kamika.model.impl.ConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.util.*;

/**
 * JDBC implementation of the Console repository.
 * Follows best practices for connection handling and exception management.
 */
public class JdbcConsoleRepository implements ConsoleRepository {

    private final DataSource dataSource;

    public JdbcConsoleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Console> findById(int id) {
        if (id <= 0) {
            throw new CrudException("Invalid console ID: " + id);
        }

        String sql = "SELECT * FROM CONSOLE WHERE CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding console by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Console save(Console console) {
        // Si es nuevo (id == 0), inserta; si ya tiene ID, actualiza
        if (console.getId() == 0) {
            return insert(console);
        } else {
            return update(console);
        }
    }

    private Console insert(Console console) {
        String sql = "INSERT INTO CONSOLE (NAME, MANUFACTURER, RELEASE_YEAR) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, console.getName());
            stmt.setString(2, console.getManufacturer());
            stmt.setObject(3, console.getReleaseYear());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Failed to insert console, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    console.setId(generatedKeys.getInt(1));
                }
            }

            return console;

        } catch (SQLException e) {
            throw new CrudException("Error inserting console", e);
        }
    }

    private Console update(Console console) {
        String sql = "UPDATE CONSOLE SET NAME = ?, MANUFACTURER = ?, RELEASE_YEAR = ? WHERE CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, console.getName());
            stmt.setString(2, console.getManufacturer());
            stmt.setInt(3, console.getReleaseYear());
            stmt.setInt(4, console.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error updating console: no rows affected");
            }

            return console;

        } catch (SQLException e) {
            throw new CrudException("Error updating console", e);
        }
    }

    @Override
    public void delete(Console console) {
        String sql = "DELETE FROM CONSOLE WHERE CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, console.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new CrudException("Error deleting console", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM CONSOLE WHERE CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting console by ID", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM CONSOLE";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting consoles", e);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM CONSOLE WHERE CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking if console exists", e);
        }
    }

    @Override
    public Set<Console> findAll() {
        Set<Console> consoles = new HashSet<>();
        String sql = "SELECT * FROM CONSOLE";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                consoles.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving all consoles", e);
        }
        return consoles;
    }

    /**
     * Maps a ResultSet row to a Console entity.
     *
     * @param rs the result set to map
     * @return the mapped Console object
     * @throws SQLException if a database access error occurs
     */
    private Console mapToEntity(ResultSet rs) throws SQLException {
        Console console = new ConsoleImpl();
        console.setId(rs.getInt("CONSOLE_ID"));
        console.setName(rs.getString("NAME"));
        console.setManufacturer(rs.getString("MANUFACTURER"));
        console.setReleaseYear(rs.getObject("RELEASE_YEAR", Integer.class));
        return console;
    }
}