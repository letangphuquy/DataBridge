package Model;

import Rules.Constants;

public class User extends Recipient {
    /**
     * username: unique, max 30 characters, NOT contain SPACE.
	 * 		used for login only
	 * userID: unique 64-bit integer, used for database
	 * publicID: unique 64-bit integer, used for chat, to hide real ID
	 * name: reserved in first 60 characters of profile (too lazy to drop whole database and redesign)
     */
    private String username;
    private String profile;
    private boolean isBanned, isPrivate;
    private int reputation;

	// recipientID/ userID (private): is set automatically in database loading process, or when creating new user
    public User(String username) {
		super('U');
        this.username = username;
        this.profile = " ".repeat(Constants.NAME_MAX_LENGTH);
        this.isBanned = false;
        this.isPrivate = true;
        this.reputation = 0;
    }
	
	public User(String[] data) {
		super('U');
		// data[0] is recipientID/ userID
		this.username = data[1];
		this.profile = data[2];
		assert data[2].length() >= Constants.NAME_MAX_LENGTH;
		this.isBanned = Boolean.parseBoolean(data[3]);
		this.isPrivate = Boolean.parseBoolean(data[4]);
		this.reputation = Integer.parseInt(data[5]);
	}

    @Override
	protected Object clone() throws CloneNotSupportedException {
		User clone = new User(toString());
		clone.setIDs(toRecipient());
		return clone;
	}

	public static User parse(String[] data) { // for Client
		User user = new User(data);
		long publicID = Long.parseLong(data[0]);
		user.setIDs(publicID, publicID);
		return user;
	}

	public long getUserID() {
		try {
			return super.getRecipientID();
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.DEFAULT_ID;
		} 
    }

	public String getUsername() {
		return username;
	}

	public String getProfile() {
		return profile.substring(Constants.NAME_MAX_LENGTH);
	}

	public void setProfile(String profile) {
		this.profile = getName() + profile;
	}

	public void setName(String name) {
		int delta = name.length() - Constants.NAME_MAX_LENGTH;
		if (delta > 0)
			name = name.substring(0, Constants.NAME_MAX_LENGTH);
		if (delta < 0)
			name = name + " ".repeat(-delta);
		profile = name + getProfile();
	}
    
	public String getName() {
		return profile.substring(0, Constants.NAME_MAX_LENGTH);
	}

	public boolean isBanned() {
		return isBanned;
	}

	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    @Override
	public String toString() {
		return String.join(Constants.DELIMITER, Long.toString(getPublicID()), username, profile, Boolean.toString(isBanned), Boolean.toString(isPrivate), Integer.toString(reputation));
	}

	@Override
	public Object[] toObjectArray() {
		return new Object[] {getUserID(), username, profile, isBanned, isPrivate, reputation};
	}

	public Recipient toRecipient() {
		//Rare BUG encounter: Internal type of the Recipient is a User, so `return this` on super method returns User's information instead of Recipient's
		return super.getRecipient();
	}
}
