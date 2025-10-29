package cat.uvic.teknos.dam.kamika.server.controllers;

import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.repositories.DeveloperRepository;
import cat.uvic.teknos.dam.kamika.server.exceptions.BadRequestException;
import cat.uvic.teknos.dam.kamika.server.exceptions.MethodNotAllowedException;
import cat.uvic.teknos.dam.kamika.server.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Implements the {@link Controller} for the Developer entity.
 * This controller is responsible for the business logic of CRUD operations.
 * It throws custom exceptions to signal specific error conditions, which are then
 * handled by a higher-level component (ClientHandler).
 * @author Montse
 * @version 2.0.0
 */
public class DeveloperController implements Controller {

    private final DeveloperRepository repository;
    private final ObjectMapper objectMapper;
    private final RawHttp http;

    /**
     * Constructs a new DeveloperController.
     *
     * @param repository The repository for data access.
     * @param objectMapper The mapper for converting Java objects to/from JSON.
     */
    public DeveloperController(DeveloperRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.http = new RawHttp();
    }

    /**
     * Handles an incoming request by routing it to the appropriate CRUD method.
     * Throws specific exceptions for invalid inputs or resource states.
     *
     * @param request The incoming HTTP request.
     * @return The generated HTTP response for successful operations.
     * @throws IOException if a low-level I/O error occurs.
     * @throws NotFoundException if a requested resource does not exist.
     * @throws BadRequestException if the request is malformed (e.g., invalid ID, bad JSON).
     * @throws MethodNotAllowedException if the HTTP method is not supported for the URI.
     */
    @Override
    public RawHttpResponse<?> handle(RawHttpRequest request) throws IOException {
        String method = request.getMethod();
        String path = request.getUri().getPath();
        String[] pathParts = path.split("/");

        boolean hasId = pathParts.length > 2;
        int id = -1;
        if (hasId) {
            try {
                id = Integer.parseInt(pathParts[2]);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid ID format. ID must be an integer.");
            }
        }

        return switch (method.toUpperCase()) {
            case "GET" -> hasId ? getDeveloperById(id) : getAllDevelopers();
            case "POST" -> hasId ? throwMethodNotAllowed(method) : createDeveloper(request);
            case "PUT" -> hasId ? updateDeveloper(id, request) : throwMethodNotAllowed(method);
            case "DELETE" -> hasId ? deleteDeveloper(id) : throwMethodNotAllowed(method);
            default -> throwMethodNotAllowed(method);
        };
    }

    private RawHttpResponse<?> getAllDevelopers() throws IOException {
        Set<Developer> developers = repository.findAll();
        String jsonBody = objectMapper.writeValueAsString(developers);
        return createJsonResponse(200, "OK", jsonBody);
    }

    private RawHttpResponse<?> getDeveloperById(int id) throws IOException {
        Developer developer = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Developer", id));
        String jsonBody = objectMapper.writeValueAsString(developer);
        return createJsonResponse(200, "OK", jsonBody);
    }

    private RawHttpResponse<?> createDeveloper(RawHttpRequest request) throws IOException {
        try {
            String jsonBody = request.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
            Developer newDeveloper = objectMapper.readValue(jsonBody, DeveloperImpl.class);
            newDeveloper.setId(0);
            Developer savedDeveloper = repository.save(newDeveloper);
            String responseJson = objectMapper.writeValueAsString(savedDeveloper);
            return createJsonResponse(201, "Created", responseJson);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid JSON format: " + e.getMessage());
        }
    }

    private RawHttpResponse<?> updateDeveloper(int id, RawHttpRequest request) throws IOException {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Developer", id);
        }
        try {
            String jsonBody = request.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
            Developer updatedData = objectMapper.readValue(jsonBody, DeveloperImpl.class);
            updatedData.setId(id);
            Developer savedDeveloper = repository.save(updatedData);
            String responseJson = objectMapper.writeValueAsString(savedDeveloper);
            return createJsonResponse(200, "OK", responseJson);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid JSON format: " + e.getMessage());
        }
    }

    private RawHttpResponse<?> deleteDeveloper(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Developer", id);
        }
        repository.deleteById(id);
        return http.parseResponse("HTTP/1.1 204 No Content");
    }

    private RawHttpResponse<?> throwMethodNotAllowed(String method) {
        throw new MethodNotAllowedException(method);
    }

    private RawHttpResponse<?> createJsonResponse(int code, String status, String jsonBody) {
        return http.parseResponse(
                "HTTP/1.1 " + code + " " + status + "\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + jsonBody.getBytes().length
        ).withBody(new StringBody(jsonBody));
    }
}