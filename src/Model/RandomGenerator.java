package Model;

import java.security.SecureRandom;

//alphanumeric
public class RandomGenerator {
    private static int SALT_LENGTH = 16;
    /**
     * @param length
     * @return random username with given length (printable characters, except space)
     */
    public static String randomString(int length) {
        int leftLimit = 33; // exlude space
        int rightLimit = 126;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int ord = (int) (Math.random() * (rightLimit - leftLimit + 1)) + leftLimit;
            sb.append((char) ord);
        }
        return sb.toString();
    }
    public static byte[] randomSalt() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            System.out.println("Error finding algorithm or provider for SecureRandom in creation of salt");
        } 
        return null;
    }
}
