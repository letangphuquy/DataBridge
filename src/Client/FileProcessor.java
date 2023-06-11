package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;

public class FileProcessor {
    private FileProcessor() {}
    private static byte[] buffer = new byte[Constants.BUFFER_SIZE];

    public static void createDirectory(String dirName, String path) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.FILE + " " + ClientCode.Command.CREATE + " " + dirName + " " + path);
    }
    /*
     * Upload procedure:
     * 1. Send request (filename)
     * 2. Send file data (in chunks)
     */
    public static void upload(String srcPath, String destPath) throws IOException {
        File file = new File(srcPath);
        Client client = Client.instance;
        client.send(ClientCode.Type.FILE + " " + ClientCode.Command.UPLOAD + " " + file.getName() + " " + file.length() + " " + destPath);

        String response = client.read();
        String[] parts = response.split(" ");
        
        if (!ServerCode.ACCEPT.toString().equals(parts[0])) return;
        System.out.println("Uploading " + file.getName() + "...");
        try (FileInputStream fileReader = new FileInputStream(file)) {
            int bytesRead = 0;
            while ((bytesRead = fileReader.read(buffer)) != -1) {
                client.sendBytes(buffer, bytesRead);
            }
        }
    }

    public static byte[] download(String path) {
        //TODO
        return null;
    }
}
/*
Some help from: 
+ stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets
+ https://heptadecane.medium.com/file-transfer-via-java-sockets-e8d4f30703a5
import java.io.*;
import java.net.Socket;

public class Client {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5000)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            sendFile("path/to/file1.pdf");
            sendFile("path/to/file2.pdf");
            
            dataInputStream.close();
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        // send file size
        dataOutputStream.writeLong(file.length());  
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }
}

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println("listening to port:5000");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected.");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            receiveFile("NewFile1.pdf");
            receiveFile("NewFile2.pdf");

            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        
        long size = dataInputStream.readLong();     // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }
}
 */