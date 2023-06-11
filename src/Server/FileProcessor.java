package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import Model.DFile;
import Model.RandomGenerator;
import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;
import Server.Database.Data;
import Server.Database.DatabaseUpdater;

public class FileProcessor {
    private FileProcessor() {}
    private static final String FILESYSTEM_ROOT = "E:\\Computer Science\\Thesis\\Yr1\\FileSystem\\";
    // private static byte[] buffer = new byte[1024 * 8];

    private static String getUserRoot(ServerThread server) {
        String root = FILESYSTEM_ROOT + server.user.getUserID() + "\\";
        new File(root).mkdirs();
        return root;
    }
    public static void createDirectory(ServerThread server, String name, String path) {
        System.out.println("Creating directory " + name + " in " + path);
        File file = new File(path);
        if (!file.exists()) 
            createDirectory(server, file.getName(), file.getParent());
        file = new File(path += "\\" + name);
        assert !file.exists();
        file.mkdir();
        
        String dirID;
        do {
            dirID = RandomGenerator.randomString(Constants.ID_LENGTH);
        } while (Data.files.containsKey(dirID));
        DFile dFile = new DFile(dirID, server.user.getUserID(), Data.pathToID.get(file.getParent()), name, "Directory", true, true, new Timestamp(new Date().getTime()));
        DatabaseUpdater.addFile(path, dFile);
    }
    /*
     * Receive procedure:
     * 1. Receive file info
     * 2. Create file and directories
     * 3. Receive file data (in chunks of 8KB)
     * 4. Register file in database
     */
    public static void receiveFile(ServerThread server, String[] params) throws IOException {
        //Files uploaded by users are stored in a remote file system
        System.out.println("Hello ");
        for (String param : params) {
            System.out.println(param);
        }
        String filename = params[0];
        long fileSize = Long.parseLong(params[1]);
        String fileDest = getUserRoot(server);
        if (!".".equals(params[2])) fileDest += (params[2] + "\\");
        assert new File(fileDest).exists();
        new File(fileDest).mkdirs();
        System.out.println("File destination: " + fileDest);
        File file = new File(fileDest + filename);
        if (!file.exists()) file.createNewFile();
        server.send(ServerCode.ACCEPT.toString());

        try (FileOutputStream fileWriter = new FileOutputStream(file)) {
            long bytesReceived = 0;
            while (bytesReceived < fileSize) {
                byte[] buffer = server.readBytes();
                fileWriter.write(buffer);
                bytesReceived += buffer.length;
            }
        }

        System.out.println("File " + filename + " received.");
        String fileID;
        do {
            fileID = RandomGenerator.randomString(Constants.ID_LENGTH);
        } while (Data.files.containsKey(fileID));

        DFile dFile = new DFile(fileID, server.user.getUserID(), Data.pathToID.get(file.getParent()), filename, "Some notes about the file content", file.isDirectory(), true, new Timestamp(new Date().getTime()));
        DatabaseUpdater.addFile(fileDest, dFile);
    }
    
    public static void process(ServerThread serverThread, ClientCode.Command command, String[] params) {
        try {
            switch (command) {
                case CREATE:
                    createDirectory(serverThread, params[0], getUserRoot(serverThread) + params[1]);
                    break;
                case UPLOAD:
                    receiveFile(serverThread, params);
                    break;
                case DOWNLOAD:
                    //TODO
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
    }
}
