package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import Model.TypesConverter;
import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;

public class FileProcessor {
    private FileProcessor() {}
    private static byte[] buffer = new byte[Constants.BUFFER_SIZE];
    private static String D = (String) Constants.DELIMITER;
    private static Client client = Client.instance;

    public static void process(ClientCode.Command command, String[] params) throws IOException {
        switch (command) {
            case UPLOAD:
                uploadData(Integer.parseInt(params[0]));
                break;
            case DOWNLOAD:
                break;
            default:
                break;
        }
    }

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
        //Initiates upload
        System.out.println("Init uploading " + srcPath + "...");
        File file = new File(srcPath);
        String msg = ClientCode.Type.FILE + D + ClientCode.Command.UPLOAD; 
        client.send(msg + D + file.getName() + D + file.length() + D + destPath + D + client.requests.size());
        client.requests.add(msg + D + srcPath);
    }
    public static void uploadData(int requestID) throws IOException {
        final String info = client.requests.get(requestID);
        String[] infoParts = info.split(D);
        assert ClientCode.Type.FILE.toString().equals(infoParts[0]);
        assert ClientCode.Command.UPLOAD.toString().equals(infoParts[1]);
        String srcPath = infoParts[2];
        File file = new File(srcPath);
        System.out.println("Uploading " + file.getName() + "...");
        client.send("FUCK WHY BUG");
        if (false)
        try (FileInputStream fileReader = new FileInputStream(file)) {
            int bytesRead = 0;
            
            while ((bytesRead = fileReader.read(buffer)) != -1) {
                String msg = info + D + requestID + D + TypesConverter.bytesToString(Arrays.copyOf(buffer, bytesRead));
                // System.out.println("Sending: " + msg);
                client.send(msg);
            }
        } 
        Thread uploadFile = new Thread(new Runnable() {
            //Non-blocking upload
            @Override
            public void run() {
                try (FileInputStream fileReader = new FileInputStream(file)) {
                    int bytesRead = 0;
                    
                    while ((bytesRead = fileReader.read(buffer)) != -1) {
                        String msg = info + D + requestID + D + TypesConverter.bytesToString(Arrays.copyOf(buffer, bytesRead));
                        // System.out.println("Sending: " + msg);
                        client.send(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Upload failed: " + e.getMessage());
                    client.debug(e);
                }
            }
        });
        // uploadFile.start();
        client.independentThreads.add(uploadFile);
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
                byte[] buffer = null;// = client.readBytes();
                fileWriter.write(buffer);
                bytesReceived += buffer.length;
            }
        }
        System.out.println("File " + filename + " downloaded successfully!");
    }
}