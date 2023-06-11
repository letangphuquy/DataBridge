package Model;

import java.sql.Timestamp;

/*
 * DFile: to distinguish from java.io.File
 * Store file's metadata
 */
public class DFile {
    String fileID, uploaderID, parentID, fileName;
    String notes;
    boolean isFolder;
    boolean isPrivate;
    Timestamp uploadTime;

    public DFile(String fileID, String uploaderID, String parentID, String fileName, String notes, boolean isFolder,
            boolean isPrivate, Timestamp uploadTime) {
        this.fileID = fileID;
        this.uploaderID = uploaderID;
        this.parentID = parentID;
        this.fileName = fileName;
        this.notes = notes;
        this.isFolder = isFolder;
        this.isPrivate = isPrivate;
        this.uploadTime = uploadTime;
    }
}
