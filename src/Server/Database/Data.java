package Server.Database;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Model.DFile;
import Model.Password;
import Model.User;

/*
 * Contains data loaded from the database
 * Store data in memory for efficient access
 */

public class Data {
    static HashMap<String, ResultSetMetaData> metadataOf = new HashMap<>();

    // Users
    public static Set<Long> recipientIDs = new HashSet<>();
    public static HashMap<Long, Long> publicIDToRecipientID = new HashMap<>();
    public static HashMap<Long, Long> recipientIDToPublicID = new HashMap<>(); //used only in loading phase
    public static HashMap<String,Long> usernameToID = new HashMap<>();
    public static HashMap<Long,User> users = new HashMap<>();
    public static HashMap<String,Password> passwordOf = new HashMap<>();

    // Files
    public static HashMap<String,DFile> files = new HashMap<>();
    public static HashMap<String,String> idToPath = new HashMap<>(); // 1 to 1
    public static HashMap<String,String> pathToID = new HashMap<>();
    public static HashMap<String, ArrayList<String>> fileTree = new HashMap<>();

}
