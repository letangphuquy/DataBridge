package Model;

public class Recipient {
    private String recipientID; 
    private char type;

    Recipient(char type) {
        // this.recipientID = RandomGenerator.randomString(Constants.RECIPIENT_ID_LENGTH);
		recipientID = null;
        this.type = type;
    }
	Recipient(String recipientID, char type) {
		this.recipientID = recipientID;
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

	public Object[] toObjectArray() {
		return new Object[] {recipientID, type};
	}
}
