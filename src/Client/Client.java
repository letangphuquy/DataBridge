package Client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import Model.SecretMessenger;
import Model.TypesConverter;
import Model.User;
import Rules.ClientCode;
import Rules.HostAddress;
import Rules.ServerCode;

public class Client {
    private static Client instance = null;
    private SecretMessenger secretMessenger = null;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private PrintWriter debugger = null; 
    private User user = null;
    private static final String URL = System.getProperty("user.home") + "\\AppData\\Roaming\\DataBridge\\";
    
    private Client() {
        initDebugger();
        try {
            socket = new Socket(HostAddress.HOSTNAME, HostAddress.PORT);
            System.out.println("Connected to server");
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String msg = in.readLine();
                byte[] serverPublicKey = TypesConverter.stringToBytes(msg.split(" ")[1]);
                doKeyExchange(serverPublicKey);
            } catch (IOException e) {
                System.out.println("Error in setting up I/O streams to communicate with server, or in key exchanging");
                debug(e);
            }
        } catch (UnknownHostException e) {
            System.out.println("Error in connecting to server: Unknown host");
            debug(e);
        } catch (IOException e) {
            System.out.println("Error in connecting to server: I/O exception. Server may be down");
            debug(e);
        }
    }
    
    private void initDebugger() {
        try {
            Path path = Paths.get(URL);
            boolean exists = Files.exists(path);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created directory: " + path.toString());
            }
            path = Paths.get(URL + "log.txt");
            debugger = new PrintWriter(new FileOutputStream(path.toFile(), true));
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
    
    private void doKeyExchange(byte[] serverPublicKey) throws IOException {
        secretMessenger = new SecretMessenger();
        sendPlainRequest(ClientCode.Type.KEY + " " + TypesConverter.bytesToString(secretMessenger.getPublicKey()));
        try {
            secretMessenger.generateSharedSecret(serverPublicKey);
        } catch (Exception e) {
            System.out.println("Error in generating shared secret");
            debug(e);
        }
    }
    
    private void sendPlainRequest(String request) throws IOException {
        out.write(request);
        out.newLine();
        out.flush();
    }

    private void sendRequest(String request) throws IOException {
        sendPlainRequest(secretMessenger.encrypt(request));
    }
    
    private void run() {
        new Thread(() -> listenToServer()).start();
        try {
            String msg = ClientCode.Type.AUTH + " " + ClientCode.Command.LOGIN + " sa 1";
            sendRequest(msg);
            String response = in.readLine();
            System.out.println(response);
            response = secretMessenger.decrypt(response);
            System.out.println(response);
        } catch (IOException e) {
            debug(e);
        }
    }
    
    void listenToServer() {
        String msg;
        try {
            while (user == null);
            while ((msg = in.readLine()) != null) {
                System.out.println("Received: " + msg);
                msg = secretMessenger.decrypt(msg);
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

    void closeAll() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    
    void debug(Exception e) {
        if (debugger != null) {
            debugger.println(e.getMessage());
            e.printStackTrace(debugger);
        }
    }
    public static void main(String[] args) {
        instance = new Client();
        instance.run();
    }
}
