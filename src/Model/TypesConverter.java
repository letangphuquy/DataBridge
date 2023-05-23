package Model;
//Thanks Chat-GPT
public class TypesConverter {
    public static byte[] stringToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    // For debugging
    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) 
            System.out.print(b + " ");
        System.out.println();
    }
}
