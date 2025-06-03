package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.repositories.GameConsole;
import cat.uvic.teknos.dam.kamika.repositories.GameConsoleRepository;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * JDBC implementation of the GameConsole repository.
 * Manages the many-to-many relationship between Game and Console with additional attributes.
 * <p>
 * This class implements all operations defined in the GameConsoleRepository interface,
 * including insert, update, delete, find by composite key, count, and check existence.
 * </p>
 */
public class JdbcGameConsoleRepository implements GameConsoleRepository {

    private final DataSource dataSource;

    /**
     * Constructs a new repository using the provided data source.
     *
     * @param dataSource the data source to use for database connections
     */
    public JdbcGameConsoleRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    /**
     * Finds a record by its composite key: gameId and consoleId.
     *
     * @param gameId   the ID of the game
     * @param consoleId the ID of the console
     * @return an Optional containing the relation if found, empty otherwise
     */
    @Override
    public <GameConsole> Optional<GameConsole> findById(int gameId, int consoleId) {
        String sql = "SELECT * FROM RELEASEDON WHERE GAMEID = ? AND CONSOLEID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, consoleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of((GameConsole) mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding game-console relation by composite key", e);
        }
        return Optional.empty();
    }

    /**
     * Saves or updates a game-console relation in the database.
     *
     * @param gameConsole the relation to save
     * @return the saved GameConsole object
     */
    @Override
    public <GameConsole> GameConsole save(GameConsole gameConsole) {
        // Assuming both IDs are set
        int gameId = ((cat.uvic.teknos.dam.kamika.repositories.GameConsole) gameConsole).getGameId();
        int consoleId = ((cat.uvic.teknos.dam.kamika.repositories.GameConsole) gameConsole).getConsoleId();

        if (!existsById(gameId, consoleId)) {
            insert((cat.uvic.teknos.dam.kamika.repositories.GameConsole) gameConsole);
        } else {
            update((cat.uvic.teknos.dam.kamika.repositories.GameConsole) gameConsole);
        }
        return gameConsole;
    }

    /**
     * Inserts a new game-console relation into the database.
     *
     * @param gameConsole the relation to insert
     */
    private void insert(GameConsole gameConsole) {
        String sql = "INSERT INTO RELEASEDON (GAMEID, CONSOLEID, RELEASEDATE, EXCLUSIVE, RESOLUTION) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameConsole.getGameId());
            stmt.setInt(2, gameConsole.getConsoleId());
            stmt.setObject(3, gameConsole.getReleaseDate());
            stmt.setBoolean(4, gameConsole.isExclusive());
            stmt.setString(5, gameConsole.getResolution());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Failed to insert game-console relation, no rows affected.");
            }

        } catch (SQLException e) {
            throw new CrudException("Error inserting game-console relation", e);
        }
    }

    /**
     * Updates an existing game-console relation in the database.
     *
     * @param gameConsole the relation to update
     */
    private void update(GameConsole gameConsole) {
        String sql = "UPDATE RELEASEDON SET RELEASEDATE = ?, EXCLUSIVE = ?, RESOLUTION = ? WHERE GAMEID = ? AND CONSOLEID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, gameConsole.getReleaseDate());
            stmt.setBoolean(2, gameConsole.isExclusive());
            stmt.setString(3, gameConsole.getResolution());
            stmt.setInt(4, gameConsole.getGameId());
            stmt.setInt(5, gameConsole.getConsoleId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error updating game-console relation: no rows affected");
            }

        } catch (SQLException e) {
            throw new CrudException("Error updating game-console relation", e);
        }
    }

    /**
     * Deletes a game-console relation by entity.
     *
     * @param gameConsole the relation to delete
     */
    @Override
    public <GameConsole> void delete(GameConsole gameConsole) {
        int gameId = ((cat.uvic.teknos.dam.kamika.repositories.GameConsole) gameConsole).getGameId();
        int consoleId = ((cat.uvic.teknos.dam.kamika.repositories.GameConsole) gameConsole).getConsoleId();

        deleteById(gameId, consoleId);
    }

    /**
     * Deletes a game-console relation by its composite key.
     *
     * @param gameId   the ID of the game
     * @param consoleId the ID of the console
     * @return true if the relation was deleted, false if it did not exist
     */
    @Override
    public boolean deleteById(int gameId, int consoleId) {
        String sql = "DELETE FROM RELEASEDON WHERE GAMEID = ? AND CONSOLEID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, consoleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting game-console relation by composite key", e);
        }
    }

    /**
     * Counts how many relations exist in the RELEASEDON table.
     *
     * @return the total number of records as a long
     */
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM RELEASEDON";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting game-console relations", e);
        }
        return 0;
    }

    /**
     * Checks whether a game-console relation exists by composite key.
     *
     * @param gameId   the ID of the game
     * @param consoleId the ID of the console
     * @return true if the relation exists, false otherwise
     */
    @Override
    public boolean existsById(int gameId, int consoleId) {
        String sql = "SELECT 1 FROM RELEASEDON WHERE GAMEID = ? AND CONSOLEID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, consoleId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking existence of game-console relation", e);
        }
    }

    /**
     * Maps a ResultSet row to a GameConsole entity.
     *
     * @param rs the result set to map
     * @return the mapped GameConsole object
     * @throws SQLException if a database access error occurs
     */
    private cat.uvic.teknos.dam.kamika.repositories.GameConsole mapToEntity(ResultSet rs) throws SQLException {
        GameConsole relation = new GameConsoleImpl();
        relation.setGameId(rs.getInt("GAMEID"));
        relation.setConsoleId(rs.getInt("CONSOLEID"));
        Date sqlDate = rs.getDate("RELEASEDATE");
        LocalDate releaseDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
        relation.setReleaseDate(releaseDate);

        relation.setExclusive(rs.getBoolean("EXCLUSIVE"));
        relation.setResolution(rs.getString("RESOLUTION"));

        return relation;
    }
}