package Server.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private final static String DBNAME = "DataBridge";
    private final static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final static String URL = "jdbc:sqlserver://localhost:1433;databaseName=" + DBNAME + ";encrypt=true;trustServerCertificate=true";
    private static int numTries = 0;
    static Connection connection = null;

    private DatabaseConnector() {}
    public static Connection getConnection() {
        final int MAX_TRIES = 3;
        while (connection == null) {
            if (numTries < MAX_TRIES) init();
            else {
                System.out.println("Too many failed attempts. Exiting...");
                System.exit(1);
            }
        }
        return connection;
    }
    
    private static void init() {
        ++numTries;
        try {
            Class.forName(DRIVER);
            System.out.println("Enter database's username & password");
            System.out.print("Username: ");
            String username = System.console().readLine();
            System.out.print("Password: ");
            String password = new String(System.console().readPassword());
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Connected!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading JDBC driver (MS SQL Server)");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }
}
