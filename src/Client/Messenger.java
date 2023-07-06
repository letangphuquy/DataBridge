package Client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import Model.FileLink;
import Model.Group;
import Model.Message;
import Model.NormalMessage;
import Rules.ClientCode.*;
import Rules.Constants;

public class Messenger {
    private Messenger() {}
    private final static String D = String.valueOf(Constants.DELIMITER);
    private static Client client = Client.instance;

    //send by public id
    private static void send(String message, long receiverID, boolean isFile) throws IOException {
        message = Type.CHAT + D + Command.SEND + D + receiverID + D + isFile + D + message + D + new Timestamp(new Date().getTime());
        client.send(message);
    }

    public static void sendNormalChat(String message, long receiverID) throws IOException {
        send(message, receiverID, false);
    }

    public static void sendFileChat(String filePath, long receiverID) throws IOException {
        filePath = filePath.replace("/", "\\"); // for Windows
        send(filePath, receiverID, true);
    }

    public static void createGroup(String groupName) throws IOException {
        client.sendRequest(Type.CHAT + D + Command.CREATE + D + groupName);
    }

    private static void createGroupAccepted(long groupID, String name) {
        Data.conversations.put(groupID, (new Group(name)));
    }

    public static void addMember(long groupID, long userID) throws IOException {
        client.sendRequest(Type.CHAT + D + Command.ADD + D + groupID + D + userID);
    }

    //TODO: Consider completing this after implementing "Social" section (Friends, etc)
    private static void addMemberAccepted(long groupID, long userID) {
    }

    public static void removeMember(long groupID, long userID) throws IOException {
        // client.send(Type.CHAT + D + Command.REMOVE + D + groupID + D + userID);
    }

    public static void addAdmin(long groupID, long userID) throws IOException {
        client.send(Type.CHAT + D + Command.PROMOTE + D + groupID + D + userID);
    }

    public static void removeAdmin(long groupID, long userID) throws IOException {
        client.send(Type.CHAT + D + Command.DEMOTE + D + groupID + D + userID);
    }

    public static long getDialougeID(Message message) {
        return (message.getReceiverID() == client.user.getPublicID()) ? message.getSenderID() : message.getReceiverID();
    }

    public static void receiveChat(String[] messageParts) {
        boolean isFile = Boolean.parseBoolean(messageParts[3]);
        String content = messageParts[5]; // file ID or message content
        Message parentMessage = new Message(messageParts); // still using the constructor with messageID
        final Message message = isFile ? new FileLink(parentMessage, content) : new NormalMessage(parentMessage, content);
        long dialougeID = getDialougeID(message); // direct message or group message
        assert Data.conversations.containsKey(dialougeID); 
        Data.conversations.compute(dialougeID, (id, conversation) -> {
            return conversation.addMessage(message);
        });
        // System.out.println("Received message in " + dialougeID + ": " + content + " from " + senderID + " at " + sendTime);
        //TODO: TEST file sharing
        if (isFile) {
            System.out.println("File sharing is not supported yet");
            Data.sharedFiles.put(message.getMessageID(), (FileLink) message);
        }
    }

    public static void process(Command reqCommand, String[] requestParts, String[] responseParts) {
        switch (reqCommand) {
            case CREATE:
                createGroupAccepted(Long.parseLong(responseParts[0]), requestParts[0]);
                break;
            case ADD:
                addMemberAccepted(Long.parseLong(requestParts[0]), Long.parseLong(requestParts[1]));
                break;
            default:
        }
    }
}
