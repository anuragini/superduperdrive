package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {
    private int fileId;
    private int userId;

    private static String fileName;
    private String fileSize;
    private static String contentType;

    private byte [] fileData;

    //main constructor
    public File(int fileId, String fileName, String fileSize, String contentType, int userId, byte [] fileData){
        this.fileId = fileId;
        File.fileName = fileName;
        this.fileSize = fileSize;
        File.contentType = contentType;
        this.userId = userId;
        this.fileData = fileData;
    }

    //default constructor
    public File(){}

    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;

    }
    public String getContentType(){
        return contentType;
    }
    public void setContentType(String contentType){
        this.contentType = contentType;

    }
    public String getFileSize(){
        return fileSize;
    }
    public byte [] getFileData() {
        return fileData;
    }
    public void setFileData(byte [] fileData){
        this.fileData = fileData;

    }

    public void setFileSize(String s) {
        this.fileSize = s;
    }
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getFileId(){
        return fileId;
    }
    public void setFileId(int fileId){
        this.fileId = fileId;

    }
}
