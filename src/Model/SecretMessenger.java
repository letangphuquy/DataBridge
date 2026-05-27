package Model;

import java.io.PrintStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * SecretMessenger - End-to-End Encrypted Communication Handler
 * 
 * Security Architecture:
 * 1. ECDH Key Exchange: secp256r1 elliptic curve for key negotiation
 * 2. Shared Secret: Derived from ECDH agreement
 * 3. Symmetric Encryption: AES-256-GCM for authenticated encryption
 * 
 * AES-256-GCM provides:
 * - Confidentiality: 256-bit AES encryption
 * - Authenticity: Galois/Counter Mode authentication tag
 * - Integrity: Detects any message tampering
 * - Nonce-based: Unique nonce per message prevents replay attacks
 * 
 * The upgrade from basic AES to AES-256-GCM:
 * - OLD: Cipher.getInstance("AES") - defaults to ECB mode (INSECURE!)
 * - NEW: Cipher.getInstance("AES/GCM/NoPadding") - authenticated encryption
 * 
 * Security implications:
 * - ECB mode had pattern leakage vulnerabilities
 * - GCM mode prevents padding oracle attacks
 * - Built-in authentication prevents forgery
 */
public class SecretMessenger {
    private static final String EC_ALGORITHM = "secp256r1";
    private static final String SYMMETRIC_ALGORITHM = "AES";
    
    private KeyPair keypair;
    private SecretKey sharedSecret;
    private PrintStream debugger = System.out;
    
    public SecretMessenger() {
        try {
            // Generate EC key pair for ECDH
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(new ECGenParameterSpec(EC_ALGORITHM));
            keypair = generator.generateKeyPair();
            System.out.println("SecretMessenger initialized with EC key pair (secp256r1)");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating key pair: no such algorithm");
            e.printStackTrace(debugger);
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Error generating key pair: invalid algorithm parameter");
            e.printStackTrace(debugger);
        }
    }

    public byte[] getPublicKey() {
        return keypair.getPublic().getEncoded();
    }

    /**
     * Perform ECDH key agreement to generate shared secret
     * The resulting shared secret must be at least 32 bytes (256-bit) for AES-256
     * 
     * @param otherPublicKey the peer's public key bytes
     * @throws Exception if key agreement fails
     */
    public void generateSharedSecret(byte[] otherPublicKey) throws Exception {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(keypair.getPrivate());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            KeySpec otherPublicKeySpec = new X509EncodedKeySpec(otherPublicKey);
            keyAgreement.doPhase(keyFactory.generatePublic(otherPublicKeySpec), true);
            
            // Generate shared secret - this is the ECDH agreement result
            byte[] agreedSecret = keyAgreement.generateSecret();
            
            // Ensure we have at least 256 bits (32 bytes) for AES-256
            if (agreedSecret.length < 32) {
                throw new Exception("Shared secret too short: " + agreedSecret.length + " bytes (minimum 32 needed)");
            }
            
            // Use first 32 bytes for AES-256
            // In production, consider using KDF (Key Derivation Function) instead
            byte[] aesKey = new byte[32];
            System.arraycopy(agreedSecret, 0, aesKey, 0, 32);
            
            sharedSecret = new SecretKeySpec(aesKey, 0, 32, SYMMETRIC_ALGORITHM);
            System.out.println("Shared secret generated successfully for AES-256-GCM");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error in key agreement: no such algorithm");
            e.printStackTrace(debugger);
            throw e;
        } catch (InvalidKeyException e) {
            System.out.println("Error in key agreement: invalid key");
            e.printStackTrace(debugger);
            throw e;
        } catch (InvalidKeySpecException e) {
            System.out.println("Error in key agreement: invalid key spec");
            e.printStackTrace(debugger);
            throw e;
        }
    }

    /**
     * Encrypt data using AES-256-GCM
     * Uses CryptoUtils for authenticated encryption
     * 
     * @param data the plaintext to encrypt
     * @return encrypted bytes (includes nonce and authentication tag)
     */
    private byte[] encrypt(byte[] data) {
        try {
            if (sharedSecret == null) {
                throw new IllegalStateException("Shared secret not initialized");
            }
            return CryptoUtils.encryptAESGCM(data, sharedSecret);
        } catch (Exception e) {
            System.out.println("Error encrypting data with AES-256-GCM");
            e.printStackTrace(debugger);
            return null;
        }
    }

    /**
     * Decrypt data using AES-256-GCM
     * Uses CryptoUtils for authenticated decryption and tag verification
     * 
     * @param data the encrypted bytes (includes nonce and authentication tag)
     * @return decrypted plaintext
     */
    private byte[] decrypt(byte[] data) {
        try {
            if (sharedSecret == null) {
                throw new IllegalStateException("Shared secret not initialized");
            }
            return CryptoUtils.decryptAESGCM(data, sharedSecret);
        } catch (Exception e) {
            System.out.println("Error decrypting data with AES-256-GCM: " + e.getMessage());
            e.printStackTrace(debugger);
            return null;
        }
    }

    public String encryptBytes(byte[] data) {
        byte[] encrypted = encrypt(data);
        if (encrypted == null) return null;
        return CryptoUtils.bytesToHex(encrypted);
    }

    public String encryptStr(String data) {
        return encryptBytes(data.getBytes());
    }

    public byte[] decryptBytes(String data) {
        try {
            byte[] encrypted = CryptoUtils.hexToBytes(data);
            return decrypt(encrypted);
        } catch (Exception e) {
            System.out.println("Error converting hex string to bytes: " + e.getMessage());
            e.printStackTrace(debugger);
            return null;
        }
    }
        
    public String decryptStr(String data) {
        byte[] decrypted = decryptBytes(data);
        if (decrypted == null) return null;
        return new String(decrypted);
    }

    public void setDebugger(PrintStream debugger) {
        this.debugger = debugger;
    }
}
