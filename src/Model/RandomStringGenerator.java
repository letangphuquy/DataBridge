package Model;
//alphanumeric
public class RandomStringGenerator {
    public static String randomString(int length) {
        int leftLimit = 32;
        int rightLimit = 126;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int ord = (int) Math.random() * (rightLimit - leftLimit + 1) + leftLimit;
            sb.append((char) ord);
        }
        return sb.toString();
    }
}
