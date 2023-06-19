package Server.Database;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

import Model.DFile;
import Model.Group;
import Model.Password;
import Model.Recipient;
import Model.User;

/*
 * Contains data loaded from the database
 * Store data in memory for efficient access
 */

public class Data {
    static HashMap<String, ResultSetMetaData> metadataOf = new HashMap<>();

    // Users
    public static HashMap<Long , Recipient> recipients = new HashMap<>();
    public static HashMap<Long , Long> publicIDToRecipientID = new HashMap<>();
    public static HashMap<String , Long> usernameToID = new HashMap<>();
    public static HashMap<Long , User> users = new HashMap<>();
    public static HashMap<Long , Group> groups = new HashMap<>();
    public static HashMap<String , Password> passwordOf = new HashMap<>();

    // Files
    public static HashMap<String , DFile> files = new HashMap<>();
    public static HashMap<String , String> idToPath = new HashMap<>(); // 1 to 1
    public static HashMap<String , String> pathToID = new HashMap<>();
    public static HashMap<String , ArrayList<String>> fileTree = new HashMap<>();

    // Messages are stored directly in object (Recipient)
}
