package Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
// import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.DFile;
import Model.FileLink;
import Model.Message;
import Model.NormalMessage;
import Model.Password;
import Model.Recipient;
import Model.User;

public class DatabaseUpdater {
    private DatabaseUpdater() {}

    private static void addToTable(String tableName, Object[] args, String[] columns) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        synchronized (connection) {
            int numArgs = args.length;
            String command = "INSERT INTO " + tableName;
            if (columns != null)
                command += " (" + String.join(",", columns) + ") ";
            command += " VALUES (" + String.join(",", "?".repeat(numArgs).split("")) + ")";
            PreparedStatement statement = connection.prepareStatement(command);
            // ResultSetMetaData metadata = Data.metadataOf.get(tableName);
            for (int i = 0; i < numArgs; i++) {
                if (args[i] instanceof Character)
                    args[i] += "";
                statement.setObject(i+1, args[i]);
                // System.out.println("Set type " + metadata.getColumnType(i+1) + " for column " + metadata.getColumnName(i+1) + " in table " + tableName);
            }
            System.out.println("Executing command: " + command);
            statement.executeUpdate();
        }
    }

    public static void addUser(User user, Password password) {
        Data.usernameToID.put(user.getUsername(), user.getUserID());
        Data.users.put(user.getUserID(), user);
        Data.passwordOf.put(user.getUsername(), password);
        try {
            Object[] args = user.toRecipient().toObjectArray();
            System.out.println("Adding recipient: ");
            for (Object arg : args)
                System.out.print(arg + " ");
            System.out.println();
            addToTable("Recipients", user.toRecipient().toObjectArray(), null);
            addToTable("Users", user.toObjectArray(), null);
            addToTable("Passwords", password.toObjectArray(), null);
        } catch (SQLException e) {
            System.out.println("Could not add user " + user.getUsername() + " to database");
            e.printStackTrace();
        }
    }

    public static void addFile(String path, DFile file) {
        Data.files.put(file.getFileID(), file);
        Data.idToPath.put(file.getFileID(), path);
        Data.pathToID.put(path, file.getFileID());
        if (!Data.fileTree.containsKey(path))
            Data.fileTree.put(path, new ArrayList<String>());
        else
            Data.fileTree.get(path).add(file.getFileID());

        try {
            addToTable("Files", file.toObjectArray(), null);
        } catch (SQLException e) {
            System.out.println("Could not add file " + file.getFileName() + "(" + file.getFileID() + ") to database");
            e.printStackTrace();
        }
    }

    private static String[] messageColumns;
    private static void getMessageColumns() {
        if (messageColumns != null) return;
        var metadata = Data.metadataOf.get("Messages");
        try {
            messageColumns = new String[metadata.getColumnCount() - 1];
            for (int i = 0; i < messageColumns.length; i++)
                messageColumns[i] = metadata.getColumnName(i+2); // skips messageID column
        } catch (SQLException e) {
            System.out.println("Error saving message to DB: could not get metadata");
            e.printStackTrace();
        }
    }

    public static void addMessage(Message message) {
        long receiverID = message.getReceiverID();
        Data.recipients.compute(receiverID, (Long id, Recipient recipient) -> {
            return recipient.addMessage(message);
        });
        getMessageColumns();
        try {
            // or else it would be interpreted as a subclass of Message
            addToTable("Messages", message.getMessage().toObjectArray(), messageColumns);
            message.setID(++Data.messageID); 
            if (message instanceof FileLink) {
                addToTable("FileLinks", ((FileLink) message).toObjectArray(), null);
            } else {
                assert(message instanceof NormalMessage);
                addToTable("NormalMessages", ((NormalMessage) message).toObjectArray(), null);
            }
        } catch (SQLException e) {
            System.out.println("Could not add message " + message + " to database");
            e.printStackTrace();
        }

    }
}
