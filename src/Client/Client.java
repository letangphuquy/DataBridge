package Client;
import Rules.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (
        Socket socket = new Socket(HostAddress.HOSTNAME, HostAddress.PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
            System.out.println("Connected to server");
            String msg = "0 sa 1";
            out.write(msg);
            out.newLine();
            out.flush();
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
