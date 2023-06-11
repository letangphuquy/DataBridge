package Client;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import Model.E2ESocket;
import Rules.HostAddress;
import Rules.ServerCode;

public class Client extends E2ESocket {
    static Client instance = null;
    private PrintStream debugger = new PrintStream(System.out);
    private static final String URL = System.getProperty("user.home") + "\\.DataBridge\\";

    private Client(Socket socket) {
        super(socket, false);
        initDebugger();
    }
    
    void listenToServer() {
        String msg;
        try {
            while (user == null);
            while ((msg = in.readLine()) != null) {
                System.out.println("Received: " + msg);
                msg = secretMessenger.decryptStr(msg);
                System.out.println("Decrypted: " + msg);
                String[] parts = msg.split(" ");
                switch (ServerCode.valueOf(parts[0])) {
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error in listening and processing server's response. Server may be down");
            debug(e);
        } 
    }

    private void run() {
        // new Thread(() -> listenToServer()).start();
        try {
            user = Authenticator.login("test2", "1");
            System.out.println("Logged in: " + user);
            user = Authenticator.register("test2", "1");
            System.out.println("Registered: " + user);
            user = Authenticator.login("test2", "1");
            System.out.println("Logged in: " + user);
            FileProcessor.upload("E:/LQDOJ/translate-cp-handbook/book.pdf", ".");
            closeAll();
        } catch (IOException e) {
            System.out.println("Error login-ing");
            debug(e);
        } finally {
            debugger.close();
        }
    }
    
    public static void main(String[] args) {
        try {
            instance = new Client(new Socket(HostAddress.HOSTNAME, HostAddress.PORT));
            instance.run();
        } catch (UnknownHostException e) {
            System.out.println("Error connecting to server: Unknown host. Server may be down");
        } catch (IOException e) {
            System.out.println("Error connecting to server: I/O exception. Server may be down");
        }
    }
    
    private void debug(Exception e) {
        if (debugger != null) {
            debugger.println(e.getMessage());
            e.printStackTrace(debugger);
        }
    }
    
    private void initDebugger() {
        try {
            Path path = Paths.get(URL);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created directory: " + path.toString());
            }
            path = Paths.get(URL + "log.txt");
            boolean exists = Files.exists(path);
            if (!exists) Files.createFile(path);
            debugger = new PrintStream(new FileOutputStream(path.toFile(), true));
            if (!exists) {
                System.out.println("Created file: " + path.toString());
                debugger.println("LOGGING: place for all error occured");
            }
            debugger.println("----------------\nSession: " + LocalDateTime.now());
        } catch (FileNotFoundException e) {
            System.out.println("Error creating debug file");
        } catch (IOException e) {
            System.out.println("Error writing to debug file");
        }
    }
    
}
