package Client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Model.FileLink;
import Model.Message;
import Model.NormalMessage;
import Rules.ClientCode;
import Rules.Constants;

public class Messenger {
    private Messenger() {}

    //send by public id
    private static void send(String message, long receiverID, boolean isFile) throws IOException {
        message = String.join(Constants.DELIMITER, new String[] {ClientCode.Type.CHAT.toString(), ClientCode.Command.SEND.toString(), String.valueOf(receiverID), String.valueOf(isFile), message, new Timestamp(new Date().getTime()).toString()});
        Client.instance.send(message);
    }

    public static void sendNormalChat(String message, long receiverID) throws IOException {
        send(message, receiverID, false);
    }

    public static void sendFileChat(String filePath, long receiverID) throws IOException {
        send(filePath, receiverID, true);
    }

    public static long getDialougeID(Message message) {
        return (message.getReceiverID() == Client.instance.user.getPublicID()) ? message.getSenderID() : message.getReceiverID();
    }

    public static void process(String[] messageParts) {
        boolean isFile = Boolean.parseBoolean(messageParts[3]);
        String content = messageParts[5]; // file ID or message content
        Message message = new Message(messageParts); // still using the constructor with messageID
        message = isFile ? new FileLink(message, content) : new NormalMessage(message, content);
        long dialougeID = getDialougeID(message); // direct message or group message
        if (!Data.conversations.containsKey(dialougeID)) 
            Data.conversations.put(dialougeID, new ArrayList<>());
        Data.conversations.get(dialougeID).add(message);
        // System.out.println("Received message in " + dialougeID + ": " + content + " from " + senderID + " at " + sendTime);
        //TODO: TEST file sharing
        if (isFile) {
            System.out.println("File sharing is not supported yet");
            Data.sharedFiles.put(message.getMessageID(), (FileLink) message);
        }
    }

}
