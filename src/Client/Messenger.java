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

    public static void process(String[] messageParts) {
        long senderID = Long.parseLong(messageParts[1]);
        long receiverID = Long.parseLong(messageParts[2]);
        boolean isFile = Boolean.parseBoolean(messageParts[3]);
        Timestamp sendTime = Timestamp.valueOf(messageParts[4]);
        String content = messageParts[5]; // file ID or message content
        Message message = new Message(senderID, receiverID, isFile, sendTime);
        message = isFile ? new FileLink(message, content) : new NormalMessage(message, content);
        long dialougeID = (receiverID == Client.instance.user.getPublicID()) ? senderID : receiverID; // direct message or group message
        if (!Data.conversations.containsKey(dialougeID)) 
            Data.conversations.put(dialougeID, new ArrayList<>());
        Data.conversations.get(dialougeID).add(message);
        System.out.println("Received message in " + dialougeID + ": " + content + " from " + senderID + " at " + sendTime);
    }

}
