package Server.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import Model.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DatabaseLoader {
    private DatabaseLoader() {}

    static ArrayList<String[]> loadDatabase(String tableName) {
        Connection connection = DatabaseConnector.getConnection();
        ArrayList<String[]> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String command = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(command);

            ResultSetMetaData metadata = resultSet.getMetaData();
            if (!Data.metadataOf.containsKey(tableName))
                Data.metadataOf.put(tableName, metadata);
    		int numColumns = metadata.getColumnCount();
            
            while (resultSet.next()) {
                String[] args = new String[numColumns];
                for (int i = 0; i < numColumns; i++) {
                    args[i] = resultSet.getString(i+1);
                }
                result.add(args);
            }
        } catch (SQLException e) {
            System.out.println("Error creating, or executing statement");
            e.printStackTrace();
        }
        return result;
    }

    public static void loadAll() throws SQLException {
        loadUsers();
        loadFiles();
        loadMessages();
    }

    static void loadUsers() throws SQLException {
        var result = loadDatabase("Recipients");
        for (var args : result) {
            long recipientID = Long.parseLong(args[0]);
            long publicID = Long.parseLong(args[1]);
            char type = args[2].charAt(0);
            Recipient recipient = new Recipient(recipientID, publicID, type);
            System.out.println("Recipient " + recipientID + " has publicID " + publicID + " and type " + type);
            Data.recipients.put(recipientID, recipient);
            Data.publicIDToRecipientID.put(publicID, recipientID);
        }
        
        result = loadDatabase("Users");
        for (var args : result) {
            User user = new User(args);
            long userID = Long.parseLong(args[0]);
            assert(Data.recipients.containsKey(userID));
            Recipient recipient = Data.recipients.get(userID);
            System.out.println(userID + " Got " + recipient);
            user.setIDs(Data.recipients.get(userID));
            Data.users.put(userID, user);
            Data.usernameToID.put(user.getUsername(), userID);
        }
        
        result = loadDatabase("Passwords");
        for (var args : result) {
            Data.passwordOf.put(args[0], new Password(args[0], args[1], args[2]));
        }

        result = loadDatabase("Groups");
        for (var args : result) {
            Group group = new Group(args);
            long groupID = Long.parseLong(args[0]);
            group.setIDs(Data.recipients.get(groupID));
            Data.groups.put(groupID, group);
        }
     
        result = loadDatabase("GroupMembership");
        for (var args : result) {
            long groupID = Long.parseLong(args[0]);
            long userID = Long.parseLong(args[1]);
            Data.groups.get(groupID).addMember(Data.users.get(userID));
        }

        System.out.println("LOAD database:\nUsers: " + Data.users.size() + " users");
        System.out.println("Passwords: " + Data.passwordOf.size() + " passwords");
        for (var entry : Data.passwordOf.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue() + ".");
        }
    }

    private static void dfs(String node, String path) {
        if (!Data.fileTree.containsKey(node)) return ;
        Data.pathToID.put(path, node);
        Data.idToPath.put(node, path);
        System.out.println("File " + node + " has path " + path);
        for (String child : Data.fileTree.get(node)) {
            dfs(child, path + "\\" + Data.files.get(child).getFileName());
        }
    }

    static void loadFiles() throws SQLException {
        var result = loadDatabase("Files");
        ArrayList<String> roots = new ArrayList<>();
        for (var args : result) {
            DFile file = new DFile(args);
            String parent = file.getParentID();
            if (parent != null) {
                if (!Data.fileTree.containsKey(parent))
                    Data.fileTree.put(parent, new ArrayList<>());
                Data.fileTree.get(parent).add(file.getFileID());
            }
            else roots.add(file.getFileID());
            Data.files.put(file.getFileID(), file);
        }
        for (String root : roots) {
            DFile file = Data.files.get(root);
            dfs(root, file.getUploaderID() + "\\" + file.getFileName());
        }
    }

    static void loadMessages() throws SQLException {
        var result = loadDatabase("Messages");
        for (var args : result) {
            Message message = new Message(args);
            Data.recipients.get(message.getReceiverID()).addMessage(message);
            Data.messageID = Math.max(Data.messageID, message.getMessageID());
        }
        //TODO: FileLinks, NormalMessages database needs to be loaded?
    }
}
