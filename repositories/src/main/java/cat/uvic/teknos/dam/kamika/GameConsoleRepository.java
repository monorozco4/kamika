// Define el paquete donde pertenece esta clase
package cat.uvic.teknos.dam.kamika;

// Importa las clases necesarias
import java.util.Optional;

/**
 * Interfaz que define operaciones básicas para gestionar la relación entre juegos y consolas.
 * Representa una tabla intermedia (GameConsole) en la base de datos.
 */
public interface GameConsoleRepository {

    /**
     * Busca un registro por sus dos claves primarias: gameId y consoleId.
     * Devuelve un Optional para evitar errores si no se encuentra.
     */
    <GameConsole> Optional<GameConsole> findById(int gameId, int consoleId);

    /**
     * Guarda o actualiza un registro en la base de datos.
     * Recibe un objeto GameConsole y lo devuelve guardado.
     */
    <GameConsole> GameConsole save(GameConsole gameConsole);

    /**
     * Elimina un registro de la base de datos.
     * Recibe el objeto a eliminar.
     */
    <GameConsole> void delete(GameConsole gameConsole);

    /**
     * Elimina un registro usando sus dos identificadores.
     * Devuelve true si se eliminó correctamente, false si no existía.
     */
    boolean deleteById(int gameId, int consoleId);

    /**
     * Cuenta cuántos registros hay en total en la tabla GameConsole.
     * Devuelve el número total como long.
     */
    long count();

    /**
     * Comprueba si existe un registro con los identificadores dados.
     * Devuelve true si existe, false si no.
     */
    boolean existsById(int gameId, int consoleId);
}