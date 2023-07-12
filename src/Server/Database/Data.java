package Server.Database;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Model.DFile;
import Model.Group;
import Model.Message;
import Model.Password;
import Model.Recipient;
import Model.User;
import Model.UserPair;

/*
 * Contains data loaded from the database
 * Store data in memory for efficient access
 */

public class Data {
    private Data() {}
    static HashMap<String, ResultSetMetaData> metadataOf = new HashMap<>();
    static HashMap<String, String[]> columnsOf = new HashMap<>();

    // Users
    // Messages are also stored directly in Recipient objects
    public static HashMap<Long, Recipient> recipients = new HashMap<>(); // NOTE: ONLY purpose is to store IDs
    public static HashMap<Long, Long> publicIDToRecipientID = new HashMap<>();
    public static HashMap<Long, Long> recipientIDToPublicID = new HashMap<>();

    public static HashMap<String, Long> usernameToID = new HashMap<>();
    public static HashMap<String, Password> passwordOf = new HashMap<>();

    // Member and Admin are stored directly in Group objects
    public static HashMap<Long, Group> groups = new HashMap<>();

    public static HashMap<Long, User> users = new HashMap<>();
    public static HashSet<Long> publicUsers = new HashSet<>();
    public static HashMap<UserPair, UserPair> relationships = new HashMap<>();

    // Files
    public static HashMap<String, DFile> files = new HashMap<>();
    public static HashMap<String, String> idToPath = new HashMap<>(); // 1 to 1
    public static HashMap<String, String> pathToID = new HashMap<>();
    public static HashMap<String, ArrayList<String>> fileTree = new HashMap<>();

    public static int messageID = 0;
    public static HashMap<Integer, Message> messages = new HashMap<>(); 

    // User-related (for login)
    public static HashMap<Long, HashSet<Long>> friendsOf = new HashMap<>();
    public static HashMap<Long, HashSet<Long>> groupsOf = new HashMap<>();
    public static HashMap<Long, HashSet<String>> filesOf = new HashMap<>();
    public static HashMap<Long, ArrayList<Message>> messagesFrom = new HashMap<>();

    public static String[] getColumnsOf(String tableName) {
        if (!columnsOf.containsKey(tableName)) {
            var metadata = metadataOf.get(tableName);
            try {
                String[] columns = new String[metadata.getColumnCount()];
                for (int i = 0; i < columns.length; i++)
                    columns[i] = metadata.getColumnName(i+1);
                columnsOf.put(tableName, columns);
            } catch (SQLException e) {
                System.out.println("Error getting columns of table " + tableName);
                e.printStackTrace();
            }
        }
        return columnsOf.get(tableName);
    }
}
