package Model;

import java.sql.Timestamp;

import Rules.Constants;

public class FileLinks extends Message {
    String fileID;

    public FileLinks(long senderID, long receiverID, boolean isFile, Timestamp sendTime, String fileID) {
        super(senderID, receiverID, isFile, sendTime);
        this.fileID = fileID;
    }

    public FileLinks(Message other, String fileID) {
        super(other);
        this.fileID = fileID;
    }

    @Override
    public String toString() {
        return super.toString() + Constants.DELIMITER + fileID;
    }
}
