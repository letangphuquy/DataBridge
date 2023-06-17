package Server;
import Rules.*;

import java.io.IOException;
import java.net.Socket;
import java.util.stream.Stream;

import Model.E2ESocket;

public class ServerThread extends E2ESocket implements Runnable {
    
    ServerThread(Socket socket) {
        super(socket, true);
    }

    @Override
    public void run() {
        try {
            while (isConnected()) {
                String msg = in.readLine();
                if (msg == null) break;
                System.out.println("Received: " + msg.substring(0, Math.min(100, msg.length())));
                msg = secretMessenger.decryptStr(msg);
                String displayMsg = msg;
                if (displayMsg.length() > 100) displayMsg = displayMsg.substring(0, 100) + "...";
                System.out.println("Decrypted: " + displayMsg);
                String[] parts = msg.split((String) Constants.DELIMITER);
                String type = parts[0];
                ClientCode.Command command = ClientCode.Command.valueOf(parts[1]);
                parts = Stream.of(parts).skip(2).toArray(String[]::new);
                switch (ClientCode.Type.valueOf(type)) {
                    case AUTH:
                        user = Authenticator.process(this, command, parts);
                        System.out.println("Query result: " + user);
                        break;
                    case USER:
                        break;
                    case FILE:
                        FileProcessor.process(this, command, parts);
                        break;
                    case CHAT:
                        break;
                    default:
                        sendPlain("Invalid request");
                }
            }
        } catch (IOException e) {
            System.out.println("Error in communicating with client. Maybe client disconnected?");
            e.printStackTrace();
        }
    }
}
