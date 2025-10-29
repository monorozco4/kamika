package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.GameConsole;
import cat.uvic.teknos.dam.kamika.repositories.GameConsoleRepository;
import cat.uvic.teknos.dam.kamika.model.impl.GameConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class JdbcGameConsoleRepository implements GameConsoleRepository {

    private final DataSource dataSource;

    public JdbcGameConsoleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<GameConsole> findByGameConsoleId(int gameConsoleId) {
        String sql = "SELECT * FROM GAME_CONSOLE WHERE GAME_CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameConsoleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error buscando por GAME_CONSOLE_ID", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteByGameConsoleId(int gameConsoleId) {
        String sql = "DELETE FROM GAME_CONSOLE WHERE GAME_CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameConsoleId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new CrudException("Error eliminando por GAME_CONSOLE_ID", e);
        }
    }

    @Override
    public Optional<GameConsole> findById(int gameId, int consoleId) {
        if (gameId <= 0 || consoleId <= 0) {
            throw new CrudException("Invalid composite ID: " + gameId + "/" + consoleId);
        }

        String sql = "SELECT * FROM GAME_CONSOLE WHERE GAME_ID = ? AND CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, consoleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding game-console relation", e);
        }
        return Optional.empty();
    }

    @Override
    public GameConsole save(GameConsole gameConsole) {
        Objects.requireNonNull(gameConsole, "Cannot save null game-console relation");

        int gameId = gameConsole.getGameId();
        int consoleId = gameConsole.getConsoleId();

        if (gameId <= 0 || consoleId <= 0)
            throw new CrudException("Game and Console IDs must be valid");

        if (!existsById(gameId, consoleId)) {
            insert(gameConsole);
        } else {
            update(gameConsole);
        }

        return gameConsole;
    }

    private void insert(GameConsole gameConsole) {
        String sql = "INSERT INTO GAME_CONSOLE (GAME_ID, CONSOLE_ID, RELEASE_DATE, IS_EXCLUSIVE, RESOLUTION) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, gameConsole.getGameId());
            stmt.setInt(2, gameConsole.getConsoleId());
            stmt.setObject(3, gameConsole.getReleaseDate());
            stmt.setBoolean(4, gameConsole.isExclusive());
            stmt.setString(5, gameConsole.getResolution());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0)
                throw new CrudException("Insert failed: no rows affected");

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    gameConsole.setGameConsoleId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error inserting game-console relation", e);
        }
    }

    private void update(GameConsole gameConsole) {
        String sql = "UPDATE GAME_CONSOLE SET RELEASE_DATE = ?, IS_EXCLUSIVE = ?, RESOLUTION = ? WHERE GAME_ID = ? AND CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, gameConsole.getReleaseDate());
            stmt.setBoolean(2, gameConsole.isExclusive());
            stmt.setString(3, gameConsole.getResolution());
            stmt.setInt(4, gameConsole.getGameId());
            stmt.setInt(5, gameConsole.getConsoleId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0)
                throw new CrudException("Update failed: no rows affected");

        } catch (SQLException e) {
            throw new CrudException("Error updating game-console relation", e);
        }
    }

    @Override
    public void delete(GameConsole gameConsole) {
        Objects.requireNonNull(gameConsole, "Cannot delete a null entity");

        int gameId = gameConsole.getGameId();
        int consoleId = gameConsole.getConsoleId();

        deleteById(gameId, consoleId);
    }

    @Override
    public boolean deleteById(int gameId, int consoleId) {
        String sql = "DELETE FROM GAME_CONSOLE WHERE GAME_ID = ? AND CONSOLE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, consoleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting game-console relation by ID", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM GAME_CONSOLE";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next())
                return rs.getLong(1);

        } catch (SQLException e) {
            throw new CrudException("Error counting game-console relations", e);
        }
        return 0;
    }

    @Override
    public boolean existsById(int gameId, int consoleId) {
        String sql = "SELECT 1 FROM GAME_CONSOLE WHERE GAME_ID = ? AND CONSOLE_ID = ?";
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

    private GameConsole mapToEntity(ResultSet rs) throws SQLException {
        GameConsoleImpl entity = new GameConsoleImpl();
        entity.setGameConsoleId(rs.getInt("GAME_CONSOLE_ID"));
        entity.setGameId(rs.getInt("GAME_ID"));
        entity.setConsoleId(rs.getInt("CONSOLE_ID"));
        Date date = rs.getDate("RELEASE_DATE");
        entity.setReleaseDate(date != null ? date.toLocalDate() : null);
        entity.setExclusive(rs.getBoolean("IS_EXCLUSIVE"));
        entity.setResolution(rs.getString("RESOLUTION"));
        return entity;
    }
}