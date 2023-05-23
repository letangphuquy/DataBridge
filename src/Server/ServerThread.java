package Server;
import Rules.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import Model.SecretMessenger;
import Model.TypesConverter;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private SecretMessenger secretMessenger = null;
    
    ServerThread(Socket socket) {
        System.out.println("New client connected");
        clientSocket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            doKeyExchange();
        } catch (IOException e) {
            System.out.println("Error in setting up I/O streams to communicate with client, or in key exchanging");
            e.printStackTrace();
        }
    }
    private void sendPlainResponse(String response) throws IOException {
        out.write(response);
        out.newLine();
        out.flush();
    }
    private void sendResponse(String response) throws IOException {
        sendPlainResponse(secretMessenger.encrypt(response));
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("Received: " + msg);
                msg = secretMessenger.decrypt(msg);
                System.out.println("Decrypted: " + msg);
                String[] parts = msg.split(" ");
                ClientCode.Command command = ClientCode.Command.valueOf(parts[1]);
                switch (ClientCode.Type.valueOf(parts[0])) {
                    case AUTH:
                        sendResponse(Authenticator.process(command, parts).toString());
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
            System.out.println("Error in communicating with client. Maybe client disconnected?");
            e.printStackTrace();
        }
    }

    private void doKeyExchange() throws IOException {
        secretMessenger = new SecretMessenger();
        sendPlainResponse(ServerCode.KEY + " " + TypesConverter.bytesToString(secretMessenger.getPublicKey()));
        String msg = in.readLine();
        byte[] clientPublicKey = TypesConverter.stringToBytes(msg.split(" ")[1]);
        try {
            secretMessenger.generateSharedSecret(clientPublicKey);
        } catch (Exception e) {
            System.out.println("Error in generating shared secret");
            e.printStackTrace();
        }
    }
}
