package cat.uvic.teknos.dam.kamika.server.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * This typically corresponds to HTTP 404 status code.
 *
 * @author Montse
 * @version 2.0.0
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new NotFoundException with the specified detail message.
     *
     * @param message the detail message explaining which resource was not found
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new NotFoundException with the specified resource type and ID.
     *
     * @param resourceType the type of resource that was not found (e.g., "Game", "Developer")
     * @param id the ID of the resource that was not found
     */
    public NotFoundException(String resourceType, int id) {
        super(resourceType + " with id " + id + " not found");
    }
}