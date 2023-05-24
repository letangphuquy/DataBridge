package Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Password;
import Model.User;
import Rules.Constants;

public class DatabaseUpdater {
    private DatabaseUpdater() {}

    private static void addToTable(String tableName, String[] args) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        int numArgs = args.length;
        String command = "INSERT INTO " + tableName + " VALUES (" + String.join(",", "?".repeat(numArgs).split("")) + ")";
        PreparedStatement statement = connection.prepareStatement(command);
        for (int i = 0; i < numArgs; i++) {
            statement.setString(i+1, args[i]);
        }
        System.out.println("Executing command: " + command);
        statement.executeUpdate();
    }

    public static void addUser(User user, Password password) {
        Data.usernameToID.put(user.getUsername(), user.getUserID());
        Data.users.put(user.getUserID(), user);
        Data.passwordOf.put(user.getUsername(), password);
        try {
            addToTable("Recipients", new String[] {user.getUserID(), "U"});
            addToTable("Users", user.toString().split((String) Constants.DELIMITER));
            addToTable("Passwords", password.toString().split(" "));
        } catch (SQLException e) {
            System.out.println("Could not add user " + user.getUsername() + " to database");
            e.printStackTrace();
        }
    }
}
