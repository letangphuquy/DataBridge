package Server;
import Rules.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket clientSocket;
    ServerThread(Socket socket) {
        clientSocket = socket;
        System.out.println("New client connected");
    }
    private void sendResponse(BufferedWriter out, ServerCode response) throws IOException {
        out.write(response.toString());
        out.newLine();
        out.flush();
    }
    @Override
    public void run() {
        try (
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        ) {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("Received: " + msg);
                String[] parts = msg.split(" ");
                int type = Integer.parseInt(parts[0]);
                int command = Integer.parseInt(parts[1]);
                switch (ClientCode.Type.values()[type]) {
                    case AUTH:
                        sendResponse(out, Authenticator.process(command, parts));
                        break;
                    case USER:
                        break;
                    case FILE:
                        break;
                    case CHAT:
                        break;
                    default:
                        out.write("Invalid request");
                        out.newLine();
                        out.flush();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
