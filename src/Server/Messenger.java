package Server;

import java.sql.Timestamp;

import Model.FileLink;
import Model.Message;
import Model.NormalMessage;
import Model.Recipient;
import Rules.ClientCode.Command;
import Server.Database.DatabaseUpdater;
import Server.Database.Data;

public class Messenger {
    private Messenger() {}

    public static void process(ServerThread serverThread, Command command, String[] params) {
        switch (command) {
            case SEND:
                receiveChat(serverThread, params);
                break;
            default:
                break;
        }
    }

    private static void receiveChat(ServerThread server, String[] params) {
        long senderID = server.user.getUserID();
        long receiverID = Long.parseLong(params[0]);
        receiverID = Data.publicIDToRecipientID.get(receiverID);
        boolean isFile = Boolean.parseBoolean(params[1]);
        String content = params[2];
        Timestamp sendTime = Timestamp.valueOf(params[3]);
        // message ID is handled by databases
        Message message = new Message(senderID, receiverID, isFile, sendTime);
        System.out.println("Received message from " + senderID + " to " + receiverID + ": " + content);
        message = (isFile) ? receiveFileChat(server, message, content) : receiveNormalChat(server, message, content);
        //TODO: save to database, send to receiver
        DatabaseUpdater.addMessage(message);
        Recipient receiver = Data.recipients.get(receiverID);
        if (receiver.getType() == 'U') {

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
