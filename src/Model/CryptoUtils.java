package Model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * CryptoUtils - Centralized cryptographic utilities
 * 
 * Provides production-grade encryption/decryption using AES-256-GCM.
 * 
 * Security features:
 * - AES-256-GCM for authenticated encryption
 * - 96-bit (12-byte) nonces, randomly generated for each message
 * - 128-bit (16-byte) authentication tags
 * - Prevention of padding oracle attacks
 * - Built-in message authentication (no separate MAC needed)
 * 
 * GCM mode ensures:
 * - Confidentiality: AES encryption
 * - Authenticity: Built-in authentication tag
 * - Integrity: Tag verification detects tampering
 */
public class CryptoUtils {
    // AES-256 uses 256-bit (32-byte) keys
    public static final int KEY_SIZE_BITS = 256;
    public static final int KEY_SIZE_BYTES = KEY_SIZE_BITS / 8;
    
    // GCM mode parameters
    public static final int GCM_NONCE_LENGTH_BYTES = 12;    // 96-bit nonce (standard for GCM)
    public static final int GCM_TAG_LENGTH_BITS = 128;       // 128-bit authentication tag
    private static final int GCM_TAG_LENGTH_BYTES = GCM_TAG_LENGTH_BITS / 8;
    
    private static final String ALGORITHM = "AES";
    private static final String MODE_PADDING = "AES/GCM/NoPadding";
    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Encrypt data using AES-256-GCM
     * 
     * Returns ciphertext format: [nonce (12 bytes)][ciphertext + tag]
     * The nonce is prepended to enable proper decryption.
     * 
     * @param plaintext the data to encrypt
     * @param key the 256-bit encryption key
     * @return encrypted bytes with nonce prepended
     * @throws Exception if encryption fails
     */
    public static byte[] encryptAESGCM(byte[] plaintext, SecretKey key) throws Exception {
        if (plaintext == null || plaintext.length == 0) {
            throw new IllegalArgumentException("Plaintext cannot be empty");
        }
        if (key == null || key.getEncoded().length != KEY_SIZE_BYTES) {
            throw new IllegalArgumentException("Key must be 256-bit (32 bytes)");
        }
        
        try {
            // Generate random nonce
            byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTES];
            secureRandom.nextBytes(nonce);
            
            // Initialize cipher in ENCRYPT mode with GCM parameters
            Cipher cipher = Cipher.getInstance(MODE_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            
            // Encrypt plaintext
            byte[] ciphertext = cipher.doFinal(plaintext);
            
            // Combine nonce and ciphertext
            // Format: [nonce (12 bytes)][ciphertext+tag (variable)]
            byte[] result = new byte[GCM_NONCE_LENGTH_BYTES + ciphertext.length];
            System.arraycopy(nonce, 0, result, 0, GCM_NONCE_LENGTH_BYTES);
            System.arraycopy(ciphertext, 0, result, GCM_NONCE_LENGTH_BYTES, ciphertext.length);
            
            return result;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new Exception("AES-GCM algorithm not available", e);
        } catch (InvalidKeyException e) {
            throw new Exception("Invalid encryption key", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new Exception("Invalid GCM parameters", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("Encryption failed", e);
        }
    }
    
    /**
     * Decrypt data using AES-256-GCM
     * 
     * Expects input format: [nonce (12 bytes)][ciphertext + tag]
     * Verifies authentication tag before decryption.
     * 
     * @param encryptedData the encrypted bytes (with nonce prepended)
     * @param key the 256-bit decryption key
     * @return decrypted plaintext bytes
     * @throws Exception if decryption fails or authentication fails
     */
    public static byte[] decryptAESGCM(byte[] encryptedData, SecretKey key) throws Exception {
        if (encryptedData == null || encryptedData.length < GCM_NONCE_LENGTH_BYTES) {
            throw new IllegalArgumentException("Invalid encrypted data format");
        }
        if (key == null || key.getEncoded().length != KEY_SIZE_BYTES) {
            throw new IllegalArgumentException("Key must be 256-bit (32 bytes)");
        }
        
        try {
            // Extract nonce from beginning
            byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTES];
            System.arraycopy(encryptedData, 0, nonce, 0, GCM_NONCE_LENGTH_BYTES);
            
            // Extract ciphertext (everything after nonce)
            byte[] ciphertext = new byte[encryptedData.length - GCM_NONCE_LENGTH_BYTES];
            System.arraycopy(encryptedData, GCM_NONCE_LENGTH_BYTES, ciphertext, 0, ciphertext.length);
            
            // Initialize cipher in DECRYPT mode with GCM parameters
            Cipher cipher = Cipher.getInstance(MODE_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            
            // Decrypt and verify authentication tag
            // If tag is invalid, this throws BadPaddingException
            byte[] plaintext = cipher.doFinal(ciphertext);
            
            return plaintext;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new Exception("AES-GCM algorithm not available", e);
        } catch (InvalidKeyException e) {
            throw new Exception("Invalid decryption key", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new Exception("Invalid GCM parameters", e);
        } catch (BadPaddingException e) {
            // This means GCM tag verification failed - message is tampered
            throw new Exception("Authentication tag verification failed - message may be tampered", e);
        } catch (IllegalBlockSizeException e) {
            throw new Exception("Decryption failed", e);
        }
    }
    
    /**
     * Generate a random 256-bit encryption key
     * @return a new SecretKey suitable for AES-256
     * @throws NoSuchAlgorithmException if AES algorithm is not available
     */
    public static SecretKey generateAES256Key() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE_BITS, secureRandom);
        return keyGen.generateKey();
    }
    
    /**
     * Create a SecretKey from raw bytes
     * @param keyBytes the key bytes (must be 32 bytes for AES-256)
     * @return SecretKey for use in encryption operations
     */
    public static SecretKey createKeyFromBytes(byte[] keyBytes) {
        if (keyBytes == null || keyBytes.length != KEY_SIZE_BYTES) {
            throw new IllegalArgumentException("Key bytes must be exactly " + KEY_SIZE_BYTES + " bytes");
        }
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, ALGORITHM);
    }
    
    /**
     * Generate cryptographically secure random bytes
     * @param length the number of random bytes to generate
     * @return array of random bytes
     */
    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
    
    /**
     * Convert bytes to hexadecimal string
     * @param bytes the bytes to convert
     * @return hex representation
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
    
    /**
     * Convert hexadecimal string to bytes
     * @param hex the hex string
     * @return bytes representation
     * @throws IllegalArgumentException if hex string contains invalid characters
     */
    public static byte[] hexToBytes(String hex) {
        if (hex == null || hex.isEmpty()) {
            throw new IllegalArgumentException("Hex string cannot be null or empty");
        }
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int highDigit = Character.digit(hex.charAt(i), 16);
            int lowDigit = Character.digit(hex.charAt(i + 1), 16);
            
            if (highDigit == -1 || lowDigit == -1) {
                throw new IllegalArgumentException("Invalid hex character at position " + i);
            }
            
            data[i / 2] = (byte) ((highDigit << 4) + lowDigit);
        }
        return data;
    }
}
