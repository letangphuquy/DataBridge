package Model;

import java.util.ArrayList;

public class Group extends Recipient {
    String name;
    ArrayList<User> members = new ArrayList<>();

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

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public long getGroupID() {
        return super.getRecipientID();
    }
}
