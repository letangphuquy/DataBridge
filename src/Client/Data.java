package Client;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Message;

public class Data {
    private Data() {}
    public static HashMap<Long, ArrayList<Message>> conversations = new HashMap<>(); // publicID to messages
}
