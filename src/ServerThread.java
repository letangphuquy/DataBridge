package src;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    public enum CODE {
        LOGIN, REGISTER, UPLOAD, SEND, UPDATE, LOGOUT
    }
    private Socket clientSocket;
    ServerThread(Socket socket) {
        clientSocket = socket;
        System.out.println("New client connected");
    }
    @Override
    public void run() {
        System.out.println("Connected to client");
        try (
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        ) {
            String msg;
            while ((msg = in.readLine()) != null) {
                //not standardized convention: type + " " + data
                System.out.println("Received: " + msg);
                String[] parts = msg.split(" ");
                int type = Integer.parseInt(msg.substring(0, 1));
                System.out.println(CODE.values()[type]);
                switch (CODE.values()[type]) {
                    case LOGIN:
                        System.out.println("Received: " + parts[1] + " " + parts[2]);
                        if ("sa".equals(parts[1]) && "1".equals(parts[2]))
                            out.write("Login successfully");
                        else
                            out.write("Wrong credentials");
                        out.newLine();
                        out.flush();
                        break;
                    case REGISTER:
                        break;
                    case UPLOAD:
                        break;
                    case SEND:
                        break;
                    case UPDATE:
                        break;
                    case LOGOUT:
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
