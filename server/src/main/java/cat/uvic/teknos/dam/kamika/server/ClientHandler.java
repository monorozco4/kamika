package cat.uvic.teknos.dam.kamika.server;

import cat.uvic.teknos.dam.kamika.security.CryptoUtils;
import cat.uvic.teknos.dam.kamika.server.exceptions.BadRequestException;
import cat.uvic.teknos.dam.kamika.server.exceptions.MethodNotAllowedException;
import cat.uvic.teknos.dam.kamika.server.exceptions.NotFoundException;
import cat.uvic.teknos.dam.kamika.server.router.RequestRouter;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles individual client connections in a dedicated thread.
 * Orchestrates the Request -> Router -> Controller -> Response flow.
 * Security Implementation:
 * Validates {@code X-Content-Hash} for incoming requests with body.
 * Rejects tampered requests with HTTP 400 Bad Request.
 * Computes and attaches {@code X-Content-Hash} to outgoing responses.
 * Uses {@code eagerly()} loading to safely read streams multiple times.
 * @author Montse Orozco
 * @version 2.0.2
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final Socket clientSocket;
    private final RequestRouter router;
    private final RawHttp http;
    private final AtomicInteger activeClients;
    private final CryptoUtils cryptoUtils;

    public ClientHandler(Socket socket, RequestRouter router, AtomicInteger activeClients) {
        this.clientSocket = socket;
        this.router = router;
        this.http = new RawHttp();
        this.activeClients = activeClients;
        this.cryptoUtils = new CryptoUtils();
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        int currentClients = activeClients.incrementAndGet();
        logger.log(Level.INFO, "[" + threadName + "] Client connected. Total active: " + currentClients);

        try (clientSocket) {
            RawHttpRequest request = http.parseRequest(clientSocket.getInputStream()).eagerly();

            if (request.getBody().isPresent()) {
                String body = request.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
                Optional<String> hashHeader = request.getHeaders().getFirst("X-Content-Hash");

                if (hashHeader.isPresent()) {
                    String computedHash = cryptoUtils.hash(body);
                    if (!computedHash.equals(hashHeader.get())) {
                        logger.log(Level.SEVERE, "Security Alert: Hash mismatch from client " + clientSocket.getInetAddress());
                        RawHttpResponse<?> errorResponse = createErrorResponse(400, "Bad Request", "Integrity check failed.");
                        signAndSendResponse(errorResponse);
                        return;
                    }
                }
            }

            RawHttpResponse<?> response;
            try {
                response = router.route(request);
            } catch (NotFoundException e) {
                response = createErrorResponse(404, "Not Found", e.getMessage());
            } catch (BadRequestException e) {
                response = createErrorResponse(400, "Bad Request", e.getMessage());
            } catch (MethodNotAllowedException e) {
                response = createErrorResponse(405, "Method Not Allowed", e.getMessage());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Internal Error", e);
                response = createErrorResponse(500, "Internal Server Error", "Unexpected error.");
            }

            signAndSendResponse(response);

        } catch (IOException e) {
            logger.log(Level.WARNING, "Communication error", e);
        } finally {
            activeClients.decrementAndGet();
        }
    }

    private void signAndSendResponse(RawHttpResponse<?> response) throws IOException {
        if (response.getBody().isPresent()) {
            response = response.eagerly();
            String body = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
            String hash = cryptoUtils.hash(body);

            response = response.withHeaders(
                    response.getHeaders().and(
                            RawHttpHeaders.newBuilder().with("X-Content-Hash", hash).build()
                    )
            );
        }
        response.writeTo(clientSocket.getOutputStream());
    }

    private RawHttpResponse<?> createErrorResponse(int code, String status, String message) {
        String body = "{\"error\": \"" + status + "\", \"message\": \"" + message + "\"}";
        return http.parseResponse(
                "HTTP/1.1 " + code + " " + status + "\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length
        ).withBody(new StringBody(body));
    }
}