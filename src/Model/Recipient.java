package Model;

import java.util.ArrayList;

import Rules.Constants;

public class Recipient implements DatabaseModel {
    private long recipientID; 
	private long publicID; // used for chat, to hide real ID
    private char type; // 'U' for user, 'G' for group
	private ArrayList<Message> messages = new ArrayList<>();

    public Recipient(char type) {
		recipientID = publicID = Constants.DEFAULT_ID;
        this.type = type;
    }

	public Recipient(long recipientID, long publicID, char type) {
		this.recipientID = recipientID;
		this.publicID = publicID;
		this.type = type;
	}

	public Recipient(Recipient other) {
		recipientID = other.recipientID;
		publicID = other.publicID;
		type = other.type;
	}
	
	public void setIDs(long recipientID, long publicID) {
		this.recipientID = recipientID;
		this.publicID = publicID;
	}

	public void setIDs(Recipient other) {
		recipientID = other.recipientID;
		publicID = other.publicID;
	}

	protected long getRecipientID() throws Exception {
		if (recipientID == Constants.DEFAULT_ID) 
			throw new Exception("RecipientID not set");
		return recipientID;
	}
	
	public long getPublicID() {
		return publicID;
	}
	public char getType() {
		return type;
	}

	public Recipient addMessage(Message message) {
		messages.add(message);
		return this;
	}

	@Override
	public Object[] toObjectArray() {
		return new Object[] {recipientID, publicID, type};
	}

	protected Recipient getRecipient() {
		// Solution for the bug found as mentioned in User.java
		return new Recipient(this);
	}
}
