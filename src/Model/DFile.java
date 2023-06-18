package Model;

import java.sql.Timestamp;

/*
 * DFile: to distinguish from java.io.File
 * Store file's metadata
 */
public class DFile {
    String fileID, parentID, fileName;
    long uploaderID;
    String notes;
    boolean isFolder;
    boolean isPrivate;
    Timestamp uploadTime;

    public DFile(String fileID, long uploaderID, String parentID, String fileName, String notes, boolean isFolder,
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

    public DFile(String[] args) {
        this.fileID = args[0];
        this.uploaderID = Long.parseLong(args[1]);
        this.parentID = args[2];
        this.fileName = args[3];
        this.notes = args[4];
        this.isFolder = Boolean.parseBoolean(args[5]);
        this.isPrivate = Boolean.parseBoolean(args[6]);
        this.uploadTime = Timestamp.valueOf(args[7]);
    }

    public String getFileID() {
        return fileID;
    }

    public long getUploaderID() {
        return uploaderID;
    }

    public String getParentID() {
        return parentID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public Object[] toObjectArray() {
        return new Object[] { fileID, uploaderID, parentID, fileName, notes, isFolder, isPrivate, uploadTime };
    }
}
