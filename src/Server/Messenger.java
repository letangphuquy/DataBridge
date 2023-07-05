package Server;

import java.io.IOException;
import java.sql.Timestamp;

import Model.FileLink;
import Model.Group;
import Model.Message;
import Model.NormalMessage;
import Model.Recipient;
import Rules.Constants;
import Rules.ServerCode;
import Rules.ClientCode.Command;
import Server.Database.DatabaseUpdater;
import Server.Database.Data;

public class Messenger {
    private Messenger() {}
    private static final String D =  (String) Constants.DELIMITER;

    public static void process(ServerThread serverThread, Command command, String[] params) throws IOException {
        switch (command) {
            case SEND:
                receiveChat(serverThread, params);
                break;
            case CREATE:
                createGroup(serverThread, params[0], Integer.parseInt(params[1]));
                break;
            case ADD:
                addGroupMember(serverThread, Long.parseLong(params[0]), Long.parseLong(params[1]), Integer.parseInt(params[2]));
                break;
            default:
                break;
        }
    }

    private static void createGroup(ServerThread server, String groupName, int requestID) throws IOException {
        Group group = new Group(groupName);
        group.setIDs(Recipient.randomRecipient());
        DatabaseUpdater.addGroup(group);
        DatabaseUpdater.addGroupMember(group.getGroupID(), server.user.getUserID());
        DatabaseUpdater.addGroupAdmin(group.getGroupID(), server.user.getUserID());
        server.send(ServerCode.ACCEPT + D + requestID + D + group.getPublicID());
    }

    private static void addGroupMember(ServerThread server, long groupID, long userID, int requestID) throws IOException {
        Group group = (Group) Data.recipients.get(groupID);
        if (group == null) {
            server.send(ServerCode.REJECT + D + requestID + D + "Group does not exist");
            return;
        }
        userID = Data.publicIDToRecipientID.get(userID);
        if (!group.hasAdmin(userID)) {
            server.send(ServerCode.REJECT + D + requestID + D + "User is not allowed to add");
            return;
        }
        if (group.hasMember(userID)) {
            server.send(ServerCode.REJECT + D + requestID + D + "User is already in group");
            return;
        }
        server.send(ServerCode.ACCEPT + D + requestID);
        DatabaseUpdater.addGroupMember(groupID, userID);
    }

    private static void sendMessage(long userID, Message message) throws IOException {
        /*
         * Obfuscation rules:
         * 1. public ID is used instead of private recipient ID
         * 2. file ID is left plain ??? TODO: find a way to "hide" file ID
         */
        String[] parts = message.toString().split(D); // needs sub-type
        parts[1] = Data.recipientIDToPublicID.get(Long.parseLong(parts[1])).toString(); // sender ID
        parts[2] = Data.recipientIDToPublicID.get(Long.parseLong(parts[2])).toString(); // receiver ID
        
        ServerThread server = ServerThread.activeUsers.get(userID);
        if (server != null)
            server.send(ServerCode.CHAT + D + String.join(D, parts));
    }

    private static void receiveChat(ServerThread server, String[] params) throws IOException {
        long senderID = server.user.getUserID();
        long receiverID = Long.parseLong(params[0]);
        System.out.println("Receiver publid ID = " + receiverID);
        receiverID = Data.publicIDToRecipientID.get(receiverID);
        boolean isFile = Boolean.parseBoolean(params[1]);
        String content = params[2];
        Timestamp sendTime = Timestamp.valueOf(params[3]);
        // message ID is handled by databases
        Message message = new Message(senderID, receiverID, isFile, sendTime);
        System.out.println("Received message from " + senderID + " to " + receiverID + ": " + content);
        message = (isFile) ? receiveFileChat(server, message, content) : receiveNormalChat(server, message, content);
        //TODO: test sending to receiver
        DatabaseUpdater.addMessage(message);
        Recipient receiver = Data.recipients.get(receiverID);
        if (receiver.getType() == 'U') {
            sendMessage(receiverID, message);
        } else {
            Group group = Data.groups.get(receiverID);
            var members = group.getMembers();
            for (var user : members) sendMessage(user.getUserID(), message);
        }
    }

    private static Message receiveNormalChat(ServerThread server, Message message, String content) {
        NormalMessage normalMessage = new NormalMessage(message, content);
        return normalMessage;
    }
        
    private static Message receiveFileChat(ServerThread server, Message message, String filepath) {
        filepath = server.user.getUserID() + "\\" + filepath;
        String fileID = Data.pathToID.get(filepath);
        System.out.println("From filepath " + filepath + " got file ID " + fileID);
        FileLink fileLink = new FileLink(message, fileID);
        return fileLink;
    }

    public static boolean checkUserReceivedMessage(long userID, Message message) {
        long recipientID = message.getReceiverID();
        Recipient receiver = Data.recipients.get(recipientID);
        if (receiver.getType() == 'U')
            return (recipientID == userID);
        return Data.groups.get(recipientID) .hasMember(userID);
    }
}
