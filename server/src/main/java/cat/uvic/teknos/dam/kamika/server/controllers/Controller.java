package cat.uvic.teknos.dam.kamika.server.controllers;

import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

/**
 * Defines the contract for all controllers in the application.
 * Each controller must be able to handle an HTTP request and return an HTTP response.
 * @author Montse
 * @version 2.0.0
 */
public interface Controller {
    /**
     * Handles an incoming HTTP request and produces a response.
     *
     * @param request The raw HTTP request from the client.
     * @return A RawHttpResponse object to be sent to the client.
     * @throws Exception if any error occurs during request processing.
     */
    RawHttpResponse<?> handle(RawHttpRequest request) throws Exception;
}