package Client;

import java.util.ArrayList;
import java.util.HashMap;

import Model.FileLink;
import Model.Message;

public class Data {
    private Data() {}
    public static HashMap<Long, ArrayList<Message>> conversations = new HashMap<>(); // publicID to messages
    // stores files?
    public static HashMap<Integer, FileLink> sharedFiles = new HashMap<>(); // messageID of FileLinks
}
