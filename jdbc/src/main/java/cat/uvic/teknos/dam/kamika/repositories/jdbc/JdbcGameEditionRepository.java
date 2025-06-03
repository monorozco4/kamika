package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.repositories.GameEdition;
import cat.uvic.teknos.dam.kamika.repositories.GameEditionRepository;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameEditionImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.*;

/**
 * JDBC implementation of the GameEdition repository.
 * Follows best practices for connection handling and exception management.
 */
public class JdbcGameEditionRepository implements GameEditionRepository {

    private final DataSource dataSource;

    /**
     * Constructs a new repository using the provided data source.
     *
     * @param dataSource the data source to use for database connections
     */
    public JdbcGameEditionRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    /**
     * Finds a game edition by its composite key: gameId and editionName.
     *
     * @param gameId      the ID of the game
     * @param editionName the name of the edition
     * @return an Optional containing the game edition if found, empty otherwise
     */
    @Override
    public Optional<GameEdition> findByGameIdAndEditionName(int gameId, String editionName) {
        String sql = "SELECT * FROM GAMEEDITION WHERE GAMEID = ? AND EDITIONNAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setString(2, editionName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding game edition by ID and name", e);
        }
        return Optional.empty();
    }

    /**
     * Saves or updates a game edition in the database.
     *
     * @param gameEdition the edition to save
     * @return the saved game edition
     */
    @Override
    public GameEdition save(GameEdition gameEdition) {
        if (gameEdition.getGame() == null || gameEdition.getEditionName() == null || gameEdition.getEditionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Game and edition name must not be null or empty");
        }

        if (!existsByGameIdAndEditionName(gameEdition.getGame().getId(), gameEdition.getEditionName())) {
            insert(gameEdition);
        } else {
            update(gameEdition);
        }

        return gameEdition;
    }

    private void insert(GameEdition gameEdition) {
        String sql = "INSERT INTO GAMEEDITION (GAMEID, EDITIONNAME, SPECIALCONTENT, PRICE) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, gameEdition.getGame().getId());
            stmt.setString(2, gameEdition.getEditionName());
            stmt.setString(3, gameEdition.getSpecialContent());
            stmt.setDouble(4, gameEdition.getPrice());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Failed to insert game edition, no rows affected.");
            }

        } catch (SQLException e) {
            throw new CrudException("Error inserting game edition", e);
        }
    }

    private void update(GameEdition gameEdition) {
        String sql = "UPDATE GAMEEDITION SET SPECIALCONTENT = ?, PRICE = ? WHERE GAMEID = ? AND EDITIONNAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameEdition.getSpecialContent());
            stmt.setDouble(2, gameEdition.getPrice());
            stmt.setInt(3, gameEdition.getGame().getId());
            stmt.setString(4, gameEdition.getEditionName());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error updating game edition: no rows affected");
            }

        } catch (SQLException e) {
            throw new CrudException("Error updating game edition", e);
        }
    }

    /**
     * Deletes a game edition from the database.
     *
     * @param gameEdition the edition to delete
     */
    @Override
    public void delete(GameEdition gameEdition) {
        if (gameEdition.getGame() == null || gameEdition.getEditionName() == null) {
            throw new IllegalArgumentException("Game and edition name must not be null");
        }

        String sql = "DELETE FROM GAMEEDITION WHERE GAMEID = ? AND EDITIONNAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameEdition.getGame().getId());
            stmt.setString(2, gameEdition.getEditionName());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error deleting game edition: no rows affected");
            }

        } catch (SQLException e) {
            throw new CrudException("Error deleting game edition", e);
        }
    }

    /**
     * Deletes a game edition by game ID and edition name.
     *
     * @param gameId      the ID of the game
     * @param editionName the name of the edition
     * @return true if the edition was deleted, false if it did not exist
     */
    @Override
    public boolean deleteByGameIdAndEditionName(int gameId, String editionName) {
        String sql = "DELETE FROM GAMEEDITION WHERE GAMEID = ? AND EDITIONNAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setString(2, editionName);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting game edition by game ID and edition name", e);
        }
    }

    /**
     * Counts how many game editions exist in the database.
     *
     * @return the total number of game editions
     */
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM GAMEEDITION";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting game editions", e);
        }
        return 0;
    }

    /**
     * Checks whether a game edition exists by game ID and edition name.
     *
     * @param gameId      the ID of the game
     * @param editionName the name of the edition
     * @return true if the edition exists, false otherwise
     */
    @Override
    public boolean existsByGameIdAndEditionName(int gameId, String editionName) {
        String sql = "SELECT 1 FROM GAMEEDITION WHERE GAMEID = ? AND EDITIONNAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setString(2, editionName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking existence of game edition", e);
        }
    }

    /**
     * Maps a ResultSet row to a GameEdition entity.
     *
     * @param rs the result set to map
     * @return the mapped GameEdition object
     * @throws SQLException if a database access error occurs
     */
    private GameEdition mapToEntity(ResultSet rs) throws SQLException {
        GameEdition edition = new GameEditionImpl();
        edition.setGame(new GameImpl());
        edition.getGame().setId(rs.getInt("GAMEID"));
        edition.setEditionName(rs.getString("EDITIONNAME"));
        edition.setSpecialContent(rs.getString("SPECIALCONTENT"));
        edition.setPrice(rs.getDouble("PRICE"));

        return edition;
    }
}