package cat.uvic.teknos.dam.kamika;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing Game entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface GameRepository {

    /**
     * Find a game by its ID.
     *
     * @param id the game ID
     * @return an Optional containing the game if found, empty otherwise
     */
    Optional<Game> findById(int id);

    /**
     * Retrieve all games.
     *
     * @return a list of all games
     */
    List<Game> findAll();

    /**
     * Save a new game or update an existing one.
     *
     * @param game the game to save
     * @return the saved game
     */
    Game save(Game game);

    /**
     * Delete a game.
     *
     * @param game the game to delete
     */
    void delete(Game game);

    /**
     * Delete a game by its ID.
     *
     * @param id the ID of the game to delete
     * @return true if the game was deleted, false if not found
     */
    boolean deleteById(int id);

    /**
     * Count the total number of games.
     *
     * @return the total number of games
     */
    long count();

    /**
     * Check if a game with the given ID exists.
     *
     * @param id the game ID
     * @return true if the game exists, false otherwise
     */
    boolean existsById(int id);

    // Custom methods

    /**
     * Find games by title (case-insensitive, partial match).
     *
     * @param title the title to search for
     * @return a list of games matching the title
     */
    List<Game> findByTitleContainingIgnoreCase(String title);

    /**
     * Find games by developer.
     *
     * @param developerId the developer ID
     * @return a list of games by the specified developer
     */
    List<Game> findByDeveloperId(int developerId);

    /**
     * Find games by publisher.
     *
     * @param publisherId the publisher ID
     * @return a list of games by the specified publisher
     */
    List<Game> findByPublisherId(int publisherId);

    /**
     * Find games by PEGI rating.
     *
     * @param pegiRating the PEGI rating
     * @return a list of games with the specified PEGI rating
     */
    List<Game> findByPegiRating(String pegiRating);

    /**
     * Find multiplayer games.
     *
     * @param isMultiplayer whether to search for multiplayer games
     * @return a list of games based on multiplayer status
     */
    List<Game> findByMultiplayer(boolean isMultiplayer);

    /**
     * Find games by genre.
     *
     * @param genreId the genre ID
     * @return a list of games with the specified genre
     */
    List<Game> findByGenresId(int genreId);

    /**
     * Find games by console.
     *
     * @param consoleId the console ID
     * @return a list of games available for the specified console
     */
    List<Game> findByConsolesId(int consoleId);

    /**
     * Find games released after a specific date.
     *
     * @param date the release date
     * @return a list of games released after the specified date
     */
    List<Game> findByReleaseDateAfter(LocalDate date);

    /**
     * Find games released before a specific date.
     *
     * @param date the release date
     * @return a list of games released before the specified date
     */
    List<Game> findByReleaseDateBefore(LocalDate date);

    /**
     * Find games released between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of games released between the specified dates
     */
    List<Game> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find games by edition.
     *
     * @param editionId the edition ID
     * @return a list of games with the specified edition
     */
    List<Game> findByEditionId(int editionId);

    /**
     * Find games with a specific combination of genres.
     *
     * @param genreIds the set of genre IDs
     * @return a list of games that have all the specified genres
     */
    List<Game> findByGenresIdIn(Set<Integer> genreIds);

    /**
     * Find games available on a specific combination of consoles.
     *
     * @param consoleIds the set of console IDs
     * @return a list of games available on all the specified consoles
     */
    List<Game> findByConsolesIdIn(Set<Integer> consoleIds);
}