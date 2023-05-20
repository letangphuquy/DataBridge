package src;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server
 */
public class Server {
    public static final String HOST = "LAPTOP-03G6O7A4";
    public static final int PORT = 1234;
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}