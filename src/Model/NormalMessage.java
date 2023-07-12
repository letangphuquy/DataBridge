package Model;

import java.sql.Timestamp;

import Rules.Constants;

public class NormalMessage extends Message {
    String content;

    public NormalMessage(long senderID, long receiverID, boolean isFile, Timestamp sendTime, String content) {
        super(senderID, receiverID, isFile, sendTime);
        this.content = content;
    }

    public NormalMessage(Message other, String content) {
        super(other);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Message makeCopy() {
        return new NormalMessage(this, content);
    }

    @Override
    public String toString() {
        return super.toString() + Constants.DELIMITER + content;
    }

    @Override
    public Object[] toObjectArray() {
        return new Object[] {messageID, content};
    }

    // public Message toMessage() {
    //     return super.getMessage();
    // }
}
