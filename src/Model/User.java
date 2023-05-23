package Model;

public class User extends Recipient {
    private String username;
    private String profile;
    private boolean isBanned, isPrivate;
    private int reputation;

    public User(String username) {
		super('U');
        this.username = username;
        this.profile = "";
        this.isBanned = false;
        this.isPrivate = true;
        this.reputation = 0;
    }

	public User(String[] data) {
		super('U');
		// data[0] is recipientID/ userID
		super.setRecipientID(data[0]);
		this.username = data[1];
		this.profile = data[2];
		this.isBanned = Boolean.parseBoolean(data[3]);
		this.isPrivate = Boolean.parseBoolean(data[4]);
		this.reputation = Integer.parseInt(data[5]);
	}

    public String getUserID() {
        return super.getRecipientID();
    }

	public String getUsername() {
		return username;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
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
		return getUserID() + " " + username + " " + profile + " " + isBanned + " " + isPrivate + " " + reputation;
	}
}
