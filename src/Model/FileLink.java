package Model;

import java.sql.Timestamp;

import Rules.Constants;

public class FileLink extends Message {
    String fileID;

    public FileLink(long senderID, long receiverID, boolean isFile, Timestamp sendTime, String fileID) {
        super(senderID, receiverID, isFile, sendTime);
        this.fileID = fileID;
    }

    public FileLink(Message other, String fileID) {
        super(other);
        this.fileID = fileID;
    }
    
    public String getFileID() {
        return fileID;
    }
    
    @Override
    public String toString() {
        return super.toString() + Constants.DELIMITER + fileID;
    }
    
    @Override
    public Object[] toObjectArray() {
        return new Object[] {messageID, fileID};
    }

    // public Message toMessage() {
    //     return super.getMessage();
    // }
}
