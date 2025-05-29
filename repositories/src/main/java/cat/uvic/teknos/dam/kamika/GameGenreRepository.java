// Define el paquete donde pertenece esta clase
package cat.uvic.teknos.dam.kamika;

// Importa las clases necesarias
import java.util.Optional;

/**
 * Interfaz que define operaciones básicas para gestionar la relación entre juegos y géneros.
 * Representa una tabla intermedia (GameGenre) en la base de datos.
 */
public interface GameGenreRepository {

    /**
     * Busca un registro por sus dos claves primarias: gameId y genreId.
     * Devuelve un Optional para evitar errores si no se encuentra.
     */
    Optional<GameGenre> findById(int gameId, int genreId);

    /**
     * Guarda o actualiza un registro en la base de datos.
     * Recibe un objeto GameGenre y lo devuelve guardado.
     */
    GameGenre save(GameGenre gameGenre);

    /**
     * Elimina un registro de la base de datos.
     * Recibe el objeto a eliminar.
     */
    void delete(GameGenre gameGenre);

    /**
     * Elimina un registro usando sus dos identificadores.
     * Devuelve true si se eliminó correctamente, false si no existía.
     */
    boolean deleteById(int gameId, int genreId);

    /**
     * Cuenta cuántos registros hay en total en la tabla GameGenre.
     * Devuelve el número total como long.
     */
    long count();

    /**
     * Comprueba si existe un registro con los identificadores dados.
     * Devuelve true si existe, false si no.
     */
    boolean existsById(int gameId, int genreId);
}