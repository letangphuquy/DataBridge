package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Rules.Constants;

public class Group extends Recipient {
    String name;
    ArrayList<User> members = new ArrayList<>();
    HashSet<Long> memberIDs = new HashSet<>();

    //Same note as User: recipientID / groupID is private, and is set automatically by the database during loading process or when creating new group
    public Group(String name) {
        super('G');
        this.name = name;
    }

    public Group(String[] args) {
        super('G');
        // args[0] is recipientID / groupID
        this.name = args[1];
    }

    public Group addMember(User user) {
        members.add(user);
        memberIDs.add(user.getUserID());
        return this;
    }

    public void removeMember(User user) {
        members.remove(user);
        memberIDs.remove(user.getUserID());
    }

    public long getGroupID() {
        try {
            return super.getRecipientID();
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.DEFAULT_ID;
        }
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return new ArrayList<User>(members);
    }

    public boolean hasMember(long userID) {
        return memberIDs.contains(userID);
    }

    
    @Override
    public Object[] toObjectArray() {
        return new Object[] { getGroupID(), name };
    }

    public Recipient toRecipient() {
		//Rare BUG encounter: Internal type of the Recipient is a User, so `return this` on super method returns User's information instead of Recipient's
		return super.getRecipient();
	}
}
