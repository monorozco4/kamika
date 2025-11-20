package cat.uvic.teknos.dam.kamika.security.tests;

import cat.uvic.teknos.dam.kamika.security.CryptoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CryptoUtils} class.
 * Verifies:
 * Deterministic behavior (same input = same hash).
 * Correct handling of String vs Byte Array inputs.
 * Avalanche effect (small change = totally different hash).
 * Exception handling for invalid inputs.
 * @author Montse Orozco
 * @version 2.0.2
 */
class CryptoUtilsTest {

    private CryptoUtils cryptoUtils;

    @BeforeEach
    void setUp() {
        cryptoUtils = new CryptoUtils();
    }

    @Test
    @DisplayName("Test: Hash function is deterministic (String)")
    void testHashIsDeterministic() {
        String input = "mySecretPassword";
        String hash1 = cryptoUtils.hash(input);
        String hash2 = cryptoUtils.hash(input);

        assertNotNull(hash1);
        assertEquals(hash1, hash2, "Hashing the same string twice must produce the same hash.");
    }

    @Test
    @DisplayName("Test: Hash function is deterministic (byte[])")
    void testHashBytesIsDeterministic() {
        byte[] input = "mySecretData".getBytes(StandardCharsets.UTF_8);
        String hash1 = cryptoUtils.hash(input);
        String hash2 = cryptoUtils.hash(input);

        assertEquals(hash1, hash2, "Hashing the same bytes twice must produce the same hash.");
    }

    @Test
    @DisplayName("Test: String hash matches byte[] hash")
    void testStringAndBytesProduceSameHash() {
        String input = "ConsistencyCheck";

        String hashFromString = cryptoUtils.hash(input);
        String hashFromBytes = cryptoUtils.hash(input.getBytes(StandardCharsets.UTF_8));

        assertEquals(hashFromString, hashFromBytes, "hash(String) and hash(byte[]) must produce identical output.");
    }

    @Test
    @DisplayName("Test: Different input produces different hash")
    void testDifferentInputProducesDifferentHash() {
        String input1 = "helloWorld";
        String input2 = "helloWorld.";

        String hash1 = cryptoUtils.hash(input1);
        String hash2 = cryptoUtils.hash(input2);

        assertNotEquals(hash1, hash2, "Changing the input slightly must produce a completely different hash.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test: Null String throws IllegalArgumentException")
    void testNullStringInput(String nullInput) {
        assertThrows(IllegalArgumentException.class, () -> cryptoUtils.hash(nullInput));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t\n"})
    @DisplayName("Test: Blank/Empty String throws IllegalArgumentException")
    void testBlankStringInput(String blankInput) {
        assertThrows(IllegalArgumentException.class, () -> cryptoUtils.hash(blankInput));
    }

    @Test
    @DisplayName("Test: Null byte[] throws IllegalArgumentException")
    void testNullByteArrayInput() {
        byte[] nullBytes = null;
        assertThrows(IllegalArgumentException.class, () -> cryptoUtils.hash(nullBytes));
    }

    @Test
    @DisplayName("Test: Empty byte[] throws IllegalArgumentException")
    void testEmptyByteArrayInput() {
        byte[] emptyBytes = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> cryptoUtils.hash(emptyBytes));
    }
}