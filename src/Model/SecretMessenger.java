package Model;

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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/*
 * SecretMessenger.java
 * Elliptic Curve Diffie-Hellman key exchange
 * Two parties generate shared secret key by first exchanging public keys
 */

public class SecretMessenger {
    private static final String EC_ALGORITHM = "secp256r1";
    private static final String SYMMETRIC_ALGORITHM = "AES";

    private KeyPair keypair;
    private SecretKeySpec sharedSecret;
    private Cipher cipher;
    
    public SecretMessenger() {
        try {
            cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error in creation of Cipher object: no such algorithm");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.out.println("Error in creation of Cipher object: no such padding");
            e.printStackTrace();
        }

        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(new ECGenParameterSpec(EC_ALGORITHM));
            keypair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating key pair: no such algorithm");
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Error generating key pair: invalid algorithm parameter");
            e.printStackTrace();
        }
    }

    public byte[] getPublicKey() {
        return keypair.getPublic().getEncoded();
    }

    public void generateSharedSecret(byte[] otherPublicKey) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, InvalidKeySpecException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(keypair.getPrivate());
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        KeySpec otherPublicKeySpec = new X509EncodedKeySpec(otherPublicKey);
        keyAgreement.doPhase(keyFactory.generatePublic(otherPublicKeySpec), true);
        sharedSecret = new SecretKeySpec(keyAgreement.generateSecret(), SYMMETRIC_ALGORITHM);
    }

    public byte[] encrypt(byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, sharedSecret);
            return cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            System.out.println("Error encrypting data, in initialization of Cipher object: invalid key");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("Error encrypting data: illegal block size");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("Error encrypting data: bad padding");
            e.printStackTrace();
        }
        return null;
    }

    public String encrypt(String data) {
        return TypesConverter.bytesToString(encrypt(data.getBytes()));
    }

    public byte[] decrypt(byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, sharedSecret);
            return cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            System.out.println("Error decrypting data, in initialization of Cipher object: invalid key");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("Error decrypting data: illegal block size");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("Error decrypting data: bad padding");
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String data) {
        return new String(decrypt(TypesConverter.stringToBytes(data)));
    }
}
