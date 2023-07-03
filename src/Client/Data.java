package Client;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Message;

public class Data {
    private Data() {}
    public static HashMap<Long, ArrayList<Message>> conversations = new HashMap<>(); // publicID to messages
    // stores files?
    public static HashMap<Integer, Message> sharedFiles = new HashMap<>(); // fileID to messageID (messageID will be used as proof to send request to server)
}
