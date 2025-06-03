package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.repositories.Game;
import cat.uvic.teknos.dam.kamika.repositories.GameRepository;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * JDBC implementation of the GameRepository interface.
 * Follows best practices for connection handling and exception management.
 */
public class JdbcGameRepository implements GameRepository {

    private final DataSource dataSource;

    public JdbcGameRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public Optional<Game> findById(int id) {
        String sql = "SELECT * FROM GAME WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding game by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Game save(Game game) {
        String sql = "INSERT INTO GAME (TITLE, RELEASEDATE, PEGIRATING, MULTIPLAYER) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, game.getTitle());
            stmt.setObject(2, game.getReleaseDate());
            stmt.setString(3, game.getPegiRating());
            stmt.setBoolean(4, game.isMultiplayer());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Failed to insert game, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setId(generatedKeys.getInt(1));
                }
            }

            return game;

        } catch (SQLException e) {
            throw new CrudException("Error saving game", e);
        }
    }

    @Override
    public void delete(Game game) {
        String sql = "DELETE FROM GAME WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, game.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new CrudException("Error deleting game", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM GAME WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting game by ID", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM GAME";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting games", e);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM GAME WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking if game exists", e);
        }
    }

    /**
     * Maps a ResultSet row to a Game entity.
     *
     * @param rs the result set to map
     * @return the mapped Game object
     * @throws SQLException if a database access error occurs
     */
    private Game mapToEntity(ResultSet rs) throws SQLException {
        Game game = new GameImpl();
        game.setId(rs.getInt("ID"));
        game.setTitle(rs.getString("TITLE"));

        // ✅ Conversión segura de java.sql.Date a java.time.LocalDate
        game.setReleaseDate(rs.getDate("RELEASEDATE").toLocalDate());

        game.setPegiRating(rs.getString("PEGIRATING"));

        // Si isMultiplayer() es solo getter, puedes hacer esto si usas GameImpl
        if (game instanceof GameImpl) {
            ((GameImpl) game).setMultiplayer(rs.getBoolean("MULTIPLAYER"));
        }

        return game;
    }
}