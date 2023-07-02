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
import java.util.ArrayList;
import java.util.stream.Stream;

import Model.E2ESocket;
import Rules.ClientCode;
import Rules.Constants;
import Rules.HostAddress;
import Rules.ServerCode;

public class Client extends E2ESocket {
    static Client instance = null;
    private PrintStream debugger = new PrintStream(System.out);
    static final String URL = System.getProperty("user.home") + "\\.DataBridge\\";

    ArrayList<String> requests = new ArrayList<>();
    ArrayList<Thread> independentThreads = new ArrayList<>();

    private Client(Socket socket) {
        super(socket, false);
        initDebugger();
    }

    @Override
    public void send(String msg) throws IOException {
        // System.out.println("Hi, I'm going to send: " + msg);
        super.send(msg);
    }
    
    Thread serverListener = new Thread(new Runnable() {
        //How to stop listening when logout?
        //https://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
        //https://stackoverflow.com/questions/671049/how-do-you-kill-a-thread-in-java
        //Problem: .read() is called before logout, so it's blocked
        //Solution: close the socket
        @Override
        public void run() {
            while (user != null) {
                System.out.println("\tListener running");
                try {
                    String msg = read();
                    if (msg == null) break;
                    String displayMsg = msg.length() > 100 ? msg.substring(0, 100) + "..." : msg;
                    System.out.println("Received: " + displayMsg);
                    String[] responseParts = msg.split((String) Constants.DELIMITER);
                    String response = responseParts[0];

                    int requestID = -1;
                    String request, requestParts[] = null, type, command;
                    ClientCode.Type reqType = ClientCode.Type.AUTH;
                    ClientCode.Command reqCommand = ClientCode.Command.LOGOUT;
                    
                    if (!ServerCode.CHAT.toString().equals(responseParts[0])) {
                        requestID = Integer.parseInt(responseParts[1]);
                        request = requests.get(requestID);
                        requestParts = request.split((String) Constants.DELIMITER);
                        type = requestParts[0];
                        command = requestParts[1];
                        reqType = ClientCode.Type.valueOf(type);
                        reqCommand = ClientCode.Command.valueOf(command);
                        requestParts = Stream.of(requestParts).skip(2).toArray(String[]::new);
                        responseParts = Stream.of(responseParts).skip(2).toArray(String[]::new);
                    }
                    System.out.println("Hello " + ServerCode.valueOf(response));
                    switch (ServerCode.valueOf(response)) {
                        case ACCEPT:
                            switch (reqType) {
                                case FILE:
                                    FileProcessor.process(reqCommand, requestID, responseParts);
                                    break;
                                default:
                                    System.out.println("Message type not recognized " + reqType);
                            }
                            // IMPORTANT: Must not remove requestID from requests here, or else all requests will be shifted
                            // requests.remove(requestID);
                            break;
                        case REJECT:
                            System.out.println("Request rejected");
                            break;
                        case DATA:
                            switch (reqType) {
                                case FILE:
                                    FileProcessor.process(reqCommand, requestID, responseParts);
                                    break;
                                default: ;
                            }
                            break;
                        case CHAT:
                            responseParts = Stream.of(responseParts).skip(1).toArray(String[]::new);
                            Messenger.process(responseParts);
                            break;
                        default:
                            System.out.println("Message type not recognized " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Error in listening and processing server's response. Server may be down");
                    debug(e);
                    break;
                } 
            }
        }  
    }, "Server Listener");

    private void run() {
        try {
            boolean flag = true;
            if (flag) {
                Authenticator.register("test2", "1");
                Authenticator.login("test2", "1");
                // System.out.println("Logged in: " + user);
                // FileProcessor.upload("E:/LQDOJ/translate-cp-handbook/book.pdf", "");
                // Messenger.sendNormalChat("eyyo sing it bro", 4530281956215267098L); //publicID
                // Messenger.sendNormalChat("eyyo sing it bro", user.getPublicID()); //publicID
                FileProcessor.download("book.pdf");
            } else {
                Authenticator.register("dsk", "vinataba");
                Authenticator.login("dsk", "vinataba");
                FileProcessor.createDirectory("in3", "in1\\in2");
                FileProcessor.upload("E:\\Computer Science\\Sandbox\\independent_test.java", "in1\\in2\\in3");
                FileProcessor.upload("E:\\Computer Science\\z News\\Danh sach \u0111i\u1EC7n SV T5.2023.xls", "in1");
                // FileProcessor.download("in1\\in2\\in3\\independent_test.java");
            }
            // Authenticator.logout();
            // closeAll();
            // System.out.println("Done, closed all");
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
    
    void debug(Exception e) {
        if (debugger != null) {
            debugger.println(e.getMessage());
            e.printStackTrace(debugger);
        } 
        e.printStackTrace();
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
