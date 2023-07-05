package Client;

import java.util.HashMap;

import Model.FileLink;
import Model.Recipient;

public class Data {
    private Data() {}
    public static HashMap<Long, Recipient> conversations = new HashMap<>(); // publicID to messages
    // stores files?
    public static HashMap<Integer, FileLink> sharedFiles = new HashMap<>(); // messageID of FileLinks
}
