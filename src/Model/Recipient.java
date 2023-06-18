package Model;

import Rules.Constants;

public class Recipient {
    private long recipientID; 
	private long publicID; // used for chat, to hide real ID
    private char type;

    Recipient(char type) {
        // this.recipientID = RandomGenerator.randomString(Constants.RECIPIENT_ID_LENGTH);
		recipientID = Constants.DEFAULT_ID;
        this.type = type;
    }
	Recipient(long recipientID, char type) {
		this.recipientID = recipientID;
		this.type = type;
	}
	
	public long getRecipientID() {
		return recipientID;
	}
	public void setRecipientID(long recipientID) {
		this.recipientID = recipientID;
	}
	public char getType() {
		return type;
	}

	public Object[] toObjectArray() {
		return new Object[] {recipientID, type};
	}
}
