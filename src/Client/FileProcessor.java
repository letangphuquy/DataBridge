package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;

public class FileProcessor {
    private FileProcessor() {}
    private static byte[] buffer = new byte[Constants.BUFFER_SIZE];
    private static String D = (String) Constants.DELIMITER;

    public static void createDirectory(String dirName, String path) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.FILE + D + ClientCode.Command.CREATE + D + dirName + D + path);
    }
    /*
     * Upload procedure:
     * 1. Send request (filename)
     * 2. Send file data (in chunks)
     */
    public static void upload(String srcPath, String destPath) throws IOException {
        File file = new File(srcPath);
        Client client = Client.instance;
        client.send(ClientCode.Type.FILE + D + ClientCode.Command.UPLOAD + D + file.getName() + D + file.length() + D + destPath);

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

    /*
     * Download procedure:
     * 1. Send request (filename)
     * 2a. Get file metadata (size, name)
     * 2. Receive file data (in chunks)
     */
    public static void download(String path) throws IOException {
        String DOWNLOAD_URL = Client.URL + "\\downloads\\";
        Client client = Client.instance;
        client.send(ClientCode.Type.FILE + D + ClientCode.Command.DOWNLOAD + D + path);

        String response = client.read();
        System.out.println(response);
        String[] parts = response.split(D);
        if (!ServerCode.ACCEPT.toString().equals(parts[0])) {
            System.out.println("Download failed: " + response);
            return ;
        }

        response = client.read();
        System.out.println(response);
        parts = response.split(D);
        String filename = parts[0];
        long fileSize = Long.parseLong(parts[1]);
        
        new File(DOWNLOAD_URL).mkdirs();
        File file = new File(DOWNLOAD_URL + filename);
        if (!file.exists()) file.createNewFile();

        try (FileOutputStream fileWriter = new FileOutputStream(file)) {
            long bytesReceived = 0;
            while (bytesReceived < fileSize) {
                byte[] buffer = client.readBytes();
                fileWriter.write(buffer);
                bytesReceived += buffer.length;
            }
        }
        System.out.println("File " + filename + " downloaded successfully!");
    }
}