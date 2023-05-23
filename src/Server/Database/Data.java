package Server.Database;

import java.util.HashMap;

import Model.Password;
import Model.User;

/*
 * Contains data loaded from the database
 * Store data in memory for efficient access
 */

public class Data {
    public static HashMap<String,String> usernameToID = new HashMap<String,String>();
    public static HashMap<String,User> users = new HashMap<String,User>();
    public static HashMap<String,Password> passwordOf = new HashMap<String,Password>();
}
