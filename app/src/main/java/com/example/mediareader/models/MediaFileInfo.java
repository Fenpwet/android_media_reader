package com.example.mediareader.models;

/**
 * Created by David on 14/01/2017.
 * Model containing media file info
 */
public class MediaFileInfo {

    private String filePath;
    private String fileName;
    private String fileType;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
