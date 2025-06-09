package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.GameConsole;

import java.util.Optional;

/**
 * Repository interface for managing GameConsole entities.
 * Follows the repository pattern to abstract the data access operations.
 */
public interface GameConsoleRepository {

    /**
     * Busca una relación GameConsole por su ID único.
     * @param gameConsoleId el ID de la relación GameConsole
     * @return un Optional con la relación encontrada, o vacío si no existe
     */
    Optional<GameConsole> findByGameConsoleId(int gameConsoleId);

    /**
     * Elimina una relación GameConsole por su ID único.
     * @param gameConsoleId el ID de la relación GameConsole a eliminar
     * @return true si se eliminó, false si no existía
     */
    boolean deleteByGameConsoleId(int gameConsoleId);

    /**
     * Finds a record by its composite primary key: gameId and consoleId.
     * Returns an Optional to avoid errors if no result is found.
     */
    Optional<GameConsole> findById(int gameId, int consoleId);

    /**
     * Saves or updates a record in the database.
     * Returns the saved GameConsole object.
     */
    GameConsole save(GameConsole gameConsole);

    /**
     * Deletes a record from the database.
     * Takes the entity to be deleted as a parameter.
     */
    void delete(GameConsole gameConsole);

    /**
     * Deletes a record using its composite key: gameId and consoleId.
     * Returns true if successfully deleted, false if it did not exist.
     */
    boolean deleteById(int gameId, int consoleId);

    /**
     * Counts how many records exist in the GameConsole table.
     * Returns the total number of records as a long.
     */
    long count();

    /**
     * Checks whether a record exists with the given identifiers.
     * Returns true if the record exists, false otherwise.
     */
    boolean existsById(int gameId, int consoleId);
}