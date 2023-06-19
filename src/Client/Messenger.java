package Client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

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


}
