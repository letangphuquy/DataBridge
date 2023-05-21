package Server.Database;

import java.sql.Connection;
import java.sql.Statement;

import Model.*;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DatabaseLoader {
    final static String DBNAME = "DataBridge";
    final static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    final static String URL = "jdbc:sqlserver://localhost:1433;databaseName=" + DBNAME + ";encrypt=true;trustServerCertificate=true";
    static boolean isInitialized = false;
    static Connection connection = null;

    private DatabaseLoader() {}
    
    private static void init() {
        isInitialized = true;
        try {
            Class.forName(DRIVER);
            System.out.println("Enter database's username & password");
            System.out.print("Username: ");
            String username = System.console().readLine();
            System.out.print("Password: ");
            String password = new String(System.console().readPassword());
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading JDBC driver (MS SQL Server)");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    static ResultSet result;
    static int loadDatabase(String tableName) {
        if (!isInitialized) init();
        try {
            Statement statement = connection.createStatement();
            String command = "SELECT * FROM " + tableName;
            result = statement.executeQuery(command);

            ResultSetMetaData metadata = result.getMetaData();
    		int numColumns = metadata.getColumnCount();
            return numColumns;
            
        } catch (SQLException e) {
            System.out.println("Error creating, or executing statement");
            e.printStackTrace();
        }
        return 0;
    }

    public static void loadAll() throws SQLException {
        int numColumns = loadDatabase("Users");
        String[] args = new String[numColumns];
        while (result.next()) {
            for (int i = 0; i < numColumns; i++)
                args[i] = result.getString(i+1);
            User user = new User(args);
            Data.users.put(user.getUserID(), user);
            Data.usernameToID.put(user.getUsername(), user.getUserID());
        }
        numColumns = loadDatabase("Passwords");
        args = new String[numColumns];
        while (result.next()) {
            for (int i = 0; i < numColumns; i++)
                args[i] = result.getString(i+1);
            Data.passwordOf.put(args[0], new Password(args[0], args[1], args[2]));
        }
    }
}
