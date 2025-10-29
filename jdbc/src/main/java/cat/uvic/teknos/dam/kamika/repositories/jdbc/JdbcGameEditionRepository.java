package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.GameEdition;
import cat.uvic.teknos.dam.kamika.repositories.GameEditionRepository;
import cat.uvic.teknos.dam.kamika.model.impl.GameEditionImpl;
import cat.uvic.teknos.dam.kamika.model.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class JdbcGameEditionRepository implements GameEditionRepository {

    private final DataSource dataSource;

    public JdbcGameEditionRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public Optional<GameEdition> findByGameIdAndEditionName(int gameId, String editionName) {
        if (gameId <= 0 || editionName == null || editionName.isBlank()) {
            throw new CrudException("Invalid parameters for finding game edition");
        }

        String sql = "SELECT * FROM GAME_EDITION WHERE GAME_ID = ? AND EDITION_NAME = ?";
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
            // ✅ Encapsulamos SQLException en CrudException
            throw new CrudException("Error retrieving game edition", e);
        }

        return Optional.empty();
    }

    @Override
    public GameEdition save(GameEdition gameEdition) {
        if (gameEdition == null) {
            throw new IllegalArgumentException("GameEdition cannot be null");
        }

        if (gameEdition.getGame() == null || gameEdition.getGame().getId() <= 0) {
            throw new IllegalArgumentException("Game must exist and have a valid ID");
        }

        if (gameEdition.getEditionName() == null || gameEdition.getEditionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Edition name must not be null or empty");
        }

        int gameId = gameEdition.getGame().getId();
        String editionName = gameEdition.getEditionName();

        if (!existsByGameIdAndEditionName(gameId, editionName)) {
            insert(gameEdition);
        } else {
            update(gameEdition);
        }

        return gameEdition;
    }

    private void insert(GameEdition gameEdition) {
        String sql = "INSERT INTO GAME_EDITION (GAME_ID, EDITION_NAME, SPECIAL_CONTENT, PRICE) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameEdition.getGame().getId());
            stmt.setString(2, gameEdition.getEditionName());
            stmt.setString(3, gameEdition.getSpecialContent());
            stmt.setDouble(4, gameEdition.getPrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Insert failed: no rows affected.");
            }

        } catch (SQLException e) {
            // ✅ Ahora todos los errores se convierten en CrudException
            throw new CrudException("Error inserting game edition", e);
        }
    }

    private void update(GameEdition gameEdition) {
        String sql = "UPDATE GAME_EDITION SET SPECIAL_CONTENT = ?, PRICE = ? WHERE GAME_ID = ? AND EDITION_NAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameEdition.getSpecialContent());
            stmt.setDouble(2, gameEdition.getPrice());
            stmt.setInt(3, gameEdition.getGame().getId());
            stmt.setString(4, gameEdition.getEditionName());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Update failed: no rows affected.");
            }

        } catch (SQLException e) {
            // ✅ Todos los errores SQL se encapsulan en CrudException
            throw new CrudException("Error updating game edition", e);
        }
    }

    @Override
    public Optional<GameEdition> findById(int id) {
        if (id <= 0) {
            throw new CrudException("Invalid game edition ID: " + id);
        }

        String sql = "SELECT * FROM GAME_EDITION WHERE GAME_EDITION_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding game edition by ID", e);
        }
        return Optional.empty();
    }

    /**
     * Maps a ResultSet row to a GameEdition entity.
     *
     * @param rs the result set to map
     * @return the mapped GameEdition object
     * @throws SQLException if a database access error occurs
     */
    private GameEdition mapToEntity(ResultSet rs) throws SQLException {
        GameEdition gameEdition = new GameEditionImpl();
        gameEdition.setId(rs.getInt("GAME_EDITION_ID"));
        gameEdition.setEditionName(rs.getString("NAME"));
        gameEdition.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        return gameEdition;
    }

    @Override
    public void delete(GameEdition gameEdition) {
        if (gameEdition == null || gameEdition.getGame() == null || gameEdition.getEditionName() == null) {
            throw new CrudException("Game edition data is invalid");
        }

        int gameId = gameEdition.getGame().getId();
        String editionName = gameEdition.getEditionName();

        deleteByGameIdAndEditionName(gameId, editionName);
    }

    @Override
    public void deleteByGameIdAndEditionName(int gameId, String editionName) {
        String sql = "DELETE FROM GAME_EDITION WHERE GAME_ID = ? AND EDITION_NAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setString(2, editionName);

            int rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new CrudException("Error deleting game edition by composite key", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM GAME_EDITION";
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

    @Override
    public boolean existsByGameIdAndEditionName(int gameId, String editionName) {
        String sql = "SELECT 1 FROM GAME_EDITION WHERE GAME_ID = ? AND EDITION_NAME = ?";
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

    @Override
    public Optional<GameEdition> findByEditionName(String editionName) {
        if (editionName == null || editionName.isBlank()) {
            throw new CrudException("Edition name must not be null or empty");
        }

        String sql = "SELECT * FROM GAME_EDITION WHERE EDITION_NAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, editionName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error retrieving game edition by edition name", e);
        }

        return Optional.empty();
    }

    @Override
    public Set<GameEdition> findAll() {
        String sql = "SELECT * FROM GAME_EDITION";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            Set<GameEdition> gameEditions = new HashSet<>();
            while (rs.next()) {
                gameEditions.add(mapToEntity(rs));
            }
            return gameEditions;

        } catch (SQLException e) {
            throw new CrudException("Error retrieving all game editions", e);
        }
    }
}