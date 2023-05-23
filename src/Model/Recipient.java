package Model;

public class Recipient {
    private String recipientID; 
    private char type;
	private class RandomStringGenerator {
		// alphanumeric
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

    Recipient(char type) {
        this.recipientID = RandomStringGenerator.randomString(10);
        this.type = type;
    }
	public String getRecipientID() {
		return recipientID;
	}
	public void setRecipientID(String recipientID) {
		this.recipientID = recipientID;
	}
	public char getType() {
		return type;
	}
}
