package cat.uvic.teknos.dam.kamika.client.api;

import cat.uvic.teknos.dam.kamika.client.exceptions.ClientException;
import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.security.CryptoUtils;
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
 * API Client for managing Developer resources via the remote server.
 * Uses RawHttp for communication and Jackson for JSON serialization.
 * Security Update:
 * Integrated {@link CryptoUtils} to ensure Data Integrity.
 * Signs outgoing requests (POST/PUT) using {@code X-Content-Hash} header.
 * Verifies incoming responses by checking the server's hash signature.
 * @author Montse Orozco
 * @version 2.0.2
 */
public class DeveloperApiClient {
    private static final String HOST = "localhost:8081";
    private static final int PORT = 8081;
    private final RawHttp http;
    private final ObjectMapper objectMapper;
    private final CryptoUtils cryptoUtils;

    /**
     * Constructs a new API client with its required dependencies.
     *
     * @param http The RawHttp instance to use for parsing requests/responses.
     * @param objectMapper The ObjectMapper instance (pre-configured) for JSON conversion.
     */
    public DeveloperApiClient(RawHttp http, ObjectMapper objectMapper) {
        this.http = http;
        this.objectMapper = objectMapper;
        this.cryptoUtils = new CryptoUtils();
    }

    /**
     * Fetches all developers from the server.
     * Verifies response integrity.
     *
     * @return A {@link Set} of all developers.
     * @throws ClientException if an error occurs or integrity check fails.
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

                validateResponseIntegrity(response, jsonBody);

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
     * Verifies response integrity.
     *
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

                validateResponseIntegrity(response, jsonBody);

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
     * Computes hash for request and verifies hash for response.
     *
     * @param developer The developer object to create.
     * @return The created developer from the server.
     * @throws ClientException if an error occurs.
     */
    public Developer create(Developer developer) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(developer);
            String contentHash = cryptoUtils.hash(jsonPayload);

            RawHttpRequest request = http.parseRequest(
                            "POST /developers HTTP/1.1\r\n" +
                                    "Host: " + HOST + "\r\n" +
                                    "Content-Type: application/json\r\n" +
                                    "X-Content-Hash: " + contentHash + "\r\n" +
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

                    validateResponseIntegrity(response, jsonBody);

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
     * Computes hash for request.
     *
     * @param developer The developer object with updated data.
     * @return true if the update was successful.
     * @throws ClientException if an error occurs.
     */
    public boolean update(Developer developer) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(developer);
            String contentHash = cryptoUtils.hash(jsonPayload);

            RawHttpRequest request = http.parseRequest(
                            "PUT /developers/" + developer.getId() + " HTTP/1.1\r\n" +
                                    "Host: " + HOST + "\r\n" +
                                    "Content-Type: application/json\r\n" +
                                    "X-Content-Hash: " + contentHash + "\r\n" +
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
     *
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

    /**
     * Helper method to validate the integrity of the response body using the X-Content-Hash header.
     *
     * @param response The RawHttpResponse received from the server.
     * @param body The decoded body string.
     * @throws ClientException if the hash is missing or does not match.
     */
    private void validateResponseIntegrity(RawHttpResponse<?> response, String body) {
        Optional<String> hashHeader = response.getHeaders().getFirst("X-Content-Hash");

        if (hashHeader.isPresent()) {
            String receivedHash = hashHeader.get();
            String computedHash = cryptoUtils.hash(body);

            if (!receivedHash.equals(computedHash)) {
                throw new ClientException("SECURITY ALERT: Response integrity check failed. " +
                        "Data may have been tampered with. " +
                        "Received: " + receivedHash + ", Computed: " + computedHash);
            }
        }
    }
}