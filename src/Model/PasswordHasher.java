package Model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
    private PasswordHasher() {}
    private static final int ITERATIONS = 50000;
    private static final int PASSWORD_LENGTH = 64;
    public static String hash(String password, String salt) {
        //implements PBKDF2 with HMAC-SHA-256
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), TypesConverter.stringToBytes(salt), ITERATIONS, PASSWORD_LENGTH/2 * 8);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
            return TypesConverter.bytesToString(hash);
        } catch (Exception e) {
            System.out.println("Error finding algorithm for SecretKeyFactory, or invalid key spec to generate private key in hashing password");
        }
        return null;
    }
}
