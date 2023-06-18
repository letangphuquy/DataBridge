package Model;

import Rules.Constants;

public class Recipient {
    private long recipientID; 
	private long publicID; // used for chat, to hide real ID
    private char type;

    Recipient(char type) {
		recipientID = publicID = Constants.DEFAULT_ID;
        this.type = type;
    }

	Recipient(Recipient other) {
		recipientID = other.recipientID;
		publicID = other.publicID;
		type = other.type;
	}
	
	protected long getRecipientID() {
		return recipientID;
	}
	
	public long getPublicID() {
		return publicID;
	}

	public void setIDs(long recipientID, long publicID) {
		this.recipientID = recipientID;
		this.publicID = publicID;
	}
	public char getType() {
		return type;
	}

	public Object[] toObjectArray() {
		return new Object[] {recipientID, publicID, type};
	}

	protected Recipient getRecipient() {
		// Solution for the bug found at mentioned in User.java
		return new Recipient(this);
	}


}
