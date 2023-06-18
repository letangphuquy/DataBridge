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
    public static Set<Long> allReIDs = new HashSet<>();
    public static HashMap<String,Long> usernameToID = new HashMap<String,Long>();
    public static HashMap<Long,User> users = new HashMap<Long,User>();
    public static HashMap<String,Password> passwordOf = new HashMap<String,Password>();

    // Files
    public static HashMap<String,DFile> files = new HashMap<String,DFile>();
    public static HashMap<String,String> idToPath = new HashMap<String,String>(); // 1 to 1
    public static HashMap<String,String> pathToID = new HashMap<String,String>();
    public static HashMap<String, ArrayList<String>> fileTree = new HashMap<String, ArrayList<String>>();

}
