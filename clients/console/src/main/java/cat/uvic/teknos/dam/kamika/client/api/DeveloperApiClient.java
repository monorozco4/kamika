package cat.uvic.teknos.dam.kamika.client.api;

import cat.uvic.teknos.dam.kamika.client.exceptions.ClientException;
import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

/**
 * A client for the /developers endpoint using RawHttp.
 * Dependencies are injected for testability.
 * @author Your Name
 * @version 2.0.1
 */
public class DeveloperApiClient {
    private static final String HOST = "localhost:8081";
    private static final int PORT = 8081;
    private final RawHttp http;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new API client with its required dependencies.
     * @param http The RawHttp instance to use for parsing requests/responses.
     * @param objectMapper The ObjectMapper instance (pre-configured) for JSON conversion.
     */
    public DeveloperApiClient(RawHttp http, ObjectMapper objectMapper) {
        this.http = http;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetches all developers from the server.
     * @return A {@link Set} of all developers.
     * @throws ClientException if an error occurs.
     */
    public Set<Developer> getAll() {
        RawHttpRequest request = http.parseRequest(
                "GET /developers HTTP/1.1\r\n" +
                        "Host: " + HOST + "\r\n" +
                        "User-Agent: KamikaConsoleClient\r\n" +
                        "Connection: close\r\n" +
                        "\r\n");

        try (Socket socket = new Socket("localhost", PORT)) {
            request.writeTo(socket.getOutputStream());
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            if (response.getStatusCode() == 200) {
                String jsonBody = response.getBody().orElseThrow(() -> new ClientException("Server response body is missing"))
                        .decodeBodyToString(StandardCharsets.UTF_8);
                return objectMapper.readValue(jsonBody, new TypeReference<>() {});
            } else {
                throw new ClientException("Server returned an error. Status: " + response.getStatusCode());
            }
        } catch (IOException e) {
            throw new ClientException("Error connecting to or reading from the server: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches a single developer by their ID.
     * @param id The ID of the developer to fetch.
     * @return An {@link Optional} containing the developer if found.
     * @throws ClientException if an error occurs.
     */
    public Optional<Developer> getById(int id) {
        RawHttpRequest request = http.parseRequest(
                "GET /developers/" + id + " HTTP/1.1\r\n" +
                        "Host: " + HOST + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n");

        try (Socket socket = new Socket("localhost", PORT)) {
            request.writeTo(socket.getOutputStream());
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            if (response.getStatusCode() == 200) {
                String jsonBody = response.getBody().orElseThrow(() -> new ClientException("Server response body is missing"))
                        .decodeBodyToString(StandardCharsets.UTF_8);
                return Optional.of(objectMapper.readValue(jsonBody, DeveloperImpl.class));
            } else if (response.getStatusCode() == 404) {
                return Optional.empty();
            } else {
                throw new ClientException("Server returned an error. Status: " + response.getStatusCode());
            }
        } catch (IOException e) {
            throw new ClientException("Error connecting to or reading from the server: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new developer on the server.
     * @param developer The developer object to create.
     * @return The created developer from the server.
     * @throws ClientException if an error occurs.
     */
    public Developer create(Developer developer) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(developer);
            RawHttpRequest request = http.parseRequest(
                            "POST /developers HTTP/1.1\r\n" +
                                    "Host: " + HOST + "\r\n" +
                                    "Content-Type: application/json\r\n" +
                                    "Content-Length: " + jsonPayload.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n")
                    .withBody(new StringBody(jsonPayload));

            try (Socket socket = new Socket("localhost", PORT)) {
                request.writeTo(socket.getOutputStream());
                RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

                if (response.getStatusCode() == 201) {
                    String jsonBody = response.getBody().orElseThrow(() -> new ClientException("Server response body is missing"))
                            .decodeBodyToString(StandardCharsets.UTF_8);
                    return objectMapper.readValue(jsonBody, DeveloperImpl.class);
                } else {
                    throw new ClientException("Server returned an error. Status: " + response.getStatusCode());
                }
            }
        } catch (IOException e) {
            throw new ClientException("Error processing request: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing developer on the server.
     * @param developer The developer object with updated data.
     * @return true if the update was successful.
     * @throws ClientException if an error occurs.
     */
    public boolean update(Developer developer) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(developer);
            RawHttpRequest request = http.parseRequest(
                            "PUT /developers/" + developer.getId() + " HTTP/1.1\r\n" +
                                    "Host: " + HOST + "\r\n" +
                                    "Content-Type: application/json\r\n" +
                                    "Content-Length: " + jsonPayload.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n")
                    .withBody(new StringBody(jsonPayload));

            try (Socket socket = new Socket("localhost", PORT)) {
                request.writeTo(socket.getOutputStream());
                RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
                return response.getStatusCode() == 200;
            }
        } catch (IOException e) {
            throw new ClientException("Error processing request: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a developer from the server.
     * @param id The ID of the developer to delete.
     * @return true if deletion was successful, false if not found.
     * @throws ClientException if an error occurs.
     */
    public boolean delete(int id) {
        RawHttpRequest request = http.parseRequest(
                "DELETE /developers/" + id + " HTTP/1.1\r\n" +
                        "Host: " + HOST + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n");

        try (Socket socket = new Socket("localhost", PORT)) {
            request.writeTo(socket.getOutputStream());
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            if (response.getStatusCode() == 204) {
                return true;
            } else if (response.getStatusCode() == 404) {
                return false;
            } else {
                throw new ClientException("Server returned an error. Status: " + response.getStatusCode());
            }
        } catch (IOException e) {
            throw new ClientException("Error connecting to or reading from the server: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a graceful disconnect message to the server, typically on client inactivity.
     * Waits for an "ACK" acknowledgement.
     *
     * @return true if acknowledgement ("ACK") was received, false otherwise.
     * @throws ClientException if a connection error occurs.
     */
    public boolean sendDisconnect() {
        RawHttpRequest request = http.parseRequest(
                "POST /disconnect HTTP/1.1\r\n" +
                        "Host: " + HOST + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n");

        try (Socket socket = new Socket("localhost", PORT)) {
            request.writeTo(socket.getOutputStream());
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            if (response.getStatusCode() == 200) {
                String body = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
                return "ACK".equals(body.trim());
            }
            return false;
        } catch (IOException e) {
            throw new ClientException("Error sending disconnect message: " + e.getMessage(), e);
        }
    }
}