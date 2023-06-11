package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;

import Rules.ClientCode;
import Rules.ServerCode;

public class E2ESocket {
    protected SecretMessenger secretMessenger = null;
    private Socket socket;
    protected BufferedReader in;
    protected BufferedWriter out;
    public User user = null;
    
    /*
     * Constructor's requirements:
     * 1. Establish connection (via Socket or ServerSocket)
     * 2. Set up I/O streams (in, out)
     * 3. Do key exchange
     */

    protected E2ESocket(Socket socket, boolean sendFirst) {
        assert socket != null;
        this.socket = socket;
        System.out.println("Connected to " + socket.getInetAddress());
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            doKeyExchange(sendFirst);
        } catch (IOException e) {
            System.out.println("Error in setting up I/O streams for communication, or in key exchanging");
        }
    }
    
    private void doKeyExchange(boolean sendFirst) throws IOException {
        secretMessenger = new SecretMessenger();
        String peerMessage;
        if (sendFirst) {
            sendPlain(ClientCode.Type.KEY + " " + TypesConverter.bytesToString(secretMessenger.getPublicKey()));
            peerMessage = readPlain();
        } else {
            peerMessage = readPlain();
            sendPlain(ClientCode.Type.KEY + " " + TypesConverter.bytesToString(secretMessenger.getPublicKey()));
        }
        String[] parts = peerMessage.split(" ");
        assert parts[0].equals(ServerCode.KEY.toString());
        byte[] peerPublicKey = TypesConverter.stringToBytes(parts[1]);

        try {
            secretMessenger.generateSharedSecret(peerPublicKey);
        } catch (Exception e) {
            System.out.println("Error in generating shared secret");
        }
    }
    
    protected void sendPlain(String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }

    public void send(String msg) throws IOException {
        sendPlain(secretMessenger.encryptStr(msg));
    }

    public void sendBytes(byte[] msg, int length) throws IOException {
        if (length < msg.length) 
            msg = Arrays.copyOf(msg, length);
        sendPlain(secretMessenger.encryptBytes(msg));
    }

    protected String readPlain() throws IOException {
        return in.readLine();
    }

    public String read() throws IOException {
        return secretMessenger.decryptStr(readPlain());
    }

    public byte[] readBytes() throws IOException {
        return secretMessenger.decryptBytes(in.readLine());
    }
    
    protected void closeAll() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
