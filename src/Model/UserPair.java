package Model;

import Rules.Constants;
import Rules.Relationship;

public class UserPair implements DatabaseModel {
    User userA, userB;
    Relationship attitudeA, attitudeB, status;

    public UserPair(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
        this.attitudeA = Relationship.NONE;
        this.attitudeB = Relationship.NONE;
        this.status = Relationship.NONE;
    }

    private void setStatus() {
        status = Relationship.values()[
            Math.min(attitudeA.ordinal(), attitudeB.ordinal())
        ];
    }

    public void setAttitudeA(Relationship attitudeA) {
        this.attitudeA = attitudeA;
        setStatus();
    }

    public void setAttitudeB(Relationship attitudeB) {
        this.attitudeB = attitudeB;
        setStatus();
    }

    public void setAttitudes(Relationship newAttitudeA, Relationship newAttitudeB) {
        setAttitudeA(newAttitudeA);
        setAttitudeB(newAttitudeB);
    }
    
    public Relationship getAttitudeA() {
        return attitudeA;
    }

    public Relationship getAttitudeB() {
        return attitudeB;
    }

    public Relationship getStatus() {
        return status;
    }

    public long getUserA() {
        return userA.getUserID();
    }

    public long getUserB() {
        return userB.getUserID();
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserPair
            && ((UserPair) obj).userA.equals(userA)
            && ((UserPair) obj).userB.equals(userB);
    }

    @Override
    public int hashCode() {
        long determinant = userA.getUserID() * userB.getPublicID() - userA.getPublicID() * userB.getUserID();
        return (int) determinant;
    }

    @Override
    public Object[] toObjectArray() {
        return new Object[] { userA.getUserID(), userB.getUserID(), attitudeA.ordinal(), attitudeB.ordinal()};
    }

    @Override
    public String toString() {
        return String.join(Constants.DELIMITER, new String[] { userA.getPublicID() + "", userB.getPublicID() + "", attitudeA.toString(), attitudeB.toString()});
    }
}
