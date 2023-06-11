package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Rules.ClientCode;
import Rules.ServerCode;

public class FileProcessor {
    private FileProcessor() {}
    private static final String FILESYSTEM_ROOT = "E:\\Computer Science\\Thesis\\Yr1\\FileSystem\\";
    private static byte[] buffer = new byte[1024 * 8];

    /*
     * Receive procedure:
     * 1. Receive file info
     */
    public static void receiveFile(ServerThread server, String[] params) throws IOException {
        //Files uploaded by users are stored in a remote file system
        System.out.println("Hello ");
        for (String param : params) {
            System.out.println(param);
        }
        String filename = params[0];
        long fileSize = Long.parseLong(params[1]);
        String fileDest = FILESYSTEM_ROOT + server.user.getUsername()+ "\\";
        new File(fileDest).mkdirs();
        if (!".".equals(params[2])) fileDest += (params[2] + "\\");
        new File(fileDest).mkdirs();
        System.out.println("File destination: " + fileDest);
        File file = new File(fileDest + filename);
        if (!file.exists()) file.createNewFile();
        server.send(ServerCode.ACCEPT.toString());

        try (FileOutputStream fileWriter = new FileOutputStream(file)) {
            long bytesReceived = 0;
            while (bytesReceived < fileSize) {
                byte[] receivedData = server.readBytes();
                fileWriter.write(receivedData);
                bytesReceived += receivedData.length;
                System.out.println(bytesReceived + "/" + fileSize);
            }
        }
    }
    
    public static void process(ServerThread serverThread, ClientCode.Command command, String[] params) {
        try {
            switch (command) {
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
