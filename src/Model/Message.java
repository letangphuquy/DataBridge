package Model;

import java.sql.Timestamp;

import Rules.Constants;

public class Message {
    int messageID;
    long senderID, receiverID;
    boolean isFile;
    Timestamp sendTime;

    public Message(long senderID, long receiverID, boolean isFile, Timestamp sendTime) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.isFile = isFile;
        this.sendTime = sendTime;
    }

    public Message(Message other) {
        messageID = other.messageID;
        senderID = other.senderID;
        receiverID = other.receiverID;
        isFile = other.isFile;
        sendTime = other.sendTime;
    }

    public Message(String[] args) {
        messageID = Integer.parseInt(args[0]);
        senderID = Long.parseLong(args[1]);
        receiverID = Long.parseLong(args[2]);
        isFile = Boolean.parseBoolean(args[3]);
        sendTime = Timestamp.valueOf(args[4]);
    }

    public long getReceiverID() {
        return receiverID;
    }

    @Override
    public String toString() {
        return String.join(Constants.DELIMITER, new String[] {String.valueOf(messageID), String.valueOf(senderID), String.valueOf(receiverID), String.valueOf(isFile), sendTime.toString()});
    }
}
