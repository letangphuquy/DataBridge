package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import Model.DFile;
import Model.RandomGenerator;
import Model.TypesConverter;
import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;
import Server.Database.Data;
import Server.Database.DatabaseUpdater;

public class FileProcessor {
    private FileProcessor() {}
    private static final String FILESYSTEM_ROOT = "E:\\Computer Science\\Thesis\\Yr1\\FileSystem\\";
    private static final String D = (String) Constants.DELIMITER;
    private static byte[] buffer = new byte[Constants.BUFFER_SIZE];

    static HashMap<Integer, FileOutputStream> filesOnReceiving = new HashMap<>();
    static HashMap<Integer, Long> bytesRemaining = new HashMap<>();

    static String getUserRoot(ServerThread server) {
        String root = FILESYSTEM_ROOT + server.user.getUserID() + "\\";
        new File(root).mkdirs();
        return root;
    }
    private static void createDirectory(ServerThread server, String name, String path) {
        System.out.println("Creating directory " + name + " in " + path);
        File file = new File(path);
        if (!file.exists()) 
            createDirectory(server, file.getName(), file.getParent());
        file = new File(path += "\\" + name);
        if (!file.mkdir()) return;
        
        String dirID;
        do {
            dirID = RandomGenerator.randomString(Constants.ID_LENGTH);
        } while (Data.files.containsKey(dirID));
        DFile dFile = new DFile(dirID, server.user.getUserID(), Data.pathToID.get(file.getParent()), name, "Directory", true, true, new Timestamp(new Date().getTime()));
        DatabaseUpdater.addFile(path, dFile);
    }
    /*
     * Note: Files uploaded by users are stored in a remote file system
     * Receive procedure:
     * 1. Receive file info
     * 2. Create file and directories
     * 3. Receive file data (in chunks of 8KB)
     * 4. Register file in database
     */
    private static void receiveFile(ServerThread server, String[] params) throws IOException {
        System.out.println("File Hello " + params[0]);
        String filename = params[0];
        long fileSize = Long.parseLong(params[1]);
        int requestID = Integer.parseInt(params[params.length - 1]);
        String fileDest = getUserRoot(server);
        if (params.length >= 3 && !"".equals(params[2])) fileDest += (params[2] + "\\");
        System.out.println("File destination: " + fileDest);

        assert new File(fileDest).exists();
        File file = new File(fileDest + filename);
        if (file.exists()) 
        {
            System.out.println("Overwriting file " + file.getPath());
            // TODO (minor) rewrite added time
        }
        else {
            file.createNewFile();
            System.out.println("File " + filename + " acknowledged.");
            String fileID;
            do {
                fileID = RandomGenerator.randomString(Constants.ID_LENGTH);
            } while (Data.files.containsKey(fileID));
    
            DFile dFile = new DFile(fileID, server.user.getUserID(), Data.pathToID.get(file.getParent()), filename, "Some notes about the file content", file.isDirectory(), true, new Timestamp(new Date().getTime()));
            DatabaseUpdater.addFile(file.getPath(), dFile);
        }
        filesOnReceiving.put(requestID, new FileOutputStream(file));
        bytesRemaining.put(requestID, fileSize);
        server.send(ServerCode.ACCEPT.toString() + " " + requestID);
        System.out.println("File " + filename + " accepted. Notified client.");
    }
    
    private static void receiveFileData(int requestID, String data) throws IOException {
        var fileWriter = filesOnReceiving.get(requestID);
        long remainingCount = bytesRemaining.get(requestID);
        byte[] buffer = TypesConverter.stringToBytes(data);
        long bytesReceived = buffer.length;
        fileWriter.write(buffer);
        System.out.println("Written " + bytesReceived + " bytes to destination file");
        if ((remainingCount -= bytesReceived) <= 0) {
            fileWriter.close();
            filesOnReceiving.remove(requestID);
            bytesRemaining.remove(requestID);
            System.out.println("File received successfully");
        }
        else bytesRemaining.put(requestID, remainingCount);
        // TODO: Consider implementing Producer-Consumer pattern
    }
    /*
     * Send procedure:
     * 1. Verify file existence
     * 2. Send file info
     * 3. Send file data (in chunks of 8KB)
     */
    private static void sendFile(ServerThread server, String path) throws IOException {
        path = getUserRoot(server) + path;
        System.out.println("Getting file " + path);
        DFile dFile = Data.files.get(Data.pathToID.get(path));
        if (dFile == null) {
            server.send(ServerCode.REJECT + D + "filepath not found");
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            server.send(ServerCode.ERROR + D + "file not found in server");
            return;
        }

        server.send(ServerCode.ACCEPT.toString());
        long fileSize = file.length();
        server.send(file.getName() + D + fileSize);

        try (FileInputStream fileReader = new FileInputStream(file)) {
            int bytesRead = 0;
            while ((bytesRead = fileReader.read(buffer)) != -1)  {
                // server.sendBytes(buffer, bytesRead);
            }
        }
    }
    
    public static void process(ServerThread serverThread, ClientCode.Command command, String[] params) {
        try {
            switch (command) {
                case CREATE:
                    createDirectory(serverThread, params[0], getUserRoot(serverThread) + params[1]);
                    break;
                case UPLOAD:
                    if (params.length == 2) receiveFileData(Integer.parseInt(params[0]), params[1]);
                    else receiveFile(serverThread, params);
                    break;
                case DOWNLOAD:
                    sendFile(serverThread, params[0]);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
    }
}
