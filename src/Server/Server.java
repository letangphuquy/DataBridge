package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

import Rules.HostAddress;
import Server.Database.DatabaseLoader;

/**
 * Server
 */
public class Server {
    public static void main(String[] args) {
        try {
            DatabaseLoader.loadAll();
        } catch (SQLException e) {
            System.out.println("Error reading tables from database");
            e.printStackTrace();
        }
        final int PORT = HostAddress.PORT;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                new Thread(new ServerThread(serverSocket.accept())).start();
            }
        } catch (IOException e) {
            System.out.println("Error: ServerSocket couldn't be created OR could not accept client connection");
            e.printStackTrace();
        }
    }
}