package Server;

import java.sql.Time;
import java.sql.Timestamp;

import Model.Message;
import Model.NormalMessage;
import Rules.ClientCode.Command;

public class Messenger {
    private Messenger() {}

    public static void process(ServerThread serverThread, Command command, String[] params) {
        switch (command) {
            case SEND:
                receiveChat(serverThread, params);
                break;
            default:
        }
    }

    private static void receiveChat(ServerThread server, String[] params) {
        long senderID = server.user.getUserID();
        long receiverID = Long.parseLong(params[0]);
        boolean isFile = Boolean.parseBoolean(params[1]);
        String content = params[2];
        Timestamp sendTime = Timestamp.valueOf(params[3]);
        // message ID is handled by databases
        Message message = new Message(senderID, receiverID, isFile, sendTime);
        System.out.println("Received message from " + senderID + " to " + receiverID + ": " + content);
        if (isFile) {
            receiveFileChat(server, message, content);
        } else {
            receiveNormalChat(server, message, content);
        }
        //TODO: save to database, send to receiver
    }

    private static void receiveNormalChat(ServerThread server, Message message, String content) {
        NormalMessage normalMessage = new NormalMessage(message, content);

    }
        
    private static void receiveFileChat(ServerThread server, Message message, String content) {
        
    }
}
