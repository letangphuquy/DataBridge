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
            default:
                break;
        }
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
        
        ServerThread.activeUsers.get(userID).send(ServerCode.CHAT + D + String.join(D, parts));
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
            assert (receiver instanceof Group);
            Group group = (Group) receiver;
            var members = group.getMembers();
            for (var user : members) sendMessage(user.getUserID(), message);
        }
    }

    private static Message receiveNormalChat(ServerThread server, Message message, String content) {
        NormalMessage normalMessage = new NormalMessage(message, content);
        return normalMessage;
    }
        
    private static Message receiveFileChat(ServerThread server, Message message, String filepath) {
        filepath = FileProcessor.getUserRoot(server) + filepath;
        FileLink fileLink = new FileLink(message, filepath);
        return fileLink;
    }
}
