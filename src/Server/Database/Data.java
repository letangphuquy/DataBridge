package Server.Database;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Model.Password;
import Model.User;

/*
 * Contains data loaded from the database
 * Store data in memory for efficient access
 */

public class Data {
    static HashMap<String, ResultSetMetaData> metadataOf = new HashMap<>();

    public static Set<String> allIDs = new HashSet<>();
    public static HashMap<String,String> usernameToID = new HashMap<String,String>();
    public static HashMap<String,User> users = new HashMap<String,User>();
    public static HashMap<String,Password> passwordOf = new HashMap<String,Password>();
}
