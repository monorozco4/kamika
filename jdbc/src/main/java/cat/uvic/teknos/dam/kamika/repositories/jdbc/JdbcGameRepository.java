package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.repositories.GameRepository;
import cat.uvic.teknos.dam.kamika.model.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class JdbcGameRepository implements GameRepository {

    private final DataSource dataSource;

    public JdbcGameRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Game> findById(int id) {
        String sql = "SELECT * FROM GAME WHERE GAME_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error retrieving game by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public Game save(Game game) {
        if (!(game instanceof GameImpl)) {
            throw new IllegalArgumentException("Only GameImpl can be saved");
        }

        // Validación mejorada con mensajes claros
        if (game.getTitle() == null || game.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Game title is required");
        }
        if (game.getReleaseDate() == null) {
            throw new IllegalArgumentException("Release date is required");
        }
        if (game.getDeveloper() == null || game.getDeveloper().getId() <= 0) {
            throw new IllegalArgumentException("Valid developer is required");
        }
        if (game.getPublisher() == null || game.getPublisher().getId() <= 0) {
            throw new IllegalArgumentException("Valid publisher is required");
        }
        if (game.getPegiRating() == null || game.getPegiRating().trim().isEmpty()) {
            throw new IllegalArgumentException("PEGI rating is required");
        }

        if (existsById(game.getId())) {
            update((GameImpl) game);
        } else {
            insert((GameImpl) game);
        }
        return game;
    }

    private void insert(GameImpl game) throws CrudException {
        String sql = "INSERT INTO GAME (TITLE, RELEASE_DATE, DEVELOPER_ID, PUBLISHER_ID, PEGI_RATING, IS_MULTIPLAYER) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, game.getTitle());
            stmt.setObject(2, game.getReleaseDate());
            stmt.setInt(3, game.getDeveloper().getId());
            stmt.setInt(4, game.getPublisher().getId());
            stmt.setString(5, game.getPegiRating());
            stmt.setBoolean(6, game.isMultiplayer());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Insert failed: no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setId(generatedKeys.getInt(1));
                } else {
                    throw new CrudException("Failed to get generated game ID");
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error inserting game", e);
        }
    }

    private void update(GameImpl game) throws CrudException {
        String sql = "UPDATE GAME SET TITLE = ?, RELEASE_DATE = ?, DEVELOPER_ID = ?, PUBLISHER_ID = ?, PEGI_RATING = ?, IS_MULTIPLAYER = ? WHERE GAME_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.getTitle());
            stmt.setObject(2, game.getReleaseDate());
            stmt.setInt(3, game.getDeveloper().getId());
            stmt.setInt(4, game.getPublisher().getId());
            stmt.setString(5, game.getPegiRating());
            stmt.setBoolean(6, game.isMultiplayer());
            stmt.setInt(7, game.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Update failed: no rows affected");
            }

        } catch (SQLException e) {
            throw new CrudException("Error updating game", e);
        }
    }

    @Override
    public void delete(Game game) {
        if (game == null || game.getId() <= 0) {
            throw new IllegalArgumentException("Invalid game ID");
        }

        String sql = "DELETE FROM GAME WHERE GAME_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, game.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Delete failed: no rows affected");
            }

        } catch (SQLException e) {
            throw new CrudException("Error deleting game", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM GAME WHERE GAME_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

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
        String sql = "SELECT 1 FROM GAME WHERE GAME_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking game existence", e);
        }
    }

    private Game mapToEntity(ResultSet rs) throws SQLException {
        GameImpl game = new GameImpl();
        game.setId(rs.getInt("GAME_ID"));
        game.setTitle(rs.getString("TITLE"));

        Date releaseDate = rs.getDate("RELEASE_DATE");
        game.setReleaseDate(releaseDate != null ? releaseDate.toLocalDate() : null);

        game.setPegiRating(rs.getString("PEGI_RATING"));
        game.setMultiplayer(rs.getBoolean("IS_MULTIPLAYER"));

        return game;
    }
}