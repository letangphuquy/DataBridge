package Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
// import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.DFile;
import Model.Password;
import Model.User;

public class DatabaseUpdater {
    private DatabaseUpdater() {}

    private static void addToTable(String tableName, Object[] args) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        synchronized (connection) {
            int numArgs = args.length;
            String command = "INSERT INTO " + tableName + " VALUES (" + String.join(",", "?".repeat(numArgs).split("")) + ")";
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
            addToTable("Recipients", user.toRecipient().toObjectArray());
            addToTable("Users", user.toObjectArray());
            addToTable("Passwords", password.toObjectArray());
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
            addToTable("Files", file.toObjectArray());
        } catch (SQLException e) {
            System.out.println("Could not add file " + file.getFileName() + "(" + file.getFileID() + ") to database");
            e.printStackTrace();
        }
    }
}
