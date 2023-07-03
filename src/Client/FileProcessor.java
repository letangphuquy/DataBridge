package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import Model.Message;
import Model.TypesConverter;
import Rules.ClientCode;
import Rules.Constants;

public class FileProcessor {
    private FileProcessor() {}
    private static byte[] buffer = new byte[Constants.BUFFER_SIZE];
    private static String D = (String) Constants.DELIMITER;
    private static Client client = Client.instance;

    final static String DOWNLOAD_URL = Client.URL + "downloads\\";
    static HashMap<Integer, FileOutputStream> filesOnReceiving = new HashMap<>();
    static HashMap<Integer, Long> bytesRemaining = new HashMap<>();

    static void process(ClientCode.Command command, int requestID, String[] params) throws IOException {
        switch (command) {
            case UPLOAD:
                uploadData(requestID);
                break;
            case DOWNLOAD:
                try {
                    long fileSize = Long.parseLong(params[0]);
                    downloadSetup(requestID, fileSize);
                } catch (Exception e) {
                    downloadData(requestID, params[0]);
                }
                break;
            default:
                break;
        }
    }

    static void createDirectory(String dirName, String path) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.FILE + D + ClientCode.Command.CREATE + D + dirName + D + path);
    }
    /*
     * Upload procedure:
     * 1. Send request (filename)
     * 2. Send file data (in chunks)
     */
    static void upload(String srcPath, String destPath) throws IOException {
        //Initiates upload
        System.out.println("Init uploading " + srcPath + "...");
        File file = new File(srcPath);
        String msg = ClientCode.Type.FILE + D + ClientCode.Command.UPLOAD; 
        client.send(msg + D + file.getName() + D + file.length() + D + destPath + D + client.requests.size());
        client.requests.add(msg + D + srcPath);
    }
    static void uploadData(int requestID) throws IOException {
        String info = client.requests.get(requestID);
        String[] infoParts = info.split(D);
        assert ClientCode.Type.FILE.toString().equals(infoParts[0]);
        assert ClientCode.Command.UPLOAD.toString().equals(infoParts[1]);
        String srcPath = infoParts[2];
        final String msgPrefix = ClientCode.Type.FILE + D + ClientCode.Command.UPLOAD + D + requestID + D; // can't reuse info because it's contains srcPath, which is not needed here
        File file = new File(srcPath);
        System.out.println("Uploading " + file.getName() + "...");
        Thread uploadFile = new Thread(new Runnable() {
            //Non-blocking upload
            @Override
            public void run() {
                try (FileInputStream fileReader = new FileInputStream(file)) {
                    int bytesRead = 0;
                    
                    while ((bytesRead = fileReader.read(buffer)) != -1) {
                        String msg = msgPrefix + TypesConverter.bytesToString(Arrays.copyOf(buffer, bytesRead));
                        client.send(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Upload failed: " + e.getMessage());
                    client.debug(e);
                }
                System.out.println("File " + file.getName() + " uploaded successfully!");
            }
        }, "Upload File " + file.getName());
        uploadFile.start();
        client.independentThreads.add(uploadFile);
    }

    /*
     * Download procedure:
     * 1. Send request (filename)
     * 2a. Get file metadata (size, name)
     * 2. Receive file data (in chunks)
     */
    static void download(Object filepath) throws IOException {
        //Initiates download
        // the argument can also be a message ID (FileLink)
        String prefix = ClientCode.Type.FILE + D + ClientCode.Command.DOWNLOAD;
        client.send(prefix + D + filepath + D + client.requests.size());
        if (filepath instanceof String)
            client.requests.add(prefix + D + filepath);
        else {
            assert filepath instanceof Integer;
            Message message = Data.sharedFiles.get((int) filepath);
            client.requests.add(prefix + D + message);
        }
    }

    static void downloadSetup(int requestID, long fileSize) throws IOException {
        String filepath = client.requests.get(requestID).split(D)[2];
        filepath = DOWNLOAD_URL + filepath;
        File file = new File(filepath);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            System.out.println("File " + filepath + " already exists! Not downloading again.");
            return ;
        }
        file.createNewFile();
        filesOnReceiving.put(requestID, new FileOutputStream(file));
        bytesRemaining.put(requestID, fileSize);
        client.send(ClientCode.Type.FILE + D + ClientCode.Command.DOWNLOAD + D + requestID);
    }

    static void downloadData(int requestID, String data) throws IOException {
        var fileWriter = filesOnReceiving.get(requestID);
        long remainingCount = bytesRemaining.get(requestID);
        byte[] buffer = TypesConverter.stringToBytes(data);
        long bytesReceived = buffer.length;
        fileWriter.write(buffer);

        if ((remainingCount -= bytesReceived) <= 0) {
            fileWriter.close();
            filesOnReceiving.remove(requestID);
            bytesRemaining.remove(requestID);
            System.out.println("File received successfully");
        }
        else bytesRemaining.put(requestID, remainingCount);
    }
}