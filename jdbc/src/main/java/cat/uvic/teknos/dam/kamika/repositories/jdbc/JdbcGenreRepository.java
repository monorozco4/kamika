package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Genre;
import cat.uvic.teknos.dam.kamika.repositories.GenreRepository;
import cat.uvic.teknos.dam.kamika.model.impl.GenreImpl;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.exceptions.CrudException;

import java.sql.*;
import java.util.*;

/**
 * JDBC implementation of the Genre repository.
 * Follows best practices for connection handling and exception management.
 */
public class JdbcGenreRepository implements GenreRepository {

    private final DataSource dataSource;

    public JdbcGenreRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    /**
     * Finds a genre by its ID.
     *
     * @param id the genre ID
     * @return an Optional containing the genre if found, empty otherwise
     */
    @Override
    public Optional<Genre> findById(int id) {
        if (id < 0) {
            throw new CrudException("Invalid id: " + id);
        }
        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error finding genre by ID", e);
        }
        return Optional.empty();
    }

    /**
     * Saves or updates a genre in the database.
     *
     * @param genre the genre to save
     * @return the saved genre
     */
    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            return insert(genre);
        } else {
            return update(genre);
        }
    }

    private Genre insert(Genre genre) {
        String sql = "INSERT INTO GENRE (NAME, DESCRIPTION) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, genre.getName());
            stmt.setString(2, genre.getDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new CrudException("Failed to insert genre, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    genre.setId(generatedKeys.getInt(1));
                }
            }

            return genre;

        } catch (SQLException e) {
            throw new CrudException("Error inserting genre", e);
        }
    }

    private Genre update(Genre genre) {
        String sql = "UPDATE GENRE SET NAME = ?, DESCRIPTION = ? WHERE GENRE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre.getName());
            stmt.setString(2, genre.getDescription());
            stmt.setInt(3, genre.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Error updating genre: no rows affected");
            }

            return genre;

        } catch (SQLException e) {
            throw new CrudException("Error updating genre", e);
        }
    }

    /**
     * Deletes a genre from the database.
     *
     * @param genre the genre to delete
     */
    @Override
    public void delete(Genre genre) {
        String sql = "DELETE FROM GENRE WHERE GENRE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, genre.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new CrudException("Error deleting genre", e);
        }
    }

    /**
     * Deletes a genre by its ID.
     *
     * @param id the genre ID
     * @return true if successfully deleted, false if not found
     */
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM GENRE WHERE GENRE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new CrudException("Error deleting genre by ID", e);
        }
    }

    /**
     * Counts how many genres exist in the database.
     *
     * @return the total number of genres
     */
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM GENRE";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting genres", e);
        }
        return 0;
    }

    /**
     * Checks whether a genre with the given ID exists.
     *
     * @param id the genre ID
     * @return true if the genre exists, false otherwise
     */
    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM GENRE WHERE GENRE_ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new CrudException("Error checking if genre exists", e);
        }
    }

    /**
     * Find a genre by name (case-insensitive exact match).
     *
     * @param name the genre name to search for
     * @return an Optional containing the genre if found, empty otherwise
     */
    @Override
    public Optional<Genre> findByNameIgnoreCase(String name) {
        String sql = "SELECT * FROM GENRE WHERE LOWER(NAME) = LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToEntity(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error finding genre by name", e);
        }
        return Optional.empty();
    }

    /**
     * Count the number of games associated with each genre.
     * Returns a map where keys are genre IDs and values are game counts.
     *
     * @return a map with genre IDs as keys and game counts as values
     */
    @Override
    public Map<Integer, Long> countGamesPerGenre() {
        Map<Integer, Long> result = new HashMap<>();
        String sql = "SELECT GENRE_ID, COUNT(*) AS GAME_COUNT FROM GAME GROUP BY GENRE_ID";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int genreId = rs.getInt("GENREID");
                long gameCount = rs.getLong("GAME_COUNT");
                result.put(genreId, gameCount);
            }

        } catch (SQLException e) {
            throw new CrudException("Error counting games per genre", e);
        }

        return result;
    }

    @Override
    public Set<Genre> findAll() {
        Set<Genre> genres = new HashSet<>();
        String sql = "SELECT * FROM GENRE";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                genres.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving all consoles", e);
        }
        return genres;
    }

    /**
     * Maps a ResultSet row to a Genre entity.
     *
     * @param rs the result set to map
     * @return the mapped Genre object
     * @throws SQLException if a database access error occurs
     */
    private Genre mapToEntity(ResultSet rs) throws SQLException {
        Genre genre = new GenreImpl();
        genre.setId(rs.getInt("GENRE_ID"));
        genre.setName(rs.getString("NAME"));
        genre.setDescription(rs.getString("DESCRIPTION"));
        return genre;
    }

    @Override
    public Optional<Genre> findByName(String name) {
        String sql = "SELECT * FROM GENRE WHERE LOWER(NAME) = LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Genre genre = mapToEntity(rs);
                    return Optional.of(genre);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving genre by name", e);
        }
        return Optional.empty();
    }
}