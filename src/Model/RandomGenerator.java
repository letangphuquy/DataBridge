package Model;

import java.security.SecureRandom;
import java.util.ArrayList;

//alphanumeric
public class RandomGenerator {
    static SecureRandom random = new SecureRandom();
    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Exception e) {
            System.out.println("Error finding algorithm or provider for SecureRandom");
        }
    }
    private RandomGenerator() {}
    private static int SALT_LENGTH = 16;

    private static boolean intialized = false;
    private static ArrayList<Character> alphanumeric, printable; 
    private static void createAlphabets() {
        if (intialized) return;
        intialized = true;
        printable = new ArrayList<Character>();
        alphanumeric = new ArrayList<Character>();
        // Printable ASCII characters
        int leftLimit = 33; // exlude space
        int rightLimit = 126;
        for (int i = leftLimit; i <= rightLimit; i++)
            printable.add((char) i);
        // Alphanumeric ASCII characters
        for (int i = (int) '0'; i <= (int) '9'; i++)
            alphanumeric.add((char) i);
        for (int i = (int) 'A'; i <= (int) 'Z'; i++)
            alphanumeric.add((char) i);
        for (int i = (int) 'a'; i <= (int) 'z'; i++)
            alphanumeric.add((char) i);
        System.out.println(printable.size() + " vs. " + alphanumeric.size());
    }

    private static String randomString(int length, ArrayList<Character> alphabet) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int ord = (int) (Math.random() * alphabet.size());
            sb.append(alphabet.get(ord));
        }
        return sb.toString();
    }

    // for fileID
    public static String randomString(int length) {
        createAlphabets();
        return randomString(length, printable);
    }

    /*
    Currently unused
    public static String randomReadableString(int length) {
        createAlphabets();
        return randomString(length, alphanumeric);
    } 
     */

    public static long randomID() {
        return random.nextLong();
    }

    public static byte[] randomSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
}
