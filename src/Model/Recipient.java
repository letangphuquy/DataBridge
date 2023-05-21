package Model;

public class Recipient {
    private String recipientID; 
    private char type;
    
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
