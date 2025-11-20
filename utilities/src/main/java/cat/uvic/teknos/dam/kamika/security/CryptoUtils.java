package cat.uvic.teknos.dam.kamika.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

/**
 * Utility class for performing cryptographic operations within the Kamika application.
 * It loads configuration (algorithm and salt) from {@code crypto.properties}.
 * Features:
 * Hashes Strings and Byte Arrays.
 * Applies a secret Salt to prevent Rainbow Table attacks.
 * Centralized configuration loading.
 * @author Montse Orozco
 * @version 2.0.2
 */
public class CryptoUtils {

    private static final String PROPERTIES_FILE = "/crypto.properties";
    private static final String ALGORITHM;
    private static final String SALT;

    static {
        Properties properties = new Properties();
        try (InputStream input = CryptoUtils.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Cannot find " + PROPERTIES_FILE + " in classpath.");
            }
            properties.load(input);
            ALGORITHM = properties.getProperty("security.hash.algorithm");
            SALT = properties.getProperty("security.hash.salt");

            if (ALGORITHM == null || ALGORITHM.isBlank()) {
                throw new RuntimeException("Property 'security.hash.algorithm' is missing.");
            }
            if (SALT == null) {
                throw new RuntimeException("Property 'security.hash.salt' is missing.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading crypto properties file", e);
        }
    }

    /**
     * Hashes a given input string using the configured algorithm and salt.
     * Delegates the logic to {@link #hash(byte[])} to avoid code duplication.
     *
     * @param input The plain text string to hash.
     * @return A Base64 encoded string representing the cryptographic hash.
     * @throws IllegalArgumentException if the input is null or blank.
     */
    public String hash(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input string to hash cannot be null or blank.");
        }
        return hash(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Hashes a given byte array using the configured algorithm and salt.
     * This is the core implementation of the hashing logic.
     *
     * @param inputBytes The byte array to hash.
     * @return A Base64 encoded string representing the cryptographic hash.
     * @throws IllegalArgumentException if the input array is null or empty.
     * @throws RuntimeException if the configured hashing algorithm is not available.
     */
    public String hash(byte[] inputBytes) {
        if (inputBytes == null || inputBytes.length == 0) {
            throw new IllegalArgumentException("Input bytes to hash cannot be null or empty.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] saltBytes = SALT.getBytes(StandardCharsets.UTF_8);

            digest.update(inputBytes);
            digest.update(saltBytes);

            byte[] hashBytes = digest.digest();

            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Configured hash algorithm '" + ALGORITHM + "' is not available.", e);
        }
    }
}